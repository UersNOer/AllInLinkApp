package com.allinlink.platformapp.video_project.provider;

import android.content.Context;
import android.content.Intent;

import com.tangxiaolv.annotations.RouterModule;
import com.tangxiaolv.annotations.RouterPath;
import com.unistrong.common.config.CommonConfig;
import com.allinlink.platformapp.video_project.ui.activity.LoginActivity;
import com.allinlink.platformapp.video_project.utils.LoginUtils;

/**
 * @author ltd
 * @description: TODO
 **/
@RouterModule(scheme = CommonConfig.SCHEME, host = CommonConfig.USER_PROVIDER)
public class UserProvider {

    /**
     * 对外提供 跳转到登录界面
     *
     * @param context
     */
    @RouterPath(value = CommonConfig.OPEN_LOGINACTIVITY_PATH)
    public void openLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 对外提供 清除用户数据
     */
    @RouterPath(value = CommonConfig.CLEAR_USER_DATA_PATH)
    public void clearUserData() {
        LoginUtils.clearUserLoginData();
    }
}
