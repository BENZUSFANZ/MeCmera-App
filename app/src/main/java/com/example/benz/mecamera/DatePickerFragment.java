package com.example.benz.mecamera;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    TextView tvDateIn, tvDateOut, tvDays, tvPrice, tvTotal;

    int id;

    public static String DateI, DateO;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // 1วันจอง / 2 วันคืน ดึงจากbooking.class
        if (id == 1){

                DateIn(year, month+1, day);

            } else {

                DateOut(year, month+1, day);

            }
    }

    private void DateIn(int year, int month, int day) {

        tvDateIn = (TextView)getActivity(). findViewById(R.id.tvDate);
        tvDateIn.setText(year+"/"+month+"/"+day);

        DateI = tvDateIn.getText().toString();  // ดึงจาก booking.class
    }


    public void DateOut(int year, int month, int day) {

        tvDateOut = (TextView)getActivity().findViewById(R.id.tvDateOut);   // ดึงจาก booking.class
        tvDateOut.setText(year+"/"+month+"/"+day);

        tvPrice = (TextView)getActivity().findViewById(R.id.tvPrice);   // ดึงจาก booking.class
        tvDays = (TextView)getActivity().findViewById(R.id.tvDays) ;    // ดึงจาก booking.class
        tvTotal = (TextView)getActivity().findViewById(R.id.tvTotal) ;  // ดึงจาก booking.class

        DateO = tvDateOut.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังคำนวน...");
        progressDialog.show();

        ipConfig ip = new ipConfig();
        final String UrlBooking = ip.getUrlBooking();

        Ion.with(this)

                .load(UrlBooking + "CalculateDays.php")     //ไปคำนวนที่ php

                .setMultipartParameter("DateIn", DateI)
                .setMultipartParameter("DateOut", DateO)

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N) //Api Date
                    @Override
                    public void onCompleted(Exception e, String string) {

                        String DayS = string;   //ผลลัพธ์ที่ได้จาก php

                        int Day = Integer.parseInt(DayS); //เเปลงข้อมูล
                        int ttDay = Day + 1; //ผลลัพธ์ที่ได้ วันที่เช่า

                        String dayS = Integer.toString(ttDay);      //เเปลงข้อมูล
                        tvDays.setText(dayS);       //เซตให้เเสดง

                        String PriceS = tvPrice.getText().toString(); // ดึงจาก booking.class

                        int Price = Integer.parseInt(PriceS);

                        int Total = Math.toIntExact(Price * ttDay); //คำนวนคาราวันเช่า

                        String TotalS = Integer.toString(Total);

                        tvTotal.setText(TotalS);


                    }
                });

        // set ให้รอ
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000); //2วินาที
    }

    // ตรวจสอบวันจอง/วันคืน
    public void setID(int id){

        this.id = id;
    }
}
