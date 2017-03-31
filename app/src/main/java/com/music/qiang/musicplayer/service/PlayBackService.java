package com.music.qiang.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.events.QueueSkipEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.playback.PlayBackManager;
import com.music.qiang.musicplayer.playback.QueueManager;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 通过service方式管理后台播放音乐的服务
 */
public class PlayBackService extends Service {

    //*****************类和对象*******************
    private PlayBackManager playBackManager;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager notificationManager;
    private MediaPlayer mediaPlayer;
    private ArrayList<MusicFile> playList;

    //*****************基本数据类型****************
    private int currentIndex = 0;
    private String musicId;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    // 开始播放
                    try {
                        /*Uri contentUri = ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();*/
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
            }
            Log.d("xuqiang", "msg.arg1=" + msg.arg1);
            // 通过startId来停止一个服务，以防影响其他正在执行的服务
            //stopSelf(msg.arg1);
            //stopSelfResult(msg.arg1);
        }
    }

    public PlayBackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Log.d("xuqiang", "service---onCreate---");

        HandlerThread thread = new HandlerThread("ServiceStartArgument",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * <p>
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     * <p>
     * <p>If you need your application to run on platform versions prior to API
     * level 5, you can use the following model to handle the older {@link #onStart}
     * callback in that case.  The <code>handleCommand</code> method is implemented by
     * you as appropriate:
     * <p>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
     * start_compatibility}
     * <p>
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     * <p/>
     * 必须返回整型数。整型数是一个值，用于描述系统应该如何在服务终止的情况下继续运行服务
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundleObject = intent.getExtras();
        playList = (ArrayList<MusicFile>) bundleObject.getSerializable("playList");
        currentIndex = bundleObject.getInt("playIndex");
        musicId = playList.get(currentIndex).musicId;

        Log.d("xuqiang", "-----startId----" + startId);
        /*Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);*/

        QueueManager queueManager = new QueueManager(playList);
        queueManager.setCurrentQueue(playList, String.valueOf(musicId));
        LocalPlayback playback = LocalPlayback.getInstance(this);
        // 创建播放类管理者
        playBackManager = new PlayBackManager(queueManager, playback, playList);
        playBackManager.handlePlay();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service destory", Toast.LENGTH_SHORT).show();
        mediaPlayer.release();
        mediaPlayer = null;
        EventBus.getDefault().unregister(this);
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .addAction(R.mipmap.ic_music_play_rewind, "Previous", prevPendingIntent) // #0
                .addAction(R.mipmap.ic_music_play, "Pause", pausePendingIntent)  // #1
                .addAction(R.mipmap.ic_music_play_forward, "Next", nextPendingIntent)     // #2
                .setStyle(new android.support.v7.app.NotificationCompat.MediaStyle())
                .setContentIntent(contentIntent);
        Notification notification = mBuilder.build();

        notificationManager.notify(1, notification);
        startForeground(1, notification);
    }

    /**
     * eventbus订阅者-播放状态修改
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void playBackEvent(PlaybackEvent event) {
        Log.d("xuqiang", "haha: " + event.state);
        switch (event.state) {
            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_BUFFERING:
                playBackManager.handlePlay();
                break;
            case PlaybackState.STATE_PLAYING:
                playBackManager.handlePause();
                break;
        }
    }

    /**
     * eventbus订阅者-播放队列切换
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void queueSkipEvent(QueueSkipEvent event) {
        Log.d("xuqiang", "haha: " + event.cmd);
        switch (event.cmd) {
            case 0:
                playBackManager.handlePre();
                break;
            case 1:
                playBackManager.handleNext();
                break;
        }
    }
}
