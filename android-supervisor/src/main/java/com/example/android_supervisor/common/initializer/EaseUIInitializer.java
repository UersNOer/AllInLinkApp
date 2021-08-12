package com.example.android_supervisor.common.initializer;//package com.example.android_supervisor.common.initializer;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMCmdMessageBody;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.chat.EMOptions;
//import com.hyphenate.chat.EMTextMessageBody;
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.EaseUI;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.model.EaseAtMessageHelper;
//import com.hyphenate.easeui.model.EaseNotifier;
//import com.hyphenate.easeui.utils.EaseCommonUtils;
//import com.hyphenate.util.EMLog;
//import com.example.android_supervisor.R;
//import com.example.android_supervisor.chat.CallReceiver;
//import com.example.android_supervisor.chat.ChatActivity;
//import com.example.android_supervisor.chat.PreferenceManager;
//import com.example.android_supervisor.chat.VideoCallActivity;
//import com.example.android_supervisor.chat.VoiceCallActivity;
//import com.example.android_supervisor.entities.Contact;
//import com.example.android_supervisor.sqlite.PublicSqliteHelper;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author wujie
// */
//public class EaseUIInitializer {
//
//    public boolean isVoiceCalling;
//    public boolean isVideoCalling;
//
//    private static EaseUIInitializer instance = null;
//    private Context appContext;
//
//    public synchronized static EaseUIInitializer getInstance() {
//        if (instance == null) {
//            instance = new EaseUIInitializer();
//        }
//        return instance;
//    }
//
//
//    public void initialize(Context context) {
//        EMOptions options = new EMOptions();
//        if (EaseUI.getInstance().init(context, options)) {
//            appContext=context;
//            EMClient.getInstance().setDebugMode(true);
//
//            EMClient.getInstance().chatManager().addMessageListener(new EmMessageAdapter(context));
//            EaseUI.getInstance().setUserProfileProvider(new EaseUserProfileProviderImpl(context));
//            EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProviderImpl(context));
//            PreferenceManager.init(context);
//            setLisener(context);
//            setCallOptions();
//        }
//    }
//
//    private void setLisener(Context context) {
//        //监听呼入通话
//        String callAction = EMClient.getInstance().callManager().getIncomingCallBroadcastAction();
//        IntentFilter callFilter = new IntentFilter(callAction);
//        context.registerReceiver(new CallReceiver(), callFilter);
//    }
//
//    public boolean isLoggedIn() {
//        return EMClient.getInstance().isLoggedInBefore();
//    }
//
//    private void setCallOptions() {
//
//        // min video kbps
//        int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
//        if (minBitRate != -1) {
//            EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
//        }
//
//        // max video kbps
//        int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
//        if (maxBitRate != -1) {
//            EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
//        }
//
//        // max frame rate
//        int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
//        if (maxFrameRate != -1) {
//            EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
//        }
//
//        // audio sample rate
//        int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
//        if (audioSampleRate != -1) {
//            EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
//        }
//
//        /**
//         * This function is only meaningful when your app need recording
//         * If not, remove it.
//         * This function need be called before the video stream started, so we set it in onCreate function.
//         * This method will set the preferred video record encoding codec.
//         * Using default encoding format, recorded file may not be played by mobile player.
//         */
//        //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);
//
//        // resolution
//        String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
//        if (resolution.equals("")) {
//            resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
//        }
//        String[] wh = resolution.split("x");
//        if (wh.length == 2) {
//            try {
//                EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // enabled fixed sample rate
//        boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
//        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);
//
//    }
//
//     class EaseUserProfileProviderImpl implements EaseUI.EaseUserProfileProvider {
//        private Context context;
//        private final Map<String, EaseUser> easeUserMap = new HashMap<>();
//
//        public EaseUserProfileProviderImpl(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public EaseUser getUser(String userId) {
//            if (userId == null) return null;
//            Log.d("dawn", "getUser: "+userId);
//            EaseUser easeUser = easeUserMap.get(userId);
//            if (easeUser == null) {
//                PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
//                Contact contact = sqliteHelper.getContactDao().queryForId(userId);
//                if (contact != null) {
//                    easeUser = new EaseUser(userId);
//                    easeUser.setNickname(contact.getNickName());
//                    easeUserMap.put(userId, easeUser);
//                }
//            }
//            return easeUser;
//        }
//    }
//
//    class EaseNotificationInfoProviderImpl implements EaseNotifier.EaseNotificationInfoProvider {
//        private Context context;
//
//        public EaseNotificationInfoProviderImpl(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public String getTitle(EMMessage message) {
//            //you can update title here
//            return null;
//        }
//
//        @Override
//        public int getSmallIcon(EMMessage message) {
//            //you can update icon here
//            return 0;
//        }
//
//        @Override
//        public String getDisplayedText(EMMessage message) {
//            // be used on notification bar, different text according the message type.
//            String ticker = EaseCommonUtils.getMessageDigest(message, context);
//            if (message.getType() == EMMessage.Type.TXT) {
//                ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//            }
//            EaseUI.EaseUserProfileProvider userProfileProvider = EaseUI.getInstance().getUserProfileProvider();
//            EaseUser user = userProfileProvider.getUser(message.getFrom());
//            if (user != null) {
//                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
//                    return String.format(context.getString(R.string.at_your_in_group), user.getNickname());
//                }
//                return user.getNickname() + ": " + ticker;
//            } else {
//                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
//                    return String.format(context.getString(R.string.at_your_in_group), message.getFrom());
//                }
//                return message.getFrom() + ": " + ticker;
//            }
//        }
//
//        @Override
//        public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
//            // here you can customize the text.
//            // return fromUsersNum + "contacts send " + messageNum + "messages to you";
//            return null;
//        }
//
//        @Override
//        public Intent getLaunchIntent(EMMessage message) {
//            // you can set what activity you want display when user click the notification
//            Intent intent = new Intent(context, ChatActivity.class);
////            Intent intent = new Intent(context, EaseChatActivity.class);
//            // open calling activity if there is call
//            if (isVideoCalling) {
//                intent = new Intent(context, VideoCallActivity.class);
//            } else if (isVoiceCalling) {
//                intent = new Intent(context, VoiceCallActivity.class);
//            } else {
//                EMMessage.ChatType chatType = message.getChatType();
//                if (chatType == EMMessage.ChatType.Chat) { // single chat message
//                    intent.putExtra("userId", message.getFrom());
//                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
//                } else { // group chat message
//                    // message.getTo() is the group id
//                    intent.putExtra("userId", message.getTo());
//                    if (chatType == EMMessage.ChatType.GroupChat) {
//                        intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
//                    } else {
//                        intent.putExtra("chatType", EaseConstant.CHATTYPE_CHATROOM);
//                    }
//                }
//            }
//            return intent;
//        }
//    }
//
//     class EmMessageAdapter implements EMMessageListener {
//        private Context context;
//
//        public EmMessageAdapter(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onMessageReceived(List<EMMessage> messages) {
//            for (EMMessage message : messages) {
////                // 判断一下是否是会议邀请
////                String confId = message.getStringAttribute(EaseConstant.MSG_ATTR_CONF_ID, "");
////                if(!"".equals(confId)){
////                    String password = message.getStringAttribute(Constant.MSG_ATTR_CONF_PASS, "");
////                    String extension = message.getStringAttribute(Constant.MSG_ATTR_EXTENSION, "");
////                    goConference(confId, password, extension);
////                }
//                // in background, do not refresh UI, notify it in notification bar
//                if(!EaseUI.getInstance().hasForegroundActivies()){
//                    EaseUI.getInstance().getNotifier().notify(message);
//                }
//            }
//        }
//
//        @Override
//        public void onCmdMessageReceived(List<EMMessage> messages) {
//            for (EMMessage message : messages) {
//                //get message body
//                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                final String action = cmdMsgBody.action();//获取自定义action
//                //获取扩展属性 此处省略
//                //maybe you need get extension of your message
//                //message.getStringAttribute("");
//                EMLog.d("EaseUI", String.format("Command：action:%s,message:%s", action,message.toString()));
//            }
//        }
//
//        @Override
//        public void onMessageRead(List<EMMessage> messages) {
//        }
//
//        @Override
//        public void onMessageDelivered(List<EMMessage> message) {
//        }
//
//        public boolean isLoggedIn() {
//            return EMClient.getInstance().isLoggedInBefore();
//        }
//
//        @Override
//        public void onMessageRecalled(List<EMMessage> messages) {
//            for (EMMessage msg : messages) {
//                if(msg.getChatType() == EMMessage.ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)){
//                    EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
//                }
//                EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//                EMTextMessageBody txtBody = new EMTextMessageBody(String.format(context.getString(R.string.msg_recall_by_user), msg.getFrom()));
//                msgNotification.addBody(txtBody);
//                msgNotification.setFrom(msg.getFrom());
//                msgNotification.setTo(msg.getTo());
//                msgNotification.setUnread(false);
//                msgNotification.setMsgTime(msg.getMsgTime());
//                msgNotification.setLocalTime(msg.getMsgTime());
//                msgNotification.setChatType(msg.getChatType());
//                msgNotification.setAttribute(EaseConstant.MESSAGE_TYPE_RECALL, true);
//                msgNotification.setStatus(EMMessage.Status.SUCCESS);
//                EMClient.getInstance().chatManager().saveMessage(msgNotification);
//            }
//        }
//
//        @Override
//        public void onMessageChanged(EMMessage message, Object change) {
//
//        }
//    }
//}
