package com.zhl.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhl.coolweather.gson.Weather;
import com.zhl.coolweather.util.PermissionsUtil;
import com.zhl.coolweather.util.Utility;

import java.security.Permission;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherSre = defaultSharedPreferences.getString("weather", null);
        if (weatherSre != null){
            //之前选择过城市了，跳过城市选择
            Weather weather = Utility.handleWeatherResponse(weatherSre);
            String weatherId = weather.basic.weatherId;
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", weatherId);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsUtil.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
