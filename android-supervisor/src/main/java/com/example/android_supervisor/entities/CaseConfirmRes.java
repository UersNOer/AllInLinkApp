package com.example.android_supervisor.entities;

import java.util.List;

public class CaseConfirmRes  {


    private List<CaseSecondaryReportBean> case_secondary_report_url;
    private List<CaseSecondaryReportBean> case_secondary_report_valid;


    public List<CaseSecondaryReportBean> getCase_secondary_report_url() {
        return case_secondary_report_url;
    }

    public void setCase_secondary_report_url(List<CaseSecondaryReportBean> case_secondary_report_url) {
        this.case_secondary_report_url = case_secondary_report_url;
    }


    public List<CaseSecondaryReportBean> getCase_secondary_report_valid() {
        return case_secondary_report_valid;
    }

    public void setCase_secondary_report_valid(List<CaseSecondaryReportBean> case_secondary_report_valid) {
        this.case_secondary_report_valid = case_secondary_report_valid;

    }

    public static class CaseSecondaryReportBean {
        /**
         * id : 1134350764332154883
         * createId : 1070143724081586177
         * createTime : 2019-10-21 11:11:37
         * updateId : 1070143724081586177
         * updateTime : 2019-10-21 11:12:47
         * configName : 案件二次上报的地址
         * configKey : case_secondary_report_url
         * configValue : https://zhcg.cszhx.top
         * configType : 1
         * remark : 案件二次上报的地址
         * dbStatus : 1
         */

        private String id;
        private String createId;
        private String createTime;
        private String updateId;
        private String updateTime;
        private String configName;
        private String configKey;
        private String configValue;
        private String configType;
        private String remark;
        private String dbStatus;

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


        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;

        }


        public String getConfigKey() {
            return configKey;
        }

        public void setConfigKey(String configKey) {
            this.configKey = configKey;

        }


        public String getConfigValue() {
            return configValue;
        }

        public void setConfigValue(String configValue) {
            this.configValue = configValue;

        }


        public String getConfigType() {
            return configType;
        }

        public void setConfigType(String configType) {
            this.configType = configType;

        }


        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;

        }


        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;

        }
    }

}
