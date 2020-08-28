package com.coopappiltda.clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table socio(codFijo int ,codUbicacion varchar,nombre varchar,direccion varchar, monto varchar,facturas int,corte varchar)");
        db.execSQL("create table historial(id int,codFijo int ,cobro varchar,consumo int, monto double, estado varchar,fechaPago varchar)");
        db.execSQL("create table afiliacion(id_socio int,nombre varchar ,alias varchar)");
        db.execSQL("create table aviso_de_cobro(id_factura int,id_socio int,cobro varchar ,mto_total float,f_emision date)");
        db.execSQL("create table cortexdeuda(id_socio int,id_corte int ,fecha date)");


        db.execSQL("create table genlect(id_genfact int,id_socio int, cod_socio int, lectant int,lectact int"+
                ",consumo int,id_mediest int,media_ant int,cobro varchar"+
                ",media int,nomb_categ varchar,nombre varchar,deuda varchar,id_categ int,id_medidor int,cloaca int,segurovjez int" +
                ",ubicacion varchar,es_descarga int,usr int,usrhora varchar,usrfecha date,direccion varchar,cantidad int"+
                ",fec_lect date,lect_ant int,consumoant int)");
        db.execSQL("create table sociosHistorial(id_genfact int,cod_socio int,id_socio int,cobro varchar,mto_total varchar,m3 int,facpago int,corte int,id_mensaje varchar,es_factura int)");
        db.execSQL("create table recorrido(id_genfact int,id_socio int,latitud double,longitud double)");
        db.execSQL("create table rutasMap (id_genfact int,zona int,ruta int,cobro VARCHAR)");
        db.execSQL("create table mediesta(id_mediest int,nomb_medie varchar)");
        db.execSQL("create table fecha(fecha varchar)");
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("y-M-d");
        String formattedDate = df.format(c);
        db.execSQL("insert into fecha values('"+formattedDate+"')");
        db.execSQL("create table categor_(" +
                "id_categ int," +
                "inicio int," +
                "fin int," +
                "mto_cubo decimal(10,6)," +
                "mto_alca decimal(10,6))");
        db.execSQL("create table categori(" +
                "id_categ int," +
                "nomb_categ varchar," +
                "grupcate int," +
                "moneda int," +
                "preciocm decimal(10,6)," +
                "comoaplimi int," +
                "preciocmal decimal(10,6)," +
                "preciosm decimal(10,6)," +
                "preciosmal decimal(10,6)," +
                "consumomin int," +
                "mto_rec decimal (10,6)," +
                "subcta varchar," +
                "totalcateg int," +
                "usr int," +
                "usrhora varchar," +
                "usrfecha date," +
                "monedaaass int)");
        db.execSQL("create table ipc(" +
                "id_ipc int," +
                "cobro varchar," +
                "fecha date," +
                "indice int," +
                "usr int," +
                "usrhora varchar," +
                "usrfecha date)");
        db.execSQL("create table paragene(" +
                "id_empresa int," +
                "iva decimal(10,4)," +
                "it decimal(10,4)," +
                "ley1886 int," +
                "ley1886cub int," +
                "ley1886por decimal(10,4))");
        db.execSQL("create table servsoc(" +
                "id_genfact int," +
                "id_servsoc int," +
                "tiposervso int," +
                "fechaserv date," +
                "id_zona int," +
                "ruta int," +
                "id_socio int," +
                "id_serv int," +
                "personas int," +
                "mesini varchar," +
                "mesfin varchar," +
                "nota varchar," +
                "es_servsoc int," +
                "moneda int," +
                "mto_serv decimal(10,5)," +
                "id_sobre int," +
                "porcserv float(10,6)," +
                "usr int," +
                "usrhora varchar," +
                "usrfecha date,codigo int)");
        db.execSQL("create table servicio(" +
                "id_serv int," +
                "nomb_serv varchar," +
                "f_servicio date," +
                "moneda int," +
                "activo int," +
                "mto_serv decimal(10,2)," +
                "iva int," +
                "tiposerv int," +
                "porciva decimal(10,2)," +
                "id_sobre int," +
                "porcserv decimal(10,3))");
        db.execSQL("CREATE TABLE ordefact (" +
                "id_ordefac int NOT NULL, " +
                "f_ordefact date NOT NULL, "+
                "id_usr int NOT NULL, "+
                "num_orden int NOT NULL, "+
                "tiponsf int NOT NULL, "+
                "f_limite date NOT NULL, "+
                "llavedosi varchar(200) NOT NULL, "+
                "codfactura varchar NOT NULL, "+
                "fact_ini int NOT NULL, "+
                "fact_fin int NOT NULL, "+
                "es_ordefac int NOT NULL, "+
                "nfactura int NOT NULL, "+
                "lote int NOT NULL, "+
                "folio int NOT NULL, "+
                "tiponota int NOT NULL, "+
                "vercodcon int NOT NULL, "+
                "usr int NOT NULL, "+
                "usrhora varchar NOT NULL, "+
                "usrfecha date NOT NULL, "+
                "sfc varchar NOT NULL, "+
                "actiemp int NOT NULL, "+
                "leyenda int NOT NULL);");

        db.execSQL("CREATE TABLE estado_ruta (" +
                "id_estado int NOT NULL, " +
                "detalle VARCHAR(15) NOT NULL);");
        db.execSQL("CREATE TABLE aviso (" +
                "id int NOT NULL, "+
                "detalle varcha(80) NOT NULL, "+
                "monto decimal(10,4) NOT NULL);");
        db.execSQL("CREATE TABLE c_t_lectura (" +
                "id_control int NOT NULL, " +
                "mes VARCHAR NOT NULL, "+
                "zona int NOT NULL, "+
                "ruta int NOT NULL, "+
                "generado VARCHAR NOT NULL, "+
                "nota VARCHAR NOT NULL, "+
                "f_control VARCHAR NOT NULL, "+
                "g_control VARCHAR NOT NULL, "+
                "id_plomero int NOT NULL, "+
                "es_lectura int NOT NULL, "+
                "es_carga int NOT NULL, "+
                "es_descarga int NOT NULL, "+
                "usr VARCHAR NOT NULL, "+
                "usrhora VARCHAR NOT NULL, "+
                "usrfecha VARCHAR NOT NULL);");
        db.execSQL("CREATE TABLE l_cred (" +
                "id_genfact int NOT NULL, " +
                "id_credito int NOT NULL, " +
                "id_socio int NOT NULL, " +
                "f_credito VARCHAR NOT NULL, "+
                "id_serv int NOT NULL, "+
                "id_sobre int NOT NULL, "+
                "porcserv float(5,3) NOT NULL, "+
                "mto_ant float(10,2) NOT NULL, "+
                "n_cuotas int NOT NULL, "+
                "pagcuotas int NOT NULL, "+
                "mto_pagado float(12,2) NOT NULL, "+
                "mesini VARCHAR NOT NULL, "+
                "mto_cred float(10,2) NOT NULL, "+
                "mto_mes float(10,2) NOT NULL, "+
                "es_credito int NOT NULL, "+
                "moneda int NOT NULL, "+
                "interes float(5,3) NOT NULL, "+
                "saldo float(10,2) NOT NULL, "+
                "nota VARCHAR(150) NOT NULL, "+
                "porciva float(5,3) NOT NULL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor getCargaAfiliados() {
        return getReadableDatabase().rawQuery("select * FROM afiliacion ;", null);
    }
    public Cursor getCarga_aviso_de_cobro() {
        return getReadableDatabase().rawQuery("select * FROM aviso_de_cobro ;", null);
    }
    public Cursor ObtenDatosParagene() {
        //return getReadableDatabase().rawQuery("select * from l_usr ;",null);
        return getReadableDatabase().rawQuery("select * from paragene ;", null);
    }

    public Cursor ObtenDatosOrdefact() {
        return getReadableDatabase().rawQuery("select * from ordefact ;", null);
    }
    public Cursor getCargaCortexDeuda() {
        return getReadableDatabase().rawQuery("select * FROM cortexdeuda ;", null);
    }
    public Cursor ObtenIpc() {
        return getReadableDatabase().rawQuery("select * from ipc ;", null);
    }

    public Cursor getPrecDifCateg(int ncat) {
        return getReadableDatabase().rawQuery("select * from categor_ where id_categ =" + ncat + " order by inicio;", null);
    }

    public Cursor getCargaAvisosCargados(){
        return getReadableDatabase().rawQuery("select * from aviso;",null);
    }
    public Cursor getServSocTipo1(int tipo,int soc){
        return getReadableDatabase().rawQuery("select s.tiposervso,s.id_zona,s.ruta,s.id_socio,s.id_serv,n.nomb_serv,s.mesini,s.mesfin," +
                "s.moneda,s.mto_serv,s.id_sobre,s.porcserv,s.codigo from servsoc s,servicio n" +
                " where s.id_serv=n.id_serv and tiposervso =" + tipo + " and s.id_socio=" + soc +";",null);
    }
    public Cursor getServSocTipo2(int tipo,int zon,int rut){
        return getReadableDatabase().rawQuery("select s.tiposervso,s.id_zona,s.ruta,s.id_socio,s.id_serv,n.nomb_serv,s.mesini,s.mesfin," +
                "s.moneda,s.mto_serv,s.id_sobre,s.porcserv,s.codigo from servsoc s,servicio n" +
                " where s.id_serv=n.id_serv and tiposervso =" + tipo + " and s.id_zona=" + zon + " and s.ruta=" + rut + ";",null);
    }
    public Cursor getServSocTipo3(int tipo,int zon){
        return getReadableDatabase().rawQuery("select s.tiposervso,s.id_zona,s.ruta,s.id_socio,s.id_serv,n.nomb_serv,s.mesini,s.mesfin," +
                "s.moneda,s.mto_serv,s.id_sobre,s.porcserv,s.codigo from servsoc s,servicio n" +
                " where s.id_serv=n.id_serv and tiposervso =" + tipo + " and s.id_zona=" + zon + ";",null);
    }
    public Cursor getServSocTipo4(int tipo,String mes){
        return getReadableDatabase().rawQuery("select s.tiposervso,s.id_zona,s.ruta,s.id_socio,s.id_serv,n.nomb_serv,s.mesini,s.mesfin," +
                "s.moneda,s.mto_serv,s.id_sobre,s.porcserv,s.codigo from servsoc s,servicio n where s.id_serv=n.id_serv and mesfin>="+mes+" and tiposervso =" + tipo + ";",null);
    }
    public void grabaAviso(int av1,String av2,float av3){
        getReadableDatabase().execSQL("INSERT INTO aviso VALUES ("+av1+",'"+av2+"',"+av3+");");
        //getReadableDatabase().execSQL("INSERT INTO aviso VALUES (10,'Prueba 10',1.00);");
    }
    public void borraAviso(){
        getReadableDatabase().execSQL("DELETE FROM aviso;");
    }
    public Cursor getServiciosTodos(){
        return getReadableDatabase().rawQuery("select * from servicio ;",null);
    }
    public Cursor getCreditos(int idsoc){
        return getReadableDatabase().rawQuery("select l.id_credito,l.id_socio,l.id_serv,l.mto_mes,l.saldo,l.moneda,s.nomb_serv FROM l_cred l,servicio s WHERE id_socio =" + idsoc + " AND l.id_serv=s.id_serv ;",null);
    }
    public Cursor getServicioMCM(int serv){
        return getReadableDatabase().rawQuery("select * from servicio where id_serv =" + serv + ";",null);
    }
    public Cursor getPrecBasCateg(int ncat){
        return getReadableDatabase().rawQuery("select * from categori where id_categ =" + ncat + ";",null);
    }
    public Cursor sacar_lectant_PromConsumo(int id_soc){
        return getReadableDatabase().rawQuery("select lectant,media from genlect where id_socio=" + id_soc + " ;",null);
    }

}