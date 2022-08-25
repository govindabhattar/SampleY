package com.tookancustomer.modules.recurring;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Developer: Rishabh
 * Dated: 14/01/16.
 */
public class MyGridView extends GridView {

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, getLayoutParams().height == LayoutParams.WRAP_CONTENT ?
                MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST) : heightMeasureSpec);
    }

}