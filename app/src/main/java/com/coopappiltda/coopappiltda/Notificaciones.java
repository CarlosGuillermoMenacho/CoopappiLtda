package com.coopappiltda.coopappiltda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
        names.add("Corte por deuda");
        names.add("Aviso de cobranza");
        names.add("Facturación");

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
            case 0:
                startActivity(new Intent(context,ListSociosCortePorDeuda.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 1:
                startActivity(new Intent(context,ListSociosAvisoCobranza.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;
            case 2:
                startActivity(new Intent(context,Facturacion.class));
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