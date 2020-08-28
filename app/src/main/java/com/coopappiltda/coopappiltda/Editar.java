package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.CuadroDialogo;
import com.coopappiltda.clases.MyAdapterItemAfiliacion;

import java.util.ArrayList;

public class Editar extends AppCompatActivity implements CuadroDialogo.finalizado {
    private ImageView ivBack1; //Boton para volver a la vista principal
    private ListView lvEditar; //Mostrará la lista de afiliaciones del socio
    private ArrayList<String> codigos;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        context = Editar.this;
        enlaces();
        cargarDatos();
        onclicks();
    }
    private void onclicks() {
        //Al presionar al textview regresa a la vista principal
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ConsultaFacturas.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        lvEditar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editar(codigos.get(position));
            }
        });

    }

    private void editar(String s) {
        new CuadroDialogo(context,Editar.this,s);
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
        lvEditar.setAdapter(myAdapterItemAfiliacion);
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolver3);
        lvEditar = findViewById(R.id.lveditar);

    }

    @Override
    public void resultado(String codigoFijo, String codUbicacion, String alias) {
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase db = adm.getWritableDatabase();
        db.execSQL("update afiliacion set alias='"+alias+"' where id_socio="+codigoFijo);
        db.close();
        cargarDatos();
    }
    @Override
    //Cuando el usuario oprime hacia atras
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ConsultaFacturas.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out); //Le da la animación de desplazamiento lateral de la vista
    }
}