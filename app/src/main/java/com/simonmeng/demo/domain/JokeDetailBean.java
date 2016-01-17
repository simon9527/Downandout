package com.simonmeng.demo.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class JokeDetailBean {
    //0阶---------
    public ShowApi showapi_res_body;
    public int showapi_res_code;
    public String showapi_res_error;

    public class ShowApi{
        public int allNum;
        public int allPages;
        public List<Contentlist> contentlist;
        public int currentPage;
        public int maxResult;
    }

    //1阶---------
    public class Contentlist{
        public String ct;
        public String text;
        public String title;
        public int type;

    }
}
