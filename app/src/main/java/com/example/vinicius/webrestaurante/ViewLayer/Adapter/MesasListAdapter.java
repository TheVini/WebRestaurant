package com.example.vinicius.webrestaurante.ViewLayer.Adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa;
import com.example.vinicius.webrestaurante.R;

import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_PEDIDO;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_QTD_LUGARES;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_STATUS;

public class MesasListAdapter extends RecyclerView.Adapter<MesasListAdapter.MyViewHolder> {

    private Cursor cursorMesas;

    public MesasListAdapter(Cursor cursorMesas){
        this.cursorMesas = cursorMesas;
    }

    @NonNull
    @Override
    public MesasListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_mesa_detalhe, viewGroup, false);
        return new MesasListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MesasListAdapter.MyViewHolder myViewHolder, int i) {
        if(i==0){
            this.cursorMesas.moveToFirst();
        }

        myViewHolder.mesaID.setText(
                String.format("Identificação: %s", this.cursorMesas.getString(
                        this.cursorMesas.getColumnIndex(PostContractMesa.PostEntry._ID))));
        myViewHolder.mesaStatus.setText(String.format("Status: %s", this.cursorMesas.getString(
                this.cursorMesas.getColumnIndex(COLUMN_STATUS))));
        myViewHolder.mesaQtdLugares.setText(String.format("Quantidade de lugares: %s", this.cursorMesas.getString(
                this.cursorMesas.getColumnIndex(COLUMN_QTD_LUGARES))));
        myViewHolder.mesaPedido.setText(String.format("Pedido: %s", this.cursorMesas.getString(
                this.cursorMesas.getColumnIndex(COLUMN_PEDIDO))));

        if(!cursorMesas.isLast()){
            this.cursorMesas.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        return this.cursorMesas.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mesaID;
        private TextView mesaQtdLugares;
        private TextView mesaStatus;
        private TextView mesaPedido;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mesaID = itemView.findViewById(R.id.textComida_fechar_desc);
            mesaQtdLugares = itemView.findViewById(R.id.textQuantidade);
            mesaStatus = itemView.findViewById(R.id.textComidaValor_fechar);
            mesaPedido = itemView.findViewById(R.id.textValorTotal);
        }
    }
}
