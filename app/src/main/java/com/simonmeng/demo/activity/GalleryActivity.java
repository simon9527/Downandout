package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.apistore.GetNewsData;
import com.simonmeng.demo.domain.NewsDetailBean;
import com.simonmeng.demo.domain.NewsListBean;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String tag = "GalleryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TextView tv_gallery = (TextView) findViewById(R.id.tv_gallery);
        String newsList = getNewsList();
        tv_gallery.setText(newsList);
        System.out.print(newsList);
        Gson gson = new Gson();
        NewsListBean bean = gson.fromJson(newsList, NewsListBean.class);
        String name = bean.showapi_res_body.channelList.get(0).name;
        tv_gallery.setText(name);

        List<NewsListBean.ChannelList> channelList = bean.showapi_res_body.channelList;

//        List<String> newsListName = new ArrayList<>();
//        for(int i=0;i<channelList.size();i++){
//            String newslistname = channelList.get(i).name;
//            newsListName.add(i,newslistname);
//        }

        String id = channelList.get(6).channelId;
        //String httpNewsDetailArg = "channelId=5572a109b3cdc86cf39001db&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1";
        String httpNewsDetailArg = "channelId="+id+"&page=1";

        String newDetail = getNewsDetail(httpNewsDetailArg);
        Gson gsonson = new Gson();
        NewsDetailBean newsDetailBean = gsonson.fromJson(newDetail, NewsDetailBean.class);
        String dec = newsDetailBean.showapi_res_body.pagebean.contentlist.get(0).desc;
        tv_gallery.setText(dec);

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
}
