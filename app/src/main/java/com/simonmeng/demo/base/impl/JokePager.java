package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.text.Html;
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
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.domain.JokeDetailBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class JokePager extends BaseRadioButtonPager {
    @ViewInject(R.id.lv_news_joke)
    private ListView jokeListView;
    private List<JokeDetailBean.Contentlist> jokeList;


    public JokePager(Context context) {
        super(context);
    }
    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Joke");
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        ib_title_menu.setVisibility(View.GONE);

        View view = View.inflate(mContext, R.layout.news_joke,null);
        ViewUtils.inject(this, view);
        frameLayoutContent.addView(view);
        getJokeDetailData(Constants.httpJokeDetailUrl);
    }

    public void getJokeDetailData(String httpUrl) {
        String json = CacheUtils.getString(mContext,"joke", null);
        if(!TextUtils.isEmpty(json)){
            processDetailData(json);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if(!(TextUtils.isEmpty(responseInfo.result))){
                    CacheUtils.putString(mContext, "joke", responseInfo.result);
                    String aa = responseInfo.result;
                    System.out.print(aa);
                    processDetailData(responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // TODO: 2016/1/15 获取失败应该处理空指针异常
            }
        });
    }


    protected void processDetailData(String json){
        Gson gson = new Gson();
        JokeDetailBean jokeDetailBean = gson.fromJson(json, JokeDetailBean.class);

        jokeList = jokeDetailBean.showapi_res_body.contentlist;
        NewsHomeListViewAdapter newsHomeListViewAdapter = new NewsHomeListViewAdapter();
        jokeListView.setAdapter(newsHomeListViewAdapter);

    }

    class NewsHomeListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() { return jokeList.size();}
        @Override
        public Object getItem(int position) { return null;}
        @Override
        public long getItemId(int position) { return 0;}
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JokeViewHolder jokeViewHolder = null;
            if(jokeViewHolder == null){
                convertView = View.inflate(mContext,R.layout.item_joke,null);
                jokeViewHolder = new JokeViewHolder();
                jokeViewHolder.tv_joke_title = (TextView) convertView.findViewById(R.id.tv_joke_title);
                jokeViewHolder.tv_joke_date = (TextView) convertView.findViewById(R.id.tv_joke_date);
                jokeViewHolder.tv_joke_time = (TextView) convertView.findViewById(R.id.tv_joke_time);
                jokeViewHolder.tv_joke_text = (TextView) convertView.findViewById(R.id.tv_joke_text);
                convertView.setTag(jokeViewHolder);
            }else {
                jokeViewHolder = (JokeViewHolder) convertView.getTag();
            }
            try {
                SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                // SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy年MM月dd日 " );
                Date date = sdf.parse(jokeList.get(position).ct);
                sdf = new SimpleDateFormat("MMM\nd", Locale.US);
                System.out.println(sdf.format(date));
                jokeViewHolder.tv_joke_date.setText(sdf.format(date));
                sdf = new SimpleDateFormat("h:mm");
                jokeViewHolder.tv_joke_time.setText(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String titleSBCCase = jokeList.get(position).title;
            String title = titleSBCCase.replaceAll("[ |　]", "");
            jokeViewHolder.tv_joke_title.setText(title);
            String text = jokeList.get(position).text.replaceAll("[ |　]", "");
            String jokeText = "<p>"+text+"</p>";
            System.out.println(jokeText);
            jokeViewHolder.tv_joke_text.setText(Html.fromHtml(jokeText));
            return convertView;
        }
    }

    class JokeViewHolder{
        public TextView tv_joke_title;
        public TextView tv_joke_date;
        public TextView tv_joke_time;
        public TextView tv_joke_text;
    }
}
