package com.music.qiang.musicplayer.ui.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.music.qiang.musicplayer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MusicPlayActivityFragment extends Fragment implements View.OnClickListener {

    // **************控件******************
    private Button startMusic, pauseMusic;
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
        startMusic = (Button) rootView.findViewById(R.id.btn_fragment_music_play_start);
        pauseMusic = (Button) rootView.findViewById(R.id.btn_fragment_music_play_pause);
    }

    private void registListener() {
        startMusic.setOnClickListener(this);
        pauseMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_music_play_start:
                break;

            case R.id.btn_fragment_music_play_pause:
                break;
        }
    }
}
