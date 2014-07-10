package com.venmo.android.splitlicious.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.venmo.android.splitlicious.R;
import com.venmo.android.splitlicious.models.Bar;


/**
 * Created by thomasjeon on 7/2/14.
 */
public class SplitBarView extends View {

    private static final float EVEN_LEVEL = 2.0f;

    private Bar mCurrentBar;
    private Paint mBarPaint;
    private Paint mTextPaint;
    private String mText;
    private boolean mShowText;
    private int mTextAlpha;
    private Bitmap mUpArrow;
    private Bitmap mDownArrow;

    private float mLeft;
    private float mRight;

    //Uses when creating the view in code
    public SplitBarView(Context context) {
        this(context, null);
    }

    //Used when inflating the view from XML
    public SplitBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SplitBarView,
                0, 0
        );

        int barColor;
        int textColor;
        float textSize;

        try {
            barColor = a.getColor(R.styleable.SplitBarView_bar_color, 0xff000000);
            textColor = a.getColor(R.styleable.SplitBarView_text_color, 0xff000000);
            textSize = a.getDimension(R.styleable.SplitBarView_text_size, 9.0f);
        } finally {
            a.recycle();
        }

        mText = "";
        mShowText = false;
        mTextAlpha = 0;

        //Paint for the bar
        mBarPaint = new Paint();
        mBarPaint.setColor(barColor);

        //Paint for the text
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAlpha(mTextAlpha);

        mUpArrow = BitmapFactory.decodeResource(this.getResources(), R.drawable.white_arrow);
        mDownArrow =  BitmapFactory.decodeResource(this.getResources(), R.drawable.white_arrow_down);
    }



    public void setBorderColor(int newColor) {
        int color = getResources().getColor(newColor);
        mBarPaint.setColor(color);
    }

    public void setText(String newText) {
        mText = newText;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLeft = 0.0f;
        mRight = w;
        mCurrentBar = new Bar(h/EVEN_LEVEL, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //draw rectangle}
        float roundedTop = mCurrentBar.getTop();
        float bottom = mCurrentBar.getBottom();

        float roundedBottom = ((bottom - roundedTop) >  50)?  roundedTop+50.0f : bottom;
        float top = ((bottom - roundedTop) > 15) ? roundedBottom-10.0f : roundedTop+5.0f;

        RectF roundedTopRect = new RectF(mLeft, roundedTop, mRight, roundedBottom);

        canvas.drawRect(mLeft, top, mRight, bottom, mBarPaint);
        canvas.drawRoundRect(roundedTopRect, 5.0f, 5.0f, mBarPaint);

        //draw dollar amount
        calcTextAlpha();
        canvas.drawText(mText, (mRight - mLeft) / 2.0f, bottom - 30.0f, mTextPaint);

        //draw arrows
        drawArrows(canvas, roundedTop);

        if(mTextAlpha<255 && mTextAlpha>0) {
            invalidate();
        }
    }

    private void drawArrows(Canvas canvas, float barTop) {

        float center = (mRight - mLeft)/2.0f;
        float arrowWidth = 22.0f;
        float ArrowLeft = center - arrowWidth/2;
        float ArrowRight = center + arrowWidth/2;
        float upArrowBottom = barTop - 15.0f;
        float upArrowTop = upArrowBottom - (arrowWidth*56.0f/75.0f);
        float downArrowTop = barTop + 15.0f;
        float downArrowBottom = downArrowTop + (arrowWidth*56.0f/75.0f);

        RectF upArrowRect = new RectF(ArrowLeft, upArrowTop, ArrowRight, upArrowBottom);
        RectF downArrowRect = new RectF(ArrowLeft, downArrowTop, ArrowRight, downArrowBottom);

        canvas.drawBitmap(mUpArrow, null, upArrowRect, null);
        canvas.drawBitmap(mDownArrow, null, downArrowRect, null);

    }

    private void calcTextAlpha() {
        if(mShowText) {
            mTextAlpha = Math.min(mTextAlpha+20, 255);
        }else{
            mTextAlpha = Math.max(mTextAlpha - 20, 0);
        }
        mTextPaint.setAlpha(mTextAlpha);
    }

    public boolean isTouchNearTop(PointF curr){
        return mCurrentBar.isYNearTop(curr.y);
    }

    public boolean isTouchOutOfRange(PointF curr){
        return mCurrentBar.isYBelowBottom(curr.y) || curr.y <= 0;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
    }

    public void setBarImmutable(boolean barImmutable){
        mCurrentBar.setImmutable(barImmutable);
    }

    public void setBarTop(float top){
        mCurrentBar.setTop(top);
    }

    public float getBarBottom() {
        return mCurrentBar.getBottom();
    }

    public float getBarTop() {
        return mCurrentBar.getTop();
    }

    public float getBarLevel() {
        return getBarBottom() - getBarTop();
    }

    public float getBarEvenLevel() {
        return getBarBottom()/EVEN_LEVEL;
    }
}
