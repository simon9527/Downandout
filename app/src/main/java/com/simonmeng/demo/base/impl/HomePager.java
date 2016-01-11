package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.utils.TypefaceUtils;

/**
 * HomePager extends BaseViewPager,it own BaseViewPager's layout,and fill HomePager's data.
 * BaseViewPager has FrameLayout,so HomePager can change the superclass's layout by setting its own layout
 */
public class HomePager extends BaseRadioButtonPager {
    public HomePager(Context context) {
        super(context);
    }

    /**
     * tv_base_radio_button_pager_header in here is superclass's, the subclass HomePager can change it.
     * notice:superclass doesn't call the initView method,but the HomePager's Construction method call the superclass Construction method[super(context)]
     *        in the superclass Construction method, we had define it call initView method.
     * */
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("World News");
        TextView tvTest = new TextView(mContext);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        tvTest.setText("World News");
        tvTest.setGravity(Gravity.CENTER);
        tvTest.setTextSize(25);
        tvTest.setTextColor(Color.RED);
        frameLayoutContent.addView(tvTest);
    }
}
