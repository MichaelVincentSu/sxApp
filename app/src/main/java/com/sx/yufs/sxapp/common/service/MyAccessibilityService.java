package com.sx.yufs.sxapp.common.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * 辅助功能
 * Created by yufs on 2016/8/5.
 */
public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("su", "-----------------------onAccessibilityEvent---------------------------");
        //事件类型
        int eventType = event.getEventType();
        //响应事件的包名，也就是哪个应用才响应了这个事件
        Log.e("su", "packageName:" + event.getPackageName() + "");
        //事件源信息
        Log.e("su", "source:" + event.getSource() + "");
        //事件源的类名，比如android.widget.TextView
        Log.e("su", "source class:" + event.getClassName());
        Log.e("su", "event type(int)" + eventType);

        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED://通知栏事件
                Log.e("su", "通知栏事件:TYPE_NOTIFICATION_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://窗体状态改变
                Log.e("su", "窗体状态改变:TYPE_WINDOW_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED://view获取到焦点
                Log.e("su", "view获取到焦点:TYPE_VIEW_ACCESSIBILITY_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START://
                Log.e("su", ":TYPE_GESTURE_DETECTION_START");
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END://
                Log.e("su", ":TYPE_GESTURE_DETECTION_END");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://
                Log.e("su", ":TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED://
                Log.e("su", ":TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED://
                Log.e("su", ":TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED://
                Log.e("su", ":TYPE_VIEW_SCROLLED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED://
                Log.e("su", ":TYPE_VIEW_TEXT_SELECTION_CHANGED");
                break;
        }

        for (CharSequence txt : event.getText()) {
            Log.e("su", "text:" + txt);
        }
//        findAndPerformActionButton("安装");
//        findAndPerformActionTextView("取消");
    }

    @Override
    public void onInterrupt() {
        Log.e("su", "-------------------------onInterrupt-------------------------");


    }

    private void findAndPerformActionButton(String text) {
        //获取当前激活窗体的根节点
        if (getRootInActiveWindow() == null) {
            return;
        }
        //通过文字找到当前的节点
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo node = nodes.get(i);
            //执行点击行为
            if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                Log.e("su", "node.getClassName():" + node.getText());
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void findAndPerformActionTextView(String text) {
        //获取当前激活窗体的根节点
        if (getRootInActiveWindow() == null) {
            return;
        }
        //通过文字找到当前的节点
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo node = nodes.get(i);
            //执行点击行为
            if (node.getClassName().equals("android.widget.TextView") && node.isEnabled()) {
                Log.e("su", "node.getClassName():" + node.getText());
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

}
