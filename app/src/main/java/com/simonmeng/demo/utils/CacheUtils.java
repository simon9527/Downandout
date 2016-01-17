package com.simonmeng.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于存储一些配置信息，SharePreferences
 */
public class CacheUtils {
    public static final String CACHE_FILE_NAME = "simonmeng_cache";
    public static SharedPreferences mSharedPreferences;

    public static void putBoolean(Context context, String key, boolean value){
        //mSharedPreferences设置为static，更易于调用，这样只要在使用前判断是否存在，如果不存在，创建，存在，直接用。
        if(mSharedPreferences == null){
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue){
        if(mSharedPreferences == null){
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key,defValue);
    }

    public static void putString(Context context, String key, String value){
        //mSharedPreferences设置为static，更易于调用，这样只要在使用前判断是否存在，如果不存在，创建，存在，直接用。
        if(mSharedPreferences == null){
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue){
        if(mSharedPreferences == null){
            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key,defValue);
    }

}
