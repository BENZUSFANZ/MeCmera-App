package com.example.benz.mecamera.MyStore;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import android.support.v7.widget.Toolbar;


import com.example.benz.mecamera.DbBitmapUtility;
import com.example.benz.mecamera.GoogleMapsApi.GoogleMapsApiActivity;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;


    public  boolean mark = false;

    public static double lat=0.0,lng=0.0;


    EditText edtStore, edtCategory, edtAddress;
    Button btnCancel, btnOk;

    ImageView imCredit;


    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public String RealPath;
    public DbBitmapUtility bitmapUtility;
    byte[] imageByte;

    private boolean mIsUploading = false;


    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            RealPath = getRealPathFromURI(getActivity(),imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                imageByte = bitmapUtility.getBytes(bitmap);

                System.out.println("Update 1 imageByte"+imageByte);

                Bitmap image = DbBitmapUtility.getImage(imageByte);
                imCredit.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public ProfileFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


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


        edtStore = (EditText) view.findViewById(R.id.edtStore);
        edtCategory = (EditText) view.findViewById(R.id.edtCategory);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);

        imCredit = (ImageView) view.findViewById(R.id.imCredit);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                fr.commit();
                ///////////////////////////////

                Toast.makeText(getActivity(), "ยกเลิกลงทะเบียน",Toast.LENGTH_SHORT).show();


            }
        });

        imCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission(com.luseen.simplepermission.permissions.Permission.READ_EXTERNAL_STORAGE, new SinglePermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean granted, boolean isDeniedForever) {
                        if(granted) {

                            openGallery();

                        } else {
                            Toast.makeText(getActivity(), "ถ้าไม่อนุญาต จะไม่สามารถเข้าถึงไฟล์ได้", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
               /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 15));  //move camera to location
                if (mMap != null) {
                    Marker hamburg = mMap.addMarker(new MarkerOptions().position(Your_Location));
                }*/
                // Rest of the stuff you need to do with the map

              /*  if(mMap != null){
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(Your_Location)
                            .title("I'm here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                }*/
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        Intent intent = new Intent(getActivity(), GoogleMapsApiActivity.class);
                        ProfileFragment.this.startActivity(intent);

                     // startActivity(new Intent(getContext(), GoogleMapsApiActivity.class));


                    }
                });

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProfile();

            }
        });


        return view;

    }


    private void saveProfile() {

        //Toast.makeText(getContext(),Double.toString(latitude), Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(),Double.toString(longitude), Toast.LENGTH_LONG).show();

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        final String Store = edtStore.getText().toString();
        String Category = edtCategory.getText().toString();
        String Address = edtAddress.getText().toString();

        byte[] imCredit = imageByte;

        String path = RealPath;


        /////// ตรวจสอบค่าว่า ////////////
            if (Store.isEmpty()) {

                Toast.makeText(getActivity(), "กรุณาใส่ชื่อร้าน", Toast.LENGTH_SHORT).show();

            } else if (Category.isEmpty()) {

                Toast.makeText(getActivity(), "กรุณาใส่ประเภทร้าน", Toast.LENGTH_SHORT).show();

            } else if (Address.isEmpty()) {

                Toast.makeText(getActivity(), "กรุณาใส่ที่อยู่ร้าน", Toast.LENGTH_SHORT).show();

            } else if (mark == false && lat ==0.0 && lng == 0.0) {

                Toast.makeText(getActivity(), "กรุณาระบุที่อยู่", Toast.LENGTH_SHORT).show();

            } else if (imCredit == null) {

                Toast.makeText(getActivity(), "กรุณาใส่รูปโปรไฟล์", Toast.LENGTH_SHORT).show();

            } else {
        ///////////////////////////////////////////////////////


            ipConfig ip = new ipConfig();
            final String Url = ip.getUrl();

            Ion.with(this)

                    .load(Url + "http-tbCredit.php")

                    .setMultipartParameter("ID_user", id_user)

                    .setMultipartFile("upload_file", new File(path)) //// upload image

                    .setMultipartParameter("Store", Store)
                    .setMultipartParameter("Category", Category)
                    .setMultipartParameter("Address", Address)

                    .setMultipartParameter("lat", String.valueOf(lat))
                    .setMultipartParameter("lng", String.valueOf(lng))

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            String status = result.get("status").getAsString();


                            if (status.endsWith("ลงทะเบียนสำเร็จ")) {

                                System.out.println(lat);
                                System.out.println(lng);

                                /////// start fragment//////
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                                fr.commit();
                                ////////////////////////////

                                Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();

                            }


                            mIsUploading = false;

                        }
                    });


        }

    }

    // รับมาจาก Google maps
    public void setMark(boolean setMark, Double latitude, Double longitude){


        if (setMark == true){

            lat = latitude;
            lng = longitude;
            mark = setMark;

            System.out.println(mark);
            System.out.println(lng);
            System.out.println(lat);

        }
    }

}
