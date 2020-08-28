package com.coopappiltda.coopappiltda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Requisito3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisito3);
        ImageView imageBack2 = findViewById(R.id.imageBack4);

        imageBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Requisito3.this,Requisitos.class));
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Requisito3.this,Requisitos.class));
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}