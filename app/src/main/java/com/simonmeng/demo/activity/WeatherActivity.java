package com.simonmeng.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.WeatherBean;

public class WeatherActivity extends AppCompatActivity {
    private String tag = "WeatherActivity";
    private SharedPreferences sp;
    private String weather_location;
    private int weather_pm;
    private String weather_qlty;
    private int max;
    private int min;
    private int tmp;

    LinearLayout ll_weather_background;
    TextView tv_weather_location;
    TextView tv_weather_temperature;
    TextView tv_weather_pm;
    TextView tv_weather_maxmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tv_weather_location = (TextView) findViewById(R.id.tv_weather_location);
        tv_weather_temperature = (TextView) findViewById(R.id.tv_weather_temperature);
        tv_weather_pm = (TextView) findViewById(R.id.tv_weather_pm);
        tv_weather_maxmin = (TextView) findViewById(R.id.tv_weather_maxmin);
        ll_weather_background = (LinearLayout) findViewById(R.id.ll_weather_background);

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
                        Gson gson = new Gson();
                        WeatherBean bean = gson.fromJson(cityWeather, WeatherBean.class);
                        String statue = bean.CityWeather.get(0).status;
                        if (statue.equalsIgnoreCase("ok")) {
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
                            //获取城市的id，去掉前两位的字母，剩下的都是数字，可以通过switch判断，来设置背景
                            int cityid = Integer.parseInt(bean.CityWeather.get(0).basic.id.substring(2));
                            String location = bean.CityWeather.get(0).basic.city;
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("location", location);
                            editor.commit();
                            Log.i(tag, cityid + "");
                            switch (cityid) {
                                case 101010100://北京
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_beijing);
                                    break;
                                case 101070101://沈阳
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_shenyang);
                                    break;
                                case 101060101://长春
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_changchun);
                                    break;
                                case 101050101://哈尔滨
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_haerbin);
                                    break;
                                case 101020100://上海
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_shanghai);
                                    break;
                                default://默认
                                    ll_weather_background.setBackgroundResource(R.mipmap.city_default);
                                    break;
                            }
                        } else if (statue.equalsIgnoreCase("unknown city")) {
                            Toast.makeText(WeatherActivity.this,"没有查到该城市",Toast.LENGTH_LONG).show();
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
}