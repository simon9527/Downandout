package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.utils.AsykTaskUtils;
import com.simonmeng.demo.utils.Constants;
import com.simonmeng.demo.utils.TypefaceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CelebrityPager extends BaseRadioButtonPager {
    public static final int GETCELESUCCESS = 1;
    public static final int GETPICSUCCESS = 2;
    public static final int GETPICFAILED = 3;

    public String celebrityNews;
    public String celebrityUrl;
    public Bitmap bitmap;
    public ImageView iv_celebrity;
    public String urlll;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETCELESUCCESS) {
                urlll = (String)msg.obj;
                new AsykTaskUtils() {
                    @Override
                    public void afterSubThreadTask() {
                    }
                    @Override
                    public void beforeSubThreadTask() {
                    }
                    @Override
                    public void doSubThreadTaskInBackground() {
                        try {
                            URL url = new URL(urlll);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                                Message msg = new Message();
                                msg.what = GETPICSUCCESS;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
            }else if(msg.what==GETPICSUCCESS){
                iv_celebrity.setImageBitmap(bitmap);
            }else if(msg.what==GETPICFAILED){
                iv_celebrity.setImageResource(R.mipmap.h);
            }
        }
    };

    public CelebrityPager(Context context) {
        super(context);
    }


    @Override
    public void initData() {
        tv_base_radio_button_pager_header.setText("Celebrity");
        ib_title_menu.setVisibility(View.GONE);
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);

//        LinearLayout celebrity = (LinearLayout) View.inflate(mContext, R.layout.celebrity, null);
//        iv_celebrity = (ImageView) celebrity.findViewById(R.id.iv_celebrity);
        iv_celebrity = new ImageView(mContext);
        frameLayoutContent.addView(iv_celebrity);


        new AsykTaskUtils() {
            @Override
            public void afterSubThreadTask() {
                Gson gson = new Gson();
                NewsDetailBean bean = gson.fromJson(celebrityNews, NewsDetailBean.class);
                if((bean.showapi_res_body.pagebean.contentlist.get(19).imageurls).size()!=0){
                    celebrityUrl = bean.showapi_res_body.pagebean.contentlist.get(19).imageurls.get(0).url;
                    Message msg = new Message();
                    msg.what = GETCELESUCCESS;
                    msg.obj = celebrityUrl;
                    handler.sendMessage(msg);
                }else {
                    Message msg = new Message();
                    msg.what = GETPICFAILED;
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void beforeSubThreadTask() {

            }

            @Override
            public void doSubThreadTaskInBackground() {
                String httpCelebrityId = "5572a108b3cdc86cf39001d5";
                celebrityNews = getNetworkData(Constants.httpNewsDetailUrl, "channelId="+httpCelebrityId+"&page=1");
                String duonei =   getNetworkData(Constants.httpNewsDetailUrl, "channelId="+"5572a108b3cdc86cf39001cd"+"&page=1");
                //娱乐最新
                String yulezuixin =   getNetworkData(Constants.httpNewsDetailUrl, "channelId="+"5572a10ab3cdc86cf39001eb"+"&page=1");
                //体育最新
                String tiyu =   getNetworkData(Constants.httpNewsDetailUrl, "channelId="+"5572a109b3cdc86cf39001e6"+"&page=1");

            }
        }.execute();

    }

    public String getNetworkData(String httpUrl, String httpArg) {
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
        return result;
    }
}
