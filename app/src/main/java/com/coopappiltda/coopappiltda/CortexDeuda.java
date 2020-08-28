package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.ItemCorteAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CortexDeuda extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ArrayList<String> nombre_tareas;
    ArrayList<String> lista_de_codigos;
    AdminSQLiteOpenHelper dblectura;
    String id2="";
    String id3="";
    ArrayList<Work_Afiliado> tareasBD;
    private RecyclerView rvlistaCortex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cortexdeuda);

        //ListWorks = (ListView) findViewById(R.id.listaS);
        id2 = Objects.requireNonNull(getIntent().getExtras()).getString("CLAVE");  // aqui llega el nro de formulario de la ruta

        id3=getIntent().getExtras().getString("codigo");

        //Toast.makeText(getApplicationContext(),"Afiliados",Toast.LENGTH_SHORT).show();
        dblectura=new AdminSQLiteOpenHelper(this,"dbReader",null,1);
        tareasBD = new ArrayList<>();
        nombre_tareas = new ArrayList<>();   // para que muestre en la vista
        lista_de_codigos = new ArrayList<>();  // para que saque el codigo y lo envie
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombre_tareas);
        //ListView ListWorks;
        ImageView ivVolvercortex = findViewById(R.id.ivVolvercortex);
        rvlistaCortex = findViewById(R.id.rvlistaCortex);
        ivVolvercortex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CortexDeuda.this, ListSociosCortePorDeuda.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        cargarDatos(id2);
    }
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(CortexDeuda.this, ListSociosCortePorDeuda.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }

    public ArrayList<Work_Afiliado> obtenDatos(){
        Cursor datos=dblectura.getCargaCortexDeuda();
        ArrayList<Work_Afiliado> usr = new ArrayList<>();
        int u1;
        String u2;
        String u3;
        if (datos.getCount()>0) {
            while (datos.moveToNext()) {
                u1 = datos.getInt(datos.getColumnIndex("id_corte"));
                u2 = datos.getString(datos.getColumnIndex("fecha"));
                u3 = "";
                Work_Afiliado work = new Work_Afiliado(u1, u2, u3);
                usr.add(work);
            }
        }
        return usr;
    }
    private void cargarDatos(String respuesta){
        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            final String status = jsonObject.getString("status");
            if (status.equals("200")) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(CortexDeuda.this, "dbReader", null, 1);
                SQLiteDatabase BaseDeDato = admin.getWritableDatabase();
                JSONObject datosArrayClientes = jsonObject.getJSONObject("corte");
                ContentValues Registros1 = new ContentValues();
                Registros1.put("id_socio", Integer.parseInt(id3));
                Registros1.put("id_corte", datosArrayClientes.getInt("id"));
                int id_cor=datosArrayClientes.getInt("id");
                Registros1.put("fecha", datosArrayClientes.getString("fecha"));
                AdminSQLiteOpenHelper dbs = new AdminSQLiteOpenHelper(CortexDeuda.this, "dbReader", null, 1);
                SQLiteDatabase sdq = dbs.getReadableDatabase();
                @SuppressLint("Recycle") Cursor cursor = sdq.rawQuery("select * from cortexdeuda where id_socio=" + id3 + " and id_corte=" + id_cor, null);
                if (cursor.getCount() == 0) {
                    BaseDeDato.insert("cortexdeuda", null, Registros1);
                }
                BaseDeDato.close();
                tareasBD = obtenDatos();
                llenarVista();
                ItemCorteAdapter itemCorteAdapter  = new ItemCorteAdapter(nombre_tareas);
                rvlistaCortex.setLayoutManager(new LinearLayoutManager(CortexDeuda.this));
                rvlistaCortex.setAdapter(itemCorteAdapter);
            }
        } catch (JSONException e) {
           Toast.makeText(CortexDeuda.this,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
           e.printStackTrace();
        }
    }
    public void llenarVista(){
        for (int i=0; i < tareasBD.size(); i++){  // tareas.size devuelve el tamaño del arraylist
            nombre_tareas.add("Id_corte: "+tareasBD.get(i).getId()+" Fecha de orden de corte: "+tareasBD.get(i).getNombre());
        }
    }
}
