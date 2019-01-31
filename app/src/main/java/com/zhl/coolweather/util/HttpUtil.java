package com.zhl.coolweather.util;

import com.google.gson.Gson;
import com.zhl.coolweather.db.City;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hxd_dary on 2019/1/31.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
