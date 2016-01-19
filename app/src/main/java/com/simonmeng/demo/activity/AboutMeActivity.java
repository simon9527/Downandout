package com.simonmeng.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;

import java.util.List;

public class AboutMeActivity extends Activity {
    private List<NewsDetailBean.Contentlist> contentlist;
    private BitmapUtils bitmapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        String httpUrl = Constants.httpNewsDetailUrl+"5572a10ab3cdc86cf39001eb";
        getCelebrityData(httpUrl, "5572a10ab3cdc86cf39001eb");
        StaggeredGridView gridView = (StaggeredGridView) findViewById(R.id.grid_view);
        CelebrityGridViewAdapter celebrityGridViewAdapter = new CelebrityGridViewAdapter();
        gridView.setAdapter(celebrityGridViewAdapter);
    }



    public void getCelebrityData(String httpUrl, final String id) {
        String json = CacheUtils.getString(this, id, null);
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

    }

//    // TODO: 2016/1/18 item点击事件
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String celbrityUrl = contentlist.get(position).link;
//        Intent intent=new Intent(mContext, WorldNewsDetailActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("deatailUrl", celbrityUrl);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
//    }

    // TODO: 2016/1/18 gridview的adapter
    class CelebrityGridViewAdapter extends BaseAdapter {
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
                convertView = View.inflate(getApplicationContext(),R.layout.item_celebrity,null);
                celebrityViewHolder = new CelebrityViewHolder();
                celebrityViewHolder.iv_celebrity_item = (ImageView)convertView.findViewById(R.id.iv_celebrity_item);
                celebrityViewHolder.tv_celebrity_item = (TextView) convertView.findViewById(R.id.tv_celebrity_item);
                convertView.setTag(celebrityViewHolder);
            }else {
                celebrityViewHolder = (CelebrityViewHolder) convertView.getTag();
            }
            celebrityViewHolder.tv_celebrity_item.setText(contentlist.get(position).title);
            if((contentlist.get(position).imageurls).size()!=0){
                String url = contentlist.get(position).imageurls.get(0).url;
                System.out.print(url);
                bitmapUtils.display(celebrityViewHolder.iv_celebrity_item,url);
            }else{
                celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_toppic_default);
            }
            //celebrityViewHolder.iv_celebrity_item.setImageResource(R.mipmap.news_default);

            return convertView;
        }
    }
    class CelebrityViewHolder{
        public ImageView iv_celebrity_item;
        public TextView tv_celebrity_item;
    }
}

