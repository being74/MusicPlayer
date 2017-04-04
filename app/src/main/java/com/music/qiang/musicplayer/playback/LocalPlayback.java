package com.music.qiang.musicplayer.playback;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.music.qiang.musicplayer.application.MyApp;
import com.music.qiang.musicplayer.support.utils.LogHelper;

import java.io.IOException;

/**
 * 媒体播放业务类
 * <p>
 * Created by xuqiang on 2016/9/30.
 */
public class LocalPlayback implements IPlayback, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

    // *****************基本数据类型*******************
    private static final String TAG = "";
    /**
     * 没有音频焦点，且不能用很小的音量播放(简称duck)
     */
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    /**
     * 没有音频焦点，但是可以用很小的音量播放
     */
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    /**
     * 有完全的焦点
     */
    private static final int AUDIO_FOCUSED = 2;
    /**
     * duck时的音量
     */
    public static final float VOLUME_DUCK = 0.2f;
    /**
     * 有音频焦点时正常的音量
     */
    public static final float VOLUME_NORMAL = 1.0f;
    /**
     * 当前播放状态
     */
    private int mState;
    /**
     * 媒体当前播放位置
     */
    private volatile int mCurrentPosition;
    /**
     * 当前播放id
     */
    private volatile String mCurrentMediaId;
    /**
     * 音频焦点的状态
     */
    private int mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
    private boolean mPlayOnFocusGain;

    // *****************类和对象*******************
    private final Context mContext;
    /**
     * 单例实例
     */
    private volatile static LocalPlayback instance;
    /**
     * 媒体播放对象
     */
    private MediaPlayer mMediaPlayer;
    /**
     * 音频管理类
     */
    private AudioManager mAudioManager;
    /**
     * WiFi锁，通常情况在屏幕熄灭2分钟后为了省电WiFi就会进入睡眠状态
     */
    private final WifiManager.WifiLock mWifiLock;
    /**
     * IPlayback中的回调方法
     */
    private Callback mCallback;

    private LocalPlayback() {
        this.mContext = MyApp.getSelf();
        this.mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        this.mWifiLock = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "musicplayer");
        this.mState = PlaybackStateCompat.STATE_NONE;
    }

    public static LocalPlayback getInstance() {
        if (instance == null) {
            synchronized (LocalPlayback.class) {
                if (instance == null) {
                    instance = new LocalPlayback();
                }
            }
        }
        return instance;
    }

    /**
     * 创建MediaPlayer对象
     */
    private void createMediaPlayerIfNeeded() {
        LogHelper.d(TAG, "createMediaPlayerIfNeeded. needed? ", (mMediaPlayer == null));
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            // 防止在播放中mediaplay失去唤醒锁
            mMediaPlayer.setWakeMode(mContext.getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);

            // 当播放发生状态变化通知我们
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
        } else {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void play(String id) {
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        boolean mediaHasChanged = !TextUtils.equals(id, mCurrentMediaId);
        if (mediaHasChanged) {
            mCurrentPosition = 0;
            mCurrentMediaId = id;
        }
        if (mState == PlaybackStateCompat.STATE_PLAYING && !mediaHasChanged && mMediaPlayer != null) {

        } else if (mState == PlaybackStateCompat.STATE_PAUSED && !mediaHasChanged && mMediaPlayer != null) {
            configMediaPlayerState();
        } else {
            try {
                createMediaPlayerIfNeeded();
                mState = PlaybackStateCompat.STATE_BUFFERING;
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
                mMediaPlayer.setDataSource(mContext, contentUri);

                mMediaPlayer.prepareAsync();
                mWifiLock.acquire();

                if (mCallback != null) {
                    mCallback.onPlaybackStatusChanged(mState);
                }

            } catch (IOException ex) {
                LogHelper.e(TAG, ex, "Exception playing song");
                if (mCallback != null) {
                    mCallback.onError(ex.getMessage());
                }
            }
        }

    }

    @Override
    public void pause() {
        if (mState == PlaybackStateCompat.STATE_PLAYING) {
            // Pause media player and cancel the 'foreground service' state.
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentPosition = mMediaPlayer.getCurrentPosition();
            }
            // while paused, retain the MediaPlayer but give up audio focus
            relaxResources(false);
        }
        mState = PlaybackStateCompat.STATE_PAUSED;
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer == null) {
            // If we do not have a current media player, simply update the current position
            mCurrentPosition = position;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mState = PlaybackStateCompat.STATE_BUFFERING;
            }
            mMediaPlayer.seekTo(position);
            if (mCallback != null) {
                mCallback.onPlaybackStatusChanged(mState);
            }
        }
    }

    @Override
    public void stop(boolean notifyListeners) {
        mState = PlaybackStateCompat.STATE_STOPPED;
        mCurrentPosition = getCurrentStreamPosition();
        relaxResources(true);
    }

    @Override
    public void setState(int state) {
        this.mState = state;
    }

    @Override
    public int getState() {
        return this.mState;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mMediaPlayer != null && mMediaPlayer.isPlaying());
    }

    @Override
    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : mCurrentPosition;
    }

    @Override
    public void setCurrentStreamPosition(int position) {
        this.mCurrentPosition = position;
    }

    @Override
    public void updateLastKnownStreamPosition() {
        if (mMediaPlayer != null) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public void setCurrentIndex(int index) {

    }

    @Override
    public int getCurrentIndex() {
        return 0;
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return this.mCurrentMediaId;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }


    /**
     * 释放资源，包括WiFi锁和mediaplay对象等
     *
     * @param releaseMediaPlayer 是否要是否mediaplay
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        LogHelper.d(TAG, "relaxResources. releaseMediaPlayer=", releaseMediaPlayer);

        // 释放mediaplay对象
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // 如果还持有WiFi锁，就释放掉
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    /**
     * 尝试获取audio焦点
     */
    private void tryToGetAudioFocus() {
        LogHelper.d(TAG, "tryToGetAudioFocus");
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_FOCUSED;
        } else {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    /**
     * Reconfigures MediaPlayer according to audio focus settings and
     * starts/restarts it. This method starts/restarts the MediaPlayer
     * respecting the current audio focus state. So if we have focus, it will
     * play normally; if we don't have focus, it will either leave the
     * MediaPlayer paused or set it to a low volume, depending on what is
     * allowed by the current focus settings. This method assumes mPlayer !=
     * null, so if you are calling it, you have to do so from a context where
     * you are sure this is the case.
     */
    private void configMediaPlayerState() {
        LogHelper.d(TAG, "configMediaPlayerState. mAudioFocus=", mAudioFocus);
        if (mAudioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            // 没有音频焦点并且不能用很小音量播放，所以就暂停
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                pause();
            }

        } else {
            //registerAudioNoisyReceiver();
            // 没有音频焦点，但是可以duck
            if (mAudioFocus == AUDIO_NO_FOCUS_CAN_DUCK) {
                // 小音量播放
                mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK);
            } else {
                if (mMediaPlayer != null) {
                    // 正常音量
                    mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL);
                }
            }
            // 当在播放的时候失去焦点时，重新播放
            if (mPlayOnFocusGain) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    LogHelper.d(TAG, "configMediaPlayerState startMediaPlayer. seeking to ",
                            mCurrentPosition);
                    if (mCurrentPosition == mMediaPlayer.getCurrentPosition()) {
                        mMediaPlayer.start();
                        mState = PlaybackStateCompat.STATE_PLAYING;
                    } else {
                        mMediaPlayer.seekTo(mCurrentPosition);
                        mState = PlaybackStateCompat.STATE_BUFFERING;
                    }
                }
                mPlayOnFocusGain = false;
            }
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    /**
     * 音频焦点发生变化
     *
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        LogHelper.d(TAG, "onAudioFocusChange. focusChange=", focusChange);
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // 获取了焦点
            mAudioFocus = AUDIO_FOCUSED;

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // 失去了焦点，但是如果可以duck就用小音量播放，否则就暂停
            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            mAudioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            // If we are playing, we need to reset media player by calling configMediaPlayerState
            // with mAudioFocus properly set.

            if (mState == PlaybackStateCompat.STATE_PLAYING && !canDuck) {
                // If we don't have audio focus and can't duck, we save the information that
                // we were playing, so that we can resume playback once we get the focus back.
                mPlayOnFocusGain = true;
            }
        } else {
            LogHelper.e(TAG, "onAudioFocusChange: Ignoring unsupported focusChange: ", focusChange);
        }
        configMediaPlayerState();
    }

    /**
     * MediaPlayer播放结束
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCallback != null) {
            mCallback.onCompletion();
        }
    }

    /**
     * MediaPlayer播放出现错误
     *
     * @param mp
     * @param what
     * @param extra
     * @return true表示错误处理了
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mCallback != null) {
            mCallback.onError("MediaPlayer error " + what + " (" + extra + ")");
        }
        return true;
    }

    /**
     * MediaPlayer准备好
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        configMediaPlayerState();
    }

    /**
     * MediaPlayer跳到某个播放位置完毕
     *
     * @param mp
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mCurrentPosition = mp.getCurrentPosition();
        if (mState == PlaybackStateCompat.STATE_BUFFERING) {
            mMediaPlayer.start();
            mState = PlaybackStateCompat.STATE_PLAYING;
        }
    }


}
