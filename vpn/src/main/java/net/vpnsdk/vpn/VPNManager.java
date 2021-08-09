package net.vpnsdk.vpn;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import net.vpnsdk.vpn.ArrayAuthInfo.AuthType;
import net.vpnsdk.vpn.ArrayAuthInfo.CertIdType;
import net.vpnsdk.vpn.Common.DeviceIDType;
import net.vpnsdk.vpn.Common.VpnError;
import net.vpnsdk.vpn.Common.VpnMsg;
import net.vpnsdk.vpn.Common.VpnStatus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * VPN管理类
 */
public class VPNManager extends NativeCallback {

    private final static String Tag = "VPNManager";

    private ArrayAuthInfo mAuthInfo = ArrayAuthInfo.getArrayAuthInfo("");
    private VPNAccount mAuthInput = new VPNAccount();
    private VPNAccount mRegisterAuthInput = new VPNAccount();
    private boolean mHasLoginAuthInput = false;
    private Handler mHandler;
    private boolean mIsVpnThreadPause = false;
    private Semaphore mSemaphore = new Semaphore(0);
    private Object mMutexObj = new Object();
    private CallbackStatus mCallBackStatus;
    private boolean mNeedDeviceId = false;
    private Context mContext;
    private Context mAppContext;
    private SSLAuthServerCert mAuthServerCert = null;
    private IVpnService mVpnService = null;
    private static VPNManager gVPNManager = null;
    private Thread mThreadStartVpn = null;
    private boolean mStartL3VpnAfterConnection = false;
    private String mL3VpnConfiguration;
    private int mDeviceIdType;
    private boolean mStartingL3Vpn = false;
    final static int VpnRequest = 8888;
    private String mVirtualIP;
    private String mSessionCookie;
    private SharedPreferences mPreferences = null;
    private boolean isLoginOnly = false;
    private int mStatus = VpnStatus.IDLE;
    private final static String LAST_SESSION_ID = "ArrayNetworksLastSessionId";

    /**
     * 初始化 VPNManager class， 返回一个单例
     *
     * @param context Android activity/service context or application context.
     * @return the single instance of VPNManager.
     */
    public static VPNManager initialize(Context context) {
        if (gVPNManager == null) {
            gVPNManager = new VPNManager(context);
        }
        return gVPNManager;
    }

    /**
     * 返回一个VPNManager单例.
     *
     * @return the single instance of VPNManager.
     */
    public static VPNManager getInstance() {
        return gVPNManager;
    }

    private VPNManager(Context context) {
        mContext = context;
        mAppContext = context.getApplicationContext();
        mAuthServerCert = new SSLAuthServerCert();
        mDeviceIdType = DeviceIDType.DEV_DEVICE;
        setLogLevel(Common.LogLevel.LOG_INFO, 0);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void bindVPNServiceAndStartL3VPN(String action, VPNAccount authInput) {
        Log.d(Tag, "bindVPNServiceAndStartL3VPN enter");
        mAuthInput = authInput;
        if (null == mVpnService) {
            Log.d(Tag, "bindVPNServiceAndStartL3VPN , mVpnService is null");
            mStartL3VpnAfterConnection = true;
            Intent serviceIntent = new Intent(action);
            // serviceIntent.setPackage("net.arraynetworks.vpn");
            boolean res = mAppContext.bindService(serviceIntent, mSrvConnection, Context.BIND_AUTO_CREATE);
            //boolean res = mAppContext.bindService(new Intent(action/*IVpnService.class.getName()*/), mSrvConnection, Context.BIND_AUTO_CREATE);
            //boolean res = mAppContext.bindService(new Intent(action/*IVpnService.class.getName()*/), mSrvConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.d(Tag, "bindVPNServiceAndStartL3VPN , start l3vpn");
            startL3Vpn(mAuthInput);
        }
    }

    /**
     * 绑定VPN服务
     *
     * @param action VPN服务的名称，默认是net.arraynetworks.vpn.VPN_SERVICE
     */
    public void bindVPNService(String action) {
        if (action.length() == 0) {
            action = "net.arraynetworks.vpn.VPN_SERVICE";
        }
        if (null == mVpnService) {
            mStartL3VpnAfterConnection = true;

            final Intent intent = new Intent();
            intent.setAction(action);
            intent.setPackage(mAppContext.getPackageName());
            //final Intent eintent = new Intent(createExplicitFromImplicitIntent(this,intent));
            boolean res = mAppContext.bindService(intent,
                    mSrvConnection, Context.BIND_AUTO_CREATE);
        }
        startVpnActivity();
    }

    private void unBindVPNService() {
        if (mVpnService != null) {
            mAppContext.unbindService(mSrvConnection);
        }
    }

    /**
     * Use this method to register the device, and the user input in this method will be authenticated by the AG server.
     * Note: this method must be invoked if the application received the message MSG_VPN_DEVREG.
     *
     * @param method the method the user choose to do device registration.
     */
    public void registerWithMethod(AAAMethod method) {
        continueVPNThreadWithAccount(method, true);
    }

    /**
     * Use this method to log in, and the user input in this method will be authenticated by the AG server.
     * Note: this method must be invoked if the application received the message MSG_VPN_LOGIN.
     *
     * @param method the method the user choose to do authentication.
     */
    public void loginWithMethod(AAAMethod method) {
        continueVPNThreadWithAccount(method, false);
    }

    static String getDeviceName() {
        if ((android.os.Build.MODEL == null) || (android.os.Build.MODEL.length() < 1)) {
            return "unknown";
        }
        return android.os.Build.MODEL;
    }

    public String getHardwareId() {
        return getDeviceId() + "|;|hostname=" + getDeviceName();
    }

    /**
     * 启动VPN
     *
     * @param host     VPN服务器的域名或者IP地址.
     * @param port     VPN服务器的端口，一般是443.
     * @param username 登陆VPN服务器的用户名.
     * @param password 登陆VPN服务器的用户密码.
     * @return 0：成功, 大于0：失败.
     */
    public int startVPN(String host, int port, String username, String password) {
        mAuthInput.clear();
        mAuthInput.setHost(host);
        mAuthInput.setPort(port);
        mAuthInput.setUsername(username);
        mAuthInput.setPassword(password);
//		return NativeLib.start2(host, port, "", "", username, password, "", "",
//				"", "", getDeviceId(), getDeviceName(), "", "", "", 0, true, true,
//				TrafficDispatchRule.AlltoUDP, Integer.MAX_VALUE,
//				Integer.MAX_VALUE, VPNAccount.getDefaultFlag(), this);
        return start2(host, port, "", "",
                username, password, "",
                "", "", "", getDeviceId(), getDeviceName(), getHardwareId(), "",
                "", "", 0, true,
                true, TrafficDispatchRule.AlltoUDP,
                Integer.MAX_VALUE, Integer.MAX_VALUE, VPNAccount.getDefaultFlag(), this);
    }

    /**
     * 启动VPN
     *
     * @param host     VPN服务器的域名或者IP地址.
     * @param port     VPN服务器的端口，一般是443.
     * @param username 登陆VPN服务器的用户名.
     * @param password 登陆VPN服务器的用户密码.
     * @param certpath 证书文件路径，必须是绝对路径（全路径），证书文件需要是p12或pfx格式.
     * @param certpass 证书的密码.
     * @return 0：成功, 大于0：失败.
     */
    public int startVPN(String host, int port, String username,
                        String password, String certpath, String certpass) {
        mAuthInput.clear();
        mAuthInput.setHost(host);
        mAuthInput.setPort(port);
        mAuthInput.setUsername(username);
        mAuthInput.setPassword(password);
//		return NativeLib.start(host, port, "", "", username, password, "", "",
//				"", "", getDeviceId(), getDeviceName(), certpath, certpass, "", 0, true, true,
//				TrafficDispatchRule.AlltoUDP, Integer.MAX_VALUE,
//				Integer.MAX_VALUE, VPNAccount.getDefaultFlag(), this);
        return start2(host, port, "", "",
                username, password, "",
                "", "", "", getDeviceId(), getDeviceName(), getHardwareId(), certpath,
                certpass, "", 0, true,
                true, TrafficDispatchRule.AlltoUDP,
                Integer.MAX_VALUE, Integer.MAX_VALUE, VPNAccount.getDefaultFlag(), this);
    }

    /**
     * 启动VPN
     *
     * @param host     VPN服务器的域名或者IP地址.
     * @param port     VPN服务器的端口，一般是443.
     * @param username 登陆VPN服务器的用户名.
     * @param password 登陆VPN服务器的用户密码.
     * @param certpath 证书文件路径，必须是绝对路径（全路径），证书文件需要是p12或pfx格式.
     * @param certpass 证书的密码.
     * @param flag     VPN的一些控制开关，一般传递VPN_FLAG_NATIVE_L3VPN即可.
     * @return 0：成功, 大于0：失败.
     */
    public int startVPN(String host, int port, String username,
                        String password, String certpath, String certpass, int flag) {
        Log.d(Tag, "startVPN enter");
        mAuthInput.clear();
        mAuthInput.setHost(host);
        mAuthInput.setPort(port);
        mAuthInput.setUsername(username);
        mAuthInput.setPassword(password);
        if (flag == 0) {
            flag = VpnFlag.VPN_FLAG_NATIVE_L3VPN;
        }

        return start2(host, port, "", "",
                username, password, "",
                "", "", "", getDeviceId(), getDeviceName(), getHardwareId(), certpath,
                certpass, "", 0, true,
                true, TrafficDispatchRule.AlltoUDP,
                Integer.MAX_VALUE, Integer.MAX_VALUE, flag | VPNAccount.getBasicFlag(), this);
    }

    /**
     * 启动VPN
     *
     * @param account 与其他startVPN函数用法一样，只不过参数类型是VPNAccount.
     * @return 0：成功, 大于0：失败.
     */
    public int startVPN(VPNAccount account) {
        if (account.getCanceled()) {
            return VpnError.ERR_INTERRUPTED;
        }
        mAuthInput = account;
//		return NativeLib.start(auth.getHost(), auth.getPort(), "", auth.getMethodName(),
//				auth.getUsername(), auth.getPassword(), auth.getPassword2(),
//				auth.getPassword3(), "", "", getDeviceId(), auth.getDeviceName(), auth.getCertPath(),
//				auth.getCertPass(), "", 0, auth.isEnableUdp(),
//				auth.isEncryptUdp(), TrafficDispatchRule.AlltoUDP,
//				Integer.MAX_VALUE, Integer.MAX_VALUE, auth.getFlag(), this);
        return start2(account.getHost(), account.getPort(), "", account.getMethodName(),
                account.getUsername(), account.getPassword(), account.getPassword2(),
                account.getPassword3(), "", "", getDeviceId(), account.getDeviceName(), getHardwareId(), account.getCertPath(),
                account.getCertPass(), "", 0, account.isEnableUdp(),
                account.isEncryptUdp(), TrafficDispatchRule.AlltoUDP,
                Integer.MAX_VALUE, Integer.MAX_VALUE, account.getFlag(), this);
    }

    /**
     * 设置处理VPN消息的Handler.
     *
     * @param handler 处理VPN消息的Handlerl.
     */
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 停止VPN
     *
     * @return 总是返回成功.
     */
    public int stopVPN() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                innerStopVPN();
                mAuthServerCert.stopVerify();
            }
        });
        t.start();

        return VpnError.ERR_SUCCESS;
    }

    private void innerStopVPN() {
        if (mVpnService != null) {
            try {
                mVpnService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        NativeLib.stop();
    }

    /**
     * 获取VPN的当前状态
     *
     * @return VPN的当前状态.
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * Return the number of bytes that sent and received through SDK.
     *
     * @return the number of bytes that sent and received through SDK.
     * long[] bytes = getStats();
     * bytes[0] is received bytes number and
     * bytes[1] is sent bytes number.
     */
    public long[] getStats() {
        return NativeLib.getStats();
    }

    /**
     * 设置日志级别
     *
     * @param level 日志级别，默认值是is Common.LogLevel.LOG_INFO
     * @param flags 0.
     */
    public void setLogLevel(int level, int flags) {
        NativeLib.setLogLevel(level, flags);
    }

    /**
     * Get current session after successful login.
     *
     * @return current session.
     */
    private String getSessionCookie() {
        return mSessionCookie;
    }

    private String getL3VpnResources() {
        return mL3VpnConfiguration;
    }

    private void parseIP(String str) {
        str = str.substring("Assigned IP:".length());
        Scanner scanner = new Scanner(str);
        scanner.useDelimiter("/");
        mVirtualIP = scanner.next().trim();
    }

    private int parseVirtualIP(String cfgString) {
        Scanner scanner = new Scanner(cfgString);
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.startsWith("Assigned IP:")) {
                parseIP(line);
            }
        }

        return 0;
    }

    private String getVirtualIP() {
        return mVirtualIP;
    }

    private String getDeviceId() {
        return getDevid();
    }

    /**
     * Cancel the login during the connecting process.
     */
    public void cancelLogin() {
        if (mIsVpnThreadPause) {
            continueVPNThreadWithAccount(null, false);
        } else {
            stopVPN();
        }
    }

    private void continueVPNThreadWithAccount(AAAMethod method, boolean isRegister) {
        if (method == null) {
            mRegisterAuthInput.setCanceled(true);
            mAuthInput.setCanceled(true);
        } else {
            if (isRegister) {
                mHasLoginAuthInput = true;
                fillVPNAccount(mRegisterAuthInput, method);
            } else {
                fillVPNAccount(mAuthInput, method);
            }
        }
        synchronized (mMutexObj) {
            // Make sure the maximum permits of this semaphore is 1.
            if (mSemaphore.availablePermits() < 1) {
                mSemaphore.release();
                Log.d(Tag,
                        "Semaphore released " + mSemaphore.availablePermits());
            }
        }
    }

    @Override
    int onVpnConnectFailed(int error) {
        Log.d(Tag, "onVpnConnectFailed " + error);
        mStatus = VpnStatus.IDLE;
        if (mHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt(VpnMsg.MSG_VPN_ERROR_CODE, error);
            msg.setData(bundle);
            msg.what = VpnMsg.MSG_VPN_CONNECT_FAILED;
            mHandler.sendMessage(msg);
        }
        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnConnected() {
        Log.d(Tag, "onVpnConnected ");
        mStatus = VpnStatus.CONNECTED;
        mSessionCookie = NativeLib.getSessionCookie();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LAST_SESSION_ID, NativeLib.getSessionId());
        editor.commit();
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = VpnMsg.MSG_VPN_CONNECTED;
            msg.obj = getDeviceId();
            mHandler.sendMessage(msg);
        }
        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnDisconnected(int error) {
        Log.d(Tag, "onVpnDisconnected " + error);
        if (!isLoginOnly && mHandler != null) {
            mStatus = VpnStatus.IDLE;
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt(VpnMsg.MSG_VPN_ERROR_CODE, error);
            msg.setData(bundle);
            msg.what = VpnMsg.MSG_VPN_DISCONNECTED;
            mHandler.sendMessage(msg);
        }
        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnConnecting() {
        Log.d(Tag, "onVpnConnecting ");
        mStatus = VpnStatus.CONNECTING;
        mHasLoginAuthInput = false;
        mRegisterAuthInput.clear();
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = VpnMsg.MSG_VPN_CONNECTING;
            mHandler.sendMessage(msg);
        }
        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnDisconnecting() {
        Log.d(Tag, "onVpnDisconnecting");
        if (!isLoginOnly && mHandler != null) {
            mStatus = VpnStatus.DISCONNECTING;
            Message msg = new Message();
            msg.what = VpnMsg.MSG_VPN_DISCONNECTING;
            mHandler.sendMessage(msg);
        }

        /*if (mVpnService != null) {
            try {
                mVpnService.stop();
				//mVpnService = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        */

        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnReconnecting() {
        Log.d(Tag, "onVpnReconnecting");
        mStatus = VpnStatus.RECONNECTING;
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = VpnMsg.MSG_VPN_RECONNECTING;
            mHandler.sendMessage(msg);
        }
        return VpnError.ERR_SUCCESS;
    }

    @Override
    int onVpnConfigRequest(String configuration) {
        Log.d(Tag, "onVpnConfigRequest");
        int index = configuration.indexOf("Tunnel Socket:");
        if (index >= 0) {
            mL3VpnConfiguration = configuration.substring(0, index);
        }
        parseVirtualIP(configuration);
        if (mVpnService != null) {
            try {
                int ret = mVpnService.getVpnInterface(configuration, mAuthInput.getHost());
                if (0 == ret) {
                    stopVPN();
                } else {
                    return ret;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(Tag, "vpn service is null");
        }
        return 0;
    }

    @Override
    int onVpnProtect(int sock) {
        if (mVpnService != null) {
            try {
                return mVpnService.protectSocket(sock);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return VpnError.ERR_SUCCESS;
    }

    /**
     * Function to handle multi-step login event.
     */
    @Override
    String onVpnLogin(String resource) {
        Log.d(Tag, "onVpnLogin " + resource);
        mCallBackStatus = CallbackStatus.STA_LOGIN;
        // The login after device registration.
        // In this case, it's no need to send message,
        // just use the previous information in AuthInput object.
        if (mHasLoginAuthInput) {
            return mAuthInput.composeInput();
        }
        mAuthInfo = ArrayAuthInfo.getArrayAuthInfo(resource);
        int error = mAuthInfo.getError();
        int errMsgId = mAuthInfo.getErrMsdId();
        Log.i(Tag, "onVpnLogin errMsgId " + errMsgId);
        if (VpnError.ERR_WRONG_USER_PASS == error && !isUsernameOrPasswordWrong(errMsgId) && errMsgId > 0) {
            error = errMsgId * 1000;
        }
        AAAMethod[] methods = getAllAAAMethod(mAuthInfo);
        String res = handleOneMethodNotNeedInput(methods);
        if (null != res) {
            return res;
        }
        if (mHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt(VpnMsg.MSG_VPN_ERROR_CODE, error);
            msg.setData(bundle);
            msg.obj = methods;
            msg.what = VpnMsg.MSG_VPN_LOGIN;
            mHandler.sendMessage(msg);
        }
        return waitForUserInput(false);
    }

    /**
     * Function to handle device register event.
     */
    @Override
    String onVpnDevReg(String resource) {
        Log.d(Tag, "onVpnDevReg " + resource);
        mCallBackStatus = CallbackStatus.STA_REGISTER;
        mAuthInfo = ArrayAuthInfo.getArrayAuthInfo(resource);
        AAAMethod[] methods = getAllAAAMethod(mAuthInfo);
        if (mHandler != null) {
            Message msg = new Message();
            msg.obj = methods;
            msg.what = VpnMsg.MSG_VPN_DEVREG;
            mHandler.sendMessage(msg);
        }
        return waitForUserInput(true);
    }

    private String waitForUserInput(boolean isRegister) {
        try {
            Log.d(Tag,
                    "Semaphore begin acquire " + mSemaphore.availablePermits());
            mIsVpnThreadPause = true;
            mSemaphore.acquire();
            mIsVpnThreadPause = false;
        } catch (InterruptedException e) {
            Log.i(Tag, "waitForUserInput " + e.getMessage());
        }
        if (isRegister) {
            return mRegisterAuthInput.composeInput();
        } else {
            return mAuthInput.composeInput();
        }
    }

    /**
     * Return the error message string by error code
     *
     * @param error the error code
     * @return the error message string
     */
    public String[] getErrorInfo(int error) {
        String[] info = new String[]{"", ""};
        if (error >= 0 && error < Common.VpnErrorInfoArray.length) {
            info[0] = Common.VpnErrorInfoArray[error].Information;
            info[1] = Common.VpnErrorInfoArray[error].Suggestion;
        }
        return info;
    }

    /**
     * The flag to start VPN tunnel.<p>
     * Set flag VPN_FLAG_HTTP_PROXY to start HTTP proxy.<p>
     * Set flag VPN_FLAG_SOCK_PROXY to start Socket proxy.<p>
     * Or use VPN_FLAG_HTTP_PROXY | VPN_FLAG_SOCK_PROXY to set both.
     */


    final class TrafficDispatchRule {
        public final static int AlltoTCP = 0;
        public final static int TCPtoTCP_OtherUDP = 1;
        public final static int TCPtoUDP_OtherTCP = 2;
        public final static int AlltoUDP = 3;
    }

    private enum CallbackStatus {
        STA_IDEL,
        STA_LOGIN,
        STA_REGISTER
    }

    /**
     * The type of InputField.<p>
     * PASSWORD indicates that a password is needed.<p>
     * USERNAME indicates that a username is needed.<p>
     * DEVICENAME indicates that a device name is needed.
     */
    public enum InputFieldType {
        /**
         * Indicates a password is needed.
         */
        PASSWORD,
        /**
         * Indicates a username is needed.
         */
        USERNAME,
        /**
         * Indicates a device name is needed.
         */
        DEVICENAME
    }

    /**
     * This class represents an authentication method sent by the AG server.<p>
     * An AAAMethod object contains a list of InputField objects which provide the information
     * of the authentication servers and request the authentication information from the users.
     */

    /**
     * This class tells developers what information the authentication server needs from users.<p>
     * If the type is PlainTextInput, then a username is needed. <p>
     * If the type is SecureTextInput, then a password is needed. <p>
     * If the type is DeviceNameInput, then a device name is needed. <p>
     * Call the method setInputString(string input) to send the information to lower layer, <p>
     * and the SDK will send this information to the AG server for authentication.
     */
    public class InputField {
        private String mName;
        private String mDescription;
        private InputFieldType mType;
        private String mInputString;
        private int mMethodIndex;

        InputField(String name, String desc, InputFieldType type, String inputString, int passwordIndex) {
            mName = name;
            mDescription = desc;
            mType = type;
            mInputString = inputString;
            mMethodIndex = passwordIndex;
        }

        /**
         * Return the name of the corresponding authentication server.
         *
         * @return a string represents the the name.
         */
        public String getName() {
            return mName;
        }

        /**
         * Return the description of the corresponding authentication server.
         *
         * @return a string represents the description.
         */
        public String getDescription() {
            return mDescription;
        }

        /**
         * Return the type of the InputField object.
         *
         * @return the enum value of InputFieldType.
         */
        public InputFieldType getType() {
            return mType;
        }

        /**
         * Return the input string come from the server.
         *
         * @return a string represents the input string.
         */
        public String getInputString() {
            return mInputString;
        }

        /**
         * Set the input string which will be sent to the server for authentication.
         *
         * @param inputString a string value, maybe a username, a password or a device name.
         */
        public void setInputString(String inputString) {
            this.mInputString = inputString;
        }

        int getMethodIndex() {
            return mMethodIndex;
        }
    }

    private void getInputField(AAAMethodInternal methodInternal, AAAMethodItem methodItem, AAAMethod method, int methodIndex) {
        if (null == methodInternal || null == methodItem || null == method) {
            return;
        }

        String inputString = "";
        InputFieldType type = InputFieldType.USERNAME;
        if (AuthType.AUTH_DEVID.ordinal() == methodItem.getType()) {
            if (!mNeedDeviceId) {
                mNeedDeviceId = true;
            }
        }
        if (methodInternal.needUsername(methodItem.getType(), methodItem.getAction())) {
            if (CertIdType.CERT_SHOW_ID.ordinal() == methodInternal.getCertIdType()) {
                inputString = methodInternal.getCertIdValue();
            }
            method.addItem(new InputField("", "", type, inputString, methodIndex));
        }

        if (methodInternal.needPassword(methodItem.getType(), methodItem.getAction())) {
            method.addItem(new InputField(methodItem.getServerName(),
                    methodItem.getServerDesc(), InputFieldType.PASSWORD, "", methodIndex));
        }
    }

    private AAAMethod[] getAllAAAMethod(ArrayAuthInfo authInfo) {
        AAAMethod[] result = new AAAMethod[authInfo.getAAAMethodNum()];
        mNeedDeviceId = false;
        for (int outer = 0; outer < authInfo.getAAAMethodNum(); outer++) {
            AAAMethodInternal methodInternal = authInfo.getMethod(outer);
            AAAMethod method = new AAAMethod(methodInternal.getName(),
                    methodInternal.getDesc());
            getInputField(methodInternal, methodInternal, method, 1);
            int inner = 0;
            for (inner = 0; inner < methodInternal.getMultiStepNum(); inner++) {
                AAAMethodItem metodItem = methodInternal.getMethod(inner);
                getInputField(methodInternal, metodItem, method, 2 + inner);
            }
            if (mCallBackStatus == CallbackStatus.STA_REGISTER) {
                String inputString = "";
                InputFieldType type = InputFieldType.DEVICENAME;
                if (!mNeedDeviceId) {
                    mNeedDeviceId = true;
                }
                method.addItem(new InputField("", "", type, inputString, 2 + inner));
            }

            result[outer] = method;
        }

        return result;
    }

    private void fillVPNAccount(VPNAccount account, AAAMethod method) {
        account.setMethodName(method.getName());
        if (mNeedDeviceId) {
            account.setDeviceId(getDeviceId());
        }
        for (int i = 0; i < method.getInputFieldCount(); i++) {
            InputField field = method.getInputField(i);
            InputFieldType type = field.getType();
            String inputStr = field.getInputString();
            switch (type) {
                case DEVICENAME:
                    account.setDeviceName(inputStr);
                    break;
                case USERNAME:
                    account.setUsername(inputStr);
                    break;
                case PASSWORD:
                    int index = field.getMethodIndex();
                    switch (index) {
                        case 1:
                            account.setPassword(inputStr);
                            break;
                        case 2:
                            account.setPassword2(inputStr);
                            break;
                        case 3:
                            account.setPassword3(inputStr);
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * If there is only one method, the type is certificate authentication,
     * the action is no challenge or anonymous, this method doesn't need any
     * input from a user, so just pass the method name to the server.
     *
     * @param methods
     * @return the formated login data
     */
    private String handleOneMethodNotNeedInput(AAAMethod[] methods) {
        if (null == methods) {
            return null;
        }
        if ((1 == methods.length) && (0 == methods[0].getInputFieldCount())) {
            fillVPNAccount(mAuthInput, methods[0]);
            return mAuthInput.composeInput();
        } else {
            return null;
        }
    }

    int getDeviceIdType() {
        return mDeviceIdType;
    }

    void setDeviceIdType(int mDeviceIdType) {
        this.mDeviceIdType = mDeviceIdType;
    }

    private static String getSimId(Context context) {
        if (null == context) {
            return null;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm) {
            return null;
        }
        boolean hasSimId = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return "TODO";
        }
        String simId = tm.getSubscriberId();
        if (simId == null || simId.length() < 1) {
            Log.e(Tag, "Get Sim ID failed!");
            hasSimId = false;
        }
        boolean hasPhoneNumber = true;
        String phoneNumber = tm.getLine1Number();
        if (phoneNumber == null || phoneNumber.length() < 1) {
            Log.e(Tag, "Get phoneNumber failed!");
            hasPhoneNumber = false;
        }
        if (hasSimId && hasPhoneNumber) {
            return simId + phoneNumber;
        } else if (hasSimId && (!hasPhoneNumber)) {
            return simId;
        } else if ((!hasSimId) && hasPhoneNumber) {
            return phoneNumber;
        } else {
            return null;
        }
    }

    private static String getHwid(Context context) {
        if (null == context) {
            return null;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm) {
            Log.e(Tag, "TELEPHONY_SERVICE not found!");
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return "TODO";
        }
        try {
            String id = tm.getDeviceId();
            if (id != null && id.length() > 0)
                return id;
            id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            // id = android.provider.Settings.Secure.getString(getContentResolver(),
            // android.provider.Settings.Secure.ANDROID_ID);
            if (id != null && id.length() > 0
                    && (!id.toLowerCase().equals("9774d56d682e549c")))
                return id;
        }catch (Exception e){
            Log.i(Tag,e.toString());
        }



        return "foo_hwid";
    }

    private String getDevid() {
        String id = "";
        switch (mDeviceIdType) {
            case DeviceIDType.DEV_DEVICE:
                id = getHwid(mContext);
                break;
            case DeviceIDType.DEV_SIM:
                id = getSimId(mContext);
                break;
            case DeviceIDType.DEV_SIM_AND_DEVICE:
                String sim = getSimId(mContext);
                id = getHwid(mContext);
                if (null != sim && sim.length() > 0) {
                    id = id + sim;
                }
                break;
            default:
                id = getHwid(mContext);
                break;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(id.getBytes("UTF-8"));
            return Base64.encode(hash).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    private ServiceConnection mSrvConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(Tag, "onServiceConnected, mStartL3VpnAfterConnection");
            mVpnService = IVpnService.Stub.asInterface(service);
            if (mStartL3VpnAfterConnection) {
                Log.i(Tag, "onServiceConnected, mStartL3VpnAfterConnection, do start l3vpn");
                mStartL3VpnAfterConnection = false;
                startL3Vpn(mAuthInput);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(Tag, "onServiceDisconnected");
            mVpnService = null;
        }
    };

    private void startL3Vpn(VPNAccount authInput) {
        Log.i(Tag, "startL3Vpn enter");
        if (mVpnService != null) {
            mStartL3VpnAfterConnection = false;
            mAuthInput = authInput;
            startVpnActivity();
        } else {
            Log.i(Tag, "startL3Vpn, mVpnService is null, cannot connect to VpnService");
        }
    }

    public void startVpnActivity() {
        Log.i(Tag, "startVpnActivity enter");
        if (VpnStatus.IDLE == getStatus() && (!mStartingL3Vpn)) {
            Log.i(Tag, "startVpnActivity start l3vpn start dialog");
            Intent i = new Intent(mAppContext, L3VpnStartDialog.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    | Intent.FLAG_FROM_BACKGROUND);
            mStartingL3Vpn = true;
            mAppContext.startActivity(i);
        }
    }

    public void onActivityResult(int request, int result) {
        if (request == VpnRequest && result == Activity.RESULT_OK) {
            Log.d(Tag, "onActivityResult: is ok");
            String host = mAuthInput.getHost();
            if (host == null || host.isEmpty()) {
                Log.d(Tag, "onActivityResult: is ok but host is empty");
                mStartingL3Vpn = false;
                return;
            }
		/*host = host.replace(" ", "");
			innerStartVpn(host, // server
					mAuthInput.getPort(), // port
					"", // alias
					mAuthInput.getMethodName(), // method
					mAuthInput.getUsername(), // user
					mAuthInput.getPassword(), // password1
					mAuthInput.getPassword2(), // password2
					mAuthInput.getPassword3(), // password3
					"", // session
					"", // valid code
					getDeviceId(), // device id
					mAuthInput.getDeviceName(), // device name
					mAuthInput.getCertPath(), // cert path
					mAuthInput.getCertPass(), // cert pass
					"", // cert data
					0,  // cert data len
					mAuthInput.isEnableUdp(), // udp enable
					mAuthInput.isEncryptUdp(), // udp encrypt
					TrafficDispatchRule.AlltoUDP, // traffic disp
					Integer.MAX_VALUE, // reconnect count
					Integer.MAX_VALUE, // max reconnect time
					VpnFlag.VPN_FLAG_NATIVE_L3VPN,
					mAuthInput.getVerifySvrCert(),
					this);*/
        } else {
            Log.d(Tag, "onActivityResult: is not ok");
            mStartingL3Vpn = false;
        }
    }

    private void innerStartVpn(
            final String host,
            final int port,
            final String alias,
            final String method,
            final String user,
            final String pass,
            final String pass2,
            final String pass3,
            final String session,
            final String validcode,
            final String deviceid,
            final String devicename,
            final String certpath,
            final String certpass,
            final String jcertdata,
            final int jcertdata_len,
            final boolean udp,
            final boolean encrypt,
            final int disp,
            final int reconn_count,
            final int reconn_max_time,
            final int flags,
            final boolean verifySvrCert,
            final NativeCallback obj) {

        Log.d(Tag, "innerStartVpn enter");
        if (mThreadStartVpn != null) {
            Log.w(Tag, "ThreadStartVpn is not null, will kill it!");
            mThreadStartVpn.interrupt();
            try {
                mThreadStartVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStartVpn = null;
        }

        mThreadStartVpn = new Thread(new Runnable() {

            @Override
            public void run() {
                int ret = VpnError.ERR_SUCCESS;
                if (verifySvrCert) {
                    onVpnConnecting();
                    ret = mAuthServerCert.verifyServerCertificate(host, port);
                }
                if (VpnError.ERR_SUCCESS == ret) {
                    ret = NativeLib.start(
                            host, // server
                            port, // port
                            alias, // alias
                            method, // method
                            user, // user
                            pass, // password1
                            pass2, // password2
                            pass3, // password3
                            session, // session
                            validcode, // valid code
                            deviceid, // device id
                            devicename, // device name
                            certpath, // certificate file path
                            certpass, // certificate password
                            jcertdata, // certificate data
                            jcertdata_len,  // certificate data len
                            udp, // udp enable
                            encrypt, // udp encrypt
                            disp, // traffic disp
                            reconn_count, // reconnect count
                            reconn_max_time, // max reconnect time
                            flags,
                            "",
                            "",
                            obj
                    );
                    if (VpnError.ERR_SUCCESS != ret) {
                        Log.e(Tag, "start failed, onVpnConnectFailed " + ret);
                        onVpnConnectFailed(ret);
                    }
                } else {
                    Log.e(Tag, "The authentication of server side certificate failed, onVpnConnectFailed " + ret);
                    onVpnConnectFailed(ret);
                }
                mStartingL3Vpn = false;
            }
        });
        mThreadStartVpn.start();
    }

    /**
     * Initialize the domain names and IP addresses for DNS query.
     * Note: invocation of this method will overwrite the data of previous invocation.
     *
     * @param arrHostname String array of domain names
     * @param arrAddress  String array of IP addresses, must be matched with arrHostnames
     * @return ERR_SUCCESS if operation is successful.
     */
    private static int initDns(String[] arrHostname, String[] arrAddress) {
        return NativeLib.initDns(arrHostname, arrAddress);
    }

    /**
     * Clear the data initialized by initDns.
     */
    private static void clearDns() {
        NativeLib.clearDns();
    }

    private int start2(String host, int port, String alias,
                       String method, String user, String pass, String pass2,
                       String pass3, String session, String validcode, String devid, String devname, String hardwareid,
                       String certpath, String certpass, String jcertdata,
                       int jcertdata_len, boolean udp, boolean encrypt, int disp,
                       int reconn_count, int reconn_max_time, int flags, NativeCallback obj) {

        String lastSessionId = mPreferences.getString(LAST_SESSION_ID, "");


        return NativeLib.start2(host, port, alias, method, user, pass, pass2, pass3, session, validcode, devid, devname, hardwareid,
                certpath, certpass, jcertdata, jcertdata_len, udp, encrypt, disp,
                reconn_count, reconn_max_time, 2, flags, new int[]{3, 4, 5, 10, 10, 10, 20, 40, 60, 120}, lastSessionId, "", "", "", obj);

    }

    private boolean isUsernameOrPasswordWrong(int error) {
        return (error == 214 || error == 215 || error == 202 || error == 203 || error == 205 || error == 206);
    }

}
