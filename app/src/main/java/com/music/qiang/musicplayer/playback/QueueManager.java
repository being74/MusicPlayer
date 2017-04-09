package com.music.qiang.musicplayer.playback;

import android.util.Log;

import com.music.qiang.musicplayer.model.MusicFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 播放队列管理类
 * <p>
 * Created by xuqiang on 2017/3/27.
 */
public class QueueManager {

    /**
     * 当前播放队列的播放序号
     */
    private int mCurrentIndex = 0;
    /**
     * 当前播放媒体id
     */
    private String mCurrentMediaId;
    /**
     * 播放模式，0-列表循环；1-单曲循环；2-随机播放
     */
    private int mCurrentMode = 0;
    /**
     * 默认播放队列
     */
    private List<MusicFile> mQueue;
    /**
     * 实际的播放队列，会将列表循环、单曲循环、随机播放存到这个对象中
     */
    private List<MusicFile> mPlayingQueue;

    private static QueueManager instance;

    private QueueManager(List<MusicFile> queue) {
        if (queue != null && queue.size() > 0) {
            this.mQueue = queue;
        }
        mCurrentIndex = 0;
        mCurrentMediaId = mQueue.get(mCurrentIndex).musicId;
    }

    public static QueueManager getInstance(List<MusicFile> queue) {
        if (instance == null) {
            synchronized (QueueManager.class) {
                if (instance == null) {
                    instance = new QueueManager(queue);
                }
            }
        }
        return instance;
    }


    public void setCurrentQueue(List<MusicFile> newQueue) {
        setCurrentQueue(newQueue, null);
    }

    /**
     * 设置列表循环队列
     * @param newQueue
     * @param initialMediaId
     */
    public void setCurrentQueue(List<MusicFile> newQueue, String initialMediaId) {
        mCurrentMode = 0;
        // 1. 设置实际播放队列
        if (newQueue != null && newQueue.size() > 0) {
            mQueue = newQueue;
        } else {
            // 获取在队列已经初始化但是有变化的情况下当前播放
            mCurrentMediaId = getCurrentMusic().musicId;
        }
        //mPlayingQueue = mQueue;
        mPlayingQueue = new ArrayList<>(mQueue.size());
        Iterator<MusicFile> iterator = mQueue.iterator();
        while(iterator.hasNext()){
            mPlayingQueue.add(iterator.next().clone());
        }

        // 2. 重新配置当前播放在队列中的位置
        int index = 0;
        if (initialMediaId != null) {
            index = getIndexOnQueue(initialMediaId);
        } else {
            index = getIndexOnQueue(mCurrentMediaId);
        }

        mCurrentIndex = Math.max(index, 0);
        mCurrentMediaId = mPlayingQueue.get(mCurrentIndex).musicId;
        Log.d("xuqiang", "setCurrentQueue#mCurrentIndex = " + mCurrentIndex);

        //mListener.onQueueUpdated(title, newQueue);
    }

    /**
     * 设置单曲循环队列
     * @param newQueue
     * @param initialMediaId
     */
    public void setSingleCycleQueue(List<MusicFile> newQueue, String initialMediaId) {
        setCurrentQueue(newQueue, initialMediaId);
        mCurrentMode = 1;
        /*// 1. 设置默认播放队列
        MusicFile mf = new MusicFile();
        if (newQueue != null && newQueue.size() > 0) {
            mQueue = newQueue;
        } else {
            // 获取在队列已经初始化但是有变化的情况下当前播放
            mCurrentMediaId = getCurrentMusic().musicId;
            mf = getCurrentMusic();
        }
        // 设置实际播放队列，单曲循环只有一个
        if (mPlayingQueue == null) {
            mPlayingQueue = new ArrayList<>();
        } else {
            mPlayingQueue.clear();
        }
        //mPlayingQueue.add(getCurrentMusic());
        // 2. 重新配置当前播放在队列中的位置
        //int index = 0;
        if (initialMediaId != null) {
            for (int i = 0; i < mQueue.size(); i++) {
                if (initialMediaId.equals(mQueue.get(i).musicId)) {
                    mPlayingQueue.add(mQueue.get(i));
                    break;
                }
            }
        } else {
            mPlayingQueue.add(mf);
        }
        mCurrentIndex = 0;
        mCurrentMediaId = mPlayingQueue.get(0).musicId;*/
        Log.d("xuqiang", "setSingleCycleQueue#mCurrentIndex = " + mCurrentIndex);
    }

    /**
     * 获取随机播放队列
     *
     * @param newQueue 新的播放队列，若为空表示没有新的播放队列
     * @param initialMediaId 初始化的播放媒体id
     */
    public void setRandomQueue(List<MusicFile> newQueue, String initialMediaId) {
        mCurrentMode = 2;
        // 1. 设置实际播放队列
        if (newQueue != null && newQueue.size() > 0) {
            mQueue = newQueue;
        } else {
            // 获取在队列已经初始化但是有变化的情况下当前播放
            mCurrentMediaId = getCurrentMusic().musicId;
        }
        //mPlayingQueue = mQueue;
        mPlayingQueue = mQueue.subList(0, mQueue.size());
        mPlayingQueue = new ArrayList<>(mQueue.size());
        Iterator<MusicFile> iterator = mQueue.iterator();
        while(iterator.hasNext()){
            mPlayingQueue.add(iterator.next().clone());
        }
        Collections.shuffle(mPlayingQueue);

        // 2. 重新配置当前播放在队列中的位置
        int index = 0;
        if (initialMediaId != null) {
            index = getIndexOnQueue(initialMediaId);
        } else {
            index = getIndexOnQueue(mCurrentMediaId);
        }

        mCurrentIndex = Math.max(index, 0);
        mCurrentMediaId = mPlayingQueue.get(mCurrentIndex).musicId;
        Log.d("xuqiang", "setRandomQueue#mCurrentIndex = " + mCurrentIndex);
    }

    public List<MusicFile> getCurrentQueue() {
        if (mPlayingQueue != null && mPlayingQueue.size() > 0) {
            return mPlayingQueue;
        } else {
            return null;
        }
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
            if (mPlayingQueue != null && mPlayingQueue.size() > 0) {
                mCurrentIndex = mPlayingQueue.size() - 1;
            }
        }

        return true;
    }

    /**
     * 下一首
     * @param skip 主要区别于单曲循环播放时区分，当单曲循环时用户点击了“下一首”按钮可以播放队列中的下一首
     *             当单曲循环中播放完当前歌曲自动播放下一首时，不能切到播放队列的下一首
     *             true: 可以切到下一首
     *             false: 不能切到下一首
     *
     * @return true:可以播放下一首，没有越界
     */
    public boolean moveToNext(boolean skip) {
        // 单曲循环
        if (mCurrentMode == 1) {
            // 单曲循环且由用户点击下一首，则可以切到下一首
            if (skip) {
                if (mCurrentIndex < mPlayingQueue.size() - 1) {
                    mCurrentIndex++;
                } else {
                    mCurrentIndex = 0;
                }
                // skip为false,表示是自动播放下一首，则播放序号不变
            } else {
                // TODO 操作待定
            }
        } else {
            if (mCurrentIndex < mPlayingQueue.size() - 1) {
                mCurrentIndex++;
            } else {
                mCurrentIndex = 0;
            }
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
     * 根据mediaid获取在播放队列中的位置
     *
     * @param target
     * @return
     */
    /*private int getIndexOnRandomQueue(String target) {
        if (mRandomQueue != null && mRandomQueue.size() > 0) {
            int index = 0;
            for (int i = 0; i < mRandomQueue.size(); i++) {
                if (target.equals(mRandomQueue.get(i).musicId)) {
                    mCurrentIndex = index;
                    return index;
                }
                index++;
            }
        }
        return -1;
    }*/

    /**
     * 获取当前应该播放的媒体
     *
     * @return MusicFile对象
     */
    public MusicFile getCurrentMusic() {
        if (mPlayingQueue != null && mPlayingQueue.size() > 0) {
            return mPlayingQueue.get(mCurrentIndex);
        } else {
            return null;
        }
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

}
