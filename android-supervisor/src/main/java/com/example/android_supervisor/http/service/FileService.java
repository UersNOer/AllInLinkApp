package com.example.android_supervisor.http.service;


import com.example.android_supervisor.http.common.UrlTranslation;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author wujie
 */
//@UrlTranslation(value = "api/filesApi/")
@UrlTranslation(value = "filesApi/")
public interface FileService {

    @Headers({"apiTerritory:FileService"})
    @Multipart
    @POST("files/upload")
    Observable<ResponseBody> upload(@Part("name") String name,
                                    @Part("parentId") String parentId,
                                    @Part MultipartBody.Part filePart);

    @Headers({"apiTerritory:FileService"})
    @DELETE("files")
    Observable<ResponseBody> delete(@Query("fileUrl") String url);
}
