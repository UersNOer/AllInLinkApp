package com.example.android_supervisor.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.example.android_supervisor.entities.GpsConfig;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.http.service.CodeService;
import com.example.android_supervisor.http.service.FileService;
import com.example.android_supervisor.http.service.OauthService;
import com.example.android_supervisor.http.service.PersonalizedService;
import com.example.android_supervisor.http.service.PlatformService;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.http.service.YqytService;
import com.example.android_supervisor.http.service.YqytsService;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * APP 配置类
 * 1、参数配置信息
 *
 * @author wujie
 */
public class AppConfig {
    // GPS 采集频率（秒）
    public static final String GPS_REQUEST_INTERVAL = "gps_request_interval";
    // GPS 上报频率（秒）
    public static final String GPS_SEND_INTERVAL = "gps_send_interval";
    // 城管通 App 自动锁定时间（分钟）
    public static final String APP_AUTO_LOCKING_INTERVAL = "app_auto_locking_interval";
    // 城管通 App 自动退出时间（分钟）
    public static final String APP_AUTO_EXIT_INTERVAL = "app_auto_exit_interval";
    // 文件服务器上对应的目录的ID
    public static final String FILES_PARENT_ID = "files_parent_id";

    // 参数配置集合（gPS)
    private static Map<String, String> globalConfigs;
    // 服务器配置集合
   // private static List<ServiceConfig> serviceConfigs;
    // 忽略鉴权列表集合
    private static List<String> ignoreAuthPatterns;

    // 服务器本地配置集合
    private static List<ServiceConfig> LocalServiceConfigs;


    /**
     *
     * 默认加载那个配置文件
     */
    public static final String CONFIG_NAME = "localservice.cfg";

    private AppConfig() {
        throw new AssertionError("不能构造 AppConfig");
    }

    public static void initialized(Context context) {
        try {
            loadGlobalConfigs(context);//包含文件夹id
            //loadServiceConfigs(context);
            loadLocalConfig(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGlobalConfigs(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream stream = assetManager.open("global.cfg");

        Properties properties = new Properties();
        properties.load(stream);

        globalConfigs = new HashMap<>();

        Set<String> appConfigNames = properties.stringPropertyNames();
        for (String configName : appConfigNames) {
            String configValue = properties.getProperty(configName);
            globalConfigs.put(configName, configValue);
        }
    }

    /**
     * 导入本地配置文件
     * @param context
     * @throws IOException
     */
    private static void loadLocalConfig(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream stream = assetManager.open("localservice.cfg");
        Properties properties = new Properties();
        properties.load(stream);

        LocalServiceConfigs = new ArrayList<>();
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.oauth.name");
            srvConfig.domain = properties.getProperty("service.oauth.domain");
            srvConfig.className = OauthService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.platform.name");
            srvConfig.domain = properties.getProperty("service.platform.domain");
            srvConfig.className = PlatformService.class.getName();
            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.digital.basic.name");
            srvConfig.domain = properties.getProperty("service.digital.basic.domain");
            srvConfig.className = BasicService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.digital.question.name");
            srvConfig.domain = properties.getProperty("service.digital.question.domain");
            srvConfig.className = QuestionService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.digital.publics.name");
            srvConfig.domain = properties.getProperty("service.digital.publics.domain");
            srvConfig.className = PublicService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.files.name");
            srvConfig.domain = properties.getProperty("service.files.domain");
            srvConfig.className = FileService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }

        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.code.name");
            srvConfig.domain = properties.getProperty("service.code.domain");
            srvConfig.className = CodeService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }


        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.yq.name");
            srvConfig.domain = properties.getProperty("service.yq.domain");
            srvConfig.className = YqService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }

        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.yqyt.name");
            srvConfig.domain = properties.getProperty("service.yqyt.domain");
            srvConfig.className = YqytService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }

        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.yqyts.name");
            srvConfig.domain = properties.getProperty("service.yqyts.domain");
            srvConfig.className = YqytsService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }
        {
            ServiceConfig srvConfig = new ServiceConfig();
            srvConfig.name = properties.getProperty("service.yqyts.name");
            srvConfig.domain = properties.getProperty("service.yqyts.domain");
            srvConfig.className = PersonalizedService.class.getName();

            LocalServiceConfigs.add(srvConfig);
        }



        String urlPatterns = properties.getProperty("service.ignoreAuth.patterns");
        ignoreAuthPatterns = Arrays.asList(urlPatterns.split(","));
    }


//    private static void loadServiceConfigs(Context context) throws IOException {
//        AssetManager assetManager = context.getAssets();
//        InputStream stream = assetManager.open("service.cfg");
//        Properties properties = new Properties();
//        properties.load(stream);
//
//        loadHttpServiceConfigs(context, properties);
//    }

//    private static void loadHttpServiceConfigs(Context context, Properties properties) {
//        serviceConfigs = new ArrayList<>();
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.oauth.name");
//            srvConfig.scheme = properties.getProperty("service.oauth.scheme");
//            srvConfig.domain = properties.getProperty("service.oauth.domain");
//            srvConfig.prefix = properties.getProperty("service.oauth.prefix");
//            srvConfig.className = OauthService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.platform.name");
//            srvConfig.scheme = properties.getProperty("service.platform.scheme");
//            srvConfig.domain = properties.getProperty("service.platform.domain");
//            srvConfig.prefix = properties.getProperty("service.platform.prefix");
//            srvConfig.className = PlatformService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.digital.basic.name");
//            srvConfig.scheme = properties.getProperty("service.digital.basic.scheme");
//            srvConfig.domain = properties.getProperty("service.digital.basic.domain");
//            srvConfig.prefix = properties.getProperty("service.digital.basic.prefix");
//            srvConfig.className = BasicService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.digital.question.name");
//            srvConfig.scheme = properties.getProperty("service.digital.question.scheme");
//            srvConfig.domain = properties.getProperty("service.digital.question.domain");
//            srvConfig.prefix = properties.getProperty("service.digital.question.prefix");
//            srvConfig.className = QuestionService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.digital.publics.name");
//            srvConfig.scheme = properties.getProperty("service.digital.publics.scheme");
//            srvConfig.domain = properties.getProperty("service.digital.publics.domain");
//            srvConfig.prefix = properties.getProperty("service.digital.publics.prefix");
//            srvConfig.className = PublicService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.files.name");
//            srvConfig.scheme = properties.getProperty("service.files.scheme");
//            srvConfig.domain = properties.getProperty("service.files.domain");
//            srvConfig.prefix = properties.getProperty("service.files.prefix");
//            srvConfig.className = FileService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//
//        {
//            ServiceConfig srvConfig = new ServiceConfig();
//            srvConfig.name = properties.getProperty("service.login.name");
//            srvConfig.scheme = properties.getProperty("service.login.scheme");
//            srvConfig.domain = properties.getProperty("service.login.domain");
//            srvConfig.prefix = properties.getProperty("service.login.prefix");
//            srvConfig.className = LoginService.class.getName();
//            serviceConfigs.add(srvConfig);
//        }
//
//
//        String urlPatterns = properties.getProperty("service.ignoreAuth.patterns");
//        ignoreAuthPatterns = Arrays.asList(urlPatterns.split(","));
//    }

    public static int getGpsRequestInterval(Context context) {
        String interval = getGlobalConfigValue(context, GPS_REQUEST_INTERVAL);
        return TextUtils.isDigitsOnly(interval) ? Math.max(Integer.valueOf(interval), 1) : 1;
    }

    public static int getGpsSendInterval(Context context) {
        String interval = getGlobalConfigValue(context, GPS_SEND_INTERVAL);
        return TextUtils.isDigitsOnly(interval) ? Math.max(Integer.valueOf(interval), 1) : 1;
    }

    public static int getAppAutoLockingInterval(Context context) {
        String interval = getGlobalConfigValue(context, APP_AUTO_LOCKING_INTERVAL);

        return TextUtils.isDigitsOnly(interval) ? Math.max(Integer.valueOf(interval), 1) : 1;
    }

    public static int getAppAutoExitInterval(Context context) {
        String interval = getGlobalConfigValue(context, APP_AUTO_EXIT_INTERVAL);
        return TextUtils.isDigitsOnly(interval) ? Math.max(Integer.valueOf(interval), 1) : 1;
    }

    public static String getDefaultAreaCode(Context context) {
        return getGlobalConfigValue(context, "map_area_code");
    }

    public static String getFilesParentId(Context context) {
        return getGlobalConfigValue(context, FILES_PARENT_ID);
    }

    private static String getGlobalConfigValue(Context context, String key) {
        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
        List<GpsConfig> config = sqliteHelper.getGpsConfigDao().queryForEq("configKey", key);
        LogUtils.e("App auto.1"+key);
        if (config == null) {

            return globalConfigs.get(key);
        } else {
            if (config.size()>0){
                LogUtils.e("App auto.3:"+config.get(0).getConfigValue());
                return config.get(0).getConfigValue();
            }
        }
        return globalConfigs.get(key);
    }

//    public static List<ServiceConfig> getServiceConfigs() {
//        return Collections.unmodifiableList(serviceConfigs);
//    }

    public static List<ServiceConfig> getLocalServiceConfigs() {
        return Collections.unmodifiableList(LocalServiceConfigs);
    }

//    public static ServiceConfig getHttpServiceConfig(String serviceName) {
//        for (ServiceConfig serviceConfig : serviceConfigs) {
//            if (serviceConfig.getName().equals(serviceName)) {
//                return serviceConfig;
//            }
//        }
//        return null;
//    }

    public static List<String> getIgnoreAuthPatterns() {
        return Collections.unmodifiableList(ignoreAuthPatterns);
    }
}
