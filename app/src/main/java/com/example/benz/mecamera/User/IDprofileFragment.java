package com.example.benz.mecamera.User;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.example.benz.mecamera.MyStore.OtherFragment;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */

public class IDprofileFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private TextView tvName, tvLastname,  tvEmail, tvBirth, tvTel, tvSetting;
    private CircleImageView imProfile;


    public IDprofileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_idprofile, container, false);


        /////////// button back in fragment ////////////////////
        final Toolbar tbIDuser = (Toolbar) view.findViewById(R.id.tbIDuser);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbIDuser);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbIDuser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                fr.commit();

            }
        });
        /////////////////////////////////////////////////////////


        imProfile = (CircleImageView) view.findViewById(R.id.imProfile);

        tvName = (TextView) view.findViewById(R.id.name);
        tvLastname = (TextView) view.findViewById(R.id.last_name);
        tvBirth = (TextView) view.findViewById(R.id.birth);
        tvEmail = (TextView) view.findViewById(R.id.email);
        tvTel = (TextView) view.findViewById(R.id.tel);

        tvSetting = (TextView) view.findViewById(R.id.tvSetting);


        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();


        Ion.with(this)
                .load(Url+"http-User.php")
                .setBodyParameter("ID_user", id_user)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String imPro = result.get("imProfile").getAsString();

                        Glide.with(IDprofileFragment.this)
                                .load(Url+ "upload-Profile/" +imPro )
                                .into(imProfile);

                        System.out.println(imProfile);

                        tvName.setText(result.get("name").getAsString());
                        tvLastname.setText(result.get("last_name").getAsString());
                        tvBirth.setText(result.get("birth").getAsString());
                        tvEmail.setText(result.get("email").getAsString());
                        tvTel.setText(result.get("tel").getAsString());

                    }
                });

        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("การตั้งค่า");
                alert.setCancelable(true);
                alert.setNegativeButton("เเก้ไขข้อมูลส่วนตัว", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /////// start fragment//////
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.mainFrame, new UpdateUserFragment()); //fragment class
                        fr.commit();
                        ///////////////////////////////

                    }
                });

                alert.show();

            }
        });



        return  view;
    }

}
