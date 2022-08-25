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

import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.termsAndConditionsData.Data;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Codes.Request.RESULT_PAYMENT_ERROR;

public class WebViewActivity extends BaseActivity implements Keys.Extras, View.OnClickListener {
    private String url, headerString = "";
    private boolean isTNC, isHTML;
    private Activity mActivity;
    private int paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;

        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            headerString = getIntent().getStringExtra(HEADER_WEBVIEW);
            isTNC = getIntent().getBooleanExtra(IS_TNC, false);
            isHTML = getIntent().getBooleanExtra(IS_HTML, false);
            paymentMethod = getIntent().getIntExtra(PAYMENT_METHOD_DATA, 0);
        }


        Log.e("URL", url + "");

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerString != null ? headerString : "");

        final WebView wvWebsite = findViewById(R.id.wvWebsite);
        wvWebsite.setWebViewClient(new MyWebViewClient());
        wvWebsite.setWebChromeClient(new WebChromeClient());
        wvWebsite.getSettings().setLoadsImagesAutomatically(true);
        wvWebsite.getSettings().setJavaScriptEnabled(true);
        wvWebsite.getSettings().setDomStorageEnabled(true);
        wvWebsite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvWebsite.getSettings().setDomStorageEnabled(true);
        wvWebsite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        wvWebsite.getSettings().setAllowFileAccessFromFileURLs(true);
//        wvWebsite.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        wvWebsite.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        wvWebsite.getSettings().setAllowFileAccess(true);
//        wvWebsite.getSettings().setAllowContentAccess(true);
//        wvWebsite.getSettings().setSupportMultipleWindows(true);
//        if (Build.VERSION.SDK_INT >= 21) {
//            wvWebsite.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
//        }

//                if (url != null)
//            if (isTNC) {
//                wvWebsite.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
//            } else {
//                wvWebsite.loadUrl(url);
//            }

        if (url != null && !url.isEmpty()) {
            if (isTNC || isHTML) {
                wvWebsite.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            } else {
                wvWebsite.loadUrl(url);
            }
        } else {
            CommonParams.Builder builder = new CommonParams.Builder()
                    .add("marketplace_reference_id", Dependencies.getMarketplaceReferenceId());

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            } else {
                builder.add("language", "en");
            }

            RestClient.getApiInterface(this).getTermsAndCondition(builder.build().getMap())
                    .enqueue(new ResponseResolver<BaseModel>(this, true, false) {
                        @Override
                        public void success(BaseModel baseModel) {
                            Log.e("success", "=====");
                            Data data = baseModel.toResponseModel(Data.class);
                            if (data.getTncType() == 0)//0 for template data else 1 for user link
                                wvWebsite.loadData(data.getTemplateData(), "text/html", "UTF-8");
                            else
                                wvWebsite.loadUrl(data.getTncUserLink());

                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {

                        }
                    });
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
            //TODO

            String transactionId;

            if (sanitizer.getValue("transactionId") != null) {
                transactionId = sanitizer.getValue("transactionId");
            } else {
                transactionId = sanitizer.getValue("transaction_id");

            }

//            String transactionId = sanitizer.getValue("transactionId");
//            String transactionId = sanitizer.getValue("transaction_id");

            ProgressDialog.dismiss();
            Log.e("URL", url + "");
            if (url.startsWith("http:") || url.startsWith("https:")) {
                if ((url.contains("success") || url.contains("Success"))) {
//                    if (!(paymentMethodValue == Constants.PaymentValue.PAYFORT.intValue)
//                            || (transactionId != null && !transactionId.isEmpty())) {

                    Bundle extras = new Bundle();
                    extras.putBoolean("hitFetchMerchantCard", true);


                    if (transactionId != null && !transactionId.isEmpty()) {
                        extras.putString("transactionId", transactionId);
                    }
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();

//                    }
                } else if ((url.contains("success.html") || url.contains("Success.html"))) {
                    Bundle extras = new Bundle();
                    extras.putBoolean("hitFetchMerchantCard", true);


                    if (transactionId != null && !transactionId.isEmpty()) {
                        extras.putString("transactionId", transactionId);
                    }
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
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

        if (url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url);
            if (url.contains("success")) {

//                Bundle extras = new Bundle();
//                extras.putBoolean("hitFetchMerchantCard", true);
//                if (transactionId != null && !transactionId.isEmpty()) {
//                    extras.putString("transactionId", transactionId);
//                }
//                Intent intent = new Intent();
//                intent.putExtras(extras);
//                setBanner(RESULT_OK, intent);
//                finish();
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