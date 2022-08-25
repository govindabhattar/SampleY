package com.tookancustomer.modules.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.PaytmUtility;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.tookancustomer.GiftCardPaymentActivity;
import com.tookancustomer.MakePaymentActivity;
import com.google.zxing.aztec.encoder.AztecCode;
import com.tookancustomer.R;
import com.tookancustomer.WalletAddMoneyActivity;
import com.tookancustomer.WebViewActivityPayuLatum;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.EnterEdittextDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.payments.TransactionUrlData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.payment.activities.ProcessingPaymentWebViewActivity;
import com.tookancustomer.modules.payment.callbacks.TransactionUrlListener;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.modules.reward.model.CreateChargeData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static com.tookancustomer.appdata.Codes.Request.OPEN_PAYTM_SDK;
import static com.tookancustomer.appdata.Codes.Request.RESULT_PAYMENT_ERROR;
import static com.tookancustomer.appdata.Keys.Extras.EXTRA_IS_LAUNDRY_EDIT_ORDER;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.JOB_ID;
import static com.tookancustomer.appdata.Keys.Extras.JOB_PAYMENT_DETAIL_ID;
import static com.tookancustomer.appdata.Keys.Extras.PAYMENT_FOR_FLOW;
import static com.tookancustomer.appdata.Keys.Extras.TRANSACTION_ID;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.VALUE_PAYMENT;
import static com.tookancustomer.appdata.Keys.MetaDataKeys.PAYMENT_METHOD;
import static com.tookancustomer.appdata.Keys.Prefs.DEVICE_TOKEN;
import static com.tookancustomer.appdata.Keys.Prefs.EXTRA_REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.Prefs.IS_LOCK_ENABLED;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AUTHORISE_DOT_NET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AZUL;
import static com.tookancustomer.appdata.Keys.Prefs.REF_ID;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.FAC;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INAPP_WALLET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INSTAPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFAST;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYMOB;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_LINK;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_UPI;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYU_LATAM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY_UPI;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.STRIPE;

public class PaymentTransactionUrlManager implements Keys.APIFieldKeys {

    private static PaymentTransactionUrlManager manager;
    private HashMap<String, String> createTaskData;
    private Datum paymentMethodData;
    private Activity mActivity;
    private long paymentMethod;
    private long paymentForFlow;
    private int userId;
    private int isLockEnabled;
    private HashMap<String, String> map;
    private TransactionUrlListener listener;
    private boolean isOrderPayment = true;
    private String additionalpaymentId = "";
    private int jobId;
    private int rPayReferenceId, rPayJobExpiredReferenceId;
    private String rPayOrderId;
    private String rezorpay_key_id;
    private String storeName = "";


    private PaymentTransactionUrlManager(Activity mActivity, long paymentMethod, long paymentForFlow, int userId, int isLockEnabled,
                                         HashMap<String, String> map, TransactionUrlListener listener,
                                         boolean isOrderPayment, Datum paymentMethodData, HashMap<String, String> createTaskData, String additionalpaymentId, int jobId, String storeName) {
        this.mActivity = mActivity;
        this.paymentMethod = paymentMethod;
        this.paymentForFlow = paymentForFlow;
        this.userId = userId;
        this.isLockEnabled = isLockEnabled;

        this.map = map;
        this.additionalpaymentId = additionalpaymentId;
        this.listener = listener;
        this.isOrderPayment = isOrderPayment;
        this.paymentMethodData = paymentMethodData;
        this.createTaskData = createTaskData;
        this.jobId = jobId;
        this.storeName = storeName;
    }

    public static PaymentTransactionUrlManager getInstance(Activity mActivity, long paymentMethod,
                                                           long paymentForFlow, int userId, int isLockEnabled, HashMap<String, String> map,
                                                           TransactionUrlListener listener, boolean isOrderPayment, Datum paymentMethodData, HashMap<String, String> createTaskData, String additionalpaymentId, int jobId, String storeName) {
        if (manager == null) {
            manager = new PaymentTransactionUrlManager(mActivity, paymentMethod, paymentForFlow, userId, isLockEnabled, map, listener, isOrderPayment, paymentMethodData, createTaskData, additionalpaymentId, jobId, storeName);
        } else {
            manager.mActivity = mActivity;
            manager.paymentMethod = paymentMethod;
            manager.paymentForFlow = paymentForFlow;
            manager.userId = userId;
            manager.isLockEnabled = isLockEnabled;

            manager.map = map;
            manager.additionalpaymentId = additionalpaymentId;
            manager.jobId = jobId;
            manager.listener = listener;
            manager.isOrderPayment = isOrderPayment;
            manager.paymentMethodData = paymentMethodData;
            manager.createTaskData = createTaskData;
            manager.storeName = storeName;
        }

        return manager;
    }

    public static PaymentTransactionUrlManager getInstance(Activity mActivity, long paymentMethod, long paymentForFlow, int userId, int isLockEnabled, HashMap<String, String> map, TransactionUrlListener listener, boolean isOrderPayment, Datum paymentMethodData, String additionalpaymentId, int jobId, String storeName) {
        getInstance(mActivity, paymentMethod, paymentForFlow, userId, isLockEnabled, map, listener, isOrderPayment, paymentMethodData, null, additionalpaymentId, jobId, storeName);

        return manager;
    }


    public static PaymentTransactionUrlManager getInstance() {
        return manager;
    }

    public static HashMap<String, String> getMap(Activity mActivity, long paymentMethod, Datum paymentMethodData, String payableAmount) {
        /*This method will be in case of PRE PAYMENT where we have no order id
         * */
        return getMap(mActivity, paymentMethod, payableAmount, 0, paymentMethodData, "");
    }

    public static HashMap<String, String> getMap(Activity mActivity, long paymentMethod, String payableAmount) {
        /*This method will be in case of PRE PAYMENT where we have no order id
         * */
        return getMap(mActivity, paymentMethod, payableAmount, 0, null, "");
    }

    public static HashMap<String, String> getMap(Activity mActivity, long paymentMethod, String payableAmount, int orderId, Datum paymentMethodData, String currency) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add("amount", payableAmount);
        if (orderId != 0) {
            commonParams.add("job_id", orderId);
        }


        if (paymentMethod == INSTAPAY.intValue) {
            if (currency.isEmpty())
                currency = Utils.getCurrencyCode();
            commonParams.add("name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName())
                    .add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail())
                    .add("phone", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo())
                    .add("currency", currency);

            commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
            commonParams.build().getMap().remove(DUAL_USER_KEY);
            commonParams.build().getMap().remove(PAYMENT_METHOD);
            commonParams.build().getMap().remove(APP_VERSION);
            commonParams.build().getMap().remove(REFERENCE_ID);
            commonParams.build().getMap().remove(MARKETPLACE_REF_ID);
            commonParams.build().getMap().remove(FORM_ID);
            commonParams.build().getMap().remove(YELO_APP_TYPE);
            commonParams.build().getMap().remove(DEVICE_TOKEN);
            commonParams.build().getMap().remove(IS_DEMO_APP);

        } /*else if (paymentMethod == FAC.intValue) {
            commonParams.build().getMap().remove(APP_ACCESS_TOKEN);

        }*/ else if (paymentMethod == PAYFAST.intValue) {
           /* commonParams.add("item_name", "addcard");

            commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
            commonParams.build().getMap().remove(APP_VERSION);
            commonParams.build().getMap().remove(REFERENCE_ID);
            commonParams.build().getMap().remove(FORM_ID);
            commonParams.build().getMap().remove(YELO_APP_TYPE);
            commonParams.build().getMap().remove(DEVICE_TOKEN);
            commonParams.build().getMap().remove(IS_DEMO_APP);*/

        } else if (paymentMethod == PAYU_LATAM.intValue) {
            if (currency.isEmpty())
                currency = Utils.getCurrencyCodeNew();
            commonParams.add("payment_method", PaymentMethodsClass.getEnabledPaymentMethod())
                    .add("currency", currency)
                    .add("customer_name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName())
                    .add("customer_email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());

            commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
            commonParams.build().getMap().remove(DUAL_USER_KEY);
            commonParams.build().getMap().remove(PAYMENT_METHOD);
            commonParams.build().getMap().remove(APP_VERSION);
            commonParams.build().getMap().remove(REFERENCE_ID);
            commonParams.build().getMap().remove(MARKETPLACE_REF_ID);
            commonParams.build().getMap().remove(FORM_ID);
            commonParams.build().getMap().remove(YELO_APP_TYPE);
            commonParams.build().getMap().remove(DEVICE_TOKEN);
            commonParams.build().getMap().remove(IS_DEMO_APP);

        } else if (paymentMethodData.getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {
            /*
             * Common payment url method added for --> PAYFORT, RAZORPAY, PAYPAL , BILLPLZ
             * */

            if (currency.isEmpty()) {
                currency = Utils.getCurrencyCode();
            }
            commonParams.add("name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName())
                    .add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail())
                    .add("phone", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo())
                    .add("user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())

                    .add("payment_method", paymentMethod)
                    .add("currency", currency);

/*
            if (paymentMethod == PAYMOB.intValue) {
                commonParams.add("get_address_url", 1);
            }
*/

        } else {
            /*
             * Common payment url method added for --> STRIPE, CASH, PAYTM, WALLET where for payment we don't have to redirect to processing screen
             * */
            commonParams.add("payment_method", paymentMethod);
            commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
        }
        return commonParams.build().getMap();
    }

    /*
     * Payment url is used for redirecting to webview where transaction will process.
     * It will not used for STRIPE, CASH, VENMO, PAYTM, INAPP_WALLET as payment will be done simultaneously. Only in case of custom quotation, they will be called to confirm the transaction only
     * Payment methods --> PAYPAL , PAYFORT, RAZORPAY, BILLPLZ, PAYFAST, FAC, INSTAPAY, PAYU_LATAM will redirect to webview for payment only
     * */
    void getPaymentUrl() {
        if (paymentMethod == PAYFAST.intValue) {
            map.put("currency", Utils.getCurrencySymbol());
            map.put("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
            map.put("name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName());
            map.put("payment_method", "1024");


        }
        map.put("payment_for", paymentForFlow + "");
        map.put(USER_ID, userId + "");
        if (jobId > 0)
            map.put(JOB_ID, jobId + "");
        else
            map.remove(JOB_ID);

        if ((paymentForFlow == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue ||
                paymentForFlow == PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue)
                && ((additionalpaymentId != null && !additionalpaymentId.isEmpty())))
            map.remove(JOB_ID);

        if (paymentMethod == PAYFORT.intValue) {
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
                                listener.onTransactionApiError();
                            } else {
                                map.put("cvv", editTextString.trim());
                                map.remove("additionalpaymentId");
                                RestClient.getApiInterface(mActivity).initatePayfortPayment(map).enqueue(getPaymentUrlResponse());
                            }
                        }

                        @Override
                        public void performNegativeAction(int purpose, Bundle backpack) {
                            listener.onTransactionApiError();
                        }
                    }).build().show();


        }  /*else if (paymentMethod == FAC.intValue) {

            RestClient.getApiInterface(mActivity).FACPayment(map).enqueue(getPaymentUrlResponse());

        }*/ else if (paymentMethod == INSTAPAY.intValue) {

            RestClient.getApiInterface(mActivity).InstaPayPayment(map).enqueue(getPaymentUrlResponse());

        } else if (paymentMethod == PAYU_LATAM.intValue) {

            RestClient.getApiInterface(mActivity).initiatePayulatam(map).enqueue(getPaymentUrlResponse());

        } else if (paymentMethod == STRIPE.intValue || paymentMethod == INAPP_WALLET.intValue
                || paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue) {

            if (isOrderPayment && paymentForFlow != PaymentConstants.PaymentForFlow.DEBT.intValue
                    && paymentForFlow != PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue
                    && paymentForFlow != PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue) {

                if (paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue) {
                    map.remove("subtotal_amount");
                }
                RestClient.getApiInterface(mActivity).initatePayment(map).enqueue(getPaymentUrlResponse());
            }else{
                    map.remove("isEditedTask");
                    if ((paymentForFlow == PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue
                            || paymentForFlow == PaymentConstants.PaymentForFlow.REWARD.intValue) && (paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue)) {
                        map.remove("subtotal_amount");
                        RestClient.getApiInterface(mActivity).initatePayment(map).enqueue(getPaymentUrlResponse());
                    } else {
                        if (paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue) {
                            map.remove("subtotal_amount");
                        }
                        RestClient.getApiInterface(mActivity).createCharge(map).enqueue(createChargeResponse());
                    }
                }
            } else if (paymentMethodData.getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {

                int paymentProcessType = 0;
                for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
                    if (StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getValue() == paymentMethod) {
                        paymentProcessType = StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getPaymentProcessType();
                        break;
                    }
                }

                if (additionalpaymentId != null && !additionalpaymentId.isEmpty() /*&& paymentMethod == RAZORPAY.intValue*/) {
                    map.put("additionalpaymentId", additionalpaymentId);
//                map.put("job_id", jobId + "");
                } else {
//                map.remove("job_id");
                    map.remove("additionalpaymentId");

                }

                if ((paymentProcessType == 2)
                        && paymentForFlow != PaymentConstants.PaymentForFlow.SIGN_UP_FEE.intValue
                        && paymentForFlow != PaymentConstants.PaymentForFlow.DEBT.intValue
                        && paymentForFlow != PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue) {
                    // Map to JSON

                    if (jobId > 0) {
                        if (paymentForFlow != PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue) {
                            map.put(JOB_ID, jobId + "");

                        } else {
                            map.remove(JOB_ID);
                        }
                    } else
                        map.remove(JOB_ID);


                    Gson gson = new Gson();

                    /* && paymentForFlow == PaymentConstants.PaymentForFlow.ORDER_PAYMENT.intValue*/

                    if (createTaskData != null) {
                        String taskType = Constants.PaymenetTaskType.FOOD;

                        if (Dependencies.isLaundryApp())
                            taskType = Constants.PaymenetTaskType.LAUNDARY;

                        if (map.containsKey("payment_for")
                                && map.get("payment_for") != null
                                && !map.get("payment_for").isEmpty()
                                && Integer.parseInt(map.get("payment_for")) > 0) {
                            taskType = Constants.PaymenetTaskType.CREATE_CHARGE;
                        }

                        if (jobId > 0 && (map.containsKey("payment_for")
                                && map.get("payment_for") != null
                                && !map.get("payment_for").isEmpty()
                                && Integer.parseInt(map.get("payment_for")) == 0))
                            taskType = Constants.PaymenetTaskType.CUSTOM_QUOTATION_ORDER;

                        if (map.containsKey("payment_for")
                                && map.get("payment_for") != null
                                && !map.get("payment_for").isEmpty()
                                && Integer.parseInt(map.get("payment_for")) == PaymentConstants.PaymentForFlow.REWARD.intValue) {
                            taskType = Constants.PaymenetTaskType.REWARD;
                        }

                        if (map.containsKey("is_custom_order") || createTaskData.containsKey("is_custom_order"))
                            taskType = Constants.PaymenetTaskType.CUSTOM_ORDER;

//                    if ((map.containsKey("is_custom_order") || createTaskData.containsKey("is_custom_order")) && Dependencies.isLaundryApp())
//                        taskType = Constants.PaymenetTaskType.LAUNDRY_CUSTOM_ORDER;
                        if (map.containsKey("is_recurring_enabled") && map.get("is_recurring_enabled").equalsIgnoreCase("1"))
                            taskType = Constants.PaymenetTaskType.RECURRING_TASk;


                        if (map.containsKey("wallet_partial_payment")) {
                            map.remove("wallet_partial_payment");
                            createTaskData.put("wallet_partial_payment", "1");
                            createTaskData.put("task_type", "1");
                        } else {
                            createTaskData.put("task_type", taskType);

                        }
                        createTaskData.put("subtotal_amount", map.get("subtotal_amount"));
                        if (map.containsKey("subtotal_amount")) {
                            map.remove("subtotal_amount");
                        }
                        String jsonFromMap = gson.toJson(createTaskData);
                        map.put("orderCreationPayload", jsonFromMap);
                    }


                    RestClient.getApiInterface(mActivity).getPaymentUrl(map).enqueue(getPaymentUrlResponse());
                } else {
                    if (map.containsKey("subtotal_amount")) {
                        map.remove("subtotal_amount");
                    }
                    RestClient.getApiInterface(mActivity).getPaymentUrl(map).enqueue(getPaymentUrlResponse());

                }

            } else {
                /* In case of stripe, paytm, cash, wallet where we have to send payment method to done the transaction ...
                 * For custom order with quotation cases
                 * */
                if (paymentMethod == PAYTM.intValue && (paymentForFlow == PaymentConstants.PaymentForFlow.DEBT.intValue
                        || paymentForFlow == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue
                        || paymentForFlow == PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue)) {
                    map.remove("isEditedTask");
                    if (additionalpaymentId != null && !additionalpaymentId.isEmpty()) {
                        map.put("additionalpaymentId", additionalpaymentId);
//                map.put("job_id", jobId + "");
                    }
                    RestClient.getApiInterface(mActivity).createCharge(map).enqueue(createChargeResponse());
                } else {

                    if (paymentMethod == FAC.intValue) {
//            hashMap.put("cvv", cvv);
                        map.put("fac_payment_flow", "2");
                    }

                    if (additionalpaymentId != null && !additionalpaymentId.isEmpty() && paymentMethod == PAYTM_LINK.intValue) {
                        if (listener != null) {
                            listener.onTransactionSuccess("", "");
                        }
                    } else if (paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue) {
                        try {
                            map.remove("isEditedTask");
                            map.remove("subtotal_amount");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        RestClient.getApiInterface(mActivity).createCharge(map).enqueue(createChargeResponse());
                    } else {
                        RestClient.getApiInterface(mActivity).initatePayment(map).enqueue(getPaymentUrlResponse());
                    }
                }
        }
    }

    private Callback<BaseModel> createChargeResponse() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CreateChargeData data = baseModel.toResponseModel(CreateChargeData.class);
                if (listener != null) {
                    listener.onTransactionSuccess(data.getTransactionId(), "");
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                listener.onTransactionApiError();
            }
        };
    }

    private Callback<BaseModel> getPaymentUrlResponse() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                String paymentUrl = "";
                Intent intentPayment;
                int referenceId = 0;
                // String rPayReferenceId;


                if (isLockEnabled == 1) {
                    referenceId = (int) Math.round(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("reference_id").toString()));
                }


                if (paymentMethod == PAYU_LATAM.intValue) {
                    TransactionUrlData transactionUrlData = baseModel.toResponseModel(TransactionUrlData.class);
                    paymentUrl = transactionUrlData.getUrl(paymentMethod);

                    intentPayment = new Intent(mActivity, WebViewActivityPayuLatum.class);
                    intentPayment.putExtra(URL_WEBVIEW, paymentUrl);
                    intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.processing_payment));
                    mActivity.startActivityForResult(intentPayment, Codes.Request.OPEN_PAYULATAM_WEBVIEW_ACTIVITY);
                    AnimationUtils.forwardTransition(mActivity);

                } else if (paymentMethodData.getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW || paymentMethod == PAYFORT.intValue) {

                    if (paymentMethod == INSTAPAY.intValue) {
                        paymentUrl = baseModel.data.toString();
                    } else {
                        TransactionUrlData transactionUrlData = baseModel.toResponseModel(TransactionUrlData.class);
                        paymentUrl = transactionUrlData.getUrl(paymentMethod);
                    }

                    if (paymentMethod == RAZORPAY_UPI.intValue) {
                        rPayReferenceId = (int) Math.round(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("ref_id").toString()));
                        Prefs.with(mActivity).save(REF_ID,rPayReferenceId);
                        rPayJobExpiredReferenceId = (int) Math.round(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("reference_id").toString()));
                        rPayOrderId = ((LinkedTreeMap) baseModel.getData()).get("orderId").toString();
                        rezorpay_key_id = ((LinkedTreeMap)baseModel.getData()).get("key_id").toString();
                        //iniit Rpay
                        startPayment(mActivity, Double.parseDouble(map.get("amount")), map.get("currency"), rPayOrderId, rezorpay_key_id);
                        return;
                    }

                    if (paymentMethod == PAYTM_UPI.intValue) {
                        //iniit paytm upi
                        rPayReferenceId = (int) Math.round(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("ref_id").toString()));
                        rPayJobExpiredReferenceId = (int) Math.round(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("reference_id").toString()));

                        startPaymentPaytm(mActivity, Double.parseDouble(map.get("amount")), map.get("currency"),
                                ((LinkedTreeMap) baseModel.getData()).get("order_id").toString(),
                                ((LinkedTreeMap) baseModel.getData()).get("merchant_id").toString(),
                                ((LinkedTreeMap) baseModel.getData()).get("txnToken").toString(),
                                ((LinkedTreeMap) baseModel.getData()).get("callback_url").toString());
                        return;
                    }



                    intentPayment = new Intent(mActivity, ProcessingPaymentWebViewActivity.class);
                    intentPayment.putExtra(URL_WEBVIEW, paymentUrl);
                    intentPayment.putExtra(VALUE_PAYMENT, paymentMethod);
                    intentPayment.putExtra(PAYMENT_FOR_FLOW, paymentForFlow);
                    intentPayment.putExtra(IS_LOCK_ENABLED, isLockEnabled);
                    intentPayment.putExtra(EXTRA_REFERENCE_ID, referenceId);
                    intentPayment.putExtra(EXTRA_IS_LAUNDRY_EDIT_ORDER, map.get("isEditedTask"));
                    mActivity.startActivityForResult(intentPayment, Codes.Request.OPEN_PROCESSING_PAYMENT_ACTIVITY);
                    AnimationUtils.forwardTransition(mActivity);

                } else {
                    /*
                     * Common payment url method added for --> STRIPE, CASH, PAYTM, WALLET where for payment we don't have to redirect to processing screen
                     * */

                    if (listener != null) {
                        if (paymentForFlow==5 && (paymentMethod == AZUL.intValue || paymentMethod == AUTHORISE_DOT_NET.intValue)){
                            listener.onTransactionSuccess(((LinkedTreeMap) baseModel.getData()).get("transaction_id").toString(),"");

                        }else
                        listener.onTransactionSuccess("", "");
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                listener.onTransactionApiError();
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data.hasExtra(TRANSACTION_ID)) {
            if (listener != null)
                listener.onTransactionSuccess(data.getStringExtra(TRANSACTION_ID), data.getStringExtra(JOB_PAYMENT_DETAIL_ID) != null ? data.getStringExtra(JOB_PAYMENT_DETAIL_ID) : "");
        } else if (resultCode == RESULT_PAYMENT_ERROR) {
            if (listener != null) {
                listener.onTransactionFailure(data.getStringExtra(TRANSACTION_ID) != null ? data.getStringExtra(TRANSACTION_ID) : "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.finish();
                        mActivity.startActivity(mActivity.getIntent());
                    }
                },1000);
            }
        } else if (resultCode == mActivity.RESULT_CANCELED) {
            if (listener != null)
                listener.onTransactionFailure("");
        }
    }


    public void apiTransactionIDPaytm(String transactionId) {

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("transaction_id", transactionId);
        commonParams.add("reference_id", rPayReferenceId);


        RestClient.getJunglePaymentsApi(mActivity).sendTransactionIdPaytm(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                if (isOrderPayment) {
                    ((MakePaymentActivity) mActivity).onTransactionSuccess(transactionId, "");
                } else {
                    ((MakePaymentActivity) mActivity).apiHitToBuyPlan(transactionId);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Log.d("", "");

            }
        });
    }

    public void apiTransactionIDPaytmCustom(String transactionId) {

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("transaction_id", transactionId);
        commonParams.add("reference_id", rPayReferenceId);


        RestClient.getJunglePaymentsApi(mActivity).sendTransactionIdPaytm(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                if (mActivity instanceof WalletAddMoneyActivity) {
                    ((WalletAddMoneyActivity) mActivity).onTransactionSuccess(transactionId, "");
                } else if (mActivity instanceof GiftCardPaymentActivity) {
                    ((GiftCardPaymentActivity) mActivity).onTransactionSuccess(transactionId, "");

                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Log.d("", "");

            }
        });
    }

    private void startPayment(Activity mActivity, double amount, String currency, String orderId, String rezorpay_key_id) {
         /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */

        //int amountToSend = Integer.parseInt(String.valueOf(amount))*100;
        final Activity activity = mActivity;

        final Checkout co = new Checkout();
        co.setKeyID(rezorpay_key_id);

        try {
            JSONObject options = new JSONObject();
            options.put("name", storeName.isEmpty() ? StorefrontCommonData.getUserData().getData().getStoreName() : storeName);
            options.put("allow_rotation", true);
            options.put("order_id", orderId);//from response of step 3.

            //You can omit the image option to fetch the image from dashboard
            options.put("currency", currency);
            options.put("amount", Math.round(amount) * 100);
            options.put("timeout",290);

            JSONObject preFill = new JSONObject();
            preFill.put("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
            preFill.put("contact", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    public void apiJobExpired() {
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("order_number", rPayJobExpiredReferenceId);
        if (paymentMethod == RAZORPAY.intValue) {
            builder.add("is_razorpay_payment", 1);
        }

        if (isLockEnabled == 1) {
            RestClient.getApiInterface(mActivity).setJobExpired(builder.build().getMap())
                    .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                        @Override
                        public void success(BaseModel baseModel) {
                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {

                        }
                    });
        }
    }

    private void startPaymentPaytm(Activity mActivity, double amount, String currency, String mPayTmOrderId,
                                   String mMerchantId, String mTxnToken, String mCallbackUrl) {

        PaytmOrder paytmOrder = new PaytmOrder(mPayTmOrderId, mMerchantId, mTxnToken, String.valueOf(amount),
                mCallbackUrl
        );
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle bundle) {
                assert bundle != null;
                if (Objects.equals(bundle.getString("RESPCODE"), "01")) {
                    if (mActivity instanceof WalletAddMoneyActivity ||
                            mActivity instanceof GiftCardPaymentActivity) {
                        apiTransactionIDPaytmCustom(bundle.get("TXNID").toString());

                    } else {
                        apiTransactionIDPaytm(bundle.get("TXNID").toString());
                    }
                } else {
                    apiJobExpired();
                    Utils.snackBar(mActivity, "Your payment has failed");
                }
            }

            @Override
            public void networkNotAvailable() { }

            @Override
            public void onErrorProceed(String s) {
                apiJobExpired();
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                apiJobExpired();
            }

            @Override
            public void someUIErrorOccurred(String s) {
                apiJobExpired();
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                apiJobExpired();
            }

            @Override
            public void onBackPressedCancelTransaction() {
                apiJobExpired();
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                apiJobExpired();
            }
        });

      /*  if (!PaytmUtility.isPaytmAppInstalled(mActivity)) {
            transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage");
        }*/

        transactionManager.startTransaction(this.mActivity, OPEN_PAYTM_SDK);

    }

}

