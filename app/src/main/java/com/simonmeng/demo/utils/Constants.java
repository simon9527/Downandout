package com.simonmeng.demo.utils;

/**
 * Created by Administrator on 2016/1/12.
 */
public class Constants {
    //屏幕宽度+高度
    public static int displayWidth;
    public static int displayHeight;
    public static final String myApiKey = "70cceff1ddfa11c873baefb4ed264850";

    //httpWeatherArg:"city=beijing"
    public static final String httpWeatherUrl = "http://apis.baidu.com/heweather/weather/free?city=";
    //httpNewsListArg:为空即可。
    public static final String httpNewsListUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";
    //httpNewsDetailArg:"channelId=xxx&page=1"
    public static final String httpNewsDetailUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=";

    public static final String httpJokeDetailUrl = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text?page=1";
}
