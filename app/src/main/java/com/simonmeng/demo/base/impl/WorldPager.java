package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.activity.CelebrityDetailActivity;
import com.simonmeng.demo.activity.WorldNewsDetailActivity;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.customview.RefreshListView;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.DpInterconvertPxUtils;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;


public class WorldPager extends BaseRadioButtonPager implements ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener, RefreshListView.OnRefreshListener {
    @ViewInject(R.id.vp_world_top_pic)
    private ViewPager topPicViewPager;
    @ViewInject(R.id.tv_world_top_desc)
    private TextView topDescTextView;
    @ViewInject(R.id.ll_world_top_points)
    private LinearLayout pointsLinearLayout;
    @ViewInject(R.id.rlv_world_detail)
    private RefreshListView worldDetailListView;
    @ViewInject(R.id.select_point)
    private View selectPoint;
    private List<NewsDetailBean.Contentlist> contentlist;
    private BitmapUtils bitmapUtils;
    private static final int AMOUNTOFTOPPIC = 5;
    private int SpaceOfPoint;
    private int selectPointBeginLeft;
    private InternalHandlerForImageCarousel mHandler;
    private int currentSelectTopPicItem;

    public WorldPager(Context context) {
        super(context);
        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
    }

    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Sports News");
        ib_title_menu.setVisibility(View.GONE);
        ib_control_layout.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        View view = View.inflate(mContext, R.layout.news_world,null);
        ViewUtils.inject(this, view);

        View newsHeader = View.inflate(mContext,R.layout.news_world_carousel_header,null);
        ViewUtils.inject(this, newsHeader);
        //worldDetailListView.addHeaderView(newsHeader);
         worldDetailListView.customListViewAddHeader(newsHeader);
         worldDetailListView.setOnRefreshListener(this);
        //刚开始吧drawPoint方法放到了processData中，致使每次调用processData都画一遍，画了很多points
        drawPoint(AMOUNTOFTOPPIC);
        frameLayoutContent.addView(view);
        String httpUrl = Constants.httpNewsDetailUrl+"5572a109b3cdc86cf39001e6";
        getNetworkData(httpUrl,"5572a109b3cdc86cf39001e6");
        selectPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //获取间距.  只要布局的参数一改变就会触发此方法.
            @Override
            public void onGlobalLayout() {
                // 我们只是想要一下点与点之间的间距，所以监听一次，拿到间距，就可以把当前的事件给移除监听.
                selectPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                SpaceOfPoint = pointsLinearLayout.getChildAt(1).getLeft() - pointsLinearLayout.getChildAt(0).getLeft();
                System.out.println("SpaceOfPoint: " + SpaceOfPoint);
            }
        });
        selectPointBeginLeft = ((RelativeLayout.LayoutParams) selectPoint.getLayoutParams()).leftMargin;
    }
    public void getNetworkData(String httpUrl, final String id) {
        String json = CacheUtils.getString(mContext,id,null);
        if(!TextUtils.isEmpty(json)){
            processData(json);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //非空判断，只用返回的结果有正确的数值才能存放在sharepreference中，但是一般错误也会返回一些信息，这里就简单地通过长度判断一下
                if(responseInfo.result.length()>200){
                    CacheUtils.putString(mContext, id, responseInfo.result);
                    System.out.print(responseInfo.result);
                    processData(responseInfo.result);
                }
                worldDetailListView.OnRefreshDataFinish();
            }
            @Override
            public void onFailure(HttpException e, String s) {
                worldDetailListView.OnRefreshDataFinish();
            }
        });
    }


    protected void processData(String json){
        Gson gson = new Gson();
        NewsDetailBean newsDetailBean = gson.fromJson(json, NewsDetailBean.class);
        contentlist =  newsDetailBean.showapi_res_body.pagebean.contentlist;
        topDescTextView.setText(contentlist.get(0).title);
        WorldTopPicAdapter worldTopPicAdapter = new WorldTopPicAdapter();
        topPicViewPager.setAdapter(worldTopPicAdapter);
        //给viewpager加一个PageChange监听这样可以控制小绿点和它同步滑动
        topPicViewPager.setOnPageChangeListener(this);

        // processData方法一般执行两次：从本地读取，再从网络读取，故非空判断，创建一个mHandler即可.
        if(mHandler == null) {
            mHandler = new InternalHandlerForImageCarousel();
        }
        /**processData执行两次，那么postDelayed可能执行两次
         * 故，再次执行时要移除Handler对应消息队列中的回调和消息---removeCallbacksAndMessages
         * */
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new AutoSwitchPagerRunnable(), 5000);

        NewsListViewAdapter newsListViewAdapter = new NewsListViewAdapter();
        worldDetailListView.setAdapter(newsListViewAdapter);
        worldDetailListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //注意：listview加个header，header占了position0
//        String detailUrl = contentlist.get(position-1+AMOUNTOFTOPPIC).link;
//        Intent intent=new Intent(mContext, WorldNewsDetailActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("deatailUrl", detailUrl);
//        //bundle.putString("pass", "123");---可以通过bundle传多组数据
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
        Intent intent=new Intent(mContext, CelebrityDetailActivity.class);
        Bundle bundle=new Bundle();
        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
        NewsDetailBean.Contentlist content = contentlist.get(position-1+AMOUNTOFTOPPIC);
        list.add(content);
        bundle.putSerializable("content", list);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void onPullDownRefresh() {
        String httpUrl = Constants.httpNewsDetailUrl+"5572a109b3cdc86cf39001e6";
        getNetworkData(httpUrl,"5572a109b3cdc86cf39001e6");
    }

    @Override
    public void onLoadingMore() {
        String httpUrl = Constants.httpNewsDetailUrl+"5572a109b3cdc86cf39001e6&page=3";
        getNetworkData(httpUrl,"5572a109b3cdc86cf39001e6");

    }


    class NewsListViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contentlist.size()-AMOUNTOFTOPPIC;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsDetailViewHolder newsDetailViewHolder = null;
            if(newsDetailViewHolder == null){
                convertView = View.inflate(mContext,R.layout.item_world_news_listview,null);
                newsDetailViewHolder = new NewsDetailViewHolder();
                newsDetailViewHolder.tv_news_detail_title = (TextView) convertView.findViewById(R.id.tv_news_detail_title);
                newsDetailViewHolder.iv_news_detail_image = (ImageView) convertView.findViewById(R.id.iv_news_detail_image);
                newsDetailViewHolder.tv_news_detail_source = (TextView) convertView.findViewById(R.id.tv_news_detail_source);
                newsDetailViewHolder.tv_news_detial_pubDate = (TextView) convertView.findViewById(R.id.tv_news_detial_pubDate);
                convertView.setTag(newsDetailViewHolder);
            }else {
                newsDetailViewHolder = (NewsDetailViewHolder)convertView.getTag();
            }
            newsDetailViewHolder.tv_news_detail_title.setText(contentlist.get(position + AMOUNTOFTOPPIC).title);
            if((contentlist.get(position+AMOUNTOFTOPPIC).imageurls).size()!=0){
                bitmapUtils.display(newsDetailViewHolder.iv_news_detail_image,contentlist.get(position+AMOUNTOFTOPPIC).imageurls.get(0).url);
            }else{
                newsDetailViewHolder.iv_news_detail_image.setImageResource(R.mipmap.news_default);
            }
            newsDetailViewHolder.tv_news_detail_source.setText(contentlist.get(position+AMOUNTOFTOPPIC).source);
            newsDetailViewHolder.tv_news_detial_pubDate.setText(contentlist.get(position+AMOUNTOFTOPPIC).pubDate);

            return convertView;
        }
    }
    class NewsDetailViewHolder{
        public TextView tv_news_detail_title;
        public ImageView iv_news_detail_image;
        public TextView tv_news_detail_source;
        public TextView tv_news_detial_pubDate;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //position 当前滑动的是第几页  positionOffset 页面滑动的百分比  positionOffsetPixels 页面移动的像素
        //
        float leftMargin = SpaceOfPoint * (position + positionOffset);
        RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) selectPoint.getLayoutParams();
        lp.leftMargin = (int) leftMargin+ selectPointBeginLeft;
        selectPoint.setLayoutParams(lp);
    }
    @Override
    public void onPageSelected(int position) {
        String title = contentlist.get(position).title;
        topDescTextView.setText(title);
        currentSelectTopPicItem = topPicViewPager.getCurrentItem();

    }
    @Override
    public void onPageScrollStateChanged(int state) { }

    class WorldTopPicAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            //此处应该是contentlist.size。但是考虑到轮播图底部有小点对应数目，如果20来个小点就不好看了
            return AMOUNTOFTOPPIC;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mContext);
            /**the src attribute[not property] of ImageView keep the aspect ratio
             * set the ScaleType as FIT_XY,it scale the image using FILL.
             * android:scaleType  Controls how the image should be resized or moved to match the size of this ImageView.
             * */
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(R.mipmap.news_toppic_default);
            container.addView(iv);//不要忘记此句，把iv放到容器中
            //在访问网络后获取图片url再访问网络---BitmapUtils直接搞定，不需要在viewpager中每次都创建一次，放到构造函数中创建，再定为成员变量，在这里赋值即可
            //configDefaultBitmapConfig：设置加载图片时，每个像素占用多少字节，4444两个字节，88884个字节。
            //BitmapUtils bitmapUtils = new BitmapUtils(mContext);
            //bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
            if((contentlist.get(position).imageurls).size()!=0){
                String url = contentlist.get(position).imageurls.get(0).url;
                bitmapUtils.display(iv, url);
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String detailUrl = contentlist.get(currentSelectTopPicItem).link;
                    Intent intent=new Intent(mContext, WorldNewsDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("deatailUrl", detailUrl);
                    //bundle.putString("pass", "123");---可以通过bundle传多组数据
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            return iv;
        }
    }

    public void drawPoint(int amount){
        /**画点，在LinearLayout布局上褒一个RelativeLayout，在RelativeLayout放一个view，background是圆点，就可以罩下面的圆点
         * 这块考验代码布局与xml的相互配合，因为底层白点是动态画出来的，上层绿点是xml中的，xml的属性值要与代码中的一致*/
        View piont;
        for(int i=0;i<amount;i++){
            piont = new View(mContext);
            piont.setBackgroundResource(R.drawable.circleshape_white);
            //布局时LinearLayout所以要导入对应的LayoutParams
            int width = DpInterconvertPxUtils.dip2px(mContext,7);
            int height = DpInterconvertPxUtils.dip2px(mContext,7);
            int margin = DpInterconvertPxUtils.dip2px(mContext,5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
            params.gravity = Gravity.CENTER;
            params.leftMargin = margin;
            piont.setLayoutParams(params);
            pointsLinearLayout.addView(piont);
        }
    }
    class InternalHandlerForImageCarousel extends Handler {
        /***
         * 定时器：
         * 在processData，处理json数据时，会创建出来一个handle，它postDelayed我设置了7000
         * 所以第一次，第一张图片可以显示7秒，然后就和7000没关系了，主要全在这里控制
         * 第一次7000后发出消息，handle处理，5000后创建AutoSwitchPagerRunnable线程，创建后线程发消息，handle处理，5000后创建线程---如此往复
         * 切换图片，getCurrentItem() + 1，就是切换到下一张的position，到最后一张就超出了，取摩AMOUNTOFTOPPIC，即可。
         * AutoSwitchPagerRunnable中得到一个消息sendToTarget，Target就是handler，handler中的handleMessage接收处理
         */
        @Override
        public void handleMessage(Message msg) {
            int currentTopPicItem = (topPicViewPager.getCurrentItem() + 1) % AMOUNTOFTOPPIC;
            topPicViewPager.setCurrentItem(currentTopPicItem);
            postDelayed(new AutoSwitchPagerRunnable(), 5000);
        }
    }
    class AutoSwitchPagerRunnable implements Runnable {
        @Override
        public void run() {
            //Message msg = mHandler.obtainMessage();
            //msg.sendToTarget();
            mHandler.obtainMessage().sendToTarget();
        }
    }
}
