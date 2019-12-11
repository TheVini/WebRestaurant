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

public class UsuariosListAdapter extends RecyclerView.Adapter<UsuariosListAdapter.MyViewHolder> {

    private Cursor cursorUsuarios;

    public UsuariosListAdapter(Cursor cursorUsuarios){
        this.cursorUsuarios = cursorUsuarios;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_usuario_detalhe, viewGroup, false);
        return new UsuariosListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(i==0){
            this.cursorUsuarios.moveToFirst();
        }

        myViewHolder.clienteNomeCompleto.setText(
                String.format("Nome: %s %s", this.cursorUsuarios.getString(
                        this.cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_NAME)),
                        this.cursorUsuarios.getString(
                                this.cursorUsuarios.getColumnIndex(
                                        PostContractUsuario.PostEntry.COLUMN_LAST_NAME))));
        myViewHolder.clienteCPF.setText(String.format("CPF: %s", this.cursorUsuarios.getString(
                this.cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_CPF))));
        myViewHolder.clienteCadastramento.setText(String.format("Data de cadastramento: %s",
                this.cursorUsuarios.getString(this.cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_CREATION))));
        if(!cursorUsuarios.isLast()){
            this.cursorUsuarios.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        return this.cursorUsuarios.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView clienteNomeCompleto;
        private TextView clienteCPF;
        private TextView clienteCadastramento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clienteNomeCompleto = itemView.findViewById(R.id.textNomeCompleto);
            clienteCPF = itemView.findViewById(R.id.textCPF);
            clienteCadastramento = itemView.findViewById(R.id.textDataCadastramento);
        }
    }
}
