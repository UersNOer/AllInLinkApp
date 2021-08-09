package net.vpnsdk.vpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class L3VpnStartDialog extends Activity {

    private static final String Tag = "L3VpnStartDialog";
    private VPNManager mVpnManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutParams params = getWindow().getAttributes();
        params.width = 1;
        params.height = 1;
        getWindow().setAttributes(params);
        mVpnManager = VPNManager.getInstance();
        startVpn();
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent) {
        Log.d(Tag, "L3VpnStartDialog, onActivityResult");
        mVpnManager.onActivityResult(request, result);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startVpn() {
        int status = mVpnManager.getStatus();
        Log.d(Tag, "L3VpnStartDialog, vpn status before start: " + status);

        if (status == Common.VpnStatus.IDLE) {
            Log.d(Tag, "L3VpnStartDialog, vpn is idle, do vpn prepare");
            Intent intent = VpnService.prepare(this);
            if (intent != null) {
                Log.d(Tag, "L3VpnStartDialog, intent is not null");
                startActivityForResult(intent,
                        VPNManager.VpnRequest);
            } else {
                Log.d(Tag, "L3VpnStartDialog, intent is null");
                onActivityResult(VPNManager.VpnRequest,
                        Activity.RESULT_OK, null);
            }
        }
    }
}