package com.example.android_supervisor.common.initializer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.upgrade.Constants;
import com.example.android_supervisor.ui.upgrade.UpgradeDialogActivity;
import com.example.android_supervisor.ui.upgrade.UpgradePecket;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * @author wujie
 */
public class BuglyInitializer implements Initializer {

    public void initialize(Context context) {
        Beta.autoCheckUpgrade = false;
        Beta.upgradeCheckPeriod = 1 * 60 * 1000;

        Beta.enableHotfix = false;
        Beta.largeIconId = R.drawable.app_icon;
        Beta.smallIconId = R.drawable.app_icon_mask;
        Beta.defaultBannerId = R.drawable.app_icon;
        // 自定义存储路径会导致某些机型无法下载文件，所以暂时注释
//        Beta.storageDir = new File(Storage.getDownloadsDir(context));
//        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.upgradeListener = new UpgradeListenerImpl(context);
        initBugly(context);

    }

    private void initBugly(Context context) {
        try {

            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            String appId = appInfo.metaData.getString("com.tencent.bugly.API_ID");
            Bugly.init(context, appId, BuildConfig.DEBUG);
            CrashReport.initCrashReport(context, appId, BuildConfig.DEBUG);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class UpgradeListenerImpl implements UpgradeListener {
        private Context context;

        public UpgradeListenerImpl(Context context) {
            this.context = context;
        }

        @Override
        public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
            if (strategy == null) {
//                    Toast.makeText(context, "无法获取安装包信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isManual) {
                if (strategy.upgradeType != 2) {
                    return;
                }
            }
            Intent intent = new Intent(context, UpgradeDialogActivity.class);
            intent.putExtra(Constants.PECKET, new UpgradePecket(strategy));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
