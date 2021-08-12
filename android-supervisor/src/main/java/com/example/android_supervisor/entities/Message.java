package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.example.android_supervisor.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_message")
public class Message implements Serializable {
    @DatabaseField
    private String fromId;

    @DatabaseField
    private boolean isNew = true;

    @DatabaseField(id = true)
    @SerializedName("messageId")
    private String msgId;

    @DatabaseField
    @SerializedName("mesType")
    private String msgType;

    private String createTime;


    @DatabaseField
    private Date msgTime;

    @DatabaseField
    private String groupCode;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField
    @SerializedName("params")
    private String extraInfo;

    public static final String  CASEUPLOAD = "case_Upload"; //上传上报提醒

    public static final String EVENT_TASK_HS = "question_supervisor_verify_task_notice"; // 核实
    public static final String EVENT_TASK_HC = "question_supervisor_verification_task_notice"; // 核查

    public static final String CHECK_UP = "publics_supervisor_spot_checkup"; // 抽查消息
    public static final String CHECK_UP_STATUS = "publics_spot_check_audit_notice"; // 抽查任务审核通知

    public static final String  VIDEO= "publics_video_invitation"; //视频消息
    public static final String  STAFF_MSG= "publics_supervisor_staff_msg"; //业务消息


    public Message() {
    }

    public void parseTime() {
        if (createTime!=null){
            this.msgTime = DateUtils.parse(createTime);
        }

    }

//    public Message(JSONObject obj) throws JSONException {
//        Message msg = new Gson().fromJson(obj.toString(), Message.class);
//        this.msgTime = DateUtils.parse(obj.getString("createTime"));
//
////        this.fromId = obj.getString("fromId");
////        this.msgId = obj.getString("messageId");
////        this.msgType = obj.getString("mesType");
//        this.msgTime = DateUtils.parse(obj.getString("createTime"));
//        this.groupCode = obj.getString("groupCode");
//        this.title = obj.getString("title");
//        this.content = obj.getString("content");
//        String params = obj.getString("params");
//        //{\"title\":\"视频邀请\",\"roomId\":\"1234509876\"}
//        if (!TextUtils.isEmpty(params)) {
//            JSONObject jsonObject = new JSONObject();
//            String str = jsonObject.put("params", params).toString();
//            this.extraInfo = str;
////            extraInfo = params.replaceAll("\\\\", "");
//        }
 //   }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }


//    public static class EventExtraInfo {
//        private String id;
//
//        @SerializedName("isCheck")
//        private int status;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//    }
}
