package com.tookancustomer.modules.payment.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.tookancustomer.appdata.Codes.Request.RESULT_PAYMENT_ERROR;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;

/*
 * This activity will be used for adding cards e.g. for stripe and payfort
 * and add paytm money
 * */
public class AddCardWebViewActivity extends BaseActivity implements View.OnClickListener, Keys.Extras {
    private WebView wvWebView;

    private long paymentMethodValue = 0L;
    private String url = "";
    private String headerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;

        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            paymentMethodValue = getIntent().getLongExtra(VALUE_PAYMENT, 0);

            if (getIntent().hasExtra(HEADER_WEBVIEW))
                headerString = getIntent().getStringExtra(HEADER_WEBVIEW);
        }

        initViews();

        if (url != null) {
            Log.e("URL", url + "");
            wvWebView.loadUrl(url);
        }
    }

    private void initViews() {
        TextView tvHeading = findViewById(R.id.tvHeading);
        if (headerString != null && !headerString.isEmpty())
            tvHeading.setText(headerString);
        else
            tvHeading.setText(getStrings(R.string.add_card));

        wvWebView = findViewById(R.id.wvWebsite);
        setWebViewProperties(wvWebView);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
    }

    private void setWebViewProperties(final WebView webView) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                //return true or false after performing the URL request

//                wvWebsite.removeAllViews();
                WebView newView = new WebView(mActivity);
                setWebViewProperties(newView);
                newView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //wvWebsite.addView(newView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onCloseWindow(WebView window) {
                Log.e("onCloseWindow", window + "");
                webView.removeView(window);
                super.onCloseWindow(window);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e("URL onJsAlert", url + "");

                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Bundle extras = new Bundle();
        extras.putBoolean("hitFetchMerchantCard", false);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return onOverrideUrlLoading(view, request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return onOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ProgressDialog.show(mActivity);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ProgressDialog.dismiss();
            super.onPageFinished(view, url);
        }
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        Log.e("URL", url + "");

        view.loadUrl(url);
        Uri uri = Uri.parse(url);

        if (paymentMethodValue == PAYTM.intValue) {
            String status = uri.getQueryParameter("status");
            if (status != null && (status.equalsIgnoreCase(Codes.PaytmStatus.SUCCESS) || status.equalsIgnoreCase(Codes.PaytmStatus.PENDING))) {
                showSuccessDialog(uri.getQueryParameter("response_message"));

            } else if (status != null && status.equalsIgnoreCase(Codes.PaytmStatus.FAILED)) {
                showErrorDialog(uri.getQueryParameter("response_message"));
            }

        } else {
            if ((url.contains("success") || url.contains("Success")) && paymentMethodValue != PAYFORT.intValue) {
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", true);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();

            }
            if ((url.contains("success.html") || url.contains("Success.html")) && paymentMethodValue == PAYFORT.intValue) {
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", true);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();

            } else if (url.contains("error")) {
                showErrorDialog(getStrings(R.string.card_cannot_be_added));
            }
        }

        return true;
    }

    private void showSuccessDialog(String successMessage) {
        new AlertDialog.Builder(mActivity).message(successMessage).listener(new AlertDialog.Listener() {
            @Override
            public void performPostAlertAction(int purpose, Bundle backpack) {
                Bundle extras = new Bundle();
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        }).build().show();
    }


    private void showErrorDialog(final String errorMessage) {
        new AlertDialog.Builder(mActivity).message(errorMessage).listener(new AlertDialog.Listener() {
            @Override
            public void performPostAlertAction(int purpose, Bundle backpack) {
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", false);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_PAYMENT_ERROR, intent);
                finish();
            }
        }).build().show();
    }
}