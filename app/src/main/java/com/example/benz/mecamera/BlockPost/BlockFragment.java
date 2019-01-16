package com.example.benz.mecamera.BlockPost;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class BlockFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterBlogPost adapter;

    FloatingActionButton uploadPost;

    public BlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_block, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcvPost);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        recyclePost();

        uploadPost = (FloatingActionButton) view.findViewById(R.id.uploadPost);
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new UploadPostFragment()); //fragment class
                fr.commit();
                ///////////////////////////////
            }
        });



        //////////////////swipe Refresh///////////////////
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRf);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclePost();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }



    private void recyclePost(){

        ipConfig ip = new ipConfig();
        final String UrlBlogPost = ip.getUrlBlogPost();

        final ArrayList<BlogPostList> itemArray = new ArrayList<>();

        Ion.with(getContext())

                .load(UrlBlogPost+"PostRecycleView.php")

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;

                        for(int i = 0; i < result.size(); i++){

                            BlogPostList item = new BlogPostList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setId(jsonObject.get("id_post").getAsInt());
                            item.setCaption(jsonObject.get("caption").getAsString());
                            item.setimPost(jsonObject.get("im_post").getAsString());
                            item.setName(jsonObject.get("name").getAsString());
                            item.setimProfile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);
                        }


                        adapter = new RecyclerViewAdapterBlogPost(itemArray, getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });



    }

}
