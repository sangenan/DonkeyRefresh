package com.example.sangenan.myapplication;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by kuwakuzukusunoki on 2017/1/1.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }
}
