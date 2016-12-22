package com.music.qiang.musicplayer.support.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.music.qiang.musicplayer.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by user on 2016/12/21.
 */
public class MusicUtils {

    public static Resources resources;
    static int musicID = 1;
    private static final Uri sArtworkUri = Uri
            .parse("content://media/external/audio/media/" + musicID
                    + "/albumart");

    /**
     * 将MP3里图片读取出来
     *
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    public static Bitmap getMusicBitmap(Context context, long songid, long albumid) {
        resources = context.getResources();
        Bitmap bm = BitmapFactory.decodeResource(resources, R.mipmap.ic_black_rubber);
        // 判断相关数据
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
                else{
                    bm = BitmapFactory.decodeResource(resources, R.mipmap.ic_black_rubber);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {
        }
        return bm;
    }
}
