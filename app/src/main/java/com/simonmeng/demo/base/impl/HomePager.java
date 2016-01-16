package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.activity.NewsActivity;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.base.BaseShowLeftHomePager;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.domain.NewsListBean;
import com.simonmeng.demo.fragment.NewsLeftFragment;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * HomePager extends BaseViewPager,it own BaseViewPager's layout,and fill HomePager's data.
 * BaseViewPager has FrameLayout,so HomePager can change the superclass's layout by setting its own layout
 */
public class HomePager extends BaseRadioButtonPager {
    @ViewInject(R.id.lv_news_home)
    private ListView newsHomeListView;
    private List<NewsDetailBean.Contentlist> contentlist;
    private List<BaseShowLeftHomePager> pagerList;
    private List<NewsListBean.ChannelList> channelList;

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
        // TODO: 2016/1/16 先初始化界面，把listview添加上，然后再访问网络，给listview加数据
        View view = View.inflate(mContext, R.layout.news_home,null);
        ViewUtils.inject(this, view);
        frameLayoutContent.addView(view);
        getNewsListFromNetwork(Constants.httpNewsListUrl);

        getNewsDetailData(Constants.httpNewsDetailUrl,channelList.get(0).channelId);


    }

    public void getNewsListFromNetwork(String httpNewsListUrl){
        String json = CacheUtils.getString(mContext,"channelList",null);
        if(json!=null){
            processNewsListData(json);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpNewsListUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CacheUtils.putString(mContext, "channelList", responseInfo.result);
                System.out.print(responseInfo.result);
                processNewsListData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // TODO: 2016/1/15 获取失败应该处理空指针异常
            }
        });
    }

    public void processNewsListData(String newsListJson){
        Gson gson = new Gson();
        NewsListBean bean = gson.fromJson(newsListJson, NewsListBean.class);
        showNewsLeft();
        channelList = bean.showapi_res_body.channelList;
        NewsLeftFragment newsLeftFragment = ((NewsActivity)mContext).getNewsLeftFragment();
        newsLeftFragment.setNewsDataList(channelList);
    }
    /***************************************************************************************/
    public void getNewsDetailData(String httpUrl, final String id) {
        String json = CacheUtils.getString(mContext,id,null);
        if(!TextUtils.isEmpty(json)){
            processDetailData(json);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CacheUtils.putString(mContext, id, responseInfo.result);
                System.out.print(responseInfo.result);
                processDetailData(responseInfo.result);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                // TODO: 2016/1/15 获取失败应该处理空指针异常
            }
        });
    }
    protected void processDetailData(String json){
        Gson gson = new Gson();
        NewsDetailBean newsDetailBean = gson.fromJson(json, NewsDetailBean.class);
//        contentlist =  newsDetailBean.showapi_res_body.pagebean.contentlist;
//        topDescTextView.setText(contentlist.get(0).title);
//        WorldTopPicAdapter worldTopPicAdapter = new WorldTopPicAdapter();
//        topPicViewPager.setAdapter(worldTopPicAdapter);
//        //给viewpager加一个PageChange监听这样可以控制小绿点和它同步滑动
//        topPicViewPager.setOnPageChangeListener(this);
        contentlist = newsDetailBean.showapi_res_body.pagebean.contentlist;
        NewsHomeListViewAdapter newsHomeListViewAdapter = new NewsHomeListViewAdapter();
        newsHomeListView.setAdapter(newsHomeListViewAdapter);

    }

    class NewsHomeListViewAdapter extends BaseAdapter{
        @Override
        public int getCount() { return contentlist.size();}
        @Override
        public Object getItem(int position) { return null;}
        @Override
        public long getItemId(int position) { return 0;}
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsHomeViewHolder newsHomeViewHolder = null;
            if(newsHomeViewHolder == null){
                convertView = View.inflate(mContext,R.layout.item_news_home_listview,null);
                newsHomeViewHolder = new NewsHomeViewHolder();
                newsHomeViewHolder.tv_news_home_cate = (TextView) convertView.findViewById(R.id.tv_news_home_cate);
                newsHomeViewHolder.tv_news_home_date = (TextView) convertView.findViewById(R.id.tv_news_home_date);
                newsHomeViewHolder.tv_news_home_time = (TextView) convertView.findViewById(R.id.tv_news_home_time);
                newsHomeViewHolder.tv_news_home_desc = (TextView) convertView.findViewById(R.id.tv_news_home_desc);
                convertView.setTag(newsHomeViewHolder);
            }else {
                newsHomeViewHolder = (NewsHomeViewHolder) convertView.getTag();
            }
            try {
                SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                // SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy年MM月dd日 " );
                Date date = sdf.parse(contentlist.get(position).pubDate);
                sdf = new SimpleDateFormat("MMM\nd", Locale.US);
                System.out.println(sdf.format(date));
                newsHomeViewHolder.tv_news_home_date.setText(sdf.format(date));
                sdf = new SimpleDateFormat("h:mm a");
                newsHomeViewHolder.tv_news_home_time.setText(sdf.format(date));

//                DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
//                System.out.println(df1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newsHomeViewHolder.tv_news_home_cate.setText(contentlist.get(position).channelName);
            newsHomeViewHolder.tv_news_home_desc.setText(contentlist.get(position).desc);
            return convertView;
        }
    }

    class NewsHomeViewHolder{
        public TextView tv_news_home_cate;
        public TextView tv_news_home_date;
        public TextView tv_news_home_time;
        public TextView tv_news_home_desc;
    }
    /***************************************************************************************/




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
        String channelId = channelList.get(position).channelId;
        getNewsDetailData(Constants.httpNewsDetailUrl,channelId);




        // TODO: 2016/1/13 因为没有创建过个界面，就一个，所以，当调用此方法，传个position会出空指针，就是不管传进来谁，都取第一个
//        BaseShowLeftHomePager pager = pagerList.get(0);
//        View view = pager.getRootView();
//        /**
//         * HomePager中只用中间的FrameLayout是显示内容的，HomePager的父类BaseRadioButtonPager同了该对象---拿来直接用
//         * 一调用switchPagerRespondLeft方法，FrameLayout先删除所有View，防止重叠。
//         * 把从集合pagerList取出来的左侧布局添加到FrameLayout
//         */
//        frameLayoutContent.removeAllViews();
//        frameLayoutContent.addView(view);
//
//        /**
//         * 对应的HomePager的title也要相应改变*/
//        String channelName = channelList.get(position).name;
//        tv_base_radio_button_pager_header.setText(channelName);
    }
}
