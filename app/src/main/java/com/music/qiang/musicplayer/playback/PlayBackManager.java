package com.music.qiang.musicplayer.playback;

import android.support.v4.media.session.PlaybackStateCompat;

import com.music.qiang.musicplayer.model.MusicFile;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 播放调度管理类
 * <p>
 * Created by xuqiang on 2017/3/21.
 */
public class PlayBackManager implements IPlayback.Callback {

    // ***************类和对象***************
    private IPlayback iPlayback;
    /**
     * 播放队列管理
     */
    private QueueManager queueManager;

    // ***************基本数据***************


    public PlayBackManager(QueueManager queueManager, IPlayback playback) {
        this.queueManager = queueManager;
        this.iPlayback = playback;
        iPlayback.setCallback(this);
    }

    /**
     * 处理播放
     */
    public void handlePlay() {
        MusicFile file = queueManager.getCurrentMusic();
        iPlayback.play(file.musicId);
        // 发送变更事件
        EventBus.getDefault().post(file);
    }

    /**
     * 处理暂停
     */
    public void handlePause() {
        if (iPlayback.isPlaying()) {
            iPlayback.pause();
        }
    }

    /**
     * 切到上一首
     */
    public void handlePre() {
        if (queueManager.moveToPre()) {
            MusicFile file = queueManager.getCurrentMusic();
            iPlayback.play(file.musicId);
            // 发送变更事件
            EventBus.getDefault().post(file);
        }
    }

    /**
     * 切到下一首
     */
    public void handleNext() {
        if (queueManager.moveToNext()) {
            MusicFile file = queueManager.getCurrentMusic();
            iPlayback.play(file.musicId);
            // 发送变更事件
            EventBus.getDefault().post(file);
        }
    }

    /**
     * 处理停止
     */
    public void handleStop() {
        iPlayback.stop(false);
    }

    public void handleSeekto(int progress) {
        iPlayback.seekTo(progress);
    }

    /**
     * 在LocalPlayback中监听到mediaplay的onCompletion，通知该类进行调度处理
     */
    @Override
    public void onCompletion() {
        handleNext();
    }

    /**
     * 在LocalPlayback中监听到mediaplay的onPlaybackStatusChanged，通知该类进行调度处理
     */
    @Override
    public void onPlaybackStatusChanged(int state) {

    }

    /**
     * 在LocalPlayback中监听到mediaplay的onError，通知该类进行调度处理
     */
    @Override
    public void onError(String error) {

    }

    /**
     * LocalPlayback通知该类进行调度处理
     */
    @Override
    public void setCurrentMediaId(String mediaId) {

    }

}
