package com.tookancustomer.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tookancustomer.R;

/**
 * Created by Parminder Singh on 2/13/17.
 */

/**
 * Draws diagnol line across the textview
 */
public class TextViewStrikeThrough extends TextView {

    private Paint paint;

    public TextViewStrikeThrough(Context context) {
        super(context);
    }

    public TextViewStrikeThrough(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.color_red));
            paint.setStyle(Paint.Style.FILL);
            paint.setStrikeThruText(true);
            paint.setStrokeWidth((int) (1 * 4.0f));
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        }
        canvas.drawLine(0, ((float)getHeight())* 0.6f, getWidth(), ((float)getHeight())* 0.3f, paint);
//        canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, paint);
    }
}