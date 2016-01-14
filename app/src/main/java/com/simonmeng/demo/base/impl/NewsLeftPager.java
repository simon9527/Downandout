package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.simonmeng.demo.base.BaseShowLeftHomePager;


public class NewsLeftPager extends BaseShowLeftHomePager {
    public NewsLeftPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView tv = new TextView(mContext);
        tv.setText("测试：显示左侧");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
