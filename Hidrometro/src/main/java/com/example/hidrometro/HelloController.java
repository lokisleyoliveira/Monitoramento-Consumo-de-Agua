package com.example.hidrometro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import model.Message;
import model.Residencia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable{

    @FXML
    private Pane paneCon;
    @FXML
    private Pane paneData;
    @FXML
    private TextField textIP;
    @FXML
    private TextField textPort;
    @FXML
    private TextField textID;
    @FXML
    private TextField textBairro;
    @FXML
    private CheckBox checkAgua;
    @FXML
    private Button butEnviar;
    @FXML
    private Spinner<Double> spinVazao = new Spinner<Double>();

    private static HelloController controller;
    private DatagramSocket dSocket;
    private DatagramPacket dPacket;
    private InetAddress ip;
    private int port;
    private Timer timer;

    public HelloController(){}

    public static HelloController getInstance(){
        if (controller == null)
            controller = new HelloController();
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        spinVazao.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 15, 2, 0.2));
        paneCon.setDisable(false);
        paneData.setDisable(true);
    }

    //Salva os arquivos inseridos na interface gráfica
    public Residencia getData(){
        return new Residencia(
                textID.getText(),
                spinVazao.getValue(),
                checkAgua.isSelected(),
                textBairro.getText(),
                new Date());
    }


    @FXML
    //Salva os dados do servidor ao qual os dados serão enviados
    public void iniciaConexao(ActionEvent event) throws SocketException, UnknownHostException {
        dSocket = new DatagramSocket();
        ip = InetAddress.getByName(textIP.getText());
        port = Integer.parseInt(textPort.getText());
        paneCon.setDisable(true);
        paneData.setDisable(false);

        System.out.println("Endereço do servidor: " + ip.toString() + "," + port);
    }

    @FXML
    //Inicia o TimerTask enviando ao servidor, os dados presentes na interface gráfica
    public void iniciaEnvio(ActionEvent event){
        butEnviar.setVisible(false);
        timer = new Timer();
        timer.schedule(new Transmissao(), 0, 5000);

    }

    //Classe TikerTask responsável por definir quais ações serão executadas periodicamente
    private class Transmissao extends TimerTask{
        Residencia info;
        Message message;

        @Override
        public void run(){
            info = getData();
            message = new Message(00, info);
            try {
                byte[] dados = serializarMensagens(message);

                dPacket = new DatagramPacket(dados, dados.length, ip, port);

                dSocket.send(dPacket);

                System.out.println("Dados enviados: " + info.getId() + " " + info.getVazao() + " " + info.isAgua() + " " + info.getZona());
            } catch (IOException e) {
                System.out.println("ERRO");
            }
        }
    }

    //Converte um objeto num Array de Bytes
    public byte[] serializarMensagens(Object mensagem) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(b);
        out.writeObject(mensagem);
        out.flush();
        return b.toByteArray();
    }

}
