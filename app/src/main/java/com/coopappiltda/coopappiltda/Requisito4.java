package com.coopappiltda.coopappiltda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Requisito4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisito4);
        ImageView imageBack2 = findViewById(R.id.imageBack5);

        imageBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Requisito4.this,Requisitos.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Requisito4.this,Requisitos.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}