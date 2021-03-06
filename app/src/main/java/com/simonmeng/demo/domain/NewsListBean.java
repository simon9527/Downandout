package com.simonmeng.demo.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class NewsListBean implements Serializable {
    //0阶---------
    public Showapi showapi_res_body;
    public int showapi_res_code;
    public String showapi_res_error;

    public class Showapi{
        public List<ChannelList> channelList;
        public int ret_code;
        public int totalNum;
    }

    //1阶---------
    public class ChannelList implements Serializable{
        public String channelId;
        public String name;
    }
}
