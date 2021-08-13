package com.example.android_supervisor.ui;

import android.os.Bundle;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.ui.fragment.MessageListFragment;

public class MsgListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        MessageTitle  messageTitle = (MessageTitle) getIntent().getSerializableExtra("messageTitleInfo");
        MessageListFragment fragment =  MessageListFragment.newInstance(messageTitle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContent,fragment).commit();
    }
}
