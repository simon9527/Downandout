package com.simonmeng.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.WeatherBean;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    private String tag = "WeatherActivity";
    private SharedPreferences sp;
    private String weather_location;
    private int weather_pm;
    private String weather_qlty;
    private int max;
    private int min;
    private int tmp;
    private WeatherBean bean;
    private Gson gson;

    LinearLayout ll_weather_background;
    ListView lv_weather_forecastweather;
    TextView tv_weather_location;
    TextView tv_weather_temperature;
    TextView tv_weather_pm;
    TextView tv_weather_maxmin;
    LinearLayout ll_weather_listheader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tv_weather_location = (TextView) findViewById(R.id.tv_weather_location);
        tv_weather_temperature = (TextView) findViewById(R.id.tv_weather_temperature);
        tv_weather_pm = (TextView) findViewById(R.id.tv_weather_pm);
        tv_weather_maxmin = (TextView) findViewById(R.id.tv_weather_maxmin);
        ll_weather_background = (LinearLayout) findViewById(R.id.ll_weather_background);
        lv_weather_forecastweather = (ListView) findViewById(R.id.lv_weather_forecastweather);

        //设置字体，typeface字体。
        Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
        tv_weather_pm.setTypeface(face);
        tv_weather_maxmin.setTypeface(face);
        tv_weather_temperature.setTypeface(face);

        sp = this.getSharedPreferences("weather_config", MODE_PRIVATE);
        String location = sp.getString("location","北京");

        getDefaultWeather(location);






    }

    //获取搜索界面用户输入的城市名，startActivityForResult开启，onActivityResult获取结果
    public void searchWeather(View view){
        Intent intent = new Intent();
        intent.setClass(WeatherActivity.this,SearchWeatherActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(data != null){
            weather_location= data.getStringExtra("inputLocation");
            getDefaultWeather(weather_location);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getDefaultWeather(String location) {
        Parameters para = new Parameters();
        para.put("city", location);
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("sdkdemo", "onSuccess");
                        //Gson解析服务器返回的json数据---responseString
                        String cityWeather = responseString.replace("HeWeather data service 3.0", "CityWeather");
                        System.out.print(cityWeather);
                        gson = new Gson();
                        bean = gson.fromJson(cityWeather, WeatherBean.class);
                        String statue = bean.CityWeather.get(0).status;
                        WeatherBean.Aqi aqi = bean.CityWeather.get(0).aqi;
                        if (statue.equalsIgnoreCase("ok") && aqi!=null) {
                            weather_location = bean.CityWeather.get(0).basic.city;
                            weather_pm = bean.CityWeather.get(0).aqi.city.pm25;
                            weather_qlty = bean.CityWeather.get(0).aqi.city.qlty;
                            max = bean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
                            min = bean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
                            tmp = bean.CityWeather.get(0).now.tmp;
                            tv_weather_location.setText(weather_location);
                            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n" + weather_qlty);
                            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
                            tv_weather_temperature.setText(tmp + "°C");
                            Log.i("sdkdemo", "onSuccess");
                            System.out.println(bean.CityWeather.get(0).daily_forecast.get(1).tmp.max+"");
                            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
                            //2.3之后取出顶部底部的阴影，android:overScrollMode=”never”代码setOverScrollMode(View.OVER_SCROLL_NEVER);
                            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);
                            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
                            int cityid = Integer.parseInt(bean.CityWeather.get(0).basic.id.substring(2));
                            int pm = bean.CityWeather.get(0).aqi.city.pm25;
                            String location = bean.CityWeather.get(0).basic.city;
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("location", location);
                            editor.commit();
                            Log.i(tag, cityid + "");
                            switch (pm/40) {
                                case 0://优蓝
                                    //ll_weather_background.setBackgroundResource(R.mipmap.city_beijing);
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#2196F3"));
                                    break;
                                case 1://良绿
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#21C3A8"));
                                    break;
                                case 2://轻橙
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#FF7F24"));
                                    break;
                                case 3://中红
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#CD5C5C"));
                                    break;
                                case 4://重紫
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#303F9F"));
                                    break;
                                case 5://严褐
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#727272"));
                                    break;

                                default://爆黑
                                    ll_weather_background.setBackgroundColor(Color.parseColor("#212121"));
                                    break;
                            }
                        }
                        else if(statue.equalsIgnoreCase("ok") && aqi==null){
                            //二线城市
                            weather_location = bean.CityWeather.get(0).basic.city;
                            max = bean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
                            min = bean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
                            tmp = bean.CityWeather.get(0).now.tmp;
                            tv_weather_location.setText(weather_location);
                            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n null");
                            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
                            tv_weather_temperature.setText(tmp + "°C");
                            Log.i("sdkdemo", "onSuccess");
                            System.out.println(bean.CityWeather.get(0).daily_forecast.get(1).tmp.max+"");
                            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
                            //2.3之后取出顶部底部的阴影，2.3之前xml中android:fadingEdge="none"即可
                            lv_weather_forecastweather.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);
                            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
                            int cityid = Integer.parseInt(bean.CityWeather.get(0).basic.id.substring(2));
                            String location = bean.CityWeather.get(0).basic.city;
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("location", location);
                            editor.commit();
                            Log.i(tag, cityid + "");
                            ll_weather_background.setBackgroundColor(Color.parseColor("#BBA87E"));
                        }
                        else{//情况1：status是"unknown city"；情况2：三线城市没有aqi
                            Toast.makeText(WeatherActivity.this,"没有查到该城市",Toast.LENGTH_LONG).show();
                            NoWeaherAdapter noWeaherAdapter = new NoWeaherAdapter();
                            lv_weather_forecastweather.setAdapter(noWeaherAdapter);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.i("sdkdemo", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.i("sdkdemo", "onError, status: " + status);
                        Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
                        tv_weather_location.setText("");
                    }
                });
    }

    //TODO 创建一个listview展示天气预报

    private class WeatherForcastAdapter extends BaseAdapter{
        List<WeatherBean.Daily_forecast> daily_forecasts = null;
        @Override
        public int getCount() {
            return 8;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position==0){
                TextView tvHeader = new TextView(getApplicationContext());
                tvHeader.setText("Weather Forecast ");
                Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
                tvHeader.setTypeface(face);
                tvHeader.setTextColor(getResources().getColor(R.color.colorWhite));
                tvHeader.setTextSize(25);
                tvHeader.setBackgroundColor(00000000);
                tvHeader.setPadding(0, 700, 0, 0);
                tvHeader.setGravity(Gravity.CENTER);
                return tvHeader;
            }else if(position==7){
                TextView tvHeader = new TextView(getApplicationContext());
                tvHeader.setText("天气转变 冷暖先知");
                tvHeader.setTextColor(getResources().getColor(R.color.colorWhite));
                tvHeader.setTextSize(15);
                tvHeader.setBackgroundColor(00000000);
                tvHeader.setPadding(0, 0, 0, 300);
                tvHeader.setGravity(Gravity.CENTER);
                return tvHeader;
            }
            View view = null;
            ViewHolder viewHolder = null;
            if(convertView!=null&&convertView instanceof LinearLayout){
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),R.layout.item_weatherforcast,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_weatherforcast_date = (TextView) view.findViewById(R.id.tv_item_weatherforcast_date);
                viewHolder.tv_item_weatherforcast_min = (TextView) view.findViewById(R.id.tv_item_weatherforcast_min);
                viewHolder.tv_item_weatherforcast_max = (TextView) view.findViewById(R.id.tv_item_weatherforcast_max);
                view.setTag(viewHolder);
            }
            //设置字体
            Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
            viewHolder.tv_item_weatherforcast_date.setTypeface(face);
            viewHolder.tv_item_weatherforcast_min.setTypeface(face);
            viewHolder.tv_item_weatherforcast_max.setTypeface(face);
            if(bean.CityWeather.get(0)!=null){
                List<WeatherBean.Daily_forecast> daily_forecasts =  bean.CityWeather.get(0).daily_forecast;
                viewHolder.tv_item_weatherforcast_date.setText(daily_forecasts.get(position).date);
                viewHolder.tv_item_weatherforcast_min.setText("Mix ↓"+daily_forecasts.get(position).tmp.min+"°");
                viewHolder.tv_item_weatherforcast_max.setText("Max ↑"+daily_forecasts.get(position).tmp.max+"°");
                return view;
            }else {
                return null;
            }

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
    class ViewHolder{
        TextView tv_item_weatherforcast_date;
        TextView tv_item_weatherforcast_min;
        TextView tv_item_weatherforcast_max;
    }

    private class NoWeaherAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
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
            return null;
        }
    }
}