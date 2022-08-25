package com.tookancustomer;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

public class ReferActivity extends BaseActivity implements OnClickListener {

    TextView tvReferalCode, tvHowItWorks, tvReferMessage, tvHeading;
    RelativeLayout rlBack;
    ImageButton ibTwitter, ibWhatsapp, ibFacebook, ibMore;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private String referralCode = "";
    private String urlToShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        mActivity = this;

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        ibTwitter = findViewById(R.id.ibTwitter);
        ibWhatsapp = findViewById(R.id.ibWhatsapp);
        ibFacebook = findViewById(R.id.ibFacebook);
        ibMore = findViewById(R.id.ibMore);
        tvReferalCode = findViewById(R.id.tvReferalCode);
        tvHowItWorks = findViewById(R.id.tvHowItWorks);
        tvHowItWorks.setText(getStrings(R.string.how_it_works));
        ((TextView) findViewById(R.id.tvTapBoxMessage)).setText(getStrings(R.string.tap_the_box_to_copy_the_referral_code));
        ((TextView) findViewById(R.id.tvShareOn)).setText(getStrings(R.string.share_on));
        tvReferMessage = findViewById(R.id.tvReferMessage);
        tvHeading = findViewById(R.id.tvHeading);
        rlBack = findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        ibTwitter.setOnClickListener(this);
        ibWhatsapp.setOnClickListener(this);
        ibFacebook.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        tvReferalCode.setOnClickListener(this);
        tvHowItWorks.setOnClickListener(this);


        referralCode = StorefrontCommonData.getUserData().getData().getVendorDetails().getReferralCode();
        setData();
        if (StorefrontCommonData.getUserData().getData().getReferral().getSmarturlEnabled() == 1) {
            urlToShare = StorefrontCommonData.getUserData().getData().getReferral().getSmartUrl();
        } else {

            urlToShare = "https://" + StorefrontCommonData.getAppConfigurationData().getDomainName()
                    + "/" + StorefrontCommonData.getSelectedLanguageCode().getLanguageCode()
                    + "/" + "share/" + StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId()
                    + "/" + StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId();
        }

    }


    public void setData() {
        tvHeading.setText(getStrings(R.string.referral));
        if (referralCode != null && !referralCode.isEmpty())
            tvReferalCode.setText(referralCode);
        tvReferMessage.setText(StorefrontCommonData.getUserData().getData().getReferral().getSenderDescription());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ibTwitter:
                shareRefferalCodeViaTwitter();
                break;
            case R.id.ibWhatsapp:
                shareRefferalCodeViaWhatsApp();
                break;
            case R.id.ibFacebook:
                shareRefferalCodeViaFacebook();
                break;
            case R.id.ibMore:
                shareRefferalCode();
                break;
            case R.id.tvReferalCode:
                copyReferralCode();
                break;
            case R.id.tvHowItWorks:
                //   howItWorks();
                break;
        }
    }

    public void copyReferralCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("referral", tvReferalCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Utils.snackbarSuccess(mActivity, getStrings(R.string.referral_code_copied_successfully));
    }

    public void shareRefferalCode() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, StorefrontCommonData.getUserData().getData().getReferral().getReceiverDescription() + " " + urlToShare);
        sendIntent.setType("text/plain");
        this.startActivity(sendIntent);
    }

    public void shareRefferalCodeViaTwitter() {
        if (referralCode != null && !referralCode.isEmpty()) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.twitter.android");
                intent.putExtra(Intent.EXTRA_TEXT, StorefrontCommonData.getUserData().getData().getReferral().getReceiverDescription());
                this.startActivity(intent);
            } catch (ActivityNotFoundException anfe) {
                Intent intentPayment = new Intent(this, WebViewActivity.class);
                intentPayment.putExtra(URL_WEBVIEW, "https://twitter.com/login");
                startActivity(intentPayment);
                AnimationUtils.forwardTransition(mActivity);
            }
        } else {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.sorry_you_dont_have_referral_code)).build().show();
//            Utils.snackBar(mActivity, getStrings(R.string.sorry_you_dont_have_referral_code));
        }
    }

    public void shareRefferalCodeViaWhatsApp() {
        if (referralCode != null && !referralCode.isEmpty()) {
            try {


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT,
                        StorefrontCommonData.getUserData().getData().getReferral().getReceiverDescription()
                                + " " + urlToShare
                );
                this.startActivity(intent);
            } catch (ActivityNotFoundException anfe) {
                new AlertDialog.Builder(mActivity).message(getStrings(R.string.whatsapp_not_found_in_your_phone)).build().show();
//                Utils.snackbar(mActivity, getStrings(R.string.whatsapp_not_found_in_your_phone));
            }
        } else {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.sorry_you_dont_have_referral_code)).build().show();
//            Utils.snackbar(mActivity, getStrings(R.string.sorry_you_dont_have_referral_code));
        }
    }

    public void shareRefferalCodeViaFacebook() {

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
////        intent.putExtra(Intent.EXTRA_SUBJECT, userData.getData().getReferral().getReceiverDescription()); // NB: has no effect!
//        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
//
//// See if official Facebook app is found
//        boolean facebookAppFound = false;
//        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
//        for (ResolveInfo info : matches) {
//            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
//                intent.setPackage(info.activityInfo.packageName);
//                facebookAppFound = true;
//                break;
//            }
//        }
//
//// As fallback, launch sharer.php in a browser
//        if (!facebookAppFound) {
//            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
//            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
//        }
//
//        startActivity(intent);


        if (referralCode != null && !referralCode.isEmpty()) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(StorefrontCommonData.getUserData().getData().getReferral().getReceiverDescription())
                        .setContentUrl(Uri.parse(urlToShare))
                        .build();
                shareDialog.show(linkContent);
            }
        } else {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.sorry_you_dont_have_referral_code)).build().show();
        }
    }

    @Override
    public void onBackPressed() {
        Transition.exit(this);
    }
}