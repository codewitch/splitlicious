package com.venmo.android.splitlicious.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.venmo.android.splitlicious.R;

/**
 * Created by thomasjeon on 7/2/14.
 */
public class UserProfileImageView extends ImageView {
    public static final String TAG = "UserProfileImageView";

    private int mColor;

    private RectF mImageBounds;
    private RectF mOuterBounds;
    private float mBorderWidth;

    private Bitmap mBitmap;

    private Paint mBorderPaint;

    //Uses when creating the view in code
    public UserProfileImageView(Context context) {
        this(context, null);
    }

    //Used when inflating the view from XML
    public UserProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.UserProfileImageView,
                0, 0
        );

        try {
            mColor = a.getColor(R.styleable.UserProfileImageView_color, 0xff000000);
            mBorderWidth = a.getDimension(R.styleable.UserProfileImageView_borderWidth, 0.0f);
        } finally {
            a.recycle();
        }

        //TODO: Dynamically set color
        //Paint for borders
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float w_minus_p = (float) w - xpad;
        float h_minus_p = (float) h - ypad;

        float diameter = Math.min(w_minus_p, h_minus_p);

        mOuterBounds = new RectF(0.0f, 0.0f, diameter, diameter);
        mImageBounds = new RectF(
                mOuterBounds.left-mBorderWidth,
                mOuterBounds.top-mBorderWidth,
                mOuterBounds.right-mBorderWidth,
                mOuterBounds.bottom-mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //fill the background
        canvas.drawOval(mOuterBounds, mBorderPaint);
        getDrawable().draw(canvas);

        canvas.translate(getPaddingLeft(), getPaddingTop());

        //if (mDrawMatrix != null) {
        canvas.clipRect(mImageBounds);

        //canvas.concat(getImageMatrix());
        //getDrawable().draw(canvas);
    }

}
