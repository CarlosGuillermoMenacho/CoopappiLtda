package com.coopappiltda.coopappiltda;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.CuadroDialogo;
import com.coopappiltda.clases.MySingleton;
import com.coopappiltda.clases.Socio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CuadroDialogo.finalizado{
    private ListView lv1;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enlazarVariables();
        cargarListView();


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);
            }
        });


    }
    //Cuando se selecciona un item de la ListView
    private void itemSelected(int position) {
        switch (position){
            case 0:
                new CuadroDialogo(this,MainActivity.this);
                break;
            case 1:
                
                break;
        }
    }
    //Mostrar las opciones en la ListView
    private void cargarListView() {
        //Agregando las opciones al array para pasarlo a la ListView
        ArrayList<String> lv1Array = new ArrayList<>();
        lv1Array.add("Consultar Facturas");
        lv1Array.add("Puntos de cobranza");
        lv1Array.add("Avisos");

        Context context = getApplicationContext(); //obteniendo el contexto de la vista actual

        //Creando un adapter para pasarle el array e luego pasar el adapter a ListView
        ArrayAdapter<String> lv1Adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lv1Array);

        //Indicando que el ListView utilizará el adapter
        lv1.setAdapter(lv1Adapter);
    }
    //Aquí se hacen los cast a cada uno de los elementos de la vista
    private void enlazarVariables() {
        //Enlazanado los elementos de la vista con las variables
        lv1 = findViewById(R.id.lv1);    //Enlace al ListView de la vista en la cual se encuetran las opciones principales
    }
    //Cuando se oprime el boton back del teléfono
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
    @Override
    //Cuando se ingresan datos en el AlertDialog y se presiona aceptar
    public void resultado(final String codigoFijo, final String codUbicacion) {
        if (!codigoFijo.isEmpty()) {
            //Mostrará un mensaje de espera mientras se cargan los datos
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Realizando consulta...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
            //Realizando request mediante POST al servidor para obtener los datos del socio
            StringRequest stringRequest =new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/getSocioHistorial.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //Obteniendo la respuesta en formato JSON
                        JSONObject jsonObject = new JSONObject(response);
                        //Obteniendo el estatus de la respuesta
                        final String status = jsonObject.getString("status");

                        if (status.equals("200")){//Hay datos del socio
                            //Obteniendo los datos del socio
                            final String mensaje = jsonObject.getString("mensaje"); //Mensaje de la respuesta
                            final String codigo = jsonObject.getString("codigo"); //Obteniendo Codigo Fijo
                            final String codUbicacion = jsonObject.getString("codUbicacion");//Obteniendo codigo de Ubicación
                            final String nombre = jsonObject.getString("nombre"); //Obteniendo el Nombre del socio
                            final String direccion = jsonObject.getString("direccion"); //Obteniendo la dirección
                            final int facturasImpagas = jsonObject.getInt("totalFacturaImpaga");//Obteniendo la cantidad de facturas impagas
                            final String fechacorte = jsonObject.getString("fechaCorte");//Obteniendo la fecha de corte
                            String monto = jsonObject.getString("monto"); //Obteniendo la deuda total

                            JSONArray jsonArray = jsonObject.getJSONArray("facturas"); //Obteniendo el historial de facturas del socio

                           //Preparando una lista con el historial de facturas
                            ArrayList<String[]> facturas = new ArrayList<>();

                            for (int i = 0 ; i < jsonArray.length(); i++){
                               JSONArray datosJson = jsonArray.getJSONArray(i);
                               String[] datos = new String[datosJson.length()];
                               for (int j = 0 ; j < datosJson.length(); j++){
                                    datos[j] = datosJson.getString(j);
                               }
                               facturas.add(datos);
                            }
                            //Creando la instancia socio para pasarle los datos del socio
                            Socio socio = new Socio(Integer.parseInt(codigo),codUbicacion,nombre,monto,direccion, facturasImpagas, fechacorte, facturas);
                            //Para almacenar los datos en la base de datos
                            socio.save(MainActivity.this);
                            //Cerrando el mensaje de espera
                            progressDialog.dismiss();
                            //Preparando un alert dialog para notificar que se obtuvieron los datos exitosamente
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);//Definiendo donde se mostrará
                            builder.setMessage(mensaje); //Asignado el mensaje que se mostrará
                            //Añadiendo un botón
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this,ConsultaFacturas.class));
                                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                                }
                            });
                            //Mostrar el alertDialog en la vista actual
                            builder.show();
                        }else{//Si no hay datos del socio
                            //Preparando un alertDialog para notificar que el socio no existe
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("El codigo ingresado no existe."); //Mensaje a mostrar
                            //Añadiendo un botón aceptar
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Al oprimir el botón cerrará el alert dialog
                                    progressDialog.dismiss();
                                    //Mostrará un nuevo alert para repetir el proceso de consulta
                                    new CuadroDialogo(MainActivity.this,MainActivity.this);
                                }
                            });
                            builder.show();//Mostrar el alertDialog para notificar que no hay datos
                        }

                    }catch (JSONException e){
                        //Cuando hay un error en los datos obtenidos
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Consulta de Socio");
                        builder.setMessage("No se pudo conectar \n intente nuevamente...");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CuadroDialogo(MainActivity.this,MainActivity.this);
                            }
                        });
                        Log.e("MainActivity Consultar",e.toString());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                //Cuando hay un problema en la red Internet
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "ERROR EN LA RED", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                //Aquí se envian los datos al servidor mediante POST
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("codigoFijo", codigoFijo);//Dato a enviar donde "codigoFijo" es la llave y codigoFijo es el valor
                    params.put("codUbicacion",codUbicacion);
                    return params;
                }
            };
            //Definiendo parametros de la conexión con un tiempo determindo de espera para que el servidor responda
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Añadiendo el request a la cola de peticiones al servidor
            MySingleton.getInstance(MainActivity.this).addToRequest(stringRequest);

        }
    }
}