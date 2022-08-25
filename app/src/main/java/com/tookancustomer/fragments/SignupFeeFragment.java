package com.tookancustomer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.WebViewActivityRazorPay;
import com.tookancustomer.WebViewPayfortActivity;
import com.tookancustomer.WebViewPaymentActivity;
import com.tookancustomer.adapters.PaymentListAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.callback.PaytmCheckBalanceCallback;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.EnterEdittextDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.RazorPayModel;
import com.tookancustomer.models.initiatePayfort.InitiatePayfortMethod;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.paymentMethodData.PaymentMethods;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.modules.payment.callbacks.FetchCardsListener;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.PaySlider;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LANGUAGE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.PAYMENT_METHOD_FIELD;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.TOTAL_AMOUNT;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.VALUE_PAYMENT;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.BILLPLZ;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_LINK;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_ON_DELIVERY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY;

public class SignupFeeFragment extends Fragment implements View.OnClickListener, PaytmCheckBalanceCallback {
    private Activity mActivity;

    private PaySlider paySlider;

    private RelativeLayout rlSliderContainer;
    private LinearLayout llPaymentParentLayout;
    private TextView tvSignupFeeErrorMessage;

    private TextView tvNoPaymentCardFound, ivAddPaymentOptions;
    private RecyclerView rvPaymentCardList;

    private HashMap<Long, Integer> hashMapPaymentMethods;
    private Set<Long> keySet;
    private long valuePaymentMethod = 0;

    private List<Datum> paymentList = new ArrayList<>();
    private PaymentListAdapter mAdapter;
    public boolean isCardSelected;
    public Integer adapterPos;
    public String selectedCardId;

    private Integer paytmVerified = null;
    private String paytmAddMoneyUrl;
    private Double paytmWalletAmount = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_fee, container, false);
        mActivity = getActivity();


        initViews(rootView);

        paySlider = new PaySlider(rootView.findViewById(R.id.rlSliderContainer)) {
            @Override
            public void onPayClick() {
                try {
                    onPaymentClick();
                } catch (Exception e) {
                    paySlider.setSlideInitial();
                }
            }
        };
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPaymentMethods();

    }


    private void getPaymentMethods() {

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
        hashMapPaymentMethods = Utils.getPaymentMethodsMap();
        hashMapPaymentMethods.remove(CASH.intValue);

        keySet = Utils.getPaymentMethodsMap().keySet();
        keySet.remove(PAYTM.intValue);
        keySet.remove(CASH.intValue);
        keySet.remove(PAY_ON_DELIVERY.intValue);
        keySet.remove(PAYTM_LINK.intValue);
        if (keySet.size() > 0) {
            valuePaymentMethod = (long) keySet.toArray()[0];
        } else if (hashMapPaymentMethods.containsKey(PAYTM.intValue)) {
            valuePaymentMethod = PAYTM.intValue;
        } else if (hashMapPaymentMethods.containsKey(CASH.intValue)) {
            valuePaymentMethod = CASH.intValue;
        } else if(hashMapPaymentMethods.containsKey(PAY_ON_DELIVERY.intValue)){
            valuePaymentMethod = PAY_ON_DELIVERY.intValue;
        } else if (hashMapPaymentMethods.containsKey(PAYTM_LINK.intValue)) {
            valuePaymentMethod = PAYTM_LINK.intValue;
        }

        valuePaymentMethod = PaymentMethodsClass.getEnabledPaymentMethod();

        if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).viewPager.getCurrentItem() == ((RegistrationOnboardingActivity) mActivity).SignUpFeeFragmentPos) {
            fetchPaymentData();
        }

    }

    public void fetchPaymentData() {
        if (mActivity != null) {
            paySlider.setSlideInitial();
            adapterPos = null;
            paytmVerified = null;
            fetchMerchantCards();
            if (hashMapPaymentMethods.containsKey(PAYTM.intValue)) paytmCheckBalance();
        }
    }

    public void initViews(View view) {
        ((TextView) view.findViewById(R.id.tvSignupFeeHeading)).setText(StorefrontCommonData.getString(mActivity, R.string.sign_up_fees));
        ((TextView) view.findViewById(R.id.tvSignupFeeMessage)).setText(StorefrontCommonData.getString(mActivity, R.string.please_pay_amount_given_below));
        ((TextView) view.findViewById(R.id.tvSignupFeeAmount)).setText(UIManager.getCurrency(Utils.getCurrencySymbol() + StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()));
        tvSignupFeeErrorMessage = view.findViewById(R.id.tvSignupFeeErrorMessage);
        tvSignupFeeErrorMessage.setText(StorefrontCommonData.getString(mActivity, R.string.payment_method_disabled_pay_cash_to_admin));

        ((TextView) view.findViewById(R.id.tvSlide)).setText(StorefrontCommonData.getTerminology().getPay(true));
        ((TextView) view.findViewById(R.id.sliderText)).setText(StorefrontCommonData.getString(mActivity, R.string.swipe_to_confirm));

        ((TextView) view.findViewById(R.id.tvPaymentMethodLabel)).setText(StorefrontCommonData.getTerminology().getPaymentMethod());

        rlSliderContainer = view.findViewById(R.id.rlSliderContainer);
        llPaymentParentLayout = view.findViewById(R.id.llPaymentParentLayout);
        tvNoPaymentCardFound = view.findViewById(R.id.tvNoPaymentCardFound);
        tvNoPaymentCardFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_payment_card_found));
        ivAddPaymentOptions = view.findViewById(R.id.ivAddPaymentOptions);
        ivAddPaymentOptions.setText(StorefrontCommonData.getString(mActivity, R.string.add_card));

        rvPaymentCardList = view.findViewById(R.id.rvPaymentCardList);
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvPaymentCardList.setNestedScrollingEnabled(false);


        Utils.setOnClickListener(this, ivAddPaymentOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddPaymentOptions:
                PaymentManager.openAddPaymentCardWebViewActivity(mActivity, PaymentMethodsClass.getEnabledPaymentMethod());
                break;
        }
    }

    private void fetchMerchantCards() {
        if (hashMapPaymentMethods.size() == 0) {
            tvSignupFeeErrorMessage.setVisibility(View.VISIBLE);
            llPaymentParentLayout.setVisibility(View.GONE);
            rlSliderContainer.setVisibility(View.GONE);
            return;
        } else {
            tvSignupFeeErrorMessage.setVisibility(View.GONE);
            llPaymentParentLayout.setVisibility(View.VISIBLE);
        }

        if (valuePaymentMethod == CASH.intValue || valuePaymentMethod == PAYTM_LINK.intValue || valuePaymentMethod == PAY_ON_DELIVERY.intValue) {
            paymentList = new ArrayList<Datum>();
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

        PaymentManager.fetchMerchantCards(mActivity, true, PaymentMethodsClass.getEnabledPaymentMethod(), new FetchCardsListener() {
            @Override
            public void onFetchCardsSuccess(List<Datum> paymentCardList) {
                paymentList = new ArrayList<Datum>();
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

    public void addCashAndWalletAndRazorPay() {
//        if (hashMapPaymentMethods.containsKey(CASH.intValue)) {
//            Datum datum = new Datum(CASH.intValue, StorefrontCommonData.getString(mActivity, R.string.continue_with_cash), CASH.intValue);
//            paymentList.add(datum);
//        }
//        if (hashMapPaymentMethods.containsKey(PAYTM.intValue)) {
//            Datum datum = new Datum(PAYTM.intValue, StorefrontCommonData.getString(mActivity, R.string.pay_with_paytm), PAYTM.intValue);
//            paymentList.add(datum);
//        }
//        if (hashMapPaymentMethods.containsKey(RAZORPAY.intValue)) {
//            Datum datum = new Datum(RAZORPAY.intValue, StorefrontCommonData.getString(mActivity, R.string.pay_with_razorpay), RAZORPAY.intValue);
//            paymentList.add(datum);
//        }
//        if (hashMapPaymentMethods.containsKey(BILLPLZ.intValue)) {
//            Datum datum = new Datum(BILLPLZ.intValue, StorefrontCommonData.getString(mActivity, R.string.pay_with_netbanking), BILLPLZ.intValue);
//            paymentList.add(datum);
//        }
//        if (hashMapPaymentMethods.containsKey(PAYTM_LINK.intValue)) {
//            Datum datum = new Datum(BILLPLZ.intValue, StorefrontCommonData.getString(mActivity, R.string.pay_with_netbanking), BILLPLZ.intValue);
//            paymentList.add(datum);
//        }
//
//        List<PaymentMethod> paymentMethods = StorefrontCommonData.getFormSettings().getPaymentMethods();
//        for (int i = 0; i < paymentMethods.size(); i++) {
//            if (paymentMethods.get(i).getValue() == STRIPE.intValue) {
//                Datum datum = new Datum(paymentMethods.get(i).getValue(), paymentMethods.get(i).getName(), paymentMethods.get(i).getValue());
//                paymentList.add(datum);
//            }
//        }

    }

    public void setPaymentAdapter(List<Datum> paymentList) {
        if (paymentList.size() > 0) {
            rvPaymentCardList.setVisibility(View.VISIBLE);
            tvNoPaymentCardFound.setVisibility(View.GONE);


            mAdapter = new PaymentListAdapter(mActivity, paymentList, paytmWalletAmount, paytmVerified);
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

            rlSliderContainer.setVisibility(View.VISIBLE);
        } else {
            rlSliderContainer.setVisibility(View.GONE);
            rvPaymentCardList.setVisibility(View.GONE);
            tvNoPaymentCardFound.setVisibility(View.VISIBLE);
        }

        if (valuePaymentMethod == PAYTM.intValue
                || valuePaymentMethod == RAZORPAY.intValue
                || valuePaymentMethod == BILLPLZ.intValue
                || valuePaymentMethod == PAYTM_LINK.intValue
                || valuePaymentMethod == CASH.intValue)
            ivAddPaymentOptions.setVisibility(View.GONE);
        else
            ivAddPaymentOptions.setVisibility(View.VISIBLE);

    }

    private void paytmCheckBalance() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(PAYMENT_METHOD_FIELD, PAYTM.intValue)
                .add("app_access_token", Dependencies.getAccessToken(mActivity))
                .add("dual_user_key", UIManager.isDualUserEnable())
                .add("app_device_type", "ANDROID")
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(mActivity)
                .paytmCheckBalance(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        PaymentMethods paymentMethodModel = new PaymentMethods();
                        paymentMethodModel.setData(baseModel.toResponseModel(com.tookancustomer.models.paymentMethodData.Data.class));

                        paytmVerified = paymentMethodModel.getData().getPaytm().getPaytmVerified();
                        paytmWalletAmount = paymentMethodModel.getData().getPaytm().getWalletBalance();
                        paytmAddMoneyUrl = paymentMethodModel.getData().getPaytm().getPaytmAddMoneyUrl();
                        paytmAddMoneyUrl += "&amount=" + getRemainingAmount(paytmWalletAmount);

                        setPaymentAdapter(paymentList);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });
    }

    private String getRemainingAmount(final Double paytmWalletAmount) {
        double payableAmount;
        try {
            payableAmount = Double.parseDouble(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount());
        } catch (final NumberFormatException ex) {
            return "0";
        } catch (final NullPointerException ex) {
            return "0";
        }
        if (payableAmount > paytmWalletAmount) {
            return "" + Utils.getDecimalFormatCalculation().format((payableAmount - paytmWalletAmount));
        } else {
            return "0";
        }
    }

    private void onPaymentClick() {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            paySlider.setSlideInitial();
            return;
        }

        if (isCardSelected && adapterPos != null) {
            if ((paymentList.get(adapterPos).getPaymentMethod() == CASH.intValue)
                    || (paymentList.get(adapterPos).getPaymentMethod() == PAYTM_LINK.intValue)
                    || (paymentList.get(adapterPos).getPaymentMethod() == PAY_ON_DELIVERY.intValue)) {
                new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.payment_method_disabled_pay_cash_to_admin)).build().show();
                paySlider.setSlideInitial();
            } else if (paymentList.get(adapterPos).getPaymentMethod() == PAYFORT.intValue) {
                new EnterEdittextDialog.Builder(mActivity)
                        .title(StorefrontCommonData.getString(mActivity, R.string.cvv))
                        .message(StorefrontCommonData.getString(mActivity, R.string.please_enter_cvv_message))
                        .editTextHint(StorefrontCommonData.getString(mActivity, R.string.enter_cvv))
                        .positiveButton(StorefrontCommonData.getString(mActivity, R.string.confirm))
                        .negativeButton(StorefrontCommonData.getString(mActivity, R.string.cancel))
                        .listener(new EnterEdittextDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack, String editTextString) {
                                if (editTextString.isEmpty()) {
                                    Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.please_enter_cvv_message));
                                    paySlider.setSlideInitial();
                                } else {
                                    initiatePayfortPayment(editTextString);
                                }
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                                paySlider.setSlideInitial();
                            }
                        }).build().show();

            } else if (paymentList.get(adapterPos).getPaymentMethod() == RAZORPAY.intValue) {
                CommonParams.Builder commonParams = new CommonParams.Builder();
                commonParams.add("app_access_token", Dependencies.getAccessToken(mActivity));
                commonParams.add("payment_for", 1); // 0 for create task and 1 for subscription fee
                commonParams.add("amount", StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount());

                RestClient.getApiInterface(mActivity).razorpayPayment(commonParams.build().getMap()).
                        enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                            @Override
                            public void success(BaseModel baseModel) {
                                RazorPayModel razorPayModel = baseModel.toResponseModel(RazorPayModel.class);

                                Intent intentPayment = new Intent(mActivity, WebViewActivityRazorPay.class);
                                intentPayment.putExtra(URL_WEBVIEW, razorPayModel.getUrl());
                                intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.processing_payment));
                                startActivityForResult(intentPayment, Codes.Request.OPEN_RAZORPAY_WEBVIEW_ACTIVITY);
                                AnimationUtils.forwardTransition(mActivity);
                            }

                            @Override
                            public void failure(APIError error, BaseModel baseModel) {
                                paySlider.setSlideInitial();
                            }
                        });

            } else if (paymentList.get(adapterPos).getPaymentMethod() == BILLPLZ.intValue) {
                CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
                commonParams.add("amount", StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount())
                        .add("payment_method", paymentList.get(adapterPos).getPaymentMethod())
                        .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName());
                commonParams.add("payment_for", 1); // 0 for create task and 1 for subscription fee
                commonParams.build().getMap().remove(APP_ACCESS_TOKEN);


                RestClient.getApiInterface(mActivity).initatePayment(commonParams.build().getMap()).
                        enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                            @Override
                            public void success(BaseModel baseModel) {
                                RazorPayModel razorPayModel = baseModel.toResponseModel(RazorPayModel.class);

                                Intent intentPayment = new Intent(mActivity, WebViewPaymentActivity.class);
                                intentPayment.putExtra(URL_WEBVIEW, razorPayModel.getUrl());
                                intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.processing_payment));
                                intentPayment.putExtra(VALUE_PAYMENT, paymentList.get(adapterPos).getPaymentMethod());
                                intentPayment.putExtra(TOTAL_AMOUNT, StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount());
                                startActivityForResult(intentPayment, Codes.Request.OPEN_BILLPLZ_WEBVIEW_ACTIVITY);
                                AnimationUtils.forwardTransition(mActivity);
                            }

                            @Override
                            public void failure(APIError error, BaseModel baseModel) {
                                paySlider.setSlideInitial();
                            }
                        });

            } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && paytmVerified != null && paytmVerified == 0) {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.paytm_account_not_linked));
                paySlider.setSlideInitial();
            } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && Double.valueOf(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()) > paytmWalletAmount) {
                if (paytmVerified == null) {
                    Utils.snackBar(mActivity, getString(R.string.error_paytm_money_load));
                    return;
                }
                Intent intentPayment = new Intent(mActivity, WebViewPaymentActivity.class);
                intentPayment.putExtra(URL_WEBVIEW, paytmAddMoneyUrl);
                intentPayment.putExtra(HEADER_WEBVIEW, getString(R.string.add_paytm_money));
                startActivityForResult(intentPayment, Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY);
                AnimationUtils.forwardTransition(mActivity);
                paySlider.setSlideInitial();
            } else {
                paySignupFee(adapterPos, "", "");
            }

        } else {
            if (tvNoPaymentCardFound.getVisibility() == View.GONE) {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.please_select_payment_option).replace(StorefrontCommonData.getString(mActivity, R.string.payment), StorefrontCommonData.getTerminology().getPayment(false)));
            } else {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.please_add_payment_option_first).replace(StorefrontCommonData.getString(mActivity, R.string.payment), StorefrontCommonData.getTerminology().getPayment(false)));
            }
            paySlider.setSlideInitial();
        }

    }

    private void paySignupFee(int adapterPos, String transactionId, String jobPaymentDetailId) {
        if (paySlider.isSliderInIntialStage())
            paySlider.fullAnimate();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity));
        commonParams.add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
        commonParams.add("plan_amount", StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount());
        commonParams.add("currency_id", Utils.getCurrencyId());

        if (paymentList.get(adapterPos).getCardId() != null)
            commonParams.add("card_id", paymentList.get(adapterPos).getCardId());

        commonParams.add("payment_method", paymentList.get(adapterPos).getPaymentMethod());

        if (valuePaymentMethod == RAZORPAY.intValue) {
            commonParams.add("transaction_id", transactionId);
        } else {
            if (transactionId != null && !transactionId.isEmpty()) {
                commonParams.add("transaction_id", transactionId);
                commonParams.add("job_payment_detail_id", jobPaymentDetailId);
            }
        }

        RestClient.getApiInterface(mActivity).updateVendorSubscription(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).setPaid(1);
                        if (mActivity instanceof RegistrationOnboardingActivity) {
                            ((RegistrationOnboardingActivity) mActivity).setCurentStep();
                        }
                    }
                }, 400);
                Utils.snackbarSuccess(mActivity, baseModel.getMessage());

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                paySlider.setSlideInitial();
            }
        });
    }

    private void initiatePayfortPayment(String editTextString) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity))
                .add("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "")
                .add("cvv", editTextString)
                .add("payment_for", 1) // 0 for create task and 1 for subscription fee
                .add("amount", StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount());

        if (paymentList.get(adapterPos).getCardId() != null)
            commonParams.add("vendor_card_id", paymentList.get(adapterPos).getCardId());

//        RestClient.getApiInterface(mActivity).initatePayfortPayment(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
        RestClient.getApiInterface(mActivity).getPaymentUrl(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                InitiatePayfortMethod initiatePayfortMethod = new InitiatePayfortMethod();
                try {
                    initiatePayfortMethod.setData(baseModel.toResponseModel(com.tookancustomer.models.initiatePayfort.Data.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                Intent intentPayment = new Intent(mActivity, WebViewPayfortActivity.class);
                intentPayment.putExtra(URL_WEBVIEW, initiatePayfortMethod.getData().get3dsUrl());
                intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.processing_payment));
                startActivityForResult(intentPayment, Codes.Request.OPEN_PAYFORT_WEBVIEW_ACTIVITY);
                AnimationUtils.forwardTransition(mActivity);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    @Override
    public void onPaytmOptionClicked() {
    }

    public void openAddPaytmMoneyWebview() {
        if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && Double.valueOf(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()) > paytmWalletAmount) {
            if (paytmVerified == null) {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.error_paytm_money_load));
                return;
            }
            Intent intentPayment = new Intent(mActivity, WebViewPaymentActivity.class);
            intentPayment.putExtra(URL_WEBVIEW, paytmAddMoneyUrl);
            intentPayment.putExtra(HEADER_WEBVIEW, getString(R.string.add_paytm_money));
            startActivityForResult(intentPayment, Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY);
            AnimationUtils.forwardTransition(mActivity);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.hideSoftKeyboard(mActivity);
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY:
                if (resultCode == RESULT_OK) {
//                    hitFetchMerchantCard = data.getExtras().getBoolean("hitFetchMerchantCard");
                }
                break;

            case Codes.Request.OPEN_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
//                    hitFetchMerchantCard = data.getExtras().getBoolean("hitFetchMerchantCard");
                }
                break;

            case Codes.Request.OPEN_PAYFORT_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("transactionId")) {
                        paySignupFee(adapterPos, data.getStringExtra("transactionId"), data.getStringExtra("jobPaymentDetailId"));
                    }
                }
                break;
            case Codes.Request.OPEN_RAZORPAY_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("rzpPaymentId")) {
                        paySignupFee(adapterPos, data.getStringExtra("rzpPaymentId"), "");
                    }
                }
                break;
            case Codes.Request.OPEN_BILLPLZ_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("transactionId")) {
                        paySignupFee(adapterPos, data.getStringExtra("transactionId"), "");
                    }
                }
                break;
            case Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY:
                if (resultCode == RESULT_OK) {
//                    hitFetchMerchantCard = true;
//                    paytmCheckBalance();
                }
                break;

            case Codes.Request.OPEN_OTP_FOR_PAYTM:
                if (resultCode == RESULT_OK) {
//                    hitFetchMerchantCard = true;
                }
                break;
        }
    }

}