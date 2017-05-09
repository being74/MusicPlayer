package com.music.qiang.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 更多音乐弹窗
 * <p/>
 * Created by xuqiang on 2017/5/9.
 */
public class PopupMusicListAdapter extends RecyclerView.Adapter<PopupMusicListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MusicFile> musicFiles;
    private MyItemClickListener mOnItemClickListener;
    private int selectedPos = 0;

    public PopupMusicListAdapter(Context mContext, ArrayList<MusicFile> mData) {
        this.mContext = mContext;
        this.musicFiles = mData;
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
        if (selectedPos == position) {
            holder.musicName.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
            holder.musicArtist.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
        } else {
            holder.musicName.setTextColor(mContext.getResources().getColor(R.color.colorBlack2c));
            holder.musicArtist.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        holder.musicName.setText(musicFiles.get(position).musicName);
        if (!StringUtils.isNullOrEmpty(musicFiles.get(position).musicArtist)) {
            holder.musicArtist.setText(" - " + musicFiles.get(position).musicArtist);
        } else {
            holder.musicArtist.setText("");
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    // 更新老的视图
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    // 更新新点击的视图
                    notifyItemChanged(selectedPos);
                    mOnItemClickListener.onItemClickListener(holder.itemView, position);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout root;
        public TextView musicName;
        public TextView musicArtist;
        public ImageView removeIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.rl_item_popup_music_queue);
            musicName = (TextView) itemView.findViewById(R.id.tv_item_popup_music_queue_name);
            musicArtist = (TextView) itemView.findViewById(R.id.tv_item_popup_music_queue_artist);
            removeIcon = (ImageView) itemView.findViewById(R.id.iv_item_popup_music_queue_remove);
        }
    }

    public void updateList(List<MusicFile> items) {
        if (items != null && items.size() > 0) {
            musicFiles.clear();
            musicFiles.addAll(items);
            notifyDataSetChanged();
        }
    }
}
