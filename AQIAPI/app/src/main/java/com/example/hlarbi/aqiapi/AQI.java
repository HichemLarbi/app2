package com.example.hlarbi.aqiapi;

public class AQI {



    private String  breezometer_aqi;
    private String breezometer_color;
    private String breezometer_description;

    public AQI(String breezometer_aqi, String breezometer_color, String breezometer_description) {
        this.breezometer_aqi = breezometer_aqi;
        this.breezometer_color = breezometer_color;
        this.breezometer_description = breezometer_description;}


        public String getBreezometer_aqi() {
        return breezometer_aqi;
    }

    public String getBreezometer_color() {
        return breezometer_color;
    }

    public String getBreezometer_description() {
        return breezometer_description;
    }


    }

