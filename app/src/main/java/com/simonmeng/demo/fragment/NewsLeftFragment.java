package com.simonmeng.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.simonmeng.demo.R;
import com.simonmeng.demo.activity.MainActivity;
import com.simonmeng.demo.activity.NewsActivity;
import com.simonmeng.demo.activity.NewsSettingActivity;
import com.simonmeng.demo.activity.WeatherActivity;
import com.simonmeng.demo.base.BaseFragment;
import com.simonmeng.demo.base.impl.HomePager;
import com.simonmeng.demo.domain.NewsListBean;
import com.simonmeng.demo.utils.CacheUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NewsLeftFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView mListView;
    private List<NewsListBean.ChannelList> channelList;
    // 当前可用的选项的索引
    private int currentEnalbledPosition;
    private NewsLeftAdapter newsLeftAdapter;
    private TextView newsLeftSetting;
    private TextView tv_news_left_weather;
    private TextView tv_news_left_head;
    public List<String> channelIDList;


    @Override
    public void initData() {
        if(CacheUtils.getBoolean(mActivity,"hasSetted",false)){
            String selectedChannelID = CacheUtils.getString(mActivity,"had_selected_channel_id_array_key",null);
            String[] arr=selectedChannelID.split(",");
            channelIDList = Arrays.asList(arr);
            currentEnalbledPosition = 0;
            newsLeftAdapter = new NewsLeftAdapter();
            mListView.setAdapter(newsLeftAdapter);
            newsLeftAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View initView() {
        //初始化initView，就是要创建view然后返回即可，就会自动挂载到Fragment上。
        LinearLayout testss = (LinearLayout) View.inflate(mActivity,R.layout.news_left_slidingmenu,null);
        mListView = (ListView) testss.findViewById(R.id.lv_testss);
        newsLeftSetting = (TextView) testss.findViewById(R.id.tv_news_left_setting);
        tv_news_left_weather = (TextView) testss.findViewById(R.id.tv_news_left_weather);
        tv_news_left_head = (TextView) testss.findViewById(R.id.tv_news_left_head);
        newsLeftSetting.setOnClickListener(this);
        tv_news_left_weather.setOnClickListener(this);
        tv_news_left_head.setOnClickListener(this);
        /**给Listview添加点击事件，哪个item被点击，就把enabled设为true，就变成绿色*/
        mListView.setOnItemClickListener(this);
        return testss;
    }
    public void refreshListView(){
        String selectedChannelID = CacheUtils.getString(mActivity, "had_selected_channel_id_array_key", null);
        String[] arr=selectedChannelID.split(",");
        channelIDList = Arrays.asList(arr);
        NewsLeftAdapter adapter = new NewsLeftAdapter();
        mListView.setAdapter(adapter);
       // newsLeftAdapter.notifyDataSetChanged();
    }

    public void setNewsDataList(List<NewsListBean.ChannelList> channelList) {
        //这个channelList还是有用的，把它传给Setting
        this.channelList = channelList;
        //这里默认就不要null，毕竟通过channeList可以去到id，还是做个默认值吧，省得空指针
        String selectedChannelID = CacheUtils.getString(mActivity,"had_selected_channel_id_array_key",channelList.get(0).name);
        String[] arr=selectedChannelID.split(",");
        channelIDList = Arrays.asList(arr);

        /**在Listview添加adapter时，默认它的第一项是被选中的，故在此处currentEnalbledPosition = 0;
         * 这样只要在adapter中的getView方法设置一下currentEnalbledPosition == position，setEnabled(true)这样第一个item就是true
         * getView最后是return View，这样我们看到的第一就是绿色。
         * notice：就是给Listview添加点击监听，这样默认是第一个，然后点击哪个，哪个就变成绿色*/
        currentEnalbledPosition = 0;
        newsLeftAdapter = new NewsLeftAdapter();
        mListView.setAdapter(newsLeftAdapter);


        swichHomePager();
    }
    /**
     * when click the listview's item in LeftFragment,the HomePager should dispaly it.
     * so we should get the HomePager first ,LeftFragment-->NewsActivity-->BaseRadioButtonPager-->HomePager
     * */
    private void swichHomePager() {
        NewsActivity newsActivity = (NewsActivity)mActivity;
        NewsContentFragment newsContentFragment = newsActivity.getNewsContentFragment();
        HomePager homePager = newsContentFragment.getHomePager();
        homePager.switchPagerRespondLeft(currentEnalbledPosition);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**监听Listview的item点击事件，哪个position被点击，currentEnalbledPosition就等于它
         * 然后通过adapter的notifyDataSetChanged告诉adapter数据变化了，然后刷新listview，
         * 在走到adapter中的getView方法，currentEnalbledPosition改变了，然后对应的item的enabled设为true，颜色变绿，然后return View，展现的就是那个item变绿了*/
        currentEnalbledPosition = position;
        newsLeftAdapter.notifyDataSetChanged();
        /**
         * item被点击，调用switchPagerRespondLeft方法，把FrameLayout切换成对应的*/
        swichHomePager();
        //当条目被点击时，Slidingmenu自动隐藏[找了好久--搜Slidingmenu属性--toggle()--如何获得Slidingmenu对象--看了一下NewsActivity获得过，把mActivity转成NewsActivity]
        ((NewsActivity)mActivity).getSlidingMenu().toggle();
    }

    // TODO: 2016/1/19 setting点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_news_left_setting:
                Intent intent=new Intent(mActivity, NewsSettingActivity.class);
                Bundle bundle=new Bundle();
                ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                list.add(channelList);
                bundle.putSerializable("channelList", list);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_news_left_weather:
                Intent weatherIntent = new Intent(mActivity, WeatherActivity.class);
                mActivity.startActivity(weatherIntent);
                break;

            case R.id.tv_news_left_head:
                Intent mainIntent = new Intent(mActivity, MainActivity.class);
                mActivity.startActivity(mainIntent);
                break;
            default:
                break;
        }

    }

    public class NewsLeftAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return channelIDList.size();
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
                tv_item_news_left_fragment = (TextView) View.inflate(mActivity, R.layout.item_news_left_fragment, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_news_left_fragment = tv_item_news_left_fragment;
                tv_item_news_left_fragment.setTag(viewHolder);
            }

           // viewHolder.tv_item_news_left_fragment.setText(channelList.get(position).name);
            viewHolder.tv_item_news_left_fragment.setText(channelIDList.get(position));
            //选中的条目，enabled属性设置成true，这样它就一直是绿色，currentEnalbledPosition == position若等，就是true，设置true就绿。
            tv_item_news_left_fragment.setEnabled(currentEnalbledPosition == position);
            return tv_item_news_left_fragment;
        }
    }
    class ViewHolder {
        TextView tv_item_news_left_fragment;
    }



}

