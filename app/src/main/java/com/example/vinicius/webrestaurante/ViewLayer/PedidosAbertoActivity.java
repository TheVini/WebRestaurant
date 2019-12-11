package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio;
import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostDbHelperCardapio;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostDbHelperPedidoComida;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostDbHelperPedido;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostDbHelperUsuario;
import com.example.vinicius.webrestaurante.R;
import com.example.vinicius.webrestaurante.ViewLayer.Adapter.RegistosAbertosListAdapter;

import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_MESA_ID;

public class PedidosAbertoActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidosAbertos;
    private Integer produtoPosicao;
    private Cursor cursorPedidosAbertos;
    private String mesaID;
    private Cursor cursorCardapio;

    private PostDbHelperPedido dbHelperPedido;
    private PostDbHelperUsuario dbHelperUsuario;
    private PostDbHelperPedidoComida dbHelperPedidoComida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_em_aberto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos em Aberto");
        setSupportActionBar(toolbar);

        recyclerPedidosAbertos = findViewById(R.id.recyclerPedidosAbertos);

        //Define Layout
        int orientation = this.getResources().getConfiguration().orientation;
        int imageAmount = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Código para o modo landscape
            imageAmount = 2;
        }
        //Inicialização do Grid do Layout
        recyclerPedidosAbertos.setLayoutManager(new GridLayoutManager(this, imageAmount));
        recyclerPedidosAbertos.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        cursorPedidosAbertos = getDataFromBD();

        recyclerPedidosAbertos.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerPedidosAbertos, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        createDialog_1(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void createDialog_1(final int position){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma opção abaixo");

        String[] choices = {"Escolher Prato", "Fechar Pedido"};
        cursorPedidosAbertos.moveToPosition(position);
        mesaID = cursorPedidosAbertos.getString(
                cursorPedidosAbertos.getColumnIndex(COLUMN_MESA_ID));
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        createDialog_2(position, mesaID);
                        break;
                    case 1:
                        Intent intent = new Intent(getBaseContext(), FecharPedidoActivity.class);
                        dbHelperPedido = new PostDbHelperPedido(getApplicationContext());
                        Double pedidoValor = PostDbHelperPedido.getValorTotal_PedidoAberto_Mesa(dbHelperPedido, mesaID);
                        String clientID = PostDbHelperPedido.getClientID_fromMesa(dbHelperPedido, mesaID);
                        String pedidoID = PostDbHelperPedido.getID_PedidoAberto_Mesa(dbHelperPedido, mesaID);

                        dbHelperUsuario = new PostDbHelperUsuario(getApplicationContext());
                        String clientFullName = PostDbHelperUsuario.getClientFullName(dbHelperUsuario, Integer.valueOf(clientID));
                        intent.putExtra("MESA_ID", mesaID);
                        intent.putExtra("VALOR_TOTAL", pedidoValor);
                        intent.putExtra("PEDIDO_ID", pedidoID);
                        intent.putExtra("CLIENT_FULL_NAME", clientFullName);
                        startActivity(intent);
                        break;
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createDialog_2(Integer position, String mesa) {
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma comida/bebida - Mesa " + mesa);
        cursorCardapio = PostDbHelperCardapio.getCardapio(getApplicationContext());

        // Add a checkbox list
        String[] produto = new String[cursorCardapio.getCount()];
        cursorCardapio.moveToFirst();

        for (int i = 0; i < cursorCardapio.getCount(); i++) {
            float preco = Float.valueOf(cursorCardapio.getString(
                    cursorCardapio.getColumnIndex(PostContractCardapio.PostEntry.COLUMN_PRICE)));
            produto[i] = String.format("%s - R$%s",
                    cursorCardapio.getString(cursorCardapio.getColumnIndex(
                            PostContractCardapio.PostEntry.COLUMN_NAME)),
                    String.format("%.2f", preco));
            if (!cursorCardapio.isLast()) {
                cursorCardapio.moveToNext();
            }
        }

        builder.setSingleChoiceItems(produto, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                produtoPosicao = which;
            }
        });

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelperPedido = new PostDbHelperPedido(getApplicationContext());
                //Obter dados do pedido clicado
                String pedidoID = PostDbHelperPedido.getID_PedidoAberto_Mesa(dbHelperPedido, mesaID);

                //Obter dados da comdia escolhida
                cursorCardapio.moveToPosition(produtoPosicao);
                String comida_ID = cursorCardapio.getString(
                        cursorCardapio.getColumnIndex(PostContractCardapio.PostEntry._ID));

                //Salvar comida escolhida no pedido
                dbHelperPedidoComida = new PostDbHelperPedidoComida(getApplicationContext());
                PostDbHelperPedidoComida.insertMealToOrder(dbHelperPedidoComida, pedidoID, comida_ID);

                //Atualizar o valor total do pedido
                Double valorComida = PostDbHelperCardapio.getComidaValue_ID(getApplicationContext(), Integer.valueOf(comida_ID));
                PostDbHelperPedido.updateOrderValue(dbHelperPedido, mesaID, valorComida);

                Toast.makeText(getApplicationContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                //Voltar para a main
                finish();
                startActivity(getIntent());

            }
        });
        builder.setNegativeButton("Cancelar", null);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Aquisição de dados das mesas do BD e exibição
    public Cursor getDataFromBD() {
        PostDbHelperPedido dbHelper = new PostDbHelperPedido(getApplicationContext());
        Cursor cursor = PostDbHelperPedido.getPedidosAbertos(dbHelper);

        RegistosAbertosListAdapter adapter = new RegistosAbertosListAdapter(cursor);
        adapter.notifyDataSetChanged();
        recyclerPedidosAbertos.setAdapter(adapter);

        return cursor;
        //Toast.makeText(getApplicationContext(), String.valueOf(cursorPedidosAbertos.getCount()), Toast.LENGTH_LONG).show();
    }

}
