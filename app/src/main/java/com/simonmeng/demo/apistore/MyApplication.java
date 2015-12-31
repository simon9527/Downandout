package com.simonmeng.demo.apistore;
import android.app.Application;
import com.baidu.apistore.sdk.ApiStoreSDK;

//请在AndroidManifest.xml中application标签下android:name中指定该类

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO 您的其他初始化流程
        ApiStoreSDK.init(this, "70cceff1ddfa11c873baefb4ed264850");
        super.onCreate();
    }
}
