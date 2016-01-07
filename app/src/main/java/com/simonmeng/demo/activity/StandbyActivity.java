package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.simonmeng.demo.R;

public class StandbyActivity extends AppCompatActivity {
    private static final String[] strs = new String[] {
            "first", "second", "third", "fourth", "fifth"
    };
    private ListView lv_standby_forecastweather;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standby);

        view = View.inflate(getApplicationContext(),R.layout.item_weatherforcast,null);
       lv_standby_forecastweather = (ListView)findViewById(R.id.lv_standby_forecastweather);
        lv_standby_forecastweather.addHeaderView(view);
       lv_standby_forecastweather.setAdapter(new ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1, strs));
    }



}

