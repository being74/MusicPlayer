package com.music.qiang.musicplayer.events;

/**
 * 当用户切换开始/暂停时给后台service发送的事件
 *
 * @see com.music.qiang.musicplayer.service.PlayBackService#playControlEvent(ServiceControlEvent) 播放service唯一订阅者
 * <p>
 * Created by xuqiang on 2017/04/08.
 */

public class ServiceControlEvent {
    /**
     * 播放状态
     * <p>
     * 状态码取自{@link android.media.session.PlaybackState#STATE_NONE#STATE_STOPPED等}
     */
    public int state;

    public ServiceControlEvent(int state) {
        this.state = state;
    }
}
