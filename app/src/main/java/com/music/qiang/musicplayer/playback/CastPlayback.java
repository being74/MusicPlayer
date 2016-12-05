package com.music.qiang.musicplayer.playback;

import android.media.session.MediaSession;

/**
 * Created by user on 2016/9/30.
 */
public class CastPlayback implements Playback {

    private static final String TAG = "";

    private static final String MIME_TYPE_AUDIO_MPEG = "audio/mpeg";
    private static final String ITEM_ID = "itemId";

    private int mState;
    private Callback mCallback;
    private volatile int mCurrentPosition;
    private volatile String mCurrentMediaId;

    @Override
    public void start() {

    }

    @Override
    public void stop(boolean notifyListeners) {

    }

    @Override
    public void setState(int state) {

    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getCurrentStreamPosition() {
        return 0;
    }

    @Override
    public void setCurrentStreamPosition(int position) {

    }

    @Override
    public void updateLastKnownStreamPosition() {

    }

    @Override
    public void play(MediaSession.QueueItem item) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

    @Override
    public String getCurrentMediaId() {
        return null;
    }

    @Override
    public void setCallback(Callback callback) {

    }
}
