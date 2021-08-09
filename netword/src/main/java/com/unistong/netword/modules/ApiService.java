package com.unistong.netword.modules;



import com.unistong.netword.Data;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * 数据接口
 */
public interface ApiService {
    @Multipart
    @POST("dmServer/DbMetadataRestServer/uploadPicture")
    Observable<Data> uploadImg(@Part MultipartBody.Part file);


    @POST("dmServer/DbMetadataRestServer/uploadToBasePicture")
    Observable<Data> uploadImgBase(@Body Map<String, Object> headers);





}

