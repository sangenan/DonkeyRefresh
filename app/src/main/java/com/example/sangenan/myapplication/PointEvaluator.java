package com.example.sangenan.myapplication;

import android.animation.TypeEvaluator;

/**
 * Created by kuwakuzukusunoki on 2017/1/6.
 */

public class PointEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointDonkey startPoint = (PointDonkey) startValue;
        PointDonkey endPoint = (PointDonkey) endValue;
        float x = startPoint.getDx() + fraction * (endPoint.getDx() - startPoint.getDx());
        float y = startPoint.getDy() + fraction * (endPoint.getDy() - startPoint.getDy());
        PointDonkey point = new PointDonkey(x , y);
        return point;
    }
}
