package com.simonmeng.demo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.simonmeng.demo.R;

public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }
}