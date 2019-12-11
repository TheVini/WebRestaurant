package com.example.vinicius.webrestaurante.ModelLayer.MesasModel;

import android.provider.BaseColumns;

public class PostContractMesa {
    private PostContractMesa() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "mesas";
        public static final String COLUMN_QTD_LUGARES = "qtd_lugares";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_PEDIDO = "pedido";
    }
}
