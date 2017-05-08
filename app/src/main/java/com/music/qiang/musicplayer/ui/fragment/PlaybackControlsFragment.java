package com.music.qiang.musicplayer.ui.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.PlaybackEvent;
import com.music.qiang.musicplayer.events.ServiceControlEvent;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.playback.QueueManager;
import com.music.qiang.musicplayer.support.utils.LogHelper;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 底部播放控制器
 */
public class PlaybackControlsFragment extends Fragment implements View.OnClickListener {

    // ~~~~~~~~~~~~~~~views~~~~~~~~~~~~~
    private View rootView;
    private ImageView thumbnail, playControl;
    private TextView musicName, musicArtist;

    // ~~~~~~~~~~~~~~~类和对象~~~~~~~~~~~~~
    private Context mContext;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private LocalPlayback localPlayback;
    private QueueManager queueManager;
    private OnFragmentInteractionListener mListener;
    // ~~~~~~~~~~~~~~~基本数据~~~~~~~~~~~~~
    private static final String TAG = LogHelper.makeLogTag(PlaybackControlsFragment.class);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_play_back, container, false);
        EventBus.getDefault().register(this);
        return rootView;
    }

    /**
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        initData();
        registerListener();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        thumbnail = (ImageView) rootView.findViewById(R.id.iv_fragment_play_back_head);
        playControl = (ImageView) rootView.findViewById(R.id.iv_fragment_play_back_control);
        musicName = (TextView) rootView.findViewById(R.id.tv_fragment_play_back_music_name);
        musicArtist = (TextView) rootView.findViewById(R.id.tv_fragment_play_back_music_artist);
    }

    private void initData() {
        localPlayback = LocalPlayback.getInstance();
        queueManager = QueueManager.getInstance();
        if (queueManager.getCurrentMusic() != null) {
            refreshUI(queueManager.getCurrentMusic());
        } else {
            rootView.setVisibility(View.GONE);
        }
        switch (localPlayback.getState()) {
            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_BUFFERING:
                playControl.setImageResource(R.mipmap.ic_music_play_dark);
                break;
            case PlaybackState.STATE_PLAYING:
                playControl.setImageResource(R.mipmap.ic_music_pause_dark);
                break;
            case PlaybackState.STATE_STOPPED:
                playControl.setImageResource(R.mipmap.ic_music_play_dark);
                break;
        }
    }

    private void refreshUI(MusicFile file) {
        if (rootView.getVisibility() != View.VISIBLE) {
            rootView.setVisibility(View.VISIBLE);
        }
        if (!StringUtils.isNullOrEmpty(file.playType) && "online".equals(file.playType)) {
            if (!StringUtils.isNullOrEmpty(file.albumpic_big)) {
                Picasso.with(mContext)
                        .load(file.albumpic_big)
                        .resize(StringUtils.dip2px(46), StringUtils.dip2px(46))
                        .error(R.mipmap.ic_black_rubber)
                        .into(thumbnail);
            } else {
                Picasso.with(mContext)
                        .load(file.albumpic_small)
                        .resize(StringUtils.dip2px(46), StringUtils.dip2px(46))
                        .error(R.mipmap.ic_black_rubber)
                        .into(thumbnail);
            }
        } else {
            Picasso.with(mContext)
                    .load(ContentUris.withAppendedId(sArtworkUri, file.musicAlubmId))
                    .resize(StringUtils.dip2px(46), StringUtils.dip2px(46))
                    .error(R.mipmap.ic_black_rubber)
                    .into(thumbnail);
        }

        musicName.setText(file.musicName);
        musicArtist.setText(file.musicArtist + " - " + file.musicAlbum);
        //playControl.setImageResource(R.mipmap.ic_music_pause_dark);
    }

    private void registerListener() {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
                startActivity(intent);
            }
        });
        playControl.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_play_back_root:
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_fragment_play_back_control:
                EventBus.getDefault().post(new ServiceControlEvent(localPlayback.getState()));
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 当前播放变更时，对ui重新赋值
     * @param file
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void mediaUpdateEvent(MusicFile file) {
        refreshUI(file);
    }

    /**
     * eventbus订阅者-播放状态修改
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void playBackEvent(PlaybackEvent event) {
        switch (event.state) {
            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_BUFFERING:
                playControl.setImageResource(R.mipmap.ic_music_play_dark);
                break;
            case PlaybackState.STATE_PLAYING:
                playControl.setImageResource(R.mipmap.ic_music_pause_dark);
                break;
            case PlaybackState.STATE_STOPPED:
                playControl.setImageResource(R.mipmap.ic_music_play_dark);
                break;
        }
    }
}
