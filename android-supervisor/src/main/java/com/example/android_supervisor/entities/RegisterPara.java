package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author wujie
 */
public class RegisterPara {
    private String identityId; // 身份证号
    private String imageUrl;
    private String loginName;
    private String remark;
    private int sex;
    private String userEmail;
    private String userMobile;
    private String userName;
    private String userPwd;
    private String userTelephone;
    private long defaultDeptId;
//    private String[] deptIds;
    @SerializedName("userSupervisorExt")
    private ObExt obExt;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public long getDefaultDeptId() {
        return defaultDeptId;
    }

    public void setDefaultDeptId(long defaultDeptId) {
        this.defaultDeptId = defaultDeptId;
    }

//    public String[] getDeptIds() {
//        return deptIds;
//    }
//
//    public void setDeptIds(String[] deptIds) {
//        this.deptIds = deptIds;
//    }

    public ObExt getObExt() {
        return obExt;
    }

    public void setObExt(ObExt obExt) {
        this.obExt = obExt;
    }

    public static class ObExt {
        private String address;
        private String areaCode;
        private String wordCard;
        private String supervisorType; // 监督员类型:0一般队员、1分队长、2中队长、3大队长
        private String pdaId; // 终端标识码
        private String pdaBrand; // PDA品牌
        private String pdaType; // PDA型号
        private String pdaVersion; // PDA版本

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getWordCard() {
            return wordCard;
        }

        public void setWordCard(String wordCard) {
            this.wordCard = wordCard;
        }

        public String getPdaId() {
            return pdaId;
        }

        public void setPdaId(String pdaId) {
            this.pdaId = pdaId;
        }

        public String getSupervisorType() {
            return supervisorType;
        }

        public void setSupervisorType(String supervisorType) {
            this.supervisorType = supervisorType;
        }

        public String getPdaBrand() {
            return pdaBrand;
        }

        public void setPdaBrand(String pdaBrand) {
            this.pdaBrand = pdaBrand;
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
    }
}
