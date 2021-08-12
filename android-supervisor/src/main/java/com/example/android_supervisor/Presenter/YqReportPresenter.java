package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.http.service.YqytsService;
import com.example.android_supervisor.ui.CaseSourceConstant;
import com.example.android_supervisor.ui.model.EsEventVO;
import com.example.android_supervisor.ui.view.ProgressText;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 *
 */
public class YqReportPresenter {

    public void report(final Context context, EsEventVO esEventVO , CallBack callBack) {



        Observable<Response<String>> observable;

        if (esEventVO.getSource() == CaseSourceConstant.YQSJ){
            YqService service = ServiceGenerator.create(YqService.class);
            observable =  service.esEvent(new JsonBody(esEventVO));
        }else {
            YqytsService service = ServiceGenerator.create(YqytsService.class);
            observable =  service.whSys(new JsonBody(esEventVO));
        }

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(context, ProgressText.submit))
                .subscribe(new ResponseObserver<String>(context){

                    @Override
                    public void onSuccess(String data) {

                        if (data!=null){
                            if(callBack!=null){

                                callBack.onSuccess();
                            }
                        }

                    }
                });

//        String url = "http://192.168.20.42:8080/esEvent";
//
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(new JsonBody(esEventVO))
//                .build();
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("", "onFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                if (response.code() == 200 && response.body() != null) {
//                    String result = response.body().string();
//                    Log.d("数据", response.protocol() + " " + response.code() + " " + response.message() + "  " + result);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        if (jsonObject != null) {
//                            String data = jsonObject.getString("data");
//
////                            if (!TextUtils.isEmpty(data)) {
////                                List<GreenbeltRes> greenGrids =new Gson().fromJson(data, new TypeToken<List<GreenbeltRes>>() {}.getType());
////                                Environments.greenGrids =greenGrids;
////                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

    }


    public interface CallBack {

        void onSuccess();

        void onFailure();
    }


//     "id": "1192712416546701314",
//             "createId": "1176680734874714113",
//             "createTime": "2019-11-08 15:56:14",
//             "updateId": null,
//             "updateTime": null,
//             "pointId": "1191601433421938690",
//             "workPlanId": "1192685281090142209",
//             "workGridCode": "440309001007A0",
//             "punchStatus": "1",
//             "punchX": "28.233002",
//             "punchY": "112.86957",
//             "punchTime": "2019-11-08 15:54:38",
//             "userId": "1176680734874714113"
}
