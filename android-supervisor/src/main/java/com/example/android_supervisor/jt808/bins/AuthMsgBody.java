package com.example.android_supervisor.jt808.bins;


import com.example.android_supervisor.jt808.Constants;
import com.example.android_supervisor.jt808.utils.BCDUtils;

/**
 * @author wujie
 */
public class AuthMsgBody extends Binary {
    // 终端鉴权码
    private final VarChar authCode = new VarChar(16);
    // 用户id
    private final VarChar userId = new VarChar(10);

    public AuthMsgBody() {
    }

    public AuthMsgBody(byte[] bytes) {
        super(bytes);
    }

    public String getAuthCode() {
        return authCode.get(Constants.STRING_ENCODING);
    }

    public void setAuthCode(String authCode) {
        if (authCode != null) {
            this.authCode.set(authCode, Constants.STRING_ENCODING);
        }
    }

    public String getUserId() {
        return BCDUtils.bcd2Str(this.userId.get());
    }

    public void setUserId(String userId) {
        if (userId != null) {
            byte[] bcdUid = BCDUtils.str2Bcd(userId);
            this.userId.set(bcdUid);
        }
    }
}
