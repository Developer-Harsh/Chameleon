package com.sneproj.chameleon;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

    private Activity mActivity;
    private DrawerLayout mDrawerLayout;
    private int mOpenDrawerContentDescRes;
    private int mCloseDrawerContentDescRes;
    private int mDrawerGravity;
    public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                       int openDrawerContentDescRes, int closeDrawerContentDescRes, int drawerGravity) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mOpenDrawerContentDescRes = openDrawerContentDescRes;
        mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        mDrawerGravity = drawerGravity;
    }
    public void onDrawerClosed(View view) {
        mActivity.invalidateOptionsMenu();
    }

    public void onDrawerOpened(View drawerView) {
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (mDrawerGravity == GravityCompat.END) {
            mActivity.getWindow().getDecorView().setTranslationX(-slideOffset * drawerView.getWidth());
        } else {
            mActivity.getWindow().getDecorView().setTranslationX(slideOffset * drawerView.getWidth());
        }
        mDrawerLayout.bringChildToFront(drawerView);
        mDrawerLayout.requestLayout();
    }

    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        mActivity.invalidateOptionsMenu();
    }
//    public void setDrawerIndicatorEnabled(boolean enable) {
//        if (mDrawerGravity == GravityCompat.END) {
//            setHomeAsUpIndicator(enable ? R.drawable.ic_arrow_back : 0);
//        } else {
//            setDrawerIndicatorEnabled(enable);
//        }
//    }
}