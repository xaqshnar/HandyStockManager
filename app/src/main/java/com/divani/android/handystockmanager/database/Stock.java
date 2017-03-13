package com.divani.android.handystockmanager.database;

/**
 * Created by ADMIN on 13-Mar-17.
 */

public class Stock {

    public static final String TABLE_NAME = "stock";
    public static final String COLUMN_NAME_STOCK_ID = "stock_id";
    public static final String COLUMN_NAME_PRODUCT_ID = "p_id";
    public static final String COLUMN_NAME_BOUGHT_QTY = "bought_qty";
    public static final String COLUMN_NAME_SOLD_QTY = "sold_qty";
    public static final String COLUMN_NAME_REMAINING_QTY = "remaining_qty";

    private int stock_id;
    private int p_id;
    private int bought_qty;
    private int sold_qty;
    private int remaining_qty;

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getBought_qty() {
        return bought_qty;
    }

    public void setBought_qty(int bought_qty) {
        this.bought_qty = bought_qty;
    }

    public int getSold_qty() {
        return sold_qty;
    }

    public void setSold_qty(int sold_qty) {
        this.sold_qty = sold_qty;
    }

    public int getRemaining_qty() {
        return remaining_qty;
    }

    public void setRemaining_qty(int remaining_qty) {
        this.remaining_qty = remaining_qty;
    }

    public Stock() { }

    public Stock(int p_id, int bought_qty, int sold_qty, int remaining_qty) {

        this.p_id = p_id;
        this.bought_qty = bought_qty;
        this.sold_qty = sold_qty;
        this.remaining_qty = remaining_qty;
    }
}
