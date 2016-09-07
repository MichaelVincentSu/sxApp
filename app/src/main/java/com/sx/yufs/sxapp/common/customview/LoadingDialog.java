package com.sx.yufs.sxapp.common.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sx.yufs.sxapp.R;

/**
 *
 * 加载
 * Created by yufs on 2016/9/7.
 */
public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context);
        initDialog(context);
    }

    private void initDialog(Context context) {
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
    }
}
