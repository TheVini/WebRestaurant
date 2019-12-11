package com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio;
import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;

import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.COLUMN_COMIDA_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.COLUMN_PEDIDO_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoComidaModel.PostContractPedidoComida.PostEntry.TABLE_NAME;

public class PostDbHelperPedidoComida extends SQLiteOpenHelper {

    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_PEDIDO_ID + " TEXT, " +
                    COLUMN_COMIDA_ID + " TEXT)";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final int DATABASE_VERSION = GlobalVariables.DATABASE_VERSION;
    private static final String DATABASE_NAME = GlobalVariables.DATABASE_NAME;

    public PostDbHelperPedidoComida(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static long insertMealToOrder(PostDbHelperPedidoComida dbHelper, String pedidoID, String comidaID){

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_PEDIDO_ID, pedidoID);
        values.put(COLUMN_COMIDA_ID, comidaID);
        // Insert the new row, returning the primary key value of the new row
        return db.insert(TABLE_NAME, null, values);
    }

    private static final String COMIDAS_NO_PEDIDO =
            "SELECT b.nome as " + PostContractCardapio.PostEntry.COLUMN_NAME +
            " , b.descricao as " + PostContractCardapio.PostEntry.COLUMN_DESCRIPTION +
            " , b.preco as " + PostContractCardapio.PostEntry.COLUMN_PRICE +
            " , b.tipo as " + PostContractCardapio.PostEntry.COLUMN_TYPE +
            " FROM " + TABLE_NAME + " a INNER JOIN " +
             PostContractCardapio.PostEntry.TABLE_NAME + " b " +
            "ON cast(a.comida_id AS INTEGER) = cast(b._id AS INTEGER) " +
            "WHERE a.pedido_id LIKE ";

    public static Cursor getComidasInOrder(PostDbHelperPedidoComida dbHelper, String pedidoID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String COMAND = COMIDAS_NO_PEDIDO + " " + pedidoID;
        Log.d("VINICIUS", COMAND);
        return db.rawQuery(COMAND, null);
    }
}
