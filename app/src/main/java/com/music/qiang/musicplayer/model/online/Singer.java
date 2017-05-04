package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;

/**
 * Created by user on 2017/5/4.
 */
public class Singer implements Serializable {
    private static final long serialVersionUID = -4226838168820129997L;
    private int id;

    private String name;

    private String mid;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMid() {
        return this.mid;
    }
}
