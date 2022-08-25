package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tookancustomer.appdata.Keys;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.net.URISyntaxException;

public class WebViewTrackingActivity extends BaseActivity implements Keys.Extras, View.OnClickListener {
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
                Bundle extras = new Bundle();
                extras.putBoolean("hitFetchMerchantCard", false);
                Intent intent = new Intent();
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            ProgressDialog.dismiss();
            Log.e("URL", url + "");
            if (url.startsWith("http:") || url.startsWith("https:")) {
                if (url.toLowerCase().contains("success")) {
                    Bundle extras = new Bundle();
                    extras.putBoolean("hitFetchMerchantCard", true);
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (url.toLowerCase().contains("error")) {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.card_cannot_be_added)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            Bundle extras = new Bundle();
                            extras.putBoolean("hitFetchMerchantCard", false);
                            Intent intent = new Intent();
                            intent.putExtras(extras);
                            setResult(RESULT_OK, intent);
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
        if (url.startsWith("http")) return false;//open web links as usual
        //try to find browse activity to handle uri
        Uri parsedUri = Uri.parse(url);
        PackageManager packageManager = getPackageManager();
        Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
        if (browseIntent.resolveActivity(packageManager) != null) {
            startActivity(browseIntent);
            return true;
        }
        //if not activity found, try to parse intent://
        if (url.startsWith("intent:")) {
            try {

                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                //try to find fallback url
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null) {
                    view.loadUrl(fallbackUrl);
                    return true;
                }
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    return true;
                }
                //invite to install
                Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse("market://details?id=" + intent.getPackage()));
                if (marketIntent.resolveActivity(packageManager) != null) {
                    startActivity(marketIntent);
                    return true;
                }
            } catch (URISyntaxException e) {
                //not an intent uri
            }
        }
        return true;//do nothing in other cases

    }
}