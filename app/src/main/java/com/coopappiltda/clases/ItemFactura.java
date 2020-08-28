package com.coopappiltda.clases;

public class ItemFactura {

    private String mes;
    private String consumo;
    private String monto;
    private String estado;

    public ItemFactura(String mes, String consumo, String monto, String estado) {
        this.mes = mes;
        this.consumo = consumo;
        this.monto = monto;
        this.estado = estado;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
