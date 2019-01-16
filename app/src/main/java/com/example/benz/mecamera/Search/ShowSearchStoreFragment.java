package com.example.benz.mecamera.Search;


import android.os.Bundle;
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
import com.example.benz.mecamera.Search.RecyclerViewAdapterSearchStore;
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
public class ShowSearchStoreFragment extends Fragment {

    public static String dtSearch;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterSearchStore adapter;


    public ShowSearchStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_search_store, container, false);

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

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcvShowStore);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {

            Bundle Show = getArguments();

            dtSearch= Show.getString("Search");

        } catch (Exception e) {
            e.printStackTrace();
        }

        recycleShowStore();

       // Toast.makeText(getActivity(), dtSearch, Toast.LENGTH_SHORT).show();

        return view;
    }

    private void recycleShowStore() {

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlSearch();

        final ArrayList<SearchList> itemArray = new ArrayList<>();

        Ion.with(getContext())

                .load(Url+"SearchStore.php")

                .setMultipartParameter("SearchStore", dtSearch)

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


                        adapter = new RecyclerViewAdapterSearchStore(itemArray, getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

}
