package com.music.qiang.musicplayer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.ArtistFile;
import com.music.qiang.musicplayer.ui.activity.ArtistDetailActivity;
import com.music.qiang.musicplayer.ui.adapter.ArtistListAdapter;

import java.util.ArrayList;

/**
 * 艺术家列表
 */
public class ArtistListFragment extends Fragment {

    // ****************Views*******************
    private View rootView;
    private RecyclerView artistlistView;

    // ****************对象********************
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArtistListAdapter artistListAdapter;
    private ArrayList<ArtistFile> artistFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_artist_list, container, false);
        mContext = getContext();
        initViews();
        initData();
        //recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        return rootView;
    }

    private void initViews() {
        artistlistView = (RecyclerView) rootView.findViewById(R.id.rv_fragment_artist_list);
    }

    private void initData() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(mContext);
        artistlistView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        artistlistView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        getAlbumList();
        artistListAdapter = new ArtistListAdapter(mContext, artistFiles);
        artistlistView.setAdapter(artistListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(artistlistView.getContext(),
                LinearLayout.VERTICAL);
        artistlistView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        artistListAdapter.setOnItemClickListener(new ArtistListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(mContext, ArtistDetailActivity.class);
                intent.putExtra("artistName", artistFiles.get(position).artist);
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    /**
     * 获得媒体文件
     */
    private void getAlbumList() {
        artistFiles = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            ArtistFile file = new ArtistFile();
            file.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
            file.artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
            file.numberOfAlbums = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
            file.numberOfTracks = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));

            artistFiles.add(file);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
