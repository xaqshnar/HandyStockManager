package com.divani.android.handystockmanager.database;

public class Product_Type {

    public static final String TABLE_NAME = "productType";
    public static final String COLUMN_NAME_PRODUCT_TYPE_ID = "product_type_id";
    public static final String COLUMN_NAME_PRODUCT_TYPE_NAME = "product_type_name";

    private int product_type_id;
    private String product_type_name;

        public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public void setProduct_type_name(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    public Product_Type(int product_type_id, String product_type_name)
    {
        this.product_type_id=product_type_id;
        this.product_type_name=product_type_name;
    }

    public Product_Type(String product_type_name)
    {
        this.product_type_name=product_type_name;
    }

    public Product_Type() { }
}
