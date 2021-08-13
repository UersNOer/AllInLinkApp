package com.example.android_supervisor.http.service;


import com.example.android_supervisor.entities.Area;
import com.example.android_supervisor.entities.CaseSourceRes;
import com.example.android_supervisor.entities.ConfigEntity;
import com.example.android_supervisor.entities.Contact;
import com.example.android_supervisor.entities.DeptRes;
import com.example.android_supervisor.entities.Dictionary;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.LayerConfig;
import com.example.android_supervisor.entities.SupervisorInfo;
import com.example.android_supervisor.entities.UpdatePdaPara;
import com.example.android_supervisor.entities.UpdatePdaRes;
import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author yangjie
 */
//@UrlTranslation(value = "api/basicApi/")
@UrlTranslation(value = "basicApi/")
public interface BasicService {

    @Headers({"apiTerritory:BasicService"})
    @POST("ignoreAuth/register")
    Observable<Response> register(@Body RequestBody body);

    @Headers({"apiTerritory:BasicService"})
    @PUT("user/changePwd")
    Observable<Response> changePwd(@Query("id") String uid, @Query("oldPwd") String oldPwd, @Query("newPwd") String newPwd);

    @Headers({"apiTerritory:BasicService"})
    @POST("caseClass/list")
    Observable<Response<List<EventType>>> getEventTypeList(@Body RequestBody body);

    @Headers({"apiTerritory:BasicService"})
    @POST("caseClassStandard/list")
    Observable<Response<List<EventType>>> getEventStandardList(@Body RequestBody body);

//    @POST("department/list")
//    Observable<Response<List<Dictionary>>> getDeptList(@Body RequestBody body);

    @Headers({"apiTerritory:BasicService"})
    @POST("area/list")
//    @POST("areaGridSys/list")
    Observable<Response<List<Area>>> getAreaList(@Body RequestBody body);

    @Headers({"apiTerritory:BasicService"})
    @POST("dictionary/list")
    Observable<Response<List<Dictionary>>> getDictionaryList(@Body RequestBody body);

//    @POST("directory/list")
//    Observable<Response<List<Contact>>> getContactList(@Body RequestBody body);
//@POST("userSupervisorExt/list")


    /*
    *获取通讯录Observable<Response<List<Contact>>>
     */
    @Headers({"apiTerritory:BasicService"})
    @POST("directory/listByRoleGroup")
    Call<Response<List<Contact>>> getContactList1();

    /*
   *获取通讯录Observable<Response<List<Contact>>>
    */
    @Headers({"apiTerritory:BasicService"})
    @POST("directory/listByRoleGroup")
    Observable<Response<List<Contact>>> getContactList();

    @POST("settingAccess/list")
    @Headers({"apiTerritory:BasicService"})
    Observable<Response<List<Map<String, Object>>>> getServerList(@Body RequestBody body);

    @GET("ignoreAuth/getCollectionTree")
    @Headers({"apiTerritory:BasicService"})
    Observable<Response<List<DeptRes>>> getDeptList2();

    /**
     * 获取fileServer
     * @return
     */
    @POST("config/getConfig")
    @Headers({"apiTerritory:BasicService"})
    Observable<Response<ConfigEntity>> getConfig();

    /**
     * 根据条件获得图层实体列表
     * @return
     */
    @POST("layerConfig/list")
    @Headers({"apiTerritory:BasicService"})
    Observable<Response<List<LayerConfig>>> getLayerConfig(@Body RequestBody body);

    @POST("user/bindCurrentUser")
    @Headers({"apiTerritory:BasicService"})
    Call<ResponseBody> bindCurrentUser1();



    @GET("userSupervisorExt/getByUserId")
    @Headers({"apiTerritory:BasicService"})
    Call<SupervisorInfo> getByUserId(@Query("userId") String id);

    @PUT("userSupervisorExt")
    @Headers({"apiTerritory:BasicService"})
    Call<UpdatePdaRes> userSupervisorEx(@Body UpdatePdaPara body);


//    @GET("oauth/getUserInfo")
//    Observable<Response<UserBase>> getUserInfo();

    @POST("user/bindCurrentUser")
    @Headers({"apiTerritory:BasicService"})
    Observable<ResponseBody> bindCurrentUser();

    @POST("unicornUserLogin/bindCurrentUser")
    @Headers({"apiTerritory:BasicService"})
    Observable<ResponseBody> bindCurrentUserNew();


    @GET("user/info")
    @Headers({"apiTerritory:BasicService"})
    Observable<Response<UserBase>> getUserInfo();


    @Headers({"apiTerritory:BasicService"})
    @POST("dictionary/list")
    Observable<Response<List<CaseSourceRes>>> getCaseSource(@Body RequestBody body);

    @Headers({"apiTerritory:BasicService"})
    @POST("userUnicorn/getUnicronToken")
    Observable<ResponseBody> getLoginTokenNew(@Body RequestBody requestBody
    );






    //https://tykj.cszhx.top:30220/basicApi/digital/basic/userArbitrator/1197485561854562305

   // https://tykj.cszhx.top:30220/basicApi/digital/basic/userArbitrator


}
