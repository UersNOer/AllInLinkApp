package com.example.android_supervisor.chat;//package com.example.android_supervisor.chat;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMGroup;
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.runtimepermissions.PermissionsManager;
//import com.hyphenate.easeui.ui.EaseChatFragment;
//import com.hyphenate.easeui.utils.EaseUserUtils;
//import com.hyphenate.util.EasyUtils;
//import com.example.android_supervisor.R;
//import com.example.android_supervisor.ui.BaseActivity;
//
///**
// * chat activity，EaseChatFragment was used
// *
// */
//public class ChatActivity extends BaseActivity {
//    EaseChatFragment chatFragment;
//    String userId;
//    int chatType;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ease_chat);
//
//        chatFragment = new ChatFragment();
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            userId = bundle.getString(EaseConstant.EXTRA_USER_ID);
//            chatType = bundle.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//            chatFragment.setArguments(bundle);
//        }
//
//        setTitle();
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, chatFragment, "")
//                .commit();
//    }
//
//    private void setTitle() {
//        switch (chatType) {
//            case EaseConstant.CHATTYPE_SINGLE:
//                EaseUser user = EaseUserUtils.getUserInfo(userId);
//                if (user != null) {
//                    setTitle(user.getNickname());
//                }
//                break;
//            case EaseConstant.CHATTYPE_GROUP:
//                EMGroup group = EMClient.getInstance().groupManager().getGroup(userId);
//                if (group != null) {
//                    setTitle(group.getGroupName());
//                }
//                break;
//            default:
//                setTitle(userId);
//                break;
//        }
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }
//}
