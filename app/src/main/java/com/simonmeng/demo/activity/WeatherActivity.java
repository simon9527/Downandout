package com.simonmeng.demo.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.simonmeng.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void quaryWeather(){
        Parameters para = new Parameters();
        para.put("city", "沈阳");
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("sdkdemo", "onSuccess");
                        //Gson解析服务器返回的json数据---responseString

                       // JSONObject jsonObject = new JSONObject(responseString);
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            tv_weather_location.setText("123");
                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
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
}
