package com.simonmeng.demo.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.google.gson.Gson;
import com.simonmeng.demo.R;
import com.simonmeng.demo.domain.WeatherBean;

public class WeatherActivity extends AppCompatActivity {
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

        //设置字体，typeface字体。
        Typeface face = Typeface.createFromAsset(getAssets(), "deftone stylus.ttf");
        tv_weather_pm.setTypeface(face);
        tv_weather_maxmin.setTypeface(face);
        tv_weather_temperature.setTypeface(face);
        //bt_splash_start.setTransformationMethod(null);

        quaryWeather();


    }

    ;

    public void quaryWeather() {
        Parameters para = new Parameters();
        para.put("city", "北京");
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("sdkdemo", "onSuccess");
                        //Gson解析服务器返回的json数据---responseString
                        String cityWeather = responseString.replace("HeWeather data service 3.0","CityWeather");
                        System.out.print(cityWeather);
                        Gson gson = new Gson();
                        WeatherBean bean = gson.fromJson(cityWeather, WeatherBean.class);
                        String weather_location = bean.CityWeather.get(0).basic.city;
                        int weather_pm = bean.CityWeather.get(0).aqi.city.pm25;
                        String weather_qlty = bean.CityWeather.get(0).aqi.city.qlty;
                        int max = bean.CityWeather.get(0).daily_forecast.get(0).tmp.max;
                        int min = bean.CityWeather.get(0).daily_forecast.get(0).tmp.min;
                        int tmp = bean.CityWeather.get(0).now.tmp;
                        tv_weather_location.setText(weather_location);
                        tv_weather_pm.setText("Pm2.5:"+weather_pm+"\n"+weather_qlty);
                        tv_weather_maxmin.setText("Max ↑"+max+"°\n  Mix ↓"+min+"°");
                        tv_weather_temperature.setText(tmp+"°C");





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

}