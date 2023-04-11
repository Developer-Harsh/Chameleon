package com.sneproj.chameleon.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sneproj.chameleon.fragments.PurchasedFragment;
import com.sneproj.chameleon.fragments.ReceivedFragment;
import com.sneproj.chameleon.fragments.SpentFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new ReceivedFragment();
        }else if (position == 1){
            return new PurchasedFragment();
        }else{
            return new SpentFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Receive";
        }else if (position == 1){
            return "Purchased";
        }else {
            return "Spent";
        }
    }
}
