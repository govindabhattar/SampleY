package com.tookancustomer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.Prefs.MARKETPLACE_REF_ID;

public class WebViewActivityFACPay extends BaseActivity implements Keys.Extras, View.OnClickListener {
    private String url = "", headerString = "";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;

        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            headerString = getIntent().getStringExtra(HEADER_WEBVIEW);
        }

        Log.e("URL", url + "");

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerString != null ? headerString : "");

        webView = findViewById(R.id.wvWebsite);
        setWebViewProperties(webView);

        if (url != null && !url.isEmpty()) {
//            WebView newView = new WebView(mActivity);
//            setWebViewProperties(newView);
//            newView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            webView.loadUrl(url);
//            webView.addView(newView);
        }
    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
//        super.onBackPressed();
            Bundle extras = new Bundle();
            extras.putBoolean("hitFetchMerchantCard", false);
            Intent intent = new Intent();
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
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
        public void onPageFinished(WebView view, String url) {
            ProgressDialog.dismiss();
            Log.e("URL onPageFinished", url + "");

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e("URL onPageStarted", url + "");
            ProgressDialog.show(mActivity);
            super.onPageStarted(view, url, favicon);
        }
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        Log.e("URL onOverrideUrlLoading", url + "");
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        String facPaymentId = sanitizer.getValue("transactionId");
        view.loadUrl(url);

        if (url.toLowerCase().contains("success") /*&& (facPaymentId != null && !facPaymentId.isEmpty())*/) {
//            https://test123-yeloservice2.taxi-hawk.com/en/facPayment/redirect?resultCode=Success&facTransactionId=832310579773&merchant_order_id=17166
//            initiatePaymentAfterCreateTaskFAC(merchant_order_id);

            Bundle extras = new Bundle();
            extras.putBoolean("hitFetchMerchantCard", true);
//            if (facPaymentId != null && !facPaymentId.isEmpty()) {
            extras.putString("FACTransactionId", facPaymentId);
//            }
            Intent intent = new Intent();
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500);

//
        } else if (url.toLowerCase().contains("error")) {
            showErrorDialog(getStrings(StorefrontCommonData.getAppConfigurationData().getPostPaymentEnable() == 1 ? R.string.transaction_failed : R.string.pre_transaction_failed));
        }

        return true;
    }

    private void showErrorDialog(final String errorMessage) {
        new AlertDialog.Builder(mActivity).message(errorMessage).listener(new AlertDialog.Listener() {
            @Override
            public void performPostAlertAction(int purpose, Bundle backpack) {
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", false);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }).build().show();
    }

    private void setWebViewProperties(final WebView webView) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAllowContentAccess(true);
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
                webView.addView(newView);
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
}