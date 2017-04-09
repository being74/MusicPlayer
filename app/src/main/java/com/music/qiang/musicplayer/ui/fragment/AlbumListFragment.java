package com.music.qiang.musicplayer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.AlbumFile;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;
import com.music.qiang.musicplayer.ui.adapter.AlbumListAdapter;
import com.music.qiang.musicplayer.ui.adapter.MusicListAdapter;

import java.util.ArrayList;

/**
 * 专辑列表fragment
 */
public class AlbumListFragment extends Fragment {

    // ****************Views*******************
    private View rootView;
    private RecyclerView albumlistView;

    // ****************对象********************
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlbumListAdapter albumListAdapter;
    private ArrayList<AlbumFile> albumFiles;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        mContext = getActivity();
        initViews();
        initData();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initViews() {
        albumlistView = (RecyclerView) rootView.findViewById(R.id.rv_fragment_album_list);
    }

    private void initData() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(mContext);
        albumlistView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        albumlistView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        getAlbumList();
        albumListAdapter = new AlbumListAdapter(mContext, albumFiles);
        albumlistView.setAdapter(albumListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(albumlistView.getContext(),
                LinearLayout.VERTICAL);
        albumlistView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        albumListAdapter.setOnItemClickListener(new AlbumListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                /*Intent intent = new Intent(mContext, MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "list");
                bundle.putInt("playIndex", position);
                bundle.putSerializable("playList", albumFiles);
                intent.putExtras(bundle);
                startActivity(intent);*/
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
        albumFiles = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            AlbumFile file = new AlbumFile();
            file.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
            file.albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
            file.albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
            file.albumSongNumber = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
            file.albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY));

            albumFiles.add(file);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
