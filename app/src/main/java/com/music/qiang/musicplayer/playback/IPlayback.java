package com.music.qiang.musicplayer.playback;

import android.media.session.PlaybackState;

/**
 * 播放类主接口
 * <p>
 * Created by xuqiang on 2016/9/30.
 */
public interface IPlayback {

    /**
     * 开始播放
     */
    void start();

    /**
     * 停止播放
     *
     * @param notifyListeners
     */
    void stop(boolean notifyListeners);

    /**
     * 设置最近的播放状态
     *
     * @param state
     */
    void setState(int state);

    /**
     * 获取当前状态 {@link PlaybackState#getState()}
     *
     * @return
     */
    int getState();

    /**
     * @return 是否准备好
     */
    boolean isConnected();

    /**
     * @return 检测播放器是否正在播放
     */
    boolean isPlaying();

    /**
     * @return 当前正在播放的位置
     */
    int getCurrentStreamPosition();

    /**
     * 设定当前正在播放的序号
     *
     * @param position 序号
     */
    void setCurrentStreamPosition(int position);

    /**
     *
     */
    void updateLastKnownStreamPosition();

    /**
     * 播放
     */
    void play(String id);

    /**
     * 暂停
     */
    void pause();

    /**
     * @param position seek to position
     */
    void seekTo(int position);

    /**
     * 设置当前mediaId
     *
     * @param mediaId
     */
    void setCurrentMediaId(String mediaId);

    /**
     * @return 当前mediaId
     */
    String getCurrentMediaId();

    void setCurrentIndex(int index);

    int getCurrentIndex();

    interface Callback {

        /**
         * 当前音乐完成
         */
        void onCompletion();

        /**
         * 播放状态出现改变
         *
         * @param state
         */
        void onPlaybackStatusChanged(int state);

        /**
         * @param error
         */
        void onError(String error);

        /**
         * @param mediaId
         */
        void setCurrentMediaId(String mediaId);
    }

    /**
     * @param callback 被调用
     */
    void setCallback(Callback callback);

}
