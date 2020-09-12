package com.coopappiltda.coopappiltda;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.ItemFacturacionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MantenimientoPreventivo extends AppCompatActivity {
    private ImageView back;
    private Context context;
    private RecyclerView facturacion;
    private AdminSQLiteOpenHelper adm;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_preventivo);
        context = MantenimientoPreventivo.this;
        String respuesta = getIntent().getStringExtra("respuesta");
        user = getIntent().getStringExtra("user");
        adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        titulo();
        enlaces();
        onclikc();
        cargarDatos(respuesta);
    }

    private void titulo() {
        TextView tv = findViewById(R.id.tvmantpreven);
        switch (Constants.lista){
            case 1:
                tv.setText("Mantenimiento Preventivo\nNotificaciones");
                break;
            case 4:
                tv.setText("Relaciones Públicas\nNotificaciones");
                break;
            case 5:
                tv.setText("Servicio Técnico\nNotificaciones");
                break;
            case 6:
                tv.setText("Nueva Conexión\nNotificaciones");
                break;
            case 7:
                tv.setText("Asistencia Social\nNotificaciones");
                break;
        }
    }
    private void guardarMensajes(String respuesta){
        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            JSONArray jsonArray = jsonObject.getJSONArray("notificaciones");
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONArray filas = jsonArray.getJSONArray(i);
                String codigo = filas.getString(0);
                String mensaje = filas.getString(1);
                Cursor cursor = adm.get_Notificacion(codigo,Integer.toString(Constants.lista),user);
                if (!cursor.moveToFirst()){
                    adm.saveNotificacion(codigo,Integer.toString(Constants.lista),user,mensaje);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos(String respuesta) {
        guardarMensajes(respuesta);
        Cursor cursor = adm.getNotificaciones(user,Integer.toString(Constants.lista));
        ArrayList<String> itemFacturacion = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                itemFacturacion.add(cursor.getString(3));
            }while (cursor.moveToNext());
        }
        ItemFacturacionAdapter itemFacturacionAdapter = new ItemFacturacionAdapter(itemFacturacion);
        facturacion.setLayoutManager(new LinearLayoutManager(context));
        facturacion.setAdapter(itemFacturacionAdapter);
    }
    private void onclikc() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Constants.lista){
                    case 1:
                        startActivity(new Intent(context,ListSociosMantenimientoPrevent.class));
                        overridePendingTransition(R.anim.right_in,R.anim.right_out);
                        break;
                    case 4:
                        startActivity(new Intent(context,ListaSociosRelacionesPublicas.class));
                        overridePendingTransition(R.anim.right_in,R.anim.right_out);
                        break;
                    case 5:
                        startActivity(new Intent(context,ListaSociosServicioTecnico.class));
                        overridePendingTransition(R.anim.right_in,R.anim.right_out);
                        break;
                    case 6:
                        startActivity(new Intent(context,ListaSociosNuevaConexion.class));
                        overridePendingTransition(R.anim.right_in,R.anim.right_out);
                        break;
                    case 7:
                        startActivity(new Intent(context,ListaSociosAsistenciaSocioalCoop.class));
                        overridePendingTransition(R.anim.right_in,R.anim.right_out);
                        break;
                }

            }
        });
    }

    private void enlaces() {
        back = findViewById(R.id.ivVolverlistNotificaciones);
        facturacion = findViewById(R.id.rvnotificaciones);
    }
    @Override
    public void onBackPressed() {
        switch (Constants.lista){
            case 1:
                startActivity(new Intent(context,ListSociosMantenimientoPrevent.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
                break;
            case 4:
                startActivity(new Intent(context,ListaSociosRelacionesPublicas.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
                break;
            case 5:
                startActivity(new Intent(context,ListaSociosServicioTecnico.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
                break;
            case 6:
                startActivity(new Intent(context,ListaSociosNuevaConexion.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
                break;
            case 7:
                startActivity(new Intent(context,ListaSociosAsistenciaSocioalCoop.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
                break;
        }
    }
}