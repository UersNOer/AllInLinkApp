package net.vpnsdk.vpn;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.xg.vpn.R;

import java.io.DataInputStream;
import java.io.FileOutputStream;

public class VpnUtils {

    private static final String Tag = "VpnUtils";
    private static VpnUtils instance;
    private Context context;
    private Thread mThreadStartVpn;
    private Thread mThreadStopVpn;


    /**
     * 一个app只能持有一个vpn通道
     *
     * @param context
     * @param handler
     * @return
     */
    public static VpnUtils getInstance(Context context, Handler handler) {
        if (instance == null)
            instance = new VpnUtils(context, handler);
        return instance;
    }

    private VpnUtils(Context context, Handler handler) {
        this.context = context;
        //初始化SDK
        VPNManager.initialize(context).setHandler(handler);
        //设置log为SDK级别
        VPNManager.getInstance().setLogLevel(Common.LogLevel.LOG_DEBUG, 0);
        //bind 服务，不bind则无法启动VPN
        VPNManager.getInstance().bindVPNService("net.vpnsdk.vpn.VPN_SERVICE");
        //把证书复制到一个临时文件中
        savePl2();
    }

    //复制加密证书
    private void savePl2() {
        saveRawResourceToFile(R.raw.rsaclient, "rsaclient.p12");
        saveRawResourceToFile(R.raw.sm2agclientenc, "sm2agclientenc.p12");
        saveRawResourceToFile(R.raw.sm2agclientsign, "sm2agclientsign.p12");
    }

    // 保存资源到文件中
    private void saveRawResourceToFile(int id, String filename) {
        Log.d(Tag, "file dir is " + context.getFilesDir());
        DataInputStream in = new DataInputStream(context.getResources().openRawResource(id));
        try {
            try {
                FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                in.close();
            } catch (Exception e) {
            }
        } finally {
        }
    }

    /**
     * 开启 StartVpnThread 连接Vpn
     *
     * @param host     ip地址
     * @param port     端口号
     * @param username 用户账号
     * @param password 用户密码
     * @param certpath 加密证书地址
     * @param certpass 加密证书密码
     * @param flag     开启L3
     */

    public void start(String host, int port, String username, String password,
                      String certpath, String certpass, int flag) {
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
        mThreadStartVpn = new StartVpnThread(host, port, username, password,
                certpath, certpass, flag);

        mThreadStartVpn.start();
    }

    public interface VpnListance {
        void onSuccess();

        void onFail();
    }

    /**
     * 关闭Vpn
     */
    public void stop(VpnListance vpnListance) {
        if (Common.VpnStatus.IDLE == VPNManager.getInstance().getStatus()) {
            return;
        }
        if (mThreadStopVpn != null) {
            Log.w(Tag, "ThreadStopVpn is not null, will kill it.");
            mThreadStopVpn.interrupt();
            try {
                mThreadStopVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStopVpn = null;
        }

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

        mThreadStopVpn = new Thread(new Runnable() {
            public void run() {
                VPNManager.getInstance().stopVPN();
                mThreadStopVpn = null;
            }
        });

        mThreadStopVpn.start();
        vpnListance.onSuccess();
    }

    /**
     * @return 当前Vpn状态
     */
    public int getVpnStatus() {
        return VPNManager.getInstance().getStatus();

    }

    public void onActivityResult(int requestCode, int resultCode) {
        VPNManager.getInstance().onActivityResult(requestCode, resultCode);
    }

    /**
     * 根据msgCode 返回错误信息
     */
    public String[] getErrorInfo(int msgcode) {
        return VPNManager.getInstance().getErrorInfo(msgcode);
    }
}
