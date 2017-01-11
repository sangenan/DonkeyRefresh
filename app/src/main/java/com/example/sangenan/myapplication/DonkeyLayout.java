package com.example.sangenan.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by kuwakuzukusunoki on 2017/1/8.
 */

public class DonkeyLayout extends FrameLayout implements PtrUIHandler {
    private drawDog donkey;
    private drawBall ball;
    private int mState;
    private static final int startRotate =270;
    private static final int endRotate = 360;


    public static final int STATE_RESET = -1;
    public static final int STATE_PREPARE = 0;
    public static final int STATE_BEGIN = 1;
    public static final int STATE_FINISH = 2;

    public DonkeyLayout(Context context) {
        super(context);
        initView();

    }

    public DonkeyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.donkey_content, this, false);
        donkey = (drawDog) view.findViewById(R.id.donkey);
        ball = (drawBall) view.findViewById(R.id.ball);
        ball.startBallAnim(1000);
        addView(view);

    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mState = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mState = STATE_PREPARE;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mState = STATE_BEGIN;
        ball.accelerateBall(500);
        donkey.launchDonkey();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mState = STATE_FINISH;
        ball.clearAnimation();

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        Log.d("tag","ofsetY"+ptrIndicator.getCurrentPosY());
        switch (mState) {
            case STATE_PREPARE:
                if (ptrIndicator.getCurrentPercent() <= 1) {
                    int margeRotate = (int) ((endRotate -startRotate) * ptrIndicator.getCurrentPercent() +startRotate);
                  //  donkey.setDonkeyRotate(margeRotate);
                }

                break;
            case STATE_BEGIN:

                break;
            case STATE_FINISH:

                break;
        }
    }
}
