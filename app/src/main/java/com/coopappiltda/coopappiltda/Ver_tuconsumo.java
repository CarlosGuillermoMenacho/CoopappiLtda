package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.MySingleton;
import com.coopappiltda.clases.TableDynamic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Ver_tuconsumo extends AppCompatActivity {

    private Spinner listMediest;

    private double latitud=-17.776746,longitud=-63.1294998;
    AlertDialog.Builder builder;
    TextView tvmontofactura;
    TextView tvmensaje;
    EditText med_actual;
    private ImageView ivVolvecal;
    public static AdminSQLiteOpenHelper bd;
    SQLiteDatabase database;
    Cursor datos;
    Cursor datosCateg;
    public static Cursor datosCategDif;
    Cursor datosPara;
    Cursor datosOrde;
    Cursor Servicios;
    Integer u1=0,u2=0,u5=0,u6=0,u7=0,u8=0,u11=0,u12=0,u14=0,u16=0,u17=0,u18=0,u19=0;
    String u3="",u4="",u9="",u10="",u13="",u15="",u20="",cobro="";
    int categ_ant=0,categ_act=0;
    String id;
    String id2;
    Button btncalcular;
    public int pgid_serv=0;
    public int pgcubos=0;
    public float pgporc=0.0f;
    public String vella="";
    public int x23=0;
    public String x24="";
    public int total_reg=0;
    public int cont_reg=0;
    private int lect=0;
    private TableLayout tableLayout;

    float totalFactura=0;  // aqui almacenare el total de la factura
    float totalConsumo=0;
    float totalAlcantarilla=0;
    float totalLey1886=0;
    float totalDsctoConsumo=0;
    float totalDsctoAlcantarilla=0;
    float totalMcm=0.0f,totalCm=0.0f;
    public static float pcmin=0.0f,pcminal=0.0f,psmin=0.0f,psminal=0.0f;
    public static float ipc=0.0f;
    public static int cmin=0,tmoneda=0,consumo=0;
    private String id_genfact,cod_socio;
    private String debug;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tuconsumo);
        bd=new AdminSQLiteOpenHelper(this,"dbReader",null,1);
        database = bd.getWritableDatabase();

        tvmontofactura = (TextView) findViewById(R.id.tvMontoFact);
        //tvmensaje=(TextView) findViewById(R.id.tvMensaje);
        med_actual=(EditText) findViewById(R.id.etTotal);
        btncalcular = (Button) findViewById(R.id.btnCalcular);
        tableLayout = (TableLayout) findViewById(R.id.table);

        final TableDynamic tableDynamic = new TableDynamic(tableLayout, Ver_tuconsumo.this);
        //tableDynamic.cargarAviso(tableLayout, Ver_tuconsumo.this);


         id2 = getIntent().getExtras().getString("CLAVE");  // aqui llega el nro de formulario de la ruta
        bd= new AdminSQLiteOpenHelper(this,"dbReader", null,1);

        categ_ant=0;categ_act=0;
        cont_reg=0;
        builder = new AlertDialog.Builder(Ver_tuconsumo.this);
        ivVolvecal = findViewById(R.id.ivVolvecal);
        ivVolvecal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ver_tuconsumo.this,Tu_consumo.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        btncalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            totalFactura=0;  // aqui almacenare el total de la factura
            totalConsumo=0;
            totalAlcantarilla=0;
            totalLey1886=0;
            totalDsctoConsumo=0;
            totalDsctoAlcantarilla=0;
            totalMcm=0.0f;
            totalCm=0.0f;
            consumo=0;
            borrarAviso();

            EditText lectura;
            lectura = (EditText) findViewById(R.id.etTotal);
            String a=lectura.getText().toString();
            lect=0;
            if (u16>0 && ((a == null) || (a.equals("")))) {
                Toast.makeText(Ver_tuconsumo.this, "Debe introducir la lectura", Toast.LENGTH_SHORT).show();
            }else{
                if ((a == null) || (a.equals(""))) {
                    lect = 0;
                } else {
                    lect = parseInt(a);  // aqui en lect ya tengo la lectura del medidor
                }

                if (!id2.isEmpty()) {
                    AdminSQLiteOpenHelper dbs = new AdminSQLiteOpenHelper(Ver_tuconsumo.this, "dbReader", null, 1);
                    SQLiteDatabase sdq = dbs.getReadableDatabase();
                    final ProgressDialog progressDialog = new ProgressDialog(Ver_tuconsumo.this);
                    progressDialog.setMessage("Validando...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/getCoopMovil.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    final String status = jsonObject.getString("status");
                                    final String message = jsonObject.getString("message");

                                    if (status.equals("200")) {
                                        String id_genfact = "";
                                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(Ver_tuconsumo.this, "dbReader", null, 1);
                                        SQLiteDatabase BaseDeDato = admin.getWritableDatabase();

                                        JSONObject datosArrayClientes = jsonObject.getJSONObject("clientes");
                                        BaseDeDato.execSQL("delete from genlect");
                                        BaseDeDato.execSQL("delete from aviso"); // borro tb el contenido de la tabla aviso
                                        ContentValues Registros1 = new ContentValues();
                                        Registros1.put("id_genfact", datosArrayClientes.getString("Nro"));
                                        Registros1.put("id_socio", datosArrayClientes.getString("id_socio"));
                                        Registros1.put("cod_socio", datosArrayClientes.getString("cod_socio"));
                                        cod_socio = datosArrayClientes.getString("cod_socio");
                                        Registros1.put("lectant", datosArrayClientes.getString("lectant"));
                                        Registros1.put("lectact", datosArrayClientes.getString("lectact"));
                                        Registros1.put("consumo", datosArrayClientes.getString("consumo"));
                                        Registros1.put("id_mediest", datosArrayClientes.getString("id_mediest"));
                                        Registros1.put("media_ant", datosArrayClientes.getString("media_ant"));
                                        Registros1.put("cobro", datosArrayClientes.getString("cobro"));
                                        cobro= datosArrayClientes.getString("cobro");
                                        Registros1.put("media", datosArrayClientes.getString("media"));
                                        Registros1.put("nomb_categ", datosArrayClientes.getString("nomb_categ"));
                                        Registros1.put("nombre", datosArrayClientes.getString("nombre"));
                                        Registros1.put("deuda", datosArrayClientes.getString("deuda"));
                                        Registros1.put("id_categ", datosArrayClientes.getString("id_categ"));
                                        u19= datosArrayClientes.getInt("id_categ");
                                        Registros1.put("id_medidor", datosArrayClientes.getString("id_medidor"));
                                        u16=datosArrayClientes.getInt("id_medidor");
                                        Registros1.put("cloaca", datosArrayClientes.getString("cloaca"));
                                        Registros1.put("segurovjez", datosArrayClientes.getString("segurovjez"));
                                        Registros1.put("ubicacion", "");
                                        Registros1.put("es_descarga", "0");
                                        Registros1.put("usr", "");
                                        Registros1.put("usrhora", "");
                                        Registros1.put("usrfecha", "");
                                        Registros1.put("direccion", datosArrayClientes.getString("direccion"));
                                        Registros1.put("cantidad", datosArrayClientes.getString("cantidad"));
                                        Registros1.put("fec_lect", datosArrayClientes.getString("fec_lect"));
                                        Registros1.put("lect_ant", datosArrayClientes.getString("lect_ant"));
                                        Registros1.put("consumoant", datosArrayClientes.getString("consumoant"));
                                        BaseDeDato.insert("genlect", null, Registros1);
                                        debug = "clientes";
                                        JSONArray datosArrayCategor_ = jsonObject.getJSONArray("categor_");
                                        BaseDeDato.execSQL("delete from categor_");
                                        for (int i = 0; i < datosArrayCategor_.length(); i++) {
                                            ContentValues RegistrosCategor_ = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayCategor_.getJSONObject(i);
                                            RegistrosCategor_.put("id_categ", jsonObject1.getString("id_categ"));
                                            RegistrosCategor_.put("inicio", jsonObject1.getString("inicio"));
                                            RegistrosCategor_.put("fin", jsonObject1.getString("fin"));
                                            RegistrosCategor_.put("mto_cubo", jsonObject1.getString("mto_cubo"));
                                            RegistrosCategor_.put("mto_alca", jsonObject1.getString("mto_alca"));
                                            BaseDeDato.insert("categor_", null, RegistrosCategor_);
                                        }
                                        debug = "categor_";
                                        JSONArray datosArrayCategori = jsonObject.getJSONArray("categori");
                                        BaseDeDato.execSQL("delete from categori");
                                        for (int i = 0; i < datosArrayCategori.length(); i++) {
                                            ContentValues RegistrosCategori = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayCategori.getJSONObject(i);
                                            RegistrosCategori.put("id_categ", jsonObject1.getString("id_categ"));
                                            RegistrosCategori.put("nomb_categ", jsonObject1.getString("nomb_categ"));
                                            RegistrosCategori.put("grupcate", jsonObject1.getString("grupcate"));
                                            RegistrosCategori.put("moneda", jsonObject1.getString("moneda"));
                                            RegistrosCategori.put("preciocm", jsonObject1.getString("preciocm"));
                                            RegistrosCategori.put("comoaplimi", jsonObject1.getString("comoaplimi"));
                                            RegistrosCategori.put("preciocmal", jsonObject1.getString("preciocmal"));
                                            RegistrosCategori.put("preciosm", jsonObject1.getString("preciosm"));
                                            RegistrosCategori.put("preciosmal", jsonObject1.getString("preciosmal"));
                                            RegistrosCategori.put("consumomin", jsonObject1.getString("consumomin"));
                                            RegistrosCategori.put("mto_rec", jsonObject1.getString("mto_rec"));
                                            RegistrosCategori.put("subcta", jsonObject1.getString("subcta"));
                                            RegistrosCategori.put("totalcateg", jsonObject1.getString("totalcateg"));
                                            RegistrosCategori.put("usr", jsonObject1.getString("usr"));
                                            RegistrosCategori.put("usrhora", jsonObject1.getString("usrhora"));
                                            RegistrosCategori.put("usrfecha", jsonObject1.getString("usrfecha"));
                                            RegistrosCategori.put("monedaaass", jsonObject1.getString("monedaaass"));
                                            BaseDeDato.insert("categori", null, RegistrosCategori);
                                        }
                                        debug = "categori";
                                        JSONObject datosObjectIpc = jsonObject.getJSONObject("ipc");
                                        ContentValues RegistrosIpc = new ContentValues();
                                        RegistrosIpc.put("id_ipc", datosObjectIpc.getString("id_ipc"));
                                        RegistrosIpc.put("cobro", datosObjectIpc.getString("cobro"));
                                        RegistrosIpc.put("fecha", datosObjectIpc.getString("fecha"));
                                        RegistrosIpc.put("indice", datosObjectIpc.getString("indice"));
                                        RegistrosIpc.put("usr", datosObjectIpc.getString("usr"));
                                        RegistrosIpc.put("usrhora", datosObjectIpc.getString("usrhora"));
                                        RegistrosIpc.put("usrfecha", datosObjectIpc.getString("usrfecha"));
                                        BaseDeDato.execSQL("delete from ipc");
                                        BaseDeDato.insert("ipc", null, RegistrosIpc);
                                        debug = "ipc";
                                        JSONArray datosArrayParagene = jsonObject.getJSONArray("paragene");
                                        BaseDeDato.execSQL("delete from paragene");
                                        for (int i = 0; i < datosArrayParagene.length(); i++) {
                                            ContentValues RegistrosParagene = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayParagene.getJSONObject(i);
                                            RegistrosParagene.put("id_empresa", jsonObject1.getString("id_empresa"));
                                            RegistrosParagene.put("iva", jsonObject1.getString("iva"));
                                            RegistrosParagene.put("it", jsonObject1.getString("it"));
                                            RegistrosParagene.put("ley1886", jsonObject1.getString("ley1886"));
                                            RegistrosParagene.put("ley1886cub", jsonObject1.getString("ley1886cub"));
                                            RegistrosParagene.put("ley1886por", jsonObject1.getString("ley1886por"));

                                            BaseDeDato.insert("paragene", null, RegistrosParagene);
                                        }
                                        debug = "paragene";
                                        JSONArray datosArrayServicio = jsonObject.getJSONArray("servicio");
                                        BaseDeDato.execSQL("delete from servicio");
                                        for (int i = 0; i < datosArrayServicio.length(); i++) {
                                            ContentValues RegistrosServicio = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayServicio.getJSONObject(i);
                                            RegistrosServicio.put("id_serv", jsonObject1.getString("id_serv"));
                                            RegistrosServicio.put("nomb_serv", jsonObject1.getString("nomb_serv"));
                                            RegistrosServicio.put("f_servicio", jsonObject1.getString("f_servicio"));
                                            RegistrosServicio.put("moneda", jsonObject1.getString("moneda"));
                                            RegistrosServicio.put("activo", jsonObject1.getString("activo"));
                                            RegistrosServicio.put("mto_serv", jsonObject1.getString("mto_serv"));
                                            RegistrosServicio.put("iva", jsonObject1.getString("iva"));
                                            RegistrosServicio.put("tiposerv", jsonObject1.getString("tiposerv"));
                                            RegistrosServicio.put("porciva", jsonObject1.getString("porciva"));
                                            RegistrosServicio.put("id_sobre", jsonObject1.getString("id_sobre"));
                                            RegistrosServicio.put("porcserv", jsonObject1.getString("porcserv"));

                                            BaseDeDato.insert("servicio", null, RegistrosServicio);
                                        }
                                        debug = "servicio";
                                        JSONArray datosArrayServSoc = jsonObject.getJSONArray("servsoc");
                                        BaseDeDato.execSQL("delete from servsoc");
                                        for (int i = 0; i < datosArrayServSoc.length(); i++) {
                                            ContentValues RegistrosServSoc = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayServSoc.getJSONObject(i);
                                            RegistrosServSoc.put("id_genfact", id_genfact);
                                            RegistrosServSoc.put("id_servsoc", jsonObject1.getString("id_servsoc"));
                                            RegistrosServSoc.put("tiposervso", jsonObject1.getString("tiposervso"));
                                            RegistrosServSoc.put("fechaserv", jsonObject1.getString("fechaserv"));
                                            RegistrosServSoc.put("id_zona", jsonObject1.getString("id_zona"));
                                            RegistrosServSoc.put("ruta", jsonObject1.getString("ruta"));
                                            RegistrosServSoc.put("id_socio", jsonObject1.getString("id_socio"));
                                            RegistrosServSoc.put("id_serv", jsonObject1.getString("id_serv"));
                                            RegistrosServSoc.put("personas", jsonObject1.getString("personas"));
                                            RegistrosServSoc.put("mesini", jsonObject1.getString("mesini"));
                                            RegistrosServSoc.put("mesfin", jsonObject1.getString("mesfin"));
                                            RegistrosServSoc.put("nota", jsonObject1.getString("nota"));
                                            RegistrosServSoc.put("es_servsoc", jsonObject1.getString("es_servsoc"));
                                            RegistrosServSoc.put("moneda", jsonObject1.getString("moneda"));
                                            RegistrosServSoc.put("mto_serv", jsonObject1.getString("mto_serv"));
                                            RegistrosServSoc.put("id_sobre", jsonObject1.getString("id_sobre"));
                                            RegistrosServSoc.put("porcserv", jsonObject1.getString("porcserv"));
                                            RegistrosServSoc.put("usr", jsonObject1.getString("usr"));
                                            RegistrosServSoc.put("usrhora", jsonObject1.getString("usrhora"));
                                            RegistrosServSoc.put("usrfecha", jsonObject1.getString("usrfecha"));
                                            RegistrosServSoc.put("codigo", jsonObject1.getString("codigo"));

                                            BaseDeDato.insert("servsoc", null, RegistrosServSoc);
                                        }
                                        debug = "servsoc";

                                         JSONArray datosArrayCred = jsonObject.getJSONArray("creditos");
                                        for (int i = 0; i < datosArrayCred.length(); i++) {
                                            ContentValues RegistrosCred = new ContentValues();
                                            JSONObject jsonObject1 = datosArrayCred.getJSONObject(i);
                                            RegistrosCred.put("id_genfact", id_genfact);
                                            RegistrosCred.put("id_credito", jsonObject1.getString("id_credito"));
                                            RegistrosCred.put("id_socio", jsonObject1.getString("id_socio"));
                                            RegistrosCred.put("f_credito", jsonObject1.getString("f_credito"));
                                            RegistrosCred.put("id_serv", jsonObject1.getString("id_serv"));
                                            RegistrosCred.put("id_sobre", jsonObject1.getString("id_sobre"));
                                            RegistrosCred.put("porcserv", jsonObject1.getString("porcserv"));
                                            RegistrosCred.put("mto_ant", jsonObject1.getString("mto_ant"));
                                            RegistrosCred.put("n_cuotas", jsonObject1.getString("n_cuotas"));
                                            RegistrosCred.put("pagcuotas", jsonObject1.getString("pagcuotas"));
                                            RegistrosCred.put("mto_pagado", jsonObject1.getString("mto_pagado"));
                                            RegistrosCred.put("mesini", jsonObject1.getString("mesini"));
                                            RegistrosCred.put("mto_cred", jsonObject1.getString("mto_cred"));
                                            RegistrosCred.put("mto_mes", jsonObject1.getString("mto_mes"));
                                            RegistrosCred.put("es_credito", jsonObject1.getString("es_credito"));
                                            RegistrosCred.put("moneda", jsonObject1.getString("moneda"));
                                            RegistrosCred.put("interes", jsonObject1.getString("interes"));
                                            RegistrosCred.put("saldo", jsonObject1.getString("saldo"));
                                            RegistrosCred.put("nota", jsonObject1.getString("nota"));
                                            RegistrosCred.put("porciva", jsonObject1.getString("porciva"));
                                            debug = "creditos";
                                            BaseDeDato.insert("l_cred", null, RegistrosCred);
                                        }
                                        BaseDeDato.close();

                                        ObtenerDatosPFacturar(pgid_serv,pgcubos,pgporc,vella,x23,x24); // obtiene de las tablas los datos necesarios para generar los avisos de cobranza
                                        categ_ant=0;categ_act=0;
                                        cont_reg=0;
                                        ObtenerPrecioBasCategorias(u19);
                                        ObtenerPrecioDifCategorias(u19);

                                        int id_servi;
                                        ObtenerTodosServicios(); // saca la lista de todos los servicios

                                        calcularConsumo(lect);  // coloca en totalConsumo el monto a cobrar por consumo de agua
                                        if (u17 == 1) {  // entra a calcular alcantarillado, si tiene cloaca
                                            grabarReg(6,"Alcantarillado Saguapac",(float) (Math.round(totalAlcantarilla*100)/100d));
                                            // ya no calcula la alcantarilla, porque el calculo lo hace dentro del calculo de agua
                                            // calcularAlcantarilla(totalConsumo, lect); // calcula alcanyarilla con o sin medidor
                                        }
                                        if (u18 == 1) {  // entra a calcular seguro de vejez
                                            calcularSeguroVejez(totalConsumo, lect);
                                        }
                                        calcularServEsp1();  // tipo de servicio = 1  por socio
                                        calcularServEsp2(); // tipo de servicio = 2   por rutas
                                        calcularServEsp3(); // tipo de servicio = 3  por zonas
                                        calcularServEsp4(); // tipo de servicio = 4   para todos
                                        calcular_creditos(u2); // saca todos los creditos del socio y los carga al aviso
                                        totalFactura=SacarTotalFactura();  // funcion que navega por la tabla aviso y saca la suma de todos los servicios facturados
                                        totalFactura = (float) (Math.round(totalFactura * 100) / 100d); // aqui REDONDEO A 2 DECIMALES
                                        tvmontofactura.setText(totalFactura + "");
                                        tableDynamic.cargarAviso(tableLayout, Ver_tuconsumo.this);
                                    }


                                } catch (JSONException e) {
                                    Toast.makeText(Ver_tuconsumo.this,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
                                    Log.e("Ver_tuconsumo", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Ver_tuconsumo.this, "ERROR EN LA RED", Toast.LENGTH_SHORT).show();
                                btncalcular.setVisibility(View.VISIBLE);
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id_soc", id2);
                                 return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        MySingleton.getInstance(Ver_tuconsumo.this).addToRequest(stringRequest);


                }else{
                    Toast.makeText(getApplicationContext(),"Faltan Datos...",Toast.LENGTH_SHORT).show();
                    btncalcular.setVisibility(View.VISIBLE);
                }
            }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Tu_consumo.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }

    private void calcular(String noMsg){
        totalFactura=0;  // aqui almacenare el total de la factura
        totalConsumo=0;
        totalAlcantarilla=0;
        totalLey1886=0;
        totalDsctoConsumo=0;
        totalDsctoAlcantarilla=0;
        totalMcm=0.0f;
        totalCm=0.0f;
        consumo=0;
        borrarAviso();

        EditText lectura;
        lectura = (EditText) findViewById(R.id.etTotal);
        String a=lectura.getText().toString();
        int lect=0;
        if (u16>0 && ((a == null) || (a.equals("")))) {
           // Toast.makeText(VerRegistroActivity.this, "Debe introducir la lectura", Toast.LENGTH_SHORT).show();
        }else{
            if ((a == null) || (a.equals(""))) {
                lect = 0;
            } else {
                lect = parseInt(a);  // aqui en lect ya tengo la lectura del medidor
            }
            int id_servi;
            ObtenerTodosServicios(); // saca la lista de todos los servicios

            calcularConsumo(lect);  // coloca en totalConsumo el monto a cobrar por consumo de agua
            if (u17 == 1) {  // entra a calcular alcantarillado, si tiene cloaca
                calcularAlcantarilla(totalConsumo, lect); // calcula alcanyarilla con o sin medidor
            }
            if (u18 == 1) {  // entra a calcular seguro de vejez
                calcularSeguroVejez(totalConsumo, lect);
            }
            calcularServEsp1();  // tipo de servicio = 1  por socio
            calcularServEsp2(); // tipo de servicio = 2   por rutas
            calcularServEsp3(); // tipo de servicio = 3  por zonas
            calcularServEsp4(); // tipo de servicio = 4   para todos
            // borrar porque el de arriba lo calcula    calcularMCM();  // codigo 9
            // borrar calcularCuotaMortuoria();  // cuota mortuoria 24

            totalFactura=SacarTotalFactura();  // funcion que navega por la tabla aviso y saca la suma de todos los servicios facturados
            totalFactura = (float) (Math.round(totalFactura * 100) / 100d); // aqui REDONDEO A 2 DECIMALES
            tvmontofactura.setText(totalFactura + "");
        }
    }
    private void calcular_creditos(int idsocio){
        float cmto_mes=000000.00f, csaldo=000000.00f, mto_t_credito=000000.00f;
        int cmoneda=0, cidcredito, cidserv;
        String cservicio;
        Cursor datosCred=bd.getCreditos(idsocio);
        if (datosCred.getCount()>0) {  // verifico que haya este servicio en la tabla
            datosCred.moveToFirst();
            for (int i=0;i<datosCred.getCount();i++) {
                mto_t_credito = 0;
                cmto_mes = datosCred.getFloat(datosCred.getColumnIndex("mto_mes"));
                csaldo = datosCred.getFloat(datosCred.getColumnIndex("saldo"));
                cmoneda = datosCred.getInt(datosCred.getColumnIndex("moneda"));
                cservicio = datosCred.getString(datosCred.getColumnIndex("nomb_serv"));
                cidcredito = datosCred.getInt(datosCred.getColumnIndex("id_credito"));
                cidserv = datosCred.getInt(datosCred.getColumnIndex("id_serv"));

                if (cmto_mes<=csaldo){  // && aqui coloco en mto_t_credito lo que se va a colocar del credito
                    mto_t_credito = cmto_mes;
                }else{
                    mto_t_credito = csaldo;
                }

                switch (cmoneda) {
                    case 1:  //  Dolares
                        break;
                    case 2:  //  Boliviano
                        grabarReg(cidserv,cservicio,mto_t_credito);
                        break;
                    case 3:  // Ufv
                        grabarReg(cidserv,cservicio, (float) (Math.round(mto_t_credito * ipc * 100) / 100d) );
                        break;
                }
            }
        }
    }

    private void cargarLista() {
        AdminSQLiteOpenHelper datos6 = new AdminSQLiteOpenHelper(Ver_tuconsumo.this,"dbReader",null,1);
        SQLiteDatabase database = datos6.getWritableDatabase();
        String[] campos = new String[]{"id_mediest","nomb_medie"};
        ArrayList<String> elementos = new ArrayList<String>();
        Cursor cursor6 = database.query("mediesta",campos,null,null,null,null,"id_mediest");
        elementos.add("Seleccionar estado de Medidor");
        if (cursor6.moveToFirst()){
            do {
                elementos.add(cursor6.getString(1));
            }while (cursor6.moveToNext());
            ArrayAdapter adp = new ArrayAdapter(Ver_tuconsumo.this,android.R.layout.simple_list_item_1,elementos);
            listMediest.setAdapter(adp);

        }
    }

    public static int sacar_lectant_promedio_consumo(int idsocio){
        int promedio=0,lectant=0;
        Cursor Prom=bd.sacar_lectant_PromConsumo(idsocio);
        if (Prom.getCount()>0){
            Prom.moveToFirst();
            lectant=Prom.getInt(Prom.getColumnIndex("lectant"));
            promedio=Prom.getInt(Prom.getColumnIndex("media"));
        }
        return lectant+promedio;
    }
    public static float SacarTotalFactura(){
        float suma=0.00f,ase01=0.00f;
        Cursor aviso=bd.getCargaAvisosCargados();
        if (aviso.getCount()>0) {  // tiene por lo menos 1 servicio especial
            int q=aviso.getCount();
            aviso.moveToFirst();
            for (int zx=0;zx<aviso.getCount();zx++){
                aviso.moveToPosition(zx);
                ase01=aviso.getFloat(aviso.getColumnIndex("monto"));
                suma=suma+ase01;
            }
        }
        return suma;
    }
    public void calcularServEsp1(){
     //   Toast.makeText(this,"ingresando Esp1 ", Toast.LENGTH_SHORT).show();
        Cursor ServEsp=bd.getServSocTipo1(1,u2);  // envio tipo de servicio y id_socio
        String ase0;
        int ase1=0,ase4=0,ase5=0;
        float ase2=0.00f,ase3=0.00f,monto=0.00f;
        if (ServEsp.getCount()>0) {  // tiene por lo menos 1 servicio especial
        //    Toast.makeText(this,"tiene estos servicios: "+ServEsp.getCount(), Toast.LENGTH_SHORT).show();
            for (int zz=0;zz<ServEsp.getCount();zz++){
                ServEsp.moveToPosition(zz);
                ase0=ServEsp.getString(ServEsp.getColumnIndex("nomb_serv"));
                //Toast.makeText(this,"Nombre servicio: "+ase0, Toast.LENGTH_SHORT).show();
                ase1=ServEsp.getInt(ServEsp.getColumnIndex("id_sobre"));
                //Toast.makeText(this,"Sobre Servicio: "+ase0, Toast.LENGTH_SHORT).show();
                ase2=ServEsp.getFloat(ServEsp.getColumnIndex("mto_serv"));
                //Toast.makeText(this,"Monto : "+ase2, Toast.LENGTH_SHORT).show();
                ase3=ServEsp.getFloat(ServEsp.getColumnIndex("porcserv"));
                //Toast.makeText(this,"Porcentaje : "+ase3, Toast.LENGTH_SHORT).show();
                ase4=ServEsp.getInt(ServEsp.getColumnIndex("id_serv"));
                ase5=ServEsp.getInt(ServEsp.getColumnIndex("moneda"));
                switch (ase5){
                    case 1:  // dolares
                        break;
                    case 2:  // Si es bolivianos no hace nada
                        break;
                    case 3:  // ufv
                        ase3=(float) (Math.round((ase3*ipc) * 100) / 100d);
                        ase2=(float) (Math.round((ase2*ipc) * 100) / 100d);
                        break;
                }

                if (ase1==0){   // monto fijo de cobro
                    monto=ase2;
                    //totalFactura=totalFactura+ase2;
                    totalFactura=totalFactura+monto;
                }else{  // porcentaje de algún servicio
                    switch(ase1){
                        case 1:  // es del total consumo
                            monto=(float) (Math.round((totalConsumo*ase3)*100)/100d);
                            //totalFactura=totalFactura+(totalConsumo*ase3);
                            totalFactura=totalFactura+monto;
                            break;
                        case 2:  // es de otro servicio
                            break;
                        case 3:  // es de otro servicio
                            break;
                    }
                }
                /*Toast.makeText(this,"Grabando id: "+ase1, Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Grabando detalle: "+ase0, Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Grabando monto: "+monto, Toast.LENGTH_SHORT).show();*/
                // aqui debe grabar en la tabla para imprimir
                grabarReg(ase4,ase0,monto);
            }
        }else{
        //    Toast.makeText(this,"No hay servicios especiales tipo: 1", Toast.LENGTH_SHORT).show();
        }
    }
    public void calcularServEsp2(){

        int zona= parseInt((cod_socio.substring(0,2)),10);
        int ruta= parseInt(cod_socio.substring(2,4),10);
        Cursor ServEsp=bd.getServSocTipo2(2,zona,ruta);
        String ase0;
        int ase1=0,ase4=0,ase5=0;
        float ase2=0.00f,ase3=0.00f,monto=0.00f;
        if (ServEsp.getCount()>0) {  // tiene por lo menos 1 servicio especial
            for (int zz=0;zz<ServEsp.getCount();zz++){
                ServEsp.moveToPosition(zz);
                ase0=ServEsp.getString(ServEsp.getColumnIndex("nomb_serv"));
                ase1=ServEsp.getInt(ServEsp.getColumnIndex("id_sobre"));
                ase2=ServEsp.getFloat(ServEsp.getColumnIndex("mto_serv"));
                ase3=ServEsp.getFloat(ServEsp.getColumnIndex("porcserv"));
                ase4=ServEsp.getInt(ServEsp.getColumnIndex("id_serv"));
                ase5=ServEsp.getInt(ServEsp.getColumnIndex("moneda"));
                switch (ase5){
                    case 1:  // dolares
                        break;
                    case 2:  // Si es bolivianos no hace nada
                        break;
                    case 3:  // ufv
                        ase3=(float) (Math.round((ase3*ipc) * 100) / 100d);
                        ase2=(float) (Math.round((ase2*ipc) * 100) / 100d);
                        break;
                }

                if (ase1==0){   // monto fijo de cobro
                    monto=ase2;
                    totalFactura=totalFactura+monto;
                    //totalFactura=totalFactura+ase2;
                }else{  // porcentaje de algún servicio
                    switch(ase1){
                        case 1:  // es del total consumo
                            monto=(float) (Math.round((totalConsumo*ase3)*100)/100d);
                            totalFactura=totalFactura+monto;
                            break;
                        case 2:  // es de otro servicio
                            break;
                        case 3:  // es de otro servicio
                            break;
                    }
                }

                grabarReg(ase4,ase0,monto);
            }
        }else{
        //    Toast.makeText(this,"No hay servicios especiales tipo: 2", Toast.LENGTH_SHORT).show();
        }
    }
    public void calcularServEsp3(){
     //   Toast.makeText(this,"ingresando Esp3 ", Toast.LENGTH_SHORT).show();
        int zona= parseInt((cod_socio.substring(0,2)),10);
        Cursor ServEsp=bd.getServSocTipo3(3,zona);
        String ase0;
        int ase1=0,ase4=0,ase5=0;
        float ase2=0.00f,ase3=0.00f,monto=0.00f;
        if (ServEsp.getCount()>0) {  // tiene por lo menos 1 servicio especial
         //   Toast.makeText(this,"tiene estos servicios: "+ServEsp.getCount(), Toast.LENGTH_SHORT).show();
            for (int zz=0;zz<ServEsp.getCount();zz++){
                ServEsp.moveToPosition(zz);
                ase0=ServEsp.getString(ServEsp.getColumnIndex("nomb_serv"));
                //Toast.makeText(this,"Nombre servicio: "+ase0, Toast.LENGTH_SHORT).show();
                ase1=ServEsp.getInt(ServEsp.getColumnIndex("id_sobre"));
                //Toast.makeText(this,"Sobre Servicio: "+ase0, Toast.LENGTH_SHORT).show();
                ase2=ServEsp.getFloat(ServEsp.getColumnIndex("mto_serv"));
                //Toast.makeText(this,"Monto : "+ase2, Toast.LENGTH_SHORT).show();
                ase3=ServEsp.getFloat(ServEsp.getColumnIndex("porcserv"));
                //Toast.makeText(this,"Porcentaje : "+ase3, Toast.LENGTH_SHORT).show();
                ase4=ServEsp.getInt(ServEsp.getColumnIndex("id_serv"));
                ase5=ServEsp.getInt(ServEsp.getColumnIndex("moneda"));
                switch (ase5){
                    case 1:  // dolares
                        break;
                    case 2:  // Si es bolivianos no hace nada
                        break;
                    case 3:  // ufv
                        ase3=(float) (Math.round((ase3*ipc) * 100) / 100d);
                        ase2=(float) (Math.round((ase2*ipc) * 100) / 100d);
                        break;
                }

                if (ase1==0){   // monto fijo de cobro
                    monto=ase2;
                    //totalFactura=totalFactura+ase2;
                    totalFactura=totalFactura+monto;
                }else{  // porcentaje de algún servicio
                    switch(ase1){
                        case 1:  // es del total consumo
                            monto=(float) (Math.round((totalConsumo*ase3)*100)/100d);
                            totalFactura=totalFactura+monto;
                            //totalFactura=totalFactura+(totalConsumo*ase3);
                            break;
                        case 2:  // es de otro servicio
                            break;
                        case 3:  // es de otro servicio
                            break;
                    }
                }
                /*Toast.makeText(this,"Grabando id: "+ase1, Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Grabando detalle: "+ase0, Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Grabando monto: "+monto, Toast.LENGTH_SHORT).show();*/
                // aqui debe grabar en la tabla para imprimir
                grabarReg(ase4,ase0,monto);
            }
        }else{
        //    Toast.makeText(this,"No hay servicios especiales tipo: 3", Toast.LENGTH_SHORT).show();
        }
    }

    public void calcularServEsp4(){
     //   Toast.makeText(this,"ingresando Esp4 ", Toast.LENGTH_SHORT).show();
        String mmes=cobro;
        Cursor ServEsp=bd.getServSocTipo4(4,mmes);
        String ase0;
        int ase1=0,ase4=0,ase5=0;
        float ase2=0.00f,ase3=0.000000f,monto=0.00f;
        if (ServEsp.getCount()>0) {  // tiene por lo menos 1 servicio especial
            int w=ServEsp.getCount();
            for (int zz=0;zz<ServEsp.getCount();zz++){
                ServEsp.moveToPosition(zz);
                ase0=ServEsp.getString(ServEsp.getColumnIndex("nomb_serv"));
                ase1=ServEsp.getInt(ServEsp.getColumnIndex("id_sobre"));
                ase2=ServEsp.getFloat(ServEsp.getColumnIndex("mto_serv"));
                ase3=ServEsp.getFloat(ServEsp.getColumnIndex("porcserv"));
                ase4=ServEsp.getInt(ServEsp.getColumnIndex("id_serv"));
                ase5=ServEsp.getInt(ServEsp.getColumnIndex("moneda"));
                switch (ase5){
                    case 1:  // dolares
                        break;
                    case 2:  // Si es bolivianos no hace nada
                        break;
                    case 3:  // ufv
                        ase3=(float) (Math.round(((ase3/10)*ipc) * 100) / 100d);
                        ase2=(float) (Math.round((ase2*ipc) * 100) / 100d);
                        break;
                }

                if (ase1==0){   // monto fijo de cobro
                    monto=ase2;
                    totalFactura=totalFactura+monto;
                    //totalFactura=totalFactura+ase2;
                }else{  // porcentaje de algún servicio
                    switch(ase1){
                        case 1:  // es del total consumo
                            monto=(float) (Math.round((totalConsumo*(ase3/10))*100)/100d);
                            totalFactura=totalFactura+monto;
                            //totalFactura=totalFactura+(totalConsumo*ase3);
                            break;
                        case 2:  // es de otro servicio
                            break;
                        case 3:  // es de otro servicio
                            break;
                    }
                }
                grabarReg(ase4,ase0,monto);
            }
        }else{
        //    Toast.makeText(this,"No hay servicios especiales tipo: 4", Toast.LENGTH_SHORT).show();
        }
    }
    public void borrarAviso(){
        bd.borraAviso();
    }
    public void grabarReg(int g1,String g2,float g3){
        bd.grabaAviso(g1,g2,g3);
    }
    public void calcularCuotaMortuoria(){
        totalCm = 1.45f;
    }
    public void ObtenerTodosServicios(){
        Servicios=bd.getServiciosTodos();
    }
    public void calcularConsumo(int lec){
        if (u16==0) {  // si es 0 no tiene medidor
            consumo=cmin;
            totalConsumo=psmin;
            totalAlcantarilla=psminal; // aqui ya calcula su alcantarillado tenga o no // lo grabara si tiene alcantarilla
        }else{  // tiene medidor
            consumo=lec-u5;  // u5: lectura anterior

            if (consumo<=cmin){  // si el consumo es menor o igual al minimo, asigna el cobro minimo
                consumo=cmin;
                totalConsumo=pcmin; // no hace ningun calculo, solo asigna el costo minimo
                totalAlcantarilla=pcminal;

            }else {   // el consumo es mayor al minimo
                totalConsumo=calculoCm(consumo);
            }
        }
        // aqui debe grabar en la tabla para imprimir
        totalConsumo=(float) (Math.round(totalConsumo*100)/100d);
        grabarReg(1,"Consumo Mensual",totalConsumo);
    }

    public float calculoCm(int consumo1){
        float totalcalculado=0.00f,mto_cubo=0.00f,mto_alca=0.00f;
        float tot_alcantarilla=0.00f;
        int ini=0,fin=0;
        totalcalculado=pcmin;
        tot_alcantarilla=pcminal;

        consumo1=consumo1-cmin;
        datosCategDif.moveToFirst();
        int i;
        for (i=0;i<datosCategDif.getCount();i++){
            datosCategDif.moveToPosition(i);
            ini=datosCategDif.getInt(datosCategDif.getColumnIndex( "inicio"));
            fin=datosCategDif.getInt(datosCategDif.getColumnIndex( "fin"));
            mto_cubo=datosCategDif.getFloat(datosCategDif.getColumnIndex( "mto_cubo"));
            mto_alca=datosCategDif.getFloat(datosCategDif.getColumnIndex( "mto_alca"));
            if (consumo1<=(fin-ini)+1){
                totalcalculado=totalcalculado+(consumo1*mto_cubo*ipc);
                tot_alcantarilla=tot_alcantarilla+(consumo1*mto_alca*ipc);
                i=datosCategDif.getCount(); // obligo a salir
            }else{
                totalcalculado=totalcalculado+(((fin-ini)+1)*mto_cubo*ipc);
                tot_alcantarilla=tot_alcantarilla+(((fin-ini)+1)*mto_alca*ipc);
                consumo1=consumo1-((fin-ini)+1);

            }
        }
        totalAlcantarilla=tot_alcantarilla;
        return totalcalculado;
    }

    public void calcularAlcantarilla(float cons,int lec){
        float auxi=0.00f,costoalcan=0.80f;
        if (u16==0) {  // si es 0 no tiene medidor
            totalAlcantarilla=psminal;
        }else{  // tiene medidor
            int consu=lec-u5;
            if (consu<=cmin){
                totalAlcantarilla = pcmin * costoalcan;
            }else {
                auxi = calculoCm(consu);    // aqui manda el consumo mayor al minimo y menor a cubos de la ley
                totalAlcantarilla = auxi * costoalcan;
                //Toast.makeText(VerRegistroActivity.this, "Alcantarilla : "+totalAlcantarilla, Toast.LENGTH_SHORT).show();
            }
        }
        grabarReg(6,"Alcantarillado Saguapac",(float) (Math.round(totalAlcantarilla*100)/100d));
    }
    public void calcularAlcantarilla_080(float cons,int lec){
        float auxi=0.00f,costoalcan=0.80f;
        if (u16==0) {  // si es 0 no tiene medidor
            totalAlcantarilla=psminal;
        }else{  // tiene medidor
            int consu=lec-u5;
            if (consu<=cmin){
                totalAlcantarilla = pcmin * costoalcan;
            }else {
                auxi = calculoCm(consu);    // aqui manda el consumo mayor al minimo y menor a cubos de la ley
                totalAlcantarilla = auxi * costoalcan;
                //Toast.makeText(VerRegistroActivity.this, "Alcantarilla : "+totalAlcantarilla, Toast.LENGTH_SHORT).show();
            }
        }
        grabarReg(6,"Alcantarillado Saguapac",(float) (Math.round(totalAlcantarilla*100)/100d));
    }
    public void calcularSeguroVejez(float cons,int lec){
        float auxi=0.00f;
        if (u16==0) {  // si es 0 no tiene medidor
            totalLey1886=cons*pgporc;
        }else{  // tiene medidor
            int consu=lec-u5;
            if (consu<=cmin){
                totalLey1886 = pcmin * pgporc;
            }else {
                if (consu>=pgcubos) {
                    auxi = calculoCm(pgcubos);  // mando a calcular los cubos de la ley 1886
                }else{
                    auxi = calculoCm(consu);    // aqui manda el consumo mayor al minimo y menor a cubos de la ley
                }
                totalLey1886 = auxi * pgporc;
            //    Toast.makeText(VerRegistroActivity.this, "ley 1886 : "+totalLey1886, Toast.LENGTH_SHORT).show();
            }
        }
        grabarReg(18,"Ley1886", (float) (Math.round((totalLey1886*(-1))*100)/100d) );
    }
    public void calcularMCM(){
        int uu1=0;
        float uu2=0.00f;
        Cursor datosMcm=bd.getServicioMCM(9);
        if (datosMcm.getCount()>0) {  // verifico que haya este servicio en la tabla
            datosMcm.moveToFirst();
            uu1 = datosMcm.getInt(datosMcm.getColumnIndex("moneda"));
            uu2 = datosMcm.getFloat(datosMcm.getColumnIndex("mto_serv"));
            switch (uu1) {
                case 1:  //  Dolares
                    break;
                case 2:  //  Boliviano
                    totalMcm = uu2;
                    break;
                case 3:  // Ufv
                    totalMcm = (float) (Math.round(uu2 * ipc * 100) / 100d);
                    break;
            }
        }
    }


    public void ObtenerPrecioBasCategorias(int v1){
        datosCateg=bd.getPrecBasCateg(v1);
        datosCateg.moveToFirst();
        pcmin=datosCateg.getFloat(datosCateg.getColumnIndex( "preciocm"));
        psmin=datosCateg.getFloat(datosCateg.getColumnIndex( "preciosm"));
        pcminal=datosCateg.getFloat(datosCateg.getColumnIndex( "preciocmal"));
        psminal=datosCateg.getFloat(datosCateg.getColumnIndex( "preciosmal"));
        cmin=datosCateg.getInt(datosCateg.getColumnIndex( "consumomin"));
        tmoneda=datosCateg.getInt(datosCateg.getColumnIndex( "moneda"));
//        tvpcmin.setText(pcmin+"");
  //      tvpsmin.setText(psmin+"");
    //    tvcmin.setText(pcmin+"");
        switch (tmoneda){
            case 1:  // dolares
                break;
            case 2:  // bolivianos
                break;
            case 3:  // ufv
                pcmin=(float) (Math.round(pcmin*ipc * 100) / 100d);  //pcmin*ipc;
                psmin=(float) (Math.round(psmin*ipc * 100) / 100d);  // (float) (Math.round(totalFactura * 100) / 100d);
                pcminal=(float) (Math.round(pcminal*ipc * 100) / 100d);  //pcminal*ipc;
                psminal=(float) (Math.round(psminal*ipc * 100) / 100d);  //psminal*ipc;
                break;
            case 4:  // otro
                break;
        }
        //tvprecub.setText(psmin+"");
    }
    public void ObtenerPrecioDifCategorias(int v1){
        datosCategDif=bd.getPrecDifCateg(v1);
    }

    public void ObtenerDatosPFacturar(int v1,int v2,float v3,String v4,int v5,String v6){
        Cursor datosPara=bd.ObtenDatosParagene();
        datosPara.moveToFirst();
        v1=datosPara.getInt(datosPara.getColumnIndex( "ley1886"));
        v2=datosPara.getInt(datosPara.getColumnIndex( "ley1886cub"));
        v3=datosPara.getFloat(datosPara.getColumnIndex( "ley1886por"));
/*        Cursor datosOrde=bd.ObtenDatosOrdefact();
        datosOrde.moveToFirst();
        v4=datosOrde.getString(datosOrde.getColumnIndex( "llavedosi"));
        v5=datosOrde.getInt(datosOrde.getColumnIndex( "num_orden"));
        v6=datosOrde.getString(datosOrde.getColumnIndex( "f_limite"));
*/
        pgid_serv=v1;
        pgcubos=v2;
        pgporc=v3;
 /*       vella=v4;
        x23=v5;
        x24=v6;*/
        Cursor datosIpc=bd.ObtenIpc();
        datosIpc.moveToFirst();
        ipc=datosIpc.getFloat(datosIpc.getColumnIndex( "indice"));
//        tvipc.setText(ipc+"");
//        tvmensaje.setText(u4);

    }
}
