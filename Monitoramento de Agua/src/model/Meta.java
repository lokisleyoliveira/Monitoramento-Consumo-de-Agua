package model;

public class Meta {

    private Double meta;
    private String email;
    private String id;

    public Meta() {
    }

    public Meta(Double meta, String email, String id) {
        this.meta = meta;
        this.email = email;
        this.id = id;
    }

    public void setAll(Double meta, String email, String id){
        this.meta = meta;
        this.email = email;
        this.id = id;
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
