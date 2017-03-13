package com.divani.android.handystockmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.divani.android.handystockmanager.custom.SpinAdapter;
import com.divani.android.handystockmanager.custom.StringFormatter;
import com.divani.android.handystockmanager.database.Product_Type;
import com.divani.android.handystockmanager.database.Products;
import com.divani.android.handystockmanager.database.StockData;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Button object
    Button addEntry, addNewProduct;

    private OnFragmentInteractionListener mListener;

    private StockData dbHelper;

    private Product_Type pt;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Add/Update Product");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        addEntry = (Button) v.findViewById(R.id.submit_product_btn);
        addNewProduct = (Button) v.findViewById(R.id.add_new_product_btn);

        /* Initializations */
        openConnection();
        setAllFields(v);

        /* Listeners */
        addEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View InputFragmentView)
            {
                if(checkFields()) {

                    Toast.makeText(getActivity().getApplicationContext(), "Entry added!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View InputFragmentView)
            {
                Log.d("Adding: ", "Adding product entry to database");
                showDialog(inflater);
            }
        });

        return v;
    }

    public void openConnection() {

        Log.d("Opening: ", "Opening connection for new products ..");
        dbHelper = StockData.getInstance(getActivity());
    }

    public boolean checkFields(){

        Spinner product_type = (Spinner) getView().findViewById(R.id.productType);
        AutoCompleteTextView brand = (AutoCompleteTextView) getView().findViewById(R.id.brand_name_txt);
        AutoCompleteTextView model_number = (AutoCompleteTextView) getView().findViewById(R.id.model_number_txt);
        EditText price = (EditText) getView().findViewById(R.id.price_txt);

        String product_txt = product_type.getSelectedItem().toString();
        String brand_txt = brand.getText().toString().trim();
        String model_txt = model_number.getText().toString().trim();
        String price_txt = price.getText().toString().trim();

        int prod_type_id = pt.getProduct_type_id(); //product_type.getSelectedItemPosition();

        if(product_txt.isEmpty() || product_txt.length() == 0 || product_txt.equals("") || product_txt == null || product_txt.equals("Select") || prod_type_id == -1)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Please select a product type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(brand_txt.isEmpty() || brand_txt.length() == 0 || brand_txt.equals("") || brand_txt == null){

            Toast.makeText(getActivity().getApplicationContext(), "Please enter a brand", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(model_txt.isEmpty() || model_txt.length() == 0 || model_txt.equals("") || brand_txt == null){

            Toast.makeText(getActivity().getApplicationContext(), "Please enter a model number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(price_txt.isEmpty() || price_txt.length() == 0 || price_txt.equals("") || brand_txt == null){

            Toast.makeText(getActivity().getApplicationContext(), "Please enter a price", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*------------------Adding entry to database----------------*/

        Log.d("VAUESSS", StringFormatter.toTitleCase(brand_txt) + " " + StringFormatter.toTitleCase(model_txt));

        Products p = new Products(prod_type_id, StringFormatter.toTitleCase(brand_txt), StringFormatter.toTitleCase(model_txt), price_txt);

        if(dbHelper.addProducts(p)) {
            Log.d("Adding: ", "Added new product ..");
            resetFields();

            setValuesForBrand(getView());
            setValuesForModel(getView());
        }
        else
            return false;

        return true;
    }

    public void setAllFields(View v) {

        setValuesForProductSpinner(v);

        setValuesForBrand(v);
        setValuesForModel(v);
    }

    private SpinAdapter adapter;

    public void setValuesForProductSpinner(View v) {

        //Get all values from database
        Product_Type[] p = dbHelper.getAllProduct_TypeArr();

        if(p !=null && p.length > 0) {

            Spinner spinner = (Spinner) v.findViewById(R.id.productType);

            adapter = new SpinAdapter(this.getActivity(), android.R.layout.simple_spinner_item, p);

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                    //get the current item that is selected by its position
                    pt = adapter.getItem(position);
                    //do whatever you want with it
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void setValuesForBrand(View v) {

        String[] values = dbHelper.getDistinctData(Products.COLUMN_NAME_BRAND, Products.TABLE_NAME);

        if(values != null && values.length > 0){

            AutoCompleteTextView e = (AutoCompleteTextView) v.findViewById(R.id.brand_name_txt);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_item, values);
            e.setThreshold(1);
            e.setAdapter(adapter);
        }
    }

    public void setValuesForModel(View v) {

        String[] values = dbHelper.getDistinctData(Products.COLUMN_NAME_MODEL, Products.TABLE_NAME);

        if(values != null && values.length > 0) {

            AutoCompleteTextView e = (AutoCompleteTextView) v.findViewById(R.id.model_number_txt);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_item, values);
            e.setThreshold(1);
            e.setAdapter(adapter);
        }
    }

    public void resetFields() {

        Spinner product_type = (Spinner) getView().findViewById(R.id.productType);
        AutoCompleteTextView brand = (AutoCompleteTextView) getView().findViewById(R.id.brand_name_txt);
        AutoCompleteTextView model_number = (AutoCompleteTextView) getView().findViewById(R.id.model_number_txt);
        EditText price = (EditText) getView().findViewById(R.id.price_txt);

        product_type.setSelection(0);
        brand.setText("");
        model_number.setText("");
        price.setText("");
    }

    public void showDialog(LayoutInflater inflater) {

        final View DIALOG_VIEW = inflater.inflate(R.layout.add_product_dialog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Add new Product Type");
        alertDialog.setIcon(R.drawable.ic_menu_add);
        alertDialog.setView(DIALOG_VIEW);

        final AlertDialog ad = alertDialog.show();

        Button add = (Button) DIALOG_VIEW.findViewById(R.id.new_product_type_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText prod_type = (EditText) DIALOG_VIEW.findViewById(R.id.new_product_type_txt);
                String prod_type_txt = prod_type.getText().toString().trim();

                if(prod_type_txt != null && !prod_type_txt.trim().equals("") && prod_type_txt.length() != 0) {

                    Product_Type product_type = new Product_Type(prod_type_txt);

                    if(dbHelper.addProduct_Type(product_type)) {

                        Toast.makeText(DIALOG_VIEW.getContext(), "New product type added!", Toast.LENGTH_SHORT).show();
                        ad.dismiss();

                        Log.d("Adding: ", "Added new product type..");

                        //reload spinner
                        setValuesForProductSpinner(getView());
                    }
                    else {
                        Toast.makeText(DIALOG_VIEW.getContext(), "Failed to add. Product Type may already exist", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(DIALOG_VIEW.getContext(), "Enter product type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
