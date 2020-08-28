package com.coopappiltda.clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Socio {
    private int codigo;
    private String codUbicacion;
    private String nombre;
    private String deuda;
    private String direccion;
    private int facturasImpagas;
    private String fechacorte;
    private ArrayList<String[]> facturas;

    public Socio(int codigo, String codUbicacion, String nombre, String deuda, String direccion, int facturasImpagas, String fechacorte, ArrayList<String[]> facturas) {
        this.codigo = codigo;
        this.codUbicacion = codUbicacion;
        this.nombre = nombre;
        this.deuda = deuda;
        this.direccion = direccion;
        this.facturasImpagas = facturasImpagas;
        this.fechacorte = fechacorte;
        this.facturas = facturas;
    }
    public Socio(){
        this.facturas = new ArrayList<>();
    }
    public int getCodigo() {
        return codigo;
    }
    public String getCodUbicacion() {
        return codUbicacion;
    }
    public String getNombre() {
        return nombre;
    }
    public ArrayList<String[]> getFacturas() {
        return facturas;
    }
    public String getDeuda() {
        return deuda;
    }
    public int getFacturasImpagas() {
        return facturasImpagas;
    }
    public String getFechacorte() {
        return fechacorte;
    }
    //Almacena en Base de Datos todos los datos obtenidos del socio
    public void save(Context context){
        com.coopappiltda.clases.AdminSQLiteOpenHelper adminSQLiteOpenHelper = new com.coopappiltda.clases.AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase dbs = adminSQLiteOpenHelper.getWritableDatabase();
        dbs.execSQL("delete from socio");
        dbs.execSQL("delete from historial");
        dbs.execSQL("insert into socio values("+this.codigo+",'"+this.codUbicacion+"','"+this.nombre+"','"+this.direccion+"','"+this.deuda+"','"+this.facturasImpagas+"','"+this.fechacorte+"')");
        for (int i = 0 ; i < facturas.size(); i++){
            String[] fila = facturas.get(i);
            dbs.execSQL("insert into historial values ("+(i+1)+","+this.codigo+",'"+ fila[0] +"','"+fila[1]+"','"+fila[2]+"','"+fila[3]+"','"+fila[4]+"')");
        }
        dbs.close();
    }
    @SuppressLint("Recycle")
    //Carga desde la base de datos los datos del socio que ha sido previamente almacenado
    public void obtenerSocio(Context context){
        com.coopappiltda.clases.AdminSQLiteOpenHelper adminSQLiteOpenHelper = new com.coopappiltda.clases.AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase dbs = adminSQLiteOpenHelper.getWritableDatabase();

        String[] campos = new String[]{"codFijo","codUbicacion","nombre","direccion","monto","facturas","corte"};
        @SuppressLint("Recycle") Cursor cursor = dbs.query("socio",campos,null,null,null,null,null);
        if (cursor.moveToFirst()){
            this.codigo = cursor.getInt(0);
            this.codUbicacion = cursor.getString(1);
            this.nombre = cursor.getString(2);
            this.direccion = cursor.getString(3);
            this.deuda = cursor.getString(4);
            this.facturasImpagas = cursor.getInt(5);
            this.fechacorte = cursor.getString(6);

        }
        campos = new String[]{"id","cobro","consumo","monto","estado","fechaPago"};
        String[] seleccion = new String[]{Integer.toString(this.codigo)};
        cursor = dbs.query("historial",campos,"codFijo=?",seleccion,null,null,"id asc");
        if (cursor.moveToFirst()){
            this.facturas.clear();
            do {
                String[] datos = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount() - 1 ; i++){
                    if (i == 4 && cursor.getString(i).equals("1")){
                        datos[i] = "Impaga";
                    }else if(i == 4 && cursor.getString(i).equals("2")){
                        datos[i] = cursor.getString(i+1);
                    }else{
                        datos[i] = cursor.getString(i);
                    }
                }
                this.facturas.add(datos);
            }while (cursor.moveToNext());
        }
    }
}
