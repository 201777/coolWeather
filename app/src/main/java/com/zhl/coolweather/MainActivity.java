package com.zhl.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhl.coolweather.util.PermissionsUtil;

import java.security.Permission;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
