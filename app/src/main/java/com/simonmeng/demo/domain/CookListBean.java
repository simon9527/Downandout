package com.simonmeng.demo.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class CookListBean {
    public String status;
    public List<Tngou> tngou;
    public double total;

    public class Tngou{
        public int count;
        public String description;
        public int fcount;
        public String food;
        public int id;
        public String images;
        public String img;
        public String keywords;
        public String name;
        public int rcount;
    }
}
