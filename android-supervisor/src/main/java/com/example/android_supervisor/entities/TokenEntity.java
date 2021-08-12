package com.example.android_supervisor.entities;


/**
 * @author wujie
 */
public class TokenEntity {

    /**
     * access_token : ec61e7a8-2c4c-48a8-8f4f-9c64097ecfa9
     * token_type : bearer
     * scope : app
     */

    private String access_token;
    private String token_type;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
