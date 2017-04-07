package com.music.qiang.musicplayer.events;

/**
 * 播放队列模式变化的事件
 * Created by xuqiang on 2017/4/7.
 */
public class PlayModeEvent {
    /**
     * 播放模式，0-列表循环；1-单曲循环；2-随机播放
     */
    public int mode;

    public PlayModeEvent(int mode) {
        this.mode = mode;
    }
}
