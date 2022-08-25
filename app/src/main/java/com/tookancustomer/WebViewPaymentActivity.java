package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
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

import com.tookancustomer.appdata.Codes;
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
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_REF_ID;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.BILLPLZ;

public class WebViewPaymentActivity extends BaseActivity implements Keys.Extras, View.OnClickListener {
    private String url = "", headerString = "";
    private boolean isTNC, fromAppWalletScreen;
    private Activity mActivity;
    private long paymentMethodValue = 0L;
    private int isLaundaryEditOrder;
    private WebView wvWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;

        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            headerString = getIntent().getStringExtra(HEADER_WEBVIEW);
            isTNC = getIntent().getBooleanExtra(IS_TNC, false);
            fromAppWalletScreen = getIntent().getBooleanExtra("fromAppWalletScreen", false);
            paymentMethodValue = getIntent().getLongExtra(VALUE_PAYMENT, 0);
            isLaundaryEditOrder = getIntent().getIntExtra(EXTRA_IS_LAUNDRY_EDIT_ORDER, 0);
        }


        Log.e("URL", url + "");

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerString != null ? headerString : "");

        wvWebsite = findViewById(R.id.wvWebsite);
        setWebViewProperties(wvWebsite);
        if (url != null)
            if (isTNC) {
                wvWebsite.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            } else {
                wvWebsite.loadUrl(url);
            }

    }

    private void setWebViewProperties(final WebView webView) {
        wvWebsite.setWebViewClient(new MyWebViewClient());
        wvWebsite.setWebChromeClient(new WebChromeClient());
        wvWebsite.getSettings().setLoadsImagesAutomatically(true);
        wvWebsite.getSettings().setJavaScriptEnabled(true);
        wvWebsite.getSettings().setDomStorageEnabled(true);
        wvWebsite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvWebsite.getSettings().setAllowFileAccessFromFileURLs(true);
        wvWebsite.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvWebsite.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvWebsite.getSettings().setAllowFileAccess(true);
        wvWebsite.getSettings().setAllowContentAccess(true);
        wvWebsite.getSettings().setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 21) {
            wvWebsite.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvWebsite.setWebChromeClient(new WebChromeClient() {
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
                wvWebsite.removeView(window);
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

            ProgressDialog.dismiss();
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
//        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
//        String transactionId = sanitizer.getValue("transactionId");

        if (url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url);
            Uri uri = Uri.parse(url);

            if (paymentMethodValue == BILLPLZ.intValue) {
                final UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
                String transactionId = sanitizer.getValue("billplz[id]");

//                String status = uri.getQueryParameter("billplz[paid]");
                if (transactionId != null && !fromAppWalletScreen) {

                    CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
                    commonParams.add("transaction_id", sanitizer.getValue("billplz[id]"));
                    commonParams.add("domain_name", StorefrontCommonData.getFormSettings().getDomainName());

                    commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
                    commonParams.build().getMap().remove(MARKETPLACE_REF_ID);

                    commonParams.add("isEditedTask", isLaundaryEditOrder);

                    RestClient.getApiInterface(mActivity).getBillPlzCharge(commonParams.build().getMap()).
                            enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                                @Override
                                public void success(BaseModel baseModel) {
                                    Bundle extras = new Bundle();
                                    extras.putBoolean("hitFetchMerchantCard", true);
                                    extras.putString("transactionId", sanitizer.getValue("billplz[id]"));
//                if (transactionId != null && !transactionId.isEmpty()) {
//                    extras.putString("transactionId", transactionId);
//                }
                                    Intent intent = new Intent();
                                    intent.putExtras(extras);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void failure(APIError error, BaseModel baseModel) {
                                    showErrorDialog(getStrings(R.string.transaction_failed));
                                }
                            });
                } else if (transactionId != null && fromAppWalletScreen) {
                    Bundle extras = new Bundle();
                    extras.putString("transactionId", sanitizer.getValue("billplz[id]"));

                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
//                else if (status != null && status.equalsIgnoreCase("false")) {
//                    showErrorDialog(getStrings(R.string.transaction_failed));
//                }
            } else {
                String status = uri.getQueryParameter("status");
                if (status != null && (status.equalsIgnoreCase(Codes.PaytmStatus.SUCCESS) || status.equalsIgnoreCase(Codes.PaytmStatus.PENDING))) {
                    showSuccessDialog(uri.getQueryParameter("response_message"));

                } else if (status != null && status.equalsIgnoreCase(Codes.PaytmStatus.FAILED)) {
                    showErrorDialog(uri.getQueryParameter("response_message"));
                }
            }
        }
        return true;
    }

    private void showSuccessDialog(String successMessage) {

        new AlertDialog.Builder(mActivity).message(successMessage).listener(new AlertDialog.Listener() {
            @Override
            public void performPostAlertAction(int purpose, Bundle backpack) {
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", true);
//                if (transactionId != null && !transactionId.isEmpty()) {
//                    extras.putString("transactionId", transactionId);
//                }
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
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }).build().show();
    }
}