package com.example.android_supervisor.Presenter;

import android.content.Context;
import io.reactivex.Observable;

import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqytService;
import com.example.android_supervisor.http.service.YqytsService;
import com.example.android_supervisor.ui.model.CaseClassTreeVO;
import com.example.android_supervisor.ui.model.YqcsDayPara;
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class YqTypePresenter {

    public void getType(Context context, CallBack callBack){

//        ArrayList<YqType> yqTypes = new ArrayList<>();
//        if (callBack!=null){
//            yqTypes.add(new YqType(0,"小作坊"));
//            yqTypes.add(new YqType(0,"小区"));
//        }
//        if(callBack!=null){
//            callBack.onSuccess(yqTypes);
//        }


        YqytService publicService = ServiceGenerator.create(YqytService.class);
        Observable<Response<List<CaseClassTreeVO>>> observable =   publicService.getYqCsType();
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<CaseClassTreeVO>>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<List<CaseClassTreeVO>>(context){

                    @Override
                    public void onSuccess(List<CaseClassTreeVO> data) {

                        if (data!=null){
                            if(callBack!=null){

                                callBack.onSuccess(data);
                            }
                        }

                    }
                });
    }



    public void getCsDayJk(Context context, YqcsDayPara para , CsBack csBack){


        YqytsService service = ServiceGenerator.create(YqytsService.class);
        Observable<Response<YqcsDayRes>> observable =  observable =  service.ListWhByTypeForYq(new JsonBody(para));

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<YqcsDayRes>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<YqcsDayRes>(context){

                    @Override
                    public void onSuccess(YqcsDayRes data) {

                        if (data!=null){
                            if(csBack!=null){
                                csBack.onSuccess(data);
                            }
                        }

                    }
                });
    }


    public interface CallBack {

        void onSuccess(List<CaseClassTreeVO> types);
    }


    public interface CsBack {

        void onSuccess(YqcsDayRes types);
    }
}
