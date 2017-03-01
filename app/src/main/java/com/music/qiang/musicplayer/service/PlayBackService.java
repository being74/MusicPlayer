package com.music.qiang.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.IPlayback;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;

import java.util.ArrayList;

/**
 * 通过service方式管理后台播放音乐的服务
 */
public class PlayBackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private MediaPlayer mediaPlayer;

    private ArrayList<MusicFile> playList;
    private int currentIndex = 0;
    private long musicId;

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
            // 模拟一个耗时操作
            try {
                //Thread.sleep(5000);
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            // 通过startId来停止一个服务，以防影响其他正在执行的服务
            //stopSelf(msg.arg1);
        }
    }

    public PlayBackService() {
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArgument",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(this, MusicPlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .addAction(R.drawable.ic_menu_manage, "Previous", prevPendingIntent) // #0
                .addAction(R.drawable.ic_menu_send, "Pause", pausePendingIntent)  // #1
                .addAction(R.drawable.ic_menu_share, "Next", nextPendingIntent)     // #2
                .setStyle(new android.support.v7.app.NotificationCompat.MediaStyle())
                .setContentIntent(contentIntent);
        Notification notification = mBuilder.build();

        mNotifyMgr.notify(1, notification);
        startForeground(1, notification);
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
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Bundle bundleObject = intent.getExtras();
        playList = (ArrayList<MusicFile>) bundleObject.getSerializable("playList");
        musicId = playList.get(currentIndex).musicId;
        //musicId = intent.getLongExtra("ID", 0);
        Log.d("xuqiang", musicId + "---------");
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();

    }

    /**
     * Called when all clients have disconnected from a particular interface
     * published by the service.  The default implementation does nothing and
     * returns false.
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return true if you would like to have the service's
     * {@link #onRebind} method later called when new clients bind to it.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service destory", Toast.LENGTH_SHORT).show();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "media prepared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this, "media onCompletion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "media onError", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 播放类的binder
     */
    private class MusicBinder extends Binder implements IPlayback {
        @Override
        public void start() {

        }

        @Override
        public void stop(boolean notifyListeners) {

        }

        @Override
        public void setState(int state) {

        }

        @Override
        public int getState() {
            return 0;
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean isPlaying() {
            return false;
        }

        @Override
        public int getCurrentStreamPosition() {
            return 0;
        }

        @Override
        public void setCurrentStreamPosition(int position) {

        }

        @Override
        public void updateLastKnownStreamPosition() {

        }

        @Override
        public void play(MediaSession.QueueItem item) {

        }

        @Override
        public void pause() {

        }

        @Override
        public void seekTo(int position) {

        }

        @Override
        public void setCurrentMediaId(String mediaId) {

        }

        @Override
        public String getCurrentMediaId() {
            return null;
        }

        @Override
        public void setCallback(Callback callback) {

        }
    }
}
