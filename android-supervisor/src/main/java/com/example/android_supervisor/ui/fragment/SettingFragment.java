package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.cache.DataCacheCleaner;
import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.ui.LoginActivity;
import com.example.android_supervisor.ui.PwdModifyActivity;
import com.example.android_supervisor.ui.TutorialActivity;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.OauthHostManager;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.HashMap;
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

/**
 * @author wujie
 */
public class SettingFragment extends BaseFragment {
    @BindView(R.id.tv_setting_data_size)
    TextView tvDataSize;

    @BindView(R.id.tv_setting_version)
    TextView tvVersion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tvVersion.setText(BuildConfig.VERSION_NAME);
        getCacheSize(getContext());
    }

    @OnClick(R.id.ll_setting_data_sync)
    public void onDataSync(View v) {
        //DataCacheManager.update(getContext());
    }

    @OnClick(R.id.ll_setting_data_clean)
    public void onDataClean(View v) {
        final CharSequence[] items = {
                "公共数据",
                "用户数据",
                "媒体文件",
                "下载目录",
        };

        final boolean[] choices = {
                true, false, false, false
        };

        int flag = 1;
        flag |= 1 << 1;
        flag |= 1 << 2;
        Map<Integer, Boolean> chooseList = new HashMap<>();
        chooseList.put(1, true);
        chooseList.put(2, false);
        chooseList.put(3, false);
        chooseList.put(4, false);

        final OnMultiChoiceClickListener listener = new OnMultiChoiceClickListener(chooseList);

        new AlertDialog.Builder(getContext())
                .setTitle("清理缓存")
                .setCancelable(false)
                .setMultiChoiceItems(items, choices, listener)
                .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<Integer, Boolean> chooseList = listener.getChooseList();
                        clearCache(getContext(), chooseList);
                    }
                })
                .setNegativeButton("取消", null).show();
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
                        String dataSize = Formatter.formatFileSize(context, cacheSize);
                        tvDataSize.setText(dataSize);
                        ToastUtils.show(context, "清理缓存完毕");
                        if (chooseList.get(2)) {
                            toLogin();
                        }
                    }
                });
    }

    private void getCacheSize(final Context context) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                long cacheSize = DataCacheCleaner.getCacheSize(context);
                emitter.onNext(cacheSize);
                emitter.onComplete();
            }
        }).compose(this.<Long>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long cacheSize) throws Exception {
                        String dataSize = Formatter.formatFileSize(context, cacheSize);
                        tvDataSize.setText(dataSize);
                    }
                });
    }

    @OnClick(R.id.ll_setting_modify_pwd)
    public void onPwdModify(View v) {
        Intent intent = new Intent(getContext(), PwdModifyActivity.class);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.ll_setting_upgrade_app)
    public void onUpgradeApp(View v) {
        Beta.checkUpgrade();
    }

    @OnClick(R.id.ll_setting_tutorial)
    public void onTutorial(View v) {
        Intent intent = new Intent(getContext(), TutorialActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.ll_setting_switch)
    public void onSwitch(View v) {
        DialogUtils.askYesNo(getContext(), "提示", "确定切换监督员账号？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toLogin();
            }
        });
    }

    private void toLogin() {
        Context context = getContext();
        AccessToken.getInstance(context).delete();
        UserSession.clear(context);
        AppServiceManager.stop(context);


        AppContext appContext = (AppContext) context.getApplicationContext();
        appContext.exit();

        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }

    @OnClick(R.id.ll_setting_exit)
    public void onExit(View v) {
        DialogUtils.show(getContext(), "确定退出监督员应用？", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getContext();
                AccessToken.getInstance(context).delete();
                UserSession.setUserId(context, "");

                AppServiceManager.stop(context);


                AppContext appContext = (AppContext) context.getApplicationContext();
                appContext.exit();
            }
        });
    }


    @OnClick(R.id.ll_setting_resume_exit)
    public void onResumeExit(View v) {
        DialogUtils.askYesNo(getContext(), "提示", "确定恢复初始设置,并退出监督员应用？", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getContext();
                OauthHostManager.getInstance(context).cleanConfigStatus();//清理授权返回
                AccessToken.getInstance(context).delete();
                UserSession.clear(context);

                AppServiceManager.stop(context);

                Map<Integer, Boolean> chooseList = new HashMap<>();
                chooseList.put(1, true);
                chooseList.put(2, true);
                chooseList.put(3, true);
                chooseList.put(4, true);
                clearCache(getContext(), chooseList);
                AppContext appContext = (AppContext) context.getApplicationContext();
                appContext.exit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // 修改密码成功后
            if (requestCode == 1) {
                DialogUtils.show(getContext(), "密码已经修改，需要重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        toLogin();
                    }
                });
            }
        }
    }

    private void resetUser() {
        Context context = getContext();
        UserSession.clear(context);
    }

    static class OnMultiChoiceClickListener implements DialogInterface.OnMultiChoiceClickListener {
        private int flag;
        private Map<Integer, Boolean> chooseList;

        public OnMultiChoiceClickListener(Map<Integer, Boolean> chooseList) {
            this.chooseList = chooseList;
        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Log.d("dawn", "onClick: " + which + isChecked);
            switch (which) {
                case 0:
                    if (isChecked) {
                        chooseList.put(1, true);
                        flag |= 1 << 1;
                    } else {
                        chooseList.put(1, false);
                        flag &= ~(1 << 1);
                    }
                    break;
                case 1:
                    if (isChecked) {
                        chooseList.put(2, true);
                        flag |= 1 << 2;
                    } else {
                        flag &= ~(1 << 2);
                        chooseList.put(2, false);
                    }
                    break;
                case 2:
                    if (isChecked) {
                        chooseList.put(3, true);
                        flag |= 1 << 3;
                    } else {
                        chooseList.put(3, false);
                        flag &= ~(1 << 3);
                    }
                    break;
                case 3:
                    if (isChecked) {
                        chooseList.put(4, true);
                        flag |= 1 << 4;
                    } else {
                        chooseList.put(4, false);
                        flag &= ~(1 << 4);
                    }
                    break;
            }
        }

        public Map<Integer, Boolean> getChooseList() {
            return chooseList;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getActivity().registerReceiver(mUpgradeReceiver,
//                new IntentFilter(Constants.UPGRADE_RECEIVER));
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        getActivity().unregisterReceiver(mUpgradeReceiver);
//    }
//
//    private BroadcastReceiver mUpgradeReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            UpgradePecket pecket = intent.getParcelableExtra(Constants.PECKET);
//            if (pecket != null) {
//                UpgradeDialog.show(getChildFragmentManager(), pecket);
//            }
//        }
//    };
}
