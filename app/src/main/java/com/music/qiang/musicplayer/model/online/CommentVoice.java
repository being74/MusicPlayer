package com.music.qiang.musicplayer.model.online;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017/5/4.
 */
public class CommentVoice implements Serializable {
    private static final long serialVersionUID = -7762498108762807116L;
    private List<Songlist1> songlist;

    private int total_song_num;

    private int color;

    private int cur_song_num;

    private int song_begin;

    public void setSonglist(List<Songlist1> songlist) {
        this.songlist = songlist;
    }

    public List<Songlist1> getSonglist() {
        return this.songlist;
    }

    public void setTotal_song_num(int total_song_num) {
        this.total_song_num = total_song_num;
    }

    public int getTotal_song_num() {
        return this.total_song_num;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setCur_song_num(int cur_song_num) {
        this.cur_song_num = cur_song_num;
    }

    public int getCur_song_num() {
        return this.cur_song_num;
    }

    public void setSong_begin(int song_begin) {
        this.song_begin = song_begin;
    }

    public int getSong_begin() {
        return this.song_begin;
    }
}
