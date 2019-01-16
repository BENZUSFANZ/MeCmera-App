package com.example.benz.mecamera.Home;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterStore adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcvStore);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        recycleStore();

        //////////////////swipe Refresh///////////////////
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRf);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recycleStore();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }



    private void recycleStore(){

        ipConfig ip = new ipConfig();
        final String UrlrcvStore = ip.getUrlrcvStore();


        final ArrayList<StoreList> itemArray = new ArrayList<>();

        Ion.with(getContext())

                .load(UrlrcvStore+"StoreRecycleView.php")

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;

                        for(int i = 0; i < result.size(); i++){

                            StoreList item = new StoreList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setId(jsonObject.get("id_store").getAsInt());
                            item.setCaption(jsonObject.get("caption").getAsString());
                            item.setPrice(jsonObject.get("price").getAsString());
                            item.setimStore(jsonObject.get("im_store").getAsString());
                            item.setName(jsonObject.get("name").getAsString());
                            item.setimProfile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);
                        }

                        adapter = new RecyclerViewAdapterStore(itemArray, getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });



    }

}
