package com.music.qiang.musicplayer.support;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.events.QueueSkipEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 通知管理类
 * <p>
 * Created by xuqiang on 2017/04/04.
 */

public class MediaNotificationManager extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 234;
    private static final int REQUEST_CODE = 100;
    private boolean mStarted;

    public static final String ACTION_PAUSE = "com.music.qiang.musicplayer.pause";
    public static final String ACTION_PLAY = "com.music.qiang.musicplayer.play";
    public static final String ACTION_PREV = "com.music.qiang.musicplayer.prev";
    public static final String ACTION_NEXT = "com.music.qiang.musicplayer.next";
    public static final String ACTION_STOP_CASTING = "com.music.qiang.musicplayer.stop_cast";
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    private PlayBackService mPlayBackService;

    private RemoteViews mRemoteViews;
    private final NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder builder;
    private Intent resultIntent;
    private final PendingIntent mPauseIntent;
    private final PendingIntent mPlayIntent;
    private final PendingIntent mPreviousIntent;
    private final PendingIntent mNextIntent;
    private final PendingIntent mStopCastIntent;

    public MediaNotificationManager(PlayBackService mPlayBackService) {
        this.mPlayBackService = mPlayBackService;

        String pkg = mPlayBackService.getPackageName();

        mNotificationManager = NotificationManagerCompat.from(mPlayBackService);
        mPauseIntent = PendingIntent.getBroadcast(mPlayBackService, REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent = PendingIntent.getBroadcast(mPlayBackService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPreviousIntent = PendingIntent.getBroadcast(mPlayBackService, REQUEST_CODE,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mNextIntent = PendingIntent.getBroadcast(mPlayBackService, REQUEST_CODE,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mStopCastIntent = PendingIntent.getBroadcast(mPlayBackService, REQUEST_CODE,
                new Intent(ACTION_STOP_CASTING).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY:
                EventBus.getDefault().post(new PlaybackEvent(LocalPlayback.getInstance().getState()));
                break;
            case ACTION_PAUSE:
                EventBus.getDefault().post(new PlaybackEvent(LocalPlayback.getInstance().getState()));
                break;
            case ACTION_PREV:
                EventBus.getDefault().post(new QueueSkipEvent(0));
                break;
            case ACTION_NEXT:
                EventBus.getDefault().post(new QueueSkipEvent(1));
                break;
            case ACTION_STOP_CASTING:
                stopNotification();
                break;
        }
    }

    public void showNotification() {
        if (!mStarted) {
            mRemoteViews = new RemoteViews(mPlayBackService.getPackageName(), R.layout.notifi_play_back);
            mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_thumb, R.mipmap.ic_black_rubber);
            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_name, "忽然");
            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_artist, "李志");
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_play, mPlayIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_pre, mPreviousIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_next, mNextIntent);

            builder = new NotificationCompat.Builder(mPlayBackService);
            builder.setTicker("开始播放音乐");// 收到通知的时候用于显示于屏幕顶部通知栏的内容
            builder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小图标,在下拉之前显示的图标
            builder.setLargeIcon(BitmapFactory.decodeResource(mPlayBackService.getResources(), R.mipmap.ic_launcher));// 落下后显示的图标
            builder.setWhen(System.currentTimeMillis());
            builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等
            builder.setAutoCancel(true);// 自动取消
            builder.setOnlyAlertOnce(true);// 只alert一次
            builder.setCustomContentView(mRemoteViews);
            builder.setSound(null);
            builder.setDefaults(Notification.DEFAULT_ALL);
            //mRemoteViews.setTextViewText(R.id.notify_time, getCurrentTime());
            //builder.setContent(mRemoteViews);

            resultIntent = new Intent(mPlayBackService, MusicPlayActivity.class);
            resultIntent.setPackage(mPlayBackService.getPackageName());
            PendingIntent pendingIntent = PendingIntent.getActivity(mPlayBackService, REQUEST_CODE, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notification.contentView = mRemoteViews;
            notification.bigContentView = mRemoteViews;
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            mNotificationManager.notify(NOTIFICATION_ID, notification);

            if (notification != null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);
                filter.addAction(ACTION_STOP_CASTING);
                mPlayBackService.registerReceiver(this, filter);

                mPlayBackService.startForeground(NOTIFICATION_ID, notification);
                mStarted = true;
            }
        }
    }

    public void stopNotification() {
        if (mStarted) {
            mStarted = false;
            try {
                mNotificationManager.cancelAll();
                mPlayBackService.unregisterReceiver(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlayBackService.stopForeground(true);
        }
    }

    public void refreshUI(MusicFile file) {
        mRemoteViews.setImageViewUri(R.id.iv_notifi_play_back_thumb, ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId));
        mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_name, file.musicName);
        mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_artist, file.musicArtist);

        Notification notification = builder.build();
        notification.contentView = mRemoteViews;
        notification.bigContentView = mRemoteViews;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
