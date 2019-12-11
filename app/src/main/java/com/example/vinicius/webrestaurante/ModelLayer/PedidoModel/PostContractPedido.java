package com.example.vinicius.webrestaurante.ModelLayer.PedidoModel;

import android.provider.BaseColumns;

public class PostContractPedido {
    private PostContractPedido() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "pedidos";
        public static final String COLUMN_MESA_ID = "mesa_id";
        public static final String COLUMN_CLIENTE_ID = "cliente_id";
        public static final String COLUMN_VALOR_TOTAL = "valor_total";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_FORMA_PGTO = "forma_pgto";
        public static final String COLUMN_CREATION = "data_criacao";
        public static final String COLUMN_DATA_PGTO = "data_pgto";
    }
}
