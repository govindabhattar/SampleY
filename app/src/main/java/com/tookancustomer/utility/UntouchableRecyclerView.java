package com.tookancustomer.utility;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Shweta on 1/9/18.
 */

public class UntouchableRecyclerView extends RecyclerView {

    public UntouchableRecyclerView(Context context) {
        super(context);
    }

    public UntouchableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UntouchableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}