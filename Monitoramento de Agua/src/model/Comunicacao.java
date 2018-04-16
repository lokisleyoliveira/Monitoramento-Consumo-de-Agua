package model;

import java.io.IOException;
import java.net.Socket;

public class Comunicacao {

    private Socket socket;
    private MyThread thread;

    public Comunicacao (String ip, int porta) throws IOException {
        this.socket = new Socket(ip, porta);
        System.out.println("Server has been started");
        
        thread = new MyThread(socket);
        thread.start();
    }

    public MyThread getThread(){
        return thread;
    }
}
