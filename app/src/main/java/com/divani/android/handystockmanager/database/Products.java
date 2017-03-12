package com.divani.android.handystockmanager.database;

public class Products {

    public static final String TABLE_NAME = "products";
    public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
    public static final String COLUMN_NAME_PRODUCT_TYPE_ID = "p_id";
    public static final String COLUMN_NAME_BRAND = "brand_name";
    public static final String COLUMN_NAME_MODEL = "model_name";
    public static final String COLUMN_NAME_PRICE = "price";

    private int product_id;
    private int p_id;
    private String brand_name;
    private String model_name;
    private String price;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Products() {

    }

    public Products(int p_id, String brand_name, String model_name, String price) {

        this.p_id = p_id;
        this.brand_name = brand_name;
        this.model_name =  model_name;
        this.price = price;

    }
}
