package com.allinlink.platformapp.video_project.bean;

import java.util.List;

public class PlayBackBean {
    /**
     * SendTime : 2020-12-02 13:43:11
     * CountItems : 1
     * items : [{"ChannelId":"2020102216433100014","ChannelName":"通道1","Scn":"1111","StartTime":"2020-12-01 13:42:25","EndTime":"2020-12-02 13:42:31","CountServers":2,"servers":[{"ids":null,"sendTime":null,"items":null,"countItems":null,"channelId":null,"channelName":null,"scn":null,"startTime":null,"endTime":null,"servers":null,"channelBindinfoIds":null,"countServer":null,"serverIp":"172.16.96.165","serverPort":10090,"recordTime":"2020-12-01 16:05:22","status":"ON"},{"ids":null,"sendTime":null,"items":null,"countItems":null,"channelId":null,"channelName":null,"scn":null,"startTime":null,"endTime":null,"servers":null,"channelBindinfoIds":null,"countServer":null,"serverIp":"172.16.96.165","serverPort":10090,"recordTime":"2020-12-01 16:56:45","status":"ON"}]}]
     */

    private String SendTime;
    private int CountItems;
    private List<ItemsBean> items;

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public int getCountItems() {
        return CountItems;
    }

    public void setCountItems(int CountItems) {
        this.CountItems = CountItems;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * ChannelId : 2020102216433100014
         * ChannelName : 通道1
         * Scn : 1111
         * StartTime : 2020-12-01 13:42:25
         * EndTime : 2020-12-02 13:42:31
         * CountServers : 2
         * servers : [{"ids":null,"sendTime":null,"items":null,"countItems":null,"channelId":null,"channelName":null,"scn":null,"startTime":null,"endTime":null,"servers":null,"channelBindinfoIds":null,"countServer":null,"serverIp":"172.16.96.165","serverPort":10090,"recordTime":"2020-12-01 16:05:22","status":"ON"},{"ids":null,"sendTime":null,"items":null,"countItems":null,"channelId":null,"channelName":null,"scn":null,"startTime":null,"endTime":null,"servers":null,"channelBindinfoIds":null,"countServer":null,"serverIp":"172.16.96.165","serverPort":10090,"recordTime":"2020-12-01 16:56:45","status":"ON"}]
         */

        private String ChannelId;
        private String ChannelName;
        private String Scn;
        private String StartTime;
        private String EndTime;
        private int CountServers;
        private List<ServersBean> servers;

        public String getChannelId() {
            return ChannelId;
        }

        public void setChannelId(String ChannelId) {
            this.ChannelId = ChannelId;
        }

        public String getChannelName() {
            return ChannelName;
        }

        public void setChannelName(String ChannelName) {
            this.ChannelName = ChannelName;
        }

        public String getScn() {
            return Scn;
        }

        public void setScn(String Scn) {
            this.Scn = Scn;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public int getCountServers() {
            return CountServers;
        }

        public void setCountServers(int CountServers) {
            this.CountServers = CountServers;
        }

        public List<ServersBean> getServers() {
            return servers;
        }

        public void setServers(List<ServersBean> servers) {
            this.servers = servers;
        }

        public static class ServersBean {
            /**
             * ids : null
             * sendTime : null
             * items : null
             * countItems : null
             * channelId : null
             * channelName : null
             * scn : null
             * startTime : null
             * endTime : null
             * servers : null
             * channelBindinfoIds : null
             * countServer : null
             * serverIp : 172.16.96.165
             * serverPort : 10090
             * recordTime : 2020-12-01 16:05:22
             * status : ON
             */

            private Object ids;
            private Object sendTime;
            private Object items;
            private Object countItems;
            private Object channelId;
            private Object channelName;
            private Object scn;
            private Object startTime;
            private Object endTime;
            private Object servers;
            private Object channelBindinfoIds;
            private Object countServer;
            private String serverIp;
            private int serverPort;
            private String recordTime;
            private String status;

            public Object getIds() {
                return ids;
            }

            public void setIds(Object ids) {
                this.ids = ids;
            }

            public Object getSendTime() {
                return sendTime;
            }

            public void setSendTime(Object sendTime) {
                this.sendTime = sendTime;
            }

            public Object getItems() {
                return items;
            }

            public void setItems(Object items) {
                this.items = items;
            }

            public Object getCountItems() {
                return countItems;
            }

            public void setCountItems(Object countItems) {
                this.countItems = countItems;
            }

            public Object getChannelId() {
                return channelId;
            }

            public void setChannelId(Object channelId) {
                this.channelId = channelId;
            }

            public Object getChannelName() {
                return channelName;
            }

            public void setChannelName(Object channelName) {
                this.channelName = channelName;
            }

            public Object getScn() {
                return scn;
            }

            public void setScn(Object scn) {
                this.scn = scn;
            }

            public Object getStartTime() {
                return startTime;
            }

            public void setStartTime(Object startTime) {
                this.startTime = startTime;
            }

            public Object getEndTime() {
                return endTime;
            }

            public void setEndTime(Object endTime) {
                this.endTime = endTime;
            }

            public Object getServers() {
                return servers;
            }

            public void setServers(Object servers) {
                this.servers = servers;
            }

            public Object getChannelBindinfoIds() {
                return channelBindinfoIds;
            }

            public void setChannelBindinfoIds(Object channelBindinfoIds) {
                this.channelBindinfoIds = channelBindinfoIds;
            }

            public Object getCountServer() {
                return countServer;
            }

            public void setCountServer(Object countServer) {
                this.countServer = countServer;
            }

            public String getServerIp() {
                return serverIp;
            }

            public void setServerIp(String serverIp) {
                this.serverIp = serverIp;
            }

            public int getServerPort() {
                return serverPort;
            }

            public void setServerPort(int serverPort) {
                this.serverPort = serverPort;
            }

            public String getRecordTime() {
                return recordTime;
            }

            public void setRecordTime(String recordTime) {
                this.recordTime = recordTime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
