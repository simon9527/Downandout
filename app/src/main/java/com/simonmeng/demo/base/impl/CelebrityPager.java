package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

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
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.util.List;


public class CelebrityPager extends BaseRadioButtonPager implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.gv_celebrity)
    private GridView celebrityGriView;

    private List<NewsDetailBean.Contentlist> contentlist;

    public CelebrityPager(Context context) {
        super(context);
    }
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Celebrity");
        ib_title_menu.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        View view = View.inflate(mContext, R.layout.news_celebrity,null);
        ViewUtils.inject(this, view);
        frameLayoutContent.addView(view);


        String httpUrl = Constants.httpNewsDetailUrl+"5572a109b3cdc86cf39001e6";
        getCelebrityData(httpUrl, "5572a109b3cdc86cf39001e6");

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
                // TODO: 2016/1/15 获取失败应该处理空指针异常
            }
        });
    }


    protected void processData(String json){
        Gson gson = new Gson();
        NewsDetailBean newsDetailBean = gson.fromJson(json, NewsDetailBean.class);
        contentlist =  newsDetailBean.showapi_res_body.pagebean.contentlist;



        CelebrityGridViewAdapter celebrityGridViewAdapter = new CelebrityGridViewAdapter();
        celebrityGriView.setAdapter(celebrityGridViewAdapter);
        celebrityGriView.setOnItemClickListener(this);
    }

    // TODO: 2016/1/18 item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    // TODO: 2016/1/18 gridview的adapter
    class CelebrityGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
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
            return null;
        }
    }
}
