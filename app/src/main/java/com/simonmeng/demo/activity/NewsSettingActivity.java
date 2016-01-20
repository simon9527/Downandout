package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.NewsListBean;
import com.simonmeng.demo.fragment.NewsLeftFragment;
import com.simonmeng.demo.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsSettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
   // private ListView newsSettingListView;
    private GridView newsSettingGridView;
    private List<NewsListBean.ChannelList> channelList;
    private final String hadSelectedChannelIDArrayKey = "had_selected_channel_id_array_key";
    private NewsSettingAdapter newsSettingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_setting);
        //newsSettingListView = (ListView) findViewById(R.id.lv_news_setting);
        newsSettingGridView = (GridView) findViewById(R.id.lv_news_setting);
        Bundle bundle=this.getIntent().getExtras();
        ArrayList list= bundle.getParcelableArrayList("channelList");
        channelList = (List<NewsListBean.ChannelList>) list.get(0);

        newsSettingAdapter = new NewsSettingAdapter();
        newsSettingGridView.setAdapter(newsSettingAdapter);
        newsSettingGridView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        NewsActivity newsActivity = (NewsActivity) GetNewsActivity.NewsActiviy;
        NewsLeftFragment newsLeftFragment= newsActivity.getNewsLeftFragment();
        newsLeftFragment.setNewsDataList(channelList);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String channelID = channelList.get(position).name;

        String hadSelectedChannelIDArray = CacheUtils.getString(this, hadSelectedChannelIDArrayKey, null);
        if(!TextUtils.isEmpty(hadSelectedChannelIDArray) && !(hadSelectedChannelIDArray.contains(channelID))) {
            CacheUtils.putBoolean(getApplicationContext(),"hasSetted",true);
            hadSelectedChannelIDArray = hadSelectedChannelIDArray + "," +channelID;
        }else if(!TextUtils.isEmpty(hadSelectedChannelIDArray) && hadSelectedChannelIDArray.contains(channelID)){
             hadSelectedChannelIDArray = hadSelectedChannelIDArray.replace(","+channelID,"");
            //hadSelectedChannelIDArray = removeChannelID(hadSelectedChannelIDArray,channelID,1);
        }else {
            hadSelectedChannelIDArray = channelID;
        }
        CacheUtils.putString(this, hadSelectedChannelIDArrayKey, hadSelectedChannelIDArray);
        //已读--文字颜色变灰--通知listview发生变化--所以Adapter就会被调用--在Adapter的getView中添加颜色变灰的方法
        newsSettingAdapter.notifyDataSetChanged();
    }
    /**
     * @param s  要操作的字符串
     * @param string  要删除的字符
     * @param i  删除第几个
     * @return
     * notice：
     * substring(int beginIndex) 返回一个新的字符串，它是从指定索引处的字符开始，直到此字符串末尾
     * substring(int beginIndex, int endIndex)从指定的 beginIndex 处开始，直到索引 endIndex - 1 处的字符
     * 这个方法很屌：如果指定删除第一个，直接截取删除，如果不是，也执行截取，但是把指定的字符串也截取进来，其实就是没删除
     * 然后，i--,再循环一次，知道循环到第i个，i--为1时，执行删除操作----这个就是编程的魅力，逻辑的魅力吧
     */
    public String removeChannelID(String s,String string,int i){
        if(i==1){
            int j=s.indexOf(string);
            s=s.substring(0, j)+s.substring(j+1);
            i--;
            return s;
        }else{
            int j=s.indexOf(string);
            i--;
            return s.substring(0, j + 1) + removeChannelID(s.substring(j+1), string, i);
        }
    }

    public class NewsSettingAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return channelList.size();
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
            TextView tv_item_news_left_fragment = null;
            ViewHolder viewHolder = null;
            if (convertView != null) {
                tv_item_news_left_fragment = (TextView) convertView;
                viewHolder = (ViewHolder) tv_item_news_left_fragment.getTag();
            } else {
                tv_item_news_left_fragment = (TextView) View.inflate(getApplicationContext(),R.layout.item_news_left_setting, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_news_left_fragment = tv_item_news_left_fragment;
                tv_item_news_left_fragment.setTag(viewHolder);
            }
            String hadSelectedChannelIDArray = CacheUtils.getString(getApplicationContext(), hadSelectedChannelIDArrayKey, null);
            if (!TextUtils.isEmpty(hadSelectedChannelIDArray) && hadSelectedChannelIDArray.contains(channelList.get(position).name)) {
                //viewHolder.tv_item_news_left_fragment.setTextColor(getApplicationContext().getResources().getColor(R.color.tealGreen));
                viewHolder.tv_item_news_left_fragment.setEnabled(true);
            } else {
               // viewHolder.tv_item_news_left_fragment.setTextColor(getApplicationContext().getResources().getColor(R.color.indianRed));
                viewHolder.tv_item_news_left_fragment.setEnabled(false);
            }

            viewHolder.tv_item_news_left_fragment.setText(channelList.get(position).name);
            return tv_item_news_left_fragment;
        }
    }
    class ViewHolder {
        TextView tv_item_news_left_fragment;
    }
}
