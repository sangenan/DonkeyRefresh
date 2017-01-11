package com.example.sangenan.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by kuwakuzukusunoki on 2017/1/3.
 */

public class drawDog extends View{
    private int locationX;
    private int locationY;
    private float degree;
    private float flyingDonkey_Height;
    private float flyingDonkey_Width;
    private float flyingBall_Width;
    private float refreshLight_Width;
    private float refreshLight_Height;
    private Bitmap flyingDonkey;
    private Bitmap refreshLight;
    private float upDateY;

    private PointDonkey pointDonkey;
    private Paint paintBackgroud;

    int density;
    private Bitmap flyingBall;


    public drawDog(Context context) {
        super(context);
        init();
    }

    public drawDog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        density = (int) getResources().getDisplayMetrics().density;
        Log.d("tag","density"+density);


        flyingDonkey = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_flying);
        refreshLight = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_light);
        flyingBall = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_ball);

        flyingDonkey_Height = flyingDonkey.getHeight() * 0.3f;
        flyingDonkey_Width = flyingDonkey.getWidth() * 0.3f;
        refreshLight_Width = refreshLight.getWidth() * 0.15f;
        refreshLight_Height = refreshLight.getHeight() * 0.15f;

        flyingBall_Width = flyingBall.getWidth() * 0.2f;

        paintBackgroud = new Paint();
        paintBackgroud.setColor(getResources().getColor(R.color.color_Refresh_Backgroud));

        degree = 0;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        locationX = w / 2;
        locationY = h;
        pointDonkey = new PointDonkey(locationX - flyingDonkey_Width / 2, locationY - flyingDonkey_Height);

        refreshLight_Height = (w / refreshLight_Width) * refreshLight_Height;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectfLight = new RectF( 0 , locationY - refreshLight_Height / 2 , 2 * locationX , locationY + refreshLight_Height /2 );
        canvas.drawBitmap(refreshLight , null , rectfLight , null);

        Matrix matrix = new Matrix();
        matrix.setScale(0.3f, 0.3f);
        matrix.postTranslate(pointDonkey.getDx(), pointDonkey.getDy());
        matrix.postRotate(degree, locationX, locationY + flyingBall_Width / 2);
        matrix.postTranslate(0 , upDateY);
        canvas.drawBitmap(flyingDonkey, matrix, null);
    }

    public void setDonkeyRotate(int rotate , float offsetY) {
        if (Math.abs(rotate - degree) >= 0.5) {
            degree = rotate;
            upDateY = offsetY;
            invalidate();
        }
    }
    public void changeDonkey(){
        flyingDonkey = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_flying_away);
        invalidate();
    }


    public void launchDonkey() {
        flyingDonkey = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pull_2_refresh_flying);
        PointDonkey startPoint = new PointDonkey(locationX - flyingDonkey_Width / 2, locationY - flyingDonkey_Height);
        PointDonkey endPoint = new PointDonkey(2 * locationX, 0);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointDonkey = (PointDonkey) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reStart();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    public void reStart() {
        clearAnimation();
        pointDonkey.setDx(locationX - flyingDonkey_Width / 2);
        pointDonkey.setDy(locationY - flyingDonkey_Height);
        degree = 180;
    }

}
