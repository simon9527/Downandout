package com.simonmeng.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

        gridview.setOnItemClickListener(new OnItemClickListener() {
            // parent GridView
            // view 每个条目的布局
            // position 点击的条目的位置
            // id
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:// 天气预报
                        Intent intent0 = new Intent(MainActivity.this,
                                WeatherActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:// 新闻浏览
                        Intent intent1 = new Intent(MainActivity.this,
                                NewsActivity.class);
                        startActivity(intent1);
                        break;
                    case 2: // 菜谱界面
                        Intent intent2 = new Intent(MainActivity.this,
                                RecipeActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:// 断点下载
                        Intent intent3 = new Intent(MainActivity.this,
                                DownloadActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:// 图片浏览
                        Intent intent4 = new Intent(MainActivity.this,
                                GalleryActivity.class);
                        startActivity(intent4);
                        break;
                    case 5: // 在线视频
                        Intent intent5 = new Intent(MainActivity.this,
                                VideoOnlineActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:// 备用界面
                        Intent intent6 = new Intent(MainActivity.this,
                                StandbyActivity.class);
                        startActivity(intent6);
                        break;

                    case 7: // 自我介绍
                        Intent intent7 = new Intent(MainActivity.this,
                                AboutMeActivity.class);
                        startActivity(intent7);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void onClick(View view){
        Intent intent = new Intent();
        intent.setClass(this,WeatherActivity.class);
        startActivity(intent);
    }
    private class HomeAdapter extends BaseAdapter {
        int[] imageId = { R.mipmap.a,R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e, R.mipmap.f, R.mipmap.g, R.mipmap.h, };

        String[] names = { "天气预报",  "新闻浏览", "美食菜谱", "断线下载","图片浏览","在线视频", "流量统计",
                "自我介绍" };

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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }
}