package com.example.benz.mecamera.Notifications;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.benz.mecamera.R.color.colorPrimary;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private ListView mListView;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mListView = (ListView)view.findViewById(R.id.rcvNotificationStore);

        /////////// button back in fragment ////////////////////
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar );
        appCompatActivity.getSupportActionBar().setTitle("เเจ้งเตือน");
        toolbar.setTitleTextColor(Color.GRAY);

        recycleNotificationStore();

        //////////////////swipe Refresh///////////////////
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRf);
        swipeRefreshLayout.setColorSchemeResources(colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recycleNotificationStore();

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        return view;
    }

    private void recycleNotificationStore(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, null);

        ipConfig ip = new ipConfig();
        final String UrlNoti = ip.getUrlNotistore();

        final ArrayList<NotiList> itemArray = new ArrayList<>();

        // รับข้อมูล เจ้าของโพสต์เข่ามา
        Ion.with(getContext())

                .load(UrlNoti+"Notifications.php")

                .setMultipartParameter("ID_user",id_user)

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;

                        for(int i = 0; i < result.size(); i++){

                            NotiList item = new NotiList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setStatus(jsonObject.get("status").getAsInt());
                            item.setId_booking(jsonObject.get("id_booking").getAsInt());
                            item.setId_store(jsonObject.get("id_store").getAsInt());
                            item.setUser_fk(jsonObject.get("user_fk").getAsInt());
                            item.setCustomer_fk(jsonObject.get("customer_fk").getAsInt());

                            item.setName(jsonObject.get("name").getAsString());
                            item.setIm_profile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);
                        }

                        ListViewAdapterNotificationStore listViewAdapterNotificationStore = new ListViewAdapterNotificationStore(getContext(), itemArray, mListView);

                        mListView.setAdapter(listViewAdapterNotificationStore);
                        mListView.setClickable(false);

                        // คลิกไปหน้าอื่น
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                //get มาจาก ArrayList
                                toDeatail(itemArray.get(position).getStatus(), itemArray.get(position).getId_booking(), itemArray.get(position).getId_store(),
                                        itemArray.get(position).getUser_fk(), itemArray.get(position).getCustomer_fk(), itemArray.get(position).getName(),
                                        itemArray.get(position).getIm_profile());
                            }

                        });
                    }
                });
    }

    private void toDeatail(int status, int id_booking, int id_store, int user_fk, int customer_fk, String name, String im_profile) {

        Bundle bundle = new Bundle();

        bundle.putInt("status", status);
        bundle.putInt("id_booking", id_booking);
        bundle.putInt("id_store", id_store);
        bundle.putInt("user_fk", user_fk);
        bundle.putInt("customer_fk", customer_fk);
        bundle.putString("name", name);
        bundle.putString("im_profile", im_profile);

        DetailBookingFragment detailBookingFragment = new DetailBookingFragment();
        detailBookingFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, detailBookingFragment);
        fragmentTransaction.commit();
    }
}
