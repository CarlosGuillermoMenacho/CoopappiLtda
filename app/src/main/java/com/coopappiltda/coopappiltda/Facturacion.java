package com.coopappiltda.coopappiltda;

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

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Facturacion extends AppCompatActivity {
    private ImageView back;
    private Context context;
    private ArrayList<String> codigos;
    private ProgressDialog progressDialog ;
    private ListView lvAfiliaciiones; //Mostrará la lista de afiliaciones del socio
    private String fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
        context = Facturacion.this;
        obtnerFecha();
        enlaces();
        onclikc();
        cargarDatos();
    }

    private void obtnerFecha() {
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context, "dbReader", null, 1);
        SQLiteDatabase db = adm.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select fecha from fecha", null);
        if (cursor.moveToFirst()) {
            fecha = cursor.getString(0);
        } else{
            Date c = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            fecha = df.format(c);
        }
    }

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
        lvAfiliaciiones.setAdapter(myAdapterItemAfiliacion);
    }

    private void onclikc() {
        back.setOnClickListener(new View.OnClickListener() {
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/facturacion.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")){
                        Intent intent = new Intent(context,ListaFacturacion.class);
                        intent.putExtra("respuesta",response);
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
                params.put("codigoFijo", s);//Dato a enviar donde "codigoFijo" es la llave y codigoFijo es el valor
                params.put("fecha",fecha);
                return params;
            }
        };
        //Definiendo parametros de la conexión con un tiempo determindo de espera para que el servidor responda
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Añadiendo el request a la cola de peticiones al servidor
        MySingleton.getInstance(context).addToRequest(stringRequest);
    }
    @SuppressLint("WrongViewCast")
    private void enlaces() {
        back = findViewById(R.id.ivVolverFacturacion);
        lvAfiliaciiones = findViewById(R.id.rvFacturacion);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,Notificaciones.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}