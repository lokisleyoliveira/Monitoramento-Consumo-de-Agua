package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsumoMensal implements Serializable {

    private ArrayList<Consumo> list;
    private Double consumoTotal;
    private Double valorEstimado;

    public ConsumoMensal() {
    }

    public ConsumoMensal(ArrayList<Consumo> list, Double consumoTotal, Double valorEstimado) {
        this.list = list;
        this.consumoTotal = consumoTotal;
        this.valorEstimado = valorEstimado;
    }

    public void setAll(ArrayList<Consumo> list, Double consumoTotal, Double valorEstimado) {
        this.list = list;
        this.consumoTotal = consumoTotal;
        this.valorEstimado = valorEstimado;
    }

    public void setConsumoTotal(Double consumoTotal){
        this.consumoTotal = consumoTotal;
    }

    public void setValorEstimado(Double valorEstimado){
        this.valorEstimado = valorEstimado;
    }

    public ArrayList<Consumo> getList() {
        return list;
    }

    public Double getConsumoTotal() {
        return consumoTotal;
    }

    public Double getValorEstimado() {
        return valorEstimado;
    }
}
