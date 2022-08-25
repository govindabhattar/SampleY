package com.tookancustomer.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tookancustomer.R;

/**
 * regular rating bar. it wraps the stars making its size fit the parent
 */
public class CustomRatingBar extends LinearLayout {

    private static final int LOW_RATING_RED = Color.parseColor("#e2574c");
    private static final int MEDIUM_RATING_YELLOW = Color.parseColor("#FFD365");
    private static final int GOOD_RATING_GREEN = Color.parseColor("#8DCF61");

    public IRatingBarCallbacks getOnScoreChanged() {
        return onScoreChanged;
    }

    public void setOnScoreChanged(IRatingBarCallbacks onScoreChanged) {
        this.onScoreChanged = onScoreChanged;
    }

    public interface IRatingBarCallbacks {
        void scoreChanged(float score);
    }

    private int mMaxStars = 5;
    private float mCurrentScore = 0f;
    private int mStarOnResource = R.drawable.icon_star_filled;
    private int mStarOffResource = R.drawable.icon_star_empty;
    private int mStarHalfResource = R.drawable.icon_star_filled;
    private ImageView[] mStarsViews;
    private float mStarPadding;
    private IRatingBarCallbacks onScoreChanged;
    private int mLastStarId;
    private boolean mOnlyForDisplay;
    private double mLastX;
    private boolean mHalfStars = true;

    public CustomRatingBar(Context context) {
        super(context);

        init();
    }


    public ImageView[] getmStarsViews() {
        return mStarsViews;
    }

    public float getScore() {
        if (mCurrentScore <= 0) mCurrentScore = 0;
        else if (mCurrentScore <= 1) mCurrentScore = 1;
        else if (mCurrentScore <= 2) mCurrentScore = 2;
        else if (mCurrentScore <= 3) mCurrentScore = 3;
        else if (mCurrentScore <= 4) mCurrentScore = 4;
        else if (mCurrentScore <= 5) mCurrentScore = 5;
        else if (mCurrentScore > 5) mCurrentScore = 5;

        return mCurrentScore;
    }

    public void setScore(float score) {
        score = Math.round(score * 2) / 2.0f;
        if (!mHalfStars)
            score = Math.round(score);
        mCurrentScore = score;
        refreshStars();
    }


    public void setStarResource(int fullIcon, int normalIcon) {
        this.mStarOnResource = fullIcon;
        this.mStarOffResource = normalIcon;
        this.mStarHalfResource = fullIcon;

    }

    public void setScrollToSelect(boolean enabled) {
        mOnlyForDisplay = !enabled;
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs, context);
        init();
    }

    private void initializeAttributes(AttributeSet attrs, Context context) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomRatingBar_maxStars)
                mMaxStars = a.getInt(attr, 5);
            else if (attr == R.styleable.CustomRatingBar_stars)
                mCurrentScore = a.getFloat(attr, 1f);
            else if (attr == R.styleable.CustomRatingBar_starHalf)
                mStarHalfResource = a.getResourceId(attr, android.R.drawable.star_on);
            else if (attr == R.styleable.CustomRatingBar_starOn)
                mStarOnResource = a.getResourceId(attr, android.R.drawable.star_on);
            else if (attr == R.styleable.CustomRatingBar_starOff)
                mStarOffResource = a.getResourceId(attr, android.R.drawable.star_off);
            else if (attr == R.styleable.CustomRatingBar_starPadding)
                mStarPadding = a.getDimension(attr, 0);
            else if (attr == R.styleable.CustomRatingBar_onlyForDisplay)
                mOnlyForDisplay = a.getBoolean(attr, false);
            else if (attr == R.styleable.CustomRatingBar_halfStars)
                mHalfStars = a.getBoolean(attr, true);
        }
        a.recycle();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeAttributes(attrs, context);
        init();
    }

    void init() {
        mStarsViews = new ImageView[mMaxStars];
        for (int i = 0; i < mMaxStars; i++) {
            ImageView v = createStar();
            addView(v);
            mStarsViews[i] = v;
        }
        refreshStars();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * hardcore math over here
     *
     * @param x
     * @return
     */
    private float getScoreForPosition(float x) {
        if (mHalfStars)
            return (float) Math.round(((x / ((float) getWidth() / (mMaxStars * 3f))) / 3f) * 2f) / 2;
        float value = (float) Math.round((x / ((float) getWidth() / (mMaxStars))));
        value = value <= 0 ? 1 : value;
        value = value > mMaxStars ? mMaxStars : value;
        return value;

    }

    private int getImageForScore(float score) {
        if (score > 0)
            return Math.round(score) - 1;
        else return -1;
    }

    private void refreshStars() {
        boolean flagHalf = (mCurrentScore != 0 && (mCurrentScore % 0.5 == 0)) && mHalfStars;
        for (int i = 1; i <= mMaxStars; i++) {

            if (i <= mCurrentScore) {
                mStarsViews[i - 1].setImageResource(mStarOnResource);

//                switch (i) {
//                    case 1:
//                    case 2:
//                        mStarsViews[i - 1].getDrawable().setColorFilter(LOW_RATING_RED, PorterDuff.Mode.SRC_ATOP);
//                        break;
//                    case 3:
//                        mStarsViews[i - 1].getDrawable().setColorFilter(MEDIUM_RATING_YELLOW, PorterDuff.Mode.SRC_ATOP);
//                        break;
//                    default:
//                        mStarsViews[i - 1].getDrawable().setColorFilter(GOOD_RATING_GREEN, PorterDuff.Mode.SRC_ATOP);
//                        break;
//                }


            } else {
                if (flagHalf && i - 0.5 <= mCurrentScore)
                    mStarsViews[i - 1].setImageResource(mStarHalfResource);
                else
                    mStarsViews[i - 1].setImageResource(mStarOffResource);


                mStarsViews[i - 1].getDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            }

            switch (i) {
                case 1:
                    mStarsViews[i - 1].getDrawable().setColorFilter(LOW_RATING_RED, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 2:
                case 3:
                    mStarsViews[i - 1].getDrawable().setColorFilter(MEDIUM_RATING_YELLOW, PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    mStarsViews[i - 1].getDrawable().setColorFilter(GOOD_RATING_GREEN, PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        }
    }

    private ImageView createStar() {
        ImageView v = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        v.setPadding((int) mStarPadding, 0, (int) mStarPadding, 0);
        v.setAdjustViewBounds(true);
        v.setScaleType(ImageView.ScaleType.FIT_CENTER);
        v.setLayoutParams(params);
        v.setImageResource(mStarOffResource);
        return v;
    }

    private ImageView getImageView(int position) {
        try {
            return mStarsViews[position];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mOnlyForDisplay)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                animateStarRelease(getImageView(mLastStarId));
                mLastStarId = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - mLastX) > 50)
                    requestDisallowInterceptTouchEvent(true);
                float lastscore = mCurrentScore;
                mCurrentScore = getScoreForPosition(event.getX());
                if (lastscore != mCurrentScore) {
                    animateStarRelease(getImageView(mLastStarId));
                    animateStarPressed(getImageView(getImageForScore(mCurrentScore)));
                    mLastStarId = getImageForScore(mCurrentScore);
                    refreshStars();
                    if (onScoreChanged != null)
                        onScoreChanged.scoreChanged(mCurrentScore);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                lastscore = mCurrentScore;
                mCurrentScore = getScoreForPosition(event.getX());
                animateStarPressed(getImageView(getImageForScore(mCurrentScore)));
                mLastStarId = getImageForScore(mCurrentScore);
                if (lastscore != mCurrentScore) {
                    refreshStars();
                    if (onScoreChanged != null)
                        onScoreChanged.scoreChanged(mCurrentScore);
                }
        }
        return true;
    }

    private void animateStarPressed(ImageView star) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
    }

    private void animateStarRelease(ImageView star) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1f).scaleY(1f).setDuration(100).start();
    }

    public boolean isHalfStars() {
        return mHalfStars;
    }

    public void setHalfStars(boolean halfStars) {
        mHalfStars = halfStars;
    }
}