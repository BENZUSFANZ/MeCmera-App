package com.example.benz.mecamera.MyStore;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyStoreFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterMyStore adapter;

    FloatingActionButton uploadStore;



    public MyStoreFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_store, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tbMyStore = (Toolbar)view.findViewById(R.id.tbMyStore);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbMyStore);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbMyStore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

        uploadStore = (FloatingActionButton) view.findViewById(R.id.uploadStore);
        uploadStore.setBackgroundColor(R.color.colorPrimary);
        uploadStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new StoreFragment()); //fragment class
                fr.commit();
                ///////////////////////////////
            }
        });

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcvMyStore);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังโหลด...");
        progressDialog.show();

        recycleMyStore();

        // set ให้รอ
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000); //2วินาที

        //////////////////swipe Refresh///////////////////
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRf);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recycleMyStore();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    private void recycleMyStore() {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        ipConfig ip = new ipConfig();
        final String UrlrcvMyStore = ip.getUrlrcvMyStore();


        final ArrayList<MyStoreList> itemArray = new ArrayList<>();

        Ion.with(getContext())

                .load(UrlrcvMyStore+"MyStoreRecycleView.php")

                .setMultipartParameter("ID_user",id_user)

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;
                        for(int i = 0; i < result.size(); i++){

                            MyStoreList item = new MyStoreList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setId(jsonObject.get("id_store").getAsInt());
                            item.setCaption(jsonObject.get("caption").getAsString());
                            item.setPrice(jsonObject.get("price").getAsString());
                            item.setimStore(jsonObject.get("im_store").getAsString());
                            item.setName(jsonObject.get("name").getAsString());
                            item.setimProfile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);

                        }

                        adapter = new RecyclerViewAdapterMyStore(itemArray, getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });



    }

}
