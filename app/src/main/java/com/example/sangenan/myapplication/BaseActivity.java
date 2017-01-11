package com.example.sangenan.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by kuwakuzukusunoki on 2016/12/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract int getID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getID());
        Log.d("tag","Oncreate_after_Base");

    }
}
