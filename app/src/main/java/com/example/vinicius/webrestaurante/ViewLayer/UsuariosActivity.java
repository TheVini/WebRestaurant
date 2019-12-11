package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostDbHelperUsuario;
import com.example.vinicius.webrestaurante.R;
import com.example.vinicius.webrestaurante.ViewLayer.Adapter.UsuariosListAdapter;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Clientes Cadastrados");
        setSupportActionBar(toolbar);

        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);

        //Define Layout
        int orientation = this.getResources().getConfiguration().orientation;
        int imageAmount = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Código para o modo landscape
            imageAmount = 2;
        }
        //Inicialização do Grid do Layout
        recyclerUsuarios.setLayoutManager(new GridLayoutManager(this,imageAmount));
        recyclerUsuarios.addItemDecoration( new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        getDataFromBD();
    }

    public void cadastrarCliente(View v){
        startActivity(new Intent(this,CadastroActivity.class));
    }

    //Aquisição de dados dos usuários do BD e exibição
    public void getDataFromBD() {
        PostDbHelperUsuario dbHelper = new PostDbHelperUsuario(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                PostContractUsuario.PostEntry.COLUMN_NAME,
                PostContractUsuario.PostEntry.COLUMN_LAST_NAME,
                PostContractUsuario.PostEntry.COLUMN_CPF,
                PostContractUsuario.PostEntry.COLUMN_CREATION
        };
        //Trecho só para exibir os usuários que estão no banco
        Cursor cursor = db.query(
                PostContractUsuario.PostEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                PostContractUsuario.PostEntry.COLUMN_NAME               // sort order
        );

        UsuariosListAdapter adapter = new UsuariosListAdapter(cursor);
        recyclerUsuarios.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
    }
}
