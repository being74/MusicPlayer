package com.music.qiang.musicplayer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

import com.music.qiang.musicplayer.R;

/**
 * 包含一个顶部的ToolBar和底部的播放控制的一个公共基本布局
 * <p/>
 * Created by xuqiang on 2017/5/2.
 */
public class BaseLayout extends RelativeLayout {

    private Context mContext;
    private View topLayout, playbackControlLayout;

    public Toolbar toolbar;


    public BaseLayout(Context context) {
        super(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseLayout(Context context, int layoutResourceId, int type) {
        super(context);
        this.mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setToolbar(layoutInflater);
        setBottomControl(layoutInflater);

        View view = layoutInflater.inflate(layoutResourceId, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        params.addRule(RelativeLayout.BELOW, R.id.rl_base_toolbar);
        params.addRule(RelativeLayout.ABOVE, R.id.fragment_playback_controls);
        addView(view, params);
    }

    /**
     * 设置顶部toolbar
     * @param layoutInflater
     */
    private void setToolbar(LayoutInflater layoutInflater) {
        topLayout = layoutInflater.inflate(R.layout.base_toolbar, null);
        toolbar = (Toolbar) topLayout.findViewById(R.id.toolbar);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(topLayout, params);
    }

    /**
     * 设置底部播放控制
     * @param layoutInflater
     */
    private void setBottomControl(LayoutInflater layoutInflater) {
        playbackControlLayout = layoutInflater.inflate(R.layout.layout_bottom_playback_controls, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(playbackControlLayout, params);
    }
}
