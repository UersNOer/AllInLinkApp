package net.vpnsdk.vpn;

/**
 * This class is used to hold the user credentials for login, such as the username, password, certificate path.
 * Developers can use the getter and setter functions to read and write its content.<p>
 * There are three passwords in this class named password, password2, and password3.
 * And we can see three pairs of getter and setter functions for them. <p>
 * The order of the passwords is just the order of InputField objects in the array within a AAAMethod object.
 */
public class VPNAccount {
    final static String method = "method";
    final static String username = "username";
    final static String f1 = "f1";
    final static String f2 = "f2";
    final static String f3 = "f3";
    final static String devid = "devid";
    final static String devname = "devname";
    final static String certpath = "certpath";
    final static String certpass = "certpass";
    private final static String delimeter = ";";
    private final static String kv_delimeter = ":";
    private final static String semicolon = "\"";
    private final static String canceled = "error:\"canceled\"";

    private String mHost;
    private int mPort;
    private String mMethodName;
    private String mUsername;
    private String mPassword;
    private String mPassword2;
    private String mPassword3;
    private String mDeviceId;
    private String mDeviceName;
    private String mCertPath;
    private String mCertPass;
    private boolean mCanceled = false;
    private int mFlag = 0;
    private boolean mEnableUdp = true;
    private boolean mEncryptUdp = true;
    private boolean mVerifySvrCert = true;
    private static int mBasicFlag = 0;
    private static int mDefaultFlag = VpnFlag.VPN_FLAG_NATIVE_L3VPN;

    /**
     * Constructor of VPNAccount.
     */
    public VPNAccount() {
        mFlag = mDefaultFlag;
        mDeviceName = VPNManager.getDeviceName();
    }

    String composeInput() {
        String res = "";

        if (mCanceled) {
            res += canceled;
            return res;
        }
        if ((mMethodName != null) && (mMethodName.length() > 0)) {
            res += method + kv_delimeter + semicolon + getMethodName() + semicolon + delimeter;
        }
        if ((mUsername != null) && (mUsername.length() > 0)) {
            res += username + kv_delimeter + semicolon + getUsername() + semicolon + delimeter;
        }
        if ((mPassword != null) && (mPassword.length() > 0)) {
            res += f1 + kv_delimeter + semicolon + getPassword() + semicolon + delimeter;
        }
        if ((mPassword2 != null) && (mPassword2.length() > 0)) {
            res += f2 + kv_delimeter + semicolon + getPassword2() + semicolon + delimeter;
        }
        if ((mPassword3 != null) && (mPassword3.length() > 0)) {
            res += f3 + kv_delimeter + semicolon + getPassword3() + semicolon + delimeter;
        }
        if ((mDeviceId != null) && (mDeviceId.length() > 0)) {
            res += devid + kv_delimeter + semicolon + getDeviceId() + semicolon + delimeter;
        }
        if ((mDeviceName != null) && (mDeviceName.length() > 0)) {
            res += devname + kv_delimeter + semicolon + getDeviceName() + semicolon + delimeter;
        }
        if ((mCertPath != null) && (mCertPath.length() > 0)) {
            res += certpath + kv_delimeter + semicolon + mCertPath + semicolon + delimeter;
        }
        if ((mCertPass != null) && (mCertPass.length() > 0)) {
            res += certpass + kv_delimeter + semicolon + mCertPass + semicolon + delimeter;
        }

        return res;
    }

    /**
     * Return the name of the authentication method that will be used for authentication.
     *
     * @return the name of the authentication method that will be used for authentication.
     */
    public String getMethodName() {
        return mMethodName;
    }

    /**
     * Set the authentication method that will be used.
     *
     * @param methodName the name of the authentication method.
     */
    public void setMethodName(String methodName) {
        mMethodName = methodName;
    }

    /**
     * Return the username for login.
     *
     * @return a string username for login.
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Set the username for login.
     *
     * @param username a string username for login.
     */
    public void setUsername(String username) {
        mUsername = username;
    }

    /**
     * Return the first password used for login.
     *
     * @return a string password.
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Set the first password used for login.
     *
     * @param password a string password.
     */
    public void setPassword(String password) {
        mPassword = password;
    }

    /**
     * Return the second password used for login.
     *
     * @return a string password.
     */
    public String getPassword2() {
        return mPassword2;
    }

    /**
     * Set the second password used for login.
     *
     * @param password2 a string password.
     */
    public void setPassword2(String password2) {
        mPassword2 = password2;
    }

    /**
     * Return the third password used for login.
     *
     * @return a string password.
     */
    public String getPassword3() {
        return mPassword3;
    }

    /**
     * Set the third password used for login.
     *
     * @param password3 a string password.
     */
    public void setPassword3(String password3) {
        mPassword3 = password3;
    }

    String getDeviceId() {
        return mDeviceId;
    }

    void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    /**
     * Return the device name used for device registration.
     *
     * @return a string value of device name.
     */
    public String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Set the device name used for device registration.
     *
     * @param deviceName a string value of device name.
     */
    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    /**
     * Return the absolute path of the .p12/.pfx certificate file used for authentication.
     *
     * @return a string value of file path.
     */
    public String getCertPath() {
        return mCertPath;
    }

    /**
     * Set the absolute path of the .p12/.pfx certificate file used for authentication.
     *
     * @param certpath a string value of file path.
     */
    public void setCertPath(String certpath) {
        mCertPath = certpath;
    }

    /**
     * Return the password for extracting the .p12/.pfx certificate file.
     *
     * @return a string value of password.
     */
    public String getCertPass() {
        return mCertPass;
    }

    /**
     * Set the password for extracting the .p12/.pfx certificate file.
     *
     * @param certpass a string value of password.
     */
    public void setCertPass(String certpass) {
        mCertPass = certpass;
    }

    /**
     * Return true if the current login or device registration process will be cancelled, otherwise, return false.
     *
     * @return true if the current login or device registration process will be cancelled, otherwise, return false.
     */
    public boolean getCanceled() {
        return mCanceled;
    }

    /**
     * Set whether to cancel the current login or device registration process.
     *
     * @param canceled true to cancel the current login or device registration process.
     */
    public void setCanceled(boolean canceled) {
        mCanceled = canceled;
    }

    /**
     * Return the current host of the VPN server.
     *
     * @return a string value of host.
     */
    public String getHost() {
        return mHost;
    }

    /**
     * Set the host of the VPN server.
     *
     * @param host a string value of host.
     */
    public void setHost(String host) {
        this.mHost = host;
    }

    /**
     * Return the port of the VPN server.
     *
     * @return an integer value of port.
     */
    public int getPort() {
        return mPort;
    }

    /**
     * Set the port of the VPN server.
     *
     * @param port an integer value of port.
     */
    public void setPort(int port) {
        this.mPort = port;
    }

    /**
     * Return the flag used to start the VPN.
     *
     * @return an integer value of flag.
     */
    public int getFlag() {
        return mFlag;
    }

    /**
     * Set the flag used to start the VPN. <p>
     * Use the value <p>
     * VpnFlag.VPN_FLAG_HTTP_PROXY to start HTTP proxy, <p>
     * VpnFlag.VPN_FLAG_SOCK_PROXY to start socket proxy, and <p>
     * VpnFlag.VPN_FLAG_HTTP_PROXY | VpnFlag.VPN_FLAG_SOCK_PROXY to start both.
     *
     * @param flag an integer value of flag
     */
    public void setFlag(int flag) {
        mFlag = flag | mBasicFlag;
    }

    static int getBasicFlag() {
        return mBasicFlag;
    }

    static int getDefaultFlag() {
        return mDefaultFlag;
    }

    boolean isEnableUdp() {
        return mEnableUdp;
    }

    void setEnableUdp(boolean enableUdp) {
        mEnableUdp = enableUdp;
    }

    boolean isEncryptUdp() {
        return mEncryptUdp;
    }

    void setEncryptUdp(boolean encryptUdp) {
        mEncryptUdp = encryptUdp;
    }

    public void setVerifySvrCert(boolean verifySvrCert) {
        mVerifySvrCert = verifySvrCert;
    }

    boolean getVerifySvrCert() {
        return mVerifySvrCert;
    }

    void clear() {
        mUsername = "";
        mDeviceId = "";
        mDeviceName = "";
        mPassword = "";
        mPassword2 = "";
        mPassword3 = "";
        mMethodName = "";
        mCanceled = false;
        mPort = 0;
    }
}