package com.example.android_supervisor.common;

import android.content.Context;

import com.example.android_supervisor.utils.LocalSaveUtils;


public class UserSession {
    private static final String PREF_NAME = "user_session";
    public static final String USERID_PDA_STUTUS="Pda_Status";

    public static final String USERID_KEY="user_id";
    public static final String USERNAME_KEY="user_name";
    public static final String USERPWD_KEY="user_pwd";
    public static final String LOGINNAME_KEY="login_name";
    public static final String USERMOBILE_KEY="mobile";

    public static final String USERROLE_ID_KEY="role_id";

    public static final String USEROLE_NAME_KEY="role_name";

    public static final String USER_DataUptate="data_Update";

    public static final String FILE_Server="file_server";

    public static final String GUIDE_STATUS="guide_enter";


    public static final String LOGIN_INFO="login_info";


    public static final String OAUTH_HOSTStatus_KEY="oauthHostStatus";


    public static void setUserId(Context context, String userId) {
        LocalSaveUtils.saveString(context, USERID_KEY, userId);
    }

    public static void setUserName(Context context, String userName) {
        LocalSaveUtils.saveString(context, USERNAME_KEY, userName);
    }

    public static void setUserPwd(Context context, String pwd) {
        LocalSaveUtils.saveString(context, USERPWD_KEY, pwd);
    }

    public static void setWritePdaStatus(Context context, boolean PdaStatus) {
        LocalSaveUtils.saveBoolean(context, USERID_PDA_STUTUS, PdaStatus);
    }

    public static boolean getWritePdaStatus(Context context) {
        return  LocalSaveUtils.getBoolean(context, USERID_PDA_STUTUS, false);
    }


    public static void setMobile(Context context, String mobile) {
        LocalSaveUtils.saveString(context, USERMOBILE_KEY, mobile);
    }

    public static void setRoleId(Context context, String role_id) {
        LocalSaveUtils.saveString(context, USERROLE_ID_KEY, role_id);

    }

    public static void setDataUpdated(Context context, boolean isUpdated) {
        LocalSaveUtils.saveBoolean(context, USER_DataUptate, isUpdated);
    }

    public static void setRoleInfoTAG(Context context, String roleInfoName) {
        LocalSaveUtils.saveString(context, USEROLE_NAME_KEY, roleInfoName);
    }


    public static boolean getDataUpdated(Context context) {
        return LocalSaveUtils.getBoolean(context, USER_DataUptate,false );
    }

    public static void setFileServer(Context context, String fileServer) {
        LocalSaveUtils.saveString(context,FILE_Server , fileServer);
    }


    public static String getFileServer(Context context) {
        return LocalSaveUtils.getString(context,FILE_Server,"");
    }

    /**
     * 用户id
     */
    public static String getUserId(Context context) {
        return LocalSaveUtils.getString(context, USERID_KEY,"00000000-0000-0000-0000-000000000000" );

    }

    /**
     * 用户密码
     */
    public static String getUserPwd(Context context) {

        return LocalSaveUtils.getString(context, USERPWD_KEY,"" );
    }

    /**
     * 用户名
     */
    public static String getUserName(Context context) {

        return LocalSaveUtils.getString(context, USERNAME_KEY,"" );
    }

    /**
     * 获取登录名
     */
//    public static String getloginName(Context context) {
//
//        return LocalSaveUtils.getString(context, LOGINNAME_KEY,"" );
//    }


    /**
     * 设置登录名
     */
//    public static void setloginName(Context context,String loginName) {
//
//        LocalSaveUtils.saveString(context, LOGINNAME_KEY, loginName);
//    }



    /**
     * 手机号码
     */
    public static String getMobile(Context context) {
        return LocalSaveUtils.getString(context, USERMOBILE_KEY,"" );
    }

    /**
     * 角色id
     */
    public static String getRoleId(Context context) {

        return LocalSaveUtils.getString(context, USERROLE_ID_KEY,"" );
    }
    /**
     * 角色名
     */
    public static String getRoleName(Context context) {
        return LocalSaveUtils.getString(context, USEROLE_NAME_KEY,"" );
    }


    public static void setGuideStatus(Context context,boolean guideStatus) {
         LocalSaveUtils.saveBoolean(context, GUIDE_STATUS,guideStatus);
    }

    public static boolean getGuideStatus(Context context) {
        return LocalSaveUtils.getBoolean(context, GUIDE_STATUS,false);
    }


    /**
     * 获取登录信息
     */
    public static String getLoginInfo(Context context) {

        return LocalSaveUtils.getString(context, LOGIN_INFO,"" );
    }

    public static void setLoginInfo(Context context, String roleInfoName) {
        LocalSaveUtils.saveString(context, LOGIN_INFO, roleInfoName);
    }





    /**
     * 清除用户信息
     */
    public static void clear(Context context) {
        setUserId(context,"");
        setUserPwd(context, "");
        setUserName(context, "");
       // setloginName(context, "");
        setMobile(context, "");
        setRoleId(context, "");
        setRoleInfoTAG(context, "");
        setDataUpdated(context,false);
        setFileServer(context,"");

//        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
//        editor.clear();
//        editor.apply();
    }


}
