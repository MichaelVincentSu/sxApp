package com.sx.yufs.sxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.sx.yufs.sxapp.R;
import com.sx.yufs.sxapp.common.utils.CommonUtils;
import com.sx.yufs.sxapp.common.utils.ThreeDESUtils;
import com.sx.yufs.sxapp.webapi.okhttp.AllRequest;
import com.sx.yufs.sxapp.webapi.okhttp.para.LoginPara;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity  {

    /**
     * 用户名
     */
    @InjectView(R.id.username)
    EditText usernameEditText;
    /**
     * 密码
     */
    @InjectView(R.id.password)
    EditText passwordEditText;
//    private UserSharedPrefence userSharedPrefence;
   // private Dialog dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        userSharedPrefence = new UserSharedPrefence(LoginActivity.this);
        ButterKnife.inject(this);
    }



    @OnClick(R.id.login_button)
    public void login() {
        if(CommonUtils.isFastDoubleClick()){
            return;
        }
        usernameEditText.setText("系统管理员");
        passwordEditText.setText("1qaz@WSX");
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            CommonUtils.ShowToast(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password)){
            CommonUtils.ShowToast(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT);
            return;
        }
        password = ThreeDESUtils.encryptMode(password);
        Log.e("username:", username);
        Log.e("password:", password);
        /*try {
            dl = CommonUtils.createLoadingDialog(LoginActivity.this);
            dl.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        CommonUtils.showLoadingDialog(LoginActivity.this);
        Subscriber<LoginPara> loginParaSubscriber = new Subscriber<LoginPara>() {
            @Override
            public void onCompleted() {
                //CommonUtils.ShowToast(MainActivity.this,"get login info complete", Toast.LENGTH_SHORT);

                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.ShowToast(LoginActivity.this,e.getMessage()+"", Toast.LENGTH_SHORT);
                CommonUtils.cancelLoadingDialog();
            }

            @Override
            public void onNext(LoginPara loginPara) {
                CommonUtils.ShowToast(LoginActivity.this,loginPara.getUsername(), Toast.LENGTH_SHORT);
                CommonUtils.cancelLoadingDialog();
                userSharedPrefence.setToken(loginPara.getToken());
                userSharedPrefence.setHasToken(true);
            }
        };
        AllRequest.getInstance().getLoginInfo(loginParaSubscriber,"系统管理员", ThreeDESUtils.encryptMode("1qaz@WSX"));
    }
}

