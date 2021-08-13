package com.example.android_supervisor.socketio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.DatabaseConnection;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/9/19.
 */
public class MsgManager {
    public static final String MSG_TYLE = "msgtype.cfg";
    public static MsgManager instance;
    private Context context;
    private ArrayList<MsgGroup> msgGroups;
    PrimarySqliteHelper sqliteHelper;

    private MsgManager(Context context){
        this.context = context;
        initMsgGroup();
        sqliteHelper = PrimarySqliteHelper.getInstance(context);
    }

    private void initMsgGroup() {
        msgGroups = new ArrayList<>();
        //自建消息类型
        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode = Message.CASEUPLOAD;
            msgGroup.describe = "案件上报提醒";
            msgGroup.imageId = R.drawable.msg_upload;
            msgGroups.add(msgGroup);
        }


        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_mobile_question";
            msgGroup.describe = "问卷调查提醒";
            msgGroups.add(msgGroup);

        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_mobile_notice";
            msgGroup.describe = "通知公告提醒";
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_mobile_news";
            msgGroup.describe = "实时新闻提醒";
            msgGroups.add(msgGroup);
        }


        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_attendance_clockin";
            msgGroup.describe = "监督员上班打卡提醒(上班打卡)";
            msgGroups.add(msgGroup);
        }


        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_attendance_clockout";
            msgGroup.describe = "监督员上班打卡提醒(下班打卡)";
            msgGroups.add(msgGroup);
        }


        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_spot_will_timeout";
            msgGroup.describe = "监督员抽查即将超时预警";
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_spot_timeout";
            msgGroup.describe = "监督员抽查超时报警";
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_grid_stay_too_long";
            msgGroup.describe = "监督员滞留过久报警";
            msgGroup.imageId = R.drawable.msg_zhiliu;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_grid_out_of_range";
            msgGroup.describe = "监督员超出网格报警";
            msgGroup.imageId = R.drawable.msg_kwg;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_attendance_late";
            msgGroup.describe = "迟到报警";
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_gps_invalid";
            msgGroup.describe = "GPS无效报警";
            msgGroup.imageId = R.drawable.msg_gps;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_spot_checkup";
            msgGroup.describe = "监督员抽查任务提醒";
            msgGroup.imageId = R.drawable.msg_choucha;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode ="publics_supervisor_staff_msg";
            msgGroup.describe = "监督员人员消息";
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode = Message.EVENT_TASK_HS;
            msgGroup.describe = "核实消息";
            msgGroup.imageId = R.drawable.heshi;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode = Message.EVENT_TASK_HC;
            msgGroup.describe = "核查消息";
            msgGroup.imageId = R.drawable.hecha;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode = Message.VIDEO;
            msgGroup.describe = "视频会议提醒";
            msgGroup.imageId = R.drawable.sphy;
            msgGroups.add(msgGroup);
        }

        {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.groupCode = Message.CHECK_UP_STATUS;
            msgGroup.describe = "抽查审核提醒";
            msgGroup.imageId = R.drawable.hecha;
            msgGroups.add(msgGroup);
        }




    }

    public static MsgManager getInstance(Context context) {
        if (instance == null) {
            instance = new MsgManager(context);
        }
        return instance;
    }

    public MsgGroup getMesGroupInfoByCode(String groupCode) {
        MsgGroup msgGroup  =  new MsgGroup();
        if (msgGroups!=null && msgGroups.size()>0){

            for (MsgGroup item:msgGroups) {
                if (item.groupCode.equals(groupCode)) {
                    return item;
                }
            }
        }
        return msgGroup;
    }

    public void addMsgToDb(Message message,boolean isAdd) {
        try{
           // PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);

            MessageTitle messageTitle = new MessageTitle();
            messageTitle.setGroupCode(message.getGroupCode());
            messageTitle.setMsgTime(message.getMsgTime());
            messageTitle.setContent(message.getContent());
            messageTitle.setTitle(message.getTitle());
            sqliteHelper.getMessageTitleDao().createOrUpdate(messageTitle);

            message.setIsNew(true);
            if (isAdd){
                sqliteHelper.getMessageDao().createOrUpdate(message);
            }


//            List<MessageTitle> messageTitles= sqliteHelper.getMessageTitleDao().queryForEq("groupCode", message.getGroupCode());
//            if (messageTitles!=null && messageTitles.size()>0){
//
//                List<Message> messages = sqliteHelper.getMessageDao().queryForEq("msgId", message.getMsgId());
//                //处理重复接收消息
//                if (messages!=null && messages.size()>0){
//                    return;
//                }
//                //如果标题已经存在，时间更新
//                messageTitle.setGroupCode(message.getGroupCode());
//                messageTitle.setMsgTime(message.getMsgTime());
//                sqliteHelper.getMessageTitleDao().update(messageTitle);
//
//            }else {
//                messageTitle.setGroupCode(message.getGroupCode());
//                messageTitle.setMsgTime(message.getMsgTime());
//                messageTitle.setContent(message.getContent());
//                messageTitle.setTitle(message.getTitle());
//                sqliteHelper.getMessageTitleDao().create(messageTitle);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public class MsgGroup{

        public int imageId = R.drawable.news;

        public String groupCode;

        public String describe;

    }



    public int  getAllMsgCount(){
        int count =0;
        List<MessageTitle> messageTitles =  getAllNewMsgTitle();
        if (messageTitles!=null && messageTitles.size()>0){
            for (MessageTitle messageTitle:messageTitles){
                count+=messageTitle.getMsgCount();
            }
        }
        return count;
    }


    public  List<MessageTitle> getAllNewMsgTitle(){

        List<MessageTitle> messageTitles =  sqliteHelper.getMessageTitleDao().queryForAll();
        if (messageTitles!=null){
            //查询二级页面消息个数
            for (MessageTitle messageTitle:messageTitles){

                List<Message> newMsg =  getNewMsgbyTitle(messageTitle);
                messageTitle.setMsgCount(newMsg.size());

            }
        }else {
            messageTitles = new ArrayList<>();
        }
        return messageTitles;

    }

    public  List<Message> getNewMsgbyTitle(MessageTitle messageTitle){

        List<Message> temp = new ArrayList<>();
        if (messageTitle==null){
            return temp;
        }

//        PrimarySqliteHelper sqliteHelper_msg = PrimarySqliteHelper.getInstance(context);
        List<Message> newMsg =  sqliteHelper.getMessageDao().queryForEq("groupCode", messageTitle.getGroupCode());

        if (newMsg!=null && newMsg.size()>0 ){
            for (Message msg: newMsg){
                if (msg.isNew()){
                    temp.add(msg);
                }
            }
        }
        return temp;

    }



    public void setReadMsgByTitleId(String  groupCode){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Message> messages =  sqliteHelper.getMessageDao().queryForEq("groupCode", groupCode);

                refreshAppBadge(groupCode,messages);
            }
        }).start();


    }


//    public void setReadMsgById(String msgId ){
//
//        List<Message> messages =  sqliteHelper.getMessageDao().queryForEq("msgId", msgId);
//        refreshAppBadge(messages);
//    }


    private void refreshAppBadge(String groupCode,List<Message> messages) {

//        AndroidDatabaseConnection adc = null;
//        adc = new AndroidDatabaseConnection(sqliteHelper.getWritableDatabase(), true);
//
        RuntimeExceptionDao<Message, String> dao =  sqliteHelper.getMessageDao();
//        dao.setAutoCommit(adc, false);

        try {
            DatabaseConnection conn = dao.startThreadConnection();
            Savepoint savePoint = conn.setSavePoint(null);
            if (messages != null && messages.size()>0) {
                for (Message msg : messages) {
                    if (msg.isNew()){
                        msg.setIsNew(false);
                        dao.update(msg);
                    }

                }
                conn.commit(savePoint);
                dao.endThreadConnection(conn);

                Intent intent = new Intent();
                intent.putExtra("refreshItem",groupCode);

                NotifyManager.notify(context, Notifies.NOTIFY_TYPE_MSG_BADGE, 1, 1,groupCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

//    public void setReadMsgByMsgId(String msgId,boolean refreshAppBadge){
//
//        List<Message> messages =  sqliteHelper.getMessageDao().queryForEq("msgId",msgId);
//        if(refreshAppBadge)refreshAppBadge(messages);
//    }


    public void addTestMsg(MsgCallBack msgCallBack) {

        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
  //                  PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(context);

    //                PrimarySqliteHelper sqliteHelper = MsgManager.getInstance(context).getSqliteHelper();

                    if (sqliteHelper!=null){
//                        List<MessageTitle> messageTitles= sqliteHelper.getMessageTitleDao().queryForEq("groupCode","提醒助手");

                        MessageTitle messageTitle = new MessageTitle();
                        messageTitle.setGroupCode("提醒助手");
                        messageTitle.setContent("您好！欢迎使用城管通，点击查看操作指南");
                        messageTitle.setMsgTime(new Date());
                        messageTitle.setTitle("提醒助手");
                        sqliteHelper.getMessageTitleDao().createOrUpdate(messageTitle);

                        Message msg  = new Message();
                        msg.setGroupCode(messageTitle.getGroupCode());
                        msg.setIsNew(true);
                        msg.setMsgId("123");
                        sqliteHelper.getMessageDao().createOrUpdate(msg);

                        Activity activity = (Activity) context;
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   if (msgCallBack!=null){
                                       msgCallBack.addMsgSuccess();
                                   }
                                }
                            });
                        }

                    }


                }
            }).start();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface MsgCallBack{

        void addMsgSuccess();
    }







}
