package model;

import java.util.ArrayList;

public class ConsumoMensal {

    ArrayList<Consumo> list;
    Double consumoTotal;
    Double valorEstimado;

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
