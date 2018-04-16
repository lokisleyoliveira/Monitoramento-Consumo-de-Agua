package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import model.Message;
import model.Residencia;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable{

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

    private static Controller controller;
    private DatagramSocket dSocket;
    private DatagramPacket dPacket;
    private InetAddress ip;
    private int port;
    private Timer timer;

    public Controller(){}

    public static Controller getInstance(){
        if (controller == null)
            controller = new Controller();
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        spinVazao.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 15, 2, 0.2));
        paneCon.setDisable(false);
        paneData.setDisable(true);
    }

    public Residencia getData(){
        return new Residencia(
                textID.getText(),
                spinVazao.getValue(),
                checkAgua.isSelected(),
                textBairro.getText(),
                new Date());
    }

    @FXML
    public void iniciaConexao(ActionEvent event) throws SocketException, UnknownHostException {
        dSocket = new DatagramSocket();
        ip = InetAddress.getByName(textIP.getText());
        port = Integer.parseInt(textPort.getText());
        paneCon.setDisable(true);
        paneData.setDisable(false);

        System.out.println("Endereço do servidor: " + ip.toString() + "," + port);
    }

    @FXML
    public void iniciaEnvio(ActionEvent event){
        butEnviar.setVisible(false);
        timer = new Timer();
        timer.schedule(new Transmissao(), 0, 1000);

    }

    private class Transmissao extends TimerTask{
        Residencia info;
        Message message;

        @Override
        public void run(){
            info = getData();
            message = new Message(00, info);

            byte[] dados;
            try {
                dados = message.getBytes();

                dPacket = new DatagramPacket(dados, dados.length, ip, port);

                dSocket.send(dPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
