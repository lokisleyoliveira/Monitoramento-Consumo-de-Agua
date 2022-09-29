package com.example.monitoramento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import com.example.model.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable{

    @FXML
    private Pane paneCon;
    @FXML
    private TextField textIP;
    @FXML
    private TextField textPort;

    @FXML
    private Pane paneID;
    @FXML
    private TextField textID;

    @FXML
    private Pane paneData;
    @FXML
    private TableView<Consumo> tabCons;
    @FXML
    private TableColumn<Consumo, Date> colDate;
    @FXML
    private TableColumn<Consumo, Double> colCons;
    @FXML
    private Label labelCons;
    @FXML
    private Label labelValor;

    private Comunicacao comun;
    private Message message;
    private String ip;
    private int port;
    private String id;
    private ArrayList<Consumo> listConsumo;

    private static HelloController helloController;

    public HelloController(){

    }

    public static HelloController getInstance(){
        if (helloController == null){
            helloController = new HelloController();
        }
        return helloController;
    }

    @FXML
    //Incializa o socket de conexão com o servidor
    public void iniciaConexao(ActionEvent event) throws IOException {
        ip = textIP.getText();
        port = Integer.parseInt(textPort.getText());

        comun = new Comunicacao(ip, port);

        paneCon.setDisable(true);
        paneID.setDisable(false);
    }

    @FXML
    //Define de qual sensor o usuário deseja avaliar o consumo
    public void setID(ActionEvent event) throws IOException, ClassNotFoundException {
        id = textID.getText();

        message = new Message(10, id);

        comun.getThread().sendMessage(message);

        message = (Message)comun.getThread().receiveMessage();

        switch (message.getCode()){
            case 90:
                JOptionPane.showMessageDialog(null, "Dados solicitados com sucesso");
                paneID.setDisable(true);
                paneData.setDisable(false);
                break;
            case 99:
                JOptionPane.showMessageDialog(null, "O ID informado não existe, ou nao consta no registro");
                textID.setText("");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Erro ao realizar comunicação com o servidor");
                break;
        }
    }

    @FXML
    // Solicita ao servidor os dados de consumo do mês corrente
    public void consumoAtual(ActionEvent event) throws IOException, ClassNotFoundException {
        message = new Message(11, id);

        comun.getThread().sendMessage(message);

        System.out.println("consumo atual, solicitado ao servidor");

        message = (Message)comun.getThread().receiveMessage();

        System.out.println("Consumo recebido");

        switch (message.getCode()){
            case 91:
                ConsumoMensal cMensal = (ConsumoMensal)message.getObject();
                listConsumo = cMensal.getList();
                System.out.println(listConsumo.size());
                labelCons.setText("Consumo Total: " + cMensal.getConsumoTotal());
                labelValor.setText("Valor estimado: R$ " + cMensal.getValorEstimado());

                tabCons.setItems(updateTable());
            case 99:
                JOptionPane.showMessageDialog(null, "Não há consumo atual registrado");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Erro ao realizar comunicação com o servidor.");
                break;

        }
    }

    @FXML
    //Solicita ao servidor os dados de consumo do mes anterior
    public void consumoPassado(ActionEvent event) throws IOException, ClassNotFoundException {
        message = new Message(12, id);

        comun.getThread().sendMessage(message);

        System.out.println("consumo anterior, solicitado ao servidor");

        message = (Message)comun.getThread().receiveMessage();

        System.out.println("Consumo recebido");
        switch (message.getCode()){
            case 91:
                ConsumoMensal consumoMensal = (ConsumoMensal)message.getObject();
                listConsumo = consumoMensal.getList();
                System.out.println(listConsumo.size());
                labelCons.setText("Consumo Total: " + consumoMensal.getConsumoTotal());
                labelValor.setText("Valor estimado: R$ " + consumoMensal.getValorEstimado());

                tabCons.setItems(updateTable());
            case 99:
                JOptionPane.showMessageDialog(null, "Não há consumo anterior a ser exibido.");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Erro ao realizar comunicação com o servidor.");
                break;
        }
    }

/*    @FXML
    public void metaConsumo(ActionEvent event) throws IOException, ClassNotFoundException {
        Double meta = Double.parseDouble(JOptionPane.showInputDialog("Informe sua meta de consumo:"));
        String email = JOptionPane.showInputDialog("Informe seu e-mail, para ser alertado quando atingir a meta:");

        Fatura fatura = new Fatura(meta, email, id);

        message = new Message(15, fatura);

        comun.getThread().sendMessage(message);

        message = (Message)comun.getThread().receiveMessage();

        switch (message.getCode()){
            case 90:
                JOptionPane.showMessageDialog( null,"Meta estabelecida com sucesso.");
                break;
            case 99:
                JOptionPane.showMessageDialog( null,"Erro ao confirmar a meta de consumo.");
                break;
            default:
            JOptionPane.showMessageDialog(null, "Erro ao realizar comunicação com o servidor.");
        }
    }*/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paneID.setDisable(true);
        paneData.setDisable(true);
        paneCon.setDisable(false);

        colCons.setCellValueFactory(new PropertyValueFactory<>("metrosCubicos"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private ObservableList<Consumo> updateTable() {
        return FXCollections.observableArrayList(listConsumo);
    }

}
