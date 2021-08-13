package com.example.android_supervisor.entities;

/**
 * Created by dw on 2019/6/28.
 */
public class ActualNews {


    /**
     * id : 1143363571044225026
     * createId : 1070143724081586177
     * createTime : 2019-06-25 11:41:33
     * updateId : null
     * updateTime : 2019-06-25 11:41:34
     * newsType : 0
     * title : 1221312321
     * content : <p>123123123</p>
     * releaseStatus : 1
     * releaseDate : 2019-06-25 11:41:33
     * dbStatus : 1
     * roleName : 系统管理员,派遣员,受理员,值班长
     * roleId : 1078168416892809218,1078169108281880578,1078169173092265985,1078169244777115650
     */

    private String id;
    private String createId;
    private String createTime;
    private String updateId;
    private String updateTime;
    private String newsType;
    private String title;
    private String content;
    private int releaseStatus;
    private String releaseDate;
    private String dbStatus;
    private String roleName;
    private String roleId;

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

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
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

    public int getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(int releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
