package com.example.android_supervisor.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.entities.UpdatePdaPara;
import com.example.android_supervisor.entities.UpdatePdaRes;
import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.service.BasicService;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2019/9/18.
 */
public class PdaToServer {


//      "pdaId": "fb34e56cab976181",
//      "pdaBrand": "HONOR",
//       "pdaType": "BKK-AL10",
//        "pdaVersion": "V2.1.13_191023_test",

    private static Context mContext;

    public static void writePda(Context context){
        mContext = context;
        //获取监督员信息
//        BasicService basicService = ServiceGenerator.create(BasicService.class);
//        Call<SupervisorInfo> call =  basicService.getByUserId(UserSession.getUserId(context));

        if (Environments.userBase == null){
            ToastUtils.show(mContext,"请先初始化Environments.userBase");
            return;
        }

        if (Environments.userBase!=null && Environments.userBase.getUserSupervisorExt()!=null){

            sendPdaData();
        }
    }

    private static void sendPdaData(){

        updata("");
    }



    public static void updata(String imageUrl){

        String brand = Build.BRAND;//品牌
        String medel = Build.MODEL;// 型号
        String versionName = BuildConfig.VERSION_NAME;//
        String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);//终端标识

        UpdatePdaPara pdaPara  = new UpdatePdaPara();
        pdaPara.setDefaultDeptId(Environments.userBase.getDefaultDepartment().getId());
        pdaPara.setId(Environments.userBase.getId());

        UserBase.UserSupervisorExtBean sourceBean  =Environments.userBase.getUserSupervisorExt();

        UpdatePdaPara.UserSupervisorExtBean userSupervisorExt = new UpdatePdaPara.UserSupervisorExtBean();

        if (sourceBean!=null&&sourceBean.getLoginName()!=null){
            pdaPara.setLoginName(sourceBean.getLoginName());
            pdaPara.setUserName(sourceBean.getUserName());
        }
        pdaPara.setUserMobile(Environments.userBase.getPhone());

        if (!TextUtils.isEmpty(imageUrl)){
            pdaPara.imageUrl = imageUrl;
        }

        if (sourceBean!=null){
            userSupervisorExt.setPdaBrand((TextUtils.isEmpty(sourceBean.getPdaBrand())?brand:""));
            userSupervisorExt.setPdaId((TextUtils.isEmpty(sourceBean.getPdaId())?androidId:""));
            userSupervisorExt.setPdaType((TextUtils.isEmpty(sourceBean.getPdaType())?medel:""));
            userSupervisorExt.setId(sourceBean.getId());//监督员id
        }
        userSupervisorExt.setPdaVersion(versionName);
        //用户id
        userSupervisorExt.setUserId(Environments.userBase.getId());

        pdaPara.setUserSupervisorExt(userSupervisorExt);


        BasicService basicService = ServiceGenerator.create(BasicService.class);

        Call<UpdatePdaRes> call =  basicService.userSupervisorEx(pdaPara);
        call.enqueue(new Callback<UpdatePdaRes>() {
            @Override
            public void onResponse(Call<UpdatePdaRes> call, retrofit2.Response<UpdatePdaRes> response) {
                if (response!=null && response.isSuccessful()) {
                    UpdatePdaRes updatePdaRes = response.body();
                    if (updatePdaRes!=null){
                        LogUtils.d(updatePdaRes.getMessage());
                        if (updatePdaRes.getCode() == 200){
                            //UserSession.setWritePdaStatus(mContext,true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePdaRes> call, Throwable t) {

            }
        });
    }



}
