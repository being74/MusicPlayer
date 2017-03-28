package com.music.qiang.musicplayer.playback;

import com.music.qiang.musicplayer.model.MusicFile;

import java.util.List;

/**
 * 播放队列管理类
 * <p>
 * Created by xuqiang on 2017/3/27.
 */
public class QueueManager {

    private int mCurrentIndex;
    private List<MusicFile> mPlayingQueue;

    public QueueManager(List<MusicFile> queue) {
        this.mPlayingQueue = queue;
        mCurrentIndex = 0;
    }

    protected void setCurrentQueue(String title, List<MusicFile> newQueue) {
        setCurrentQueue(title, newQueue, null);
    }

    protected void setCurrentQueue(String title, List<MusicFile> newQueue,
                                   String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;

        mCurrentIndex = Math.max(index, 0);
        //mListener.onQueueUpdated(title, newQueue);
    }
}
