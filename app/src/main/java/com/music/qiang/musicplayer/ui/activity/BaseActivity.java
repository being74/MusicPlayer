package com.music.qiang.musicplayer.ui.activity;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.music.qiang.musicplayer.ui.fragment.PlaybackControlsFragment;
import com.music.qiang.musicplayer.ui.view.BaseLayout;

public class BaseActivity extends AppCompatActivity {

    private MediaBrowserCompat mMediaBrowser;
    private PlaybackControlsFragment mControlsFragment;
    private BaseLayout baseLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_base);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mControlsFragment = getFragmentManager().findFragmentById(R.id.f)
    }

    protected void setView(int layoutResId, int type) {
        baseLayout = new BaseLayout(this, layoutResId, type);
        setContentView(baseLayout);
        initToolbar();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setSupportActionBar(baseLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        baseLayout.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void hidePlaybackControls() {
        /*getFragmentManager().beginTransaction()
                .hide(mControlsFragment)
                .commit();*/
    }

    protected void showPlaybackControls() {
        /*if (NetworkHelper.isOnline(this)) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                    .show(mControlsFragment)
                    .commit();
        }*/
    }

    /**
     * Check if the MediaSession is active and in a "playback-able" state
     * (not NONE and not STOPPED).
     *
     * @return true if the MediaSession's state requires playback controls to be visible.
     */
    protected boolean shouldShowControls() {
        MediaControllerCompat mediaController = getSupportMediaController();
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            return false;
        }
        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }
}
