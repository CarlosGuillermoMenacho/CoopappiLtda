package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.clases.ItemFactura;
import com.coopappiltda.clases.ItemFacturaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class historial extends AppCompatActivity {
private String json;
private Context context;
private ImageView imgBack6;
private RecyclerView facturas;
private ScrollView scrooll;
    private TextView nombresocio, codigosocio, ubicscionsocio, direccionsocio, facturassocio, deudasocio, cortesocio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        json = getIntent().getStringExtra("json");
        context = getApplicationContext();

        enlaces();
        onclikc();
        cargarDatos();
        scrooll.setFocusableInTouchMode(true);
        scrooll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

    }


    private void cargarDatos() {
        ArrayList<ItemFactura> itemFacturas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")){
                nombresocio.setText(jsonObject.getString("nombre"));
                codigosocio.setText(jsonObject.getString("codigo"));
                ubicscionsocio.setText(jsonObject.getString("codUbicacion"));
                direccionsocio.setText(jsonObject.getString("direccion"));
                facturassocio.setText(jsonObject.getString("totalFacturaImpaga"));
                cortesocio.setText(jsonObject.getString("fechaCorte"));
                deudasocio.setText(jsonObject.getString("monto"));
                JSONArray jsonArray = jsonObject.getJSONArray("facturas");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONArray filas = jsonArray.getJSONArray(i);
                    String mes = filas.getString(0);
                    String consumo = filas.getString(1);
                    String monto = filas.getString(2);
                    String estado = "Impaga";
                    if (filas.getString(3).equals("2")){
                        estado = filas.getString(4);
                    }
                    itemFacturas.add(new ItemFactura(mes,consumo,monto,estado));
                }
                ItemFacturaAdapter itemFacturaAdapter = new ItemFacturaAdapter(itemFacturas);
                facturas.setLayoutManager(new LinearLayoutManager(context));
                facturas.setAdapter(itemFacturaAdapter);
            }
        } catch (JSONException e) {
            Toast.makeText(context,"Error al procesar los datos...",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private void onclikc() {
        imgBack6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ConsultaFacturas.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void enlaces() {
        facturas = findViewById(R.id.reciclerid);
        imgBack6 = findViewById(R.id.imgBack6);
        nombresocio = findViewById(R.id.nombresocio);
        codigosocio = findViewById(R.id.codigosocio);
        ubicscionsocio = findViewById(R.id.ubicscionsocio);
        direccionsocio =findViewById(R.id.direccionsocio);
        facturassocio = findViewById(R.id.facturassocio);
        deudasocio = findViewById(R.id.deudasocio);
        cortesocio = findViewById(R.id.cortesocio);
        scrooll = findViewById(R.id.scrooll);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,ConsultaFacturas.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}