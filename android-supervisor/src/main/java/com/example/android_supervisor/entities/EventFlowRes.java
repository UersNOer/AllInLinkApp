package com.example.android_supervisor.entities;

import java.util.List;

/**
 * @author wujie
 */
public class EventFlowRes {


    /**
     * actInstId : 1167350599279808513
     * arrivalTime : 2019-08-30 16:17:26
     * assignId : 1166278126804533249
     * assignName : 区受理员(光明区受理员)
     * attachNameList : []
     * comments : 自动完成当前任务
     * count : 1
     * currentLink : 登记
     * delayList : []
     * endTime : 2019-08-30 16:17:27
     * evtId : 1167350598189289473
     * goalTaskList : []
     * hasDelay : false
     * hasRollBack : false
     * hasTodo : false
     * hasUrge : false
     * id : 1167350611099357185
     * isTimeOut : false
     * jumType : 0
     * millis : 1分钟
     * procInstId : 1167350599179145216
     * rollBackAuditList : []
     * rollbackList : []
     * shouldFinishTime : 2019-08-30 16:47:25
     * timeLimit : 30分钟
     * todoList : []
     * umEvtAttchAndTaskList : []
     * umEvtAttchList : [{"acceptId":"1167350598189289473","actInstId":"1167350599279808513","attchDisposeStatus":"0","attchFileName":"111.jpeg","attchFilePath":"5d68db941c4ae527840cd895.jpeg","attchType":"0","attchUsage":"0","createId":"1166278126804533249","createName":"光明区受理员","createTime":"2019-08-30 16:17:27","dbStatus":"1","id":"1167350604396859393"}]
     * umEvtTaskInfoList : []
     * urgeVOList : []
     * usedTime : 1分钟
     */

    private String actInstId;
    private String arrivalTime;
    private String assignId;
    private String assignName;
    private String comments;
    private int count;
    private String currentLink;
    private String endTime;
    private String evtId;
    private boolean hasDelay;
    private boolean hasRollBack;
    private boolean hasTodo;
    private boolean hasUrge;
    private String id;
    private boolean isTimeOut;
    private String jumType;
    private String millis;
    private String procInstId;
    private String shouldFinishTime;
    private String timeLimit;
    private String usedTime;
    private List<?> attachNameList;
    private List<?> delayList;
    private List<?> goalTaskList;
    private List<?> rollBackAuditList;
    private List<?> rollbackList;
    private List<?> todoList;
    private List<?> umEvtAttchAndTaskList;
    /**
     * acceptId : 1167350598189289473
     * actInstId : 1167350599279808513
     * attchDisposeStatus : 0
     * attchFileName : 111.jpeg
     * attchFilePath : 5d68db941c4ae527840cd895.jpeg
     * attchType : 0
     * attchUsage : 0
     * createId : 1166278126804533249
     * createName : 光明区受理员
     * createTime : 2019-08-30 16:17:27
     * dbStatus : 1
     * id : 1167350604396859393
     */

    private List<UmEvtAttchListBean> umEvtAttchList;
    private List<?> umEvtTaskInfoList;
    private List<?> urgeVOList;

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink = currentLink;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEvtId() {
        return evtId;
    }

    public void setEvtId(String evtId) {
        this.evtId = evtId;
    }

    public boolean isHasDelay() {
        return hasDelay;
    }

    public void setHasDelay(boolean hasDelay) {
        this.hasDelay = hasDelay;
    }

    public boolean isHasRollBack() {
        return hasRollBack;
    }

    public void setHasRollBack(boolean hasRollBack) {
        this.hasRollBack = hasRollBack;
    }

    public boolean isHasTodo() {
        return hasTodo;
    }

    public void setHasTodo(boolean hasTodo) {
        this.hasTodo = hasTodo;
    }

    public boolean isHasUrge() {
        return hasUrge;
    }

    public void setHasUrge(boolean hasUrge) {
        this.hasUrge = hasUrge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(boolean isTimeOut) {
        this.isTimeOut = isTimeOut;
    }

    public String getJumType() {
        return jumType;
    }

    public void setJumType(String jumType) {
        this.jumType = jumType;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getShouldFinishTime() {
        return shouldFinishTime;
    }

    public void setShouldFinishTime(String shouldFinishTime) {
        this.shouldFinishTime = shouldFinishTime;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public List<?> getAttachNameList() {
        return attachNameList;
    }

    public void setAttachNameList(List<?> attachNameList) {
        this.attachNameList = attachNameList;
    }

    public List<?> getDelayList() {
        return delayList;
    }

    public void setDelayList(List<?> delayList) {
        this.delayList = delayList;
    }

    public List<?> getGoalTaskList() {
        return goalTaskList;
    }

    public void setGoalTaskList(List<?> goalTaskList) {
        this.goalTaskList = goalTaskList;
    }

    public List<?> getRollBackAuditList() {
        return rollBackAuditList;
    }

    public void setRollBackAuditList(List<?> rollBackAuditList) {
        this.rollBackAuditList = rollBackAuditList;
    }

    public List<?> getRollbackList() {
        return rollbackList;
    }

    public void setRollbackList(List<?> rollbackList) {
        this.rollbackList = rollbackList;
    }

    public List<?> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<?> todoList) {
        this.todoList = todoList;
    }

    public List<?> getUmEvtAttchAndTaskList() {
        return umEvtAttchAndTaskList;
    }

    public void setUmEvtAttchAndTaskList(List<?> umEvtAttchAndTaskList) {
        this.umEvtAttchAndTaskList = umEvtAttchAndTaskList;
    }

    public List<UmEvtAttchListBean> getUmEvtAttchList() {
        return umEvtAttchList;
    }

    public void setUmEvtAttchList(List<UmEvtAttchListBean> umEvtAttchList) {
        this.umEvtAttchList = umEvtAttchList;
    }

    public List<?> getUmEvtTaskInfoList() {
        return umEvtTaskInfoList;
    }

    public void setUmEvtTaskInfoList(List<?> umEvtTaskInfoList) {
        this.umEvtTaskInfoList = umEvtTaskInfoList;
    }

    public List<?> getUrgeVOList() {
        return urgeVOList;
    }

    public void setUrgeVOList(List<?> urgeVOList) {
        this.urgeVOList = urgeVOList;
    }

    public static class UmEvtAttchListBean {
        private String acceptId;
        private String actInstId;
        private String attchDisposeStatus;
        private String attchFileName;
        private String attchFilePath;
        private String attchType;
        private String attchUsage;
        private String createId;
        private String createName;
        private String createTime;
        private String dbStatus;
        private String id;

        public String getAcceptId() {
            return acceptId;
        }

        public void setAcceptId(String acceptId) {
            this.acceptId = acceptId;
        }

        public String getActInstId() {
            return actInstId;
        }

        public void setActInstId(String actInstId) {
            this.actInstId = actInstId;
        }

        public String getAttchDisposeStatus() {
            return attchDisposeStatus;
        }

        public void setAttchDisposeStatus(String attchDisposeStatus) {
            this.attchDisposeStatus = attchDisposeStatus;
        }

        public String getAttchFileName() {
            return attchFileName;
        }

        public void setAttchFileName(String attchFileName) {
            this.attchFileName = attchFileName;
        }

        public String getAttchFilePath() {
            return attchFilePath;
        }

        public void setAttchFilePath(String attchFilePath) {
            this.attchFilePath = attchFilePath;
        }

        public String getAttchType() {
            return attchType;
        }

        public void setAttchType(String attchType) {
            this.attchType = attchType;
        }

        public String getAttchUsage() {
            return attchUsage;
        }

        public void setAttchUsage(String attchUsage) {
            this.attchUsage = attchUsage;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
