package com.music.qiang.musicplayer.support;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;

/**
 * 通知管理类
 * <p>
 * Created by xuqiang on 2017/04/04.
 */

public class MediaNotificationManager extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 234;
    private static final int REQUEST_CODE = 100;

    public static final String ACTION_PAUSE = "com.music.qiang.musicplayer.pause";
    public static final String ACTION_PLAY = "com.music.qiang.musicplayer.play";
    public static final String ACTION_PREV = "com.music.qiang.musicplayer.prev";
    public static final String ACTION_NEXT = "com.music.qiang.musicplayer.next";
    public static final String ACTION_STOP_CASTING = "com.music.qiang.musicplayer.stop_cast";

    private PlayBackService mPlayBackService;

    private final NotificationManagerCompat mNotificationManager;
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
                Toast.makeText(mPlayBackService, ACTION_PLAY, Toast.LENGTH_SHORT).show();
                break;
            case ACTION_PAUSE:
                Toast.makeText(mPlayBackService, ACTION_PAUSE, Toast.LENGTH_SHORT).show();
                break;
            case ACTION_PREV:
                Toast.makeText(mPlayBackService, ACTION_PREV, Toast.LENGTH_SHORT).show();
                break;
            case ACTION_NEXT:
                Toast.makeText(mPlayBackService, ACTION_NEXT, Toast.LENGTH_SHORT).show();
                break;
            case ACTION_STOP_CASTING:
                Toast.makeText(mPlayBackService, ACTION_STOP_CASTING, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showNotification() {
        RemoteViews mRemoteViews = new RemoteViews(mPlayBackService.getPackageName(), R.layout.notifi_play_back);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_notifi_play_back_play, mPlayIntent);

        Notification.Builder builder = new Notification.Builder(mPlayBackService);
        builder.setTicker("Hello RemotesViews!");// 收到通知的时候用于显示于屏幕顶部通知栏的内容
        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小图标,在下拉之前显示的图标
        builder.setLargeIcon(BitmapFactory.decodeResource(mPlayBackService.getResources(), R.mipmap.ic_launcher));// 落下后显示的图标
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等
        builder.setAutoCancel(true);// 自动取消
        builder.setOnlyAlertOnce(true);// 只alert一次
        builder.setDefaults(Notification.DEFAULT_ALL);
        mRemoteViews.setImageViewResource(R.id.iv_notifi_play_back_thumb, R.mipmap.ic_black_rubber);
        mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_name, "这是自定义view的title");
        mRemoteViews.setTextViewText(R.id.tv_notifi_play_back_artist, "这里是自定义view的内容");
        //mRemoteViews.setTextViewText(R.id.notify_time, getCurrentTime());
        builder.setContent(mRemoteViews);

        Intent intent = new Intent(mPlayBackService, MusicPlayActivity.class);
        //intent.putExtra(SINGLE, REMOTE_VIEWS_NOTIFICATION);
        intent.setPackage(mPlayBackService.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(mPlayBackService, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
        mPlayBackService.startForeground(NOTIFICATION_ID, notification);
    }
}
