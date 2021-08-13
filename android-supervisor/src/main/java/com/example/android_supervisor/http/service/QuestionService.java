package com.example.android_supervisor.http.service;


import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.entities.CensusTaskRes;
import com.example.android_supervisor.entities.EventFlowRes;
import com.example.android_supervisor.entities.EventProc;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.WordRes;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author wujie
 */
//@UrlTranslation(value = "api/questionApi/")
@UrlTranslation(value = "questionApi/")
public interface QuestionService {

    /**
     * 监督员新增案件的方法
     *
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent")
    Observable<Response<String>> addEvent(@Body RequestBody body);

    /**
     * 监督员修改案件的方法
     *
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @PUT("android/umEvent")
    Observable<Response> editEvent(@Body RequestBody body);

    /**
     * 监督员修改预受理件的方法
     *
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @PUT("android/umEvtAccept")
    Observable<Response> editTmpEvent(@Body RequestBody body);

    /**
     * android获得流程定义列表的方法
     *
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtAccept/listProcessDTOByCategory")
    Observable<Response<List<EventProc>>> getEventProc(@Query("category") String id);

    /**
     * android获得流程定义列表的方法
     *
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtAccept/listProcessDTOByCategory")
    Call<Response<List<EventProc>>> getEventProc_bg(@Query("category") String id);



    /**
     * android获得流程定义列表的方法
     *
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @GET("umEvtAccept/listProcess/{category}")
    Observable<Response<List<EventProc>>> getEventlistProcess(@Path("category") String category);

    /**
     * 监督员获得个人案件任务的方法  核查
     *
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/list")
    Observable<Response<List<EventRes>>> getHcTaskList(@Body RequestBody body);



    /**
     * 监督员获得个人预受理件任务的方法  核实
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtAccept/list")
    Observable<Response<List<EventRes>>> getHsTaskList(@Body RequestBody body);


    /**
     * 监督员获取常用语
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("umEvtFreqWords/list")
    Observable<Response<List<WordRes>>> getumEvtFreqWords(@Body RequestBody body);



    /**
     * 根据条件查询已退件数据
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/listTheProduct")
    Observable<Response<List<EventRes>>> getTjTaskList(@Body RequestBody body);


    /**
     * 统一 历史记录的已核实 已核查 已退件 接口
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/listVerifyCheckAndTheProduct")
    Observable<Response<List<EventRes>>> listVerifyCheckAndTheProduct(@Body RequestBody body);



    /**
     * 地图界面显示 核实和核查 案件信息
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/listVerifyAndCheckEvent")
    Observable<Response<List<EventRes>>> getCaseEventListInMap(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvent/getById")
    Observable<Response<EventRes>> getHcTaskById(@Query("id") String id); // 获取待办核查案件详情

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvent/getBasicInfo")
    Observable<Response<EventRes>> getHcTaskById2(@Query("id") String id); // 获取已办核查案件详情

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtAccept/{id}")
    Observable<Response<EventRes>> getHsTaskById(@Path("id") String id); // 获取待办核实案件详情

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtAccept/getBasicInfoById/{id}")
    Observable<Response<EventRes>> getHsTaskById2(@Path("id") String id); // 获取已办核实案件详情

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvent/listProcessInfoById")
    Observable<Response<List<EventFlowRes>>> getEventFlowList(@Query("id") String id);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtAccept/complete")
    Observable<Response> handleHsTask(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/complete")
    Observable<Response> handleHcTask(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtRollback/rollback")
    Observable<Response> rollback(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtCensus")
    Observable<Response<String>> addCensusEvent(@Body RequestBody body);

    /**
     * 根据id  获取普查任务 历史上报详情
     * @param id
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtCensus/{id}")
    Observable<Response<CensusEventRes>> getCensusEventById(@Path("id") String id);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtCensusTask/list")
    Observable<Response<List<CensusTaskRes>>> getCensusTaskList(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvtCensusTask/{id}")
    Observable<Response<CensusTaskRes>> getCensusTaskById(@Path("id") String id);

    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvtCensus/listHistoryReporterData")
    Observable<Response<List<CensusEventRes>>> getCensusEventList(@Body RequestBody body);

    /**
     * 监督员获得个人任务的统计
     *
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @GET("android/supervisor/countSelfInfoForSomeTime")
    Observable<Response<Map<String, Integer>>> getSelfInfo(@Query("startTime") String beginTime,
                                                           @Query("endTime") String endTime,
                                                           @Query("userId") String userId);

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/supervisor/countSelfTotalByTypeAndUserForSomeTime")
    Observable<Response<List<Map<String, Object>>>> getSelfTotal(@Query("type") int type,
                                                                 @Query("startTime") String beginTime,
                                                                 @Query("endTime") String endTime,
                                                                 @Query("userId") String userId);

    /**
     *  已上报
     * @param body
     * @return
     */
    @Headers({"apiTerritory:QuestionService"})
    @POST("android/umEvent/listEventData")
    Observable<Response<List<EventRes>>> searchEvent(@Body RequestBody body);

    @Headers({"apiTerritory:QuestionService"})
    @GET("android/umEvent/listAttachById")
    Observable<Response<List<Attach>>> getAttachList(@Query("evtId") String evtId);



}
