package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;

/**
 * Created by user on 2017/5/4.
 */
public class Showapi_res_body implements Serializable {
    private static final long serialVersionUID = 4784541877172505069L;
    private int ret_code;

    private Pagebean pagebean;

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getRet_code() {
        return this.ret_code;
    }

    public void setPagebean(Pagebean pagebean) {
        this.pagebean = pagebean;
    }

    public Pagebean getPagebean() {
        return this.pagebean;
    }
}
