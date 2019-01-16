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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.DbBitmapUtility;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
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
public class UpdateProfileFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;


    EditText edtStore, edtCategory, edtAddress ;

    Button btnCancel, btnUpdate;

    ImageView imCredit;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public String RealPath = "null";
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



    public UpdateProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);


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
                fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                fr.commit();

            }
        });
        /////////////////////////////////////////////////////////



        edtStore = (EditText) view.findViewById(R.id.edtStore);
        edtCategory = (EditText) view.findViewById(R.id.edtCategory);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);

        imCredit = (ImageView) view.findViewById(R.id.imCredit);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);


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

                        Glide.with(UpdateProfileFragment.this)
                                .load(Url+ "upload-Credit/" +imPro )
                                .into(imCredit);

                        System.out.println(imCredit);

                        edtStore.setText(result.get("Store").getAsString());
                        edtCategory.setText(result.get("Category").getAsString());
                        edtAddress.setText(result.get("Address").getAsString());


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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateProfile();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                fr.commit();
                ///////////////////////////
                Toast.makeText(getActivity(),"ยกเลิกเเก้ไข", Toast.LENGTH_SHORT).show();


            }
        });


        return  view;
    }


    private void UpdateProfile(){

        String Store = edtStore.getText().toString();
        String Category = edtCategory.getText().toString();
        String Address = edtAddress.getText().toString();


        byte[] imProfile = imageByte;

        String path = RealPath;
        System.out.println("Path:"+path);



        if (Store.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ชื่อร้าน", Toast.LENGTH_SHORT).show();

        } else if (Category.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ประเภทร้าน", Toast.LENGTH_SHORT).show();

        } else if (Address.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ที่อยู่ร้าน", Toast.LENGTH_SHORT).show();

        }else {

            //sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            final String id_user = sharedpreferences.getString(ID_user, "");

            ipConfig ip = new ipConfig();
            final String Url = ip.getUrl();


            if(path.equals("null")) {

                Ion.with(this)
                        .load(Url + "http-UpdateNoCreditProfile.php")

                        .setMultipartParameter("ID_user", id_user)

                        .setMultipartParameter("Store", Store)
                        .setMultipartParameter("Category", Category)
                        .setMultipartParameter("Address", Address)


                        //  .setMultipartParameter("imPro", imPro)

                        // .setMultipartFile("upload_file", new File(path)) //// upload image

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                                    fr.commit();
                                    ///////////////////////////

                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }else {

                Ion.with(this)
                        .load(Url + "http-UpdateProfile.php")

                        .setMultipartParameter("ID_user", id_user)

                        .setMultipartFile("upload_file", new File(path)) //// upload image

                        .setMultipartParameter("Store", Store)
                        .setMultipartParameter("Category", Category)
                        .setMultipartParameter("Address", Address)

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new ShowProfileFragment()); //fragment class
                                    fr.commit();
                                    ///////////////////////////
                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }

        }

    }


}