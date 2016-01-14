package com.simonmeng.demo.utils;

import android.os.Handler;
//只是定义一个模板，先执行，再执行，最后执行，具体执行什么，子类直接覆写即可。
public abstract class AsykTaskUtils {
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            afterSubThreadTask();
        }
    };
    /**
     * 在子线程之前执行的代码
     */
    public abstract void beforeSubThreadTask();
    /**
     * 在子线程之后执行的代码
     */
    public abstract void afterSubThreadTask();
    /**
     * 在子线程中执行的代码
     */
    public abstract void doSubThreadTaskInBackground();
    /**
     * 执行
     */
    public void execute(){
        beforeSubThreadTask();
        new Thread(){
            public void run() {
                doSubThreadTaskInBackground();
                handler.sendEmptyMessage(0);
            };
        }.start();
    }
}
