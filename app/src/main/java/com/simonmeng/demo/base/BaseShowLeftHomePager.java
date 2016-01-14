package com.simonmeng.demo.base;

//HomePage用于显示SlidingMenu条目对应的新闻，这个基类，

import android.content.Context;
import android.view.View;

public abstract class BaseShowLeftHomePager  {

    //定义构造方法，一个需要传递Context的构造方法，要new该类对象，就要传一个Context
    public Context mContext;
    private  View rootView;
    public BaseShowLeftHomePager(Context context){
        this.mContext = context;
        rootView = initView();
    }

    public abstract View initView();
    public View getRootView(){return rootView;}
    public void initData(){}
}
