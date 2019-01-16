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
public class SerchCategoryFragment extends Fragment {

    Button btnStore, btnLocation, btnPrice, btnSearch;
    EditText edtCategory, edtBrand, edtAge;

    public SerchCategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serch_category, container, false);

        btnStore = (Button) view.findViewById(R.id.btnStore);
        btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnPrice = (Button) view.findViewById(R.id.btnPrice);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        edtCategory = (EditText) view.findViewById(R.id.edtCategory);
        edtBrand = (EditText) view.findViewById(R.id.edtBrand);
        edtAge = (EditText) view.findViewById(R.id.edtAge);

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

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new SerchPriceFragment()); //fragment class
                fr.commit();
                ///////////////////////////
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dtCategory = edtCategory.getText().toString();
                String dtBrand = edtBrand.getText().toString();
                String dtAge = edtAge.getText().toString();

                if (dtCategory.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }else if (dtBrand.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }else if (dtAge.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }
                else {
                    toShow(dtCategory, dtBrand, dtAge);
                }




            }
        });

        return view;
    }

    public void toShow(String Category, String Brand, String Age){

        Bundle Show = new Bundle();

        Show.putString("Category", Category);
        Show.putString("Brand", Brand);
        Show.putString("Age", Age);

        ShowSearchCategoryFragment toShow = new ShowSearchCategoryFragment();
        toShow.setArguments(Show);
        /////// start fragment//////
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, toShow); //fragment class
        fr.commit();
        ///////////////////////////////
    }
}
