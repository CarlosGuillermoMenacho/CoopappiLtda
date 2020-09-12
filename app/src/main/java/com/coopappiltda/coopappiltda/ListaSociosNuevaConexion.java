package com.coopappiltda.coopappiltda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.MyAdapterItemAfiliacion;
import com.coopappiltda.clases.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaSociosNuevaConexion extends AppCompatActivity {
    private ImageView ivBack1; //Boton para volver a la vista principal
    private ListView lvAfiliaciones; //Mostrará la lista de afiliaciones del socio
    private ArrayList<String> codigos;
    private ProgressDialog progressDialog ;
    private AdminSQLiteOpenHelper adm;
    private String codMensaje;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_socios_nueva_conexion);
        context = ListaSociosNuevaConexion.this;
        adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        enlaces();
        cargarDatos();
        onclicks();
    }
    private void obtenerUltimaNotificacion(String s) {
        Cursor cursor = adm.getNotificaciones(s,Integer.toString(Constants.lista));
        if (cursor.moveToFirst()){
            codMensaje = cursor.getString(0);
        }
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
        lvAfiliaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        obtenerUltimaNotificacion(s);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/get_nuevaconexion.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")){
                        Intent intent = new Intent(context,MantenimientoPreventivo.class);
                        intent.putExtra("respuesta",response);
                        intent.putExtra("user",s);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    }else{
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
                params.put("cod_notif",codMensaje);
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
    private void cargarDatos() {
        codigos = new ArrayList<>();
        ArrayList<String[]> names = new ArrayList<>();
        ArrayList<Integer> imgs = new ArrayList<>();

        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase db = adm.getReadableDatabase();

        String[] campos = new String[]{"id_socio","nombre","alias"};
        @SuppressLint("Recycle") Cursor cursor = db.query("afiliacion",campos,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                names.add(new String[]{cursor.getString(2),cursor.getString(0),cursor.getString(1)});
                codigos.add(cursor.getString(0));
                imgs.add(R.mipmap.casita);
            }while (cursor.moveToNext());
        }
        db.close();


        MyAdapterItemAfiliacion myAdapterItemAfiliacion = new MyAdapterItemAfiliacion(this, names,imgs);
        lvAfiliaciones.setAdapter(myAdapterItemAfiliacion);
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolvernuevaconexion);
        lvAfiliaciones = findViewById(R.id.listanuevaconexion);
    }

    @Override
    //Cuando el usuario oprime hacia atras
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(context,Notificaciones.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out); //Le da la animación de desplazamiento lateral de la vista
    }

}