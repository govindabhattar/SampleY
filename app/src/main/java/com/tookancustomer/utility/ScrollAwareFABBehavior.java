package com.tookancustomer.utility;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import android.view.View;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout,
                child, directTargetChild, target, axes, type);
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
           child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
               @Override
               public void onHidden(FloatingActionButton fab) {
                   super.onHidden(fab);
                   fab.setVisibility(View.INVISIBLE);
               }
           });
            // } else if (dyUnconsumed < 0 && child.visibility != View.VISIBLE) {
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }

    }
}
