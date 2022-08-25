package com.tookancustomer.instagram;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tookancustomer.R;
import com.tookancustomer.SplashActivity;
import com.tookancustomer.dialog.ProgressDialog;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by Ashutosh Ojha on 9/8/18.
 */
public class MyAlertDialogFragment extends DialogFragment {
    private String url;
    private Context mCtx;
    private OAuthDialogListener mListener;
    private View vPlaceHolder;

    public MyAlertDialogFragment(final Context mCtx, final String mAuthUrl, final OAuthDialogListener mListener) {
        this.url = mAuthUrl;
        this.mCtx = mCtx;
        this.mListener = mListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_instagram, container,
                false);

        vPlaceHolder = rootView.findViewById(R.id.vPlaceHolder);
//        rlActionBarl.setVisibility(View.GONE);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setWebView(rootView);
        return rootView;
    }

    private void setWebView(View view) {

        Log.e("URL", url + "");

        final WebView wvWebsite = view.findViewById(R.id.wvWebsite);
        wvWebsite.setVerticalScrollBarEnabled(false);
        wvWebsite.setHorizontalScrollBarEnabled(false);
        wvWebsite.setWebViewClient(new OAuthWebViewClient());
        wvWebsite.getSettings().setJavaScriptEnabled(true);
        wvWebsite.loadUrl(url);

        wvWebsite.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= 21) {
            wvWebsite.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }


    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("TAG", "Redirecting URL " + url);
            vPlaceHolder.setVisibility(View.VISIBLE);

            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                ProgressDialog.dismiss();

                dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.d("tag", "Page error: " + description);

            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
            ProgressDialog.dismiss();
            dismiss();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("Tag", "Loading URL: " + url);

            super.onPageStarted(view, url, favicon);
            ProgressDialog.show((SplashActivity) mCtx);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            String title = mWebView.getTitle();
//            if (title != null && title.length() > 0) {
//                mTitle.setText(title);
//            }
            Log.d("TAG", "onPageFinished URL: " + url);
            ProgressDialog.dismiss();

        }

    }

    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);

        public abstract void onError(String error);
    }
}
