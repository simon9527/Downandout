package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;


public class WorldPager extends BaseRadioButtonPager {
    @ViewInject(R.id.vp_world_top_pic)
    private ViewPager topPicViewPager;
    @ViewInject(R.id.tv_world_top_desc)
    private TextView topDescTextView;
    @ViewInject(R.id.ll_world_top_points)
    private LinearLayout pointsLinearLayout;
    @ViewInject(R.id.lv_world_detail)
    private ListView worldDetailListView;

    public WorldPager(Context context) {
        super(context);

    }





    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("World News");
        ib_title_menu.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
       // frameLayoutContent.removeAllViews();
        View view = View.inflate(mContext, R.layout.news_world,null);
        ViewUtils.inject(this, view);
        frameLayoutContent.addView(view);
        getNetworkData(Constants.httpNewsDetailUrl,"5572a10ab3cdc86cf39001eb");
    }

    protected void processData(String json){
        Gson gson = new Gson();
        NewsDetailBean newsDetailBean = gson.fromJson(json,NewsDetailBean.class);
        // TODO: 解析json
        WorldTopPicAdapter worldTopPicAdapter = new WorldTopPicAdapter();
        topPicViewPager.setAdapter(worldTopPicAdapter);
    }


    class WorldTopPicAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }



    public  void getNetworkData(String httpUrl, String channelId) {

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpUrl,params , new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.print(responseInfo.result);
                topDescTextView.setText(responseInfo.result);
                String aa = responseInfo.result;
                topDescTextView.setText(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


//        BufferedReader reader = null;
//        String result = null;
//        StringBuffer sbf = new StringBuffer();
//        httpUrl = httpUrl + "?channelId="+channelId+"&page=1";
//        try {
//            URL url = new URL(httpUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            // 填入apikey到HTTP header
//            connection.setRequestProperty("apikey", Constants.myApiKey);
//            connection.connect();
//            InputStream is = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String strRead = null;
//            while ((strRead = reader.readLine()) != null) {
//                sbf.append(strRead);
//                sbf.append("\r\n");
//            }
//            reader.close();
//            result = sbf.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //缓存json数据，key名要唯一，请求参数，value就是json数据
//        if(result!=null){
//            CacheUtils.putString(mContext, "channelId", result);
//        }
//        return result;
    }

}
