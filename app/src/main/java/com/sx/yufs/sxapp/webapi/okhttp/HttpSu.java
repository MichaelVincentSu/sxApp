package com.sx.yufs.sxapp.webapi.okhttp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sx.yufs.sxapp.SxApplication;
import com.sx.yufs.sxapp.common.Constant;
import com.sx.yufs.sxapp.common.UserSharedPrefence;
import com.sx.yufs.sxapp.webapi.okhttp.interfacepackage.RequestI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 测试okhttp
 * Created by yufs on 2016/8/18.
 */
public class HttpSu {
    /**
     * 打印请求日志
     */
    private HttpLoggingInterceptor httpLoggingInterceptor;
    /**
     * okhttp
     */
    private OkHttpClient okHttpClient;
    /**
     * 请求拦截器
     */
    private Interceptor interceptor;
    /**
     * retrofit
     */
    private Retrofit retrofit;
    private Context context;
    protected RequestI requestI;

    private UserSharedPrefence userSharedPrefence;

//    public HttpSu(Context context) {
//        this.context = context;
//        userSharedPrefence = new UserSharedPrefence(SxApplication.getAppContext());
//    }

    public HttpSu() {
        requestI = initRetrofit().create(RequestI.class);
        userSharedPrefence = new UserSharedPrefence(SxApplication.getAppContext());
    }


    /**
     * 初始化网络请求框架
     *
     * @return
     */
    public Retrofit initRetrofit() {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印日志的等级，默认不打印
        //创建过滤器实例
        interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if (TextUtils.isEmpty(userSharedPrefence.getToken())) {
                    Request authorised = originalRequest.newBuilder()
//                            .addHeader("Content-Type", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    Log.e("su","authorised:"+authorised.header("Content-Type"));
                    return chain.proceed(authorised);
                }
                Request authorised = originalRequest.newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Token", userSharedPrefence.getToken())
                        .build();
                Log.e("su","Token:"+authorised.header("Token"));
                Log.e("su","authorised:"+authorised.header("Content-Type"));
                return chain.proceed(authorised);
            }
        };
        //配置请求实例
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//出现错误重新连接
                .connectTimeout(15, TimeUnit.SECONDS)//设置重连时间和单位
                .addNetworkInterceptor(interceptor)//让所有网络请求都添加此拦截器
                .addInterceptor(httpLoggingInterceptor)//添加日志打印
                .build();
        //创建请求实例
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WEB_ROOT_UREL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }


}
