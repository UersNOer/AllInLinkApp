package com.allinlink.platformapp.video_project.bean;

import java.util.List;

public class OrderBean {

    /**
     * totalRecord : 15
     * currentPage : 1
     * pageSize : 10
     * datas : [{"gid":"2020112410004200000","createDate":"2020-11-24 10:00:42","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112410004500001","taskInfo":"2020112410004200000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 10:00:45"}]},{"gid":"2020112410053700000","createDate":"2020-11-24 10:05:36","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112410054000001","taskInfo":"2020112410053700000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 10:05:39"}]},{"gid":"2020112410064600002","createDate":"2020-11-24 10:06:46","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112410064600003","taskInfo":"2020112410064600002","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 10:06:46"}]},{"gid":"2020112411010800000","createDate":"2020-11-24 11:01:08","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411011100001","taskInfo":"2020112411010800000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:01:10"}]},{"gid":"2020112411014700002","createDate":"2020-11-24 11:01:47","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411014700003","taskInfo":"2020112411014700002","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:01:47"}]},{"gid":"2020112411032700000","createDate":"2020-11-24 11:03:27","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411032700001","taskInfo":"2020112411032700000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:03:27"}]},{"gid":"2020112411034700002","createDate":"2020-11-24 11:03:47","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411034700003","taskInfo":"2020112411034700002","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:03:47"}]},{"gid":"2020112411070200004","createDate":"2020-11-24 11:07:02","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411070200005","taskInfo":"2020112411070200004","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:07:02"}]},{"gid":"2020112411124800000","createDate":"2020-11-24 11:12:49","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411124800001","taskInfo":"2020112411124800000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:12:49"}]},{"gid":"2020112411131100002","createDate":"2020-11-24 11:13:12","sn":"1","status":"0","formType":"维护","hardwareCode":"22","address":"10.1.100.80","taskExplain":"遮挡请处理","docFiles":"","tenantInfo":"2020102613342600002","deviceType":"智慧城管","acceptor":null,"taskAssignDTOList":[{"gid":"2020112411131200003","taskInfo":"2020112411131100002","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 11:13:12"}]}]
     */

    private int totalRecord;
    private int currentPage;
    private int pageSize;
    private List<DatasBean> datas;

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * gid : 2020112410004200000
         * createDate : 2020-11-24 10:00:42
         * sn : 1
         * status : 0
         * formType : 维护
         * hardwareCode : 22
         * address : 10.1.100.80
         * taskExplain : 遮挡请处理
         * docFiles :
         * tenantInfo : 2020102613342600002
         * deviceType : 智慧城管
         * acceptor : null
         * taskAssignDTOList : [{"gid":"2020112410004500001","taskInfo":"2020112410004200000","acceptor":"2020112710093100000","status":"0","mark":"遮挡请处理","createDate":"2020-11-24 10:00:45"}]
         */

        private String gid;
        private String createDate;
        private String sn;
        private String status;
        private String formType;
        private String hardwareCode;
        private String address;
        private String taskExplain;
        private String docFiles;
        private String tenantInfo;
        private String deviceType;
        private String tenantName;
        private String formTypeLable;
        private Object acceptor;
        private String deviceTypeLable;
        private List<TaskAssignDTOListBean> taskAssignDTOList;

        public String getFormTypeLable() {
            return formTypeLable;
        }

        public void setFormTypeLable(String formTypeLable) {
            this.formTypeLable = formTypeLable;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getDeviceTypeLable() {
            return deviceTypeLable;
        }

        public void setDeviceTypeLable(String deviceTypeLable) {
            this.deviceTypeLable = deviceTypeLable;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFormType() {
            return formType;
        }

        public void setFormType(String formType) {
            this.formType = formType;
        }

        public String getHardwareCode() {
            return hardwareCode;
        }

        public void setHardwareCode(String hardwareCode) {
            this.hardwareCode = hardwareCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTaskExplain() {
            return taskExplain;
        }

        public void setTaskExplain(String taskExplain) {
            this.taskExplain = taskExplain;
        }

        public String getDocFiles() {
            return docFiles;
        }

        public void setDocFiles(String docFiles) {
            this.docFiles = docFiles;
        }

        public String getTenantInfo() {
            return tenantInfo;
        }

        public void setTenantInfo(String tenantInfo) {
            this.tenantInfo = tenantInfo;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public Object getAcceptor() {
            return acceptor;
        }

        public void setAcceptor(Object acceptor) {
            this.acceptor = acceptor;
        }

        public List<TaskAssignDTOListBean> getTaskAssignDTOList() {
            return taskAssignDTOList;
        }

        public void setTaskAssignDTOList(List<TaskAssignDTOListBean> taskAssignDTOList) {
            this.taskAssignDTOList = taskAssignDTOList;
        }

        public static class TaskAssignDTOListBean {
            /**
             * gid : 2020112410004500001
             * taskInfo : 2020112410004200000
             * acceptor : 2020112710093100000
             * status : 0
             * mark : 遮挡请处理
             * createDate : 2020-11-24 10:00:45
             */

            private String gid;
            private String taskInfo;
            private String acceptor;
            private String status;
            private String mark;
            private String createDate;

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getTaskInfo() {
                return taskInfo;
            }

            public void setTaskInfo(String taskInfo) {
                this.taskInfo = taskInfo;
            }

            public String getAcceptor() {
                return acceptor;
            }

            public void setAcceptor(String acceptor) {
                this.acceptor = acceptor;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }
        }
    }
}
