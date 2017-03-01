package com.music.qiang.musicplayer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.IPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;

import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    //***************views***************
    /**
     * 开始/暂停 按钮
     */
    private ImageButton playButton;

    //***************基本数据***************
    private ArrayList<MusicFile> playList;

    //***************对象***************
    private IPlayback iPlayback;

    /**
     * bindservice connection
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPlayback = (IPlayback) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        fetchIntents();
        initViews();
        registListener();

        long musicId = getIntent().getIntExtra("musicId", 0);


        Intent intent = new Intent(this, PlayBackService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playList", playList);
        intent.putExtras(bundle);
        //intent.putExtra("ID", musicId);
        //startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    private void fetchIntents() {
        Bundle bundleObject = getIntent().getExtras();
        playList = (ArrayList<MusicFile>) bundleObject.getSerializable("playList");
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playButton = (ImageButton) findViewById(R.id.ib_fragment_music_play);
    }

    private void registListener() {
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_fragment_music_play:
                iPlayback.getState();
                break;
        }
    }
}
