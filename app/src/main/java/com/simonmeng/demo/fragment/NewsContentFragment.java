package com.simonmeng.demo.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.activity.NewsActivity;
import com.simonmeng.demo.base.BaseFragment;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.base.impl.CelebrityPager;
import com.simonmeng.demo.base.impl.HomePager;
import com.simonmeng.demo.base.impl.JokePager;
import com.simonmeng.demo.base.impl.WorldPager;

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

    //覆盖父类BaseFragment的initView方法
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
        basePagerList.add(new WorldPager(mActivity));
        basePagerList.add(new CelebrityPager(mActivity));
        basePagerList.add(new JokePager(mActivity));


        NewsContentAdapter newsContentAdapter = new NewsContentAdapter();
        mViewPager.setAdapter(newsContentAdapter);
        //mViewPager set a Listener,so when it change,the mRadioGroup's radiobutton change correspondingly
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        //the Listener is similar to the mViewPager's Listener,when the radiobutton change,the mViewPager change correspondingly
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.rb_news_content_one);
    }

    /**
     * 当RadioGroup中的RadioButton选中状态改变时触发此方法.
     * @param group
     * @param checkedId 被选中的RadioButton的id
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_news_content_one:
                mViewPager.setCurrentItem(0);
                isShowSlidingMenu(true);
                break;
            case R.id.rb_news_content_two:
                mViewPager.setCurrentItem(1);
                isShowSlidingMenu(false);
                break;
            case R.id.rb_news_content_three:
                mViewPager.setCurrentItem(2);
                isShowSlidingMenu(false);
                break;
            case R.id.rb_news_content_four:
                mViewPager.setCurrentItem(3);
                isShowSlidingMenu(false);
                break;
            default:
                break;
        }
    }
    private  void isShowSlidingMenu(boolean isShow){
        SlidingMenu slidingMenu = ((NewsActivity)mActivity ).getSlidingMenu();
        if(isShow){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.rb_news_content_one);
                    break;
                case 1:
                    mRadioGroup.check(R.id.rb_news_content_two);
                    break;
                case 2:
                    mRadioGroup.check(R.id.rb_news_content_three);
                    break;
                case 3:
                    mRadioGroup.check(R.id.rb_news_content_four);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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

    /**
     * provide a method,NewsActivity can use it get HomePager through this NewsContentFragment
     * so that LeftFragmetn--NewsActivity--NewsContentFragmetn--[getHomePager]--HomePager
     * */

    public HomePager getHomePager(){
        HomePager homePager = (HomePager) basePagerList.get(0);
        return homePager;
    }

}
