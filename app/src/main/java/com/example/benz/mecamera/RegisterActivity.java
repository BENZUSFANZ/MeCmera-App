package com.example.benz.mecamera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.Permission;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;
import com.santalu.maskedittext.MaskEditText;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends PermissionActivity {


    private Button btnRegis, btnCancel;
    private EditText edtName, edtLastname,  edtEmail,  edtUsername, edtPassword,edtBirth,edtTel;
    private CircleImageView imProfile;


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

            RealPath = getRealPathFromURI(getApplicationContext(),imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                imageByte = bitmapUtility.getBytes(bitmap);

                System.out.println("Update 1 imageByte"+imageByte);

                Bitmap image = DbBitmapUtility.getImage(imageByte);
                imProfile.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /////////////////set button back ActionBar////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("สมัครสมาชิก");
        ///////////////////////////////////

        btnRegis = (Button) findViewById(R.id.btnRegis);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        edtName = (EditText) findViewById(R.id.name);
        edtLastname = (EditText) findViewById(R.id.last_name);
        edtBirth = (EditText) findViewById(R.id.birth);
        edtEmail = (EditText) findViewById(R.id.email);
        edtTel = (EditText) findViewById(R.id.tel);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);

        imProfile = (CircleImageView) findViewById(R.id.imProfile) ;

        //////////////back home//////////////
      btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                Toast.makeText(RegisterActivity.this, "ยกเลิกลงทะเบียน",Toast.LENGTH_SHORT).show();
            }
       });
       //////////////////////////////////////


        imProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermission(com.luseen.simplepermission.permissions.Permission.READ_EXTERNAL_STORAGE, new SinglePermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean granted, boolean isDeniedForever) {
                        if(granted) {

                            openGallery();

                        } else {
                            Toast.makeText(getApplicationContext(), "ถ้าไม่อนุญาต จะไม่สามารถเข้าถึงไฟล์ได้", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });



        btnRegis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String Name = edtName.getText().toString();
               String Lastname = edtLastname.getText().toString();
               String Birth = edtBirth.getText().toString();
               String Email= edtEmail.getText().toString();
               String Tel = edtTel.getText().toString();

               byte[] imProfile = imageByte;

               String path = RealPath;

               final String Username = edtUsername.getText().toString();
               String Password = edtPassword.getText().toString();



               /////// ตรวจสอบค่าว่า ////////////
               if (Name.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่ชื่อ", Toast.LENGTH_SHORT).show();

               } else if (Lastname.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่นามสกุล", Toast.LENGTH_SHORT).show();

               } else if (Birth.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่เกิดวันที่", Toast.LENGTH_SHORT).show();

               } else if (Email.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่อีเมล", Toast.LENGTH_SHORT).show();

               } else if (Tel.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่เบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();



               }else if (imProfile == null) {

                       Toast.makeText(getApplicationContext(), "กรุณาใส่สำเนาบัตรประชาชน", Toast.LENGTH_SHORT).show();

               } else if (Username.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่ชื่อผู้ใช้", Toast.LENGTH_SHORT).show();

               } else if (Password.isEmpty()) {

                   Toast.makeText(getApplicationContext(), "กรุณาใส่รหัสผ่าน", Toast.LENGTH_SHORT).show();

               } else {
               /////////////////////////////////////////////////////////////////////


                   ipConfig ip = new ipConfig();
                   final String Url = ip.getUrl();


                   Ion.with(getApplicationContext())
                           .load(Url + "http-tbUser.php")
                           .setMultipartParameter("name", Name)
                           .setMultipartParameter("last_name", Lastname)
                           .setMultipartParameter("birth", Birth)
                           .setMultipartParameter("email", Email)
                           .setMultipartParameter("tel", Tel)

                           .setMultipartFile("upload_file", new File(path)) //// upload image

                           .setMultipartParameter("username", Username)
                           .setMultipartParameter("password", Password)

                           .asString()
                           .setCallback(new FutureCallback<String>() {
                               @Override
                               public void onCompleted(Exception e, String result) {
                                   if (result.endsWith("ลงทะเบียนสำเร็จ")) {

                                       startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                       Toast.makeText(RegisterActivity.this, result+" กรุณาเข้าสู่ระบบใหม่", Toast.LENGTH_SHORT).show();

                                   } else {

                                       Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();

                                   }
                               }
                           });

               }
           }
       });



    }
    // back activity toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // custom font
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
