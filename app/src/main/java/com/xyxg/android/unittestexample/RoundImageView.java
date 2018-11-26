package com.xyxg.android.unittestexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author YML
 * @date 2016/9/8
 */

public class RoundImageView extends ImageView {

    private Paint mPaint;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        super.onDraw(canvas1);
        BitmapShader shader =
                new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        Rect rect = new Rect();
        getDrawingRect(rect);
        //        RectF rectF = new RectF(getLeft() - getPaddingLeft(), getTop() - getPaddingTop(), getRight() - getPaddingRight(), getBottom() - getPaddingBottom());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, 100, 100, mPaint);
    }
}
