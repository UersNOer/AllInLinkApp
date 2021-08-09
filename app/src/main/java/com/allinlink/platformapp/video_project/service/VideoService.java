package com.allinlink.platformapp.video_project.service;


import com.unistrong.network.BaseOutput;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.config.Configs;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * description: pda接口
 * <p>
 * author ltd
 * date 2020-10-20
 **/
public interface VideoService {


    /**
     * 登录
     */
    @POST(Configs.LOGIN_REQUEST_PATH)
    Observable<BaseBean<LoginOutput>> login(@Body Map body);

    /**
     * 登出
     */
    @GET(Configs.LOGOUT)
    Observable<BaseBean> logout();

    /**
     * 获得地图Marker
     */
    @POST(Configs.MAP_GETMARKER)
    Observable<BaseBean<MapMarkerBean>> getMapMarker(@Body Map body);


    /**
     * 查询所有摄像头
     */
    @POST(Configs.CAMERA_ALL)
    Observable<BaseBean<CameraBean>> getAllCamera(@Body Map<String, Object> headers);


    /**
     * 查询所有摄像头
     */
    @POST(Configs.UMeng_TOKEN)
    Observable<BaseBean> addUMengToken(@Body Map<String, Object> headers);

    /**
     * 添加设备
     */
    @POST(Configs.CAMERA_ADD)
    Observable<BaseBean<String>> addCamera(@Body Map<String, Object> headers);

    /**
     * 查询字典
     */
    @POST(Configs.FindChannelInfoChecked)
    Observable<BaseBean<String>> findChannelInfoChecked(@Body Map<String, Object> headers);
    /**
     * 获取所有公司
     */
    @POST(Configs.FindAllTenantInfoInList)
    Observable<BaseBean<String>> findAllTenanInfoList(@Body Map<String, Object> headers);

    /**
     * 摄像机分组管理
     */
    @POST(Configs.FindAllCreamManage)
    Observable<BaseBean<String>> findCreamManage(@Body Map<String, Object> headers);
    /**
     * 云台控制
     */
    @POST(Configs.CLOUND_CONTROL)
    Observable<BaseBean<String>> cloundControl(@Body Map<String, Object> headers);

    /**
     * 通过ID查询通道
     */
    @POST(Configs.CAMERA_QUERYBYID)
    Observable<BaseBean<List<ChannelBean>>> queryChannelById(@Body Map<String, Object> headers);


    /**
     * 通过ID查询通道
     */
    @POST(Configs.CAMERA_UPIFO)
    Observable<BaseBean> upChannelInfo(@Body Map<String, Object> headers);

    /**
     * 通过ID查询通道
     */
    @POST(Configs.USER_ORDER)
    Observable<BaseBean<OrderBean>> queryOrder(@Body Map<String, Object> headers);

    /**
     * 关闭工单
     */
    @POST(Configs.USER_ORDER_CLOSE)
    Observable<BaseBean> coloseOrder(@Body Map<String, Object> headers);

    /**
     * 查询用户可查看通道
     */
    @POST(Configs.CAMERA_QUER)
    Observable<BaseBean<List<ChannelBean>>> queryChannel(@Body Map<String, Object> headers);

    /**
     * 更改密码
     */
    @POST(Configs.USER_UP_PASSWORD)
    Observable<BaseBean> updatePassword(@Body Map<String, Object> headers);


    /**
     * 通过时间
     */
    @POST(Configs.CHANNEL_BYTIME)
    Observable<BaseBean<PlayBackBean>> queryChannelByTime(@Body Map<String, Object> headers);


    /**
     * 获取用户信息
     */
    @POST(Configs.USER_INFO)
    Observable<BaseBean<LoginOutput>> getUserInfo(@Body Map<String, Object> headers);


    /**
     * 新增收藏通道
     */
    @POST(Configs.USER_ADD_COLLECT)
    Observable<BaseBean> addChannelUserfavorites(@Body Map<String, Object> headers);


    /**
     * 删除收藏通道
     */
    @POST(Configs.USER_REMOVE_COLLECT)
    Observable<BaseBean> removeChannelUserfavoritesById(@Body Map<String, Object> headers);

    /**
     * 修改用户信息
     */


    @POST(Configs.USER_UPINFO)
    Observable<BaseBean> upUserInfo(@Body Map<String, Object> headers);

    /**
     * 查询收藏列表
     */

    @POST(Configs.USER_COLLECT)
    Observable<BaseBean<List<ChannelBean>>> queryCollect(@Body Map<String, Object> headers);

    @POST(Configs.ORDER_DOWNLOAD_FILE)
    Observable<ResponseBody> downLoadFile(@Query("fileName") String fileName, @Query("gid") String gid);
}
