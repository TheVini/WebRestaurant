package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.res.Configuration;
import android.database.Cursor;
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

import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostDbHelperCardapio;
import com.example.vinicius.webrestaurante.R;
import com.example.vinicius.webrestaurante.ViewLayer.Adapter.CardapioListAdapter;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView recyclerCardapio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);

        recyclerCardapio = findViewById(R.id.recyclerCardapio);

        //Define Layout
        int orientation = this.getResources().getConfiguration().orientation;
        int imageAmount = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Código para o modo landscape
            imageAmount = 2;
        }
        //Inicialização do Grid do Layout
        recyclerCardapio.setLayoutManager(new GridLayoutManager(this, imageAmount));
        recyclerCardapio.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        getDataFromBD();
    }

    //Aquisição de dados das mesas do BD e exibição
    public void getDataFromBD() {
        Cursor cursor = PostDbHelperCardapio.getCardapio(getApplicationContext());
        CardapioListAdapter adapter = new CardapioListAdapter(cursor);
        recyclerCardapio.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
    }

}
