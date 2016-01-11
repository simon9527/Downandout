package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.apistore.GetNewsData;
import com.simonmeng.demo.domain.CookDetailBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GalleryActivity extends AppCompatActivity {

    private String tag = "GalleryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        TextView tv_gallery = (TextView) findViewById(R.id.tv_gallery);
        ImageView mImageView = (ImageView) findViewById(R.id.mImageView);

        String httpCookStyleArg = "?name=宫保鸡丁";
        String weiCookStyleJs = getCookStyle(httpCookStyleArg);
        //注意在字符串中加引号需要转义符号
//        String weiCookStyleJson = "{\"CookStyle\":["+weiCookStyleJs+"]}";
        Gson cookStyleGson = new Gson();
        CookDetailBean bean = cookStyleGson.fromJson(weiCookStyleJs,CookDetailBean.class);
        String name = bean.description;
        tv_gallery.setText(name);
//
//        String newsList = getNewsList();
//        tv_gallery.setText(newsList);
//        System.out.print(newsList);
//        Gson gson = new Gson();
//        NewsListBean bean = gson.fromJson(newsList, NewsListBean.class);
//        String name = bean.showapi_res_body.channelList.get(0).name;
//
//        List<NewsListBean.ChannelList> channelList = bean.showapi_res_body.channelList;

//        List<String> newsListName = new ArrayList<>();
//        for(int i=0;i<channelList.size();i++){
//            String newslistname = channelList.get(i).name;
//            newsListName.add(i,newslistname);
//        }

//        String id = channelList.get(8).channelId;
//        //String httpNewsDetailArg = "channelId=5572a109b3cdc86cf39001db&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1";
//        String httpNewsDetailArg = "channelId="+id+"&page=1";
//
//        String newDetail = getNewsDetail(httpNewsDetailArg);
//        Gson gsonson = new Gson();
//        NewsDetailBean newsDetailBean = gsonson.fromJson(newDetail, NewsDetailBean.class);
//        String lin = newsDetailBean.showapi_res_body.pagebean.contentlist.get(2).imageurls.get(0).url;
//
//        try {
//            URL url = new URL(picUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setConnectTimeout(5000);
//            int code = conn.getResponseCode();
//            if(code==200){
//                InputStream is = conn.getInputStream();
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                mImageView.setImageBitmap(bitmap);
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
    public String getNewsList(){
        String httpNewsListUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";
        String httpNewsListArg = "";
        String jsonResult = GetNewsData.request(httpNewsListUrl, httpNewsListArg);
        return jsonResult;
    }
    public String getNewsDetail(String httpNewsDetailArg){
        String httpNewsDetailUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
        String jsonResult = GetNewsData.request(httpNewsDetailUrl, httpNewsDetailArg);
        return jsonResult;
    }




        public  String getCookStyle( String httpCookStyleArg) {
            String httpCookStyleUrl = "http://apis.baidu.com/tngou/cook/name";
            BufferedReader reader = null;
            String result = null;
            StringBuffer sbf = new StringBuffer();
            httpCookStyleUrl = httpCookStyleUrl + "?" + httpCookStyleArg;

            try {
                URL url = new URL(httpCookStyleUrl);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                // 填入apikey到HTTP header
                connection.setRequestProperty("apikey",  "70cceff1ddfa11c873baefb4ed264850");
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
