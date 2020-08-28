package com.coopappiltda.coopappiltda;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.coopappiltda.clases.AdminSQLiteOpenHelper;
import com.coopappiltda.clases.MyAdapterItemAfiliacion;

import java.util.ArrayList;

public class Tu_consumo extends AppCompatActivity {
    ListView ListWorks;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter3;
    ArrayList<String> nombre_tareas;
    ArrayList<String> lista_de_codigos;
    AdminSQLiteOpenHelper dblectura;

    ArrayList<Work_Afiliado> tareasBD;
 //   Map<String, Integer> mapaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_consumo);

        ListWorks = findViewById(R.id.listaS);

        //Toast.makeText(getApplicationContext(),"Afiliados",Toast.LENGTH_SHORT).show();
        dblectura=new AdminSQLiteOpenHelper(this,"dbReader",null,1);
        tareasBD = new ArrayList<>();
        tareasBD = obtenDatos();
        nombre_tareas = new ArrayList<>();   // para que muestre en la vista
        lista_de_codigos = new ArrayList<>();  // para que saque el codigo y lo envie
        //nombre_genfact = new ArrayList<String>();
   //     mapaTareas=new HashMap<String, Integer>();
        ImageView ivVolveraf = findViewById(R.id.ivVolveraf);
        ivVolveraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tu_consumo.this,MainActivity.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });

        llenarVista();
        MyAdapterItemAfiliacion myAdapterItemAfiliacion = new MyAdapterItemAfiliacion(Tu_consumo.this,obtenerArray(),obtenerImages());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombre_tareas);
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_de_codigos);
        ListWorks.setAdapter(myAdapterItemAfiliacion);
        ListWorks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre=adapter3.getItem(position);
                Intent intent = new Intent(Tu_consumo.this,Ver_tuconsumo.class);
                intent.putExtra("CLAVE",nombre);
                startActivity(intent);  // inicia el nuevo layout - pantalla en este caso VerRegistro
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });

    }

    private ArrayList<Integer> obtenerImages() {
        ArrayList<Integer>images = new ArrayList<>();
        for (int i = 0 ; i<tareasBD.size(); i++){
            images.add(R.mipmap.casita);
        }
        return images;
    }

    private ArrayList<String[]> obtenerArray() {
        ArrayList<String[]> lista =new ArrayList<>();
        for (int i=0; i<tareasBD.size();i++){
            lista.add(new String[]{tareasBD.get(i).getAlias(),Integer.toString(tareasBD.get(i).getId()),tareasBD.get(i).getNombre(),});
        }
        return lista;
    }


    public ArrayList<Work_Afiliado> obtenDatos(){
       // dblectura.grabaAfiliados(); // borrar esto luego

        Cursor datos=dblectura.getCargaAfiliados();

        ArrayList<Work_Afiliado> usr = new ArrayList<>();
        int u1;
        String u2;
        String u3;
        if (datos.getCount()>0) {
            while (datos.moveToNext()) {
                u1 = datos.getInt(datos.getColumnIndex("id_socio"));
                u2 = datos.getString(datos.getColumnIndex("nombre"));
                u3 = datos.getString(datos.getColumnIndex("alias"));
                Work_Afiliado work = new Work_Afiliado(u1, u2, u3);
                usr.add(work);
            }
            //Toast.makeText(this,"DATOS CARGADOS",Toast.LENGTH_SHORT).show();
        }//Toast.makeText(this,"No cargo datos",Toast.LENGTH_SHORT).show();

        return usr;
    }
    
    public void llenarVista(){
        for (int i=0; i < tareasBD.size(); i++){  // tareas.size devuelve el tamaño del arraylist
            nombre_tareas.add("Id: "+tareasBD.get(i).getId()+" Nombre: "+tareasBD.get(i).getNombre()+" Alias: "+tareasBD.get(i).getAlias());
            lista_de_codigos.add(tareasBD.get(i).getId()+"");
        }
    }
/*    public void llenarRuta(){
        String llave;
        int valor;
        for (int i=0; i<tareasBD.size(); i++){
//            llave = "No: "+tareasBD.get(i).getId()+" Nombre: "+tareasBD.get(i).getNombre()+" Alias: "+tareasBD.get(i).getAlias();
            llave = tareasBD.get(i).getNombre();
            valor = tareasBD.get(i).getId();
            mapaTareas.put(llave,valor);
        }
    }
*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}
