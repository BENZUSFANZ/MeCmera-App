package com.example.benz.mecamera.custom_font;

import android.app.Application;

import com.example.benz.mecamera.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Font();
    }

    private void Font() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Kanit-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
