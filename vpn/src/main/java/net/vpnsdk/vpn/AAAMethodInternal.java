package net.vpnsdk.vpn;

import net.vpnsdk.vpn.ArrayAuthInfo.AuthAction;
import net.vpnsdk.vpn.ArrayAuthInfo.AuthType;
import net.vpnsdk.vpn.ArrayAuthInfo.CertIdType;

import java.util.ArrayList;

class AAAMethodInternal extends AAAMethodItem {
    private String mName = "";
    private String mDesc = "";
    private int mCertIdType;
    private String mCertIdValue = "";
    private int mMultiStepNum;
    private ArrayList<AAAMethodItem> mMultiSteps;
    private int mCurIdx = -1;

    public AAAMethodInternal() {
        mMultiSteps = new ArrayList<AAAMethodItem>();
    }

    public void addMethod(AAAMethodItem m) {
        mMultiSteps.add(m);
    }

    public AAAMethodItem getMethod() {
        if ((mCurIdx < 0) || (mCurIdx > mMultiSteps.size() - 1)) {
            return null;
        }
        return mMultiSteps.get(mCurIdx);
    }

    public AAAMethodItem getMethod(int index) {
        if ((index < 0) || (index > mMultiSteps.size() - 1)) {
            return null;
        }
        return mMultiSteps.get(index);
    }

    public boolean moveToNext() {
        mCurIdx += 1;
        return mCurIdx < mMultiSteps.size();
    }

    public void gotoFirst() {
        mCurIdx = -1;
    }

    public int getCount() {
        return mMultiSteps.size();
    }

    public String getName() {
        return mName;
    }

    void setName(String name) {
        mName = name;
    }

    public String getDesc() {
        return mDesc;
    }

    void setDesc(String desc) {
        mDesc = desc;
    }

    public int getCertIdType() {
        return mCertIdType;
    }

    void setCertIdType(int certIdType) {
        mCertIdType = certIdType;
    }

    public String getCertIdValue() {
        return mCertIdValue;
    }

    void setCertIdValue(String certIdValue) {
        mCertIdValue = certIdValue;
    }

    public int getMultiStepNum() {
        return mMultiStepNum;
    }

    void setMultiStepNum(int multiStepNum) {
        mMultiStepNum = multiStepNum;
    }

    public boolean needPassword(int type, int action) {
        if (type > AuthType.AUTH_HTTP.getValue() || type < 0) {
            return true;
        }
        AuthType authType = AuthType.values()[type];
        AuthAction authAction = AuthAction.values()[action];

        if (authType == AuthType.AUTH_LDAP || authType == AuthType.AUTH_LOCALDB
                || authType == AuthType.AUTH_RADIUS
                || authType == AuthType.AUTH_RADIUS_ACCOUNT
                || authType == AuthType.AUTH_KERVEROS
                || authType == AuthType.AUTH_HTTP) {
            return true;
        }

        if (authType == AuthType.AUTH_CERTIFICATE
                && authAction == AuthAction.CERT_CHALLENGE) {
            return true;
        }
        return false;
    }

    public boolean needUsername(int type, int action) {
        if (type > AuthType.AUTH_HTTP.getValue() || type < 0) {
            return true;
        }
        AuthType authType = AuthType.values()[type];
        AuthAction authAction = AuthAction.values()[action];

        if (authType == AuthType.AUTH_LDAP || authType == AuthType.AUTH_LOCALDB
                || authType == AuthType.AUTH_RADIUS || authType == AuthType.AUTH_RADIUS_ACCOUNT
                || authType == AuthType.AUTH_KERVEROS
                || authType == AuthType.AUTH_SMX || authType == AuthType.AUTH_HTTP) {
            return true;
        }

        if (authType == AuthType.AUTH_CERTIFICATE && authAction == AuthAction.CERT_CHALLENGE) {
            return ((mCertIdType == CertIdType.CERT_GET_ID.ordinal())
                    || (mCertIdType == CertIdType.CERT_SHOW_ID.ordinal()));
        }

        if (authType == AuthType.AUTH_DEVID) {
            return (authAction == AuthAction.DEVID_BINDUSER);
        }
        return false;
    }
}
