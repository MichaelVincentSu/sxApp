package com.sx.yufs.sxapp.webapi.core;


import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.sx.yufs.sxapp.SxApplication;
import com.sx.yufs.sxapp.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * WebApi接口调用基类，所有业务WebApi接口请求可继承该类来完成相关操作。
 *
 * @param <REQUEST_TYPE>  请求参数
 * @param <RESPONSE_TYPE> 返回参数
 */
public abstract class BaseRestApi<REQUEST_TYPE extends BaseRestApi.Request, RESPONSE_TYPE extends BaseRestApi.Response<?>> {
    /**
     * Logcat Tag
     */
    private static final String TAG = "BaseRestApi";

    /**
     * 业务WebApi请求根地址
     */
    private static final String BASE_URL = Constant.WEB_ROOT_UREL;

    /**
     * CookieStore
     */
    public static PersistentCookieStore persistentCookieStore;

    /**
     * 执行WebApi对应的上下文
     */
    public WeakReference<Context> mContext;

    /**
     * WebApi异步请求类（AsyncHttpClient）
     */
    protected AsyncHttpClient asyncHttpClient;

    /**
     * WebApi同步请求类（SyncHttpClient）
     */
    protected SyncHttpClient syncHttpClient;

    /**
     * WebApi请求参数类
     */
    protected REQUEST_TYPE request;

    /**
     * Api接口的相对Url
     */
    private String relativeUrl;

    /**
     * Http请求方法，默认为POST
     */
    private HttpMethod httpMethod;

    /**
     * 如果请求非业务WebApi可以改变请求时的根地址
     */
    private String baseUrl;

    /**
     * WebApi请求Header参数
     */
    private List<Header> requestHeadersList;

    /**
     * HTTP ContentType
     */
    private String contentType = "application/json";

    /**
     * WebApi请求参数Class
     */
    private Class<REQUEST_TYPE> requestClass;

    /**
     * WebApi请求结果Class
     */
    private Class<RESPONSE_TYPE> responseClass;

    /**
     * 是否需要执行对WebApi返回结果的共通Check
     */
    private boolean check_validity;

    /**
     * 构造函数
     *
     * @param relativeUrl Api接口的相对Url
     */
    public BaseRestApi(String relativeUrl) {
        this(relativeUrl, HttpMethod.POST);
    }

    /**
     * 构造函数
     *
     * @param relativeUrl Api接口的相对Url
     * @param httpMethod  Http请求方式
     */
    public BaseRestApi(String relativeUrl, HttpMethod httpMethod) {
        // 初始化全局变量
        this.check_validity = true;
        this.asyncHttpClient = new AsyncHttpClient();
        this.requestHeadersList = new ArrayList<Header>();

        // 保存传入参数
        this.relativeUrl = relativeUrl;
        this.setHttpMethod(httpMethod);

        // 初始化超时参数
        asyncHttpClient.setMaxRetriesAndTimeout(0, 0);
        setConnectTimeout(3000);
        setResponseTimeout(5000);

        // 启用Cookie
        if (persistentCookieStore == null) {
            persistentCookieStore = new PersistentCookieStore(SxApplication.getApplication());
        }
        asyncHttpClient.setCookieStore(persistentCookieStore);

        // 取得继承该BaseRestApi.Request类及BaseRestApi.Response类的Class类型
        Type[] actualTypeArguments = ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments();
        for (Type type : actualTypeArguments) {
            if (type.toString().contains("Request")) {
                requestClass = (Class<REQUEST_TYPE>) actualTypeArguments[0];
            } else if (type.toString().contains("Response")) {
                responseClass = (Class<RESPONSE_TYPE>) actualTypeArguments[1];
            }
        }

        // 实例化Request类
        try {
            this.request = requestClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        this.getRequestHeadersList().add(new BasicHeader("Accept", "application/json"));
    }

    /**
     * @param check_validity 是否需要执行对WebApi返回结果的共通Check
     */
    public void setCheckValidity(boolean check_validity) {
        this.check_validity = check_validity;
    }

    /**
     * 取得WebApi请求的根地址
     */
    public String getBaseUrl() {
        String url = baseUrl;

        if (url == null || url.isEmpty()) {
            url = BASE_URL;
        }

        return url;
    }

    /**
     * 设置WebApi请求的根地址，访问非业务WebApi时使用
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Set both the connection and socket timeouts. By default, both are set to
     * 10 seconds.
     *
     * @param value the connect/socket timeout in milliseconds, at least 1
     *              second
     * @see #setConnectTimeout(int)
     * @see #setResponseTimeout(int)
     */
    public void setTimeount(int value) {
        asyncHttpClient.setTimeout(value);
    }

    /**
     * Set connection timeout limit (milliseconds). By default, this is set to
     * 10 seconds.
     *
     * @param value Connection timeout in milliseconds, minimal value is 1000 (1
     *              second).
     */
    public void setConnectTimeout(int value) {
        asyncHttpClient.setConnectTimeout(value);
    }

    /**
     * Set response timeout limit (milliseconds). By default, this is set to 10
     * seconds.
     *
     * @param value Response timeout in milliseconds, minimal value is 1000 (1
     *              second).
     */
    public void setResponseTimeout(int value) {
        asyncHttpClient.setResponseTimeout(value);
    }

    /**
     * 取得WebApi请求参数类
     */
    public final REQUEST_TYPE getRequest() {
        return request;
    }

    /**
     * 获取HTTP ContentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 设置HTTP ContentType、默认"application/json"
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 同步执行Web请求（该API只能在子线程中运行。）
     *
     * @param context 上下文对象
     * @return 请求结果
     */
    public RESPONSE_TYPE syncRequest(Context context) {
        if (syncHttpClient == null) {
            syncHttpClient = new SyncHttpClient();
        }

        syncHttpClient.setMaxRetriesAndTimeout(0, 0);
        syncHttpClient.setConnectTimeout(asyncHttpClient.getConnectTimeout());
        syncHttpClient.setResponseTimeout(asyncHttpClient.getResponseTimeout());
        syncHttpClient.setCookieStore(persistentCookieStore);

        mContext = new WeakReference<Context>(context);

        RestAPIResponseHandler responseHandler = new RestAPIResponseHandler(null);

        if (getHttpMethod() == BaseRestApi.HttpMethod.GET) {
            syncHttpClient.get(context, getAbsoluteUrl(), getRequestHeaders(),
                    getRequestParams(),
                    responseHandler);
        } else if (getHttpMethod() == BaseRestApi.HttpMethod.POST) {
            HttpEntity httpEntity = getHttpEntity();

            if (httpEntity == null) {
                syncHttpClient.post(context, getAbsoluteUrl(),
                        getRequestParams(),
                        responseHandler);
            } else {
                syncHttpClient.post(context, getAbsoluteUrl(),
                        getRequestHeaders(),
                        httpEntity, null,
                        responseHandler);
            }
        }

        return responseHandler.response;
    }

    /**
     * 异步执行Web请求
     *
     * @param context  上下文
     * @param listener WebApi请求回调监听类
     */
    public void asyncRequest(Context context, com.sx.yufs.sxapp.webapi.core.IRestApiListener<RESPONSE_TYPE> listener) {
        mContext = new WeakReference<Context>(context);
        AsyncHttpResponseHandler responseHandler = new RestAPIResponseHandler(listener);
        if (getHttpMethod() == BaseRestApi.HttpMethod.GET) {
            asyncHttpClient.get(context, getAbsoluteUrl(),
                    getRequestHeaders(),
                    getRequestParams(),
                    responseHandler);
        } else if (getHttpMethod() == BaseRestApi.HttpMethod.POST) {
            HttpEntity httpEntity = getHttpEntity();

            if (httpEntity == null) {
                asyncHttpClient.post(context, getAbsoluteUrl(),
                        getRequestHeaders(),
                        getRequestParams(), getContentType(),
                        responseHandler);
            } else {
                asyncHttpClient.post(context, getAbsoluteUrl(),
                        getRequestHeaders(),
                        httpEntity, null,
                        responseHandler);
            }
        } else if (getHttpMethod() == BaseRestApi.HttpMethod.PUT) {
            asyncHttpClient.put(context, getAbsoluteUrl(), getRequestParams(), responseHandler);
        } else if (getHttpMethod() == BaseRestApi.HttpMethod.DELETE) {
            asyncHttpClient.delete(context, getAbsoluteUrl(), null, getRequestParams(),
                    responseHandler);
        }
    }

    /**
     * 取消WebApi请求
     */
    public void cancelRequests() {
        asyncHttpClient.cancelAllRequests(true);
    }

    /**
     * 取得WebApi请求时的URL
     *
     * @return WebApi请求时的URL
     */
    public String getAbsoluteUrl() {
        return getBaseUrl() + getRelativeUrl();
    }

    /**
     * 取得Api接口的相对Url
     */
    protected String getRelativeUrl() {
        return relativeUrl;
    }

    /**
     * 取得Http请求方法，默认为POST
     */
    protected HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * 设置Http请求方法，默认为POST
     */
    protected void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * 取得WebApi请求Header参数
     */
    protected List<Header> getRequestHeadersList() {
        return requestHeadersList;
    }

    /**
     * 取得WebApi请求Header参数
     */
    protected Header[] getRequestHeaders() {
        List<Header> headers = getRequestHeadersList();
        return headers.toArray(new Header[headers.size()]);
    }

    /**
     * 将Request类转为RequestParams对象
     */
    protected final RequestParams getRequestParams() {
        RequestParams params = null;

        if (request != null) {
            params = request.toRequestParams();
        }

        return params;
    }

    /**
     * 将Request类转为HttpEntity对象
     */
    protected HttpEntity getHttpEntity() {
        HttpEntity httpEntity = null;
        if (request != null) {
            httpEntity = request.toHttpEntity();
        }

        return httpEntity;
    }

    /**
     * 取得WebApi请求结果Class
     */
    protected final Class<RESPONSE_TYPE> getResponseClass() {
        return responseClass;
    }

    /**
     * 执行对WebApi返回结果的共通Check
     *
     * @param response WebApi返回结果
     */
    private void checkValidity(RESPONSE_TYPE response) {
//        Context context = mContext.get();
        // 返回值code为99、98、97时均为登录状态失效
//        if (context != null && response != null && response.code != null &&
//                (response.code == 97 || response.code == 98 || response.code == 99)) {
            // 执行一下退出当前用户的操作，并跳转至登录页面
//            CommonUtils.logout();
//            context.startActivity(new Intent(context, LoginActivity.class));
//            CommonUtils.showWebApiMessage(context, response, "登录状态失效，请重新登录。");
//        }
    }


    /**
     * Http请求方法定义
     */
    public static enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    /**
     * WebApi请求参数基类
     */
    public static abstract class Request implements Serializable {
        private static final long serialVersionUID = -6138075979267823613L;

        /**
         * 将实现类转为RequestParams参数类，默认返回null，子类需要重写本方法
         */
        public HttpEntity toHttpEntity() {
            return null;
        }

        /**
         * 将实现类转为RequestParams参数类
         */
        public RequestParams toRequestParams() {
            RequestParams params = new RequestParams();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                String rawJsonData = objectMapper.writeValueAsString(this);
                Log.i("RequestParams", rawJsonData);

                Map<String, Object> paramsMap = objectMapper.readValues(
                        new JsonFactory().createParser(rawJsonData),
                        Map.class).next();
                params.setContentEncoding("UTF-8");
                params.setUseJsonStreamer(true);

                addValueToRequestParams(params, paramsMap);
            } catch (Exception e) {
                // e.printStackTrace();
            }

            return params;
        }

        /**
         * 将Map中的内容添加到RequestParams中
         */
        private void addValueToRequestParams(RequestParams requestParams,
                                             Map<String, Object> paramsMap) {
            Set<String> keys = paramsMap.keySet();

            for (String key : keys) {
                Object value = paramsMap.get(key);
                if (value instanceof Map) {
                    JSONObject jsonObject = new JSONObject((Map<?, ?>) value);
                    requestParams.put(key, jsonObject);
                } else if (value instanceof ArrayList) {
                    JSONArray jsonArray = new JSONArray();
                    toJSONArray(jsonArray, (ArrayList<?>) value);

                    requestParams.put(key, jsonArray);
                } else {
                    requestParams.put(key, value);
                }
            }
        }

        /**
         * ArrayList对象转为JSONArray对象
         *
         * @param jsonArray
         * @param value
         */
        private void toJSONArray(JSONArray jsonArray, ArrayList<?> value) {
            for (Object arrayItem : value) {
                if (arrayItem instanceof Map) {
                    JSONObject jsonObject = new JSONObject((Map<?, ?>) arrayItem);
                    jsonArray.put(jsonObject);
                } else if (arrayItem instanceof ArrayList) {
                    JSONArray arrayItemArray = new JSONArray();
                    toJSONArray(arrayItemArray, (ArrayList<?>) arrayItem);
                    jsonArray.put(arrayItemArray);
                } else {
                    jsonArray.put(arrayItem);
                }
            }
        }

    }

    /**
     * WebApi返回结果基类
     *
     * @param <T> 业务WebApi返回的result参数内容
     */
    public static abstract class Response<T extends Object> implements Serializable {
        private static final long serialVersionUID = -3794321246435599689L;

        /**
         * Web请求状态值 [0：成功，非0：失败（code < 0网络异常/程序异常;code > 0业务异常）]
         */
        public Integer code;

        /**
         * 业务处理状态
         */
        public boolean success;

        /**
         * 业务处理返回消息
         */
        public String msg;

        /**
         * 业务数据
         */
        public T result;

        /**
         * 判断请求返回结果数据是否成功
         */
        public boolean isSucceed() {
            return (code != null && code == 0);
        }

    }

    /**
     * WebApi异步执行回调处理
     */
    protected class RestAPIResponseHandler extends BaseJsonHttpResponseHandler<RESPONSE_TYPE> {
        /**
         * WebApi返回结果
         */
        protected RESPONSE_TYPE response;
        /**
         * 业务层WebApi请求回调监听类
         */
        private com.sx.yufs.sxapp.webapi.core.IRestApiListener<RESPONSE_TYPE> restApiListener;

        /**
         * 构造函数
         *
         * @param restApiListener WebApi请求回调监听类
         */
        public RestAPIResponseHandler(com.sx.yufs.sxapp.webapi.core.IRestApiListener<RESPONSE_TYPE> restApiListener) {
            super("UTF-8");

            if (restApiListener != null) {
                this.restApiListener = restApiListener;
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                              String rawJsonData, RESPONSE_TYPE response) {
            try {
                this.response = response;

                Log.i(TAG, (rawJsonData == null ? "null" : rawJsonData));
                Log.i(TAG, "onFailure:" + throwable.getMessage());
                if (restApiListener != null) {
                    restApiListener.onFailure(statusCode, throwable, response);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                              RESPONSE_TYPE response) {
            Log.i(TAG, "onSuccess:" + response.msg);
            Log.i(TAG, "onSuccess:rawJsonResponse:" + rawJsonResponse);

            this.response = response;

            // 是否需要执行对WebApi返回结果的共通Check
            if (check_validity) {
                checkValidity(response);
            }

            try {
                if (restApiListener != null) {
                    restApiListener.onSuccess(statusCode, response);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }

        @Override
        protected RESPONSE_TYPE parseResponse(String rawJsonData, boolean isFailure)
                throws Throwable {
            Log.i(TAG, "parseResponse");

            RESPONSE_TYPE response = null;

            ObjectMapper objectMapper = new ObjectMapper();
            // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            response = objectMapper.readValues(
                    new JsonFactory().createParser(rawJsonData),
                    getResponseClass()).next();

            return response;
        }
    }
}

