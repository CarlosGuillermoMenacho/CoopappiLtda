package com.coopappiltda.coopappiltda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coopappiltda.clases.Socio;
import com.coopappiltda.clases.TableDynamic;

import java.util.ArrayList;


public class ConsultaFacturas extends AppCompatActivity {
    private ImageView ivBack1; //Boton para volver a la vista principal
    private TableLayout table; //Mostrará el historial de facturas del socio
    //Mostraran los datos del socio
    private TextView tvNombreSocio, tvCodigoFijo, tvTotalMonto, tvTotalFacturaImpaga, tvfechaCorte, tvCodUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_facturas);
        enlaces();
        cargarDatos();
        //Al presionar al textview regresa a la vista principal
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    //Se cargan los datos del socio para mostrar en la vista activity_consulta_facturas
    private void cargarDatos() {

        Socio socio = new Socio(); //Declarando una variable de tipo Socio
        socio.obtenerSocio(this); //Se cargan desde la base de datos los datos del socio en los atributos de socio

        //Asignando los datos a cada uno de los elementos de la vista
        tvNombreSocio.setText(socio.getNombre());
        tvCodigoFijo.setText(Integer.toString(socio.getCodigo()));
        tvTotalMonto.setText(socio.getDeuda());
        tvTotalFacturaImpaga.setText(Integer.toString(socio.getFacturasImpagas()));
        tvfechaCorte.setText(socio.getFechacorte());
        tvCodUbicacion.setText(socio.getCodUbicacion());

        //Declarando un array de arrays de cadenas para obtener el historial de facturas del socio
        ArrayList<String[]> historial = new ArrayList<>(socio.getFacturas());
        //Preparando la tabla para mostrar el historial
        final TableDynamic tableDynamic = new TableDynamic(table,this);
        //Agregando el encabezado
        String[] header = new String[]{"No.","Fecha","Consumo","Monto","Fecha/Pago"};
        tableDynamic.addHeader(header);
        //Agregando el historial
        tableDynamic.addData(historial);
    }
    //Aquí se hace el cast a cada uno de los elementos de la vista
    private void enlaces() {
        ivBack1 = findViewById(R.id.ivVolver);
        table = findViewById(R.id.table);
        tvNombreSocio = findViewById(R.id.tvNombreSocio);
        tvCodigoFijo = findViewById(R.id.tvCodigoFijo);
        tvfechaCorte = findViewById(R.id.tvfechaCorte);
        tvTotalFacturaImpaga = findViewById(R.id.tvTotalFacturaImpaga);
        tvTotalMonto = findViewById(R.id.tvTotalMonto);
        tvCodUbicacion = findViewById(R.id.tvCodUbicacion);
    }

    @Override
    //Cuando el usuario oprime hacia atras
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out); //Le da la animación de desplazamiento lateral de la vista
    }
}