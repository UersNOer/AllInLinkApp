package com.example.android_supervisor.http.service;


import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.ui.model.CaseClassTreeVO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * @author yangjie
 */
//@UrlTranslation(value = "api/basicApi/")
@UrlTranslation(value = "basicApi/")
public interface YqytService {

    @Headers({"apiTerritory:YqytService"})
    @GET("caseClass/getWhTreeForYQ")
    Observable<Response<List<CaseClassTreeVO>>> getYqCsType();







//    @Headers({"apiTerritory:YqytService"})
//    @POST("whSys")
//    Observable<Response<List<EsEventVO>>> whSys(@Body RequestBody body);
    //https://tykj.cszhx.top:30220/basicApi/digital/basic/userArbitrator/1197485561854562305

   // https://tykj.cszhx.top:30220/basicApi/digital/basic/userArbitrator


}
