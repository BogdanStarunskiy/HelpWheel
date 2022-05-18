package com.example.helpwheel.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpwheel.ui.dashboard.viewPagerFragments.DescriptionWeather;
import com.example.helpwheel.ui.dashboard.viewPagerFragments.WeatherWindSpeed;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DescriptionWeather();
            case 1:
                return new WeatherWindSpeed();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
