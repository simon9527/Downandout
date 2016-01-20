package com.simonmeng.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
        //SlidingMenu set TOUCHMODE_MARGIN,not TOUCHMODE_FULLSCREEN,it will conflict with  ViewPager.
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //主界面留在屏幕上多少像素
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int Offset = windowWidth / 4;
        mSlidingMenu.setBehindOffset(Offset);

        initFragment();

        GetNewsActivity.NewsActiviy = this;


    }

    private void initFragment() {
        //1.获取FragmentManager 2.开启实务 3.替换 4.提交
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fl_news_content, new NewsContentFragment(), NEWS_CONTENT_TAG);
        mFragmentTransaction.replace(R.id.fl_news_left, new NewsLeftFragment(), NEWS_LEFT_TAG);
        mFragmentTransaction.commit();
    }

    /**
     * 因为news的子菜单很多时候都会与SlidingMenu交互，但是还不能直接交互，需要找到NewsActivity，然后拿到FragmentManager。
     * 然后才能进行交互，故，在NewsActivity中提供一个方法：可以获取SlidingMenu的实例NewsLeftFragment。
     * notice：FragmentManager不能通过findViewById找到NewsLeftFragment，id只能在xml中设置，但是它还提供一方法：findFragmentByTag
     * 这就是上面在initFragment中replace时加了个tag参数，就是在那里给两个Fragment打了印记，供以后能够找到他们
     */
    public NewsLeftFragment getNewsLeftFragment() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        NewsLeftFragment newsLeftFragment = (NewsLeftFragment) mFragmentManager.findFragmentByTag(NEWS_LEFT_TAG);
        return newsLeftFragment;
    }

    /**
     * 同样从Slidingmenu获取交互主界面，也需要NewsActivity提供一个getNewsContentFragment的方法
     */
    public NewsContentFragment getNewsContentFragment() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        NewsContentFragment newsContentFragment = (NewsContentFragment) mFragmentManager.findFragmentByTag(NEWS_CONTENT_TAG);
        return newsContentFragment;
    }

    //退出后有提示
    @Override
    public void finish() {
        super.finish();
        if (isTaskRoot()) {
            Toast.makeText(this, "已经退出程序", Toast.LENGTH_LONG).show();
        }
    }
    //退出前有提示
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }
    protected void dialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(NewsActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //AccoutList.this.finish();
                        //System.exit(1);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}


