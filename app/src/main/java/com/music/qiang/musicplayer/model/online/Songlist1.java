package com.music.qiang.musicplayer.model.online;

/**
 * Created by user on 2017/5/4.
 */
public class Songlist1 {
    private String title;

    private String mb;

    private Data data;

    private Vid vid;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setMb(String mb) {
        this.mb = mb;
    }

    public String getMb() {
        return this.mb;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public void setVid(Vid vid) {
        this.vid = vid;
    }

    public Vid getVid() {
        return this.vid;
    }
}
