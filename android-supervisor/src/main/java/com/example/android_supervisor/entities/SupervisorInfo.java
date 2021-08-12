package com.example.android_supervisor.entities;

import android.text.TextUtils;

/**
 * Created by Administrator on 2019/9/18.
 */
public class SupervisorInfo extends BasePara{


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String createId;
        private String createTime;
        private Object updateId;
        private Object updateTime;
        private String userId;
        private String deptId;
        private Object address;
        private String supervisorType;
        private Object wordCard;
        private String pdaId;
        private Object pdaBrand;
        private Object pdaType;
        private Object pdaVersion;
        private String isEvaluate;
        private Object job;
        private String deptName;
        private String areaName;
        private String userName;
        private String loginName;
        private String userMobile;
        private String userDbStatus;
        /**
         * id : 1174142738896048129
         * createId : 1000000000000000001
         * createTime : 2019-09-18 10:06:58
         * updateId : null
         * updateTime : null
         * userName : 杨杰
         * loginName : yj
         * userMobile : 18573181058
         * userTelephone :
         * userEmail :
         * identityId :
         * dbStatus : 1
         * userPwd : $2a$10$MSRSHF5Dqz8SSAjC5E32keptehblUZWfYwdbtA8XSTt8hfiSSrv.O
         * imageUrl : null
         * orderNum : 0
         * timeLimit : null
         * remark :
         * areaCode : 440309
         * sex : 1
         * unionId : null
         * registerSource : 1
         * userType : 2
         * isSystem : 0
         * centerAccount : null
         * centerPassword : null
         * defaultDeptId : 1004405
         * auditStatus : 0
         * auditUserId : null
         * auditTime : null
         * auditSuggest : null
         */

        private UserBean user;
        private String online;
        private String onlineTime;
        private String gps;
        private String gpsLastOnlinePosition;
        private String gpsLastOnlineTime;
        private String roleId;
        private String roleName;
        private Object userContractFile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public Object getUpdateId() {
            return updateId;
        }

        public void setUpdateId(Object updateId) {
            this.updateId = updateId;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getSupervisorType() {
            return supervisorType;
        }

        public void setSupervisorType(String supervisorType) {
            this.supervisorType = supervisorType;
        }

        public Object getWordCard() {
            return wordCard;
        }

        public void setWordCard(Object wordCard) {
            this.wordCard = wordCard;
        }

        public String getPdaId() {
            return pdaId;
        }

        public void setPdaId(String pdaId) {
            this.pdaId = pdaId;
        }

        public Object getPdaBrand() {
            return pdaBrand;
        }

        public void setPdaBrand(Object pdaBrand) {
            this.pdaBrand = pdaBrand;
        }

        public Object getPdaType() {
            return pdaType;
        }

        public void setPdaType(Object pdaType) {
            this.pdaType = pdaType;
        }

        public Object getPdaVersion() {
            return pdaVersion;
        }

        public void setPdaVersion(Object pdaVersion) {
            this.pdaVersion = pdaVersion;
        }

        public String getIsEvaluate() {
            return isEvaluate;
        }

        public void setIsEvaluate(String isEvaluate) {
            this.isEvaluate = isEvaluate;
        }

        public Object getJob() {
            return job;
        }

        public void setJob(Object job) {
            this.job = job;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public String getUserDbStatus() {
            return userDbStatus;
        }

        public void setUserDbStatus(String userDbStatus) {
            this.userDbStatus = userDbStatus;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getOnlineTime() {
            return onlineTime;
        }

        public void setOnlineTime(String onlineTime) {
            this.onlineTime = onlineTime;
        }

        public String getGps() {
            return gps;
        }

        public void setGps(String gps) {
            this.gps = gps;
        }

        public String getGpsLastOnlinePosition() {
            return gpsLastOnlinePosition;
        }

        public void setGpsLastOnlinePosition(String gpsLastOnlinePosition) {
            this.gpsLastOnlinePosition = gpsLastOnlinePosition;
        }

        public String getGpsLastOnlineTime() {
            return gpsLastOnlineTime;
        }

        public void setGpsLastOnlineTime(String gpsLastOnlineTime) {
            this.gpsLastOnlineTime = gpsLastOnlineTime;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public Object getUserContractFile() {
            return userContractFile;
        }

        public void setUserContractFile(Object userContractFile) {
            this.userContractFile = userContractFile;
        }

        public boolean isEmpty(String string){
            return TextUtils.isEmpty(string);
        }

        public static class UserBean {
            private String id;
            private String createId;
            private String createTime;
            private Object updateId;
            private Object updateTime;
            private String userName;
            private String loginName;
            private String userMobile;
            private String userTelephone;
            private String userEmail;
            private String identityId;
            private String dbStatus;
            private String userPwd;
            private Object imageUrl;
            private int orderNum;
            private Object timeLimit;
            private String remark;
            private String areaCode;
            private String sex;
            private Object unionId;
            private String registerSource;
            private String userType;
            private String isSystem;
            private Object centerAccount;
            private Object centerPassword;
            private String defaultDeptId;
            private String auditStatus;
            private Object auditUserId;
            private Object auditTime;
            private Object auditSuggest;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public Object getUpdateId() {
                return updateId;
            }

            public void setUpdateId(Object updateId) {
                this.updateId = updateId;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getLoginName() {
                return loginName;
            }

            public void setLoginName(String loginName) {
                this.loginName = loginName;
            }

            public String getUserMobile() {
                return userMobile;
            }

            public void setUserMobile(String userMobile) {
                this.userMobile = userMobile;
            }

            public String getUserTelephone() {
                return userTelephone;
            }

            public void setUserTelephone(String userTelephone) {
                this.userTelephone = userTelephone;
            }

            public String getUserEmail() {
                return userEmail;
            }

            public void setUserEmail(String userEmail) {
                this.userEmail = userEmail;
            }

            public String getIdentityId() {
                return identityId;
            }

            public void setIdentityId(String identityId) {
                this.identityId = identityId;
            }

            public String getDbStatus() {
                return dbStatus;
            }

            public void setDbStatus(String dbStatus) {
                this.dbStatus = dbStatus;
            }

            public String getUserPwd() {
                return userPwd;
            }

            public void setUserPwd(String userPwd) {
                this.userPwd = userPwd;
            }

            public Object getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(Object imageUrl) {
                this.imageUrl = imageUrl;
            }

            public int getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(int orderNum) {
                this.orderNum = orderNum;
            }

            public Object getTimeLimit() {
                return timeLimit;
            }

            public void setTimeLimit(Object timeLimit) {
                this.timeLimit = timeLimit;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public Object getUnionId() {
                return unionId;
            }

            public void setUnionId(Object unionId) {
                this.unionId = unionId;
            }

            public String getRegisterSource() {
                return registerSource;
            }

            public void setRegisterSource(String registerSource) {
                this.registerSource = registerSource;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getIsSystem() {
                return isSystem;
            }

            public void setIsSystem(String isSystem) {
                this.isSystem = isSystem;
            }

            public Object getCenterAccount() {
                return centerAccount;
            }

            public void setCenterAccount(Object centerAccount) {
                this.centerAccount = centerAccount;
            }

            public Object getCenterPassword() {
                return centerPassword;
            }

            public void setCenterPassword(Object centerPassword) {
                this.centerPassword = centerPassword;
            }

            public String getDefaultDeptId() {
                return defaultDeptId;
            }

            public void setDefaultDeptId(String defaultDeptId) {
                this.defaultDeptId = defaultDeptId;
            }

            public String getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(String auditStatus) {
                this.auditStatus = auditStatus;
            }

            public Object getAuditUserId() {
                return auditUserId;
            }

            public void setAuditUserId(Object auditUserId) {
                this.auditUserId = auditUserId;
            }

            public Object getAuditTime() {
                return auditTime;
            }

            public void setAuditTime(Object auditTime) {
                this.auditTime = auditTime;
            }

            public Object getAuditSuggest() {
                return auditSuggest;
            }

            public void setAuditSuggest(Object auditSuggest) {
                this.auditSuggest = auditSuggest;
            }
        }
    }

}
