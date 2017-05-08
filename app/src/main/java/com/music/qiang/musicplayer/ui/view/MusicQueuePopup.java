package com.music.qiang.musicplayer.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.playback.QueueManager;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.adapter.MusicListAdapter;

import java.util.ArrayList;

/**
 * Created by xuqiang on 2017/5/8.
 */
public class MusicQueuePopup extends PopupWindow implements View.OnClickListener {

    // **************类和对象*****************
    private Context mContext;
    private LayoutInflater inflater;
    private RecyclerView.LayoutManager mLayoutManager;
    private MusicListAdapter musicListAdapter;

    // **************views*****************
    private View convertView;
    private RecyclerView recyclerView;

    // **************基本数据*****************
    private ArrayList<MusicFile> musicFiles;

    public MusicQueuePopup(Context context) {
        super(context);
        this.mContext = context;
        //this.musicFiles = musicFiles;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.popup_music_queue, null);

        initPopupWindow();
        initData();
        initView();
        registerListener();
    }

    /**
     * 初始化popupwindow属性
     */
    private void initPopupWindow() {
        this.setContentView(convertView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(StringUtils.dip2px(360));
        //this.setHeight(ScreenUtils.getScreenPix(mContext).heightPixels - StringUtils.dip2px(36));
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
        musicListAdapter = new MusicListAdapter(mContext, musicFiles);
        recyclerView.setAdapter(musicListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        musicListAdapter.setOnItemClickListener(new MusicListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                /*Intent intent = new Intent(mContext, MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "list");
                bundle.putInt("playIndex", position);
                bundle.putSerializable("playList", musicFiles);
                intent.putExtras(bundle);
                startActivity(intent);*/
                dismiss();
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    private void initData() {
        musicFiles = (ArrayList<MusicFile>) QueueManager.getInstance().getDefaultQueue();
    }

    private void registerListener() {

    }

    @Override
    public void onClick(View v) {

    }

}
