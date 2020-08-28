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

public class ItemFacturaAdapter extends RecyclerView.Adapter<ItemFacturaAdapter.ViewHolderFacturas> {
    private ArrayList<ItemFactura> itemFacturas;

    public ItemFacturaAdapter(ArrayList<ItemFactura> itemFacturas) {
        this.itemFacturas = itemFacturas;
    }


    @NonNull
    @Override
    public ViewHolderFacturas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfacturas,null,false);
        return new ViewHolderFacturas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFacturas holder, int position) {
        holder.mesfactura.setText(itemFacturas.get(position).getMes());
        holder.consumofactura.setText(itemFacturas.get(position).getConsumo());
        holder.montofactura.setText(itemFacturas.get(position).getMonto());
        holder.estadofactura.setText(itemFacturas.get(position).getEstado());

    }

    @Override
    public int getItemCount() {
        return itemFacturas.size();
    }

    public static class ViewHolderFacturas extends RecyclerView.ViewHolder {
        TextView mesfactura, consumofactura, montofactura, estadofactura;
        public ViewHolderFacturas(@NonNull View itemView) {
            super(itemView);
            mesfactura = itemView.findViewById(R.id.mesfactura);
            consumofactura = itemView.findViewById(R.id.consumofactura);
            montofactura = itemView.findViewById(R.id.montofactura);
            estadofactura = itemView.findViewById(R.id.estadofactura);
        }

    }
}
