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
import com.example.benz.mecamera.MyStore.MyStoreFragment;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.luseen.simplepermission.permissions.PermissionFragment;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends PermissionFragment {

   EditText edtCaption, edtPrices;
   Button btnCancel, btnOk;

   ImageView imStore;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;



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
                imStore.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tbStore = (Toolbar)view.findViewById(R.id.tbStore);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbStore);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbStore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////


        edtCaption = (EditText) view.findViewById(R.id.edtCaptions);
        edtPrices = (EditText) view.findViewById(R.id.edtPrice);

        imStore = (ImageView) view.findViewById(R.id.imStore);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                fr.commit();

                Toast.makeText(getActivity(), "ยกเลิกโพสต์",Toast.LENGTH_SHORT).show();
                ///////////////////////////////

            }
        });

        imStore.setOnClickListener(new View.OnClickListener() {
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


        btnOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveStore();

            }
        });



        return view;
    }




    private void saveStore(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        String Caption = edtCaption.getText().toString();
        String Price = edtPrices.getText().toString();

        byte[] imStore = imageByte;

        String path = RealPath;

        /////// ตรวจสอบค่าว่าง ////////////
        if (Caption.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ชื่อโพสต์", Toast.LENGTH_SHORT).show();

        } else if (Price.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ราคาต่อวัน", Toast.LENGTH_SHORT).show();

        } else if (imStore == null) {

            Toast.makeText(getActivity(), "กรุณาใส่รูปภาพ", Toast.LENGTH_SHORT).show();

        } else {
        //////////////////////////////////


            ipConfig ip = new ipConfig();
            final String Url = ip.getUrl();

            Ion.with(this)

                    .load(Url + "http-tbStore.php")

                    .uploadProgress(new ProgressCallback() {
                        @Override
                        public void onProgress(long loaded, long total) {


                        }
                    })

                    .setMultipartParameter("ID_user", id_user)

                    .setMultipartFile("upload_file", new File(path)) //// upload image


                    .setMultipartParameter("Caption", Caption)
                    .setMultipartParameter("Price", Price)

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            String status = result.get("status").getAsString();



                            if (status.endsWith("โพสต์สำเร็จ")) {

                                /////// start fragment//////
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
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

}
