package com.tencent.liteav.demo.trtc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.liteav.demo.R;
import com.tencent.liteav.demo.trtc.debug.GenerateTestUserSig;

public class SettingActivity extends Activity {
    private EditText et_SDKAppID;
    private EditText et_SECRETKEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        et_SDKAppID= (EditText) findViewById(R.id.et_SDKAppID);
        et_SECRETKEY= (EditText) findViewById(R.id.et_SECRETKEY);

        et_SDKAppID.setText(String.valueOf(GenerateTestUserSig.SDKAPPID));
        et_SECRETKEY.setText(GenerateTestUserSig.SECRETKEY);

    }

    public void onClick(View view){
        try{
            String appId = et_SDKAppID.getText().toString().trim();
            if (appId!=null){
                GenerateTestUserSig.SDKAPPID = Integer.valueOf(appId);
                putInt(GenerateTestUserSig.SDKAPPID);
            }

            String SECRETKEY  = et_SECRETKEY.getText().toString().trim();
            if (SECRETKEY!=null){
                GenerateTestUserSig.SECRETKEY = SECRETKEY;
                putString( GenerateTestUserSig.SECRETKEY);
            }
        }catch (Exception e){
            Toast.makeText(SettingActivity.this, "配置设置出错,请检查", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        finish();

    }

    public void putString(String SECRETKEY){
        SharedPreferences.Editor editor=getSharedPreferences("config,txt", Context.MODE_PRIVATE)
                .edit();

        if (TextUtils.isEmpty(SECRETKEY)){
            editor.putString("SECRETKEY",SECRETKEY);
        }
        editor.commit();
    }


    public void putInt(int SDKAPPID){
        SharedPreferences.Editor editor=getSharedPreferences("config,txt", Context.MODE_PRIVATE)
                .edit();

        editor.putInt("SDKAPPID", SDKAPPID);

        editor.commit();
    }

}
