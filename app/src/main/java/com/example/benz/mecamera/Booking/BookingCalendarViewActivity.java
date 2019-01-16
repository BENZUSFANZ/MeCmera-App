package com.example.benz.mecamera.Booking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benz.mecamera.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingCalendarViewActivity extends AppCompatActivity {

    Button btnOk;
    TextView tvPrice;

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_calendar_view);

        /////////////////set button back ActionBar////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("คุณต้องการเช่าเมื่อใด");
        ///////////////////////////////////

        btnOk = (Button) findViewById(R.id.btnOk);
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        calendarView = (CalendarView) findViewById(R.id.CalendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String date = (year+"/"+month+"/"+dayOfMonth);

                Toast.makeText(BookingCalendarViewActivity.this, date, Toast.LENGTH_SHORT).show();


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
