package com.example.benz.mecamera.Notifications;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailBookingFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    public static TextView tvName, tvPrice, tvCaption, tvDateIn, tvDateOut, icMore;
    public static ImageView imStore;
    public static CircleImageView imProfile;
    public static Button btnCancel, btnOk, btnCanceiBooking;

    public static Integer status, id_booking, id_store, user_fk, customer_fk;
    public static String name, im_profile;

    public DetailBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_booking, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar toolbar3 = (Toolbar)view.findViewById(R.id.toolbar3);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar3);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new NotificationsFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvCaption = (TextView) view.findViewById(R.id.tvCaption);

        tvDateIn = (TextView) view.findViewById(R.id.tvDate);
        tvDateOut = (TextView) view.findViewById(R.id.tvDateOut);

        imProfile = (CircleImageView) view.findViewById(R.id.imProfile);

        imStore = (ImageView) view.findViewById(R.id.imStore);
        imStore.setScaleType(ImageView.ScaleType.CENTER_CROP); //CENTER_CROP

        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnCanceiBooking = (Button) view.findViewById(R.id.btnCancelBooking);
        btnCanceiBooking.setVisibility(View.GONE);  // set ให้ซ่อนก่อนตั้งเเต่เเรก

        icMore = (TextView) view.findViewById(R.id.icMore);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังโหลด...");
        progressDialog.show();

        try{

            Bundle bundle = getArguments();

            status = bundle.getInt("status");
            id_booking = bundle.getInt("id_booking");
            id_store = bundle.getInt("id_store");
            user_fk = bundle.getInt("user_fk");
            customer_fk = bundle.getInt("customer_fk");

            name = bundle.getString("name");
            im_profile = bundle.getString("im_profile");


        }catch (Exception e){
            e.printStackTrace();
        }

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, null);

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        final String UrlNoti = ip.getUrlNotistore();

        // store //ตรวจสอบ int == string ต้องใช้ .equals
        if (String.valueOf(user_fk).equals(id_user)){

            // ถ้า status เท่ากับ 1 หรือ 2 ใหซ่อนปุ่ม
            if (status == 1 || status == 2){

                btnOk.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);

            }


        }

        //Customer //ตรวจสอบ int == string ต้องใช้ .equals
        else if (String.valueOf(customer_fk).equals(id_user) ){

            Ion.with(getContext())
                    .load(UrlNoti + "NotificationStoreProfile.php")
                    .setMultipartParameter("User_fk", String.valueOf(user_fk))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            String imPro = result.get("im_profile").getAsString();
                            Glide.with(getContext())
                                    .load(Url + "upload-Profile/" + imPro)  //รูปภาพโปรไฟล์
                                    .into(imProfile);

                            tvName.setText(result.get("name").getAsString());    //ชื่อยูสเซอร์

                        }
                    });

            System.out.println(status);

            // ตรวจสอบว่่า ถ้า status == 0 ให้เเสดงปุ่มยกเลิกการจอง
            if (status == 0){

                btnCanceiBooking.setVisibility(View.VISIBLE); //เเสดงปุ่ม
            }
                btnOk.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
        }

        // status == 0 ของร้าน
        tvName.setText(name);
        Glide.with(getContext())
                .load(Url+ "upload-Profile/" +im_profile )
                .into(imProfile);

        // ดึงรายละเอียดการจอง
        Ion.with(getContext())
                .load(UrlNoti + "NotiDetailBooking.php")
                .setMultipartParameter("id_booking", String.valueOf(id_booking))
                .setMultipartParameter("id_store", String.valueOf(id_store))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String im_Store = result.get("im_store").getAsString();
                        Glide.with(getContext())
                                .load(Url + "upload-Store/" + im_Store)
                                .into(imStore);
                        tvCaption.setText(result.get("caption").getAsString());

                        String days = result.get("days").getAsString();
                        tvPrice.setText(result.get("ttPrice").getAsString());
                        tvDateIn.setText(result.get("dateIn").getAsString());
                        tvDateOut.setText(result.get("dateOut").getAsString());

                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toBookingNo();

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toBookingYes();

            }
        });

        btnCanceiBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toCancelBooking();
            }
        });

        icMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("การตั้งค่า");
                alert.setCancelable(true);
                alert.setNegativeButton("ข้อมูลส่วนตัว", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        toProfile();

                    }

                });
                alert.show();
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

    // ยกเลิกการเช่า สำหรับCustomer
    private void toCancelBooking() {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, null);

        ipConfig ip = new ipConfig();
        final String UrlBooking = ip.getUrlBooking();

        Ion.with(this)
                .load(UrlBooking + "CancelBooking.php")

                .setMultipartParameter("id_booking", String.valueOf(id_booking))    //ไอดีเช่า
                .setMultipartParameter("id_user", id_user) //ผู้มาเช่า
                .setMultipartParameter("user_fk", String.valueOf(user_fk))    //ผู้ปล่อยเช่า

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.mainFrame, new NotificationsFragment()); //fragment class
                        fr.commit();

                    }
                });
    }

    private void toProfile() {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, null);

        //store
        if (String.valueOf(user_fk).equals(id_user)){

            Bundle bundle = new Bundle();
            bundle.putInt("id_user", customer_fk); //ส่ง ID คนเช่า

            DetailBookingShowStoreFragment detailBookingShowStoreFragment = new DetailBookingShowStoreFragment();
            detailBookingShowStoreFragment.setArguments(bundle);

            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.mainFrame, detailBookingShowStoreFragment); //fragment class
            fr.commit();
        }
        //customer
        else {

            Bundle bundle = new Bundle();
            bundle.putInt("id_user", user_fk); //ส่ง ID ผู้ปล่อยเช่า

            DetailBookingShowStoreFragment detailBookingShowStoreFragment = new DetailBookingShowStoreFragment();
            detailBookingShowStoreFragment.setArguments(bundle);

            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.mainFrame, detailBookingShowStoreFragment); //fragment class
            fr.commit();

        }

    }


    private void toBookingYes(){

        String Id_booking = Integer.toString(id_booking);
        String Customer_fk = Integer.toString(customer_fk);
        String User_fk = Integer.toString(user_fk);


        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlNotistore();

        Ion.with(this)

                .load(Url+ "SayYesBookingStore.php")

                .setMultipartParameter("id_booking", Id_booking)
                .setMultipartParameter("customer_fk", Customer_fk)
                .setMultipartParameter("user_fk", User_fk)

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result.endsWith("ส่งไปยังผู้เช่าเรียบร้อย")) {

                            /////// start fragment//////
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.mainFrame, new NotificationsFragment()); //fragment class
                            fr.commit();
                            ////////////////////////////

                        } else {

                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void toBookingNo(){

        String Id_booking = Integer.toString(id_booking);
        String Customer_fk = Integer.toString(customer_fk);
        String User_fk = Integer.toString(user_fk);

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlNotistore();

        Ion.with(this)

                .load(Url+ "SayNoBookingStore.php")

                .setMultipartParameter("id_booking", Id_booking)
                .setMultipartParameter("customer_fk", Customer_fk)
                .setMultipartParameter("user_fk", User_fk)

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result.endsWith("ส่งไปยังผู้เช่าเรียบร้อย")) {

                            /////// start fragment//////
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.mainFrame, new NotificationsFragment()); //fragment class
                            fr.commit();
                            ////////////////////////////

                        } else {

                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
