package com.simonmeng.demo.domain;

import java.util.List;

public class WeatherBean {
    //0阶---------
    public List<WeatherDetail> CityWeather;

    public class WeatherDetail{
        public Aqi aqi;
        public Basic basic;
        public List<Daily_forecast> daily_forecast;
        public List<Hourly_forecast> hourly_forecast;
        public Now now;
        public String status;
        public Suggestion suggestion;
    }



    //1阶---------
    public class Aqi{
        public City city;
    }
    public class Basic{
        public String city;
        public String cnty;
        public String id;
        public double lat;
        public double lon;
        public Update update;
    }
    public class Daily_forecast{
        public Astro astro;
        public Cond cond;
        public String date;
        public int hum;
        public double pcpn;
        public double pop;
        public int pres;
        public Tmp tmp;
        public int vis;
        public Wind wind;
    }
    public class Hourly_forecast{
        public String date;
        public int hum;
        public int pop;
        public int pres;
        public int tmp;
        public Wind wind;
    }
    public class Now{
        public Cond cond;
        public int fl;
        public int hum;
        public int pcpn;
        public int pres;
        public int tmp;
        public int vis;
        public Wind wind;
    }
    public class Suggestion{
        public Comf comf;
        public Cw cw;
        public Drsg drsg;
        public Flu flu;
        public Sport sport;
        public Trav trav;
        public Uv uv;
    }

    //2阶---------
    //属于aqi
    public class City{
        public int aqi;
        public int co;
        public int no2;
        public int o3;
        public int pm10;
        public int pm25;
        public String qlty;
        public int so2;
    }

    //属于basic
    public class Update{
        public String loc;
        public String utc;
    }

    //属于daily_forecast
    public class Astro{
        public String sr;
        public String ss;
    }
    public class Cond{
        public int code_d;
        public int code_n;
        public String txt_d;
        public String txt_n;
        public int code;
        public String txt;
    }
    public class Tmp{
        public int max;
        public int min;
    }
    public class Wind{
        public int deg;
        public String dir;
        public String sc;
        public int spd;
    }


    //属于suggestion
    public class Comf{
        public String brf;
        public String txt;
    }
    public class Cw{
        public String brf;
        public String txt;
    }
    public class Drsg{
        public String brf;
        public String txt;
    }
    public class Flu{
        public String brf;
        public String txt;
    }
    public class Sport{
        public String brf;
        public String txt;
    }
    public class Trav{
        public String brf;
        public String txt;
    }
    public class Uv{
        public String brf;
        public String txt;
    }



}
