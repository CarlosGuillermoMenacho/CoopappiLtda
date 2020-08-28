package com.coopappiltda.clases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.coopappiltda.coopappiltda.R;

import java.util.Objects;

public class CuadroDialogo {
    public interface finalizado{
        void resultado(String codigoFijo,String codUbicacion);
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
        Button aceptar = dialog.findViewById(R.id.btnDialogAceptar);
        Button cancelar = dialog.findViewById(R.id.btnDialogCancelar);

        codigoFijo.setInputType(InputType.TYPE_CLASS_NUMBER);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.resultado(codigoFijo.getText().toString(),codUbicacion.getText().toString());
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
