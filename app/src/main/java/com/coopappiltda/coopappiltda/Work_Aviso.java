package com.coopappiltda.coopappiltda;

public class Work_Aviso {
    private String cobro;
    private Float monto;
    private String fecha;

    public Work_Aviso(){

    }

    public Work_Aviso(String nr, Float id_us, String nombr){
        this.cobro = nr;
        this.monto=id_us;
        this.fecha=nombr;
    }

    public String getCobro() {
        return cobro;
    }

    public void setCobro(String id) {
        this.cobro = id;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float nombre) {
        this.monto = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String alias) {
        this.fecha = alias;
    }
}
