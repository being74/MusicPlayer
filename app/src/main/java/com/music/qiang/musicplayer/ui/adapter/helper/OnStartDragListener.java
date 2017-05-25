package com.music.qiang.musicplayer.ui.adapter.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xuqiang on 2017/5/24.
 */
public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
