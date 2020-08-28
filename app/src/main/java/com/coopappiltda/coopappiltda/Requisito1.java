package com.coopappiltda.coopappiltda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coopappiltda.clases.Imagen;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class Requisito1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisito1);
        ImageView imageBack2 = findViewById(R.id.imageBack2);
        SubsamplingScaleImageView imageView = findViewById(R.id.imgZoom);

        TextView textView = findViewById(R.id.tituloRequisito);
        imageView.setImage(ImageSource.bitmap(Imagen.img));
        textView.setText(Imagen.titulo);

        imageBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Requisito1.this,Requisitos.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Requisito1.this,Requisitos.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }

}