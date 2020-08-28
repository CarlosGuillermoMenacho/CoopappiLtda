package com.coopappiltda.clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coopappiltda.coopappiltda.R;

import java.util.ArrayList;

public class MyAdapterListView2 extends BaseAdapter {
    private Context context;
    private ArrayList<String> names;
    private ArrayList<Integer> images;

    public MyAdapterListView2(Context context, ArrayList<String> names, ArrayList<Integer> images) {
        this.context = context;
        this.names = names;
        this.images = images;
    }
    @Override
    public int getCount() {
        return this.names.size();
    }

    @Override
    public Object getItem(int position) {
        return this.names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        v = inflater.inflate(R.layout.itemlistview2,null);
        String currentName = names.get(position);
        int currentImage = images.get(position);

        TextView textView = v.findViewById(R.id.textitem2);
        ImageView imageView = v.findViewById(R.id.imageitem2);
        imageView.setImageResource(currentImage);
        textView.setText(currentName);
        return v;
    }
}
