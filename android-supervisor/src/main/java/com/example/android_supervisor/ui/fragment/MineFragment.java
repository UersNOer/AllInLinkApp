package com.example.android_supervisor.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.activity.RegisterAndRecognizeActivity2;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.bugly.beta.Beta;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.cache.DataCacheCleaner;
import com.example.android_supervisor.cache.DataCacheManager;
import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.ConstantEntity;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.ui.MainActivity;
import com.example.android_supervisor.ui.PwdModifyActivity;
import com.example.android_supervisor.ui.ServiceSettingActivity;
import com.example.android_supervisor.ui.TutorialActivity;
import com.example.android_supervisor.ui.UserInfoModifyActivity;
import com.example.android_supervisor.ui.media.MediaInfo;
import com.example.android_supervisor.ui.media.UploadServiceManager;
import com.example.android_supervisor.ui.view.MyOneLineView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.FileServerUtils;
import com.example.android_supervisor.utils.GlideEngine;
import com.example.android_supervisor.utils.ImageUtils;
import com.example.android_supervisor.utils.LocalSaveUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;
import com.example.android_supervisor.utils.PdaToServer;
import com.example.android_supervisor.utils.ToastUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MineFragment extends BaseFragment implements MyOneLineView.OnRootClickListener {
    @BindView(R.id.myItem)
    LinearLayout mMyItemView;
    @BindView(R.id.text_info)
    TextView mTvInfo;
    @BindView(R.id.text_phone)
    TextView mTvPhone;

    @BindView(R.id.head_icon)
    ImageView head_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initHeadView();
        initItemView();
        Glide.with(MineFragment.this).load(FileServerUtils.getPath(getContext(),Environments.userBase.getProfile())).into(head_icon);
        head_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PictureSelector.create(MineFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
                        .maxSelectNum(1)
                        .enableCrop(true)
                        .circleDimmedLayer(true)
                        .freeStyleCropEnabled(true)
                        .showCropFrame(false)
                        .showCropGrid(false)
                        .glideOverride(160,160)
                        .forResult(PictureConfig.CHOOSE_REQUEST);

            }
        });

    }



    @SuppressLint("SetTextI18n")
    private void initHeadView() {
        if (Environments.userBase!=null){
            mTvInfo.setText(UserSession.getUserName(getContext())
                    +"("+Environments.userBase.getDefaultRole().getName()+")");
            mTvPhone.setText(UserSession.getMobile(getContext()));
        }

    }

    private void initItemView() {
        setItemView(R.drawable.icon_guide,"????????????",ConstantEntity.GUIDE_TAG,"",true);
        setItemView(R.drawable.icon_update,"????????????", ConstantEntity.VERSION_TAG, BuildConfig.VERSION_NAME,false);
        setItemView(R.drawable.data_tongbu,"????????????", ConstantEntity.DATA_TAG,"",false);
        setItemView(R.drawable.info_setting,"????????????", ConstantEntity.DAXG_TAG,"",true);
        setItemView(R.drawable.init,"??????????????????", ConstantEntity.INIT_TAG,"",false);


    }

    private void setItemView(int iconName, String textValue, int tag,String rightValue,boolean isShowArrow) {
        MyOneLineView guideItem = new MyOneLineView(getContext())
                .initMine(iconName, textValue, rightValue, isShowArrow,tag)
                .setOnRootClickListener(this,tag);
        mMyItemView.addView(guideItem);
    }

    //????????????????????????
    private void onPwdModify() {
        Intent intent = new Intent(getContext(), PwdModifyActivity.class);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //????????????
    private void onUpgradeApp() {
        Beta.checkUpgrade();
    }

    //????????????
    private void onTutorial() {
        Intent intent = new Intent(getContext(), TutorialActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @OnClick(R.id.ll_setting_exit)
    public void onExit(View v) {
        DialogUtils.askYesNo(getContext(), "","??????????????????????????????", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getContext();
                AccessToken.getInstance(context).delete();
                UserSession.setUserId(context, "");

                AppServiceManager.stop(context);

                AppContext appContext = (AppContext) context.getApplicationContext();
                appContext.exit();

                PrimarySqliteHelper.getInstance(context).exit();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }

    private void onResumeExit() {
        DialogUtils.askYesNo(getContext(), "??????", "????????????????????????,???????????????????????????", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getContext();

                LocalSaveUtils.clean(context);
                OauthHostManager.getInstance(context).cleanConfigStatus();
                AccessToken.getInstance(context).delete();

                // TODO ?????????????????????????????????????????????????????????,?????????????????????????????????db
                //DataCacheManager.cleanDb(context);
                try {
                    DataCacheManager.doClean(getContext());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                AppServiceManager.stop(context);
                Map<Integer, Boolean> chooseList = new HashMap<>();
                chooseList.put(1, true);
                chooseList.put(2, true);
                chooseList.put(3, true);
                chooseList.put(4, true);
                clearCache(getContext(), chooseList);
                AppContext appContext = (AppContext) context.getApplicationContext();
                appContext.exit();
                System.exit(0);
            }
        });
    }

    private void clearCache(final Context context, final Map<Integer, Boolean> chooseList) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                DataCacheCleaner.clear(context, chooseList);
                long cacheSize = DataCacheCleaner.getCacheSize(context);
                emitter.onNext(cacheSize);
                emitter.onComplete();
            }
        }).compose(this.<Long>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Long>(context, ProgressText.load))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long cacheSize) throws Exception {
                        ToastUtils.show(context, "??????????????????");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // ?????????????????????
            if (requestCode == 1) {
                DialogUtils.show(getContext(), "???????????????????????????????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else if (requestCode == PictureConfig.CHOOSE_REQUEST){
                // ????????????????????????
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList ==null || selectList.size() ==0){
                    return;
                }

                UploadServiceManager uploadService = new UploadServiceManager();
                MediaInfo mediaInfo = new MediaInfo();
                mediaInfo.setOriginalPath( selectList.get(0).getPath());
                if (selectList.get(0).isCut()){
                    mediaInfo.setThumbnailPath(selectList.get(0).getCutPath());
                }else {
                    mediaInfo.setThumbnailPath(selectList.get(0).getPath());
                }
                mediaInfo.setMimeType(selectList.get(0).getMimeType());

                try {
                    String photosDir = Storage.getImageDir(getContext());
                    File scaledImageFile = ImageUtils.compress(new File(selectList.get(0).getPath()), 1024, 1024, 90, photosDir);//??????
                    mediaInfo.setThumbnailPath(scaledImageFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                List<String> list = new ArrayList<>();
//                list.add(selectList.get(0).getPath());
//
//                Luban.with(getContext()).load(list)
//                        .ignoreBy(100)// ???????????????????????????.ignoreBy(100)                                  // ??????????????????????????????
//                        .setTargetDir(selectList.get(0).getPath())                        // ?????????????????????????????????
//                        .setCompressListener(new OnCompressListener() { //????????????
//                            @Override
//                            public void onStart() {
//                                // TODO ???????????????????????????????????????????????? loading UI
//                            }
//
//                            @Override
//                            public void onSuccess(List<LocalMedia> list) {
//
//                            }
//
//
//                            @Override
//                            public void onError(Throwable e) {
//                                // TODO ????????????????????????????????????
//                            }
//                        }).launch();    //????????????
                uploadService.upload(getContext(),mediaInfo, new UploadServiceManager.Callback() {

                    String path = "";
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long progress, long length) {

                    }


                    @Override
                    public void originalUploaded(String url) {

                    }

                    @Override
                    public void thumbnailUploaded(String url) {
                        path = FileServerUtils.getPath(getContext(),url);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.d("????????????????????????2???" +path);
                                Glide.with(MineFragment.this).load(path).into(head_icon);

                                if (  getActivity() instanceof MainActivity){
                                    ((MainActivity) getActivity()).setHomeIcon(url);
                                }

//                                PdaToServer.updata(path);
                                PdaToServer.updata(url);

                            }
                        });
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ToastUtils.show(getContext(),"onError"+throwable);
                            }
                        });
                    }
                });

                // ?????? LocalMedia ?????????????????? path
                // 1.media.getPath(); ????????? path
                // 2.media.getCutPath();???????????? path???????????? media.isCut();????????? true
                // 3.media.getCompressPath();???????????? path???????????? media.isCompressed();????????? true
                // ????????????????????????????????????????????????????????????????????????????????????
            }
        }
    }

    //????????????
    private void onDataSync() {
        DataCacheManager.manualSync(getContext(), true);
    }

    @Override
    public void onRootClick(View view) {
        switch ((int)view.getTag()){
            case ConstantEntity.GUIDE_TAG://????????????
                onTutorial();
                break;
            case ConstantEntity.VERSION_TAG://????????????
                onUpgradeApp();
                break;
            case ConstantEntity.DATA_TAG://????????????
                onDataSync();
                break;
            case ConstantEntity.PSW_TAG://????????????
                onPwdModify();
                break;
            case ConstantEntity.INIT_TAG:
                onResumeExit();
                break;
            case ConstantEntity.Face_TAG://????????????
                bindFace();
                break;
            case ConstantEntity.DAXG_TAG://????????????
                startActivity(new Intent(getContext(), UserInfoModifyActivity.class));
                break;

        }
    }

    private void bindFace() {
        Intent intent = new Intent(getActivity(), RegisterAndRecognizeActivity2.class);
        if (Environments.userBase!=null && Environments.userBase.getUserSupervisorExt()!=null){
            if (Environments.userBase.getUserSupervisorExt().getLoginName()!=null){
                intent.putExtra("loginName",Environments.userBase.getUserSupervisorExt().getLoginName());
            }
            intent.putExtra("pwd",Environments.userBase.getPwd());
            intent.putExtra("isShowRegister",true);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private int mClickCount = 0;//??????????????????
    private long mClickTime = 0L;
    public static final int CLICKED_NUM = 6;//????????????

    /**
     * ??????????????? LOGO 7 ???????????????????????????????????????????????? ServiceSettingActivity
     *
     * @param v
     */
    @OnClick(R.id.head_icon)
    public void onLogoClick(View v) {
        if (mClickTime == 0) {
            mClickTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - mClickTime > 500) {
            mClickCount = 0;
        } else {
            mClickCount++;
        }
        mClickTime = System.currentTimeMillis();
        if (mClickCount == CLICKED_NUM) { //???????????????????????? Activity
            Intent intent = new Intent(getActivity(), ServiceSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//???????????????????????? Activity
            startActivity(intent);
        }
    }




}
