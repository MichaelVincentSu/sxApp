package com.sx.yufs.sxapp.webapi.okhttp.para;

/**
 * 登录参数
 * Created by yufs on 2016/8/18.
 */
public class LoginPara extends ResponseSu{
    private static final long serialVersionUID = 1L;
    /**
     * 身份令牌
     */
    public String Token;
    /**
     * 用户名
     */
    public String Username;
    /**
     * 用户Id
     */
    public int Id;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
