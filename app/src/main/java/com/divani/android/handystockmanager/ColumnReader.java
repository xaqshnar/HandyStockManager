package com.divani.android.handystockmanager;

import android.provider.BaseColumns;

/**
 * Created by Aniruddha on 1/4/2017.
 */

public final class ColumnReader {

    private ColumnReader(){}

    public static class ProductType implements BaseColumns {

        public static final String TABLE_NAME = "productType";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
    }

    public static class Brand implements BaseColumns {

        public static final String TABLE_NAME = "brandType";
        public static final String COLUMN_NAME_BRAND_ID = "brand_id";
        public static final String COLUMN_NAME_BRAND_NAME = "brand_name";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
    }

    public static class Model implements BaseColumns {

        public static final String TABLE_NAME = "modelDetails";
        public static final String COLUMN_NAME_MODEL_ID = "moddel_id";
        public static final String COLUMN_NAME_MODEL_NAME = "model_name";
        public static final String COLUMN_NAME_MODEL_NUMBER = "model_number";
    }


}
