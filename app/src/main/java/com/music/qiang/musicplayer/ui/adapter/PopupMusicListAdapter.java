package com.music.qiang.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.adapter.helper.ItemTouchHelperAdapter;
import com.music.qiang.musicplayer.ui.adapter.helper.ItemTouchHelperViewHolder;
import com.music.qiang.musicplayer.ui.adapter.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 更多音乐弹窗
 * <p/>
 * Created by xuqiang on 2017/5/9.
 */
public class PopupMusicListAdapter extends RecyclerView.Adapter<PopupMusicListAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    public ArrayList<MusicFile> musicFiles;
    private MyItemClickListener mOnItemClickListener;
    private OnStartDragListener mDragStartListener;
    private int selectedPos = 0;
    /**
     * 列表模式 0：正常展示模式  1：编辑模式
     */
    private int mode = 0;

    public PopupMusicListAdapter(Context mContext, ArrayList<MusicFile> mData, OnStartDragListener dragStartListener) {
        this.mContext = mContext;
        this.musicFiles = mData;
        this.mDragStartListener = dragStartListener;
    }

    public void setSelectedPos(int pos) {
        notifyItemChanged(selectedPos);
        this.selectedPos = pos;
        notifyItemChanged(selectedPos);
    }

    public interface MyItemClickListener {
        void onItemClickListener(View view, int position);

        void onItemLongClickListener(View view, int position);
    }

    public void setOnItemClickListener(MyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popup_music_queue, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.d("xuqiang", "the " + position + " show.");
        final MusicFile file = musicFiles.get(position);

        // 处理选中的item的颜色以及正在播放的图标
        if (selectedPos == position) {
            holder.musicName.setTextColor(mContext.getResources().getColor(R.color.colorBrown));
            holder.musicArtist.setTextColor(mContext.getResources().getColor(R.color.colorBrown));
            holder.playingIcon.setVisibility(View.VISIBLE);
        } else {
            holder.musicName.setTextColor(mContext.getResources().getColor(R.color.colorBlack2c));
            holder.musicArtist.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.playingIcon.setVisibility(View.GONE);
        }
        holder.musicName.setText(file.musicName);
        if (!StringUtils.isNullOrEmpty(file.musicArtist)) {
            holder.musicArtist.setText(" - " + file.musicArtist);
        } else {
            holder.musicArtist.setText("");
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                file.isChecked = isChecked;
            }
        });
        // 处理正常和编辑模式下不同的布局
        if (mode == 0) {
            holder.selectIcon.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.GONE);
            holder.removeIcon.setVisibility(View.VISIBLE);
        } else if (mode == 1) {
            holder.selectIcon.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.removeIcon.setVisibility(View.GONE);
            if (file.isChecked) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        }
        holder.selectIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == 0) {
                        int position = holder.getAdapterPosition();
                        // 更新老的视图
                        notifyItemChanged(selectedPos);
                        selectedPos = position;
                        // 更新新点击的视图
                        notifyItemChanged(selectedPos);
                        mOnItemClickListener.onItemClickListener(holder.itemView, position);
                    } else if (mode == 1) {
                        if (file.isChecked) {
                            file.isChecked = false;
                        } else {
                            file.isChecked = true;
                        }
                        notifyItemChanged(position);
                    }

                }
            });
            holder.removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClickListener(holder.removeIcon, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public RelativeLayout root;
        public TextView musicName;
        public TextView musicArtist;
        public ImageView removeIcon, playingIcon, selectIcon;
        public CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.rl_item_popup_music_queue);
            musicName = (TextView) itemView.findViewById(R.id.tv_item_popup_music_queue_name);
            musicArtist = (TextView) itemView.findViewById(R.id.tv_item_popup_music_queue_artist);
            removeIcon = (ImageView) itemView.findViewById(R.id.iv_item_popup_music_queue_remove);
            playingIcon = (ImageView) itemView.findViewById(R.id.iv_item_popup_music_queue_playing);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_item_popup_music_queue_select);
            selectIcon = (ImageView) itemView.findViewById(R.id.iv_item_popup_music_queue_select);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.DKGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundResource(R.drawable.ripple_item_selector);
        }
    }

    public void updateList(List<MusicFile> items) {
        if (items != null && items.size() > 0) {
            //musicFiles.clear();
            musicFiles = new ArrayList<>(items.size());
            Iterator<MusicFile> iterator = items.iterator();
            while (iterator.hasNext()) {
                musicFiles.add(iterator.next().clone());
            }
            //musicFiles.addAll(items);
            notifyDataSetChanged();
        }
    }

    /**
     * 切换正式模式还是编辑模式
     *
     * @param m 0：正常模式 1：编辑模式
     */
    public void notifyToEditMode(int m) {
        mode = m;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(musicFiles, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(musicFiles, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onItemDismiss(int postion) {
        musicFiles.remove(postion);
        notifyItemRemoved(postion);
    }
}
