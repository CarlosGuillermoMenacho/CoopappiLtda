package com.coopappiltda.coopappiltda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.MyAdapterListView;
import com.coopappiltda.clases.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obtenerFecha();
        enlazarVariables();

        cargarListView();


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);
            }
        });


    }

    private void obtenerFecha() {
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(MainActivity.this,"dbReader",null,1);
        Cursor cursor = adm.getfecha();
        if (cursor.moveToFirst()){
            Constants.fecha = cursor.getString(0);
        }
    }

    //Cuando se selecciona un item de la ListView
    private void itemSelected(int position) {
        switch (position){
            case 0:
                startActivity(new Intent(this,ConsultaFacturas.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 1:
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Obteniendo puntos de cobro...");
                progressDialog.setIndeterminate(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Constants.SERVER_URL + "/get_puntosCobro.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonObject = new JSONArray(response);
                            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                            intent.putExtra("respuesta",response);
                            intent.putExtra("vista","pc");
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in,R.anim.left_out);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,"No se puede mostrar...",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Error en la red...",Toast.LENGTH_SHORT).show();
                    }
                });
                //Definiendo parametros de la conexión con un tiempo determindo de espera para que el servidor responda
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Añadiendo el request a la cola de peticiones al servidor
                MySingleton.getInstance(MainActivity.this).addToRequest(stringRequest);

                break;
            case 2:
               startActivity(new Intent(this,Requisitos.class));
               overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 3:
               startActivity(new Intent(this,Tu_consumo.class));
               overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 4:
                startActivity(new Intent(this,Notificaciones.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
        }
    }
    //Mostrar las opciones en la ListView
    private void cargarListView() {
        //Agregando las opciones al array para pasarlo a la ListView
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();
        names.add("Afiliación y\nConsulta de Facturas");
        names.add("Puntos de Pago");
        names.add("Requisitos");
        names.add("Consumo");
        names.add("Notificaciones");

        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        //Creando un adapter con todos los item
        MyAdapterListView myAdapterListView = new MyAdapterListView(this, names,images);
        //Indicando que el ListView utilizará el adapter
        lv1.setAdapter(myAdapterListView);
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
   }