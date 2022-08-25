package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tookancustomer.adapters.GiftCardPaymentListAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.callback.PaytmCheckBalanceCallback;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.paymentMethodData.PaytmData;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.modules.payment.PaymentTransactionUrl;
import com.tookancustomer.modules.payment.PaymentTransactionUrlManager;
import com.tookancustomer.modules.payment.callbacks.FetchCardsListener;
import com.tookancustomer.modules.payment.callbacks.PaytmBalanceListener;
import com.tookancustomer.modules.payment.callbacks.TransactionUrlListener;
import com.tookancustomer.modules.payment.callbacks.WalletBalanceListener;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
 * Currently we are supporting
 * STRIPE, PAYFORT, PAYPAL and in app wallet for gift card
 * */
public class GiftCardPaymentActivity extends BaseActivity implements View.OnClickListener, PaytmCheckBalanceCallback, TransactionUrlListener, PaymentResultWithDataListener {
    public boolean isCardSelected;
    public Integer adapterPos;
    public String selectedCardId;
    public double giftCardAmount = 0;
    public HashMap<String, String> giftCardData = new HashMap<>();
    private TextView tvHeading, btnBuyGiftCard;
    private TextView tvPaymentMethodLabel, tvNoPaymentCardFound, ivAddPaymentOptions;
    private RecyclerView rvPaymentCardList;
    private List<Datum> paymentList = new ArrayList<>();
    private GiftCardPaymentListAdapter mAdapter;
    private long valuePaymentMethod = 0;
    private Integer paytmVerified = null;
    private String paytmAddMoneyUrl;
    private Double paytmWalletAmount = 0.0;
    private long selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_payment);
        mActivity = this;
        getpaymentmethods();


    }

    private void getpaymentmethods() {
        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        paymenthashMap.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        paymenthashMap.put(LANGUAGE, StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());

        RestClient.getApiInterface(mActivity).getPaymentMethods(paymenthashMap).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Log.e("payment......", "payment");


                ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentMethods = new Gson().fromJson(new Gson().toJson(baseModel.data), new TypeToken<ArrayList<PaymentMethod>>() {
                }.getType());
                Log.e("payment......", "payment");
                PaymentMethodsClass.clearPaymentHashMaps();
                StorefrontCommonData.getFormSettings().setPaymentMethods(paymentMethods);
                setdata();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void setdata() {
        valuePaymentMethod = PaymentMethodsClass.getEnabledPaymentMethod();
        giftCardAmount = getIntent().getExtras().getDouble("GIFT_CARD_AMOUNT", 0);
        giftCardData = (HashMap<String, String>) getIntent().getExtras().getSerializable("GIFT_CARD_DATA");
        initViews();
        fetchMerchantCards();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
        if (PaymentMethodsClass.isInAppWalletPaymentEnabled()) getWalletBalance();
    }

    public void initViews() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getTerminology().getPayment());

        btnBuyGiftCard = findViewById(R.id.btnBuyGiftCard);
        btnBuyGiftCard.setText(StorefrontCommonData.getTerminology().getBuy());

        tvPaymentMethodLabel = findViewById(R.id.tvPaymentMethodLabel);
        tvPaymentMethodLabel.setText(StorefrontCommonData.getTerminology().getPaymentMethod());
        tvNoPaymentCardFound = findViewById(R.id.tvNoPaymentCardFound);
        tvNoPaymentCardFound.setText(getStrings(R.string.no_payment_card_found));
        ivAddPaymentOptions = findViewById(R.id.ivAddPaymentOptions);
        ivAddPaymentOptions.setText(getStrings(R.string.add_card));

        rvPaymentCardList = findViewById(R.id.rvPaymentCardList);
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvPaymentCardList.setNestedScrollingEnabled(false);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), btnBuyGiftCard, ivAddPaymentOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;

            case R.id.btnBuyGiftCard:
                if (validateAddMoney()) {
                    if (paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.STRIPE.intValue ||
                            paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.VISTA_MONEY.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.FAC.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.AZUL.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.AUTHORISE_DOT_NET.intValue) {
                        buyGiftCard(paymentList.get(adapterPos).getCardId());
                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.PAYTM.intValue)) {
                        if (paytmVerified != null) {
                            if (paytmVerified == 0) {
                                Utils.snackBar(mActivity, getStrings(R.string.paytm_account_not_linked));
                            } else if (getAmountToBeAdded() > paytmWalletAmount) {
                                PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
                            } else {
                                buyGiftCard("");
                            }
                        } else {
                            Utils.snackBar(this, getString(R.string.error_paytm_money_load));
                        }
                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.INAPP_WALLET.intValue)) {
                        if (getAmountToBeAdded() > (Dependencies.getWalletBalance())) {
                            PaymentManager.openAddWalletBalanceActivity(mActivity, getAmountToBeAdded());
                        } else {
                            buyGiftCard("");
                        }
                    } else {
                        HashMap<String, String> map = PaymentTransactionUrlManager.getMap(mActivity, paymentList.get(adapterPos).getPaymentMethod(),
                                paymentList.get(adapterPos), getAmountToBeAdded() + ""
                        );

                        if (paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.PAYFORT.intValue) {
                            map.put("vendor_card_id", paymentList.get(adapterPos).getCardId());
                        }

                        if (getPaymentProcessType() == 2) {
                            new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                                    .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                                    .setPaymentForFlow(PaymentConstants.PaymentForFlow.GIFT_CARD.intValue)
                                    .setMap(map)
                                    .setUserId(StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                                    .setCreateTaskData(getGiftCardHashmap("", false).build().getMap())
                                    .setPaymentData(paymentList.get(adapterPos))
                                    .setListener(this)
                                    .build();


                        } else {

                            new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                                    .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                                    .setPaymentForFlow(PaymentConstants.PaymentForFlow.GIFT_CARD.intValue)
                                    .setMap(map)
                                    .setUserId(StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                                    .setPaymentData(paymentList.get(adapterPos))
                                    .setListener(this)
                                    .build();
                        }
                    }
                }
                break;

            case R.id.ivAddPaymentOptions:
                PaymentManager.openAddPaymentCardWebViewActivity(mActivity, valuePaymentMethod);
                break;
        }
    }


    private int getPaymentProcessType() {
        int paymentProcessType = 0;
        for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
            if (StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getValue() == paymentList.get(adapterPos).getPaymentMethod()) {
                paymentProcessType = StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getPaymentProcessType();
                break;
            }
        }
        return paymentProcessType;
    }

    private boolean validateAddMoney() {
        if (!isCardSelected || (adapterPos == null)) {
            if (tvNoPaymentCardFound.getVisibility() == View.GONE) {
                Utils.snackBar(mActivity, getStrings(R.string.please_select_paymentOption).replace(PAYMENT, StorefrontCommonData.getTerminology().getPayment(false)));
            } else {
                Utils.snackBar(mActivity, getStrings(R.string.please_add_paymentOption_first).replace(PAYMENT, StorefrontCommonData.getTerminology().getPayment(false)));
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    fetchMerchantCards();
                }
                break;

            case Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
                }
                break;

            case Codes.Request.OPEN_PROCESSING_PAYMENT_ACTIVITY:
                if (data.getStringExtra(MODE_PAYMENT) != null && data.getStringExtra(MODE_PAYMENT).equals("stripe3ds")) {
                    onTransactionSuccess(paymentList.get(adapterPos).getCardId(), "stripe3ds");
                } else {
                    PaymentTransactionUrlManager.getInstance().onActivityResult(requestCode, resultCode, data);
                }

                break;

            case Codes.Request.OPEN_WALLET_ADD_MONEY_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if ((Dependencies.getWalletBalance() >= getAmountToBeAdded())) {
                        btnBuyGiftCard.performClick();
                    }
                }
                break;
        }
    }

    private void buyGiftCard(String transactionId) {
        buyGiftCard(transactionId, false);
    }

    private void buyGiftCard(String transactionId, boolean isFailedTransaction) {

        CommonParams.Builder commonParams = getGiftCardHashmap(transactionId, isFailedTransaction);
        commonParams.build().getMap().remove("fac_payment_flow");


        RestClient.getApiInterface(mActivity).createCharge(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Bundle bundleExtra = new Bundle();
                bundleExtra.putString("GIFT_CARD_MESSAGE", baseModel.getMessage());

//                Dependencies.setWalletBalance(baseModel.toResponseModel(AddWalletBalanceResponseData.class).getWalletBalanceAfterTxn());

                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundleExtra);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }


    private CommonParams.Builder getGiftCardHashmap(String transactionId, boolean isFailedTransaction) {
        selectedPaymentMethod = paymentList.get(adapterPos).getPaymentMethod();

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add("payment_method", selectedPaymentMethod)
                .add("amount", getAmountToBeAdded())
                .add("payment_for", PaymentConstants.PaymentForFlow.GIFT_CARD.intValue);

        if (isFailedTransaction) commonParams.add("is_transaction_failed", 1);

        if (selectedPaymentMethod == PaymentConstants.PaymentValue.STRIPE.intValue
                || selectedPaymentMethod == PaymentConstants.PaymentValue.VISTA_MONEY.intValue
                || selectedPaymentMethod == PaymentConstants.PaymentValue.FAC.intValue
                || selectedPaymentMethod == PaymentConstants.PaymentValue.AZUL.intValue
                || selectedPaymentMethod == PaymentConstants.PaymentValue.AUTHORISE_DOT_NET.intValue) {
            commonParams.add("card_id", transactionId);
        } else if (selectedPaymentMethod == PaymentConstants.PaymentValue.PAYTM.intValue
                || selectedPaymentMethod == PaymentConstants.PaymentValue.INAPP_WALLET.intValue) {
            //PAYTM need not send any key for add wallet
        } else {
            commonParams.add("transaction_id", transactionId);
        }

        commonParams.add("sender_name", giftCardData.get("sender_name"))
                .add("receiver_name", giftCardData.get("receiver_name"))
                .add("receiver_email", giftCardData.get("receiver_email"))
                .add("message", giftCardData.get("message"));

        return commonParams;

    }

    private void paytmCheckBalance() {
        PaymentManager.getPaytmBalance(mActivity, false, new PaytmBalanceListener() {
            @Override
            public void onPaytmBalanceSuccess(PaytmData paytmData) {
                paytmVerified = paytmData.getPaytmVerified();
                paytmWalletAmount = paytmData.getWalletBalance();
                paytmAddMoneyUrl = paytmData.getPaytmAddMoneyUrl();
                setPaymentAdapter(paymentList);
            }

            @Override
            public void onPaytmBalanceFailure() {

            }
        });
    }

    private void fetchMerchantCards() {
        if (!PaymentMethodsClass.isCardPaymentEnabled()) {
            paymentList.clear();
//            PaymentMethodsClass.getPaymentList(mActivity, paymentList, false, false);
            addCashAndWalletAndRazorPay();
            for (int i = 0; i < paymentList.size(); i++) {
                if (adapterPos != null && adapterPos == i) {
                    paymentList.get(adapterPos).selectedCard = true;
                } else {
                    paymentList.get(i).selectedCard = false;
                }
            }

            setPaymentAdapter(paymentList);
            return;
        }

        PaymentManager.fetchMerchantCards(mActivity, true, valuePaymentMethod, new FetchCardsListener() {
            @Override
            public void onFetchCardsSuccess(List<Datum> paymentCardList) {
                paymentList = new ArrayList<Datum>();
//                        PaymentMethodsClass.getPaymentList(mActivity, paymentList, false, false);
                addCashAndWalletAndRazorPay();
                paymentList.addAll(paymentCardList);

                for (int i = 0; i < paymentList.size(); i++) {
                    if (adapterPos != null && adapterPos == i) {
                        paymentList.get(adapterPos).selectedCard = true;
                    } else {
                        paymentList.get(i).selectedCard = false;
                    }
                }
                setPaymentAdapter(paymentList);
            }

            @Override
            public void onFetchCardsFailure() {
                paymentList = new ArrayList<Datum>();
//                        PaymentMethodsClass.getPaymentList(mActivity, paymentList, false, false);
                addCashAndWalletAndRazorPay();

                for (int i = 0; i < paymentList.size(); i++) {
                    if (adapterPos != null && adapterPos == i) {
                        paymentList.get(adapterPos).selectedCard = true;
                    } else {
                        paymentList.get(i).selectedCard = false;
                    }
                }
                setPaymentAdapter(paymentList);
            }
        });
    }

    public void setPaymentAdapter(List<Datum> paymentList) {
        if (paymentList.size() > 0) {
            rvPaymentCardList.setVisibility(View.VISIBLE);
            tvNoPaymentCardFound.setVisibility(View.GONE);

            Iterator<Datum> it = paymentList.iterator();
            while (it.hasNext()) {
                Datum paay = it.next();
                if (paay.getPaymentFlowType() == Constants.PAYMENT_MODES.CASHMETHOD)
                    it.remove();
            }

            mAdapter = new GiftCardPaymentListAdapter(mActivity, paymentList, paytmWalletAmount, paytmVerified);
            rvPaymentCardList.setAdapter(mAdapter);

            for (int i = 0; i < paymentList.size(); i++) {
                if (StorefrontCommonData.getLastPaymentMethod() != 0 && selectedCardId == null) {
                    if (paymentList.get(i).getPaymentMethod() == StorefrontCommonData.getLastPaymentMethod()) {
                        paymentList.get(i).selectedCard = true;
                        adapterPos = i;
                        isCardSelected = true;
                        break;
                    }
                    if (i == paymentList.size() - 1 && !isCardSelected) {/*If after traversing the whole list, payment is still not selected, then by default select first one*/
                        paymentList.get(0).selectedCard = true;
                        adapterPos = 0;
                        isCardSelected = true;
                    }
                } else {
                    if (selectedCardId == null) {
//                    paymentList.get(paymentList.size() - 1).selectedCard = true;
//                    adapterPos = paymentList.size() - 1;
                        paymentList.get(0).selectedCard = true;
                        adapterPos = 0;
                        isCardSelected = true;
                        break;
                    }
                    if (selectedCardId.equals(paymentList.get(i).getCardId())) {
                        paymentList.get(i).selectedCard = true;
                        adapterPos = i;
                        isCardSelected = true;
                    } else {
                        paymentList.get(i).selectedCard = false;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();

        } else {
            rvPaymentCardList.setVisibility(View.GONE);
            tvNoPaymentCardFound.setVisibility(View.VISIBLE);
        }

        ivAddPaymentOptions.setVisibility(PaymentMethodsClass.isCardPaymentEnabled() ? View.VISIBLE : View.GONE);
    }

    public void openAddPaytmMoneyWebview() {
        PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
    }

    @Override
    public void onPaytmOptionClicked() {

    }

    public void addCashAndWalletAndRazorPay() {

        List<PaymentMethod> paymentMethods = StorefrontCommonData.getFormSettings().getPaymentMethods();


        for (int i = 0; i < paymentMethods.size(); i++) {
            if (PaymentMethodsClass.getPaymentMethodsMap().containsKey(paymentMethods.get(i).getValue())
                    && paymentMethods.get(i).getPaymentMode() != Constants.PAYMENT_MODES.CARD) {
                Datum datum = new Datum(paymentMethods.get(i).getValue(),
                        paymentMethods.get(i).getLabel(),
                        paymentMethods.get(i).getValue(),
                        paymentMethods.get(i).getPaymentMode());
                paymentList.add(datum);
            }
        }

    }

    private void getWalletBalance() {
        PaymentManager.getWalletBalance(mActivity, false, new WalletBalanceListener() {
            @Override
            public void onWalletBalanceSuccess() {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onWalletBalanceFailure() {

            }
        });
    }

    public double getAmountToBeAdded() {
        return giftCardAmount;
    }

    @Override
    public void onTransactionSuccess(String transactionId, String jobPaymentDetailId) {
        if (getPaymentProcessType() == 2) {
            Bundle bundleExtra = new Bundle();
            Utils.snackbarSuccess(GiftCardPaymentActivity.this, getStrings(R.string.payment_successful));
            bundleExtra.putString("GIFT_CARD_MESSAGE", getStrings(R.string.giftcard_sent_successfully).replace(GIFT_CARD, StorefrontCommonData.getTerminology().getGiftCard()));
            bundleExtra.putLong(VALUE_PAYMENT, selectedPaymentMethod);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else
            buyGiftCard(transactionId);
    }

    @Override
    public void onTransactionFailure(String transactionId) {
        if (getPaymentProcessType() == 2) {
            Bundle bundleExtra = new Bundle();
            bundleExtra.putString("GIFT_CARD_MESSAGE", getStrings(R.string.pre_transaction_failed));
            Utils.snackBar(GiftCardPaymentActivity.this, getStrings(R.string.pre_transaction_failed), false);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_ERROR, returnIntent);
            finish();
        } else
            buyGiftCard(transactionId, true);
    }

    @Override
    public void onTransactionApiError() {
    }

    @Override
    public void onPaymentSuccess(String transactionId, PaymentData paymentData) {
        onTransactionSuccess(transactionId, "");
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        // PaymentTransactionUrlManager.getInstance().apiJobExpired();
    }

}