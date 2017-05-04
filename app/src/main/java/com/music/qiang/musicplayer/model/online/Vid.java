package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;

/**
 * Created by user on 2017/5/4.
 */
public class Vid implements Serializable {

    private static final long serialVersionUID = 1566688020103692122L;
    private String Fvid;

    private String Fmv_id;

    private String Fstatus;

    public void setFvid(String Fvid) {
        this.Fvid = Fvid;
    }

    public String getFvid() {
        return this.Fvid;
    }

    public void setFmv_id(String Fmv_id) {
        this.Fmv_id = Fmv_id;
    }

    public String getFmv_id() {
        return this.Fmv_id;
    }

    public void setFstatus(String Fstatus) {
        this.Fstatus = Fstatus;
    }

    public String getFstatus() {
        return this.Fstatus;
    }
}
