
package com.sx.yufs.sxapp.webapi.core;

/**
 * WebApi请求回调监听类
 * 
 * @param <RESPONSE_TYPE>
 */
public interface IRestApiListener<RESPONSE_TYPE extends BaseRestApi.Response<?>> {

    /**
     * WebApi请求出错时执行
     * 
     * @param statusCode 执行状态
     * @param throwable 异常内容
     * @param response 请求结果
     */
    public void onFailure(int statusCode, Throwable throwable, RESPONSE_TYPE response);

    /**
     * WebApi请求成功时执行
     * 
     * @param statusCode 执行状态
     * @param response 请求结果
     */
    public void onSuccess(int statusCode, RESPONSE_TYPE response);

}
