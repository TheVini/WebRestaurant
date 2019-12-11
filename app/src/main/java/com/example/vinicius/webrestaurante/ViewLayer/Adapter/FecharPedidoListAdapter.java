package com.example.vinicius.webrestaurante.ViewLayer.Adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vinicius.webrestaurante.R;

import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_DESCRIPTION;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_PRICE;

public class FecharPedidoListAdapter extends RecyclerView.Adapter<FecharPedidoListAdapter.MyViewHolder>  {
    private Cursor cursorProdutos;

    public FecharPedidoListAdapter(Cursor cursorProdutos){
        this.cursorProdutos = cursorProdutos;
    }

    @NonNull
    @Override
    public FecharPedidoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_fechar_pedido_detalhe, viewGroup, false);
        return new FecharPedidoListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(i==0){
            this.cursorProdutos.moveToFirst();
        }

        myViewHolder.comidaNome.setText(String.format("Item: %s", this.cursorProdutos.getString(
                this.cursorProdutos.getColumnIndex(COLUMN_NAME))));
        float valor = Float.valueOf(this.cursorProdutos.getString(this.cursorProdutos.getColumnIndex(COLUMN_PRICE)));
        myViewHolder.comidaPreco.setText("R$ " + String.format("%.2f", valor));
        myViewHolder.comidaDesricao.setText(String.format("Descrição: %s", this.cursorProdutos.getString(
                this.cursorProdutos.getColumnIndex(COLUMN_DESCRIPTION))));

        if(!cursorProdutos.isLast()){
            this.cursorProdutos.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        return this.cursorProdutos.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView comidaNome;
        private TextView comidaPreco;
        private TextView comidaDesricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            comidaNome = itemView.findViewById(R.id.textComida_fechar_desc);
            comidaPreco = itemView.findViewById(R.id.textComidaValor_fechar);
            comidaDesricao = itemView.findViewById(R.id.textProdutoDescricao);
        }
    }
}
