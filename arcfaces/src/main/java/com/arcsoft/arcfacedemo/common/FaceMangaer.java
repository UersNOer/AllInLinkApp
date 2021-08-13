package com.arcsoft.arcfacedemo.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arcsoft.arcfacedemo.activity.RegisterAndRecognizeActivity2;
import com.arcsoft.arcfacedemo.faceserver.CompareResult;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.model.UserFace;
import com.arcsoft.arcfacedemo.util.FaceUtils;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yj on 2019/10/16.
 */
public class FaceMangaer {
    private FaceEngine faceEngine = new FaceEngine();
    public static FaceMangaer instance;
    private Context mContext;

    private List<CompareResult> compareResultList;

    private FaceMangaer(Context context){
        this.mContext = context;
    }

    public static FaceMangaer getInstance(Context context) {
        if (instance == null) {
            instance = new FaceMangaer(context);
            FaceServer.getInstance().init(context);
        }
        return instance;
    }



    public void initfaceFeature(String loginName){
        if (mContext ==null){
            return;
        }
        FaceServer.getInstance().getFaceByteformDb(mContext,loginName);
        //没有绑定人脸返回
        //本地人脸库初始化
       // startFaceCheck();
    }

    public List<UserFace> getfaceFeature(String loginName){

        List<UserFace> userFaces = FaceUtils.getFace(mContext,loginName);
        if (userFaces!=null && userFaces.size()>0){
            return userFaces;
        }
        return  userFaces = new ArrayList<>();
    }




    public void startFaceCheck(){
        Intent intent = new Intent(mContext,RegisterAndRecognizeActivity2.class);
        mContext.startActivity(intent);
    }


    /**
     * 激活引擎
     *
     *
     */
    public  void activeEngine() {
//        if (!checkPermissions(NEEDED_PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
//            return;
//        }
//        if (view != null) {
//            view.setClickable(false);
//        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(mContext, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {

                            //Toast.makeText(context, context.getString(com.arcsoft.arcfacedemo.R.string.active_success)+" " +activeCode, Toast.LENGTH_SHORT).show();
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            //Toast.makeText(context, context.getString(com.arcsoft.arcfacedemo.R.string.already_activated)+" " +activeCode, Toast.LENGTH_SHORT).show();

                        } else {
                            //Toast.makeText(context, context.getString(com.arcsoft.arcfacedemo.R.string.active_failed)+" " +activeCode, Toast.LENGTH_SHORT).show();
                          //  ToastUtils.show(context,context.getString(com.arcsoft.arcfacedemo.R.string.active_failed, activeCode));
                        }

//                        if (view != null) {
//                            view.setClickable(true);
//                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(mContext,activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i("d", activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void checkFace(List<UserFace> userFaces) {

        if (userFaces==null && userFaces.size()>0){

        }
    }
}
