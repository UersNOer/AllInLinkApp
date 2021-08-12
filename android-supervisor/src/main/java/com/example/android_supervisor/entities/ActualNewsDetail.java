package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dw on 2019/6/28.
 */
public class ActualNewsDetail implements Serializable {

    /**
     * id : 1144499124867358722
     * createId : 1070143724081586177
     * createTime : 2019-06-28 14:53:50
     * updateId : null
     * updateTime : 2019-06-28 14:53:50
     * newsType : 1
     * title : 同一人
     * content : <h1 class="ql-align-center"><strong>么么么么么</strong></h1><p>么么哒</p>
     * releaseStatus : 1
     * releaseDate : 2019-06-28 14:53:50
     * dbStatus : 1
     * roleName : 监督员
     * roleId : 1078920920731611137
     * files : [{"url":"http://192.168.20.72:5004/files/5d15b95ef7e5ae2bc42b023a","name":"TIM图片20190628100712.png"}]
     * roleIdList : ["1078920920731611137"]
     */

    private String id;
    private String createId;
    private String createTime;
    private Object updateId;
    private String updateTime;
    private String newsType;
    private String title;
    private String content;
    private int releaseStatus;
    private String releaseDate;
    private String dbStatus;
    private String roleName;
    private String roleId;
    private List<FilesBean> files;
    private List<String> roleIdList;

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

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public static class FilesBean {
        /**
         * url : http://192.168.20.72:5004/files/5d15b95ef7e5ae2bc42b023a
         * name : TIM图片20190628100712.png
         */

        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
