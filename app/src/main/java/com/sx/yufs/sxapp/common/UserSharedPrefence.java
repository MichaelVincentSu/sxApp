package com.sx.yufs.sxapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPrefence
 * Created by yufs on 2016/4/25.
 */
public class UserSharedPrefence {

    private SharedPreferences sharedPreferences;

    public UserSharedPrefence(Context context) {
        sharedPreferences = context.getSharedPreferences("info", Activity.MODE_PRIVATE);
    }

    /**
     * 设置是否登录
     */
    public void setLogin(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", b);
        editor.commit();
    }

    /**
     * 是否登录
     * @return
     */
    public boolean getLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    /**
     * 设置是否登录
     */
    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    /**
     * 是否登录
     * @return
     */
    public String getToken() {
        return sharedPreferences.getString("token","");
    }
}
