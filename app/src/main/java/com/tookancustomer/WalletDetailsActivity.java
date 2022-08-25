package com.tookancustomer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.tookancustomer.adapters.WalletTransactionHistoryAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.WalletTransactionResponse.TxnHistory;
import com.tookancustomer.models.WalletTransactionResponse.WalletTransactionData;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY_UPI;

public class WalletDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //1: "ADD", 2: "DEDUCTION", 3: "REFUND", 4: "CREDIT", 5: "FAIL"
    String[] walletFilter = {"ALL", "PAID", "ADDED", "DEDUCTED",
            "REFUNDED", "CREDITED", "FAILED"};

    private TextView tvWalletBalanceLabel, tvWalletBalance, tvLatestTransactionsLabel;
    private TextView tvNoTransactions;
    private LinearLayout rlUnavailNet;
    private TextView tvRetry;
    private LinearLayout llLoadMoreView;
    private ShimmerFrameLayout shimmerLayout;
    private int typeWalletFilter = 0;
    private RecyclerView rvWalletTransactions;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<TxnHistory> transactionHistoryList = new ArrayList<>();
    private WalletTransactionHistoryAdapter adapter;
    private AppCompatSpinner spWalletFilter;
    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private int paginationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);
        mActivity = this;
        initViews();
        startShimmerAnimation(shimmerLayout);
        getWalletTransactions(0, 0);

    }

    private void initViews() {
        spWalletFilter = findViewById(R.id.spWalletFilter);
        spWalletFilter.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, walletFilter);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spWalletFilter.setAdapter(aa);

        tvWalletBalanceLabel = findViewById(R.id.tvWalletBalanceLabel);
        tvWalletBalanceLabel.setText(getStrings(R.string.wallet_balance).replace(WALLET, StorefrontCommonData.getTerminology().getWallet()));
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        tvWalletBalance.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getWalletBalance()))));

        tvLatestTransactionsLabel = findViewById(R.id.tvLatestTransactionsLabel);
        tvLatestTransactionsLabel.setText(getStrings(R.string.latest_transactions));

        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        rlUnavailNet.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tvNoNetConnect)).setText(getStrings(R.string.no_internet_connection));
        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setText(getStrings(R.string.retry_underlined));

        tvNoTransactions = findViewById(R.id.tvNoTransactions);
        tvNoTransactions.setText(getStrings(R.string.no_transactions_found));

        llLoadMoreView = findViewById(R.id.llLoadMoreView);

        shimmerLayout = findViewById(R.id.shimmerLayout);

        rvWalletTransactions = findViewById(R.id.rvWalletTransactions);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvWalletTransactions.setLayoutManager(mLayoutManager);
        adapter = new WalletTransactionHistoryAdapter(mActivity, transactionHistoryList);
        rvWalletTransactions.setAdapter(adapter);

        rvWalletTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    getWalletTransactions(transactionHistoryList.size(), typeWalletFilter);
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.rlAddWalletMoney), tvRetry);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.rlAddWalletMoney:
                PaymentManager.openAddWalletBalanceActivity(mActivity, 0);
                break;
            case R.id.tvRetry:
                startShimmerAnimation(shimmerLayout);
                getWalletTransactions(0, typeWalletFilter);
                break;
        }
    }

    private void getWalletTransactions(final int offset, final int type) {
        CommonParams.Builder commonParamsBuilder = new CommonParams.Builder();

        if (type != 0) {
            commonParamsBuilder.add("type", type);
        }


        commonParamsBuilder.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("length", 10)
                .add("start", offset);

        RestClient.getApiInterface(mActivity).getWalletTxnHistory(commonParamsBuilder.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                stopShimmerAnimation(shimmerLayout);
                llLoadMoreView.setVisibility(View.GONE);
                rlUnavailNet.setVisibility(View.GONE);
                showMoreLoading = true;

                WalletTransactionData walletTransactionData = new Gson().fromJson(new Gson().toJson(baseModel.data), WalletTransactionData.class);
                paginationCount = walletTransactionData.getCount();

                Dependencies.setWalletBalance(walletTransactionData.getWalletBalance());
                tvWalletBalance.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getWalletBalance()))));

                if (offset == 0) {
                    transactionHistoryList.clear();
                }
                transactionHistoryList.addAll(walletTransactionData.getTxnHistory());

//                if (walletTransactionData.getTxnHistory().size() == 0) {
//                    showMoreLoading = false; //Show more loading sets to false because no more transactions are there in the list.
//                } else {
//                    showMoreLoading = true;
//                }

                if (transactionHistoryList.size() > 0) {
                    rvWalletTransactions.getRecycledViewPool().clear();
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
            case Codes.Request.OPEN_WALLET_ADD_MONEY_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.hasExtra("ADD_MONEY_MESSAGE")) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra("ADD_MONEY_MESSAGE"));
                    }
                    if(data != null  && data.getLongExtra(VALUE_PAYMENT, 0) == RAZORPAY_UPI.intValue)
                        new Handler().postDelayed(() ->  getWalletTransactions(0, typeWalletFilter),1500);
                    else
                        getWalletTransactions(0, typeWalletFilter);

                } else {
                    if (data != null && data.hasExtra("ADD_MONEY_MESSAGE")) {
                        Utils.snackBar(mActivity, data.getStringExtra("ADD_MONEY_MESSAGE"), false);
                    }
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);

        if (position == 0) {
            getWalletTransactions(0, 0);
            typeWalletFilter = 0;
        } else if (position == 1) {
            getWalletTransactions(0, 2);
            typeWalletFilter = 2;
        } else if (position == 2) {
            getWalletTransactions(0, 1);
            typeWalletFilter = 1;

        } else if (position == 3) {
            getWalletTransactions(0, 8);
            typeWalletFilter = 8;

        } else if (position == 4) {
            getWalletTransactions(0, 3);
            typeWalletFilter = 3;
        } else if (position == 5) {
            getWalletTransactions(0, 4);
            typeWalletFilter = 4;
        } else if (position == 6) {
            getWalletTransactions(0, 5);
            typeWalletFilter = 5;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}