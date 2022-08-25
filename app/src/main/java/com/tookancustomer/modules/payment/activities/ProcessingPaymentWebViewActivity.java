package com.tookancustomer.modules.payment.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.tookancustomer.appdata.Keys.Prefs.EXTRA_REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.Prefs.IS_LOCK_ENABLED;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.BILLPLZ;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFAST;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.VPOS;
import static com.tookancustomer.utility.Utils.clearCookies;

/* Handled payment gateways for processing payment--> PAYPAL, RAZORPAY ,BILLPLZ, PAYFORT, PAYFAST, FAC, INSTAPAY */
public class ProcessingPaymentWebViewActivity extends BaseActivity implements View.OnClickListener, Keys.Extras, PaymentConstants {
    private WebView wvWebView;

    private String url = "";
    private int referenceId = 0;
    private long paymentMethod = 0L;
    private long paymentFor = 0L;
    private int isLaundaryEditOrder = 0, isLockEnabled = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        if (getIntent() != null) {
            url = getIntent().getStringExtra(URL_WEBVIEW);
            paymentMethod = getIntent().getLongExtra(VALUE_PAYMENT, 0);
            paymentFor = getIntent().getLongExtra(PAYMENT_FOR_FLOW, 0);
            isLockEnabled = getIntent().getIntExtra(IS_LOCK_ENABLED, 0);
            referenceId = getIntent().getIntExtra(EXTRA_REFERENCE_ID, 0);
            try {
                isLaundaryEditOrder = getIntent().getIntExtra(EXTRA_IS_LAUNDRY_EDIT_ORDER, 0);
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }
        }

        initViews();

        if (url != null) {
            Log.e("URL", url + "");
            wvWebView.clearCache(true);
            wvWebView.clearHistory();
            clearCookies(this);
            wvWebView.loadUrl(url.replace(".amp;", ""));
        }

       /* Bundle extras = new Bundle();
        extras.putString(TRANSACTION_ID, "");
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_PAYMENT_ERROR, intent);*/

        if (isLockEnabled == 1) {
            try {
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        apiJobExpired(referenceId, false);
                    }
                }, 300000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void apiJobExpired(int referenceId, final boolean fromBackButton) {
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("order_number", referenceId);
        RestClient.getApiInterface(this).setJobExpired(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        if (fromBackButton) {
                            Bundle extras = new Bundle();
                            Intent intent = new Intent();
                            intent.putExtras(extras);
                            setResult(RESULT_PAYMENT_ERROR, intent);
                            finish();
                        } else {
                            showErrorDialog(getStrings(R.string.payment_failed));
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {


                    }
                });
    }

    private void initViews() {
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.processing_payment));

        wvWebView = findViewById(R.id.wvWebsite);
        setWebViewProperties(wvWebView);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean ret = super.dispatchTouchEvent(event);
        View view = getCurrentFocus();
        try {
            if (view != null && view instanceof EditText) {
                View w = getCurrentFocus();
                int[] scrcoords = new int[2];
                assert w != null;
                w.getLocationOnScreen(scrcoords);
                float x = event.getRawX() + w.getLeft() - scrcoords[0];
                float y = event.getRawY() + w.getTop() - scrcoords[1];

                if (event.getAction() == MotionEvent.ACTION_UP
                        && (x < w.getLeft() || x >= w.getRight()
                        || y < w.getTop() || y > w.getBottom())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            } else {
//                if (!(this.getClass().getSimpleName().equalsIgnoreCase("WebviewActivity")))
//                    Utils.hideSoftKeyboard(this);
            }
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewProperties(final WebView webView) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                //return true or false after performing the URL request

//                wvWebsite.removeAllViews();
                WebView newView = new WebView(mActivity);
                newView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                if (paymentMethod == RAZORPAY.intValue)
                    webView.addView(newView);

                if (paymentMethod == RAZORPAY.intValue)
                    setWebViewProperties(newView); //Dialog update on same screen
                else setWebViewProperties(wvWebView); //Dialog update on different screen

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
                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (isLockEnabled == 1) {
            try {
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        apiJobExpired(referenceId, true);
                    }
                }, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent();
            intent.putExtras(new Bundle());
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        Log.e("URL", url + "");

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        String transactionId = "", jobPaymentDetailId = "";


        if (paymentMethod == RAZORPAY.intValue) {
            transactionId = sanitizer.getValue("rzp_payment_id");

        } else if (paymentMethod == BILLPLZ.intValue) {
            transactionId = sanitizer.getValue("billplz[id]");

            if (transactionId != null) {
                if (paymentFor == PaymentConstants.PaymentForFlow.ORDER_PAYMENT.intValue
                        || paymentFor == PaymentForFlow.REPAY_FROM_TASK_DETAILS.intValue) {
                    getBillPlzCharge(transactionId);
                } else {
                    Bundle extras = new Bundle();
                    extras.putString(TRANSACTION_ID, transactionId);
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            }
        } else if (paymentMethod == PAYFORT.intValue) {
            transactionId = sanitizer.getValue("transactionId");
            jobPaymentDetailId = sanitizer.getValue("job_payment_detail_id");

        } else {
            /* paymentMethod == PAYFAST || FAC || INSTAPAY || PAYPAL || PAYMOB  returns transactionId*/

            if (sanitizer.getValue("transactionId") != null) {
                transactionId = sanitizer.getValue("transactionId");
            } else {
                transactionId = sanitizer.getValue("transaction_id");
            }
        }

        if (url.startsWith("http:") || url.startsWith("https:")) {

            view.loadUrl(url.replace(".amp;", ""));

//            if (((paymentMethod == PAYMOB.intValue) ? (url.contains("success.html") || url.contains("Success.html")) : (url.contains("success") || url.contains("Success"))) && transactionId != null && !transactionId.isEmpty()) {
            if (url.contains("success.html")) {

                Bundle extras = new Bundle();
                if (paymentMethod == VPOS.intValue) {
                    extras.putString(TRANSACTION_ID, "12345");
                }
                if (transactionId != null && !transactionId.isEmpty()) {
                    extras.putString(TRANSACTION_ID, transactionId);
                }
                extras.putString(JOB_PAYMENT_DETAIL_ID, jobPaymentDetailId);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
//                finish();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
//            } else if ((paymentMethod == PAYMOB.intValue) ? url.contains("error.html") : url.contains("error")) {
            } else if (url.contains("error.html")) {

                if (isLockEnabled == 1) {

                    apiJobExpired(referenceId, false);
                } else {
                    Bundle extras = new Bundle();
                    if (transactionId != null && !transactionId.isEmpty()) {
                        extras.putString(TRANSACTION_ID, transactionId);
                    }
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_PAYMENT_ERROR, intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);
                }
            }
        }

        return true;
    }

    private void showErrorDialog(final String errorMessage) {
        new AlertDialog.Builder(mActivity).message(errorMessage).listener(new AlertDialog.Listener() {
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

    private void getBillPlzCharge(final String transactionId) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("domain_name", StorefrontCommonData.getFormSettings().getDomainName());
        commonParams.add("transaction_id", transactionId);
        commonParams.add("isEditedTask", isLaundaryEditOrder);

        commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
        commonParams.build().getMap().remove(MARKETPLACE_REF_ID);

        RestClient.getApiInterface(mActivity).getBillPlzCharge(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Bundle extras = new Bundle();
                        extras.putString(TRANSACTION_ID, transactionId);

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
}
