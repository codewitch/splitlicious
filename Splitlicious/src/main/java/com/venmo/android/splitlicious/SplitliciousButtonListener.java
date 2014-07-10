package com.venmo.android.splitlicious;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by thomasjeon on 7/6/14.
 */
public class SplitliciousButtonListener implements View.OnTouchListener {

    private View mButtonView;
    private Runnable mRunnable;
    private AlphaAnimation mAlphaDown;
    private AlphaAnimation mAlphaUp;
    private int mAlphaDuration;

    public SplitliciousButtonListener(View buttonView, Runnable runnable, float toOpacity){

        mButtonView = buttonView;
        mRunnable = runnable;

        mAlphaDuration = 100;

        mAlphaDown = new AlphaAnimation(1.0f, toOpacity);
        mAlphaUp = new AlphaAnimation(toOpacity, 1.0f);
        mAlphaDown.setDuration(mAlphaDuration);
        mAlphaUp.setDuration(mAlphaDuration);
        mAlphaDown.setFillAfter(true);
        mAlphaUp.setFillAfter(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mButtonView.startAnimation(mAlphaDown);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mButtonView.startAnimation(mAlphaUp);
                if(rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                    ViewCompat.postOnAnimationDelayed(mButtonView, mRunnable, mAlphaDuration+100);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mButtonView.startAnimation(mAlphaUp);
                break;
        }

        return true;
    }
}
