package com.example.android_supervisor.Presenter;

import android.content.Context;


import com.example.android_supervisor.entities.CaseConfirmRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.utils.Environments;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CaseConfirmPresenter {

    public static String CASE_SECONDARY_REPORT_VALID = "case_secondary_report_valid";//是否开启案件二次上报
    public static String CASE_SECONDARY_REPORT_URL = "case_secondary_report_url";//案件二次上报的地址

    public void getCaseConfirm(final Context context, List<String> arrayList, CaseConfirmCallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        Observable<Response<CaseConfirmRes>> observable = publicService.getCaseConfirm(arrayList);

        observable
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Response<EventRes>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<CaseConfirmRes>(context) {
                    @Override
                    public void onSuccess(CaseConfirmRes data) {
                       if (data!=null){
                           List<CaseConfirmRes.CaseSecondaryReportBean> caseSecondaryReportBeans =  data.getCase_secondary_report_valid();
                           if (caseSecondaryReportBeans!=null && caseSecondaryReportBeans.size()>0){
                               Environments.isCaseCheck = caseSecondaryReportBeans.get(0).getConfigValue().equals("1")?true:false;
                           }
                           List<CaseConfirmRes.CaseSecondaryReportBean> caseSecondaryReportBeans1 =  data.getCase_secondary_report_url();
                           if (caseSecondaryReportBeans1!=null && caseSecondaryReportBeans1.size()>0){
                               if (Environments.isCaseCheck){
                                   Environments.caseCheckUrl = caseSecondaryReportBeans1.get(0).getConfigValue();
                               }
                           }

                           if (callBack!=null){
                               callBack.onSuccess();
                           }

                       }
                    }
                });

    }


    public interface CaseConfirmCallBack {

        void onSuccess();
    }
}
