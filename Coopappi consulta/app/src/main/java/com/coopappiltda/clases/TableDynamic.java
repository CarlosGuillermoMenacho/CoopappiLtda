package com.coopappiltda.clases;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDynamic {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]>data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout=tableLayout;
        this.context=context;
    }

    public void addHeader(String[] header){
        this.header=header;
        createHeader();
    }
    public void addData(ArrayList<String[]>data){
        this.data=data;
        createDataTable();
    }


    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(14);
    }
    private void createHeader(){
        indexC=0;
        newRow();
        while (indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }
    @SuppressLint("SetTextI18n")
    private void createDataTable(){
        String info;
        int indexR;
        for (indexR = 1; indexR <= data.size(); indexR++ ){
            newRow();
            String[] columns=data.get(indexR - 1);
            for (indexC = 0; indexC<header.length; indexC++){

                info = (indexC < columns.length)?columns[indexC]:"";

                    newCell();
                    txtCell.setText(info);
                    tableRow.addView(txtCell,newTableRowParams());

            }
            tableLayout.addView(tableRow);
        }
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight = 1;
        return params;
    }

}
