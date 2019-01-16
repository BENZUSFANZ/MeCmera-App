package com.example.benz.mecamera.Search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.benz.mecamera.GoogleMapsApi.GoogleMapsApiSearchActivity;
import com.example.benz.mecamera.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class SerchLocationFragment extends Fragment {

    Button btnStore, btnCategory, btnPrice;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;


    public SerchLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serch_location, container, false);

        btnStore = (Button) view.findViewById(R.id.btnStore);
        btnCategory = (Button) view.findViewById(R.id.btnCategory);
        btnPrice = (Button) view.findViewById(R.id.btnPrice);

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


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        startActivity(new Intent(getContext(), GoogleMapsApiSearchActivity.class));

                    }
                });

            }
        });


        return view;
    }




}
