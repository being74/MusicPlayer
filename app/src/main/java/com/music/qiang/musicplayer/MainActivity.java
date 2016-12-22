package com.music.qiang.musicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.music.qiang.musicplayer.ui.fragment.AlbumListFragment;
import com.music.qiang.musicplayer.ui.fragment.ItemFragment;
import com.music.qiang.musicplayer.ui.fragment.MoreFragment;
import com.music.qiang.musicplayer.ui.fragment.MusicListFragment;
import com.music.qiang.musicplayer.ui.fragment.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    // ****************Views*******************
    private Toolbar toolbar;
    private ViewPager vp_activity_main;
    private BottomNavigationView nav_activity_main;
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
        nav_activity_main = (BottomNavigationView) findViewById(R.id.nav_activity_main);
    }

    private void ininData() {
        vp_activity_main.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
    }

    private void registerListener() {

        nav_activity_main.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_bottom_nav_music:
                        vp_activity_main.setCurrentItem(0);
                        break;
                    case R.id.main_bottom_nav_album:
                        vp_activity_main.setCurrentItem(1);
                        break;
                    case R.id.main_bottom_nav_artist:
                        vp_activity_main.setCurrentItem(2);
                        break;
                    case R.id.main_bottom_nav_more:
                        vp_activity_main.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        vp_activity_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nav_activity_main.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class HomePagerAdapter extends FragmentStatePagerAdapter {

        public HomePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        private final String[] TITLES = {"歌曲", "专辑", "艺术家", "更多"};

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
                case 3:
                    fragment = new MoreFragment();
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
