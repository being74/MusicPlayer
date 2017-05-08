package com.music.qiang.musicplayer.support;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.QueueSkipEvent;
import com.music.qiang.musicplayer.events.ServiceControlEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;
import com.squareup.picasso.Picasso;

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

    private final NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder builder;
    private Intent resultIntent;
    private final PendingIntent mPauseIntent;
    private final PendingIntent mPlayIntent;
    private final PendingIntent mPreviousIntent;
    private final PendingIntent mNextIntent;
    private final PendingIntent mStopCastIntent;
    private Notification notification;

    private RemoteViews mRemoteViews, mRemoteViewsSmall;

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
                EventBus.getDefault().post(new ServiceControlEvent(LocalPlayback.getInstance().getState()));
                break;
            case ACTION_PAUSE:
                EventBus.getDefault().post(new ServiceControlEvent(LocalPlayback.getInstance().getState()));
                break;
            case ACTION_PREV:
                EventBus.getDefault().post(new QueueSkipEvent(0));
                break;
            case ACTION_NEXT:
                EventBus.getDefault().post(new QueueSkipEvent(1));
                break;
            case ACTION_STOP_CASTING:
                mPlayBackService.stopPlay();
                break;
        }
    }

    public void showNotification() {
        if (!mStarted) {
            // 通知大视图初始化
            mRemoteViews = new RemoteViews(mPlayBackService.getPackageName(), R.layout.notifi_play_back);
            mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_thumb, R.mipmap.ic_black_rubber);
            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_name, "忽然");
            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_artist, "李志");
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_play, mPlayIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_pre, mPreviousIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_next, mNextIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_close, mStopCastIntent);
            // 通知小视图初始化
            mRemoteViewsSmall = new RemoteViews(mPlayBackService.getPackageName(), R.layout.notifi_play_back_small);
            mRemoteViewsSmall.setImageViewResource(R.id.iv_notifi_play_back_small_thumb, R.mipmap.ic_black_rubber);
            mRemoteViewsSmall.setTextViewText(R.id.tv_notifi_play_back_small_name, "忽然");
            mRemoteViewsSmall.setTextViewText(R.id.tv_notifi_play_back_small_artist, "李志");
            mRemoteViewsSmall.setOnClickPendingIntent(R.id.iv_notifi_play_back_small_play, mPlayIntent);
            mRemoteViewsSmall.setOnClickPendingIntent(R.id.iv_notifi_play_back_small_next, mNextIntent);
            mRemoteViewsSmall.setOnClickPendingIntent(R.id.iv_notifi_play_back_small_close, mStopCastIntent);

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
            //builder.setContent(mRemoteViews);

            resultIntent = new Intent(mPlayBackService, MusicPlayActivity.class);
            resultIntent.setPackage(mPlayBackService.getPackageName());
            PendingIntent pendingIntent = PendingIntent.getActivity(mPlayBackService, REQUEST_CODE, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);

            notification = builder.build();
            notification.bigContentView = mRemoteViews;
            notification.contentView = mRemoteViewsSmall;
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

    /**
     * 更新remoteViews
     * @param file
     */
    public void refreshUI(MusicFile file) {
        if (!mStarted) {
            showNotification();
        }
        if (file != null) {
            // 给大尺寸通知赋值
            if (!StringUtils.isNullOrEmpty(file.playType) && "online".equals(file.playType)) {
                if (!StringUtils.isNullOrEmpty(file.albumpic_big)) {
                    // 大视图的图标
                    Picasso.with(mPlayBackService)
                            .load(file.albumpic_big)
                            .into(mRemoteViews, R.id.iv_notifi_play_back_thumb, NOTIFICATION_ID, notification);
                    // 小视图的图标
                    Picasso.with(mPlayBackService)
                            .load(file.albumpic_big)
                            .into(mRemoteViews, R.id.iv_notifi_play_back_small_thumb, NOTIFICATION_ID, notification);
                } else {
                    // 大视图的图标
                    Picasso.with(mPlayBackService)
                            .load(file.albumpic_small)
                            .into(mRemoteViews, R.id.iv_notifi_play_back_thumb, NOTIFICATION_ID, notification);
                    // 小视图的图标
                    Picasso.with(mPlayBackService)
                            .load(file.albumpic_small)
                            .into(mRemoteViews, R.id.iv_notifi_play_back_small_thumb, NOTIFICATION_ID, notification);
                }
            } else {
                mRemoteViews.setImageViewUri(R.id.iv_notifi_play_back_thumb, ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId));
                mRemoteViewsSmall.setImageViewUri(R.id.iv_notifi_play_back_small_thumb, ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId));
            }

            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_name, file.musicName);
            mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_artist, file.musicArtist);
            // 给小尺寸通知赋值

            mRemoteViewsSmall.setTextViewText(R.id.tv_notifi_play_back_small_name, file.musicName);
            mRemoteViewsSmall.setTextViewText(R.id.tv_notifi_play_back_small_artist, file.musicArtist);
        }

        LocalPlayback localPlayback = LocalPlayback.getInstance();
        switch (localPlayback.getState()) {
            case PlaybackState.STATE_PAUSED:
                mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_play, R.mipmap.ic_music_play);
                mRemoteViewsSmall.setImageViewResource(R.id.iv_notifi_play_back_small_play, R.mipmap.ic_music_play);
                break;
            case PlaybackState.STATE_BUFFERING:
                mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_play, R.mipmap.ic_music_play);
                mRemoteViewsSmall.setImageViewResource(R.id.iv_notifi_play_back_small_play, R.mipmap.ic_music_play);
                break;
            case PlaybackState.STATE_PLAYING:
                mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_play, R.mipmap.ic_music_pause);
                mRemoteViewsSmall.setImageViewResource(R.id.iv_notifi_play_back_small_play, R.mipmap.ic_music_pause);
                break;
        }

        Notification notification = builder.build();
        notification.bigContentView = mRemoteViews;
        notification.contentView = mRemoteViewsSmall;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
