package com.divani.android.handystockmanager.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.divani.android.handystockmanager.database.Product_Type;
import com.divani.android.handystockmanager.database.Products;

public class ProductAdapter extends ArrayAdapter {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private Products[] values;

    public ProductAdapter(Context context, int textViewResourceId,
                       Products[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public Products getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }

    //"passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);

        // Then you can get the current item using the values array and the current position
        // You can NOW reference each method you have created in your bean object
        label.setText(values[position].getModel_name());
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // Drop down state
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values[position].getModel_name());
        label.setTextSize(18);
        label.setPadding(10,10,10,10);

        return label;
    }
}
