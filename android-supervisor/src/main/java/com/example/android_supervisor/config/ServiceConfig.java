package com.example.android_supervisor.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * @author tj
 */
public class ServiceConfig {
    String name;
    String scheme;
    String domain;
    String prefix;
    String className;

    List<String> localTag;//作为tag使用

    ServiceConfig() {
    }

    public String getName() {
        return name;
    }

    public String getScheme() {
        return scheme;
    }

    public String getDomain() {
        return domain;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getLocalTag() {
        return localTag;
    }

    public void setLocalTag(List<String> localTag) {
        this.localTag = localTag;
    }

    public void setTag(Class<?> classs) {
        List<String> localTag = new ArrayList<>();

        Method[] methods = classs.getDeclaredMethods();
        for (Method method : methods) {
//                if (method.getAnnotation(GET.class)!=null){
//                    localTag.add(method.getAnnotation(GET.class).value())  ;
//                }
            if(method.isAnnotationPresent(GET.class)){

                String[] values = method.getAnnotation(GET.class).value().split("/");

                String value = values[values.length-1];
                if (value.contains("{") && value.contains("}")){
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i=0;i<values.length;i++){
                        if (i<values.length-1){

                            if (i > 0) {
                                stringBuffer.append("/").append(values[i]);

                            } else {
                                stringBuffer.append(values[i]);
                            }

//                            if (i==values.length-2){
//                                stringBuffer.append(value).append("/");
//                            }else {
//                                stringBuffer.append(value);
//                            }
                        }
                    }

                    localTag.add(stringBuffer.toString());
                }else {
                    localTag.add(method.getAnnotation(GET.class).value());
                }


            }else if (method.isAnnotationPresent(POST.class)){
                if (method.getAnnotation(POST.class)!=null){
                    localTag.add(method.getAnnotation(POST.class).value())  ;
                }

            }
            else if (method.isAnnotationPresent(PUT.class)){
                localTag.add(method.getAnnotation(PUT.class).value())  ;
            }
            else if (method.isAnnotationPresent(DELETE.class)){
                localTag.add(method.getAnnotation(DELETE.class).value())  ;
            }
        }
        setLocalTag(localTag);
    }
}
