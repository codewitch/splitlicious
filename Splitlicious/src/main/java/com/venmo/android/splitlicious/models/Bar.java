package com.venmo.android.splitlicious.models;

import android.graphics.PointF;

/**
 * Created by thomasjeon on 7/2/14.
 */
public class Bar {
    private static final float mTouchRange = 55;

    private float mTop;
    private float mBottom;
    private boolean mImmutable;

    public Bar(float top, float bottom){
        mTop = top;
        mBottom = bottom;
        mImmutable = true;
    }

    public boolean isYNearTop(float touchY) {
        if ( touchY <= mTop+mTouchRange && touchY >= mTop-mTouchRange){
            return true;
        }
        return false;
    }

    public boolean isYBelowBottom(float touchY) {
        if (touchY > mBottom) {
            return true;
        }
        return false;
    }

    public float getBottom() {
        return mBottom;
    }

    public float getTop() {
        return mTop;
    }

    public boolean setBottom(float bottom) {
        if( mImmutable ){ return false; }
        mBottom = bottom;
        return true;
    }

    public boolean setTop(float top) {
        if( mImmutable ){ return false; }
        mTop = top;
        return true;
    }

    public boolean isImmutable() {
        return mImmutable;
    }

    public void setImmutable(boolean immutable) {
        mImmutable = immutable;
    }
}
