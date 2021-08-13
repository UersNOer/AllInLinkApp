package com.example.android_supervisor.cache;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.example.android_supervisor.Presenter.CaseSourcePresenter;
import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Area;
import com.example.android_supervisor.entities.AutuSysData;
import com.example.android_supervisor.entities.ConfigEntity;
import com.example.android_supervisor.entities.Contact;
import com.example.android_supervisor.entities.Dictionary;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.LayerConfig;
import com.example.android_supervisor.entities.ManualSyncEntity;
import com.example.android_supervisor.entities.MapGrid;
import com.example.android_supervisor.entities.PhotoEntity;
import com.example.android_supervisor.entities.SyncRequestBody;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.map.MapNotifyReceiver;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.android_supervisor.entities.ConstantEntity.GEOFENCE_BROADCAST_ACTION;


public class DataCacheManager {

    /**
     * 修改数据同步功能  自动同步私有数据
     */
    public static void automaticSync(final Context context){

        String roleId = UserSession.getRoleId(context);
        String userId = UserSession.getUserId(context);
        SyncRequestBody syncRequestBody = new SyncRequestBody();
        if(!TextUtils.isEmpty(roleId)){
            syncRequestBody.setRoleId(Long.parseLong(roleId));
        }
        if(!TextUtils.isEmpty(userId)){
            syncRequestBody.setUserId(Long.parseLong(userId));
        }

        syncRequestBody.setType("1");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(syncRequestBody));

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                doCleanUser(context);
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<Response<AutuSysData>>>() {

            @Override
            public ObservableSource<Response<AutuSysData>> apply(Integer response) throws Exception {
                PublicService publicService = ServiceGenerator.create(PublicService.class);
                return publicService.autoSync(body);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<AutuSysData>>(context, ProgressText.sync))
                .subscribe(new ResponseObserver<AutuSysData>(context) {
                    @Override
                    public void onSuccess(AutuSysData data) {

                        //排班数据  和工作网格数据 存入内容
                        if (data!=null){
                            Environments.autuSysData = data;

                            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);
                            ToastUtils.show(context, "自动数据同步完成");
                        }


                    }
                });
    }

    /**
     * 手动同步共有数据
     * @param context
     */
    public static void manualSync(final Context context,boolean isUpdate){

        if (!isUpdate){
            return;
        }

        String roleId = UserSession.getRoleId(context);
        String userId = UserSession.getUserId(context);
        SyncRequestBody syncRequestBody = new SyncRequestBody();
        if(!TextUtils.isEmpty(roleId)){
            syncRequestBody.setRoleId(Long.parseLong(roleId));
        }
        if(!TextUtils.isEmpty(userId)){
            syncRequestBody.setUserId(Long.parseLong(userId));
        }
        syncRequestBody.setType("1");
        Log.d("dd", new Gson().toJson(syncRequestBody));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(syncRequestBody));

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<Response<ManualSyncEntity>>>() {

            @Override
            public ObservableSource<Response<ManualSyncEntity>> apply(Integer response) throws Exception {
                PublicService publicService = ServiceGenerator.create(PublicService.class);

                CaseSourcePresenter caseSourcePresenter = new CaseSourcePresenter();
                caseSourcePresenter.getCaseSource(context,null);

                return publicService.manualSync(body);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<ManualSyncEntity>>(context, ProgressText.sync))
                .subscribe(new ResponseObserver<ManualSyncEntity>(context) {
                    @Override
                    public void onSuccess(ManualSyncEntity data) {
                        try {
                            doClean(context);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        boolean isSuccess =false;
                        try{
                            PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                            if (data.getCaseClass() != null && data.getCaseClass().size()>0) {
                                sqliteHelper.getEventTypeDao().create(data.getCaseClass());
                            }
                            if (data.getStandard() != null && data.getStandard().size()>0) {
                                sqliteHelper.getEventTypeDao().create(data.getStandard());
                                for (int i = 0; i < data.getStandard().size(); i++) {
                                    LogUtils.d("案件标准:"+ data.getStandard().get(i).getName());
                                }

                            }

                            if (data.getSettingPhoto() != null) {
                                sqliteHelper.getPhotoSettingDao().create(data.getSettingPhoto());
                            }

                            //图片url配置 这里注意 开发环境与测试环境 等环境切换时 此处一定要手动更新一次
                            if (data.getConfig()!=null){
                                //http://117.159.24.7/api/filesApi/files/
//                                UserSession.setFileServer(context,data.getConfig().getFileServer());
//                                String hostAddress = OauthHostManager.getInstance(context).getApiConfig();
//                                if (hostAddress.contains("117.159.24.4")){
//                                    UserSession.setFileServer(context,"http://117.159.24.4:30079/api/filesApi/files/");
//                                }

                                UserSession.setFileServer(context,data.getConfig().getFileServer());
                                Log.d("data.getConfig()",data.getConfig()+"");

                            }

                            //还差 区域网格数据

                            //图层图标数据
                            if (data.getLayerConfig() != null  && data.getLayerConfig().size()>0) {
//                                if(data.getConfig()!=null && data.getConfig().getFileServer()!= null){
//                                    for (LayerConfig layerConfig:data.getLayerConfig()){
//                                        String layerNeturl = layerConfig.getLayerUrl() + data.getConfig().getFileServer();
//                                        layerConfig.setLayerUrl(layerNeturl);
//                                    }
//                                }
                                sqliteHelper.getLayerConfigDao().create(data.getLayerConfig());
                            }

                            //考勤设置
                            if (data.getAttendanceSetting() != null) {
                                sqliteHelper.getAttendanceSettingDao().create(data.getAttendanceSetting());
                            }

                            //参数设置(gps）
                            if (data.getSettingConfig() != null && data.getSettingConfig().size() >0) {
                                sqliteHelper.getGpsConfigDao().create(data.getSettingConfig());
                            }
                            if (data.getSettingWatermark() != null){
                                sqliteHelper.getWaterSetDao().create(data.getSettingWatermark());

                            }
                            if (data.getCaseLevel() != null && data.getCaseLevel().size()>0) {
                                sqliteHelper.getCaseLevelDao().create(data.getCaseLevel());
                            }
                            isSuccess = true;
                        }catch (Exception e){
                            ToastUtils.show(context, "基础数据异常:"+e.getMessage());
                            isSuccess=false;
                            e.printStackTrace();
                        }

                        if (isSuccess){
                            try{
                                UserSession.setDataUpdated(context,true);
                                AppServiceManager.RestartServce(context);
                                ToastUtils.show(context, "基础数据同步完成");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else {
                            try {
                                doClean(context);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });

    }


    public static void doCleanUser(Context context) throws SQLException {
        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);

        sqliteHelper.getWorkPointDao().deleteBuilder().delete();
    }


    public static void doClean(Context context) throws SQLException {
        // 清除数据库
        releaseMemoryCache();
        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);

        sqliteHelper.getEventTypeDao().deleteBuilder().delete();
        sqliteHelper.getCaseLevelDao().deleteBuilder().delete();
        sqliteHelper.getPhotoSettingDao().deleteBuilder().delete();
        sqliteHelper.getAttendanceSettingDao().deleteBuilder().delete();
        sqliteHelper.getLayerConfigDao().deleteBuilder().delete();
        sqliteHelper.getGpsConfigDao().deleteBuilder().delete();
        sqliteHelper.getCaseLevelDao().deleteBuilder().delete();

        sqliteHelper.getCaseSourcesDao().deleteBuilder().delete();
        sqliteHelper.getWaterSetDao().deleteBuilder().delete();

//
//        sqliteHelper.getAreaDao().deleteBuilder().delete();
//        sqliteHelper.getContactDao().deleteBuilder().delete();
//        sqliteHelper.getDictionaryDao().deleteBuilder().delete();
//
//        sqliteHelper.getGlobalConfigDao().deleteBuilder().delete();

//        PrimarySqliteHelper sqliteHelper2 = PrimarySqliteHelper.getInstance(context);
//                sqliteHelper2.getMapGridDao().deleteBuilder().delete();
//        sqliteHelper2.getAreaGridDao().deleteBuilder().delete();

    }

    /**
     * 自动同部数据
     * @param context
     */
//    public static void manualSync1(final Context context) {
//
//        String roleId = UserSession.getRoleId(context);
//        String userId = UserSession.getUserId(context);
//        SyncRequestBody syncRequestBody = new SyncRequestBody();
//        syncRequestBody.setRoleId(Long.parseLong(roleId));
//        syncRequestBody.setUserId(Long.parseLong(userId));
//        syncRequestBody.setType("1");
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(syncRequestBody));
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                // 清除数据库
//
//                doClean(context);
//                emitter.onNext(1);
//                emitter.onComplete();
//            }
//        }).flatMap(new Function<Integer, ObservableSource<Response<ManualSyncEntity>>>() {
//            @Override
//            public ObservableSource<Response<ManualSyncEntity>> apply(Integer response) throws Exception {
//                PublicService publicService = ServiceGenerator.create(PublicService.class);
//                return publicService.autoSync(body);
//            }
//        }) .subscribeOn(Schedulers.newThread())
//           .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Response<ManualSyncEntity>>(context, ProgressText.sync))
//                .subscribe(new ResponseObserver<ManualSyncEntity>(context) {
//                    @Override
//                    public void onSuccess(ManualSyncEntity data) {
//
//                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
//                        if (data.getCaseClass()!=null){
//                            sqliteHelper.getEventTypeDao().create(data.getCaseClass());
//                        }
//                        if (data.getCaseLevel() != null && data.getCaseLevel().size() > 0) {
//
//                        }
//
//                        //TODO 自动同步数据
//                        ToastUtils.show(context, "数据同步完成");
//                    }
//                });
//    }

    /**
     * 手动同步数据
     * @param context
     */
    public static void 打他(final Context context) {
        String roleId = UserSession.getRoleId(context);
        String userId = UserSession.getUserId(context);
        SyncRequestBody syncRequestBody = new SyncRequestBody();
        syncRequestBody.setRoleId(Long.parseLong(roleId));
        syncRequestBody.setUserId(Long.parseLong(userId));
        syncRequestBody.setType("1");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(syncRequestBody));
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 清除数据库
                releaseMemoryCache();
                PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                sqliteHelper.getEventTypeDao().deleteBuilder().delete();
                sqliteHelper.getAreaDao().deleteBuilder().delete();
                sqliteHelper.getContactDao().deleteBuilder().delete();
                sqliteHelper.getDictionaryDao().deleteBuilder().delete();
//                sqliteHelper.getPhotoDao().deleteBuilder().delete();
//                sqliteHelper.getAttendanceDao().deleteBuilder().delete();
               // sqliteHelper.getGlobalConfigDao().deleteBuilder().delete();

                PrimarySqliteHelper sqliteHelper2 = PrimarySqliteHelper.getInstance(context);
//                sqliteHelper2.getMapGridDao().deleteBuilder().delete();
                sqliteHelper2.getAreaGridDao().deleteBuilder().delete();

                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<Response<ConfigEntity>>>() {
            // 获取fileServer
            @Override
            public ObservableSource<Response<ConfigEntity>> apply(Integer response) throws Exception {
                BasicService oauthService1 = ServiceGenerator.create(BasicService.class);
                return oauthService1.getConfig();
            }
        }).doOnNext(new Consumer<Response<ConfigEntity>>() {
            @Override
            public void accept(final Response<ConfigEntity> response) throws Exception {
                ConfigEntity configEntity = response.getData();
                String fileServer = configEntity.getFileServer();
//                SaveObjectUtils.getInstance(context).setObject("fileServer", fileServer);
            }
        }).flatMap(new Function<Response<ConfigEntity>, ObservableSource<Response<ManualSyncEntity>>>() {
            @Override
            public ObservableSource<Response<ManualSyncEntity>> apply(Response<ConfigEntity> result) throws Exception {
                // 配置信息
                PublicService publicService = ServiceGenerator.create(PublicService.class);
                return publicService.manualSync(body);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<ManualSyncEntity>>(context, ProgressText.sync))
                .subscribe(new ResponseObserver<ManualSyncEntity>(context) {
                    @Override
                    public void onSuccess(ManualSyncEntity syncData) {

                        if (syncData != null) {
                            PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                            PrimarySqliteHelper sqlitePrimaryHelper = PrimarySqliteHelper.getInstance(context);
                            if (syncData.getArea() != null && syncData.getArea().size() > 0) {
                                sqliteHelper.getAreaDao().create(syncData.getArea());
                            }
                            if (syncData.getAttendanceSetting() != null) {
//                                sqliteHelper.getAttendanceDao().create(syncData.getAttendanceSetting());
                            }
                            if (syncData.getCaseClass() != null && syncData.getCaseClass().size() > 0) {
                                sqliteHelper.getEventTypeDao().create(syncData.getCaseClass());
                            }
                            if (syncData.getCaseLevel() != null && syncData.getCaseLevel().size() > 0) {

                            }
                            if (syncData.getLayerConfig() != null && syncData.getLayerConfig().size() > 0) {
//                                SaveObjectUtils.getInstance(context).setObject("layerConfig", syncData.getLayerConfig());
                            }
//                            if (syncData.getSettingConfig() != null && syncData.getSettingConfig().size() > 0) {
//                                sqliteHelper.getGlobalConfigDao().create(syncData.getSettingConfig());
//                            }
                            if (syncData.getSettingPhoto() != null) {

                            }
                            if (syncData.getStandard() != null && syncData.getStandard().size() > 0) {
                                sqliteHelper.getEventTypeDao().create(syncData.getStandard());
                            }
                            if (syncData.getAreaGridSys() != null && syncData.getAreaGridSys().size() > 0) {
                                sqlitePrimaryHelper.getAreaGridDao().create(syncData.getAreaGridSys());
                            }
                        }
                        ToastUtils.show(context, "数据同步完成");
                    }
                });
    }

    private static void releaseTableCache() {
    }

//    @SuppressWarnings("unchecked")
//    public static void update(final Context context) {
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                // 清除数据库
//                releaseMemoryCache();
//                FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                sqliteHelper.getEventTypeDao().deleteBuilder().delete();
//                sqliteHelper.getAreaDao().deleteBuilder().delete();
//                sqliteHelper.getContactDao().deleteBuilder().delete();
//                sqliteHelper.getDictionaryDao().deleteBuilder().delete();
//
//                PrimarySqliteHelper sqliteHelper2 = PrimarySqliteHelper.getInstance(context);
//                sqliteHelper2.getMapGridDao().deleteBuilder().delete();
//
//                emitter.onNext(1);
//                emitter.onComplete();
//            }
//        }).flatMap(new Function<Integer, ObservableSource<Response<List<GlobalConfig>>>>() {
//            @Override
//            public ObservableSource<Response<List<GlobalConfig>>> apply(Integer result) throws Exception {
//                // 配置信息
//                PublicService publicService = ServiceGenerator.create(PublicService.class);
//                return publicService.getGlobalConfig(new QueryBody.Builder().create());
//            }
//        }).doOnNext(new Consumer<Response<List<GlobalConfig>>>() {
//            @Override
//            public void accept(Response<List<GlobalConfig>> response) throws Exception {
//                if (response.isSuccess()) {
//                    List<GlobalConfig> data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                        sqliteHelper.getGlobalConfigDao().create(data);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<GlobalConfig>>, ObservableSource<Response<List<EventType>>>>() {
//            @Override
//            public ObservableSource<Response<List<EventType>>> apply(Response<List<GlobalConfig>> result) throws Exception {
//                // 同步案件大小类
//                BasicService basicService = ServiceGenerator.create(BasicService.class);
//                return basicService.getEventTypeList(new QueryBody.Builder().eq("dbStatus", "1").create());
//            }
//        }).doOnNext(new Consumer<Response<List<EventType>>>() {
//            @Override
//            public void accept(Response<List<EventType>> response) throws Exception {
//                // 保存案件大小类
//                if (response.isSuccess()) {
//                    List<EventType> data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                        sqliteHelper.getEventTypeDao().create(data);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<EventType>>, ObservableSource<Response<List<EventType>>>>() {
//            @Override
//            public ObservableSource<Response<List<EventType>>> apply(Response<List<EventType>> response) throws Exception {
//                // 同步案件立案标准
//                BasicService basicService = ServiceGenerator.create(BasicService.class);
//                return basicService.getEventStandardList(new QueryBody.Builder().eq("dbStatus", "1").create());
//            }
//        }).doOnNext(new Consumer<Response<List<EventType>>>() {
//            @Override
//            public void accept(Response<List<EventType>> response) throws Exception {
//                // 保存案件立案标准
//                if (response.isSuccess()) {
//                    List<EventType> data = response.getData();
//                    if (data != null) {
//                        synchronized (context) {
//                            FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                            sqliteHelper.getEventTypeDao().create(data);
//                        }
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<EventType>>, ObservableSource<Response<List<Area>>>>() {
//            @Override
//            public ObservableSource<Response<List<Area>>> apply(Response<List<EventType>> response) throws Exception {
//                // 同步区域数据
//                BasicService basicService = ServiceGenerator.create(BasicService.class);
//                return basicService.getAreaList(new QueryBody.Builder().eq("dbStatus", "1").create());
//            }
//        }).doOnNext(new Consumer<Response<List<Area>>>() {
//            @Override
//            public void accept(Response<List<Area>> response) throws Exception {
//                // 保存区域数据
//                if (response.isSuccess()) {
//                    List<Area> data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                        sqliteHelper.getAreaDao().create(data);
//                    }
//                }
//            }
//        })
//                .flatMap(new Function<Response<List<Area>>, ObservableSource<Response<List<Dictionary>>>>() {
//                    @Override
//                    public ObservableSource<Response<List<Dictionary>>> apply(Response<List<Area>> response) throws Exception {
//                        // 同步字典数据
//                        BasicService basicService = ServiceGenerator.create(BasicService.class);
//                        return basicService.getDictionaryList(new QueryBody.Builder().eq("dbStatus", "1").create());
//                    }
//                }).doOnNext(new Consumer<Response<List<Dictionary>>>() {
//            @Override
//            public void accept(Response<List<Dictionary>> response) throws Exception {
//                // 保存字典数据
//                if (response.isSuccess()) {
//                    List<Dictionary> data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                        sqliteHelper.getDictionaryDao().create(data);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<Dictionary>>, ObservableSource<Response<PhotoEntity>>>() {
//            @Override
//            public ObservableSource<Response<PhotoEntity>> apply(Response<List<Dictionary>> response) throws Exception {
//                PublicService publicService = ServiceGenerator.create(PublicService.class);
//                return publicService.settingPhoto("1");
//            }
//        }).doOnNext(new Consumer<Response<PhotoEntity>>() {
//            @Override
//            public void accept(Response<PhotoEntity> response) throws Exception {
//                // 保存字典数据
//                if (response.isSuccess()) {
//                    PhotoEntity data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
////                        sqliteHelper.getPhotoDao().create(data);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<PhotoEntity>, ObservableSource<Response<List<Contact>>>>() {
//            @Override
//            public ObservableSource<Response<List<Contact>>> apply(Response<PhotoEntity> response) throws Exception {
//                // 同步联系人
//                BasicService basicService = ServiceGenerator.create(BasicService.class);
//                return basicService.getContactList(new QueryBody.Builder().pageSize(-1).create());
//            }
//        }).doOnNext(new Consumer<Response<List<Contact>>>() {
//            @Override
//            public void accept(Response<List<Contact>> response) throws Exception {
//                // 保存联系人
//                if (response.isSuccess()) {
//                    List<Contact> data = response.getData();
//                    if (data != null) {
//                        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(context);
//                        sqliteHelper.getContactDao().create(data);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<Contact>>, ObservableSource<Response<ConfigEntity>>>() {
//            // 获取fileServer
//            @Override
//            public ObservableSource<Response<ConfigEntity>> apply(Response<List<Contact>> response) throws Exception {
//                BasicService oauthService1 = ServiceGenerator.create(BasicService.class);
//                return oauthService1.getConfig();
//            }
//        }).doOnNext(new Consumer<Response<ConfigEntity>>() {
//            @Override
//            public void accept(final Response<ConfigEntity> response) throws Exception {
//                ConfigEntity configEntity = response.getData();
//                String fileServer = configEntity.getFileServer();
////                SaveObjectUtils.getInstance(context).setObject("fileServer", fileServer);
//            }
//        }).flatMap(new Function<Response<ConfigEntity>, ObservableSource<Response<List<LayerConfig>>>>() {
//            @Override
//            public ObservableSource<Response<List<LayerConfig>>> apply(Response<ConfigEntity> response) throws Exception {
//                // 获取图标图层
//                BasicService basicService = ServiceGenerator.create(BasicService.class);
//                return basicService.getLayerConfig(new QueryBody.Builder().create());
//            }
//        }).doOnNext(new Consumer<Response<List<LayerConfig>>>() {
//            @Override
//            public void accept(final Response<List<LayerConfig>> response) throws Exception {
//                List<LayerConfig> layerConfigList = response.getData();
//                if (layerConfigList != null) {
////                    SaveObjectUtils.getInstance(context).setObject("layerConfig", layerConfigList);
//                }
//            }
//        }).flatMap(new Function<Response<List<LayerConfig>>, ObservableSource<Response<List<MapGrid>>>>() {
//            @Override
//            public ObservableSource<Response<List<MapGrid>>> apply(Response<List<LayerConfig>> response) throws Exception {
//                // 获取监督员工作网格
//                PublicService publicService = ServiceGenerator.create(PublicService.class);
//                return publicService.getWorkGrid(UserSession.getUserId(context));
//            }
//        }).doOnNext(new Consumer<Response<List<MapGrid>>>() {
//            @Override
//            public void accept(Response<List<MapGrid>> response) throws Exception {
//                // 保存监督员工作网格
//                if (response.isSuccess()) {
//                    List<MapGrid> data = response.getData();
//                    if (data != null) {
//                        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);
//                        sqliteHelper.getMapGridDao().create(data);
////                        initMapFence(data, context);
//                    }
//                }
//            }
//        }).flatMap(new Function<Response<List<MapGrid>>, ObservableSource<Response>>() {
//            @Override
//            public ObservableSource<Response> apply(Response<List<MapGrid>> response) throws Exception {
//                return Observable.create(new ObservableOnSubscribe<Response>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
//                        Response response = new Response();
//                        response.setSuccess(true);
//                        emitter.onNext(response);
//                        emitter.onComplete();
//                    }
//                });
//            }
//        })
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Response>(context, ProgressText.sync))
//                .subscribe(new ResponseObserver(context) {
//                    @Override
//                    public void onSuccess(Object data) {
//                        ToastUtils.show(context, "数据同步完成");
//                    }
//                });
//    }

    public static void releaseMemoryCache() {

    }

    /**
     * 创建地理围栏
     */
    private static void initMapFence(List<MapGrid> mMapGridList, Context context) {
        GeoFenceClient mGeoFenceClient = new GeoFenceClient(context);
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_OUT);
        for (MapGrid mapGrid : mMapGridList) {
            List<DPoint> points = new ArrayList<DPoint>();
            for (int i = 0; i < mapGrid.getGeometry().getCoordinates().get(0).get(0).size(); i++) {
                points.add(new DPoint(mapGrid.getGeometry().getCoordinates().get(0).get(0).get(i).get(1),
                        mapGrid.getGeometry().getCoordinates().get(0).get(0).get(i).get(0)));
            }
            mGeoFenceClient.addGeoFence(points, mapGrid.getProperties().get("sqName"));
            Log.d("mMapGridList", "initMapFence: " + points.size() + ",sqName:" + mapGrid.getProperties().get("sqName"));
            GeoFenceListener fenceListenter = new GeoFenceListener() {
                @Override
                public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList,
                                                     int errorCode, String s) {
                    Log.d("initMapFence", errorCode + "添加围栏!" + geoFenceList.size());
                    if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {//判断围栏是否创建成功
                        Log.d("initMapFence", "添加围栏成功!" + s);
                        //geoFenceList是已经添加的围栏列表，可据此查看创建的围栏
                    } else {
                        Log.d("initMapFence", "添加围栏失败!" + s);
                    }
                }
            };
            mGeoFenceClient.setGeoFenceListener(fenceListenter);//设置回调监听
            mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
            MapNotifyReceiver mGeoFenceReceiver = new MapNotifyReceiver();
            IntentFilter filter = new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(GEOFENCE_BROADCAST_ACTION);
            context.registerReceiver(mGeoFenceReceiver, filter);
        }
    }

    public static void updateSy(final Context context){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 清除数据库
                releaseMemoryCache();
                PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                sqliteHelper.getEventTypeDao().deleteBuilder().delete();
                sqliteHelper.getAreaDao().deleteBuilder().delete();
                sqliteHelper.getContactDao().deleteBuilder().delete();
                sqliteHelper.getDictionaryDao().deleteBuilder().delete();

                PrimarySqliteHelper sqliteHelper2 = PrimarySqliteHelper.getInstance(context);
                sqliteHelper2.getMapGridDao().deleteBuilder().delete();

                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<Response<List<EventType>>>>() {
            @Override
            public ObservableSource<Response<List<EventType>>> apply(Integer result) throws Exception {
                // 同步案件大小类
                BasicService basicService = ServiceGenerator.create(BasicService.class);
                return basicService.getEventTypeList(new QueryBody.Builder().eq("dbStatus", "1").create());
            }
        }).doOnNext(new Consumer<Response<List<EventType>>>() {
            @Override
            public void accept(Response<List<EventType>> response) throws Exception {
                // 保存案件大小类
                if (response.isSuccess()) {
                    List<EventType> data = response.getData();
                    if (data != null) {
                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                        sqliteHelper.getEventTypeDao().create(data);
                    }
                }
            }
        }).flatMap(new Function<Response<List<EventType>>, ObservableSource<Response<List<EventType>>>>() {
            @Override
            public ObservableSource<Response<List<EventType>>> apply(Response<List<EventType>> response) throws Exception {
                // 同步案件立案标准
                BasicService basicService = ServiceGenerator.create(BasicService.class);
                return basicService.getEventStandardList(new QueryBody.Builder().eq("dbStatus", "1").create());
            }
        }).doOnNext(new Consumer<Response<List<EventType>>>() {
            @Override
            public void accept(Response<List<EventType>> response) throws Exception {
                // 保存案件立案标准
                if (response.isSuccess()) {
                    List<EventType> data = response.getData();
                    if (data != null) {
                        synchronized (context) {
                            PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                            sqliteHelper.getEventTypeDao().create(data);
                        }
                    }
                }
            }
        }).flatMap(new Function<Response<List<EventType>>, ObservableSource<Response<List<Area>>>>() {
            @Override
            public ObservableSource<Response<List<Area>>> apply(Response<List<EventType>> response) throws Exception {
                // 同步区域数据
                BasicService basicService = ServiceGenerator.create(BasicService.class);
                return basicService.getAreaList(new QueryBody.Builder().eq("dbStatus", "1").create());
            }
        }).doOnNext(new Consumer<Response<List<Area>>>() {
            @Override
            public void accept(Response<List<Area>> response) throws Exception {
                // 保存区域数据
                if (response.isSuccess()) {
                    List<Area> data = response.getData();
                    if (data != null) {
                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                        sqliteHelper.getAreaDao().create(data);
                    }
                }
            }
        })
                .flatMap(new Function<Response<List<Area>>, ObservableSource<Response<List<Dictionary>>>>() {
                    @Override
                    public ObservableSource<Response<List<Dictionary>>> apply(Response<List<Area>> response) throws Exception {
                        // 同步字典数据
                        BasicService basicService = ServiceGenerator.create(BasicService.class);
                        return basicService.getDictionaryList(new QueryBody.Builder().eq("dbStatus", "1").create());
                    }
                }).doOnNext(new Consumer<Response<List<Dictionary>>>() {
            @Override
            public void accept(Response<List<Dictionary>> response) throws Exception {
                // 保存字典数据
                if (response.isSuccess()) {
                    List<Dictionary> data = response.getData();
                    if (data != null) {
                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                        sqliteHelper.getDictionaryDao().create(data);
                    }
                }
            }
        }).flatMap(new Function<Response<List<Dictionary>>, ObservableSource<Response<PhotoEntity>>>() {
            @Override
            public ObservableSource<Response<PhotoEntity>> apply(Response<List<Dictionary>> response) throws Exception {
                PublicService publicService = ServiceGenerator.create(PublicService.class);
                return publicService.settingPhoto("1");
            }
        }).doOnNext(new Consumer<Response<PhotoEntity>>() {
            @Override
            public void accept(Response<PhotoEntity> response) throws Exception {
                // 保存字典数据
                if (response.isSuccess()) {
                    PhotoEntity data = response.getData();
                    if (data != null) {
                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
//                        sqliteHelper.getPhotoDao().create(data);
                    }
                }
            }
        }).flatMap(new Function<Response<PhotoEntity>, ObservableSource<Response<List<Contact>>>>() {
            @Override
            public ObservableSource<Response<List<Contact>>> apply(Response<PhotoEntity> response) throws Exception {
                // 同步联系人
                BasicService basicService = ServiceGenerator.create(BasicService.class);
                return basicService.getContactList();
            }
        }).doOnNext(new Consumer<Response<List<Contact>>>() {
            @Override
            public void accept(Response<List<Contact>> response) throws Exception {
                // 保存联系人
                if (response.isSuccess()) {
                    List<Contact> data = response.getData();
                    if (data != null) {
                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
                        sqliteHelper.getContactDao().create(data);
                    }
                }
            }
        }).flatMap(new Function<Response<List<Contact>>, ObservableSource<Response<ConfigEntity>>>() {
            // 获取fileServer
            @Override
            public ObservableSource<Response<ConfigEntity>> apply(Response<List<Contact>> response) throws Exception {
                BasicService oauthService1 = ServiceGenerator.create(BasicService.class);
                return oauthService1.getConfig();
            }
        }).doOnNext(new Consumer<Response<ConfigEntity>>() {
            @Override
            public void accept(final Response<ConfigEntity> response) throws Exception {
                ConfigEntity configEntity = response.getData();
                String fileServer = configEntity.getFileServer();
//                SaveObjectUtils.getInstance(context).setObject("fileServer", fileServer);
            }
        }).flatMap(new Function<Response<ConfigEntity>, ObservableSource<Response<List<LayerConfig>>>>() {
            @Override
            public ObservableSource<Response<List<LayerConfig>>> apply(Response<ConfigEntity> response) throws Exception {
                // 获取图标图层
                BasicService basicService = ServiceGenerator.create(BasicService.class);
                return basicService.getLayerConfig(new QueryBody.Builder().create());
            }
        }).doOnNext(new Consumer<Response<List<LayerConfig>>>() {
            @Override
            public void accept(final Response<List<LayerConfig>> response) throws Exception {
                List<LayerConfig> layerConfigList = response.getData();
                if (layerConfigList != null) {
//                    SaveObjectUtils.getInstance(context).setObject("layerConfig", layerConfigList);
                }
            }
        }).flatMap(new Function<Response<List<LayerConfig>>, ObservableSource<Response<List<MapGrid>>>>() {
            @Override
            public ObservableSource<Response<List<MapGrid>>> apply(Response<List<LayerConfig>> response) throws Exception {
                // 获取监督员工作网格
                PublicService publicService = ServiceGenerator.create(PublicService.class);
                return publicService.getWorkGrid(UserSession.getUserId(context));
            }
        }).doOnNext(new Consumer<Response<List<MapGrid>>>() {
            @Override
            public void accept(Response<List<MapGrid>> response) throws Exception {
                // 保存监督员工作网格
                if (response.isSuccess()) {
                    List<MapGrid> data = response.getData();
                    if (data != null) {
                        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);
                        sqliteHelper.getMapGridDao().create(data);
//                        initMapFence(data, context);
                    }
                }
            }
        }).flatMap(new Function<Response<List<MapGrid>>, ObservableSource<Response>>() {
            @Override
            public ObservableSource<Response> apply(Response<List<MapGrid>> response) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Response>() {
                    @Override
                    public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                        Response response = new Response();
                        response.setSuccess(true);
                        emitter.onNext(response);
                        emitter.onComplete();
                    }
                });
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(context, ProgressText.sync))
                .subscribe(new ResponseObserver(context) {
                    @Override
                    public void onSuccess(Object data) {
                        ToastUtils.show(context, "数据同步完成");
                    }
                });

    }

    /**
     * 重置app 最好删除掉db 文件、防止异常情况
     * @param context
     */
    public static void cleanDb(Context context){
        String dir = Storage.getDataDir(context);
        DataCacheCleaner.clearFolder(dir, false);
    }

    public static void cleanUserDb(Context context){
        String dir = Storage.getUserDataDir(context);
        DataCacheCleaner.clearFolder(dir, false);
    }

}
