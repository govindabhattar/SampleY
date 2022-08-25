package com.tookancustomer.utility;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Shweta on 12/22/17.
 */

public class AdvanceViewPager extends ViewPager {
    private boolean enabled;
    public AdvanceViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }
    public void setCurrentItem(int item, DrawerLayout drawerLayout) {
        if (item == 0) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        super.setCurrentItem(item, false);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
    /**
     * Enable or disable swipe.
     *
     * @param enabled
     */
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}