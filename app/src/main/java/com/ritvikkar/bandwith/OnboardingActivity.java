package com.ritvikkar.bandwith;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnboardingActivity extends AppCompatActivity {

    public static final int NUM_PAGES = 2;

    ViewPager pager;
    MainPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onboarding);

        pager = findViewById(R.id.vpOnboarding);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        ButterKnife.bind(this);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] screens;

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
            this.screens = new ProfilePictureFragment[NUM_PAGES];

            ProfilePictureFragment fragment = new ProfilePictureFragment();
            fragment.setRetainInstance(true);

            

            this.screens[0] = fragment;
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

    @OnClick(R.id.btnContinueOnboarding)
    void continueBoarding() {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }
}
