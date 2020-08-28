package com.coopappiltda.coopappiltda;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.clases.ItemFacturacionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaFacturacion extends AppCompatActivity {
    private ImageView back;
    private Context context;
    private RecyclerView facturacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_facturacion);
        context = ListaFacturacion.this;
        String respuesta = getIntent().getStringExtra("respuesta");
        enlaces();
        onclikc();
        cargarDatos(respuesta);
    }

    private void cargarDatos(String respuesta) {
        ArrayList<String> itemFacturacion = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            String status = jsonObject.getString("status");
            if (status.equals("ok")){
                JSONArray jsonArray = jsonObject.getJSONArray("historial");
                for (int i = 0 ; i < jsonArray.length() ; i++){
                    JSONArray filas = jsonArray.getJSONArray(i);
                    itemFacturacion.add("Estimado cliente la factura correspondiente al mes de "+mes(filas.getInt(1))+" de "+filas.getString(0)+" ha sido cancelada en fecha "+filas.getString(2)+" a horas "+filas.getString(4)+ ".\nCuyo monto total es de bs. "+filas.getString(3));
                }
                ItemFacturacionAdapter itemFacturacionAdapter = new ItemFacturacionAdapter(itemFacturacion);
                facturacion.setLayoutManager(new LinearLayoutManager(context));
                facturacion.setAdapter(itemFacturacionAdapter);
            }else{
                Toast.makeText(context,jsonObject.getString("mensaje"),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private String mes(int mes){
        String[] meses = new String[]{"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
        return meses[mes-1];
    }
    private void onclikc() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Facturacion.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }

    private void enlaces() {
        back = findViewById(R.id.ivVolverlistFact);
        facturacion = findViewById(R.id.rvFacturasPagadas);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,Facturacion.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}