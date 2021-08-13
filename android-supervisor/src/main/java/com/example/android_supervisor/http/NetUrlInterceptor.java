package com.example.android_supervisor.http;

import android.content.Context;
import android.text.TextUtils;


import com.example.android_supervisor.config.ServiceConfig;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;
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
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


class NetUrlInterceptor implements Interceptor {

    private static List<ServiceConfig> srvConfigs;//配置库
    Map<Class<?>, ServiceConfig> serviceConfigMap;
    Context context;

    public NetUrlInterceptor(Context context, List<ServiceConfig> srvConfigs, Map<Class<?>, ServiceConfig> serviceConfigMap) {
        this.context = context;
        this.srvConfigs = srvConfigs;
        this.serviceConfigMap = serviceConfigMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        ServiceConfig serviceConfig = null;

        Request.Builder builder = request.newBuilder();
        HttpUrl oldHttpUrl = request.url();

        List<String> headerValues = request.headers("apiTerritory");
        if (headerValues != null && headerValues.size() > 0) {
            builder.removeHeader("baseurl");
            String headerValue = headerValues.get(0);
            serviceConfig = matchedService(headerValue);
        }

        if (serviceConfig == null) {
            return chain.proceed(request);
        }

        HttpUrl newHttpUrl = null;
        //正式环境下
        if (!Environments.isDeBug()) {

            String hostAddress = OauthHostManager.getInstance(context).getApiConfig();
            //  hostAddress  = "https://tykj.cszhx.top";

            if (!TextUtils.isEmpty(hostAddress)) {

                HttpUrl baseUrl = HttpUrl.parse(hostAddress);

                 newHttpUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(baseUrl.scheme())
                        .host(baseUrl.host())
                        .port(baseUrl.port())
                        .build();

                System.out.println("newHttpUrl:" + newHttpUrl);

            }
        } else {

            if (serviceConfig != null && !TextUtils.isEmpty(serviceConfig.getDomain())) {

                HttpUrl baseUrl = HttpUrl.parse(serviceConfig.getDomain());

                newHttpUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(baseUrl.scheme())
                        .host(baseUrl.host())
                        .port(baseUrl.port())
                        .build();

                //重建这个request，通过builder.url(newFullUrl).build()；
                System.out.println("newHttpUrl:" + newHttpUrl);

                LogUtils.d("请求方法:" + request.method());

            }

        }
        if (newHttpUrl!=null){
            return chain.proceed(builder.url(newHttpUrl).build());
        }else {
            return chain.proceed(request);
        }


    }

    /*
     *  根据header 注解 匹配对应的领域
     */
    private ServiceConfig matchedService(String headerValue) {

        ServiceConfig serviceConfig = null;
        switch (headerValue) {
            case "BasicService":
                Class<BasicService> basicService = BasicService.class;
                serviceConfig = serviceConfigMap.get(basicService);
                break;

            case "FileService":
                Class<FileService> fileServiceClass = FileService.class;
                serviceConfig = serviceConfigMap.get(fileServiceClass);
                break;


            case "OauthService":
                Class<OauthService> oauthService = OauthService.class;
                serviceConfig = serviceConfigMap.get(oauthService);
                break;

            case "PlatformService":
                Class<PlatformService> platformServiceClass = PlatformService.class;
                serviceConfig = serviceConfigMap.get(platformServiceClass);
                break;

            case "PublicService":
                Class<PublicService> publicServiceClass = PublicService.class;
                serviceConfig = serviceConfigMap.get(publicServiceClass);
                break;

            case "QuestionService":
                Class<QuestionService> questionServiceClass = QuestionService.class;
                serviceConfig = serviceConfigMap.get(questionServiceClass);
                break;

            case "CodeService":
                Class<CodeService> codeService = CodeService.class;
                serviceConfig = serviceConfigMap.get(codeService);
                break;

        }
        return serviceConfig;
    }







//    public Response check401(Chain chain , Request request) throws IOException {
//        //登录失败也返回401
//        Response response = chain.proceed(request);
//        if (response.code() == 401) {
//            // 这里应该调用自己的刷新token的接口
//            // 这里发起的请求是同步的，刷新完成token后再增加到header中
//            // 这里抛出的错误会直接回调 onError
////                String token = refreshToken();
//            String token = Credentials.basic(UserSession.getUserName(context), UserSession.getUserPwd(context), Charset.forName("UTF-8"));
//            // 创建新的请求，并增加header
//            Request retryRequest = request
//                    .newBuilder()
//                    .header("Authorization", token)
//                    .build();
//            // 再次发起请求
//            return chain.proceed(retryRequest);
//        }
//        return null;
//    }



}
