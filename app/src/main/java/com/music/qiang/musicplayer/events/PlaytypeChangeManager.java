package com.music.qiang.musicplayer.events;

import java.util.Observable;

/**
 * 队列播放方式改变的观察者管理类
 * <p/>
 * Created by xuqiang on 2017/5/23.
 */
public class PlaytypeChangeManager extends Observable {

    private static PlaytypeChangeManager instance = null;

    private PlaytypeChangeManager() {

    }

    public static PlaytypeChangeManager getInstance() {
        if (null == instance) {
            instance = new PlaytypeChangeManager();
        }
        return instance;
    }

    /**
     * 通知观察者有变更
     *
     * @param date 变更后的播放方式的值
     */
    public void notifyChange(int date) {
        //首先设置为有事件变动
        setChanged();
        // 通知观察者进行变更
        notifyObservers(date);
    }
}
