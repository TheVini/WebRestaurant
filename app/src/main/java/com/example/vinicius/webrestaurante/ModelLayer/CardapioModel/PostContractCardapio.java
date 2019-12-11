package com.example.vinicius.webrestaurante.ModelLayer.CardapioModel;

import android.provider.BaseColumns;

public class PostContractCardapio {
    private PostContractCardapio() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "cardapio";
        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_DESCRIPTION = "descricao";
        public static final String COLUMN_PRICE = "preco";
        public static final String COLUMN_TYPE = "tipo";
    }
}
