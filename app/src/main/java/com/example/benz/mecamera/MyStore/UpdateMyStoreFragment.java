package com.example.benz.mecamera.MyStore;


import android.content.Context;
import android.content.Intent;
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
import com.example.benz.mecamera.MyStore.MyStoreFragment;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateMyStoreFragment extends PermissionFragment {

    int id;
    String caption, price, imMyStore;

    EditText edtCaption, edtPrices;
    Button btnCancel, btnOk;

    ImageView imStore;

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

            RealPath = getRealPathFromURI(getContext(),imageUri);

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


    public UpdateMyStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_my_store, container, false);

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




        try {

            Bundle DetailStore = getArguments();

            id = DetailStore.getInt("id_store");
            caption = DetailStore.getString("caption");
            price = DetailStore.getString("price");
            imMyStore = DetailStore.getString("imStore");

        } catch (Exception e) {
            e.printStackTrace();
        }


        edtCaption.setText(caption);
        edtPrices.setText(price);

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        Glide.with(this)
                .load(Url+ "upload-Store/" +imMyStore )
                .into(imStore);




        imStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission(com.luseen.simplepermission.permissions.Permission.READ_EXTERNAL_STORAGE, new SinglePermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean granted, boolean isDeniedForever) {
                        if(granted) {

                            openGallery();

                        } else {
                            Toast.makeText(getContext(), "ถ้าไม่อนุญาต จะไม่สามารถเข้าถึงไฟล์ได้", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateMyStore();

            }

        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                fr.commit();

            }
        });

        return view;
    }

    private void UpdateMyStore() {


        String Caption = edtCaption.getText().toString();
        String Price = edtPrices.getText().toString();

        String id_store = Long.toString(id);


        byte[] imProfile = imageByte;

        String path = RealPath;
        System.out.println("Path:"+path);



        if (Caption.isEmpty()) {

            Toast.makeText(getContext(), "กรุณาใส่รายละเอียด", Toast.LENGTH_SHORT).show();

        } else if (Price.isEmpty()) {

            Toast.makeText(getContext(), "กรุณาใส่ราคา", Toast.LENGTH_SHORT).show();


        }else {


            ipConfig ip = new ipConfig();
            final String UrlrcvMyStore = ip.getUrlrcvMyStore();


            if(path.equals("null")) {

                Ion.with(this)
                        .load(UrlrcvMyStore + "UpdateNoImMyStore.php")

                        .setMultipartParameter("id_store", id_store)

                        .setMultipartParameter("caption", Caption)
                        .setMultipartParameter("price", Price)

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                                    fr.commit();

                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }else {

                Ion.with(this)
                        .load(UrlrcvMyStore + "UpdateMyStore.php")

                        .setMultipartParameter("id_store", id_store)

                        .setMultipartFile("upload_file", new File(path)) //// upload image

                        .setMultipartParameter("caption", Caption)
                        .setMultipartParameter("price", Price)

                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (result.endsWith("เเก้ไขสำเร็จ")) {

                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.mainFrame, new MyStoreFragment()); //fragment class
                                    fr.commit();

                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }

        }


    }

}
