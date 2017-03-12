package com.divani.android.handystockmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.divani.android.handystockmanager.database.Product_Type;
import com.divani.android.handystockmanager.database.Products;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StockData extends SQLiteOpenHelper {

    private static  int DATABASE_VERSION = 1;
    private static  String DATABASE_NAME = "stockData.db";

    private static final String SQL_CREATE_ENTRIES_PRODUCT_TYPE =
            "CREATE TABLE IF NOT EXISTS " + Product_Type.TABLE_NAME + " (" +
                    Product_Type.COLUMN_NAME_PRODUCT_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Product_Type.COLUMN_NAME_PRODUCT_TYPE_NAME + " TEXT NOT NULL)";

    private static final String SQL_CREATE_ENTRIES_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS " + Products.TABLE_NAME + " (" +
                    Products.COLUMN_NAME_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Products.COLUMN_NAME_PRODUCT_TYPE_ID + " INTEGER NOT NULL, " +
                    Products.COLUMN_NAME_BRAND + " TEXT NOT NULL, " +
                    Products.COLUMN_NAME_MODEL + " TEXT NOT NULL, " +
                    Products.COLUMN_NAME_PRICE + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + Products.COLUMN_NAME_MODEL + ") REFERENCES "
                    + Product_Type.TABLE_NAME +"("+ Product_Type.COLUMN_NAME_PRODUCT_TYPE_ID + "));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Product_Type.TABLE_NAME;

    private static StockData instance = null;
    private static Context context;
    private static SQLiteDatabase db;

    /**
     * Retrieves a thread-safe instance of the singleton object
     */

    public static synchronized StockData getInstance(Context context) {
        if (instance == null) {
            instance = new StockData(context, DATABASE_NAME, null, DATABASE_VERSION);

            openConnection();
        }
        return instance;
    }

    /**
     * Constructor instantiating every member.
     */
    private StockData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        StockData.context = context;
    }

    public static void openConnection() {

        if ( db == null ){
            db = instance.getWritableDatabase();
        }
    }

    public synchronized void closeConnection() {
        if (instance != null) {
            db.close();
            instance.close();

            db = null;
            instance = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES_PRODUCT_TYPE);
        db.execSQL(SQL_CREATE_ENTRIES_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + Products.TABLE_NAME);

        onCreate(db);
    }

    //Add new Product Type
    public void addProduct_Type(Product_Type p) {

        ContentValues values = new ContentValues();
        values.put(Product_Type.COLUMN_NAME_PRODUCT_TYPE_NAME, p.getProduct_type_name()); // Shop Name

        // Inserting Row
        db.insert(Product_Type.TABLE_NAME, null, values);
    }

    public List<Product_Type> getAllProduct_Type() {

        List<Product_Type> pl = new ArrayList<Product_Type>();

        String selectQuery = "SELECT  * FROM " + Product_Type.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product_Type p = new Product_Type();
                p.setProduct_type_id(Integer.parseInt(cursor.getString(0)));
                p.setProduct_type_name(cursor.getString(1));

                pl.add(p);

            } while (cursor.moveToNext());
        }

        // return list
        return pl;
    }

    public Product_Type[] getAllProduct_TypeArr() {

        String selectQuery = "SELECT  * FROM " + Product_Type.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Product_Type[] pl = new Product_Type[cursor.getCount()+1];

        pl[0] = new Product_Type(-1, "Select"); //add the no selection text

        int i = 1;

        if (cursor.moveToFirst()) {
            do {
                Product_Type p = new Product_Type();
                p.setProduct_type_id(Integer.parseInt(cursor.getString(0)));
                p.setProduct_type_name(cursor.getString(1));

                pl[i] = p;

                i++;

            } while (cursor.moveToNext());
        }

        // return array
        return pl;
    }
}
