package com.example.vinicius.webrestaurante.ModelLayer.MesasModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;

import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_PEDIDO;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_QTD_LUGARES;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.COLUMN_STATUS;
import static com.example.vinicius.webrestaurante.ModelLayer.MesasModel.PostContractMesa.PostEntry.TABLE_NAME;

public class PostDbHelperMesa extends SQLiteOpenHelper {

    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PostContractMesa.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_QTD_LUGARES + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_PEDIDO + " TEXT)";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final int DATABASE_VERSION = GlobalVariables.DATABASE_VERSION;
    private static final String DATABASE_NAME = GlobalVariables.DATABASE_NAME;

    public PostDbHelperMesa(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void createTables(SQLiteDatabase db) {
        db.beginTransaction();

        //Criar 5 mesas de 4 lugares
        for (int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_QTD_LUGARES, 4);
            values.put(COLUMN_STATUS, "Livre");
            values.put(COLUMN_PEDIDO, "Livre");
            // Insert the new row, returning the primary key value of the new row
            db.insert(TABLE_NAME, null, values);
        }

        //Criar 5 mesas de 6 lugares
        for (int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_QTD_LUGARES, 6);
            values.put(COLUMN_STATUS, "Livre");
            values.put(COLUMN_PEDIDO, "Livre");
            // Insert the new row, returning the primary key value of the new row
            db.insert(TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POSTS);
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Cursor getFreeTables(SQLiteDatabase db) {
        String[] projection = {
                PostContractMesa.PostEntry._ID,
                PostContractMesa.PostEntry.COLUMN_QTD_LUGARES,
        };
        String selection = COLUMN_STATUS + " = ?";
        String[] selectionArgs = {"Livre"};
        //Trecho só para exibir os usuários que estão no banco
        Cursor cursor;
        cursor = db.query(
                PostContractMesa.PostEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                PostContractMesa.PostEntry._ID               // sort order
        );

        return cursor;
    }

    public static void bookTable(PostDbHelperMesa dbHelper, String pedido_ID, String table_ID){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // New value for two column
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "Ocupada");
        values.put(COLUMN_PEDIDO, pedido_ID);

        // Which row to update, based on the title
        String selection = PostContractMesa.PostEntry._ID + " LIKE ?";
        String[] selectionArgs = { table_ID };

        int count = db.update(
                PostContractMesa.PostEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void changeMesaStatus_toFree(PostDbHelperMesa dbHelper, String mesa_ID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "Livre");
        values.put(COLUMN_PEDIDO, "Livre");

        // Which row to update, based on the title
        String selection = PostContractMesa.PostEntry._ID + " LIKE ?";
        String[] selectionArgs = { mesa_ID };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
