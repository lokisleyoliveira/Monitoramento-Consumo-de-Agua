package com.example.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyThread extends Thread{
    
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public MyThread (Socket socket) throws IOException {
        this.socket = socket;
        
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }
    
    
    public void sendMessage (Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public Object receiveMessage () throws IOException, ClassNotFoundException {
        return input.readObject();
    }
    
}
