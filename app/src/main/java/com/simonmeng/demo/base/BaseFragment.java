package com.simonmeng.demo.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/9.
 */
public abstract class BaseFragment extends Fragment {

    /**************************************************/
    // Fragment不算Activity组件，可以通过getActivity实例，即fragment挂载的activity，用于上下文.
    public Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /**************************************************/
    //返回的view在当前的fragment显示  //initView设置为抽象，子类必须覆盖实现
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }
    public abstract View initView();

    /**************************************************/
    // 调用初始化数据的方法   //子类去覆盖initData方法, 实现自己的数据初始化.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    public void initData() {}
}
