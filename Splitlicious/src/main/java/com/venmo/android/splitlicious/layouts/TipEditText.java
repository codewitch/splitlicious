package com.venmo.android.splitlicious.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.venmo.android.splitlicious.R;

/**
 * Created by thomasjeon on 7/6/14.
 */
public class TipEditText extends View {

    private Paint mLabelPaint;
    private Paint mAmountPaint;
    private Paint mViewPaint;
    private Paint mCanvasPaint;
    private Bitmap mIconLeft;
    private Bitmap mIconUpArrow;
    private Bitmap mIconDownArrow;
    private Bitmap mIconUpArrowPressed;
    private Bitmap mIconDownArrowPressed;
    private String mTipLabel;
    private String mTipAmount;
    private boolean mUpArrowPressed;
    private boolean mDownArrowPressed;

    private RectF mArrowUpRange;
    private RectF mArrowDownRange;

    private float mWidth;
    private float mHeight;

    //Uses when creating the view in code
    public TipEditText(Context context) {
        this(context, null);
    }

    //Used when inflating the view from XML
    public TipEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SplitliciousTipEditText,
                0, 0
        );

        String tip_label;
        String tip_percentage;
        float tip_label_size;
        float tip_amount_size;
        int iconLeftId;
        int iconUpId;
        int iconDownId;
        int iconUpIdPressed;
        int iconDownIdPressed;
        int textColor;
        int backgroundColor;
        int canvasColor;

        try {
            tip_label = a.getString(R.styleable.SplitliciousTipEditText_tip_label);
            tip_percentage = a.getString(R.styleable.SplitliciousTipEditText_tip_percentage);
            tip_label_size = a.getDimension(R.styleable.SplitliciousTipEditText_tip_label_size, 12.0f);
            tip_amount_size = a.getDimension(R.styleable.SplitliciousTipEditText_tip_amount_size, 20.0f);
            iconLeftId= a.getResourceId(R.styleable.SplitliciousTipEditText_tip_icon_left, R.drawable.gray_cross);
            iconUpId = a.getResourceId(R.styleable.SplitliciousTipEditText_tip_icon_up, R.drawable.gray_arrow);
            iconDownId= a.getResourceId(R.styleable.SplitliciousTipEditText_tip_icon_down, R.drawable.gray_arrow_down);
            iconUpIdPressed = a.getResourceId(R.styleable.SplitliciousTipEditText_tip_icon_up_pressed, R.drawable.white_arrow);
            iconDownIdPressed= a.getResourceId(R.styleable.SplitliciousTipEditText_tip_icon_down_pressed, R.drawable.white_arrow_down);
            textColor = a.getColor(R.styleable.SplitliciousTipEditText_tip_text_color, 0xff000000);
            backgroundColor = a.getColor(R.styleable.SplitliciousTipEditText_tip_background_color, 0xff000000);
            canvasColor = a.getColor(R.styleable.SplitliciousTipEditText_tip_canvas_color, 0xff000000);
        } finally {
            a.recycle();
        }

        //text paint
        mLabelPaint = new Paint();
        mLabelPaint.setColor(textColor);
        mLabelPaint.setTextSize(tip_label_size);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mLabelPaint.setStyle(Paint.Style.FILL);

        mAmountPaint = new Paint();
        mAmountPaint.setColor(textColor);
        mAmountPaint.setTextSize(tip_amount_size);
        mAmountPaint.setTextAlign(Paint.Align.CENTER);
        mAmountPaint.setStyle(Paint.Style.FILL);

        //background paint
        mViewPaint = new Paint();
        mViewPaint.setColor(backgroundColor);

        //canvas paint
        mCanvasPaint = new Paint();
        mCanvasPaint.setColor(canvasColor);

        mIconLeft = BitmapFactory.decodeResource(this.getResources(), iconLeftId);
        mIconUpArrow = BitmapFactory.decodeResource(this.getResources(), iconUpId);
        mIconDownArrow = BitmapFactory.decodeResource(this.getResources(), iconDownId);
        mIconUpArrowPressed = BitmapFactory.decodeResource(this.getResources(), iconUpIdPressed);
        mIconDownArrowPressed = BitmapFactory.decodeResource(this.getResources(), iconDownIdPressed);

        mTipLabel = tip_label + " " + tip_percentage;
        mTipAmount = "$0.00";

        mUpArrowPressed = false;
        mDownArrowPressed = false;
    }

    public void setTipLabel(String tipLabel) {
        mTipLabel = tipLabel;
        invalidate();
    }

    public void setTipAmount(String tipAmount) {
        mTipAmount = tipAmount;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPaint(mCanvasPaint);

        float flapHeight = 80.0f;
        float flapWidth = 35.0f;
        float hCenter = (mWidth+flapWidth)/2.0f;
        float vCenter = mHeight/2.0f;

        //draw main view area
        RectF mainRect = new RectF(flapWidth, 0.0f, mWidth, mHeight);
        canvas.drawRect(mainRect, mViewPaint);

        //draw icon flap
        RectF roundedFlapRect = new RectF(0.0f, vCenter-flapHeight/2.0f, flapWidth+20.0f, vCenter+flapHeight/2.0f);
        canvas.drawRoundRect(roundedFlapRect, 15.0f, 15.0f, mViewPaint);

        float textOffset = 10.0f;
        //draw label text
        canvas.drawText(mTipLabel, hCenter, vCenter-textOffset, mLabelPaint);

        //draw amount text
        canvas.drawText(mTipAmount, hCenter, vCenter+flapHeight/2.0f-textOffset, mAmountPaint);

        //draw icons
        drawIcons(canvas, hCenter, vCenter);
    }

    private void drawIcons(Canvas canvas, float hCenter, float vCenter) {

        //icon left
        float iconWidth = 30.0f;
        float iconTop = vCenter - iconWidth/2.0f;
        float iconBottom = vCenter + iconWidth/2.0f;
        float iconLeft = 25.0f;
        float iconRight = iconLeft + iconWidth;

        RectF iconRect = new RectF(iconLeft, iconTop, iconRight, iconBottom);
        canvas.drawBitmap(mIconLeft, null, iconRect, null);

        //icon arrows
        float arrowWidth = 22.0f;
        float arrowHeight = arrowWidth*56.0f/75.0f;
        float arrowPadding = 22.0f;

        float ArrowLeft = hCenter - arrowWidth/2;
        float ArrowRight = hCenter + arrowWidth/2;
        float upArrowTop = arrowPadding;
        float upArrowBottom = upArrowTop + arrowHeight;
        float downArrowBottom = mHeight - arrowPadding;
        float downArrowTop = downArrowBottom - arrowHeight;

        RectF upArrowRect = new RectF(ArrowLeft, upArrowTop, ArrowRight, upArrowBottom);
        RectF downArrowRect = new RectF(ArrowLeft, downArrowTop, ArrowRight, downArrowBottom);

        if (mUpArrowPressed){
            canvas.drawBitmap(mIconUpArrowPressed, null, upArrowRect, null);
        }else {
            canvas.drawBitmap(mIconUpArrow, null, upArrowRect, null);
        }
        if (mDownArrowPressed) {
            canvas.drawBitmap(mIconDownArrowPressed, null, downArrowRect, null);
        }else {
            canvas.drawBitmap(mIconDownArrow, null, downArrowRect, null);
        }

        float touchOffset = 5.0f;
        mArrowUpRange = new RectF(ArrowLeft-touchOffset, upArrowTop-touchOffset, ArrowRight+touchOffset, upArrowBottom+touchOffset);
        mArrowDownRange = new RectF(ArrowLeft-touchOffset, downArrowTop-touchOffset, ArrowRight+touchOffset, downArrowBottom+touchOffset);

    }

    public boolean isTouchNearUp(PointF curr) {
        return mArrowUpRange.contains(curr.x, curr.y);
    }

    public boolean isTouchNearDown(PointF curr) {
        return mArrowDownRange.contains(curr.x, curr.y);
    }

    public void setUpArrowPressed(boolean pressed){
        mUpArrowPressed = pressed;
        invalidate();
    }

    public void setDownArrowPressed(boolean pressed){
        mDownArrowPressed = pressed;
        invalidate();
    }
}
