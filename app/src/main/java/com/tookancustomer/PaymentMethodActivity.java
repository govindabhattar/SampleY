package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tookancustomer.adapters.PaymentListAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.callback.PaytmCheckBalanceCallback;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.paymentMethodData.PaytmData;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.modules.payment.callbacks.FetchCardsListener;
import com.tookancustomer.modules.payment.callbacks.PaytmBalanceListener;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;


public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener, Keys.Extras, Keys.APIFieldKeys, PaytmCheckBalanceCallback {
    private LinearLayout rlUnavailNet;
    private TextView tvNoNetConnect, tvHeading;

    private LinearLayout rlAvailNet;

    private RecyclerView rvPaymentCardList;
    private PaymentListAdapter paymentListAdapter;
    private List<Datum> paymentList = new ArrayList<>();
    private TextView tvNoPaymentCardFound;
    private TextView ivAddPaymentOptions;
    private long valuePaymentMethod = 0;

    private Integer paytmVerified = null;
    private Double paytmWalletAmount = 0.0;

    private String paytmAddMoneyUrl;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        mActivity = this;

        valuePaymentMethod = PaymentMethodsClass.getEnabledPaymentMethod();
        initializeData();
        fetchMerchantCards();
    }

    private void initializeData() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getTerminology().getPayment(true));

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        rvPaymentCardList = findViewById(R.id.rvPaymentCardList);
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(this));
        rvPaymentCardList.setNestedScrollingEnabled(false);
        rvPaymentCardList.setVisibility(View.GONE);
        tvNoPaymentCardFound = findViewById(R.id.tvNoPaymentCardFound);
        tvNoPaymentCardFound.setText(getStrings(R.string.no_payment_card_found));
        tvNoPaymentCardFound.setVisibility(View.VISIBLE);
        ivAddPaymentOptions = findViewById(R.id.ivAddPaymentOptions);
        ivAddPaymentOptions.setText(getStrings(R.string.add_card));

        rlAvailNet = findViewById(R.id.rlAvailNet);
        rlAvailNet.setVisibility(View.VISIBLE);
        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        rlUnavailNet.setVisibility(View.GONE);
        tvNoNetConnect = findViewById(R.id.tvNoNetConnect);
        tvNoNetConnect.setText(getStrings(R.string.no_internet_connection));
        ((TextView) findViewById(R.id.tvRetry)).setText(getStrings(R.string.retry_underlined));
        ((TextView) findViewById(R.id.tvPaymentMethodLabel)).setVisibility(View.GONE);


        paymentListAdapter = new PaymentListAdapter(this, paymentList);
        rvPaymentCardList.setAdapter(paymentListAdapter);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), ivAddPaymentOptions, findViewById(R.id.tvRetry));
    }

    @Override
    protected void onResume() {
        super.onResume();
        paytmVerified = null;
        if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
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

            case R.id.tvRetry:
                fetchMerchantCards();
                if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
                break;

            case R.id.ivAddPaymentOptions:
                PaymentManager.openAddPaymentCardWebViewActivity(mActivity, valuePaymentMethod);
                break;
        }
    }

    public void paytmCheckBalance() {
        PaymentManager.getPaytmBalance(mActivity, false, new PaytmBalanceListener() {
            @Override
            public void onPaytmBalanceSuccess(PaytmData paytmData) {
                paytmVerified = paytmData.getPaytmVerified();
                paytmWalletAmount = paytmData.getWalletBalance();
                paytmAddMoneyUrl = paytmData.getPaytmAddMoneyUrl();

                setPaymentAdapter();
            }

            @Override
            public void onPaytmBalanceFailure() {

            }
        });
    }

    private void fetchMerchantCards() {
        if (!PaymentMethodsClass.isCardPaymentEnabled()) {
            paymentList.clear();
            if (PaymentMethodsClass.isPaytmEnabled()) {
                Datum datum = new Datum(PAYTM.intValue, getStrings(R.string.paytm), PAYTM.intValue, Constants.PAYMENT_MODES.paytm);
                paymentList.add(datum);
            }
            setPaymentAdapter();
            return;
        }

        startShimmerAnimation(shimmerFrameLayout);
        PaymentManager.fetchMerchantCards(mActivity, false, valuePaymentMethod, new FetchCardsListener() {
            @Override
            public void onFetchCardsSuccess(List<Datum> paymentCardList) {
                rlUnavailNet.setVisibility(View.GONE);
                rlAvailNet.setVisibility(View.VISIBLE);

                paymentList.clear();
                if (PaymentMethodsClass.isPaytmEnabled()) {
                    Datum datum = new Datum(PAYTM.intValue, getStrings(R.string.paytm), PAYTM.intValue, Constants.PAYMENT_MODES.paytm);
                    paymentList.add(datum);
                }
                paymentList.addAll(paymentCardList);
                stopShimmerAnimation(shimmerFrameLayout);
                setPaymentAdapter();
            }

            @Override
            public void onFetchCardsFailure() {
                stopShimmerAnimation(shimmerFrameLayout);
                rlUnavailNet.setVisibility(View.VISIBLE);
                rlAvailNet.setVisibility(View.GONE);
                tvNoNetConnect.setText(Utils.internetCheck(mActivity) ? getStrings(R.string.something_went_wrong) : getStrings(R.string.no_internet_connection));
            }
        });
    }

    public void setPaymentAdapter() {
        ivAddPaymentOptions.setVisibility(PaymentMethodsClass.isCardPaymentEnabled() ? View.VISIBLE : View.GONE);
        setPaymentListViews();

        paymentListAdapter = new PaymentListAdapter(mActivity, paymentList, paytmWalletAmount, paytmVerified);
        rvPaymentCardList.setAdapter(paymentListAdapter);
    }

    public void setPaymentListViews() {
        if (paymentList.size() == 0) {
            rvPaymentCardList.setVisibility(View.GONE);
            tvNoPaymentCardFound.setVisibility(View.VISIBLE);
        } else {
            rvPaymentCardList.setVisibility(View.VISIBLE);
            tvNoPaymentCardFound.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY:
                /* RESULT_CANCELLED  and RESULT_FAILED
                 * not handled in add payment card activity
                 * Fetch merchant cards only happened in case of success
                 * */
                if (resultCode == RESULT_OK) {
                    fetchMerchantCards();
                }
                break;
            case Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    //code here
                }
                break;
        }
    }

    @Override
    public void onPaytmOptionClicked() {
        if (PaymentMethodsClass.isPaytmEnabled()) {
            PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
        }
    }
}