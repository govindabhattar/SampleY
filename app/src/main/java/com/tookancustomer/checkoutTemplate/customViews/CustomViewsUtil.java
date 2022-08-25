package com.tookancustomer.checkoutTemplate.customViews;

import android.app.Activity;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.tookancustomer.R;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Utils;

public class CustomViewsUtil {

    public static Spannable createSpan(Activity activity, String label, String label1, String label2) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(label + label1 + label2);
            wordToSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.colorRed)),
                    label.length(), label.length() + label1.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            return wordToSpan;
        }

    }

    public static Spannable createSpan(Activity activity, String label,
                                       String label1, String label2, String label3, int color) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(label + label1 + label2 + label3);
            wordToSpan.setSpan(new ForegroundColorSpan(color),
                    label.length(), wordToSpan.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            return wordToSpan;
        }

    }

    public static Spannable createSpan(Activity activity, String label, String label1) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(label + label1);
            wordToSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.colorRed)),
                    label.length(), label.length() + label1.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            return wordToSpan;
        }

    }

    public static Spannable createSpanForBoldText(Activity activity, String text, String boldText) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(text);
            wordToSpan.setSpan(new CustomTypefaceSpan("", Font.getSemiBold(activity)),
                    text.indexOf(boldText), text.indexOf(boldText) + boldText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            return wordToSpan;
        }

    }
    public static Spannable createSpanForExtraBoldText(Activity activity, String text, String boldText) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(text);
            wordToSpan.setSpan(new CustomTypefaceSpan("", Font.getBold(activity)),
                    text.indexOf(boldText), text.indexOf(boldText) + boldText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            return wordToSpan;
        }

    }
}
