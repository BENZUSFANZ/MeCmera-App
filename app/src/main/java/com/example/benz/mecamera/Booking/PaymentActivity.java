package com.example.benz.mecamera.Booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.benz.mecamera.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        /////////////////set button back ActionBar////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ชำระเงิน");
        ///////////////////////////////////

    }
}
