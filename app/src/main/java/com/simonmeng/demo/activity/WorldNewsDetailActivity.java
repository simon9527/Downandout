package com.simonmeng.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.simonmeng.demo.R;

public class WorldNewsDetailActivity extends Activity {
    private  WebView wv_world_news_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_news_detail);
        wv_world_news_detail = (WebView) findViewById(R.id.wv_world_news_detail);
        WebSettings settings = wv_world_news_detail.getSettings();

        //settings.setJavaScriptEnabled(true);
        wv_world_news_detail.setWebViewClient(new WebViewClient() {
                                       public boolean shouldOverrideUrlLoading(WebView view, String url)
                                       {
                                           view.loadUrl(url);
                                           return true;
                                       }
                                   });
        Bundle bundle=this.getIntent().getExtras();
        String detailUrl = bundle.get("deatailUrl").toString();
        wv_world_news_detail.loadUrl(detailUrl);
    }
}
