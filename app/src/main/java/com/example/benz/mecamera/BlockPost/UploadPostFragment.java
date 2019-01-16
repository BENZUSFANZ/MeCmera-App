package com.example.benz.mecamera.BlockPost;


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
public class UploadPostFragment extends PermissionFragment {


    EditText edtCaption;
    Button btnCancel, btnOk;

    ImageView imPost;

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
                imPost.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public UploadPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tbUpload = (Toolbar)view.findViewById(R.id.tbUpload);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbUpload);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbUpload.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new BlockFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////


        edtCaption = (EditText) view.findViewById(R.id.edtCaptions);

        imPost = (ImageView) view.findViewById(R.id.imPost);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new BlockFragment()); //fragment class
                fr.commit();

                Toast.makeText(getActivity(), "ยกเลิกโพสต์",Toast.LENGTH_SHORT).show();
                ///////////////////////////////

            }
        });

        imPost.setOnClickListener(new View.OnClickListener() {
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

                UploadPost();

            }
        });


        return view;
    }



    private void UploadPost(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        String Caption = edtCaption.getText().toString();

        byte[] imStore = imageByte;

        String path = RealPath;

        /////// ตรวจสอบค่าว่าง ////////////
        if (Caption.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ชื่อโพสต์", Toast.LENGTH_SHORT).show();

        } else if (imStore == null) {

            Toast.makeText(getActivity(), "กรุณาใส่รูปภาพ", Toast.LENGTH_SHORT).show();

        } else {
            //////////////////////////////////


            ipConfig ip = new ipConfig();
            final String UrlBlogPost = ip.getUrlBlogPost();

            Ion.with(this)

                    .load(UrlBlogPost + "tbBlogPost.php")


                    .setMultipartParameter("ID_user", id_user)

                    .setMultipartFile("upload_file", new File(path)) //// upload image

                    .setMultipartParameter("Caption", Caption)

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            String status = result.get("status").getAsString();



                            if (status.endsWith("โพสต์สำเร็จ")) {

                                /////// start fragment//////
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.mainFrame, new BlockFragment()); //fragment class
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
