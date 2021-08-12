package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/9/18.
 */
public class UpdatePdaPara implements Serializable {


    public String imageUrl;
    /**
     * dbStatus : 1
     * defaultDeptId : 1004405
     * id : 1170214710334271490
     * identityId :
     * loginName : gmqcjy
     * orderNum : 0
     * remark :
     * roleIds : ["1078920920731611137"]
     * sex : 1
     * supervisorType : 0
     * userEmail :
     * userMobile : 13187014992
     * userName : 光明区第一采集员
     * userSupervisorExt : {"deptId":"1004405","id":"1170214710581735426","isEvaluate":"1","pdaBrand":"aaa","pdaId":"ddd","pdaType":"ccc","pdaVersion":"bbbxxx","supervisorType":"0","userId":"1170214710334271490"}
     * userTelephone :
     */

    private String dbStatus;
    private String defaultDeptId;
    private String id;
    private String identityId;
    private String loginName;
    private int orderNum;
    private String remark;
    private String sex;
    private String supervisorType;
    private String userEmail;
    private String userMobile;
    private String userName;
    /**
     * deptId : 1004405
     * id : 1170214710581735426
     * isEvaluate : 1
     * pdaBrand : aaa
     * pdaId : ddd
     * pdaType : ccc
     * pdaVersion : bbbxxx
     * supervisorType : 0
     * userId : 1170214710334271490
     */

    private UserSupervisorExtBean userSupervisorExt;
    private String userTelephone;
    private List<String> roleIds;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getDefaultDeptId() {
        return defaultDeptId;
    }

    public void setDefaultDeptId(String defaultDeptId) {
        this.defaultDeptId = defaultDeptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSupervisorType() {
        return supervisorType;
    }

    public void setSupervisorType(String supervisorType) {
        this.supervisorType = supervisorType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserSupervisorExtBean getUserSupervisorExt() {
        return userSupervisorExt;
    }

    public void setUserSupervisorExt(UserSupervisorExtBean userSupervisorExt) {
        this.userSupervisorExt = userSupervisorExt;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public static class UserSupervisorExtBean {
        private String deptId;
        private String id;
        private String isEvaluate;
        private String pdaBrand;
        private String pdaId;
        private String pdaType;
        private String pdaVersion;
        private String supervisorType;
        private String userId;

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsEvaluate() {
            return isEvaluate;
        }

        public void setIsEvaluate(String isEvaluate) {
            this.isEvaluate = isEvaluate;
        }

        public String getPdaBrand() {
            return pdaBrand;
        }

        public void setPdaBrand(String pdaBrand) {
            this.pdaBrand = pdaBrand;
        }

        public String getPdaId() {
            return pdaId;
        }

        public void setPdaId(String pdaId) {
            this.pdaId = pdaId;
        }

        public String getPdaType() {
            return pdaType;
        }

        public void setPdaType(String pdaType) {
            this.pdaType = pdaType;
        }

        public String getPdaVersion() {
            return pdaVersion;
        }

        public void setPdaVersion(String pdaVersion) {
            this.pdaVersion = pdaVersion;
        }

        public String getSupervisorType() {
            return supervisorType;
        }

        public void setSupervisorType(String supervisorType) {
            this.supervisorType = supervisorType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
