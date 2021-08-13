package com.example.android_supervisor.http.service;


import com.example.android_supervisor.entities.GridCodeRes;
import com.example.android_supervisor.entities.GridCodeRes1;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author yjdigital/grid/
 */
//@UrlTranslation(value = "api/gridApi/")
@UrlTranslation(value = "gridApi/")
public interface CodeService {

    @Headers({"apiTerritory:CodeService"})
    @POST("areaGridSys/getByPoint")
    Observable<Response<List<GridCodeRes>>> getByPoint(@Query("gridType") String gridType, @Body RequestBody body);


    @Headers({"apiTerritory:CodeService"})
    @POST("areaGridSys/getByPoint")  //@Query("gridType") String gridType,
    Observable<Response<List<GridCodeRes>>> getByPoint(@Body RequestBody body);

    @Headers({"apiTerritory:CodeService"})
    @POST("gridSys/getGridByCondition")
    Observable<Response<List<GridCodeRes1>>> getGridByCondition(@Body RequestBody body);


}
