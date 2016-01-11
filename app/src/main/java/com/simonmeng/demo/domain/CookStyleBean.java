package com.simonmeng.demo.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class CookStyleBean {

    public List<CookStyle> CookStyle;


    public class CookStyle{
        public String status;
        public List<Tngou> tngou;
    }
    public class Tngou{
        public int cookclass;
        public String description;
        public String id;
        public String keywords;
        public String name;
        public int seq;
        public String title;
    }
}


