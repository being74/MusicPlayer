package com.music.qiang.musicplayer.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求
 * <p>
 * Created by xuqiang on 2017/04/13.
 */

public class HttpUtil {

    private static OkHttpClient okhttpClient = new OkHttpClient();
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String BASE_URL = "";
    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * okhttp的get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static void doGet(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void doGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void doPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    /**
     * @param context 检查网络连接状况
     * @return ture : 连接， false : 未连接
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
