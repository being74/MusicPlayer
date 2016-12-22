package com.music.qiang.musicplayer.ui.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 音乐列表适配器
 * <p>
 * Created by xuqiang on 2016/08/06.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFile> musicFiles;
    private MyItemClickListener mOnItemClickListener;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private Uri uri;

    public MusicListAdapter(Context mContext, ArrayList<MusicFile> mData) {
        this.mContext = mContext;
        this.musicFiles = mData;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.musicName.setText(musicFiles.get(position).musicName);
        holder.musicInfo.setText(musicFiles.get(position).musicArtist + " - " + musicFiles.get(position).musicAlbum);

        uri = ContentUris.withAppendedId(sArtworkUri, musicFiles.get(position).musicAlubmId);
        Picasso.with(mContext)
                .load(uri)
                .resize(StringUtils.dip2px(48), StringUtils.dip2px(48))
                .error(R.mipmap.ic_black_rubber)
                .into(holder.musicIcon);

        Log.d("xuqiang", "------------- load view -----------" + position);

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
        public RelativeLayout root;
        public TextView musicName;
        public TextView musicInfo;
        public ImageView musicIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.rl_item_music_list_root);
            musicName = (TextView) itemView.findViewById(R.id.tv_item_music_list_name);
            musicInfo = (TextView) itemView.findViewById(R.id.tv_item_music_list_info);
            musicIcon = (ImageView) itemView.findViewById(R.id.iv_item_music_list_thumbnail);
        }
    }

}
