package com.music.qiang.musicplayer.application;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.music.qiang.musicplayer.R;

/**
 * Created by xuqiang on 2016/12/22.
 */
public class MyApp extends Application {

    private static MyApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = (MyApp) getApplicationContext();
        toastMgr.builder.init(mApp);
    }

    public static MyApp getSelf() {
        return mApp;
    }

    /**
     * toast singleton，用来统一显示toast，这样就可以实现toast的快速刷新
     *
     */
    public enum toastMgr {
        builder;

        private View v;
        private TextView tv;
        private Toast toast;

        private void init(Context c) {
            // v = Toast.makeText(c, "", Toast.LENGTH_SHORT).getView();
            v = LayoutInflater.from(c).inflate(R.layout.my_toast, null);
            tv = (TextView) v.findViewById(R.id.tv_toast);
            toast = new Toast(c);
            toast.setView(v);
        }

        public void display(CharSequence text, int duration) {
            if (text.length() != 0) {
                tv.setText(text);
                toast.setDuration(duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        public void display(int Resid, int duration) {
            if (Resid != 0) {
                tv.setText(Resid);
                toast.setDuration(duration);
                toast.show();
            }
        }

        public void display(CharSequence text, int duration, int position, int yOffset) {
            if (text.length() != 0) {
                tv.setText(text);
                toast.setDuration(duration);
                toast.setGravity(position, 0, yOffset);
                toast.show();
            }
        }
    }
}
