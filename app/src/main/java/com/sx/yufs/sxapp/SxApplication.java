package com.sx.yufs.sxapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * application
 * Created by yufs on 2016/4/25.
 */
public class SxApplication extends Application {
    /**
     * application静态对象
     */
    private static SxApplication sxApplication;

    public SxApplication() {
        super();
        Log.e("su","Application SxApplication");
        sxApplication = this;
    }

    public static SxApplication getApplication() {
        return sxApplication;
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        SxApplication.context = getApplicationContext();
        Log.e("su","Application onCreate");
    }

    public static Context getAppContext() {
        return SxApplication.context;
    }
}
