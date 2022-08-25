package com.tookancustomer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tookancustomer.adapters.BillBreakdownAdapter;
import com.tookancustomer.adapters.NewPromosAdapter;
import com.tookancustomer.adapters.PaymentListAdapter;
import com.tookancustomer.adapters.TipAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.callback.PaytmCheckBalanceCallback;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.RecuringSurgeDetailsDialog;
import com.tookancustomer.dialog.UnavailableProductsDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.CreateTaskResponse;
import com.tookancustomer.models.PaytmResponse;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.models.SendPaymentTask.SendPaymentForTask;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.UnavailableProductData.UnavailableProductData;
import com.tookancustomer.models.appConfiguration.MappedPages;
import com.tookancustomer.models.billbreakdown.BillBreakDowns;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.models.billbreakdown.Data;
import com.tookancustomer.models.billbreakdown.OnSubTotal;
import com.tookancustomer.models.billbreakdown.OnTotal;
import com.tookancustomer.models.billbreakdown.TipModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.paymentMethodData.PaytmData;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.models.userDebt.UserDebtData;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.customerSubscription.PlanList;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.modules.payment.PaymentTransactionUrl;
import com.tookancustomer.modules.payment.PaymentTransactionUrlManager;
import com.tookancustomer.modules.payment.callbacks.FetchCardsListener;
import com.tookancustomer.modules.payment.callbacks.PaytmBalanceListener;
import com.tookancustomer.modules.payment.callbacks.TransactionUrlListener;
import com.tookancustomer.modules.payment.callbacks.WalletBalanceListener;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.modules.reward.activity.PlanDetailsActivity;
import com.tookancustomer.modules.reward.model.CreateChargeData;
import com.tookancustomer.payulatam.Payulatam;
import com.tookancustomer.payulatam.PayulatamPaymentManager;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.PaySlider;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Callback;

import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AUTHORISE_DOT_NET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AZUL;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.BILLPLZ;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.FAC;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INAPP_WALLET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INSTAPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.MPAISA;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFAST;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYMOB;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYPAL;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_LINK;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_LATER;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_ON_DELIVERY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY_UPI;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.STRIPE;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.getPaymentString;

public class MakePaymentActivity extends BaseActivity implements View.OnClickListener, Keys.Extras, Keys.APIFieldKeys, Keys.MetaDataKeys,
        PaytmCheckBalanceCallback, Payulatam.PayulatamListener, TransactionUrlListener, PaymentListAdapter.OnPaymentOptionSelectedListener, PaymentResultWithDataListener {
    public static final int REQUEST_CODE_TO_OPEN_PAYMENT_PAGE = 1002;
    public BillBreakDowns billBreakDown;
    public boolean isCardSelected;
    public Integer adapterPos;
    public String selectedCardId;
    public String currencySymbol;
    public BigDecimal actualAmount;
    boolean hitFetchMerchantCard = true;
    Dialog thankYouDialog;
    boolean isOpenWalletAddMoneyActivity = false;
    private ArrayList<PromosModel> promoList = new ArrayList<>();
    private Integer promoID;
    private String referralCode, promoCode;
    private TextView ivAddPaymentOptions, tvAvailableValue, tvMaxPointsValue, editOrderNoteTV;
    private Dialog phoneNumberDialog;
    private LinearLayout llPaymentParentLayout;
    //tip
    private double tip = 0.0;
    private int points = 0, tempLoyaltyPoints = 0;
    private NewPromosAdapter promosAdapter;
    private ArrayList<Datum> paymentList = new ArrayList<>();
    //Promo
    private RecyclerView promosRV;
    private TextView tvAddAnotherPromo;
    private LinearLayout llPromoCodeView;
    //bill
    private TextView tvTotal, tvRecurringTotal, tvRecurringTotalHeading;
    private TextView tvAmountPrice, tvNoPaymentCardFound;
    private RecyclerView rvPaymentCardList, rvTaxesList;
    private BillBreakdownAdapter taxAdapter;
    private HashMap<String, String> hashMap;
    private HashMap<String, String> hashMapLocations;
    private HashMap<String, String> hashMap3;
    private SendPaymentForTask sendPayment;
    private String subtotalAmount = "0";
    private PaymentListAdapter mAdapter;
    private LinearLayout llAddTipView, llAddPointsView;
    private RecyclerView rvTipArraylist;
    private TipAdapter tipAdapter;
    private LinearLayout llEditableTipParent;
    private EditText etTipManual, etPointsManual;
    private TextView tvApplyTip, tvCurrencySymbol, tvApplyPoints, tvCurrencySymbolPoints;
    private ArrayList<TipModel> tipArraylist = new ArrayList<>();
    private int tipType;
    private int selectedTipPos = -1;
    private TextWatcher tipTextWatcher;
    private Integer paytmVerified = null;
    private String paytmAddMoneyUrl;
    private Double paytmWalletAmount = 0.0;
    private TextView deliverySurgeChargesTV;
    private long valuePaymentMethod = 0;
    private boolean isPickupAnywhere;
    private boolean isCustomOrder;
    private String latitude = "", longitude = "";
    private String jobPickupAddress = "";
    private PaySlider paySlider;
    /**
     * for checkout template flow
     */
    private ArrayList<Template> templateDataList;
    private String successString = "";
    private boolean sendPoints = false;
    private ArrayList<TipModel> tipModelArrayList = new ArrayList<>();
    private JSONObject payViaHippoObject;
    private TaskData payViaTaskDetailsObject;
    private ArrayList<TaxesModel> hippoTaxesArrayList = new ArrayList<>();
    private boolean returnFromPaytm = false;
    private RelativeLayout rlHoldPayment;
    private TextView tvHoldPayment, btnAcceptHold;
    private boolean isOrderPayment = true;
    private long paymentFor = 0;
    private UserDebtData userDebtData;
    private PlanList planList;
    private ArrayList<MappedPages> mappedPages = new ArrayList<>();
    private boolean isRecurringTaskEnable = false;
    private ArrayList<PaymentMethod> paymentMethodsViaHippo;
    private int hippoUseId;
    private String merchantUserId = "";
    private boolean isCustomOrderMerchantLevel;
    private boolean hippoIsCustomOrder = false;
    private boolean isEditOrder;
    private int editJobId, sochitelOperatorId;
    private int rewardPaymentId = 0;
    private boolean isPdFlow = false, isSelfPickUp = false;
    private ArrayList<OnSubTotal> newBillBreakdownArrayListSubTotal = new ArrayList<>();
    private ArrayList<OnTotal> newBillBreakdownArrayListTotal = new ArrayList<>();
    private String oldDeliveryCharge = "";

    private String storename = "";

    public static Bundle convertJsonToBundle(JSONObject json) {
        Bundle bundle = new Bundle();
        try {
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = json.get(key);
                switch (value.getClass().getSimpleName()) {
                    case "String":
                        bundle.putString(key, (String) value);
                        break;
                    case "Integer":
                        bundle.putInt(key, (Integer) value);
                        break;
                    case "Long":
                        bundle.putLong(key, (Long) value);
                        break;
                    case "Boolean":
                        bundle.putBoolean(key, (Boolean) value);
                        break;
                    case "JSONObject":
                        bundle.putBundle(key, convertJsonToBundle((JSONObject) value));
                        break;
                    case "Float":
                        bundle.putFloat(key, (Float) value);
                        break;
                    case "Double":
                        bundle.putDouble(key, (Double) value);
                        break;
                    default:
                        bundle.putString(key, value.getClass().getSimpleName());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        mActivity = this;
        valuePaymentMethod = PaymentMethodsClass.getEnabledPaymentMethod();

        payViaHippoObject = getPayViaHippoObject();
        if (payViaHippoObject != null) {
            isCustomOrder = true;
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey(IS_PD_FLOW)) {
            isPdFlow = bundle.getBoolean(IS_PD_FLOW);
        }

        if (bundle.containsKey(IS_SELF_PICKUP)) {
            isSelfPickUp = bundle.getBoolean(IS_SELF_PICKUP);
        }

        storename = getIntent().getStringExtra("storename");

        if (intent.hasExtra(TASK_DETAILS)) {
            payViaTaskDetailsObject = (TaskData) intent.getSerializableExtra(TASK_DETAILS);
        }
        if (intent.hasExtra("paymentForFlow")) {
            paymentFor = getIntent().getLongExtra("paymentForFlow", 0);
        }
        if (getIntent().hasExtra("userDebtData")) {
            userDebtData = (UserDebtData) intent.getSerializableExtra("userDebtData");
        }
        if (getIntent().hasExtra("subscriptionPlan")) {
            planList = (PlanList) intent.getSerializableExtra("subscriptionPlan");
        }

        if (getIntent().hasExtra(OLD_DELIVERY_CHARGE)) {
            oldDeliveryCharge = getIntent().getStringExtra(OLD_DELIVERY_CHARGE);
        }


        isEditOrder = getIntent().getBooleanExtra(IS_EDIT_ORDER, false);

        if (isEditOrder)
            editJobId = getIntent().getIntExtra(EDIT_JOB_ID, 0);

        sochitelOperatorId = getIntent().getIntExtra(SOCHITEL_OPERATOR, 0);


        if (payViaHippoObject == null && payViaTaskDetailsObject == null && paymentFor == 0)
            if (getIntent().getExtras() != null) {
                if (bundle.getSerializable(CheckOutCustomActivity.class.getName()) != null)
                    hashMap = (HashMap<String, String>) bundle.getSerializable(CheckOutCustomActivity.class.getName());
                else
                    hashMap = (HashMap<String, String>) bundle.getSerializable(CheckOutActivity.class.getName());

                if (getIntent().hasExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST))
                    templateDataList = (ArrayList<Template>) intent.getSerializableExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST);

                if (getIntent().hasExtra(TIP_OPTION_LIST))
                    tipModelArrayList = (ArrayList<TipModel>) intent.getSerializableExtra(TIP_OPTION_LIST);

                if (getIntent().hasExtra(SEND_PAYMENT_FOR_TASK)) {
                    sendPayment = bundle.getParcelable(SEND_PAYMENT_FOR_TASK);
                    subtotalAmount = sendPayment.getData().getPerTaskCost();
                }

                isCustomOrder = intent.getBooleanExtra("isCustomOrder", false);
                isCustomOrderMerchantLevel = intent.getBooleanExtra("isCustomOrderMerchantLevel", false);
                merchantUserId = intent.getStringExtra("merchantUserId");
//                rewardId = getIntent().getIntExtra("reward_id", -1);
                isPickupAnywhere = intent.getBooleanExtra("isPickupAnywhere", true);

                if (hashMap != null && hashMap.containsKey(JOB_PICKUP_LATITUDE))
                    latitude = hashMap.get(JOB_PICKUP_LATITUDE);
                if (hashMap != null && hashMap.containsKey(JOB_PICKUP_LONGITUDE))
                    longitude = hashMap.get(JOB_PICKUP_LONGITUDE);
                if (hashMap != null && hashMap.containsKey(JOB_PICKUP_ADDRESS))
                    jobPickupAddress = hashMap.get(JOB_PICKUP_ADDRESS);
            }


        if (hashMap != null && hashMap.containsKey("is_recurring_enabled") && hashMap.get("is_recurring_enabled").equalsIgnoreCase("1")) {
            isRecurringTaskEnable = true;
        } else {
            isRecurringTaskEnable = false;
        }


        if (isCustomOrder) {
            tip = 0;
        } else if (Dependencies.getSelectedProductsArrayList().size() > 0) {
//                && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getSelectedPickupMode() != Constants.SelectedPickupMode.SELF_PICKUP)
//                {
            if (StorefrontCommonData.getAppConfigurationData().getTipEnableDisable() == 1
                    && StorefrontCommonData.getAppConfigurationData().getEnableDefaultTip() == 1
                    && StorefrontCommonData.getAppConfigurationData().getMinimumTip() == 0
                    && tipModelArrayList.size() > 0) {
                double totalTipSum = 0.0;
                for (TipModel tipModel : tipModelArrayList) {
                    totalTipSum = totalTipSum + tipModel.getAmount().doubleValue();
                }
                totalTipSum = totalTipSum / tipModelArrayList.size();
                for (TipModel tipModel : tipModelArrayList) {
                    if (totalTipSum >= tipModel.getAmount().doubleValue() && tip <= tipModel.getAmount().doubleValue()) {
                        tip = tipModel.getAmount().doubleValue();
                    }
                }
            } else {
                if (StorefrontCommonData.getAppConfigurationData().getMinimumTipType() == 2) {
                    tip = StorefrontCommonData.getAppConfigurationData().getMinimumTip();
                } else {
//                    tip = Double.valueOf(Utils.getDoubleTwoDigits((StorefrontCommonData.getAppConfigurationData().getMinimumTip() * Double.parseDouble(Utils.getDoubleTwoDigits(Double.parseDouble(subtotalAmount)))) / 100));
                    tip = Double.valueOf(Utils.getDoubleTwoDigits(Double.valueOf(StorefrontCommonData.getAppConfigurationData().getMinimumTip() / 100)
                            *
                            Double.parseDouble(Utils.getDoubleTwoDigits(Double.parseDouble(subtotalAmount)))));

                }
            }
        } else {
            tip = 0;
        }


        initializeData();
        etTipManual.setText(String.valueOf(tip));

        paySlider = new PaySlider(findViewById(R.id.rlSliderContainer)) {
            @Override
            public void onPayClick() {
                try {
                    tvAmountPrice.performClick();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                } catch (Exception e) {
                    paySlider.setSlideInitial();
                }
            }
        };


        if (UIManager.isMerchantPaymentMethodsEnabled()) {
            paymentMethodsCheck();
        }
    }

    private void paymentMethodsCheck() {
        if (payViaTaskDetailsObject != null) {
            PaymentManager.setCustomPaymentMethods(payViaTaskDetailsObject.getPaymentMethods());
        } else if (payViaHippoObject != null) {
            PaymentManager.setCustomPaymentMethods(paymentMethodsViaHippo);
        } else if (isCustomOrderMerchantLevel && getIntent().hasExtra("merchantPaymentMethods")) {
            PaymentManager.setCustomPaymentMethods((List<PaymentMethod>) getIntent().getSerializableExtra("merchantPaymentMethods"));
        } else if (isCustomOrder || userDebtData != null || planList != null) {

            PaymentManager.setCustomPaymentMethods(StorefrontCommonData.getUserData().getData().getFormSettings().get(0).getPaymentMethods());

        } else {
            PaymentManager.setCustomPaymentMethods(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPaymentMethods());

        }
    }

    private void setBillBreakDownsWhenPayViaHippo() {

        //Subtotal :
        //---------------------------------
        //- discount(subtotal)
        //---------------------------------
        //+ Additional_charges(subtotal):
        //---------------------------------
        //Taxable Amount :
        //---------------------------------
        //+ tax
        //---------------------------------
        //+ additional charges (Total):
        //---------------------------------
        //+ tip
        //---------------------------------
        //+ delivery charge
        //---------------------------------
        //- discount(delivery charge)
        //---------------------------------
        //- loyalty point
        //---------------------------------
        //Total :
        ArrayList<BillBreakdownData> billBreakdownArrayList = new ArrayList<>();
        billBreakDown = new BillBreakDowns();
        try {
            if (payViaHippoObject.has("remaining_balance") &&
                    payViaHippoObject.getDouble("remaining_balance") > 0) {
              /*  TaxesModel subtotalData = new TaxesModel(getStrings(R.string.subtotal),
                        new BigDecimal(payViaHippoObject.getDouble("remaining_balance")), null);
                billBreakdownArrayList.add(subtotalData);*/
                billBreakdownArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal),
                        new BigDecimal(payViaHippoObject.getDouble("remaining_balance")), null));

                tvTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(payViaHippoObject.getDouble("remaining_balance")))));
                billBreakDown.setData(new Data(null,
                        null,
                        new BigDecimal(payViaHippoObject.getDouble("remaining_balance"))));

            } else {

                //  String taxName, BigDecimal taxAmount, boolean isDiscount, String description, String currencySymbol

                if (payViaHippoObject.has("actual_amount")) {
                    billBreakdownArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal),
                            new BigDecimal(payViaHippoObject.getDouble("actual_amount")),
                            false, null, currencySymbol, null));
                    //   billBreakdownArrayList.add(subtotalData);
                }
                if (hippoTaxesArrayList != null && !hippoTaxesArrayList.isEmpty()) {
                    for (int i = 0; i < hippoTaxesArrayList.size(); i++)
                        billBreakdownArrayList.add(new BillBreakdownData(hippoTaxesArrayList.get(i).getTaxName(),
                                new BigDecimal(hippoTaxesArrayList.get(i).getTaxAmount()),
                                false, hippoTaxesArrayList.get(i).getTaxPercentage(),
                                hippoTaxesArrayList.get(i).getTaxType(),
                                hippoTaxesArrayList.get(i).getTaxDiscount()));
                }

                if (payViaHippoObject.has("delivery_charge")) {
                    /*TaxesModel deliveryData = new TaxesModel(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                            new BigDecimal(payViaHippoObject.getDouble("delivery_charge")),
                            false, null, currencySymbol, null);
                    billBreakdownArrayList.add(deliveryData);*/

                    billBreakdownArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                            new BigDecimal(payViaHippoObject.getDouble("delivery_charge")),
                            false, null, currencySymbol, null));
                }

                tvTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(currencySymbol) + Utils.getDoubleTwoDigits(payViaHippoObject.getDouble("total_amount")))));
                billBreakDown.setData(new Data(null,
                        null,
                        new BigDecimal(payViaHippoObject.getDouble("total_amount"))));

            }

            taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownArrayList);
            rvTaxesList.setAdapter(taxAdapter);


        } catch (JSONException e) {

            Utils.printStackTrace(e);
        }
    }

    private void setBillBreakDownsWhenPayViaTaskDetails() {
        ArrayList<BillBreakdownData> billBreakdownArrayList = new ArrayList<>();
        billBreakDown = new BillBreakDowns();
        billBreakDown.setData(new Data(null, null, new BigDecimal(payViaTaskDetailsObject.getTotalAmount())));

        tvTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(payViaTaskDetailsObject.getOrderCurrencySymbol()) + Utils.getDoubleTwoDigits(Double.valueOf(payViaTaskDetailsObject.getTotalAmount())))));

        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownArrayList);
        rvTaxesList.setAdapter(taxAdapter);
    }

    private void setBillBreakDownsWhenPayForDebt() {
        ArrayList<BillBreakdownData> billBreakdownArrayList = new ArrayList<>();
        billBreakDown = new BillBreakDowns();

        tvTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount()))));
        billBreakDown.setData(new Data(null, null, new BigDecimal(StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount())));

        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownArrayList);
        rvTaxesList.setAdapter(taxAdapter);
    }

    private void setBillBreakDownsWhenPayForCustomerSubscription() {
        ArrayList<BillBreakdownData> billBreakdownArrayList = new ArrayList<>();
        billBreakDown = new BillBreakDowns();

        tvTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(planList.getAmount()))));
        billBreakDown.setData(new Data(null, null, new BigDecimal(planList.getAmount())));

        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownArrayList);
        rvTaxesList.setAdapter(taxAdapter);
    }

    private JSONObject getPayViaHippoObject() {
        if (getIntent().hasExtra("PAYMENT_VIA_HIPPO_DATA")) {
            String jsonString = getIntent().getStringExtra("PAYMENT_VIA_HIPPO_DATA");
            hippoTaxesArrayList = (ArrayList<TaxesModel>) getIntent().getSerializableExtra("HIPPO_TAXES_LIST");
            paymentMethodsViaHippo = (ArrayList<PaymentMethod>) getIntent().getSerializableExtra("PAYMENT_METHODS");
            hippoUseId = getIntent().getIntExtra("USER_ID", -1);
            currencySymbol = getIntent().getStringExtra("CURRENCY_SYMBOL");
            currencySymbol = getIntent().getStringExtra("CURRENCY_SYMBOL");

            try {
                payViaHippoObject = new JSONObject(jsonString);

                if (payViaHippoObject.has("reward_id")) {
                    rewardPaymentId = payViaHippoObject.getInt("reward_id");
                }

                if (payViaHippoObject.has("order_id") && payViaHippoObject.getInt("order_id") == 0) {
                    isOrderPayment = false;
                }
                try {
                    hippoIsCustomOrder = payViaHippoObject.getInt("is_custom_order") == 1;
                    currencySymbol = payViaHippoObject.getJSONObject("currencyObj").getString("symbol");
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                return payViaHippoObject;
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }

        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        paySlider.setSlideInitial();
        getPaymentMethods();


    }

    private void initializeData() {
        ((TextView) findViewById(R.id.tvSlide)).setText(StorefrontCommonData.getTerminology().getPay(true));


        ((TextView) findViewById(R.id.sliderText)).setText(getStrings(R.string.swipe_to_confirm));

        ((TextView) findViewById(R.id.tvTotalHeading)).setText(getStrings(R.string.total));

        ((TextView) findViewById(R.id.tvPromoHeader)).setText(getStrings(R.string.add_promo));
        ((TextView) findViewById(R.id.tvAddAnotherPromo)).setText(getStrings(R.string.add_a_promo_referral_code));

        ((TextView) findViewById(R.id.tvTipHeader)).setText(StorefrontCommonData.getTerminology().getTip());
        ((TextView) findViewById(R.id.tvPointsHeader)).setText(StorefrontCommonData.getTerminology().getLoyaltyPoints());
        ((TextView) findViewById(R.id.etTipManual)).setHint(getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getTip()));
        ((TextView) findViewById(R.id.etPointsManual)).setHint(getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getLoyaltyPoints()));

        rvPaymentCardList = findViewById(R.id.rvPaymentCardList);
        editOrderNoteTV = findViewById(R.id.editOrderNoteTV);
        tvAvailableValue = findViewById(R.id.tvAvailableValue);
        tvMaxPointsValue = findViewById(R.id.tvMaxPointsValue);
        deliverySurgeChargesTV = findViewById(R.id.deliverySurgeChargesTV);
        llPaymentParentLayout = findViewById(R.id.llPaymentParentLayout);
        deliverySurgeChargesTV.setText(getStrings(R.string.view_delivery_surge_charges).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()));

        if (isEditOrder) {
            ((TextView) findViewById(R.id.tvSlide)).setText(R.string.confirm);
            llPaymentParentLayout.setVisibility(View.GONE);
            editOrderNoteTV.setVisibility(View.VISIBLE);
        }

        rvTaxesList = findViewById(R.id.rvTaxesList);
        ((TextView) findViewById(R.id.tvHeading)).setText(StorefrontCommonData.getTerminology().getPayment(true));
        ivAddPaymentOptions = findViewById(R.id.ivAddPaymentOptions);
        ivAddPaymentOptions.setText(getStrings(R.string.add_card));
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvPaymentCardList.setNestedScrollingEnabled(false);
        rvTaxesList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvTaxesList.setNestedScrollingEnabled(false);
        tvAmountPrice = findViewById(R.id.tvAmountPrice);
        tvNoPaymentCardFound = findViewById(R.id.tvNoPaymentCardFound);
        tvNoPaymentCardFound.setText(getStrings(((userDebtData != null) && (valuePaymentMethod == CASH.intValue
                || valuePaymentMethod == PAY_LATER.intValue
                || valuePaymentMethod == PAYTM_LINK.intValue || valuePaymentMethod == PAY_ON_DELIVERY.intValue)) ? R.string.contact_to_admin_to_clear_debt : R.string.no_payment_card_found));
        //Promo
        llPromoCodeView = findViewById(R.id.llPromoCodeView);
        promosRV = findViewById(R.id.promosRV);
        promosRV.setLayoutManager(new LinearLayoutManager(mActivity));
        promosRV.setNestedScrollingEnabled(false);
        tvAddAnotherPromo = findViewById(R.id.tvAddAnotherPromo);

        //tip
        llAddTipView = findViewById(R.id.llAddTipView);
        llAddPointsView = findViewById(R.id.llAddPointsView);
        rvTipArraylist = findViewById(R.id.rvTipArraylist);
        rvTipArraylist.setLayoutManager(new GridLayoutManager(mActivity, 4));

        if (hashMap != null && hashMap.containsKey("currency_symbol")) {
            currencySymbol = hashMap.get("currency_symbol");
        } else {
            if (payViaTaskDetailsObject != null)
                currencySymbol = payViaTaskDetailsObject.getOrderCurrencySymbol();
        }

        tipAdapter = new TipAdapter(mActivity, tipArraylist, tipType, currencySymbol, new TipAdapter.Callback() {
            @Override
            public void onTipSelected(TipModel tipModel) {
                etTipManual.removeTextChangedListener(tipTextWatcher);
                etTipManual.setText(Utils.getDoubleTwoDigits(Double.parseDouble(tipModel == null ? "0.00" : tipModel.getAmountString())) + "");
                etTipManual.addTextChangedListener(tipTextWatcher);
                if (!isCustomOrder) {
                    if (Double.parseDouble(etTipManual.getText().toString()) < billBreakDown.getData().getMinimumTip()) {
                        Utils.snackBar(mActivity, getStrings(R.string.minimum_tip_amount_is_amount).replace(TIP, StorefrontCommonData.getTerminology().getTip())
                                .replace(AMOUNT, Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + Utils.getDoubleTwoDigits(billBreakDown.getData().getMinimumTip())));
                    } else {
                        tip = Double.valueOf(etTipManual.getText().toString());
                        getBillBreakdowns();
                    }


                }
            }
        });
        rvTipArraylist.setAdapter(tipAdapter);
        llEditableTipParent = findViewById(R.id.llEditableTipParent);
        etTipManual = findViewById(R.id.etTipManual);
        etPointsManual = findViewById(R.id.etPointsManual);
        tvCurrencySymbol = findViewById(R.id.tvCurrencySymbol);
        tvCurrencySymbolPoints = findViewById(R.id.tvCurrencySymbolPoints);
        tipTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tipAdapter != null) {
                    tipAdapter.selectedItemPos = -1;
                    tipAdapter.notifyDataSetChanged();
                }
            }
        };

        etTipManual.addTextChangedListener(tipTextWatcher);
        tvApplyTip = findViewById(R.id.tvApplyTip);
        tvApplyPoints = findViewById(R.id.tvApplyPoints);
        tvApplyPoints.setText(getStrings(R.string.submit));
        tvApplyTip.setText(getStrings(R.string.submit));

        //bill
        ((TextView) findViewById(R.id.tvBillHeader)).setText(StorefrontCommonData.getTerminology().getBillSummary());
        tvTotal = findViewById(R.id.tvTotal);
        tvRecurringTotal = findViewById(R.id.tvRecurringTotal);
        tvRecurringTotalHeading = findViewById(R.id.tvRecurringTotalHeading);
        ((TextView) findViewById(R.id.tvPaymentMethodLabel)).setText(StorefrontCommonData.getTerminology().getPaymentMethod());

        rlHoldPayment = findViewById(R.id.rlHoldPayment);
        tvHoldPayment = findViewById(R.id.tvHoldPayment);
        btnAcceptHold = findViewById(R.id.btnAcceptHold);

        btnAcceptHold.setText(getStrings(R.string.okay_got_it));

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.ivAddPaymentOptions),
                tvAmountPrice, tvAddAnotherPromo, tvApplyTip, tvApplyPoints, btnAcceptHold, deliverySurgeChargesTV);
    }

    public void getBillBreakdowns() {
        getBillBreakdowns(promoID, referralCode, promoCode);
    }

    public void getBillBreakdowns(final Integer promoID, final String referralCode,
                                  final String promoCode) {
        Log.e("getbillbreakdown api", ">>>>>>>>>>>>>>>>>>  getbillbreakdown api");

        if (isCustomOrder)
            hashMap.put("amount", 0 + "");
        else {
            hashMap.put("amount", Double.parseDouble(subtotalAmount + "") + "");
            if (tip != 0.0) {
                hashMap.put("tip", this.tip + "");
                hashMap.put("tip_type", "2");
            } else {
                hashMap.remove("tip");
                hashMap.remove("tip_type");
            }

            if (sendPoints && tempLoyaltyPoints != 0) {
                hashMap.put("loyalty_points", tempLoyaltyPoints + "");
            } else {
                hashMap.remove("loyalty_points");
            }
        }

        if (promoID != null) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_code");
            hashMap.put("promo_id", promoID + "");
        } else if (referralCode != null && !referralCode.isEmpty()) {
            hashMap.remove("promo_code");
            hashMap.remove("promo_id");
            hashMap.put("referral_code", referralCode);
        } else if (promoCode != null && !promoCode.isEmpty()) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.put("promo_code", promoCode);
        } else {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.remove("promo_code");
        }
//        hashMap.put("discount", billBreakDown != null ? billBreakDown.getData().getDiscount() : "0");
//        hashMap.put("currency_id", Utils.getCurrencyId());

        if (isCustomOrder) {


            if (UIManager.isCustomQuotationEnabled()) {
                hashMap.put("is_custom_order", "2");
            } else {
                hashMap.put("is_custom_order", "1");
            }
            if (isPickupAnywhere) {
                hashMap.put("is_pickup_anywhere", "1");
                hashMap.remove("job_pickup_latitude");
                hashMap.remove("job_pickup_longitude");
                hashMap.remove(JOB_PICKUP_ADDRESS);
            }
        } else {
            // hashMap.put("latitude", hashMap.get(JOB_PICKUP_LATITUDE));
            //  hashMap.put("longitude", hashMap.get(JOB_PICKUP_LONGITUDE));
            if (hashMap.get(JOB_PICKUP_LATITUDE) == null) {
                hashMap.put("latitude", latitude);
                hashMap.put("longitude", longitude);
            } else {
                hashMap.put("latitude", hashMap.get(JOB_PICKUP_LATITUDE));
                hashMap.put("longitude", hashMap.get(JOB_PICKUP_LONGITUDE));
            }

            if (isPdFlow) {
                hashMap.put("customer_address", hashMap.get(CUSTOME_PICKUP_ADDRESS));
            } else {
                hashMap.put("customer_address", hashMap.get(JOB_PICKUP_ADDRESS));
            }
        }

        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (isCustomOrder) {
            if (isCustomOrderMerchantLevel) {
                hashMap.put("user_id", merchantUserId);
            } else {
                hashMap.put(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");

            }
        }


        if (templateDataList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
            hashMap.put("checkout_template", String.valueOf(myCustomArray));
        }
        if (!hashMap.containsKey("customer_address")) {
            if (hashMap.containsKey(JOB_PICKUP_ADDRESS))
                hashMap.put(JOB_PICKUP_ADDRESS, hashMap.get(JOB_PICKUP_ADDRESS));
            else if (hashMap.containsKey(CUSTOME_PICKUP_ADDRESS)) {
                hashMap.put(CUSTOME_PICKUP_ADDRESS, hashMap.get(CUSTOME_PICKUP_ADDRESS));
            }
        }

        if (isEditOrder && editJobId > 0)
            hashMap.put(PREV_JOB_ID, editJobId + "");


//        //TODO
//        boolean is_recurring_task=true;
//        if(is_recurring_task){
//            hashMap.put("is_recurring_task","1");
//        }

        if (!oldDeliveryCharge.isEmpty()) {
            hashMap.put("old_delivery_charge", oldDeliveryCharge);
        }


        if (isPdFlow) {
            hashMap.put("job_pickup_latitude", hashMap.get("custom_pickup_latitude"));
            hashMap.put("job_pickup_longitude", hashMap.get("custom_pickup_longitude"));
            hashMap.put("latitude", hashMap.get("custom_pickup_latitude"));
            hashMap.put("longitude", hashMap.get("custom_pickup_longitude"));
            hashMap.put("custom_pickup_latitude", latitude);
            hashMap.put("custom_pickup_longitude", longitude);
        }
        hashMapLocations = new HashMap<>();
        if (isPdFlow) {
            hashMapLocations.put("job_pickup_latitude", hashMap.get("job_pickup_latitude"));
            hashMapLocations.put("job_pickup_longitude", hashMap.get("job_pickup_longitude"));
            hashMapLocations.put("latitude", hashMap.get("job_pickup_latitude"));
            hashMapLocations.put("longitude", hashMap.get("job_pickup_longitude"));
            hashMapLocations.put("custom_pickup_latitude", latitude);
            hashMapLocations.put("custom_pickup_longitude", longitude);
        }


        RestClient.getApiInterface(this).getBillBreakDown(hashMap)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {

                        Log.e("getbillbreakdown api", ">>>>>>>>>>>>>>>>>>  getbillbreakdown sucess");

                        BillBreakDowns billBreakDowns = new BillBreakDowns();
                        try {
                            billBreakDowns.setData(baseModel.toResponseModel(com.tookancustomer.models.billbreakdown.Data.class));
                        } catch (Exception e) {
//
                            Utils.printStackTrace(e);
                        }
                        billBreakDown = billBreakDowns;
                        if (oldDeliveryCharge.isEmpty()) {
                            oldDeliveryCharge = billBreakDown.getData().getDeliveryCharge();
                        }

                        setPromoReferalCode(promoID, referralCode, promoCode);

                        promoList.clear();

                        if (billBreakDown.getData().getPromosArray() != null) {
                            for (int i = 0; i < billBreakDown.getData().getPromosArray().size(); i++) {
                                PromosModel promosModel = billBreakDown.getData().getPromosArray().get(i);
                                if ((promosModel.getPromoId() + "").equals(promoID + "") || (promosModel.getPromoCode() + "").equalsIgnoreCase(promoCode)) {
                                    promosModel.setSelected(true);
                                } else {
                                    promosModel.setSelected(false);
                                }
                                promoList.add(promosModel);
                            }
                        }

                        for (int i = 0; i < billBreakDown.getData().getReferralArray().size(); i++) {
                            PromosModel promosModel = billBreakDown.getData().getReferralArray().get(i);
                            if ((promosModel.getPromoCode()).equalsIgnoreCase(referralCode) || (promosModel.getPromoCode()).equalsIgnoreCase(promoCode)) {
                                promosModel.setSelected(true);
                            } else {
                                promosModel.setSelected(false);
                            }
                            promoList.add(promosModel);
                        }
                        promosAdapter = new NewPromosAdapter(mActivity, promoList);
                        promosRV.setAdapter(promosAdapter);
                        if (isCustomOrder || isRecurringTaskEnable)
                            llPromoCodeView.setVisibility(View.GONE);
                        else
                            llPromoCodeView.setVisibility((promoList.size() > 0
                                    || StorefrontCommonData.getUserData().getData().getReferral().getStatus() == 1
                                    || billBreakDown.getData().isShowPromoBtn()) ? View.VISIBLE : View.GONE);

                        billBreakDown.getData().setPaybleAmountBeforeDiscount(billBreakDowns.getData().paybleAmount);
                        if (tipAdapter != null) selectedTipPos = tipAdapter.selectedItemPos;
                        if (etTipManual.getText().toString().isEmpty()) {
                            etTipManual.setText(Utils.getDoubleTwoDigits(billBreakDown.getData().getMinimumTip()));
                        }
                        tvCurrencySymbol.setText(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + " ");
                        if (etPointsManual.getText().toString().isEmpty()) {
                            etPointsManual.setText(0 + "");
                            tvCurrencySymbolPoints.setText(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + " ");
                        }
                        Dependencies.setWalletBalance(billBreakDown.getData().getWalletDetails().getWalletBalance());
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            /**
                             * if payment list is already available
                             * and bill breakdown is hit later than show amount to be holded
                             * if payfort card is selected
                             */
                            onPaymentOptionSelected(paymentList.get(adapterPos));
                        }

                        setBillBreakDowns();
                    }

                    @Override
                    public void failure(APIError error, final BaseModel baseModel) {


                        if (billBreakDown == null) {
                            if (baseModel != null) {
                                try {

                                    if (((LinkedTreeMap) baseModel.data).containsKey("debt_amount")) {

                                        new AlertDialog.Builder(mActivity).message(error.getMessage()).listener(new AlertDialog.Listener() {
                                            @Override
                                            public void performPostAlertAction(int purpose, Bundle backpack) {
                                                StorefrontCommonData.getUserData().getData().getVendorDetails().setDebtAmount(Double.parseDouble(((LinkedTreeMap) baseModel.data).get("debt_amount").toString()));
                                                Bundle extras = new Bundle();
                                                Transition.transitForResult(mActivity, UserDebtActivity.class, Codes.Request.OPEN_TASK_DETAIL_ACTIVITY, extras, false);
//                                                onBackPressed();
                                            }
                                        }).build().show();
                                    } else {
                                        onBackPressed();
                                    }
                                } catch (WindowManager.BadTokenException badTokenException) {
//                                    badTokenException.printStackTrace();
                                    onBackPressed();
                                }
                            } else {
                                onBackPressed();
                            }

                        } else {
                            Utils.snackBar(mActivity, error.getMessage());
                        }
                        tempLoyaltyPoints = points;


                    }


                });
    }

    public void setBillBreakDowns() {
        Log.e("setbillbreakdown api", ">>>>>>>>>>>>>>>>>>  setbillbreakdown sucess");

        if (billBreakDown.getData() != null && billBreakDown.getData().getPrevJobAmount() != null && billBreakDown.getData().getPrevJobAmount().compareTo(BigDecimal.ZERO) > 0) {
            if (billBreakDown.getData().getPrevJobAmount().compareTo(billBreakDown.getData().getActualAmountBigdecimal()) > 0) {

                if (billBreakDown.getData().getPrevJobAmount().subtract(new BigDecimal(billBreakDown.getData().getPaybleAmount())).compareTo(BigDecimal.ZERO) > 0)
                    editOrderNoteTV.setText(
                            getStrings(R.string.note) + ": " + UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) +
                                    " " + Utils.getDoubleTwoDigits(billBreakDown.getData().getPrevJobAmount().subtract(new BigDecimal(billBreakDown.getData().getPaybleAmount())))) +
                                    " " + getStrings(R.string.amount_would_be_refund));
            } else {

                if (new BigDecimal(billBreakDown.getData().getPaybleAmount()).subtract(billBreakDown.getData().getPrevJobAmount()).compareTo(BigDecimal.ZERO) > 0)
                    editOrderNoteTV.setText(
                            getStrings(R.string.note) + ": " + UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) +
                                    " " + Utils.getDoubleTwoDigits(new BigDecimal(billBreakDown.getData().getPaybleAmount()).subtract(billBreakDown.getData().getPrevJobAmount()))) +
                                    " " + getStrings(R.string.amount_added_in_debt).replace(ORDER, StorefrontCommonData.getTerminology().getOrder()));

            }
        }

        boolean isDeliveryAutoAppliedPromo = false, isSubtotalAutoAppliedPromo = false;
        String deliveryAutoAppliedPromoDescription = "", subtotalAutoAppliedPromoDescription = "";

        if (billBreakDown.getData().getAppliedPromos() != null && billBreakDown.getData().getAppliedPromos().size() > 0)
            for (PromosModel appliedPromo : billBreakDown.getData().getAppliedPromos()) {
                if (appliedPromo.getPromoMode() == Constants.PromoMode.AUTO_APPLY && appliedPromo.getPromoOn() == Constants.PromotionOn.DELIVERY_CHARGE) {
                    isDeliveryAutoAppliedPromo = true;
                    deliveryAutoAppliedPromoDescription = appliedPromo.getDescription();
                } else if (appliedPromo.getPromoMode() == Constants.PromoMode.AUTO_APPLY && appliedPromo.getPromoOn() == Constants.PromotionOn.SUBTOTAL) {
                    isSubtotalAutoAppliedPromo = true;
                    subtotalAutoAppliedPromoDescription = appliedPromo.getDescription();
                }
            }

        ArrayList<BillBreakdownData> billBreakdownDataArrayList = new ArrayList<>();
        try {
            Dependencies.getSelectedProductsArrayList().get(0).setPaymentSettings(billBreakDown.getData().getPaymentSettings());
        } catch (Exception e) {
            e.printStackTrace();
            //   Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings())
        }
        if (billBreakDown.getData().getSurgeAmount() != null && billBreakDown.getData().getSurgeAmount().compareTo(BigDecimal.ZERO) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal),
                    billBreakDown.getData().actualAmount,
                    StorefrontCommonData.getString(this, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge()) + " "
                            + UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings())
                            + billBreakDown.getData().getSurgeAmount()), true, billBreakDown.getData().getPaymentSettings().getSymbol(),
                    billBreakDown.getData().getDeliverySurgeAmount()));
        } else {

            if (billBreakDown.getData().getPaymentSettings() != null)
                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal),
                        billBreakDown.getData().actualAmount,
                        false, null,
                        billBreakDown.getData().getPaymentSettings().getSymbol(),
                        billBreakDown.getData().getDeliverySurgeAmount()));
            else
                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal),
                        billBreakDown.getData().actualAmount, false, null,
                        currencySymbol, billBreakDown.getData().getDeliverySurgeAmount()));
            //testing
            //  subtotalData = new TaxesModel(getStrings(R.string.subtotal), billBreakDown.getData().actualAmount, false, null, currencySymbol, billBreakDown.getData().getDeliverySurgeAmount());
        }

        if (Double.valueOf(billBreakDown.getData().getDiscount()) > 0) {
            if (isSubtotalAutoAppliedPromo) {
               /* discountData = new TaxesModel(getStrings(R.string.discount), billBreakDown.getData().discount,
                        true, subtotalAutoAppliedPromoDescription,
                        billBreakDown.getData().getDeliverySurgeAmount());*/
                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.discount), billBreakDown.getData().discount,
                        true, subtotalAutoAppliedPromoDescription,
                        billBreakDown.getData().getDeliverySurgeAmount()));

            } else {
             /*   discountData = new TaxesModel(getStrings(R.string.discount),
                        billBreakDown.getData().discount, true,
                        billBreakDown.getData().getDeliverySurgeAmount());*/
                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.discount),
                        billBreakDown.getData().discount, true,
                        billBreakDown.getData().getDeliverySurgeAmount()));

            }
        }


        if (billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getAdditionalCharges() != null &&
                billBreakDown.getData().getAdditionalCharges().getOnSubTotal() != null && billBreakDown.getData().getAdditionalCharges().getOnSubTotal().size() > 0) {
            {
                for (int i = 0; i < billBreakDown.getData().getAdditionalCharges().getOnSubTotal().size(); i++) {
                    billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                    billBreakdownDataArrayList.add(new BillBreakdownData(billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).getName(),
                            billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).getAmount(),
                            false, billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).getPercentage(),
                            billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).getType()));

                }
            }

            // newBillBreakdownArrayListSubTotal.addAll(billBreakDown.getData().getAdditionalCharges().getOnSubTotal());
        }

        if (billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getTaxableAmount() != null
                && billBreakDown.getData().getTaxableAmount().compareTo(BigDecimal.ZERO) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData("Taxable Amount" + ":", billBreakDown.getData().getTaxableAmount(), false, 0, 1));

        }

        if (isDeliveryAutoAppliedPromo) {
            billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                    billBreakDown.getData().getDeliveryChargeAfterDiscount(),
                    deliveryAutoAppliedPromoDescription, false, billBreakDown.getData().getPaymentSettings().getSymbol()
                    , billBreakDown.getData().getDeliverySurgeAmount()));
            // billBreakdownArrayList.add(deliveryChargeData);
        } else if (Double.valueOf(billBreakDown.getData().getDeliveryCharge()) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                    billBreakDown.getData().getDeliveryChargeAfterDiscount(),
                    billBreakDown.getData().getPaymentSettings().getSymbol(),
                    billBreakDown.getData().getDeliverySurgeAmount()));
        }

        if (billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getDeliveryTaxesArray().size() > 0) {
            for (int i = 0; i < billBreakDown.getData().getDeliveryTaxesArray().size(); i++) {
                billBreakDown.getData().getDeliveryTaxesArray().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
            }
            for (int i = 0; i < billBreakDown.getData().getDeliveryTaxesArray().size(); i++) {
                billBreakdownDataArrayList.add(new BillBreakdownData(billBreakDown.getData().getDeliveryTaxesArray().get(i).getTaxName(),
                        new BigDecimal(billBreakDown.getData().getDeliveryTaxesArray().get(i).getTaxAmount()),
                        false, billBreakDown.getData().getDeliveryTaxesArray().get(i).getTaxPercentage(),
                        billBreakDown.getData().getDeliveryTaxesArray().get(i).getTaxType()));
            }

        }

        if (billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getUserTaxesArray().size() > 0) {
            for (int i = 0; i < billBreakDown.getData().getUserTaxesArray().size(); i++) {
                billBreakDown.getData().getUserTaxesArray().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
            }
            for (int i = 0; i < billBreakDown.getData().getUserTaxesArray().size(); i++) {
                if (billBreakDown.getData().getUserTaxesArray().get(i).getTaxAppliedOn() != 2) {
                    billBreakdownDataArrayList.add(new BillBreakdownData(billBreakDown.getData().getUserTaxesArray().get(i).getTaxName(),
                            new BigDecimal(billBreakDown.getData().getUserTaxesArray().get(i).getTaxAmount()),
                            false, billBreakDown.getData().getUserTaxesArray().get(i).getTaxPercentage(),
                            billBreakDown.getData().getUserTaxesArray().get(i).getTaxType()));
                }
            }

        }

        if (billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getAdditionalCharges()
                != null && billBreakDown.getData().getAdditionalCharges().getOnTotal() != null && billBreakDown.getData().getAdditionalCharges().getOnTotal().size() > 0) {
            {
                for (int i = 0; i < billBreakDown.getData().getAdditionalCharges().getOnTotal().size(); i++) {
                    billBreakDown.getData().getAdditionalCharges().getOnTotal().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                    billBreakdownDataArrayList.add(new BillBreakdownData(billBreakDown.getData().getAdditionalCharges().getOnTotal().get(i).getName(),
                            billBreakDown.getData().getAdditionalCharges().getOnTotal().get(i).getAmount(),
                            false, billBreakDown.getData().getAdditionalCharges().getOnTotal().get(i).getPercentage(),
                            billBreakDown.getData().getAdditionalCharges().getOnTotal().get(i).getType()));

                }
            }

        }

        if (!isCustomOrder && Double.valueOf(billBreakDown.getData().getTip()) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getTip() + ":", billBreakDown.getData().tip, false, 0, 1));

        }


        if (!isCustomOrder && billBreakDown.getData().getLoyaltyPointUsed() > 0) {
            TaxesModel pointsData = new TaxesModel(StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.loyalty_points_used) + " "
                    + billBreakDown.getData().loyaltyPointUsed, billBreakDown.getData().loyaltyPointDiscount,
                    true, billBreakDown.getData().getDeliverySurgeAmount());
            billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.loyalty_points_used) + " "
                    + billBreakDown.getData().loyaltyPointUsed, billBreakDown.getData().loyaltyPointDiscount,
                    true, billBreakDown.getData().getDeliverySurgeAmount()));

        }


        if (billBreakDown.getData().getAdditionalAmount() != null &&
                billBreakDown.getData().getAdditionalAmount().doubleValue() > 0) {
            /*TaxesModel additionalAmountData = new TaxesModel(getStrings(R.string.additional_amount),
                    billBreakDown.getData().getAdditionalAmount(),
                    billBreakDown.getData().getPaymentSettings() != null ?
                            billBreakDown.getData().getPaymentSettings() : null,
                    billBreakDown.getData().getDeliverySurgeAmount());
            billBreakdownArrayList.add(additionalAmountData);*/
            billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.additional_amount),
                    billBreakDown.getData().getAdditionalAmount(),
                    billBreakDown.getData().getPaymentSettings() != null ?
                            billBreakDown.getData().getPaymentSettings().getSymbol() : null,
                    billBreakDown.getData().getDeliverySurgeAmount()));
        }


        tempLoyaltyPoints = billBreakDown.getData().getLoyaltyPointUsed();
        points = billBreakDown.getData().getLoyaltyPointUsed();


        if (Double.valueOf(billBreakDown.getData().getCreditUsed()) > 0) {
            /*TaxesModel creditData = new TaxesModel(getStrings(R.string.credit_used),
                    BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().creditUsed)),
                    true, billBreakDown.getData().getDeliverySurgeAmount());*/
            // billBreakdownArrayList.add(creditData);
            billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.credit_used),
                    BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().creditUsed)),
                    true, billBreakDown.getData().getDeliverySurgeAmount()));
        }

        if (paymentList.size() > 0 && paymentList.get(adapterPos).getPaymentMethod() == MPAISA.intValue) {
            if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData().getTransactionalChargesInfo().getmPAISA() != null) {
                /*TaxesModel mPaisaTransactionCharge = new TaxesModel(getStrings(R.string.transaction_charges),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo().
                                getmPAISA().getTransactionCharges())), false,
                        billBreakDown.getData().getDeliverySurgeAmount());
                billBreakdownArrayList.add(mPaisaTransactionCharge);*/

                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.transaction_charges),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo().
                                getmPAISA().getTransactionCharges())), false,
                        billBreakDown.getData().getDeliverySurgeAmount()));
            }
        }

        if (paymentList.size() > 0 && paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue) {
            if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData().getTransactionalChargesInfo().getStripe() != null) {
                /*TaxesModel mPaisaTransactionCharge = new TaxesModel(getStrings(R.string.transaction_charges),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo().getStripe()
                                .getTransactionCharges())), false,
                        billBreakDown.getData().getDeliverySurgeAmount());
                billBreakdownArrayList.add(mPaisaTransactionCharge);*/
                billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.transaction_charges),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo().getStripe()
                                .getTransactionCharges())), false,
                        billBreakDown.getData().getDeliverySurgeAmount()));
            }
        }


        if (paymentList.size() > 0 && (paymentList.get(adapterPos).getPaymentMethod() == PAY_LATER.intValue)) {
            if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData()
                    .getTransactionalChargesInfo().getPayLater() != null) {
               /* TaxesModel mPaisaTransactionCharge = new TaxesModel(StorefrontCommonData.getTerminology()
                        .getTransactionCharge(),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo()
                                .getPayLater().getTransactionCharges())), false
                        , billBreakDown.getData().getDeliverySurgeAmount());
                billBreakdownArrayList.add(mPaisaTransactionCharge);*/

                billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology()
                        .getTransactionCharge(),
                        BigDecimal.valueOf(Double.valueOf(billBreakDown.getData().getTransactionalChargesInfo()
                                .getPayLater().getTransactionCharges())), false
                        , billBreakDown.getData().getDeliverySurgeAmount()));
            }
        }


        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownDataArrayList);
        rvTaxesList.setAdapter(taxAdapter);

        if (billBreakDown.getData().getTipOptionEnable() != 1) {
            etTipManual.setEnabled(true);
            etTipManual.setHint(getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getTip()));
            etTipManual.setBackground(getResources().getDrawable(R.drawable.bg_white_round_corners_edittext));
            llEditableTipParent.setVisibility(View.VISIBLE);
        } else {
            etTipManual.setEnabled(false);
            etTipManual.setHint(getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getTip()));
            etTipManual.setBackground(getResources().getDrawable(R.drawable.bg_dull_round_corners_edittext));
            llEditableTipParent.setVisibility(View.GONE);
        }

        tipArraylist.clear();
        rvTipArraylist.setVisibility(billBreakDown.getData().getTipOptionList().size() > 0 ? View.VISIBLE : View.GONE);
        tipArraylist.addAll(billBreakDown.getData().getTipOptionList());
        tipType = billBreakDown.getData().getTipType();
        tipAdapter.tipType = this.tipType;
        tipAdapter.notifyDataSetChanged();

        if (isCustomOrder || isRecurringTaskEnable) {
            llAddTipView.setVisibility(View.GONE);
            llAddPointsView.setVisibility(View.GONE);
        } else {
            if (billBreakDown.getData().getTipEnableDisable() == 1 && Dependencies.getSelectedProductsArrayList().size() > 0) {
//                    &&
//                    Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getSelectedPickupMode()
//                            != Constants.SelectedPickupMode.SELF_PICKUP) {

                if ((StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() == 1) && (isSelfPickUp)) {
                    llAddTipView.setVisibility(View.VISIBLE);
                } else if ((StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() == 0) && (isSelfPickUp)) {
                    llAddTipView.setVisibility(View.GONE);
                } else {
                    llAddTipView.setVisibility(View.VISIBLE);
                }




               /* if (StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() != 1) {
                    llAddTipView.setVisibility(View.GONE);
                } else {
                    if (isSelfPickUp) {
                        llAddTipView.setVisibility(View.VISIBLE);
                    }
                }*/
            } else {
                billBreakDown.getData().setMinimumTip(0.0);
                llAddTipView.setVisibility(View.GONE);
            }
            if (UIManager.getIsLoyaltyEnable() == 1) {
                llAddPointsView.setVisibility(View.VISIBLE);
            } else {
                llAddPointsView.setVisibility(View.GONE);
            }
        }

        if (adapterPos != null && paymentList.size() > 0 && (paymentList.get(adapterPos).getPaymentMethod() == MPAISA.intValue
                || paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue) || (adapterPos != null && paymentList.get(adapterPos).getPaymentMethod() == PAY_LATER.intValue)) {
            if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData().getTransactionalChargesInfo().getmPAISA() != null) {
                tvTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + billBreakDown.getData().getTransactionalChargesInfo().getmPAISA().getTotalCharges()));

            } else if (billBreakDown.getData().getTransactionalChargesInfo() != null
                    && billBreakDown.getData().getTransactionalChargesInfo().getStripe() != null) {
                tvTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + billBreakDown.getData().getTransactionalChargesInfo().getStripe().getTotalCharges()));

            } else if (billBreakDown.getData().getTransactionalChargesInfo() != null
                    && paymentList.get(adapterPos).getPaymentMethod() == PAY_LATER.intValue
                    && billBreakDown.getData().getTransactionalChargesInfo().getPayLater() != null) {
                tvTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol(billBreakDown.getData()
                        .getPaymentSettings()) + billBreakDown.getData().getTransactionalChargesInfo().getPayLater()
                        .getTotalCharges()));

            } else {
                tvTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew((billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getPaymentSettings().getSymbol() != null)
                        ? billBreakDown.getData().getPaymentSettings().getSymbol() : currencySymbol) + billBreakDown.getData().getPaybleAmount()));
            }
        } else {
            tvTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew((billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getPaymentSettings().getSymbol() != null)
                    ? billBreakDown.getData().getPaymentSettings().getSymbol() : currencySymbol) + billBreakDown.getData().getPaybleAmount()));
        }


        if (isRecurringTaskEnable) {

//            String msg = "Subscribed " + billBreakDown.getData().getOccurrenceCount() + " Orders" + "(" + Utils.getCurrencySymbol() + billBreakDown.getData().getPaybleAmount()
//                    + "will be deducted on creation of each bookings" + ")";
//            "Subscribed COUNT_COUNT ORDERS_ORDERS (CURRENCY_CURRENCY 123 will be deducted on the creation of each çççç)"
            String msg = getStrings(R.string.subscription_total_amount_text)
                    .replace(TerminologyStrings.COUNT_COUNT, billBreakDown.getData().getOccurrenceCount() + "")
                    .replace(TerminologyStrings.ORDERS, StorefrontCommonData.getTerminology().getOrders())
                    .replace(TerminologyStrings.CURRENCY_CURRENCY, Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()))
                    .replace(TerminologyStrings.AMOUNT, billBreakDown.getData().getPaybleAmount())
                    .replace(TerminologyStrings.ORDER, StorefrontCommonData.getTerminology().getOrder());


            findViewById(R.id.viewSeperator).setVisibility(View.VISIBLE);


            if (billBreakDown.getData().getTransactionalChargesInfo() != null
                    && paymentList.get(adapterPos).getPaymentMethod() == PAY_LATER.intValue
                    && billBreakDown.getData().getTransactionalChargesInfo().getPayLater() != null) {
                tvRecurringTotal.setText(billBreakDown.getData().getTransactionalChargesInfo().getPayLater().getPayLaterRecuuringCharges() + "");


                msg = getStrings(R.string.subscription_total_amount_text)
                        .replace(TerminologyStrings.COUNT_COUNT, billBreakDown.getData().getOccurrenceCount() + "")
                        .replace(TerminologyStrings.ORDERS, StorefrontCommonData.getTerminology().getOrders())
                        .replace(TerminologyStrings.CURRENCY_CURRENCY
                                , Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()))
                        .replace(TerminologyStrings.AMOUNT, tvTotal.getText().toString())
                        .replace(TerminologyStrings.ORDER, StorefrontCommonData.getTerminology().getOrder());


            } else {
                tvRecurringTotal.setText(billBreakDown.getData().getTotalRecurringAmount() + "");
            }
            tvRecurringTotalHeading.setText(msg);

        }

        if (Double.valueOf(billBreakDown.getData().getPaybleAmount()) < 1) {
            tvAmountPrice.setText(getStrings(R.string.continue_for_free));
        } else {
            tvAmountPrice.setText(StorefrontCommonData.getTerminology().getPay(true) + " "
                    + Utils.getCurrencySymbolNew((billBreakDown.getData().getPaymentSettings() != null && billBreakDown.getData().getPaymentSettings().getSymbol() != null) ? billBreakDown.getData().getPaymentSettings().getSymbol() : currencySymbol) + billBreakDown.getData().getPaybleAmount());
        }
        tvMaxPointsValue.setText(getStrings(R.string.maximum_loyalty_points_apply).replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints()).replace(AMOUNT, billBreakDown.getData().getLoyaltyMaxPoints() + ""));
        tvAvailableValue.setText(getStrings(R.string.loyalty_points_available).replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints()) + ": " + billBreakDown.getData().getLoyaltyPoints());
        if (billBreakDown.getData().getRecurringSurgeListData() != null && billBreakDown.getData().getRecurringSurgeListData().size() > 0)
            deliverySurgeChargesTV.setVisibility(View.VISIBLE);
        else
            deliverySurgeChargesTV.setVisibility(View.GONE);

        if (isOpenWalletAddMoneyActivity && billBreakDown != null && (Dependencies.getWalletBalance() >= Double.valueOf(billBreakDown.getData().getPaybleAmount()))) {
            tvAmountPrice.performClick();
            isOpenWalletAddMoneyActivity = false;
        }


    }

    private void paytmCheckBalance() {
        PaymentManager.getPaytmBalance(mActivity, false, new PaytmBalanceListener() {
            @Override
            public void onPaytmBalanceSuccess(PaytmData paytmData) {
                paytmVerified = paytmData.getPaytmVerified();
                paytmWalletAmount = paytmData.getWalletBalance();
                paytmAddMoneyUrl = paytmData.getPaytmAddMoneyUrl();
                paytmAddMoneyUrl += "&amount=" + getRemainingAmount(paytmWalletAmount);

                setPaymentAdapter(paymentList);
                if (returnFromPaytm) {
                    returnFromPaytm = false;
                    if (billBreakDown != null && Double.valueOf(billBreakDown.getData().getPaybleAmount()) <= paytmWalletAmount) {
                        createTaskViaVendor(adapterPos);
                    }
                }
            }

            @Override
            public void onPaytmBalanceFailure() {
            }
        });
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

    private String getRemainingAmount(final Double paytmWalletAmount) {
        double payableAmount;
        try {
            payableAmount = Double.parseDouble(billBreakDown.getData().getPaybleAmount());
        } catch (final NumberFormatException ex) {
            return "0";
        } catch (final NullPointerException ex) {
            return "0";
        }
        if (billBreakDown != null && payableAmount > paytmWalletAmount) {
            return "" + Utils.getDecimalFormatCalculation().format((payableAmount - paytmWalletAmount));
        } else {
            return "0";
        }
    }

    private void fetchMerchantCards() {
        if (!PaymentMethodsClass.isCardPaymentEnabled()) {
            paymentList.clear();
            PaymentMethodsClass.getPaymentList(mActivity, paymentList, true, isOrderPayment && userDebtData == null && planList == null);
            for (int i = 0; i < paymentList.size(); i++) {
                if (adapterPos != null && adapterPos == i) {
                    paymentList.get(adapterPos).selectedCard = true;
                } else {
                    paymentList.get(i).selectedCard = false;
                }
            }

            setPaymentAdapter(paymentList);

            getdataBill();
        } else {

            PaymentManager.fetchMerchantCards(mActivity, true, valuePaymentMethod, new FetchCardsListener() {
                @Override
                public void onFetchCardsSuccess(List<Datum> paymentCardList) {
                    for (int i = 0; i < paymentCardList.size(); i++) {
                        paymentCardList.get(i).setPaymentFlowType(Constants.PAYMENT_MODES.CARD);
                    }
                    paymentList = new ArrayList<Datum>();

                    PaymentMethodsClass.getPaymentList(mActivity, paymentList, true, isOrderPayment && userDebtData == null && planList == null);
                    paymentList.addAll(paymentCardList);

                    for (int i = 0; i < paymentList.size(); i++) {
                        if (adapterPos != null && adapterPos == i) {
                            paymentList.get(adapterPos).selectedCard = true;
                        } else {
                            paymentList.get(i).selectedCard = false;
                        }
                    }

                    setPaymentAdapter(paymentList);
                    getdataBill();
                }

                @Override
                public void onFetchCardsFailure() {
                    paymentList = new ArrayList<Datum>();
                    PaymentMethodsClass.getPaymentList(mActivity, paymentList, true, isOrderPayment && userDebtData == null && planList == null);

                    for (int i = 0; i < paymentList.size(); i++) {
                        if (adapterPos == i) {
                            paymentList.get(adapterPos).selectedCard = true;
                        } else {
                            paymentList.get(i).selectedCard = false;
                        }
                    }

                    setPaymentAdapter(paymentList);
                }
            });
        }


    }

    private void getdataBill() {
        if (payViaHippoObject != null)
            setBillBreakDownsWhenPayViaHippo();
        else if (payViaTaskDetailsObject != null)
            setBillBreakDownsWhenPayViaTaskDetails();
        else if (paymentFor == PaymentConstants.PaymentForFlow.DEBT.intValue && userDebtData != null)
            setBillBreakDownsWhenPayForDebt();
        else if (paymentFor == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue && planList != null)
            setBillBreakDownsWhenPayForCustomerSubscription();
        else {
//            if (!PaymentMethodsClass.isCardPaymentEnabled()) {
//                fetchMerchantCards();
//            } else {
            getBillBreakdowns();
//            }
        }
    }

    public void setPaymentAdapter(List<Datum> paymentList) {
        if (paymentList.size() > 0) {
            rvPaymentCardList.setVisibility(View.VISIBLE);
            tvNoPaymentCardFound.setVisibility(View.GONE);

            mAdapter = new PaymentListAdapter(mActivity, paymentList, paytmWalletAmount, paytmVerified, this);
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

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.rlBack) {
            onBackPressed();
        } else if (i == R.id.ivAddPaymentOptions) {
            PaymentManager.openAddPaymentCardWebViewActivity(mActivity, valuePaymentMethod);

        } else if (i == R.id.tvAmountPrice) {


            long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();


            if (isEditOrder) {
                updateOrder();
                return;
            }

            if (isRecurringTaskEnable && (
                    !(paymentMethod == PaymentConstants.PaymentValue.CASH.intValue
                            || paymentMethod == PaymentConstants.PaymentValue.INAPP_WALLET.intValue
                            || paymentMethod == PaymentConstants.PaymentValue.PAY_LATER.intValue
                            || paymentMethod == PaymentConstants.PaymentValue.PAY_ON_DELIVERY.intValue))) {
                Utils.snackBar(mActivity, getStrings(R.string.payment_method_not_allowed));
                paySlider.setSlideInitial();
                return;

            } else if (isCardSelected && adapterPos != null && billBreakDown != null) {
                if (!Utils.internetCheck(mActivity)) {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                    paySlider.setSlideInitial();
                    return;
                }
                if (!isCustomOrder && tip < billBreakDown.getData().getMinimumTip()) {
                    Utils.snackBar(mActivity, getStrings(R.string.minimum_tip_amount_is_amount).replace(TIP, StorefrontCommonData.getTerminology().getTip())
                            .replace(AMOUNT, Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + Utils.getDoubleTwoDigits(billBreakDown.getData().getMinimumTip())));
                    paySlider.setSlideInitial();
                    return;
                }
                updateHashMapPaymentMethod(adapterPos);
                if (hashMap != null) {
                    if (paymentList.get(adapterPos).getPaymentMethod() == PAYFORT.intValue) {

                        if (billBreakDown.getData().getHoldPayment())
                            setTransactionBuilder();
                        else {
                            if (billBreakDown.getData().paybleAmount.doubleValue() > 0) {
                                setTransactionBuilder();
                            } else {
                                createTaskViaVendor(adapterPos);
                            }
                        }

                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && paytmVerified != null && paytmVerified == 0) {
                        PaymentManager.requestOtpForPaytm(mActivity);
//                        Utils.snackBar(mActivity, getStrings(R.string.paytm_account_not_linked));
                        paySlider.setSlideInitial();

                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && billBreakDown != null
                            && Double.valueOf(billBreakDown.getData().getPaybleAmount()) > paytmWalletAmount) {
                        PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
                        paySlider.setSlideInitial();

                    } else if ((paymentList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue) && billBreakDown != null
                            && (Double.valueOf(billBreakDown.getData().getPaybleAmount()) > (Dependencies.getWalletBalance()))) {


                        Date todaysDate = null, startScheduleDate = null;
                        if (hashMap.containsKey("start_schedule")) {
                            startScheduleDate = DateUtils.getInstance().getDate(hashMap.get("start_schedule"));
                            todaysDate = DateUtils.getInstance().getDate(DateUtils.getInstance().getTodaysDate(ONLY_DATE));
                        }

                        if (isRecurringTaskEnable && startScheduleDate != null && !startScheduleDate.equals(todaysDate)) {

                            new OptionsDialog.Builder(mActivity).negativeButton(R.string.add_later)
                                    .positiveButton(R.string.add_now).message(getStrings(R.string.error_wallet_balance_low)).listener(new OptionsDialog.Listener() {
                                @Override
                                public void performPositiveAction(int purpose, Bundle backpack) {

                                    PaymentManager.setCustomPaymentMethods(null);
                                    PaymentManager.openAddWalletBalanceActivity(mActivity, Double.valueOf(billBreakDown.getData().getPaybleAmount()));
                                    paySlider.setSlideInitial();
                                }

                                @Override
                                public void performNegativeAction(int purpose, Bundle backpack) {
                                    createTaskViaVendor(adapterPos);
                                }
                            }).build().show();

                        } else {
                            PaymentManager.setCustomPaymentMethods(null);
                            PaymentManager.openAddWalletBalanceActivity(mActivity, Double.valueOf(billBreakDown.getData().getPaybleAmount()));
                            paySlider.setSlideInitial();
                        }

                    } else if (paymentList.get(adapterPos).getPaymentMethod() == PAYTM_LINK.intValue) {
                        if (billBreakDown != null)
                            opendialogforPhoneNumber(adapterPos, 0);
                    } else if ((getPaymentProcess() == 0 || getPaymentProcess() == 2)
                            && billBreakDown.getData().paybleAmount.doubleValue() > 0
                            && paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {

                        if (paymentMethod == INSTAPAY.intValue && Double.parseDouble(billBreakDown.getData().getPaybleAmount()) < 10) {
                            Utils.snackBar(this, getString(R.string.minimum_amount_ten));
                            paySlider.setSlideInitial();
                        } else {
                            if (isCustomOrder) {
                                setTransactionBuilder();
                            } else {
                                validateOrder();
                            }
                        }

                    } else if (billBreakDown != null) {
                        createTaskViaVendor(adapterPos);
                    }

                } else {

                    if (payViaHippoObject != null
                            || payViaTaskDetailsObject != null
                            || (userDebtData != null && paymentFor == PaymentConstants.PaymentForFlow.DEBT.intValue)
                            || (planList != null && paymentFor == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue)) {
                        try {
                            int orderIdPayViaObject = 0;

                            if (payViaHippoObject != null || payViaTaskDetailsObject != null)
                                orderIdPayViaObject = payViaHippoObject != null ? (int) payViaHippoObject.getDouble("order_id") : payViaTaskDetailsObject.getJobId();
                            else if (userDebtData != null)
                                orderIdPayViaObject = userDebtData.getJobId();
                            else if (planList != null)
                                orderIdPayViaObject = planList.getPlanId();

                            if ((paymentList.get(adapterPos).getPaymentMethod() == CASH.intValue
                                    || paymentList.get(adapterPos).getPaymentMethod() == PAY_LATER.intValue
                                    || paymentList.get(adapterPos).getPaymentMethod() == PAYTM_LINK.intValue
                                    || paymentList.get(adapterPos).getPaymentMethod() == PAY_ON_DELIVERY.intValue) && billBreakDown.getData().paybleAmount.doubleValue() > 0) {

                                if (paymentList.get(adapterPos).getPaymentMethod() == PAYTM_LINK.intValue) {
                                    opendialogforPhoneNumber(0, orderIdPayViaObject);
                                } else {
                                    setTransactionBuilder(orderIdPayViaObject);
                                }


                            } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && paytmVerified != null && paytmVerified == 0) {
                                PaymentManager.requestOtpForPaytm(mActivity);
                                paySlider.setSlideInitial();
                            } else if ((paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) && billBreakDown != null
                                    && Double.valueOf(billBreakDown.getData().getPaybleAmount()) > paytmWalletAmount) {
                                PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
                                paySlider.setSlideInitial();

                            } else if ((paymentList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue) && billBreakDown != null
                                    && (Double.valueOf(billBreakDown.getData().getPaybleAmount()) > (Dependencies.getWalletBalance()))) {

                                PaymentManager.setCustomPaymentMethods(null);
                                PaymentManager.openAddWalletBalanceActivity(mActivity, Double.valueOf(billBreakDown.getData().getPaybleAmount()));
                                paySlider.setSlideInitial();
                            } else if (billBreakDown.getData().paybleAmount.doubleValue() > 0) {

                                if (paymentMethod == INSTAPAY.intValue && Double.parseDouble(billBreakDown.getData().getPaybleAmount()) < 10) {
                                    Utils.snackBar(this, getString(R.string.minimum_amount_ten));
                                    paySlider.setSlideInitial();
                                } else {
                                    setTransactionBuilder(orderIdPayViaObject);
                                }

                            } else {
                                setTransactionBuilder(orderIdPayViaObject);
                            }
                        } catch (JSONException e) {

                            Utils.printStackTrace(e);
                        }
                    } else {
                        paySlider.setSlideInitial();
                    }

                }
            } else {
                if (tvNoPaymentCardFound.getVisibility() == View.GONE) {
                    Utils.snackBar(mActivity, getStrings(R.string.please_select_paymentOption).replace(PAYMENT, StorefrontCommonData.getTerminology().getPayment(false)));
                } else {
                    Utils.snackBar(mActivity, getStrings(R.string.please_add_paymentOption_first).replace(PAYMENT, StorefrontCommonData.getTerminology().getPayment(false)));
                }
                paySlider.setSlideInitial();
            }

        } else if (i == R.id.tvAddAnotherPromo) {
            Utils.showGenericDialog(mActivity, true);

        } else if (i == R.id.tvApplyTip) {
            if (!isCustomOrder) {
                if (etTipManual.getText().toString().trim().isEmpty() || etTipManual.getText().toString().trim().equals(".")) {
                    Utils.snackBar(MakePaymentActivity.this, getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getTip()));
                } else if (Double.parseDouble(etTipManual.getText().toString()) < billBreakDown.getData().getMinimumTip()) {
                    Utils.snackBar(mActivity, getStrings(R.string.minimum_tip_amount_is_amount).replace(TIP, StorefrontCommonData.getTerminology().getTip())
                            .replace(AMOUNT, Utils.getCurrencySymbol(billBreakDown.getData().getPaymentSettings()) + Utils.getDoubleTwoDigits(billBreakDown.getData().getMinimumTip())));
                } else {

                    tip = Double.valueOf(etTipManual.getText().toString());
                    getBillBreakdowns();
                }
            }
        } else if (i == R.id.tvApplyPoints) {
            if (!isCustomOrder) {
                if (etPointsManual.getText().toString().trim().isEmpty() || etPointsManual.getText().toString().trim().equals(".")) {
                    Utils.snackBar(MakePaymentActivity.this, getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getLoyaltyPoints()));
                } else {
                    tempLoyaltyPoints = Integer.parseInt(etPointsManual.getText().toString());
                    sendPoints = true;
                    getBillBreakdowns();
                }
            }
        } else if (i == R.id.btnAcceptHold) {
            rlHoldPayment.setVisibility(View.GONE);
        } else if (i == R.id.deliverySurgeChargesTV) {
            new RecuringSurgeDetailsDialog(MakePaymentActivity.this, new RecuringSurgeDetailsDialog.SelectionListner() {
                @Override
                public void onDialogDismiss() {

                }
            }, billBreakDown.getData().getRecurringSurgeListData(), 5).show();
        }

    }

    private void opendialogforPhoneNumber(final int adapterPos, final int orderIdPayViaObject) {
        try {
            if (phoneNumberDialog != null) {
                phoneNumberDialog.dismiss();
                phoneNumberDialog = null;
            }
            phoneNumberDialog = new Dialog(mActivity, R.style.NotificationDialogTheme);
            phoneNumberDialog.setContentView(R.layout.custom_dialog_view_enter_phone_number);

            Window window = phoneNumberDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            params.gravity = Gravity.BOTTOM;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setAttributes(params);

            phoneNumberDialog.setCancelable(true);
            phoneNumberDialog.setCanceledOnTouchOutside(false);


//            changeNumberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            changeNumberDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//            changeNumberDialog.getWindow().setGravity(Gravity.CENTER);
//
//            params.dimAmount = 0.6f;
//            window.getAttributes().windowAnimations = R.style.CustomDialog;
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


            final EditText etCountryCode = phoneNumberDialog.findViewById(R.id.etCountryCode);
            final EditText etPhoneNumber = phoneNumberDialog.findViewById(R.id.etPhoneNumber);
            final TextView tvDialogDescription = phoneNumberDialog.findViewById(R.id.tvDialogDescription);
            etPhoneNumber.requestFocus();
            etPhoneNumber.setText(getStrings(R.string.enter_phone_number));
            tvDialogDescription.setHint(getStrings(R.string.please_confirm_your_mobile_number));
            Button btnCancel = phoneNumberDialog.findViewById(R.id.btnCancel);
            btnCancel.setText(getStrings(R.string.cancel));
            Button btnDone = phoneNumberDialog.findViewById(R.id.btnDone);
            btnDone.setText(getStrings(R.string.submit));


            String phone = "";

            if (hashMap != null && hashMap.get("job_pickup_phone") != null && !hashMap.get("job_pickup_phone").isEmpty()) {
                phone = hashMap.get("job_pickup_phone");
            } else if (!StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().isEmpty()) {
                phone = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo();
            }

            if (!phone.isEmpty()) {
                try {
//            String[] phoneNumber = Utils.splitNumberByCode(this, vendorData.getPhoneNo());
                    String[] phoneNumber = Utils.splitNumberByCodeNew(this, phone);
                    etCountryCode.setText(phoneNumber[0]);
                    etPhoneNumber.setText(phoneNumber[1].replace("+", ""));
                } catch (Exception e) {
                    String countryCode = Utils.getCountryCode(mActivity, phone.trim());
                    etCountryCode.setText(countryCode);
//            etCountryCode.setText(countryPicker.getUserCountryInfo(this).getDialCode());
                    etPhoneNumber.setText(phone.trim().replace(countryCode, "").replace("+", "").replace("-", " ").trim());
                }
            } else
                etCountryCode.setText(Utils.getDefaultCountryCode(this));

            etCountryCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideSoftKeyboard(mActivity);

                    CountrySelectionDailog.getInstance(MakePaymentActivity.this, new CountryPickerAdapter.OnCountrySelectedListener() {
                        @Override
                        public void onCountrySelected(Country country) {
                            etCountryCode.setText(country.getCountryCode());
                            CountrySelectionDailog.dismissDialog();
                        }
                    }).show();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneNumberDialog.dismiss();
                    paySlider.setSlideInitial();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.hideSoftKeyboard(mActivity);

                        }
                    }, 100);
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contact = etCountryCode.getText().toString() + " " + etPhoneNumber.getText().toString();
                    String phoneNumber = etPhoneNumber.getText().toString();

                    boolean cancel = false;
                    View focusView = null;

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(phoneNumber) || !Utils.isValidPhoneNumber(phoneNumber)) {
                        etPhoneNumber.setError(getStrings(TextUtils.isEmpty(phoneNumber) ? R.string.error_enter_contact : R.string.enter_valid_phone));
//                        Utils.snackBar(mActivity, getStrings(TextUtils.isEmpty(contact) ? R.string.error_enter_contact : R.string.enter_valid_phone));
                        focusView = etPhoneNumber;
                        cancel = true;
                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {
                        Utils.hideSoftKeyboard(mActivity, phoneNumberDialog.findViewById(R.id.etPhoneNumber));
                        phoneNumberDialog.dismiss();
                        if (orderIdPayViaObject > 0) {
                            setTransactionBuilder(orderIdPayViaObject);
                        } else {
                            createTaskViaVendor(adapterPos, contact);
                        }
//                        changeNumber(contact);
                    }
                }
            });
//            changeNumberDialog.show();

            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            phoneNumberDialog.show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 200);

        } catch (WindowManager.BadTokenException badTokenExcep) {
            //badTokenExcep.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PaymentManager.setCustomPaymentMethods(null);


    }

    private void saveRecurringTask(HashMap<String, String> hashMap) {

        if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData()
                .getTransactionalChargesInfo().getPayLater() != null) {
            hashMap.put("transaction_charges", billBreakDown.getData()
                    .getTransactionalChargesInfo().getPayLater().getPayLaterRecuuringCharges() + "");
        } else {
            hashMap.put("transaction_charges", "0");
        }


        hashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        hashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        hashMap.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");


        CommonParams.Builder cParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        cParams.add("occurrence_count", hashMap.get("occurrence_count"));

        cParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        cParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        cParams.add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
//        cParams.add("payment_preference", paymentList.get(adapterPos).getPaymentMethod());

        cParams.add("end_schedule", hashMap.get("end_schedule"));
        cParams.add("start_schedule", hashMap.get("start_schedule"));
        cParams.add("schedule_time", hashMap.get("schedule_time"));
        cParams.add("day_array", hashMap.get("day_array"));
        cParams.add("request_body", new Gson().toJson(hashMap));

//        cParams.add("request_body",hashMap);
//        cParams.add("request_body",hashMap.toString());

//        cParams.add("product_json", hashMap.get("products"));


        RestClient.getApiInterface(mActivity).saveRecurringTask(cParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel validReqUrlPojo) {
                CreateTaskResponse createTaskResponse = validReqUrlPojo.toResponseModel(CreateTaskResponse.class);
                successString = validReqUrlPojo.getMessage();

                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ORDER_CREATED_SUCCESS, paymentList.get(adapterPos).getPaymentMethod() + "");
                StorefrontCommonData.setLastPaymentMethod(paymentList.get(adapterPos).getPaymentMethod());


                createTaskSuccessPaymentResponse(validReqUrlPojo.getMessage());
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                paySlider.setSlideInitial();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Utils.hideSoftKeyboard(this);
            super.onActivityResult(requestCode, resultCode, data);
            /* Code to analyse the User action on asking to enable gps */
            switch (requestCode) {
                case Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY:
                    //TODO  onResume fetchcards removed and only implemented when result is ok
                    if (resultCode == RESULT_OK) {
                        hitFetchMerchantCard = data.getExtras().getBoolean("hitFetchMerchantCard");
                        Utils.snackbarSuccess(this, "Card added successfully");
                    }
                    break;

                case Codes.Request.OPEN_PROCESSING_PAYMENT_ACTIVITY:
                    if (data.getStringExtra(MODE_PAYMENT) != null &&
                            data.getStringExtra(MODE_PAYMENT).equals("stripe3ds")) {
                        if (data.getBooleanExtra("isBuySubscription", false)) {
                            String successMessage = "Payment Successful.";
                            new AlertDialog.Builder(mActivity)
                                    .message(successMessage.isEmpty() ? getStrings(R.string.payment_successful) : successMessage)
                                    .button(getStrings(R.string.ok_text))
                                    .listener(new AlertDialog.Listener() {
                                        @Override
                                        public void performPostAlertAction(int purpose, Bundle backpack) {
                                            Bundle bundleExtra = new Bundle();
                                            // bundleExtra.putDouble("debtAmount", data.getDebtAmount());
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtras(bundleExtra);
                                            setResult(RESULT_OK, returnIntent);
                                            finish();
                                        }
                                    }).build().show();

                        } else {
                            onTransactionSuccess(data.getStringExtra(TRANSACTION_ID), "");
                        }
                    } else {
                        PaymentTransactionUrlManager.getInstance().onActivityResult(requestCode, resultCode, data);
                    }
                    break;

                case Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY:
                    if (resultCode == RESULT_OK) {
                        returnFromPaytm = true;
                        hitFetchMerchantCard = true;
                        paytmCheckBalance();
                    }
                    break;

                case Codes.Request.OPEN_OTP_FOR_PAYTM:
                    if (resultCode == RESULT_OK) {
                        returnFromPaytm = true;
                        hitFetchMerchantCard = true;
                        paytmCheckBalance();
                    }
                    break;

                case Codes.Request.OPEN_PAYULATAM_WEBVIEW_ACTIVITY:
                    PayulatamPaymentManager.getInstance().onActivityResult(requestCode, resultCode, data);
                    break;

                case Codes.Request.OPEN_PAYTM_SDK:
                    try {
                        PaytmResponse mpaytmResponse = new Gson().fromJson(data.getStringExtra("response"), PaytmResponse.class);
                        if (mpaytmResponse.getRespcode().equals("01")) {
                            if (mActivity instanceof WalletAddMoneyActivity ||
                                    mActivity instanceof GiftCardPaymentActivity) {
                                PaymentTransactionUrlManager.getInstance().apiTransactionIDPaytmCustom(mpaytmResponse.getTxnid().toString());
                            } else {
                                PaymentTransactionUrlManager.getInstance().apiTransactionIDPaytm(mpaytmResponse.getTxnid().toString());
                            }
                        } else {
                            PaymentTransactionUrlManager.getInstance().apiJobExpired();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;


                case Codes.Request.OPEN_WALLET_ADD_MONEY_ACTIVITY:
                    isOpenWalletAddMoneyActivity = true;


                    if (UIManager.isMerchantPaymentMethodsEnabled()) {
                        paymentMethodsCheck();
                    }
                    if (resultCode == RESULT_OK) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        /*if (billBreakDown != null && (Dependencies.getWalletBalance() >= Double.valueOf(billBreakDown.getData().getPaybleAmount()))) {
                            tvAmountPrice.performClick();
                        }*/
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTaskSuccessPaymentResponse(String successMessage, Boolean isSucess) {
        if (payViaHippoObject != null || payViaTaskDetailsObject != null) {

            new AlertDialog.Builder(mActivity)
                    .message(successMessage.isEmpty() ? getStrings(R.string.payment_successful) : successMessage)
                    .button(getStrings(R.string.ok_text))
                    .listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            finish();
                        }
                    }).build().show();
            return;
        }

        if (isSucess) {
            Bundle bundle = new Bundle();
            bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
            bundle.putString(Keys.Extras.SUCCESS_MESSAGE, successMessage);
            bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            bundle.putSerializable(MakePaymentActivity.class.getName(), hashMap);

            Transition.transitBookingSuccess(mActivity, bundle);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void updateHashMapPaymentMethod(final int adapterPos) {
        if (hashMap != null && hashMap.containsKey("pickup_meta_data")) {
            try {
                JSONArray jsonArray = new JSONArray(hashMap.get("pickup_meta_data"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("label") && jsonObject.get("label").equals(PAYMENT_METHOD)) {
                        jsonObject.put("data", paymentList.get(adapterPos).getPaymentMethod() + "");
                        jsonArray.put(i, jsonObject);
                    }
                    if (jsonObject.has("label") && jsonObject.get("label").equals(PAYMENT_MODE)) {
                        jsonObject.put("data", getPaymentString(mActivity, paymentList.get(adapterPos).getPaymentMethod(), paymentList.get(adapterPos).getLast4Digits()) + "");
                        jsonArray.put(i, jsonObject);
                    }
                }
                hashMap.put("pickup_meta_data", jsonArray + "");
            } catch (Exception e) {
            }
        }
    }

    public void apply(final String code) {
        boolean isPromoApplied = false;
        boolean isReferralApplied = false;

        for (int i = 0; i < promoList.size(); i++) {
            if ((promoList.get(i).getPromoId() + "").equalsIgnoreCase(code) && promoList.get(i).getIsPromo() == 1) {
                isPromoApplied = true;
                isReferralApplied = false;
//                promoList.get(i).isSelected = true;
//                promoID = promoList.get(i).getPromoId();
//                promosAdapter.notifyDataSetChanged();
//                setPromoReferalCode(promoID,"","");
                getBillBreakdowns(promoList.get(i).getPromoId(), "", "");
            } else if (promoList.get(i).getPromoCode().equalsIgnoreCase(code) && promoList.get(i).getIsPromo() == 0) {
                isReferralApplied = true;
                isPromoApplied = false;
//                promoList.get(i).isSelected = true;
//                referralCode = promoList.get(i).getPromoCode();
//                promosAdapter.notifyDataSetChanged();
//                setPromoReferalCode(null,referralCode,"");
                getBillBreakdowns(null, promoList.get(i).getPromoCode(), "");
            } else {
//                promoList.get(i).isSelected = false;
            }
        }
        if (promosAdapter != null) promosAdapter.notifyDataSetChanged();

        if (!isPromoApplied && !isReferralApplied) {
//            setPromoReferalCode(null, "", code);
            getBillBreakdowns(null, "", code);
        }
    }

    public void setPromoReferalCode(Integer promoID, String referralCode, String promoCode) {
        this.promoID = promoID;
        this.promoCode = promoCode;
        this.referralCode = referralCode;
        if (promosAdapter != null) promosAdapter.notifyDataSetChanged();
    }

    private void createTaskViaVendor(final int adapterPos) {
        createTaskViaVendor(adapterPos, "", "");
    }

    private void createTaskViaVendor(final int adapterPos, String transactionId, String jobPaymentDetailId) {
        createTaskViaVendor(adapterPos, transactionId, jobPaymentDetailId, "");
    }

    private void createTaskViaVendor(final int adapterPos, String phoneNumber) {
        createTaskViaVendor(adapterPos, "", "", phoneNumber);
    }

    private void updateOrder() {


        if (paySlider.isSliderInIntialStage())
            paySlider.fullAnimate();


        hashMap.put("job_pickup_latitude", latitude);
        hashMap.put("job_pickup_longitude", longitude);
        hashMap.put(JOB_PICKUP_ADDRESS, jobPickupAddress);
        if (isCustomOrder && isPickupAnywhere) {
            hashMap.put("is_pickup_anywhere", "1");
        }
        if (isCustomOrder) {
            if (!UIManager.isCustomQuotationEnabled())
                hashMap.put("is_custom_order", "1");
            else
                hashMap.put("is_custom_order", "2");
            hashMap.remove("is_app_product_tax_enabled");
        }


        if (!isCustomOrder && billBreakDown.getData().getTipEnableDisable() == 1) {
            hashMap.put("tip", tip + "");
        }
        if (!isCustomOrder && UIManager.getIsLoyaltyEnable() == 1
                && points != 0
        ) {
            hashMap.put("loyalty_points", points + "");
        } else {
            hashMap.remove("loyalty_points");
        }

        if (billBreakDown.getData().getDeliverySurgeAmount() != null && billBreakDown.getData().getDeliverySurgeAmount().compareTo(BigDecimal.ZERO) > 0) {
            hashMap.put("delivery_surge", billBreakDown.getData().getDeliverySurgeAmount().toString());
        }
//        hashMap.put("currency_id", Utils.getCurrencyId());
        if (paymentList.get(adapterPos).getCardId() != null)
            hashMap.put("card_id", paymentList.get(adapterPos).getCardId());
        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 || billBreakDown.getData().getHoldPayment()) {
            hashMap.put("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "");
        } else {
            hashMap.put("payment_method", CASH.intValue + "");
        }
        if (promoID != null) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_code");
            hashMap.put("promo_id", promoID + "");
        } else if (referralCode != null && !referralCode.isEmpty()) {
            hashMap.remove("promo_code");
            hashMap.remove("promo_id");
            hashMap.put("referral_code", referralCode);
        } else if (promoCode != null && !promoCode.isEmpty()) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.put("promo_code", promoCode);
        } else {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.remove("promo_code");
        }


        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 &&

                getPaymentProcess() == 1
                && paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW
        ) {
            hashMap.put("is_payment_done", "0");   // in case of post payment only
        } else {
            hashMap.remove("is_payment_done");
        }


        hashMap.put("delivery_charge", billBreakDown.getData().getDeliveryCharge() + "");
        hashMap.put("tax", billBreakDown.getData().getTax() + "");
        if (isCustomOrder) {
            hashMap.put("amount", 0 + "");
        } else {


            hashMap.put("amount", getAmountForBooking());
        }
        hashMap.put("AppIP", Utils.getIPAddress(true));


        if (billBreakDown.getData().getDeliveryChargesFormulaFields() != null) {
            Gson gson = new GsonBuilder().create();
            String deliveryChargePayload = gson.toJson(billBreakDown.getData().getDeliveryChargesFormulaFields());
            hashMap.put("delivery_charges_formula_fields", deliveryChargePayload);
        }
        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (hashMap.containsKey(IS_SCHEDULED) && hashMap.get(IS_SCHEDULED).equals("0")) {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
            if (hashMap.containsKey(JOB_DELIVERY_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
        }

        if (isCustomOrder) {
            hashMap.remove("is_app_product_tax_enabled");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
            hashMap.put(USER_ID, isCustomOrderMerchantLevel ? merchantUserId :
                    StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
            RestClient.getApiInterface(mActivity).updateOrder(hashMap).enqueue(updateTaskResponce());
        } else {
            hashMap.put("is_app_product_tax_enabled", "1");
            if (templateDataList != null) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                hashMap.put("checkout_template", String.valueOf(myCustomArray));
            }
            hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
            RestClient.getApiInterface(mActivity).updateOrder(hashMap).enqueue(updateTaskResponce());
        }
    }

    private void createTaskViaVendor(final int adapterPos, String transactionId, String jobPaymentDetailId, String phoneNumber) {


        long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();

        if (paySlider.isSliderInIntialStage())
            paySlider.fullAnimate();


        hashMap.put("job_pickup_latitude", latitude);
        hashMap.put("job_pickup_longitude", longitude);
        hashMap.put(JOB_PICKUP_ADDRESS, jobPickupAddress);
        if (isCustomOrder && isPickupAnywhere) {
            hashMap.put("is_pickup_anywhere", "1");
        }
        if (isCustomOrder) {
            if (!UIManager.isCustomQuotationEnabled())
                hashMap.put("is_custom_order", "1");
            else
                hashMap.put("is_custom_order", "2");
            hashMap.remove("is_app_product_tax_enabled");
        }


        if (!isCustomOrder && billBreakDown.getData().getTipEnableDisable() == 1) {
            if ((StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() == 0)
                    && (isSelfPickUp)) {
                hashMap.put("tip", 0 + "");

            } else {
                hashMap.put("tip", tip + "");
            }
        }
        if (!isCustomOrder && UIManager.getIsLoyaltyEnable() == 1
                && points != 0
        ) {
            hashMap.put("loyalty_points", points + "");
        } else {
            hashMap.remove("loyalty_points");
        }

        if (billBreakDown.getData().getDeliverySurgeAmount() != null && billBreakDown.getData().getDeliverySurgeAmount().compareTo(BigDecimal.ZERO) > 0) {
            hashMap.put("delivery_surge", billBreakDown.getData().getDeliverySurgeAmount().toString());
        }
//        hashMap.put("currency_id", Utils.getCurrencyId());
        if (paymentList.get(adapterPos).getCardId() != null)
            hashMap.put("card_id", paymentList.get(adapterPos).getCardId());
        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 || billBreakDown.getData().getHoldPayment()) {
            hashMap.put("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "");
        } else {
            hashMap.put("payment_method", CASH.intValue + "");
        }
        if (promoID != null) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_code");
            hashMap.put("promo_id", promoID + "");
        } else if (referralCode != null && !referralCode.isEmpty()) {
            hashMap.remove("promo_code");
            hashMap.remove("promo_id");
            hashMap.put("referral_code", referralCode);
        } else if (promoCode != null && !promoCode.isEmpty()) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.put("promo_code", promoCode);
        } else {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.remove("promo_code");
        }

        if (transactionId != null && !transactionId.isEmpty()) {
            hashMap.put("transaction_id", transactionId);
        }
        if (jobPaymentDetailId != null && !jobPaymentDetailId.isEmpty()) {
            hashMap.put("job_payment_detail_id", jobPaymentDetailId);
        }
//        }

        if (valuePaymentMethod == PAYTM_LINK.intValue) {
            hashMap.put("paytm_number", phoneNumber);
        }

        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 &&

                getPaymentProcess() == 1
                && paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {
            hashMap.put("is_payment_done", "0");   // in case of post payment only
        } else {
            hashMap.remove("is_payment_done");
        }


        hashMap.put("delivery_charge", billBreakDown.getData().getDeliveryCharge() + "");
        hashMap.put("tax", billBreakDown.getData().getTax() + "");
        if (isCustomOrder) {
            hashMap.put("amount", 0 + "");
        } else {


            hashMap.put("amount", getAmountForBooking());
        }
        hashMap.put("AppIP", Utils.getIPAddress(true));


        if (billBreakDown.getData().getDeliveryChargesFormulaFields() != null) {
            Gson gson = new GsonBuilder().create();
            String deliveryChargePayload = gson.toJson(billBreakDown.getData().getDeliveryChargesFormulaFields());
            hashMap.put("delivery_charges_formula_fields", deliveryChargePayload);
        }
        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (hashMap.containsKey(IS_SCHEDULED) && hashMap.get(IS_SCHEDULED).equals("0")) {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
            if (hashMap.containsKey(JOB_DELIVERY_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
        }


        if (paymentMethod == FAC.intValue) {
//            hashMap.put("cvv", cvv);
            hashMap.put("fac_payment_flow", "2");
        }
        hashMap.put("subtotal_amount", billBreakDown.getData().getActualAmount() + "");


        if (Dependencies.isLaundryApp()) {
            hashMap.remove("is_app_product_tax_enabled");
            hashMap.remove("job_payment_detail_id");
            if (hashMap.containsKey("is_recurring_enabled"))
                hashMap.remove("is_recurring_enabled");
            if (isCustomOrder)
                RestClient.getApiInterface(this).createTaskLaundryCustom(hashMap).enqueue(createTaskResponse());
            else {
                if(hashMap.containsKey("old_delivery_charge"))
                    hashMap.remove("old_delivery_charge");
                RestClient.getApiInterface(this).createTaskLaundry(hashMap).enqueue(createTaskResponse());
            }
        }
        //TODO

//        if (hashMap.get("is_recurring_enabled").equalsIgnoreCase("1")) {
        else if (isRecurringTaskEnable) {

            saveRecurringTask(hashMap);

        } else if (isCustomOrder) {
            hashMap.remove("is_app_product_tax_enabled");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
            hashMap.put(USER_ID, isCustomOrderMerchantLevel ? merchantUserId :
                    StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
            RestClient.getApiInterface(mActivity).createTaskViaVendorCustom(hashMap).enqueue(createTaskResponse());
        } else {
            hashMap.put("is_app_product_tax_enabled", "1");
            if (templateDataList != null) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                hashMap.put("checkout_template", String.valueOf(myCustomArray));
            }
            hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }

            if (sochitelOperatorId != 0) {
                hashMap.put("operator", String.valueOf(sochitelOperatorId));

            }

           /* if (isPdFlow) {
                hashMap.put("job_pickup_latitude", hashMap.get("custom_pickup_latitude"));
                hashMap.put("job_pickup_longitude", hashMap.get("custom_pickup_longitude"));
                hashMap.put("latitude", hashMap.get("custom_pickup_latitude"));
                hashMap.put("longitude", hashMap.get("custom_pickup_longitude"));
                hashMap.put("custom_pickup_latitude", latitude);
                hashMap.put("custom_pickup_longitude", longitude);
            }*/
            hashMap3 = new HashMap<>();

            hashMap3.putAll(hashMap);
            if (isPdFlow)
                hashMap3.putAll(hashMapLocations);


            RestClient.getApiInterface(mActivity).createTaskViaVendor(hashMap3).enqueue(createTaskResponse());
        }
    }

    private HashMap<String, String> getcreateTaskmap(final int adapterPos, String
            transactionId, String jobPaymentDetailId, String phoneNumber) {

        long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();

        if (paySlider.isSliderInIntialStage())
            paySlider.fullAnimate();

        hashMap.put("job_pickup_latitude", latitude);
        hashMap.put("job_pickup_longitude", longitude);
        hashMap.put(JOB_PICKUP_ADDRESS, jobPickupAddress);
        if (isCustomOrder && isPickupAnywhere) {
            hashMap.put("is_pickup_anywhere", "1");
        }
        if (isCustomOrder) {
            if (!UIManager.isCustomQuotationEnabled())
                hashMap.put("is_custom_order", "1");
            else
                hashMap.put("is_custom_order", "2");
            hashMap.remove("is_app_product_tax_enabled");
        }

        if (sochitelOperatorId != 0) {
            hashMap.put("operator", String.valueOf(sochitelOperatorId));

        }
        if (!isCustomOrder && billBreakDown.getData().getTipEnableDisable() == 1) {
            if ((StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() == 0)
                    && (isSelfPickUp)) {
                hashMap.put("tip", 0 + "");

            } else {
                hashMap.put("tip", tip + "");
            }

        }
        if (!isCustomOrder && UIManager.getIsLoyaltyEnable() == 1
                && points != 0
        ) {
            hashMap.put("loyalty_points", points + "");
        } else {
            hashMap.remove("loyalty_points");
        }

//        hashMap.put("currency_id", Utils.getCurrencyId());
        if (paymentList.get(adapterPos).getCardId() != null)
            hashMap.put("card_id", paymentList.get(adapterPos).getCardId());
        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 || billBreakDown.getData().getHoldPayment()) {
            hashMap.put("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "");
        } else {
            hashMap.put("payment_method", CASH.intValue + "");
        }
        if (promoID != null) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_code");
            hashMap.put("promo_id", promoID + "");
        } else if (referralCode != null && !referralCode.isEmpty()) {
            hashMap.remove("promo_code");
            hashMap.remove("promo_id");
            hashMap.put("referral_code", referralCode);
        } else if (promoCode != null && !promoCode.isEmpty()) {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.put("promo_code", promoCode);
        } else {
            hashMap.remove("referral_code");
            hashMap.remove("promo_id");
            hashMap.remove("promo_code");
        }


        if (transactionId != null && !transactionId.isEmpty()) {
            hashMap.put("transaction_id", transactionId);
        }
        if (jobPaymentDetailId != null && !jobPaymentDetailId.isEmpty()) {
            hashMap.put("job_payment_detail_id", jobPaymentDetailId);
        }

        if (valuePaymentMethod == PAYTM_LINK.intValue) {
            hashMap.put("paytm_number", phoneNumber);
        }

        if (billBreakDown.getData().paybleAmount.doubleValue() > 0 &&

                getPaymentProcess() == 1
//                StorefrontCommonData.getAppConfigurationData().getPostPaymentEnable() == 1
                && paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {
            hashMap.put("is_payment_done", "0");   // in case of post payment only
        } else {
            hashMap.remove("is_payment_done");
        }


        hashMap.put("delivery_charge", billBreakDown.getData().getDeliveryCharge() + "");
        hashMap.put("tax", billBreakDown.getData().getTax() + "");
        if (isCustomOrder) {
            hashMap.put("amount", 0 + "");
        } else {


            hashMap.put("amount", getAmountForBooking());
        }
        hashMap.put("AppIP", Utils.getIPAddress(true));


        if (billBreakDown.getData().getDeliveryChargesFormulaFields() != null) {
            Gson gson = new GsonBuilder().create();
            String deliveryChargePayload = gson.toJson(billBreakDown.getData().getDeliveryChargesFormulaFields());
            hashMap.put("delivery_charges_formula_fields", deliveryChargePayload);
        }
        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (hashMap.containsKey(IS_SCHEDULED) && hashMap.get(IS_SCHEDULED).equals("0")) {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
            if (hashMap.containsKey(JOB_DELIVERY_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
        }


        if (paymentMethod == FAC.intValue) {
//            hashMap.put("cvv", cvv);
            hashMap.put("fac_payment_flow", "2");
        }


        if (Dependencies.isLaundryApp()) {
            hashMap.remove("is_app_product_tax_enabled");
            hashMap.remove("job_payment_detail_id");
            if (hashMap.containsKey("is_recurring_enabled"))
                hashMap.remove("is_recurring_enabled");
        }
        //TODO

//        if (hashMap.get("is_recurring_enabled").equalsIgnoreCase("1")) {
        else if (isRecurringTaskEnable) {

            saveRecurringTask(hashMap);

        } else if (isCustomOrder) {
            hashMap.remove("is_app_product_tax_enabled");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
            hashMap.put(USER_ID, isCustomOrderMerchantLevel ? merchantUserId :
                    StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        } else {
            hashMap.put("is_app_product_tax_enabled", "1");
            if (templateDataList != null) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                hashMap.put("checkout_template", String.valueOf(myCustomArray));
            }
            hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
        }

        return hashMap;
    }

    String getAmountForBooking() {

        if (paymentList.size() > 0 && (paymentList.get(adapterPos).getPaymentMethod() == MPAISA.intValue
                || paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue)) {
            if (billBreakDown.getData().getTransactionalChargesInfo() != null && billBreakDown.getData().getTransactionalChargesInfo().getmPAISA() != null) {

                return billBreakDown.getData().getTransactionalChargesInfo().getmPAISA().getTotalCharges() + "";
//                hashMap.put("amount", billBreakDown.getData().getTransactionalChargesInfo().getmPAISA().getTotalCharges() + "");

            } else if (billBreakDown.getData().getTransactionalChargesInfo() != null
                    && billBreakDown.getData().getTransactionalChargesInfo().getStripe() != null) {

//                hashMap.put("amount", billBreakDown.getData().getTransactionalChargesInfo().getStripe().getTotalCharges() + "");
                return billBreakDown.getData().getTransactionalChargesInfo().getStripe().getTotalCharges() + "";


            } else {
                return billBreakDown.getData().getPaybleAmount() + "";
//                hashMap.put("amount", billBreakDown.getData().getPaybleAmount() + "");

            }
        } else {
            return billBreakDown.getData().getPaybleAmount() + "";

//            hashMap.put("amount", billBreakDown.getData().getPaybleAmount() + "");

        }

    }

    private Callback<BaseModel> updateTaskResponce() {
        return new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {

                orderCmpleteSucess("Order Updated Successfully");

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                paySlider.setSlideInitial();

            }
        };

    }

    private Callback<BaseModel> createTaskResponse() {
        return new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel validReqUrlPojo) {
                long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();

                CreateTaskResponse createTaskResponse = validReqUrlPojo.toResponseModel(CreateTaskResponse.class);
                successString = validReqUrlPojo.getMessage();

                if (createTaskResponse.getMappedPages() != null && createTaskResponse.getMappedPages().size() > 0)
                    mappedPages = createTaskResponse.getMappedPages();
                else {
                    mappedPages = new ArrayList<>();
                }


                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ORDER_CREATED_SUCCESS, paymentMethod + "");
                StorefrontCommonData.setLastPaymentMethod(paymentMethod);

                if (billBreakDown.getData().paybleAmount.doubleValue() > 0
                        && getPaymentProcess() == 1 &&
                        paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW
                ) {
                    setTransactionBuilder(createTaskResponse.getOrderId());
                } else {
                    createTaskSuccessPaymentResponse(validReqUrlPojo.getMessage());
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                try {
                    UnavailableProductData unavailableProductData = baseModel.toResponseModel(UnavailableProductData.class);
                    if (unavailableProductData.getUnavailableProducts() != null && unavailableProductData.getUnavailableProducts().size() > 0)
                        new UnavailableProductsDialog(MakePaymentActivity.this, unavailableProductData, mActivity, () -> onBackPressed()).show();
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }


                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ORDER_CREATED_FAILURE, paymentList.get(adapterPos).getPaymentMethod() + "");
                paySlider.setSlideInitial();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        };
    }

    public void notifyTip(double tipAmount) {
        tip = tipAmount;

        if (tipAdapter != null) {
            tipAdapter.selectedItemPos = -1;
            tipAdapter.notifyDataSetChanged();
        }

        getBillBreakdowns();
    }

    @Override
    public void onPaytmOptionClicked() {

        if (billBreakDown != null && paymentList.get(adapterPos).getPaymentMethod() == MPAISA.intValue || paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue) {
            //   setBillBreakDowns();
        }
        if (payViaHippoObject != null)
            setBillBreakDownsWhenPayViaHippo();
        else if (payViaTaskDetailsObject != null)
            setBillBreakDownsWhenPayViaTaskDetails();
        else if (paymentFor == PaymentConstants.PaymentForFlow.DEBT.intValue && userDebtData != null)
            setBillBreakDownsWhenPayForDebt();
        else if (paymentFor == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue && planList != null)
            setBillBreakDownsWhenPayForCustomerSubscription();
        else
            setBillBreakDowns();

    }

    public void openAddPaytmMoneyWebview() {
        PaymentManager.openAddPaytmMoneyWebview(mActivity, paytmVerified, paytmAddMoneyUrl);
    }

    private void createTaskSuccessPaymentResponse(String successMessage) {

        if (paymentList.get(adapterPos).getPaymentMethod() == PAYTM_LINK.intValue && payViaTaskDetailsObject != null) {
            successMessage = getStrings(R.string.payment_link_sent);
        }

        if (payViaHippoObject != null || payViaTaskDetailsObject != null) {
            new AlertDialog.Builder(mActivity)
                    .message(successMessage.isEmpty() ? getStrings(R.string.payment_successful) : successMessage)
                    .button(getStrings(R.string.ok_text))
                    .listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            finish();
                        }
                    }).build().show();
            return;
        }

        if (mappedPages != null && mappedPages.size() > 0) {
            String url = "";
            for (int i = 0; i < mappedPages.size(); i++) {
                if (mappedPages.get(i).getType().equalsIgnoreCase(Constants.ContentPagesTypes.THANKYOU_PAGE)) {
                    url = mappedPages.get(i).getTemplateData();
                    break;
                }
            }
            showSuccessFaliureDialog(successMessage, url, true);
        } else {
            orderCmpleteSucess(successMessage);
        }

    }

    private void showSuccessFaliureDialog(final String successMessage, String url,
                                          final boolean isSucess) {
        thankYouDialog =
                new Dialog(this, R.style.FullScreenDialog);
        thankYouDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        thankYouDialog.setCancelable(false);
        thankYouDialog.setContentView(R.layout.dialog_thankyou);
        Button okBT = thankYouDialog.findViewById(R.id.okBT);
        okBT.setText(getStrings(R.string.ok_got_it));
        WebView webView = thankYouDialog.findViewById(R.id.webView);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        webView.loadDataWithBaseURL("", url, mimeType, encoding, "");
        okBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSucess)
                    orderCmpleteSucess(null);
                thankYouDialog.dismiss();
            }
        });
        thankYouDialog.show();
    }

    private void orderCmpleteSucess(String successMessage) {

        if (isEditOrder)
            Utils.showToast(mActivity, successMessage);

        Bundle bundle = new Bundle();
        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
        bundle.putString(Keys.Extras.SUCCESS_MESSAGE, successMessage);
        bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        bundle.putSerializable(MakePaymentActivity.class.getName(), hashMap);

        Transition.transitBookingSuccess(mActivity, bundle);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void validateOrder() {
        if (paySlider.isSliderInIntialStage())
            paySlider.fullAnimate();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity));
        commonParams.add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
        commonParams.add("dual_user_key", UIManager.isDualUserEnable());
        commonParams.add("layout_type", hashMap.get(LAYOUT_TYPE));
        commonParams.add("timezone", Dependencies.getTimeZoneInMinutes());
        commonParams.add("vertical", hashMap.get(VERTICAL));
        commonParams.add("marketplace_reference_id", Dependencies.getMarketplaceReferenceId());
        commonParams.add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add("has_pickup", hashMap.get(HAS_PICKUP));
        commonParams.add("has_delivery", hashMap.get(HAS_DELIVERY));
        commonParams.add("job_pickup_latitude", latitude);
        commonParams.add("job_pickup_longitude", longitude);
        commonParams.add("job_pickup_address", jobPickupAddress);
        commonParams.add("job_pickup_email", hashMap.get(JOB_PICKUP_EMAIL));
        commonParams.add("job_pickup_phone", hashMap.get(JOB_PICKUP_PHONE));
        commonParams.add("job_pickup_name", hashMap.get(JOB_PICKUP_NAME));
        commonParams.add("is_scheduled", hashMap.get(IS_SCHEDULED));
        commonParams.add("products", hashMap.get("products"));
        commonParams.add("domain_name", hashMap.get("domain_name"));

        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP)
                commonParams.add("self_pickup", 1);
            else
                commonParams.add("self_pickup", 0);
        } else {
            commonParams.add("self_pickup", 0);
        }

        commonParams.add("job_description", hashMap.get(JOB_DESCRIPTION_FIELD));

        if (!isCustomOrder && billBreakDown.getData().getTipEnableDisable() == 1 && tip > 0) {
            if ((StorefrontCommonData.getAppConfigurationData().getShowTipInPickup() == 0)
                    && (isSelfPickUp)) {
                hashMap.put("tip", 0 + "");
            } else {
                hashMap.put("tip", tip + "");
            }//            commonParams.add("tip_type", "2");
        }
        commonParams.add("currency_id", hashMap.get("currency_id"));
        commonParams.add("amount", billBreakDown.getData().getPaybleAmount() + "");
        commonParams.add("subtotal_amount", billBreakDown.getData().getActualAmount() + "");

        if (paymentList.get(adapterPos).getCardId() != null)
            commonParams.add("card_id", paymentList.get(adapterPos).getCardId());

        commonParams.add("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "");

        commonParams.add("AppIP", Utils.getIPAddress(true));

        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (hashMap.containsKey(USER_ID)) {
                commonParams.add(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }
        } else {
            commonParams.add(USER_ID, hashMap.get(USER_ID));
        }

        if (hashMap.containsKey(IS_SCHEDULED) && hashMap.get(IS_SCHEDULED).equals("0")) {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                commonParams.add(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
            if (hashMap.containsKey(JOB_DELIVERY_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_DELIVERY_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                commonParams.add(JOB_DELIVERY_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
        } else {
            commonParams.add(JOB_PICKUP_DATETIME, hashMap.get(JOB_PICKUP_DATETIME));
            commonParams.add(JOB_DELIVERY_DATETIME, hashMap.get(JOB_DELIVERY_DATETIME));
        }
        if (templateDataList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
            commonParams.add("checkout_template", String.valueOf(myCustomArray));
        }

        commonParams.add("perform_validation", "1");

        RestClient.getApiInterface(this).validateServingDistance(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        hashMap.remove("perform_validation");
                        long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();

                        if (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {
                            setTransactionBuilder();
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        paySlider.setSlideInitial();

                        try {
                            UnavailableProductData unavailableProductData = baseModel.toResponseModel(UnavailableProductData.class);
                            if (unavailableProductData.getUnavailableProducts() != null && unavailableProductData.getUnavailableProducts().size() > 0)
                                new UnavailableProductsDialog(MakePaymentActivity.this, unavailableProductData, mActivity, () -> onBackPressed()).show();
                        } catch (Exception e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
    }

    private int isLaundryEditOrder() {
        try {
            if (payViaHippoObject != null && payViaHippoObject.has("remaining_balance") &&
                    payViaHippoObject.getDouble("remaining_balance") > 0)
                return 1;
        } catch (JSONException e) {
            return 0;
        }
        return 0;
    }

    @Override
    public void onPayulatamSuccess(final String payuTransactionId) {
        if (userDebtData != null || planList != null) {
            createCharge(payuTransactionId);

        } else {
            if (payViaHippoObject != null || payViaTaskDetailsObject != null) {
                createTaskSuccessPaymentResponse(successString);

            } else {
                if (getPaymentProcess() == 1) {
//                if (StorefrontCommonData.getAppConfigurationData().getPostPaymentEnable() == 1) {
                    createTaskSuccessPaymentResponse(successString);
                } else {
                    createTaskViaVendor(adapterPos, payuTransactionId, "");
                }
            }
        }
    }

    @Override
    public void onPayulatamFailure() {
        paySlider.setSlideInitial();
    }

    private void setTransactionBuilder() {
       /* if (paymentList.get(adapterPos).getPaymentMethod() == PAYU_LATAM.intValue) {
            HashMap<String, String> map = PayulatamPaymentManager.getMap(0, mActivity, billBreakDown.getData().getPaybleAmount());
            if (payViaTaskDetailsObject != null)
                map.put("payment_for", PaymentConstants.PaymentForFlow.REPAY_FROM_TASK_DETAILS.intValue + "");

            new Payulatam.PayulatamBuilder(mActivity)
                    .setMap(map)
                    .setListener(MakePaymentActivity.this)
                    .build();
        } else {*/
        String currency = "";
        if (billBreakDown != null
                && billBreakDown.getData() != null
                && billBreakDown.getData().getPaymentSettings() != null
                && billBreakDown.getData().getPaymentSettings().getCode() != null
                && !billBreakDown.getData().getPaymentSettings().getCode().isEmpty())
            currency = billBreakDown.getData().getPaymentSettings().getCode();


        HashMap<String, String> map = PaymentTransactionUrlManager.getMap(mActivity,
                paymentList.get(adapterPos).getPaymentMethod(), billBreakDown.getData().getPaybleAmount(), 0, paymentList.get(adapterPos), currency);
        setTransactionBuilder(map);
//        }
    }

    private void setTransactionBuilder(int orderId) {
        if ((userDebtData != null || planList != null) && (paymentList.get(adapterPos).getPaymentMethod() != PAYFORT.intValue) &&
                (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.CARD
                        || paymentList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue
                        || paymentList.get(adapterPos).getPaymentMethod() == PAYTM.intValue
                        || paymentList.get(adapterPos).getPaymentMethod() == AZUL.intValue
                        || paymentList.get(adapterPos).getPaymentMethod() == AUTHORISE_DOT_NET.intValue)
        ) {
            createCharge("");
        } else {
            if (paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue ||
                    paymentList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue) {
                HashMap<String, String> map = new HashMap<>();
                if (isOrderPayment) {

                    String amount = "";

                    if (userDebtData != null)
                        amount = StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount() + "";
                    else if (planList != null)
                        amount = planList.getAmount() + "";
                    else
                        amount = billBreakDown.getData().getPaybleAmount();

                    String currency = "";
                    if (hippoIsCustomOrder) {
                        try {
                            currency = payViaHippoObject.getJSONObject("currencyObj").getString("code");
                        } catch (JSONException e) {
                            Utils.printStackTrace(e);
                        }

                    }

                    map = PaymentTransactionUrlManager.getMap(mActivity,
                            paymentList.get(adapterPos).getPaymentMethod(),
                            amount
                            , orderId, paymentList.get(adapterPos), currency);

                    if (planList != null) {
                        map.remove(JOB_ID);
                    }

                    setTransactionBuilder(map);
                } else {
                    UserData userData = StorefrontCommonData.getUserData();

                    map.put("domain_name", StorefrontCommonData.getFormSettings().getDomainName());
                    map.put("is_transaction_failed", "0");

                    if (billBreakDown != null
                            && billBreakDown.getData() != null
                            && billBreakDown.getData().getPaymentSettings() != null
                            && !billBreakDown.getData().getPaymentSettings().getCode().isEmpty())
                        map.put("currency", billBreakDown.getData().getPaymentSettings().getCode());
                    else
                        map.put("currency", Utils.getCurrencyCode());

                    if (paymentList.get(adapterPos).getPaymentMethod() == STRIPE.intValue)
                        map.put("card_id", paymentList.get(adapterPos).getCardId());
                    map.put("amount", getAmountForBooking());
//                    map.put("amount", billBreakDown.getData().getPaybleAmount());
                    map.put("payment_method", paymentList.get(adapterPos).getPaymentMethod() + "");
                    map.put("payment_for", String.valueOf(PaymentConstants.PaymentForFlow.REWARD.intValue));
                    map.put("vendor_id", userData.getData().getVendorDetails().getVendorId().toString());
                    map.put("marketplace_user_id", String.valueOf(userData.getData().getVendorDetails().getMarketplaceUserId()));
                    map.put("access_token", Dependencies.getAccessToken(this));
                    map.put("user_id", StorefrontCommonData.getFormSettings().getUserId().toString());
                    map.put("app_access_token", Dependencies.getAccessToken(this));
                    map.put("app_type", "ANDROID");

                    setTransactionBuilderForRewardStripe(map, PaymentConstants.PaymentForFlow.REWARD.intValue);
                }
            } else {
                String amount = "";

                if (userDebtData != null) {
                    amount = StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount() + "";
                } else if (planList != null) {
                    amount = planList.getAmount() + "";
                } else {
                    amount = getAmountForBooking();
                }
                String currency = "";

                if (hippoIsCustomOrder) {
                    try {
                        currency = payViaHippoObject.getJSONObject("currencyObj").getString("code");
                    } catch (JSONException e) {
                        Utils.printStackTrace(e);
                    }

                }

                HashMap<String, String> map = PaymentTransactionUrlManager.getMap(mActivity,
                        paymentList.get(adapterPos).getPaymentMethod(),
                        amount
                        , orderId, paymentList.get(adapterPos), currency);
                if (planList != null) {
                    map.remove(JOB_ID);
                }

                setTransactionBuilder(map);
            }
        }
    }

    private void setTransactionBuilder(final HashMap<String, String> map) {
        if (paymentList.get(adapterPos).getPaymentMethod() == PAYFORT.intValue) {
            map.put("vendor_card_id", paymentList.get(adapterPos).getCardId());
        } else if (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.CARD) {
            map.put("card_id", paymentList.get(adapterPos).getCardId());
        }

        map.put("isEditedTask", isLaundryEditOrder() + "");
        map.put("subtotal_amount", billBreakDown.getData().getActualAmount() + "");


        long paymentFor = PaymentConstants.PaymentForFlow.ORDER_PAYMENT.intValue;

        if (payViaTaskDetailsObject != null) {
            paymentFor = PaymentConstants.PaymentForFlow.REPAY_FROM_TASK_DETAILS.intValue;
        } else if (userDebtData != null) {
            paymentFor = PaymentConstants.PaymentForFlow.DEBT.intValue;
        } else if (planList != null) {
            paymentFor = PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue;
        } else if (hippoIsCustomOrder) {
            paymentFor = PaymentConstants.PaymentForFlow.CUSTOM_ORDER_HIPPO.intValue;
        }

        int userId;

        if (payViaTaskDetailsObject != null) {
            userId = payViaTaskDetailsObject.getUserId();
        } else if (payViaHippoObject != null && hippoUseId != -1) {

            userId = hippoUseId;

        } else if (isCustomOrderMerchantLevel) {
            userId = Integer.parseInt(merchantUserId);
        } else if (isCustomOrder || userDebtData != null || planList != null) {

            userId = StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId();

        } else if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            userId = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getStorefrontUserId();
        } else {
            userId = StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId();

        }

        if (paymentList.get(adapterPos).getPaymentMethod() == PAYFAST.intValue && hashMap != null) {
            JSONArray jsonArray = new JSONArray();
            if (hashMap.containsKey("products")) {
                for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {
                    jsonArray.put(Dependencies.getSelectedProductsArrayList().get(i).getProductId());
                }
            }
            map.put("product_ids", jsonArray.toString());
        }


        int paymentProcessType = getPaymentProcess();
        int isLockEnabled = getPaymentLockStatus();


        if (map.containsKey("phone") && map.get("phone").isEmpty())
            map.remove("phone");

        String additionalpaymentId = "";
        int jobId = 0;
        if (map.containsKey("job_id"))
            jobId = Integer.parseInt(map.get("job_id"));


        if (payViaHippoObject != null && hippoIsCustomOrder) {
            try {
                additionalpaymentId = payViaHippoObject.getInt("additionalpaymentId") + "";
                jobId = (int) payViaHippoObject.getDouble("order_id");
                userId = payViaHippoObject.getInt("user_id");


            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }
        }

        if (rewardPaymentId > 0 && paymentProcessType == 2) {
            map.put("payment_for", PaymentConstants.PaymentForFlow.REWARD.intValue + "");
            paymentFor = PaymentConstants.PaymentForFlow.REWARD.intValue;

        }else if(rewardPaymentId > 0 && paymentProcessType == 0){
                map.put("payment_for", PaymentConstants.PaymentForFlow.REWARD.intValue + "");
                paymentFor = PaymentConstants.PaymentForFlow.REWARD.intValue;
            }

            if (paymentProcessType == 2 && paymentFor == 0 && hashMap != null) {
            new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                    .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                    .setPaymentForFlow(paymentFor)
                    .setMap(map)
                    .setCreateTaskData(getcreateTaskmap(adapterPos, "", "", ""))
                    .setUserId(userId)
                    .setIsLockEnabled(isLockEnabled)
                    .setadditionalpaymentId(additionalpaymentId)
                    .setJobId(jobId)
                    .setPaymentData(paymentList.get(adapterPos))
                    .setIsorderPayment(isOrderPayment)
                    .setStoreName(storename)
                    .setListener(this)
                    .build();
        } else {

            if (paymentProcessType == 2) {
                new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                        .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                        .setPaymentForFlow(paymentFor)
                        .setMap(map)
                        .setUserId(userId)
                        .setIsLockEnabled(isLockEnabled)
                        .setCreateTaskData(createChargeHashmap("").build().getMap())
                        .setJobId(jobId)
                        .setadditionalpaymentId(additionalpaymentId)
                        .setPaymentData(paymentList.get(adapterPos))
                        .setIsorderPayment(isOrderPayment)
                        .setListener(this)
                        .build();
            } else {
                new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                        .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                        .setPaymentForFlow(paymentFor)
                        .setMap(map)
                        .setUserId(userId)
                        .setIsLockEnabled(isLockEnabled)
                        .setJobId(jobId)
                        .setadditionalpaymentId(additionalpaymentId)
                        .setPaymentData(paymentList.get(adapterPos))
                        .setIsorderPayment(isOrderPayment)
                        .setListener(this)
                        .build();
            }


        }
    }

    private void setTransactionBuilderForRewardStripe(final HashMap<String, String> map,
                                                      long paymentFlow) {
        String additionalpaymentId = "";
        int jobId = 0;
        if (map.containsKey("job_id"))
            jobId = Integer.parseInt(map.get("job_id"));

        try {
            additionalpaymentId = payViaHippoObject.getInt("additionalpaymentId") + "";
            jobId = (int) payViaHippoObject.getDouble("order_id");
            if (hippoIsCustomOrder
            )
                map.put("user_id", payViaHippoObject.getInt("user_id") + "");

        } catch (JSONException e) {
            Utils.printStackTrace(e);
        }

        new PaymentTransactionUrl.PaymentTransactionBuilder(mActivity)
                .setPaymentMethod(paymentList.get(adapterPos).getPaymentMethod())
                .setPaymentForFlow(paymentFlow)
                .setMap(map)
                .setJobId(jobId)
                .setadditionalpaymentId(additionalpaymentId)
                .setPaymentData(paymentList.get(adapterPos))
                .setIsorderPayment(isOrderPayment)
                .setListener(this)
                .build();
    }

    @Override
    public void onTransactionSuccess(String transactionId, String jobPaymentDetailId) {
//        paySlider.setSlideInitial();
        long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();


        if (userDebtData != null || planList != null || hippoIsCustomOrder) {

            if ((getPaymentProcess() == 2 || paymentMethod == STRIPE.intValue || paymentMethod == INAPP_WALLET.intValue)
                    && userDebtData == null
                    && planList == null
            ) {
                createTaskSuccessPaymentResponse(getStrings(R.string.payment_successful));
            } else {
                createCharge(transactionId);
            }
        } else {
            if (paymentMethod == PAYFORT.intValue) {
                if (payViaHippoObject == null && payViaTaskDetailsObject == null)
                    createTaskViaVendor(adapterPos, transactionId, jobPaymentDetailId);
                else {
                    if (isOrderPayment) {
                        createTaskSuccessPaymentResponse(successString);
                    } else {
                        apiHitToBuyPlan(transactionId);
                    }
                }
            } else if (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW) {

                if (!isOrderPayment) {
                    apiHitToBuyPlan(transactionId);
                } else {


                    if (getPaymentProcess() == 2) {
                        createTaskSuccessPaymentResponse(getStrings(R.string.payment_successful));
//                    } else if (StorefrontCommonData.getAppConfigurationData().getPostPaymentEnable() == 0 && (payViaHippoObject == null && payViaTaskDetailsObject == null && userDebtData == null)) {
                    } else if (getPaymentProcess() == 0 && (payViaHippoObject == null && payViaTaskDetailsObject == null && userDebtData == null && planList == null)) {
                        createTaskViaVendor(adapterPos, transactionId, "");
                    } else {
                        createTaskSuccessPaymentResponse(successString);
                    }
                }
            } else if (paymentMethod == BILLPLZ.intValue /*|| paymentMethod == PAYPAL.intValue*/) {
                createTaskSuccessPaymentResponse(successString);
            } else if ((paymentMethod == STRIPE.intValue || paymentMethod == INAPP_WALLET.intValue
                    || paymentMethod == AZUL.intValue|| paymentMethod == AUTHORISE_DOT_NET.intValue) && !isOrderPayment) {
                apiHitToBuyPlan(transactionId);
            } else {
                createTaskSuccessPaymentResponse(getStrings(R.string.payment_successful));
            }
        }
    }

    private int getPaymentProcess() {
        int paymentProcessType = 0;
        for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
            if (StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getValue() == paymentList.get(adapterPos).getPaymentMethod()) {
                paymentProcessType = StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getPaymentProcessType();
                break;
            }
        }
        return paymentProcessType;
    }

    public void createCharge(String transactionId) {

        CommonParams.Builder commonParams = createChargeHashmap(transactionId);

        RestClient.getApiInterface(mActivity).createCharge(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                paySlider.setSlideInitial();
                CreateChargeData data = baseModel.toResponseModel(CreateChargeData.class);
                StorefrontCommonData.getUserData().getData().getVendorDetails().setDebtAmount(data.getDebtAmount());

                Bundle bundleExtra = new Bundle();
                bundleExtra.putDouble("debtAmount", data.getDebtAmount());
                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundleExtra);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                paySlider.setSlideInitial();
                if (userDebtData != null) {
                    Intent intent = new Intent();
                    setResult(RESULT_ERROR, intent);
                    finish();
                }
            }
        });

    }


    public CommonParams.Builder createChargeHashmap(String transactionId) {

        int paymentProcessType = getPaymentProcess();


        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add("user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("currency", Utils.getCurrencyCode())
                .add("payment_method", paymentList.get(adapterPos).getPaymentMethod());

        if (rewardPaymentId > 0 && paymentProcessType == 2) {
            commonParams.add("payment_for", PaymentConstants.PaymentForFlow.REWARD.intValue + "");
            commonParams.add("reward_id", rewardPaymentId + "");
        }

        if (userDebtData != null)
            commonParams.add("payment_for", PaymentConstants.PaymentForFlow.DEBT.intValue)
                    .add("amount", userDebtData.getDebtAmount())
                    .add("job_id", userDebtData.getJobId());
        else if (planList != null)
            commonParams.add("payment_for", PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue)
                    .add("amount", planList.getAmount())
//                    .add("job_id", planList.getPlanId())
                    .add("plan_id", planList.getPlanId());


        if (paymentList.get(adapterPos).getPaymentMethod() == FAC.intValue && !hippoIsCustomOrder) {
            commonParams.add("fac_payment_flow", "2");
        }


        if (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.CARD) {
            commonParams.add("card_id", paymentList.get(adapterPos).getCardId());

        } else if (paymentList.get(adapterPos).getPaymentMethod() != INAPP_WALLET.intValue
                && paymentList.get(adapterPos).getPaymentMethod() != PAYTM.intValue) {
            commonParams.add("transaction_id", transactionId);
        }

        if (paymentList.get(adapterPos).getPaymentMethod() == PAYFORT.intValue)
            commonParams.add("transaction_id", transactionId);

        if (hippoIsCustomOrder) {
            commonParams.add("payment_for", 10);

            try {
                commonParams.add("user_id", payViaHippoObject.getInt("user_id") + "");

//                JSONObject currencyObj = payViaHippoObject.getJSONObject("currencyObj");
//
//                commonParams.add("currency_id", currencyObj.getInt("currency_id") + "");

                commonParams.add("job_id", (int) payViaHippoObject.getDouble("order_id"))
                        .add("amount", (int) payViaHippoObject.getDouble("total_amount"))
                        .add("additionalpaymentId", payViaHippoObject.getInt("additionalpaymentId"));
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }

        }

        if (transactionId.isEmpty())
            commonParams.remove("transaction_id");

        return commonParams;
    }

    @Override
    public void onTransactionFailure(String transactionId) {
        paySlider.setSlideInitial();
        long paymentMethod = paymentList.get(adapterPos).getPaymentMethod();
        if (userDebtData != null || planList != null) {
            Intent intent = new Intent();
            setResult(RESULT_ERROR, intent);
            finish();
        } else {

            if (paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW
                    && getPaymentProcess() == 2) {
                Utils.snackBar(this, getStrings(R.string.pre_transaction_failed), false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.finish();
                        mActivity.startActivity(mActivity.getIntent());
                    }
                },1000);

            } else if ((paymentMethod == BILLPLZ.intValue
                    || paymentMethod == PAYPAL.intValue
                    || paymentMethod == RAZORPAY.intValue
                    || paymentMethod == PAYMOB.intValue)
//                    && (StorefrontCommonData.getAppConfigurationData().getPostPaymentEnable() == 1 ||
                    && (getPaymentProcess() == 1 ||
                    payViaHippoObject != null || payViaTaskDetailsObject != null || userDebtData != null || planList != null)) {

                if (mappedPages != null && mappedPages.size() > 0) {
                    createTaskSuccessPaymentResponse("");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                    bundle.putString(Keys.Extras.FAILURE_MESSAGE, getStrings(R.string.transaction_failed));

                    Transition.transitBookingSuccess(mActivity, bundle);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }


            } else if (
                    paymentList.get(adapterPos).getPaymentFlowType() == Constants.PAYMENT_MODES.WEBVIEW
                            && getPaymentProcess() == 1 && payViaHippoObject == null) {
                if (mappedPages != null && mappedPages.size() > 0) {
                    createTaskSuccessPaymentResponse(payViaTaskDetailsObject != null ? getStrings(R.string.repay_transaction_failed) : getStrings(R.string.transaction_failed));
                } else {
                    createTaskSuccessPaymentResponse(payViaTaskDetailsObject != null ? getStrings(R.string.repay_transaction_failed) : getStrings(R.string.transaction_failed), true);
                }
            } else {
                Utils.snackBar(this, getStrings(R.string.pre_transaction_failed), false);
            }
        }
    }

    @Override
    public void onTransactionApiError() {

        if (getPaymentProcess() == 1 && payViaHippoObject == null && paymentFor == 0) {
            createTaskSuccessPaymentResponse(payViaTaskDetailsObject != null ? getStrings(R.string.repay_transaction_failed) : getStrings(R.string.transaction_failed), true);

        } else {
            paySlider.setSlideInitial();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(getIntent());
                }
            }, 1000);

        }
    }

    /**
     * if in payfort hold payment flow
     * then show alert to user of amount to be holded
     */
    @Override
    public void onPaymentOptionSelected(Datum data) {
        if (billBreakDown != null && billBreakDown.getData() != null && billBreakDown.getData().getHoldPayment()
                && data.getPaymentMethod() == PAYFORT.intValue
                && (payViaHippoObject == null && payViaTaskDetailsObject == null)) {
            tvHoldPayment.setText(getStrings(R.string.amount_amount_will_be_holded).replace(AMOUNT, Utils.getCurrencySymbol() + billBreakDown.getData().getHoldAmount()));
//                    String.format("%s%s " + getStrings(R.string.amount_will_be_holded), Utils.getCurrencySymbol(), billBreakDown.getData().getHoldAmount()));
            rlHoldPayment.setVisibility(View.VISIBLE);
        } else {
            rlHoldPayment.setVisibility(View.GONE);
        }
        if (data.getPaymentMethod() == RAZORPAY_UPI.intValue) {
            Checkout.preload(getApplicationContext());
        }

    }

    public void apiHitToBuyPlan(String transactionId) {

        int paymentProcessType = getPaymentProcess();

        Intent intent = new Intent();
        intent.putExtra(PlanDetailsActivity.EXTRA_TRANSACTION_ID, transactionId);
        intent.putExtra(PlanDetailsActivity.EXTRA_PAYMENT_FLOW, paymentList.get(adapterPos).getPaymentMethod());
        intent.putExtra(PlanDetailsActivity.EXTRA_PAYMENT_FLOW_TYPE, paymentProcessType);

        setResult(RESULT_OK, intent);
        finish();

    }


    private void getPaymentMethods() {

        HashMap<String, String> paymenthashMap = new HashMap<>();

        // paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity));
        } else {
            paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }

        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        if ((paymentFor == PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue && planList != null) || (isCustomOrder && !isCustomOrderMerchantLevel)) {
            paymenthashMap.put(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");

        }
        if (hippoIsCustomOrder) {
            try {
                paymenthashMap.put(USER_ID, payViaHippoObject.getInt("user_id") + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
                if (hitFetchMerchantCard) {
                    fetchMerchantCards();
                    if (PaymentMethodsClass.isPaytmEnabled()) paytmCheckBalance();
                    if (PaymentMethodsClass.isInAppWalletPaymentEnabled()) getWalletBalance();
                } else {
                    paymentList.clear();
                    PaymentMethodsClass.getPaymentList(mActivity, paymentList, true, isOrderPayment && userDebtData == null);
                    for (int i = 0; i < paymentList.size(); i++) {
                        if (adapterPos != null && adapterPos == i) {
                            paymentList.get(adapterPos).selectedCard = true;
                        } else {
                            paymentList.get(i).selectedCard = false;
                        }
                    }

                    setPaymentAdapter(paymentList);
                    getdataBill();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }





    private int getPaymentLockStatus() {
        int isLockEnabled = 0;
        for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
            if (StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getValue() == paymentList.get(adapterPos).getPaymentMethod()) {
                isLockEnabled = StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getLockEnabled();
                break;
            }
        }
        return isLockEnabled;
    }

    @Override
    public void onPaymentSuccess(String transactionId, PaymentData paymentData) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOrderPayment) {
                    ((MakePaymentActivity) mActivity).onTransactionSuccess(transactionId, "");
                } else {
                    ((MakePaymentActivity) mActivity).apiHitToBuyPlan(transactionId);
                }
            }
        },2500);

    }

    @Override
    public void onPaymentError(int i, String transactionId, PaymentData paymentData) {
        Log.e("onPaymentError", "" + paymentData);
        onTransactionFailure(transactionId);
    }

}
