package com.example.vinicius.webrestaurante.ModelLayer.PedidoModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;
import com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_CLIENTE_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_CREATION;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_DATA_PGTO;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_FORMA_PGTO;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_MESA_ID;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_STATUS;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.COLUMN_VALOR_TOTAL;
import static com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido.PostEntry.TABLE_NAME;

public class PostDbHelperPedido extends SQLiteOpenHelper {
    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PostContractPedido.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_MESA_ID + " TEXT, " +
                    COLUMN_CLIENTE_ID + " TEXT, " +
                    COLUMN_VALOR_TOTAL + " REAL, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_CREATION + " TEXT, " +
                    COLUMN_DATA_PGTO + " TEXT, " +
                    COLUMN_FORMA_PGTO + " TEXT)";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final int DATABASE_VERSION = GlobalVariables.DATABASE_VERSION;
    private static final String DATABASE_NAME = GlobalVariables.DATABASE_NAME;

    public PostDbHelperPedido(Context context) {
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

    public static long createNewOrder(PostDbHelperPedido dbHelper, String mesa_ID, String cliente_ID){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESA_ID, mesa_ID);
        values.put(COLUMN_CLIENTE_ID, cliente_ID);
        values.put(COLUMN_VALOR_TOTAL, 0.00);
        values.put(COLUMN_STATUS, "Em aberto");
        values.put(COLUMN_FORMA_PGTO, "Em aberto");
        values.put(COLUMN_DATA_PGTO, "-");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        // Insert the new row, returning the primary key value of the new row
        return db.insert(TABLE_NAME, null, values);
    }

    private static String selectionArgs = "'Em aberto'";
    private static final String PEDIDOS_ABERTOS = "SELECT a.mesa_id as " + COLUMN_MESA_ID +
            " , a._id as " + PostContractPedido.PostEntry._ID +
            " , b.nome as " + PostContractUsuario.PostEntry.COLUMN_NAME +
            " , b.sobrenome as " + PostContractUsuario.PostEntry.COLUMN_LAST_NAME +
            " , a.valor_total as " + COLUMN_VALOR_TOTAL +
            " , a.status as " + COLUMN_STATUS +
            " FROM " + TABLE_NAME + " a INNER JOIN " +
            PostContractUsuario.PostEntry.TABLE_NAME + " b " +
            "ON cast(a.cliente_id AS REAL) = cast(b._id AS REAL) " +
            "WHERE a.status LIKE " + String.valueOf(selectionArgs);

    public static Cursor getPedidosAbertos(PostDbHelperPedido dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(PEDIDOS_ABERTOS, null);
    }

    public static String getID_PedidoAberto_Mesa(PostDbHelperPedido dbHelper, String mesa_ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String PEDIDOS_ABERTOS_MESA = PEDIDOS_ABERTOS + " and " +
                COLUMN_MESA_ID + " LIKE "+ mesa_ID;
        Cursor cursor = db.rawQuery(PEDIDOS_ABERTOS_MESA, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(PostContractPedido.PostEntry._ID));
    }

    public static Double getValorTotal_PedidoAberto_Mesa(PostDbHelperPedido dbHelper, String mesa_ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String PEDIDOS_ABERTOS_MESA = PEDIDOS_ABERTOS + " and " +
                COLUMN_MESA_ID + " LIKE "+ mesa_ID;
        Cursor cursor = db.rawQuery(PEDIDOS_ABERTOS_MESA, null);
        cursor.moveToFirst();
        return Double.valueOf(cursor.getString(cursor.getColumnIndex(PostContractPedido.PostEntry.COLUMN_VALOR_TOTAL)));
    }

    public static String getClientID_fromMesa(PostDbHelperPedido dbHelper, String mesa_ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PostContractPedido.PostEntry._ID,
                COLUMN_MESA_ID,
                COLUMN_CLIENTE_ID
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_MESA_ID + " = ? and " + COLUMN_STATUS + " LIKE ? ";
        String[] selectionArgs = { mesa_ID, "Em aberto" };

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(PostContractPedido.PostEntry.COLUMN_CLIENTE_ID));
    }

    public static void updateOrderValue(PostDbHelperPedido dbHelper, String mesa_ID, Double newValue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        Double newValue_BD = newValue;
        Double currentTotalValue_BD = getValorTotal_PedidoAberto_Mesa(dbHelper,mesa_ID);
        Double newTotalValue_BD = newValue_BD + currentTotalValue_BD;
        String pedidoID = getID_PedidoAberto_Mesa(dbHelper,mesa_ID);

        ContentValues values = new ContentValues();
        values.put(COLUMN_VALOR_TOTAL, newTotalValue_BD);

        // Which row to update, based on the title
        String selection = PostContractPedido.PostEntry._ID + " LIKE ?";
        String[] selectionArgs = { pedidoID };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void closeOrder(PostDbHelperPedido dbHelper, String mesa_ID, String formaPgto){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "Pago");
        values.put(COLUMN_FORMA_PGTO, formaPgto);
        values.put(COLUMN_DATA_PGTO, currentDateandTime);

        // Which row to update, based on the title
        String selection = COLUMN_MESA_ID + " LIKE ?";
        String[] selectionArgs = { mesa_ID };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
