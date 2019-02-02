package com.zhl.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hxd_dary on 2019/1/31.
 */
public class Basic {

    @SerializedName("city") // 映射成我们需要的名称
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}
