package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tookancustomer.adapters.GiftCardHistoryAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.giftCardTransactionResponse.GiftCardTransactionData;
import com.tookancustomer.models.giftCardTransactionResponse.TxnHistory;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY_UPI;

public class GiftCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvBuyGiftCardButton, tvRedeemGiftCardButton;

    private TextView tvLatestTransactionsLabel;
    private ShimmerFrameLayout shimmerLayout;
    private TextView tvNoTransactions;
    private LinearLayout rlUnavailNet;
    private TextView tvRetry;
    private LinearLayout llLoadMoreView;

    private RecyclerView rvGiftCardTransactions;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<TxnHistory> transactionHistoryList = new ArrayList<>();
    private GiftCardHistoryAdapter adapter;

    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private int paginationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);

        mActivity = this;
        initViews();
        startShimmerAnimation(shimmerLayout);
        getGiftCardTransactions(0);
    }

    private void initViews() {
        tvBuyGiftCardButton = findViewById(R.id.tvBuyGiftCardButton);
        tvBuyGiftCardButton.setText(StorefrontCommonData.getTerminology().getBuy());

        tvRedeemGiftCardButton = findViewById(R.id.tvRedeemGiftCardButton);
        tvRedeemGiftCardButton.setText(StorefrontCommonData.getTerminology().getRedeem());

        tvLatestTransactionsLabel = findViewById(R.id.tvLatestTransactionsLabel);
        tvLatestTransactionsLabel.setText(getStrings(R.string.recent_gift_card).replace(GIFT_CARD, StorefrontCommonData.getTerminology().getGiftCard()));

        shimmerLayout = findViewById(R.id.shimmerLayout);

        tvNoTransactions = findViewById(R.id.tvNoTransactions);
        tvNoTransactions.setText(getStrings(R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getGiftCard()));

        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        rlUnavailNet.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tvNoNetConnect)).setText(getStrings(R.string.no_internet_connection));
        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setText(getStrings(R.string.retry_underlined));

        llLoadMoreView = findViewById(R.id.llLoadMoreView);

        rvGiftCardTransactions = findViewById(R.id.rvGiftCardTransactions);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvGiftCardTransactions.setLayoutManager(mLayoutManager);
        adapter = new GiftCardHistoryAdapter(mActivity, transactionHistoryList);
        rvGiftCardTransactions.setAdapter(adapter);

        rvGiftCardTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int pastVisiblesItems, visibleItemCount, totalItemCount;

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (showMoreLoading && paginationCount > totalItemCount) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            showMoreLoading = false;
                            llLoadMoreView.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getGiftCardTransactions(transactionHistoryList.size());
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), tvRetry, tvBuyGiftCardButton, tvRedeemGiftCardButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.tvBuyGiftCardButton:
                Transition.transitForResult(mActivity, BuyGiftCardActivity.class, OPEN_GIFT_CARD_ACTIVITY, new Bundle());
                break;
            case R.id.tvRedeemGiftCardButton:
                Transition.transitForResult(mActivity, RedeemGiftCardActivity.class, OPEN_GIFT_CARD_ACTIVITY, new Bundle());
                break;
            case R.id.tvRetry:
                startShimmerAnimation(shimmerLayout);
                getGiftCardTransactions(0);
                break;
        }
    }

    private void getGiftCardTransactions(final int offset) {
        CommonParams.Builder commonParamsBuilder = new CommonParams.Builder();

        commonParamsBuilder.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(APP_ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("length", 10)
                .add("start", offset);

        RestClient.getApiInterface(mActivity).getGiftCardTxnHistory(commonParamsBuilder.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                stopShimmerAnimation(shimmerLayout);
                llLoadMoreView.setVisibility(View.GONE);
                rlUnavailNet.setVisibility(View.GONE);
                showMoreLoading = true;

                GiftCardTransactionData giftCardTransactionData = baseModel.toResponseModel(GiftCardTransactionData.class);
                paginationCount = giftCardTransactionData.getCount();

                if (offset == 0) {
                    transactionHistoryList.clear();
                }
                transactionHistoryList.addAll(giftCardTransactionData.getTxnHistory());

//
                if (transactionHistoryList.size() > 0) {
                    rvGiftCardTransactions.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    tvNoTransactions.setVisibility(View.GONE);
                } else {
                    tvNoTransactions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                showMoreLoading = true;
                stopShimmerAnimation(shimmerLayout);
                llLoadMoreView.setVisibility(View.GONE);
                if (transactionHistoryList.size() == 0) {
                    rlUnavailNet.setVisibility(View.VISIBLE);
                    tvNoTransactions.setVisibility(View.GONE);
                }
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_GIFT_CARD_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.hasExtra("GIFT_CARD_MESSAGE")) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra("GIFT_CARD_MESSAGE"));
                    }
                    if(data != null  && data.getLongExtra(VALUE_PAYMENT, 0) == RAZORPAY_UPI.intValue)
                        new Handler().postDelayed(() -> getGiftCardTransactions(0),2500);
                    else
                        getGiftCardTransactions(0);

                } else {
                    if (data != null && data.hasExtra("GIFT_CARD_MESSAGE")) {
                        Utils.snackBar(mActivity, data.getStringExtra("GIFT_CARD_MESSAGE"), false);
                    }
                }

                break;
        }
    }
}