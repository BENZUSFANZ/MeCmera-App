package com.example.benz.mecamera.User;


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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateUserFragment extends PermissionFragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private EditText edtName, edtLastname,  edtEmail, edtBirth, edtTel;
    private CircleImageView imProfile;

    Button btnUpdate, btnCancel;

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
                imProfile.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public UpdateUserFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);


        /////////// button back in fragment ////////////////////
        final Toolbar tbUpdate = (Toolbar) view.findViewById(R.id.tbUpdate);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbUpdate);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbUpdate.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
                fr.commit();

            }
        });
        /////////////////////////////////////////////////////////


        imProfile = (CircleImageView) view.findViewById(R.id.imProfile);

        edtName = (EditText) view.findViewById(R.id.name);
        edtLastname = (EditText) view.findViewById(R.id.last_name);
        edtBirth = (EditText) view.findViewById(R.id.birth);
        edtEmail = (EditText) view.findViewById(R.id.email);
        edtTel = (EditText) view.findViewById(R.id.tel);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);


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

                        Glide.with(UpdateUserFragment.this)
                                .load(Url+ "upload-Profile/" +imPro )
                                .into(imProfile);

                        System.out.println(imProfile);

                        edtName.setText(result.get("name").getAsString());
                        edtLastname.setText(result.get("last_name").getAsString());
                        edtBirth.setText(result.get("birth").getAsString());
                        edtEmail.setText(result.get("email").getAsString());
                        edtTel.setText(result.get("tel").getAsString());

                    }
                });

        imProfile.setOnClickListener(new View.OnClickListener() {
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

                UpdateUser();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////// start fragment//////
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
                fr.commit();
                ///////////////////////////
                Toast.makeText(getActivity(),"ยกเลิกเเก้ไข", Toast.LENGTH_SHORT).show();


            }
        });


        return  view;
    }


    private void UpdateUser(){

        String Name = edtName.getText().toString();
        String Lastname = edtLastname.getText().toString();
        String Birth = edtBirth.getText().toString();
        String Email= edtEmail.getText().toString();
        String Tel = edtTel.getText().toString();


        byte[] imProfile = imageByte;

        String path = RealPath;
        System.out.println("Path:"+path);



        if (Name.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่ชื่อ", Toast.LENGTH_SHORT).show();

        } else if (Lastname.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่นามสกุล", Toast.LENGTH_SHORT).show();

        } else if (Birth.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่เกิดวันที่", Toast.LENGTH_SHORT).show();

        } else if (Email.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่อีเมล", Toast.LENGTH_SHORT).show();

        } else if (Tel.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่เบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();

        }else {

            //sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            final String id_user = sharedpreferences.getString(ID_user, "");

            ipConfig ip = new ipConfig();
            final String Url = ip.getUrl();


            if(path.equals("null")) {

                Ion.with(this)
                        .load(Url + "http-UpdateNoProfileUser.php")

                        .setMultipartParameter("ID_user", id_user)

                        .setMultipartParameter("name", Name)
                        .setMultipartParameter("last_name", Lastname)
                        .setMultipartParameter("birth", Birth)
                        .setMultipartParameter("email", Email)
                        .setMultipartParameter("tel", Tel)

                        //  .setMultipartParameter("imPro", imPro)

                       // .setMultipartFile("upload_file", new File(path)) //// upload image

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
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
                        .load(Url + "http-UpdateUser.php")

                        .setMultipartParameter("ID_user", id_user)

                        .setMultipartParameter("name", Name)
                        .setMultipartParameter("last_name", Lastname)
                        .setMultipartParameter("birth", Birth)
                        .setMultipartParameter("email", Email)
                        .setMultipartParameter("tel", Tel)

                        //  .setMultipartParameter("imPro", imPro)

                        .setMultipartFile("upload_file", new File(path)) //// upload image

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    /////// start fragment//////
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new IDprofileFragment()); //fragment class
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