package com.example.android_supervisor.http.service;

import android.database.Observable;

import com.example.android_supervisor.entities.ServicesConfig;
import com.example.android_supervisor.http.response.Response;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 云中心网络配置接口
 * 1 配置授权码
 * @author yj
 */
public interface CloudCenterService {

    @Headers({"apiTerritory:CloudCenterService"})
    @GET("accessAuths/getByAuthCode")
    Observable<Response<List<ServicesConfig>>> obtainOauthCode(@Query("code") String id);
}
