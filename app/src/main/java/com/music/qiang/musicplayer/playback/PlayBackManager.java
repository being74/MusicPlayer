package com.music.qiang.musicplayer.playback;

import android.media.MediaPlayer;

import com.music.qiang.musicplayer.model.MusicFile;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/21.
 */
public class PlayBackManager implements IPlayback.Callback {

    private IPlayback iPlayback;
    private ArrayList<MusicFile> musicList;

    public PlayBackManager(IPlayback playback, ArrayList<MusicFile> musicList) {
        this.musicList = musicList;
        this.iPlayback = playback;
        iPlayback.setCallback(this);
    }

    public void handlePlay() {
        //iPlayback.play(musicList.get(0).musicId);
    }

    public void handlePause() {
        if (iPlayback.isPlaying()) {
            iPlayback.pause();
        }
    }

    public void handleStop() {

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
