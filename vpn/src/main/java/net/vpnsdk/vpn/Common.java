package net.vpnsdk.vpn;

/**
 * This class defines VPN constants.
 */
public abstract class Common {

    static VpnErrorInfo VpnErrorInfoArray[] = new VpnErrorInfo[]{
            new VpnErrorInfo(
                    VpnError.ERR_SUCCESS,
                    "",
                    ""
            ),

            new VpnErrorInfo(
                    VpnError.ERR_FAILURE,
                    "一般故障",
                    ""
            ),
            new VpnErrorInfo(
                    VpnError.ERR_SSL_READ,
                    "SSL读取失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SSL_WRITE,
                    "SSL写入失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SSL_CONN,
                    "SSL连接失败",
                    "请检查网络连接和您的证书"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SSL_CLIENT_CERT,
                    "服务器需要客户端证书",
                    "请指定一个证书文件"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SESS_INVALID,
                    "会话无效",
                    "请登录再试一次"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SESS_TIMEOUT,
                    "会话超时",
                    "请登录再试一次"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_MEM_ALLOC,
                    "内存分配失败",
                    "请杀死所有VPNs的进程，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_CREATE,
                    "socket创建失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_ACCEPT,
                    "socket接受失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_RECV,
                    "socket读取失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_CLOSE,
                    "socket关闭失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_BIND,
                    "socket绑定失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_LISTEN,
                    "socket监听失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_SEND,
                    "socket发送失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_CONN,
                    "socket连接失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_ITEM_NOT_FOUND,
                    "对象无法找到",
                    "对象无法找到"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_PARSE_COOKIE,
                    "解析cookie失败",
                    "解析cookie失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HTTP_NOT_REDIR,
                    "非http重定向",
                    "非http重定向"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HTTP_NO_LOCATION,
                    "HTTP头部没有location",
                    "HTTP头部没有location"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CONTROL_SOCK_BIND,
                    "绑定控制socket失败",
                    "请杀死所有VPN进程，再试一次"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_APP_CONN_LIMIT,
                    "超出应用程序限制",
                    "请请检查应用程序数目"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_BUF_SIZE,
                    "缓冲区大小过小",
                    "缓冲区大小过小"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_UNKNOWN_HOST,
                    "未知的主机",
                    "请检查主机名"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_STILL_RUNNING,
                    "进程或线程仍在运行",
                    "进程或线程仍在运行"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_NOT_RUNNING,
                    "没有正在运行的进程或线程",
                    "没有正在运行的进程或线程"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_THREAD_JOIN,
                    "加入线程失败",
                    "请杀死所有VPN的进程，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_THREAD_CREATE,
                    "创建线程失败",
                    "请杀死所有VPN的进程，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_SELECT,
                    "socket选择失败",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SERVER_CONFIG,
                    "服务器配置无效",
                    "请检查服务器配置"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SESS_SECOND_LOGIN,
                    "同一用户已从另一台计算机登录",
                    "请从所有计算机注销，再试一次"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_PASS,
                    "证书密码错误",
                    "请核查证书密码"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_PARSE,
                    "解析证书文件失败",
                    "请检查您的证书文件及权限"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_FILE_READ,
                    "读取证书文件失败",
                    "请检查您的证书文件及权限"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CTL_SOCK_CONN,
                    "连接控制器失败",
                    "请杀死所有VPN的进程，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CTL_SOCK_SEND,
                    "发送控制器失败",
                    "请杀死所有VPN的进程，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_WRONG_USER_PASS,
                    "登录失败",
                    "请检查您的用户名和密码"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_GET_AAA_METHOD,
                    "获取AAA方式失败",
                    "请检查您的虚拟站点配置或网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CONFIG_L3VPN,
                    "配置L3VPN通道失败，请确保您拥有VPN权限",
                    "配置L3VPN通道失败，请确保您拥有VPN权限或停止应用程序，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_GET_L3VPN_CONFIG,
                    "获取L3VPN的配置失败",
                    "请检查您的虚拟站点配置或网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_INTERRUPTED,
                    "",
                    ""
            ),

            new VpnErrorInfo(
                    VpnError.ERR_PARAM_INVALID,
                    "参数无效",
                    "参数无效"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_TUN_CREATE,
                    "创建tun设备失败",
                    "创建tun设备失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_TUN_CONFIG,
                    "配置tun设备失败",
                    "配置tun设备失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SETUP_UDP,
                    "设置速度通道失败",
                    "设置速度通道失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_UDP_SEND,
                    "无法发送udp数据包",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_UDP_RECV,
                    "未能接收udp数据包",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_TUN_DOWN,
                    "用户关闭vpn, tun设备停机",
                    "用户关闭vpn, tun设备停机"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SERVER_AG,
                    "不支持的vpn服务器",
                    "请联系您的IT管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SERVER_NO_RESP,
                    "服务器无响应",
                    "请检查网络连接"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CLIENT_SECURITY,
                    "不支持客户端的安全性",
                    "请联系您的IT管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CHOOSE_METHOD,
                    "认证方法过多",
                    "请联系您的IT管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_UNSUPPORT_METHOD,
                    "认证方法不受支持",
                    "请联系您的IT管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SHUTDOWN_SOCKET,
                    "关闭socket失败",
                    "关闭socket失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HTTP_PROXY_START_FAILED,
                    "http代理启动失败",
                    "http代理无法启动"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_PROXY_START_FAILED,
                    "socket代理启动失败",
                    "socket代理无法启动"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HTTP_PROXY_STOP_FAILED,
                    "http代理停止失败",
                    "http代理无法停止"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SOCK_PROXY_STOP_FAILED,
                    "socket代理停止失败",
                    "socket代理无法停止"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HTTP_SOCK_PROXY_STOP_FAILED,
                    "代理停止失败",
                    "http代理和socket代理无法停止"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CALLBACK_FAILED,
                    "上层回调失败",
                    "请检查您的输入内容"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_DEVID_APPROVE_PENDING,
                    "您的设备尚未获得批准",
                    "请联系您的管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_DEVID_APPROVE_DENY,
                    "您的设备被拒绝",
                    "请联系您的管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_DEVID_USER_LIMIT,
                    "可以使用该设备的用户数已达到上限",
                    "请联系您的管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_DEVID_DEV_LIMIT,
                    "用户设备数已经达到上限",
                    "请联系您的管理员"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_DEVID_UNREG,
                    "您的设备尚未注册",
                    "请注册"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CREATE_SESSION,
                    "会话创建失败",
                    "会话创建失败"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_NO,
                    "证书错误",
                    "请选择证书"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_INVALID_SIGNTURE,
                    "证书错误",
                    "客户端的证书具有无效的签名"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_UNTRUSTED,
                    "证书错误",
                    "客户端证书不受信任"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_EXPIRED,
                    "证书错误",
                    "客户端证书过期"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_INVALID,
                    "证书错误",
                    "客户端证书无效"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_REVOKED,
                    "证书错误",
                    "客户端证书被吊销"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_NO,
                    "证书错误",
                    "服务器未提供证书"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_INVALID_SIGNTURE,
                    "证书错误",
                    "服务器的证书具有无效的签名"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_UNTRUSTED,
                    "证书错误r",
                    "服务器的证书不受信任"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_EXPIRED,
                    "证书错误",
                    "服务器的证书过期"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_INVALID,
                    "证书错误",
                    "服务器的证书无效"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CERT_SERVER_REVOKED,
                    "证书错误",
                    "服务器的证书被吊销"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CHANGE_PASSWORD,
                    "密码修改失败",
                    "请检查您输入的密码是否符合要求"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_AUTHORIZE_FAILED,
                    "授权失败",
                    "请与您的管理员联系以获取有效身份"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CONNECT_TIMEOUT,
                    "连接超时",
                    "请检查您的网络，然后重试"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_PROXY_AUTH,
                    "代理服务器认证失败",
                    "请检查您的代理服务器设置"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SMS_GET_PHONE,
                    "短信错误",
                    "系统无法获取您的电话号码进行OTP身份验证"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SMS_SEND,
                    "短信错误",
                    "系统无法向您的手机发送消息"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_SMS_TOO_MANY_RETRY,
                    "短信错误",
                    "您多次输入错误的短信验证码"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_IPC,
                    "进程通信错误",
                    "请重新安装VPN应用"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HWID_DENY,
                    "错误",
                    "硬件ID被拒绝，请联系管理员!"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_HWID_PENDING,
                    "错误",
                    "硬件ID需要批准，请联系管理员!"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_CS_DOWNLOAD,
                    "ERR_CS_DOWNLOAD",
                    "ERR_CS_DOWNLOAD"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_FILE_WRITE,
                    "ERR_FILE_WRITE",
                    "ERR_FILE_WRITE"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_FILE_OPEN,
                    "ERR_FILE_OPEN",
                    "ERR_FILE_OPEN"
            ),

            new VpnErrorInfo(
                    VpnError.ERR_FILE_READ,
                    "ERR_FILE_READ",
                    "ERR_FILE_READ"
            ),

            new VpnErrorInfo(VpnError.ERR_DD_CLIENT_VERIFY_FAIL, "", ""),
            new VpnErrorInfo(VpnError.ERR_CLIENT_SECURITY_FAIL, "", ""),
            new VpnErrorInfo(VpnError.ERR_CLIENT_NEED_UPDATE, "", ""),
            new VpnErrorInfo(VpnError.ERR_HTTP_NO_POST_URL, "", ""),
            new VpnErrorInfo(VpnError.ERR_HTTP_NO_REALM, "", ""),
            new VpnErrorInfo(VpnError.ERR_GET_LOGIN_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_POST_START_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_HTTP_NO_CSRF, "", ""),
            new VpnErrorInfo(VpnError.ERR_HTTP_NO_DYNAMIC_DATA, "", ""),
            new VpnErrorInfo(VpnError.ERR_GET_GRID_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_POST_GRID_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_GET_BASE64_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_HTTP_NO_SECGRID, "", ""),
            new VpnErrorInfo(VpnError.ERR_DECODE_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_POST_SECGRID_RESP_FAILED, "", ""),
            new VpnErrorInfo(VpnError.ERR_NOT_SYFERLOCK_AUTH_SITE, "", ""),
            new VpnErrorInfo(VpnError.ERR_NAME_ADDR_NUM_MISMATCH, "", ""),
            new VpnErrorInfo(VpnError.ERR_INVALID_IP_ADDRESS, "", ""),
            new VpnErrorInfo(VpnError.ERR_INVALID_DOMAIN_NAME, "", ""),

            new VpnErrorInfo(
                    VpnError.ERR_MAX,
                    "此错误不存在",
                    ""
            ),
    };

    private Common() {

    }

    /**
     * The messages in the VPN life cycle.
     */
    public final static class VpnMsg {
        /**
         * The client is connecting to a server.
         */
        public static final int MSG_VPN_CONNECTING = 1;
        /**
         * Log in successfully and the VPN tunnel is established.
         */
        public static final int MSG_VPN_CONNECTED = 2;
        /**
         * The client is disconnecting from a server.
         */
        public static final int MSG_VPN_DISCONNECTING = 3;
        /**
         * The session is terminated and the VPN tunnel is disconnected.
         */
        public static final int MSG_VPN_DISCONNECTED = 4;
        /**
         * The client fails to log in.
         */
        public static final int MSG_VPN_CONNECT_FAILED = 5;
        /**
         * The client is disconnected and is reconnecting to the server automatically.
         */
        public static final int MSG_VPN_RECONNECTING = 7;
        /**
         * A user needs to input credentials.
         */
        public static final int MSG_VPN_LOGIN = 9;
        /**
         * A user needs to input credentials and a device name for device registration.
         */
        public static final int MSG_VPN_DEVREG = 10;
        /**
         * This string is used as a key to get the error code when login failed.<p>
         * int errCode = msg.getData().getInt(VpnMsg.MSG_VPN_ERROR_CODE);
         */
        public static final String MSG_VPN_ERROR_CODE = "error";
    }

    /**
     * The status of the VPN client.
     */
    public final static class VpnStatus {
        /**
         * The VPN is idle.
         */
        public final static int IDLE = 0;
        /**
         * The client is connecting to a server.
         */
        public final static int CONNECTING = 1;
        /**
         * Log in successfully and the VPN tunnel is established.
         */
        public final static int CONNECTED = 2;
        /**
         * The client is disconnecting from a server.
         */
        public final static int DISCONNECTING = 3;
        /**
         * The client is disconnected and is reconnecting to the server automatically.
         */
        public final static int RECONNECTING = 4;
    }

    /**
     * The log levels supported by this SDK.
     */
    public final static class LogLevel {
        /**
         * Output the debug level logs to android logcat.
         */
        public final static int LOG_DEBUG = 3;
        /**
         * Output the info level logs to android logcat.
         */
        public final static int LOG_INFO = 4;
        /**
         * Output the warning level logs to android logcat.
         */
        public final static int LOG_WARNING = 5;
        /**
         * Output the error level logs to android logcat.
         */
        public final static int LOG_ERROR = 6;
    }

    /**
     * This class defines the error codes that corresponds to login failure.<p>
     * Developers can get an error code by calling the following function When the VPN message MSG_VPN_CONNECT_FAILED is received by a program.
     * <pre>
     * {@code
     * int error = msg.getData().getInt(VpnMsg.MSG_VPN_ERROR_CODE);
     * }
     * </pre>
     */
    public final static class VpnError {
        /**
         * The operation succeed.
         */
        public final static int ERR_SUCCESS = 0; // success
        /**
         * The operation failed.
         */
        public final static int ERR_FAILURE = 1;
        /**
         * SSL read failed.
         */
        public final static int ERR_SSL_READ = 2;
        /**
         * SSL write failed.
         */
        public final static int ERR_SSL_WRITE = 3;
        /**
         * SSL connection failed.
         */
        public final static int ERR_SSL_CONN = 4;
        /**
         * Reserved for future.
         */
        public final static int ERR_SSL_CLIENT_CERT = 5;
        /**
         * Session invalid, the client should log in again.
         */
        public final static int ERR_SESS_INVALID = 6; // session is invalid
        /**
         * Session timeout, the client should log in again.
         */
        public final static int ERR_SESS_TIMEOUT = 7; // session is timeout
        /**
         * The system failed to allocate memory. Please kill all VPN' process and try again.
         */
        public final static int ERR_MEM_ALLOC = 8;
        /**
         * Failed to create socket, please check network connection.
         */
        public final static int ERR_SOCK_CREATE = 9;
        /**
         * Socket accept failed, please check network connection.
         */
        public final static int ERR_SOCK_ACCEPT = 10;
        /**
         * A socket failed to receive packets, please check network connection.
         */
        public final static int ERR_SOCK_RECV = 11;
        /**
         * A socket cannot be closed, please check network connection.
         */
        public final static int ERR_SOCK_CLOSE = 12;
        /**
         * Socket bind failed, please check network connection.
         */
        public final static int ERR_SOCK_BIND = 13;
        /**
         * A socket failed to listen, please check network connection.
         */
        public final static int ERR_SOCK_LISTEN = 14;
        /**
         * A socket failed to send packets, please check network connection.
         */
        public final static int ERR_SOCK_SEND = 15;
        /**
         * Socket connection failed, please check network connection.
         */
        public final static int ERR_SOCK_CONN = 16;
        /**
         * Reserved for future.
         */
        public final static int ERR_ITEM_NOT_FOUND = 17;
        /**
         * Reserved for future.
         */
        public final static int ERR_PARSE_COOKIE = 18;
        /**
         * Reserved for future.
         */
        public final static int ERR_HTTP_NOT_REDIR = 19;
        /**
         * Reserved for future.
         */
        public final static int ERR_HTTP_NO_LOCATION = 20;
        /**
         * Reserved for future.
         */
        public final static int ERR_CONTROL_SOCK_BIND = 21;
        /**
         * Reserved for future.
         */
        public final static int ERR_APP_CONN_LIMIT = 22; // too many connections
        /**
         * Reserved for future.
         */
        public final static int ERR_BUF_SIZE = 23;
        /**
         * Unknown host.
         */
        public final static int ERR_UNKNOWN_HOST = 24;
        /**
         * The process or thread is still running and cannot be started again.
         */
        public final static int ERR_STILL_RUNNING = 25; // still running, don't start again
        /**
         * The process or thread is not running, so there's no need to stop it.
         */
        public final static int ERR_NOT_RUNNING = 26; // not running, don't stop
        /**
         * Failed to join thread. Please kill all VPN' process and try again.
         */
        public final static int ERR_THREAD_JOIN = 27;
        /**
         * Failed to create thread. Please kill all VPN' process and try again.
         */
        public final static int ERR_THREAD_CREATE = 28;
        /**
         * Failed to select socket, please check the network connection.
         */
        public final static int ERR_SOCK_SELECT = 29;
        /**
         * The server configuration is invalid.
         */
        public final static int ERR_SERVER_CONFIG = 30;
        /**
         * Reserved for future.
         */
        public final static int ERR_SESS_SECOND_LOGIN = 31;
        /**
         * The password of certificate is wrong.
         */
        public final static int ERR_CERT_PASS = 32;
        /**
         * Failed to parse certificate file.
         */
        public final static int ERR_CERT_PARSE = 33;
        /**
         * Failed to read certificate file.
         */
        public final static int ERR_CERT_FILE_READ = 34;
        /**
         * Failed to connect to control socket. Please kill all VPN' process and try again..
         */
        public final static int ERR_CTL_SOCK_CONN = 35;
        /**
         * Failed to send to control socket. Please kill all VPN' process and try again.
         */
        public final static int ERR_CTL_SOCK_SEND = 36;
        /**
         * The username or password is wrong.
         */
        public final static int ERR_WRONG_USER_PASS = 37;
        /**
         * Failed to get authentication methods from the server.
         */
        public final static int ERR_GET_AAA_METHOD = 38;
        /**
         * Failed to configure L3VPN.
         */
        public final static int ERR_CONFIG_L3VPN = 39;
        /**
         * Failed to get L3VPN configuration.
         */
        public final static int ERR_GET_L3VPN_CONFIG = 40;
        /**
         * The login or device registration is cancelled by a user.
         */
        public final static int ERR_INTERRUPTED = 41;
        /**
         * Reserved for future.
         */
        public final static int ERR_PARAM_INVALID = 42;
        /**
         * Failed to create tun device.
         */
        public final static int ERR_TUN_CREATE = 43;
        /**
         * Failed to configure tun device.
         */
        public final static int ERR_TUN_CONFIG = 44;
        /**
         * Failed to setup speed tunnel.
         */
        public final static int ERR_SETUP_UDP = 45;
        /**
         * Failed to send udp packets.
         */
        public final static int ERR_UDP_SEND = 46;
        /**
         * Failed to receive udp packets.
         */
        public final static int ERR_UDP_RECV = 47;
        /**
         * Tun device is down.
         */
        public final static int ERR_TUN_DOWN = 48;
        /**
         * The vpn server is not supported.
         */
        public final static int ERR_SERVER_AG = 49;
        /**
         * No response from server.
         */
        public final static int ERR_SERVER_NO_RESP = 50;
        /**
         * Client Security is not supported.
         */
        public final static int ERR_CLIENT_SECURITY = 51;
        /**
         * Reserved for future.
         */
        public final static int ERR_CHOOSE_METHOD = 52;
        /**
         * The Authentication method is not supported.
         */
        public final static int ERR_UNSUPPORT_METHOD = 53;
        /**
         * Failed to shut down socket.
         */
        public final static int ERR_SHUTDOWN_SOCKET = 54;
        /**
         * Failed to start HTTP proxy.
         */
        public final static int ERR_HTTP_PROXY_START_FAILED = 55;
        /**
         * Failed to start socket proxy.
         */
        public final static int ERR_SOCK_PROXY_START_FAILED = 56;
        /**
         * Failed to stop HTTP proxy.
         */
        public final static int ERR_HTTP_PROXY_STOP_FAILED = 57;
        /**
         * Failed to stop socket proxy.
         */
        public final static int ERR_SOCK_PROXY_STOP_FAILED = 58;
        /**
         * Both HTTP proxy and socket proxy failed to stop.
         */
        public final static int ERR_HTTP_SOCK_PROXY_STOP_FAILED = 59;
        /**
         * Reserved for future.
         */
        public final static int ERR_CALLBACK_FAILED = 60;
        /**
         * Your device is not approved.
         */
        public final static int ERR_DEVID_APPROVE_PENDING = 61;
        /**
         * The device is denied for registration.
         */
        public final static int ERR_DEVID_APPROVE_DENY = 62;
        /**
         * The number of users who can use the device has reached the upper limit
         */
        public final static int ERR_DEVID_USER_LIMIT = 63;
        /**
         * The user's device number has reached the upper limit.
         */
        public final static int ERR_DEVID_DEV_LIMIT = 64;
        /**
         * The device is not registered.
         */
        public final static int ERR_DEVID_UNREG = 65;
        /**
         * Session creation failed.
         */
        public final static int ERR_CREATE_SESSION = 66;
        /**
         * The client failed to provide a certificate to the server.
         */
        public final static int ERR_CERT_NO = 67;
        /**
         * The certificate of client has an invalid signature.
         */
        public final static int ERR_CERT_INVALID_SIGNTURE = 68;
        /**
         * The certificate of client is untrusted.
         */
        public final static int ERR_CERT_UNTRUSTED = 69;
        /**
         * The certificate of client is expired.
         */
        public final static int ERR_CERT_EXPIRED = 70;
        /**
         * The certificate of client is invalid.
         */
        public final static int ERR_CERT_INVALID = 71;
        /**
         * The certificate of client is revoked.
         */
        public final static int ERR_CERT_REVOKED = 72;
        /**
         * The server does not provide a certificate.
         */
        public final static int ERR_CERT_SERVER_NO = 73;
        /**
         * The certificate of the server has an invalid signature.
         */
        public final static int ERR_CERT_SERVER_INVALID_SIGNTURE = 74;
        /**
         * The certificate of the server is untrusted.
         */
        public final static int ERR_CERT_SERVER_UNTRUSTED = 75;
        /**
         * The certificate of the server is expired.
         */
        public final static int ERR_CERT_SERVER_EXPIRED = 76;
        /**
         * The certificate of the server is invalid.
         */
        public final static int ERR_CERT_SERVER_INVALID = 77;
        /**
         * The certificate of the server is revoked.
         */
        public final static int ERR_CERT_SERVER_REVOKED = 78;
        /**
         * Reserved for future.
         */
        public final static int ERR_CHANGE_PASSWORD = 79;
        /**
         * Authorization failed.
         */
        public final static int ERR_AUTHORIZE_FAILED = 80;

        public final static int ERR_CONNECT_TIMEOUT = 81;
        public final static int ERR_PROXY_AUTH = 82;
        public final static int ERR_SMS_GET_PHONE = 83;
        public final static int ERR_SMS_SEND = 84;
        public final static int ERR_SMS_TOO_MANY_RETRY = 85;
        public final static int ERR_IPC = 86;
        public final static int ERR_HWID_DENY = 87;
        public final static int ERR_HWID_PENDING = 88;
        public final static int ERR_CS_DOWNLOAD = 89;
        public final static int ERR_FILE_WRITE = 90;
        public final static int ERR_FILE_OPEN = 91;
        public final static int ERR_FILE_READ = 92;
        public final static int ERR_DD_CLIENT_VERIFY_FAIL = 93;
        public final static int ERR_CLIENT_SECURITY_FAIL = 94;
        public final static int ERR_CLIENT_NEED_UPDATE = 95;
        public final static int ERR_HTTP_NO_POST_URL = 96;
        public final static int ERR_HTTP_NO_REALM = 97;
        public final static int ERR_GET_LOGIN_RESP_FAILED = 98;
        public final static int ERR_POST_START_RESP_FAILED = 99;
        public final static int ERR_HTTP_NO_CSRF = 100;
        public final static int ERR_HTTP_NO_DYNAMIC_DATA = 101;
        public final static int ERR_GET_GRID_RESP_FAILED = 102;
        public final static int ERR_POST_GRID_RESP_FAILED = 103;
        public final static int ERR_GET_BASE64_RESP_FAILED = 104;
        public final static int ERR_HTTP_NO_SECGRID = 105;
        public final static int ERR_DECODE_FAILED = 106;
        public final static int ERR_POST_SECGRID_RESP_FAILED = 107;
        public final static int ERR_NOT_SYFERLOCK_AUTH_SITE = 108;
        public final static int ERR_NAME_ADDR_NUM_MISMATCH = 109;
        public final static int ERR_INVALID_IP_ADDRESS = 110;
        public final static int ERR_INVALID_DOMAIN_NAME = 111;

        /**
         * This error doesn't exist.
         */
        public final static int ERR_MAX = 112;
    }

    static class VpnErrorInfo {
        public int ErrorCode;
        public String Information;
        public String Suggestion;

        public VpnErrorInfo(int err, String msg, String sugg) {
            ErrorCode = err;
            Information = msg;
            Suggestion = sugg;
        }
    }

    final class DeviceIDType {
        public final static int DEV_DEVICE = 1;
        public final static int DEV_SIM = 2;
        public final static int DEV_SIM_AND_DEVICE = 3;
    }

    ;

    final class AppType {
        public final static int APP_TYPE_TCP = 1;
        public final static int APP_TYPE_RDP = 2;
        public final static int APP_TYPE_HTTP = 3;
        public final static int APP_TYPE_UDP = 4;
        public final static int APP_TYPE_SIP_UDP = 5;
    }

}
