package com.layercontent.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Detalist extends AppCompatActivity {
String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalist);
        country=getIntent().getStringExtra("country");

    }
}