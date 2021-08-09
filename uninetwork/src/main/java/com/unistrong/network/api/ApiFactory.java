package com.unistrong.network.api;


import com.unistrong.network.api.impl.DefaultApi;


/**
 * Description:ApiFactory
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class ApiFactory {
    private ApiFactory() {
    }

    /**
     * 为外部提供注入方法，允许外部去自定义自己的IApi实现类。
     *
     * @param api
     */
    public static void setApi(IApi api) {
        Holder.setInstance(api);
    }

    private static class Holder {
        private static IApi instance = new DefaultApi();

        public static void setInstance(IApi instance) {
            Holder.instance = instance;
        }
    }

    public static IApi getInstance() {
        return Holder.instance;
    }
}
