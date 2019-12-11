package com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.vinicius.webrestaurante.ModelLayer.GlobalVariables;
import com.example.vinicius.webrestaurante.ModelLayer.PedidoModel.PostContractPedido;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_CPF;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_CREATION;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_LAST_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.COLUMN_NAME;
import static com.example.vinicius.webrestaurante.ModelLayer.UsuarioModel.PostContractUsuario.PostEntry.TABLE_NAME;

public class PostDbHelperUsuario extends SQLiteOpenHelper {

    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PostContractUsuario.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_CPF + " TEXT, " +
                    COLUMN_CREATION + " TEXT)";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final int DATABASE_VERSION = GlobalVariables.DATABASE_VERSION;
    private static final String DATABASE_NAME = GlobalVariables.DATABASE_NAME;

    public PostDbHelperUsuario(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POSTS);
        createUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private boolean customerExists(PostDbHelperUsuario dbHelper, String cpf){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_CPF
        };

        // Filter results WHERE "COLUMN_CPF" = 'cpf'
        String selection = COLUMN_CPF + " = ?";
        String[] selectionArgs = { cpf };

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // don't sort order
        );
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static String getClientFullName(PostDbHelperUsuario dbHelper, Integer clientID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PostContractUsuario.PostEntry._ID,
                COLUMN_NAME,
                COLUMN_LAST_NAME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PostContractUsuario.PostEntry._ID + " = ? ";
        String[] selectionArgs = { String.valueOf(clientID) };

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
        String FULL_NAME = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)) +
                " " + cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
        return FULL_NAME;
    }

    public long createNewCustomer(PostDbHelperUsuario dbHelper, String clienteNome, String clienteSobreNome, String clienteCPF){
        if(!customerExists(dbHelper, clienteCPF)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, clienteNome);
            values.put(COLUMN_LAST_NAME, clienteSobreNome);
            values.put(COLUMN_CPF, clienteCPF);
            values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
            // Insert the new row, returning the primary key value of the new row
            return db.insert(TABLE_NAME, null, values);
        }
        else {
            return -1;
        }
    }

    private static final String CLIENTES_COM_NENHUM_PEDIDO_EM_ABERTO =
            "SELECT a._id as " + PostContractUsuario.PostEntry._ID +
            " , a.nome as " + COLUMN_NAME +
            " , a.sobrenome as " + COLUMN_LAST_NAME +
            " , a.cpf as " + COLUMN_CPF +
            " FROM " + TABLE_NAME + " a LEFT JOIN " +
            "(SELECT cliente_id, status from " + PostContractPedido.PostEntry.TABLE_NAME
                    + " where status LIKE 'Em aberto') b " +
            "ON cast(a._id AS INTEGER) = cast(b.cliente_id AS INTEGER) " +
            "WHERE b.status IS NULL";

    public static Cursor getFreeClients(SQLiteDatabase db){
        Log.d("VINICIUS",CLIENTES_COM_NENHUM_PEDIDO_EM_ABERTO);
        return db.rawQuery(CLIENTES_COM_NENHUM_PEDIDO_EM_ABERTO, null);
    }

    public static void createUsers(SQLiteDatabase db){
        db.beginTransaction();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "Jorge");
        values.put(COLUMN_LAST_NAME, "Sullivan");
        values.put(COLUMN_CPF, "71837047232");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Anna");
        values.put(COLUMN_LAST_NAME, "Swan");
        values.put(COLUMN_CPF, "84526713248");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Karla");
        values.put(COLUMN_LAST_NAME, "Simpson");
        values.put(COLUMN_CPF, "15975385246");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Liza");
        values.put(COLUMN_LAST_NAME, "Sutton");
        values.put(COLUMN_CPF, "89764531201");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Thom");
        values.put(COLUMN_LAST_NAME, "Mayer");
        values.put(COLUMN_CPF, "14632178956");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Adeni");
        values.put(COLUMN_LAST_NAME, "Mendes");
        values.put(COLUMN_CPF, "21478526985");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Giulia");
        values.put(COLUMN_LAST_NAME, "Miranda");
        values.put(COLUMN_CPF, "13264598745");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Neemias");
        values.put(COLUMN_LAST_NAME, "Souza");
        values.put(COLUMN_CPF, "59698751258");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Ruben");
        values.put(COLUMN_LAST_NAME, "Ferreira");
        values.put(COLUMN_CPF, "45612395175");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Otávio");
        values.put(COLUMN_LAST_NAME, "Carvalho");
        values.put(COLUMN_CPF, "96314725845");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Vinícius");
        values.put(COLUMN_LAST_NAME, "Rodrigues");
        values.put(COLUMN_CPF, "95431089546");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Lucas");
        values.put(COLUMN_LAST_NAME, "Cardoso");
        values.put(COLUMN_CPF, "47237071823");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Patrick");
        values.put(COLUMN_LAST_NAME, "Betten");
        values.put(COLUMN_CPF, "74532091823");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Alex");
        values.put(COLUMN_LAST_NAME, "Austuriano");
        values.put(COLUMN_CPF, "65487532155");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Mateus");
        values.put(COLUMN_LAST_NAME, "Vidal");
        values.put(COLUMN_CPF, "46513078915");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Beatriz");
        values.put(COLUMN_LAST_NAME, "Santos");
        values.put(COLUMN_CPF, "99507154358");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Adeni");
        values.put(COLUMN_LAST_NAME, "Brito");
        values.put(COLUMN_CPF, "98474154923");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Jhonny ");
        values.put(COLUMN_LAST_NAME, "Simon");
        values.put(COLUMN_CPF, "74821532689");
        values.put(COLUMN_CREATION, String.valueOf(currentDateandTime));
        db.insert(TABLE_NAME, null, values);

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
