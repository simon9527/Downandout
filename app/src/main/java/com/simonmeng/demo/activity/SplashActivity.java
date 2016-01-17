package com.simonmeng.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.simonmeng.demo.R;

public class SplashActivity extends Activity {

    private Button bt_splash_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bt_splash_start = (Button) findViewById(R.id.bt_splash_start);
        //设置字体，typeface字体。
        Typeface face = Typeface.createFromAsset(getAssets(),"Walkway Bold.ttf");
        bt_splash_start.setTypeface(face);
        bt_splash_start.setTransformationMethod(null);

    }

    public void toMain(View view){
        Intent openErrorPageIntent = new Intent();
        openErrorPageIntent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(openErrorPageIntent);
        finish();
    }

}
