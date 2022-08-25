package com.tookancustomer.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.tookancustomer.R;
import com.tookancustomer.plugin.AccelerateOvershootInterpolator;

public class AnimationUtils {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean isRTL(Activity mActivity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = mActivity.getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }


    public static void exitTransition(Activity mActivity) {
        if (isRTL(mActivity)) mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        else mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public static void forwardTransition(Activity mActivity) {
        if (isRTL(mActivity)) mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        else mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void BottonToTopTransition(Activity mActivity) {
        if (isRTL(mActivity))
            mActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        else mActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public static Animation outToLeftAnimation(int duration) {
        Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(duration);
        outtoLeft.setInterpolator(new AccelerateOvershootInterpolator(2.0f, 0.7f));
        return outtoLeft;
    }

    public static Animation inFromRightAnimation(int duration) {
        AccelerateOvershootInterpolator interpolator = new AccelerateOvershootInterpolator(2.0f, 0.7f);
        Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(duration);
        inFromRight.setInterpolator(interpolator);
        return inFromRight;
    }

    public static Animation outToRightAnimation(int duration) {
        Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(duration);
        outtoRight.setInterpolator(new AccelerateOvershootInterpolator(2.0f, 0.7f));
        return outtoRight;
    }

    public static Animation inFromLeftAnimation(int duration) {
        Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(duration);
        inFromLeft.setInterpolator(new AccelerateOvershootInterpolator(2.0f, 0.7f));
        return inFromLeft;
    }

}