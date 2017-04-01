package com.music.qiang.musicplayer.events;

/**
 * 播放进度事件
 * <p>
 * Created by xuqiang on 2017/4/1.
 */
public class MusicProgressEvent {

    public int progress;

    public MusicProgressEvent(int progress) {
        this.progress = progress;
    }
}
