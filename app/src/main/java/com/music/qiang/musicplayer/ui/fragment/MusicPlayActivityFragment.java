package com.music.qiang.musicplayer.ui.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.qiang.musicplayer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MusicPlayActivityFragment extends Fragment implements View.OnClickListener {

    // **************控件******************
    private View rootView;

    // **************对象******************
    private MediaPlayer mediaPlayer;

    public MusicPlayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_music_play, container, false);
        initViews();
        registListener();
        return rootView;
    }

    private void initViews() {
        mediaPlayer = new MediaPlayer();
    }

    private void registListener() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
