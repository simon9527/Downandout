package com.simonmeng.demo.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/1/15.
 */
public class OutterViewPager extends ViewPager {
    public OutterViewPager(Context context) {
        super(context);
    }

    public OutterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
