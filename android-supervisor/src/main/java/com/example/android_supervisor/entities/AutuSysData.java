package com.example.android_supervisor.entities;

import java.util.List;

public class AutuSysData {

    private List<WorkGridSys> WorkGridSys;//网格数据
    private List<WorkPlanBean> WorkPlan;//排班计划


    public List<WorkGridSys> getWorkGridSys() {
        return WorkGridSys;
    }

    public void setWorkGridSys(List<WorkGridSys> workGridSys) {
        WorkGridSys = workGridSys;
    }

    public List<WorkPlanBean> getWorkPlan() {
        return WorkPlan;
    }

    public void setWorkPlan(List<WorkPlanBean> workPlan) {
        WorkPlan = workPlan;
    }

    public static class WorkPlanBean {
        /**
         * areaCode : 440309001007
         * beginTime : 2019-11-06 09:00
         * createId : 1000000000000000001
         * createTime : 2019-11-05 19:39:54
         * dbStatus : 1
         * endTime : 2019-11-06 11:50
         * gridName : A0
         * id : 1191681539771359233
         * roleClass : 2
         * schId : 1184650146340048897
         * schName : 上午班
         * userId : 1176680734874714113
         * userName : 深圳市第一采集员
         * workDate : 2019-11-06
         * workGridCode : 440309001007A0
         */

        private String areaCode;
        private String beginTime;
        private String createId;
        private String createTime;
        private String dbStatus;
        private String endTime;
        private String gridName;
        private String id;
        private String roleClass;
        private String schId;
        private String schName;
        private String userId;
        private String userName;
        private String workDate;
        private String workGridCode;

        private boolean isCanClock = false;

        public boolean isCanClock() {
            return isCanClock;
        }

        public void setCanClock(boolean canClock) {
            isCanClock = canClock;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoleClass() {
            return roleClass;
        }

        public void setRoleClass(String roleClass) {
            this.roleClass = roleClass;
        }

        public String getSchId() {
            return schId;
        }

        public void setSchId(String schId) {
            this.schId = schId;
        }

        public String getSchName() {
            return schName;
        }

        public void setSchName(String schName) {
            this.schName = schName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }

        public String getWorkGridCode() {
            return workGridCode;
        }

        public void setWorkGridCode(String workGridCode) {
            this.workGridCode = workGridCode;
        }
    }
}
