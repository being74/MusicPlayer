package com.music.qiang.musicplayer.events;

import android.media.session.PlaybackState;

/**
 * Created by user on 2017/3/28.
 */
public class PlaybackEvent {

    /**
     * 播放状态
     * 状态码取自{@link PlaybackState}STATE_NONE、STATE_STOPPED等
     *
     */
    public int state = 0;

    public PlaybackEvent(int state) {
        this.state = state;
    }
}
