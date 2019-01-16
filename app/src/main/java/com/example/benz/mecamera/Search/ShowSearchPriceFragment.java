package com.example.benz.mecamera.Search;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benz.mecamera.R;
import com.example.benz.mecamera.Search.RecyclerViewAdapterSearchPrice;
import com.example.benz.mecamera.Search.SearchFragment;
import com.example.benz.mecamera.Search.SearchList;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSearchPriceFragment extends Fragment {

    public static String dtLow, dtHeight;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterSearchPrice adapter;


    public ShowSearchPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_search_price, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tb = (Toolbar)view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tb);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new SearchFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcvShowPrice);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังโหลด...");
        progressDialog.show();

        try {

            Bundle Show = getArguments();

            dtLow= Show.getString("low");
            dtHeight= Show.getString("height");

        } catch (Exception e) {
            e.printStackTrace();
        }

        recycleShowPrice();

        // set ให้รอ
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000); //1วินาที

        return view;
    }

    private void recycleShowPrice() {

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlSearch();

        final ArrayList<SearchList> itemArray = new ArrayList<>();

        Ion.with(getContext())

                .load(Url+"SearchPrice.php")

                .setMultipartParameter("low", dtLow)
                .setMultipartParameter("height", dtHeight)

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;

                        for(int i = 0; i < result.size(); i++){

                            SearchList item = new SearchList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setId(jsonObject.get("id_store").getAsInt());
                            item.setCaption(jsonObject.get("caption").getAsString());
                            item.setPrice(jsonObject.get("price").getAsString());
                            item.setImStore(jsonObject.get("im_store").getAsString());
                            item.setName(jsonObject.get("name").getAsString());
                            item.setImProfile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);
                        }

                        adapter = new RecyclerViewAdapterSearchPrice(itemArray, getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

}
