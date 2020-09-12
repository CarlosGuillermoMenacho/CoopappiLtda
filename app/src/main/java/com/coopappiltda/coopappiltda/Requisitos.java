package com.coopappiltda.coopappiltda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.coopappiltda.clases.Constants;
import com.coopappiltda.clases.Imagen;
import com.coopappiltda.clases.MyAdapterListView2;
import com.coopappiltda.clases.MySingleton;

import java.util.ArrayList;

public class Requisitos extends AppCompatActivity {
private ListView lv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisitos);

        lv2 = findViewById(R.id.lv2);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);
            }
        });
        ImageView imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Requisitos.this,MainActivity.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
        cargarLista();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Requisitos.this,MainActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
    //Cuando se selecciona un item de la ListView
    private void itemSelected(int position) {
        String urlRequest;
        switch (position){
            case 0:
                urlRequest = "/requisitos/requisito1.jpeg";
                Imagen.titulo = "Descuento a personas mayores de 60 años\n(Ley 1886)";
                obtenerImagen(urlRequest);
                break;
            case 1:
                urlRequest = "/requisitos/requisito2.jpeg";
                Imagen.titulo = "Coopappi cerca de usted (Contacto)";
                obtenerImagen(urlRequest);
                break;
            case 2:
                urlRequest = "/requisitos/requisito3.jpeg";
                Imagen.titulo = "Transferencia del Certificado de Aportación\n(Por sucesión hereditaria)";
                obtenerImagen(urlRequest);
                break;
            case 3:
                urlRequest = "/requisitos/requisito4.jpeg";
                Imagen.titulo = "Transferencia del Certificado de Aportación\n(Empresa o instituciones)";
                obtenerImagen(urlRequest);
                break;
            case 4:
                urlRequest = "/requisitos/requisito5.jpeg";
                Imagen.titulo = "Transferencia del Certificado de Aportación\n(Persona natural)";
                obtenerImagen(urlRequest);
                break;
            case 5:
                urlRequest = "/requisitos/requisito6.jpeg";
                Imagen.titulo = "Control de Consumo Coopappi Móvil";
                obtenerImagen(urlRequest);
                break;
            case 6:
                urlRequest = "/requisitos/requisito7.jpeg";
                Imagen.titulo = "Conociendo tu factura";
                obtenerImagen(urlRequest);
                break;
            case 7:
                urlRequest = "/requisitos/requisito8.jpeg";
                Imagen.titulo = "Instalación de Agua Potable";
                obtenerImagen(urlRequest);
                break;
            case 8:
                urlRequest = "/requisitos/requisito9.jpeg";
                Imagen.titulo = "Baja Temporal";
                obtenerImagen(urlRequest);
                break;
            case 9:
                urlRequest = "/requisitos/requisito10.jpeg";
                Imagen.titulo = "Cambio de Nombre";
                obtenerImagen(urlRequest);
                break;
            case 10:
                urlRequest = "/requisitos/requisito11.jpeg";
                Imagen.titulo = "Beneficios de Cuota Mortuoria";
                obtenerImagen(urlRequest);
                break;
            case 11:
                urlRequest = "/requisitos/requisito12.jpeg";
                Imagen.titulo = "Rclamos a ODECO";
                obtenerImagen(urlRequest);
                break;
            case 12:
                urlRequest = "/requisitos/requisito13.jpeg";
                Imagen.titulo = "Reclamos de alcantarillado";
                obtenerImagen(urlRequest);
                break;
        }
    }

    private void obtenerImagen(String urlRequest) {
        final ProgressDialog progressDialog = new ProgressDialog(Requisitos.this);
        progressDialog.setMessage("Obteniendo requisitos...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ImageRequest imgRequest = new ImageRequest(Constants.SERVER_URL+urlRequest,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(Requisitos.this,Requisito1.class);
                        Imagen.img = response;
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Error en la red...",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        imgRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Añadiendo el request a la cola de peticiones al servidor
        MySingleton.getInstance(Requisitos.this).addToRequest(imgRequest);
    }

    private void cargarLista() {
        ArrayList<String> lv2Array = new ArrayList<>();
        ArrayList<Integer> imgs = new ArrayList<>();
        lv2Array.add("Descuento a personas mayores de 60 años\n(Ley 1886)");
        lv2Array.add("Coopappi cerca de usted (Contacto)");
        lv2Array.add("Transferencia del Certificado de Aportación\n(Por sucesión hereditaria)");
        lv2Array.add("Transferencia del Certificado de Aportación\n(Empresa o instituciones)");
        lv2Array.add("Transferencia del Certificado de Aportación\n(Persona natural)");
        lv2Array.add("Control de Consumo Coopappi Móvil");
        lv2Array.add("Conociendo tu factura");
        lv2Array.add("Instalación de Agua Potable");
        lv2Array.add("Baja Temporal");
        lv2Array.add("Cambio de Nombre");
        lv2Array.add("Beneficios de Cuota Mortuoria");
        lv2Array.add("Reclamos ODECO");
        lv2Array.add("Reclamo de Alcantarillado");

        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);
        imgs.add(R.drawable.gotits);

        //Creando un adapter con todos los item
        MyAdapterListView2 myAdapterListView2 = new MyAdapterListView2(this, lv2Array,imgs);
        //Indicando que el ListView utilizará el adapter
        lv2.setAdapter(myAdapterListView2);
    }
}