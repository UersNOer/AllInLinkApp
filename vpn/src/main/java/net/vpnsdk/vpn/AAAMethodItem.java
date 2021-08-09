package net.vpnsdk.vpn;

class AAAMethodItem {
    private String mServerName = "";
    private String mServerDesc = "";
    private int mType = 0;
    private int mAction = 0;

    public String getServerName() {
        return mServerName;
    }

    void setServerName(String server) {
        mServerName = server;
    }

    public String getServerDesc() {
        return mServerDesc;
    }

    void setServerDesc(String serverDesc) {
        mServerDesc = serverDesc;
    }

    public int getType() {
        return mType;
    }

    void setType(int type) {
        mType = type;
    }

    public int getAction() {
        return mAction;
    }

    void setAction(int action) {
        mAction = action;
    }
}
