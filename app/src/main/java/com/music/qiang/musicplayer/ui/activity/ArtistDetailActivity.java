package com.music.qiang.musicplayer.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.ui.adapter.MusicListAdapter;

import java.util.ArrayList;

/**
 * 歌手详情页
 */
public class ArtistDetailActivity extends AppCompatActivity {

    // ****************Views*******************
    private Toolbar toolbar;
    private RecyclerView musicListView;

    // ****************对象********************
    private RecyclerView.LayoutManager mLayoutManager;
    private MusicListAdapter musicListAdapter;
    private ArrayList<MusicFile> musicFiles;
    // ****************基本数据************
    private String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        initViews();
        fetchIntent();
        initData();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle(playList.get(playIndex).musicName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        musicListView = (RecyclerView) findViewById(R.id.rv_activity_album_detail_list);
    }

    private void fetchIntent() {
        artistName = getIntent().getStringExtra("artistName");
    }

    private void initData() {
        initRecyclerView();
        setTitle(artistName);
    }

    private void initRecyclerView() {
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        musicListView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        musicListView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        getMusicList();
        musicListAdapter = new MusicListAdapter(this, musicFiles);
        musicListView.setAdapter(musicListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(musicListView.getContext(),
                LinearLayout.VERTICAL);
        musicListView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        musicListAdapter.setOnItemClickListener(new MusicListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(ArtistDetailActivity.this, MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "list");
                bundle.putInt("playIndex", position);
                bundle.putSerializable("playList", musicFiles);
                intent.putExtras(bundle);
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
    private void getMusicList() {
        musicFiles = new ArrayList<>();
        String condition = MediaStore.Audio.Media.ARTIST + "='" + artistName + "'";
        if (artistName.contains("'")) {
            condition = MediaStore.Audio.Media.ARTIST + "=\"" + artistName + "\"";
        }
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, condition , null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            MusicFile file = new MusicFile();
            file.musicId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            file.musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            file.musicAlbum = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            file.musicArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            file.musicPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            file.musicSize = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            file.musicTime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            file.musicAlubmId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            musicFiles.add(file);
            cursor.moveToNext();
        }
        cursor.close();

    }
}
