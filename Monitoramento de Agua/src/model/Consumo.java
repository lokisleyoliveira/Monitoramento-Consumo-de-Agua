package model;

import java.io.Serializable;
import java.util.Date;

public class Consumo  implements Serializable {
    private Double metrosCubicos;
    private Date date;

    public Consumo() {
    }

    public Consumo(Double metrosCubicos, Date date) {
        this.metrosCubicos = metrosCubicos;
        this.date = date;
    }

    public void setAll(Double metrosCubicos, Date date) {
        this.metrosCubicos = metrosCubicos;
        this.date = date;
    }

    public Double getMetrosCubicos() {
        return metrosCubicos;
    }

    public Date getDate() {
        return date;
    }
}
