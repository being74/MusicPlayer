package com.music.qiang.musicplayer.support.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Handling audio focus, 集中处理音频聚焦接口
 * <p/>
 * Created by user on 2016/9/30.
 */
public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {

    private Context mContext;
    AudioManager mAudioManager;

    public AudioFocusHelper(Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * requestAudioFocus的第一个参数是OnAudioFocusChangeListener回调函数
     * @return
     */
    public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);

    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        //TODO 获取焦点之后处理相关业务
    }
}
