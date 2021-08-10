package com.unistrong.network.provider;


import com.unistrong.network.provider.impl.DefaultNetProvider;

/**
 * Description: NetProviderFactory
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class NetProviderFactory {
    private NetProviderFactory() {
    }

    /**
     * 提供注入
     *
     * @param instance
     */
    public static void setInstance(INetProvider instance) {
        Holder.setInstance(instance);
    }


    private static class Holder {
        private static INetProvider instance = new DefaultNetProvider();

        public static void setInstance(INetProvider instance) {
            Holder.instance = instance;
        }
    }

    public static final INetProvider getInstance() {
        return Holder.instance;
    }

}
