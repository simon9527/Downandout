package com.simonmeng.demo.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseFragment;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.base.impl.CelebrityPager;
import com.simonmeng.demo.base.impl.FinancePager;
import com.simonmeng.demo.base.impl.HomePager;
import com.simonmeng.demo.base.impl.JokePager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NewsContentFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.vp_fragment_news_content)
    private ViewPager mViewPager;
    @ViewInject(R.id.rg_fragment_news_content)
    private RadioGroup mRadioGroup;
    private List<BaseRadioButtonPager> basePagerList;

    @Override
    public View initView() {
        //初始化initView，就是要创建view然后返回即可，就会自动挂载到Fragment上。
        View view = View.inflate(mActivity, R.layout.fragment_new_content, null);
        ViewUtils.inject(this, view);
        return view;
    }


    //覆盖父类BaseFragment的initData方法

    @Override
    public void initData() {
        basePagerList = new ArrayList<BaseRadioButtonPager>();
        basePagerList.add(new HomePager(mActivity));
        basePagerList.add(new CelebrityPager(mActivity));
        basePagerList.add(new JokePager(mActivity));
        basePagerList.add(new FinancePager(mActivity));

        NewsContentAdapter newsContentAdapter = new NewsContentAdapter();
        mViewPager.setAdapter(newsContentAdapter);

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.rb_news_content_worldnews);
    }

    /**
     * 当RadioGroup中的RadioButton选中状态改变时触发此方法.
     * @param group
     * @param checkedId 被选中的RadioButton的id
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_news_content_worldnews:
                mViewPager.setCurrentItem(0);
                // 把SlidingMenu侧滑菜单给屏蔽
                //isEnableMenu(false);
                break;
            case R.id.rb_news_content_celebrity:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_news_content_joke:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.rb_news_content_finance:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    class NewsContentAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return basePagerList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseRadioButtonPager baseRadioButtonPager = basePagerList.get(position);
            View view = baseRadioButtonPager.getRootView();
            container.addView(view);
            baseRadioButtonPager.initData();
            return view;
        }
    }
}
