package com.sx.yufs.sxapp.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sx.yufs.sxapp.R;
import com.sx.yufs.sxapp.webapi.core.BaseRestApi;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全局的静态方法
 * Created by Administrator on 2015/12/8.
 */
public class CommonUtils {


    public static String getNowTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String getTouchEvent(int i) {
        if (i == 0) {
            return "ACTION_DOWN";
        } else if (i == 1) {
            return "ACTION_UP";
        } else if (i == 2) {
            return "ACTION_MOVE";
        }
        return "";
    }

    /**
     * 保存应用中当前正在显示的Toast。
     */
    private static Toast globalToast;
    private static long mLastClickTime = 0;

    /**
     * 打开应用内浏览器画面
     *
     * @param context 上下文
     * @param url     网址
     */
//    public static boolean openWebBrowse(Context context, String url) {
//        boolean blnRet = false;
//        // 启动浏览器画面
//        if (!TextUtils.isEmpty(url) && (url.toLowerCase().startsWith("http://")
//                || url.toLowerCase().startsWith("https://")
//                || url.toLowerCase().startsWith("www."))) {
//            Intent intent = new Intent(context, WebBrowseActivity.class);
//            intent.putExtra("url", url);
//            context.startActivity(intent);
//
//            blnRet = true;
//        }
//
//        return blnRet;
//    }

    /**
     * 取消当前正在显示的Toast，立即显示新指定的Toast内容。
     *
     * @param context  上下文(AppicationContext, Activity...)
     * @param text     提示内容
     * @param duration Toast.LENGTH_LONG/Toast.LENGTH_SHORT
     */
    public static void ShowToast(Context context, CharSequence text, int duration) {
        try {
            if (globalToast != null) {
                globalToast.cancel();
                globalToast = null;
            }

            globalToast = Toast.makeText(context, text, duration);
            globalToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示执行WebApi后应该显示的消息
     *
     * @param context
     * @param response
     * @param defaultMessage 服务器返回消息为空或调用失败要显示的消息
     */
    public static void showWebApiMessage(Context context, BaseRestApi.Response<?> response,
                                         String defaultMessage) {
        if (response == null || TextUtils.isEmpty(response.msg)) {
            ShowToast(context, defaultMessage, Toast.LENGTH_SHORT);
        } else {
            ShowToast(context, response.msg, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 防止重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    /**
     * 自定义的progressDialog
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.zh_dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v
                .findViewById(R.id.zh_dialogimg);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.zh_loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
        loadingDialog.setContentView(layout);// 设置布局
        return loadingDialog;
    }

    /**
     * 二维码Dialog
     *
     * @param context
     * @return
     */
//    public static Dialog createCodeDialog(Context context, String url) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.my_code_layout, null);// 得到加载view
//        FrameLayout layout = (FrameLayout) v.findViewById(R.id.my_code_layout);
//        // main.xml中的ImageView
//        ImageView spaceshipImage = (ImageView) v
//                .findViewById(R.id.my_code_dialog_img);
//        Picasso.with(context).load(url).into(spaceshipImage);
//        Dialog loadingDialog = new Dialog(context, R.style.code_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(true);
//        loadingDialog.setCanceledOnTouchOutside(true);//点击屏幕消失
//        loadingDialog.setContentView(layout);// 设置布局
//        return loadingDialog;
//    }

    /**
     * 将字符串转成MD5值
     *
     * @param value 字符串
     * @return MD5值
     */
    public static String stringToMD5(String value) {
//        String md5Value = null;
//
//        try {
//            byte[] hash = MessageDigest.getInstance("MD5").digest(value.getBytes("UTF-8"));
//
//            StringBuilder hex = new StringBuilder(hash.length * 2);
//
//            for (byte b : hash) {
//                if ((b & 0xFF) < 0x10) {
//                    hex.append("0");
//                }
//
//                hex.append(Integer.toHexString(b & 0xFF));
//            }
//
//            md5Value = hex.toString();
//
//        } catch (Exception e) {
//            // nothing
//        }
        return value;
    }


    /**
     * 取得Map中的Value
     *
     * @param map Map对象
     * @param key key
     * @param <K> key type
     * @param <V> value type
     * @return value
     */
    public static <K, V> V getMapValue(Map<K, V> map, K key) {
        V value = null;
        if (map != null && map.containsKey(key)) {
            value = map.get(key);
        }
        return value;
    }

    /**
     * 收起状态栏
     *
     * @param ctx
     */
    public static final void collapseStatusBar(Context ctx) {
        Object sbservice = ctx.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapse;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                collapse = statusBarManager.getMethod("collapsePanels");
            } else {
                collapse = statusBarManager.getMethod("collapse");
            }
            collapse.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展开状态栏
     *
     * @param ctx
     */
    public static final void expandStatusBar(Context ctx) {
        Object sbservice = ctx.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand;
            if (Build.VERSION.SDK_INT >= 17) {
                expand = statusBarManager.getMethod("expandNotificationsPanel");
            } else {
                expand = statusBarManager.getMethod("expand");
            }
            expand.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * listview adapter 减少代码量
     * <p/>
     * public View getView(int position, View convertView, ViewGroup parent) {
     * if (convertView == null) {
     * convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_feed_item, parent, false);
     * }
     * <p/>
     * ImageView thumnailView = getAdapterView(convertView, R.id.video_thumbnail);
     * ImageView avatarView =  getAdapterView(convertView, R.id.user_avatar);
     * ImageView appIconView = getAdapterView(convertView, R.id.app_icon);
     *
     * @param convertView
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T getAdapterView(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    /**
     * 获取可启动的应用
     * @param context
     * @return
     */
    public static List<ResolveInfo> getSystemAppList(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, 0);
        return activities;
    }

    /**
     * 泛型arraylist转数组
     * @param cls
     * @param items
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Class<?> cls, ArrayList<T> items) {
        if (items == null || items.size() == 0) {
            return (T[]) Array.newInstance(cls, 0);
        }
        return items.toArray((T[]) Array.newInstance(cls, items.size()));
    }



}
