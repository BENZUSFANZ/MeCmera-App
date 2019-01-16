package com.example.benz.mecamera.MyStore;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.maps.CameraUpdateFactory.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowProfileFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    public static String latS, lngS;

    TextView tvStore, tvCategory, tvAddress, tvSetting;
    ImageView imCredit;

    public ShowProfileFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);


        /////////// button back in fragment ////////////////////
        final Toolbar tbProfile = (Toolbar) view.findViewById(R.id.tbProfile);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbProfile);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                fr.commit();

            }
        });
        /////////////////////////////////////////////////////////

        tvStore = (TextView) view.findViewById(R.id.tvStore);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);

        imCredit = (ImageView) view.findViewById(R.id.imCredit);
        imCredit.setScaleType(ImageView.ScaleType.CENTER_CROP);

        tvSetting = (TextView) view.findViewById(R.id.tvSetting);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();


        Ion.with(this)

                .load(Url+"http-ShowProfile.php")

                .setBodyParameter("ID_user", id_user)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String imPro = result.get("imCredit").getAsString();

                        latS = result.get("latitude").getAsString();
                        lngS = result.get("longitude").getAsString();

                        Double lat = Double.parseDouble(latS);
                        Double lng = Double.parseDouble(lngS);

                        /////////////// My Location /////////////////
                       final LatLng MyLocation = new LatLng(lat, lng);

                        mapFragment = (SupportMapFragment) getChildFragmentManager()
                                .findFragmentById(R.id.map);

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                mMap = googleMap;
                                mMap.moveCamera(newLatLngZoom(MyLocation, 15));  //move camera to location
                                if (mMap != null) {
                                    Marker hamburg = mMap.addMarker(new MarkerOptions()
                                            .position(MyLocation)); // Marker MyLocation
                                }
                            }
                        });
                        /////////////////////////////////////////

                        Glide.with(ShowProfileFragment.this)
                                .load(Url+ "upload-Credit/" +imPro )
                                .into(imCredit);

                        System.out.println(imCredit);

                        tvStore.setText(result.get("Store").getAsString());
                        tvCategory.setText(result.get("Category").getAsString());
                        tvAddress.setText(result.get("Address").getAsString());

                    }
                });

        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("การตั้งค่า");
                alert.setCancelable(true);
                alert.setNegativeButton("ปิดกิจการ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String id_user = sharedpreferences.getString(ID_user, "");

                        ipConfig ip = new ipConfig();
                        final String Url = ip.getUrl();


                        Ion.with(getContext())

                                .load(Url+"http-DeleteProfile.php")

                                .setBodyParameter("ID_user", id_user)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {

                                        if (result.endsWith("คุณได้ลบข้อมูลร้านเรีบยร้อย")) {

                                            /////// start fragment//////
                                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                                            fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                                            fr.commit();
                                            ///////////////////////////
                                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });


                    }
                });


                alert.setPositiveButton("เเก้ไขข้อมูลร้าน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /////// start fragment//////
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.mainFrame, new UpdateProfileFragment()); //fragment class
                        fr.commit();
                        ///////////////////////////////

                    }
                });

                alert.show();
            }
        });


        return view;

    }

}