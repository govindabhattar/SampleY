package com.tookancustomer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tookancustomer.adapters.CartRecyclerAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.ExtraConstants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.customviews.CustomFieldCheckboxDelivery;
import com.tookancustomer.customviews.CustomFieldCheckboxPickup;
import com.tookancustomer.customviews.CustomFieldImageDelivery;
import com.tookancustomer.customviews.CustomFieldImagePickup;
import com.tookancustomer.customviews.CustomFieldTextViewDelivery;
import com.tookancustomer.customviews.CustomFieldTextViewPickup;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.UnavailableProductsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.SendPaymentTask.Data;
import com.tookancustomer.models.SendPaymentTask.SendPaymentForTask;
import com.tookancustomer.models.UnavailableProductData.UnavailableProductData;
import com.tookancustomer.models.staticAddressData.StaticAddressData;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.models.userdata.PickupDetails;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CheckOutActivityOld extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Keys.APIFieldKeys {
    private String TAG = CheckOutActivityOld.class.getSimpleName();
    RelativeLayout rlBack, rlCartHeader, rlDescriptionHeader, rlPickupDetailsHeader, rlDeliveryDetailsHeader, rlAddressHeader;
    LinearLayout llPickupStartDate, llCartView, llPickupView, llDeliveryView, llCartChild, llDescriptionChild, llAddressDetailsChild, llPickupDetailsChild, llDeliveryDetailsChild, llPickupCustomFields, llDeliveryCustomFields, llPickupDetailsCountryCode, llDeliveryDetailsCountryCode, llEndDate, llPickupResetRemove, llDeliveryResetRemove;
    TextView tvHeading, tvNotesHeader, tvCartHeader, tvCartAction, tvSubTotal, tvProceedToPay, tvCartSize, tvDescriptionAction, tvPickupDetailsHeader, tvPickupDetailsAction, tvStartDateText, tvPickupDetailsReset, tvPickupDetailsRemove, tvPickupDetailsCountryCode, tvDeliveryDetailsCountryCode, tvDeliveryDetailsAction, tvDeliveryDetailsReset, tvDeliveryDetailsRemove, tvAddressHeader;
    EditText etPickupDetailsAddress, etPickupDetailsName, etPickupDetailsPhoneNumber, etPickupDetailsEmail, etPickupDetailsreadyForPickup, etPickupDetailsEndDate, etDeliveryDetailsAddress, etDeliveryDetailsName, etDeliveryDetailsPhoneNumber, etDeliveryDetailsEmail, etDeliveryDetailsLatestDeliveryByTime;
    RecyclerView rvCart;
    EditText etDescription;
    boolean isPickup;
    private int customFieldPositionPickup, customFieldPositionDelivery;
    private int barcodeRequestCode;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = -1;
    private int endYear = 0;
    private int endMonth = 0;
    private int endDay = 0;
    private int endHour = 0;
    private int endMinute = -1;
    private boolean isStartDate;
    private long valueEnabledPayment;
    private boolean paymentMethodEnabled = false;
    private PickupDetails pickupDetails;
    private PickupDetails deliveryDetails;
    private Double pickUpLatitude = 0.0, deliveryLatitude = 0.0;
    private Double pickUpLongitude = 0.0, deliveryLongitude = 0.0;
    ImageView ibArrow, ibArrowPickupDetails;
    private int isScheduled = 0;
    NestedScrollView svCheckout;
    boolean isPickupAdded, isDeliveryAdded;
    String mapLayoutAddress;
    View vwPickupDetailsName, vwPickupDetailsPhone, vwPickupDetailsEmail, vwDeliveryDetailsName, vwDeliveryDetailsphone, vwDeliveryDetailsEmail;
    TextView tvPickupNameText, tvPickupPhoneText, tvPickupEmailText, tvDeliveryNameText, tvDeliveryPhoneText, tvDeliveryEmailText;
    View vwShadow;
    private final int iBack = R.id.rlBack;
    private Double minAmountPrice = 0.0;

    private LinearLayout llSelectDateTime, llDateTimeChild, llStartDate, llEndDateSchedule, llStartEndTimeSchedule;
    private RelativeLayout rlDateTimeHeader;
    private RadioGroup rgInstantSchedule;
    private RadioButton rbInstant;
    private TextView tvDateTimeHeader;
    private EditText etStartDateTime, etEndDateTimeSchedule, etStartEndTimeSchedule;

    private RelativeLayout rlSubtotalPrice, rlTotalPrice;
    private TextView tvNoOfItems, tvTotalPriceCart;
    com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private Integer instantTask, scheduledTask, isStartEndTimeEnabled = 0;

    private LinearLayout llSelfPickupDelivery, llHomeDelivery, llSelfPickup;
    private TextView tvHomeDelivery, tvSelfPickup;
    private int selectedPickupMode = Constants.SelectedPickupMode.NONE;

    private String address = "";
    private ArrayList<StaticAddressData> addressesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mActivity = this;
        storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            storefrontData = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData();
        }

        mapLayoutAddress = getIntent().getStringExtra(PICKUP_ADDRESS);
        if (getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE) != null) {
            Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
        } else if (getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE) != null) {
            Utils.snackBar(mActivity, getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE));
        }

        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null && Dependencies.getUserOptions(this) != null) {
            for (int i = 0; i < StorefrontCommonData.getUserData().getData().getUserOptions().getItems().size(); i++) {
                Object data = Dependencies.getUserOptions(this).getItems().get(i).getData();
                StorefrontCommonData.getUserData().getData().getUserOptions().getItems().get(i).setData(data);
            }
        }
        if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null && Dependencies.getDeliveryOptions(this) != null) {
            for (int i = 0; i < StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().size(); i++) {
                Object data = Dependencies.getDeliveryOptions(this).getItems().get(i).getData();
                StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().get(i).setData(data);
            }
        }

        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            minAmountPrice = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder();
        } else {
            minAmountPrice = StorefrontCommonData.getFormSettings().getMerchantMinimumOrder();
        }

        initializeFields();
        showPaymentSlider();
        renderCustomFields();
        setLayoutType();
//        if (mapLayoutAddress != null && !mapLayoutAddress.isEmpty()) {
        address = getIntent().getStringExtra(PICKUP_ADDRESS);
        etPickupDetailsAddress.setText(getIntent().getStringExtra(PICKUP_ADDRESS));
        pickUpLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickUpLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
            etDeliveryDetailsAddress.setText(getIntent().getStringExtra(PICKUP_ADDRESS));
            deliveryLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
            deliveryLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        }
//        } else {
//            executeSetAddress();
//            if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
//                deliveryLatitude = getCurrentLocation().latitude;
//                deliveryLongitude = getCurrentLocation().longitude;
//            }
//            pickUpLatitude = getCurrentLocation().latitude;
//            pickUpLongitude = getCurrentLocation().longitude;
//        }


        //svCheckout.pageScroll(View.FOCUS_UP);
        rvCart.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                svCheckout.scrollTo(0, 0);
                // recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
        svCheckout.scrollTo(0, 0);
        // svCheckout.fullScroll(ScrollView.FOCUS_UP);

        //etPickupDetailsAddress.setHint(getStrings(R.string.getting_address));
        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
            etDeliveryDetailsAddress.setHint(getStrings(R.string.getting_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        }

        etPickupDetailsName.setCursorVisible(false);
        etDeliveryDetailsName.setCursorVisible(false);

        etDeliveryDetailsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDeliveryDetailsName.setCursorVisible(true);
            }
        });

        etPickupDetailsName.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                etPickupDetailsName.requestFocus();
                etPickupDetailsName.requestFocusFromTouch();
                etPickupDetailsName.setCursorVisible(true);
                return false;
            }
        });

        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            instantTask = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getInstantTask();
            scheduledTask = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getScheduledTask();
            isStartEndTimeEnabled = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getIsStartEndTimeEnable();
        } else {
            instantTask = StorefrontCommonData.getFormSettings().getInstantTask();
            scheduledTask = StorefrontCommonData.getFormSettings().getScheduledTask();
        }

        resetSelfPickupDeliveryAssets();
        setDateFields();


        /*if (Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                UIManager.isStaticAddressEnabledForAdmin()) {
            setAddressBarText();
            callbackForStaticAddresses(StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        } else if (!Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                Dependencies.getSelectedProductsArrayList().get(0).isStaticAddressEnabled()) {
            setAddressBarText();
            callbackForStaticAddresses(Dependencies.getSelectedProductsArrayList().get(0).getUserId());
        }
        */
        if (isStaticAddressFlow()) {
            setAddressBarText();
            callbackForStaticAddresses(getUserIdForStaticAddressFlow());
        }

        /*setAddressBarText();
        callbackForStaticAddresses(StorefrontCommonData.getUserData().getData().getVendorDetails().getUserId());
        */
    }

    private boolean isStaticAddressFlow() {
        if (Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                UIManager.isStaticAddressEnabledForAdmin()) {
            return true;
        } else if (!Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                Dependencies.getSelectedProductsArrayList().get(0).isStaticAddressEnabled()) {
            return true;
        }

        return false;
    }


    private int getUserIdForStaticAddressFlow() {
        /*if (Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                UIManager.isStaticAddressEnabledForAdmin()) {
            return StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId();
        } else if (!Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
                Dependencies.getSelectedProductsArrayList().get(0).isStaticAddressEnabled()) {
            return Dependencies.getSelectedProductsArrayList().get(0).getUserId();
        }
        return 0;*/

        return Dependencies.getSelectedProductsArrayList().get(0).getUserId();
    }


    private void goToStaticAddressActivity() {
        Intent intent = new Intent(this, SelectStaticAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ExtraConstants.EXTRA_ADDRESS_LIST, addressesList);
        intent.putExtras(bundle);
        startActivityForResult(intent, Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY);
    }


    private void setAddressBarText() {
        etPickupDetailsAddress.setText(getStrings(R.string.text_select_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        pickUpLatitude = 0.0;
        pickUpLongitude = 0.0;
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

    private void initializeFields() {
        ((TextView) findViewById(R.id.tvAddAnotherPromo)).setText(getStrings(R.string.subtotal));
        ((TextView) findViewById(R.id.tvTotal)).setText(getStrings(R.string.total));

        ((TextView) findViewById(R.id.tvAddress)).setText(StorefrontCommonData.getTerminology().getAddress());


        ((TextView) findViewById(R.id.tvDateTimeHeader)).setText(getStrings(R.string.checkout_select_date_time));
        ((RadioButton) findViewById(R.id.rbSchedule)).setText(getStrings(R.string.schedule_later));
        ((EditText) findViewById(R.id.etStartDateTime)).setHint(getStrings(R.string.select_date_time));
        ((MaterialEditText) findViewById(R.id.etStartDateTime)).setFloatingLabelText(getStrings(R.string.date_and_time));
//        ((EditText) findViewById(R.id.etStartEndTimeSchedule)).setHint(getStrings(R.string.enter_scheduled_time));
//        ((MaterialEditText) findViewById(R.id.etStartEndTimeSchedule)).setFloatingLabelText(getStrings(R.string.scheduled_time));
//
//        ((TextView) findViewById(R.id.tvDescriptionAction)).setText(getStrings(R.string.add));
//
//        ((TextView) findViewById(R.id.tvPickupDetailsReset)).setText(getStrings(R.string.reset));
//        ((TextView) findViewById(R.id.tvPickupDetailsRemove)).setText(getStrings(R.string.remove));
//        ((TextView) findViewById(R.id.tvPickupDetailsAction)).setText(getStrings(R.string.add));
//        ((TextView) findViewById(R.id.tvPickupNameText)).setText(getStrings(R.string.name));
//        ((EditText) findViewById(R.id.etPickupDetailsName)).setHint(getStrings(R.string.enter_name));
//        ((TextView) findViewById(R.id.tvPickupPhoneText)).setText(getStrings(R.string.phone));
//        ((EditText) findViewById(R.id.etPickupDetailsPhoneNumber)).setHint(getStrings(R.string.enter_phone_number));
//        ((TextView) findViewById(R.id.tvPickupEmailText)).setText(getStrings(R.string.email));
//        ((EditText) findViewById(R.id.etPickupDetailsEmail)).setHint(getStrings(R.string.enter_email));
//        ((TextView) findViewById(R.id.tvStartDateText)).setText(getStrings(R.string.ready_for_pickup));
//        ((EditText) findViewById(R.id.etPickupDetailsreadyForPickup)).setHint(getStrings(R.string.enter_pickup_time));
//        ((TextView) findViewById(R.id.tvEndDate)).setText(getStrings(R.string.end_date));
//        ((EditText) findViewById(R.id.etPickupDetailsEndDate)).setHint(getStrings(R.string.enter_end_date));


        tvHeading = findViewById(R.id.tvHeading);
        rlBack = findViewById(R.id.rlBack);
        tvProceedToPay = findViewById(R.id.tvProceedToPay);
        tvProceedToPay.setText(StorefrontCommonData.getTerminology().getProceedToPay(true));

        svCheckout = findViewById(R.id.svCheckout);
        //cart
//        llCartView = findViewById(R.id.llCartView);
        rlCartHeader = findViewById(R.id.rlCartHeader);
        llCartChild = findViewById(R.id.llCartChild);
        tvCartHeader = findViewById(R.id.tvCartHeader);
        tvCartHeader.setText(StorefrontCommonData.getTerminology().getCart(true));
        tvNotesHeader = findViewById(R.id.tvNotesHeader);
        tvNotesHeader.setText(getStrings(R.string.additional_info));
        tvCartAction = findViewById(R.id.tvCartAction);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        rvCart = findViewById(R.id.rvCart);
//        tvCartSize = findViewById(R.id.tvCartSize);
//        ibArrow = findViewById(R.id.ibArrow);
//        ibArrowPickupDetails = findViewById(R.id.ibArrowPickupDetails);
//        //task description
//        tvDescriptionAction = findViewById(R.id.tvDescriptionAction);
//        etDescription = findViewById(R.id.etDescription);
//        etDescription.setHint(getStrings(R.string.enter_additional_info));
//        rlDescriptionHeader = findViewById(R.id.rlDescriptionHeader);
//        llDescriptionChild = findViewById(R.id.llDescriptionChild);
//        //pickup details
//        llPickupView = findViewById(R.id.llPickupView);
//        rlPickupDetailsHeader = findViewById(R.id.rlPickupDetailsHeader);
//        llPickupDetailsChild = findViewById(R.id.llPickupDetailsChild);
//        llAddressDetailsChild = findViewById(R.id.llAddressDetailsChild);
//        tvPickupDetailsHeader = findViewById(R.id.tvPickupDetailsHeader);
//        tvPickupDetailsHeader.setText(getStrings(R.string.personal) + " " + getStrings(R.string.details));
//        tvPickupDetailsAction = findViewById(R.id.tvPickupDetailsAction);
//        tvStartDateText = findViewById(R.id.tvStartDateText);
//        tvPickupDetailsReset = findViewById(R.id.tvPickupDetailsReset);
//        tvPickupDetailsRemove = findViewById(R.id.tvPickupDetailsRemove);
//        tvPickupDetailsCountryCode = findViewById(R.id.tvPickupDetailsCountryCode);
//        etPickupDetailsAddress = findViewById(R.id.etPickupDetailsAddress);
//        etPickupDetailsAddress.setHint(getStrings(R.string.add_address));
//        etPickupDetailsName = findViewById(R.id.etPickupDetailsName);
//        etPickupDetailsPhoneNumber = findViewById(R.id.etPickupDetailsPhoneNumber);
//        etPickupDetailsEmail = findViewById(R.id.etPickupDetailsEmail);
//        etPickupDetailsEndDate = findViewById(R.id.etPickupDetailsEndDate);
//        etPickupDetailsreadyForPickup = findViewById(R.id.etPickupDetailsreadyForPickup);
//        llPickupStartDate = findViewById(R.id.llPickupStartDate);
//        llPickupCustomFields = findViewById(R.id.llPickupCustomFields);
//        llPickupDetailsCountryCode = findViewById(R.id.llPickupDetailsCountryCode);
//        llEndDate = findViewById(R.id.llEndDate);
//        llPickupResetRemove = findViewById(R.id.llPickupResetRemove);
//        //delivery details
//        llDeliveryView = findViewById(R.id.llDeliveryView);
//        rlDeliveryDetailsHeader = findViewById(R.id.rlDeliveryDetailsHeader);
//        rlAddressHeader = findViewById(R.id.rlAddressHeader);
//        llDeliveryDetailsChild = findViewById(R.id.llDeliveryDetailsChild);
//        tvDeliveryDetailsAction = findViewById(R.id.tvDeliveryDetailsAction);
//        tvDeliveryDetailsCountryCode = findViewById(R.id.tvDeliveryDetailsCountryCode);
//        tvDeliveryDetailsReset = findViewById(R.id.tvDeliveryDetailsReset);
//        tvDeliveryDetailsRemove = findViewById(R.id.tvDeliveryDetailsRemove);
//        etDeliveryDetailsAddress = findViewById(R.id.etDeliveryDetailsAddress);
//        etDeliveryDetailsName = findViewById(R.id.etDeliveryDetailsName);
//        etDeliveryDetailsPhoneNumber = findViewById(R.id.etDeliveryDetailsPhoneNumber);
//        etDeliveryDetailsEmail = findViewById(R.id.etDeliveryDetailsEmail);
//        etDeliveryDetailsLatestDeliveryByTime = findViewById(R.id.etDeliveryDetailsLatestDeliveryByTime);
//        llDeliveryCustomFields = findViewById(R.id.llDeliveryCustomFields);
//        llDeliveryDetailsCountryCode = findViewById(R.id.llDeliveryDetailsCountryCode);
//        llDeliveryResetRemove = findViewById(R.id.llDeliveryResetRemove);
//
//        vwPickupDetailsName = findViewById(R.id.vwPickupDetailsName);
//        vwPickupDetailsPhone = findViewById(R.id.vwPickupDetailsPhone);
//        vwPickupDetailsEmail = findViewById(R.id.vwPickupDetailsEmail);
//        vwDeliveryDetailsName = findViewById(R.id.vwDeliveryDetailsName);
//        vwDeliveryDetailsphone = findViewById(R.id.vwDeliveryDetailsphone);
//        vwDeliveryDetailsEmail = findViewById(R.id.vwDeliveryDetailsEmail);
//
//        rlSubtotalPrice = findViewById(R.id.rlSubtotalPrice);
//        rlTotalPrice = findViewById(R.id.rlTotalPrice);
//        tvNoOfItems = findViewById(R.id.tvNoOfItems);
//        tvTotalPriceCart = findViewById(R.id.tvTotalPriceCart);
//
//        rlSubtotalPrice.setVisibility(View.VISIBLE);
//        rlTotalPrice.setVisibility(View.GONE);
//
//        tvPickupNameText = findViewById(R.id.tvPickupNameText);
//        tvPickupPhoneText = findViewById(R.id.tvPickupPhoneText);
//        tvPickupEmailText = findViewById(R.id.tvPickupEmailText);
//        tvDeliveryNameText = findViewById(R.id.tvDeliveryNameText);
//        tvDeliveryPhoneText = findViewById(R.id.tvDeliveryPhoneText);
//        tvDeliveryEmailText = findViewById(R.id.tvDeliveryEmailText);
//        vwShadow = findViewById(R.id.vwShadow);

        rbInstant = findViewById(R.id.rbInstant);
        rbInstant.setText(Utils.getCallTaskAs(true, true) + " " + getStrings(R.string.order_now));

        llSelfPickupDelivery = findViewById(R.id.llSelfPickupDelivery);
        llHomeDelivery = findViewById(R.id.llHomeDelivery);
        llSelfPickup = findViewById(R.id.llSelfPickup);
        tvHomeDelivery = findViewById(R.id.tvHomeDelivery);
        tvHomeDelivery.setText(StorefrontCommonData.getTerminology().getHomeDelivery());
        tvSelfPickup = findViewById(R.id.tvSelfPickup);
        tvSelfPickup.setText(StorefrontCommonData.getTerminology().getSelfPickup());

        if (storefrontData != null) {
            if (storefrontData.getSelfPickup() == 1 && storefrontData.getHomeDelivery() == 1) {
                selectedPickupMode = storefrontData.getSelectedPickupMode();
                llSelfPickupDelivery.setVisibility(View.VISIBLE);
                llPickupView.setVisibility(View.VISIBLE);
            } else if (storefrontData.getSelfPickup() == 1) {
                selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
                llSelfPickupDelivery.setVisibility(View.GONE);
                llPickupView.setVisibility(View.GONE);
            } else {
                selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
                llSelfPickupDelivery.setVisibility(View.GONE);
                llPickupView.setVisibility(View.VISIBLE);
            }

        }

        Utils.setOnClickListener(this, llHomeDelivery, llSelfPickup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 4);
        vwShadow.setLayoutParams(layoutParams);

        tvHeading.setText(getStrings(R.string.review) + " " + Utils.getCallTaskAs(true, true));

        llSelectDateTime = findViewById(R.id.llSelectDateTime);
//        rlDateTimeHeader = findViewById(R.id.rlDateTimeHeader);
//        llDateTimeChild = findViewById(R.id.llDateTimeChild);
//        rgInstantSchedule = findViewById(R.id.rgInstantSchedule);
        llStartDate = findViewById(R.id.llStartDate);
        llEndDateSchedule = findViewById(R.id.llEndDateSchedule);
        etStartDateTime = findViewById(R.id.etStartDateTime);
//        llStartEndTimeSchedule = findViewById(R.id.llStartEndTimeSchedule);
//        etStartEndTimeSchedule = findViewById(R.id.etStartEndTimeSchedule);
//        etStartEndTimeSchedule.setVisibility(View.GONE);
        etEndDateTimeSchedule = findViewById(R.id.etEndDateTimeSchedule);
        etEndDateTimeSchedule.setHint(getStrings(R.string.enter) + " " + StorefrontCommonData.getTerminology().getEndTime(false));
        ((MaterialEditText) etEndDateTimeSchedule).setFloatingLabelText(StorefrontCommonData.getTerminology().getEndTime(true));
        tvDateTimeHeader = findViewById(R.id.tvDateTimeHeader);
        tvAddressHeader = findViewById(R.id.tvAddressHeader);
        tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));

        rvCart.setLayoutManager(new LinearLayoutManager(mActivity));
        rvCart.setNestedScrollingEnabled(false);
        rvCart.setAdapter(new CartRecyclerAdapter(CheckOutActivityOld.this, Dependencies.getSelectedProductsArrayList()));
        // tvCartSize.setText(Dependencies.getSelectedProductsArrayList().size() + "");
        tvPickupDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        tvDeliveryDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        Utils.setOnClickListener(this, rlBack, tvProceedToPay, rlCartHeader, tvCartAction, rlDescriptionHeader, tvDescriptionAction,
                rlPickupDetailsHeader, tvPickupDetailsReset, tvPickupDetailsRemove, etPickupDetailsAddress, etPickupDetailsreadyForPickup, etPickupDetailsEndDate, llPickupDetailsCountryCode, tvPickupDetailsAction,
                rlDeliveryDetailsHeader, rlAddressHeader, tvDeliveryDetailsReset, tvDeliveryDetailsRemove, etDeliveryDetailsAddress, etDeliveryDetailsLatestDeliveryByTime, llDeliveryDetailsCountryCode, tvDeliveryDetailsAction,
                rlDateTimeHeader, etStartDateTime, etEndDateTimeSchedule, etStartEndTimeSchedule);
        etDescription.addTextChangedListener(passwordWatcher);
        if (etDescription.getText().toString().isEmpty()) {
            tvDescriptionAction.setText(getStrings(R.string.add));
        } else {
            tvDescriptionAction.setText(getStrings(R.string.clear));
        }
        setDateTime();
        showPreFilledData();
//        if (!(StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(getSubTotal()) <= 0)) {
//            tvCartAction.setVisibility(View.VISIBLE);
//        }
        tvCartAction.setText(Utils.getCurrencySymbol() + getSubTotal() + "");
        setCartSize();
        ibArrow.setRotation(-90);
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


    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                tvDescriptionAction.setText(getStrings(R.string.add));
            } else {
                tvDescriptionAction.setText(getStrings(R.string.clear));
            }
        }
    };

    private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        int i = view.getId();
        if (i == iBack) {
            performBackAction();
        } else if (i == R.id.tvProceedToPay) {
            if (minAmountPrice > Dependencies.getProductListSubtotal()) {
                Utils.snackBar(mActivity, getStrings(R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + getStrings(R.string.minimum_order_amount_is) + " " + Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minAmountPrice));
                return;
            }

            proceedToPayClick();

        } else if (i == R.id.rlCartHeader) {
            if (llCartChild.getVisibility() == View.GONE) {
                llCartChild.setVisibility(View.VISIBLE);
                tvCartAction.setVisibility(View.GONE);
                ibArrow.setRotation(-90);
            } else {
                llCartChild.setVisibility(View.GONE);
                ibArrow.setRotation(90);
                if (!(StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(getSubTotal()) <= 0)) {
                    tvCartAction.setVisibility(View.VISIBLE);
                }
                //  tvCartAction.setText(Utils.getCurrencySymbol() +getSubTotal()+"");
            }

        } else if (i == R.id.tvCartAction) {
//                if (llCartChild.getVisibility() == View.VISIBLE) {
//                    new OptionsDialog.Builder(mActivity).message(getStrings(R.string.sure_clear_cart)).listener(new OptionsDialog.Listener() {
//                        @Override
//                        public void performPositiveAction(int purpose, Bundle backpack) {
//                            Dependencies.getSelectedProductsArrayList().clear();
//                            Dependencies.setSelectedProductsArrayList(new ArrayList<ProductTypeData>());
//                            performBackAction();
//                        }
//
//                        @Override
//                        public void performNegativeAction(int purpose, Bundle backpack) {
//                        }
//                    }).build().show();
//                }
//        } else if (i == R.id.rlDescriptionHeader) {
//            if (llDescriptionChild.getVisibility() == View.GONE) {
//                llDescriptionChild.setVisibility(View.VISIBLE);
//                if (etDescription.getText().toString().isEmpty()) {
//                    tvDescriptionAction.setText(getStrings(R.string.add));
//                    etDescription.requestFocus();
//                } else {
//                    tvDescriptionAction.setText(getStrings(R.string.clear));
//                    etDescription.requestFocus();
//                }
//            } else {
//                llDescriptionChild.setVisibility(View.GONE);
//                if (etDescription.getText().toString().isEmpty()) {
//                    tvDescriptionAction.setText(getStrings(R.string.add));
//                } else {
//                    tvDescriptionAction.setText(getStrings(R.string.clear));
//                }
//            }
//
//        } else if (i == R.id.tvDescriptionAction) {
//            if (etDescription.getText().toString().isEmpty()) {
//                etDescription.requestFocus();
//            } else {
//                etDescription.setText("");
//            }
//            if (llDescriptionChild.getVisibility() == View.GONE) {
//                llDescriptionChild.setVisibility(View.VISIBLE);
//                if (etDescription.getText().toString().isEmpty()) {
//                    tvDescriptionAction.setText(getStrings(R.string.add));
//                    etDescription.requestFocus();
//                } else {
//                    tvDescriptionAction.setText(getStrings(R.string.clear));
//                    etDescription.requestFocus();
//                }
//            } else {
//                llDescriptionChild.setVisibility(View.GONE);
//                if (etDescription.getText().toString().isEmpty()) {
//                    tvDescriptionAction.setText(getStrings(R.string.add));
//                } else {
//                    tvDescriptionAction.setText(getStrings(R.string.clear));
//                }
//            }
//
//            //PickupDetails
//        } else if (i == R.id.rlPickupDetailsHeader) {
//            if (llPickupDetailsChild.getVisibility() == View.GONE) {
//                llPickupDetailsChild.setVisibility(View.VISIBLE);
//                ibArrowPickupDetails.setRotation(-90);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llPickupResetRemove.setVisibility(View.VISIBLE);
//                    tvPickupDetailsAction.setVisibility(View.GONE);
//                    isPickupAdded = true;
//                }
//                etPickupDetailsName.requestFocus();
//            } else {
//                llPickupDetailsChild.setVisibility(View.GONE);
//                ibArrowPickupDetails.setRotation(90);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    if (isPickupAdded) {
//                        llPickupResetRemove.setVisibility(View.VISIBLE);
//                        tvPickupDetailsAction.setVisibility(View.GONE);
//                    } else {
//                        llPickupResetRemove.setVisibility(View.GONE);
//                        tvPickupDetailsAction.setVisibility(View.VISIBLE);
//                    }
//                }
//            }

//        } else if (i == R.id.rlAddressHeader) {
//            if (llAddressDetailsChild.getVisibility() == View.GONE) {
//                llAddressDetailsChild.setVisibility(View.VISIBLE);
//            } else {
//                llAddressDetailsChild.setVisibility(View.GONE);
//            }
//
//        } else if (i == R.id.rlDateTimeHeader) {
//            if (llDateTimeChild.getVisibility() == View.GONE) {
//                llDateTimeChild.setVisibility(View.VISIBLE);
//            } else {
//                llDateTimeChild.setVisibility(View.GONE);
//            }
//        } else if (i == R.id.tvPickupDetailsReset) {
//            etPickupDetailsAddress.setText("");
//            etPickupDetailsName.setText("");
//            tvPickupDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
//            etPickupDetailsPhoneNumber.setText("");
//            etPickupDetailsEmail.setText("");
//            etPickupDetailsreadyForPickup.setText("");
//            etPickupDetailsEndDate.setText("");
//
//        } else if (i == R.id.tvPickupDetailsRemove) {
//            removePickUpDetails();
//
//        } else if (i == R.id.tvPickupDetailsAction) {
//            if (llPickupDetailsChild.getVisibility() == View.GONE) {
//                llPickupDetailsChild.setVisibility(View.VISIBLE);
//                ibArrowPickupDetails.setRotation(-90);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llPickupResetRemove.setVisibility(View.VISIBLE);
//                    tvPickupDetailsAction.setVisibility(View.GONE);
//                }
//                etPickupDetailsName.requestFocus();
//            } else {
//                llPickupDetailsChild.setVisibility(View.GONE);
//                ibArrowPickupDetails.setRotation(90);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llPickupResetRemove.setVisibility(View.GONE);
//                    tvPickupDetailsAction.setVisibility(View.VISIBLE);
//                }
//            }
//            isPickupAdded = true;
//
//        } else if (i == R.id.etPickupDetailsAddress) {
//            if (!Utils.internetCheck(this)) {
//                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
//                return;
//            }
//            if (Dependencies.isDemoRunning()) {
//                gotoMapActivity();
//            } else {
//
//                if (isStaticAddressFlow()) {
//                    goToStaticAddressActivity();
//                } else {
//                    gotoFavLocationActivity();
//                }
//
//
//               /* if ((Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
//                        UIManager.isStaticAddressEnabledForAdmin()) ||
//                        (!Dependencies.getSelectedProductsArrayList().get(0).isDeliveryByAdmin() &&
//                                Dependencies.getSelectedProductsArrayList().get(0).isStaticAddressEnabled()))
//                    goToStaticAddressActivity();
//                else
//                    gotoFavLocationActivity();
//                */
//
//                /* goToStaticAddressActivity();*/
//            }
//            isPickup = true;
//
//        } else if (i == R.id.etPickupDetailsreadyForPickup || i == R.id.etStartDateTime) {
//            isStartDate = true;
//
//            if (isStartEndTimeEnabled.equals(1) && scheduledTask.equals(0) && !(Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
//                openDatePicker();
//            } else {
//                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
//                intent.putExtra(IS_START_TIME, isStartDate);
//                intent.putExtra(SELECTED_DATE, !isStartDate ? etStartDateTime.getText().toString() : etEndDateTimeSchedule.getText().toString());
//                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
//            }
//
//        } else if (i == R.id.etPickupDetailsEndDate || i == R.id.etEndDateTimeSchedule) {
//            isStartDate = false;
//
//            if (isStartEndTimeEnabled.equals(1) && scheduledTask.equals(0) && !(Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
//                openDatePicker();
//            } else {
//                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
//                intent.putExtra(IS_START_TIME, isStartDate);
//                intent.putExtra(SELECTED_DATE, !isStartDate ? etStartDateTime.getText().toString() : etEndDateTimeSchedule.getText().toString());
//                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
//            }
//
//        } else if (i == R.id.etStartEndTimeSchedule) {
//            isStartDate = true;
//            Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
//            intent.putExtra(IS_START_TIME, isStartDate);
//            intent.putExtra(SELECTED_DATE, !isStartDate ? etStartDateTime.getText().toString() : etEndDateTimeSchedule.getText().toString());
//            startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
//        } else if (i == R.id.llPickupDetailsCountryCode) {
//            isPickup = true;
//            Utils.hideSoftKeyboard(this, etPickupDetailsPhoneNumber);
////            if (countryPicker == null) {
////            countryPicker = CountryPicker.newInstance(getStrings(R.string.select_country));
////            countryPicker.setListener(this);
//////            }
////            countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
//
//            CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
//                @Override
//                public void onCountrySelected(Country country) {
//                    if (isPickup) {
//                        tvPickupDetailsCountryCode.setText(country.getCountryCode());
//                        etPickupDetailsPhoneNumber.requestFocus();
//                    } else {
//                        tvDeliveryDetailsCountryCode.setText(country.getCountryCode());
//                        etDeliveryDetailsPhoneNumber.requestFocus();
//                    }
//                    CountrySelectionDailog.dismissDialog();
//                }
//            }).show();
//
//            //DeliveryDetails
//        } else if (i == R.id.rlDeliveryDetailsHeader) {
//            if (llDeliveryDetailsChild.getVisibility() == View.GONE) {
//                llDeliveryDetailsChild.setVisibility(View.VISIBLE);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llDeliveryResetRemove.setVisibility(View.VISIBLE);
//                    tvDeliveryDetailsAction.setVisibility(View.GONE);
//                    isDeliveryAdded = true;
//                }
//                etDeliveryDetailsName.requestFocus();
//            } else {
//                llDeliveryDetailsChild.setVisibility(View.GONE);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    if (isDeliveryAdded) {
//                        llDeliveryResetRemove.setVisibility(View.VISIBLE);
//                        tvDeliveryDetailsAction.setVisibility(View.GONE);
//                    } else {
//                        llDeliveryResetRemove.setVisibility(View.GONE);
//                        tvDeliveryDetailsAction.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//        } else if (i == R.id.tvDeliveryDetailsReset) {
//            etDeliveryDetailsAddress.setText("");
//            etDeliveryDetailsName.setText("");
//
//            tvDeliveryDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
//            etDeliveryDetailsPhoneNumber.setText("");
//            etDeliveryDetailsEmail.setText("");
//            etDeliveryDetailsLatestDeliveryByTime.setText("");
//
//        } else if (i == R.id.tvDeliveryDetailsRemove) {
//            removeDeliveryDetails();
//
//        } else if (i == R.id.tvDeliveryDetailsAction) {
//            if (llDeliveryDetailsChild.getVisibility() == View.GONE) {
//                llDeliveryDetailsChild.setVisibility(View.VISIBLE);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llDeliveryResetRemove.setVisibility(View.VISIBLE);
//                    tvDeliveryDetailsAction.setVisibility(View.GONE);
//                }
//                etDeliveryDetailsName.requestFocus();
//            } else {
//                llDeliveryDetailsChild.setVisibility(View.GONE);
//                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
//                    llDeliveryResetRemove.setVisibility(View.GONE);
//                    tvDeliveryDetailsAction.setVisibility(View.VISIBLE);
//                }
//            }
//            isDeliveryAdded = true;
//
//        } else if (i == R.id.etDeliveryDetailsAddress) {
//            if (!Utils.internetCheck(this)) {
//                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
//                return;
//            }
//            isPickup = false;
//            if (Dependencies.isDemoRunning()) {
//                if (!Utils.preventMultipleClicks()) {
//                    return;
//                }
//                gotoMapActivity();
//            } else {
//                gotoFavLocationActivity();
//
//            }
//
//        } else if (i == R.id.etDeliveryDetailsLatestDeliveryByTime) {
//            isStartDate = false;
//            openDatePicker();
//
//        } else if (i == R.id.llDeliveryDetailsCountryCode) {
//            isPickup = false;
//            Utils.hideSoftKeyboard(this, etDeliveryDetailsPhoneNumber);
////            if (countryPicker == null) {
////            countryPicker = CountryPicker.newInstance(getStrings(R.string.select_country));
////            countryPicker.setListener(this);
//////            }
////            countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
//
//            CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
//                @Override
//                public void onCountrySelected(Country country) {
//                    if (isPickup) {
//                        tvPickupDetailsCountryCode.setText(country.getCountryCode());
//                        etPickupDetailsPhoneNumber.requestFocus();
//                    } else {
//                        tvDeliveryDetailsCountryCode.setText(country.getCountryCode());
//                        etDeliveryDetailsPhoneNumber.requestFocus();
//                    }
//                    CountrySelectionDailog.dismissDialog();
//                }
//            }).show();
//
//        } else if (i == R.id.llHomeDelivery) {
//            selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
//            resetSelfPickupDeliveryAssets();
//        } else if (i == R.id.llSelfPickup) {
//            selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
//            resetSelfPickupDeliveryAssets();
        }
    }

    public void removeDeliveryDetails() {
        //Location location = LocationUtils.getLastLocation(this);
        // LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        // etDeliveryDetailsAddress.setText(getAddressFromLatLng(mActivity, currentLatlng));
        deliveryLatitude = 0.0;
        deliveryLongitude = 0.0;
        etDeliveryDetailsAddress.setText("");

        endYear = 0;
        endMonth = 0;
        endDay = 0;
        endHour = 0;
        endMinute = -1;

        tvDeliveryDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 0 || StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 1) {
            etDeliveryDetailsName.setText("");
            etDeliveryDetailsPhoneNumber.setText("");
            etDeliveryDetailsEmail.setText("");
        }
        etDeliveryDetailsLatestDeliveryByTime.setText("");
        llDeliveryDetailsChild.setVisibility(View.GONE);
        llDeliveryResetRemove.setVisibility(View.GONE);
        tvDeliveryDetailsAction.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null && Dependencies.getDeliveryOptions(this) != null) {
            for (int i = 0; i < StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().size(); i++) {
                Object data = Dependencies.getDeliveryOptions(this).getItems().get(i).getData();
                StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().get(i).setData(data);
            }
        }
        renderCustomFieldsDelivery();
        setDateTime();
        isDeliveryAdded = false;
    }

    public void removePickUpDetails() {
        Location location = LocationUtils.getLastLocation(this);
        executeSetAddress();
        pickUpLatitude = location.getLatitude();
        pickUpLongitude = location.getLongitude();

        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = -1;

        etPickupDetailsAddress.setText("");
        tvPickupDetailsCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 0 || StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 1) {
            etPickupDetailsName.setText("");
            etPickupDetailsPhoneNumber.setText("");
            etPickupDetailsEmail.setText("");
        }
        etPickupDetailsreadyForPickup.setText("");
        etPickupDetailsEndDate.setText("");
        llPickupDetailsChild.setVisibility(View.GONE);
        ibArrowPickupDetails.setRotation(90);
        llPickupResetRemove.setVisibility(View.GONE);
        tvPickupDetailsAction.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null && Dependencies.getUserOptions(this) != null) {
            for (int i = 0; i < StorefrontCommonData.getUserData().getData().getUserOptions().getItems().size(); i++) {
                Object data = Dependencies.getUserOptions(this).getItems().get(i).getData();
                StorefrontCommonData.getUserData().getData().getUserOptions().getItems().get(i).setData(data);
            }
        }
        renderCustomFieldsPickup();
        setDateTime();
        isPickupAdded = false;
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    public void performBackAction() {
        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() ==
                    Constants.ProductAddedInCart.SINGLE_PRODUCT) {
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

    public void setSubTotal(double subTotal) {
        tvTotalPriceCart.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(subTotal));
        tvSubTotal.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(subTotal));
        tvCartAction.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(subTotal));
        setCartSize();

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && subTotal <= 0) {
            rlSubtotalPrice.setVisibility(View.GONE);
            tvCartAction.setVisibility(View.GONE);
        } else {
            rlSubtotalPrice.setVisibility(View.VISIBLE);
        }

    }

    public void setCartSize() {
        tvCartSize.setText(Dependencies.getCartSize() + "");
        tvNoOfItems.setText("(" + Dependencies.getCartSize() + " " + (Dependencies.getCartSize() > 1 ? getStrings(R.string.items) : getStrings(R.string.item)) + ") :");
    }

    @Override
    protected void onResume() {
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void renderCustomFields() {
        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
            renderCustomFieldsPickup();
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
            renderCustomFieldsDelivery();
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
            renderCustomFieldsPickup();
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
            renderCustomFieldsPickup();
        } else {
            if (StorefrontCommonData.getUserData().getData().getUserOptions() != null)
                renderCustomFieldsPickup();
            if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null)
                renderCustomFieldsDelivery();
        }
    }

    private void renderCustomFieldsPickup() {
        UserOptions userOptions = StorefrontCommonData.getUserData().getData().getUserOptions();
        llPickupCustomFields.removeAllViews();
        if (userOptions == null) {
            return;
        }
        for (Item item : userOptions.getItems()) {
            if (item.getLabel().equalsIgnoreCase("task_details")) {
                item.setData(getJobDescription());
            }
            if (item.isShow()) {
                View view = null;
                switch (item.getDataType()) {
                    case Keys.DataType.NUMBER:
                    case Keys.DataType.EMAIL:
                    case Keys.DataType.TELEPHONE:
                    case Keys.DataType.TEXT:
                    case Keys.DataType.URL:
                    case Keys.DataType.DATE:
                    case Keys.DataType.DATE_FUTURE:
                    case Keys.DataType.DATE_PAST:
                    case Keys.DataType.DATE_TODAY:
                    case Keys.DataType.DATETIME:
                    case Keys.DataType.DATETIME_FUTURE:
                    case Keys.DataType.DATETIME_PAST:
                        view = new CustomFieldTextViewPickup(this).render(item);
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                        view = new CustomFieldCheckboxPickup(this).render(item);
                        break;
                    case Keys.DataType.IMAGE:
                        view = new CustomFieldImagePickup(this).render(item);
                        break;
                }
                if (view != null) {
                    llPickupCustomFields.addView(view);
                }
            }
        }
    }

    private void renderCustomFieldsDelivery() {
        UserOptions userOptions = StorefrontCommonData.getUserData().getData().getDeliveryOptions();
        llDeliveryCustomFields.removeAllViews();
        if (userOptions == null) {
            return;
        }
        for (Item item : userOptions.getItems()) {
            if (item.getLabel().equalsIgnoreCase("task_details")) {
                item.setData(getJobDescription());
            }
            if (item.isShow()) {
                View view = null;
                switch (item.getDataType()) {
                    case Keys.DataType.NUMBER:
                    case Keys.DataType.EMAIL:
                    case Keys.DataType.TELEPHONE:
                    case Keys.DataType.TEXT:
                    case Keys.DataType.URL:
                    case Keys.DataType.DATE:
                    case Keys.DataType.DATE_FUTURE:
                    case Keys.DataType.DATE_PAST:
                    case Keys.DataType.DATE_TODAY:
                    case Keys.DataType.DATETIME:
                    case Keys.DataType.DATETIME_FUTURE:
                    case Keys.DataType.DATETIME_PAST:
                        view = new CustomFieldTextViewDelivery(this).render(item);
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                        view = new CustomFieldCheckboxDelivery(this).render(item);
                        break;
                    case Keys.DataType.IMAGE:
                        view = new CustomFieldImageDelivery(this).render(item);
                        break;
                }
                if (view != null) {
                    llDeliveryCustomFields.addView(view);
                }
            }
        }
    }

    private String getJobDescription() {
        return Dependencies.getProductListDescription();
    }

    /**
     * Method to set the Listener for a CustomFieldImageView
     *
     * @param item
     */
    public void setCustomFieldPositionPickup(Item item) {
        this.customFieldPositionPickup = StorefrontCommonData.getUserData().getData().getUserOptions().getItems().indexOf(item);
    }

    public void setCustomFieldPositionDelivery(Item item) {
        this.customFieldPositionDelivery = StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().indexOf(item);
    }
//    public void setCustomFieldPosition(Item item) {
//        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
//            this.customFieldPositionPickup = StorefrontCommonData.getUserData().getData().getUserOptions().getItems().indexOf(item);
//        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
//            this.customFieldPositionDelivery = StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().indexOf(item);
//        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
//            this.customFieldPositionPickup = StorefrontCommonData.getUserData().getData().getUserOptions().getItems().indexOf(item);
//        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
//            this.customFieldPositionPickup = StorefrontCommonData.getUserData().getData().getUserOptions().getItems().indexOf(item);
//        } else {
//            if (StorefrontCommonData.getUserData().getData().getUserOptions() != null)
//                this.customFieldPositionPickup = StorefrontCommonData.getUserData().getData().getUserOptions().getItems().indexOf(item);
//            if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null)
//                this.customFieldPositionDelivery = StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems().indexOf(item);
//        }
//    }

    /**
     * Method to set the Listener for a CustomFieldImageView
     *
     * @param barcodeRequestCode
     */
    public void setBarcodeRequestCode(int barcodeRequestCode) {
        this.barcodeRequestCode = barcodeRequestCode;
    }

    /**
     * Method to ask the User to logout
     *
     * @param message
     */
    public void scanBarcodePopup(String message) {
        // Utils.hideSoftKeyboard(this, mStartTimeView);
        new OptionsDialog.Builder(this).message(message).positiveButton(getStrings(R.string.scan)).negativeButton(getStrings(R.string.manually))
                .listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        IntentIntegrator integrator = new IntentIntegrator(mActivity);
                        integrator.setOrientationLocked(false);
                        integrator.initiateScan();
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                        if (UIManager.isPickup) {
                            CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                            if (cfBarcode != null)
                                cfBarcode.addBarcodeManually();
                        } else {
                            CustomFieldTextViewDelivery cfBarcode = getCustomFieldBarcodeDelivery();
                            if (cfBarcode != null)
                                cfBarcode.addBarcodeManually();
                        }
//                        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
//                            CustomFieldTextView cfBarcode = getCustomFieldBarcodePickup();
//                            if (cfBarcode != null)
//                                cfBarcode.addBarcodeManually();
//                        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
//                            CustomFieldTextViewDelivery cfBarcode = getCustomFieldBarcodeDelivery();
//                            if (cfBarcode != null)
//                                cfBarcode.addBarcodeManually();
//                        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
//                            CustomFieldTextView cfBarcode = getCustomFieldBarcodePickup();
//                            if (cfBarcode != null)
//                                cfBarcode.addBarcodeManually();
//                        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
//                            CustomFieldTextView cfBarcode = getCustomFieldBarcodePickup();
//                            if (cfBarcode != null)
//                                cfBarcode.addBarcodeManually();
//                        } else {
//                            if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
//                                CustomFieldTextView cfBarcodePickup = getCustomFieldBarcodePickup();
//                                if (cfBarcodePickup != null)
//                                    cfBarcodePickup.addBarcodeManually();
//                            }
//                            if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null) {
//                                CustomFieldTextViewDelivery cfBarcodeDelivery = getCustomFieldBarcodeDelivery();
//                                if (cfBarcodeDelivery != null)
//                                    cfBarcodeDelivery.addBarcodeManually();
//                            }
//                        }
                    }
                }).build().show();
    }

    /**
     * Method to get the CustomFieldBarcodePickup via Listeners
     *
     * @return
     */
    private CustomFieldTextViewPickup getCustomFieldBarcodePickup() {
        List<Item> customFieldsList = StorefrontCommonData.getUserData().getData().getUserOptions().getItems();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPositionPickup).getView();
        return customFieldView instanceof CustomFieldTextViewPickup ? (CustomFieldTextViewPickup) customFieldView : null;
    }

    /**
     * Method to get the CustomFieldBarcodeDelivery via Listeners
     *
     * @return
     */
    private CustomFieldTextViewDelivery getCustomFieldBarcodeDelivery() {
        List<Item> customFieldsList = StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPositionDelivery).getView();
        return customFieldView instanceof CustomFieldTextViewDelivery ? (CustomFieldTextViewDelivery) customFieldView : null;
    }

    /**
     * Method to get the CustomFieldImageViewPickup via Listeners
     *
     * @return
     */
    private CustomFieldImagePickup getCustomFieldImageViewPickup() {
        List<Item> customFieldsList = StorefrontCommonData.getUserData().getData().getUserOptions().getItems();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPositionPickup).getView();
        return customFieldView instanceof CustomFieldImagePickup ? (CustomFieldImagePickup) customFieldView : null;
    }

    /**
     * Method to get the CustomFieldImageView via Listeners
     *
     * @return
     */
    private CustomFieldImageDelivery getCustomFieldImageViewDelivery() {
        List<Item> customFieldsList = StorefrontCommonData.getUserData().getData().getDeliveryOptions().getItems();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPositionDelivery).getView();
        return customFieldView instanceof CustomFieldImageDelivery ? (CustomFieldImageDelivery) customFieldView : null;
    }

    public void deleteCustomFieldImagePickup(int position) {
        CustomFieldImagePickup cFIVDeleteImage = getCustomFieldImageViewPickup();
        if (cFIVDeleteImage != null) {
            cFIVDeleteImage.deleteImage(position);
        }
    }

    public void deleteCustomFieldImageDelivery(int position) {
        CustomFieldImageDelivery cFIVDeleteImage = getCustomFieldImageViewDelivery();
        if (cFIVDeleteImage != null) {
            cFIVDeleteImage.deleteImage(position);
        }
    }

    public void showPreFilledData() {
        if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() != 0) {
            if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 1) {
                etPickupDetailsName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());


                setDefaultPhoneDetails(tvPickupDetailsCountryCode, etPickupDetailsPhoneNumber);
//                etPickupDetailsPhoneNumber.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().substring(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().indexOf(" ") + 1));

                etPickupDetailsEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                    etDeliveryDetailsName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
                    setDefaultPhoneDetails(tvDeliveryDetailsCountryCode, etDeliveryDetailsPhoneNumber);
//                    etDeliveryDetailsPhoneNumber.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().substring(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().indexOf(" ") + 1));
                    etDeliveryDetailsEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
                }
            }
            if (StorefrontCommonData.getFormSettings().getIsShowPrefilledData() == 2) {
                etPickupDetailsName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
                setDefaultPhoneDetails(tvPickupDetailsCountryCode, etPickupDetailsPhoneNumber);
//                etPickupDetailsPhoneNumber.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().substring(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().indexOf(" ") + 1));
                etPickupDetailsEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
                etPickupDetailsName.setVisibility(View.GONE);
                etPickupDetailsPhoneNumber.setVisibility(View.GONE);
                etPickupDetailsEmail.setVisibility(View.GONE);
                vwPickupDetailsName.setVisibility(View.GONE);
                vwPickupDetailsPhone.setVisibility(View.GONE);
                vwPickupDetailsEmail.setVisibility(View.GONE);
                tvPickupNameText.setVisibility(View.GONE);
                tvPickupPhoneText.setVisibility(View.GONE);
                tvPickupEmailText.setVisibility(View.GONE);
                llPickupDetailsCountryCode.setVisibility(View.GONE);
                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                    etDeliveryDetailsName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
                    setDefaultPhoneDetails(tvDeliveryDetailsCountryCode, etDeliveryDetailsPhoneNumber);
//                    etDeliveryDetailsPhoneNumber.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().substring(StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().indexOf(" ") + 1));
                    etDeliveryDetailsEmail.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
                    etDeliveryDetailsName.setVisibility(View.GONE);
                    etDeliveryDetailsPhoneNumber.setVisibility(View.GONE);
                    etDeliveryDetailsEmail.setVisibility(View.GONE);
                    vwDeliveryDetailsName.setVisibility(View.GONE);
                    vwDeliveryDetailsphone.setVisibility(View.GONE);
                    vwDeliveryDetailsEmail.setVisibility(View.GONE);
                    tvDeliveryNameText.setVisibility(View.GONE);
                    tvDeliveryPhoneText.setVisibility(View.GONE);
                    tvDeliveryEmailText.setVisibility(View.GONE);
                    llDeliveryDetailsCountryCode.setVisibility(View.GONE);
                }
            }
        }
    }

    public void gotoFavLocationActivity() {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_ADDRESS);

        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_ADDRESS);

//      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Date d = new Date(), endDate = new Date();
                        d.setTime(data.getLongExtra("date", -1));
                        endDate.setTime(data.getLongExtra("endDate", -1));
//                        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1) {
//
//                            etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(d, Constants.DateFormat.END_USER_DATE_FORMAT));
//                            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(d, Constants.DateFormat.END_USER_DATE_FORMAT));
//                            etPickupDetailsEndDate.setText(DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.END_USER_DATE_FORMAT));
//                            etEndDateTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.END_USER_DATE_FORMAT));
//
//                            etStartEndTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(d, Constants.DateFormat.END_USER_DATE_FORMAT) + " - " + DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.END_USER_DATE_FORMAT));
//
//                        } else {
                        if (isStartDate) {
                            etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                        } else {
                            etPickupDetailsEndDate.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                            etEndDateTimeSchedule.setText(DateUtils.getInstance().getFormattedDate(d, UIManager.getDateTimeFormat()));
                        }
//                        }
                    }
                }
                break;
            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (isPickup) {
                        etPickupDetailsAddress.setText(data.getStringExtra("address"));
                        pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                        pickUpLongitude = data.getDoubleExtra("longitude", 0.0);
                    } else {
                        etDeliveryDetailsAddress.setText(data.getStringExtra("address"));
                        deliveryLatitude = data.getDoubleExtra("latitude", 0.0);
                        deliveryLongitude = data.getDoubleExtra("longitude", 0.0);
                    }
                }
                break;
            case Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (isPickup) {
                        etPickupDetailsAddress.setText(data.getStringExtra("address"));
                        pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                        pickUpLongitude = data.getDoubleExtra("longitude", 0.0);
                    } else {
                        etDeliveryDetailsAddress.setText(data.getStringExtra("address"));
                        deliveryLatitude = data.getDoubleExtra("latitude", 0.0);
                        deliveryLongitude = data.getDoubleExtra("longitude", 0.0);
                    }
                }
                break;
            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == RESULT_OK) {
                    executeSetAddress();
                    if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                        deliveryLatitude = getCurrentLocation().latitude;
                        deliveryLongitude = getCurrentLocation().longitude;
                    }
                    pickUpLatitude = getCurrentLocation().latitude;
                    pickUpLongitude = getCurrentLocation().longitude;
                }
                break;
           /* case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    if (isPickup) {
                        etPickupDetailsAddress.setText(place.getAddress());
                        pickUpLatitude = place.getLatLng().latitude;
                        pickUpLongitude = place.getLatLng().longitude;
                    } else {
                        etDeliveryDetailsAddress.setText(place.getAddress());
                        deliveryLatitude = place.getLatLng().latitude;
                        deliveryLongitude = place.getLatLng().longitude;
                    }
                    //updateMap(place.getLatLng(), "" + place.getAddress());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i("Error", "" + status.getStatusMessage());
                    Utils.snackBar(this, status.getStatusMessage());
                    // TODO: Handle the error.
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;*/
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (UIManager.isPickup) {
                        CustomFieldImagePickup cFIVCamera = getCustomFieldImageViewPickup();
                        if (cFIVCamera != null) {
                            cFIVCamera.compressAndSaveImageBitmap();
                        }
                    } else {
                        CustomFieldImageDelivery cFIVCamera = getCustomFieldImageViewDelivery();
                        if (cFIVCamera != null) {
                            cFIVCamera.compressAndSaveImageBitmap();
                        }
                    }
//                    if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
//                        CustomFieldImagePickup cFIVCamera = getCustomFieldImageViewPickup();
//                        if (cFIVCamera != null) {
//                            cFIVCamera.compressAndSaveImageBitmap();
//                        }
//                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
//                        CustomFieldImageDelivery cFIVCamera = getCustomFieldImageViewDelivery();
//                        if (cFIVCamera != null) {
//                            cFIVCamera.compressAndSaveImageBitmap();
//                        }
//                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
//                        CustomFieldImagePickup cFIVCamera = getCustomFieldImageViewPickup();
//                        if (cFIVCamera != null) {
//                            cFIVCamera.compressAndSaveImageBitmap();
//                        }
//                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
//                        CustomFieldImagePickup cFIVCamera = getCustomFieldImageViewPickup();
//                        if (cFIVCamera != null) {
//                            cFIVCamera.compressAndSaveImageBitmap();
//                        }
//                    } else {
//                        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
//                            CustomFieldImagePickup cFIVCamera = getCustomFieldImageViewPickup();
//                            if (cFIVCamera != null) {
//                                cFIVCamera.compressAndSaveImageBitmap();
//                            }
//                        }
//                        if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null) {
//                            CustomFieldImageDelivery cFIVCameraDelivery = getCustomFieldImageViewDelivery();
//                            if (cFIVCameraDelivery != null) {
//                                cFIVCameraDelivery.compressAndSaveImageBitmap();
//                            }
//                        }
//                    }
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

            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (UIManager.isPickup) {
                        CustomFieldImagePickup cFIMVGallery = getCustomFieldImageViewPickup();
                        if (cFIMVGallery != null) {
                            try {
                                cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                                cFIMVGallery.compressAndSaveImageBitmap();
                            } catch (IOException e) {

                                Utils.printStackTrace(e);
                                Utils.toast(this, getStrings(R.string.could_not_read_image));
                            }
                        }
                    } else {
                        CustomFieldImageDelivery cFIMVGallery = getCustomFieldImageViewDelivery();
                        if (cFIMVGallery != null) {
                            try {
                                cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                                cFIMVGallery.compressAndSaveImageBitmap();
                            } catch (IOException e) {

                                Utils.printStackTrace(e);
                                Utils.toast(this, getStrings(R.string.could_not_read_image));
                            }
                        }
                    }
                }
                break;
            case IntentIntegrator.REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this, getStrings(R.string.cancelled), Toast.LENGTH_LONG).show();
                    } else {
                        switch (barcodeRequestCode) {
                            case Codes.Request.OPEN_SCANNER_CUSTOMFIELD_BARCODE:
                                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
                                    CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                                    if (cfBarcode != null) {
                                        cfBarcode.addBarcode(result.getContents());
                                    }
                                } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                                    CustomFieldTextViewDelivery cfBarcode = getCustomFieldBarcodeDelivery();
                                    if (cfBarcode != null) {
                                        cfBarcode.addBarcode(result.getContents());
                                    }
                                } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
                                    CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                                    if (cfBarcode != null) {
                                        cfBarcode.addBarcode(result.getContents());
                                    }
                                } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
                                    CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                                    if (cfBarcode != null) {
                                        cfBarcode.addBarcode(result.getContents());
                                    }
                                } else {
                                    if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
                                        CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                                        if (cfBarcode != null) {
                                            cfBarcode.addBarcode(result.getContents());
                                        }
                                    }
                                    if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null) {
                                        CustomFieldTextViewDelivery cfBarcodeDelivery = getCustomFieldBarcodeDelivery();
                                        if (cfBarcodeDelivery != null) {
                                            cfBarcodeDelivery.addBarcode(result.getContents());
                                        }
                                    }
                                }
                                break;
                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    private void setLayoutType() {

        llCartView.setVisibility(View.VISIBLE);
        switch (StorefrontCommonData.getFormSettings().getWorkFlow()) {
            case Constants.LayoutType.PICKUP:
                if (llPickupView.getVisibility() == View.VISIBLE)
                    llPickupView.setVisibility(View.VISIBLE);
                llDeliveryView.setVisibility(View.GONE);
                renderCustomFieldsPickup();
                llEndDate.setVisibility(View.GONE);
                tvStartDateText.setText(getStrings(R.string.ready_for_pickup));
//                llPickupDetailsChild.setVisibility(View.VISIBLE);
                llPickupResetRemove.setVisibility(View.GONE);
                tvPickupDetailsAction.setVisibility(View.GONE);
                tvDescriptionAction.setVisibility(View.GONE);
                break;
            case Constants.LayoutType.DELIVERY:
                llPickupView.setVisibility(View.GONE);
                llDeliveryView.setVisibility(View.VISIBLE);
                renderCustomFieldsDelivery();
                llDeliveryDetailsChild.setVisibility(View.VISIBLE);
                llDeliveryResetRemove.setVisibility(View.GONE);
                tvDeliveryDetailsAction.setVisibility(View.GONE);
                tvDescriptionAction.setVisibility(View.GONE);
                break;
            case Constants.LayoutType.APPOINTMENT:
                if (llPickupView.getVisibility() == View.VISIBLE)
                    llPickupView.setVisibility(View.VISIBLE);
                llDeliveryView.setVisibility(View.GONE);
                tvPickupDetailsHeader.setText(getStrings(R.string.appointment_details));
                tvPickupDetailsHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment, 0, 0, 0);
                renderCustomFieldsPickup();
                llEndDate.setVisibility(View.VISIBLE);
                tvStartDateText.setText(getStrings(R.string.start_date));
//                llPickupDetailsChild.setVisibility(View.VISIBLE);
                llPickupResetRemove.setVisibility(View.GONE);
                tvPickupDetailsAction.setVisibility(View.GONE);
                tvDescriptionAction.setVisibility(View.GONE);
            case Constants.LayoutType.FOS:
                if (llPickupView.getVisibility() == View.VISIBLE)
                    llPickupView.setVisibility(View.VISIBLE);
                llDeliveryView.setVisibility(View.GONE);
                tvPickupDetailsHeader.setText(getStrings(R.string.appointment_details));
                tvPickupDetailsHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_appointment, 0, 0, 0);
                renderCustomFieldsPickup();
                llEndDate.setVisibility(View.VISIBLE);
                tvStartDateText.setText(getStrings(R.string.start_date));
//                llPickupDetailsChild.setVisibility(View.VISIBLE);
                llPickupResetRemove.setVisibility(View.GONE);
                tvPickupDetailsAction.setVisibility(View.GONE);
                tvDescriptionAction.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void showPaymentSlider() {
        valueEnabledPayment = PaymentMethodsClass.getEnabledPaymentMethod();
        paymentMethodEnabled = PaymentMethodsClass.isPaymentEnabled();
    }

    public void proceedToPayClick() {
        if (!Utils.internetCheck(this)) {
            new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        if (isValidTaskDetails()) {
            createTask();
        }
    }

    private boolean isValidTaskDetails() {
        String name = etPickupDetailsName.getText().toString().trim();
        String email = StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail().toString().trim();
        String phone = etPickupDetailsPhoneNumber.getText().toString();
//        String phone = tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString();
        String startTime = etPickupDetailsreadyForPickup.getText().toString();
        String endTime = etPickupDetailsEndDate.getText().toString();
        String address = etPickupDetailsAddress.getText().toString();
        String delName = etDeliveryDetailsName.getText().toString().trim();
        String delEmail = etDeliveryDetailsEmail.getText().toString().trim();
        String delPhone = etDeliveryDetailsPhoneNumber.getText().toString();
//        String delPhone = tvDeliveryDetailsCountryCode.getText().toString() + etDeliveryDetailsPhoneNumber.getText().toString();
        String delStartTime = etDeliveryDetailsLatestDeliveryByTime.getText().toString();
        String delAddress = etDeliveryDetailsAddress.getText().toString();

        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
            if (StorefrontCommonData.getFormSettings().getForcePickupDelivery() == 1) {
                // Store values at the time of the login attempt.
                // Check for a valid password, if the user entered one.
                if (!isPickupAdded && !isDeliveryAdded) {
                    Utils.snackBar(this, getStrings(R.string.please_add_pickup_delivery_details));
                    return false;
                }

                if ((isPickupAdded && !isDeliveryAdded) || (!isPickupAdded && isDeliveryAdded)) {
                    Utils.snackBar(this, getStrings(R.string.please_enter_both_pickup_delivery_details));
                    return false;
                }

                if (isDeliveryAdded && isPickupAdded) {
                    if (TextUtils.isEmpty(name)) {
                        // gotoFavLocationActivity();
                        // isPickup = true;
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        return false;
                    }
                    if (TextUtils.isEmpty(address)) {
                        // gotoFavLocationActivity();
                        // isPickup = true;
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return false;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_pickup_contact));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(phone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_phone));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }

                    if (TextUtils.isEmpty(email)) {
                        etPickupDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(email)) {
//                    if (!TextUtils.isEmpty(email) && !Utils.isEmailValid(email)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_email));
                        etPickupDetailsEmail.requestFocus();
                        return false;
                    }

                    if (TextUtils.isEmpty(startTime)) {
                        etPickupDetailsreadyForPickup.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.pickuo_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(pickupDetails != null ? R.string.invalid_delivery_time : R.string.invalid_pickup_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = true;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }


                    /////

                    if (TextUtils.isEmpty(delName)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        // gotoFavLocationActivity();
                        // isPickup = false;
                        return false;
                    }
                    if (TextUtils.isEmpty(delAddress)) {

                        Utils.snackBar(this, getStrings(R.string.please_enter_delivery_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        // gotoFavLocationActivity();
                        // isPickup = false;
                        return false;
                    }
                    if (TextUtils.isEmpty(delPhone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_delivery_contact));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(phone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_phone));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delEmail)) {
                        etDeliveryDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(delEmail)) {
//                    if (!TextUtils.isEmpty(delEmail) && !Utils.isEmailValid(delEmail)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_email));
                        etDeliveryDetailsEmail.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delStartTime)) {
                        etDeliveryDetailsLatestDeliveryByTime.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.delivery_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(pickupDetails != null ? R.string.invalid_delivery_time : R.string.invalid_pickup_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = false;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }

                }
            } else {
                if (isPickupAdded && !isDeliveryAdded) {
                    if (TextUtils.isEmpty(name)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        // gotoFavLocationActivity();
                        //isPickup = true;
                        return false;
                    }
                    if (TextUtils.isEmpty(address)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        // gotoFavLocationActivity();
                        //isPickup = true;
                        return false;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_pickup_contact));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(phone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_phone));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(email)) {
                        etPickupDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(email)) {
//                    if (!TextUtils.isEmpty(email) && !Utils.isEmailValid(email)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_email));
                        etPickupDetailsEmail.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(startTime)) {
                        etPickupDetailsreadyForPickup.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.pickuo_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(R.string.invalid_pic_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = true;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }

                } else if (!isPickupAdded && isDeliveryAdded) {
                    if (TextUtils.isEmpty(delName)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        //  gotoFavLocationActivity();
                        // isPickup = false;
                        return false;
                    }
                    if (TextUtils.isEmpty(delAddress)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_delivery_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        //  gotoFavLocationActivity();
                        // isPickup = false;
                        return false;
                    }
                    if (TextUtils.isEmpty(delPhone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_delivery_contact));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(delPhone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_phone));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delEmail)) {
                        etDeliveryDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(delEmail)) {
//                    if (!TextUtils.isEmpty(delEmail) && !Utils.isEmailValid(delEmail)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_email));
                        etDeliveryDetailsEmail.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delStartTime)) {
                        etDeliveryDetailsLatestDeliveryByTime.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.delivery_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(R.string.invalid_del_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = false;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }

                } else if (isPickupAdded && isDeliveryAdded) {
                    if (TextUtils.isEmpty(name)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        return false;
                    }
                    if (TextUtils.isEmpty(address)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return false;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_pickup_contact));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(phone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_phone));
                        etPickupDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(email)) {
                        etPickupDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(email)) {
//                    if (!TextUtils.isEmpty(email) && !Utils.isEmailValid(email)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_pickup_email));
                        etPickupDetailsEmail.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(startTime)) {
                        etPickupDetailsreadyForPickup.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.pickuo_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(pickupDetails != null ? R.string.invalid_delivery_time : R.string.invalid_pickup_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = true;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }

                    /////
                    if (TextUtils.isEmpty(delName)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                        return false;
                    }
                    if (TextUtils.isEmpty(delAddress)) {
                        Utils.snackBar(this, getStrings(R.string.please_enter_delivery_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return false;
                    }
                    if (TextUtils.isEmpty(delPhone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_delivery_contact));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (!Utils.isValidPhoneNumber(delPhone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_phone));
                        etDeliveryDetailsPhoneNumber.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delEmail)) {
                        etDeliveryDetailsEmail.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        return false;
                    }
                    if (!Utils.isEmailValid(delEmail)) {
//                    if (!TextUtils.isEmpty(delEmail) && !Utils.isEmailValid(delEmail)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_delivery_email));
                        etDeliveryDetailsEmail.requestFocus();
                        return false;
                    }
                    if (TextUtils.isEmpty(delStartTime)) {
                        etDeliveryDetailsLatestDeliveryByTime.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.delivery_date_time_is_required));
                        return false;
                    }
                    if (!isValidStartDate()) {
                        new AlertDialog.Builder(this).message(getStrings(pickupDetails != null ? R.string.invalid_delivery_time : R.string.invalid_pickup_time)).button(getStrings(R.string.ok_text))
                                .listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        isStartDate = false;
                                        openDatePicker();
                                    }
                                }).build().show();
                        return false;
                    }

                } else if (!isDeliveryAdded && !isPickupAdded) {
                    Utils.snackBar(mActivity, getStrings(R.string.please_select_pickup_or_delivery));
                    return false;
                }
            }
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
            if (TextUtils.isEmpty(delName)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                return false;
            }
            if (TextUtils.isEmpty(delAddress)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_delivery_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                return false;
            }
            if (TextUtils.isEmpty(delPhone)) {
                Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                etDeliveryDetailsPhoneNumber.requestFocus();
                return false;
            }
            if (!Utils.isValidPhoneNumber(delPhone)) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
                etDeliveryDetailsPhoneNumber.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(delEmail)) {
                etDeliveryDetailsEmail.requestFocus();
                Utils.snackBar(this, getStrings(R.string.email_is_required));
                return false;
            }
            if (!Utils.isEmailValid(delEmail)) {
//            if (!TextUtils.isEmpty(delEmail) && !Utils.isEmailValid(delEmail)) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_email));
                etDeliveryDetailsEmail.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(delStartTime)) {
                etDeliveryDetailsLatestDeliveryByTime.requestFocus();
                Utils.snackBar(this, getStrings(R.string.date_time_is_required));
                return false;
            }
            if (!isValidStartDate()) {
                new AlertDialog.Builder(this).message(getStrings(R.string.invalid_selected_date)).button(getStrings(R.string.ok_text))
                        .listener(new AlertDialog.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {
                                isStartDate = false;
                                openDatePicker();
                            }
                        }).build().show();
                return false;
            }

        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP || StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT || StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
            if ((Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE) &&
                    (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 0 || (isStartEndTimeEnabled.equals(1))) &&
                    isScheduled == 0 &&
                    Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getIsStorefrontOpened() == 0) {
                Utils.snackBar(this, getStrings(R.string.store_currently_closed));
                return false;
            }

            if (selectedPickupMode == Constants.SelectedPickupMode.NONE) {
                Utils.snackBar(this, getStrings(R.string.choose_either_home_delivery_or_self_pickup)
                        .replace(HOME_DELIVERY, StorefrontCommonData.getTerminology().getHomeDelivery())
                        .replace(TAKE_AWAY, StorefrontCommonData.getTerminology().getSelfPickup()));
                return false;
            }
            if (TextUtils.isEmpty(name)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                return false;
            }
            if (TextUtils.isEmpty(address)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                return false;
            }
            if (TextUtils.isEmpty(phone)) {
                Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                etPickupDetailsPhoneNumber.requestFocus();
                return false;
            }
            if (!Utils.isValidPhoneNumber(phone)) {
                Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
                etPickupDetailsPhoneNumber.requestFocus();
                return false;
            }
//            if (TextUtils.isEmpty(email)) {
//                etPickupDetailsEmail.requestFocus();
//                Utils.snackBar(this, getStrings(R.string.email_is_required));
//                return false;
//            }
//            if (!Utils.isEmailValid(email)) {
////            if (!TextUtils.isEmpty(email) && !Utils.isEmailValid(email)) {
//                Utils.snackBar(this, getStrings(R.string.enter_valid_email));
//                etPickupDetailsEmail.requestFocus();
//                return false;
//            }

//            if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1) {
//                if (TextUtils.isEmpty(etStartEndTimeSchedule.getText().toString().trim())) {
//                    etStartEndTimeSchedule.requestFocus();
//                    Utils.snackBar(this, getStrings(R.string.date_time_is_required));
//                    return false;
//                }
//            } else {


            if (!(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
//                    && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT
            )) {
                if (TextUtils.isEmpty(startTime)) {
                    etStartDateTime.requestFocus();
                    if (isStartEndTimeEnabled.equals(1) || (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                        Utils.snackBar(this, getStrings(R.string.start_time_is_required).replace(getStrings(R.string.start_time), StorefrontCommonData.getTerminology().getStartTime(true)));
                    } else {
                        Utils.snackBar(this, getStrings(R.string.date_time_is_required));
                    }
                    return false;
                }

                if (isStartEndTimeEnabled.equals(1) || (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                    isStartDate = true;
                    Date startDate = DateUtils.getInstance().getDateFromString(etStartDateTime.getText().toString(), UIManager.getDateTimeFormat());

                    if (startDate.before(Calendar.getInstance().getTime()) || startDate.equals(Calendar.getInstance().getTime())) {
                        etStartDateTime.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.invalid_start_date_greater_than_current_time).replace(getStrings(R.string.start_time),
                                StorefrontCommonData.getTerminology().getStartTime(true)));
                        return false;
                    }

                    if (TextUtils.isEmpty(endTime)) {
                        etEndDateTimeSchedule.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.end_time_is_required).replace(getStrings(R.string.end_time), StorefrontCommonData.getTerminology().getEndTime(true)));
                        return false;
                    }

                    isStartDate = false;
                    Date endDate = DateUtils.getInstance().getDateFromString(etEndDateTimeSchedule.getText().toString(),
                            UIManager.getDateTimeFormat());

                    if (endDate.before(Calendar.getInstance().getTime()) || endDate.equals(Calendar.getInstance().getTime())) {
                        etEndDateTimeSchedule.requestFocus();
                        Utils.snackBar(this, getStrings(R.string.invalid_end_date_greater_than_current_time).replace(getStrings(R.string.end_time),
                                StorefrontCommonData.getTerminology().getEndTime(true)));
                        return false;
                    }

                    if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                            && !(Constants.ProductsUnitType.getUnitType(Dependencies.getSelectedProductsArrayList().get(0).getUnitType()) == Constants.ProductsUnitType.FIXED)) {

                        long differenceMultiple = Constants.ProductsUnitType.getStartEndDifferenceMultiple(Dependencies.getSelectedProductsArrayList().get(0).getUnitType(), Dependencies.getSelectedProductsArrayList().get(0).getUnit().intValue());
                        long currentDifference = DateUtils.getInstance().getDateFromString(etEndDateTimeSchedule.getText().toString(),
                                UIManager.getDateTimeFormat()).getTime() - DateUtils.getInstance().getDateFromString(etStartDateTime.getText().toString(),
                                UIManager.getDateTimeFormat()).getTime();

                        if (!(currentDifference % differenceMultiple == 0)) {
                            new AlertDialog.Builder(this).message(getStrings(R.string.start_end_time_should_be_multiple_of_).replace(getStrings(R.string.end_time), StorefrontCommonData.getTerminology().getEndTime(true)).replace(getStrings(R.string.start_time), StorefrontCommonData.getTerminology().getStartTime(true)) + " " +
                                    Constants.ProductsUnitType.getUnitTypeText(mActivity,
                                            Dependencies.getSelectedProductsArrayList().get(0).getUnit().intValue(),
                                            Dependencies.getSelectedProductsArrayList().get(0).getUnitType(), true))
                                    .button(getStrings(R.string.ok_text))
                                    .build().show();
                            return false;
                        }
                    } else {
                        if (endDate.before(startDate) || endDate.equals(startDate)) {
                            etEndDateTimeSchedule.requestFocus();
                            Utils.snackBar(this, pickupDetails != null ? getStrings(R.string.invalid_delivery_time)
                                    : getStrings(R.string.invalid_start_date).replace(getStrings(R.string.end_time),
                                    StorefrontCommonData.getTerminology().getEndTime(true)).replace(getStrings(R.string.start_time),
                                    StorefrontCommonData.getTerminology().getStartTime(true)));
                            return false;
                        }
                    }
                }
            }
//            }
        }
        return checkTaskForCompletion();
    }

    private void createTask() {

        switch (StorefrontCommonData.getFormSettings().getWorkFlow()) {
            case Constants.LayoutType.PICKUP:
            case Constants.LayoutType.DELIVERY:
            case Constants.LayoutType.APPOINTMENT:
            case Constants.LayoutType.FOS:
                createSingleTaskWithPayment();
                break;
            case Constants.LayoutType.PICKUP_DELIVERY:
                if (!isPickupAdded) {
                    etPickupDetailsAddress.setText("");
                    etPickupDetailsPhoneNumber.setText("");
                }
                savePickupDeliveryTask();
                break;
        }
    }/*
     */

    /**
     * Method to check whether the Fleet has Completed the Task
     *
     * @return
     */
    private boolean checkTaskForCompletion() {
        // Check for custom fields
        boolean isCustomFieldPassed = true;
        UserOptions pickUptTemplates = StorefrontCommonData.getUserData().getData().getUserOptions();
        UserOptions deliveryTemplates = StorefrontCommonData.getUserData().getData().getDeliveryOptions();
        if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY) {
            if (StorefrontCommonData.getFormSettings().getForcePickupDelivery() == 1) {
                if (pickUptTemplates != null) {
                    ArrayList<String> requiredCustomFields = new ArrayList<>();
                    for (Item item : pickUptTemplates.getItems()) {
                        String data = item.getData().toString();
                        String error = null;
                        switch (item.getDataType()) {
                            case Keys.DataType.EMAIL:
                                error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                break;
                            case Keys.DataType.URL:
                                error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                break;
                            case Keys.DataType.TELEPHONE:
                                error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                break;
                        }
                        if (data.isEmpty() && item.getRequired() == 1) {
                            Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                            return false;
                        }
                        if (error != null) {
                            isCustomFieldPassed = false;
                            String name = item.getLabel();
                            requiredCustomFields.add(name + "(" + error + ")");
                        }
                    }
                    if (!isCustomFieldPassed) {
                        String errorFiels = "";
                        for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                            if (errorFiels.isEmpty()) {
                                errorFiels = errorFiels.concat(Utils.assign(subItem));
                            } else {
                                errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                            }
                        }
//                        final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                        builder.message(getStrings(R.string.fields_invalid_text));
//                        builder.add(getStrings(R.string.custom_fields_text),
//                                requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                    }
//                                });
//                        builder.itemBullet('*').subItemBullet('-')
//                                .build().show();
                    }
                }
                if (deliveryTemplates != null) {
                    ArrayList<String> requiredCustomFields = new ArrayList<>();
                    for (Item item : deliveryTemplates.getItems()) {
                        String data = item.getData().toString();
                        String error = null;
                        switch (item.getDataType()) {
                            case Keys.DataType.EMAIL:
                                error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                break;
                            case Keys.DataType.URL:
                                error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                break;
                            case Keys.DataType.TELEPHONE:
                                error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                break;
                        }
                        if (data.isEmpty() && item.getRequired() == 1) {
                            Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                            return false;
                        }
                        if (error != null) {
                            isCustomFieldPassed = false;
                            String name = item.getLabel();
                            requiredCustomFields.add(name + "(" + error + ")");
                        }
                    }
                    if (!isCustomFieldPassed) {
                        String errorFiels = "";
                        for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                            if (errorFiels.isEmpty()) {
                                errorFiels = errorFiels.concat(Utils.assign(subItem));
                            } else {
                                errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                            }
                        }
//                        final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                        builder.message(getStrings(R.string.fields_invalid_text));
//                        builder.add(getStrings(R.string.custom_fields_text),
//                                requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                    }
//                                });
//                        builder.itemBullet('*').subItemBullet('-')
//                                .build().show();
                    }
                }
            } else {
                if (isPickupAdded && !isDeliveryAdded) {
                    if (pickUptTemplates != null) {
                        ArrayList<String> requiredCustomFields = new ArrayList<>();
                        for (Item item : pickUptTemplates.getItems()) {
                            String data = item.getData().toString();
                            String error = null;
                            switch (item.getDataType()) {
                                case Keys.DataType.EMAIL:
                                    error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                    break;
                                case Keys.DataType.URL:
                                    error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                    break;
                                case Keys.DataType.TELEPHONE:
                                    error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                    break;
                            }
                            if (data.isEmpty() && item.getRequired() == 1) {
                                Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                                return false;
                            }
                            if (error != null) {
                                isCustomFieldPassed = false;
                                String name = item.getLabel();
                                requiredCustomFields.add(name + "(" + error + ")");
                            }
                        }
                        if (!isCustomFieldPassed) {
                            String errorFiels = "";
                            for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                                if (errorFiels.isEmpty()) {
                                    errorFiels = errorFiels.concat(Utils.assign(subItem));
                                } else {
                                    errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                                }
                            }
//                            final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                            builder.message(getStrings(R.string.fields_invalid_text));
//                            builder.add(getStrings(R.string.custom_fields_text),
//                                    requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    });
//                            builder.itemBullet('*').subItemBullet('-')
//                                    .build().show();
                        }
                    }
                } else if (!isPickupAdded && isDeliveryAdded) {
                    if (pickUptTemplates != null) {
                        ArrayList<String> requiredCustomFields = new ArrayList<>();
                        for (Item item : pickUptTemplates.getItems()) {
                            String data = item.getData().toString();
                            String error = null;
                            switch (item.getDataType()) {
                                case Keys.DataType.EMAIL:
                                    error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                    break;
                                case Keys.DataType.URL:
                                    error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                    break;
                                case Keys.DataType.TELEPHONE:
                                    error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                    break;
                            }
                            if (data.isEmpty() && item.getRequired() == 1) {
                                Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                                return false;
                            }
                            if (error != null) {
                                isCustomFieldPassed = false;
                                String name = item.getLabel();
                                requiredCustomFields.add(name + "(" + error + ")");
                            }
                        }
                        if (!isCustomFieldPassed) {
                            String errorFiels = "";
                            for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                                if (errorFiels.isEmpty()) {
                                    errorFiels = errorFiels.concat(Utils.assign(subItem));
                                } else {
                                    errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                                }
                            }
//                            final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                            builder.message(getStrings(R.string.fields_invalid_text));
//                            builder.add(getStrings(R.string.custom_fields_text),
//                                    requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    });
//                            builder.itemBullet('*').subItemBullet('-')
//                                    .build().show();
                        }
                    }
                } else if (isPickupAdded && isDeliveryAdded) {
                    if (pickUptTemplates != null) {
                        ArrayList<String> requiredCustomFields = new ArrayList<>();
                        for (Item item : pickUptTemplates.getItems()) {
                            String data = item.getData().toString();
                            String error = null;
                            switch (item.getDataType()) {
                                case Keys.DataType.EMAIL:
                                    error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                    break;
                                case Keys.DataType.URL:
                                    error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                    break;
                                case Keys.DataType.TELEPHONE:
                                    error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                    break;
                            }
                            if (data.isEmpty() && item.getRequired() == 1) {
                                Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                                return false;
                            }
                            if (error != null) {
                                isCustomFieldPassed = false;
                                String name = item.getLabel();
                                requiredCustomFields.add(name + "(" + error + ")");
                            }
                        }
                        if (!isCustomFieldPassed) {
                            String errorFiels = "";
                            for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                                if (errorFiels.isEmpty()) {
                                    errorFiels = errorFiels.concat(Utils.assign(subItem));
                                } else {
                                    errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                                }
                            }
//                            final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                            builder.message(getStrings(R.string.fields_invalid_text));
//                            builder.add(getStrings(R.string.custom_fields_text),
//                                    requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    });
//                            builder.itemBullet('*').subItemBullet('-')
//                                    .build().show();
                        }
                    }
                    if (deliveryTemplates != null) {
                        ArrayList<String> requiredCustomFields = new ArrayList<>();
                        for (Item item : deliveryTemplates.getItems()) {
                            String data = item.getData().toString();
                            String error = null;
                            switch (item.getDataType()) {
                                case Keys.DataType.EMAIL:
                                    error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                                    break;
                                case Keys.DataType.URL:
                                    error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                                    break;
                                case Keys.DataType.TELEPHONE:
                                    error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                                    break;
                            }
                            if (data.isEmpty() && item.getRequired() == 1) {
                                Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                                return false;
                            }
                            if (error != null) {
                                isCustomFieldPassed = false;
                                String name = item.getLabel();
                                requiredCustomFields.add(name + "(" + error + ")");
                            }
                        }
                        if (!isCustomFieldPassed) {
                            String errorFiels = "";
                            for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                                if (errorFiels.isEmpty()) {
                                    errorFiels = errorFiels.concat(Utils.assign(subItem));
                                } else {
                                    errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                                }
                            }
//                            final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                            builder.message(getStrings(R.string.fields_invalid_text));
//                            builder.add(getStrings(R.string.custom_fields_text),
//                                    requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    });
//                            builder.itemBullet('*').subItemBullet('-')
//                                    .build().show();
                        }
                    }
                }
            }
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
            if (deliveryTemplates != null) {
                ArrayList<String> requiredCustomFields = new ArrayList<>();
                for (Item item : deliveryTemplates.getItems()) {
                    String data = item.getData().toString();
                    String error = null;
                    switch (item.getDataType()) {
                        case Keys.DataType.EMAIL:
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                            break;
                        case Keys.DataType.TELEPHONE:
                            error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                            break;
                    }
                    if (data.isEmpty() && item.getRequired() == 1) {
                        Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                        return false;
                    }
                    if (error != null) {
                        isCustomFieldPassed = false;
                        String name = item.getLabel();
                        requiredCustomFields.add(name + "(" + error + ")");
                    }
                }
                if (!isCustomFieldPassed) {
                    String errorFiels = "";
                    for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                        if (errorFiels.isEmpty()) {
                            errorFiels = errorFiels.concat(Utils.assign(subItem));
                        } else {
                            errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                        }
                    }
                    Utils.snackBar(this, errorFiels);
//                    final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                    builder.message(getStrings(R.string.fields_invalid_text));
//                    builder.add(getStrings(R.string.custom_fields_text),
//                            requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                }
//                            });
//                    builder.itemBullet('*').subItemBullet('-')
//                            .build().show();
                }
            }
        } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP || StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS || StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
            if (pickUptTemplates != null) {
                ArrayList<String> requiredCustomFields = new ArrayList<>();
                for (Item item : pickUptTemplates.getItems()) {
                    String data = item.getData().toString();
                    String error = null;
                    switch (item.getDataType()) {
                        case Keys.DataType.EMAIL:
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                            break;
                        case Keys.DataType.TELEPHONE:
                            error = (data.isEmpty() || Utils.isValidPhoneNumber(data)) ? null : getStrings(R.string.invalid_phone_number);
                            break;
                    }
                    if (data.isEmpty() && item.getRequired() == 1) {
                        Utils.snackBar(this, item.getLabel() + " " + getStrings(R.string.is_required));
                        return false;
                    }
                    if (error != null) {
                        isCustomFieldPassed = false;
                        String name = item.getLabel();
                        requiredCustomFields.add(name + "(" + error + ")");
                    }
                }
                if (!isCustomFieldPassed) {
                    String errorFiels = "";
                    for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                        if (errorFiels.isEmpty()) {
                            errorFiels = errorFiels.concat(Utils.assign(subItem));
                        } else {
                            errorFiels = errorFiels.concat("\n" + Utils.assign(subItem));
                        }
                    }
//                    final TaskMandatoryFieldsDialog.Builder builder = new TaskMandatoryFieldsDialog.Builder(this);
//                    builder.message(getStrings(R.string.fields_invalid_text));
//                    builder.add(getStrings(R.string.custom_fields_text),
//                            requiredCustomFields.toArray(new String[requiredCustomFields.size()]), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                }
//                            });
//                    builder.itemBullet('*').subItemBullet('-')
//                            .build().show();
                }
            }
        }
        return isCustomFieldPassed;
    }

    private CommonParams.Builder getCommonParamsBuilder() {
        int layoutType = StorefrontCommonData.getFormSettings().getWorkFlow();
        layoutType = layoutType == Constants.LayoutType.PICKUP || layoutType == Constants.LayoutType.DELIVERY ? Constants.LayoutType.PICKUP_DELIVERY : layoutType;
        CommonParams.Builder builder = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        builder.add(IS_SCHEDULED, isScheduled);
        switch (layoutType) {
            case Constants.LayoutType.APPOINTMENT:
            case Constants.LayoutType.FOS:
                builder.add(HAS_DELIVERY, 0)
                        .add(HAS_PICKUP, 0)
                        .add(CUSTOMER_ADDRESS, etPickupDetailsAddress.getText().toString())
                        .add(CUSTOMER_EMAIL, etPickupDetailsEmail.getText().toString().trim())
                        .add(CUSTOMER_PHONE, tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                        .add(CUSTOMER_USERNAME, etPickupDetailsName.getText().toString().trim())
                        .add(JOB_DELIVERY_DATETIME, getEndTime())
                        .add(LATITUDE, pickUpLatitude)
                        .add(LONGITUDE, pickUpLongitude)
                        .add(JOB_PICKUP_DATETIME, getStartTime());
                addCustomFieldsData(builder, StorefrontCommonData.getUserData().getData().getUserOptions());
                break;
            case Constants.LayoutType.PICKUP_DELIVERY:
                if (pickupDetails != null || deliveryDetails != null) {
                    boolean isPickupDetailsAdded = pickupDetails != null;
                    boolean isDeliveryDetailsAdded = deliveryDetails != null;
                    builder.add(HAS_DELIVERY, 1)
                            .add(HAS_PICKUP, 1)
                            .add(JOB_PICKUP_ADDRESS, isPickupDetailsAdded ? pickupDetails.getAddress() : etPickupDetailsAddress.getText().toString())
                            .add(JOB_PICKUP_EMAIL, isPickupDetailsAdded ? pickupDetails.getEmail() : etPickupDetailsEmail.getText().toString().trim())
                            .add(JOB_PICKUP_PHONE, isPickupDetailsAdded ? pickupDetails.getCountryCode() + pickupDetails.getPhoneNumber() : tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                            .add(JOB_PICKUP_DATETIME, isPickupDetailsAdded ? getFormattedTime(pickupDetails.getPickupTime()) : getStartTime())
                            .add(JOB_PICKUP_LATITUDE, isPickupDetailsAdded ? pickupDetails.getLatitude() : pickUpLatitude)
                            .add(JOB_PICKUP_LONGITUDE, isPickupDetailsAdded ? pickupDetails.getLongitude() : pickUpLongitude)
                            .add(JOB_PICKUP_NAME, isPickupDetailsAdded ? pickupDetails.getUsername() : etPickupDetailsName.getText().toString().trim())
                            .add(CUSTOMER_ADDRESS, isDeliveryDetailsAdded ? deliveryDetails.getAddress() : etDeliveryDetailsAddress.getText().toString())
                            .add(CUSTOMER_EMAIL, isDeliveryDetailsAdded ? deliveryDetails.getEmail() : etDeliveryDetailsEmail.getText().toString())
                            .add(CUSTOMER_PHONE, isDeliveryDetailsAdded ? deliveryDetails.getCountryCode() + deliveryDetails.getPhoneNumber() : tvDeliveryDetailsCountryCode.getText().toString() + etDeliveryDetailsPhoneNumber.getText().toString())
                            .add(CUSTOMER_USERNAME, isDeliveryDetailsAdded ? deliveryDetails.getUsername() : etDeliveryDetailsName.getText().toString())
                            .add(JOB_DELIVERY_DATETIME, isDeliveryDetailsAdded ? getFormattedTime(deliveryDetails.getPickupTime()) : getDeliveryTime())
                            .add(LATITUDE, isDeliveryDetailsAdded ? deliveryDetails.getLatitude() : deliveryLatitude)
                            .add(LONGITUDE, isDeliveryDetailsAdded ? deliveryDetails.getLongitude() : deliveryLongitude);
                    addCustomFieldsData(builder, isDeliveryDetailsAdded ? deliveryDetails.getUserOptions() : StorefrontCommonData.getUserData().getData().getDeliveryOptions());
                    addPickupCustomField(builder, isPickupDetailsAdded ? pickupDetails.getUserOptions() : StorefrontCommonData.getUserData().getData().getUserOptions());
                } else {
                    if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
                        builder.add(HAS_DELIVERY, 0)
                                .add(HAS_PICKUP, 1)
                                .add(JOB_PICKUP_ADDRESS, etPickupDetailsAddress.getText().toString())
                                .add(JOB_PICKUP_EMAIL, StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail().trim())
                                .add(JOB_PICKUP_PHONE, tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                                .add(JOB_PICKUP_NAME, etPickupDetailsName.getText().toString().trim());

                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
                            builder.add(JOB_PICKUP_DATETIME, getStartTime());
                            builder.add(JOB_DELIVERY_DATETIME, getStartTime());
                        } else {
                            builder.add(JOB_PICKUP_DATETIME, getStartTime());
                            if (isStartEndTimeEnabled.equals(1)) {
                                builder.add(JOB_DELIVERY_DATETIME, getEndTime());
                            }
                        }

                        builder.add("currency_id", Utils.getCurrencyId());

                        if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
                            builder.add("self_pickup", 1);
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

                        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                        builder.add(HAS_DELIVERY, 1)
                                .add(HAS_PICKUP, 0)
                                .add(CUSTOMER_ADDRESS, etDeliveryDetailsAddress.getText().toString())
                                .add(CUSTOMER_EMAIL, etDeliveryDetailsEmail.getText().toString())
                                .add(CUSTOMER_PHONE, tvDeliveryDetailsCountryCode.getText().toString() + etDeliveryDetailsPhoneNumber.getText().toString())
                                .add(CUSTOMER_USERNAME, etDeliveryDetailsName.getText().toString())
                                .add(JOB_DELIVERY_DATETIME, getDeliveryTime())
                                .add(LATITUDE, deliveryLatitude)
                                .add(LONGITUDE, deliveryLongitude);
                        addCustomFieldsData(builder, StorefrontCommonData.getUserData().getData().getDeliveryOptions());
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
                        builder.add(HAS_DELIVERY, 0)
                                .add(HAS_PICKUP, 1)
                                .add(JOB_PICKUP_ADDRESS, etPickupDetailsAddress.getText().toString())
                                .add(JOB_PICKUP_EMAIL, etPickupDetailsEmail.getText().toString())
                                .add(JOB_PICKUP_PHONE, tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                                .add(JOB_PICKUP_DATETIME, getStartTime())
                                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                                .add(JOB_PICKUP_NAME, etPickupDetailsName.getText().toString().trim());
                        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
                        builder.add(HAS_DELIVERY, 0)
                                .add(HAS_PICKUP, 1)
                                .add(JOB_PICKUP_ADDRESS, etPickupDetailsAddress.getText().toString())
                                .add(JOB_PICKUP_EMAIL, etPickupDetailsEmail.getText().toString().trim())
                                .add(JOB_PICKUP_PHONE, tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                                .add(JOB_PICKUP_DATETIME, getStartTime())
                                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                                .add(JOB_PICKUP_NAME, etPickupDetailsName.getText().toString().trim());
                        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());
                    } else {
                        builder.add(HAS_DELIVERY, 1)
                                .add(HAS_PICKUP, 1)
                                .add(JOB_PICKUP_ADDRESS, etPickupDetailsAddress.getText().toString())
                                .add(JOB_PICKUP_EMAIL, etPickupDetailsEmail.getText().toString().trim())
                                .add(JOB_PICKUP_PHONE, tvPickupDetailsCountryCode.getText().toString() + etPickupDetailsPhoneNumber.getText().toString())
                                .add(JOB_PICKUP_DATETIME, getStartTime())
                                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                                .add(JOB_PICKUP_NAME, etPickupDetailsName.getText().toString().trim())
                                .add(CUSTOMER_ADDRESS, etDeliveryDetailsAddress.getText().toString())
                                .add(CUSTOMER_EMAIL, etDeliveryDetailsEmail)
                                .add(CUSTOMER_PHONE, tvDeliveryDetailsCountryCode.getText().toString() + etDeliveryDetailsPhoneNumber.getText().toString())
                                .add(CUSTOMER_USERNAME, etDeliveryDetailsName.getText().toString())
                                .add(JOB_DELIVERY_DATETIME, getDeliveryTime())
                                .add(LATITUDE, deliveryLatitude)
                                .add(LONGITUDE, deliveryLongitude);
                        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());
                        addCustomFieldsData(builder, StorefrontCommonData.getUserData().getData().getDeliveryOptions());


                    }
                }
                break;
        }
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

                            jsonObject.put("customizations", customArray);
                            jsonArray.put(jsonObject);
                        }
                    }
                }

                builder.add("products", jsonArray);
            }
        } catch (JSONException e) {
        }
        builder.add(JOB_DESCRIPTION_FIELD, etDescription.getText().toString())
                .add(AUTO_ASSIGNMENT, 1)
                .add(LAYOUT_TYPE, layoutType)
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add(VERTICAL, UIManager.getVertical());

        return builder;
    }

    public String getStartTime() {
        return DateUtils.getInstance().parseDateAs(etPickupDetailsreadyForPickup.getText().toString().trim(),
                UIManager.getDateTimeFormat(), Constants.DateFormat
                        .STANDARD_DATE_FORMAT);
    }

    public String getEndTime() {
        return DateUtils.getInstance().parseDateAs(etPickupDetailsEndDate.getText().toString().trim(),
                UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
    }

    public String getDeliveryTime() {
        return DateUtils.getInstance().parseDateAs(etDeliveryDetailsLatestDeliveryByTime.getText().toString().trim(),
                UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
    }

    public String getFormattedTime(String dateTime) {
        return DateUtils.getInstance().parseDateAs(dateTime, UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT);
    }

    private void addPickupCustomField(CommonParams.Builder builder, UserOptions userOptions) {
        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
            builder.add(PICKUP_CUSTOM_FIELD_TEMPLATE, StorefrontCommonData.getUserData().getData().getUserOptions().getTemplate())
                    .add(PICKUP_META_DATA, getMetaData(userOptions));
        }
    }

    private void addCustomFieldsData(CommonParams.Builder builder, UserOptions userOptions) {
        if (userOptions != null) {
            builder.add(CUSTOM_FIELD_TEMPLATE, userOptions.getTemplate())
                    .add(META_DATA, getMetaData(userOptions));
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
                        jsonObjectPickUpMetaData.put("data", getJobDescription());
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

    private String getSubTotal() {
        return Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal());
    }

    private void savePickupDeliveryTask() {
        pickupDetails = new PickupDetails();
        pickupDetails.setAddress(etPickupDetailsAddress.getText().toString());
        pickupDetails.setDescription(etDescription.getText().toString());
        pickupDetails.setUsername(etPickupDetailsName.getText().toString());
        pickupDetails.setEmail(etPickupDetailsEmail.getText().toString());
        pickupDetails.setPickupTime(etPickupDetailsreadyForPickup.getText().toString());
        pickupDetails.setLatitude(pickUpLatitude);
        pickupDetails.setLongitude(pickUpLongitude);
        pickupDetails.setPhoneNumber(etPickupDetailsPhoneNumber.getText().toString());
        pickupDetails.setCountryCode(tvPickupDetailsCountryCode.getText().toString());
        pickupDetails.setUserOptions(StorefrontCommonData.getUserData().getData().getUserOptions());
        deliveryDetails = new PickupDetails();
        deliveryDetails.setAddress(etDeliveryDetailsAddress.getText().toString());
        deliveryDetails.setDescription(etDescription.getText().toString());
        deliveryDetails.setUsername(etDeliveryDetailsName.getText().toString());
        deliveryDetails.setEmail(etDeliveryDetailsEmail.getText().toString());
        deliveryDetails.setPickupTime(etDeliveryDetailsLatestDeliveryByTime.getText().toString());
        deliveryDetails.setLatitude(deliveryLatitude);
        deliveryDetails.setLongitude(deliveryLongitude);
        deliveryDetails.setCountryCode(tvDeliveryDetailsCountryCode.getText().toString());
        deliveryDetails.setPhoneNumber(etDeliveryDetailsPhoneNumber.getText().toString());
        deliveryDetails.setUserOptions(StorefrontCommonData.getUserData().getData().getDeliveryOptions());
        openReviewBothTaskActivity();
    }

    /**
     * Method to check whether the TrackingData Permission
     * is Granted by the User
     */
    private boolean checkLocationPermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return AppManager.getInstance().askUserToGrantPermission(this, permissionsRequired, getStrings(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    executeSetAddress();
                if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                    deliveryLatitude = getCurrentLocation().latitude;
                    deliveryLongitude = getCurrentLocation().longitude;
                }
                pickUpLatitude = getCurrentLocation().latitude;
                pickUpLongitude = getCurrentLocation().longitude;
                break;
            case Codes.Permission.CAMERA:
            case Codes.Permission.OPEN_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.PICKUP) {
                        CustomFieldImagePickup customFieldImageView = getCustomFieldImageViewPickup();
                        customFieldImageView.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                        CustomFieldImageDelivery customFieldImageView = getCustomFieldImageViewDelivery();
                        customFieldImageView.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.APPOINTMENT) {
                        CustomFieldImagePickup customFieldImageView = getCustomFieldImageViewPickup();
                        customFieldImageView.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    } else if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.FOS) {
                        CustomFieldImagePickup customFieldImageView = getCustomFieldImageViewPickup();
                        customFieldImageView.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    } else {
                        if (StorefrontCommonData.getUserData().getData().getUserOptions() != null) {
                            CustomFieldImagePickup customFieldImageViewPickup = getCustomFieldImageViewPickup();
                            customFieldImageViewPickup.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        }
                        if (StorefrontCommonData.getUserData().getData().getDeliveryOptions() != null) {
                            CustomFieldImageDelivery customFieldImageViewDelivery = getCustomFieldImageViewDelivery();
                            customFieldImageViewDelivery.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        }
                    }
                }
                break;
        }
    }


    public void executeSetAddress() {

        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    private class SetAddress extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGAPIAddress(new LatLng(pickUpLatitude, pickUpLongitude));
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(String s) {
            etPickupDetailsAddress.setText(s);
            if (StorefrontCommonData.getFormSettings().getWorkFlow() == Constants.LayoutType.DELIVERY) {
                etDeliveryDetailsAddress.setText(s);
            }
        }
    }

    public void openDatePicker() {
        Utils.hideSoftKeyboard(this, etPickupDetailsreadyForPickup);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        if (isStartDate) {
            if (year > 0 && month > 0 && day > 0) {
                datePickerFragment.setDay(day);
                datePickerFragment.setMonth(month);
                datePickerFragment.setYear(year);
            }
        } else {
            if (endYear > 0 && endMonth > 0 && endDay > 0) {
                datePickerFragment.setDay(endDay);
                datePickerFragment.setMonth(endMonth);
                datePickerFragment.setYear(endYear);
            }
        }
        datePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    public void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        if (isStartDate) {
            if (hour > 0 && minute > 0) {
                timePickerFragment.setHour(hour);
                timePickerFragment.setMinute(minute);
            }
        } else {
            if (endHour > 0 && endMinute > 0) {
                timePickerFragment.setHour(endHour);
                timePickerFragment.setMinute(endMinute);
            }
        }
        timePickerFragment.show(this.getSupportFragmentManager(), TAG);
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

    public boolean isValidDate(int year, int monthOfYear, int dayOfMonth) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();
            if (year < calendar.get(Calendar.YEAR)) {
                isValidDate = false;
            } else if (year == calendar.get(Calendar.YEAR)) {
                if (monthOfYear < calendar.get(Calendar.MONTH)) {
                    isValidDate = false;
                } else if (monthOfYear == calendar.get(Calendar.MONTH)) {
                    if (dayOfMonth < calendar.get(Calendar.DAY_OF_MONTH)) {
                        isValidDate = false;
                    }
                }
            }
        }
        return isValidDate;
    }

    public boolean isValidTime(int hour, int minute) {
        boolean isValidTime = true;
        Calendar calendar = Calendar.getInstance();
        if (isStartDate) {
            if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
                if (hour < calendar.get(Calendar.HOUR_OF_DAY)) {
                    isValidTime = false;
                } else if (hour == calendar.get(Calendar.HOUR_OF_DAY)) {
                    if (minute < calendar.get(Calendar.MINUTE)) {
                        isValidTime = false;
                    }
                }
            }
        } else {
            if (endYear == calendar.get(Calendar.YEAR) && endMonth == calendar.get(Calendar.MONTH) && endDay == calendar.get(Calendar.DAY_OF_MONTH)) {
                if (hour < calendar.get(Calendar.HOUR_OF_DAY)) {
                    isValidTime = false;
                } else if (hour == calendar.get(Calendar.HOUR_OF_DAY)) {
                    if (minute < calendar.get(Calendar.MINUTE)) {
                        isValidTime = false;
                    }
                }
            }
        }
        return isValidTime;
    }

    public void setDateVariables(int year, int monthOfYear, int dayOfMonth) {
        if (isStartDate) {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;
        } else {
            this.endYear = year;
            this.endMonth = monthOfYear;
            this.endDay = dayOfMonth;
        }
    }

    public void setTimeVariables(int hour, int minute) {
        if (isStartDate) {
            this.hour = hour;
            this.minute = minute;
        } else {
            this.endHour = hour;
            this.endMinute = minute;
        }
    }

    public void setDateTimeText() {
        if (isStartDate) {
            String timeSelected = hour + " " + minute;
            String dateSelected = day + "/" + (month + 1) + "/" + year;
            etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected, "dd/MM/yyyy HH mm",
                    UIManager.getDateTimeFormat()));
            etStartDateTime.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected, "dd/MM/yyyy HH mm",
                    UIManager.getDateTimeFormat()));
        } else {
            String timeSelected = endHour + " " + endMinute;
            String dateSelected = endDay + "/" + (endMonth + 1) + "/" + endYear;
            etDeliveryDetailsLatestDeliveryByTime.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected, "dd/MM/yyyy HH mm",
                    UIManager.getDateTimeFormat()));
            etPickupDetailsEndDate.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected, "dd/MM/yyyy HH mm",
                    UIManager.getDateTimeFormat()));
            etEndDateTimeSchedule.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected,
                    "dd/MM/yyyy HH mm", UIManager.getDateTimeFormat()));
        }
    }

    private boolean isValidStartDate() {
        Log.i("year", "==" + year + "==" + endYear);
        Log.i("month", "==" + month + "==" + endMonth);
        Log.i("day", "==" + day + "==" + endDay);
        Log.i("hour", "==" + hour + "==" + endHour);
        Log.i("min", "==" + minute + "==" + endMinute);
        boolean isValidStartDate = true;
        if (minute != -1 && endMinute != -1) {
            if (year > endYear) {
                isValidStartDate = false;
            } else if (year == endYear) {
                if (month < endMonth) {
                    isValidStartDate = true;
                } else if (month == endMonth) {
                    if (day > endDay) {
                        isValidStartDate = false;
                    } else if (day == endDay) {
                        if (hour > endHour) {
                            isValidStartDate = false;
                        } else if (hour == endHour) {
                            if (minute >= endMinute || endMinute - minute < 2) {
//                            if (minute >= endMinute) {
                                isValidStartDate = false;
                            }
                        }
                    }
                }
            }
        }
        return isValidStartDate;
    }

    private void setDateTime() {
        Calendar calendar = Calendar.getInstance();
        //if(StorefrontCommonData.getFormSettings().getWorkFlow()==Constants.LayoutType.PICKUP_DELIVERY||StorefrontCommonData.getFormSettings().getWorkFlow()==Constants.LayoutType.PICKUP||StorefrontCommonData.getFormSettings().getWorkFlow()==Constants.LayoutType.DELIVERY)
        try {
//            calendar.add(Calendar.MINUTE, StorefrontCommonData.getFormSettings().getBufferSchedule().intValue());
            calendar.add(Calendar.MINUTE, Constants.START_TIME_BUFFER);
        } catch (Exception e) {
            calendar.add(Calendar.MINUTE, Constants.START_TIME_BUFFER);
        }
        isStartDate = true;
        setTimeVariables(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        setDateVariables(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        try {
            String timeSelected = calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);
            String dateSelected = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " +
                    timeSelected, "dd/MM/yyyy HH mm", UIManager.getDateTimeFormat()));
            //etDeliveryDetailsLatestDeliveryByTime.setText(DateUtils.getInstance().parseDateAs(dateSelected + " " + timeSelected, "dd/MM/yyyy HH mm", Constants.DateFormat.END_USER_DATE_FORMAT));
            switch (StorefrontCommonData.getFormSettings().getWorkFlow()) {
                case Constants.LayoutType.APPOINTMENT:
                case Constants.LayoutType.FOS:
                case Constants.LayoutType.PICKUP:
                    calendar.add(Calendar.MINUTE, Constants.END_TIME_BUFFER);
                    isStartDate = false;
                    setTimeVariables(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    setDateVariables(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    timeSelected = calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);
                    dateSelected = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    etPickupDetailsEndDate.setText(DateUtils.getInstance().parseDateAs(dateSelected + " "
                            + timeSelected, "dd/MM/yyyy HH mm", UIManager.getDateTimeFormat()));
                    break;
                case Constants.LayoutType.DELIVERY:
                    calendar.add(Calendar.MINUTE, Constants.END_TIME_BUFFER);
                    isStartDate = false;
                    setTimeVariables(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    setDateVariables(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    timeSelected = calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);
                    dateSelected = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    etDeliveryDetailsLatestDeliveryByTime.setText(DateUtils.getInstance().parseDateAs(dateSelected + " "
                            + timeSelected, "dd/MM/yyyy HH mm", UIManager.getDateTimeFormat()));
                    break;
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    private void createSingleTaskWithPayment() {

        final HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

        if (!Utils.internetCheck(this)) {
            new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        if (paymentMethodEnabled) {
            if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
                sendPaymentForTask();
            } else {
                validateServingDistance();
            }
        } else {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) &&
                    (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME),
                            Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }

            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
                if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                    hashMap.put("is_multi_currency_enabled_app", "1");
                }

                RestClient.getApiInterface(this).createTaskViaVendor(hashMap).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel validReqUrlPojo) {

//                        Bundle bundle = new Bundle();
//                        bundle.putString(Keys.Extras.SUCCESS_MESSAGE, validReqUrlPojo.getMessage());
//                        bundle.putBoolean(IS_PAYMENT_ENABLED, paymentMethodEnabled);
//                        bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
//                        Transition.transitBookingSuccess(mActivity, bundle);

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
                    }
                });
            } else {
                createBookingWithoutLogin(hashMap);
            }
        }


    }

    private void openReviewBothTaskActivity() {
        int forcePickupDelivery = StorefrontCommonData.getFormSettings().getForcePickupDelivery();
        boolean isPaymentEnabled = paymentMethodEnabled;

        if (pickupDetails == null) pickupDetails = new PickupDetails();
        if (deliveryDetails == null) deliveryDetails = new PickupDetails();
        isDeliveryAdded = deliveryDetails.getAddress() != null && !deliveryDetails.getAddress().isEmpty();
        isPickupAdded = pickupDetails.getAddress() != null && !pickupDetails.getAddress().isEmpty();


        if (!Utils.internetCheck(this)) {
            new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }

        if (forcePickupDelivery == 1 && !(isDeliveryAdded && isPickupAdded)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.pick_up_and_delivery_mandatory)).build().show();
        } else {
            if (isPickupAdded && isDeliveryAdded) {
                SimpleDateFormat sdf = new SimpleDateFormat(UIManager.getDateTimeFormat());
                try {
                    Date pickupDateTime = sdf.parse(pickupDetails.getPickupTime());
                    Date deliveryDateTime = sdf.parse(deliveryDetails.getPickupTime());
                    if (deliveryDateTime.getTime() <= pickupDateTime.getTime()) {
                        new AlertDialog.Builder(this).message(getStrings(R.string.invalid_delivery_time))
                                .button(getStrings(R.string.ok_text)).build().show();
                        return;
                    }
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }

//                        if (pickupDetails.getUserOptions() != null && pickupDetails.getUserOptions().getItems() != null) {
//
//                            if (totalDistance == null || totalDistance.isEmpty() || totalDistance.equalsIgnoreCase("0")) {
//                                if (Utils.isMultipleCategoryApp()) {
//                                    getTotalDistance(pickupDetails.getUserOptions(), true);
//                                    return;
//                                }
//                            }
//                        }
            if (isPaymentEnabled) {
                if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
                    sendPaymentForTask();
                } else {
                    validateServingDistance();
                }
            } else {
                HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();
                if (hashMap.containsKey(JOB_PICKUP_DATETIME) && (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME),
                        Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                    hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
                }
                if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                    hashMap.put(IS_APP_MENU_ENABLED, UIManager.isMenuEnabled() ? "1" : "0");
                    if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                        hashMap.put("is_multi_currency_enabled_app", "1");
                    }
                    RestClient.getApiInterface(this).createTaskViaVendor(hashMap).enqueue(new ResponseResolver<BaseModel>
                            (mActivity, true, true) {
                        @Override
                        public void success(BaseModel validReqUrlPojo) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Keys.Extras.SUCCESS_MESSAGE, validReqUrlPojo.getMessage());
//                            bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
//                            Transition.transitBookingSuccess(mActivity, bundle);

                            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ORDER_CREATED_SUCCESS, 0 + "");

                            Bundle bundle = new Bundle();
                            bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                            bundle.putString(Keys.Extras.SUCCESS_MESSAGE, validReqUrlPojo.getMessage());
                            bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
//                            bundle.putSerializable(MakePaymentActivity.class.getName(), hashMap);

                            Transition.transitBookingSuccess(mActivity, bundle);
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();


                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {
                        }
                    });
                } else {
                    createBookingWithoutLogin(hashMap);
                }
            }
        }
    }

    private void setDefaultPhoneDetails(TextView etCountryCode, EditText etPhone) {


//            String[] phoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().replace("-", " ").trim().split(" ");

        try {
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

    private void setDateFields() {

        if (!(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
//                && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT
        )) {

            if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1) || (isStartEndTimeEnabled.equals(1))) {

                if ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getEnableTookanAgent() == 1)) {
                    isScheduled = 0;
                } else {
                    isScheduled = scheduledTask;
                }

                llSelectDateTime.setVisibility(View.VISIBLE);
                tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                rgInstantSchedule.setVisibility(View.GONE);
                llStartDate.setVisibility(View.VISIBLE);
                llEndDateSchedule.setVisibility(View.VISIBLE);
                llStartEndTimeSchedule.setVisibility(View.GONE);

                etStartDateTime.setText("");
                etPickupDetailsreadyForPickup.setText("");
                etEndDateTimeSchedule.setText("");
                etPickupDetailsEndDate.setText("");

                etStartDateTime.setHint(getStrings(R.string.enter) + " " + StorefrontCommonData.getTerminology().getStartTime(false));
                ((MaterialEditText) etStartDateTime).setFloatingLabelText(StorefrontCommonData.getTerminology().getStartTime(true));

            } else {
                instantTask = Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getIsStorefrontOpened() == 0 ? 0 : instantTask;

                llStartEndTimeSchedule.setVisibility(View.GONE);
                llEndDateSchedule.setVisibility(View.GONE);
                llStartDate.setVisibility(View.VISIBLE);
                etStartDateTime.setHint(getStrings(R.string.select_date_time));
                ((MaterialEditText) etStartDateTime).setFloatingLabelText(getStrings(R.string.date_and_time));

                if (instantTask.equals(1) && scheduledTask.equals(1)) {
                    isScheduled = 0;
                    llSelectDateTime.setVisibility(View.VISIBLE);
                    tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                    rgInstantSchedule.setVisibility(View.VISIBLE);
                    llStartDate.setVisibility(View.GONE);

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                    etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                    etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));

                } else if (scheduledTask.equals(1)) {
                    isScheduled = scheduledTask;
                    llSelectDateTime.setVisibility(View.VISIBLE);
                    tvDateTimeHeader.setText(StorefrontCommonData.getTerminology().getScheduleOrder());
                    rgInstantSchedule.setVisibility(View.GONE);
                    llStartDate.setVisibility(View.VISIBLE);

                    etStartDateTime.setText("");
                    etPickupDetailsreadyForPickup.setText("");

                } else if (instantTask.equals(1)) {
                    isScheduled = scheduledTask;
                    llSelectDateTime.setVisibility(View.GONE);

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                    etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                    etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                } else {
                    isScheduled = scheduledTask;
                    llSelectDateTime.setVisibility(View.GONE);

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                    etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                    etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                }
            }

            rgInstantSchedule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbInstant) {
                        isScheduled = 0;
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
                        etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                        etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
                        llStartDate.setVisibility(View.GONE);
                    } else if (checkedId == R.id.rbSchedule) {
                        isScheduled = 1;
                        etStartDateTime.setText("");
                        etPickupDetailsreadyForPickup.setText("");
                        llStartDate.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            isScheduled = 0;
            llSelectDateTime.setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, Constants.START_TIME_BUFFER_INSTANT);
            etStartDateTime.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
            etPickupDetailsreadyForPickup.setText(DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
        }
    }

    private void createBookingWithoutLogin(HashMap hashMap) {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_PAYMENT);

        hashMap.put("amount", getSubTotal());
        Bundle bundle = new Bundle();
        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
        bundle.putString(Keys.Extras.SUCCESS_MESSAGE, getStrings(R.string.order_placed_successfully));
        bundle.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        bundle.putSerializable(MakePaymentActivity.class.getName(), hashMap);

        Transition.transitBookingSuccess(mActivity, bundle);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void resetSelfPickupDeliveryAssets() {
        storefrontData.setSelectedPickupMode(selectedPickupMode);
        if (selectedPickupMode == Constants.SelectedPickupMode.NONE) {
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            tvSelfPickup.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_self_pickup_inactive, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            tvHomeDelivery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_delivery_inactive, 0, 0);
        } else if (selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvSelfPickup);
            tvSelfPickup.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_self_pickup, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            tvHomeDelivery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_delivery_inactive, 0, 0);
            tvAddressHeader.setText((StorefrontCommonData.getTerminology().getPickup(true)) + " " + Utils.capitaliseWords(getStrings(R.string.from)));
            Utils.setTextColor(mActivity, R.color.primary_text_color, etPickupDetailsAddress);
            address = etPickupDetailsAddress.getText().toString();
            etPickupDetailsAddress.setText(storefrontData.getDisplayAddress());
//            ((EditText) findViewById(R.id.etPickupDetailsAddress)).setEnabled(false);
        } else {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvHomeDelivery);
            tvHomeDelivery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_delivery, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            tvSelfPickup.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_self_pickup_inactive, 0, 0);
            tvAddressHeader.setText((StorefrontCommonData.getTerminology().getDeliveryTo(true)));
            Utils.setTextColor(mActivity, R.color.primary_text_color, etPickupDetailsAddress);
//            ((EditText) findViewById(R.id.etPickupDetailsAddress)).setText(address);
//            ((EditText) findViewById(R.id.etPickupDetailsAddress)).setEnabled(true);
        }
    }

    private void validateServingDistance() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("user_id", Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getStorefrontUserId())
                .add("job_pickup_latitude", pickUpLatitude)
                .add("job_pickup_longitude", pickUpLongitude)
                .add("access_token", Dependencies.getAccessToken(mActivity))
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("dual_user_key", UIManager.isDualUserEnable());

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
                                new UnavailableProductsDialog(CheckOutActivityOld.this, unavailableProductData, mActivity, () -> {

                                }).show();
                        } catch (Exception e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
    }

    private void sendPaymentForTask() {
        getCommonParamsBuilder().add(PAYMENT_METHOD_FIELD, valueEnabledPayment + "");
        RestClient.getApiInterface(this).sendPaymentForTask(getCommonParamsBuilder().build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                SendPaymentForTask sendPaymentForTask = new SendPaymentForTask();
                try {
                    sendPaymentForTask.setData(baseModel.toResponseModel(Data.class));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_PAYMENT);
                Bundle extras = new Bundle();
                extras.putLong(VALUE_PAYMENT, valueEnabledPayment);
                extras.putSerializable(CheckOutActivityOld.class.getName(), getCommonParamsBuilder().build().getMap());
                extras.putParcelable(SEND_PAYMENT_FOR_TASK, sendPaymentForTask);
                Intent intent = new Intent(mActivity, MakePaymentActivity.class);
                //  Intent intent = new Intent(mActivity, CheckOutActivity.class);
                intent.putExtras(extras);
                startActivityForResult(intent, OPEN_MAKE_PAYMENT_ACTIVITY);

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }
}