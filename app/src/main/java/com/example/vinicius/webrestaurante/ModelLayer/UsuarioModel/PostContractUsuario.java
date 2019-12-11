package com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel;

import android.provider.BaseColumns;

public class PostContractUsuario {
    private PostContractUsuario() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "usuarios";
        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_LAST_NAME = "sobrenome";
        public static final String COLUMN_CPF = "cpf";
        public static final String COLUMN_CREATION = "data_criacao";
    }
}
