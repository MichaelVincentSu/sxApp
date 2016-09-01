package com.sx.yufs.sxapp.webapi.okhttp.interfacepackage;


import com.sx.yufs.sxapp.webapi.okhttp.para.FablicList;
import com.sx.yufs.sxapp.webapi.okhttp.para.LoginPara;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 请求接口
 * Created by yufs on 2016/8/18.
 */
public interface RequestI {
    @FormUrlEncoded
    @POST("Customer")
    Observable<LoginPara> getLoginInfo(@Field("Username") String username, @Field("Password") String password);//Call<LoginPara> getLoginInfo(@Field("Username") String username, @Field("Password") String password);

    @FormUrlEncoded
    @POST("Fabric")
    Observable<FablicList> getFablicList(@Field("Page") String Page, @Field("Start") String Start, @Field("Limit") String Limit);
}
