package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.MyAdapterUnidades;
import com.coopappiltda.clases.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnidadesdeEmergencia extends AppCompatActivity {
    private ImageView ivBack1; //Boton para volver a la vista principal
    private ListView lvAfiliaciiones; //Mostrará la lista de afiliaciones del socio
    private ArrayList<String> codigos;
    private ProgressDialog progressDialog ;
    private String respuesta;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidadesde_emergencia);
        context = UnidadesdeEmergencia.this;
        respuesta = getIntent().getStringExtra("respuesta");

        enlaces();
        cargarDatos(respuesta);
        onclicks();
    }
    private void onclicks() {

        //Al presionar al textview regresa a la vista principal
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Notificaciones.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        lvAfiliaciiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog =  new ProgressDialog(context);
                progressDialog.setMessage("Validando...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                consultar(codigos.get(position));
            }
        });
    }

    private void consultar(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/getGPS.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")){
                        Intent intent = new Intent(context,MapsActivity.class);
                        String coordenadas = jsonObject.getString("coordenadas");
                        intent.putExtra("respuesta",coordenadas);
                        intent.putExtra("response",respuesta);
                        intent.putExtra("vista","ue");
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    }else if(status.equals("noData")){
                        Toast.makeText(context,"No hay datos a mostrar...",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context,"Error en la red...",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigo", s);//Dato a enviar donde "codigoFijo" es la llave y codigoFijo es el valor
                return params;
            }
        };
        //Definiendo parametros de la conexión con un tiempo determindo de espera para que el servidor responda
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Añadiendo el request a la cola de peticiones al servidor
        MySingleton.getInstance(context).addToRequest(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    //Se cargan los datos del socio para mostrar en la vista activity_consulta_facturas
    private void cargarDatos(String respuesta) {
        codigos = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> imgs = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            JSONArray jsonArray = jsonObject.getJSONArray("unidades");
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONArray fila = jsonArray.getJSONArray(i);
                codigos.add(fila.getString(0));
                names.add(fila.getString(1));
                imgs.add(R.mipmap.casita);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyAdapterUnidades myAdapterUnidades = new MyAdapterUnidades(this, names,imgs);
        lvAfiliaciiones.setAdapter(myAdapterUnidades);
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolverlistaunidades);
        lvAfiliaciiones = findViewById(R.id.listaunidadesdeemergencia);
    }

    @Override
    //Cuando el usuario oprime hacia atras
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(context,ListaSociosUnidades.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out); //Le da la animación de desplazamiento lateral de la vista
    }
}