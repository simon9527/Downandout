package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.simonmeng.demo.R;
import com.simonmeng.demo.fragment.NewsContentFragment;
import com.simonmeng.demo.fragment.NewsLeftFragment;

public class NewsActivity extends SlidingFragmentActivity {
    private final String NEWS_CONTENT_TAG = "content";
    private final String NEWS_LEFT_TAG = "left";


    TextView tv_news_slogon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        setBehindContentView(R.layout.news_left);
        SlidingMenu mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //主界面留在屏幕上多少像素
        mSlidingMenu.setBehindOffset(100);

        initFragment();

//        tv_news_slogon = (TextView) findViewById(R.id.tv_news_slogon);
//        Typeface face = Typeface.createFromAsset(getAssets(),"Walkway Bold.ttf");
//        tv_news_slogon.setTypeface(face);
//        tv_news_slogon.setTransformationMethod(null);
    }

    private void initFragment(){
        //1.获取FragmentManager 2.开启实务 3.替换 4.提交
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fl_news_content, new NewsContentFragment(), NEWS_CONTENT_TAG);
        mFragmentTransaction.replace(R.id.fl_news_left, new NewsLeftFragment(), NEWS_LEFT_TAG);
        mFragmentTransaction.commit();
    }

}
