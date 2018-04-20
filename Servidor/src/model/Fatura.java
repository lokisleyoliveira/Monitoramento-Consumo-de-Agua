package model;

import java.io.Serializable;

public class Fatura implements Serializable {

    private String id;
    private Double consumoTotal;
    private Double valor;

    public Fatura() {
    }

    public Fatura(String id, Double consumoTotal) {
        this.id = id;
        this.consumoTotal = consumoTotal;
    }

    public void setAll( String id, Double consumoTotal){
        this.id = id;
        this.consumoTotal = consumoTotal;
    }

    public void setConsumoTotal(Double consumoTotal){
        this.consumoTotal = consumoTotal;
    }

    public void setValor(Double valor){
        this.valor = valor;
    }

    public Double getConsumoTotal() {
        return consumoTotal;
    }

    public String getId(){
        return id;
    }

    public Double getValor(){
        return valor;
    }
}
