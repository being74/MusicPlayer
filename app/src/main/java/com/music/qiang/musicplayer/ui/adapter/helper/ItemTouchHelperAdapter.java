package com.music.qiang.musicplayer.ui.adapter.helper;

/**
 * Created by xuqiang on 2017/5/24.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int from, int to);

    void onItemDismiss(int postion);
}
