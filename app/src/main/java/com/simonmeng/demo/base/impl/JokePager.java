package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.utils.TypefaceUtils;


public class JokePager extends BaseRadioButtonPager {
    public JokePager(Context context) {
        super(context);
    }
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Joke");
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        ib_title_menu.setVisibility(View.GONE);
        RelativeLayout rl = (RelativeLayout) View.inflate(mContext, R.layout.activity_about_me, null);
        frameLayoutContent.addView(rl);
    }
}
