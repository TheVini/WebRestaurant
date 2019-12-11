package com.example.vinicius.webrestaurante.ViewLayer.Adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario;
import com.example.vinicius.webrestaurante.R;

import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_MESA_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_VALOR_TOTAL;

public class RegistosAbertosListAdapter extends RecyclerView.Adapter<RegistosAbertosListAdapter.MyViewHolder> {

    private Cursor cursorPedidos;

    public RegistosAbertosListAdapter(Cursor cursorPedidos){
        this.cursorPedidos = cursorPedidos;
    }

    @NonNull
    @Override
    public RegistosAbertosListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_pedidos_em_aberto_detalhe, viewGroup, false);
        return new RegistosAbertosListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistosAbertosListAdapter.MyViewHolder myViewHolder, int i) {
        if(i==0){
            this.cursorPedidos.moveToFirst();
        }

        myViewHolder.mesaID.setText(
                String.format("Mesa: %s", this.cursorPedidos.getString(
                        this.cursorPedidos.getColumnIndex(COLUMN_MESA_ID))));

        myViewHolder.clienteNome.setText(String.format("Cliente: %s %s", this.cursorPedidos.getString(
                this.cursorPedidos.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_NAME)),
                this.cursorPedidos.getString(
                        this.cursorPedidos.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_LAST_NAME))));

        float valor = Float.valueOf(this.cursorPedidos.getString(
                this.cursorPedidos.getColumnIndex(COLUMN_VALOR_TOTAL)));
        myViewHolder.valorTotalPedido.setText("Valor total: R$ " + String.format("%.2f", valor));

        if(!cursorPedidos.isLast()){
            this.cursorPedidos.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        return this.cursorPedidos.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mesaID;
        private TextView clienteNome;
        private TextView valorTotalPedido;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mesaID = itemView.findViewById(R.id.textMesaID_fechar);
            clienteNome = itemView.findViewById(R.id.textClienteNome_fechar);
            valorTotalPedido  = itemView.findViewById(R.id.textValorTotalPedido_fechar);
        }
    }
}

