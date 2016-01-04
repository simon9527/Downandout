package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.simonmeng.demo.R;

public class SearchWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_weather);
    }

    //返回用户输入的城市
    public void sendLocation(View view){

    }
}
