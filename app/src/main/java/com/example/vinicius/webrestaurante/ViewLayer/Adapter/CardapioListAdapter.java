package com.example.vinicius.webrestaurante.ViewLayer.Adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio;
import com.example.vinicius.webrestaurante.R;

import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_DESCRIPTION;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_PRICE;

public class CardapioListAdapter extends RecyclerView.Adapter<CardapioListAdapter.MyViewHolder> {
    private Cursor cursorCardapio;

    public CardapioListAdapter(Cursor cursorCardapio){
        this.cursorCardapio = cursorCardapio;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_cardapio_detalhe, viewGroup, false);
        return new CardapioListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(i==0){
            this.cursorCardapio.moveToFirst();
        }

        myViewHolder.IDName.setText(
                String.format("ID %s - %s", this.cursorCardapio.getString(
                        this.cursorCardapio.getColumnIndex(PostContractCardapio.PostEntry._ID)),
                        this.cursorCardapio.getString(
                                this.cursorCardapio.getColumnIndex(
                                        COLUMN_NAME))));
        myViewHolder.mealDescription.setText(String.format("Descrição: %s", this.cursorCardapio.getString(
                this.cursorCardapio.getColumnIndex(COLUMN_DESCRIPTION))));
        float valor = Float.valueOf(this.cursorCardapio.getString(this.cursorCardapio.getColumnIndex(COLUMN_PRICE)));
        myViewHolder.mealPrice.setText("R$ " + String.format("%.2f", valor));


        if(!cursorCardapio.isLast()){
            this.cursorCardapio.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        return this.cursorCardapio.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView IDName;
        private TextView mealDescription;
        private TextView mealPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IDName = itemView.findViewById(R.id.textComida_fechar_desc);
            mealDescription = itemView.findViewById(R.id.textDescricao);
            mealPrice = itemView.findViewById(R.id.textQuantidade);
        }
    }
}
