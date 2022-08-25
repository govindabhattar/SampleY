package com.tookancustomer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.ExtraConstants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckListFilterCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckboxCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldDocumentCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldImageCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldTextViewCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.checkoutTemplate.model.CheckoutTemplateStatus;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AdminVerficationDialog;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.SendPaymentTask.Data;
import com.tookancustomer.models.SendPaymentTask.SendPaymentForTask;
import com.tookancustomer.models.appConfiguration.CustomOrderPage;
import com.tookancustomer.models.payViaHippo.CreateCustomOrderData;
import com.tookancustomer.models.staticAddressData.StaticAddressData;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.models.userpages.UserPagesData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.PathUtil;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.UNACCOUNTED;

public class CheckOutCustomActivity extends SideMenuBaseActivity implements View.OnClickListener, Keys.APIFieldKeys, AdminVerficationDialog.AdminVerificationDialogListener {
    private static final int SELECT_USER_PICKUP_ADDRESS = 701;
    // Address fields
    private TextView tvAddressHeader;
    private boolean isAddressPopUpShown = false;
    private Dialog mDialog;
    private ArrayList<StaticAddressData> addressesList;


    private LinearLayout llCustomPickupFields, llAddressCustom;
    private TextView etAddress;
    // Address variables
    private boolean isPickupAddress;
    private Double pickUpLatitude = 0.0, pickUpLongitude = 0.0;
    private Double deliverLatitude = 0.0, deliverLongitude = 0.0;
    private String address = "";
    // User detail fields
    private TextView tvUserCountryCode;
    private LinearLayout llUserNameLayout, llUserEmailLayout, llUserPhoneLayout;
    private EditText etUserName, etUserEmail, etUserPhoneNumber;
    // Cart fields
    private TextView tvProceedToPay, etPickupAddress, etUserPickupAddress;
    // Extra notes fields
    private EditText etDescription;
    private boolean paymentMethodEnabled = false;
    private long valueEnabledPayment;
    private RadioGroup rgPickupType;
    private RadioButton rbAnywhere, rbCustom;
    private boolean isPickupAnywhere = true;
    private boolean isCustomOrder = true;
    private String prefillPhoneNo;
    //laundry
    private LinearLayout llSelectDateTime, layoutPickupAddress, layoutDeliveryAddress;
    private int isScheduled = 0;
    private EditText etStartDateTime, etEndDateTimeSchedule;
    private int serviceTime = 0;
    private TextView tvDateTimeHeader;
    private RadioGroup rgInstantSchedule;
    private LinearLayout llStartDate, llEndDateSchedule;
    private boolean isStartDate;
    private Date endStartDateLaundry;
    private boolean isSideMenuEnabled = false;
    private TextView tvCustomOrderPage;
    private CustomOrderPage customOrderPage;
    private CheckBox cbDeliverAddressSameAsPickup;
    private double customerPickupLat, customerPickupLng;
    private CardView cvPickupOptions;
    //checkoutTemplate
    private boolean isCheckoutTemplateEnabled;
    private ArrayList<Template> templateDataList;
    private CardView cvAdditionalPriceParent;
    private RelativeLayout rlAdditionalPrice, rlTotallPrice;
    private TextView tvAdditionalHeading, tvTotalHeading, tvAdditionalAmount, tvTotalAmount;
    private double finalAmount = 0.0;
    private LinearLayout llCustomFieldsParent, llCustomFields;
    private TextView tvCustomFieldsHeading;
    private CardView cvCustomFields;
    private boolean isCustomFieldOnSameScreen = true, isCustomOrderMerchantLevel = false;

    private int customFieldPosition;
    private Datum storefrontData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_checkout_new);
        mActivity = this;
        addTopLayout(mActivity, R.layout.activity_custom_checkout_new);
        if (UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 0 &&
                StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 0 && storefrontData == null) {
            isSideMenuEnabled = true;
        } else {
            isSideMenuEnabled = false;
        }

        isCustomFieldOnSameScreen = StorefrontCommonData.getAppConfigurationData().getCustomCheckoutTemplateOnSingleScreen() == 1;
        isCustomOrder = (getIntent().getBooleanExtra("isCustomOrder", true));
        isCustomOrderMerchantLevel = (getIntent().getBooleanExtra("isCustomOrderMerchantLevel", false));
        address = getIntent().getStringExtra(PICKUP_ADDRESS) != null ? getIntent().getStringExtra(PICKUP_ADDRESS) : "";
        deliverLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        deliverLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        storefrontData = (Datum) getIntent().getSerializableExtra(STOREFRONT_DATA);

        initializeFields();
        showPaymentSlider();
        setDataFields();

        if (isSideMenuEnabled) {
            unlockSideMenu();
            ((ImageView) findViewById(R.id.ivBack)).setImageResource(R.drawable.ic_icon_menu);
        } else {
            ((ImageView) findViewById(R.id.ivBack)).setImageResource(R.drawable.ic_back);
            lockSideMenu();
        }
        callbackForCheckoutTemplateStatus();

        Utils.showPopup(mActivity, Codes.Request.OPEN_POPUP, new Utils.adDialogInterface() {
            @Override
            public void onAdDialogDismiss() {
                StorefrontCommonData.getAppConfigurationData().setPopupEnabled(false);
            }
        });

        if (isStaticAddressFlow()) {

            // setAddressBarText();
            callbackForStaticAddresses();
        }

    }

    private void callbackForStaticAddresses() {
        UserData userData = StorefrontCommonData.getUserData();


        CommonParams.Builder builder = new CommonParams.Builder();
        builder.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());
//                .add(USER_ID, userData.getData().getVendorDetails().getUserId())
        if (isCustomOrderMerchantLevel) {
            builder.add(USER_ID, storefrontData.getStorefrontUserId() + "");
        } else {
            builder.add(USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());
        }
        builder.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
        builder.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        builder.add(DUAL_USER_KEY, UIManager.isDualUserEnable());
        builder.add("language", "en");


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
        tvDeliveryAddressHeader.setText(StorefrontCommonData.getString(this, R.string.delivery_address));
        tvDeliveringTo.setText(StorefrontCommonData.getString(this, R.string.delivering_to));
        TextView tvAddress = mDialog.findViewById(R.id.tvAddress);
        ImageView ivCloseDialog = mDialog.findViewById(R.id.ivCloseDialog);
        tvAddress.setText(etAddress.getText().toString());
        //}


        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                    gotoMapActivity();
                } else {
                    if (isStaticAddressFlow()) {
                        goToStaticAddressActivity();
                    } else {
                        gotoFavLocationActivity();
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


    private void setDataFields() {
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

            layoutPickupAddress.setVisibility(View.VISIBLE);
            cbDeliverAddressSameAsPickup.setVisibility(View.VISIBLE);
//            cbDeliverAddressSameAsPickup.setText(getStrings(R.string.deliver_details_same_as_pickup_details));

            cbDeliverAddressSameAsPickup.setText(getStrings(R.string.deliveryDetails_same_as_pickupDetails)
                    .replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery())
                    .replace(PICKUP, StorefrontCommonData.getTerminology().getPickup()));

            cbDeliverAddressSameAsPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setCustomerDeliveryAddress(true);
                }
            });

        }
        customOrderPage = UIManager.getCustomOrderPage();
        if (StorefrontCommonData.getAppConfigurationData().getIsDynamicPagesActive() == 1 && customOrderPage != null) {
            tvCustomOrderPage.setVisibility(View.VISIBLE);
            tvCustomOrderPage.setText(createSpan(customOrderPage.getName()));
        }


        setCustomOrderType();

    }

    private void setCustomOrderType() {
        if (StorefrontCommonData.getAppConfigurationData().getIsAnywhereRequired() == 1 &&
                StorefrontCommonData.getAppConfigurationData().getIsCustomRequired() == 1) {
            rgPickupType.setVisibility(View.VISIBLE);
            tvAddressHeader.setVisibility(View.VISIBLE);

        } else if (StorefrontCommonData.getAppConfigurationData().getIsAnywhereRequired() == 1) {
            rbAnywhere.setChecked(true);
            tvAddressHeader.setVisibility(View.GONE);
            cvPickupOptions.setVisibility(View.GONE);
        } else {
            rbCustom.setChecked(true);
            rgPickupType.setVisibility(View.GONE);
            tvAddressHeader.setVisibility(View.VISIBLE);
        }


        if (storefrontData != null) {
            rgPickupType.check(R.id.rbCustom);
            etUserName.setText(storefrontData.getStoreName());
            if (StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1) {
                etUserEmail.setText(storefrontData.getEmail());
                setDefaultPhoneDetails(tvUserCountryCode, etUserPhoneNumber, storefrontData.getPhone());
            }
            if (StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 1) {
                etPickupAddress.setText(storefrontData.getAddress());
                pickUpLatitude = Double.valueOf(storefrontData.getLatitude());
                pickUpLongitude = Double.valueOf(storefrontData.getLongitude());
            }
        }

        if (isCustomOrderMerchantLevel) {
            rbAnywhere.setChecked(true);
            llAddressCustom.setVisibility(View.GONE);
        }
    }


    /**
     * set customer delivery address details in 2 cases
     * whenever checkbox is checked then added user pickup address in deliver address as well else remove delivery address
     * and 2nd case if it comes from selecting pickup address and checkbox is already checked then add pickup address in
     * delivery adress as well
     */
    private void setCustomerDeliveryAddress(boolean isCheckboxEvent) {
        if (cbDeliverAddressSameAsPickup.isChecked()) {
            etAddress.setText(etUserPickupAddress.getText().toString());
            deliverLatitude = customerPickupLat;
            deliverLongitude = customerPickupLng;
            layoutDeliveryAddress.setVisibility(View.GONE);
        } else {
            if (isCheckboxEvent) {
                etAddress.setText("");
                deliverLatitude = 0.0;
                deliverLongitude = 0.0;
                layoutDeliveryAddress.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setProceedButtonText() {
        if (isCheckoutTemplateEnabled && templateDataList == null && !isCustomFieldOnSameScreen) {
            ((TextView) findViewById(R.id.tvProceedToPay)).setText(getStrings(R.string.proceed));
        } else {
            if (paymentMethodEnabled) {
                tvProceedToPay.setText(StorefrontCommonData.getTerminology().getProceedToPay(true));
            } else {
                tvProceedToPay.setText(getStrings(R.string.place_order).replace(ORDER, Utils.getCallTaskAs(true, true)));
            }
        }

        if (UIManager.isCustomQuotationEnabled() && !UIManager.isHoldPaymentEnabled()) {
            tvProceedToPay.setText(getStrings(R.string.submit));
        }
    }

    private void callbackForCheckoutTemplateStatus() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId())
                .add("user_id", userData.getData().getVendorDetails().getMarketplaceUserId())
                .add("type", 1);

        RestClient.getApiInterface(this).getCheckoutTemplateStatus(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        CheckoutTemplateStatus templateStatus = baseModel.toResponseModel(CheckoutTemplateStatus.class);
                        isCheckoutTemplateEnabled = templateStatus.getIsCheckoutTemplateEnabled() == 1;
                        setProceedButtonText();

                        if (isCheckoutTemplateEnabled && isCustomFieldOnSameScreen) {
                            callbackForCheckoutTemplate();
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });
    }


    private void callbackForCheckoutTemplate() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
        builder.add("user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
        builder.add("type", 1);

        RestClient.getApiInterface(this).getCheckoutTemplate(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        com.tookancustomer.checkoutTemplate.model.Data checkoutData = baseModel.toResponseModel(com.tookancustomer.checkoutTemplate.model.Data.class);
                        templateDataList = checkoutData.getTemplate();
                        renderCustomFieldsUi(templateDataList);
                        llCustomFieldsParent.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }


    private void renderCustomFieldsUi(ArrayList<Template> templateDataList) {
        llCustomFields.removeAllViews();
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);
            switch (template.getDataType()) {

                case CustomViewsConstants.CHECKBOX:
                case CustomViewsConstants.SINGLE_SELCT:
                    CustomFieldCheckboxCheckout checkboxFreelancer = new CustomFieldCheckboxCheckout(mActivity, false);
                    llCustomFields.addView(checkboxFreelancer.render(template));
                    break;
                case CustomViewsConstants.NUMBER:
                case CustomViewsConstants.EMAIL:
                case CustomViewsConstants.TELEPHONE:
                case CustomViewsConstants.URL:
                case CustomViewsConstants.DATE:
                case CustomViewsConstants.DATE_FUTURE:
                case CustomViewsConstants.DATE_PAST:
                case CustomViewsConstants.DATE_TODAY:
                case CustomViewsConstants.DATETIME:
                case CustomViewsConstants.DATETIME_FUTURE:
                case CustomViewsConstants.DATETIME_PAST:
                case CustomViewsConstants.TEXT:
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    CustomFieldTextViewCheckout editTextFreelancer = new CustomFieldTextViewCheckout(mActivity, false);
                    llCustomFields.addView(editTextFreelancer.render(template));
                    break;
                case CustomViewsConstants.MULTI_SELECT:
                    template.setAllowedValuesWithIsSelected();
                    CustomFieldCheckListFilterCheckout customFieldCheckListFilter = new CustomFieldCheckListFilterCheckout(mActivity);
                    llCustomFields.addView(customFieldCheckListFilter.render(template, false));

                    break;

                case CustomViewsConstants.IMAGE:
                    CustomFieldImageCheckout customFieldImageFreelancer =
                            new CustomFieldImageCheckout(mActivity, i == templateDataList.size() - 1, false);
                    llCustomFields.addView(customFieldImageFreelancer.render(template));
                    break;
                case CustomViewsConstants.DOCUMENT:
                    CustomFieldDocumentCheckout customFieldDocumentCheckout =
                            new CustomFieldDocumentCheckout(mActivity, i == templateDataList.size() - 1, false);
                    llCustomFields.addView(customFieldDocumentCheckout.render(template));
                    break;

            }
        }
    }

    public void setCustomFieldPosition(Template item) {
        this.customFieldPosition = templateDataList.indexOf(item);
    }

    public void deleteCustomFieldImageSignup(int position) {
        CustomFieldImageCheckout cFIVDeleteImage = getCustomFieldImageView();
        if (cFIVDeleteImage != null) {
            cFIVDeleteImage.deleteImage(position);
        }
    }

    public void deleteCustomFieldDocumentSignup(int position) {
        CustomFieldDocumentCheckout cFIVDeleteDocument = getCustomFieldDocumentView();
        if (cFIVDeleteDocument != null) {
            cFIVDeleteDocument.deleteImage(position);
        }
    }

    private CustomFieldImageCheckout getCustomFieldImageView() {
        List<Template> customFieldsList = templateDataList;
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldImageCheckout ? (CustomFieldImageCheckout) customFieldView : null;
    }

    private CustomFieldDocumentCheckout getCustomFieldDocumentView() {
        List<Template> customFieldsList = templateDataList;
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldDocumentCheckout ? (CustomFieldDocumentCheckout) customFieldView : null;
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

    @Override
    public void onBackPressed() {
        if (isSideMenuEnabled) {
            performMainBackPress();
        } else {
            Bundle extras = new Bundle();
            extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            Intent intent = new Intent();
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void initializeFields() {
        // Main Screen fields
        ((TextView) findViewById(R.id.tvHeading)).setText(StorefrontCommonData.getTerminology().getCheckout());
        etPickupAddress = findViewById(R.id.etPickupAddress);
        etUserPickupAddress = findViewById(R.id.etUserPickupAddress);
        tvProceedToPay = findViewById(R.id.tvProceedToPay);
        tvCustomOrderPage = findViewById(R.id.tvCustomOrderPage);
        llAddressCustom = findViewById(R.id.llAddressCustom);
        setProceedButtonText();

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), tvProceedToPay, tvCustomOrderPage);

        // Address fields
        tvAddressHeader = findViewById(R.id.tvAddressHeader);

//        tvAddressHeader.setText((StorefrontCommonData.getTerminology().getPickup(true)));
        tvAddressHeader.setText(getStrings(R.string.store_options).replace(STORE, StorefrontCommonData.getTerminology().getPickup(true)));
        etAddress = findViewById(R.id.etAddress);
        etAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        etPickupAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        etUserPickupAddress.setHint(getStrings(R.string.add_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        /**
         * sets current address in etAddress or etUserPickupAddress based on laundry flow or any else
         */
        setDefaultAddress();

        //laundry
        llSelectDateTime = findViewById(R.id.llSelectDateTime);


        // UserDetails fields
        ((TextView) findViewById(R.id.tvUserDetailsHeader)).setText(StorefrontCommonData.getTerminology().getDeliveryTo(true));
//        ((TextView) findViewById(R.id.tvUserPickupDetailsHeader)).setText(StorefrontCommonData.getTerminology().getPickup(true));
        ((TextView) findViewById(R.id.tvUserPickupDetailsHeader)).setText(StorefrontCommonData.getTerminology().getPickupFrom(true));
        ((TextView) findViewById(R.id.tvUserNameLabel)).setText(getStrings(R.string.name));
        llUserNameLayout = findViewById(R.id.llUserNameLayout);
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
        llUserEmailLayout = findViewById(R.id.llUserEmailLayout);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserEmail.setHint(getStrings(R.string.enter_email));

        ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText(getStrings(R.string.phone));
        tvUserCountryCode = findViewById(R.id.tvUserCountryCode);
        tvUserCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        llUserPhoneLayout = findViewById(R.id.llUserPhoneLayout);
        etUserPhoneNumber = findViewById(R.id.etUserPhoneNumber);
        etUserPhoneNumber.setHint(getStrings(R.string.enter_phone_number));

        Utils.setOnClickListener(this, findViewById(R.id.llUserCountryCode));


        //checkout Template
        llCustomFieldsParent = findViewById(R.id.llCustomFieldsParent);
        llCustomFields = findViewById(R.id.llCustomFields);
        tvCustomFieldsHeading = findViewById(R.id.tvCustomFieldsHeading);
        tvCustomFieldsHeading.setText(StorefrontCommonData.getTerminology().getCheckoutTemplate());

        cvCustomFields = findViewById(R.id.cvCustomFields);

        cvAdditionalPriceParent = findViewById(R.id.cvAdditionalPriceParent);
        rlAdditionalPrice = findViewById(R.id.rlAdditionalPrice);
//        rlTotallPrice = findViewById(R.id.rlTotallPrice);
//        tvTotalHeading = findViewById(R.id.tvTotalHeading);
        tvAdditionalHeading = findViewById(R.id.tvAdditionalHeading);
        tvAdditionalAmount = findViewById(R.id.tvAdditionalAmount);

        //radio buttons on pickup
        rgPickupType = findViewById(R.id.rgPickupType);
        rbAnywhere = findViewById(R.id.rbAnywhere);
        rbAnywhere.setText(StorefrontCommonData.getTerminology().getAnywhere());
        rbCustom = findViewById(R.id.rbCustom);
        llCustomPickupFields = findViewById(R.id.llCustomPickupFields);
        rbCustom.setText(StorefrontCommonData.getTerminology().getCustom());
        rgPickupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbAnywhere.getId()) {
                    llCustomPickupFields.setVisibility(View.GONE);
                    isPickupAnywhere = true;
                } else {
                    llCustomPickupFields.setVisibility(View.VISIBLE);

                    llUserNameLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getCustomOrderName() == Constants.CustomOrderFields.HIDDEN ? View.GONE : View.VISIBLE);
                    Spannable userNameLabel = CustomViewsUtil.createSpan(mActivity, getStrings(R.string.name), StorefrontCommonData.getAppConfigurationData().getCustomOrderName() == Constants.CustomOrderFields.MANDATORY ? "*" : "");
                    ((TextView) findViewById(R.id.tvUserNameLabel)).setText((userNameLabel));

                    llUserEmailLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() == Constants.CustomOrderFields.HIDDEN ? View.GONE : View.VISIBLE);
                    Spannable userEmailLabel = CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() == Constants.CustomOrderFields.MANDATORY ? "*" : "");
                    ((TextView) findViewById(R.id.tvUserEmailabel)).setText((userEmailLabel));

                    llUserPhoneLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() == Constants.CustomOrderFields.HIDDEN ? View.GONE : View.VISIBLE);
                    Spannable userPhoneLabel = CustomViewsUtil.createSpan(mActivity, getStrings(R.string.phone), StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() == Constants.CustomOrderFields.MANDATORY ? "*" : "");
                    ((TextView) findViewById(R.id.tvUserPhoneLabel)).setText((userPhoneLabel));

                    isPickupAnywhere = false;
                }
            }
        });

        // DateAndTime fields for laundry
        llSelectDateTime = findViewById(R.id.llSelectDateTime);
        tvDateTimeHeader = findViewById(R.id.tvDateTimeHeader);
        tvDateTimeHeader.setText(getStrings(R.string.checkout_select_date_time));
        rgInstantSchedule = findViewById(R.id.rgInstantSchedule);
        llStartDate = findViewById(R.id.llStartDate);
        etStartDateTime = findViewById(R.id.etStartDateTime);
        etStartDateTime.setHint(getStrings(R.string.select_date_time));
        ((MaterialEditText) etStartDateTime).setFloatingLabelText(getStrings(R.string.date_and_time));
        llEndDateSchedule = findViewById(R.id.llEndDateSchedule);
        etEndDateTimeSchedule = findViewById(R.id.etEndDateTimeSchedule);
        etEndDateTimeSchedule.setHint(getStrings(R.string.enter_terminology).replace(TerminologyStrings.TERMINOLOGY, StorefrontCommonData.getTerminology().getEndTime(false)));
        ((MaterialEditText) etEndDateTimeSchedule).setFloatingLabelText(StorefrontCommonData.getTerminology().getEndTime(true));

        Utils.setOnClickListener(this, etAddress, etPickupAddress, etUserPickupAddress, etStartDateTime, etEndDateTimeSchedule, tvAdditionalHeading);
        // Notes fields
        ((TextView) findViewById(R.id.tvNotesHeader)).setText(StorefrontCommonData.getTerminology().getYourOrderDescription());

        etDescription = findViewById(R.id.etDescription);
        etDescription.setHint(StorefrontCommonData.getTerminology().getEnterDescriptionHere());
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

        setDefaultPhoneDetails();

        layoutPickupAddress = findViewById(R.id.layoutPickupAddress);
        layoutDeliveryAddress = findViewById(R.id.layoutDeliveryAddress);
        cbDeliverAddressSameAsPickup = findViewById(R.id.cbDeliverAddressSameAsPickup);
        cvPickupOptions = findViewById(R.id.cvPickupOptions);

    }

    /**
     * if it is laundry custom order then set current address is etUserPickupAddress
     * else set in etAddress
     */
    private void setDefaultAddress() {
        if (!Dependencies.isLaundryApp())
            if (!isStaticAddressFlow())
                etAddress.setText(address);
            else if (!isStaticAddressFlow())
                etUserPickupAddress.setText(address);
    }

    private String getSubTotal() {
        return Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal());
    }

    private void showPaymentSlider() {
        valueEnabledPayment = PaymentMethodsClass.getEnabledPaymentMethod();
        paymentMethodEnabled = PaymentMethodsClass.isPaymentEnabled();
        setProceedButtonText();
    }

    private void setDefaultPhoneDetails() {
//            String[] phoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().replace("-", " ").trim().split(" ");
        try {
//            String[] phoneNumber = Utils.splitNumberByCode(this, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());
            String[] phoneNumber = Utils.splitNumberByCodeNew(this, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());
            prefillPhoneNo = phoneNumber[0] + "" + phoneNumber[1].replace("+", "");

        } catch (Exception e) {
            String countryCode = Utils.getCountryCode(mActivity, StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().trim());
//            etCountryCode.setText(countryPicker.getUserCountryInfo(this).getDialCode());
            prefillPhoneNo = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo().replace(countryCode, "").replace("+", "").replace("-", " ").trim();

        }
    }

    private void setDefaultPhoneDetails(TextView etCountryCode, EditText etPhone, String phoneNo) {
//            String[] phoneNumber = phoneNo.replace("-", " ").trim().split(" ");
        try {
            String[] phoneNumber = Utils.splitNumberByCodeNew(this, phoneNo);

            etCountryCode.setText(phoneNumber[0]);
            etPhone.setText(phoneNumber[1].replace("+", ""));
        } catch (Exception e) {
            String countryCode = Utils.getCountryCode(mActivity, phoneNo.trim());
            etCountryCode.setText(countryCode);
//            etCountryCode.setText(countryPicker.getUserCountryInfo(this).getDialCode());
            etPhone.setText(phoneNo.replace(countryCode, "").replace("+", "").replace("-", " ").trim());
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.rlBack:
                if (isSideMenuEnabled) {
                    openSideMenu();
                } else {
                    onBackPressed();
                }
                break;

            case R.id.etAddress:
                isPickupAddress = false;
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

                break;
            case R.id.etPickupAddress:
                isPickupAddress = true;
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_ADDRESS);

                if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                    gotoMapActivity();
                } else {
                    gotoFavLocationActivity();

                }

                break;

            case R.id.llUserCountryCode:
                Utils.hideSoftKeyboard(this, etUserPhoneNumber);
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvUserCountryCode.setText(country.getCountryCode());
                        etUserPhoneNumber.requestFocus();
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();
                break;

            case R.id.etStartDateTime:
            case R.id.etEndDateTimeSchedule:
                isStartDate = i == R.id.etStartDateTime;

                if (Dependencies.isLaundryApp()) {
                    if (!isStartDate && getStartTime().isEmpty()) {
                        Utils.snackBar(mActivity, getStrings(R.string.pickup_dateTime_is_required).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)));
                        return;
                    }
                    Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                    intent.putExtra(Keys.Extras.IS_START_TIME, isStartDate);
                    intent.putExtra(Keys.Extras.SELECTED_DATE, !isStartDate ? DateUtils.getInstance().getFormattedDate(endStartDateLaundry,
                            UIManager.getDateTimeFormat()) : getEndTime());
                    if (!isStartDate) intent.putExtra("service_time", serviceTime);
                    intent.putExtra("isCustomOrder", isCustomOrder);
                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                }
                break;
            case R.id.tvProceedToPay:
                if (isCheckoutTemplateEnabled && templateDataList == null && !isCustomFieldOnSameScreen) {
                    if (isValidTaskDetails()) {
                        Intent templateIntent = new Intent(this, CheckoutTemplateActivity.class);
                        templateIntent.putExtra(CheckoutTemplateConstants.IS_CUSTOM_ORDER, true);
                        templateIntent.putExtra(STOREFRONT_DATA, storefrontData);

                        templateIntent.putExtra("isCustomOrderMerchantLevel", isCustomOrderMerchantLevel);
                        startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);
                    }
                } else {
                    if (!Utils.internetCheck(this)) {
                        new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                        return;
                    }
                    if (UIManager.isCustomerLoginRequired() || (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                        if (UIManager.isCutomerVerifactionRequired()) {
                            AdminVerficationDialog.getInstance(this).show(getSupportFragmentManager(), "ADMIN_VERIFICATION");
                            return;
                        }

                        if (isValidTaskDetails() && verifyData()) {
                            createSingleTaskWithPayment();
                        }
                    } else {
                        Dependencies.setDemoRun(false);

                        Intent intent;
                        if (UIManager.isFacebookAvailable() || UIManager.isInstagramAvailable() || UIManager.isGPlusAvailable() || UIManager.isOtpLoginAvailable()) {
                            intent = new Intent(mActivity, SplashActivity.class);
                        } else {
                            intent = new Intent(mActivity, SignInActivity.class);
                        }
                        intent.putExtra(CheckOutActivity.class.getName(), true);
                        mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
                        AnimationUtils.forwardTransition(mActivity);
                    }
                }
                break;
            case R.id.tvCustomOrderPage:
                if (customOrderPage != null)
                    getMerchantDynamicPages(customOrderPage.getRoute());
                break;

            case R.id.etUserPickupAddress:
                selectUserPickupAddress();
                break;

            case R.id.tvAdditionalHeading:
                if (!isCustomFieldOnSameScreen) {
                    Intent templateIntent = new Intent(this, CheckoutTemplateActivity.class);
                    if (templateDataList != null) {
                        templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
                        templateIntent.putExtra(CheckoutTemplateConstants.IS_CUSTOM_ORDER, true);
                        templateIntent.putExtra(STOREFRONT_DATA, storefrontData);
                        templateIntent.putExtra("isCustomOrderMerchantLevel", isCustomOrderMerchantLevel);
                        templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, false);
                    }
                    startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);
                }
                break;

        }

    }

    private void selectUserPickupAddress() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, SELECT_USER_PICKUP_ADDRESS);
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

    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
//      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    private boolean isValidTaskDetails() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String phone = etUserPhoneNumber.getText().toString();
        String address = etAddress.getText().toString();
        String addressPickup = etPickupAddress.getText().toString();
        String userPickupAddress = etUserPickupAddress.getText().toString();
        String description = etDescription.getText().toString().trim();
        String startTime = getStartTime();
        String endTime = getEndTime();


        if (TextUtils.isEmpty(description)) {
            Utils.snackBar(this, getStrings(R.string.check_description_text_term).replace(TerminologyStrings.YOUR_ORDER_DESCRIPTION, StorefrontCommonData.getTerminology().getYourOrderDescription()));
            return false;
        }


        if (llCustomPickupFields.getVisibility() == View.VISIBLE) {

            if (StorefrontCommonData.getAppConfigurationData().getCustomOrderName() == Constants.CustomOrderFields.MANDATORY) {
                if (TextUtils.isEmpty(name)) {
                    Utils.snackBar(this, getStrings(R.string.please_enter_pickup_name));
                    return false;
                }
            }


            if (StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() != Constants.CustomOrderFields.HIDDEN) {
                if (StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() == Constants.CustomOrderFields.MANDATORY) {
                    if (TextUtils.isEmpty(email)) {
                        Utils.snackBar(this, getStrings(R.string.email_is_required));
                        etUserEmail.requestFocus();
                        return false;
                    }
                }
                if (StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() == Constants.CustomOrderFields.MANDATORY
                        || !TextUtils.isEmpty(email)) {
                    if (!Utils.isEmailValid(email)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_email));
                        etUserEmail.requestFocus();
                        return false;
                    }
                }
            }
            if (StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() != Constants.CustomOrderFields.HIDDEN) {
                if (StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() == Constants.CustomOrderFields.MANDATORY) {
                    if (TextUtils.isEmpty(phone)) {
                        Utils.snackBar(this, getStrings(R.string.error_enter_contact));
                        etUserPhoneNumber.requestFocus();
                        return false;
                    }
                }
                if (StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() == Constants.CustomOrderFields.MANDATORY
                        || !TextUtils.isEmpty(phone)) {
                    if (!Utils.isValidPhoneNumber(phone)) {
                        Utils.snackBar(this, getStrings(R.string.enter_valid_phone));
                        etUserPhoneNumber.requestFocus();
                        return false;
                    }
                }
            }
            if (TextUtils.isEmpty(addressPickup)) {
                Utils.snackBar(this, getStrings(R.string.pls_enter_store_address));
                return false;
            }
        }

        if (Dependencies.isLaundryApp()) {
            if (TextUtils.isEmpty(userPickupAddress)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_pickup_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                return false;
            }

            if (TextUtils.isEmpty(address)) {
                Utils.snackBar(this, getStrings(R.string.please_enter_delivery_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                return false;
            }


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

        }

        if (!Dependencies.isLaundryApp() && TextUtils.isEmpty(address)) {
            Utils.snackBar(this, getStrings(R.string.please_enter_deliveryAddress).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
            return false;
        }

        if ((StorefrontCommonData.getAppConfigurationData().getIsShowDeliveryPopup() == 1)
                && (!isAddressPopUpShown) && (!Dependencies.isLaundryApp())) {
            showAddressDialog();
            return false;
        }


        return true;
    }

    private void createSingleTaskWithPayment() {
        final HashMap<String, String> hashMap = getCommonParamsBuilder().build().getMap();

        if (UIManager.isCustomQuotationEnabled() && !UIManager.isHoldPaymentEnabled() && !isCustomOrderMerchantLevel) {
            apiHitForCreateTaskViaHippo(hashMap);
        } else if (paymentMethodEnabled) {
            sendPaymentForTask();
        } else {
            if (hashMap.containsKey(JOB_PICKUP_DATETIME) &&
                    (DateUtils.getInstance().getDateFromString(hashMap.get(JOB_PICKUP_DATETIME), Constants.DateFormat.STANDARD_DATE_FORMAT).before(Calendar.getInstance().getTime()))) {
                hashMap.put(JOB_PICKUP_DATETIME, DateUtils.getInstance().getFormattedDate(Calendar.getInstance().getTime(), Constants.DateFormat.STANDARD_DATE_FORMAT));
            }
            if (isCheckoutTemplateEnabled && templateDataList != null) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
                hashMap.put("checkout_template", String.valueOf(myCustomArray));
            }
            if (isCustomOrder) {
                hashMap.put("is_custom_order", "1");
                if (isPickupAnywhere) {
                    hashMap.put("is_pickup_anywhere", "1");
                }
                if (Dependencies.isLaundryApp()) {
                    RestClient.getApiInterface(this).createTaskLaundryCustom(hashMap).enqueue(createTaskResponseResolver(hashMap));
                } else {
                    if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                        hashMap.put("is_multi_currency_enabled_app", "1");
                    }
                    RestClient.getApiInterface(mActivity).createTaskViaVendorCustom(hashMap).enqueue(createTaskResponseResolver(hashMap));
                }
            }
        }
    }

    private ResponseResolver<BaseModel> createTaskResponseResolver(final HashMap<String, String> hashMap) {

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

            }
        };
    }

    private ResponseResolver<BaseModel> createTaskCustomQuotationResponseResolver() {

        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CreateCustomOrderData data = baseModel.toResponseModel(CreateCustomOrderData.class);
                Bundle bundle = new Bundle();
                bundle.putInt(JOB_ID, Integer.valueOf(data.getJobId()));
                bundle.putInt("from", 1);
                Intent intent = new Intent(CheckOutCustomActivity.this, TaskDetailsNewActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, OPEN_TASKDETAILS_ACTIVITY);
//                finish();
            }

            @Override
            public void failure(APIError error, final BaseModel baseModel) {
                try {

                    if (baseModel != null && ((LinkedTreeMap) baseModel.data).containsKey("debt_amount")) {

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
                }
            }
        };
    }

    private void apiHitForCreateTaskViaHippo(HashMap<String, String> hashMap) {

        if (isCustomOrder && isPickupAnywhere) {
            hashMap.put("is_pickup_anywhere", "1");
        }
        if (isCustomOrder)
            hashMap.put("is_custom_order", "2");

        if (templateDataList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
            hashMap.put("checkout_template", String.valueOf(myCustomArray));
        }

        hashMap.put(PAYMENT_METHOD_FIELD, String.valueOf(UNACCOUNTED.intValue));

        if (Dependencies.isLaundryApp()) {
            RestClient.getApiInterface(this).createTaskLaundryCustom(hashMap)
                    .enqueue(createTaskCustomQuotationResponseResolver());

        } else {

            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                hashMap.put("is_multi_currency_enabled_app", "1");
            }
            RestClient.getApiInterface(this).createTaskViaVendorCustom(hashMap)
                    .enqueue(createTaskCustomQuotationResponseResolver());
        }
    }

    private CommonParams.Builder getCommonParamsBuilder() {
        int layoutType = StorefrontCommonData.getFormSettings().getWorkFlow();
        layoutType = layoutType == Constants.LayoutType.PICKUP || layoutType == Constants.LayoutType.DELIVERY ? Constants.LayoutType.PICKUP_DELIVERY : layoutType;
        CommonParams.Builder builder = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        builder.add(HAS_DELIVERY, 1)
                .add(HAS_PICKUP, 1)
                .add(JOB_PICKUP_ADDRESS, etPickupAddress.getText().toString())
                .add(CUSTOMER_EMAIL, StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail().trim())
                .add(CUSTOMER_PHONE, prefillPhoneNo)
                .add(LONGITUDE, deliverLongitude)
                .add(LATITUDE, deliverLatitude)
                .add(CUSTOMER_USERNAME, StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName())
                .add(CUSTOMER_ADDRESS, etAddress.getText().toString())
                .add(JOB_PICKUP_EMAIL, StorefrontCommonData.getAppConfigurationData().getCustomOrderEmail() != Constants.CustomOrderFields.HIDDEN && !isPickupAnywhere ? etUserEmail.getText().toString() : "")
                .add(JOB_PICKUP_PHONE, StorefrontCommonData.getAppConfigurationData().getCustomOrderPhone() != Constants.CustomOrderFields.HIDDEN && !isPickupAnywhere ? tvUserCountryCode.getText().toString() + etUserPhoneNumber.getText().toString() : "")
                .add(JOB_PICKUP_NAME, StorefrontCommonData.getAppConfigurationData().getCustomOrderName() != Constants.CustomOrderFields.HIDDEN && !isPickupAnywhere ? etUserName.getText().toString() : "")
                .add(JOB_PICKUP_LATITUDE, pickUpLatitude)
                .add(JOB_PICKUP_LONGITUDE, pickUpLongitude)
                .add(IS_SCHEDULED, isScheduled);

        if (Dependencies.isLaundryApp()) {
            builder.add(JOB_PICKUP_DATETIME, getStartTime())
                    .add(JOB_DELIVERY_DATETIME, getEndTime())
                    .add("customer_pickup_address", etUserPickupAddress.getText().toString())
                    .add("customer_pickup_latitude", customerPickupLat)
                    .add("customer_pickup_longitude", customerPickupLng);
        } else {
            builder.add(JOB_PICKUP_DATETIME, DateUtils.getInstance().parseDateAs("",
                    UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT))
                    .add(JOB_DELIVERY_DATETIME, DateUtils.getInstance().parseDateAs("",
                            UIManager.getDateTimeFormat(), Constants.DateFormat.STANDARD_DATE_FORMAT));
        }


        builder.add("currency_id", Utils.getCurrencyId());
        builder.add("is_custom_order", "1");

        if (!isCustomOrderMerchantLevel)
            builder.add(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());


        if (paymentMethodEnabled && valueEnabledPayment != 0) {
            builder.add("payment_method", valueEnabledPayment);
        } else {
            builder.add("payment_method", 0);
        }

        builder.add("currency_id", Utils.getCurrencyId());


        if (paymentMethodEnabled && valueEnabledPayment != 0) {
            builder.add("payment_method", valueEnabledPayment);
        } else {
            builder.add("payment_method", 0);
        }

        addPickupCustomField(builder, StorefrontCommonData.getUserData().getData().getUserOptions());

        builder.add(JOB_DESCRIPTION_FIELD, etDescription.getText().toString())
                .add(AUTO_ASSIGNMENT, 1)
                .add(LAYOUT_TYPE, layoutType)
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add(VERTICAL, UIManager.getVertical());

        if (!isCustomOrder && !Dependencies.isLaundryApp())
            builder.add("is_app_product_tax_enabled", 1);
        return builder;
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

    private void sendPaymentForTask() {
        getCommonParamsBuilder().add(PAYMENT_METHOD_FIELD, valueEnabledPayment + "").build();

//        RestClient.getApiInterface(this).sendPaymentForTask(getCommonParamsBuilder().build().getMap())
//                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
//                    @Override
//                    public void success(BaseModel baseModel) {
        SendPaymentForTask sendPaymentForTask = new SendPaymentForTask();
        try {
            sendPaymentForTask.setData(new Data());
            sendPaymentForTask.getData().setPerTaskCost(String.valueOf(0.0));
        } catch (Exception e) {
            Utils.printStackTrace(e);

        }


        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_PAYMENT);
        Bundle extras = new Bundle();
        extras.putLong(VALUE_PAYMENT, valueEnabledPayment);
        extras.putBoolean("isPickupAnywhere", isPickupAnywhere);
        extras.putBoolean("isCustomOrder", isCustomOrder);
        if (storefrontData != null && storefrontData.getStorefrontUserId() != null) {
            extras.putBoolean("isCustomOrderMerchantLevel", isCustomOrderMerchantLevel);
            extras.putString("merchantUserId", storefrontData.getStorefrontUserId() + "");
            extras.putSerializable("merchantPaymentMethods", (Serializable) storefrontData.getPaymentMethods());
        } else {
            extras.putBoolean("isCustomOrderMerchantLevel", false);
        }
        if (isCheckoutTemplateEnabled && templateDataList != null)
            extras.putSerializable(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
        extras.putSerializable(CheckOutCustomActivity.class.getName(), getCommonParamsBuilder().build().getMap());
        extras.putParcelable(SEND_PAYMENT_FOR_TASK, sendPaymentForTask);
        Intent intent = new Intent(mActivity, MakePaymentActivity.class);
        //  Intent intent = new Intent(mActivity, CheckOutActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, OPEN_MAKE_PAYMENT_ACTIVITY);

        //}

//                    @Override
//                    public void failure(APIError error, BaseModel baseModel) {
//                    }
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        if (requestCode == Codes.Request.OPEN_POPUP) {
            Utils.dismissAdDialog();
            return;
        }
        switch (requestCode) {
            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (isPickupAddress) {
                        etPickupAddress.setText(data.getStringExtra("address"));
                        pickUpLatitude = data.getDoubleExtra("latitude", 0.0);
                        pickUpLongitude = data.getDoubleExtra("longitude", 0.0);

                    } else {
                        etAddress.setText(data.getStringExtra("address"));
                        deliverLatitude = data.getDoubleExtra("latitude", 0.0);
                        deliverLongitude = data.getDoubleExtra("longitude", 0.0);
                    }
                }
                break;
            case Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    etAddress.setText(data.getStringExtra("address"));
                    deliverLatitude = data.getDoubleExtra("latitude", 0.0);
                    deliverLongitude = data.getDoubleExtra("longitude", 0.0);

                }
                break;

            case SELECT_USER_PICKUP_ADDRESS:
                if (resultCode == RESULT_OK) {
                    etUserPickupAddress.setText(data.getStringExtra("address"));
                    customerPickupLat = data.getDoubleExtra("latitude", 0.0);
                    customerPickupLng = data.getDoubleExtra("longitude", 0.0);
                    setCustomerDeliveryAddress(false);
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

            case Codes.Request.OPEN_TASKDETAILS_ACTIVITY:
            case Codes.Request.OPEN_MAKE_PAYMENT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");

                        if (isSideMenuEnabled) {
                            restartActivity();
                        } else {
                            setResult(RESULT_OK, data);
                            Transition.exit(this);
                        }

                    }
                }
                break;

            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    tvProceedToPay.performClick();
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
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {

                    CustomFieldImageCheckout cFIVCamera = getCustomFieldImageView();
                    if (cFIVCamera != null) {
                        cFIVCamera.compressAndSaveImageBitmap();
                    }
                }
                break;
            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    CustomFieldImageCheckout cFIMVGallery = getCustomFieldImageView();
                    if (cFIMVGallery != null) {
                        try {
                            cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                            cFIMVGallery.compressAndSaveImageBitmap();
                        } catch (IOException e) {

                            Utils.printStackTrace(e);
                            Utils.toast(mActivity, getStrings(R.string.could_not_read_image));
                        }
                    }

                }
                break;
            case Codes.Request.OPEN_STORAGE_DOCUMENT:
                if (resultCode == RESULT_OK) {
                    CustomFieldDocumentCheckout cFIMVGallery = getCustomFieldDocumentView();
                    if (cFIMVGallery != null) {
                        try {
                            Log.e("doc", "doc");
                            Uri selectedFileURI = data.getData();
                            String fullPath = PathUtil.getPath(this, selectedFileURI);
                            File file = new File(fullPath);

//                            cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                            cFIMVGallery.uploadCustomFieldImage(file);
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                            Utils.toast(mActivity, getStrings(R.string.could_not_read_file));
                        }
                    }

                }
                break;
        }
    }

    public Spannable createSpan(String label) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(label);
            wordToSpan.setSpan(new UnderlineSpan(), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

            Utils.printStackTrace(e);
            return wordToSpan;
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
        if (!isCustomFieldOnSameScreen || cost > 0) {

            if (cost > 0) {
                if (isCustomOrderMerchantLevel)

                    tvAdditionalAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew((storefrontData.getPaymentSettings() != null && storefrontData.getPaymentSettings().getSymbol() != null) ?
                            storefrontData.getPaymentSettings().getSymbol() : null) + "" + Utils.getDoubleTwoDigits(cost)));

                else
                    tvAdditionalAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(cost)));

//                tvAdditionalAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(cost)));
            } else
                tvAdditionalAmount.setText("");


            cvAdditionalPriceParent.setVisibility(View.VISIBLE);
            //rlTotallPrice.setVisibility(View.VISIBLE);
            //tvTotalAmount.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(cost));
            finalAmount = cost;
            tvAdditionalHeading.setText(CustomViewsUtil.createSpan(mActivity, cost > 0 ? getStrings(R.string.additional_amount) :
                            getStrings(R.string.additional_info),
                    " (", getStrings(R.string.edit), ")", ContextCompat.getColor(mActivity, R.color.colorPrimary)));

        }

    }


    public void getMerchantDynamicPages(String route) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("is_admin_page", 1);
        commonParams.add("route", route);
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        commonParams.add(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());

        RestClient.getApiInterface(mActivity).getUserPages(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserPagesData userPagesData = baseModel.toResponseModel(UserPagesData.class);

                if (userPagesData.getTemplateData().size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(HEADER_WEBVIEW, userPagesData.getTemplateData().get(0).getName());
                    bundle.putString(URL_WEBVIEW, userPagesData.getTemplateData().get(0).getTemplateData());
                    bundle.putBoolean(IS_HTML, true);
                    Transition.startActivity(mActivity, WebViewActivity.class, bundle, false);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
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

    private boolean verifyData() {
        boolean isVerified = true;
        String error = "";
        if (templateDataList == null) {
            return true;
        }

        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);

            if (template.getDataType().equals(CustomViewsConstants.CHECKBOX)
                    || template.getDataType().equals(CustomViewsConstants.NUMBER)
                    || template.getDataType().equals(CustomViewsConstants.EMAIL)
                    || template.getDataType().equals(CustomViewsConstants.TELEPHONE)
                    || template.getDataType().equals(CustomViewsConstants.URL)
                    || template.getDataType().equals(CustomViewsConstants.DATE)
                    || template.getDataType().equals(CustomViewsConstants.DATE_FUTURE)
                    || template.getDataType().equals(CustomViewsConstants.DATE_PAST)
                    || template.getDataType().equals(CustomViewsConstants.DATE_TODAY)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME_FUTURE)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME_PAST)
                    || template.getDataType().equals(CustomViewsConstants.TEXT)) {

                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.enter) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }

                if (template.getDataType().equals(CustomViewsConstants.TELEPHONE)) {
                    Log.e("PHONE======", template.getValue().toString());
                }

            } else if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)) {
                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.Select_terminology).replace(TERMINOLOGY, template.getDisplayName());
                    break;
                }

            } else if (template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {
                boolean isAnySelected = false;
                double cost = 0;
                ArrayList<String> selectedValues = new ArrayList<>();
                for (int j = 0; j < template.getAllowedValuesWithIsSelected().size(); j++) {
                    if (template.getAllowedValuesWithIsSelected().get(j).isChecked()) {
                        isAnySelected = true;
                        cost += template.getOption().get(j).getCost();
                        selectedValues.add(template.getAllowedValuesWithIsSelected().get(j).getDisplayName());
                    }
                }

                if (template.isRequired()) {
                    if (!isAnySelected) {
                        isVerified = false;
                        error = getStrings(R.string.select_atleast_one) + getStrings(R.string.empty_space) + template.getDisplayName();
                        break;
                    }
                }

//                if (isAnySelected) {
                template.setData(selectedValues);
                template.setCost(cost);
//                }

            } else if (template.getDataType().equals(CustomViewsConstants.IMAGE)) {
                if (template.isRequired() && (template.getData().toString().isEmpty() || template.getData().toString().equals("[]"))) {
                    isVerified = false;
                    error = getStrings(R.string.select_atleast_one_image_for) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }
            } else if (template.getDataType().equals(CustomViewsConstants.DOCUMENT)) {
                if (template.isRequired() && (template.getData().toString().isEmpty() || template.getData().toString().equals("[]"))) {
                    isVerified = false;
                    error = getStrings(R.string.select_atleast_one_document_for) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }
            }


        }
        if (!isVerified)
            Utils.snackBar(mActivity, error);
        return isVerified;
    }


    private boolean isStaticAddressFlow() {

        /*if let storeData = StoreFrontModal.selectedMerchant {
            if storeData.is_static_address_enabled.boolValue && storeData.static_address_enabled_for_custom_order.boolValue {
                param["user_id"] = StoreFrontModal.selectedMerchant.storefrontUserId
                self.resetCurrentAddressForStaticAddress()
                self.isStaticAddressEnabled = true
            }
else {
                return
            }
        } else if AppConfiguration.current.isAdminStaticAddressEnabled && AppConfiguration.current.static_address_enabled_for_custom_order.boolValue {
            param["user_id"] = Singleton.sharedInstance.formDetailsInfo.user_id!
                    self.resetCurrentAddressForStaticAddress()
            self.isStaticAddressEnabled = true
        } else {
            self.isStaticAddressEnabled = false
            return
        }*/
        //admin

        if (isCustomOrderMerchantLevel && storefrontData != null && storefrontData.getIsStaticAddressEnabled() == 1 && storefrontData.getStaticAddressEnabledForCustomOrder() == 1) {
            return true;
        } else if (!isCustomOrderMerchantLevel && UIManager.isStaticAddressEnabledForAdmin() &&
                StorefrontCommonData.getAppConfigurationData()
                        .getStaticAddressEnabledForCustomOrder() == 1) {
            return true;
        }
        return false;
    }

    private void goToStaticAddressActivity() {
        Intent intent = new Intent(this, SelectStaticAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ExtraConstants.EXTRA_ADDRESS_LIST, addressesList);
        intent.putExtras(bundle);
        startActivityForResult(intent, Codes.Request.OPEN_STATIC_ADDRESS_ACTIVITY);
    }


}