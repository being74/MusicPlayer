package com.music.qiang.musicplayer.ui.activity;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.IPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.FastBlurUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    //***************views***************
    /**
     * 开始/暂停 按钮
     */
    private ImageView playButton, playPre, playNext, musicThumbnail;
    private LinearLayout ll_fragment_music_play;
    private TextView musicName, musicArtist;

    //***************基本数据***************
    private ArrayList<MusicFile> playList;
    private int playIndex;

    //***************对象***************
    private IPlayback iPlayback;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

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
        initData();

        long musicId = getIntent().getIntExtra("musicId", 0);

        Intent intent = new Intent(this, PlayBackService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playList", playList);
        bundle.putInt("playIndex", playIndex);
        intent.putExtras(bundle);
        //bindService(intent, connection, BIND_AUTO_CREATE);
        startService(intent);
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

        ll_fragment_music_play = (LinearLayout) findViewById(R.id.ll_fragment_music_play);
        playButton = (ImageView) findViewById(R.id.ib_fragment_music_play);
        playPre = (ImageView) findViewById(R.id.ib_fragment_music_play_pre);
        playNext = (ImageView) findViewById(R.id.ib_fragment_music_play_next);
        musicThumbnail = (ImageView) findViewById(R.id.iv_fragment_music_play_black_rubber);
        musicName = (TextView) findViewById(R.id.tv_fragment_music_play_name);
        musicArtist = (TextView) findViewById(R.id.tv_fragment_music_play_artist);
    }

    private void registListener() {
        playButton.setOnClickListener(this);
        playPre.setOnClickListener(this);
        playNext.setOnClickListener(this);
    }

    private void initData() {
        Picasso.with(this)
                .load(ContentUris.withAppendedId(sArtworkUri, playList.get(playIndex).musicAlubmId))
                //.resize(StringUtils.dip2px(48), StringUtils.dip2px(48))
                .error(R.mipmap.ic_black_rubber)
                .into(musicThumbnail);
        setBlurBackground();


        musicName.setText(playList.get(playIndex).musicName);
        musicArtist.setText(playList.get(playIndex).musicArtist + " - " + playList.get(playIndex).musicAlbum);
    }

    /**
     * 设置毛玻璃背景
     */
    @SuppressWarnings("deprecation")
    private void setBlurBackground() {
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(),id);//从资源文件中得到图片，并生成Bitmap图片
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ContentUris.withAppendedId(sArtworkUri, playList.get(playIndex).musicAlubmId));
            //final Bitmap blurBmp = BlurUtil.fastblur(this, bitmap, 25);//0-25，表示模糊值
            final Bitmap blurBmp = FastBlurUtil.doBlur(bitmap, 8, false);//0-25，表示模糊值
            final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp); // 将Bitmap转换为Drawable
            ll_fragment_music_play.setBackgroundDrawable(newBitmapDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_fragment_music_play:
                if (iPlayback.isPlaying()) {
                    playButton.setImageResource(R.mipmap.ic_music_play);
                    iPlayback.pause();
                } else {
                    playButton.setImageResource(R.mipmap.ic_music_pause);
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
