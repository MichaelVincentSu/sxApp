
package com.sx.yufs.sxapp.common.utils;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Field;

public class DeviceInfo {
    public static int width;
    public static int getWidth() {
        return width;
    }
    public static void setWidth(int width) {
        DeviceInfo.width = width;
    }
    public static int getHeight() {
        return height;
    }
    public static void setHeight(int height) {
        DeviceInfo.height = height;
    }
    public static int height;
    public static String model;
    public static int sdkVersion;
    public static String osVersion;
    public static int titleHeight;
    public DeviceInfo(Context context) {
        model = Build.MODEL;
        sdkVersion = Build.VERSION.SDK_INT;
        osVersion = Build.VERSION.RELEASE;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            titleHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
