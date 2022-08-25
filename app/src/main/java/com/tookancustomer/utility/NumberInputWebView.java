package com.tookancustomer.utility;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.WebView;
/**
 * Created by cl-macmini-85 on 05/05/17.
 */
public class NumberInputWebView extends WebView {
    public NumberInputWebView(Context context) {
        super(context);
    }
    public NumberInputWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NumberInputWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public NumberInputWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public NumberInputWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection ic = new BaseInputConnection(this, true);
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER; // Tells the keyboard to show the number pad
        return ic;
    }
}