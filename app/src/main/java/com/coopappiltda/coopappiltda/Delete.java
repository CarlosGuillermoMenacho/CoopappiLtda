package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.coopappiltda.clases.MyAdapterItemAfiliacion;

import java.util.ArrayList;

public class Delete extends AppCompatActivity {
    private ArrayList<String> codigos; //Array que contendrá la lista de codigos de socio será usado para controlar el onclicitem
    private ImageView ivBack1; //Boton para volver a la vista principal
    private ListView lvAfiliaciiones; //Mostrará la lista de afiliaciones del socio
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        context = Delete.this;
        enlaces();
        cargarDatos();
        onclicks();
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolver2);
        lvAfiliaciiones = findViewById(R.id.lveliminar);
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
    private void onclicks() {
        //Al presionar al textview regresa a la vista principal
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ConsultaFacturas.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        lvAfiliaciiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea eliminar este registro?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(codigos.get(position));
                    }
                });
                builder.setNegativeButton("NO",null);
                builder.show();

            }
        });

    }

    private void delete(String s) {
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase db = adm.getWritableDatabase();
        db.execSQL("delete from afiliacion where id_socio="+s);
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