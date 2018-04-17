package Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Message {

    private int code;
    private Object object;

    public Message() {
    }

    public Message(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public void setAll (int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(b);
        out.writeObject(this);
        out.flush();
        return b.toByteArray();
    }

    public Message byteToObject(byte[] data){
        return null;
    }
}
