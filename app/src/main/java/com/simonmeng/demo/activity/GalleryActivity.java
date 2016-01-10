package com.simonmeng.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.simonmeng.demo.R;
import com.simonmeng.demo.apistore.GetNewsData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private String tag = "GalleryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TextView tv_gallery = (TextView) findViewById(R.id.tv_gallery);
        tv_gallery.setText(getNews());
        String a = (String) tv_gallery.getText();
        System.out.println(a);
        Log.i(tag,a);

        wr(a);

    }
    public String getNews(){
        String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
        String httpArg = "channelId=5572a109b3cdc86cf39001db&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1";
        String jsonResult = GetNewsData.request(httpUrl, httpArg);
        return jsonResult;
    }
    public void wr(String string){

        FileWriter fw = null;
        File f = new File(getCacheDir(),"info.txt");

        try {
            if(!f.exists()){
                f.createNewFile();
            }
            fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(string, 0, string.length()-1);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
