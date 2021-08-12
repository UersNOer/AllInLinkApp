package com.example.android_supervisor.http;

import android.content.Context;
import android.text.TextUtils;


import com.example.android_supervisor.Presenter.LoginPresenter;
import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.config.ServiceConfig;
import com.example.android_supervisor.http.common.UrlTranslation;
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
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


class NetInterceptor implements Interceptor {

    private static List<ServiceConfig> srvConfigs;//配置库
    Map<Class<?>, ServiceConfig> serviceConfigMap;
    Context context;

    public NetInterceptor(Context context, List<ServiceConfig> srvConfigs, Map<Class<?>, ServiceConfig> serviceConfigMap) {
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


        //正式环境下
        if (!Environments.isDeBug()) {

            String hostAddress = OauthHostManager.getInstance(context).getApiConfig();
            //  hostAddress  = "https://tykj.cszhx.top";

            if (!TextUtils.isEmpty(hostAddress)) {

                if (serviceConfig != null) {

                    try {
                        Class classs = Class.forName(serviceConfig.getClassName());

                        UrlTranslation urlTranslation = (UrlTranslation) classs.getAnnotation(UrlTranslation.class);
                        String prefix = urlTranslation != null ? urlTranslation.value() : "";


                        HttpUrl baseUrl = HttpUrl.parse(hostAddress);

                        //水印上传不上去，换上新的api地址
//                        if (oldHttpUrl.toString().contains("settingWatermarks/uploadWaterMark")) {
//                            //从老的构建 baseUrl 服务器地址 已被替换
//                            if (!TextUtils.isEmpty(UserSession.getFileServer(context))){
//
//                                HttpUrl fileUrl = HttpUrl.parse(UserSession.getFileServer(context));
//
//                                baseUrl = baseUrl.newBuilder()
//                                        .scheme(fileUrl.scheme())
//                                        .host(fileUrl.host())
//                                        .port(fileUrl.port())
//                                        .build();
//                            }
//                        }


//                        if (serviceConfig.getClassName() == FileService.class.getName()){
//                            baseUrl = HttpUrl.parse(TextUtils.isEmpty(UserSession.getFileServer(context))?
//                                            hostAddress:UserSession.getFileServer(context).contains("192.168")?hostAddress:UserSession.getFileServer(context)
//                                    ) ;
//                            if (UserSession.getFileServer(context).contains("192.168")){
//                               UserSession.setFileServer(context,hostAddress+"/files/");
//                            }
//                        }


                        //  decideCaseCheckUrl(baseUrl,request.method(), oldHttpUrl.toString().endsWith("addEvent"));//判断

                        HttpUrl.Builder newBuilder = new HttpUrl.Builder();
                        newBuilder.scheme(baseUrl.scheme());
                        newBuilder.host(baseUrl.host());
                        newBuilder.port(baseUrl.port());

                        String temp = baseUrl.toString().replace(newBuilder.toString(), "");
                        temp = temp.replace(temp.lastIndexOf("/") > 0 ? "/" : "", "");

                        //注： 此处/ 转为%2f
                        if (!TextUtils.isEmpty(temp) && temp.length() > 0) {
                            newBuilder.addEncodedPathSegment(URLEncoder.encode(temp, "UTF-8"));
                        }

                        newBuilder.addEncodedPathSegment(URLEncoder.encode(prefix, "UTF-8"));
                        List<String> urls = oldHttpUrl.pathSegments();
                        if (urls != null) {
                            for (String url : urls) {
                                if (url.contains("")){
                                    newBuilder.addEncodedPathSegment(url);
                                }

                            }
                        }
                        Set<String> set = oldHttpUrl.queryParameterNames();
                        if (set != null) {
                            for (String names : set) {
                                List<String> values = oldHttpUrl.queryParameterValues(names);
                                for (String value : values) {
                                    newBuilder.addEncodedQueryParameter(names, value);
                                }
                            }
                        }
                        System.out.println("重新构造:" + newBuilder.build().toString());
//
//                        HttpUrl newFullUrl =urlBulder.build();
//                        System.out.println("newHttpUrl:" + newFullUrl.toString());
//                        check401(chain,builder.url(newBuilder.build().toString()).build());

                        //登录失败也返回401  --待测试
                        Response response = chain.proceed(builder.url(newBuilder.build().toString()).build());

                        if ((response.code() == 401 || response.code() == 500) && !newBuilder.build().toString().contains("oauthApi/oauth/token")) {

                            // 这里应该调用自己的刷新token的接口  同步
                            // 这里发起的请求是同步的，刷新完成token后再增加到header中
                            // 这里抛出的错误会直接回调 onError
                            LogUtils.d("freshen_access_token"+ response.code());
                            String accessToken = new LoginPresenter().refreshToken(AppContext.getInstance());
                            if (!TextUtils.isEmpty(accessToken)){

                                Request retryRequest = builder.url(newBuilder.build().toString()).build();
                                LogUtils.d("freshen_access_token"+ "再次发起请求");
                                // 再次发起请求
                                return chain.proceed(retryRequest);
                            }
                        }else {
                            return response;
                        }


                        return chain.proceed(builder.url(newBuilder.build().toString()).build());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    return chain.proceed(request);
                }
            } else {
                return chain.proceed(request);
            }

        }

        //开发环境下
//        ServiceConfig serviceConfig = serviceConfigMap.get(mCurrentService);//并发出现问题

        if (serviceConfig != null && !TextUtils.isEmpty(serviceConfig.getDomain())) {

            HttpUrl baseUrl = HttpUrl.parse(serviceConfig.getDomain());

            //decideCaseCheckUrl(baseUrl,request.method(), oldHttpUrl.toString().endsWith("addEvent"));

            HttpUrl newHttpUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(baseUrl.scheme())
                    .host(baseUrl.host())
                    .port(baseUrl.port())
                    .build();

            //重建这个request，通过builder.url(newFullUrl).build()；
            System.out.println("newHttpUrl:" + newHttpUrl);

            LogUtils.d("请求方法:" + request.method());
            return chain.proceed(builder.url(newHttpUrl).build());
        } else {
            return chain.proceed(request);
        }
    }

    /*
     *  根据header 注解 匹配对应的领域//匹配对应得服务器地址，根据不同得模块，
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

            case "YqService":
                Class<YqService> yqServiceClass = YqService.class;
                serviceConfig = serviceConfigMap.get(yqServiceClass);
                break;

            case "YqytService":
                Class<YqytService> YqytService = YqytService.class;
                serviceConfig = serviceConfigMap.get(YqytService);
                break;

            case "YqytsService":
                Class<YqytsService> yqytsService = YqytsService.class;
                serviceConfig = serviceConfigMap.get(yqytsService);
                break;
            case "PersonalizedService":
                Class<PersonalizedService> personalizedServiceClass = PersonalizedService.class;
                serviceConfig = serviceConfigMap.get(personalizedServiceClass);
                break;


        }
        return serviceConfig;
    }


    /**
     * 根据老的url请求  匹配对应的领域
     */
    private ServiceConfig matchedService(Request request, HttpUrl oldHttpUrl) {

        System.out.println("请求打印:" + oldHttpUrl.pathSegments());//需要处理
        System.out.println("请求打印:" + oldHttpUrl.queryParameterNames().toString());//需要处理


        ServiceConfig serviceConfig = null;

        StringBuffer stringBuffer = new StringBuffer();

        List<String> pathSegments = oldHttpUrl.pathSegments();
        if (pathSegments != null) {
            for (int i = 0; i < pathSegments.size(); i++) {
                if (i > 0) {
                    stringBuffer.append("/").append(pathSegments.get(i));

                } else {
                    stringBuffer.append(pathSegments.get(i));
                }
            }
        }

        for (ServiceConfig serviceConfig1 : srvConfigs) {
            List<String> tag = serviceConfig1.getLocalTag();
            if (stringBuffer.toString() != null) {
                if (tag.contains(stringBuffer.toString())) {
                    try {
                        serviceConfig = serviceConfigMap.get(Class.forName(serviceConfig1.getClassName()));
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    for (String path : pathSegments) {
                        if (tag.contains(path)) {
                            try {
                                serviceConfig = serviceConfigMap.get(Class.forName(serviceConfig1.getClassName()));
                                break;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
//                    pathSegments.remove(pathSegments.size()-1);
//                    for (int i = 0; i < pathSegments.size(); i++) {
//                        if (i > 0) {
//                            stringBuffer.append("/").append(pathSegments.get(i));
//
//                        } else {
//                            stringBuffer.append(pathSegments.get(i));
//                        }
//                    }
//                    if (tag.contains(stringBuffer.toString())) {
//                        try {
//                            serviceConfig = serviceConfigMap.get(Class.forName(serviceConfig1.getClassName()));
//                            break;
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }

        }
        return serviceConfig;
    }


    public Response check401(Chain chain , Request request) throws IOException {
        //登录失败也返回401
        Response response = chain.proceed(request);
        if (response.code() == 401) {
            // 这里应该调用自己的刷新token的接口
            // 这里发起的请求是同步的，刷新完成token后再增加到header中
            // 这里抛出的错误会直接回调 onError
//                String token = refreshToken();
            String token = Credentials.basic(UserSession.getUserName(context), UserSession.getUserPwd(context), Charset.forName("UTF-8"));
            // 创建新的请求，并增加header
            Request retryRequest = request
                    .newBuilder()
                    .header("Authorization", token)
                    .build();
            // 再次发起请求
            return chain.proceed(retryRequest);
        }
        return null;
    }



}
