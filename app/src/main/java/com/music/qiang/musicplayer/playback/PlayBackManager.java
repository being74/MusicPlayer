package com.music.qiang.musicplayer.playback;

import com.music.qiang.musicplayer.model.MusicFile;

import java.util.ArrayList;

/**
 * 播放调度管理类
 * <p>
 * Created by xuqiang on 2017/3/21.
 */
public class PlayBackManager implements IPlayback.Callback {

    // ***************类和对象***************
    private IPlayback iPlayback;
    // ***************基本数据***************
    private ArrayList<MusicFile> musicList;


    public PlayBackManager(IPlayback playback, ArrayList<MusicFile> musicList) {
        this.musicList = musicList;
        this.iPlayback = playback;
        iPlayback.setCallback(this);
    }

    public void handlePlay() {
        iPlayback.play(String.valueOf(musicList.get(0).musicId));
    }

    public void handlePause() {
        if (iPlayback.isPlaying()) {
            iPlayback.pause();
        }
    }

    public void handleStop() {
        iPlayback.stop(false);
    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onPlaybackStatusChanged(int state) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

}
