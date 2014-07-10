package com.venmo.android.splitlicious.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.venmo.android.splitlicious.R;

/**
 * Created by thomasjeon on 7/6/14.
 */
public class SplitliciousButton extends View{

    //TODO: disabled button view (here?)

    private static final int BUTTON_PRESSED_ALPHA = 200;

    private Paint mButtonPaint;
    private Paint mTextPaint;
    private String mText;
    private Bitmap mButtonIcon;
    private RectF mButtonRect;

    private float mWidth;
    private float mHeight;

    //Uses when creating the view in code
    public SplitliciousButton(Context context) {
        this(context, null);
    }

    //Used when inflating the view from XML
    public SplitliciousButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SplitliciousButton,
                0, 0
        );

        int buttonIconId;
        int buttonColor;
        int textColor;
        float textSize;

        try {
            buttonIconId = a.getResourceId(R.styleable.SplitliciousButton_button_icon, R.drawable.white_check);
            buttonColor = a.getColor(R.styleable.SplitliciousButton_button_color, 0xff000000);
            mText = a.getString(R.styleable.SplitliciousButton_button_text);
            textColor = a.getColor(R.styleable.SplitliciousButton_button_text_color, 0xff000000);
            textSize = a.getDimension(R.styleable.SplitliciousButton_button_text_size, 25.0f);
        } finally {
            a.recycle();
        }

        //button color
        mButtonPaint = new Paint();
        mButtonPaint.setColor(buttonColor);

        //text paint
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setStyle(Paint.Style.FILL);

        mButtonIcon = BitmapFactory.decodeResource(this.getResources(), buttonIconId);
    }

    public void setText(String newText) {
        mText = newText;
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

        //calcButtonAlpha();

        float flapHeight = 35.0f;
        float flapWidth = 80.0f;
        float hCenter = mWidth/2.0f;
        float vCenter = (mHeight+flapHeight)/2.0f;

        //draw main button area
        mButtonRect = new RectF(0.0f, flapHeight, mWidth, mHeight);
        canvas.drawRect(mButtonRect, mButtonPaint);

        //draw icon flap
        RectF roundedFlapRect = new RectF(hCenter-flapWidth/2.0f, 0.0f, hCenter+flapWidth/2.0f, flapHeight+30.0f);
        canvas.drawRoundRect(roundedFlapRect, 15.0f, 15.0f, mButtonPaint);

        //draw button text
        canvas.drawText(mText, hCenter, vCenter + 16.0f, mTextPaint);

        //draw icon
        drawIcon(canvas);

    }

    private void drawIcon(Canvas canvas) {

        float iconWidth = 22.0f;
        float iconLeft = mWidth/2.0f - iconWidth/2.0f;
        float iconRight = mWidth/2.0f + iconWidth/2.0f;
        float iconTop = 25.0f;
        float iconBottom = iconTop + iconWidth*56.0f/75.0f;

        RectF iconRect = new RectF(iconLeft, iconTop, iconRight, iconBottom);
        canvas.drawBitmap(mButtonIcon, null, iconRect, null);

    }

    public void animateSplitFinish(){
        /*set
        LayoutParams
        setLayoutParams();*/
    }

}
