package com.music.qiang.musicplayer.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.music.qiang.musicplayer.R;

import java.util.List;

public class PlayMusicService extends MediaBrowserServiceCompat {

    private MediaSessionCompat mMediaSessionCompat;
    //private MediaNotificationManager mMediaNotificationManager;

    public PlayMusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create your MediaSessionCompat. You should already be doing this
        mMediaSessionCompat = new MediaSessionCompat(this, PlayMusicService.class.getSimpleName());

        setSessionToken(mMediaSessionCompat.getSessionToken());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot(
                getString(R.string.app_name), // Name visible in Android Auto
                null); // Bundle of optional extras
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        // I promise we’ll get to browsing
        result.sendResult(null);
    }
}
