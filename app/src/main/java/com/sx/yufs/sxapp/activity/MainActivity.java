package com.sx.yufs.sxapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sx.yufs.sxapp.R;
import com.sx.yufs.sxapp.common.customview.MyTouchView;
import com.sx.yufs.sxapp.common.customview.MyTouchViewGroup;
import com.sx.yufs.sxapp.common.utils.CommonUtils;
import com.sx.yufs.sxapp.common.utils.ThreeDESUtils;
import com.sx.yufs.sxapp.webapi.okhttp.AllRequest;
import com.sx.yufs.sxapp.webapi.okhttp.para.FablicList;
import com.sx.yufs.sxapp.webapi.okhttp.para.LoginPara;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscriber;


/**
 * 主activity
 * Created by yufs on 2016/4/25.
 */
public class MainActivity extends BaseActivity {

    @InjectView(R.id.my_touch_view)
    MyTouchView myTouchView;

    @InjectView(R.id.my_touch_view_group)
    MyTouchViewGroup myTouchViewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        myTouchView.setText(myTouchViewGroup.getEvent() + myTouchView.getEvent());
        myTouchViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("su", "myTouchViewGroup clicked");
            }
        });
//        myTouchViewGroup.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("su", "myTouchViewGroup touched");
//                return false;
//            }
//        });
//        myTouchView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("su", "myTouchView touched");
//                return false;
//            }
//        });
        myTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("su", "myTouchView onClick");
            }
        });

    }
    @OnClick(R.id.get)
public void getList(){
        Subscriber<FablicList> loginParaSubscriber = new Subscriber<FablicList>() {
            @Override
            public void onCompleted() {
                //CommonUtils.ShowToast(MainActivity.this,"get login info complete", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FablicList loginPara) {
                CommonUtils.ShowToast(MainActivity.this,loginPara.getTotal()+"", Toast.LENGTH_SHORT);

            }
        };
        AllRequest.getInstance().getFablicList(loginParaSubscriber,"1", "1","10");
}
    @OnClick(R.id.button)
    public void clickButton() {
        Log.e("su", "button clicked");
        Subscriber<LoginPara> loginParaSubscriber = new Subscriber<LoginPara>() {
            @Override
            public void onCompleted() {
                //CommonUtils.ShowToast(MainActivity.this,"get login info complete", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginPara loginPara) {
                CommonUtils.ShowToast(MainActivity.this,loginPara.getUsername(), Toast.LENGTH_SHORT);
                userSharedPrefence.setToken(loginPara.getToken());
            }
        };
        AllRequest.getInstance().getLoginInfo(loginParaSubscriber,"系统管理员", ThreeDESUtils.encryptMode("1qaz@WSX"));


       /* HttpSu okHttpTest = new HttpSu(MainActivity.this);
        Retrofit retrofit = okHttpTest.initRetrofit();
        RequestI login =  retrofit.create(RequestI.class);

        login.getLoginInfo("系统管理员","OS+PMumzzfeOKedH10XQKw==")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginPara>() {
                    @Override
                    public void onCompleted() {
                        CommonUtils.ShowToast(MainActivity.this,"get login info complete", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonUtils.ShowToast(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(LoginPara loginPara) {
                        CommonUtils.ShowToast(MainActivity.this,loginPara.getUsername(), Toast.LENGTH_SHORT);
                    }
                });*/
       /* Call<LoginPara> call = login.getLoginInfo("系统管理员","OS+PMumzzfeOKedH10XQKw==");
        call.enqueue(new Callback<LoginPara>() {
            @Override
            public void onResponse(Call<LoginPara> call, Response<LoginPara> response) {
                Log.e("su", "Token:" + response.body().getToken() + "\n"+"Username:" + response.body().getUsername() + "\n"+"Id:" + response.body().getId() + "");
            }

            @Override
            public void onFailure(Call<LoginPara> call, Throwable t) {
                Log.e("su", "");
            }
        });*/
    }

}
