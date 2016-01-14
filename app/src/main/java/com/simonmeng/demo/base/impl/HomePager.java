package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simonmeng.demo.activity.NewsActivity;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.base.BaseShowLeftHomePager;
import com.simonmeng.demo.domain.NewsListBean;
import com.simonmeng.demo.fragment.NewsLeftFragment;
import com.simonmeng.demo.utils.AsykTaskUtils;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * HomePager extends BaseViewPager,it own BaseViewPager's layout,and fill HomePager's data.
 * BaseViewPager has FrameLayout,so HomePager can change the superclass's layout by setting its own layout
 */
public class HomePager extends BaseRadioButtonPager {

    public String newListJson;
    List<BaseShowLeftHomePager> pagerList;
    List<NewsListBean.ChannelList> channelList;

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
        tv_base_radio_button_pager_header.setText("Home");
        TextView tvTest = new TextView(mContext);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);

        tvTest.setGravity(Gravity.CENTER);
        tvTest.setTextSize(25);
        tvTest.setTextColor(Color.RED);
        frameLayoutContent.addView(tvTest);
        tvTest.setText("据台湾媒体报道,《康熙来了》");

        //get the channelList.by open a child thread
        new AsykTaskUtils(){
            @Override
            public void afterSubThreadTask() {
                Gson gson = new Gson();
               if(newListJson!=null){
                   NewsListBean bean = gson.fromJson(newListJson, NewsListBean.class);
                   //此方法要在setNewsDataList调用，就是先创建左侧菜单集合，然后setNewsDataList会初始化左侧菜单，并设置第一个为默认选中，否则会空指针
                   showNewsLeft();
                   channelList = bean.showapi_res_body.channelList;
                   NewsLeftFragment newsLeftFragment = ((NewsActivity)mContext).getNewsLeftFragment();
                   newsLeftFragment.setNewsDataList(channelList);
               }
            }
            @Override
            public void beforeSubThreadTask() {
                String httpNewsListArg = "";
                String json = CacheUtils.getString(mContext,"channelList",null);
                if(json!=null){
                    Gson gson = new Gson();
                    NewsListBean bean = gson.fromJson(json, NewsListBean.class);
                    showNewsLeft();
                    channelList = bean.showapi_res_body.channelList;
                    NewsLeftFragment newsLeftFragment = ((NewsActivity)mContext).getNewsLeftFragment();
                    newsLeftFragment.setNewsDataList(channelList);
                }
            }
            @Override
            public void doSubThreadTaskInBackground() {
                String httpNewsListArg = "";
                newListJson = getNetworkData(Constants.httpNewsListUrl,httpNewsListArg);
            }
        }.execute();
    }

    // the method can get json from network
    public  String getNetworkData(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", Constants.myApiKey);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存json数据，key名要唯一，就是url+请求参数，value就是json数据
        if(result!=null){
            CacheUtils.putString(mContext,"channelList",result);
        }
        return result;
    }


    /**显示左侧条目的界面---在HomePager页面
     * 如果每个条目显示不同布局，就要创建对应的Java类，然后继承BaseShowLeftHomePager,这样他们就可以放到同一个list中处理
     * 点击左侧item，HomePager显示对应布局---太麻烦，这里都通过一个布局展示，因为还有radiobutton对应的不同布局可以展示。
     * 创建一个NewsLeftPager
     * */
    public void showNewsLeft(){
        pagerList = new ArrayList<BaseShowLeftHomePager>();
        pagerList.add(new NewsLeftPager(mContext));
    }
    /**
     * HomePager需要提供一个方法，可以切换主界面的显示，为NewsLeftFragment提供，
     * 点击NewsLeftFragment的item --NewsActivity--NewsContentFragment--HomePager--switchPagerRespondLeft()
     * */
    public  void switchPagerRespondLeft(int position){
        // TODO: 2016/1/13 因为没有创建过个界面，就一个，所以，当调用此方法，传个position会出空指针，就是不管传进来谁，都取第一个
        BaseShowLeftHomePager pager = pagerList.get(0);
        View view = pager.getRootView();
        /**
         * HomePager中只用中间的FrameLayout是显示内容的，HomePager的父类BaseRadioButtonPager同了该对象---拿来直接用
         * 一调用switchPagerRespondLeft方法，FrameLayout先删除所有View，防止重叠。
         * 把从集合pagerList取出来的左侧布局添加到FrameLayout
         */
        frameLayoutContent.removeAllViews();
        frameLayoutContent.addView(view);

        /**
         * 对应的HomePager的title也要相应改变*/
        String channelName = channelList.get(position).name;
        tv_base_radio_button_pager_header.setText(channelName);
    }
}
