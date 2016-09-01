package com.sx.yufs.sxapp.webapi;


import com.sx.yufs.sxapp.webapi.core.BaseRestApi;

import java.io.Serializable;

/**
 * 获取个人信息WebApi实现
 */
public class GetUserInfo extends BaseRestApi<GetUserInfo.Request, GetUserInfo.Response> {

    private static final String RELATIVE_URL = "/api/getuserinfo";

    /**
     * 初始化WebApi请求参数
     */
    public GetUserInfo() {
        super(RELATIVE_URL);
    }

    /**
     * 初始化WebApi请求参数
     *
     * @param token 身份令牌
     */
    public GetUserInfo(String token) {
        this();
        this.request.token = token;
    }

    /**
     * 获取个人信息WebApi请求参数
     */
    public static class Request extends BaseRestApi.Request {
        private static final long serialVersionUID = 1L;

        /**
         * 身份令牌
         */
        public String token;
    }

    /**
     * 获取个人信息WebApi返回结果定义
     */
    public static class Response extends BaseRestApi.Response<UserInfo> {
        private static final long serialVersionUID = 1L;

    }

    /**
     * 用户信息
     */
    public static class UserInfo implements Serializable {
        /**
         * 用户ID
         */
        public String id;

        /**
         * 姓名
         */
        public String name;

        /**
         * 头像url
         */
        public String face;

        /**
         * 昵称
         */
        public String nickname;

        /**
         * 手机号
         */
        public String mobile;

        /**
         * 生日(格式：YYYY-MM-DD)
         */
        public String birthday;

        /**
         * 性别(0：女；1：男)
         */
        public int sex;

        /**
         * 车牌号
         */
        public String carno;

        /**
         * 有登录密码(true:有密码；false：无密码)
         */
        public Boolean haspassword;

        /**
         * 有支付密码(true:有密码；false：无密码)
         */
        public Boolean haspayword;

        /**
         * 免打扰开关0：关闭免打扰（能推消息）；1：打开免打扰；
         */
        public String msgstatus;
    }
}
