package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseShowLeftHomePager;


public class NewsLeftPager extends BaseShowLeftHomePager {
    @ViewInject(R.id.lv_news_home)
    private ListView newsHomeDetailListView;
    public NewsLeftPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.news_home,null);
        ViewUtils.inject(this, view);

        return view;
    }
}
