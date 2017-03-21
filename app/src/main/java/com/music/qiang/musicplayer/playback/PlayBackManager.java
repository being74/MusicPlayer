package com.music.qiang.musicplayer.playback;

/**
 * Created by user on 2017/3/21.
 */
public class PlayBackManager implements IPlayback.Callback {

    private IPlayback iPlayback;

    public PlayBackManager(IPlayback playback) {
        this.iPlayback = playback;
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
