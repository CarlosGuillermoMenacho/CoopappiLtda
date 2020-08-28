package com.coopappiltda.clases;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coopappiltda.coopappiltda.R;

import java.util.ArrayList;

public class ItemFacturacionAdapter extends RecyclerView.Adapter<ItemFacturacionAdapter.ViewHolderFacturas> {
    private ArrayList<String> itemFacturas;

    public ItemFacturacionAdapter(ArrayList<String> itemFacturas) {
        this.itemFacturas = itemFacturas;
    }


    @NonNull
    @Override
    public ViewHolderFacturas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfacturacion,null,false);
        return new ViewHolderFacturas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFacturas holder, int position) {
        holder.mensaje.setText(itemFacturas.get(position));

    }

    @Override
    public int getItemCount() {
        return itemFacturas.size();
    }

    public static class ViewHolderFacturas extends RecyclerView.ViewHolder {
        TextView mensaje;
        public ViewHolderFacturas(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.mensajeFacturacion);

        }

    }
}
