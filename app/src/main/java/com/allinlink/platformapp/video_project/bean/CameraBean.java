package com.allinlink.platformapp.video_project.bean;

import com.unistrong.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class CameraBean {

    /**
     * totalRecord : 48
     * currentPage : 1
     * pageSize : 10
     * datas : [{"gid":"1","cameraCode":"33333333333333","cameraName":"一号机","cameraGroup":"2020102711335500006","tenantInfo":"2020102613335700001","ipAddress":"12.0.100.98","port":"5546","userName":"admin","userPsw":"dh123456","standardType":"onvif","factory":"海康","managerRest":null,"mediadisRet":"2020110214025500005","online":"1032","onlineDuration":null,"onlineLabel":"不在线","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"海康","tenantName":"武汉合众思壮"},{"gid":"2020102816494500005","cameraCode":"1122","cameraName":"三号机","cameraGroup":"一号组","tenantInfo":"2020102613335700001","ipAddress":"45.54.54.45","port":"5546","userName":"admin","userPsw":"dh123456","standardType":"onvif","factory":"大华","managerRest":"","mediadisRet":"2020110214025500005","online":"1","onlineDuration":null,"onlineLabel":"1","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"大华","tenantName":"武汉合众思壮"},{"gid":"2020102819434000000","cameraCode":"2312313","cameraName":"六号机","cameraGroup":null,"tenantInfo":"2020102613335700001","ipAddress":"23.34.45.56","port":"5546","userName":"admin","userPsw":"dh123456","standardType":null,"factory":"海康","managerRest":null,"mediadisRet":"2020110215263600014","online":"1031","onlineDuration":null,"onlineLabel":"在线","onlineDurationLabel":"19天22时13分","lastOnline":"2020-10-29 12:00:00","factoryLabel":"海康","tenantName":"武汉合众思壮"},{"gid":"2020110213514300009","cameraCode":"1243242","cameraName":"王小黑","cameraGroup":null,"tenantInfo":"2020102613335700001","ipAddress":"192.168.1.1","port":"5546","userName":"12","userPsw":"dh123456","standardType":null,"factory":"大华","managerRest":null,"mediadisRet":"2020110215263600014","online":null,"onlineDuration":null,"onlineLabel":"","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"大华","tenantName":"武汉合众思壮"},{"gid":"2020110518360100008","cameraCode":"231313","cameraName":"北京东方国信","cameraGroup":null,"tenantInfo":"2020102613335700001","ipAddress":"2.2.2.2","port":"8966","userName":"1","userPsw":"dh123456","standardType":null,"factory":"大华","managerRest":null,"mediadisRet":null,"online":null,"onlineDuration":null,"onlineLabel":"","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"大华","tenantName":"武汉合众思壮"},{"gid":"2020110519402300009","cameraCode":"6536462","cameraName":"安徽东方国信","cameraGroup":null,"tenantInfo":"2020102613335700001","ipAddress":"3.3.3.3","port":"68","userName":"2","userPsw":"dh123456","standardType":null,"factory":"海康","managerRest":null,"mediadisRet":null,"online":null,"onlineDuration":null,"onlineLabel":"","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"海康","tenantName":"武汉合众思壮"},{"gid":"2020102816514600006","cameraCode":"23233","cameraName":"三号机","cameraGroup":"一号组","tenantInfo":"2020102613342600002","ipAddress":"23.33.33.33","port":"5546","userName":"ad","userPsw":"dh123456","standardType":"onvif","factory":"大华","managerRest":"","mediadisRet":"2020110214025500006","online":"1032","onlineDuration":null,"onlineLabel":"不在线","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"大华","tenantName":"郑州合众思壮"},{"gid":"2020102816544500007","cameraCode":"232323","cameraName":"四号机","cameraGroup":"一号组","tenantInfo":"2020102613342600002","ipAddress":"23.23.23.23","port":"5546","userName":"ad","userPsw":"dh123456","standardType":"onvif","factory":"大华","managerRest":"","mediadisRet":"2020110214025500006","online":"1032","onlineDuration":null,"onlineLabel":"不在线","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"大华","tenantName":"郑州合众思壮"},{"gid":"2020102819214800007","cameraCode":"2312321231","cameraName":"五号机","cameraGroup":null,"tenantInfo":"2020102613342600002","ipAddress":"231.321.312.231","port":"5546","userName":"admin","userPsw":"dh123456","standardType":null,"factory":"海康","managerRest":null,"mediadisRet":"2020110215263600014","online":"1032","onlineDuration":null,"onlineLabel":"不在线","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"海康","tenantName":"郑州合众思壮"},{"gid":"2020110214010700000","cameraCode":"2132313","cameraName":"二号机","cameraGroup":null,"tenantInfo":"2020102613342600002","ipAddress":"23.323.23.32","port":"5546","userName":"admin","userPsw":"dh123456","standardType":null,"factory":"海康","managerRest":null,"mediadisRet":"2020110214025500005","online":null,"onlineDuration":null,"onlineLabel":"","onlineDurationLabel":"0天0时0分","lastOnline":null,"factoryLabel":"海康","tenantName":"郑州合众思壮"}]
     */

    private int totalRecord;
    private int currentPage;
    private int pageSize;
    private List<DatasBean> datas;

    @Override
    public String toString() {
        return "CameraBean{" +
                "totalRecord=" + totalRecord +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", datas=" + datas +
                '}';
    }

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

    public static class DatasBean implements Serializable {
        private MapMarkerBean.FeaturesBean latLng;

        public MapMarkerBean.FeaturesBean getLatLng() {
            return latLng;
        }

        public void setLatLng(MapMarkerBean.FeaturesBean latLng) {
            this.latLng = latLng;
        }

        /**
         * gid : 1
         * cameraCode : 33333333333333
         * cameraName : 一号机
         * cameraGroup : 2020102711335500006
         * tenantInfo : 2020102613335700001
         * ipAddress : 12.0.100.98
         * port : 5546
         * userName : admin
         * userPsw : dh123456
         * standardType : onvif
         * factory : 海康
         * managerRest : null
         * mediadisRet : 2020110214025500005
         * online : 1032
         * onlineDuration : null
         * onlineLabel : 不在线
         * onlineDurationLabel : 0天0时0分
         * lastOnline : null
         * factoryLabel : 海康
         * tenantName : 武汉合众思壮
         */

        private String gid;
        private String cameraCode;
        private String cameraName;
        private String cameraGroup;
        private String tenantInfo;
        private String ipAddress;
        private String port;
        private String userName;
        private String userPsw;
        private String standardType;
        private String factory;
        private Object managerRest;
        private String mediadisRet;
        private String online;
        private Object onlineDuration;
        private String onlineLabel;
        private String onlineDurationLabel;
        private Object lastOnline;
        private String factoryLabel;
        private String tenantName;

        @Override
        public String toString() {
            return "DatasBean{" +
                    "gid='" + gid + '\'' +
                    ", cameraCode='" + cameraCode + '\'' +
                    ", cameraName='" + cameraName + '\'' +
                    ", cameraGroup='" + cameraGroup + '\'' +
                    ", tenantInfo='" + tenantInfo + '\'' +
                    ", ipAddress='" + ipAddress + '\'' +
                    ", port='" + port + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPsw='" + userPsw + '\'' +
                    ", standardType='" + standardType + '\'' +
                    ", factory='" + factory + '\'' +
                    ", managerRest=" + managerRest +
                    ", mediadisRet='" + mediadisRet + '\'' +
                    ", online='" + online + '\'' +
                    ", onlineDuration=" + onlineDuration +
                    ", onlineLabel='" + onlineLabel + '\'' +
                    ", onlineDurationLabel='" + onlineDurationLabel + '\'' +
                    ", lastOnline=" + lastOnline +
                    ", factoryLabel='" + factoryLabel + '\'' +
                    ", tenantName='" + tenantName + '\'' +
                    '}';
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getCameraCode() {
            return cameraCode;
        }

        public void setCameraCode(String cameraCode) {
            this.cameraCode = cameraCode;
        }

        public String getCameraName() {
            return cameraName;
        }

        public void setCameraName(String cameraName) {
            this.cameraName = cameraName;
        }

        public String getCameraGroup() {
            return cameraGroup;
        }

        public void setCameraGroup(String cameraGroup) {
            this.cameraGroup = cameraGroup;
        }

        public String getTenantInfo() {
            return tenantInfo;
        }

        public void setTenantInfo(String tenantInfo) {
            this.tenantInfo = tenantInfo;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPsw() {
            return userPsw;
        }

        public void setUserPsw(String userPsw) {
            this.userPsw = userPsw;
        }

        public String getStandardType() {
            return standardType;
        }

        public void setStandardType(String standardType) {
            this.standardType = standardType;
        }

        public String getFactory() {
            return factory;
        }

        public void setFactory(String factory) {
            this.factory = factory;
        }

        public Object getManagerRest() {
            return managerRest;
        }

        public void setManagerRest(Object managerRest) {
            this.managerRest = managerRest;
        }

        public String getMediadisRet() {
            return mediadisRet;
        }

        public void setMediadisRet(String mediadisRet) {
            this.mediadisRet = mediadisRet;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public Object getOnlineDuration() {
            return onlineDuration;
        }

        public void setOnlineDuration(Object onlineDuration) {
            this.onlineDuration = onlineDuration;
        }

        public String getOnlineLabel() {
            return onlineLabel;
        }

        public void setOnlineLabel(String onlineLabel) {
            this.onlineLabel = onlineLabel;
        }

        public String getOnlineDurationLabel() {
            return onlineDurationLabel;
        }

        public void setOnlineDurationLabel(String onlineDurationLabel) {
            this.onlineDurationLabel = onlineDurationLabel;
        }

        public Object getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(Object lastOnline) {
            this.lastOnline = lastOnline;
        }

        public String getFactoryLabel() {
            return factoryLabel;
        }

        public void setFactoryLabel(String factoryLabel) {
            this.factoryLabel = factoryLabel;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }
    }
}
