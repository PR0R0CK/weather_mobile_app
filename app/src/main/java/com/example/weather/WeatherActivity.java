package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.dto.WeatherResponse;
import com.example.weather.service.WeatherService;
import com.squareup.picasso.Picasso;


public class WeatherActivity extends AppCompatActivity {
    private String city;
    private TextView tvCityName, tvTemp, tvPressure, tvHumidity, tvTempMin, tvTempMax, tvDescription;
    private ImageView ivImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String AppId = "749561a315b14523a8f5f1ef95e45864";
//    private static String cityQuery = "Warsaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvCityName = findViewById(R.id.city_textView_weather);
        ivImage = findViewById(R.id.image_imageView_weather);
        tvDescription = findViewById(R.id.description_textView_weather);
        tvTemp = findViewById(R.id.temp_textView_weather);
        tvPressure = findViewById(R.id.ppressure_textView_weather);
        tvHumidity = findViewById(R.id.humidity_textView_weather);
        tvTempMin = findViewById(R.id.tempMin_textView_weather);
        tvTempMax = findViewById(R.id.tempMax_textView_weather);
        swipeRefreshLayout = findViewById(R.id.swipe_swipeRefreshLayout_weather);

        city = getIntent().getStringExtra("CITY");
        getCurrentData();
        swipeToRefresh();
        refreshEveryFiveMinutes();
        isInternetConnection();

    }

    public boolean isInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void refreshEveryFiveMinutes() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentData();
                    Toast.makeText(WeatherActivity.this, "Automatically refreshed!", Toast.LENGTH_SHORT).show();
                }
            }, 300000); // 300000 ms = 5'
    }


    private void swipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }

    public void getCurrentData() {
        if (isInternetConnection()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WeatherService service = retrofit.create(WeatherService.class);
            Call<WeatherResponse> call = service.getCurrentWeatherData(city, AppId);

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(@NonNull Call<WeatherResponse> call,
                                       @NonNull Response<WeatherResponse> response) {

                    if (!response.isSuccessful()) {
                        Log.d("Weather", "Error!");
                        return;
                    }
    //                if (response.code() == 200)
                        WeatherResponse weatherResponse = response.body();
                        assert weatherResponse != null;

                        String url = "http://openweathermap.org/img/wn/" + weatherResponse.getWeather().get(0).getIcon() +".png";
                        loadImage(url);
                        tvCityName.setText(weatherResponse.getName());
                        tvTemp.setText(Math.round(weatherResponse.getMain().getTemp() - 267.43) + " °C");
                        tvDescription.setText(weatherResponse.getWeather().get(0).getDescription());
                        tvPressure.setText(weatherResponse.getMain().getPressure() + " hPa");
                        tvHumidity.setText(weatherResponse.getMain().getHumidity() + " %");
                        tvTempMin.setText(Math.round(weatherResponse.getMain().getTemp_min() - 267.43) + " °C");
                        tvTempMax.setText(Math.round(weatherResponse.getMain().getTemp_max() - 267.43) + " °C");

                    Toast.makeText(WeatherActivity.this,"Current data!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                    tvCityName.setText(t.getMessage());
                }
            });
        } else {
            Toast.makeText(WeatherActivity.this, "You don't have Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(String url) {
        Picasso.get().load(url).into(ivImage);
    }
}


