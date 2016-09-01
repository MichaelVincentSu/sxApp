package com.sx.yufs.sxapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.sx.yufs.sxapp.SxApplication;
import com.sx.yufs.sxapp.common.UserSharedPrefence;

/**
 * activit基类
 * Created by yufs on 2016/4/25.
 */
public class BaseActivity extends Activity{

    protected UserSharedPrefence userSharedPrefence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("su","BaseActivity onCreate");
        userSharedPrefence = new UserSharedPrefence(SxApplication.getAppContext());
    }
}
