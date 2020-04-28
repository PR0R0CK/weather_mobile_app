package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private String city;
    private TextInputEditText etCity;
    private Button bCheckTheWeather;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.searchCity_textInputEditText_main);
        bCheckTheWeather = findViewById(R.id.checkTheWeather_button_main);

        lastSearchedCity();
        checkTheWeather();
    }

    private void lastSearchedCity() {
        sharedPreferences = getSharedPreferences("City", MODE_PRIVATE);
        sharedPreferences.edit().putString("lastSearchedCity",etCity.getText().toString()).commit();
        city = sharedPreferences.getString("lastSearchedCity","California");
    }

    private void checkTheWeather() {
        bCheckTheWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnection()) {
                    city = etCity.getText().toString();
                    Log.d("MainActivity", "Selected city: " + city);
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("CITY", city);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You don't have Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
