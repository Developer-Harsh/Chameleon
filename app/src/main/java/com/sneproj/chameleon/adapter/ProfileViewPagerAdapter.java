package com.sneproj.chameleon.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sneproj.chameleon.fragments.ProfileAboutFragment;
import com.sneproj.chameleon.fragments.ProfileSchedulesFragment;
import com.sneproj.chameleon.fragments.ProfileVideoFragment;
import com.sneproj.chameleon.fragments.PurchasedFragment;
import com.sneproj.chameleon.fragments.ReceivedFragment;
import com.sneproj.chameleon.fragments.SpentFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    public ProfileViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new ProfileAboutFragment();
        }else if (position == 1){
            return new ProfileSchedulesFragment();
        }else{
            return new ProfileVideoFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "AboutME";
        }else if (position == 1){
            return "Schedule";
        }else {
            return "Video";
        }
    }
}
