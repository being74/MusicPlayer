package com.music.qiang.musicplayer.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.playback.QueueManager;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.adapter.PopupMusicListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by xuqiang on 2017/5/8.
 */
public class MusicQueuePopup extends PopupWindow implements View.OnClickListener {

    // **************类和对象*****************
    private Context mContext;
    private LayoutInflater inflater;
    private RecyclerView.LayoutManager mLayoutManager;
    private PopupMusicListAdapter musicListAdapter;
    private QueueManager queueManager;
    private LocalPlayback localPlayback;

    // **************views*****************
    private View convertView;
    private RecyclerView recyclerView;

    // **************基本数据*****************
    private ArrayList<MusicFile> musicFiles;
    /**
     * 当前播放在默认队列中的位置
     */
    private int currentIndex = 0;
    /**
     * 播放模式，0-列表循环；1-单曲循环；2-随机播放
     */
    private int currentMode = 0;

    public MusicQueuePopup(Context context, int playMode) {
        super(context);
        this.mContext = context;
        this.currentMode = playMode;
        //this.musicFiles = musicFiles;
        EventBus.getDefault().register(this);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.popup_music_queue, null);
        localPlayback = LocalPlayback.getInstance();
        queueManager = QueueManager.getInstance();
        musicFiles = (ArrayList<MusicFile>) queueManager.getDefaultQueue();

        initPopupWindow();
        initView();
        initData();
        registerListener();
    }

    /**
     * 初始化popupwindow属性
     */
    private void initPopupWindow() {
        this.setContentView(convertView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(StringUtils.dip2px(360));
        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        //this.setAnimationStyle(R.style.AnimBottom);
        //this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.colorWhite)));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        recyclerView = (RecyclerView) convertView.findViewById(R.id.rv_popup_music_queue);
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        musicListAdapter = new PopupMusicListAdapter(mContext, musicFiles);
        musicListAdapter.setSelectedPos(currentIndex);
        recyclerView.setAdapter(musicListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        musicListAdapter.setOnItemClickListener(new PopupMusicListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_item_popup_music_queue_remove:
                        queueManager.removeFromQueue(musicFiles.get(position));
                        musicListAdapter.updateList(queueManager.getDefaultQueue());
                        updateUI(queueManager.getCurrentMusic());
                        break;
                    default:
                        Intent intent = new Intent(mContext, PlayBackService.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentMode", currentMode);
                        bundle.putString("from", "list");
                        bundle.putSerializable("playList", musicFiles);
                        bundle.putInt("playIndex", position);
                        intent.putExtras(bundle);
                        mContext.startService(intent);
                        break;
                }
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    private void initData() {
        updateUI(queueManager.getCurrentMusic());
    }

    private void registerListener() {
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().unregister(this);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void updateUI(MusicFile file) {
        if (queueManager.getCurrentMusic() != null) {
            currentIndex = queueManager.getIndexOnDefaultQueue(file.musicId);
            Log.d("xuqiang", "---currentIndex = " + currentIndex);
        }
        musicListAdapter.setSelectedPos(currentIndex);
        recyclerView.scrollToPosition(currentIndex);
    }

    /**
     * 当前播放变更时，对ui重新赋值
     * @param file
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void mediaUpdateEvent(MusicFile file) {
        updateUI(file);
    }

}
