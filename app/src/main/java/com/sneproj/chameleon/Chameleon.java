package com.sneproj.chameleon;

import android.app.Application;

import com.sneva.easyprefs.EasyPrefs;

public class Chameleon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyPrefs.initialize(this);
    }
}
