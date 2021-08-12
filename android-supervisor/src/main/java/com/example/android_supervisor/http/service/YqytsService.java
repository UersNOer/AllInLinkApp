package com.example.android_supervisor.http.service;


import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.ui.model.CsDescRess;
import com.example.android_supervisor.ui.model.YqcsDayRes;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author yangjie
 */
//@UrlTranslation(value = "api/basicApi/")
@UrlTranslation(value = "gridApi/")
public interface YqytsService {



    @Headers({"apiTerritory:YqytsService"})
    @POST("whSys")
    Observable<Response<String>> whSys(@Body RequestBody body);


    /**(
     * 综合上报获取场所
     * @return
     */
    @Headers({"apiTerritory:YqytsService"})
    @POST("whSys/ListWhByTypeForYq")
    Observable<Response<YqcsDayRes>> ListWhByTypeForYq(@Body RequestBody body);


    /**(
     *
     * @return
     */
    @Headers({"apiTerritory:YqytsService"})
    @GET("whSys/getByIdForYQ")
    Observable<Response<CsDescRess>> getByIdForYQ(@Query("whId") String whId);
}
