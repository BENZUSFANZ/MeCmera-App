package com.example.benz.mecamera.MyStore;


import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.AboutFragment;
import com.example.benz.mecamera.User.IDprofileFragment;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends PermissionFragment {

    TextView tvProfile, tvStore, tvAbout,tvName ;

    CircleImageView imProfile;
    Button logout;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;


    public OtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     final View view = inflater.inflate(R.layout.fragment_other, container, false);

        tvProfile = (TextView) view.findViewById(R.id.tvProfile);
        tvStore = (TextView) view.findViewById(R.id.tvStore);
        tvAbout = (TextView) view.findViewById(R.id.tvAbout);

        imProfile = (CircleImageView) view.findViewById(R.id.imProfile);
        tvName = (TextView) view.findViewById(R.id.tvName);

        logout = (Button) view.findViewById(R.id.btnLogout);



        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");


        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();


        Ion.with(this)
                .load(Url+"http-IDuser.php")
                .setBodyParameter("ID_user", id_user)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String imPro = result.get("imProfile").getAsString();

                        Glide.with(OtherFragment.this)
                                .load(Url+ "upload-Profile/" +imPro )
                                .into(imProfile);

                        System.out.println(imProfile);

                        tvName.setText(result.get("name").getAsString());

                    }
                });

        imProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
                fr.commit();
                ///////////////////////////////

            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
                fr.commit();
                ///////////////////////////////

            }
        });




        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ipConfig ip = new ipConfig();
                final String Url = ip.getUrl();

                Ion.with(getContext())

                        .load(Url+"http-CheckProfile.php")

                        .setBodyParameter("ID_user", id_user)

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                String user = result.get("user").getAsString();
                                String store = result.get("store").getAsString();

                                System.out.print(id_user);
                                System.out.print(user);
                                System.out.print(store);

                                if (user.equals(store)){

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                                    fr.commit();
                                    ///////////////////////////////

                                }else {

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new ProfileFragment()); //fragment class
                                    fr.commit();
                                    ///////////////////////////////
                                }


                            }
                        });

            }
        });




        tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ipConfig ip = new ipConfig();
                final String Url = ip.getUrl();

                Ion.with(getContext())

                        .load(Url+"http-CheckProfile.php")

                        .setBodyParameter("ID_user", id_user)

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                String user = result.get("user").getAsString();
                                String store = result.get("store").getAsString();

                                System.out.print(id_user);
                                System.out.print(user);
                                System.out.print(store);

                                if (user.equals(store)){

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                                    fr.commit();
                                    ///////////////////////////////

                                }else {



                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    alert.setMessage("คุณยังไม่ได้สมัครเป็นผู้ปล่อยเช่า");
                                    alert.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.cancel();

                                        }
                                    });

                                    alert.show();

                                }


                            }
                        });

            }
        });


        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new AboutFragment()); //fragment class
                fr.commit();
                ///////////////////////////////

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ipConfig ip = new ipConfig();
                final String Url = ip.getUrlNotistore();

                /// Clear Token
                Ion.with(getContext())
                        .load(Url + "ClearToken.php")

                        .setMultipartParameter("id_user", id_user)

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {

                            }
                        });

                //////////session (sharedpreferences)//////////////
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
                editor.commit();  // ยืนยันการแก้ไข preferences
                //////////////////////////////////////////////////

                /////////หยุดทำงาน Job///////////////////
                JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
                scheduler.cancelAll();

                ////////// Logout //////////////
                getActivity().finish();
                //////////////////////

            }

        });





    return view;

    }

}
