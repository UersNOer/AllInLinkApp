package com.example.android_supervisor.http.service;


import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.entities.UserInfo;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wujie
 */
@UrlTranslation(value = "/platformApi/")
public interface PlatformService {

    @Headers({"apiTerritory:PlatformService"})
    @GET("oauth/getUserInfo")
    Observable<Response<UserBase>> getUserInfo();

    @Headers({"apiTerritory:PlatformService"})
    @GET("user/{id}")
    Observable<Response<UserInfo>> getUserInfo(@Path("id") String uid);

    @Headers({"apiTerritory:PlatformService"})
    @POST("user/updateRoleInfo")
    Observable<Response> updateRoleInfo(@Body RequestBody body);
}
