package model;

import Main.Controller;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;

public class Receptor implements Runnable {

    private Controller controller = Controller.getInstance();
    private DatagramPacket pacote;
    private Socket socket;
    private Message message;
    private ObjectOutputStream obj;
    private ObjectInputStream in;

    /**
     * Recebe um pacote UDP
     * @param pacote
     */
    public Receptor(DatagramPacket pacote){
        this.pacote = pacote;
        try {
            message = (Message)byteToObject(pacote.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Recebe informações do socket TCP
     * @param socket
     */
    public Receptor(Socket socket){
        this.socket = socket;

        try {
            this.obj = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            message = (Message)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converte um array de bytes para um objeto
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object byteToObject(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream message = new ByteArrayInputStream(data);
        ObjectInput read = new ObjectInputStream(message);
        return read.readObject();
    }

    /**
     * Metodo executado na inicializacao do objeto Receptor, que capta a mensagem recebida,
     * e a trata apropriadamente
     */
    @Override
    public void run() {

        ConsumoMensal c;

        switch (message.getCode()){
            case 00:
                //Armazena os dados recebidos apartir do sensor
                controller.getSensorData(message);
                try {
                    controller.salvaArquivo();
                } catch (IOException e) {
                    System.out.println("Erro na persistencia dos daddos");
                }
                break;
            case 10:
                // Verifica se um ID solicitado está presente no sistema
                System.out.println(10);
                if (controller.checkID(message)){
                    try {
                        System.out.println("ID Confirmado");
                        obj.writeObject(new Message(90, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.println("ID nao encontrado");
                        obj.writeObject(new Message(99, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 11:
                //Obtém o consumo do mês corrente
                System.out.println(11);
                c = controller.getConsumoAtual(message);
                if (c == null){
                    try {
                        System.out.println("Nao existe consumo");
                        obj.writeObject(new Message(99, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.println("Enviando o consumo atual");
                        obj.writeObject(new Message(91, c));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 12:
                //Obtém o consumo do mês anterior
                System.out.println(12);
                c = controller.getConsumoAnterior(message);
                System.out.println("Consumo antigo solicitado");
                if (c == null) {
                    try {
                        obj.writeObject(new Message(99, null));
                        System.out.println("Consumo antigo nao encontrado");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        obj.writeObject(new Message(91, c));
                        System.out.println("Consumo antigo enviado");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            /*case 15:
                controller.setMeta(message);
                try {
                    obj.writeObject(new Message(90, null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;*/
            case 20:
                //Gera as faturas com os dados de consumo presentes no servidor
                System.out.println(20);
                controller.geraFaturas(message);
                try {
                    obj.writeObject(new Message(90, null));
                    controller.salvaArquivo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 21:
                //Obtém dados de vazamentos e escassez nas zonas cadastradas
                try {
                    obj.writeObject(new Message(91, controller.getZonaStatus(message)));
                    controller.salvaArquivo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
