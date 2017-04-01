package com.divani.android.handystockmanager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.divani.android.handystockmanager.custom.ProductAdapter;
import com.divani.android.handystockmanager.custom.SpinAdapter;
import com.divani.android.handystockmanager.database.Product_Type;
import com.divani.android.handystockmanager.database.Products;
import com.divani.android.handystockmanager.database.StockData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private StockData dbHelper;
    private Product_Type pt;
    private Products selected_product = null;
    private SpinAdapter adapter;

    public SellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellFragment.
     */
    public static SellFragment newInstance(String param1, String param2) {
        SellFragment fragment = new SellFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Sell Stock");

        String[] defaultValue = {"Select"};

        ArrayAdapter<String> defaultList = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_item, defaultValue);
        Spinner brand_spinner = (Spinner) view.findViewById(R.id.brand_spinner);
        Spinner model_spinner = (Spinner) view.findViewById(R.id.model_spinner);
        Button sold_button = (Button) view.findViewById(R.id.sold_button);
        final EditText quantity = (EditText) view.findViewById(R.id.quantity_txt);

        brand_spinner.setAdapter(defaultList);
        model_spinner.setAdapter(defaultList);

        sold_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_product != null) {
                    if (quantity.getText() != null && quantity.getText().length() > 0) {
                        try {
                            StockData.addBoughtEntry(selected_product, Integer.parseInt(quantity.getText().toString()), "sold");

                        } catch (NumberFormatException ex) {
                            Toast.makeText(getContext(), "Invalid quantity entered..Enter valid number",Toast.LENGTH_LONG).show();
                            quantity.setText("");
                        }

                    }else {
                        Toast.makeText(getContext(), "Enter quantity bought", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getContext(), "Please select model number..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openConnection();
        setProducts(view);

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
        void onFragmentInteraction(Uri uri);
    }

    public void openConnection() {

        Log.d("Opening: ", "Opening connection for new products ..");
        dbHelper = StockData.getInstance(getActivity());
    }

    public void setProducts(View v) {

        //Get all values from database

        Product_Type[] p = dbHelper.getAllProduct_TypeArr();

        if(p !=null && p.length > 0) {

            Spinner spinner = (Spinner) v.findViewById(R.id.prod_type_spinner);

            adapter = new SpinAdapter(this.getActivity(), R.layout.spinner_layout, p);

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                    //get the current item that is selected by its position
                    pt = adapter.getItem(position);
                    //do whatever you want with it
                    setBrands(getView());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public void setBrands(View v) {
        final String[] brand_values = dbHelper.getDataForSpinner(Products.COLUMN_NAME_BRAND, Products.TABLE_NAME,
                Products.COLUMN_NAME_PRODUCT_TYPE_ID, Integer.toString(pt.getProduct_type_id()));
        final Spinner brand_spinner = (Spinner) v.findViewById(R.id.brand_spinner);

        if(brand_values != null && brand_values.length > 0) {
            ArrayAdapter<String> brandList = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, brand_values){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);
                    TextView label = new TextView(getContext());
                    label.setText(brand_values[position]);
                    label.setTextColor(Color.BLACK);
                    label.setTextSize(18);

                    // And finally return your dynamic (or custom) view for each spinner item
                    return label;
                }

                // Drop down state
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    TextView label = new TextView(getContext());
                    label.setText(brand_values[position]);
                    label.setTextColor(Color.BLACK);
                    label.setTextSize(18);
                    label.setPadding(10,10,10,10);

                    return label;
                }
            };
            brand_spinner.setAdapter(brandList);
        }

        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String brand_selected = (String) brand_spinner.getSelectedItem();
                setModels(getView(), brand_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setModels(View v, String brand) {
        Products[] products = dbHelper.getAllProductsArr(brand);
        Products[] new_products = new Products[products.length + 1];
        new_products[0] = new Products(-1, null, "Select", null);
        System.arraycopy(products, 0, new_products, 1, products.length);

        final Spinner model_spinner = (Spinner) v.findViewById(R.id.model_spinner);
        if(products != null && products.length > 0) {
            ProductAdapter productList = new ProductAdapter(getContext(), R.layout.spinner_layout, new_products);

            if (productList != null) {
                model_spinner.setAdapter(productList);
            }
        }

        model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selected_product = (Products) model_spinner.getSelectedItem();
                Log.d("Item selected ", selected_product.getModel_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
