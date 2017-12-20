package com.music.qiang.musicplayer.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.MusicProgressEvent;
import com.music.qiang.musicplayer.events.PlayModeEvent;
import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.events.PlaytypeChangeManager;
import com.music.qiang.musicplayer.events.PlaytypeChangeObserver;
import com.music.qiang.musicplayer.events.QueueSkipEvent;
import com.music.qiang.musicplayer.events.ServiceControlEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.support.utils.ViewUtils;
import com.music.qiang.musicplayer.ui.view.MusicQueuePopup;
import com.music.qiang.musicplayer.ui.view.RoundImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    //***************views***************
    /**
     * 开始/暂停 按钮
     */
    private ImageView playButton, playPre, playNext, playQueueTypeIcon, playQueueMoreIcon;
    private TextView musicPlayTime, musicPlayAlltime;
    private SeekBar seekBar;
    private RoundImageView roundImageView;
    private Toolbar toolbar;
    private FrameLayout rubberLayout;

    //***************基本数据***************
    /**
     * 播放模式，0-列表循环；1-单曲循环；2-随机播放
     */
    private int currentMode = 0;
    /**
     * 标记从哪里跳转过来，list:播放列表 notification:通知栏 等等
     * 只有从播放列表过来的才重新获取播放列表
     */
    private String from;
    private ArrayList<MusicFile> playList;
    private int playIndex;
    /**
     * 当前旋转动画的值
     */
    private float currentRotationValue;

    //***************对象***************
    private SharedPreferences sharedPreferences;
    private ObjectAnimator rubberRotation;
    private MusicQueuePopup musicQueuePopup;
    /**
     * 播放业务处理类
     */
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

    /**
     * 播放队列方式改变的观察者
     */
    private PlaytypeChangeObserver changeObserver = new PlaytypeChangeObserver() {
        @Override
        public void update(Observable o, Object arg) {
            super.update(o, arg);
            currentMode = Integer.parseInt(arg.toString());
            switch (currentMode) {
                case 0:
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_dark);
                    break;
                case 1:
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_one_dark);
                    break;
                case 2:
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_random_dark);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        EventBus.getDefault().register(this);
        fetchIntents();
        initViews();
        registListener();
        initData();

        /*
         * 播放队列改变的订阅者添加订阅
         */
        PlaytypeChangeManager.getInstance().addObserver(changeObserver);
        Intent intent = new Intent(this, PlayBackService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("currentMode", currentMode);
        bundle.putString("from", from);
        if (playList != null) {
            bundle.putSerializable("playList", playList);
            bundle.putInt("playIndex", playIndex);
        }
        intent.putExtras(bundle);
        startService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopSeekbarUpdate();
        stopAnimation();
        mExecutorService.shutdown();
        /*
         * 播放队列改变的订阅者取消订阅
         */
        PlaytypeChangeManager.getInstance().deleteObserver(changeObserver);
    }

    private void fetchIntents() {
        Bundle bundleObject = getIntent().getExtras();
        if (bundleObject != null) {
            from = bundleObject.getString("from");
            if (from != null && "list".equals(from)) {
                playList = (ArrayList<MusicFile>) bundleObject.getSerializable("playList");
                playIndex = bundleObject.getInt("playIndex");
            }
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle(playList.get(playIndex).musicName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rubberLayout = (FrameLayout) findViewById(R.id.fl_activity_music_play_rubber);
        roundImageView = (RoundImageView) findViewById(R.id.riv_activity_music_play);
        playButton = (ImageView) findViewById(R.id.ib_fragment_music_play);
        playPre = (ImageView) findViewById(R.id.ib_fragment_music_play_pre);
        playNext = (ImageView) findViewById(R.id.ib_fragment_music_play_next);
        playQueueTypeIcon = (ImageView) findViewById(R.id.iv_activity_music_play_type);
        playQueueMoreIcon = (ImageView) findViewById(R.id.iv_activity_music_play_more);
        seekBar = (SeekBar) findViewById(R.id.sb_activity_music_play);
        musicPlayTime = (TextView) findViewById(R.id.tv_activity_music_play_time);
        musicPlayAlltime = (TextView) findViewById(R.id.tv_activity_music_play_all_time);
    }

    private void initData() {
        // 设置数字格式，如01
        df.setMinimumIntegerDigits(2);
        localPlayback = LocalPlayback.getInstance();
        sharedPreferences = getSharedPreferences("playback", MODE_PRIVATE);
        currentMode = sharedPreferences.getInt("playMode", 0);
        switch (currentMode) {
            case 0:
                playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_dark);
                break;
            case 1:
                playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_one_dark);
                break;
            case 2:
                playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_random_dark);
                break;
        }
        // 如果进入页面时正在播放，开始旋转动画
        if (localPlayback.getState() == PlaybackState.STATE_PLAYING && rubberRotation == null) {
            startAnimation();
        }
        switch (localPlayback.getState()) {
            case PlaybackState.STATE_PAUSED:
                playButton.setImageResource(R.mipmap.ic_music_play_dark);
                pauseAnimation();
                break;
            case PlaybackState.STATE_BUFFERING:
            case PlaybackState.STATE_STOPPED:
                playButton.setImageResource(R.mipmap.ic_music_play_dark);
                pauseAnimation();
                break;
            case PlaybackState.STATE_PLAYING:
                playButton.setImageResource(R.mipmap.ic_music_pause_dark);
                startAnimation();
                break;
        }
    }

    private void registListener() {
        playButton.setOnClickListener(this);
        playPre.setOnClickListener(this);
        playNext.setOnClickListener(this);
        playQueueTypeIcon.setOnClickListener(this);
        playQueueMoreIcon.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String minute = df.format((progress / 1000 / 60) % 60);
                String second = df.format((progress / 1000) % 60);
                musicPlayTime.setText(minute + ":" + second);
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
        Log.d("xuqiang", "---------MusicPlayActivity  refreshUI--------" + file.musicName);
        setBlurBackground(file);

        toolbar.setTitle(file.musicName);
        toolbar.setSubtitle(file.musicArtist);

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
            if (!StringUtils.isNullOrEmpty(file.playType) && "online".equals(file.playType)) {
                if (!StringUtils.isNullOrEmpty(file.albumpic_big)) {
                    Picasso.with(this)
                            .load(file.albumpic_big)
                            .into(roundImageView);
                } else {
                    Picasso.with(this)
                            .load(file.albumpic_small)
                            .into(roundImageView);
                }
            } else {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId));
                //roundImageView.setImageBitmap(bitmap);
                //final Bitmap blurBmp = BlurUtil.fastblur(this, bitmap, 25);//0-25，表示模糊值
                //final Bitmap blurBmp = FastBlurUtil.doBlur(bitmap, 8, false);//0-25，表示模糊值
                //final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp); // 将Bitmap转换为Drawable
                Picasso.with(this)
                        .load(ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId))
                        .into(roundImageView);
                Log.d("xuqiang", ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId).toString());
            }
            if (roundImageView.getVisibility() != View.VISIBLE) {
                roundImageView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            roundImageView.setVisibility(View.GONE);
        }
    }

    /**
     * 开始动画
     * */
    @SuppressLint("NewApi")
    public void startAnimation() {
        // 设置动画，从上次停止位置开始,这里是顺时针旋转360度
        rubberRotation = ObjectAnimator.ofFloat(rubberLayout, "Rotation",
                currentRotationValue - 360, currentRotationValue);
        // 设置持续时间 dev set 50000
        rubberRotation.setDuration(45000);
        // 设置循环播放
        rubberRotation.setRepeatCount(ObjectAnimator.INFINITE);
        rubberRotation.setInterpolator(new LinearInterpolator());
        // 设置动画监听
        rubberRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 监听动画执行的位置，以便下次开始时，从当前位置开始
                currentRotationValue = (Float) animation.getAnimatedValue();

            }
        });
        rubberRotation.start();
    }

    /**
     * 暂停动画
     * */
    @SuppressLint("NewApi")
    public void pauseAnimation() {
        if (rubberRotation != null) {
            rubberRotation.cancel();
        }
        rubberLayout.clearAnimation();// 清除此View身上的动画
    }

    private void stopAnimation() {
        if (rubberRotation != null) {
            rubberRotation.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_fragment_music_play:
                LocalPlayback playback = LocalPlayback.getInstance();
                switch (playback.getState()) {
                    case PlaybackState.STATE_PLAYING:
                        stopSeekbarUpdate();
                        break;
                }
                EventBus.getDefault().post(new ServiceControlEvent(playback.getState()));
                break;
            case R.id.ib_fragment_music_play_pre:
                EventBus.getDefault().post(new QueueSkipEvent(0));
                break;
            case R.id.ib_fragment_music_play_next:
                EventBus.getDefault().post(new QueueSkipEvent(1));
                break;
            // 切换播放模式，列表 列表循环 单曲循环
            case R.id.iv_activity_music_play_type:
                if (currentMode == 0) {
                    currentMode = 1;
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_one_dark);
                    ViewUtils.toast(this, "单曲循环", true);
                } else if (currentMode == 1) {
                    currentMode = 2;
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_random_dark);
                    ViewUtils.toast(this, "随机播放", true);
                } else if (currentMode == 2) {
                    currentMode = 0;
                    playQueueTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_dark);
                    ViewUtils.toast(this, "列表循环", true);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("playMode", currentMode);
                editor.apply();
                EventBus.getDefault().post(new PlayModeEvent(currentMode));
                break;
            case R.id.iv_activity_music_play_more:
                handleMusicQueuePopup();
                break;
        }
    }

    private void handleMusicQueuePopup() {
        musicQueuePopup = new MusicQueuePopup(this, currentMode);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final WindowManager.LayoutParams lp = window.getAttributes();
        musicQueuePopup.setAnimationStyle(R.style.AnimBottom);
        // 弹出popupwindow
        musicQueuePopup.showAtLocation(findViewById(R.id.ll_activity_music_play), Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗(渐变动画)
        final ValueAnimator showAnimation = ValueAnimator.ofFloat(1.0f, 0.4f);
        showAnimation.setDuration(300);
        showAnimation.start();
        showAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.alpha = (float) valueAnimator.getAnimatedValue();
                window.setAttributes(lp);
            }
        });
        musicQueuePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                final ValueAnimator animation = ValueAnimator.ofFloat(0.4f, 1.0f);
                animation.setDuration(300);
                animation.start();

                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        lp.alpha = (float) valueAnimator.getAnimatedValue();
                        window.setAttributes(lp);
                    }
                });
            }
        });
    }

    /**
     * 当前播放变更时，对ui重新赋值
     * @param file
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void mediaUpdateEvent(MusicFile file) {
        refreshUI(file);
    }

    /**
     * eventbus订阅者-播放状态修改
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void playBackEvent(PlaybackEvent event) {
        switch (event.state) {
            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_BUFFERING:
            case PlaybackState.STATE_STOPPED:
                playButton.setImageResource(R.mipmap.ic_music_play_dark);
                pauseAnimation();
                break;
            case PlaybackState.STATE_PLAYING:
                playButton.setImageResource(R.mipmap.ic_music_pause_dark);
                startAnimation();
                break;

        }
    }

}
