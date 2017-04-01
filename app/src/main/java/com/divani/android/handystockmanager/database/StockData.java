package com.divani.android.handystockmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
                    Product_Type.COLUMN_NAME_PRODUCT_TYPE_NAME + " TEXT NOT NULL," +
                    "UNIQUE( " + Product_Type.COLUMN_NAME_PRODUCT_TYPE_NAME + " COLLATE NOCASE));";

    private static final String SQL_CREATE_ENTRIES_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS " + Products.TABLE_NAME + " (" +
                    Products.COLUMN_NAME_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Products.COLUMN_NAME_PRODUCT_TYPE_ID + " INTEGER NOT NULL, " +
                    Products.COLUMN_NAME_BRAND + " TEXT NOT NULL, " +
                    Products.COLUMN_NAME_MODEL + " TEXT NOT NULL, " +
                    Products.COLUMN_NAME_PRICE + " TEXT NOT NULL, " +
                    "UNIQUE( " + Products.COLUMN_NAME_BRAND + ", " +
                    Products.COLUMN_NAME_MODEL + ", " + Products.COLUMN_NAME_PRODUCT_TYPE_ID + "), " +
                    "FOREIGN KEY (" + Products.COLUMN_NAME_MODEL + ") REFERENCES "
                    + Product_Type.TABLE_NAME +"("+ Product_Type.COLUMN_NAME_PRODUCT_TYPE_ID + "));";

    private static final String SQL_CREATE_ENTRIES_STOCK =
            "CREATE TABLE IF NOT EXISTS " + Stock.TABLE_NAME + " (" +
                    Stock.COLUMN_NAME_STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Stock.COLUMN_NAME_PRODUCT_ID + " INTEGER NOT NULL, " +
                    Stock.COLUMN_NAME_BOUGHT_QTY + " INTEGER NOT NULL, " +
                    Stock.COLUMN_NAME_SOLD_QTY+ " INTEGER NOT NULL, " +
                    Stock.COLUMN_NAME_REMAINING_QTY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + Stock.COLUMN_NAME_PRODUCT_ID + ") REFERENCES "
                    + Products.TABLE_NAME +"("+ Products.COLUMN_NAME_PRODUCT_ID + "));";

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
            //insertDummyData();
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
        db.execSQL(SQL_CREATE_ENTRIES_STOCK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //db.execSQL(SQL_DELETE_ENTRIES);
        //db.execSQL("DROP TABLE IF EXISTS " + Products.TABLE_NAME);

        //onCreate(db);
    }

    /*--------------------------------------Product Type --------------------------------------------------*/

    public boolean addProduct_Type(Product_Type p) {

        boolean res = false;

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(Product_Type.COLUMN_NAME_PRODUCT_TYPE_NAME, p.getProduct_type_name());

        long ret = db.insertWithOnConflict(Product_Type.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if(ret > -1) {

            Log.d("INSERT", "Inserted new Product Type " + ret);

            db.setTransactionSuccessful();
            res =  true;
        }

        db.endTransaction();
        return res;
    }

    public Product_Type[] getAllProduct_TypeArr() {

        String selectQuery = "SELECT * FROM " + Product_Type.TABLE_NAME;

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

        return pl;
    }

    /*-------------------------------------------Products--------------------------------------------------*/

    public boolean addProducts(Products p) {

        long result = -2;
        boolean res = false;

        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(Products.COLUMN_NAME_PRODUCT_TYPE_ID, p.getP_id());
            values.put(Products.COLUMN_NAME_BRAND, p.getBrand_name());
            values.put(Products.COLUMN_NAME_MODEL, p.getModel_name());
            values.put(Products.COLUMN_NAME_PRICE, p.getPrice());

            result = db.insertOrThrow(Products.TABLE_NAME, null, values);
            Log.d("INSERT", "Inserted new products " + result);

            db.setTransactionSuccessful();
            res = true;

        } catch (Exception ex) {

            if(db.inTransaction())
                db.endTransaction();

            Log.d("EXCEPTION", "Caught exception while inserting Products");

            if (result == -2)
                if (updateProducts(p))
                    res = true;

        } finally {

            if(db.inTransaction())
                db.endTransaction();
        }

        return res;
    }

    public boolean updateProducts(Products p) {

        boolean res = false;

        db.beginTransaction();

        ContentValues cv = new ContentValues();

        cv.put(Products.COLUMN_NAME_PRICE, p.getPrice());

        long result = db.update(Products.TABLE_NAME, cv, Products.COLUMN_NAME_BRAND + "='" + p.getBrand_name()
                + "' AND " + Products.COLUMN_NAME_MODEL + "='" + p.getModel_name()
                + "' AND " + Products.COLUMN_NAME_PRODUCT_TYPE_ID + "='" + p.getP_id() + "'" , null); //returns number of rows affected

        Log.d("UPDATE", "Updating of products returned " + result);

        if(result > 0) {
            db.setTransactionSuccessful();
            res =  true;
        }

        db.endTransaction();

        return res;
    }

    public Products[] getAllProductsArr() {

        String selectQuery = "SELECT * FROM " + Products.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Products[] pl = new Products[cursor.getCount()];

        int i = 0;

        if (cursor.moveToFirst()) {
            do {

                Products p = new Products();
                p.setProduct_id(Integer.parseInt(cursor.getString(0)));
                p.setP_id(Integer.parseInt(cursor.getString(1)));
                p.setBrand_name(cursor.getString(2));
                p.setModel_name(cursor.getString(3));
                p.setPrice(cursor.getString(4));

                pl[i] = p;

                i++;

            } while (cursor.moveToNext());
        }

        // return array
        return pl;
    }


    public Products[] getAllProductsArr(String brand) {

        String selectQuery = "SELECT * FROM " + Products.TABLE_NAME + " WHERE " + Products.COLUMN_NAME_BRAND + " like '" + brand + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        Products[] pl = new Products[cursor.getCount()];

        int i = 0;

        if (cursor.moveToFirst()) {
            do {

                Products p = new Products();
                p.setProduct_id(Integer.parseInt(cursor.getString(0)));
                p.setP_id(Integer.parseInt(cursor.getString(1)));
                p.setBrand_name(cursor.getString(2));
                p.setModel_name(cursor.getString(3));
                p.setPrice(cursor.getString(4));

                pl[i] = p;

                i++;

            } while (cursor.moveToNext());
        }

        // return array
        return pl;
    }

    /*-----------------------------------------------------------------------------*/
    public String[] getDistinctData(String column, String table) {

        String selectQuery = "SELECT DISTINCT " + column + " FROM " + table;

        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] b = new String[cursor.getCount()];

        int i = 0;

        if (cursor.moveToFirst()) {
            do {

                b[i] = cursor.getString(0);
                i++;

            } while (cursor.moveToNext());
        }

        // return array
        return b;
    }
    /*-----------------------------------------------------------------------------*/

    public String[] getDataForSpinner(String column, String table, String key_column, String key) {

        String selectQuery = "SELECT DISTINCT " + column +","+ key_column + " FROM " + table + " WHERE " + key_column + " LIKE " + "'"+key+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] list = new String[cursor.getCount()+1];
        list[0] = "Select";
        int i = 1;
        if (cursor.moveToFirst()) {
            do {

                list[i] = cursor.getString(0);
                i++;

            } while (cursor.moveToNext());
        }
        return list;
    }

    public static void insertDummyData() {

        //insert dummy values
        Log.d("Inserting: ", "Inserting data ..");
        instance.addProduct_Type(new Product_Type("Mobile"));
        instance.addProduct_Type(new Product_Type("TV"));
        instance.addProduct_Type(new Product_Type("Phablet"));
        instance.addProduct_Type(new Product_Type("Tablet"));
        instance.addProduct_Type(new Product_Type("Laptop"));

        instance.addProducts(new Products(1, "Samsung", "Galaxy Note", "35000"));
        instance.addProducts(new Products(1, "Micromax", "Canvas", "25000"));
        instance.addProducts(new Products(4, "Lenovo", "Yoga Tab 3", "16000"));
        instance.addProducts(new Products(2, "Kenstar", "SmartTV", "53000"));
        instance.addProducts(new Products(1, "Moto", "G4", "37000"));
        instance.addProducts(new Products(3, "Lenovo", "Phab+", "28000"));

    }

    public static void addBoughtEntry(Products selected_product, int quantity, String action) {
        Log.d("Inserting: ", quantity+" Products bought");

        long result = -2;
        int alreadyBought, stockRemaining, alreadySold;
        int bought = 0, sold = 0;
        ContentValues values = new ContentValues();
        String selectQuery;
        Cursor cursor;

        selectQuery = "SELECT DISTINCT "+Stock.COLUMN_NAME_BOUGHT_QTY+", "+Stock.COLUMN_NAME_REMAINING_QTY+", "+Stock.COLUMN_NAME_SOLD_QTY+
                " FROM " + Stock.TABLE_NAME +
                " WHERE " + Stock.COLUMN_NAME_PRODUCT_ID + " LIKE " + "'"+selected_product.getProduct_id()+"'";
        cursor = db.rawQuery(selectQuery, null);

        alreadyBought = cursor.getInt(0);
        stockRemaining = cursor.getInt(1);
        alreadySold = cursor.getInt(2);

        switch (action) {

            case "bought":
                bought = quantity;
                sold = 0;

                if (cursor.getCount() == 0) {
            /*
            *   Create new entry in the table
            * */
                    try {
                        Log.d("Adding", "new entry");
                        db.beginTransaction();

                        values.put(Stock.COLUMN_NAME_PRODUCT_ID, selected_product.getProduct_id());
                        values.put(Stock.COLUMN_NAME_BOUGHT_QTY, bought);
                        values.put(Stock.COLUMN_NAME_REMAINING_QTY, bought);
                        values.put(Stock.COLUMN_NAME_SOLD_QTY, sold);

                        result = db.insertOrThrow(Stock.TABLE_NAME, null, values);
                        db.setTransactionSuccessful();
                        db.endTransaction();

                    } catch (Exception ex) {

                        if(db.inTransaction()) {
                            db.endTransaction();
                        }
                    }
                    return;
                } else {

                    if (cursor.moveToFirst()) {

                        bought = bought + alreadyBought;
                        stockRemaining = bought;

                        Log.d("Already Bought ", Integer.toString(alreadyBought));
                        Log.d("Stock remaining ", Integer.toString(stockRemaining));

                        values.put(Stock.COLUMN_NAME_BOUGHT_QTY, bought);
                        values.put(Stock.COLUMN_NAME_REMAINING_QTY, stockRemaining);
                    }
                }

                break;
            case "sold":
                sold = quantity;

                if (cursor.getCount() == 0) {
                    Toast.makeText(context, "Stock not available", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (cursor.moveToFirst()) {

                        if(stockRemaining == 0) {
                            Toast.makeText(context, "Stock not available", Toast.LENGTH_SHORT).show();
                            return;

                        } else if(stockRemaining < sold) {
                            Toast.makeText(context, "Not enough stock", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            bought = alreadyBought - sold;
                            sold = alreadySold + sold;
                            stockRemaining = bought;

                            Log.d("Already Sold ", Integer.toString(alreadySold));
                            Log.d("Stock remaining ", Integer.toString(stockRemaining));
                            Log.d("Stock Sold ", Integer.toString(sold));

                            values.put(Stock.COLUMN_NAME_BOUGHT_QTY, bought);
                            values.put(Stock.COLUMN_NAME_REMAINING_QTY, stockRemaining);
                            values.put(Stock.COLUMN_NAME_SOLD_QTY, sold);

                        }
                    }
                }

                break;
        }

        if (cursor.getCount() > 0){

            /*
            *  Update existing entry in the table
            * */

            try {
                Log.d("Updating", "existing entry");
                db.beginTransaction();

                result = db.update(Stock.TABLE_NAME, values, Stock.COLUMN_NAME_PRODUCT_ID + "='" + selected_product.getProduct_id() + "'", null);

                if (result > 0) {
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
            }catch (Exception ex) {
                if(db.inTransaction()) {
                    db.endTransaction();
                }
            }

        }

    }


}
