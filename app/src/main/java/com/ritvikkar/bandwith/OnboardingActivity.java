package com.ritvikkar.bandwith;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ritvikkar.bandwith.fragments.GenresFragment;
import com.ritvikkar.bandwith.fragments.LocationsFragment;
import com.ritvikkar.bandwith.fragments.PhoneNumberFragment;
import com.ritvikkar.bandwith.fragments.ProfilePictureFragment;
import com.ritvikkar.bandwith.fragments.TalentsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnboardingActivity extends FragmentActivity {

    public static final int NUM_PAGES = 5;

    @BindView(R.id.btnContinue)
    Button btnContinue;

    NoSwipeViewPager pager;
    MainPagerAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onboarding);

        pager = findViewById(R.id.vpOnboarding);
        progressBar = findViewById(R.id.pbOnloading);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        toggleContinue();
        ButterKnife.bind(this);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] screens;

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
            this.screens = new Fragment[NUM_PAGES];

            ProfilePictureFragment fragment = new ProfilePictureFragment();
            fragment.setRetainInstance(true);

            TalentsFragment talentsFragment = new TalentsFragment();
            fragment.setRetainInstance(true);

            LocationsFragment locationsFragment = new LocationsFragment();
            locationsFragment.setRetainInstance(true);

            GenresFragment genresFragment = new GenresFragment();
            genresFragment.setRetainInstance(true);

            PhoneNumberFragment phoneNumberFragment = new PhoneNumberFragment();
            phoneNumberFragment.setRetainInstance(true);

            this.screens[0] = fragment;
            this.screens[1] = talentsFragment;
            this.screens[2] = locationsFragment;
            this.screens[3] = genresFragment;
            this.screens[4] = phoneNumberFragment;
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

    public void toggleContinue() {
        if (btnContinue.isEnabled()) {
            btnContinue.setEnabled(false);
            btnContinue.setBackgroundResource(R.drawable.rounded_button_light);
        } else {
            btnContinue.setEnabled(true);
            btnContinue.setBackgroundResource(R.drawable.rounded_button);
        }
    }

    @OnClick(R.id.btnContinueOnboarding)
    void continueBoarding() {
        progressBar.setProgress(progressBar.getProgress() + 1);
        pager.setCurrentItem(pager.getCurrentItem() + 1);
        toggleContinue();
    }
}
