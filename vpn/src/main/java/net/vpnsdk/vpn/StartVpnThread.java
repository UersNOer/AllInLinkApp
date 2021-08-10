package net.vpnsdk.vpn;
public class StartVpnThread extends Thread {
    String mHost;
    int mPort;
    String mUserName;
    String mPassword;
    String mCertPath;
    String mCertPass;
    int mFlag;

    public StartVpnThread(String host, int port, String username,
                          String password, String certpath, String certpass, int flag) {
        mHost = host;
        mPort = port;
        mUserName = username;
        mPassword = password;
        mCertPath = certpath;
        mCertPass = certpass;
        mFlag = flag;
    }

    public void run() {
        VPNManager.getInstance().startVPN(mHost, mPort, mUserName,
                mPassword, mCertPath, mCertPass, mFlag);
    }
}