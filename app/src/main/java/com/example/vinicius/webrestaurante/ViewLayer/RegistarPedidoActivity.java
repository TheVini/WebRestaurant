package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa;
import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostDbHelperMesa;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostDbHelperPedido;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostDbHelperUsuario;
import com.example.vinicius.webrestaurante.R;

public class RegistarPedidoActivity extends AppCompatActivity {

    private EditText clienteID;
    private EditText clienteNome;
    private EditText clienteCPF;
    private EditText clienteMesa;

    private String mesaID_for_BD;
    private String clienteID_for_BD;
    private Integer clientePosition;
    private Integer mesaPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_pedido);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registrar Pedido");
        setSupportActionBar(toolbar);

        clienteID = findViewById(R.id.editID);
        clienteNome = findViewById(R.id.editCliente);
        clienteCPF = findViewById(R.id.editCPF);
        clienteMesa = findViewById(R.id.editMesa);

        clienteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomerDialog();
            }
        });

        clienteMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTableDialog();
            }
        });
    }

    public void createCustomerDialog(){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha um cliente");

        PostDbHelperUsuario dbHelper = new PostDbHelperUsuario(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursorUsuarios = PostDbHelperUsuario.getFreeClients(db);

        // Add a checkbox list
        String[] customers = new String[cursorUsuarios.getCount()];
        cursorUsuarios.moveToFirst();

        for(int i = 0; i<cursorUsuarios.getCount(); i++){
            customers[i] = String.format("%s %s", cursorUsuarios.getString(
                    cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_NAME)),
                    cursorUsuarios.getString(cursorUsuarios.getColumnIndex(
                                    PostContractUsuario.PostEntry.COLUMN_LAST_NAME)));
            if(!cursorUsuarios.isLast()){
                cursorUsuarios.moveToNext();
            }
        }

        builder.setSingleChoiceItems(customers, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clientePosition = which;
            }
        });

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cursorUsuarios.moveToPosition(clientePosition);
                clienteID_for_BD = cursorUsuarios.getString(
                        cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry._ID));
                clienteID.setText(clienteID_for_BD);

                clienteNome.setText(String.format("%s %s", cursorUsuarios.getString(
                        cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_NAME)), cursorUsuarios.getString(
                        cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_LAST_NAME))));
                clienteCPF.setText(cursorUsuarios.getString(
                        cursorUsuarios.getColumnIndex(PostContractUsuario.PostEntry.COLUMN_CPF)));
            }
        });
        builder.setNegativeButton("Cancelar", null);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createTableDialog(){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma mesa");

        PostDbHelperMesa dbHelper = new PostDbHelperMesa(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursorMesas = PostDbHelperMesa.getFreeTables(db);

        // Add a checkbox list
        String[] tables = new String[cursorMesas.getCount()];
        cursorMesas.moveToFirst();

        for(int i = 0; i<cursorMesas.getCount(); i++){
            tables[i] = String.format("Mesa: %s - %s lugares", cursorMesas.getString(
                    cursorMesas.getColumnIndex(PostContractMesa.PostEntry._ID)),
                    cursorMesas.getString(cursorMesas.getColumnIndex(
                            PostContractMesa.PostEntry.COLUMN_QTD_LUGARES)));
            if(!cursorMesas.isLast()){
                cursorMesas.moveToNext();
            }
        }

        builder.setSingleChoiceItems(tables, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mesaPosition = which;
            }
        });

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cursorMesas.moveToPosition(mesaPosition);
                mesaID_for_BD = cursorMesas.getString(
                        cursorMesas.getColumnIndex(PostContractMesa.PostEntry._ID));
                clienteMesa.setText(mesaID_for_BD);
            }
        });
        builder.setNegativeButton("Cancelar", null);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void registrarPedido(View v){
        //Gerar pedido
        PostDbHelperPedido dbHelperPedido = new PostDbHelperPedido(getApplicationContext());
        String id_pedido = String.valueOf(PostDbHelperPedido.createNewOrder(dbHelperPedido, mesaID_for_BD, clienteID_for_BD));

        //Alocar Mesa
        PostDbHelperMesa dbHelperMesa= new PostDbHelperMesa(getApplicationContext());
        PostDbHelperMesa.bookTable(dbHelperMesa,id_pedido,mesaID_for_BD);

        Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

}
