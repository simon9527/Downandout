package com.simonmeng.demo.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseFragment;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NewsContentFragment extends BaseFragment {
    @ViewInject(R.id.vp_fragment_news_content)
    private ViewPager mViewPager;
    @ViewInject(R.id.rg_fragment_news_content)
    private RadioGroup mRadioGroup;

    @Override
    public View initView() {
        //初始化initView，就是要创建view然后返回即可，就会自动挂载到Fragment上。
        View view = View.inflate(mActivity, R.layout.fragment_new_content, null);
        ViewUtils.inject(this,view);
        return view;
    }


    //覆盖父类BaseFragment的initData方法

    @Override
    public void initData() {

    }
}
