package com.example.android_supervisor.x5;


import android.os.Bundle;

import com.example.android_supervisor.utils.Environments;

public class LeaveNoteActivity extends BrowserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ctz);
    }

    @Override
    public String loadUrl() {
        if (Environments.isDeBug()){
            return "http://192.168.20.77:8001/#/leaveList";
        }else {
//             return "https://tykj.cszhx.top/h5";
            return "http://117.159.24.4:30079/#/leavelist";
//        }
       // return "file:///android_asset/webpage/fileChooser.html";
      //  return "http://192.168.20.170:9989/h5/";
      //  return "https://www.jianshu.com/p/3a95523fb21b";
    }}
}
