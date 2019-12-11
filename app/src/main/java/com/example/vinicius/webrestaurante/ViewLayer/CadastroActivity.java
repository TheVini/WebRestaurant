package com.example.vinicius.webrestaurante.ViewLayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostDbHelperUsuario;
import com.example.vinicius.webrestaurante.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText clienteNome;
    private EditText clienteSobreNome;
    private EditText clienteCPF;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastrar do Cliente");
        setSupportActionBar(toolbar);

        clienteNome = findViewById(R.id.editCliente);
        clienteSobreNome = findViewById(R.id.editSobrenome);
        clienteCPF = findViewById(R.id.editCPF);
        btnSalvar = findViewById(R.id.btnCriarCadastro);

        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(String.valueOf(clienteNome.getText()).length() > 0 &&
                        String.valueOf(clienteSobreNome.getText()).length() > 0 &&
                        String.valueOf(clienteCPF.getText()).length() == 11) {

                    PostDbHelperUsuario dbHelper = new PostDbHelperUsuario(getApplicationContext());
                    long newRowId = dbHelper.createNewCustomer(dbHelper,
                            String.valueOf(clienteNome.getText()),
                            String.valueOf(clienteSobreNome.getText()),
                            String.valueOf(clienteCPF.getText()));
                    if (newRowId != -1) {
                        Toast.makeText(getApplicationContext(), "Dados salvos com suscesso", Toast.LENGTH_LONG).show();
                        returnToMain();
                    } else {
                        Toast.makeText(getApplicationContext(), "CPF já cadastrado!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Campos inválidos! Por favor, corrija os dados escritos.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void returnToMain(){
        startActivity(new Intent(this, MainActivity.class));
    }
}