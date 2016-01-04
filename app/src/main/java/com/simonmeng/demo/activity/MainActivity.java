package com.simonmeng.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonmeng.demo.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gv_main_menu);
        gridview.setAdapter(new HomeAdapter());
    }

    public void onClick(View view){
        Intent intent = new Intent();
        intent.setClass(this,WeatherActivity.class);
        startActivity(intent);
    }
    private class HomeAdapter extends BaseAdapter {
        int[] imageId = { R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e, R.mipmap.f, R.mipmap.g, R.mipmap.h,};

        String[] names = { "天气预报", "通讯卫士", "美食菜谱", "在线视频", "流量统计", "图片浏览",
                "断线下载", "新闻浏览" };

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getApplicationContext(),
                    R.layout.item_main, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.mImageView);
            TextView tv_name = (TextView) view.findViewById(R.id.mTextView);
            iv_icon.setImageResource(imageId[position]);
            tv_name.setText(names[position]);

            return view;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}