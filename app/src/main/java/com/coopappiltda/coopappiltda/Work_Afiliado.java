package com.coopappiltda.coopappiltda;

public class Work_Afiliado {
    private int id;
    private String nombre;
    private String alias;

    public Work_Afiliado(){

    }

    public Work_Afiliado(int nr, String id_us, String nombr){
        this.id = nr;
        this.nombre=id_us;
        this.alias=nombr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
