package com.example.android_supervisor.http.service;


import com.example.android_supervisor.entities.DayNoticeModel;
import com.example.android_supervisor.entities.LeaveRecordModel;
import com.example.android_supervisor.entities.LeaveTypeModel;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

@UrlTranslation(value = "personalizedApi")
public interface PersonalizedService {

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("obLogManage/list")
    Observable<Response<List<DayNoticeModel>>> getDayNoticeList(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("obLogManage")
    Observable<Response<String>> saveDayNotice(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("obLogManage")
    Observable<Response<String>> submitDayNotice(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @PUT("obLogManage")
    Observable<Response<String>> putDayNotice(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("obLogManage/get")
    Observable<Object> queryById(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("android/leave/list")
    Observable<Response<List<LeaveRecordModel>>> getLeaveListData(@Body RequestBody requestBody);

    @Headers({"apiTerritory:PersonalizedService"})
    @GET("android/leave/getLeaveType")
    Observable<Response<List<LeaveTypeModel>>> getLeaveTypeList();

    @Headers({"apiTerritory:PersonalizedService"})
    @POST("android/leave")
    Observable<Response<Object>> submitLeave(@Body RequestBody requestBody);
}
