package com.tookancustomer.utility;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by shwetaaggarwal on 17/07/17.
 */

public class RectangleImageView extends ImageView {
    public RectangleImageView(Context context) {
        super(context);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (width*1)/2);
    }

}
