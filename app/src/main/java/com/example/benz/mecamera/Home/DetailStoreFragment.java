package com.example.benz.mecamera.Home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.Booking.BookingCalendarViewActivity;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.comment.CommentActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;


/**
 * A simple {@link Fragment} subclass.
 */


public class DetailStoreFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    public static int user_fk;

    int id;
    String  caption, price, imStoreS, imProfileP, name;

    TextView tvName, tvCaption, tvPrice, tvAddress, tvPhone, tvMail, tvTvLike, tvComment;
    Button btnBooking;

    ImageView imStore, icLike, icComment ;
    CircleImageView imProfile;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    public static String latS, lngS;

    ipConfig ip = new ipConfig();

    public DetailStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_store, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tbDetailStore = (Toolbar)view.findViewById(R.id.tbDetailStore);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbDetailStore);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbDetailStore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new HomeFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvCaption = (TextView) view.findViewById(R.id.tvCaption);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvMail = (TextView) view.findViewById(R.id.tvMail);

        imProfile = (CircleImageView) view.findViewById(R.id.imProfile);

        tvTvLike = (TextView) view.findViewById(R.id.tvTtLike);
        tvComment = (TextView) view.findViewById(R.id.tvTtComment) ;
        icComment = (ImageView) view.findViewById(R.id.icComment);
        icLike = (ImageView) view.findViewById(R.id.icLike);

        imStore = (ImageView) view.findViewById(R.id.imStore);
        imStore.setScaleType(ImageView.ScaleType.CENTER_CROP);

        btnBooking = (Button) view.findViewById(R.id.btnBooking);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังโหลด...");
        progressDialog.show();

        try {

           Bundle DetailStore = getArguments();

            id = DetailStore.getInt("id_store");
            name = DetailStore.getString("name");
            caption = DetailStore.getString("caption");
            price = DetailStore.getString("price");

            imProfileP = DetailStore.getString("imProfile");
            imStoreS = DetailStore.getString("imStore");

        } catch (Exception e) {
            e.printStackTrace();
        }

       final String id_store = Long.toString(id);

       final String UrlDtStore = ip.getUrlDtStore();

        Ion.with(getContext())

                .load(UrlDtStore+"DetailStore.php")

                .setBodyParameter("id_store", id_store)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        user_fk = result.get("user_fk").getAsInt();

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

                        if (id_user.equals(Long.toString(user_fk))){

                            btnBooking.setVisibility(View.GONE);    //ซ่อนปุ่มของตัวเอง

                        }else {

                            btnBooking.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    toBooking(id_store, price);

                                }
                            });
                        }

                        tvPhone.setText(result.get("Phone").getAsString());
                        tvMail.setText(result.get("Mail").getAsString());
                        tvAddress.setText(result.get("Address").getAsString());

                    }
                });

        //icLike
        final String UrlLike = ip.getUrlLikeComment();
        Ion.with(getContext())

                .load(UrlLike+"LikeCount.php")

                .setBodyParameter("id_store", String.valueOf(id))
                .setBodyParameter("id_user", String.valueOf(id_user))

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                       // Integer Liked = result.get("liked").getAsInt();
                        tvTvLike.setText(result.get("likeCount").getAsString()); //นับยอดไลค์ของ ID นั้นๆ

                    }
                });

        Ion.with(getContext())

                .load(UrlLike+"CommentCount.php")

                .setBodyParameter("id_store", String.valueOf(id))
                .setBodyParameter("id_user", String.valueOf(id_user))

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        // Integer Liked = result.get("liked").getAsInt();
                        tvComment.setText(result.get("CommentCount").getAsString()); //นับยอดไลค์ของ ID นั้นๆ

                    }
                });

        icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toLike();
            }
        });

        tvName.setText(name);
        tvCaption.setText(caption);
        tvPrice.setText(price);

        final String Url = ip.getUrl();

        Glide.with(this)
                .load(Url+ "upload-Profile/" +imProfileP )
                .into(imProfile);

        Glide.with(this)
                .load(Url+ "upload-Store/" +imStoreS )
                .into(imStore);

        icComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), CommentActivity.class);
                i.putExtra("id_store", id_store);
                startActivity(i);

            }
        });

        // set ให้รอ
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000); //1วินาที


        return view;
    }


    public void toBooking(String id_store, String price){

        Bundle Booking = new Bundle();

        Booking.putString("id_store", id_store);
        Booking.putString("price", price);

        BookingFragment bookingFragment = new BookingFragment();
        bookingFragment.setArguments(Booking);

        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, bookingFragment); //fragment class
        fr.commit();

    }

    public void toLike(){

        final String Url = ip.getUrlLikeComment();

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        Ion.with(getContext())

                .load(Url+"Like.php")

                .setBodyParameter("id_store", String.valueOf(id))
                .setBodyParameter("id_user", id_user)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        tvTvLike.setText(result.get("likeCount").getAsString());

                    }
                });

    }
}
