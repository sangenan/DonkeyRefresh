package com.example.sangenan.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by kuwakuzukusunoki on 2017/1/11.
 */

public class drawPlanet extends View {

    private int locationX;
    private int locationY;
    private Bitmap flyingPlanet;
    private float upDateY;

    public drawPlanet(Context context) {
        super(context);
        initView();
    }

    public drawPlanet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        flyingPlanet = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_img_planet);
        upDateY = 0;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        locationX = w / 2;
        locationY = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrixPlanet = new Matrix();
        matrixPlanet.setScale(0.4f, 0.4f);
        matrixPlanet.postTranslate(locationX / 2 * 3, locationY /4);
        matrixPlanet.postTranslate(0, upDateY);
        canvas.drawBitmap(flyingPlanet,matrixPlanet,null);

    }
    public void startTranslatePlanet(int duration){
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(-50.0f, 50.0f);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                upDateY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
