package com.ritvikkar.bandwith;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ritvikkar.bandwith.fragments.HomeFragment;
import com.ritvikkar.bandwith.fragments.JobFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {

    public static final int NUM_PAGES = 2;

    private NoSwipeViewPager pager;
    private MainPagerAdapter adapter;

    @BindView(R.id.llLeft)
    LinearLayout llLeft;

    @BindView(R.id.llRight)
    LinearLayout llRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        pager = findViewById(R.id.vpMain);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(NUM_PAGES);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] screens;

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
            this.screens = new Fragment[NUM_PAGES];

            HomeFragment home = new HomeFragment();
            home.setRetainInstance(true);

            JobFragment job = new JobFragment();
            job.setRetainInstance(true);

            this.screens[0] = home;
            this.screens[1] = job;
        }

        @Override
        public Fragment getItem(int position) {
            return screens[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @OnClick(R.id.llLeft)
    public void onHome() {
        if (pager.getCurrentItem() != 0) {
            pager.setCurrentItem(0);
            llLeft.setBackgroundColor(getResources().getColor(R.color.denim));
            llRight.setBackgroundColor(getResources().getColor(R.color.clear_blue));
        }

    }

    @OnClick(R.id.llRight)
    public void onJobs() {
        if (pager.getCurrentItem() != 1) {
            pager.setCurrentItem(1);
            llRight.setBackgroundColor(getResources().getColor(R.color.denim));
            llLeft.setBackgroundColor(getResources().getColor(R.color.clear_blue));
        }
    }
}
