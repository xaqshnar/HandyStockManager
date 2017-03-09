package com.divani.android.handystockmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StockData extends SQLiteOpenHelper {

    public static  int DATABASE_VERSION = 1;
    public static  String DATABASE_NAME = "stockData.db";

    private static final String SQL_CREATE_ENTRIES_PRODUCT =
            "CREATE TABLE IF NOT EXISTS " + ColumnReader.ProductType.TABLE_NAME + " (" +
                    ColumnReader.ProductType.COLUMN_NAME_PRODUCT_ID + " INT PRIMARY KEY, " +
                    ColumnReader.ProductType.COLUMN_NAME_PRODUCT_NAME + " TEXT)";

    private static final String SQL_CREATE_ENTRIES_BRAND =
            "CREATE TABLE IF NOT EXISTS " + ColumnReader.Brand.TABLE_NAME + " (" +
                    ColumnReader.Brand.COLUMN_NAME_BRAND_ID + " INT NOT NULL, " +
                    ColumnReader.Brand.COLUMN_NAME_BRAND_NAME + " TEXT," +
                    ColumnReader.Brand.COLUMN_NAME_PRODUCT_ID + " INT, " +
                    "FOREIGN KEY ("+ColumnReader.Brand.COLUMN_NAME_PRODUCT_ID+ ") REFERENCES"+
                    ColumnReader.ProductType.TABLE_NAME + "("+ColumnReader.ProductType._ID+")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ColumnReader.ProductType.TABLE_NAME;

    public StockData(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES_BRAND);
        db.execSQL(SQL_CREATE_ENTRIES_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
