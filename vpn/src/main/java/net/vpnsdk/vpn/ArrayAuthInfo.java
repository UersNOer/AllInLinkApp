package net.vpnsdk.vpn;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ArrayAuthInfo {
    public final static String error = "error";
    public final static String Tag = "ArrayAuthInfo";
    private final static String dev_type = "dev_type";
    private final static String def_method_idx = "def_method_idx";
    private final static String err_msd_id = "err_msd_id";
    private final static String rank_on = "rank_on";
    private final static String rank_method_idx = "rank_method_idx";
    private final static String aaa_method_num = "aaa_method_num";
    private final static String aaa_method = "aaa_method";
    private final static String name = "name";
    private final static String desc = "desc";
    private final static String server = "server";
    private final static String server_desc = "server_desc";
    private final static String type = "type";
    private final static String action = "action";
    private final static String cert_id_type = "cert_id_type";
    private final static String cert_id_value = "cert_id_value";
    private final static String multi_step_num = "multi_step_num";
    private final static String multi_steps = "multi_steps";
    private int mError;
    private int mDevType;
    private int mDefMethodIdx;
    private int mErrMsdId;
    private boolean mRankOn;
    private int mRankMethodIdx;
    private int mAAAMethodNum;
    private ArrayList<AAAMethodInternal> mMethods;

    private int mCurIdx = -1;

    public ArrayAuthInfo() {
        mMethods = new ArrayList<AAAMethodInternal>();
    }

    static ArrayAuthInfo getArrayAuthInfo(String resource) {
        ArrayAuthInfo auth = new ArrayAuthInfo();
        if ((resource == null) || (0 == resource.length())) {
            return auth;
        }
        JSONObject jobj;
        try {
            jobj = new JSONObject(resource);
            fillAuthInfo(jobj, auth);
        } catch (JSONException e) {
            Log.e(Tag, "JSONException " + e.getMessage());
        }
        return auth;
    }

    private static void fillAuthInfo(JSONObject jobj, ArrayAuthInfo auth) {
        try {
            auth.setError(jobj.getInt(error));
            auth.setDevType(jobj.getInt(dev_type));
            auth.setDefMethodIdx(jobj.getInt(def_method_idx));
            auth.setErrMsdId(jobj.getInt(err_msd_id));
            if (0 == jobj.getInt(rank_on)) {
                auth.setRankOn(false);
            } else {
                auth.setRankOn(true);
            }
            auth.setRankMethodIdx(jobj.getInt(rank_method_idx));
            auth.setAAAMethodNum(jobj.getInt(aaa_method_num));

            fillAuthMethods(jobj, auth);
        } catch (JSONException e) {
            Log.e(Tag, "JSONException " + e.getMessage());
        }
    }

    private static void fillAuthMethods(JSONObject jobj, ArrayAuthInfo auth) {
        try {
            JSONArray methods = jobj.getJSONArray(aaa_method);
            for (int i = 0; i < methods.length(); i++) {
                JSONObject jmethod = methods.getJSONObject(i);
                AAAMethodInternal m = new AAAMethodInternal();
                fillOneAuthMethod(jmethod, m);
                auth.addMethod(m);
            }
        } catch (JSONException e) {
            Log.e(Tag, "JSONException " + e.getMessage());
        }
    }

    private static void fillOneAuthMethod(JSONObject jobj, AAAMethodInternal method) {
        try {
            method.setName(jobj.getString(name));
            method.setDesc(jobj.getString(desc));
            method.setServerName(jobj.getString(server));
            method.setServerDesc(jobj.getString(server_desc));
            method.setType(jobj.getInt(type));
            method.setAction(jobj.getInt(action));
            method.setCertIdType(jobj.getInt(cert_id_type));
            method.setCertIdValue(jobj.getString(cert_id_value));
            method.setMultiStepNum(jobj.getInt(multi_step_num));

            JSONArray jmultimethods = jobj.getJSONArray(multi_steps);
            for (int i = 0; i < jmultimethods.length(); i++) {
                JSONObject jmethod = jmultimethods.getJSONObject(i);
                AAAMethodItem m = new AAAMethodItem();
                fillOneMultiMethod(jmethod, m);
                method.addMethod(m);
            }
        } catch (JSONException e) {
            Log.e(Tag, "JSONException " + e.getMessage());
        }
    }

    private static void fillOneMultiMethod(JSONObject jobj, AAAMethodItem multimethod) {
        try {
            multimethod.setServerName(jobj.getString(server));
            multimethod.setServerDesc(jobj.getString(server_desc));
            multimethod.setType(jobj.getInt(type));
            multimethod.setAction(jobj.getInt(action));
        } catch (JSONException e) {
            Log.e(Tag, "JSONException " + e.getMessage());
        }
    }

    private void addMethod(AAAMethodInternal m) {
        mMethods.add(m);
    }

    public AAAMethodInternal getMethod() {
        if ((mCurIdx < 0) || (mCurIdx > mMethods.size() - 1)) {
            return null;
        }
        return mMethods.get(mCurIdx);
    }

    public boolean moveToNext() {
        mCurIdx += 1;
        return mCurIdx < mMethods.size();
    }

    public AAAMethodInternal getMethod(int index) {
        if ((index < 0) || (index > mMethods.size() - 1)) {
            return null;
        }
        return mMethods.get(index);
    }

    public AAAMethodInternal getMethod(String name) {
        for (int i = 0; i < mMethods.size(); i++) {
            if (mMethods.get(i).getName() != null) {
                if (mMethods.get(i).getName().equals(name)) {
                    return mMethods.get(i);
                }
            }
        }
        return null;
    }

    public void gotoFirst() {
        mCurIdx = -1;
    }

    public int getCount() {
        return mMethods.size();
    }

    public int getError() {
        return mError;
    }

    private void setError(int error) {
        mError = error;
    }

    public int getDevType() {
        return mDevType;
    }

    private void setDevType(int devType) {
        mDevType = devType;
    }

    public int getDefMethodIdx() {
        return mDefMethodIdx;
    }

    private void setDefMethodIdx(int defMethodIdx) {
        mDefMethodIdx = defMethodIdx;
    }

    public int getErrMsdId() {
        return mErrMsdId;
    }

    private void setErrMsdId(int errMsdId) {
        mErrMsdId = errMsdId;
    }

    public boolean isRankOn() {
        return mRankOn;
    }

    private void setRankOn(boolean rankOn) {
        mRankOn = rankOn;
    }

    public int getRankMethodIdx() {
        return mRankMethodIdx;
    }

    private void setRankMethodIdx(int rankMethodIdx) {
        mRankMethodIdx = rankMethodIdx;
    }

    public int getAAAMethodNum() {
        return mAAAMethodNum;
    }

    private void setAAAMethodNum(int aAAMethodNum) {
        mAAAMethodNum = aAAMethodNum;
    }

    private boolean IsDeviceIdOnly() {
        if (1 == getCount()) {
            AAAMethodInternal method = getMethod(0);
            if ((0 == method.getMultiStepNum()) &&
                    (AuthType.AUTH_DEVID.getValue() == method.getType()) &&
                    (AuthAction.DEVID_BINDUSER.getValue() != method.getAction())) {
                return true;
            }
        }
        return false;
    }

    public enum AuthType {
        AUTH_LDAP(0), AUTH_LOCALDB(1), AUTH_RADIUS(2), AUTH_CERTIFICATE(3), AUTH_RADIUS_ACCOUNT(
                4), AUTH_KERVEROS(5), AUTH_EXTERNAL(6), AUTH_SMS(7), AUTH_DEVID(8), AUTH_SMX(9), AUTH_HTTP(10);

        private int value;

        private AuthType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum AuthAction {
        CERT_ANONYMOUS(0), CERT_CHALLENGE(1), DEVID_BINDUSER(2);

        private int value;

        private AuthAction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CertIdType {
        CERT_NO_ID(0), CERT_SHOW_ID(1), CERT_GET_ID(2);

        private int value;

        private CertIdType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
