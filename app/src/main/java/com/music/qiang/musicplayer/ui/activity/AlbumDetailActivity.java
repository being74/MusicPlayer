package com.music.qiang.musicplayer.ui.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.support.utils.StringUtils;
import com.music.qiang.musicplayer.ui.adapter.MusicListAdapter;
import com.music.qiang.musicplayer.ui.adapter.MusicListInNumberAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 专辑详情页
 */
public class AlbumDetailActivity extends AppCompatActivity {

    // ****************Views*******************
    private Toolbar toolbar;
    private RecyclerView musicListView;
    private ImageView albumBg;

    // ****************对象********************
    private RecyclerView.LayoutManager mLayoutManager;
    private MusicListInNumberAdapter musicListAdapter;
    private ArrayList<MusicFile> musicFiles;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private Uri uri;
    // ****************基本数据************
    private String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        initViews();
        fetchIntent();
        initData();
    }

    private void initViews() {
        initToolbar();
        musicListView = (RecyclerView) findViewById(R.id.rv_activity_album_detail_list);
        albumBg = (ImageView) findViewById(R.id.iv_activity_album_detail_bg);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchIntent() {
        albumName = getIntent().getStringExtra("albumName");
    }

    private void initData() {
        initRecyclerView();
        setTitle(albumName);
        toolbar.setSubtitle(musicFiles.get(0).musicArtist);
        if (!StringUtils.isNullOrEmpty(musicFiles.get(0).albumpic_big) || !StringUtils.isNullOrEmpty(musicFiles.get(0).albumpic_small)) {
            if (!StringUtils.isNullOrEmpty(musicFiles.get(0).albumpic_big)) {
                Picasso.with(this)
                        .load(musicFiles.get(0).albumpic_big)
                        .resize(StringUtils.dip2px(48), StringUtils.dip2px(48))
                        .error(R.mipmap.ic_black_rubber)
                        .into(albumBg);
            } else {
                Picasso.with(this)
                        .load(musicFiles.get(0).albumpic_small)
                        .resize(StringUtils.dip2px(48), StringUtils.dip2px(48))
                        .error(R.mipmap.ic_black_rubber)
                        .into(albumBg);
            }
        } else {
            uri = ContentUris.withAppendedId(sArtworkUri, musicFiles.get(0).musicAlubmId);
            Picasso.with(this)
                    .load(uri)
                    .error(R.mipmap.ic_black_rubber)
                    .into(albumBg);
        }
    }

    private void initRecyclerView() {
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        musicListView.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        musicListView.setHasFixedSize(true);
        // 3. 创建并设置适配器
        getMusicList();
        musicListAdapter = new MusicListInNumberAdapter(this, musicFiles);
        musicListView.setAdapter(musicListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(musicListView.getContext(),
                LinearLayout.VERTICAL);
        musicListView.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        musicListAdapter.setOnItemClickListener(new MusicListInNumberAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(AlbumDetailActivity.this, MusicPlayActivity.class);
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
        String condition = MediaStore.Audio.Media.ALBUM + "='" + albumName + "'";
        if (albumName.contains("'")) {
            condition = MediaStore.Audio.Media.ALBUM + "=\"" + albumName + "\"";
        }
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, condition, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {

        }
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
