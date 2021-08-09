package com.unistrong.network.router;

import android.content.Context;

import com.tangxiaolv.router.AndroidRouter;
import com.unistrong.common.config.CommonConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 康鹏飞
 * @description: user模块Router
 * @date 2020-06-5
 **/
public class UserModuleRouter {
    /**
     * 跳转到登录页面
     */
    public void openLoginActivity(Context context) {
        Map map = new HashMap();
        map.put("context", context);
        AndroidRouter.open(CommonConfig.SCHEME, CommonConfig.USER_PROVIDER, CommonConfig.OPEN_LOGINACTIVITY_PATH, map).call();
    }


    /**
     * 清除用户数据
     */
    public void clearUserData() {
        AndroidRouter.open(CommonConfig.SCHEME, CommonConfig.USER_PROVIDER, CommonConfig.CLEAR_USER_DATA_PATH).call();
    }

}
