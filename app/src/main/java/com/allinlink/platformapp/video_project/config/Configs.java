
package com.allinlink.platformapp.video_project.config;

/**
 * 配置类
 *
 * @author ltd
 * @date 2020/9/18
 **/
public class Configs {

    public static final String CONFIG_TAB = "tabConfig.json";

    //  public static String DOMAIN = "http://117.159.24.13:8881/";
//  public static String DOMAIN = "http://10.1.100.63:6001//";
    public static String DOMAIN = "http://10.19.67.123:9003/";
    //登陆
    public static final String LOGIN_REQUEST_PATH = "iot/api/iot.server.login";
    //登出
    public static final String LOGOUT = "ipms/user/logout";

    //获取地图点位
    public static final String MAP_GETMARKER = "iot/api/iot.wfs.test";
    //分页查询摄像头
    public static final String CAMERA_ALL = "iot/api/findAllCameraInfoInPage";
    public static final String UMeng_TOKEN = "iot/api/iot.server.updateAppCode";
    //添加设备
    public static final String CAMERA_ADD = "iot/api/addDevice";
    //查询字典 10004设备字典
    public static final String FindChannelInfoChecked = "iot/api/iot.mj.findMjByType";
    //查询所有组织
    public static final String FindAllTenantInfoInList="iot/api/iot.tenant.findAllTenantInfoInList";
    //查询所有组织
    public static final String FindAllCreamManage="iot/api/iot.fltx.getTree";
    //云台控制
    public static final String CLOUND_CONTROL = "iot/api/iot.server.ptzControl";
    //通过GID查询通道
    public static final String CAMERA_QUERYBYID = "iot/api/findAllChannelInfoInList";
    public static final String CAMERA_UPIFO = "iot/api/updateChannelBaseInfo";
    public static final String CAMERA_QUER = "iot/api/findChannelInfoChecked";
    //查询订单
    public static final String USER_ORDER = "iot/api/findAllTaskInfoInPageEx";
    //修改订单状态
    public static final String USER_ORDER_CLOSE = "iot/api/updateTaskInfoStatus";


    //分页查询摄像头
    public static final String USER_UP_PASSWORD = "iot/api/iot.server.updatePassword";
    //获取用户信息
    public static final String USER_INFO = "iot/api/iot.passportInfo.getUserMessage";
    //修改用户信息
    public static final String USER_UPINFO = "iot/api/iot.server.updatePassportInfo";
    //查询收藏列表
    public static final String USER_COLLECT="iot/api/findChannelUserfavoritesByUserId";
    //下载路径
    public static final String ORDER_DOWNLOAD_FILE="iot/api/downLoad";
    //添加收藏
    public static final String USER_ADD_COLLECT = "iot/api/addChannelUserfavorites";
    //删除收藏
    public static final String USER_REMOVE_COLLECT = "iot/api/removeChannelUserfavoritesById";
    //通过时间获取视频
    public static final String CHANNEL_BYTIME = "iot/api/findPlaybackByTime";

    public static final String BST_PTZ_UP = "100"; //云台向上
    public static final String BST_PTZ_DOWN = "101"; //云台向下
    public static final String BST_PTZ_LEFT = "102"; //云台向左
    public static final String BST_PTZ_RIGHT = "103";//云台向右
    public static final String BST_PTZ_ZOOM_IN = "104"; //焦距变大(倍率变大)
    public static final String BST_PTZ_ZOOM_OUT = "105"; //焦距变小(倍率变小)
    public static final String BST_PTZ_FOCUS_IN = "106"; //焦点前调
    public static final String BST_PTZ_FOCUS_OUT = "107"; //焦点后调
    public static final String BST_PTZ_IRIS_IN = "108"; //光圈扩大
    public static final String BST_PTZ_IRIS_OUT = "109";//光圈缩小
    public static final String BST_PTZ_SET_PRESET = "110";//设置预置点
    public static final String BST_PTZ_CLEAR_PRESET = "111"; //清除预置点
    public static final String BST_PTZ_GOTO_PRESET = "112"; //转至预置点
    public static final String BST_PTZ_STOP = "0"; //停止云台操作

    public static final String SERACH_CHANNEL_HISTORY = "serach_channel_history";


}
