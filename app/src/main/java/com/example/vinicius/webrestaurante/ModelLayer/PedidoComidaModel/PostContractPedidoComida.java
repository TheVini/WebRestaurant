package com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel;

import android.provider.BaseColumns;

public class PostContractPedidoComida {
    private PostContractPedidoComida() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "pedido_comida";
        public static final String COLUMN_PEDIDO_ID = "pedido_id";
        public static final String COLUMN_COMIDA_ID = "comida_id";
    }
}
