package com.example.android_supervisor.http.service;



import com.example.android_supervisor.entities.Account;
import com.example.android_supervisor.entities.ActualNews;
import com.example.android_supervisor.entities.ActualNewsDetail;
import com.example.android_supervisor.entities.AutuSysData;
import com.example.android_supervisor.entities.CaseConfirmRes;
import com.example.android_supervisor.entities.CheckHfRes;
import com.example.android_supervisor.entities.CheckInRes;
import com.example.android_supervisor.entities.CheckStatusRes;
import com.example.android_supervisor.entities.CheckUpRes;
import com.example.android_supervisor.entities.ManualSyncEntity;
import com.example.android_supervisor.entities.MapGrid;
import com.example.android_supervisor.entities.MyTimeRes;
import com.example.android_supervisor.entities.PhotoEntity;
import com.example.android_supervisor.entities.ServicesConfig;
import com.example.android_supervisor.entities.UserSigRes;
import com.example.android_supervisor.entities.VideoRoomPara;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.entities.WorkScheRes;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 公众服务相关接口
 *
 * 阿斌服务
 */
//@UrlTranslation(value = "api/publicsApi/")
@UrlTranslation(value = "publicsApi/")
public interface PublicService {

    @Headers({"apiTerritory:PublicService"})
    @GET("spotChks/{id}")
    Observable<Response<CheckUpRes>> getCheckUpById(@Path("id") String id);

    @Headers({"apiTerritory:PublicService"})
    @POST("spotChks")
    Observable<Response<List<CheckUpRes>>> getCheckUpList(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @PUT("spotChks/replyTask")
    Observable<Response> replyCheckUp(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @POST("accessParameters")
    Observable<Response<List<Map<String, Object>>>> getServerList(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @POST("clockLogs/punch")
    Observable<Response> checkIn(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @POST("clockLogs")
    Observable<Response<List<CheckInRes>>> getCheckInList(@Body RequestBody body);

//    @POST("settingConfigs/listConfig")
//    Observable<Response<List<GlobalConfig>>> getGlobalConfig(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @POST("noticeNews")
    Observable<Response<List<ActualNews>>> getNoticeNews(@Body RequestBody body);


    @Headers({"apiTerritory:PublicService"})
    @POST("noticeNews")
    Call<ResponseBody> getNoticeNewsData(@Body RequestBody body);

    @Headers({"apiTerritory:PublicService"})
    @GET("noticeNews/{id}")
    Observable<Response<ActualNewsDetail>> getNoticeNewsDetail(@Path("id") String id);

    @Headers({"apiTerritory:PublicService"})
    @GET("cusWorkGrids/listWorkGridsByUserId")
    Observable<Response<List<MapGrid>>> getWorkGrid(@Query("id") String id);

    @Headers({"apiTerritory:PublicService"})
    @GET("accessAuths/getByAuthCode")
    Observable<Response<List<ServicesConfig>>> obtainOauthCode(@Query("code") String id);

    @Headers({"apiTerritory:PublicService"})
    @PUT("spotRsts/msgCallBack")
    Call<ResponseBody> msgCallback(@Body RequestBody body);

    /**
     * 拍照设置
     * @param type 应用类型（0：小程序，1：app）
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @GET("settingPhotos/{type}")
    Observable<Response<PhotoEntity>> settingPhoto(@Path("type") String type);

    @Headers({"apiTerritory:PublicService"})
    @Multipart
    @POST("settingWatermarks/uploadWaterMark")
    Observable<ResponseBody> upload(@Part MultipartBody.Part filePart,
                                    @Part("type") int type );

    /**
     * 自动同步
     */
    @Headers({"apiTerritory:PublicService"})
    @POST("androidDataSync/automaticSync")
    Observable<Response<AutuSysData>> autoSync(@Body RequestBody body);

    /**
     * 手动同步
     */
    @Headers({"apiTerritory:PublicService"})
    @POST("androidDataSync/manualSync")
    Observable<Response<ManualSyncEntity>> manualSync(@Body RequestBody body);

    /**
     * 同步系统时间
     */
    @Headers({"apiTerritory:PublicService"})
    @GET("androidDataSync/getSystemTime")
    Call<MyTimeRes> getSystemTime();

    /**
     * 视频秘钥获取
     * @param body
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @POST("cloudVideo/getUserSig")
    Observable<UserSigRes> getUserSig(@Body Account body);


    /**
     * 视频 id  获取
     * @param
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @GET("cloudVideo/getSdkAppId")
    Observable<UserSigRes> getUserAppId();



    /**
     * 进入房间
     * @param body
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @POST("cloudVideoRooms/cloudVideoRoom")
    Observable<Response<ManualSyncEntity>> addCloudVideoRoom(@Body VideoRoomPara body);

    /**
     * 推出房间
     * @param body
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @DELETE("cloudVideoRooms/cloudVideoRoom")
    Observable<Response<ManualSyncEntity>> deleteCloudVideoRoom(@Body VideoRoomPara body);


    /**
     * 获取案件二次确认
     * @param value
     * @return  @Query("keys") String key,@Query("keys") String key1
     */
    @Headers({"apiTerritory:PublicService"})
    @GET("settingConfigs/getByKeys")
    Observable<Response<CaseConfirmRes>> getCaseConfirm(@Query("keys") List<String> value);


    //通过抽查任务id 获取 抽查审核结果
    @Headers({"apiTerritory:PublicService"})
    @GET("spotRsts/getAuditDetail")
    Observable<Response<CheckStatusRes>> getAuditDetail(@Query("chkId") String checkId, @Query("userId") String userId);


    //抽查审核结果查询
    @Headers({"apiTerritory:PublicService"})
    @POST("spotRsts")
    Observable<Response<List<CheckHfRes>>> getSpotRstsList(@Body RequestBody body);

    /**
     * 根据用户id与日期查询日期内数据
     * @param body
     * @return
     */
//    @POST("workPlan/selectByDay")
    @Headers({"apiTerritory:PublicService"})
    @POST("workPlan/listByDay")
    Observable<Response<List<WorkScheRes>>> getWorkScheList(@Body RequestBody body);



    /**
     * @param body
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @GET("pointPunchLog/getPointTask")
    Observable<Response<List<WorkPlanData>>> getPointTask(@Query("userId") String userId);

    /**
     * @param
     * @return
     */
    @Headers({"apiTerritory:PublicService"})
    @POST("pointPunchLog/pointPunch")
    Observable<Response<WorkPlanData.PointListBean>> pointPunch(@Body RequestBody body);




}
