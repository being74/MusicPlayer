package com.music.qiang.musicplayer.events;

import android.media.session.PlaybackState;

import com.music.qiang.musicplayer.service.PlayBackService;

/**
 * 当后台service处理完state切换时，发送给ui界面进行更新的事件
 * Created by user on 2017/3/28.
 */
public class PlaybackEvent {

    /**
     * 播放状态
     * 状态码取自{@link PlaybackState#STATE_NONE#STATE_STOPPED等}
     *
     * 相关订阅者在下面链接
     * @see PlayBackService#playBackEvent(PlaybackEvent) 播放service的订阅
     * @see com.music.qiang.musicplayer.ui.activity.MusicPlayActivity#playBackEvent(PlaybackEvent) 播放页的订阅
     * @see com.music.qiang.musicplayer.ui.fragment.PlaybackControlsFragment#playBackEvent(PlaybackEvent) 首页控制栏的订阅
     */
    public int state = 0;
    /**
     * 发送方编号，0：ui  1：后台service切换状态完毕
     */
    public int from;

    public PlaybackEvent(int state) {
        this.state = state;
    }
}
