package com.music.qiang.musicplayer.events;

/**
 * Created by xuqiang on 2017/3/31.
 */
public class QueueSkipEvent {
    /**
     * 0:上一首  1：下一首
     */
    public int cmd = -1;

    public QueueSkipEvent(int cmd) {
        this.cmd = cmd;
    }
}
