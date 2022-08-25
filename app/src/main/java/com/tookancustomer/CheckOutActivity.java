package com.tookancustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.adapters.CheckoutCartAdapter;
import com.tookancustomer.adapters.SideOrderCheckoutAdapter;
import com.tookancustomer.agentListing.AgentData;
import com.tookancustomer.agentListing.AgentListingCheckoutActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.ExtraConstants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.cancellationPolicy.ViewCancellationPolicyActivity;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.checkoutTemplate.model.CheckoutTemplateStatus;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AdminVerficationDialog;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.UnavailableProductsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.models.SendPaymentTask.Data;
import com.tookancustomer.models.SendPaymentTask.SendPaymentForTask;
import com.tookancustomer.models.UnavailableProductData.UnavailableProductData;
import com.tookancustomer.models.billbreakdown.BillBreakDowns;
import com.tookancustomer.models.reqCatalogue.ReqError;
import com.tookancustomer.models.sochitelOperators.SochitelOperators;
import com.tookancustomer.models.staticAddressData.StaticAddressData;
import com.tookancustomer.models.subscription.Days;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.modules.merchantCatalog.adapters.MerchantProductsAdapter;
import com.tookancustomer.modules.recurring.SubscriptionActivity;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.listDialog.ListDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tookancustomer.appdata.Codes.StatusCode.SHOW_REQUIRED_CATALOUGE;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;
import static com.tookancustomer.appdata.Keys.Prefs.OLD_DELIVERY_CHARGE;

import static com.tookancustomer.appdata.StorefrontCommonData.getFormSettings;

public class CheckOutActivity extends BaseActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        Keys.APIFieldKeys, AdminVerficationDialog.AdminVerificationDialogListener, ListDialog.OnListItemClickListener {
    public Dialog oftenDialog;
    private String TAG = CheckOutActivity.class.getSimpleName();
    private boolean misOftenBought = true;
    private int layout_type = 1;
    private double freeDeliveryAmount;
    private boolean isDontShowOftenBought = false;


    // Main Screen fields
    private NestedScrollView svCheckout;
    private ShimmerFrameLayout shimmerLayout;
    // Address fields
    private TextView tvAddressHeader, tvSideOrderLabel, tvFreeDeliveryHeader;
    private LinearLayout llSelfPickupDelivery, llPickAndDrop, llHomeDelivery, llSelfPickup;
    private TextView tvHomeDelivery, tvSelfPickup, tvPickAndDrop;
    private TextView etAddress;
    private RelativeLayout rlPickup, rlDeliveryView, rlPickAndDropView;
    // Address variables
    private boolean isPickupAddress;
    private boolean isPickupAddressPdflow = false;
    private int selectedPickupMode = Constants.SelectedPickupMode.NONE;
    private Double pickUpLatitude = 0.0, pickUpLongitude = 0.0;
    private Double deliverLatitude = 0.0, deliverLongitude = 0.0;
    //    private Double pickUpPdflowLatitude = 0.0, picketDeliverAddressUpPdflowLongitude = 0.0;
    private String address = "";
    private ArrayList<StaticAddressData> addressesList;
    // User detail fields
    private LinearLayout llUserDetailsParentView;
    private TextView tvUserCountryCode;
    private EditText etUserName, etUserEmail, etUserPhoneNumber;
    private CheckBox cbDeliverAddressSameAsPickup;
    // PickUp detail fields
    private TextView etPickupAddress, tvPickupUserCountryCode;
    private EditText etPickupUserName, etPickupUserEmail, etPickupUserPhoneNumber;
    private boolean isDeliveryCountryCode = true;
    // Delivery detail fields
    private TextView etDeliverAddress, tvDeliveryUserCountryCode;
    private EditText etDeliveryUserName, etDeliveryUserEmail, etDeliveryUserPhoneNumber;
    private boolean isPickupCountryCode = true;
    // Cart fields
    private TextView tvCartHeader, tvCartAction, tvSubTotal, tvAdditionalHeading, tvTotalHeading, tvAdditionalAmount, tvTotalAmount;
    private ImageView ibArrow;
    private LinearLayout llCartChild;
    private RecyclerView rvCart;
    private RelativeLayout rlSubtotalPrice, rlAdditionalPrice, rlTotallPrice;
    // DateTime fields
    private LinearLayout llSelectDateTime;
    private TextView tvDateTimeHeader, tvCancellationPolicy;
    private RadioGroup rgInstantSchedule;
    private RadioButton rbInstant, rbSchedule, rbSubscribe;
    private LinearLayout llStartDate, llEndDateSchedule;
    private EditText etStartDateTime, etEndDateTimeSchedule;
    private int serviceTime = 0; //For laundry, difference between pickup and deliver datetime.
    // DateTime variables
    private Integer instantTask, scheduledTask, isStartEndTimeEnabled = 0, subscribeTask;
    private boolean isStartDate;
    private Date selectedStartDate, selectedEndDate;
    private Date endStartDateLaundry;
    // Extra notes fields
    private EditText etDescription;
    // Extra fields
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private Double minAmountPrice = 0.0;
    private boolean paymentMethodEnabled = false;
    private long valueEnabledPayment;
    private int isScheduled = 0;
    //For checkout template flow
    private ArrayList<Template> templateDataList;
    private boolean isCheckoutTemplateEnabled;
    private RecyclerView rvSideOrder;
    private SideOrderCheckoutAdapter sideOrderCheckoutAdapter;
    private ArrayList<Datum> sideOrderArrayList = new ArrayList<>();
    private LinearLayout llSideOrder;
    private double productTotalCalculatedPrice = 0.0;
    private double finalAmount = 0.0;
    //Subscription
    private String startDate, startEnd, startTime, endDate, endAfter;
    private ArrayList<Days> selectedDaysList;
    private boolean isSubscriptionCount;
    //select agent layout
    private LinearLayout llSelectAgent;
    private TextView tvAgentHeader;
    private EditText etSelectAgent;
    private int selectedAgentID = 0;
    private boolean isEditOrder;
    private int editJobId;
    private ImageView ivHomedelivery, ivSelfPickup, ivPickAndDrop;
    private boolean isFromPDFlowAddressChange = false;
    private Dialog mDialog;
    private boolean isAddressPopUpShown = false;
    private ArrayList<String> arrayListSochitelOp = new ArrayList();
    private ArrayList<String> arrayListSochitelOpIds = new ArrayList();
    private String sochitelId;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mActivity = this;

        rlPickAndDropView = findViewById(R.id.rlPickAndDropView);
        llPickAndDrop = findViewById(R.id.llPickAndDrop);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        svCheckout = findViewById(R.id.svCheckout);
        shimmerLayout.setVisibility(View.VISIBLE);
        svCheckout.setVisibility(View.GONE);
        getpaymentmethods();

        if (StorefrontCommonData.getAppConfigurationData().getSochitelEnable() == 1) {
            //if account id is added
            getSochitelOperators();
        }
    }

    private void showSochitelOperatorDialog() {
        ListDialog.with(mActivity).show("Select Operator", arrayListSochitelOp, this);
    }

    private void getSochitelOperators() {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add("is_app", 1)
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
        RestClient.getApiInterface(mActivity).getSochitelOperators(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                SochitelOperators mSochitelOperators = new SochitelOperators();
                com.tookancustomer.models.sochitelOperators.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.sochitelOperators.Datum[].class);
                try {
                    mSochitelOperators.setData(new ArrayList<>(Arrays.asList(datum)));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                if (mSochitelOperators.getData().size() == 0) {
                    Utils.showToast(CheckOutActivity.this, "You must configure sochitel in order to proceed");
                    finish();
                    return;
                }

                for (int i = 0; i < mSochitelOperators.getData().size(); i++) {
                    arrayListSochitelOp.add(mSochitelOperators.getData().get(i).getName());
                    arrayListSochitelOpIds.add(mSochitelOperators.getData().get(i).getId());
                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    private void getpaymentmethods() {
        HashMap<String, String> paymenthashMap = new HashMap<>();

        // paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity));
        } else {
            paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }

        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        paymenthashMap.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            paymenthashMap.put(LANGUAGE, StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            paymenthashMap.put(LANGUAGE, "en");

        }


        RestClient.getApiInterface(mActivity).getPaymentMethods(paymenthashMap).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Log.e("payment......", "payment");
                shimmerLayout.setVisibility(View.GONE);
                svCheckout.setVisibility(View.VISIBLE);

                ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentMethods = new Gson().fromJson(new Gson().toJson(baseModel.data), new TypeToken<ArrayList<PaymentMethod>>() {
                }.getType());
                Log.e("payment......", "payment");
                PaymentMethodsClass.clearPaymentHashMaps();
                StorefrontCommonData.getFormSettings().setPaymentMethods(paymentMethods);
                setdata(false);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
//                setdata();
            }
        });
    }

    private void showAddressDialog() {

        mDialog = new Dialog(this);


        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm_address);
        mDialog.setCanceledOnTouchOutside(true);

        Button btnChangeAddress = mDialog.findViewById(R.id.btnChangeAddress);
        Button btnConfirmAddress = mDialog.findViewById(R.id.btnConfirmAddress);
        TextView tvDeliveryAddressHeader = mDialog.findViewById(R.id.tvDeliveryAddressHeader);
        TextView tvDeliveringTo = mDialog.findViewById(R.id.tvDeliveringTo);

        btnChangeAddress.setText(StorefrontCommonData.getString(this, R.string.change));
        btnConfirmAddress.setText(StorefrontCommonData.getString(this, R.string.confirm));
        tvDeliveryAddressHeader.setText(StorefrontCommonData.getString(this, R.string.delivery_address).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()));
        tvDeliveringTo.setText(StorefrontCommonData.getString(this, R.string.delivering_to));
        TextView tvAddress = mDialog.findViewById(R.id.tvAddress);
        ImageView ivCloseDialog = mDialog.findViewById(R.id.ivCloseDialog);

        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            tvAddress.setText(etDeliverAddress.getText().toString());
        } else {
            tvAddress.setText(etAddress.getText().toString());
        }


        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
                    isFromPDFlowAddressChange = true;
                } else {
                    isFromPDFlowAddressChange = false;
                }

                if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                    gotoMapActivity();
                } else {
                    if (isStaticAddressFlow()) {
                        goToStaticAddressActivity();
                    } else {
                        gotoFavLocationActivityFromPopUp();
                    }
                }

                mDialog.dismiss();

            }
        });
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        btnConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                isAddressPopUpShown = true;
                findViewById(R.id.tvProceedToPay).performClick();
                /*if (isValidTaskDetails()) {
                    createSingleTaskWithPayment();
                }*/
            }
        });
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.show();
    }


    private void setdata(boolean fromOftenBought) {
        if (Dependencies.getSelectedProductsArrayList().size() == 0) {
            finish();
        }
        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            storefrontData = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData();
            getSingleMerchantDetails(storefrontData.getStorefrontUserId());

            if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
                minAmountPrice = storefrontData.getMinimumSelfPickupAmount();
            } else {
                minAmountPrice = storefrontData.getMerchantMinimumOrder();
            }
           /* Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().
                    setSymbol(storefrontData.getPaymentSettings().getSymbol());*/

            instantTask = storefrontData.getInstantTask();
            scheduledTask = storefrontData.getScheduledTask();
            isStartEndTimeEnabled = storefrontData.getIsStartEndTimeEnable();
        } else {
            storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
            getSingleMerchantDetails(storefrontData.getStorefrontUserId());

            if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
                minAmountPrice = StorefrontCommonData.getFormSettings().getMinimum_self_pickup_amount();
            } else {
                minAmountPrice = StorefrontCommonData.getFormSettings().getMerchantMinimumOrder();
            }

            instantTask = StorefrontCommonData.getFormSettings().getInstantTask();
            scheduledTask = StorefrontCommonData.getFormSettings().getScheduledTask();
        }

        address = getIntent().getStringExtra(PICKUP_ADDRESS) != null ? getIntent().getStringExtra(PICKUP_ADDRESS) : "";
        pickUpLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        deliverLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickUpLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        deliverLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);

        isEditOrder = getIntent().getBooleanExtra(IS_EDIT_ORDER, false);

        if (isEditOrder)
            editJobId = getIntent().getIntExtra(EDIT_JOB_ID, 0);

        initializeFields();
        showPaymentSlider();

        setAddressData();
        resetSelfPickupDeliveryAssets();
        if (isStaticAddressFlow()) {
            setAddressBarText();
            callbackForStaticAddresses(getUserIdForStaticAddressFlow());
        }

        showPreFilledData();
        setLaundryDetails(); /*In laundry details, we have both pickup and deliver details ...
        In future this can be replaced if there is a config of both pickup and delivery details are required.*/

        setDateTime();
        setDateFields();

        //viewHide agent selection layout
        setAgentFields();

        callbackForCheckoutTemplateStatus();

        if (UIManager.getSideOrderActive() &&
                StorefrontCommonData.getAppConfigurationData().getOnboardingBusinessType() != Constants.OnboardingBusinessType.CATERING)
            getSubCategory();
        misOftenBought = fromOftenBought;
        if (!misOftenBought && MyApplication.getInstance().isShowOftenBoughtDialog()) {
            try {
                getOftenBought();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void getOftenBought() {
        if (Dependencies.getCartOftenBoughtIds().size() == 0) {
            return;
        }
        if (oftenDialog != null && oftenDialog.isShowing()) {
            oftenDialog.dismiss();
            storefrontData.setProductLayoutType(layout_type);
        }
        Location location = LocationUtils.getLastLocation(mActivity);
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("point", location.getLatitude() + " " + location.getLongitude());
        commonParams.remove(USER_ID);
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());
//        commonParams.add("is_veg", 1);

        /*if (getFormSettings().getProductView() == 1) {
            commonParams.build().getMap().remove("point");
            commonParams.add(LATITUDE, location.getLatitude());
            commonParams.add(LONGITUDE, location.getLongitude());
            if (selectedStartDate != null && selectedEndDate != null) {
                commonParams.add(FILTER_START_DATE, selectedStartDate);
                commonParams.add(FILTER_END_DATE, selectedEndDate);
            }
        }*/

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        ArrayList<Integer> first3ids = new ArrayList<>();
        if (Dependencies.getCartOftenBoughtIds().size() < 3) {
            commonParams.add("product_ids_array", Dependencies.getCartOftenBoughtIds());
        } else {
            for (int i = 0; i < 3; i++) {
                first3ids.add(Dependencies.getCartOftenBoughtIds().get(i));
            }
            commonParams.add("product_ids_array", first3ids);
        }

        if (getFormSettings().getProductView() == 0) {
            commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());
            RestClient.getApiInterface(mActivity).getProductCatalogue(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                @Override
                public void success(BaseModel baseModel) {
                    ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();
                    try {
                        com.tookancustomer.models.ProductCatalogueData.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.ProductCatalogueData.Datum[].class);
                        productCatalogueDataResponse.setData(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>(Arrays.asList(datum)));
                    } catch (Exception e) {

                        Utils.printStackTrace(e);
                    }
                    setProductCatalogueData(productCatalogueDataResponse, 0);
                }

                @Override
                public void failure(APIError error, BaseModel baseModel) {
                }
            });
        }
    }

    private void setProductCatalogueData(ProductCatalogueData productCatalogueDataResponse, int offset) {
        for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {
            productCatalogueDataResponse.getData().get(i).setStorefrontData(storefrontData);
            productCatalogueDataResponse.getData().get(i).setFormId(getFormSettings().getFormId());
            productCatalogueDataResponse.getData().get(i).setVendorId(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
            for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                if (productCatalogueDataResponse.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                    productCatalogueDataResponse.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                    productCatalogueDataResponse.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                    productCatalogueDataResponse.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                    productCatalogueDataResponse.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());
                    productCatalogueDataResponse.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());
                    productCatalogueDataResponse.getData().get(i).setPaymentSettings(Dependencies.getSelectedProductsArrayList().get(j).getPaymentSettings());
                    Dependencies.getSelectedProductsArrayList().set(j, productCatalogueDataResponse.getData().get(i));
                }
            }
        }
        ArrayList<Datum> productCatalogueArrayList = new ArrayList<>(productCatalogueDataResponse.getData());
        if (productCatalogueArrayList.size() > 0) {
            showOftenBought(productCatalogueArrayList);
        }
    }

    private void showOftenBought(ArrayList<Datum> productCatalogueArrayList) {
        layout_type = productCatalogueArrayList.get(0).getStorefrontData().getProductLayoutType();
        productCatalogueArrayList.get(0).getStorefrontData().setProductLayoutType(3);
        MerchantProductsAdapter merchantProductsAdapter = new MerchantProductsAdapter(mActivity, getSupportFragmentManager(), productCatalogueArrayList, true, new MerchantProductsAdapter.Callback() {
            @Override
            public void onQuantityUpdated() {
                if (mActivity instanceof MerchantCatalogActivity) {
                    ((MerchantCatalogActivity) mActivity).setTotalQuantity();

                    if (storefrontData.getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT
                            && storefrontData.getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        ((MerchantCatalogActivity) mActivity).openCheckoutActivity();
                    }
                }
            }
        });
        oftenDialog = new Dialog(mActivity);
        oftenDialog.setContentView(R.layout.dialog_often_bought);

        Window dialogWindow = oftenDialog.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        oftenDialog.setCancelable(false);
        oftenDialog.setCanceledOnTouchOutside(false);

        TextView tvViewMore = oftenDialog.findViewById(R.id.tvViewMore);
        tvViewMore.setText(getStrings(mActivity,R.string.view_more));
        RecyclerView rvOften = oftenDialog.findViewById(R.id.rvOftenBought);

        rvOften.setAdapter(merchantProductsAdapter);

        oftenDialog.findViewById(R.id.tvViewMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oftenDialog.dismiss();
                storefrontData.setProductLayoutType(layout_type);
                Location location = LocationUtils.getLastLocation(CheckOutActivity.this);
                String latitude = storefrontData.getLatitude();
                String longitude = storefrontData.getLongitude();
                Bundle bundle = new Bundle();
                bundle.putSerializable(STOREFRONT_DATA, storefrontData);
                bundle.putSerializable(DATE_TIME, "");
                bundle.putSerializable(PICKUP_LATITUDE, latitude == null ? (location != null ? location.getLatitude() : 0.0) : latitude);
                bundle.putSerializable(PICKUP_LONGITUDE, longitude == null ? (location != null ? location.getLongitude() : 0.0) : longitude);
                bundle.putSerializable(PICKUP_ADDRESS, address);
                bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");
                bundle.putBoolean(IS_OFTEN_BOUGHT, true);
                bundle.putInt("PRODUCT_SIZE", Dependencies.getSelectedProductsArrayList().size());
                Intent intentCatalog = new Intent(CheckOutActivity.this, MerchantCatalogActivity.class);
                intentCatalog.putExtras(bundle);
                startActivityForResult(intentCatalog, Codes.Request.OFTEN_VIEW_MORE_ACTIVITY);
            }
        });

        oftenDialog.findViewById(R.id.tvContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDontShowOftenBought = true;
                oftenDialog.dismiss();
                storefrontData.setProductLayoutType(layout_type);
                setdata(true);
                rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
            }
        });

        oftenDialog.show();


    }


    private void getSubCategory() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        String dateTime;
        if (Dependencies.getSelectedProductsArrayList().get(0).getIsPreorderSelecetedForMenu() == 0) {
            dateTime = DateUtils.getInstance().getCurrentDateTimeUtc();
        } else {
            dateTime = Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime();
        }

        commonParams.add(DATE_TIME, dateTime);
        commonParams.add("offset", 0);
        commonParams.add("limit", 0);


        RestClient.getApiInterface(this).getSubcategory(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true) {
                    @Override
                    public void success(BaseModel baseModel) {

                        ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();
                        UserData userData = StorefrontCommonData.getUserData();

                        try {

                            Datum[] datum = baseModel.toResponseModel(Datum[].class);
                            productCatalogueDataResponse.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                        }


                        for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {

                            Datum sideOrderData = productCatalogueDataResponse.getData().get(i);
                            sideOrderData.setStorefrontData(storefrontData);
                            sideOrderData.setFormId(StorefrontCommonData.getFormSettings().getFormId());
                            sideOrderData.setVendorId(userData.getData().getVendorDetails().getVendorId());

                            for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {

                                Datum selectedData = Dependencies.getSelectedProductsArrayList().get(j);
                                if (sideOrderData.getProductId().equals(selectedData.getProductId())) {
                                    sideOrderData.setSelectedQuantity(selectedData.getSelectedQuantity());
                                    sideOrderData.setProductStartDate(selectedData.getProductStartDate());
                                    sideOrderData.setSurgeAmount(selectedData.getSurgeAmount());
                                    sideOrderData.setPaymentSettings(selectedData.getPaymentSettings());
                                    sideOrderData.setProductEndDate(selectedData.getProductEndDate());
                                }
                            }
                        }

                        sideOrderArrayList.clear();
                        sideOrderArrayList.addAll(productCatalogueDataResponse.getData());

                        setSideOrderLabelVisibility();

                        rvSideOrder.getAdapter().notifyDataSetChanged();

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        findViewById(R.id.llSideOrder).setVisibility(View.GONE);
                    }
                });
    }


    private void callbackForCheckoutTemplateStatus() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId())
                .add("user_id", Dependencies.getSelectedProductsArrayList().get(0).getUserId());

        RestClient.getApiInterface(this).getCheckoutTemplateStatus(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        CheckoutTemplateStatus templateStatus = baseModel.toResponseModel(CheckoutTemplateStatus.class);
                        isCheckoutTemplateEnabled = templateStatus.getIsCheckoutTemplateEnabled() == 1;
                        setProceedButtonText();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });
    }

    private void setProceedButtonText() {

        if (!allMandatoryProductAdded()) {
            ((TextView) findViewById(R.id.tvProceedToPay)).setText(getStrings(R.string.add_mandatory_products));

        } else if (isCheckoutTemplateEnabled && templateDataList == null) {
            ((TextView) findViewById(R.id.tvProceedToPay)).setText(getStrings(R.string.proceed));
        } else {
            if (paymentMethodEnabled) {
                ((TextView) findViewById(R.id.tvProceedToPay)).setText(StorefrontCommonData.getTerminology().getProceedToPay(true));
            } else {
                ((TextView) findViewById(R.id.tvProceedToPay)).setText(getStrings(R.string.place_order).replace(ORDER, Utils.getCallTaskAs(true, true)));
            }
        }
    }

    //TODO
    private boolean allMandatoryProductAdded() {

        if (storefrontData != null && storefrontData.getReqCatalogues() != null && storefrontData.getReqCatalogues().size() > 0) {
            for (int i = 0; i < storefrontData.getReqCatalogues().size(); i++) {
                boolean isAdded = false;
                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {

                    if (Dependencies.getSelectedProductsArrayList().get(j).getParentCategoryId()
                            == storefrontData.getReqCatalogues().get(i).catalogueId) {
                        isAdded = true;
                    }
                }

                if (!isAdded) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }


    }

    @Override
    protected void onResume() {
        if (!isDontShowOftenBought) {
            if (MyApplication.getInstance().isShowOftenBoughtDialog()) {
                try {
                    getOftenBought();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                Dependencies.clearSelectedProductArraylist();
            }
        }
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initializeFields() {
        // Main Screen fields
        ((TextView) findViewById(R.id.tvHeading)).setText(StorefrontCommonData.getTerminology().getCheckout());

        setProceedButtonText();

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.tvProceedToPay));
        rlPickup = findViewById(R.id.rlPickup);
        rlDeliveryView = findViewById(R.id.rlDeliveryView);
        // Address fields
        tvAddressHeader = findViewById(R.id.tvAddressHeader);
        tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));

        llSelfPickupDelivery = findViewById(R.id.llSelfPickupDelivery);
        tvHomeDelivery = findViewById(R.id.tvHomeDelivery);
        tvHomeDelivery.setText(StorefrontCommonData.getTerminology().getHomeDelivery());
        tvSelfPickup = findViewById(R.id.tvSelfPickup);
        tvSelfPickup.setText(StorefrontCommonData.getTerminology().getSelfPickup());
        tvPickAndDrop = findViewById(R.id.tvPickAndDrop);
        tvPickAndDrop.setText(StorefrontCommonData.getTerminology().getPickupAndDrop());

        ivHomedelivery = findViewById(R.id.ivHomedelivery);
        ivSelfPickup = findViewById(R.id.ivSelfPickup);
        ivPickAndDrop = findViewById(R.id.ivPickAndDrop);


        etAddress = findViewById(R.id.etAddress);
        etAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));

        llSelectAgent = findViewById(R.id.llSelectAgent);
        tvAgentHeader = findViewById(R.id.tvAgentHeader);
        etSelectAgent = findViewById(R.id.etSelectAgent);
        llHomeDelivery = findViewById(R.id.llHomeDelivery);
        llSelfPickup = findViewById(R.id.llSelfPickup);

        etAddress.setHint(getStrings(R.string.add_address));
        etAddress.setText(address);

        Utils.setOnClickListener(this, llHomeDelivery, llSelfPickup, etAddress, llPickAndDrop);

        // UserDetails fields
        llUserDetailsParentView = findViewById(R.id.llUserDetailsParentView);
        ((TextView) findViewById(R.id.tvUserDetailsHeader)).setText(getStrings(R.string.personal_details));
        ((TextView) findViewById(R.id.tvUserNameLabel)).setText(getStrings(R.string.name));
        etUserName = findViewById(R.id.etUserName);
        etUserName.setHint(getStrings(R.string.enter_name));
        etUserName.setCursorVisible(false);
        etUserName.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                etUserName.requestFocus();
                etUserName.requestFocusFromTouch();
                etUserName.setCursorVisible(true);
                return false;
            }
        });

        ((TextView) findViewById(R.id.tvUserEmailabel)).setText(getStrings(R.string.email));

        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            if (StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 2
                    || StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 1) {
                ((TextView) findViewById(R.id.tvUserEmailabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), "*"));
            } else {
                ((TextView) findViewById(R.id.tvUserEmailabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), ""));
            }

        } else {
            if ((UIManager.getSignupField() == 2 || UIManager.getSignupField() == 0)) {
                ((TextView) findViewById(R.id.tvUserEmailabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), "*"));
            }
        }


        etUserEmail = findViewById(R.id.etUserEmail);
        etUserEmail.setHint(getStrings(R.string.enter_email));

        ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(getStrings(R.string.phone));

        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            if (StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2
                    || StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 1) {
                ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.phone), "*"));
            } else {
                ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.phone), ""));
            }
        } else {
            if ((UIManager.getSignupField() == 2 || UIManager.getSignupField() == 0)) {
                ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.phone), "*"));
            }
        }
        /*if (UIManager.getSignupField() == 2 || UIManager.getSignupField() == 1) {

            ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.phone), "*"));
        }*/
        tvUserCountryCode = findViewById(R.id.tvUserCountryCode);
        tvUserCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        etUserPhoneNumber = findViewById(R.id.etUserPhoneNumber);
        etUserPhoneNumber.setHint(getStrings(R.string.enter_phone_number));

        Utils.setOnClickListener(this, findViewById(R.id.llUserCountryCode));

        // Checkbox for pickup same as delivery
        cbDeliverAddressSameAsPickup = findViewById(R.id.cbDeliverAddressSameAsPickup);
//        cbDeliverAddressSameAsPickup.setText(getStrings(R.string.deliver_details_same_as_pickup_details));
        cbDeliverAddressSameAsPickup.setText(getStrings(R.string.deliveryDetails_same_as_pickupDetails)
                .replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery())
                .replace(PICKUP, StorefrontCommonData.getTerminology().getPickup()));


        // PickUp Details fields
        ((TextView) findViewById(R.id.tvPickupAddressHeader)).setText(getStrings(R.string.pickupDetails).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
        etPickupAddress = findViewById(R.id.etPickupAddress);
        etPickupAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        etPickupAddress.setText("");

        etPickupAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etPickupAddress.getText().toString().isEmpty())
                    etPickupAddress.setError(null);
            }
        });


        ((TextView) findViewById(R.id.tvPickupUserNameLabel)).setText(getStrings(R.string.name));
        etPickupUserName = findViewById(R.id.etPickupUserName);
        etPickupUserName.setHint(getStrings(R.string.enter_name));

        ((TextView) findViewById(R.id.tvPickupUserEmailLabel)).setText(getStrings(R.string.email));
        etPickupUserEmail = findViewById(R.id.etPickupUserEmail);
        etPickupUserEmail.setHint(getStrings(R.string.enter_email));

        ((TextView) findViewById(R.id.tvPickupUserPhoneLabel)).setText(getStrings(R.string.phone));
        tvPickupUserCountryCode = findViewById(R.id.tvPickupUserCountryCode);
        tvPickupUserCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        etPickupUserPhoneNumber = findViewById(R.id.etPickupUserPhoneNumber);
        etPickupUserPhoneNumber.setHint(getStrings(R.string.enter_phone_number));

        Utils.setOnClickListener(this, findViewById(R.id.etPickupAddress), findViewById(R.id.llPickupUserCountryCode));


        // Delivery Details fields
        ((TextView) findViewById(R.id.tvDeliverAddressHeader)).setText(getStrings(R.string.deliveryDetails).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
        etDeliverAddress = findViewById(R.id.etDeliverAddress);
        etDeliverAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        etDeliverAddress.setText("");


        ((TextView) findViewById(R.id.tvDeliveryUserNameLabel)).setText(getStrings(R.string.name));
        etDeliveryUserName = findViewById(R.id.etDeliveryUserName);
        etDeliveryUserName.setHint(getStrings(R.string.enter_name));

        ((TextView) findViewById(R.id.tvDeliveryUserEmailLabel)).setText(getStrings(R.string.email));
        etDeliveryUserEmail = findViewById(R.id.etDeliveryUserEmail);
        etDeliveryUserEmail.setHint(getStrings(R.string.enter_email));

        ((TextView) findViewById(R.id.tvDeliveryUserPhoneLabel)).setText(getStrings(R.string.phone));
        tvDeliveryUserCountryCode = findViewById(R.id.tvDeliveryUserCountryCode);
        tvDeliveryUserCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        etDeliveryUserPhoneNumber = findViewById(R.id.etDeliveryUserPhoneNumber);
        etDeliveryUserPhoneNumber.setHint(getStrings(R.string.enter_phone_number));

        Utils.setOnClickListener(this, findViewById(R.id.etDeliverAddress), findViewById(R.id.llDeliveryUserCountryCode));

        // Cart fields
        tvCartHeader = findViewById(R.id.tvCartHeader);
        tvCartHeader.setText(StorefrontCommonData.getTerminology().getCart(true)
//                + " " + (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getProduct() : StorefrontCommonData.getTerminology().getProduct())
                + "(" + Dependencies.getCartSize() + ")");
        ibArrow = findViewById(R.id.ibArrow);
        tvCartAction = findViewById(R.id.tvCartAction);
        tvCartAction.setVisibility(View.VISIBLE);
        tvCartAction.setText(getStrings(R.string.clear));

        llCartChild = findViewById(R.id.llCartChild);
        rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(mActivity));
        rvCart.setNestedScrollingEnabled(false);
        rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
        rvCart.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                svCheckout.scrollTo(0, 0);
            }
        });
        rlSubtotalPrice = findViewById(R.id.rlSubtotalPrice);
        /**
         * additonal and total price layout
         */
        rlAdditionalPrice = findViewById(R.id.rlAdditionalPrice);
        rlTotallPrice = findViewById(R.id.rlTotallPrice);
        tvTotalHeading = findViewById(R.id.tvTotalHeading);
        tvAdditionalHeading = findViewById(R.id.tvAdditionalHeading);

        tvTotalHeading.setText(getStrings(R.string.total_product_text));
        tvAdditionalAmount = findViewById(R.id.tvAdditionalAmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        ((TextView) findViewById(R.id.tvSubtotalHeading)).setText(getStrings(R.string.subtotal));
        tvSubTotal = findViewById(R.id.tvSubTotal);

        Utils.setOnClickListener(this, findViewById(R.id.rlCartHeader), tvCartAction, tvAdditionalHeading);

        // DateAndTime fields
        llSelectDateTime = findViewById(R.id.llSelectDateTime);
        tvDateTimeHeader = findViewById(R.id.tvDateTimeHeader);
        tvDateTimeHeader.setText(getStrings(R.string.checkout_select_date_time));
        tvAgentHeader.setText(getStrings(R.string.select_agent).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        rgInstantSchedule = findViewById(R.id.rgInstantSchedule);
        rbInstant = findViewById(R.id.rbInstant);
        rbInstant.setText(getStrings(R.string.text_order_now).replace(ORDER, Utils.getCallTaskAs(true, true)));
        rbSchedule = findViewById(R.id.rbSchedule);
        rbSchedule.setText(getStrings(R.string.schedule_later));
        rbSubscribe = findViewById(R.id.rbSubscribe);
        rbSubscribe.setText(StorefrontCommonData.getTerminology().getSubscribe());
        llStartDate = findViewById(R.id.llStartDate);
        etStartDateTime = findViewById(R.id.etStartDateTime);
        etStartDateTime.setHint(getStrings(R.string.select_date_time));
        ((MaterialEditText) etStartDateTime).setFloatingLabelText(getStrings(R.string.date_and_time));
        llEndDateSchedule = findViewById(R.id.llEndDateSchedule);
        etEndDateTimeSchedule = findViewById(R.id.etEndDateTimeSchedule);
        etEndDateTimeSchedule.setHint(getStrings(R.string.enter_terminology).replace(TerminologyStrings.TERMINOLOGY, StorefrontCommonData.getTerminology().getEndTime(false)));
        ((MaterialEditText) etEndDateTimeSchedule).setFloatingLabelText(StorefrontCommonData.getTerminology().getEndTime(true));


        etSelectAgent.setHint(getStrings(R.string.select_agent).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        ((MaterialEditText) etSelectAgent).setFloatingLabelText(getStrings(R.string.selected_agent).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));

        tvCancellationPolicy = findViewById(R.id.tvCancellationPolicy);


        tvSideOrderLabel = findViewById(R.id.tvSideOrderLabel);
        tvFreeDeliveryHeader = findViewById(R.id.tvFreeDeliveryHeader);
        tvSideOrderLabel.setText(getStrings(R.string.you_may_also_like));


        Utils.setOnClickListener(this, etStartDateTime, etEndDateTimeSchedule, tvCancellationPolicy, etSelectAgent);
        // Notes fields
        ((TextView) findViewById(R.id.tvNotesHeader)).setText(getStrings(R.string.additional_info));

        etDescription = findViewById(R.id.etDescription);
        etDescription.setHint(StorefrontCommonData.getTerminology().getOrderRemarks());
        etDescription.setScroller(new Scroller(this));
        etDescription.setMaxLines(5);
        etDescription.setVerticalScrollBarEnabled(true);
        etDescription.setMovementMethod(new ScrollingMovementMethod());
        etDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.etDescription) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        findViewById(R.id.llSideOrder).setVisibility(UIManager.getSideOrderActive()
                && StorefrontCommonData.getAppConfigurationData().getOnboardingBusinessType() != Constants.OnboardingBusinessType.CATERING ? View.VISIBLE : View.GONE);
        rvSideOrder = findViewById(R.id.rvSideOrder);
        sideOrderCheckoutAdapter = new SideOrderCheckoutAdapter(mActivity, getSupportFragmentManager(), sideOrderArrayList, new SideOrderCheckoutAdapter.Callback() {
            @Override
            public void updateItemTotalPrice(final Datum datum) {
                Dependencies.addCartItem(mActivity, datum);

                rvCart.getAdapter().notifyDataSetChanged();

                tvCartHeader.setText(StorefrontCommonData.getTerminology().getCart(true)
//                        + " " + (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getProduct() : StorefrontCommonData.getTerminology().getProduct())
                        + "(" + Dependencies.getCartSize() + ")");

                setSideOrderLabelVisibility();
            }
        });

        rvSideOrder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSideOrder.setAdapter(sideOrderCheckoutAdapter);

    }

    private void setSideOrderLabelVisibility() {

        for (int i = 0; i < sideOrderArrayList.size(); i++) {
            if (sideOrderArrayList.get(i).getSelectedQuantity() == 0) {
                tvSideOrderLabel.setVisibility(View.VISIBLE);

                return;

            }
        }

        tvSideOrderLabel.setVisibility(View.GONE);


    }

    private String getSubTotal() {
        return Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal());
    }

    public void setSubTotal(double subTotal) {
        productTotalCalculatedPrice = subTotal;
        finalAmount = subTotal;
        //*ngIf="sessionService.get('info') && sessionService.get('info').is_free_delivery_enabled && !(sessionService.get('info').free_delivery_toggle == 0)"
        //((sessionService.get('info').free_delivery_toggle) ? sessionService.get('info').free_delivery_toggle : sessionService.get('info').free_delivery_amount)


        tvSubTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol(storefrontData.getPaymentSettings())
                + "" + Utils.getDoubleTwoDigits(productTotalCalculatedPrice)));

        if (llCartChild.getVisibility() == View.GONE) {
            tvCartAction.setText((UIManager.getCurrency(Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(productTotalCalculatedPrice))));
        }
        tvCartHeader.setText(StorefrontCommonData.getTerminology().getCart(true)
//                + " " + (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getProduct() : StorefrontCommonData.getTerminology().getProduct())
                + "(" + Dependencies.getCartSize() + ")");

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && productTotalCalculatedPrice <= 0) {
            rlSubtotalPrice.setVisibility(View.GONE);
            if (llCartChild.getVisibility() == View.GONE) {
                tvCartAction.setVisibility(View.GONE);
            }
        } else {
            rlSubtotalPrice.setVisibility(View.VISIBLE);
        }

        if (templateDataList != null) {
            displayAdditionalTotalPrice(templateDataList);
        }

        setProceedButtonText();

    }

    private void showPaymentSlider() {
        valueEnabledPayment = PaymentMethodsClass.getEnabledPaymentMethod();
        paymentMethodEnabled = PaymentMethodsClass.isPaymentEnabled();

        setProceedButtonText();
    }

    private void setAddressData() {
        // Address fields set data


        if ((storefrontData.getSelfPickup() == 1
                && storefrontData.getHomeDelivery() == 1
                && storefrontData.getIsPdFlow())
                || (storefrontData.getSelfPickup() == 1 && storefrontData.getHomeDelivery() == 1)
                || (storefrontData.getSelfPickup() == 1 && storefrontData.getIsPdFlow())
                || (storefrontData.getHomeDelivery() == 1 && storefrontData.getIsPdFlow())
        ) {
            if (selectedPickupMode == Constants.SelectedPickupMode.NONE)
                selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            llSelfPickupDelivery.setVisibility(View.VISIBLE);

            if (storefrontData.getSelfPickup() == 1
                    && storefrontData.getHomeDelivery() == 1
                    && storefrontData.getIsPdFlow()) {
                selectedPickupMode = Dependencies.getSelectedDeliveryMode();

                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            } else if ((storefrontData.getSelfPickup() == 1 && storefrontData.getHomeDelivery() == 1)) {

                if (Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.SELF_PICKUP ||
                        Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.HOME_DELIVERY)
                    selectedPickupMode = Dependencies.getSelectedDeliveryMode();
                else
                    selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;


                llPickAndDrop.setVisibility(View.GONE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            } else if (storefrontData.getSelfPickup() == 1 && storefrontData.getIsPdFlow()) {


                if (Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.SELF_PICKUP ||
                        Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.PICK_AND_DROP)
                    selectedPickupMode = Dependencies.getSelectedDeliveryMode();
                else
                    selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;


                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.GONE);
            } else if (storefrontData.getHomeDelivery() == 1 && storefrontData.getIsPdFlow()) {

                if (Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.HOME_DELIVERY ||
                        Dependencies.getSelectedDeliveryMode() == Constants.SelectedPickupMode.PICK_AND_DROP)
                    selectedPickupMode = Dependencies.getSelectedDeliveryMode();
                else
                    selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;


                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.GONE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            }


            resetSelfPickupDeliveryAssets();
        } else {
            if (storefrontData.getSelfPickup() == 1) {
                selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
                llSelfPickupDelivery.setVisibility(View.GONE);
                llUserDetailsParentView.setVisibility(View.VISIBLE);
            } else if (storefrontData.getHomeDelivery() == 1) {
                selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
                llSelfPickupDelivery.setVisibility(View.GONE);
                llUserDetailsParentView.setVisibility(View.VISIBLE);
            } else if (storefrontData.getIsPdFlow()) {
                selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
            }
            llSelfPickupDelivery.setVisibility(View.GONE);
        }


//        if (storefrontData != null) {
//            if ((storefrontData.getSelfPickup() == 1 && storefrontData.getHomeDelivery() == 1 && storefrontData.getIsPdFlow())
//                    || (storefrontData.getSelfPickup() == 1 && storefrontData.getHomeDelivery() == 1)
//                    || (storefrontData.getSelfPickup() == 1 && storefrontData.getIsPdFlow())
//                    || (storefrontData.getHomeDelivery() == 1 && storefrontData.getIsPdFlow())) {
//                llSelfPickupDelivery.setVisibility(View.VISIBLE);
//                llUserDetailsParentView.setVisibility(View.VISIBLE);
//
//
//            } else if (storefrontData.getSelfPickup() == 1) {
//
//            } else {
//
//            }
//
//            if (storefrontData.getIsPdFlow()) {
//                llPickAndDrop.setVisibility(View.VISIBLE);
//            } else {
//                llPickAndDrop.setVisibility(View.GONE);
//            }
//
//        }

    }

    public void resetSelfPickupDeliveryAssets() {
        if (selectedPickupMode == Constants.SelectedPickupMode.NONE) {

            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);

            ivHomedelivery.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            ivPickAndDrop.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            ivSelfPickup.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_inactive));
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);
            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llPickupDetailsParentView).setVisibility(View.GONE);
            etAddress.setVisibility(View.VISIBLE);


            etAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_black, 0);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
            rlPickup.setVisibility(View.INVISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
        } else if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);

            setDeliveryIcon(Constants.SelectedPickupMode.SELF_PICKUP);

            rlPickup.setVisibility(View.VISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
            tvAddressHeader.setText(Utils.capitaliseWords(getStrings(R.string.pickup_from).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true))));
            Utils.setTextColor(mActivity, R.color.primary_text_color, etAddress);
            etAddress.setText(storefrontData.getDisplayAddress());
            address = etAddress.getText().toString();
            etAddress.setEnabled(false);
            etDeliverAddress.setText(address);

            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llPickupDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llUserDetailsParentView).setVisibility(View.VISIBLE);

            etAddress.setVisibility(View.VISIBLE);


            etAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvHomeDelivery);

            setDeliveryIcon(Constants.SelectedPickupMode.HOME_DELIVERY);

            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);
            rlPickup.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
            rlDeliveryView.setVisibility(View.VISIBLE);
            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llPickupDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llUserDetailsParentView).setVisibility(View.VISIBLE);

            etAddress.setVisibility(View.VISIBLE);


            if (Dependencies.isLaundryApp()) {
                tvAddressHeader.setText(Utils.capitaliseWords(getStrings(R.string.pickup_from).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true))));
            } else {
                tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));
            }
            Utils.setTextColor(mActivity, R.color.primary_text_color, etAddress);
            // etAddress.setText(address);
            etDeliverAddress.setText(address);
            etAddress.setEnabled(true);
            etAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_black, 0);
        } else if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvPickAndDrop);

            setDeliveryIcon(Constants.SelectedPickupMode.PICK_AND_DROP);

            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            rlPickup.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.VISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.VISIBLE);
            findViewById(R.id.llPickupDetailsParentView).setVisibility(View.VISIBLE);
            findViewById(R.id.llUserDetailsParentView).setVisibility(View.GONE);
            etAddress.setVisibility(View.GONE);


          /*  if (Dependencies.isLaundryApp()) {
                tvAddressHeader.setText(Utils.capitaliseWords(getStrings(R.string.pickup_from).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true))));
            } else {
                tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));
            }*/
            Utils.setTextColor(mActivity, R.color.primary_text_color, etAddress);
            etAddress.setText(address);
            etAddress.setEnabled(true);
            etAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_black, 0);
            setDeliveryAddressData();

        } else {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvPickAndDrop);

            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);

            ivHomedelivery.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            ivPickAndDrop.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            ivSelfPickup.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_inactive));

            rlDeliveryView.setVisibility(View.INVISIBLE);
            rlPickup.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.VISIBLE);
            if (Dependencies.isLaundryApp()) {
                tvAddressHeader.setText(Utils.capitaliseWords(getStrings(R.string.pickup_from).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true))));
            } else {
                tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));
            }
            Utils.setTextColor(mActivity, R.color.primary_text_color, etAddress);
            etAddress.setText(address);
            etDeliverAddress.setText(address);
            etAddress.setEnabled(true);
            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);
            findViewById(R.id.llPickupDetailsParentView).setVisibility(View.GONE);
            etAddress.setVisibility(View.VISIBLE);


            etAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_black, 0);

        }


        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
                minAmountPrice = storefrontData.getMinimumSelfPickupAmount();
            } else {
                minAmountPrice = storefrontData.getMerchantMinimumOrder();
            }
        } else {

            if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
                minAmountPrice = StorefrontCommonData.getFormSettings().getMinimum_self_pickup_amount();
            } else {
                minAmountPrice = StorefrontCommonData.getFormSettings().getMerchantMinimumOrder();
            }
        }

    }

    private void setDeliveryIcon(int deliveryMode) {

        if (deliveryMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_active)
                        .setFallback(R.drawable.ic_pickup_active)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_active));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

        } else if (deliveryMode == Constants.SelectedPickupMode.PICK_AND_DROP) {

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_selected)
                        .setFallback(R.drawable.ic_pick_drop_selected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }


        } else if (deliveryMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_active)
                        .setFallback(R.drawable.ic_home_delivery_active)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_active));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
        }
    }


    private boolean isStaticAddressFlow() {
        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                    UIManager.isStaticAddressEnabledForAdmin()) {
                return true;
            } else if (!Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                    Dependencies.getSelectedProductsArrayList().get(0).isStaticAddressEnabled()) {
                return true;
            }
        }

        return false;
    }

    private int getUserIdForStaticAddressFlow() {

        return Dependencies.getSelectedProductsArrayList().get(0).getUserId();
    }

    private void setAddressBarText() {
        if (!(selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP)) {
            etAddress.setHint(getStrings(R.string.text_select_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
            etAddress.setText("");

        }
    }

    private void callbackForStaticAddresses(int userId) {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder builder = new CommonParams.Builder()
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
//                .add(USER_ID, userData.getData().getVendorDetails().getUserId())
                .add(USER_ID, userId)
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable())
                .add("language", "en");

        if (isValidDeliveryAddress()) {
            builder.add("latitude", pickUpLatitude)
                    .add("longitude", pickUpLongitude);
        }


        RestClient.getApiInterface(this).getStaticAddresses(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        StaticAddressData[] addresses = baseModel.toResponseModel(StaticAddressData[].class);
                        addressesList = new ArrayList<StaticAddressData>(Arrays.asList(addresses));
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }

    private boolean isValidDeliveryAddress() {
        if (pickUpLatitude != 0.0 && pickUpLongitude != 0.0)
            return true;
        return false;
    }

    public void showPreFilledData() {
        if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() != 0) {
            etUserName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
            etUserEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
            setDefaultPhoneDetails(tvUserCountryCode, etUserPhoneNumber);
            if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 2) {
                llUserDetailsParentView.setVisibility(View.GONE);
            }
        }
    }

    private void setLaundryDetails() {
        if (Dependencies.isLaundryApp() || selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            (findViewById(R.id.tvUserDetailsHeader)).setVisibility(View.GONE);
            tvAddressHeader.setText(getStrings(R.string.pickupDetails).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
            if (Dependencies.isLaundryApp()) {
                findViewById(R.id.cbDeliverAddressSameAsPickup).setVisibility(View.VISIBLE);
                findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.VISIBLE);
                cbDeliverAddressSameAsPickup.setChecked(false);
                etPickupAddress.setText(etAddress.getText().toString());
                etPickupUserName.setText(etUserName.getText().toString().trim());
                etPickupUserEmail.setText(etUserEmail.getText().toString().trim());
                etPickupUserPhoneNumber.setText(etUserPhoneNumber.getText().toString().trim());
                tvPickupUserCountryCode.setText(tvDeliveryUserCountryCode.getText().toString().trim());
            }
            cbDeliverAddressSameAsPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);

                        etDeliverAddress.setText(etAddress.getText().toString());
                        deliverLatitude = pickUpLatitude;
                        deliverLongitude = pickUpLongitude;
                        etDeliveryUserName.setText(etUserName.getText().toString().trim());
                        etDeliveryUserPhoneNumber.setText(etUserPhoneNumber.getText().toString().trim());
                        tvDeliveryUserCountryCode.setText(tvDeliveryUserCountryCode.getText().toString().trim());
                    } else {
                        findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.VISIBLE);
                        etDeliverAddress.setText("");
                        deliverLatitude = 0.0;
                        deliverLongitude = 0.0;
                        etDeliveryUserName.setText("");
                        etDeliveryUserPhoneNumber.setText("");
                        tvDeliveryUserCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
                    }
                }
            });

        } else {
            (findViewById(R.id.tvUserDetailsHeader)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvUserDetailsHeader)).setText(getStrings(R.string.personal_details));
//            tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));
            findViewById(R.id.cbDeliverAddressSameAsPickup).setVisibility(View.GONE);
            findViewById(R.id.llDeliveryDetailsParentView).setVisibility(View.GONE);
        }
    }

    private void setDeliveryAddressData() {
        if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() != 0) {
            etDeliveryUserName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
            etDeliveryUserEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
            setDefaultPhoneDetails(tvDeliveryUserCountryCode, etDeliveryUserPhoneNumber);
            if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 2) {
                llUserDetailsParentView.setVisibility(View.GONE);
            }
        }
    }

    private void setDefaultPhoneDetails(TextView etCountryCode, EditText etPhone) {
//            String[] phoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().replace("-", " ").trim().split(" ");
        try {
//            String[] phoneNumber = Utils.splitNumberByCode(this, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());
            String[] phoneNumber = Utils.splitNumberByCodeNew(this, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());

            etCountryCode.setText(phoneNumber[0]);
            etPhone.setText(phoneNumber[1].replace("+", ""));
        } catch (Exception e) {
            String countryCode = Utils.getCountryCode(mActivity, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().trim());
            etCountryCode.setText(countryCode);
//            etCountryCode.setText(countryPicker.getUserCountryInfo(this).getDialCode());
            etPhone.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().replace(countryCode, "").replace("+", "").replace("-", " ").trim());
        }
    }

    private void setDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.START_TIME_BUFFER);

        isStartDate = true;
        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(calendar.getTime(), UIManager.getDateTimeFormat()));
    }

    private void setAgentFields() {

        if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                .getStorefrontData().isOrderAgentShedulingEnabled() &&
                Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {

            llSelectAgent.setVisibility(View.VISIBLE);
        } else {
            llSelectAgent.setVisibility(View.GONE);
        }

    }

    private void setDateFields() {
        if (Dependencies.isLaundryApp()) {
            isScheduled = 1;

            llSelectDateTime.setVisibility(View.VISIBLE);
            tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());

            rgInstantSchedule.setVisibility(View.GONE);

            llStartDate.setVisibility(View.VISIBLE);
            llEndDateSchedule.setVisibility(View.VISIBLE);

            etStartDateTime.setText("");
            etEndDateTimeSchedule.setText("");

            etStartDateTime.setHint(getStrings(R.string.enter_pickup_date_and_time).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(false)));
            ((MaterialEditText) etStartDateTime).setFloatingLabelText(getStrings(R.string.pickup_date_and_time).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));

            etEndDateTimeSchedule.setHint(getStrings(R.string.enter_delivery_date_and_time).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(false)));
            ((MaterialEditText) etEndDateTimeSchedule).setFloatingLabelText(getStrings(R.string.delivery_date_and_time).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));

        } else {

            if (!(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() ==
                    Constants.BusinessType.SERVICES_BUSINESS_TYPE
//                && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT
            )) {
                if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                        .getStorefrontData().getEnableTookanAgent() == 1) || (isStartEndTimeEnabled.equals(1))) {
                    if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList()
                            .get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                        isScheduled = 0;
                    } else {
                        isScheduled = scheduledTask;
                    }

                    llSelectDateTime.setVisibility(View.VISIBLE);
                    tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                    rgInstantSchedule.setVisibility(View.GONE);
                    llStartDate.setVisibility(View.VISIBLE);
                    llEndDateSchedule.setVisibility(View.VISIBLE);

                    etStartDateTime.setText("");
                    etEndDateTimeSchedule.setText("");

                    etStartDateTime.setHint(getStrings(R.string.enter_terminology).replace(TerminologyStrings.TERMINOLOGY, StorefrontCommonData.getTerminology().getStartTime(false)));
                    ((MaterialEditText) etStartDateTime).setFloatingLabelText(StorefrontCommonData.getTerminology().getStartTime(true));

                } else {
                    instantTask = Dependencies.getSelectedProductsArrayList().size() > 0 &&
                            Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getIsStorefrontOpened() == 0 ? 0 : instantTask;


                    if (UIManager.getRecurringTaskActive() && (storefrontData.getRecurrinTask() == 1) && isAllProductsRecurringEnable()) {
                        subscribeTask = 1;
                    } else {
                        subscribeTask = 0;
                    }
                    llEndDateSchedule.setVisibility(View.GONE);
                    llStartDate.setVisibility(View.VISIBLE);
                    etStartDateTime.setHint(getStrings(R.string.select_date_time));
                    ((MaterialEditText) etStartDateTime).setFloatingLabelText(getStrings(R.string.date_and_time));

                    if (instantTask.equals(1) && scheduledTask.equals(1) && subscribeTask.equals(1)) {
                        isScheduled = 0;
                        llSelectDateTime.setVisibility(View.VISIBLE);
                        tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                        rgInstantSchedule.setVisibility(View.VISIBLE);
                        llStartDate.setVisibility(View.GONE);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));

                        if (UIManager.isMenuEnabled() &&
                                Dependencies.getSelectedProductsArrayList().get(0).getIsMenuEnabled() == 1 &&
                                Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime() != null) {

                            if (Dependencies.getSelectedProductsArrayList().get(0).getIsPreorderSelecetedForMenu() == 0) {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(DateUtils.getInstance().getCurrentDateTimeUtc()));
                                isScheduled = 0;
                                llSelectDateTime.setVisibility(View.GONE);
                            } else {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime()));
                                isScheduled = 1;
                            }

                            Utils.makeViewNonEditable(etStartDateTime);
                            rgInstantSchedule.setVisibility(View.GONE);
                            llStartDate.setVisibility(View.VISIBLE);
                        }


                    } else if (instantTask.equals(1) && scheduledTask.equals(1)) {
                        isScheduled = 0;
                        llSelectDateTime.setVisibility(View.VISIBLE);
                        tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                        rgInstantSchedule.setVisibility(View.VISIBLE);
                        llStartDate.setVisibility(View.GONE);
                        rbSubscribe.setVisibility(View.GONE);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));

                        if (UIManager.isMenuEnabled() &&
                                Dependencies.getSelectedProductsArrayList().get(0).getIsMenuEnabled() == 1 &&
                                Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime() != null) {

                            if (Dependencies.getSelectedProductsArrayList().get(0).getIsPreorderSelecetedForMenu() == 0) {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(DateUtils.getInstance().getCurrentDateTimeUtc()));
                                isScheduled = 0;
                                llSelectDateTime.setVisibility(View.GONE);
                            } else {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime()));
                                isScheduled = 1;
                            }

                            Utils.makeViewNonEditable(etStartDateTime);
                            rgInstantSchedule.setVisibility(View.GONE);
                            llStartDate.setVisibility(View.VISIBLE);
                        }

                    } else if (instantTask.equals(1) && subscribeTask.equals(1)) {
                        isScheduled = scheduledTask;
                        llSelectDateTime.setVisibility(View.VISIBLE);

                        rgInstantSchedule.setVisibility(View.VISIBLE);
                        rbSchedule.setVisibility(View.GONE);
                        rbInstant.setChecked(true);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));

                    } else if (scheduledTask.equals(1) && subscribeTask.equals(1)) {
                        isScheduled = scheduledTask;
                        llSelectDateTime.setVisibility(View.VISIBLE);
                        tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                        rgInstantSchedule.setVisibility(View.VISIBLE);
                        llStartDate.setVisibility(View.VISIBLE);

                        etStartDateTime.setText("");

                        rbInstant.setVisibility(View.GONE);
                        rbSchedule.setVisibility(View.VISIBLE);
                        rbSchedule.setChecked(true);


                        if (UIManager.isMenuEnabled() &&
                                Dependencies.getSelectedProductsArrayList().get(0).getIsMenuEnabled() == 1 &&
                                Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime() != null) {

                            if (Dependencies.getSelectedProductsArrayList().get(0).getIsPreorderSelecetedForMenu() == 0) {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(DateUtils.getInstance().getCurrentDateTimeUtc()));
                            } else {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime()));
                            }
                            Utils.makeViewNonEditable(etStartDateTime);
                        }

                    } else if (scheduledTask.equals(1)) {
                        isScheduled = scheduledTask;
                        llSelectDateTime.setVisibility(View.VISIBLE);
                        tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                        rgInstantSchedule.setVisibility(View.GONE);
                        llStartDate.setVisibility(View.VISIBLE);

                        etStartDateTime.setText("");


                        if (UIManager.isMenuEnabled() &&
                                Dependencies.getSelectedProductsArrayList().get(0).getIsMenuEnabled() == 1 &&
                                Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime() != null) {

                            if (Dependencies.getSelectedProductsArrayList().get(0).getIsPreorderSelecetedForMenu() == 0) {
                                try {
                                    Date currentdate = new Date();
                                    Calendar c = Calendar.getInstance();
                                    c.add(Calendar.MINUTE, storefrontData.getPreBookingBuffer());
                                    currentdate = c.getTime();
                                    String date = DateUtils.getInstance().convertDateObjectToUtc(currentdate);
                                    etStartDateTime.setText(DateUtils.getInstance()
                                            .convertToLocal(date));
                                } catch (ParseException e) {
                                    Utils.printStackTrace(e);
                                }

                            } else {
                                etStartDateTime.setText(DateUtils.getInstance()
                                        .convertToLocal(Dependencies.getSelectedProductsArrayList().get(0).getPreorderDateTime()));
                            }
                            Utils.makeViewNonEditable(etStartDateTime);
                        }

                    } else if (instantTask.equals(1)) {
                        isScheduled = scheduledTask;
                        llSelectDateTime.setVisibility(View.GONE);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                    } else if (subscribeTask.equals(1)) {

                        rgInstantSchedule.setVisibility(View.VISIBLE);
                        llSelectDateTime.setVisibility(View.VISIBLE);
                        rbInstant.setVisibility(View.GONE);
                        rbSchedule.setVisibility(View.GONE);
                        llStartDate.setVisibility(View.GONE);
                        llEndDateSchedule.setVisibility(View.GONE);
                        rbSubscribe.setVisibility(View.VISIBLE);

                        rbSubscribeClickListener();
                    } else {
                        isScheduled = scheduledTask;
                        llSelectDateTime.setVisibility(View.GONE);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                    }
                }

                rgInstantSchedule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rbInstant) {
                            isScheduled = 0;
                            subscribeTask = 0;
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                            llStartDate.setVisibility(View.GONE);
                        } else if (checkedId == R.id.rbSchedule) {
                            isScheduled = 1;
                            subscribeTask = 0;

                            etStartDateTime.setText("");
                            llStartDate.setVisibility(View.VISIBLE);
                        } else if (checkedId == R.id.rbSubscribe) {
                            isScheduled = 0;
                            subscribeTask = 1;
                            llStartDate.setVisibility(View.GONE);
                            llEndDateSchedule.setVisibility(View.GONE);

                            rbSubscribeClickListener();
                        }
                    }
                });


            } else {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                if (storefrontData.getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                    isScheduled = 1;
                    rgInstantSchedule.setVisibility(View.GONE);
                    llSelectDateTime.setVisibility(View.VISIBLE);
                    llEndDateSchedule.setVisibility(View.GONE);
                    etStartDateTime.setText("");
                } else {
                    isScheduled = 0;
                    llSelectDateTime.setVisibility(View.GONE);
                }
            }
        }

    }

    private void rbSubscribeClickListener() {
        rbSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {

                    if (TextUtils.isEmpty(etDeliverAddress.getText().toString())) {
                        Utils.snackBar(CheckOutActivity.this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(etAddress.getText().toString())) {
                        Utils.snackBar(CheckOutActivity.this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return;
                    }
                }

                HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

                hashMap.put("amount", 0 + "");
                hashMap.put("currency_id", Utils.getCurrencyId());
                hashMap.put("latitude", hashMap.get(JOB_PICKUP_LATITUDE));
                hashMap.put("longitude", hashMap.get(JOB_PICKUP_LONGITUDE));

                if (Dependencies.getSelectedProductsArrayList().size() > 0)
                    if (hashMap.containsKey(USER_ID)) {
                        hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
                    }

                if (templateDataList != null) {
                    Gson gson = new GsonBuilder().create();
                    JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                    hashMap.put("checkout_template", String.valueOf(myCustomArray));
                }

                if (!hashMap.containsKey("customer_address")) {
                    if (hashMap.containsKey(JOB_PICKUP_ADDRESS))
                        hashMap.put("customer_address", hashMap.get(JOB_PICKUP_ADDRESS));
                }


                Intent subscribeIntent = new Intent(mActivity, SubscriptionActivity.class);
                subscribeIntent.putExtra("selectedDays", selectedDaysList);
                subscribeIntent.putExtra("startDate", startDate);
                subscribeIntent.putExtra("startTime", startTime);
                subscribeIntent.putExtra("endDate", endDate);
                subscribeIntent.putExtra("endAfter", endAfter);
                subscribeIntent.putExtra("hashMap", hashMap);
                subscribeIntent.putExtra("isSubscriptionCount", isSubscriptionCount);
                startActivityForResult(subscribeIntent, SUBSCRIPTION_ACTIVITY);
            }
        });
    }

    private boolean isAllProductsRecurringEnable() {

        boolean isAllProductsRecurringEnable = true;

        for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {

            if (Dependencies.getSelectedProductsArrayList().get(i).getRecurring_enabled() == 0) {
                isAllProductsRecurringEnable = false;
                break;
            }
        }
        return isAllProductsRecurringEnable;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();


        if (i == R.id.rlBack) {
            onBackPressed();

        } else if (i == R.id.llPickAndDrop) {
            selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
            setProceedButtonText();
            resetSelfPickupDeliveryAssets();

        } else if (i == R.id.llHomeDelivery) {
            if (templateDataList != null && templateDataList.size() > 0)
                templateDataList.clear();

            if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP && isEditOrder) {
                address = "";
                pickUpLatitude = 0.0;
                pickUpLongitude = 0.0;

            }

            selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            setProceedButtonText();
            resetSelfPickupDeliveryAssets();

        } else if (i == R.id.llSelfPickup) {
            if (templateDataList != null && templateDataList.size() > 0)
                templateDataList.clear();
            selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            setProceedButtonText();
            resetSelfPickupDeliveryAssets();

        } else if (i == R.id.etAddress || i == R.id.etDeliverAddress) {
            isPickupAddress = i == R.id.etAddress;
            if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
                isPickupAddressPdflow = true;
            }
            if (!Utils.internetCheck(this)) {
                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                return;
            }
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_ADDRESS);
            if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                gotoMapActivity();
            } else {
                if (isStaticAddressFlow()) {
                    goToStaticAddressActivity();
                } else {
                    gotoFavLocationActivity();
                }
            }

        } else if (i == R.id.etPickupAddress) {
            isPickupAddressPdflow = true;
            isPickupAddress = true;
            if (!Utils.internetCheck(this)) {
                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                return;
            }
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_ADDRESS);
            if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                gotoMapActivity();
            } else {
                if (isStaticAddressFlow() && !isPickupAddressPdflow) {
                    goToStaticAddressActivity();
                } else {
                    gotoFavLocationActivity();
                }
            }

        } else if (i == R.id.llDeliveryUserCountryCode) {
            isDeliveryCountryCode = false;
            Utils.hideSoftKeyboard(this, etUserPhoneNumber);
            CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                @Override
                public void onCountrySelected(Country country) {
                    if (isDeliveryCountryCode) {
                        tvUserCountryCode.setText(country.getCountryCode());
                        etUserPhoneNumber.requestFocus();
                    } else {
                        tvPickupUserCountryCode.setText(country.getCountryCode());
                        etPickupUserPhoneNumber.requestFocus();
                    }
                    CountrySelectionDailog.dismissDialog();
                }
            }).show();

        } else if (i == R.id.llUserCountryCode || i == R.id.llPickupUserCountryCode) {
            isPickupCountryCode = i == R.id.llUserCountryCode;
            Utils.hideSoftKeyboard(this, etUserPhoneNumber);
            CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                @Override
                public void onCountrySelected(Country country) {
                    if (isPickupCountryCode) {
                        tvUserCountryCode.setText(country.getCountryCode());
                        etUserPhoneNumber.requestFocus();
                    } else {
                        tvPickupUserCountryCode.setText(country.getCountryCode());
                        etPickupUserPhoneNumber.requestFocus();
                    }
                    CountrySelectionDailog.dismissDialog();
                }
            }).show();

        } else if (i == R.id.rlCartHeader) {
            if (llCartChild.getVisibility() == View.GONE) {
                llCartChild.setVisibility(View.VISIBLE);
                tvCartAction.setVisibility(View.VISIBLE);
                tvCartAction.setText(getStrings(R.string.clear));
                ibArrow.setRotation(180);
                tvCartAction.setEnabled(true);
            } else {
                llCartChild.setVisibility(View.GONE);
                ibArrow.setRotation(0);
                tvCartAction.setEnabled(false);
                if (!(StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(getSubTotal()) <= 0)) {
                    tvCartAction.setVisibility(View.VISIBLE);
                    tvCartAction.setText(Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(productTotalCalculatedPrice));
                } else {
                    tvCartAction.setVisibility(View.GONE);

                }
            }
        } else if (i == R.id.tvCartAction) {
            if (llCartChild.getVisibility() == View.VISIBLE) {
                new OptionsDialog.Builder(mActivity).message((getStrings(R.string.sure_to_clear_cart).replace(CART, StorefrontCommonData.getTerminology().getCart()))).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {

                        Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                        MyApplication.getInstance().setShowOftenBoughtDialog(true);
                        onBackPressed();
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {

                    }
                }).build().show();

            }
        } else if (i == R.id.etStartDateTime || i == R.id.etEndDateTimeSchedule) {
            isStartDate = i == R.id.etStartDateTime;

            if (Dependencies.isLaundryApp()) {
                if (!isStartDate && getStartTime().isEmpty()) {
                    Utils.snackBar(mActivity, getStrings(R.string.pickup_dateTime_is_required).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
                    return;
                }
                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                        .getStorefrontData().isOrderAgentShedulingEnabled() &&
                        Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                        && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {

                    if (selectedAgentID > 0) {
                        intent.putExtra(Keys.Extras.IS_AGENT_SELECTED, true);
                        intent.putExtra(Keys.Extras.AGENT_ID, selectedAgentID);
                    } else {
                        Utils.showToast(mActivity, getStrings(R.string.please_select_agent).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
                        return;
                    }
                }
                intent.putExtra(Keys.Extras.IS_START_TIME, isStartDate);
                intent.putExtra(Keys.Extras.SELECTED_DATE, !isStartDate ? DateUtils.getInstance().getFormattedDate(endStartDateLaundry,
                        UIManager.getDateTimeFormat()) : getEndTime());
                if (!isStartDate) intent.putExtra("service_time", serviceTime);
                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);

            } else if (isStartEndTimeEnabled.equals(1) && scheduledTask.equals(0) && !(Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                openDatePicker();
            } else {
                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                        .getStorefrontData().isOrderAgentShedulingEnabled() &&
                        Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                        && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {

                    if (selectedAgentID > 0) {
                        intent.putExtra(Keys.Extras.IS_AGENT_SELECTED, true);
                        intent.putExtra(Keys.Extras.AGENT_ID, selectedAgentID);
                    } else {
                        Utils.snackBar(mActivity, getStrings(R.string.please_select_agent).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
                        return;
                    }
                }
                intent.putExtra(Keys.Extras.IS_START_TIME, isStartDate);
                intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, true);
                intent.putExtra(Keys.Extras.SELECTED_DATE, !isStartDate ? getStartTime() : getEndTime());
                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
            }

        } else if (i == R.id.tvProceedToPay) {
            //TODO for checkout template page


            if (!allMandatoryProductAdded()) {
                getBillBreakDown();
            } else if (validateDeliveryFields()) {
                proceedToPayCheck();
            }


        } else if (i == R.id.tvAdditionalHeading) {
            Intent templateIntent = new Intent(this, CheckoutTemplateActivity.class);
            if (templateDataList != null) {
                templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
                templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, false);
            }
            startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);
        } else if (i == R.id.tvCancellationPolicy) {
            Intent cancellationIntent = new Intent(this, ViewCancellationPolicyActivity.class);
            cancellationIntent.putExtra("STORE_ID",
                    Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getStorefrontUserId());
            startActivity(cancellationIntent);
        } else if (i == R.id.etSelectAgent) {

            setAgentList();

        }
    }


    private boolean validateDeliveryFields() {
        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            if (TextUtils.isEmpty(etPickupAddress.getText().toString())) {
                Utils.snackBar(this, getStrings(R.string.text_select_address));
                return false;

            }

            if (TextUtils.isEmpty(etDeliverAddress.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.please_enter_deliveryAddress).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
                return false;
            }

            if (!TextUtils.isEmpty(etPickupUserEmail.getText().toString()) && !Utils.isEmailValid(etPickupUserEmail.getText().toString())) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_email));
                return false;
            }
            if (!TextUtils.isEmpty(etPickupUserPhoneNumber.getText().toString()) && !Utils.isValidPhoneNumber(etPickupUserPhoneNumber.getText().toString())) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
                return false;
            }


        }
        return true;
    }

    public void setAgentList() {
        Intent intent = new Intent(mActivity, AgentListingCheckoutActivity.class);
        intent.putExtra(PICKUP_LATITUDE, pickUpLatitude);
        intent.putExtra(PICKUP_LONGITUDE, pickUpLongitude);
        startActivityForResult(intent, OPEN_AGENT_LIST_ACTIVITY);
    }

    private void goToStaticAddressActivity() {
        Intent intent = new Intent(this, SelectStaticAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ExtraConstants.EXTRA_ADDRESS_LIST, addressesList);
        intent.putExtras(bundle);
        startActivityForResult(intent, Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY);
    }

    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoFavLocationActivityFromPopUp() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY_FROM_DELIVERY_POPUP);
    }


    private boolean isValidTaskDetails() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String phone = etUserPhoneNumber.getText().toString();
        String startTime = getStartTime();
        String endTime = getEndTime();
        String address = etAddress.getText().toString();

        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP)
            address = etPickupAddress.getText().toString().trim();

        if ((Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE) &&
                (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 0 || (isStartEndTimeEnabled.equals(1))) &&
                isScheduled == 0 && subscribeTask == 0 &&
//                    isScheduled == 0 && //TODO check if we need to add this check for store close in case of subscribeTask also  added on 11 April 2019
                Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getIsStorefrontOpened() == 0) {
            Utils.snackBar(this, getStrings(R.string.store_currently_closed));
            return false;
        }

        if (selectedPickupMode == Constants.SelectedPickupMode.NONE) {
            Utils.snackBar(this, getStrings(R.string.choose_either_home_delivery_or_self_pickup)
                    .replace(TerminologyStrings.HOME_DELIVERY, StorefrontCommonData.getTerminology().getHomeDelivery())
                    .replace(TerminologyStrings.TAKE_AWAY, StorefrontCommonData.getTerminology().getSelfPickup()));
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
            return false;
        }
        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            if ((StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 2
                    || StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 1)
                    && TextUtils.isEmpty(email)) {
                Utils.snackBar(this, getStrings(R.string.email_is_required));
                etUserEmail.requestFocus();
                return false;
            }

        } else {
            if (((UIManager.getSignupField() == 2 || UIManager.getSignupField() == 0) && TextUtils.isEmpty(email))) {
                Utils.snackBar(this, getStrings(R.string.email_is_required));
                etUserEmail.requestFocus();
                return false;
            }
        }
        if (!email.isEmpty() && !Utils.isEmailValid(email)) {
            Utils.snackBar(this, getStrings(R.string.enter_valid_email));
            etUserEmail.requestFocus();
            return false;
        }

        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            if ((StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2
                    || StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 1)
                    && TextUtils.isEmpty(phone)) {
                Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                etUserPhoneNumber.requestFocus();
                return false;
            }
        } else {
            if (((UIManager.getSignupField() == 2 || UIManager.getSignupField() == 0) && TextUtils.isEmpty(phone))) {
                Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                etUserPhoneNumber.requestFocus();
                return false;
            }
        }

        /*if ((UIManager.getSignupField() == 2 || UIManager.getSignupField() == 1) && TextUtils.isEmpty(phone)) {
            Utils.snackBar(this, getStrings(R.string.error_enter_contact));
            etUserPhoneNumber.requestFocus();
            return false;
        }*/
        if (!phone.isEmpty() && !Utils.isValidPhoneNumber(phone)) {
            Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
            etUserPhoneNumber.requestFocus();
            return false;
        }

        if (Dependencies.isLaundryApp() && !cbDeliverAddressSameAsPickup.isChecked()) {
            if (TextUtils.isEmpty(etDeliverAddress.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.please_enter_deliveryAddress).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
                return false;
            }
            if (TextUtils.isEmpty(etDeliveryUserName.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                etDeliveryUserName.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(etDeliveryUserEmail.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.email_is_required));
                etDeliveryUserEmail.requestFocus();
                return false;
            }
            if (!Utils.isEmailValid(etDeliveryUserEmail.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_email));
                etDeliveryUserEmail.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(etDeliveryUserPhoneNumber.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                etDeliveryUserPhoneNumber.requestFocus();
                return false;
            }
            if (!Utils.isValidPhoneNumber(etDeliveryUserPhoneNumber.getText().toString().trim())) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
                etDeliveryUserPhoneNumber.requestFocus();
                return false;
            }
        }

        if (Dependencies.isLaundryApp()) {
            if (TextUtils.isEmpty(startTime)) {
                etStartDateTime.requestFocus();
                Utils.snackBar(mActivity, getStrings(R.string.pickup_dateTime_is_required).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
                return false;
            }
            Date startDate = DateUtils.getInstance().getDateFromString(getStartTime(), Constants.DateFormat.STANDARD_DATE_FORMAT);

            if (startDate.before(Calendar.getInstance().getTime()) || startDate.equals(Calendar.getInstance().getTime())) {
                etStartDateTime.requestFocus();
                Utils.snackBar(this, getStrings(R.string.invalid_selected_date));
                return false;
            }

            if (TextUtils.isEmpty(endTime)) {
                etEndDateTimeSchedule.requestFocus();
                Utils.snackBar(this, getStrings(R.string.delivery_dateTime_is_required).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
                return false;
            }

            Date endDate = DateUtils.getInstance().getDateFromString(getEndTime(), Constants.DateFormat.STANDARD_DATE_FORMAT);

            if (endDate.before(Calendar.getInstance().getTime()) || endDate.equals(Calendar.getInstance().getTime())) {
                etEndDateTimeSchedule.requestFocus();
                Utils.snackBar(this, getStrings(R.string.invalid_selected_date));
                return false;
            }

            if (endDate.before(startDate) || endDate.equals(startDate)) {
                etEndDateTimeSchedule.requestFocus();
                Utils.snackBar(this, getStrings(R.string.errorText_deliveryTime_shouldBe_greater_than_pickupTime).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
                return false;
            }

            if ((endDate.getTime() - endStartDateLaundry.getTime()) / (1000 * 60) < serviceTime) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(endStartDateLaundry);
                cal.add(Calendar.MINUTE, serviceTime);

                Utils.showToast(mActivity, getStrings(R.string.please_select_deliverDate_after_date).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)).replace(TerminologyStrings.DATE, DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat())));
                return false;
            }

        } else {
            if (!(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType()
                    == Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {


//                if (instantTask.equals(0) && scheduledTask.equals(0) && subscribeTask.equals(1) && !isSubscriptionValid() || rbSubscribe.isChecked() && subscribeTask.equals(1) && !isSubscriptionValid()) {
                if (rbSubscribe.isChecked() && subscribeTask.equals(1) && !isSubscriptionValid()) {

                    showErrorDialog();

                    return false;
                }


                if (!rbSubscribe.isChecked() && TextUtils.isEmpty(startTime)) {
                    etStartDateTime.requestFocus();
                    if (isStartEndTimeEnabled.equals(1) || (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                        Utils.snackBar(this, getStrings(R.string.startTime_is_required).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)));
                    }
//                    else if (subscribeTask == 1 && (rbSchedule.isChecked() || rbInstant.isChecked())) {
//                        //TODO check if this condition can be removed
////                        return true;
//                        Utils.snackBar(this, getStrings(R.string.startTime_is_required).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)));
//                    }
                    else {
                        Utils.snackBar(this, getStrings(R.string.date_time_is_required));
                    }
                    return false;
                }

                if (isStartEndTimeEnabled.equals(1) || (Dependencies.getSelectedProductsArrayList().size() > 0
                        && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                    isStartDate = true;
                    Date startDate = DateUtils.getInstance().getDateFromString(getStartTime(), Constants.DateFormat.STANDARD_DATE_FORMAT);

                    if (startDate.before(Calendar.getInstance().getTime()) || startDate.equals(Calendar.getInstance().getTime())) {
                        etStartDateTime.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.errorText_startTime_should_greater_than_currentTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)));
                        return false;
                    }

                    if (TextUtils.isEmpty(endTime)) {
                        etEndDateTimeSchedule.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.endTime_is_required).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
                        return false;
                    }

                    isStartDate = false;
                    Date endDate = DateUtils.getInstance().getDateFromString(getEndTime(), Constants.DateFormat.STANDARD_DATE_FORMAT);

                    if (endDate.before(Calendar.getInstance().getTime()) || endDate.equals(Calendar.getInstance().getTime())) {
                        etEndDateTimeSchedule.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.errorText_endTime_should_greater_than_currentTime).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
                        return false;
                    }

                    if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                            && !(Constants.ProductsUnitType.getUnitType(Dependencies.getSelectedProductsArrayList().get(0).getUnitType()) == Constants.ProductsUnitType.FIXED)) {

                        long differenceMultiple = Constants.ProductsUnitType.getStartEndDifferenceMultiple(Dependencies.getSelectedProductsArrayList().get(0).getUnitType(), Dependencies.getSelectedProductsArrayList().get(0).getUnit().intValue());
                        long currentDifference = DateUtils.getInstance().getDateFromString(getEndTime(), Constants.DateFormat.STANDARD_DATE_FORMAT).getTime() - DateUtils.getInstance().getDateFromString(getStartTime(), Constants.DateFormat.STANDARD_DATE_FORMAT).getTime();

                        if (!(currentDifference % differenceMultiple == 0)) {
                            new AlertDialog.Builder(this).message(
                                    getStrings(R.string.errorText_diff_bw_startTime_endTime_should_be_multiple_of)
                                            .replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true))
                                            .replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true))
                                            .replace(TerminologyStrings.UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(mActivity, Dependencies.getSelectedProductsArrayList().get(0).getUnit().intValue(), Dependencies.getSelectedProductsArrayList().get(0).getUnitType(), true)))
                                    .button(getStrings(R.string.ok_text))
                                    .build().show();
                            return false;
                        }
                    } else {
                        if (endDate.before(startDate) || endDate.equals(startDate)) {
                            etEndDateTimeSchedule.requestFocus();
                            Utils.snackBar(this, getStrings(R.string.errorText_startTime_shouldBe_less_than_endTime)
                                    .replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true))
                                    .replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
                            return false;
                        }
                    }
                }
            } else {
                if (storefrontData.getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                    if (TextUtils.isEmpty(startTime)) {
                        etStartDateTime.requestFocus();
                        if (isStartEndTimeEnabled.equals(1) || (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                            Utils.snackBar(this, getStrings(R.string.startTime_is_required).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)));
                        } else {
                            Utils.snackBar(this, getStrings(R.string.date_time_is_required));
                        }
                        return false;
                    }

                }
            }
        }

        if ((StorefrontCommonData.getAppConfigurationData().getIsShowDeliveryPopup() == 1)
                && (!isAddressPopUpShown) && (!Dependencies.isLaundryApp()) &&
                (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP
                        || selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY)) {
            showAddressDialog();
            return false;
        }

        return true;
    }

    private void showErrorDialog() {

        new OptionsDialog.Builder(mActivity).message(getStrings(mActivity, R.string.error_subscription)).listener(new OptionsDialog.Listener() {
            @Override
            public void performPositiveAction(int purpose, Bundle backpack) {

                rbSubscribe.performClick();
            }

            @Override
            public void performNegativeAction(int purpose, Bundle backpack) {
            }
        }).build().show();
    }

    private boolean isSubscriptionValid() {

        boolean isDaysSelected = false;

        if (selectedDaysList != null) {
            for (int i = 0; i < selectedDaysList.size(); i++) {
                if (selectedDaysList.get(i).isActive() && selectedDaysList.get(i).isSelected()) {
                    isDaysSelected = true;
                    break;
                }
            }
        }


        if (!isDaysSelected) {
            return false;
        }

        if (TextUtils.isEmpty(startDate)) {
            return false;
        }

        if (TextUtils.isEmpty(startTime)) {
            return false;
        }

        if (isSubscriptionCount) {
            if (TextUtils.isEmpty(endAfter)) {
                return false;
            }
        } else {

            if (TextUtils.isEmpty(endDate)) {
                return false;
            }

        }
        return true;
    }

    private void createSingleTaskWithPayment() {
        final HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

        if (paymentMethodEnabled) {
            if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
                sendPaymentForTask();
            } else {
                validateServingDistance();
            }
        } else if (selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) &&
                    (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }


            if (isCheckoutTemplateEnabled && templateDataList != null) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                hashMap.put("checkout_template", String.valueOf(myCustomArray));
            }

            if (Dependencies.isLaundryApp()) {
                RestClient.getApiInterface(this).createTaskLaundry(hashMap).enqueue(createTaskResponseResolver(hashMap));
            } else {
                hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
                if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                    hashMap.put("is_multi_currency_enabled_app", "1");
                }
                RestClient.getApiInterface(this).createTaskViaVendor(hashMap).enqueue(createTaskResponseResolver(hashMap));
            }
        }

    }

    private ResponseResolver<BaseModel> createTaskResponseResolver(
            final HashMap<String, String> hashMap) {

        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel validReqUrlPojo) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ORDER_CREATED_SUCCESS, 0 + "");
                Bundle bundle = new Bundle();
                bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                bundle.putString(Keys.Extras.SUCCESS_MESSAGE, validReqUrlPojo.getMessage());
                bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                bundle.putSerializable(MakePaymentActivity.class.getName(), hashMap);

                Transition.transitBookingSuccess(mActivity, bundle);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

                try {
                    UnavailableProductData unavailableProductData = baseModel.toResponseModel(UnavailableProductData.class);
                    if (unavailableProductData.getUnavailableProducts() != null && unavailableProductData.getUnavailableProducts().size() > 0)
                        new UnavailableProductsDialog(CheckOutActivity.this, unavailableProductData, mActivity, () -> {

                        }).show();
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

            }
        };
    }

    private CommonParams.Builder getCommonParamsBuilder() {

        int layoutType = StorefrontCommonData.getFormSettings().getWorkFlow();
        layoutType = layoutType == Constants.LayoutType.PICKUP || layoutType == Constants.LayoutType.DELIVERY ? Constants.LayoutType.PICKUP_DELIVERY : layoutType;
        CommonParams.Builder builder = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        builder.add(IS_SCHEDULED, isScheduled);
        builder.add(HAS_DELIVERY, 0)
                .add(HAS_PICKUP, 1)
                .add(JOB_PICKUP_ADDRESS, etAddress.getText().toString())
                .add(JOB_PICKUP_EMAIL, etUserEmail.getText().toString().trim())
                .add(JOB_PICKUP_PHONE, tvUserCountryCode.getText().toString() + etUserPhoneNumber.getText().toString())
                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                .add(JOB_PICKUP_NAME, etUserName.getText().toString().trim());


        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP)
            builder.add(CUSTOME_PICKUP_ADDRESS, address);

        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            builder.add(JOB_PICKUP_ADDRESS, etDeliverAddress.getText().toString());
            builder.add(CUSTOME_PICKUP_LATITUDE, deliverLatitude);
            builder.add(CUSTOME_PICKUP_LONGITUDE, deliverLongitude);
            builder.add(CUSTOME_PICKUP_NAME, etPickupUserName.getText().toString().trim());
            builder.add(CUSTOME_PICKUP_EMAIL, etPickupUserEmail.getText().toString().trim());
            if (etPickupUserPhoneNumber.getText().toString().trim().isEmpty())
                builder.add(CUSTOME_PICKUP_PHONE, "");
            else
                builder.add(CUSTOME_PICKUP_PHONE, tvPickupUserCountryCode.getText().toString() + etPickupUserPhoneNumber.getText().toString());
        }
        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
            builder.add(JOB_PICKUP_DATETIME, getStartTime());
            builder.add(JOB_DELIVERY_DATETIME, getStartTime());
        } else {
            builder.add(JOB_PICKUP_DATETIME, getStartTime());
            if (isStartEndTimeEnabled.equals(1)) {
                builder.add(JOB_DELIVERY_DATETIME, getEndTime());
            }
        }

        if (Dependencies.isLaundryApp()) {
            builder.add(JOB_DELIVERY_DATETIME, getEndTime());
            if (cbDeliverAddressSameAsPickup.isChecked()) {
                builder.add(JOB_DELIVERY_ADDRESS, etAddress.getText().toString());
                builder.add(JOB_DELIVERY_LATITUDE, pickUpLatitude);
                builder.add(JOB_DELIVERY_LONGITUDE, pickUpLongitude);
                builder.add(JOB_DELIVERY_NAME, etUserName.getText().toString().trim());
                builder.add(JOB_DELIVERY_EMAIL, etUserEmail.getText().toString().trim());
                builder.add(JOB_DELIVERY_PHONE, tvUserCountryCode.getText().toString() + etUserPhoneNumber.getText().toString());
            } else {
                builder.add(JOB_DELIVERY_ADDRESS, etDeliverAddress.getText().toString());
                builder.add(JOB_DELIVERY_LATITUDE, deliverLatitude);
                builder.add(JOB_DELIVERY_LONGITUDE, deliverLongitude);
                builder.add(JOB_DELIVERY_NAME, etDeliveryUserName.getText().toString().trim());
                builder.add(JOB_DELIVERY_EMAIL, etDeliveryUserEmail.getText().toString().trim());
                builder.add(JOB_DELIVERY_PHONE, tvDeliveryUserCountryCode.getText().toString() + etDeliveryUserPhoneNumber.getText().toString());
            }
        }


        builder.add("currency_id", Utils.getCurrencyId());

        if (storefrontData.getPaymentSettings() != null && storefrontData.getPaymentSettings().getSymbol() != null)
            builder.add("currency_symbol", storefrontData.getPaymentSettings().getSymbol());
        else
            builder.add("currency_symbol", Utils.getCurrencySymbol());


        if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            builder.add("self_pickup", 1);
        }

        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            builder.add("pick_and_drop", 1);

        }

        if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            builder.add(JOB_PICKUP_LATITUDE, storefrontData.getLatitude())
                    .add(JOB_PICKUP_LONGITUDE, storefrontData.getLongitude());
        }

        if (paymentMethodEnabled && valueEnabledPayment != 0) {
            builder.add("payment_method", valueEnabledPayment);
        } else {
            builder.add("payment_method", 0);
        }

        if (rbSubscribe.isChecked()) {

            builder.add("occurrence_count", endAfter);
            builder.add("start_schedule", startDate);
            builder.add("end_schedule", endDate);
            builder.add("schedule_time", DateUtils.getInstance().parseDateAs(startTime, STANDARD_DATE_FORMAT_TZ,
                    Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND));

            JSONArray jsonArray = new JSONArray();
            if (selectedDaysList != null) {
                for (int i = 0; i < selectedDaysList.size(); i++) {
                    if (selectedDaysList.get(i).isActive() && selectedDaysList.get(i).isSelected()) {
                        jsonArray.put(selectedDaysList.get(i).getDayId());
                    }
                }
            }
            builder.add("day_array", jsonArray.toString());
            builder.add("is_recurring_enabled", "1");

        } else {
            builder.add("is_recurring_enabled", "0");
        }

        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());

        try {
            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                ArrayList<Datum> selectedProductsList = Dependencies.getSelectedProductsArrayList();
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < selectedProductsList.size(); i++) {

                    if (selectedProductsList.get(i).getItemSelectedList().size() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("product_id", selectedProductsList.get(i).getProductId());
                        jsonObject.put("unit_price", selectedProductsList.get(i).getPrice());
                        jsonObject.put("quantity", selectedProductsList.get(i).getSelectedQuantity());
                        jsonObject.put("total_price", selectedProductsList.get(i).getTotalPrice());
                        jsonObject.put("return_enabled", selectedProductsList.get(i).getIsReturn());
                        if (selectedProductsList.get(i).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
                            jsonObject.put("start_time", DateUtils.getInstance().getFormattedDate(selectedProductsList.get(i).getProductStartDate(), Constants.DateFormat.STANDARD_DATE_FORMAT));
                            if (!(selectedProductsList.get(i).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                    && (Constants.ProductsUnitType.getUnitType(selectedProductsList.get(i).getUnitType()) == Constants.ProductsUnitType.FIXED)
                                    && selectedProductsList.get(i).getEnableTookanAgent() == 0))
                                jsonObject.put("end_time", DateUtils.getInstance().getFormattedDate(selectedProductsList.get(i).getProductEndDate(), Constants.DateFormat.STANDARD_DATE_FORMAT));
                        }
                        jsonObject.put("customizations", new JSONArray());

                        if (selectedProductsList.get(i).getIsProductTemplateEnabled() == 1 && selectedProductsList.get(i).getQuestionnaireTemplate() != null) {
                            Gson gson = new GsonBuilder().create();

                            JsonArray myCustomArray = gson.toJsonTree(selectedProductsList.get(i).getQuestionnaireTemplate()).getAsJsonArray();
                            jsonObject.put("template", new JSONArray(String.valueOf(myCustomArray)));
                        }
                        if (selectedProductsList.get(i).isAgentSelected())
                            jsonObject.put("agent_id", selectedProductsList.get(i).getSelectedAgentId());

                        jsonArray.put(jsonObject);
                    } else {
                        for (int j = 0; j < selectedProductsList.get(i).getItemSelectedList().size(); j++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("product_id", selectedProductsList.get(i).getProductId());
                            jsonObject.put("unit_price", selectedProductsList.get(i).getPrice());
                            jsonObject.put("quantity", selectedProductsList.get(i).getItemSelectedList().get(j).getQuantity());
                            jsonObject.put("total_price", selectedProductsList.get(i).getPrice().doubleValue() * selectedProductsList.get(i).getItemSelectedList().get(j).getQuantity());
                            jsonObject.put("return_enabled", selectedProductsList.get(i).getIsReturn());
                            if (selectedProductsList.get(i).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
                                jsonObject.put("start_time", DateUtils.getInstance().getFormattedDate(selectedProductsList.get(i).getProductStartDate(), Constants.DateFormat.STANDARD_DATE_FORMAT));
                                if (!(selectedProductsList.get(i).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                        && (Constants.ProductsUnitType.getUnitType(selectedProductsList.get(i).getUnitType()) == Constants.ProductsUnitType.FIXED)
                                        && selectedProductsList.get(i).getEnableTookanAgent() == 0))
                                    jsonObject.put("end_time", DateUtils.getInstance().getFormattedDate(selectedProductsList.get(i).getProductEndDate(), Constants.DateFormat.STANDARD_DATE_FORMAT));
                            }

                            JSONArray customArray = new JSONArray();

                            for (int k = 0; k < selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().size(); k++) {
                                for (int l = 0; l < selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomizeOptions().size(); l++) {

                                    JSONObject customJSONObject = new JSONObject();

                                    customJSONObject.put("cust_id", selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomizeOptions().get(l));
                                    customJSONObject.put("name", selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeText(selectedProductsList.get(i),
                                            selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomizeOptions().get(l)));
                                    customJSONObject.put("unit_price", selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomisationUnitPrice(selectedProductsList.get(i),
                                            selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomizeOptions().get(l)));
                                    customJSONObject.put("quantity", 1);
                                    customJSONObject.put("total_price", selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomisationUnitPrice(selectedProductsList.get(i),
                                            selectedProductsList.get(i).getItemSelectedList().get(j).getCustomizeItemSelectedList().get(k).getCustomizeOptions().get(l)));

                                    customArray.put(customJSONObject);
                                }
                            }

                            if (selectedProductsList.get(i).getIsProductTemplateEnabled() == 1 && selectedProductsList.get(i).getQuestionnaireTemplate() != null) {
                                Gson gson = new GsonBuilder().create();
                                JsonArray myCustomArray = gson.toJsonTree(selectedProductsList.get(i).getQuestionnaireTemplate()).getAsJsonArray();
                                jsonObject.put("template", new JSONArray(String.valueOf(myCustomArray)));
                            }

                            if (selectedProductsList.get(i).isAgentSelected())
                                jsonObject.put("agent_id", selectedProductsList.get(i).getSelectedAgentId());

                            jsonObject.put("customizations", customArray);
                            jsonArray.put(jsonObject);
                        }
                    }

                    if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                            .getStorefrontData().isOrderAgentShedulingEnabled() &&
                            Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                            && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {

                        if (selectedAgentID > 0) {
                            builder.add("tookan_agent_id", selectedAgentID);
                        }
                    }
                }
                builder.add("products", jsonArray);
            }
        } catch (JSONException e) {
            Utils.printStackTrace(e);
        }
        builder.add(JOB_DESCRIPTION_FIELD, etDescription.getText().
                toString())
                .add(AUTO_ASSIGNMENT, 1)
                .add(LAYOUT_TYPE, layoutType)
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add(VERTICAL, UIManager.getVertical())
                .add("is_app_product_tax_enabled", 1);
        return builder;
    }

    public String getStartTime() {
        if (etStartDateTime.getText().toString().trim().isEmpty()) {
            return "";
        }
        if (Dependencies.isLaundryApp()) {
            String[] startDate = etStartDateTime.getText().toString().trim().split("-");
            return DateUtils.getInstance().parseDateAs(startDate[0], UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
        } else {
            return DateUtils.getInstance().parseDateAs(etStartDateTime.getText().toString().trim(), UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
        }
    }

    public String getEndTime() {
        if (etEndDateTimeSchedule.getText().toString().trim().isEmpty()) {
            return "";
        }
        if (Dependencies.isLaundryApp()) {
            String[] endDate = etEndDateTimeSchedule.getText().toString().trim().split("-");
            return DateUtils.getInstance().parseDateAs(endDate[0], UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
        } else {
            return DateUtils.getInstance().parseDateAs(etEndDateTimeSchedule.getText().toString().trim(), UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
        }
    }

    private void addPickupCustomField(CommonParams.Builder builder, UserOptions userOptions) {
        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
            builder.add(PICKUP_CUSTOM_FIELD_TEMPLATE, StorefrontCommonData.getUserData().getData().getUserOptions().getTemplate())
                    .add(PICKUP_META_DATA, getMetaData(userOptions));
        }
    }

    private JSONArray getMetaData(UserOptions userOptions) {
        JSONArray jArrayMetaData = new JSONArray();
        for (Item item : userOptions.getItems()) {
            switch (item.getDataType()) {
                case Keys.DataType.IMAGE:
                    break;
            }
            JSONObject jsonObjectPickUpMetaData = new JSONObject();
            String data = item.getData().toString();
            try {
                if (item.getDataType().equalsIgnoreCase(Keys.DataType.IMAGE) && !item.getData().toString().isEmpty()) {
                    JSONArray jsonArray = new JSONArray();
                    List<String> imagesList = (ArrayList<String>) item.getData();
                    for (String url : imagesList) {
                        jsonArray.put(url);
                    }
                    data = jsonArray.toString();
                }
                if (item.getDataType().equalsIgnoreCase(Keys.DataType.TABLE) || item.getDataType().equalsIgnoreCase(Keys.DataType.CHECKLIST)) {
                    //Do Nothing
                } else {
                    jsonObjectPickUpMetaData.put("label", item.getLabel());
                    if (item.getLabel().equalsIgnoreCase("subtotal")) {
                        jsonObjectPickUpMetaData.put("data", getSubTotal());
                    } else if (item.getLabel().equalsIgnoreCase("task_details")) {
                        jsonObjectPickUpMetaData.put("data", Dependencies.getProductListDescription());
                    } else {
                        jsonObjectPickUpMetaData.put("data", data);
                    }
                    jArrayMetaData.put(jsonObjectPickUpMetaData);

                }
            } catch (JSONException e) {

                Utils.printStackTrace(e);
            }
        }
        return jArrayMetaData;
    }

    private void validateServingDistance() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("user_id", Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getStorefrontUserId())
                .add("job_pickup_latitude", pickUpLatitude)
                .add("job_pickup_longitude", pickUpLongitude)
                .add("access_token", Dependencies.getAccessToken(mActivity))
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("dual_user_key", UIManager.isDualUserEnable());

        if (selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            commonParams.add("pick_and_drop", 1);
            commonParams.add("job_delivery_latitude", deliverLatitude);
            commonParams.add("job_delivery_longitude", deliverLongitude);

        }

        if ((Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty())) {
            commonParams.add("access_token", Dependencies.getAccessTokenGuest(mActivity));

        }


        if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP)
            commonParams.add("self_pickup", 1);
        else
            commonParams.add("self_pickup", 0);

        if (isEditOrder && editJobId > 0)
            commonParams.add(PREV_JOB_ID, editJobId + "");

        RestClient.getApiInterface(this).validateServingDistance(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        sendPaymentForTask();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        try {
                            UnavailableProductData unavailableProductData = baseModel.toResponseModel(UnavailableProductData.class);
                            if (unavailableProductData.getUnavailableProducts() != null && unavailableProductData.getUnavailableProducts().size() > 0)
                                new UnavailableProductsDialog(CheckOutActivity.this, unavailableProductData, mActivity, () -> {

                                }).show();
                        } catch (Exception e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
    }

    private void sendPaymentForTask() {
        getCommonParamsBuilder().add(PAYMENT_METHOD_FIELD, valueEnabledPayment + "").build();

        SendPaymentForTask sendPaymentForTask = new SendPaymentForTask();
        try {
            //sendPaymentForTask.setData(baseModel.toResponseModel(ReqCatalogueData.class));
            sendPaymentForTask.setData(new Data());
            sendPaymentForTask.getData().setPerTaskCost(String.valueOf(productTotalCalculatedPrice));

        } catch (Exception e) {

            Utils.printStackTrace(e);
        }

        if (StorefrontCommonData.getAppConfigurationData().getTipEnableDisable() == 1
                && StorefrontCommonData.getAppConfigurationData().getEnableDefaultTip() == 1
                && StorefrontCommonData.getAppConfigurationData().getMinimumTip() == 0
                && selectedPickupMode != Constants.SelectedPickupMode.SELF_PICKUP) {
            getBillBreakDown(sendPaymentForTask);
        } else {
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_PAYMENT);
            Bundle extras = new Bundle();
            extras.putLong(VALUE_PAYMENT, valueEnabledPayment);


            extras.putSerializable(CheckOutActivity.class.getName(), getCommonParamsBuilder().build().getMap());
            if (isCheckoutTemplateEnabled && templateDataList != null)
                extras.putSerializable(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);

            extras.putParcelable(SEND_PAYMENT_FOR_TASK, sendPaymentForTask);
            extras.putBoolean(IS_PD_FLOW, selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP);
            extras.putBoolean(IS_SELF_PICKUP, selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP);
            extras.putBoolean(IS_EDIT_ORDER, isEditOrder);
            extras.putString("storename", storefrontData.getStoreName());
            if (StorefrontCommonData.getAppConfigurationData().getSochitelEnable() == 1) {
                if (sochitelId != null) {
                    extras.putInt(SOCHITEL_OPERATOR, Integer.parseInt(sochitelId));
                } else {
                    Utils.showToast(CheckOutActivity.this, "Please select an operator to proceed.");
                    showSochitelOperatorDialog();
                    return;
                }
            }
            if (isEditOrder)
                extras.putInt(EDIT_JOB_ID, editJobId);

            Intent intent = new Intent(mActivity, MakePaymentActivity.class);
            //  Intent intent = new Intent(mActivity, CheckOutActivity.class);
            intent.putExtras(extras);
            startActivityForResult(intent, OPEN_MAKE_PAYMENT_ACTIVITY);

        }

    }

    private void getBillBreakDown(final SendPaymentForTask sendPaymentForTask) {
        HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

        hashMap.put("amount", Double.parseDouble(sendPaymentForTask.getData().getPerTaskCost() + "") + "");
        hashMap.put("currency_id", Utils.getCurrencyId());
        hashMap.put("latitude", hashMap.get(JOB_PICKUP_LATITUDE));
        hashMap.put("longitude", hashMap.get(JOB_PICKUP_LONGITUDE));

        if (isEditOrder && editJobId > 0)
            hashMap.put("PREV_JOB_ID", editJobId + "");

        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (templateDataList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
            hashMap.put("checkout_template", String.valueOf(myCustomArray));
        }

        if (!hashMap.containsKey("customer_address")) {
            if (hashMap.containsKey(JOB_PICKUP_ADDRESS))
                hashMap.put("customer_address", hashMap.get(JOB_PICKUP_ADDRESS));
        }

        RestClient.getApiInterface(this).getBillBreakDown(hashMap)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        BillBreakDowns billBreakDowns = new BillBreakDowns();
                        try {
                            billBreakDowns.setData(baseModel.toResponseModel(com.tookancustomer.models.billbreakdown.Data.class));
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                        }

                        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_PAYMENT);
                        Bundle extras = new Bundle();
                        extras.putLong(VALUE_PAYMENT, valueEnabledPayment);
                        extras.putSerializable(CheckOutActivity.class.getName(), getCommonParamsBuilder().build().getMap());
                        if (isCheckoutTemplateEnabled && templateDataList != null)
                            extras.putSerializable(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);

                        extras.putSerializable(TIP_OPTION_LIST, billBreakDowns.getData().getTipOptionList());
                        extras.putString(OLD_DELIVERY_CHARGE, billBreakDowns.getData().getDeliveryCharge());

                        extras.putBoolean(IS_EDIT_ORDER, isEditOrder);
                        try{
                            extras.putInt(SOCHITEL_OPERATOR, Integer.parseInt(sochitelId));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (isEditOrder)
                            extras.putInt(EDIT_JOB_ID, editJobId);
                        extras.putParcelable(SEND_PAYMENT_FOR_TASK, sendPaymentForTask);
                        extras.putString("storename", storefrontData.getStoreName());

                        Intent intent = new Intent(mActivity, MakePaymentActivity.class);
                        //  Intent intent = new Intent(mActivity, CheckOutActivity.class);
                        intent.putExtras(extras);
                        startActivityForResult(intent, OPEN_MAKE_PAYMENT_ACTIVITY);
                    }

                    @Override
                    public void failure(APIError error, final BaseModel baseModel) {
                       /* try {

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
                            }
                        } catch (WindowManager.BadTokenException badTokenException) {
                            onBackPressed();
                        }*/


                    }
                });
    }


    private void getBillBreakDown() {
        HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

        hashMap.put("amount", 0 + "");
        hashMap.put("currency_id", Utils.getCurrencyId());
        hashMap.put("latitude", hashMap.get(JOB_PICKUP_LATITUDE));
        hashMap.put("longitude", hashMap.get(JOB_PICKUP_LONGITUDE));
        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            if (hashMap.containsKey(USER_ID)) {
                hashMap.put(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getSellerId() + "");
            }

        if (templateDataList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
            hashMap.put("checkout_template", String.valueOf(myCustomArray));
        }

        if (!hashMap.containsKey("customer_address")) {
            if (hashMap.containsKey(JOB_PICKUP_ADDRESS))
                hashMap.put("customer_address", hashMap.get(JOB_PICKUP_ADDRESS));
        }
        RestClient.getApiInterface(this).getBillBreakDown(hashMap)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        proceedToPayCheck();

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                        if (error.getStatusCode() == SHOW_REQUIRED_CATALOUGE.getStatusCode()) {
                            try {
                                ReqError reqError = error.toResponseModel(ReqError.class);

                                if (reqError.getMerchantCategoriesData() != null && reqError.getMerchantCategoriesData().getResult().size() > 0) {

                                    Log.d("ReqError", reqError.getMerchantCategoriesData().getResult().get(0).getDescription() + " error");


                                    Location location = LocationUtils.getLastLocation(CheckOutActivity.this);
                                    String latitude = storefrontData.getLatitude();
                                    String longitude = storefrontData.getLongitude();


                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(STOREFRONT_DATA, storefrontData);
                                    bundle.putSerializable(DATE_TIME, "");
                                    bundle.putSerializable(PICKUP_LATITUDE, latitude == null ? (location != null ? location.getLatitude() : 0.0) : latitude);
                                    bundle.putSerializable(PICKUP_LONGITUDE, longitude == null ? (location != null ? location.getLongitude() : 0.0) : longitude);
                                    bundle.putSerializable(PICKUP_ADDRESS, address);
                                    bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");
                                    bundle.putBoolean(IS_FROM_MANDATORY_CATEGORY, true);

                                    bundle.putSerializable("reqCatalogue", reqError.getMerchantCategoriesData());

                                    Intent intentCatalog = new Intent(CheckOutActivity.this, MerchantCatalogActivity.class);
                                    intentCatalog.putExtras(bundle);
                                    startActivityForResult(intentCatalog, MANDATORY_CATEGORY_ACTIVITY);


                                } else {
                                    Utils.snackBar(mActivity, error.getMessage());

                                }
                            } catch (Exception e) {

                                Utils.printStackTrace(e);
                                Utils.snackBar(mActivity, error.getMessage());
                            }
                        } else {
                            Utils.snackBar(mActivity, error.getMessage());

                        }


                    }
                });
    }

    void proceedToPayCheck() {
        if (isCheckoutTemplateEnabled && templateDataList == null) {
            if (isValidTaskDetails()) {
                Intent templateIntent = new Intent(CheckOutActivity.this, CheckoutTemplateActivity.class);
                startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);
            }
        } else {
            if (!Utils.internetCheck(CheckOutActivity.this)) {
                new AlertDialog.Builder(CheckOutActivity.this).message(getStrings(R.string.no_internet_try_again)).build().show();
                return;
            }
            if (minAmountPrice > Dependencies.getProductListSubtotal()) {
                Utils.snackBar(mActivity, getStrings(R.string.minimumOrderAmountIs).replace(ORDER, Utils.getCallTaskAs(true, false))
                        .replace(AMOUNT, Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + (UIManager.getCurrency(Utils.getDoubleTwoDigits(minAmountPrice)))));
                return;
            }

            if (UIManager.isCutomerVerifactionRequired()) {
                AdminVerficationDialog.getInstance(CheckOutActivity.this).show(getSupportFragmentManager(),
                        "ADMIN_VERIFICATION");
                return;
            }

//
//            if (storefrontData.getIsPdFlow()) {
//                createSingleTaskWithPayment();
//            } else {
            if (isValidTaskDetails())
                createSingleTaskWithPayment();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_LOCATION_ACTIVITY:
            case Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (isPickupAddressPdflow) {
                        if (isPickupAddress) {
                            address = data.getStringExtra("address");
                            etPickupAddress.setText(data.getStringExtra("address"));
                            etAddress.setText(data.getStringExtra("address"));
                            pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                            pickUpLongitude = data.getDoubleExtra("longitude", 0.0);
                            isPickupAddressPdflow = false;
                            if (cbDeliverAddressSameAsPickup.isChecked()) {
                                etDeliverAddress.setText(etAddress.getText().toString());
                                deliverLatitude = pickUpLatitude;
                                deliverLongitude = pickUpLongitude;
                            }
                        } else {
                            etDeliverAddress.setText(data.getStringExtra("address"));
                            deliverLatitude = data.getDoubleExtra("latitude", 0.0);
                            deliverLongitude = data.getDoubleExtra("longitude", 0.0);
                        }
                    } else {
                        if (isPickupAddress) {
                            address = data.getStringExtra("address");
                            etAddress.setText(data.getStringExtra("address"));
                            pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                            pickUpLongitude = data.getDoubleExtra("longitude", 0.0);

                            if (cbDeliverAddressSameAsPickup.isChecked()) {
                                etDeliverAddress.setText(etAddress.getText().toString());
                                deliverLatitude = pickUpLatitude;
                                deliverLongitude = pickUpLongitude;
                            }

                        } else {
                            etDeliverAddress.setText(data.getStringExtra("address"));
                            deliverLatitude = data.getDoubleExtra("latitude", 0.0);
                            deliverLongitude = data.getDoubleExtra("longitude", 0.0);
                        }
                    }
                }


                break;

            case Codes.Request.OPEN_MAKE_PAYMENT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                        setResult(RESULT_OK, data);
                        Transition.exit(this);
                    }
                }
                break;

            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (Dependencies.isLaundryApp()) {
                        Date d = new Date(), endDate = new Date();
                        d.setTime(data.getLongExtra("date", -1));
                        endDate.setTime(data.getLongExtra("endDate", -1));
                        serviceTime = data.getIntExtra("service_time", 0);

                        if (!isStartDate) {
                            etEndDateTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()) + "-"
                                    + DateUtils.getInstance().getFormattedDate(endDate, UIManager.getTimeFormat()));
                        } else {
                            llEndDateSchedule.setVisibility(View.VISIBLE);
                            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()) + "-"
                                    + DateUtils.getInstance().getFormattedDate(endDate, UIManager.getTimeFormat()));
                            endStartDateLaundry = endDate;
                        }


                    } else if (data != null) {
                        Date d = new Date(), endDate = new Date();
                        d.setTime(data.getLongExtra("date", -1));
                        endDate.setTime(data.getLongExtra("endDate", -1));

                        if (isStartDate) {
                            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                        } else {
                            etEndDateTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                        }
                    }
                }
                break;

            case CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE:
                isAddressPopUpShown = true;
                if (resultCode == RESULT_OK)
                    if (data.hasExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST)) {
                        templateDataList = (ArrayList<Template>) data.getSerializableExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST);
                        displayAdditionalTotalPrice(templateDataList);
                        setProceedButtonText();
                        if (getCustomTemplateCost(templateDataList) <= 0)
                            findViewById(R.id.tvProceedToPay).performClick();
                    }
                break;
            case OPEN_QUESTIONNAIRE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    setProceedButtonText();
                    rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
//                    rvCart.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            svCheckout.scrollTo(0, 0);
//                        }
//                    });
                }
                break;


            case SUBSCRIPTION_ACTIVITY:

                if (resultCode == RESULT_OK || selectedDaysList != null) {
                    if (data != null) {
                        startDate = data.getStringExtra("startDate");
                        startTime = data.getStringExtra("startTime");
                        endDate = data.getStringExtra("endDate");
                        endAfter = data.getStringExtra("endAfter");
                        isSubscriptionCount = data.getBooleanExtra("isSubscriptionCount", false);
                        selectedDaysList = (ArrayList<Days>) data.getSerializableExtra("selectedDays");
                    }
                }

                break;

            case MANDATORY_CATEGORY_ACTIVITY:
                setProceedButtonText();
                rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
                rvCart.post(new Runnable() {
                    @Override
                    public void run() {
                        svCheckout.scrollTo(0, 0);
                    }
                });
                break;
            case OPEN_AGENT_LIST_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    selectedAgentID = data.getIntExtra(AGENT_ID, 0);
                    AgentData agentData = (AgentData) data.getSerializableExtra("agentData");
                    etSelectAgent.setText(agentData.getName());
                }
                break;
            case OPEN_LOCATION_ACTIVITY_FROM_DELIVERY_POPUP:
                if (resultCode == RESULT_OK) {
                    if (isFromPDFlowAddressChange) {
                        etDeliverAddress.setText(data.getStringExtra("address"));
                        deliverLatitude = data.getDoubleExtra("latitude", 0.0);
                        deliverLongitude = data.getDoubleExtra("longitude", 0.0);
                        isFromPDFlowAddressChange = false;
                    } else {
                        if (data != null) {
                            etAddress.setText(data.getStringExtra("address"));
                            pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                            pickUpLongitude = data.getDoubleExtra("longitude", 0.0);
                        }
                    }
                }

                break;
            case OPEN_CUSTOMISATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;

                    Datum productDataItem = null;

//                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
//                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
//                    }
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }
                    setdata(false);

                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                    }
                    // setTotalQuantity(true);
                    //activityResultCheckoutScreen();

                   /* if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }*/

//                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
//                        rlTotalQuantity.performClick();
//                    }
                }
                rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
                break;
            case Codes.Request.OFTEN_VIEW_MORE_ACTIVITY:
                setdata(true);
                rvCart.setAdapter(new CheckoutCartAdapter(CheckOutActivity.this, Dependencies.getSelectedProductsArrayList()));
                break;
        }

    }

    private double getCustomTemplateCost(ArrayList<Template> templateDataList) {
        double cost = 0;
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);
            if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)
                    || template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {

                cost += template.getCost();
            }
        }
        return cost;
    }

    private void displayAdditionalTotalPrice(ArrayList<Template> templateDataList) {
        double cost = getCustomTemplateCost(templateDataList);

        rlAdditionalPrice.setVisibility(View.VISIBLE);
        rlTotallPrice.setVisibility(View.VISIBLE);

        if (cost > 0)
            tvAdditionalAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(cost)));
        else
            tvAdditionalAmount.setText("");


        if (productTotalCalculatedPrice <= 0) {
            tvTotalAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(cost + Dependencies.getProductListSubtotal())));
            finalAmount = cost + Dependencies.getProductListSubtotal();
        } else {
            tvTotalAmount.setText((UIManager.getCurrency(Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(cost + productTotalCalculatedPrice))));
            finalAmount = cost + productTotalCalculatedPrice;
        }
        tvAdditionalHeading.setText(CustomViewsUtil.createSpan(mActivity, cost > 0 ? getStrings(R.string.additional_amount) :
                        getStrings(R.string.additional_info),
                " (", getStrings(R.string.edit), ")", ContextCompat.getColor(mActivity, R.color.colorPrimary)));


    }

    public void openDatePicker() {
        Utils.hideSoftKeyboard(this, etStartDateTime);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        //Check if date is valid in lollipop devices
        if (isValidDate(year, monthOfYear, dayOfMonth)) {
            setDateVariables(year, monthOfYear, dayOfMonth);
            if (datePicker.isShown()) {
                showTimePicker();
            }
        } else {
            new AlertDialog.Builder(this).message(getStrings(R.string.invalid_selected_date)).button(getStrings(R.string.ok_text)).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    openDatePicker();
                }
            }).build().show();
        }
    }

    public boolean isValidDate(int year, int monthOfYear, int dayOfMonth) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar currentDateCalendar = Calendar.getInstance();

            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(Calendar.YEAR, year);
            selectedDateCalendar.set(Calendar.MONTH, monthOfYear);
            selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (selectedDateCalendar.before(currentDateCalendar)) {
                isValidDate = false;
            }
        }
        return isValidDate;
    }

    public void setDateVariables(int year, int monthOfYear, int dayOfMonth) {
        if (isStartDate) {
            selectedStartDate = DateUtils.getInstance().getDateFromString(year + "-" + monthOfYear + "-" + dayOfMonth, Constants.DateFormat.ONLY_DATE);
        } else {
            selectedEndDate = DateUtils.getInstance().getDateFromString(year + "-" + monthOfYear + "-" + dayOfMonth, Constants.DateFormat.ONLY_DATE);
        }
    }

    public void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (isValidTime(hour, minute)) {
            setTimeVariables(hour, minute);
            setDateTimeText();
        } else {
            new AlertDialog.Builder(this).message(getStrings(R.string.invalid_selected_date)).button(getStrings(R.string.ok_text)).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    showTimePicker();
                }
            }).build().show();
        }
    }

    public boolean isValidTime(int hour, int minute) {
        boolean isValidTime = true;
        Calendar currentCal = Calendar.getInstance();
        Calendar selectedDateTime = Calendar.getInstance();
        selectedDateTime.setTimeInMillis(isStartDate ? selectedStartDate.getTime() : selectedEndDate.getTime());
        selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
        selectedDateTime.set(Calendar.MINUTE, minute);
        if (selectedDateTime.before(currentCal)) {
            isValidTime = false;
        }
        return isValidTime;
    }

    public void setTimeVariables(int hour, int minute) {
        if (isStartDate) {
            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.setTimeInMillis(selectedStartDate.getTime());
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
            selectedDateTime.set(Calendar.MINUTE, minute);
            selectedStartDate.setTime(selectedDateTime.getTimeInMillis());
        } else {
            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.setTimeInMillis(selectedEndDate.getTime());
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
            selectedDateTime.set(Calendar.MINUTE, minute);
            selectedEndDate.setTime(selectedDateTime.getTimeInMillis());
        }
    }

    public void setDateTimeText() {
        if (isStartDate) {
            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(selectedStartDate, UIManager.getDateTimeFormat()));
        } else {
            etEndDateTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(selectedEndDate, UIManager.getDateTimeFormat()));
        }
    }

    /**
     * Product whose quantity becomes zero in cart need to be shown back in the sideorder list
     *
     * @param productId ProductId that is removed from cart
     */
    public void makeSideOrderProductVisible(final Integer productId) {
        for (int i = 0; i < sideOrderArrayList.size(); i++) {
            if (sideOrderArrayList.get(i).getProductId().equals(productId)) {
                {
                    sideOrderArrayList.get(i).setSelectedQuantity(0);
//                    sideOrderCheckoutAdapter.setData(sideOrderArrayList);
                    sideOrderCheckoutAdapter.notifyDataSetChanged();

                    tvCartHeader.setText(StorefrontCommonData.getTerminology().getCart(true)
//                            + " " + (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getProduct() : StorefrontCommonData.getTerminology().getProduct())
                            + "(" + Dependencies.getCartSize() + ")");
                    break;

                }


            }
        }

    }

    @Override
    public void onAcknowledge() {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
        bundle.putString(Keys.Extras.SUCCESS_MESSAGE, "");
        bundle.putBoolean(Keys.Extras.IS_ADMIN_VERIFICATION_REQUIRED, true);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onListItemSelected(int pos, String itemString) {
        sochitelId = arrayListSochitelOpIds.get(pos);
    }

    public void gotoMapActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void getSingleMerchantDetails(final int storefrontId) {
        final Location location = LocationUtils.getLastLocation(mActivity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.LATITUDE, location != null ? location.getLatitude() : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, location != null ? location.getLongitude() : 0);
        commonParams.add(USER_ID, storefrontId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));

                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontDataNew = cityStorefrontsModels.getData().get(0);


                if (storefrontDataNew != null && storefrontDataNew.getIsFreeDeliveryEnabled() == 1
                        && storefrontDataNew.getFree_delivery_toggle() == null) {
                    freeDeliveryAmount = storefrontDataNew.getFree_delivery_amount();
                    tvFreeDeliveryHeader.setText(
                            "(Free Delivery, if order value is more than \n "
                                    + UIManager.getCurrency(Utils.getCurrencySymbol(storefrontDataNew.getPaymentSettings()))
                                    + Utils.getDoubleTwoDigits(freeDeliveryAmount) + ")");
                    if (freeDeliveryAmount > 0.0) {
                        tvFreeDeliveryHeader.setVisibility(View.VISIBLE);
                    } else {
                        tvFreeDeliveryHeader.setVisibility(View.GONE);
                    }

                } else if (storefrontDataNew != null && storefrontDataNew.getIsFreeDeliveryEnabled() == 1
                        && storefrontDataNew.getFree_delivery_toggle() != null
                        && storefrontDataNew.getFree_delivery_toggle() != 0.0) {

                    freeDeliveryAmount = storefrontDataNew.getFree_delivery_toggle();

                    tvFreeDeliveryHeader.setText(
                            "(Free Delivery, if order value is more than \n "
                                    + UIManager.getCurrency(Utils.getCurrencySymbol(storefrontDataNew.getPaymentSettings()))
                                    + Utils.getDoubleTwoDigits(freeDeliveryAmount) + ")");
                    if (freeDeliveryAmount > 0.0) {
                        tvFreeDeliveryHeader.setVisibility(View.VISIBLE);
                    } else {
                        tvFreeDeliveryHeader.setVisibility(View.GONE);
                    }
                } else {
                    tvFreeDeliveryHeader.setVisibility(View.GONE);
                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }


}
