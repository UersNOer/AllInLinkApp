package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android_supervisor.R;
import com.example.android_supervisor.cache.DataCacheManager;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.ui.view.MyOneLineView;
import com.example.android_supervisor.utils.DevLocalHostManager;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;

import java.sql.SQLException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wujie
 */
public class ServiceSettingActivity extends BaseActivity {
    @BindView(R.id.ll_config)
    LinearLayout llConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (Environments.isDeBug()) {
            try {
                Map<String, String> configs = DevLocalHostManager.getHashMapData(this, DevLocalHostManager.KEY_DEV_LOCALHOST_SERVER_CONFIG);
                LogUtils.e("ServiceSettingActivity", configs.toString());
                setItemView(R.drawable.app_icon, "当前处于 Debug 模式");
                for (Map.Entry entry : configs.entrySet()) {
                    setItemView(R.drawable.ic_msg, entry.getKey() + " ：" + entry.getValue());
                }
            } catch (Exception e) {
                LogUtils.e("加载本地数据库配置信息数据出错", e.getMessage());
            }
        } else {
            setItemView(R.drawable.ic_home, OauthHostManager.getInstance(this).getApiConfig());
            setItemView(R.drawable.ic_msg, OauthHostManager.getInstance(this).getMsgConfig());
            setItemView(R.drawable.ic_gps, OauthHostManager.getInstance(this).getGpsConfig());
        }
    }

    private void setItemView(int iconName, String textValue) {
        MyOneLineView guideItem = new MyOneLineView(this)
                .initMine(iconName, textValue, "", false,0);
        llConfig.addView(guideItem);
    }

    @Override
    protected boolean isEnableAppLock() {
        return false;
    }

    @OnClick(R.id.ll_setting_exit)
    void onClick(View v) {
        OauthHostManager.getInstance(this).cleanConfigStatus();
        UserSession.clear(this);
        // TODO 此处还需要采集员对应的自己数据同步数据,如果是需要跳转不能清理db
        //DataCacheManager.cleanDb(this);
        try {
            DataCacheManager.doClean(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        appContext.exit();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //System.exit(0);
    }
}