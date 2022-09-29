package com.example.Model;

import java.io.Serializable;

public class Message implements Serializable {

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
}
