package com.simonmeng.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.simonmeng.demo.R;

public class AboutMeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_joke);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);


    }
}
