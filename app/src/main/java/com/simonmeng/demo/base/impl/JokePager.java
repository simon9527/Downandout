package com.simonmeng.demo.base.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simonmeng.demo.R;
import com.simonmeng.demo.base.BaseRadioButtonPager;
import com.simonmeng.demo.utils.TypefaceUtils;


public class JokePager extends BaseRadioButtonPager {
    public WebView wv;
    public Button bt;
    public int togglele = 1;
    public static int SHOUQI = 1;
    public static int XIANGQING = 0;

    public JokePager(Context context) {
        super(context);
    }


    @Override
    public void initData() {

        tv_base_radio_button_pager_header.setText("Joke");
        TypefaceUtils.setCustomTypeface(mContext, tv_base_radio_button_pager_header);
        ib_title_menu.setVisibility(View.GONE);


        RelativeLayout rl = (RelativeLayout) View.inflate(mContext, R.layout.activity_about_me, null);
        frameLayoutContent.addView(rl);
        ListView lv = (ListView) rl.findViewById(R.id.lv_aboutme);
        lv.setAdapter(new Myadapter());

//        bt = (Button) rl.findViewById(R.id.bt_aboutme);
//        wv = (WebView) rl.findViewById(R.id.wv_aboutme);
//        wv.setVerticalScrollBarEnabled(false);
//        wv.loadUrl("http://view.inews.qq.com/a/ENT2016011301562504");
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (togglele == SHOUQI) {
//                    wv.setVisibility(View.VISIBLE);
//                    bt.setText("收起");
//                    togglele = XIANGQING;
//                } else {
//                    wv.setVisibility(View.GONE);
//                    bt.setText("详情");
//                    togglele = SHOUQI;
//                }
//            }
//        });

    }

    class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 1;
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
            View view = View.inflate(mContext.getApplicationContext(),R.layout.item_aboutme_test,null);
            TextView tv = (TextView) view.findViewById(R.id.tv_zhengwen);
            Button bt = (Button) view.findViewById(R.id.bt_aboutme);
            Button btq = (Button) view.findViewById(R.id.bt_shouqi);
            WebView wv = (WebView) view.findViewById(R.id.wv_aboutme);
            wv.setVerticalScrollBarEnabled(false);
            wv.loadUrl("http://view.inews.qq.com/a/ENT2016011301562504");
            return view;
        }
    }
}
