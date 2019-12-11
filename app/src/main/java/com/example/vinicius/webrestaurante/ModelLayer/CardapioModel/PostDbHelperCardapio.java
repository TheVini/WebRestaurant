package com.example.vinicius.webrestaurante.ModelLayer.CardapioModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido;

import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_DESCRIPTION;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_PRICE;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.COLUMN_TYPE;
import static com.example.vinicius.webrestaurante.ModelLayer.CardapioModel.PostContractCardapio.PostEntry.TABLE_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_VALOR_TOTAL;

public class PostDbHelperCardapio extends SQLiteOpenHelper {
    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PostContractCardapio.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_TYPE + " TEXT)";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final int DATABASE_VERSION = GlobalVariables.DATABASE_VERSION;
    private static final String DATABASE_NAME = GlobalVariables.DATABASE_NAME;

    public PostDbHelperCardapio(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POSTS);
        createMeals(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Cursor getCardapio(Context context){
        PostDbHelperCardapio dbHelper = new PostDbHelperCardapio(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                PostContractCardapio.PostEntry._ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_PRICE
        };
        //Trecho só para exibir as comidas que estão no banco
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                COLUMN_NAME,                   // don't group the rows
                null,                   // don't filter by row groups
                COLUMN_TYPE +" DESC" // sort order
        );
        return cursor;
    }

    public static Double getComidaValue_ID(Context context, Integer comidaID){
        PostDbHelperCardapio dbHelper = new PostDbHelperCardapio(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                PostContractCardapio.PostEntry._ID,
                COLUMN_PRICE
        };
        // Which row to update, based on the title
        String selection = PostContractCardapio.PostEntry._ID + " = ?";
        String[] selectionArgs = { Integer.toString(comidaID)};

        //Trecho só para exibir as comidas que estão no banco
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null // sort order
        );
        cursor.moveToFirst();
        return Double.valueOf(cursor.getString(cursor.getColumnIndex(PostContractCardapio.PostEntry.COLUMN_PRICE)));
    }

    public static void createMeals(SQLiteDatabase db){
        db.beginTransaction();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, "Americana");
        values.put(COLUMN_DESCRIPTION, "presunto, champignon e requeijão salpicado com bacon");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Caipira");
        values.put(COLUMN_DESCRIPTION, "Filé de frango desfiado e milho cobertos com  requeijão cremoso");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Quatro queijos");
        values.put(COLUMN_DESCRIPTION, "Muçarela, requeijão, provolone e parmesão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Escarola");
        values.put(COLUMN_DESCRIPTION, "Escarola e bacon crocante cobertos com muçarela");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Portuguesa");
        values.put(COLUMN_DESCRIPTION, "Presunto, ovos e cebola cobertos com muçarela");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Alho");
        values.put(COLUMN_DESCRIPTION, "alho em grãos, cebola");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Brócolis");
        values.put(COLUMN_DESCRIPTION, "Brócolis, bacon crocante e leve cobertura de muçarela ou requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Atum");
        values.put(COLUMN_DESCRIPTION, "Atum sólido com finas fatias de cebola cobertura de muçarela ou requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Bacon");
        values.put(COLUMN_DESCRIPTION, "Bacon crocante disposto sobre muçarela ou requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Tradicional queijo");
        values.put(COLUMN_DESCRIPTION, "Provolone, orégano e tomatinho");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Cinco queijos");
        values.put(COLUMN_DESCRIPTION, "Muçarela, requeijão, provolone, gorgonzola e parmesão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Baiana");
        values.put(COLUMN_DESCRIPTION, "Calabresa moída, ovos, pimenta, cebola e leve toque de parmesão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Calabresa");
        values.put(COLUMN_DESCRIPTION, "Calabresa fatiada e finas fatias de cebola");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Frango catupiry");
        values.put(COLUMN_DESCRIPTION, "Filé de frango desfiado com cobertura de requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Marguerita");
        values.put(COLUMN_DESCRIPTION, "Muçarela, rodelas de tomate, parmesão e folhas de manjericão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Lombinho");
        values.put(COLUMN_DESCRIPTION, "Lombinho e cebola cobertos com muçarela ou requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Rúcula com tomate seco");
        values.put(COLUMN_DESCRIPTION, "rúcula inteira, tomate seco, manjericão ");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "24.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Milho");
        values.put(COLUMN_DESCRIPTION, "Milho com cobertura de muçarela ou requeijão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Napolitana");
        values.put(COLUMN_DESCRIPTION, "Muçarela, rodelas de tomate e parmesão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "23.5");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Toscana");
        values.put(COLUMN_DESCRIPTION, "Calabresa moída, ovos, requeijão, muçarela e folhas de manjericão");
        values.put(COLUMN_TYPE, "Comida");
        values.put(COLUMN_PRICE, "29.9");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Coca-Cola");
        values.put(COLUMN_DESCRIPTION, "Refrigerante");
        values.put(COLUMN_TYPE, "Bebida");
        values.put(COLUMN_PRICE, "10");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Pepsi");
        values.put(COLUMN_DESCRIPTION, "Refrigerante");
        values.put(COLUMN_TYPE, "Bebida");
        values.put(COLUMN_PRICE, "8");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Itubaína");
        values.put(COLUMN_DESCRIPTION, "Refrigerante");
        values.put(COLUMN_TYPE, "Bebida");
        values.put(COLUMN_PRICE, "6");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Dolly");
        values.put(COLUMN_DESCRIPTION, "Refrigerante");
        values.put(COLUMN_TYPE, "Bebida");
        values.put(COLUMN_PRICE, "5");
        db.insert(TABLE_NAME, null, values);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

}
