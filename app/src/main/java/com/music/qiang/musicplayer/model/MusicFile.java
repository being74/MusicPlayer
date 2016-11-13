package com.music.qiang.musicplayer.model;

import java.io.Serializable;

/**
 * Created by qiang on 2016/06/27.
 */
public class MusicFile implements Serializable {

    private static final long serialVersionUID = -6068044584522608925L;

    public int musicId; // 音乐ID
    public String musicName; // 音乐名称
    public String musicArtist; // 艺术家名称
    public String musicPath; // 音乐路径
    public String musicAlbum; // 所在专辑名称
    public String musicType; // 音乐类别
    public String musicTime; // 音乐时长
    public String musicSize; // 音乐文件大小

}
