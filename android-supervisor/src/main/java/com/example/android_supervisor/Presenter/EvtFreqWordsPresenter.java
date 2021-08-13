package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.example.android_supervisor.entities.WordRes;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * yj  区域code
 */
public class EvtFreqWordsPresenter {

    public final static String actInstIdFrom = "pre_accept";
    public final static String actInstIdTo = "pre_end";
    public final static String procDefKey = "pre_process";


    public void getWords(Context context, EvtFreqWordsCallBack callBack) {

        Observable.create(new ObservableOnSubscribe<List<WordRes>>() {
            @Override
            public void subscribe(ObservableEmitter< List<WordRes>> emitter) throws Exception {
                List<WordRes> wordRess = PublicSqliteHelper.getInstance(context).getWordDao().queryForAll();
                emitter.onNext(wordRess);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WordRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<WordRes> wordRess) {
                        if (wordRess != null && wordRess.size() > 0) {
                            if (callBack != null) {
                                callBack.onSuccess(wordRess);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) {
                            callBack.onError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });





/*        QueryBody queryBody = new QueryBody.Builder()
                .actInstIdFrom(actInstIdFrom)
                .actInstIdTo(actInstIdTo)
                .pageIndex(1)
                .pageSize(50)
                .procDefKey(procDefKey)
                .type("2")
                .userId(UserSession.getUserId(context))
                .create();


        QuestionService questionService = ServiceGenerator.create(QuestionService.class);

        Observable<Response<List<WordRes>>> observable = questionService.getumEvtFreqWords(queryBody);

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<WordRes>>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<List<WordRes>>(context) {
                    @Override
                    public void onSuccess(List<WordRes> data) {
                       if (data!=null && data.size()>0){


                           if (callBack!=null){
                               callBack.onSuccess(data);
                           }

                       }else {
                           onFailure();
                       }

                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        if (callBack!=null){
                            callBack.onError();
                        }
                    }
                });*/

    }


    public interface EvtFreqWordsCallBack {

        void onSuccess(List<WordRes> data);

        void onError();
    }
}
