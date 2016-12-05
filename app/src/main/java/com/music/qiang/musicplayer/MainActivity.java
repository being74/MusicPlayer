package com.music.qiang.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.music.qiang.musicplayer.ui.activity.SettingsActivity;
import com.music.qiang.musicplayer.ui.fragment.AlbumListFragment;
import com.music.qiang.musicplayer.ui.fragment.ItemFragment;
import com.music.qiang.musicplayer.ui.fragment.MusicListFragment;
import com.music.qiang.musicplayer.ui.fragment.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    // ****************Views*******************
    private Toolbar toolbar;
    private ViewPager vp_activity_main;
    private RadioGroup rg_activity_main_tabs;
    private RadioButton rb_activity_main_tabs_music, rb_activity_main_tabs_album, rb_activity_main_tabs_artist, rb_activity_main_tabs_more;
    private RadioButton[] radios;
    // ****************对象********************

    // ****************数据********************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        ininData();
        registerListener();

    }

    private void initViews() {
        vp_activity_main = (ViewPager) findViewById(R.id.vp_activity_main);
        rg_activity_main_tabs = (RadioGroup) findViewById(R.id.rg_activity_main_tabs);
        rb_activity_main_tabs_music = (RadioButton) findViewById(R.id.rb_activity_main_tabs_music);
        rb_activity_main_tabs_album = (RadioButton) findViewById(R.id.rb_activity_main_tabs_album);
        rb_activity_main_tabs_artist = (RadioButton) findViewById(R.id.rb_activity_main_tabs_artist);
    }

    private void ininData() {
        vp_activity_main.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        radios = new RadioButton[3];
        radios[0] = rb_activity_main_tabs_music;
        radios[1] = rb_activity_main_tabs_album;
        radios[2] = rb_activity_main_tabs_artist;
    }

    private void registerListener() {
        rg_activity_main_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_activity_main_tabs_music:
                        vp_activity_main.setCurrentItem(0);
                        break;
                    case R.id.rb_activity_main_tabs_album:
                        vp_activity_main.setCurrentItem(1);
                        break;
                    case R.id.rb_activity_main_tabs_artist:
                        vp_activity_main.setCurrentItem(2);
                        break;
                    case R.id.rb_activity_main_tabs_more:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                }
            }
        });

        vp_activity_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radios[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public class HomePagerAdapter extends FragmentStatePagerAdapter {

        public HomePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        private final String[] TITLES = {"歌曲", "专辑", "艺术家"};

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new MusicListFragment();
                    break;
                case 1:
                    fragment = new AlbumListFragment();
                    break;
                case 2:
                    fragment = new ItemFragment();
                    break;
                default:
                    fragment = new ItemFragment();
                    break;
            }
            return fragment;
        }

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }
}
