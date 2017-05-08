package com.music.qiang.musicplayer.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.music.qiang.musicplayer.R;
import com.music.qiang.musicplayer.model.MusicFile;
import com.music.qiang.musicplayer.model.MusicOnlineBean;
import com.music.qiang.musicplayer.support.utils.HttpUtil;
import com.music.qiang.musicplayer.ui.activity.MusicPlayActivity;
import com.music.qiang.musicplayer.ui.adapter.MusicListAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 *
 */
public class MoreFragment extends Fragment {

    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private View rootView;
    private MusicListAdapter musicListAdapter;
    private RecyclerView rv_music_list;
    private String response = "";
    private ArrayList<MusicFile> musicFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        mContext = getContext();
        rv_music_list = (RecyclerView) rootView.findViewById(R.id.rv_fragment_more);
        initRecyclerView();
        initData();
        return rootView;
    }

    private void initRecyclerView() {
        // 1. 创建线性LayoutManager
        mLayoutManager = new LinearLayoutManager(mContext);
        rv_music_list.setLayoutManager(mLayoutManager);
        // 2. 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rv_music_list.setHasFixedSize(true);
        // 3. 创建并设置适配器
        //getMusicList();
        //musicListAdapter = new MusicListAdapter(mContext, musicFiles);
        //rv_music_list.setAdapter(musicListAdapter);
        // 4. 添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_music_list.getContext(),
                LinearLayout.VERTICAL);
        rv_music_list.addItemDecoration(dividerItemDecoration);

        // 5. 设置点击事件
        /*musicListAdapter.setOnItemClickListener(new MusicListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
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
        });*/
    }

    private void initData() {
        final String url = "http://route.showapi.com/213-4?showapi_appid=35608&topid=6&showapi_sign=dd559b3a18ee485fa098924d3df792ab";

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                response = new String(responseBody);
                Gson gson = new GsonBuilder().create();
                MusicOnlineBean bean = gson.fromJson(response, MusicOnlineBean.class);
                List<MusicOnlineBean.ShowapiResBodyBean.PagebeanBean.SonglistBeanX> list = bean.getShowapi_res_body().getPagebean().getSonglist();
                initAdapter(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };
        HttpUtil.doGet(url, asyncHttpResponseHandler);

    }

    private void initAdapter(List<MusicOnlineBean.ShowapiResBodyBean.PagebeanBean.SonglistBeanX> list) {
        musicFiles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MusicFile file = new MusicFile();
            file.musicName = list.get(i).getSongname();
            file.musicId = list.get(i).getSongid() + "";
            file.musicAlbum = list.get(i).getAlbummid();
            file.musicArtist = list.get(i).getSingername();
            file.musicPath = list.get(i).getUrl();
            file.musicSize = "";
            file.musicTime = list.get(i).getSeconds() * 1000 + "";
            file.musicAlubmId = list.get(i).getAlbumid();
            file.albumpic_big = list.get(i).getAlbumpic_big();
            file.albumpic_small = list.get(i).getAlbumpic_small();
            file.playType = "online";   // 区别于本地音乐播放
            musicFiles.add(file);
        }

        musicListAdapter = new MusicListAdapter(mContext, musicFiles);
        rv_music_list.setAdapter(musicListAdapter);

        musicListAdapter.setOnItemClickListener(new MusicListAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
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
}
