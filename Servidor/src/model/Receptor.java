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

    public Receptor(DatagramPacket pacote){
        this.pacote = pacote;
        try {
            message = (Message)byteToObject(pacote.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Receptor(Socket socket){
        this.socket = socket;

        InputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream obj=  new ObjectOutputStream(bytes);
            obj.writeObject(((ObjectInputStream) inputStream).readObject());
            message = (Message) byteToObject(bytes.toByteArray());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object byteToObject(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream message = new ByteArrayInputStream(data);
        ObjectInput read = new ObjectInputStream(message);
        return read.readObject();
    }

    @Override
    public void run() {
        switch (message.getCode()){
            case 00:
                controller.getSensorData(message);
                break;
            case 01:
                break;
            default:
                break;
        }
    }
}
