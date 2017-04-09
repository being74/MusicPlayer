package com.music.qiang.musicplayer.model;

import java.io.Serializable;

/**
 * 专辑信息
 * Created by xuqiang on 2017/04/09.
 */

public class AlbumFile implements Serializable {
    private static final long serialVersionUID = -4461462269667821218L;

    public long albumId;
    public String albumName;
    public String albumArtist;
    public String albumKey;
    public String albumSongNumber;
}
