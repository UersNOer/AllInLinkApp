
package com.allinlink.platformapp.video_project.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.unistrong.utils.AppContextUtils;
import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.ui.activity.LoginActivity;
import com.allinlink.platformapp.video_project.bean.LoginOutput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 登录工具类
 * Created by ltd ON 2020/4/21
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class LoginUtils {

    /**
     * 登录成功保存数据
     */
    public static void setUserLoginData(LoginOutput result) {
        SharedPreferencesUtil.getInstance().putLoginUsername(result.getUserName());
        SharedPreferencesUtil.getInstance().putUserId(result.getGid());
    }


    /**
     * 清除用户登录数据
     */
    public static void clearUserLoginData() {
        SharedPreferencesUtil.getInstance().putToken("");
        SharedPreferencesUtil.getInstance().putUserId("");
        SharedPreferencesUtil.getInstance().putLoginPwd("");
        SharedPreferencesUtil.getInstance().putLoginUsername("");
//        SharedPreferencesUtil.getInstance().putUserEmail("");
//        SharedPreferencesUtil.getInstance().putMobile("");
//        SharedPreferencesUtil.getInstance().putNickName("");
    }

    /**
     * 获取登录的用户名
     *
     * @retusrn uername
     */
    public static String getLoginUsername() {
        return SharedPreferencesUtil.getInstance().getLoginUsername();
    }

    /**
     * 获取登录账号的密码
     *
     * @return pwd
     */
    public static String getLoginPwd() {
        return SharedPreferencesUtil.getInstance().getLoginPwd();
    }

    /**
     * 保存登录未加密密码
     *
     * @param password
     */
    public static void setLoginPwd(String password) {
        SharedPreferencesUtil.getInstance().putLoginPwd(password);
    }


    /**
     * 判断是否为正确密码格式
     * 密码至少要含数字，字母、字符 两种组合且长度为8-16位
     *
     * @param password 密码
     */
    public static boolean isPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }

        String regular = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\W]+$)[a-zA-Z0-9\\W]{8,16}";
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(password);
        return m.matches();

    }

    /**
     * 用户名格式
     *
     * @param username 用户名
     */
    public static boolean isUsername(String username) {
        String regular = "^[\\u4E00-\\u9FA50-9a-zA-Z_]+$";
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(username);
        return m.matches();
    }


    public static void openLoginActivity(Context context) {
        SharedPreferencesUtil.getInstance().putToken("");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        AppContextUtils.finishAllActivity();
    }


}
