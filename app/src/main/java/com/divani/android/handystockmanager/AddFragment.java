package com.divani.android.handystockmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Button object
    Button addEntry;

    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Add/Update Product");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View InputFragmentView = inflater.inflate(R.layout.fragment_add, container, false);
        addEntry = (Button) InputFragmentView.findViewById(R.id.submit_product_btn);

        addEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View InputFragmentView)
            {
                if(checkFields())
                    Toast.makeText(getActivity().getApplicationContext(), "Entry added!", Toast.LENGTH_SHORT).show();
            }
        });

        return InputFragmentView;
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

    public boolean checkFields(){

        Spinner product_type = (Spinner) getView().findViewById(R.id.productType);
        Spinner brand = (Spinner) getView().findViewById(R.id.brandName);
        Spinner model_number = (Spinner) getView().findViewById(R.id.modelName);
        EditText price = (EditText) getView().findViewById(R.id.priceTxt);

        String product_txt = product_type.getSelectedItem().toString();
        String brand_txt = brand.getSelectedItem().toString();
        String model_txt = model_number.getSelectedItem().toString();
        String price_txt = price.getText().toString().trim();

        if(product_txt.isEmpty() || product_txt.length() == 0 || product_txt.equals("") || product_txt == null)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter a product type", Toast.LENGTH_SHORT).show();
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

        return true;

    }
}
