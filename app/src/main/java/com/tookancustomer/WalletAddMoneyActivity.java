package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tookancustomer.adapters.AddWalletPaymentListAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.callback.PaytmCheckBalanceCallback;
import com.tookancustomer.models.AddWalletBalanceResponseData;
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
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AUTHORISE_DOT_NET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AZUL;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.FAC;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.VISTA_MONEY;

/*
 * Currently we are supporting
 * STRIPE, PAYFORT, PAYPAL, RAZORPAY, BILLPLZ for in app wallets
 * */
public class WalletAddMoneyActivity extends BaseActivity implements View.OnClickListener, PaytmCheckBalanceCallback, TransactionUrlListener, PaymentResultWithDataListener {
    private TextView tvHeading;
    private TextView tvAvailableBalance;
    private MaterialEditText etEnterAmount;
    private Button btnAddMoney;

    private TextView tvPaymentMethodLabel, tvNoPaymentCardFound, ivAddPaymentOptions;
    private RecyclerView rvPaymentCardList;
    private List<Datum> paymentList = new ArrayList<>();
    private AddWalletPaymentListAdapter mAdapter;
    public boolean isCardSelected;
    public Integer adapterPos;

    private long valuePaymentMethod = 0;

    private Integer paytmVerified = null;
    private String paytmAddMoneyUrl;
    private Double paytmWalletAmount = 0.0;
    public String selectedCardId;

    public double balanceToBeAdded = 0.0;
    private long selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_add_money);
        mActivity = this;

        getPaymentMethods();

    }


    private void getPaymentMethods() {

        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
//        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
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

        balanceToBeAdded = getIntent().getExtras().getDouble(BALANCE_TO_BE_ADDED, 0.0);
        initViews();

        fetchMerchantCards();
        if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
    }


    private void initViews() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getTerminology().getWallet());

        tvAvailableBalance = findViewById(R.id.tvAvailableBalance);
        tvAvailableBalance.setText(getStrings(R.string.available_balance) + ": " + UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getWalletBalance())));
        etEnterAmount = findViewById(R.id.etEnterAmount);
        etEnterAmount.setHint(getStrings(R.string.enter_amount) + " (" + Utils.getCurrencySymbol() + ")");
        etEnterAmount.setFloatingLabelText(getStrings(R.string.enter_amount) + " (" + Utils.getCurrencySymbol() + ")");
        if (balanceToBeAdded > 0) {
            etEnterAmount.setText(Utils.getDoubleTwoDigits(balanceToBeAdded));
        }

        tvPaymentMethodLabel = findViewById(R.id.tvPaymentMethodLabel);
        tvPaymentMethodLabel.setText(StorefrontCommonData.getTerminology().getPaymentMethod());
        tvNoPaymentCardFound = findViewById(R.id.tvNoPaymentCardFound);
        tvNoPaymentCardFound.setText(getStrings(R.string.no_payment_card_found));
        ivAddPaymentOptions = findViewById(R.id.ivAddPaymentOptions);
        ivAddPaymentOptions.setText(getStrings(R.string.add_card));

        rvPaymentCardList = findViewById(R.id.rvPaymentCardList);
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvPaymentCardList.setNestedScrollingEnabled(false);

        btnAddMoney = findViewById(R.id.btnAddMoney);
        btnAddMoney.setText(getStrings(R.string.add_money));

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), btnAddMoney, ivAddPaymentOptions);
    }

    public double getAmountToBeAdded() {
        try {
            if (!(etEnterAmount.getText().toString().trim().isEmpty()) && !etEnterAmount.getText().toString().trim().equals(".")) {
                return Double.valueOf(etEnterAmount.getText().toString().trim());
            } else {
                return 0;
            }
        } catch (final NumberFormatException ex) {
            return 0;
        } catch (final NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.btnAddMoney:
                if (validateAddMoney()) {
                    if (paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.STRIPE.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == VISTA_MONEY.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == FAC.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == AZUL.intValue
                            || paymentList.get(adapterPos).getPaymentMethod() == AUTHORISE_DOT_NET.intValue) {
                        addWalletMoney(paymentList.get(adapterPos).getCardId());
                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.PAYTM.intValue)) {
                        if (paytmVerified != null) {
                            if (paytmVerified == 0) {
                                Utils.snackBar(mActivity, getStrings(R.string.paytm_account_not_linked));
                            } else if (getAmountToBeAdded() > paytmWalletAmount) {
                                PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
                            } else {
                                addWalletMoney("");
                            }
                        } else {
                            Utils.snackBar(this, getString(R.string.error_paytm_money_load));
                        }
                    } else {
                        HashMap<String, String> map = PaymentTransactionUrlManager.getMap(mActivity, paymentList.get(adapterPos).getPaymentMethod(), paymentList.get(adapterPos), getAmountToBeAdded() + "");
                        if (paymentList.get(adapterPos).getPaymentMethod() == PaymentConstants.PaymentValue.PAYFORT.intValue) {
                            map.put("vendor_card_id", paymentList.get(adapterPos).getCardId());
                        }

                        if (getPaymentProcessType() == 2) {
                            new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                                    .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                                    .setPaymentForFlow(PaymentConstants.PaymentForFlow.IN_APP_WALLET.intValue)
                                    .setMap(map)
                                    .setCreateTaskData(getAddAmountMap("", false).build().getMap())
                                    .setUserId(StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                                    .setPaymentData(paymentList.get(adapterPos))
                                    .setListener(this)
                                    .build();
                        } else {

                            new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                                    .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                                    .setPaymentForFlow(PaymentConstants.PaymentForFlow.IN_APP_WALLET.intValue)
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
        if (getAmountToBeAdded() <= 0) {
            Utils.snackBar(mActivity, getStrings(R.string.please_enter_amount));
            return false;
        }
        if (!isCardSelected || (adapterPos == null)) {
            if (tvNoPaymentCardFound.getVisibility() == View.GONE) {
                Utils.snackBar(mActivity, getStrings(R.string.please_select_payment_option).replace(getStrings(R.string.payment), StorefrontCommonData.getTerminology().getPayment(false)));
            } else {
                Utils.snackBar(mActivity, getStrings(R.string.please_add_payment_option_first).replace(getStrings(R.string.payment), StorefrontCommonData.getTerminology().getPayment(false)));
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
                PaymentTransactionUrlManager.getInstance().onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void addWalletMoney(String transactionId) {
        addWalletMoney(transactionId, false);
    }

    private void addWalletMoney(String transactionId, boolean isFailedTransaction) {
        CommonParams.Builder commonParams = getAddAmountMap(transactionId, isFailedTransaction);
        commonParams.build().getMap().remove("fac_payment_flow");

        RestClient.getApiInterface(mActivity).addWalletMoney(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Bundle bundleExtra = new Bundle();
                bundleExtra.putString("ADD_MONEY_MESSAGE", baseModel.getMessage());

                Dependencies.setWalletBalance(baseModel.toResponseModel(AddWalletBalanceResponseData.class).getWalletBalanceAfterTxn());

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

    private CommonParams.Builder getAddAmountMap(String transactionId, boolean isFailedTransaction) {
        selectedPaymentMethod = paymentList.get(adapterPos).getPaymentMethod();

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add("payment_method", selectedPaymentMethod)
                .add("amount", getAmountToBeAdded())
                .add("payment_for", PaymentConstants.PaymentForFlow.IN_APP_WALLET.intValue);

        if (selectedPaymentMethod == FAC.intValue) {
//            hashMap.put("cvv", cvv);
            commonParams.add("fac_payment_flow", "2");
        }


        if (isFailedTransaction) commonParams.add("is_transaction_failed", 1);

        if (selectedPaymentMethod == PaymentConstants.PaymentValue.STRIPE.intValue
                || selectedPaymentMethod == VISTA_MONEY.intValue
                || selectedPaymentMethod == FAC.intValue
                || selectedPaymentMethod == AZUL.intValue
                || selectedPaymentMethod == AUTHORISE_DOT_NET.intValue) {
            commonParams.add("card_id", transactionId);
        } else if (selectedPaymentMethod == PaymentConstants.PaymentValue.PAYTM.intValue) {
            //PAYTM need not send any key for add wallet
        } else {
            commonParams.add("transaction_id", transactionId);
        }


        if (selectedPaymentMethod == FAC.intValue) {
//            hashMap.put("cvv", cvv);
            commonParams.add("fac_payment_flow", "2");
        }
        return commonParams;
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
                if (paay.getPaymentMethod() == PaymentConstants.PaymentValue.CASH.intValue
                        || paay.getPaymentMethod() == PaymentConstants.PaymentValue.INAPP_WALLET.intValue
                        || paay.getPaymentMethod() == PaymentConstants.PaymentValue.PAY_LATER.intValue
                        || paay.getPaymentMethod() == PaymentConstants.PaymentValue.PAY_ON_DELIVERY.intValue)
                    it.remove();
            }


            mAdapter = new AddWalletPaymentListAdapter(mActivity, paymentList, paytmWalletAmount, paytmVerified);
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
                Datum datum = new Datum(paymentMethods.get(i).getValue(), paymentMethods.get(i).getLabel(), paymentMethods.get(i).getValue(), paymentMethods.get(i).getPaymentMode());
                paymentList.add(datum);
            }
        }

    }

    @Override
    public void onTransactionSuccess(String transactionId, String jobPaymentDetailId) {
        if (getPaymentProcessType() == 2) {
            Bundle bundleExtra = new Bundle();
            Utils.snackbarSuccess(WalletAddMoneyActivity.this, getStrings(R.string.payment_successful));
            bundleExtra.putString("ADD_MONEY_MESSAGE", getStrings(R.string.successful_text));
            bundleExtra.putLong(VALUE_PAYMENT, selectedPaymentMethod);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else
            addWalletMoney(transactionId);
    }

    @Override
    public void onTransactionFailure(String transactionId) {
        /* Transaction failure will be calling on payfort cases */
        if (getPaymentProcessType() == 2) {
            Bundle bundleExtra = new Bundle();
            bundleExtra.putString("ADD_MONEY_MESSAGE",getStrings(R.string.transaction_failed));
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_ERROR, returnIntent);
            finish();
        } else
            addWalletMoney(transactionId, true);
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
