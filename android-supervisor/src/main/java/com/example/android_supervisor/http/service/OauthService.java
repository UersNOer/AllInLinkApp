package com.example.android_supervisor.http.service;

import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * # 授权服务器
 * @author 问题中心 yj
 */

//@UrlTranslation(value = "api/oauthApi")
@UrlTranslation(value = "oauthApi")
public interface OauthService {

    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<ResponseBody> getToken(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("grant_type") String grantType,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("scope") String scope,
                                     @Field("is_encrypt") boolean isEncrypt,
                                     @Field("is_manager") boolean isManager,
                                     @Field("auth_type") String authType);

    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<ResponseBody> getToken(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("grant_type") String grantType,
                                      @Field("client_id") String clientId,
                                      @Field("client_secret") String clientSecret,
                                      @Field("scope") String scope,
                                      @Field("is_encrypt") boolean isEncrypt,
                                      @Field("is_manager") boolean isManager,
                                      @Field("auth_type") String authType,
                                      @Field("refresh_token") String refreshToken);




    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Call<ResponseBody> getLoginToken2(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("grant_type")String grant_type,
                                      @Field("client_id")String client_id,
                                      @Field("client_secret")String client_secret,
                                      @Field("scope") String scope,
                                      @Field("auth_type") String auth_type,
                                      @Field("is_encrypt") boolean verifyCode
    );




    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Call<ResponseBody> refreshToken(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("grant_type")String grant_type,
                                    @Field("client_id")String client_id,
                                    @Field("client_secret")String client_secret,
                                    @Field("scope") String scope,
                                    @Field("auth_type") String auth_type,
                                    @Field("is_encrypt") boolean verifyCode,
                                    @Field("refresh_token") String refresh_token
    );



//    @GET("oauth/getUserInfo")
//    Observable<Response<UserBase>> getUserInfo();


    @Headers({"apiTerritory:OauthService"})
    @POST("oauth/updateRoleInfo")
    Observable<Response> updateRoleInfo(@Body RequestBody body);

    /**
     * 或者验证码
     * @return
     */
    @Headers({"apiTerritory:OauthService"})
    @GET("oauth/getPhoneVerifyCode")
    Observable<Response<String>> getPhoneVerifyCode(@Query("phone") String phone);

    /**
     * 新token 授权
     * @param phone
     * @param grant_type
     * @param client_id
     * @param client_secret
     * @param scope
     * @param auth_type
     * @param verifyCode
     * @return
     */
    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<ResponseBody> getLoginToken(@Field("username") String username,
                                           @Field("password") String password,
                                       @Field("grant_type")String grant_type,
                                       @Field("client_id")String client_id,
                                       @Field("client_secret")String client_secret,
                                       @Field("scope") String scope,
                                       @Field("auth_type") String auth_type,
                                       @Field("is_encrypt") boolean verifyCode
                                       );



    @Headers({"apiTerritory:OauthService"})
    @FormUrlEncoded
    @POST("oauth/token")
    Call<ResponseBody> executeLoginToken(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("grant_type")String grant_type,
                                         @Field("client_id")String client_id,
                                         @Field("client_secret")String client_secret,
                                         @Field("scope") String scope,
                                         @Field("auth_type") String auth_type,
                                         @Field("is_encrypt") boolean verifyCode
    );



    /**
     * 短信验证 token 授权
     * @param phone
     * @param grant_type
     * @param client_id
     * @param client_secret
     * @param scope
     * @param auth_type
     * @param verifyCode
     * @return
     */
    @Headers({"apiTerritory:OauthService"})
    @GET("oauth/getPhoneVerifyCode")
    Observable<ResponseBody> getPhoneRegistrationToken(@Query("username") String username,
                                           @Query("password") String password,
                                           @Query("grant_type")String grant_type,
                                           @Query("client_id")String client_id,
                                           @Query("client_secret")String client_secret,
                                           @Query("scope") String scope,
                                           @Query("auth_type") String auth_type,
                                           @Query("is_encrypt") boolean verifyCode
    );

   // http://192.168.10.135:5000/oauth/token?
    // username=gmqsly
    // &password=lBTqrKS0kZixOFXeZ0HRng==
    // &grant_type=password
    // &client_id=1666367FA3BA4E319588EA08B6E27279
    // &client_secret=lBTqrKS0kZixOFXeZ0HRng==
    // &scope=app
    // &auth_type=account
    // &is_encrypt=true

//    http://192.168.10.131:5000/oauth/token?username=13187014992&grant_type=password&client_id=1666367FA3BA4E319588EA08B6E27279&client_secret=lBTqrKS0kZixOFXeZ0HRng==&scope=app&
//    // auth_type=phone&verifyCode=3365

}
