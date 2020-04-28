package com.example.weather.dto;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private float speed;
    @SerializedName("deg")
    private float deg;
    @SerializedName("gust")
    private float gust;

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    public float getGust() {
        return gust;
    }
}
