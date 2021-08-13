package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.example.android_supervisor.entities.CaseSourceRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * yj
 */
public class CaseSourcePresenter {


    public void getCaseSource(Context context, CaseSourceCallBack callBack){

        QueryBody queryBody = new QueryBody.Builder()
                .eq("parentCode","case_source")
                .eq("dbStatus","1")
                .create();

        BasicService basicService = ServiceGenerator.create(BasicService.class);

        Observable<Response<List<CaseSourceRes>>> observable = basicService.getCaseSource(queryBody);

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<List<CaseSourceRes>>(context) {
                    @Override
                    public void onSuccess(List<CaseSourceRes> data) {
                       if (data!=null && data.size()>0){

                           try {

                               PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                               sqliteHelper.getCaseSourcesDao().create(data);

                           } catch (Exception e) {
                               e.printStackTrace();
                           }


                           if (callBack!=null){
                               callBack.onSuccess(data);
                           }

                       }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }



    public interface CaseSourceCallBack {

        void onSuccess(List<CaseSourceRes> data);
    }
}
