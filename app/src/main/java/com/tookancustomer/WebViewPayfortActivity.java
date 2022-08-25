package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tookancustomer.appdata.Keys;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Codes.Request.RESULT_PAYMENT_ERROR;

public class WebViewPayfortActivity extends BaseActivity implements Keys.Extras, View.OnClickListener {
    private String url = "", headerString = "";
    private boolean isTNC;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;

        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            headerString = getIntent().getStringExtra(HEADER_WEBVIEW);
            isTNC = getIntent().getBooleanExtra(IS_TNC, false);
        }
        Log.e("URL", url + "");

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerString != null ? headerString : "");

        WebView wvWebsite = findViewById(R.id.wvWebsite);
        wvWebsite.setWebViewClient(new MyWebViewClient());
        wvWebsite.setWebChromeClient(new WebChromeClient());
        wvWebsite.getSettings().setLoadsImagesAutomatically(true);
        wvWebsite.getSettings().setJavaScriptEnabled(true);
        wvWebsite.getSettings().setDomStorageEnabled(true);
        wvWebsite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (url != null)
            if (isTNC) {
                wvWebsite.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            } else {
                wvWebsite.loadUrl(url);
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
//        super.onBackPressed();
        Bundle extras = new Bundle();
        extras.putBoolean("hitFetchMerchantCard", false);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
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
        public void onPageFinished(WebView view, String url) {
            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
            String transactionId = sanitizer.getValue("transactionId");
            String jobPaymentDetailId = sanitizer.getValue("job_payment_detail_id");

            ProgressDialog.dismiss();
            Log.e("URL", url + "");
            if (url.startsWith("http:") || url.startsWith("https:")) {
                if (url.toLowerCase().contains("success") && (transactionId != null && !transactionId.isEmpty())) {
                    Bundle extras = new Bundle();
                    extras.putBoolean("hitFetchMerchantCard", true);
                    if (transactionId != null && !transactionId.isEmpty()) {
                        extras.putString("transactionId", transactionId);
                        extras.putString("jobPaymentDetailId", jobPaymentDetailId);
                    }
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (url.toLowerCase().contains("error")) {

                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.transaction_failed)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            Bundle extras = new Bundle();
                            Intent intent = new Intent();
                            intent.putExtras(extras);
                            setResult(RESULT_PAYMENT_ERROR, intent);
                            finish();
                        }
                    }).build().show();
                }

            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ProgressDialog.show(mActivity);
            super.onPageStarted(view, url, favicon);
        }
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        Log.e("URL", url + "");
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        String transactionId = sanitizer.getValue("transactionId");
        String jobPaymentDetailId = sanitizer.getValue("job_payment_detail_id");

        if (url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url);
            if (url.toLowerCase().contains("success") && (transactionId != null && !transactionId.isEmpty())) {

                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", true);
                if (transactionId != null && !transactionId.isEmpty()) {
                    extras.putString("transactionId", transactionId);
                    extras.putString("jobPaymentDetailId", jobPaymentDetailId);
                }
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            } else if (url.toLowerCase().contains("error")) {
                new AlertDialog.Builder(mActivity).message(getStrings(R.string.card_cannot_be_added)).listener(new AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        Bundle extras = new Bundle();
                        Intent intent = new Intent();
                        intent.putExtras(extras);
                        setResult(RESULT_PAYMENT_ERROR, intent);
                        finish();
                    }
                }).build().show();
            }
        } else if (url.startsWith(WebView.SCHEME_TEL) ||
                url.startsWith("sms:") ||
                url.startsWith(WebView.SCHEME_MAILTO) ||
                url.startsWith(WebView.SCHEME_GEO) ||
                url.startsWith("maps:")) {
            try {
                Log.d("WEbview", "loading in external app");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url.replace("Not Available", "")));
                startActivity(intent);
                AnimationUtils.forwardTransition(mActivity);
            } catch (android.content.ActivityNotFoundException e) {
                Log.e("WEbview", "Error opening external app " + url + ": " + e.toString());
            }
        } else if (url.startsWith("intent:")) {
        } else {
            return true;
        }
        return true;
    }
}