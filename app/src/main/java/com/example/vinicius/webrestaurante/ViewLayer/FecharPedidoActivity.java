package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostDbHelperMesa;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostDbHelperPedidoComida;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostDbHelperPedido;
import com.example.vinicius.webrestaurante.R;
import com.example.vinicius.webrestaurante.ViewLayer.Adapter.FecharPedidoListAdapter;

import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.COLUMN_COMIDA_ID;

public class FecharPedidoActivity extends AppCompatActivity {

    private RecyclerView recyclerCompras;
    private TextView mesaID;
    private TextView nomeCliente;
    private TextView valorTotalPedido;

    private String pedidoID;
    private String mesaID_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechar_pedido);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gerar Conta");
        setSupportActionBar(toolbar);

        mesaID = findViewById(R.id.textMesaID_fechar);
        nomeCliente = findViewById(R.id.textClienteNome_fechar);
        valorTotalPedido = findViewById(R.id.textValorTotalPedido_fechar);

        mesaID_value = getIntent().getStringExtra("MESA_ID");
        mesaID.setText("Mesa: " + mesaID_value);

        Double valorTotal = getIntent().getDoubleExtra("VALOR_TOTAL",0);
        valorTotalPedido.setText("Valor total do pedido: R$ " + String.format("%.2f", valorTotal));

        String clientFullName = getIntent().getStringExtra("CLIENT_FULL_NAME");
        nomeCliente.setText("Cliente: " + clientFullName);
        pedidoID = getIntent().getStringExtra("PEDIDO_ID");

        recyclerCompras = findViewById(R.id.recyclerCompras);

        //Define Layout
        int orientation = this.getResources().getConfiguration().orientation;
        int imageAmount = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Código para o modo landscape
            imageAmount = 2;
        }
        //Inicialização do Grid do Layout
        recyclerCompras.setLayoutManager(new GridLayoutManager(this, imageAmount));
        recyclerCompras.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog_1();
            }
        });

        getDataFromBD(pedidoID);
    }

    //Aquisição de dados das compras do BD e exibição
    public void getDataFromBD(String pedidoID) {
        PostDbHelperPedidoComida dbHelper = new PostDbHelperPedidoComida(getApplicationContext());
        Cursor cursor = PostDbHelperPedidoComida.getComidasInOrder(dbHelper, pedidoID);

        FecharPedidoListAdapter adapter = new FecharPedidoListAdapter(cursor);
        recyclerCompras.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
    }

    public void createDialog_1(){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha a forma de pagamento");

        final String[] choices = {"Dinheiro", "Cartão - À vista", "Cartão - Débito"};

        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fecharPedido(choices[which]);
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fecharPedido(String formaPgto){
        //Mudar o Status do pedido, definindo a forma de pagamento  e data
        PostDbHelperPedido dbHelperPedidos = new PostDbHelperPedido(getApplicationContext());
        PostDbHelperPedido.closeOrder(dbHelperPedidos, mesaID_value, formaPgto);
        //Mudar o Status da mesa
        PostDbHelperMesa dbHelperMesa = new PostDbHelperMesa(getApplicationContext());
        PostDbHelperMesa.changeMesaStatus_toFree(dbHelperMesa, mesaID_value);
        //Voltar para a main
        Toast.makeText(getApplicationContext(), "Conta fechada com sucesso!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }
}
