package com.tookancustomer.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tookancustomer.R;

/**
 * Created by cl-macmini-85 on 20/06/17.
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private Context context;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        this.context=context;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + Utils.convertDpToPixels(context,15);
        int right = parent.getWidth() - parent.getPaddingRight() -  Utils.convertDpToPixels(context,15);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            //For grid Layout divider --> starts
            if (i < childCount - 1) {
                if (parent.getChildAt(i).getBottom() == parent.getChildAt(i + 1).getBottom()) {
                    mDivider.setBounds(parent.getChildAt(i).getRight(), parent.getChildAt(i).getTop() +  Utils.convertDpToPixels(context,15),
                            parent.getChildAt(i).getRight() + mDivider.getIntrinsicHeight(), parent.getChildAt(i).getBottom() -  Utils.convertDpToPixels(context,15));
                    mDivider.draw(c);
                }
            }
            // For grid layout divider --> ends

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}