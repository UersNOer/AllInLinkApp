package com.example.android_supervisor.http.service;


import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.ui.model.CountInfoRes;
import com.example.android_supervisor.ui.model.CsDescRes;
import com.example.android_supervisor.ui.model.EsPersonCheckCountVO;
import com.example.android_supervisor.ui.model.SjNoteRes;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author yangjie
 */
//@UrlTranslation(value = "api/basicApi/")
@UrlTranslation(value = "virusApi/")
public interface YqService {

    @Headers({"apiTerritory:YqService"})
    @POST("esEvent")
    Observable<Response<String>> esEvent(@Body RequestBody body);


    @Headers({"apiTerritory:YqService"})
    @POST("/esPersonHealthDetail/list")
    Observable<Response<List<CountInfoRes>>> esPersonHealthDetailList(@Body RequestBody body);

    @Headers({"apiTerritory:YqService"})
    @POST("esPersonHealth")
    Observable<Response<Boolean>> esPersonHealth(@Body RequestBody body);


    /**
     * 卡点每日上报
     * @param body
     * @return
     */
    @Headers({"apiTerritory:YqService"})
    @POST("esPersonHealth")
    Observable<Response<Boolean>> esPersonCheck(@Body RequestBody body);

    @Headers({"apiTerritory:YqService"})
    @POST("esPersonCheck/countInfo")
    Observable<Response<List<EsPersonCheckCountVO>>> countInfo(@Body RequestBody body);


    @Headers({"apiTerritory:YqService"})
    @POST("esEvent/list")
    Observable<Response<List<SjNoteRes>>> ysCsRoportedList(@Body RequestBody body);


    @Headers({"apiTerritory:YqService"})
    @GET("esEvent/{id}")
    Observable<Response<CsDescRes>> esEventDesc(@Path("id") String id );
}
