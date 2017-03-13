package com.divani.android.handystockmanager.database;

public class Brand {

    public static final String TABLE_NAME = "brand";
    public static final String COLUMN_NAME_BRAND_ID = "brand_id";
    public static final String COLUMN_NAME_BRAND_NAME = "brand_name";

    private int brand_id;
    private String brand_name;

    public int getBrand_id() {
        return brand_id;
    }
    public String getBrand_name() {
        return brand_name;
    }
    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }
    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }



}
