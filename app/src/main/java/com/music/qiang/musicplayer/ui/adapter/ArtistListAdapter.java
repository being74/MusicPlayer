package com.music.qiang.musicplayer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.ArtistFile;

import java.util.List;

/**
 * 艺术家列表
 */
public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {

    private Context mContext;
    private ArtistListAdapter.MyItemClickListener mOnItemClickListener;
    private final List<ArtistFile> mValues;

    public ArtistListAdapter(Context mContext, List<ArtistFile> items) {
        this.mContext = mContext;
        mValues = items;
    }

    public interface MyItemClickListener {
        void onItemClickListener(View view, int position);

        void onItemLongClickListener(View view, int position);
    }

    public void setOnItemClickListener(ArtistListAdapter.MyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).artist);
        holder.mNumber.setText("共" + mValues.get(position).numberOfTracks + "首歌曲");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.itemView, holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mNumber;
        public ArtistFile mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.tv_item_artist_list_name);
            mNumber = (TextView) itemView.findViewById(R.id.tv_item_artist_list_number);
        }
    }
}
