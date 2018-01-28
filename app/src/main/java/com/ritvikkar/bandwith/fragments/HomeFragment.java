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
import android.widget.TextView;

import com.ritvikkar.bandwith.R;

/**
 * Created by Noah on 1/28/18.
 */

public class HomeFragment extends Fragment {

    static final int NUM_PAGES = 2;
    private View rootView;

    private ViewPager pager;
    private ScreenSlidePagerAdapter adapter;
    private final static int ABOUT = 0;
    private final static int TRACKS = 1;

    private View underlineLeft;
    private View underlineRight;
    private TextView tvAbout;
    private TextView tvTracks;

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
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchPages(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        underlineLeft = rootView.findViewById(R.id.underlineLeft);
        underlineRight = rootView.findViewById(R.id.underlineRight);
        tvAbout = rootView.findViewById(R.id.tvAbout);
        tvTracks = rootView.findViewById(R.id.tvTracks);
        switchPages(ABOUT);

        rootView.findViewById(R.id.rlLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() != ABOUT) {
                    pager.setCurrentItem(ABOUT);
                }
            }
        });
        rootView.findViewById(R.id.rlRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() != TRACKS) {
                    pager.setCurrentItem(TRACKS);
                }
            }
        });
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

    private void switchPages (int position) {
        switch (position) {
            case ABOUT:
                underlineLeft.setVisibility(View.VISIBLE);
                underlineRight.setVisibility(View.INVISIBLE);
                tvAbout.setTextColor(getResources().getColor(R.color.white));
                tvTracks.setTextColor(getResources().getColor(R.color.powder_blue));
                break;
            case TRACKS:
                underlineLeft.setVisibility(View.INVISIBLE);
                underlineRight.setVisibility(View.VISIBLE);
                tvAbout.setTextColor(getResources().getColor(R.color.powder_blue));
                tvTracks.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

}
