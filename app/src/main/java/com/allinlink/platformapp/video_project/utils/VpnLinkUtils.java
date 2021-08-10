package com.allinlink.platformapp.video_project.utils;

import android.content.Context;
import android.os.Handler;

import net.vpnsdk.vpn.AAAMethod;
import net.vpnsdk.vpn.Common;
import net.vpnsdk.vpn.VPNManager;
import net.vpnsdk.vpn.VpnFlag;
import net.vpnsdk.vpn.VpnUtils;

public class VpnLinkUtils {
    private static final String Tag = "VpnLinkUtils";
    private static AAAMethod[] mMethods = null;
    private static  VpnUtils vpnUtils;
    public VpnListance vpnListance;

    public VpnLinkUtils setVpnListance(VpnListance vpnListance) {
        this.vpnListance = vpnListance;
        return this;
    }


    public int getVpnState(){
        return vpnUtils.getVpnStatus();
    }
    public interface VpnListance {
        void onSuccess();

        void onFail();
    }

    public static VpnUtils initUtils(Context context, Handler mHandler) {
        if(null==vpnUtils){
            vpnUtils = VpnUtils.getInstance(context, mHandler);
        }
        return vpnUtils;
    }


    public void startLogin(String name, String pwd) {
        if (null != vpnUtils)

            vpnUtils.start("117.159.24.16", 10443, name, pwd, "", "", VpnFlag.VPN_FLAG_NATIVE_L3VPN);
    }


}
