package com.simonmeng.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.WeatherBean;
import com.simonmeng.demo.utils.CacheUtils;
import com.simonmeng.demo.utils.Constants;

import java.util.List;

public class WeatherActivity extends Activity {
    private String weather_location;
    private int weather_pm;
    private String weather_qlty;
    private int max;
    private int min;
    private int tmp;
    private String location;
    public WeatherBean.WeatherDetail cityWeather;


    RelativeLayout ll_weather_background;
    ListView lv_weather_forecastweather;

    @ViewInject(R.id.tv_weather_location)
    TextView tv_weather_location;
    @ViewInject(R.id.tv_weather_temperature)
    TextView tv_weather_temperature;
    @ViewInject(R.id.tv_weather_pm)
    TextView tv_weather_pm;
    @ViewInject(R.id.tv_weather_maxmin)
    TextView tv_weather_maxmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tv_weather_location = (TextView) findViewById(R.id.tv_weather_location);
        ll_weather_background = (RelativeLayout) findViewById(R.id.rl_weather_background);
        lv_weather_forecastweather = (ListView) findViewById(R.id.lv_weather_forecastweather);
        View weatherListviewHeader = View.inflate(this,R.layout.activity_weather_listview_header,null);
        ViewUtils.inject(this, weatherListviewHeader);
        lv_weather_forecastweather.addHeaderView(weatherListviewHeader);

        //设置字体，typeface字体。
        Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
        tv_weather_pm.setTypeface(face);
        tv_weather_maxmin.setTypeface(face);
        tv_weather_temperature.setTypeface(face);

        String currentCityName = CacheUtils.getString(this,"currentCityName","北京");
        getWeatherData(Constants.httpWeatherUrl,currentCityName);


    }

    //获取搜索界面用户输入的城市名，startActivityForResult开启，onActivityResult获取结果
    public void searchWeather(View view) {
        Intent intent = new Intent();
        intent.setClass(WeatherActivity.this, SearchWeatherActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String serachCityName = data.getStringExtra("inputLocation");
            getWeatherData(Constants.httpWeatherUrl, serachCityName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /******************************************************************************/
    protected void processWeatherlData(String json) {
        Gson gson = new Gson();
        WeatherBean wBean = gson.fromJson(json,WeatherBean.class);
        String statue = wBean.CityWeather.get(0).status;
        WeatherBean.Aqi aqi = wBean.CityWeather.get(0).aqi;
        cityWeather = wBean.CityWeather.get(0);
        if (statue.equalsIgnoreCase("ok") && aqi != null) {
            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
            //2.3之后取出顶部底部的阴影，android:overScrollMode=”never”代码setOverScrollMode(View.OVER_SCROLL_NEVER);
            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);

            weather_location = wBean.CityWeather.get(0).basic.city;
            CacheUtils.putString(getApplicationContext(),"currentCityName",weather_location);
            weather_pm = wBean.CityWeather.get(0).aqi.city.pm25;
            weather_qlty = wBean.CityWeather.get(0).aqi.city.qlty;
            max = wBean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
            min = wBean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
            tmp = wBean.CityWeather.get(0).now.tmp;
            tv_weather_location.setText(weather_location);
            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n" + weather_qlty);
            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
            tv_weather_temperature.setText(tmp + "°C");
            Log.i("sdkdemo", "onSuccess");
            System.out.println(wBean.CityWeather.get(0).daily_forecast.get(1).tmp.max + "");

            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
            int cityid = Integer.parseInt(wBean.CityWeather.get(0).basic.id.substring(2));
            int pm = wBean.CityWeather.get(0).aqi.city.pm25;

            switch (pm / 40) {
                case 0://优蓝
                    //多种方法设置背景
                    //ll_weather_background.setBackgroundResource(R.mipmap.city_beijing);
                    //ll_weather_background.setBackgroundColor(Color.parseColor("#2196F3"));
                    //ll_weather_background.setBackground(getResources().getDrawable(R.drawable.weather_pm_a_background));
                    ll_weather_background.setBackgroundColor(Color.parseColor("#2196F3"));
                    break;
                case 1://良绿
                    ll_weather_background.setBackgroundColor(Color.parseColor("#21C3A8"));
                    break;
                case 2://轻橙
                    ll_weather_background.setBackgroundColor(Color.parseColor("#FF7F24"));
//                    ll_weather_background.setBackgroundDrawable(getResources().getDrawable(R.drawable.weather_pm_a_background));
                    break;
                case 3://中红
                    ll_weather_background.setBackgroundColor(Color.parseColor("#CD5C5C"));
                    break;
                case 4://重紫
                    ll_weather_background.setBackgroundColor(Color.parseColor("#727272"));
                    break;
                case 5://严褐
                    ll_weather_background.setBackgroundColor(Color.parseColor("#727272"));
                    break;

                default://爆黑
                    ll_weather_background.setBackgroundColor(Color.parseColor("#212121"));
                    break;
            }
        } else if (statue.equalsIgnoreCase("ok") && aqi == null) {
            //二线城市
            weather_location = wBean.CityWeather.get(0).basic.city;
            CacheUtils.putString(getApplicationContext(),"currentCityName",weather_location);
            max = wBean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
            min = wBean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
            tmp = wBean.CityWeather.get(0).now.tmp;
            tv_weather_location.setText(weather_location);
            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n null");
            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
            tv_weather_temperature.setText(tmp + "°C");
            Log.i("sdkdemo", "onSuccess");
            System.out.println(wBean.CityWeather.get(0).daily_forecast.get(1).tmp.max + "");
            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
            //2.3之后取出顶部底部的阴影，2.3之前xml中android:fadingEdge="none"即可
            lv_weather_forecastweather.setOverScrollMode(View.OVER_SCROLL_NEVER);
            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);
            ll_weather_background.setBackgroundColor(Color.parseColor("#BBA87E"));
        } else {//情况1：status是"unknown city"；情况2：三线城市没有aqi
            Toast.makeText(WeatherActivity.this, "没有查到该城市", Toast.LENGTH_LONG).show();
            NoWeaherAdapter noWeaherAdapter = new NoWeaherAdapter();
            lv_weather_forecastweather.setAdapter(noWeaherAdapter);
        }
    }



    public void getWeatherData(String httpWeatherUrl, String cityName) {
        this.location = cityName;
        String json = CacheUtils.getString(this, location, null);
        if (!TextUtils.isEmpty(json)) {
            processWeatherlData(json);
        }
        httpWeatherUrl = httpWeatherUrl + location;
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", Constants.myApiKey);
        utils.send(HttpRequest.HttpMethod.GET, httpWeatherUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //非空判断，只用返回的结果有正确的数值才能存放在sharepreference中，但是一般错误也会返回一些信息，这里就简单地通过长度判断一下
                if (responseInfo.result.length() > 200) {
                    String cityWeatherJson = (responseInfo.result).replace("HeWeather data service 3.0", "CityWeather");
                    CacheUtils.putString(getApplicationContext(),location,cityWeatherJson);
                    processWeatherlData(cityWeatherJson);
                }else {
                    Toast.makeText(WeatherActivity.this, "没有查到该城市", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    /******************************************************************************/
//    public void getDefaultWeather(String location) {
//        Parameters para = new Parameters();
//        para.put("city", location);
//        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
//                ApiStoreSDK.GET,
//                para,
//                new ApiCallBack() {
//                    @Override
//                    public void onSuccess(int status, String responseString) {
//                        Log.i("sdkdemo", "onSuccess");
//                        //Gson解析服务器返回的json数据---responseString
//                        String cityWeather = responseString.replace("HeWeather data service 3.0", "CityWeather");
//                        System.out.print(cityWeather);
//                        gson = new Gson();
//                        weatherBean = gson.fromJson(cityWeather, WeatherBean.class);
//                        String statue = weatherBean.CityWeather.get(0).status;
//                        WeatherBean.Aqi aqi = weatherBean.CityWeather.get(0).aqi;
//                        if (statue.equalsIgnoreCase("ok") && aqi != null) {
//                            weather_location = weatherBean.CityWeather.get(0).basic.city;
//                            weather_pm = weatherBean.CityWeather.get(0).aqi.city.pm25;
//                            weather_qlty = weatherBean.CityWeather.get(0).aqi.city.qlty;
//                            max = weatherBean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
//                            min = weatherBean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
//                            tmp = weatherBean.CityWeather.get(0).now.tmp;
//                            tv_weather_location.setText(weather_location);
//                            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n" + weather_qlty);
//                            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
//                            tv_weather_temperature.setText(tmp + "°C");
//                            Log.i("sdkdemo", "onSuccess");
//                            System.out.println(weatherBean.CityWeather.get(0).daily_forecast.get(1).tmp.max + "");
//                            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
//                            //2.3之后取出顶部底部的阴影，android:overScrollMode=”never”代码setOverScrollMode(View.OVER_SCROLL_NEVER);
//                            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);
//                            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
//                            int cityid = Integer.parseInt(weatherBean.CityWeather.get(0).basic.id.substring(2));
//                            int pm = weatherBean.CityWeather.get(0).aqi.city.pm25;
//                            String location = weatherBean.CityWeather.get(0).basic.city;
//                            SharedPreferences.Editor editor = sp.edit();
//                            editor.putString("location", location);
//                            editor.commit();
//                            Log.i(tag, cityid + "");
//                            switch (pm / 40) {
//                                case 0://优蓝
//                                    //多种方法设置背景
//                                    //ll_weather_background.setBackgroundResource(R.mipmap.city_beijing);
//                                    //ll_weather_background.setBackgroundColor(Color.parseColor("#2196F3"));
//                                    //ll_weather_background.setBackground(getResources().getDrawable(R.drawable.weather_pm_a_background));
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#2196F3"));
//                                    break;
//                                case 1://良绿
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#21C3A8"));
//                                    break;
//                                case 2://轻橙
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#FF7F24"));
//                                    break;
//                                case 3://中红
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#CD5C5C"));
//                                    break;
//                                case 4://重紫
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#303F9F"));
//                                    break;
//                                case 5://严褐
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#727272"));
//                                    break;
//
//                                default://爆黑
//                                    ll_weather_background.setBackgroundColor(Color.parseColor("#212121"));
//                                    break;
//                            }
//                        } else if (statue.equalsIgnoreCase("ok") && aqi == null) {
//                            //二线城市
//                            weather_location = weatherBean.CityWeather.get(0).basic.city;
//                            max = weatherBean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
//                            min = weatherBean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
//                            tmp = weatherBean.CityWeather.get(0).now.tmp;
//                            tv_weather_location.setText(weather_location);
//                            tv_weather_pm.setText("Pm2.5:" + weather_pm + "\n null");
//                            tv_weather_maxmin.setText("Max ↑" + max + "°\n  Mix ↓" + min + "°");
//                            tv_weather_temperature.setText(tmp + "°C");
//                            Log.i("sdkdemo", "onSuccess");
//                            System.out.println(weatherBean.CityWeather.get(0).daily_forecast.get(1).tmp.max + "");
//                            WeatherForcastAdapter WeatherForcastAdapter = new WeatherForcastAdapter();
//                            //2.3之后取出顶部底部的阴影，2.3之前xml中android:fadingEdge="none"即可
//                            lv_weather_forecastweather.setOverScrollMode(View.OVER_SCROLL_NEVER);
//                            lv_weather_forecastweather.setAdapter(WeatherForcastAdapter);
//                            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
//                            int cityid = Integer.parseInt(weatherBean.CityWeather.get(0).basic.id.substring(2));
//                            String location = weatherBean.CityWeather.get(0).basic.city;
//                            SharedPreferences.Editor editor = sp.edit();
//                            editor.putString("location", location);
//                            editor.commit();
//                            Log.i(tag, cityid + "");
//                            ll_weather_background.setBackgroundColor(Color.parseColor("#BBA87E"));
//                        } else {//情况1：status是"unknown city"；情况2：三线城市没有aqi
//                            Toast.makeText(WeatherActivity.this, "没有查到该城市", Toast.LENGTH_LONG).show();
//                            NoWeaherAdapter noWeaherAdapter = new NoWeaherAdapter();
//                            lv_weather_forecastweather.setAdapter(noWeaherAdapter);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.i("sdkdemo", "onComplete");
//                    }
//
//                    @Override
//                    public void onError(int status, String responseString, Exception e) {
//                        Log.i("sdkdemo", "onError, status: " + status);
//                        Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
//                        tv_weather_location.setText("");
//                    }
//                });
//    }


    private class WeatherForcastAdapter extends BaseAdapter {
        List<WeatherBean.Daily_forecast> daily_forecasts = null;

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            int windowHeight = wm.getDefaultDisplay().getHeight();
//            if (position == 0) {
//                TextView tvHeader = new TextView(getApplicationContext());
//                tvHeader.setText("Weather Forecast ");
//                Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
//                tvHeader.setTypeface(face);
//                tvHeader.setTextColor(getResources().getColor(R.color.colorWhite));
//                tvHeader.setTextSize(25);
//                tvHeader.setBackgroundColor(00000000);
//                //int dip =DpInterconvertPxUtils.px2dip(WeatherActivity.this,2000);
//
//                tvHeader.setPadding(0, windowHeight, 0, 0);
//                tvHeader.setGravity(Gravity.CENTER);
//                 return tvHeader;
//            }
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_weatherforcast, null);
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
            if (cityWeather != null) {
                List<WeatherBean.Daily_forecast> daily_forecasts = cityWeather.daily_forecast;
                viewHolder.tv_item_weatherforcast_date.setText(daily_forecasts.get(position+1).date);
                viewHolder.tv_item_weatherforcast_min.setText("Mix ↓" + daily_forecasts.get(position+1).tmp.min + "°");
                viewHolder.tv_item_weatherforcast_max.setText("Max ↑" + daily_forecasts.get(position+1).tmp.max + "°");
                return view;
            } else {
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

    class ViewHolder {
        TextView tv_item_weatherforcast_date;
        TextView tv_item_weatherforcast_min;
        TextView tv_item_weatherforcast_max;
    }

    private class NoWeaherAdapter extends BaseAdapter {
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