package com.example.android_supervisor.chat;

import java.io.Serializable;

/**
 * Created by dw on 2019/8/15.
 */
public class ChatModel implements Serializable {

    public void setContactSynced(boolean synced){
        PreferenceManager.getInstance().setContactSynced(synced);
    }

    public boolean isContactSynced(){
        return PreferenceManager.getInstance().isContactSynced();
    }
}
