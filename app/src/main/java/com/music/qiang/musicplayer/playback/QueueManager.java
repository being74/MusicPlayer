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

    /*public void setQueueFromList(String initialMediaId) {
        setCurrentQueue();
    }*/

    public void setCurrentQueue(List<MusicFile> newQueue) {
        setCurrentQueue(newQueue, null);
    }

    public void setCurrentQueue(List<MusicFile> newQueue, String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = getIndexOnQueue(initialMediaId);
        }

        mCurrentIndex = Math.max(index, 0);

        //mListener.onQueueUpdated(title, newQueue);
    }

    /**
     * 上一首
     *
     * @return true:可以播放上一首，没有越界  false：已经是第一首
     */
    public boolean moveToPre() {
        if (mCurrentIndex > 0) {
            mCurrentIndex--;
        } else {
            return false;
        }
        return true;
    }

    /**
     * 下一首
     *
     * @return true:可以播放下一首，没有越界  false：最后一首了
     */
    public boolean moveToNext() {
        if (mCurrentIndex < mPlayingQueue.size() - 1) {
            mCurrentIndex++;
        } else {
            return false;
        }
        return true;
    }

    /**
     * 根据mediaid获取在播放队列中的位置
     *
     * @param target 目标mediaid
     * @return 队列中的位置
     */
    private int getIndexOnQueue(String target) {
        int index = 0;
        for (int i = 0; i < mPlayingQueue.size(); i++) {
            if (target.equals(mPlayingQueue.get(i).musicId)) {
                mCurrentIndex = index;
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * 获取当前应该播放的媒体
     *
     * @return MusicFile对象
     */
    public MusicFile getCurrentMusic() {
        return mPlayingQueue.get(mCurrentIndex);
    }
}
