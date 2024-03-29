package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;
import com.example.vinicius.webrestaurante.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurante");
        setSupportActionBar(toolbar);

        //GlobalVariables.createTable(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void consultarCliente(View v){
        startActivity(new Intent(this,UsuariosActivity.class));
    }

    public void gerenciarMesas(View v){
        startActivity(new Intent(this,MesasActivity.class));
    }

    public void cardapio(View v){
        startActivity(new Intent(this,CardapioActivity.class));
    }

    public void pedidosEmAberto(View v){
        startActivity(new Intent(this, PedidosAbertoActivity.class));
    }
}
