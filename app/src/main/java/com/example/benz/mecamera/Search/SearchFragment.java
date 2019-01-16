package com.example.benz.mecamera.Search;


import android.app.ProgressDialog;
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
public class SearchFragment extends Fragment {

    Button btnCategory, btnLocation, btnPrice, btnSearch;
    EditText edtStore;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        btnCategory = (Button) view.findViewById(R.id.btnCategory);
        btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnPrice = (Button) view.findViewById(R.id.btnPrice);
        btnSearch = (Button) view.findViewById(R.id.btnSearch); 
        
        edtStore = (EditText) view.findViewById(R.id.edtStore); 

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

                String dtSearch = edtStore.getText().toString();

                if (dtSearch.isEmpty()) {

                    Toast.makeText(getActivity(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

                }else {

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("กำลังค้นหา...");
                    progressDialog.show();

                    toShow(dtSearch);

                    progressDialog.dismiss();

                }

            }
        });
        
        return view;
    }

    public void toShow(String search){

        Bundle Show = new Bundle();

        Show.putString("Search", search);

        ShowSearchStoreFragment toShow = new ShowSearchStoreFragment();
        toShow.setArguments(Show);
        /////// start fragment//////
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, toShow); //fragment class
        fr.commit();
        ///////////////////////////////

    }
    
}
