package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Zona {
    String zona;
    ArrayList<Residencia> lastUpdate;
    boolean vazamento;
    boolean escassez;

    public Zona() {
        lastUpdate = new ConcurrentHashMap<>();
        vazamento = false;
        escassez = false;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public boolean isVazamento() {
        return vazamento;
    }

    public void setVazamento(boolean vazamento) {
        this.vazamento = vazamento;
    }

    public boolean isEscassez() {
        return escassez;
    }

    public void setEscassez(boolean escassez) {
        this.escassez = escassez;
    }

    public void addUpdate (Residencia r){
        Iterator i = lastUpdate.iterator();

        Residencia contem = null;

        while (i.hasNext()){
            contem = (Residencia)i.next();
            if (contem.getId() == r.getId()){
                contem.setAll(r.getId(), r.getVazao(), r.isAgua(), r.getZona(), r.getDate());
            }
        }
        if (contem == null){
            lastUpdate.add(r);
        }

        while (i.hasNext()){
            contem = (Residencia)i.next();
            if (!contem.isAgua() && contem.getVazao() == 0){
                escassez = true;
            } else if (contem.getVazao() == 10){
                vazamento = true;
            }
        }
        if(contem == null){
            escassez = false;
            vazamento = false; 
        }

    }
}
