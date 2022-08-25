package com.tookancustomer;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import androidx.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class WebViewActivityPayuLatum extends BaseActivity implements Keys.Extras, View.OnClickListener {
    private String url = "", headerString = "";
    private WebView webView;
    private String backupUrl;


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
            webView.loadUrl(url);
        }

    }

    private boolean checkWritePermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return AppManager.getInstance().askUserToGrantPermission(mActivity, permissionsRequired, getStrings(R.string.please_grant_permission_to_storage), Codes.Permission.LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Codes.Permission.LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (url != null && !url.isEmpty()) {
//                webView.loadUrl(url);
//            }
            startDownloading();
        } else {

            Toast.makeText(mActivity, getStrings(R.string.please_grant_permission_to_storage) + " to make payment", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }


//    protected void onResume() {
//        super.onResume();
//        if (backupUrl != null && !backupUrl.isEmpty()) {
//            webView.loadUrl(backupUrl);
//        }
//    }


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

//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//        super.onBackPressed();
        Bundle extras = new Bundle();
        extras.putBoolean("hitFetchMerchantCard", false);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
//        }
    }


    private class MyWebViewClient extends WebViewClient implements DownloadListener {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.setDownloadListener(this);
            return onOverrideUrlLoading(view, request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.setDownloadListener(this);

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

            if (!url.contains(".pdf")) {
                ProgressDialog.show(mActivity);
                backupUrl = url;
                super.onPageStarted(view, url, favicon);
            }
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype,
                                    final long contentLength) {

            ProgressDialog.dismiss();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

//            if (!checkWritePermissions()) {
////                onBackPressed();
//                return;
//            }


//            startDownloading();
//
//            if (webView.canGoBack()) {
//                webView.goBack();
//            }
        }


    }

    private void startDownloading() {

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));

        request.allowScanningByMediaScanner();
        request.setTitle("cashReceipt");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "cashReceipt" + "." + "pdf");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG).show();
        ProgressDialog.dismiss();
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        Log.e("URL onOverrideUrlLoading", url + "");
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        String transactionId = sanitizer.getValue("transactionId");
        view.loadUrl(url);

        if (url.toLowerCase().contains("success")) {


            Bundle extras = new Bundle();
            extras.putBoolean("hitFetchMerchantCard", true);

            extras.putString("transactionId", transactionId);

            Intent intent = new Intent();
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();

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
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);

//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setSupportMultipleWindows(false);

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
