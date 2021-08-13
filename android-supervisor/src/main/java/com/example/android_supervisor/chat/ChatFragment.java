package com.example.android_supervisor.chat;//package com.example.android_supervisor.chat;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.easeui.ui.EaseChatFragment;
//import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
//import com.hyphenate.easeui.ui.ImageGridActivity;
//import com.hyphenate.easeui.widget.EaseChatVoiceCallPresenter;
//import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
//import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
//import com.hyphenate.util.PathUtil;
//import com.example.android_supervisor.R;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.List;
//
//public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {
//
//	// constant start from 11 to avoid conflict with constant in base class
//    private static final int ITEM_VIDEO = 11;
//    private static final int ITEM_FILE = 12;
//    private static final int ITEM_VOICE_CALL = 13;
//    private static final int ITEM_VIDEO_CALL = 14;
//
//    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
//    private static final int REQUEST_CODE_SELECT_FILE = 12;
//    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
//
//
//    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
//    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
//    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
//    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    protected void setUpView() {
//        setChatFragmentHelper(this);
//        super.setUpView();
//        // set click listener
//    }
//
//    @Override
//    protected void registerExtendMenuItem() {
//        //use the menu in base class
//        super.registerExtendMenuItem();
//        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//        if(chatType == Constant.CHATTYPE_SINGLE){
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == Activity.RESULT_OK){
//            switch (requestCode) {
//            case REQUEST_CODE_SELECT_VIDEO: //send the video
//                if (data != null) {
//                    int duration = data.getIntExtra("dur", 0);
//                    String videoPath = data.getStringExtra("path");
//                    File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
//                    try {
//                        FileOutputStream fos = new FileOutputStream(file);
//                        Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
//                        ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
//                        fos.close();
//                        sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            case REQUEST_CODE_SELECT_FILE: //send the file
//                if (data != null) {
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        sendFileByUri(uri);
//                    }
//                }
//                break;
//            case REQUEST_CODE_SELECT_AT_USER:
//                if(data != null){
//                    String username = data.getStringExtra("username");
//                    inputAtUsername(username, false);
//                }
//                break;
//            default:
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void onSetMessageAttributes(EMMessage message) {
//    }
//
//    @Override
//    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
//        return new CustomChatRowProvider();
//    }
//
//
//    @Override
//    public void onEnterToChatDetails() {
//
//    }
//
//    @Override
//    public void onAvatarClick(String username) {
//        //handling when user click avatar
//    }
//
//    @Override
//    public void onAvatarLongClick(String username) {
//        inputAtUsername(username);
//    }
//
//
//    @Override
//    public boolean onMessageBubbleClick(EMMessage message) {
//        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
//        return false;
//    }
//
//    @Override
//    public void onMessageBubbleLongClick(EMMessage message) {
//
//    }
//
//    @Override
//    public void onCmdMessageReceived(List<EMMessage> messages) {
//        super.onCmdMessageReceived(messages);
//    }
//
//
//    @Override
//    public boolean onExtendMenuItemClick(int itemId, View view) {
//        switch (itemId) {
//        case ITEM_VIDEO:
//            Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
//            break;
//        case ITEM_FILE: //file
//            selectFileFromLocal();
//            break;
//        case ITEM_VOICE_CALL:
//            startVoiceCall();
//            break;
//        case ITEM_VIDEO_CALL:
//            startVideoCall();
//            break;
//        default:
//            break;
//        }
//        //keep exist extend menu
//        return false;
//    }
//
//    /**
//     * select file
//     */
//    protected void selectFileFromLocal() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//
//        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
//    }
//
//    /**
//     * make a voice call
//     */
//    protected void startVoiceCall() {
//        if (!EMClient.getInstance().isConnected()) {
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        } else {
//            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // voiceCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
//    }
//
//    /**
//     * make a video call
//     */
//    protected void startVideoCall() {
//        if (!EMClient.getInstance().isConnected())
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        else {
//            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // videoCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
//    }
//
//    /**
//     * chat row provider
//     *
//     */
//    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
//        @Override
//        public int getCustomChatRowTypeCount() {
//            //here the number is the message type in EMMessage::Type
//        	//which is used to count the number of different chat row
//            return 14;
//        }
//
//        @Override
//        public int getCustomChatRowType(EMMessage message) {
//            if(message.getType() == EMMessage.Type.TXT){
//                //voice call
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
//                }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    //video call
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
//                }
//            }
//            return 0;
//        }
//
//        @Override
//        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
//            if(message.getType() == EMMessage.Type.TXT){
//                // voice call or video call
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                    message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    EaseChatRowPresenter presenter = new EaseChatVoiceCallPresenter();
//                    return presenter;
//                }
//            }
//            return null;
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//}
