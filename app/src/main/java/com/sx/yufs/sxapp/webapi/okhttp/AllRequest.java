package com.sx.yufs.sxapp.webapi.okhttp;

import com.sx.yufs.sxapp.webapi.okhttp.para.FablicList;
import com.sx.yufs.sxapp.webapi.okhttp.para.LoginPara;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 请求类
 * Created by yufs on 2016/8/19.
 */
public class AllRequest extends HttpSu {

    public AllRequest() {
        super();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final AllRequest INSTANCE = new AllRequest();
    }

    //获取单例
    public static AllRequest getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 为每一次请求添加观察者模式
     * @param subscriber
     * @param pack
     */
    private <T> void packRequest(Subscriber<T> subscriber, Observable<T> pack) {
        pack.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录
     */
    public void getLoginInfo(Subscriber<LoginPara> subscriber, String name, String password) {
        packRequest(subscriber, requestI.getLoginInfo(name, password));
    }

    /**
     * 获取面料列表
     */
    public void getFablicList(Subscriber<FablicList> subscriber, String page, String start, String limit) {
        packRequest(subscriber, requestI.getFablicList(page, start,limit));
    }


}
