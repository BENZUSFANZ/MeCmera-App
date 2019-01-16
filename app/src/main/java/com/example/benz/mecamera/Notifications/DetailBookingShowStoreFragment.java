package com.example.benz.mecamera.Notifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailBookingShowStoreFragment extends Fragment {

    TextView tvName, tvLastname, tvEmail, tvTel;
    CircleImageView imProfile;

    int id_user;

    public DetailBookingShowStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_detail_booking_show_store, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new DetailBookingFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

       tvName = (TextView) view.findViewById(R.id.tvName);
       tvLastname = (TextView) view.findViewById(R.id.tvLastname);
       tvEmail = (TextView) view.findViewById(R.id.tvEmail);
       tvTel = (TextView) view.findViewById(R.id.tvTel);

       imProfile = (CircleImageView) view.findViewById(R.id.imProfile);

        try {
            Bundle bundle = getArguments();

            id_user = bundle.getInt("id_user");

        } catch (Exception e) {
            e.printStackTrace();
        }

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();
        final String UrlNoti = ip.getUrlNotistore();

        Ion.with(this)
                .load(UrlNoti + "NotiDetailProfile.php")
                .setMultipartParameter("id_user", String.valueOf(id_user))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String imPro = result.get("im_profile").getAsString();

                        Glide.with(getContext())
                                .load(Url+ "upload-Profile/" +imPro )
                                .into(imProfile);

                        tvName.setText(result.get("name").getAsString());
                        tvLastname.setText(result.get("last_name").getAsString());
                        tvEmail.setText(result.get("email").getAsString());
                        tvTel.setText(result.get("tel").getAsString());

                    }
                });

        return view;
    }

}
