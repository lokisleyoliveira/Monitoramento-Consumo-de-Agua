package model;

import java.util.Date;

public class Residencia {

    private String id;
    private Double vazao;
    private boolean agua;
    private String zona;
    private Date date;

    public Residencia() {
    }

    public Residencia(String id, Double vazao, boolean agua, String zona, Date date) {
        this.id = id;
        this.vazao = vazao;
        this.agua = agua;
        this.zona = zona;
        this.date = date;
    }

    public void setAll(String id, Double vazao, boolean agua, String zona, Date date) {
        this.id = id;
        this.vazao = vazao;
        this.agua = agua;
        this.zona = zona;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Double getVazao() {
        return vazao;
    }

    public boolean isAgua() {
        return agua;
    }

    public String getZona() {
        return zona;
    }

    public Date getDate() {
        return date;
    }
}
