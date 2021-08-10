package net.vpnsdk.vpn;

final class NativeLib {
    public static final int VPN_RESOURCE_EXCLUDEDCLIENTSUBNET = 0x00000002;

    public static native int start(String host, int port, String alias,
                                   String method, String user, String pass, String pass2,
                                   String pass3, String session, String validcode, String devid, String devname,
                                   String certpath, String certpass, String jcertdata,
                                   int jcertdata_len, boolean udp, boolean encrypt, int disp,
                                   int reconn_count, int reconn_max_time, int flags, String ext2, String client_info, Object obj);

    public static int start(String host, int port, String alias, String user,
                            String pass, String devid, boolean udp, boolean encrypt, int disp,
                            int reconn_count, int reconn_max_time, int flags, Object obj) {

        return start(host, port, alias, null, user, pass, null, null, null,
                null, devid, "", null, null, null, 0, udp, encrypt, disp,
                reconn_count, reconn_max_time, flags, "", "", obj);
    }

    public static native int start2(String host, int port, String alias,
                                    String method, String user, String pass, String pass2,
                                    String pass3, String session, String validcode, String devid, String devname, String hardwareid,
                                    String certpath, String certpass, String jcertdata,
                                    int jcertdata_len, boolean udp, boolean encrypt, int disp,
                                    int reconn_count, int reconn_max_time, int svrtype, int flags, int[] timeout_arr, String lastSessionId, String ext2, String client_info, String clientid, Object obj);

    public static native int stop();

    public static native int setLogLevel(int level, int flags);

    public static native int getStatus();

    public static native long[] getStats();

    public static native int[] tcpProxyEntryCreate(String host, int port, int jvport);

    public static native int[] udpProxyEntryCreate(String host, int port);

    public static native int tcpProxyEntryClose(String vip, int vport);

    public static native int udpProxyEntryClose(String vip, int vport);

    public static native int httpProxyGetPort();

    public static native String getSessionCookie();

    public static native String getSessionId();

    public static native String getClientId();

    public static native void logout();

    public static native int initDns(String[] arrHostname, String[] arrAddress);

    public static native void clearDns();

    public static native String[] getIfconfig() throws IllegalArgumentException;

    public static native int getResourceFlags();

    public static native int getServerIP();

    static {
//        System.loadLibrary("vpnsm2"); //RSA & SM2R
        System.loadLibrary("vpn"); // RSA & ECC
    }
}
