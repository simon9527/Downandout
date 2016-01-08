package com.simonmeng.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.simonmeng.demo.R;

public class SearchWeatherActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private EditText et_searchweather_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_weather);
        et_searchweather_location =(EditText) findViewById(R.id.et_searchweather_location);
        //进入该页面就弹出输入法[SOFT_INPUT_STATE_ALWAYS_HIDDEN是隐藏]
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //返回用户输入的城市
    public void sendLocation(View view){
        String inputLocation = et_searchweather_location.getText().toString().trim();

        Intent intent = new Intent();
        intent.putExtra("inputLocation", inputLocation);
        setResult(0, intent);

        //用户点击搜索，关闭该页面，跳转到天气预报界面
        finish();
    }
}
