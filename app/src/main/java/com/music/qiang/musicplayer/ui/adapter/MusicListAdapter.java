package com.music.qiang.musicplayer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;

import java.util.ArrayList;

/**
 * Created by qiang on 2016/08/06.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {

    private ArrayList<MusicFile> musicFiles;
    private MyItemClickListener mOnItemClickListener;

    public MusicListAdapter(ArrayList<MusicFile> mData) {
        this.musicFiles = mData;
    }

    public interface MyItemClickListener{
        void onItemClickListener(View view, int position);
        void onItemLongClickListener(View view, int position);
    }

    public void setOnItemClickListener(MyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.musicName.setText(musicFiles.get(position).musicName);
        holder.musicInfo.setText(musicFiles.get(position).musicArtist + " - " + musicFiles.get(position).musicAlbum);
        holder.musicIcon.setImageBitmap(musicFiles.get(position).thumbnail);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClickListener(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView musicName;
        public TextView musicInfo;
        public RelativeLayout root;
        public ImageView musicIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            musicName = (TextView) itemView.findViewById(R.id.tv_item_music_list_name);
            musicInfo = (TextView) itemView.findViewById(R.id.tv_item_music_list_info);
            root = (RelativeLayout) itemView.findViewById(R.id.rl_item_music_list_root);
            musicIcon = (ImageView) itemView.findViewById(R.id.iv_item_music_list_thumbnail);
        }
    }

}
