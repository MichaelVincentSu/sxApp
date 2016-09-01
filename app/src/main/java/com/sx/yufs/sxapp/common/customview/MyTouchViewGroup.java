package com.sx.yufs.sxapp.common.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.sx.yufs.sxapp.common.utils.CommonUtils;

/**
 * 测试分层布局的点击事件
 * Created by yufs on 2016/8/8.
 */
public class MyTouchViewGroup extends FrameLayout {


    public static boolean dispatchTouchEvent = false;

    public static boolean onTouchEvent = false;

    public static boolean onInterceptTouchEvent = false;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event = "";

    public MyTouchViewGroup(Context context) {
        super(context);
    }

    public MyTouchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("su", "layout onTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        setEvent("\nView onTouchEvent:" + CommonUtils.getTouchEvent(event.getAction()) + " " + CommonUtils.getNowTime());
        onTouchEvent = super.onTouchEvent(event);
        return onTouchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("su", "layout dispatchTouchEvent:" + CommonUtils.getTouchEvent(ev.getAction()) + " " + CommonUtils.getNowTime());
        setEvent("\nView dispatchTouchEvent:" + CommonUtils.getTouchEvent(ev.getAction()) + " " + CommonUtils.getNowTime());
        dispatchTouchEvent = super.dispatchTouchEvent(ev);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("su", "layout onInterceptTouchEvent:" + CommonUtils.getTouchEvent(ev.getAction()) + " " + CommonUtils.getNowTime());
        setEvent("\nView onInterceptTouchEvent:" + CommonUtils.getTouchEvent(ev.getAction()) + " " + CommonUtils.getNowTime());
        onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        return onInterceptTouchEvent;
    }
}
