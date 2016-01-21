package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
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
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.ImageCacheUtils;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;


public class CelebrityPager extends BaseRadioButtonPager implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.gv_celebrity)
    private GridView celebrityGridView;
    @ViewInject(R.id.lv_celebrity)
    private ListView celebrityListView;
    @ViewInject(R.id.grid_view)
    private StaggeredGridView gridView;
    private String isXLayout = "isXLayout";
    private List<NewsDetailBean.Contentlist> contentlist;
    private BitmapUtils bitmapUtils;
    private ImageCacheUtils imageCacheUtils;
    private InternalHandler mHandler;


    /**
     * @author andong
     * 接收消息的Handler处理器
     */
    class InternalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView listImageView;
            switch (msg.what) {
                case ImageCacheUtils.SUCCESS:
                    Bitmap bm = (Bitmap) msg.obj;
                    int tag = msg.arg1;
                    // 在ListView中找到含有当前tag的ImageView, 把Bitmap设置给它.
                    // TODO: 2016/1/21 此处可能需要先判断当前layout，然后再判断
                    switch (CacheUtils.getInt(mContext,"isXLayout",0)){
                        case 0:
                            // 切换到列表页面
                            listImageView= (ImageView) celebrityGridView.findViewWithTag(tag);
                            if(listImageView!=null){
                                listImageView.setImageBitmap(bm);
                            }
                            break;
                        case 1:

                            // 切换到瀑布流页面
                            listImageView = (ImageView) celebrityListView.findViewWithTag(tag);
                            if(listImageView!=null){
                                listImageView.setImageBitmap(bm);
                            }
                            break;
                        case 2:
                            // 切换到网格页面
                            listImageView = (ImageView) gridView.findViewWithTag(tag);
                            if(listImageView!=null){
                                listImageView.setImageBitmap(bm);
                            }
                            break;
                        default:
                            break;
                    }

                case ImageCacheUtils.FAILED:
                    Toast.makeText(mContext, "图片加载中...", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    }

    public CelebrityPager(Context context) {
        super(context);
        mHandler = new InternalHandler();
        imageCacheUtils = new ImageCacheUtils(mContext, mHandler);

        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
    }
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Celebrity");
        ib_title_menu.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        View view = View.inflate(mContext, R.layout.news_celebrity,null);
        ViewUtils.inject(this, view);
        frameLayoutContent.addView(view);

        ib_control_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (CacheUtils.getInt(mContext,"isXLayout",0)){
                    case 0:
                        // 切换到列表页面
                        CacheUtils.putInt(mContext,isXLayout,1);
                        ib_control_layout.setImageResource(R.mipmap.news_cele_list);
                        celebrityGridView.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        celebrityListView.setVisibility(View.VISIBLE);
                        celebrityListView.setAdapter(new CelebrityGridViewAdapter());
                    break;
                    case 1:
                        // 切换到瀑布流页面
                        CacheUtils.putInt(mContext,isXLayout,2);
                        ib_control_layout.setImageResource(R.mipmap.news_cele_list);
                        celebrityListView.setVisibility(View.GONE);
                        celebrityGridView.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                        gridView.setAdapter(new WaterFallAdapter());
                        break;
                    case 2:
                        // 切换到网格页面
                        CacheUtils.putInt(mContext,isXLayout,0);
                        ib_control_layout.setImageResource(R.mipmap.news_cele_list);
                        gridView.setVisibility(View.GONE);
                        celebrityListView.setVisibility(View.GONE);
                        celebrityGridView.setVisibility(View.VISIBLE);
                        celebrityGridView.setAdapter(new CelebrityGridViewAdapter());
                        break;
                    default:
                        break;
                }
            }
        });

        String httpUrl = Constants.httpNewsDetailUrl+"5572a10ab3cdc86cf39001eb";
        getCelebrityData(httpUrl, "5572a10ab3cdc86cf39001eb");

    }



    public void getCelebrityData(String httpUrl, final String id) {
        String json = CacheUtils.getString(mContext, id, null);
        if(!TextUtils.isEmpty(json)){
            processData(json);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.length() > 200) {
                    CacheUtils.putString(mContext, id, responseInfo.result);
                    System.out.print(responseInfo.result);
                    processData(responseInfo.result);
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }


    protected void processData(String json){
        Gson gson = new Gson();
        NewsDetailBean newsDetailBean = gson.fromJson(json, NewsDetailBean.class);
        contentlist =  newsDetailBean.showapi_res_body.pagebean.contentlist;
        setXLayout();
    }



    public void setXLayout() {
        switch (CacheUtils.getInt(mContext,"isXLayout",0)){
            case 0:
                // GridView页面
                CelebrityGridViewAdapter celebrityGridViewAdapter = new CelebrityGridViewAdapter();
                celebrityGridView.setAdapter(celebrityGridViewAdapter);

                celebrityGridView.setOnItemClickListener(this);
                celebrityListView.setOnItemClickListener(this);
                gridView.setOnItemClickListener(this);

                celebrityListView.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                break;
            case 1:
                // ListView页面
                CelebrityGridViewAdapter celebrityLridViewAdapter = new CelebrityGridViewAdapter();
                celebrityListView.setAdapter(celebrityLridViewAdapter);

                celebrityGridView.setOnItemClickListener(this);
                celebrityListView.setOnItemClickListener(this);
                gridView.setOnItemClickListener(this);

                celebrityGridView.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                break;
            case 2:
                // StaggeredGridView页面
                WaterFallAdapter waterFallAdapter = new WaterFallAdapter();
                gridView.setAdapter(waterFallAdapter);

                celebrityGridView.setOnItemClickListener(this);
                celebrityListView.setOnItemClickListener(this);
                gridView.setOnItemClickListener(this);

                celebrityListView.setVisibility(View.GONE);
                celebrityGridView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(mContext, CelebrityDetailActivity.class);
        Bundle bundle=new Bundle();
        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
        NewsDetailBean.Contentlist content = contentlist.get(position);
        list.add(content);
        bundle.putSerializable("content", list);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    //Listview和Gridview共用此adapter
    class CelebrityGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contentlist.size();
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
            CelebrityViewHolder celebrityViewHolder = null;
            if(celebrityViewHolder == null){
                convertView = View.inflate(mContext,R.layout.item_celebrity,null);
                celebrityViewHolder = new CelebrityViewHolder();
                celebrityViewHolder.iv_celebrity_item = (ImageView)convertView.findViewById(R.id.iv_celebrity_item);
                celebrityViewHolder.tv_celebrity_item = (TextView) convertView.findViewById(R.id.tv_celebrity_item);
                convertView.setTag(celebrityViewHolder);
            }else {
                celebrityViewHolder = (CelebrityViewHolder) convertView.getTag();
            }
            celebrityViewHolder.tv_celebrity_item.setText(contentlist.get(position).title);
//           if((contentlist.get(position).imageurls).size()!=0){
//                String url = contentlist.get(position).imageurls.get(0).url;
//                System.out.print(url);
//                bitmapUtils.display(celebrityViewHolder.iv_celebrity_item,url);
//            }else{
//                celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
//            }
            celebrityViewHolder.iv_celebrity_item.setTag(position);
            if((contentlist.get(position).imageurls).size()!=0){
                String url = contentlist.get(position).imageurls.get(0).url;
                Bitmap bitMap = imageCacheUtils.getBitmapFromUrl(url,position);
                if(bitMap!=null){
                    celebrityViewHolder.iv_celebrity_item.setImageBitmap(bitMap);
                }else {
                    celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
                }
                // bitmapUtils.display(celebrityViewHolder.iv_celebrity_item,url);
            }else{
                celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
            }

            return convertView;
        }
    }

    //因为item图片布局不同，这里给WaterFall单独一个adapter
    class WaterFallAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contentlist.size();
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
            CelebrityViewHolder celebrityViewHolder = null;
            if(celebrityViewHolder == null){
                convertView = View.inflate(mContext,R.layout.item_waterfall_celebrity,null);
                celebrityViewHolder = new CelebrityViewHolder();
                celebrityViewHolder.iv_celebrity_item = (ImageView)convertView.findViewById(R.id.iv_waterfall_celebrity_item);
                convertView.setTag(celebrityViewHolder);
            }else {
                celebrityViewHolder = (CelebrityViewHolder) convertView.getTag();
            }

            celebrityViewHolder.iv_celebrity_item.setTag(position);
            if((contentlist.get(position).imageurls).size()!=0){
                String url = contentlist.get(position).imageurls.get(0).url;
                Bitmap bitMap = imageCacheUtils.getBitmapFromUrl(url,position);
                if(bitMap!=null){
                    celebrityViewHolder.iv_celebrity_item.setImageBitmap(bitMap);
                }else {
                    celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
                }
               // bitmapUtils.display(celebrityViewHolder.iv_celebrity_item,url);
            }else{
                celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
            }

            return convertView;
        }
    }
    class CelebrityViewHolder{
        public ImageView iv_celebrity_item;
        public TextView tv_celebrity_item;
    }

}
