package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;

/**
 * Created by user on 2017/5/4.
 */
public class Pay implements Serializable {
    private static final long serialVersionUID = -773162899770721102L;
    private int paydownload;

    private int payplay;

    private int timefree;

    private int payalbum;

    private int paytrackprice;

    private int payinfo;

    private int paytrackmouth;

    private int payalbumprice;

    public void setPaydownload(int paydownload){
        this.paydownload = paydownload;
    }
    public int getPaydownload(){
        return this.paydownload;
    }
    public void setPayplay(int payplay){
        this.payplay = payplay;
    }
    public int getPayplay(){
        return this.payplay;
    }
    public void setTimefree(int timefree){
        this.timefree = timefree;
    }
    public int getTimefree(){
        return this.timefree;
    }
    public void setPayalbum(int payalbum){
        this.payalbum = payalbum;
    }
    public int getPayalbum(){
        return this.payalbum;
    }
    public void setPaytrackprice(int paytrackprice){
        this.paytrackprice = paytrackprice;
    }
    public int getPaytrackprice(){
        return this.paytrackprice;
    }
    public void setPayinfo(int payinfo){
        this.payinfo = payinfo;
    }
    public int getPayinfo(){
        return this.payinfo;
    }
    public void setPaytrackmouth(int paytrackmouth){
        this.paytrackmouth = paytrackmouth;
    }
    public int getPaytrackmouth(){
        return this.paytrackmouth;
    }
    public void setPayalbumprice(int payalbumprice){
        this.payalbumprice = payalbumprice;
    }
    public int getPayalbumprice(){
        return this.payalbumprice;
    }
}
