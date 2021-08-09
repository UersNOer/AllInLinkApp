package net.vpnsdk.vpn;
public final class VpnFlag {
    /**
     * Start the HTTP proxy
     */
    public static final int VPN_FLAG_HTTP_PROXY = 0x0001;
    /**
     * Start the Socket proxy
     */
    public static final int VPN_FLAG_SOCK_PROXY = 0x0002;

    static final int VPN_FLAG_PROXY_SCOPE_PROCESS = 0x0004;   /* allow clients from the same process*/
    static final int VPN_FLAG_PROXY_SCOPE_LOCALHOST = 0x0008;   /* allow clients from the same localhost */
    static final int VPN_FLAG_PROXY_SCOPE_ALL = 0x0010;   /* allow all clients */
    static final int VPN_FLAG_DEVID_LOGIN = 0x0020;   /* use devid to login */
    static final int VPN_FLAG_MOTIONPRO_V2 = 0x0040;   /* api for motionpro 2.0 */
    static final int VPN_FLAG_LOGOUT_DEV_SESSION = 0x0080;   /* logout device's all session before login */
    static final int VPN_FLAG_MOTIONPRO_V1 = 0x0100;   /* api for motionpro 1.0 */
    public static final int VPN_FLAG_NATIVE_L3VPN = 0x0200;   /* 启动三层 */
    static final int VPN_FLAG_DESKTOP_DIRECT = 0x0400;   /* desktop direct */
    static final int VPN_FLAG_CHANGEPASS_ONLY = 0x0800;   /* only do change password */
    /**
     * Login to the server, but do not start tunnel
     */
    public static final int VPN_FLAG_LOGIN_ONLY = 0x00001000;   /* only do login and get resources */
    static final int VPN_FLAG_TUNEMU = 0x00002000;   /* use tunemu api to handle tun, for max os x */
    static final int VPN_FLAG_SKIP_LOGOUT = 0x00004000;   /* do not logout session before vpn stop */
    static final int VPN_FLAG_L4VPN_TCP_PROXY = 0x00008000;   /* use l4vpn for tcp proxy */
    static final int VPN_FLAG_L4VPN_ONLY = 0x00010000;   /* use l4vpn only, disable l3vpn, only tcp proxy work */
    static final int VPN_FLAG_SYFERLOCK_LOGIN = 0x00020000;   /* use syferlock authentication */
    static final int VPN_FLAG_API_V2 = 0x80000000;   /* internal use */
}