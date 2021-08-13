package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;


public class UserBase implements Serializable {



    private String id;
    private String profile;
    private String account;
    private String password;
    private String username;
    private Object secret;
    private String phone;
    private String email;
    private String idcard;
    private Object permissions;
    private String userType;
    private DefaultDepartmentBean defaultDepartment;
    private DefaultRoleBean defaultRole;
    private UserSupervisorExtBean userSupervisorExt;
    private CustomAppInfoBean customAppInfo;
    private String areaCoordinateStr;
    private List<RolesBean> roles;
    private List<DepartmentsBean> departments;



    private String pwd;



    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getSecret() {
        return secret;
    }

    public void setSecret(Object secret) {
        this.secret = secret;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public DefaultDepartmentBean getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(DefaultDepartmentBean defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public DefaultRoleBean getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(DefaultRoleBean defaultRole) {
        this.defaultRole = defaultRole;
    }

    public UserSupervisorExtBean getUserSupervisorExt() {
        return userSupervisorExt;
    }

    public void setUserSupervisorExt(UserSupervisorExtBean userSupervisorExt) {
        this.userSupervisorExt = userSupervisorExt;
    }

    public CustomAppInfoBean getCustomAppInfo() {
        return customAppInfo;
    }

    public void setCustomAppInfo(CustomAppInfoBean customAppInfo) {
        this.customAppInfo = customAppInfo;
    }

    public String getAreaCoordinateStr() {
        return areaCoordinateStr;
    }

    public void setAreaCoordinateStr(String areaCoordinateStr) {
        this.areaCoordinateStr = areaCoordinateStr;
    }

    public List<RolesBean> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesBean> roles) {
        this.roles = roles;
    }

    public List<DepartmentsBean> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentsBean> departments) {
        this.departments = departments;
    }

    public static class DefaultDepartmentBean {
        /**
         * id : 1004405
         * name : 光明区采集公司
         * code : 4403094405
         * defaulting : true
         * parentId : 2
         * maxUserNum : 2
         * projectChargerName :
         * projectChargerTel :
         * deptDuty : null
         * legalPersonName :
         * legalPersonTel :
         * companyAddress :
         * dbStatus : 1
         * orderNum : 0
         * deptAcceptevt : 1
         * deptParentsend : 1
         * remark :
         * areaCode : 440309
         * deptType : 2
         * isSystem : 0
         */

        private String id;
        private String name;
        private String code;
        private boolean defaulting;
        private Long parentId;
        private int maxUserNum;
        private String projectChargerName;
        private String projectChargerTel;
        private Object deptDuty;
        private String legalPersonName;
        private String legalPersonTel;
        private String companyAddress;
        private String dbStatus;
        private int orderNum;
        private String deptAcceptevt;
        private String deptParentsend;
        private String remark;
        private String areaCode;
        private String deptType;
        private String isSystem;

        private String gridId;
        private String workGridId;
        private String manageGridId;

        private String areaCode1;

        public String getAreaCode1() {
            return areaCode1;
        }

        public void setAreaCode1(String areaCode1) {
            this.areaCode1 = areaCode1;
        }

        public String getManageGridId() {
            return manageGridId;
        }

        public void setManageGridId(String manageGridId) {
            this.manageGridId = manageGridId;
        }

        public String getWorkGridId() {
            return workGridId;
        }

        public void setWorkGridId(String workGridId) {
            this.workGridId = workGridId;
        }

        public String getGridId() {
            return gridId;
        }

        public void setGridId(String gridId) {
            this.gridId = gridId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isDefaulting() {
            return defaulting;
        }

        public void setDefaulting(boolean defaulting) {
            this.defaulting = defaulting;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public int getMaxUserNum() {
            return maxUserNum;
        }

        public void setMaxUserNum(int maxUserNum) {
            this.maxUserNum = maxUserNum;
        }

        public String getProjectChargerName() {
            return projectChargerName;
        }

        public void setProjectChargerName(String projectChargerName) {
            this.projectChargerName = projectChargerName;
        }

        public String getProjectChargerTel() {
            return projectChargerTel;
        }

        public void setProjectChargerTel(String projectChargerTel) {
            this.projectChargerTel = projectChargerTel;
        }

        public Object getDeptDuty() {
            return deptDuty;
        }

        public void setDeptDuty(Object deptDuty) {
            this.deptDuty = deptDuty;
        }

        public String getLegalPersonName() {
            return legalPersonName;
        }

        public void setLegalPersonName(String legalPersonName) {
            this.legalPersonName = legalPersonName;
        }

        public String getLegalPersonTel() {
            return legalPersonTel;
        }

        public void setLegalPersonTel(String legalPersonTel) {
            this.legalPersonTel = legalPersonTel;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getDeptAcceptevt() {
            return deptAcceptevt;
        }

        public void setDeptAcceptevt(String deptAcceptevt) {
            this.deptAcceptevt = deptAcceptevt;
        }

        public String getDeptParentsend() {
            return deptParentsend;
        }

        public void setDeptParentsend(String deptParentsend) {
            this.deptParentsend = deptParentsend;
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



        public String getDeptType() {
            return deptType;
        }

        public void setDeptType(String deptType) {
            this.deptType = deptType;
        }

        public String getIsSystem() {
            return isSystem;
        }

        public void setIsSystem(String isSystem) {
            this.isSystem = isSystem;
        }
    }

    public static class DefaultRoleBean {
        /**
         * id : 1128116518366773250
         * code : district_supervisor
         * name : 区监督员
         * defaulting : false
         * category : 2
         * grade : 3
         */

        private String id;
        private String code;
        private String name;
        private boolean defaulting;
        private String category;
        private int grade;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDefaulting() {
            return defaulting;
        }

        public void setDefaulting(boolean defaulting) {
            this.defaulting = defaulting;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }

    public static class UserSupervisorExtBean {
        /**
         * id : 1174142739760074753
         * createId : 1000000000000000001
         * createTime : 2019-09-18 10:06:58
         * updateId : null
         * updateTime : 2019-10-23 14:03:28
         * userId : 1174142738896048129
         * deptId : 1004405
         * address : null
         * supervisorType : 0
         * wordCard : null
         * pdaId : fb34e56cab976181
         * pdaBrand : HONOR
         * pdaType : BKK-AL10
         * pdaVersion : V2.1.13_191023_test
         * isEvaluate : 1
         * job : null
         * deptName : 光明区采集公司
         * areaName : 光明区
         * userName : yj
         * loginName : yj
         * userMobile : 18573181058
         * userDbStatus : 1
         * user : {"id":"1174142738896048129","createId":"1000000000000000001","createTime":"2019-09-18 10:06:58","updateId":null,"updateTime":"2019-10-23 14:03:28","userName":"yj","loginName":"yj","userMobile":"18573181058","userTelephone":"","userEmail":"","identityId":"","dbStatus":"1","userPwd":"$2a$10$/rELfdivur.LiBila.y7Cu.X3chcU9t5.mAX5/0QoNmUuyXZmZ.la","imageUrl":"5d871df8a3c1de3ee4cd2234.jpg","orderNum":0,"timeLimit":null,"remark":"","areaCode":"440309","sex":"1","unionId":null,"registerSource":"1","userType":"2","isSystem":"0","centerAccount":null,"centerPassword":null,"defaultDeptId":"1004405","auditStatus":"2","auditUserId":null,"auditTime":null,"auditSuggest":null}
         * online : 1
         * onlineTime : 0天0小时43分0秒
         * gps : 1
         * gpsLastOnlinePosition : [112.87475,28.229471]
         * gpsLastOnlineTime : 2019-10-23 15:14:49
         * roleId : 1128116518366773250
         * roleName : 区监督员
         * userContractFile : []
         */

        private String id;
        private String createId;
        private String createTime;
        private Object updateId;
        private String updateTime;
        private String userId;
        private String deptId;
        private Object address;
        private String supervisorType;
        private Object wordCard;
        private String pdaId;
        private String pdaBrand;
        private String pdaType;
        private String pdaVersion;
        private String isEvaluate;
        private Object job;
        private String deptName;
        private String areaName;
        private String userName;
        private String loginName;
        private String userMobile;
        private String userDbStatus;
        private UserBean user;
        private String online;
        private String onlineTime;
        private String gps;
        private String gpsLastOnlinePosition;
        private String gpsLastOnlineTime;
        private String roleId;
        private String roleName;
        private List<?> userContractFile;

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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
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

        public List<?> getUserContractFile() {
            return userContractFile;
        }

        public void setUserContractFile(List<?> userContractFile) {
            this.userContractFile = userContractFile;
        }

        public static class UserBean {
            /**
             * id : 1174142738896048129
             * createId : 1000000000000000001
             * createTime : 2019-09-18 10:06:58
             * updateId : null
             * updateTime : 2019-10-23 14:03:28
             * userName : yj
             * loginName : yj
             * userMobile : 18573181058
             * userTelephone :
             * userEmail :
             * identityId :
             * dbStatus : 1
             * userPwd : $2a$10$/rELfdivur.LiBila.y7Cu.X3chcU9t5.mAX5/0QoNmUuyXZmZ.la
             * imageUrl : 5d871df8a3c1de3ee4cd2234.jpg
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
             * auditStatus : 2
             * auditUserId : null
             * auditTime : null
             * auditSuggest : null
             */

            private String id;
            private String createId;
            private String createTime;
            private Object updateId;
            private String updateTime;
            private String userName;
            private String loginName;
            private String userMobile;
            private String userTelephone;
            private String userEmail;
            private String identityId;
            private String dbStatus;
            private String userPwd;
            private String imageUrl;
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

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
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

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
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

    public static class CustomAppInfoBean {
        /**
         * code : null
         * name : null
         * redirectUri : null
         * accessTokenValidity : null
         * lockLoginUser : false
         * lockLoginIp : false
         * openKeepOnline : false
         * tenantId : null
         */

        private Object code;
        private Object name;
        private Object redirectUri;
        private Object accessTokenValidity;
        private boolean lockLoginUser;
        private boolean lockLoginIp;
        private boolean openKeepOnline;
        private Object tenantId;

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(Object redirectUri) {
            this.redirectUri = redirectUri;
        }

        public Object getAccessTokenValidity() {
            return accessTokenValidity;
        }

        public void setAccessTokenValidity(Object accessTokenValidity) {
            this.accessTokenValidity = accessTokenValidity;
        }

        public boolean isLockLoginUser() {
            return lockLoginUser;
        }

        public void setLockLoginUser(boolean lockLoginUser) {
            this.lockLoginUser = lockLoginUser;
        }

        public boolean isLockLoginIp() {
            return lockLoginIp;
        }

        public void setLockLoginIp(boolean lockLoginIp) {
            this.lockLoginIp = lockLoginIp;
        }

        public boolean isOpenKeepOnline() {
            return openKeepOnline;
        }

        public void setOpenKeepOnline(boolean openKeepOnline) {
            this.openKeepOnline = openKeepOnline;
        }

        public Object getTenantId() {
            return tenantId;
        }

        public void setTenantId(Object tenantId) {
            this.tenantId = tenantId;
        }
    }

    public static class RolesBean {
        /**
         * id : 1128116518366773250
         * code : district_supervisor
         * name : 区监督员
         * defaulting : false
         * category : 2
         * grade : 3
         */

        private String id;
        private String code;
        private String name;
        private boolean defaulting;
        private String category;
        private int grade;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDefaulting() {
            return defaulting;
        }

        public void setDefaulting(boolean defaulting) {
            this.defaulting = defaulting;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }

    public static class DepartmentsBean {
        /**
         * id : 1004405
         * name : 光明区采集公司
         * code : 4403094405
         * defaulting : true
         * parentId : 2
         * maxUserNum : 2
         * projectChargerName :
         * projectChargerTel :
         * deptDuty : null
         * legalPersonName :
         * legalPersonTel :
         * companyAddress :
         * dbStatus : 1
         * orderNum : 0
         * deptAcceptevt : 1
         * deptParentsend : 1
         * remark :
         * areaCode : 440309
         * deptType : 2
         * isSystem : 0
         */

        private String id;
        private String name;
        private String code;
        private boolean defaulting;
        private Long parentId;
        private int maxUserNum;
        private String projectChargerName;
        private String projectChargerTel;
        private Object deptDuty;
        private String legalPersonName;
        private String legalPersonTel;
        private String companyAddress;
        private String dbStatus;
        private int orderNum;
        private String deptAcceptevt;
        private String deptParentsend;
        private String remark;
        private String areaCode;
        private String deptType;
        private String isSystem;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isDefaulting() {
            return defaulting;
        }

        public void setDefaulting(boolean defaulting) {
            this.defaulting = defaulting;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public int getMaxUserNum() {
            return maxUserNum;
        }

        public void setMaxUserNum(int maxUserNum) {
            this.maxUserNum = maxUserNum;
        }

        public String getProjectChargerName() {
            return projectChargerName;
        }

        public void setProjectChargerName(String projectChargerName) {
            this.projectChargerName = projectChargerName;
        }

        public String getProjectChargerTel() {
            return projectChargerTel;
        }

        public void setProjectChargerTel(String projectChargerTel) {
            this.projectChargerTel = projectChargerTel;
        }

        public Object getDeptDuty() {
            return deptDuty;
        }

        public void setDeptDuty(Object deptDuty) {
            this.deptDuty = deptDuty;
        }

        public String getLegalPersonName() {
            return legalPersonName;
        }

        public void setLegalPersonName(String legalPersonName) {
            this.legalPersonName = legalPersonName;
        }

        public String getLegalPersonTel() {
            return legalPersonTel;
        }

        public void setLegalPersonTel(String legalPersonTel) {
            this.legalPersonTel = legalPersonTel;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getDeptAcceptevt() {
            return deptAcceptevt;
        }

        public void setDeptAcceptevt(String deptAcceptevt) {
            this.deptAcceptevt = deptAcceptevt;
        }

        public String getDeptParentsend() {
            return deptParentsend;
        }

        public void setDeptParentsend(String deptParentsend) {
            this.deptParentsend = deptParentsend;
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

        public String getDeptType() {
            return deptType;
        }

        public void setDeptType(String deptType) {
            this.deptType = deptType;
        }

        public String getIsSystem() {
            return isSystem;
        }

        public void setIsSystem(String isSystem) {
            this.isSystem = isSystem;
        }
    }
}
