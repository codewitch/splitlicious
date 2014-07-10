package com.venmo.android.splitlicious;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.venmo.android.splitlicious.layouts.TipEditText;
import com.venmo.android.splitlicious.models.Bill;
import com.venmo.android.splitlicious.models.BillLab;

import java.util.UUID;

/**
 * Created by thomasjeon on 7/6/14.
 */
public class SplitliciousTipTouchListener implements View.OnTouchListener {

    private Context mAppContext;
    private TipEditText mTipView;
    private Bill mBill;
    private boolean mTouchIsDown;
    private float mIncrementAmount;

    public SplitliciousTipTouchListener(TipEditText tipView, UUID billId, Context context){

        mAppContext = context;
        mTipView = tipView;
        mBill = BillLab.get(mAppContext).getBill(billId);
        mTouchIsDown = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIncrementAmount = 0.0f;
                mTouchIsDown = true;
                if (mTipView.isTouchNearUp(curr)){
                    mTipView.setDownArrowPressed(false);
                    mTipView.setUpArrowPressed(true);
                    mIncrementAmount = 0.01f;
                }else if(mTipView.isTouchNearDown(curr)){
                    mTipView.setUpArrowPressed(false);
                    mTipView.setDownArrowPressed(true);
                    mIncrementAmount = -0.01f;
                }
                incrementTip();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTipView.isTouchNearUp(curr)){
                    mTipView.setDownArrowPressed(false);
                    mTipView.setUpArrowPressed(true);
                    mIncrementAmount = 0.01f;
                }else if(mTipView.isTouchNearDown(curr)){
                    mTipView.setUpArrowPressed(false);
                    mTipView.setDownArrowPressed(true);
                    mIncrementAmount = -0.01f;
                }else {
                    mTipView.setDownArrowPressed(false);
                    mTipView.setUpArrowPressed(false);
                    mIncrementAmount = 0.0f;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchOff();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchOff();
                break;
        }

        return true;
    }



    public void touchOff(){
        mTouchIsDown = false;
        mIncrementAmount = 0;
        mTipView.setDownArrowPressed(false);
        mTipView.setUpArrowPressed(false);
    }

    public void incrementTip(){

        mBill.setTipAmount(mBill.getTipAmount()+mIncrementAmount);

        String newTipAmount = "$" + String.format("%.2f", mBill.getTipAmount());

        int tipPercent = (int) Math.round(mBill.getTipPercentage()*100);
        String newTipLabel = "TIP " + String.format("%d", tipPercent) + "%";

        mTipView.setTipLabel(newTipLabel);
        mTipView.setTipAmount(newTipAmount);

        if(mTouchIsDown){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    incrementTip();
                }
            }, 10);
        }
    }

}
