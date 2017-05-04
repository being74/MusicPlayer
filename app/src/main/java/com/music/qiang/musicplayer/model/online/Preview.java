package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;

/**
 * Created by user on 2017/5/4.
 */
public class Preview implements Serializable {
    private static final long serialVersionUID = 384370945531019478L;
    private int trybegin;

    private int tryend;

    private int trysize;

    public void setTrybegin(int trybegin) {
        this.trybegin = trybegin;
    }

    public int getTrybegin() {
        return this.trybegin;
    }

    public void setTryend(int tryend) {
        this.tryend = tryend;
    }

    public int getTryend() {
        return this.tryend;
    }

    public void setTrysize(int trysize) {
        this.trysize = trysize;
    }

    public int getTrysize() {
        return this.trysize;
    }
}
