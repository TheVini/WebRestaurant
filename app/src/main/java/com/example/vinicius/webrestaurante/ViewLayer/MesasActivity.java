package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa;
import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostDbHelperMesa;
import com.example.vinicius.webrestaurante.R;
import com.example.vinicius.webrestaurante.ViewLayer.Adapter.MesasListAdapter;

import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_PEDIDO;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_QTD_LUGARES;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_STATUS;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.TABLE_NAME;

public class MesasActivity extends AppCompatActivity {

    private RecyclerView recyclerMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mesas");
        setSupportActionBar(toolbar);

        recyclerMesas = findViewById(R.id.recyclerMesas);

        //Define Layout
        int orientation = this.getResources().getConfiguration().orientation;
        int imageAmount = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Código para o modo landscape
            imageAmount = 2;
        }
        //Inicialização do Grid do Layout
        recyclerMesas.setLayoutManager(new GridLayoutManager(this,imageAmount));
        recyclerMesas.addItemDecoration( new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarPedido(view);
            }
        });

        getDataFromBD();
    }

    //Aquisição de dados das mesas do BD e exibição
    public void getDataFromBD() {
        PostDbHelperMesa dbHelper = new PostDbHelperMesa(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                PostContractMesa.PostEntry._ID,
                COLUMN_QTD_LUGARES,
                COLUMN_STATUS,
                COLUMN_PEDIDO
        };
        //Trecho só para exibir as mesas que estão no banco
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                PostContractMesa.PostEntry._ID // sort order
        );

        MesasListAdapter adapter = new MesasListAdapter(cursor);
        recyclerMesas.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
    }

    public void criarPedido(View v){
        startActivity(new Intent(this,RegistarPedidoActivity.class));
    }

}
