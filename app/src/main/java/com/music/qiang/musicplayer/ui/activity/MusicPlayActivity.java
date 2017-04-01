package com.music.qiang.musicplayer.ui.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.MusicProgressEvent;
import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.events.QueueSkipEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.BlurUtil;
import com.music.qiang.musicplayer.support.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    //***************views***************
    /**
     * 开始/暂停 按钮
     */
    private ImageView playButton, playPre, playNext, background_image;
    private TextView musicName, musicArtist, musicPlayTime, musicPlayAlltime;
    private SeekBar seekBar;

    //***************基本数据***************
    private ArrayList<MusicFile> playList;
    private int playIndex;

    //***************对象***************
    private LocalPlayback localPlayback;
    private DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;
    private final Handler mHandler = new Handler();
    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("xuqiang", "----------onCreate----------");
        setContentView(R.layout.activity_music_play);
        EventBus.getDefault().register(this);
        fetchIntents();
        initViews();
        registListener();
        initData();
        refreshUI(playList.get(playIndex));

        Intent intent = new Intent(this, PlayBackService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playList", playList);
        bundle.putInt("playIndex", playIndex);
        intent.putExtras(bundle);
        startService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xuqiang", "----------onStop----------");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSeekbarUpdate();
        mExecutorService.shutdown();
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
        setTitle(playList.get(playIndex).musicName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        background_image = (ImageView) findViewById(R.id.background_image);
        playButton = (ImageView) findViewById(R.id.ib_fragment_music_play);
        playPre = (ImageView) findViewById(R.id.ib_fragment_music_play_pre);
        playNext = (ImageView) findViewById(R.id.ib_fragment_music_play_next);
        musicName = (TextView) findViewById(R.id.tv_fragment_music_play_name);
        musicArtist = (TextView) findViewById(R.id.tv_fragment_music_play_artist);
        seekBar = (SeekBar) findViewById(R.id.sb_activity_music_play);
        musicPlayTime = (TextView) findViewById(R.id.tv_activity_music_play_time);
        musicPlayAlltime = (TextView) findViewById(R.id.tv_activity_music_play_all_time);
    }

    private void initData() {
        df.setMinimumIntegerDigits(2);
    }

    private void registListener() {
        playButton.setOnClickListener(this);
        playPre.setOnClickListener(this);
        playNext.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //musicPlayTime.setText(df.f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopSeekbarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventBus.getDefault().post(new MusicProgressEvent(seekBar.getProgress()));
                scheduleSeekbarUpdate();
            }
        });
    }

    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }

    private void updateSeekbar() {
        if (localPlayback == null) {
            return;
        }
        long currentPosition = localPlayback.getCurrentStreamPosition();
        Log.d("xuqiang", "currentPosition = " + currentPosition);
        /*if (mLastPlaybackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }*/
        seekBar.setProgress((int) currentPosition);
        String minute = df.format((currentPosition / 1000 / 60) % 60);
        String second = df.format((currentPosition / 1000) % 60);
        musicPlayTime.setText(minute + ":" + second);
    }

    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private void refreshUI(MusicFile file) {
        /*Picasso.with(this)
                .load(ContentUris.withAppendedId(sArtworkUri, playList.get(playIndex).musicAlubmId))
                .error(R.mipmap.ic_black_rubber)
                .into(background_image);*/
        localPlayback = LocalPlayback.getInstance(this);
        setBlurBackground(file);

        setTitle(file.musicName);
        musicName.setText(file.musicName);
        musicArtist.setText(file.musicArtist + " - " + file.musicAlbum);

        if (StringUtils.canParseInt(file.musicTime)) {
            // 音乐时长，单位毫秒
            int musicTime = Integer.parseInt(file.musicTime);
            seekBar.setMax(musicTime);

            scheduleSeekbarUpdate();

            String minute = df.format((musicTime / 1000 / 60) % 60);
            String second = df.format((musicTime / 1000) % 60);
            musicPlayAlltime.setText(minute + ":" + second);
        }

    }

    /**
     * 设置毛玻璃背景
     */
    @SuppressWarnings("deprecation")
    private void setBlurBackground(MusicFile file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId));
            final Bitmap blurBmp = BlurUtil.fastblur(this, bitmap, 25);//0-25，表示模糊值
            //final Bitmap blurBmp = FastBlurUtil.doBlur(bitmap, 8, false);//0-25，表示模糊值
            //final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp); // 将Bitmap转换为Drawable
            background_image.setImageBitmap(blurBmp);
        } catch (IOException e) {
            e.printStackTrace();
            background_image.setImageResource(R.drawable.fullscreen_toolbar_bg_gradient);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_fragment_music_play:
                LocalPlayback playback = LocalPlayback.getInstance(this);
                switch (playback.getState()) {
                    case PlaybackState.STATE_PAUSED:
                    case PlaybackState.STATE_BUFFERING:
                        playButton.setImageResource(R.mipmap.ic_music_pause);
                        break;
                    case PlaybackState.STATE_PLAYING:
                        playButton.setImageResource(R.mipmap.ic_music_play);
                        break;
                }
                EventBus.getDefault().post(new PlaybackEvent(playback.getState()));
                break;
            case R.id.ib_fragment_music_play_pre:
                EventBus.getDefault().post(new QueueSkipEvent(0));
                break;
            case R.id.ib_fragment_music_play_next:
                EventBus.getDefault().post(new QueueSkipEvent(1));
                break;
        }
    }

    /**
     * 当前播放变更时，对ui重新赋值
     * @param file
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void mediaUpdateEvent(MusicFile file) {
        refreshUI(file);
    }

}
