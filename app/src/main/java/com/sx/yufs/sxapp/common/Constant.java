package com.sx.yufs.sxapp.common;

import android.os.Environment;

/**
 * App 内全局变量
 * Created by yufs on 2016/4/25.
 */
public class Constant {
    /**
     * app名
     */
    public static final String APP_NAME = "DemoApp";
    /**
     * web请求根地址
     */
    public static final String WEB_ROOT_UREL = "http://f.5xyz.net/services/api/";
//    public static final String WEB_ROOT_UREL = "http://www.dsyun.com.cn/Services/api/";

    /**
     * 图片存放根地址
     */
    public static final String PICTURE_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_NAME + "/picture/";
    /**
     * 文件存放根地址
     */
    public static final String FILE_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_NAME + "/file/";


}
