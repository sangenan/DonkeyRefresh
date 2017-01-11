package com.example.sangenan.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by kuwakuzukusunoki on 2017/1/8.
 */

public class drawBall extends View {

    private Bitmap flyingBall;
    private float flyingBall_Height;
    private float flyingBall_Width;
    private int locationX;
    private int locationY;
    private Bitmap cloudBig;
    private Bitmap cloudSmall;
    private float degreeBall;
    private float upDateY;
    private RectF rectfCloudBig;
    private RectF rectfCloudSmall;
    private float sizeCloudBig;
    private float sizeCloudSmall;

    public drawBall(Context context) {
        super(context);
        initView();
    }

    public drawBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        flyingBall = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_ball);
        cloudBig = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_cloud_big);
        cloudSmall = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_cloud_small);

        flyingBall_Height = flyingBall.getHeight() * 0.2f;
        flyingBall_Width = flyingBall.getWidth() * 0.2f;
        sizeCloudBig = (float) cloudBig.getWidth() / cloudBig.getHeight();
        sizeCloudSmall = (float) cloudSmall.getWidth() / cloudSmall.getHeight();

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        locationX = w / 2;
        locationY = h;

        rectfCloudBig = new RectF(locationX +flyingBall_Width /4,locationY -flyingBall_Width /4 - 60
                ,locationX + flyingBall_Width /4 + 60 * sizeCloudBig , locationY -flyingBall_Width / 4);

        rectfCloudSmall = new RectF(locationX - flyingBall_Width / 4 - 50 * sizeCloudSmall , locationY -flyingBall_Width / 4 - 50 ,
                locationX - flyingBall_Width / 4,locationY - flyingBall_Width / 4);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrixBall = new Matrix();
        matrixBall.setScale(0.2f, 0.2f);
        if ((locationY  + upDateY) > (locationY - flyingBall_Height / 2)) {
            matrixBall.postTranslate(locationX - flyingBall_Width / 2, locationY  + upDateY);
            matrixBall.postRotate(degreeBall, locationX, (locationY +upDateY + flyingBall_Height /2)  );
            Log.d("tag","upDateY"+upDateY);
        }
        else {
            matrixBall.postTranslate(locationX - flyingBall_Width / 2, locationY - flyingBall_Height / 2);
            matrixBall.postRotate(degreeBall, locationX, locationY);

        }

        canvas.drawBitmap(flyingBall, matrixBall, null);
        canvas.drawBitmap(cloudBig , null , rectfCloudBig , null);
        canvas.drawBitmap(cloudSmall , null , rectfCloudSmall ,null);

    }

    public void startBallAnim(long duration) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0.0f, 360.0f);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degreeBall = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
    public void UpBall(float offsetY){
        if (upDateY!=offsetY) {
            upDateY = offsetY;
            invalidate();
        }
    }

    public void accelerateBall(long duration) {
        clearAnimation();
        startBallAnim(duration);
    }
}
