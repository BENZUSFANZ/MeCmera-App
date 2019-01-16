package com.example.benz.mecamera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView Regis;
    EditText username, password;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = (EditText) findViewById(R.id.idUser);
        password = (EditText) findViewById(R.id.idPass) ;


        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IdUser();
            }
        });


        Regis = (TextView) findViewById(R.id.idRegis);
        Regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });



        ////////////// session ///////////////////////////
        sharedpreferences = this.getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        // ดึง share preference ชื่อ MyPrefs เก็บไว้ในตัวแปร sharedpreferences
        if (sharedpreferences.contains(ID_user))   // ตรวจสอบ name ใน preference
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        ////////////////////////////////////////////////////

    }

    public void IdUser(){

        final SharedPreferences.Editor editor = sharedpreferences.edit();

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        Ion.with(getApplicationContext())
                .load(Url+"http-login.php")
                .setBodyParameter("username",username.getText().toString())
                .setBodyParameter("password",password.getText().toString())

                .asJsonObject()

                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String id_user = result.get("id_user").getAsString();
                        String status = result.get("status").getAsString();


                        if (status.endsWith("เข้าสู่ระบบสำเร็จ")){

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            editor.putString(ID_user, id_user);  // preferance เก็บค่า id_user จาก edittext
                            editor.commit();  // ยืนยันการแก้ไข preferance


                        }else {

                            Toast.makeText(LoginActivity.this, status,Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }



}
