package com.venmo.android.splitlicious;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.venmo.android.splitlicious.layouts.SplitBarView;
import com.venmo.android.splitlicious.models.Bill;
import com.venmo.android.splitlicious.models.BillLab;

import java.util.UUID;

/**
 * Created by thomasjeon on 7/7/14.
 */
public class SplitliciousBarTouchListener implements View.OnTouchListener {

    private Context mAppContext;
    private SplitBarView mBarView;
    private Bill mBill;
    private UUID mPersonId;

    public SplitliciousBarTouchListener(SplitBarView barView, UUID billId, UUID personId, Context context){

        mAppContext = context;
        mBarView = barView;
        mBill = BillLab.get(mAppContext).getBill(billId);
        mPersonId = personId;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mBarView.isTouchNearTop(curr)){
                    String newText = "$" + String.format("%.2f", mBill.getPersonAmount(mPersonId));
                    mBarView.setText(newText);
                    mBarView.setBarImmutable(false);
                    mBarView.setShowText(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mBarView.isTouchOutOfRange(curr)) {
                    mBarView.setBarTop(curr.y);
                    mBill.setPersonLevel(mPersonId, mBarView.getBarLevel());
                    mBill.commit();
                    String newText = "$" + String.format("%.2f", mBill.getPersonAmount(mPersonId));
                    mBarView.setText(newText);
                }
                break;
            case MotionEvent.ACTION_UP:
                touchOff();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchOff();
                break;
        }

        mBarView.invalidate();
        return true;
    }

    public void touchOff(){
        mBarView.setShowText(false);
        mBarView.setBarImmutable(true);
    }

}

