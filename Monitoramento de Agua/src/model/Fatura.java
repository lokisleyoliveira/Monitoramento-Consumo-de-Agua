package model;

public class Fatura {

    private Double meta;
    private String email;
    private String id;
    private Double consumoTotal;

    public Fatura() {
    }

    public Fatura(Double meta, String email, String id) {
        this.meta = meta;
        this.email = email;
        this.id = id;
    }

    public Fatura(Double meta, String email, String id, Double consumoTotal) {
        this.meta = meta;
        this.email = email;
        this.id = id;
        this.consumoTotal = consumoTotal;
    }

    public void setAll(Double meta, String email, String id, Double consumoTotal){
        this.meta = meta;
        this.email = email;
        this.id = id;
        this.consumoTotal = consumoTotal;
    }

    public void setConsumoTotal(Double consumoTotal){
        this.consumoTotal = consumoTotal;
    }

    public Double getConsumoTotal() {
        return consumoTotal;
    }

    public Double getMeta() {
        return meta;
    }

    public String getEmail() {
        return email;
    }

    public String getId(){
        return id;
    }
}
