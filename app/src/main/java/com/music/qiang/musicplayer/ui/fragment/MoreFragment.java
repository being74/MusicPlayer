package com.music.qiang.musicplayer.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.support.utils.HttpUtil;

import java.io.IOException;

/**
 *
 */
public class MoreFragment extends Fragment {

    private View rootView;
    private TextView test;
    private String response = "";

    private Handler mHandler = new Handler();
    private final Runnable updateUi = new Runnable() {
        @Override
        public void run() {
            test.setText(response);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        test = (TextView) rootView.findViewById(R.id.test);
        initData();
        return rootView;
    }

    private void initData() {
        //URL u=new URL("http://route.showapi.com/213-4?showapi_appid=myappid&topid=&showapi_sign=mysecret");
        final String url = "http://route.showapi.com/213-4?showapi_appid=" + 35608 + "&topid=5&showapi_sign=dd559b3a18ee485fa098924d3df792ab";


        new Thread() {
            //在新线程中发送网络请求
            public void run() {
                try {
                    response = HttpUtil.run(url);
                    mHandler.post(updateUi);
                    Log.d("xuqiang", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }
}
