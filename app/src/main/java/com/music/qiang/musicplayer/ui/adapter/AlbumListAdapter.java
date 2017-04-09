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
import com.music.qiang.musicplayer.model.AlbumFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 专辑列表适配器
 * 
 * Created by xuqiang on 2017/04/09.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<AlbumFile> albumFiles;
    private AlbumListAdapter.MyItemClickListener mOnItemClickListener;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private Uri uri;

    public AlbumListAdapter(Context mContext, ArrayList<AlbumFile> mData) {
        this.mContext = mContext;
        this.albumFiles = mData;
    }

    public interface MyItemClickListener {
        void onItemClickListener(View view, int position);

        void onItemLongClickListener(View view, int position);
    }

    public void setOnItemClickListener(AlbumListAdapter.MyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public AlbumListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_list, null);
        return new AlbumListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumListAdapter.MyViewHolder holder, int position) {
        holder.albumName.setText(albumFiles.get(position).albumName);
        holder.albumArtist.setText(albumFiles.get(position).albumArtist);

        uri = ContentUris.withAppendedId(sArtworkUri, albumFiles.get(position).albumId);
        Picasso.with(mContext)
                .load(uri)
                .resize(StringUtils.dip2px(48), StringUtils.dip2px(48))
                .error(R.mipmap.ic_black_rubber)
                .into(holder.albumIcon);

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
        return albumFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout root;
        public TextView albumName;
        public TextView albumArtist;
        public ImageView albumIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.rl_item_album_list_root);
            albumName = (TextView) itemView.findViewById(R.id.tv_item_album_list_name);
            albumArtist = (TextView) itemView.findViewById(R.id.tv_item_album_list_artist);
            albumIcon = (ImageView) itemView.findViewById(R.id.iv_item_album_list_thumbnail);
        }
    }

}
