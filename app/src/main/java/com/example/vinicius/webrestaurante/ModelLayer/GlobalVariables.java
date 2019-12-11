package com.example.vinicius.webrestaurante.ModelLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio;
import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostDbHelperCardapio;
import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa;
import com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostDbHelperMesa;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostDbHelperPedidoComida;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostDbHelperPedido;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostDbHelperUsuario;

import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_DESCRIPTION;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_PRICE;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_TYPE;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.TABLE_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_PEDIDO;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_QTD_LUGARES;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_STATUS;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.COLUMN_COMIDA_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.COLUMN_PEDIDO_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_CLIENTE_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_CREATION;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_DATA_PGTO;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_FORMA_PGTO;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_MESA_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_VALOR_TOTAL;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_CPF;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_LAST_NAME;

public class GlobalVariables {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restaurante_4.db";

    public static void createTable(Context context){
        PostDbHelperCardapio dbHelper = new PostDbHelperCardapio(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PostContractCardapio.PostEntry.TABLE_NAME);
        String SQL_CREATE_POSTS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        PostContractCardapio.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_PRICE + " REAL, " +
                        COLUMN_TYPE + " TEXT)";
        db.execSQL(SQL_CREATE_POSTS);
        PostDbHelperCardapio.createMeals(db);

        PostDbHelperMesa dbHelperMesa = new PostDbHelperMesa(context);
        SQLiteDatabase db_mesa = dbHelperMesa.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PostContractMesa.PostEntry.TABLE_NAME);
        String SQL_CREATE_POSTS_TABLE =
                "CREATE TABLE " + PostContractMesa.PostEntry.TABLE_NAME + " (" +
                        PostContractMesa.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        COLUMN_QTD_LUGARES + " TEXT, " +
                        COLUMN_STATUS + " TEXT, " +
                        COLUMN_PEDIDO + " TEXT)";
        db_mesa.execSQL(SQL_CREATE_POSTS_TABLE);
        PostDbHelperMesa.createTables(db_mesa);

        PostDbHelperPedido dbHelperPedido = new PostDbHelperPedido(context);
        SQLiteDatabase db_pedido = dbHelperPedido.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PostContractPedido.PostEntry.TABLE_NAME);
        String SQL_CREATE_POSTS_PEDIDO =
                "CREATE TABLE " + PostContractPedido.PostEntry.TABLE_NAME + " (" +
                        PostContractPedido.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        COLUMN_MESA_ID + " TEXT, " +
                        COLUMN_CLIENTE_ID + " TEXT, " +
                        COLUMN_VALOR_TOTAL + " REAL, " +
                        PostContractPedido.PostEntry.COLUMN_STATUS + " TEXT, " +
                        COLUMN_CREATION + " TEXT, " +
                        COLUMN_DATA_PGTO + " TEXT, " +
                        COLUMN_FORMA_PGTO + " TEXT)";
        db_pedido.execSQL(SQL_CREATE_POSTS_PEDIDO);

        PostDbHelperUsuario dbHelperUsuario = new PostDbHelperUsuario(context);
        SQLiteDatabase db_usuario = dbHelperUsuario.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PostContractUsuario.PostEntry.TABLE_NAME);
        String SQL_CREATE_POSTS_USUARIO =
                "CREATE TABLE " + PostContractUsuario.PostEntry.TABLE_NAME + " (" +
                        PostContractUsuario.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        PostContractUsuario.PostEntry.COLUMN_NAME + " TEXT, " +
                        COLUMN_LAST_NAME + " TEXT, " +
                        COLUMN_CPF + " TEXT, " +
                        PostContractUsuario.PostEntry.COLUMN_CREATION + " TEXT)";
        db_usuario.execSQL(SQL_CREATE_POSTS_USUARIO);
        PostDbHelperUsuario.createUsers(db_usuario);

        PostDbHelperPedidoComida dbHelperPedidoComida = new PostDbHelperPedidoComida(context);
        SQLiteDatabase db_pedido_comida = dbHelperPedidoComida.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PostContractPedidoComida.PostEntry.TABLE_NAME);
        String SQL_CREATE_POSTS_PEDIDO_COMIDA =
                "CREATE TABLE " + PostContractPedidoComida.PostEntry.TABLE_NAME + " (" +
                        COLUMN_PEDIDO_ID + " TEXT, " +
                        COLUMN_COMIDA_ID + " TEXT)";
        db_pedido_comida.execSQL(SQL_CREATE_POSTS_PEDIDO_COMIDA);
    }
}
