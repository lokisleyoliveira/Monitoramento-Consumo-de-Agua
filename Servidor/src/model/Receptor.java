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

        try {
            this.obj = new ObjectOutputStream(socket.getOutputStream());
            InputStream inputStream = new ObjectInputStream(socket.getInputStream());
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

        ConsumoMensal c;

        switch (message.getCode()){
            case 00:
                controller.getSensorData(message);
                break;
            case 10:
                if (controller.checkID(message)){
                    try {
                        obj.writeObject(new Message(90, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        obj.writeObject(new Message(99, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 11:
                c = controller.getConsumoAtual(message);
                if (c == null){
                    try {
                        obj.writeObject(new Message(99, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        obj.writeObject(new Message(91, c));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 12:
                c = controller.getConsumoAnterior(message);
                if (c == null) {
                    try {
                        obj.writeObject(new Message(99, null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        obj.writeObject(new Message(91, c));
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
                controller.geraFaturas(message);
                try {
                    obj.writeObject(new Message(90, null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 21:
                break;
            default:
                break;
        }
    }
}
