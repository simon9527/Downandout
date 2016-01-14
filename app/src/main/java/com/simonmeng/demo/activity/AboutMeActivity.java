package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.simonmeng.demo.R;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        WebView wv =  (WebView)findViewById(R.id.wv_aboutme);
        wv.setVerticalScrollBarEnabled(false);
        wv.loadUrl("http://view.inews.qq.com/a/ENT2016011301562504");
    }
}
