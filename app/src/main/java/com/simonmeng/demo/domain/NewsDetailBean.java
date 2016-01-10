package com.simonmeng.demo.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class NewsDetailBean {
    //0阶---------
    public Showapi showapi_res_body;
    public int showapi_res_code;
    public String showapi_res_error;

    public class Showapi{
        public Pagebean pagebean;
        public int ret_code;
    }

    //1阶---------
    public class Pagebean{
        public double allNum;
        public int allPages;
        public List<Contentlist> contentlist;
        public int currentPage;
        public int maxResult;
    }

    //2阶---------
    public class Contentlist{
        public String channelId;
        public String channelName;
        public Comment comment;
        public String desc;
        public List<Imageurls> imageurls;
        public String link;
        public String nid;
        public String pubDate;
        public String source;
        public String title;
    }

    //3阶---------
    public class Imageurls{
        public int height;
        public int width;
        public String url;
    }

    public class Comment{
        public int count;
    }
}
