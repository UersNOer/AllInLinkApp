package com.example.android_supervisor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.ui.LoginActivity;


/**
 * @author wujie
 */
public class ForceReloginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String refreshToken = AccessToken.getInstance(context).getRefreshToken();

        AccessToken.getInstance(context).delete();
        UserSession.setUserId(context, "");

        AppServiceManager.stop(context);
//        EaseUI.getInstance().getNotifier().reset();
//        EMClient.getInstance().logout(true);

        AppContext appContext = (AppContext) context.getApplicationContext();
        appContext.exit();

        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.putExtra("refresh_token", refreshToken);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
        //{"error":"unauthorized","error_description":"RemoteAppService#info(String) failed and no fallback available."}
        Toast.makeText(context, "鉴权已经过期，请重新登录,", Toast.LENGTH_LONG).show();
    }
}
