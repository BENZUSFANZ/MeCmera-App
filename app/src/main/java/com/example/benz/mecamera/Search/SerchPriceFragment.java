package com.example.benz.mecamera.Search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benz.mecamera.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SerchPriceFragment extends Fragment {

    Button btnStore, btnLocation, btnCategory, btnSearch;
    EditText edtHeight, edtLow;

    public SerchPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serch_price, container, false);

        btnStore = (Button) view.findViewById(R.id.btnStore);
        btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnCategory = (Button) view.findViewById(R.id.btnCategory);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        edtLow = (EditText) view.findViewById(R.id.edtLow);
        edtHeight = (EditText) view.findViewById(R.id.edtHeight);

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new SearchFragment()); //fragment class
                fr.commit();
                ///////////////////////////
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new SerchLocationFragment()); //fragment class
                fr.commit();
                ///////////////////////////
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new SerchCategoryFragment()); //fragment class
                fr.commit();
                ///////////////////////////
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dtLow = edtLow.getText().toString();
                String dtHeight = edtHeight.getText().toString();

                if (dtLow.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }else if (dtHeight.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }
                else {
                    toShow(dtLow, dtHeight);
                }




            }
        });


        return view;
    }

    public void toShow(String Low, String Height){

        Bundle Show = new Bundle();

        Show.putString("low", Low);
        Show.putString("height", Height);

        ShowSearchPriceFragment toShow = new ShowSearchPriceFragment();
        toShow.setArguments(Show);
        /////// start fragment//////
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, toShow); //fragment class
        fr.commit();
        ///////////////////////////////
    }
}
