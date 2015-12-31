package com.simonmeng.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.simonmeng.demo.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public void connectNet(View view){
        Intent toWeather = new Intent();
        toWeather.setClass(this,WeatherActivity.class);
        startActivity(toWeather);
    }





}
