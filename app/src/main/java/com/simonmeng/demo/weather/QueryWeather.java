package com.simonmeng.demo.weather;

import com.baidu.apistore.sdk.ApiCallBack;


public class QueryWeather extends ApiCallBack{

    public QueryWeather() {
        super();
    }

    @Override
    public void onSuccess(int i, String s) {
        super.onSuccess(i, s);
    }

    @Override
    public void onError(int i, String s, Exception e) {
        super.onError(i, s, e);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }
}
