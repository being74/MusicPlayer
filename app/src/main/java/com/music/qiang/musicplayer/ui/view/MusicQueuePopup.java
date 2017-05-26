package com.music.qiang.musicplayer.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.events.PlayModeEvent;
import com.music.qiang.musicplayer.events.PlaytypeChangeManager;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.LocalPlayback;
import com.music.qiang.musicplayer.playback.QueueManager;
import com.music.qiang.musicplayer.service.PlayBackService;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.adapter.PopupMusicListAdapter;
import com.music.qiang.musicplayer.ui.adapter.helper.OnStartDragListener;
import com.music.qiang.musicplayer.ui.adapter.helper.SimpleItemTouchHelperCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表弹出框
 * <p/>
 * Created by xuqiang on 2017/5/8.
 */
public class MusicQueuePopup extends PopupWindow implements View.OnClickListener, OnStartDragListener {

    // **************类和对象*****************
    private Context mContext;
    private LayoutInflater inflater;
    private RecyclerView.LayoutManager mLayoutManager;
    private PopupMusicListAdapter musicListAdapter;
    private QueueManager queueManager;
    private LocalPlayback localPlayback;
    private SharedPreferences sharedPreferences;
    private ItemTouchHelper mItemTouchHelper;

    // **************views*****************
    private View convertView;
    private RecyclerView recyclerView;
    private LinearLayout playTypeLayout;
    private TextView playType, playEdit, removeButton;
    private ImageView playTypeIcon;

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
        this.setHeight(StringUtils.dip2px(430));
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
        playTypeLayout = (LinearLayout) convertView.findViewById(R.id.ll_popup_music_queue_play_type);
        playType = (TextView) convertView.findViewById(R.id.tv_popup_music_queue_play_type);
        playEdit = (TextView) convertView.findViewById(R.id.tv_popup_music_queue_edit);
        removeButton = (TextView) convertView.findViewById(R.id.tv_popup_music_queue_remove);
        playTypeIcon = (ImageView) convertView.findViewById(R.id.iv_popup_music_queue_play_type);
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        musicListAdapter = new PopupMusicListAdapter(mContext, musicFiles, this);
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

            @Override
            public void onListSwapListener(List<MusicFile> list) {
                /**
                 * 1.重新给播放列表赋值
                 */
                musicFiles = (ArrayList<MusicFile>) list;
                /*queueManager.swapQueue(musicFiles);
                updateUI(queueManager.getCurrentMusic());*/
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(musicListAdapter);
        /*ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.d("xuqiang", "---------onMove---------");
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("xuqiang", "---------onSwiped---------");
            }
        };*/
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        sharedPreferences = mContext.getSharedPreferences("playback", Context.MODE_PRIVATE);
        currentMode = sharedPreferences.getInt("playMode", 0);
        switch (currentMode) {
            case 0:
                playTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_dark);
                playType.setText("列表循环");
                break;
            case 1:
                playTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_one_dark);
                playType.setText("单曲循环");
                break;
            case 2:
                playTypeIcon.setImageResource(R.mipmap.ic_music_play_random_dark);
                playType.setText("随机播放");
                break;
        }
        updateUI(queueManager.getCurrentMusic());
    }

    private void registerListener() {
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().unregister(this);
            }
        });
        playTypeLayout.setOnClickListener(this);
        playEdit.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击切换播放方式
            case R.id.ll_popup_music_queue_play_type:
                if (currentMode == 0) {
                    currentMode = 1;
                    playTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_one_dark);
                    playType.setText("单曲循环");
                } else if (currentMode == 1) {
                    currentMode = 2;
                    playTypeIcon.setImageResource(R.mipmap.ic_music_play_random_dark);
                    playType.setText("随机播放");
                } else if (currentMode == 2) {
                    currentMode = 0;
                    playTypeIcon.setImageResource(R.mipmap.ic_music_play_repeat_dark);
                    playType.setText("列表循环");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("playMode", currentMode);
                editor.apply();
                EventBus.getDefault().post(new PlayModeEvent(currentMode));
                /*
                 * 切换播放方式，向订阅的观察者发送变更通知
                 */
                PlaytypeChangeManager.getInstance().notifyChange(currentMode);
                break;
            // 编辑播放列表
            case R.id.tv_popup_music_queue_edit:
                if ("编辑".equals(playEdit.getText().toString())) {
                    playEdit.setText("完成");
                    removeButton.setVisibility(View.VISIBLE);
                    musicListAdapter.notifyToEditMode(1);
                } else {
                    playEdit.setText("编辑");
                    removeButton.setVisibility(View.GONE);
                    musicListAdapter.notifyToEditMode(0);
                    /**
                     * 1.给queueManager中播放列表赋值
                     * 2.更新列表选中位置
                     */
                    queueManager.swapQueue(musicFiles);
                    updateUI(queueManager.getCurrentMusic());
                }
                break;
            // 移除选中项
            case R.id.tv_popup_music_queue_remove:
                if (musicListAdapter.musicFiles != null) {
                    boolean foo = false;
                    for (int i = 0; i < musicListAdapter.musicFiles.size(); i++) {
                        if (musicListAdapter.musicFiles.get(i).isChecked) {
                            queueManager.removeFromQueue(musicListAdapter.musicFiles.get(i));
                            foo = true;
                        }
                    }
                    if (foo) {
                        updateUI(queueManager.getCurrentMusic());
                        musicListAdapter.updateList(queueManager.getDefaultQueue());
                    }
                }
                break;
        }
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
     *
     * @param file
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void mediaUpdateEvent(MusicFile file) {
        updateUI(file);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        Log.d("xuqiang", "----------onStartDrag---------");
        mItemTouchHelper.startDrag(viewHolder);
    }
}
