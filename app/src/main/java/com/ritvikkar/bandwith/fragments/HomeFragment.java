package com.ritvikkar.bandwith.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ritvikkar.bandwith.R;

/**
 * Created by Noah on 1/28/18.
 */

public class HomeFragment extends Fragment {

    static final int NUM_PAGES = 2;
    private View rootView;

    private ViewPager pager;
    private ScreenSlidePagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view;
        pager = rootView.findViewById(R.id.vpHome);
        adapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(NUM_PAGES);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] screens;

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.screens = new Fragment[NUM_PAGES];

            AboutFragment about = new AboutFragment();
            about.setRetainInstance(true);

            TracksFragment tracks = new TracksFragment();
            tracks.setRetainInstance(true);

            this.screens[0] = about;
            this.screens[1] = tracks;
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

}
