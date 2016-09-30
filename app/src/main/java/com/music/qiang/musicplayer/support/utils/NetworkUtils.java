package com.music.qiang.musicplayer.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查网络相关方法
 *
 * Created by xuqiang on 2016/9/30.
 */
public class NetworkUtils {

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
