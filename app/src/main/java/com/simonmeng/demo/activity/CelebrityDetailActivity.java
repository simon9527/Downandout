package com.simonmeng.demo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.NewsDetailBean;

import java.util.ArrayList;

public class CelebrityDetailActivity extends AppCompatActivity {

    private NewsDetailBean.Contentlist content;
    private ImageView celebrityDetailImageView;
    TextView celebrityDetailTitleTextView;
    TextView celebrityDetailLongDescTextView;
    TextView celebrityDetailSourceTextView;
    TextView celebrityDetailDateTextView;
    private BitmapUtils bitmapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity_detail);
        celebrityDetailImageView = (ImageView) findViewById(R.id.iv_celebrity_detail);
        celebrityDetailTitleTextView = (TextView) findViewById(R.id.tv_celebrity_detail_title);
        celebrityDetailLongDescTextView = (TextView) findViewById(R.id.tv_celebrity_detail_long_desc);
        celebrityDetailSourceTextView = (TextView) findViewById(R.id.tv_celebrity_detail_source);
        celebrityDetailDateTextView = (TextView) findViewById(R.id.tv_celebrity_detail_date);


        Bundle bundle=this.getIntent().getExtras();
        ArrayList list= bundle.getParcelableArrayList("content");
        content = (NewsDetailBean.Contentlist) list.get(0);

        if(content.imageurls.size()!=0){
            String imageurl = content.imageurls.get(0).url;
            bitmapUtils = new BitmapUtils(this);
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
            bitmapUtils.display(celebrityDetailImageView, imageurl);
        }else {
            celebrityDetailImageView.setImageResource(R.mipmap.news_toppic_default);
        }



        celebrityDetailTitleTextView.setText(content.title);
        if(!TextUtils.isEmpty(content.long_desc)){
            celebrityDetailLongDescTextView.setText(content.long_desc);
        }else {
            celebrityDetailLongDescTextView.setText("NO LONG DESC");
        }
        celebrityDetailSourceTextView.setText(content.source);
        celebrityDetailDateTextView.setText(content.pubDate);

    }
}
