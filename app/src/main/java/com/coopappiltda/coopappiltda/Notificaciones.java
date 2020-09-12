package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.MyAdapterListView;

import java.util.ArrayList;

public class Notificaciones extends AppCompatActivity {
    private ImageView ivVolverNoti;
    private Context context;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        context = getApplicationContext();
        enlaces();
        llenaarListView();
        onclikc();
    }

    private void llenaarListView() {
        //Agregando las opciones al array para pasarlo a la ListView
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();

        names.add("Aviso de cobranza");
        names.add("Mantenimiento Preventivo");
        names.add("Corte por deuda");
        names.add("Facturación");
        names.add("Relaciones Públicas");
        names.add("Servicio Técnico");
        names.add("Nueva Conexión");
        names.add("Asistencia Social Cooperativa");
        names.add("Unidades de Emergencia");

        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        images.add(R.drawable.gotits);
        //Creando un adapter con todos los item
        MyAdapterListView myAdapterListView = new MyAdapterListView(this, names,images);
        //Indicando que el ListView utilizará el adapter
        listView.setAdapter(myAdapterListView);
    }

    private void onclikc() {
        ivVolverNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onclicItem(position);
            }
        });
    }

    private void onclicItem(int position) {
        switch (position){
            case 2:
                startActivity(new Intent(context,ListSociosCortePorDeuda.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 0:
                startActivity(new Intent(context,ListSociosAvisoCobranza.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 3:
                startActivity(new Intent(context,Facturacion.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 1:
                Constants.lista=1;
                startActivity(new Intent(context,ListSociosMantenimientoPrevent.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 4:
                Constants.lista=4;
                startActivity(new Intent(context,ListaSociosRelacionesPublicas.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 5:
                Constants.lista=5;
                startActivity(new Intent(context,ListaSociosServicioTecnico.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 6:
                Constants.lista=6;
                startActivity(new Intent(context,ListaSociosNuevaConexion.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 7:
                Constants.lista=7;
                startActivity(new Intent(context,ListaSociosAsistenciaSocioalCoop.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 8:
                Constants.lista=8;
                startActivity(new Intent(context,ListaSociosUnidades.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
        }
    }

    @SuppressLint("WrongViewCast")
    private void enlaces() {
        ivVolverNoti = findViewById(R.id.ivVolverNoti);
        listView = findViewById(R.id.lvNotific);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,MainActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}