package com.coopappiltda.clases;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.coopappiltda.coopappiltda.R;

import java.util.Objects;

public class CuadroDialogo {
    public interface finalizado{
        void resultado(String codigoFijo,String codUbicacion,String alias);
    }
    private finalizado interfaz;
    public CuadroDialogo(Context context, finalizado actividad){
        interfaz = actividad;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cuadrodialogo);

        final EditText codigoFijo = dialog.findViewById(R.id.etCodigoFijo);
        final EditText codUbicacion = dialog.findViewById(R.id.etUbicacion);
        final EditText editText2 = dialog.findViewById(R.id.editText2);
        Button aceptar = dialog.findViewById(R.id.btnDialogAceptar);
        Button cancelar = dialog.findViewById(R.id.btnDialogCancelar);

        codigoFijo.setInputType(InputType.TYPE_CLASS_NUMBER);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
               v.startAnimation(buttonClick);
                interfaz.resultado(codigoFijo.getText().toString(), codUbicacion.getText().toString(), editText2.getText().toString());
                dialog.dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public CuadroDialogo(Context context, finalizado actividad,String codigo){
        interfaz = actividad;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cuadrodialogo);

        final EditText codigoFijo = dialog.findViewById(R.id.etCodigoFijo);
        final EditText codUbicacion = dialog.findViewById(R.id.etUbicacion);
        final EditText editText2 = dialog.findViewById(R.id.editText2);

        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(context,"dbReader",null,1);
        SQLiteDatabase db = adm.getReadableDatabase();
        String[] campos = new String[]{"id_socio","nombre","alias"};
        Cursor cursor = db.query("afiliacion",campos,"id_socio="+codigo,null,null,null,null);
        if (cursor.moveToFirst()){
            codigoFijo.setText(cursor.getString(0));
            codUbicacion.setText(cursor.getString(1));
            editText2.setText(cursor.getString(2));
        }
        codigoFijo.setEnabled(false);
        codUbicacion.setEnabled(false);
        Button aceptar = dialog.findViewById(R.id.btnDialogAceptar);
        Button cancelar = dialog.findViewById(R.id.btnDialogCancelar);

        codigoFijo.setInputType(InputType.TYPE_CLASS_NUMBER);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);
                interfaz.resultado(codigoFijo.getText().toString(), codUbicacion.getText().toString(), editText2.getText().toString());
                dialog.dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
