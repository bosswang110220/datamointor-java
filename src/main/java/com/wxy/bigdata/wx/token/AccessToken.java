package com.wxy.bigdata.wx.token;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
public class AccessToken {
    private String token;//微信后台返回的token
    private int expiresIn;//失效时间

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}