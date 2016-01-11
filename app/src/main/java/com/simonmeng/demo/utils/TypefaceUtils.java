package com.simonmeng.demo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/11.
 */
public class TypefaceUtils {
    public static void setCustomTypeface(Context context,TextView view){
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(), "Walkway Bold.ttf");
        view.setTypeface(typeFace);
    }
}
