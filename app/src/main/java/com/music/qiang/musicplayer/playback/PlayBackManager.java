package com.music.qiang.musicplayer.playback;

import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

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
    /**
     * 播放类型，分为"online"和”local“
     */
    private String playType;

    public PlayBackManager(QueueManager queueManager, IPlayback playback) {
        this.queueManager = queueManager;
        this.iPlayback = playback;
        iPlayback.setCallback(this);
    }

    public void setPlayType(String type) {
        this.playType = type;
    }

    /**
     * 处理播放
     */
    public void handlePlay() {
        MusicFile file = queueManager.getCurrentMusic();
        if (file != null) {
            if (!StringUtils.isNullOrEmpty(playType) && "online".equals(playType)) {
                iPlayback.playOnline(file);
            } else {
                iPlayback.play(file.musicId);
            }
            // 发送变更事件
            EventBus.getDefault().post(file);
        }
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
            if (file != null) {
                if (!StringUtils.isNullOrEmpty(playType) && "online".equals(playType)) {
                    iPlayback.playOnline(file);
                } else {
                    iPlayback.play(file.musicId);
                }
                // 发送变更事件
                EventBus.getDefault().post(file);
            }
        }
    }

    /**
     * 切到下一首
     * @param skip 主要区别于单曲循环播放时区分，当单曲循环时用户点击了“下一首”按钮可以播放队列中的下一首
     *             当单曲循环中播放完当前歌曲自动播放下一首时，不能切到播放队列的下一首
     *             true: 可以切到下一首
     *             false: 不能切到下一首
     */
    public void handleNext(boolean skip) {
        if (queueManager.moveToNext(skip)) {
            MusicFile file = queueManager.getCurrentMusic();
            if (file != null) {
                if (!StringUtils.isNullOrEmpty(playType) && "online".equals(playType)) {
                    iPlayback.playOnline(file);
                } else {
                    iPlayback.play(file.musicId);
                }
                // 发送变更事件
                EventBus.getDefault().post(file);
            }
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
        handleNext(false);
    }

    /**
     * 在LocalPlayback中监听到mediaplay的onPlaybackStatusChanged，通知该类进行调度处理
     */
    @Override
    public void onPlaybackStatusChanged(int state) {
        EventBus.getDefault().post(new PlaybackEvent(state));
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
