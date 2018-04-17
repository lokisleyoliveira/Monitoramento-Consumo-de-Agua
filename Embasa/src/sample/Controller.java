package sample;

import Model.Comunicacao;
import Model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private Pane paneCon;
    @FXML
    private TextField textIP;
    @FXML
    private TextField textPort;

    @FXML
    private Pane paneManage;

    private Comunicacao comun;
    private Message message;
    private String ip;
    private int port;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paneCon.setDisable(false);
        paneManage.setDisable(true);
    }

    @FXML
    public void iniciaConexao(ActionEvent event) throws IOException {
        ip = textIP.getText();
        port = Integer.parseInt(textPort.getText());

        comun = new Comunicacao(ip, port);

        paneCon.setDisable(true);
        paneManage.setDisable(false);
    }

    @FXML
    public void gerarFatura(ActionEvent event) throws IOException, ClassNotFoundException {
        message = new Message(20, null);

        comun.getThread().sendMessage(message);

        message = (Message)comun.getThread().receiveMessage();

        switch (message.getCode()){
            case 90:
                JOptionPane.showMessageDialog(null, "Faturas geradas com sucesso");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Erro durante a comunicação com o servidor");
        }
    }

    @FXML
    public void getZonaStatus(ActionEvent event) throws IOException, ClassNotFoundException {
        message = new Message(21, null);

        comun.getThread().sendMessage(message);

        message = (Message)comun.getThread().receiveMessage();

        switch (message.getCode()){
            case 91:
                break;
            default:
                break;
        }
    }
}
