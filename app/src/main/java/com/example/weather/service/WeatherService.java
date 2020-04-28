package com.example.weather.service;

import com.example.weather.dto.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String cityName, @Query("APPID") String app_id);
}
