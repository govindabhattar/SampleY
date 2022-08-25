package com.tookancustomer.utility;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by shwetaaggarwal on 17/07/17.
 */

public class CategoryRectangleImageView extends ImageView {
    public CategoryRectangleImageView(Context context) {
        super(context);
    }

    public CategoryRectangleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryRectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategoryRectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (width*2)/5);
    }

}
