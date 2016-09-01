package com.sx.yufs.sxapp.common.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.sx.yufs.sxapp.common.utils.CommonUtils;

/**
 * 测试view点击事件的分发
 * Created by yufs on 2016/8/8.
 */
public class MyTouchView extends TextView {

    public static boolean dispatchTouchEvent = false;

    public static boolean onTouchEvent = false;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event = "";

    public MyTouchView(Context context) {
        super(context);
    }

    public MyTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("su", "View dispatchTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        setEvent("\nView dispatchTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        dispatchTouchEvent = super.dispatchTouchEvent(event);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("su", "View onTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        setEvent("\nView onTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        onTouchEvent = super.onTouchEvent(event);
        return onTouchEvent;
    }


}
