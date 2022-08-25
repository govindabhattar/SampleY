package com.tookancustomer.utility;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by cl-macmini-85 on 19/05/17.
 */
public class ScrollingTextView0 extends TextView implements Runnable {

    private static final float DEFAULT_SPEED = 5.0f;
    private Scroller scroller;
    private float speed = DEFAULT_SPEED;
    private boolean continuousScrolling = true;

    public ScrollingTextView0(Context context) {
        super(context);
        setup(context);
    }

    public ScrollingTextView0(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup(context);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }



    private void setup(Context context) {
        scroller = new Scroller(context, new LinearInterpolator());
        setScroller(scroller);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (scroller.isFinished() && !getText().toString().isEmpty()) {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();
            scrollLeftwards();
        } else {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text == null || text.toString().isEmpty()) {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();

        } else {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();
            scrollLeftwards();
        }

    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        if (getText() == null || getText().toString().isEmpty()) {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();

        } else {
            if (scroller != null)
//                scroller.forceFinished(true);
                scroller.abortAnimation();
            scrollLeftwards();
        }
    }

    private void scrollLeftwards() {
        int viewWidth = getWidth();
        int visibleWidth = viewWidth - getPaddingRight() - getPaddingLeft();
//        int lineHeight = getLineHeight();





        int offset = -1 * visibleWidth;
//        int distance = visibleWidth * (getLineCount()+1);
        int distance = (2 * visibleWidth) + 2 * (getText().length());
        int duration = (int) (distance * speed);

        scroller.startScroll(offset, 0, distance, 0, duration);
//        scroller.startScroll(offset, 0, distance, 0);

        if (continuousScrolling) {
            post(this);
        }
    }

    private void scrollUpwards() {
        int viewHeight = getHeight();
        int visibleHeight = viewHeight - getPaddingBottom() - getPaddingTop();
        int lineHeight = getLineHeight();

        int offset = -1 * visibleHeight;
        int distance = visibleHeight + getLineCount() * lineHeight;
        int duration = (int) (distance * speed);

        scroller.startScroll(0, offset, 0, distance, duration);

        if (continuousScrolling) {
            post(this);
        }
    }

    @Override
    public void run() {
        if (scroller.isFinished()) {
            scrollLeftwards();
        } else {
            post(this);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setContinuousScrolling(boolean continuousScrolling) {
        this.continuousScrolling = continuousScrolling;
    }

    public boolean isContinuousScrolling() {
        return continuousScrolling;
    }
}