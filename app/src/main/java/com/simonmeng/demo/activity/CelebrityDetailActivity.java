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


        Bundle bundle = this.getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("content");
        content = (NewsDetailBean.Contentlist) list.get(0);

        if (content.imageurls.size() != 0) {
            String imageurl = content.imageurls.get(0).url;
            bitmapUtils = new BitmapUtils(this);
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
            bitmapUtils.display(celebrityDetailImageView, imageurl);
        } else {
            celebrityDetailImageView.setImageResource(R.mipmap.news_toppic_default);
        }

        if((content.title).contains(" ")){
            celebrityDetailTitleTextView.setText(removeString((content.title)," ",1));
        }else {
            celebrityDetailTitleTextView.setText(content.title);
        }
        if (!TextUtils.isEmpty(content.long_desc)) {
            celebrityDetailLongDescTextView.setText(content.long_desc);
        } else {
            celebrityDetailLongDescTextView.setText("NO LONG DESC");
        }
        celebrityDetailSourceTextView.setText(content.source);
        celebrityDetailDateTextView.setText(content.pubDate);

    }

    /**
     * @param s      要操作的字符串
     * @param string 要删除的字符
     * @param i      删除第几个
     * @return
     */
    /***修改一下，需求标题太长，总是换行，但是一般换行的标题中间都有空格---如果有一个空格，删除空格后面的全部，如果有多个，就删除第二
     * notice：
     * substring(int beginIndex) 返回一个新的字符串，它是从指定索引处的字符开始，直到此字符串末尾
     * substring(int beginIndex, int endIndex)从指定的 beginIndex 处开始，直到索引 endIndex - 1 处的字符
     * 这个方法很屌：如果指定删除第一个，直接截取删除，如果不是，也执行截取，但是把指定的字符串也截取进来，其实就是没删除
     * 然后，i--,再循环一次，知道循环到第i个，i--为1时，执行删除操作----这个就是编程的魅力，逻辑的魅力吧
     * */

    public String removeString(String s, String string, int i) {
        if (i == 1) {
            int j = s.indexOf(string);
            s = s.substring(0, j);
            i--;
            return s;
        } else {
            int j = s.indexOf(string);
            i--;
            return s.substring(0, j + 1) + removeString(s.substring(j + 1), string, i);
        }

    }

}
