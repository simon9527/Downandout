package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.view.View;

import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.utils.TypefaceUtils;


public class CelebrityPager extends BaseRadioButtonPager {

    public CelebrityPager(Context context) {
        super(context);
    }
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Celebrity");
        ib_title_menu.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
    }

}
