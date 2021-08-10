package com.allinlink.platformapp.video_project.bean;

public class ControlBean {
    private String gid;
    private String cmd;

    public ControlBean(String gid, String cmd) {
        this.gid = gid;
        this.cmd = cmd;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
