package com.example.hlarbi.aqiapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://api.breezometer.com/baqi/";
@GET("?lat=40.7324296&lon=-73.9977264&key=fb2a9afbd6a24f948170ddf84cbaca97")
    Call<AQI> getAqui();
}
