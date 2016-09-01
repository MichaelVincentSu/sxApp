package com.sx.yufs.sxapp.webapi;


import com.sx.yufs.sxapp.common.utils.CommonUtils;
import com.sx.yufs.sxapp.webapi.core.BaseRestApi;

import java.io.Serializable;

/**
 * 登录demo webapi
 * Created by yufs on 2016/4/25.
 */
public class Login extends BaseRestApi<Login.Request, Login.Response> {

    private static final String RELATIVE_URL = "/api/login";

    /**
     * 初始化WebApi请求参数
     */
    public Login() {
        super(RELATIVE_URL);
        setCheckValidity(false);
//        this.request.client_type = Constant.CLIENT_TYPE;
    }

    /**
     * 初始化WebApi请求参数
     *
     * @param mobile       手机号码
     * @param password     密码(传入密码md5后的值，密码长度：6~30)
     * @param client_token 设备token
     */
    public Login(String mobile, String password, String client_token) {
        this();
        this.request.mobile = mobile;
        this.request.password = CommonUtils.stringToMD5(password);
        this.request.client_token = client_token;
    }

    /**
     * 登录WebApi请求参数
     */
    public static class Request extends BaseRestApi.Request {
        private static final long serialVersionUID = 1L;

        /**
         * 手机号码
         */
        public String mobile;

        /**
         * 密码(传入密码md5后的值，密码长度：6~30)
         */
        public String password;

        /**
         * 设备类型(2：安卓；1：IOS)
         */
        public String client_type;

        /**
         * 设备token
         */
        public String client_token;
    }

    /**
     * 登录WebApi返回结果定义
     */
    public static class Response extends BaseRestApi.Response<LoginResponse> {
        private static final long serialVersionUID = 1L;

    }

    public static class LoginResponse implements Serializable {
        /**
         * 身份令牌
         */
        public String token;
        /**
         * 用户信息
         */
        public GetUserInfo.UserInfo userinfo;
    }
}
