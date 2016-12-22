package com.music.qiang.musicplayer.application;

import android.app.Application;

/**
 * Created by user on 2016/12/22.
 */
public class MyApp extends Application {

    private static MyApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = (MyApp) getApplicationContext();
    }

    public static MyApp getSelf() {
        return mApp;
    }
}
