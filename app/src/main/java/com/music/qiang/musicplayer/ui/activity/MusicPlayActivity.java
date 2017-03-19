package com.music.qiang.musicplayer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private ImageButton playButton, playPre, playNext;

    //***************基本数据***************
    private ArrayList<MusicFile> playList;
    private int playIndex;

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
        Log.d("xuqiang", "activity  ------  onCreate");
        setContentView(R.layout.activity_music_play);
        fetchIntents();
        initViews();
        registListener();

        long musicId = getIntent().getIntExtra("musicId", 0);


        Intent intent = new Intent(this, PlayBackService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playList", playList);
        intent.putExtras(bundle);
        bindService(intent, connection, BIND_AUTO_CREATE);
        //startService(intent);
    }

    private void fetchIntents() {
        Bundle bundleObject = getIntent().getExtras();
        playList = (ArrayList<MusicFile>) bundleObject.getSerializable("playList");
        playIndex = bundleObject.getInt("playIndex");
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
        playPre = (ImageButton) findViewById(R.id.ib_fragment_music_play_pre);
        playNext = (ImageButton) findViewById(R.id.ib_fragment_music_play_next);
    }

    private void registListener() {
        playButton.setOnClickListener(this);
        playPre.setOnClickListener(this);
        playNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_fragment_music_play:
                if (iPlayback.isPlaying()) {
                    Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
                    iPlayback.pause();
                } else {
                    Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                    iPlayback.play();
                }
                break;
            case R.id.ib_fragment_music_play_pre:
                break;
            case R.id.ib_fragment_music_play_next:
                iPlayback.setCurrentIndex(playIndex++);
                break;
        }
    }
}
