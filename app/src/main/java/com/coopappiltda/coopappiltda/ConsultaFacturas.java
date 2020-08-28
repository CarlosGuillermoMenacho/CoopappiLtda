package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.coopappiltda.clases.CuadroDialogo;
import com.coopappiltda.clases.MyAdapterItemAfiliacion;
import com.coopappiltda.clases.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ConsultaFacturas extends AppCompatActivity implements CuadroDialogo.finalizado {
    private ImageView ivBack1; //Boton para volver a la vista principal
    private ListView lvAfiliaciiones; //Mostrará la lista de afiliaciones del socio
    private Button btnAgregar;
    private Button btnBorrar;
    private Button btnEditar;
    private ArrayList<String> codigos;
    private ProgressDialog progressDialog ;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_facturas);
        context = ConsultaFacturas.this;
        enlaces();
        cargarDatos();
        onclicks();
    }

    private void onclicks() {
        //Al presionar el boton agregar
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                btnAgregar.startAnimation(buttonClick);
                new CuadroDialogo(context,ConsultaFacturas.this); //Mostrar el formulario de afiliacion
            }
        });
        //Cuando se oprime el boton borrar
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Delete.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Editar.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
        //Al presionar al textview regresa a la vista principal
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/getSocioHistorial.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")){
                        Intent intent = new Intent(context,historial.class);
                        intent.putExtra("json",response);
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
        lvAfiliaciiones.setAdapter(myAdapterItemAfiliacion);
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolver);
        lvAfiliaciiones = findViewById(R.id.lvAfiliaciones);
        btnAgregar = findViewById(R.id.btnNuevo);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnEditar = findViewById(R.id.btnEditar);
    }

    @Override
    //Cuando el usuario oprime hacia atras
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out); //Le da la animación de desplazamiento lateral de la vista
    }

    @Override
    public void resultado(final String codigoFijo, final String codUbicacion,final String alias) {
        if (!codigoFijo.isEmpty() && !codUbicacion.isEmpty() && !alias.isEmpty()) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Validando...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL + "/afiliacion.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss(); //Cerrando el mensaje de progreso de la consulta
                    //Obteniendo la respuesta en formato JSON
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")) {
                            String NameSocio = jsonObject.getString("nombre");
                            AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context, "dbReader", null, 1);
                            SQLiteDatabase db = adm.getWritableDatabase();
                            db.execSQL("insert into afiliacion values(" + codigoFijo + ",'" + NameSocio + "','" + alias + "')");
                            db.close();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Se ha validado correctamente...");
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cargarDatos();
                                }
                            });
                            builder.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Los datos ingresados son incorrectos...\nPara mayor información contactenos al Telf. 3847771");
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cargarDatos();
                                }
                            });
                            builder.show();
                        }
                    } catch (JSONException e) { //Si la respuesta del servidor no es un objeto JSON
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Error al procesar los datos...");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context,"Error en la Red...",Toast.LENGTH_LONG).show();
                }
            }) {
                //Aquí se envian los datos al servidor mediante POST
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("codigoFijo", codigoFijo);//Dato a enviar donde "codigoFijo" es la llave y codigoFijo es el valor
                    params.put("cedula", codUbicacion);
                    return params;
                }
            };
            //Definiendo parametros de la conexión con un tiempo determindo de espera para que el servidor responda
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Añadiendo el request a la cola de peticiones al servidor
            MySingleton.getInstance(context).addToRequest(stringRequest);
        }else{
            //Cuando el usuario no ha llenado todo el formulario de afiliacion
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Debe llenar todos los datos...");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new CuadroDialogo(context,ConsultaFacturas.this);
                }
            });
            builder.show();
        }
    }
}