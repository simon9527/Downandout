package com.simonmeng.demo.fragment;

import android.view.View;
import android.widget.TextView;

import com.simonmeng.demo.base.BaseFragment;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NewsLeftFragment extends BaseFragment {
    @Override
    public View initView() {
        //初始化initView，就是要创建view然后返回即可，就会自动挂载到Fragment上。
        TextView tv= new TextView(mActivity);
        tv.setText(".");
        return tv;
    }
}
