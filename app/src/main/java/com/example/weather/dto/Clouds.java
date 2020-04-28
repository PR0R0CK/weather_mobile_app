package com.example.weather.dto;

import com.google.gson.annotations.SerializedName;

public class Clouds {
    @SerializedName("all")
    private float all;

    public float getAll() {
        return all;
    }
}
