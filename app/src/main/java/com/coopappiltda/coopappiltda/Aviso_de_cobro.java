package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.ItemCorteAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Aviso_de_cobro extends AppCompatActivity {
    ListView ListWorks;
    private RecyclerView rvavisocobro;
    AlertDialog.Builder builder;
    ArrayAdapter<String> adapter;
    ArrayList<String> nombre_tareas;
    ArrayList<String> lista_de_codigos;
    AdminSQLiteOpenHelper dblectura;
    String id2="";
    String id3="";
    ArrayList<Work_Aviso> tareasBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_de_cobro);

        ListWorks = findViewById(R.id.listaS);
        id2 = Objects.requireNonNull(getIntent().getExtras()).getString("json");  // aqui llega el nro de formulario de la ruta
        id3 = getIntent().getExtras().getString("codigo");
        builder = new AlertDialog.Builder(Aviso_de_cobro.this);

        dblectura=new AdminSQLiteOpenHelper(this,"dbReader",null,1);
        tareasBD = new ArrayList<>();
        nombre_tareas = new ArrayList<>();   // para que muestre en la vista
        lista_de_codigos = new ArrayList<>();  // para que saque el codigo y lo envie
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombre_tareas);
        rvavisocobro = findViewById(R.id.rvavisocobro);
        ImageView ivVolvercobro = findViewById(R.id.ivVolvercobro);
        ivVolvercobro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Aviso_de_cobro.this,ListSociosAvisoCobranza.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });

        cargarDatos(id2);
    }
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ListSociosAvisoCobranza.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
    private void cargarDatos(String respuesta){
        try {

            JSONObject jsonObject = new JSONObject(respuesta);
            final String status = jsonObject.getString("status");
            if (status.equals("200")) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(Aviso_de_cobro.this, "dbReader", null, 1);
                SQLiteDatabase BaseDeDato = admin.getWritableDatabase();

                JSONObject datosArrayClientes = jsonObject.getJSONObject("aviso");
                ContentValues Registros1 = new ContentValues();
                Registros1.put("id_factura", datosArrayClientes.getInt("id_factura"));
                int id_fac=datosArrayClientes.getInt("id_factura");
                Registros1.put("id_socio", datosArrayClientes.getInt("id_socio"));
                Registros1.put("cobro", datosArrayClientes.getString("cobro"));
                Registros1.put("mto_total", datosArrayClientes.getDouble("mto_total"));
                Registros1.put("f_emision", datosArrayClientes.getString("f_emision"));

                AdminSQLiteOpenHelper dbs = new AdminSQLiteOpenHelper(Aviso_de_cobro.this, "dbReader", null, 1);
                SQLiteDatabase sdq = dbs.getReadableDatabase();
                @SuppressLint("Recycle") Cursor cursor = sdq.rawQuery("select * from aviso_de_cobro where id_socio=" + id3 + " and id_factura=" + id_fac, null);
                if (cursor.getCount() == 0) {
                    BaseDeDato.insert("aviso_de_cobro", null, Registros1);
                }
                BaseDeDato.close();

                tareasBD = obtenDatos();
                llenarVista();
                ItemCorteAdapter itemCorteAdapter = new ItemCorteAdapter(nombre_tareas);
                rvavisocobro.setLayoutManager(new LinearLayoutManager(Aviso_de_cobro.this));
                rvavisocobro.setAdapter(itemCorteAdapter);
               // ListWorks.setAdapter(adapter);

            }
        } catch (JSONException e) {
            Toast.makeText(Aviso_de_cobro.this,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
            Log.e("CortexDeuda", e.toString());
            e.printStackTrace();
        }
    }
    public ArrayList<Work_Aviso> obtenDatos(){

        Cursor datos=dblectura.getCarga_aviso_de_cobro();

        ArrayList<Work_Aviso> usr = new ArrayList<>();
        float u2;
        String u1,u3;
        if (datos.getCount()>0) {
            while (datos.moveToNext()) {
                u1 = datos.getString(datos.getColumnIndex("cobro"));
                u2 = datos.getFloat(datos.getColumnIndex("mto_total"));
                u3 = datos.getString(datos.getColumnIndex("f_emision"));
                Work_Aviso work = new Work_Aviso(u1, u2, u3);
                usr.add(work);
            }
            //Toast.makeText(this,"DATOS CARGADOS",Toast.LENGTH_SHORT).show();
        }
        return usr;
    }
    
    public void llenarVista(){
        for (int i=0; i < tareasBD.size(); i++){  // tareas.size devuelve el tamaño del arraylist
            nombre_tareas.add("La factura del mes: "+tareasBD.get(i).getCobro()+" por el monto de: "+tareasBD.get(i).getMonto()+" emitido el: "+tareasBD.get(i).getFecha()+" está disponible para su pago.");
        }
    }

}
