package com.example.android_supervisor.jt808.bins;

import com.example.android_supervisor.jt808.Constants;

/**
 * @author wujie
 */
public class AuthMsgBody extends Binary {
    // 终端鉴权码
    private final VarChar authCode = new VarChar(16);

    public AuthMsgBody() {
    }

    public AuthMsgBody(byte[] bytes) {
        super(bytes);
    }

    public String getAuthCode() {
        return authCode.get(Constants.STRING_ENCODING);
    }

    public void setAuthCode(String authCode) {
        this.authCode.set(authCode, Constants.STRING_ENCODING);
    }
}
