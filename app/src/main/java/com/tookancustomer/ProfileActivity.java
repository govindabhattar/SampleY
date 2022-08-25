package com.tookancustomer;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.tookancustomer.adapters.LanguageSpinnerAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.customviews.CustomFieldCheckboxProfile;
import com.tookancustomer.customviews.CustomFieldImageProfile;
import com.tookancustomer.customviews.CustomFieldTextViewProfile;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.userdata.Data;
import com.tookancustomer.models.userdata.ProfilePojo;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userdata.UserRights;
import com.tookancustomer.models.userdata.VendorDetails;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.MultipartParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.BulletPassTransformationMethod;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.FilterUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;
import com.tookancustomer.utility.imagepicker.ImageChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ImageChooser.OnImageSelectListener, AdapterView.OnItemSelectedListener {
    boolean removeFirstView = true;
    boolean isLanguageChanged = false;
    private Dialog sochitelIdDialog;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private ImageView ibProfile;
    private ImageView ivAvtar;
    private RelativeLayout rlEditImage;
    private ProgressBar progressBarAvtar;
    private EditText etName, etEmail, etCountryCode, etPhone, etCurrentPassword, etNewPassword, etPassword, etAddress, etDOB;
    private RelativeLayout rlCurrentPassword, rlNewPassword, rlDOB;
    private ImageButton ibViewCurrentPassword, ibViewNewPassword;
    private Button btnProfile;
    private VendorDetails vendorData;
    private boolean isEditing = false;

    private ValidateClass validateClass;
    private ImageChooser mImageChooser;
    private Boolean sendPic = false;
    private File imageFile;
    private LinearLayout llCustomFields, llAdditionalInfo;
    private ScrollView swScrollView;
    private int customFieldPosition;
    private LinearLayout llProfileUserRights, llProfileUserRightsChild, llProfileCustomFieldsChild, llSelectLanguage;
    private RelativeLayout rlProfileUserRightsHeader, rlProfileCustomFieldsHeader;
    private TextView tvAdditionalInfo, tvCustomerRights, tvPhoneHeader, tvCustomerRating;
    private TextView tvProfileUserRightsSubmit, tvProfileCustomFieldsUpdate;
    private Spinner spinnerUserRights;
    private EditText etDescription, etUserRights;
    private UserRights selectedUserRight;
    private ImageView ibArrowCustomerRights, ibArrowCustomFields;

    private EditText etChooseLanguage;
    private Spinner spinnerChooseLanguage;
    private LanguagesCode selectedLanguageCode;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private boolean setEnable = false;
    private ArrayList<String> templateDataList;
    private boolean update = false;
    private int check = 0;
    private RelativeLayout rlSochitelLayout;
    private TextView tvActionSochitelId;
    private MaterialEditText metSochitelId;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.snackbarSuccess(mActivity, getStrings(R.string.password_updated));
                }
            }, 1000);
        }
    };

    private static void logoutTransition(Activity mActivity) {
        askUserToLogout(mActivity);
    }

    /**
     * Method to ask the User to logout
     */
    private static void askUserToLogout(final Activity mActivity) {
        new OptionsDialog.Builder(mActivity).message(getStrings(mActivity, R.string.sure_to_logout)).listener(new OptionsDialog.Listener() {
            @Override
            public void performPositiveAction(int purpose, Bundle backpack) {
//                if (Dependencies.isMarketplaceApp()|| Dependencies.isEcommerceApp()) {
//                    AppManager.getInstance().invalidateSession(mActivity, false, "");
//                } else {
                logoutUser(mActivity);
//                }
            }

            @Override
            public void performNegativeAction(int purpose, Bundle backpack) {
            }
        }).build().show();
    }

    private static void logoutUser(final Activity mActivity) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(mActivity).vendorLogout(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                AppManager.getInstance().invalidateSession(mActivity, false, "");
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        mActivity = this;
        vendorData = StorefrontCommonData.getUserData().getData().getVendorDetails();

        initializeData();
        setFilter();
        setUserData();
        setEnable(false);

    }

    private void initializeData() {
        validateClass = new ValidateClass(mActivity);
        mImageChooser = new ImageChooser(this);

        rlBack = findViewById(R.id.rlBack);
        rlSochitelLayout = findViewById(R.id.rlSochitelLayout);
        tvActionSochitelId = findViewById(R.id.tvActionSochitelId);
        metSochitelId = findViewById(R.id.etSochitelId);
        ((MaterialEditText) metSochitelId).setFloatingLabelText(getStrings(R.string.sochitel_id));
        metSochitelId.setEnabled(false);

        tvHeading = findViewById(R.id.tvHeading);


        swScrollView = (ScrollView) findViewById(R.id.swScrollView);

        RelativeLayout rlHeaderTextOption = findViewById(R.id.rlHeaderTextOption);
        rlHeaderTextOption.setVisibility(View.VISIBLE);


        ivAvtar = findViewById(R.id.ivAvtar);
        rlEditImage = findViewById(R.id.rlEditImage);
        progressBarAvtar = findViewById(R.id.progressBarAvtar);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etDOB = findViewById(R.id.etDOB);
        etEmail = findViewById(R.id.etEmail);
        etCountryCode = findViewById(R.id.etCountryCode);

        etCountryCode.setText(Utils.getDefaultCountryCode(mActivity));
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        rlCurrentPassword = findViewById(R.id.rlCurrentPassword);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        rlDOB = findViewById(R.id.rlDOB);
        ibViewCurrentPassword = findViewById(R.id.ibViewCurrentPassword);

        rlNewPassword = findViewById(R.id.rlNewPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        ibViewNewPassword = findViewById(R.id.ibViewNewPassword);

        btnProfile = findViewById(R.id.btnProfile);
        ibProfile = findViewById(R.id.ibProfile);

        llAdditionalInfo = findViewById(R.id.llAdditionalInfo);
        llCustomFields = findViewById(R.id.llCustomFields);

        llSelectLanguage = findViewById(R.id.llSelectLanguage);
        llSelectLanguage.setVisibility(UIManager.getLanguagesArrayList().size() > 1 ? View.VISIBLE : View.GONE);

        llProfileUserRights = findViewById(R.id.llProfileUserRights);
        rlProfileUserRightsHeader = findViewById(R.id.rlProfileUserRightsHeader);
        tvProfileUserRightsSubmit = findViewById(R.id.tvProfileUserRightsSubmit);
        tvProfileCustomFieldsUpdate = findViewById(R.id.tvProfileCustomFieldsUpdate);
        tvProfileCustomFieldsUpdate.setText(getStrings(R.string.edit));
        llProfileUserRightsChild = findViewById(R.id.llProfileUserRightsChild);
        llProfileCustomFieldsChild = findViewById(R.id.llProfileCustomFieldsChild);
        etUserRights = findViewById(R.id.etUserRights);
        etUserRights.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_icon_country_code_arrow, 0);
        spinnerUserRights = findViewById(R.id.spinnerUserRights);
        etDescription = findViewById(R.id.etDescription);
        ibArrowCustomerRights = findViewById(R.id.ibArrowCustomerRights);
        rlProfileCustomFieldsHeader = findViewById(R.id.rlProfileCustomFieldsHeader);
        ibArrowCustomFields = findViewById(R.id.ibArrowCustomFields);
        tvCustomerRating = findViewById(R.id.tvCustomerRating);

        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
        tvCustomerRights = findViewById(R.id.tvCustomerRights);

        tvPhoneHeader = findViewById(R.id.tvPhoneHeader);

        spinnerChooseLanguage = findViewById(R.id.spinnerChooseLanguage);
        etChooseLanguage = findViewById(R.id.etChooseLanguage);
        Utils.setOnClickListener(this, etChooseLanguage, etAddress);

        LanguageSpinnerAdapter languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, UIManager.getLanguagesArrayList());
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseLanguage.setAdapter(languageSpinnerAdapter);
        for (int i = 0; i < StorefrontCommonData.getAppConfigurationData().getLanguages().size(); i++) {
            if (StorefrontCommonData.getSelectedLanguageCode() != null && StorefrontCommonData.getAppConfigurationData().getLanguages().get(i).getLanguageCode().equalsIgnoreCase(StorefrontCommonData.getSelectedLanguageCode().getLanguageCode())) {
                spinnerChooseLanguage.setSelection(i);
                break;
            }

        }
        spinnerChooseLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    selectedLanguageCode = (LanguagesCode) parent.getItemAtPosition(position);
                    etChooseLanguage.setText(selectedLanguageCode.getLanguageDisplayName());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        Utils.setOnClickListener(this, rlBack, ibProfile, btnProfile, ivAvtar, rlEditImage, etCountryCode
                , ibViewCurrentPassword, ibViewNewPassword, rlHeaderTextOption, rlProfileUserRightsHeader
                , tvProfileUserRightsSubmit, tvProfileCustomFieldsUpdate, rlProfileCustomFieldsHeader, etUserRights);

        if (StorefrontCommonData.getAppConfigurationData().getSochitelEnable() == 1) {
            rlSochitelLayout.setVisibility(View.VISIBLE);
            apiGetSochitelId();
        } else {
            rlSochitelLayout.setVisibility(View.GONE);
        }

        tvActionSochitelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sochitelDialog();
            }
        });
    }

    private void sochitelDialog() {
        try {
            if (sochitelIdDialog != null) {
                sochitelIdDialog.dismiss();
                sochitelIdDialog = null;
            }
            sochitelIdDialog = new Dialog(mActivity, R.style.NotificationDialogTheme);
            sochitelIdDialog.setContentView(R.layout.custom_dialog_view);

            Window window = sochitelIdDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            params.gravity = Gravity.CENTER;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setAttributes(params);

            sochitelIdDialog.setCancelable(true);
            sochitelIdDialog.setCanceledOnTouchOutside(false);


            sochitelIdDialog.findViewById(R.id.tvSochitelAccountId).setVisibility(View.VISIBLE);


            final EditText etSochitelId = sochitelIdDialog.findViewById(R.id.etAmount);
            etSochitelId.setHint(getStrings(R.string.enter_sochitel_Id));
            etSochitelId.setText(metSochitelId.getText().toString());
            etSochitelId.setSelection(etSochitelId.getText().length());

            Button btSave = sochitelIdDialog.findViewById(R.id.btAdd);
            btSave.setText(getStrings(R.string.save));
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etSochitelId.getText().toString().isEmpty()) {
                        Utils.showToast(ProfileActivity.this, "Sochitel account id can not be empty.");
                        return;
                    }
                    if (tvActionSochitelId.getText().toString().equalsIgnoreCase(getStrings(R.string.add))) {
                        apiAddSochitelId(etSochitelId.getText().toString());
                    } else {
                        apiEditSochitelId(etSochitelId.getText().toString());
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.hideSoftKeyboard(mActivity);
                        }
                    }, 100);
                }
            });
            Button btnCancel = sochitelIdDialog.findViewById(R.id.btCancel);
            btnCancel.setText(getStrings(R.string.cancel));
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sochitelIdDialog.dismiss();

                }
            });

           /* InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
            sochitelIdDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void apiAddSochitelId(String accountId) {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCOUNT_ID, accountId)
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
        RestClient.getApiInterface(mActivity).addSochitelKey(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                tvActionSochitelId.setText(getStrings(R.string.edit));
                metSochitelId.setText(accountId);
                if (sochitelIdDialog != null) {
                    sochitelIdDialog.dismiss();
                    sochitelIdDialog = null;
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    private void apiEditSochitelId(String accountId) {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCOUNT_ID, accountId);
        RestClient.getApiInterface(mActivity).updateSochitelKey(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                metSochitelId.setText(accountId);
                if (sochitelIdDialog != null) {
                    sochitelIdDialog.dismiss();
                    sochitelIdDialog = null;
                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    private void apiGetSochitelId() {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
        RestClient.getApiInterface(mActivity).getSochitelKey(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                try {
                    if ((((ArrayList) baseModel.data).size() == 0)) {
                        metSochitelId.setText("");
                        tvActionSochitelId.setText(getStrings(R.string.add));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String json = new JSONObject(new Gson().toJson(((ArrayList) baseModel.data).get(0))).toString();
                    JSONObject obj = new JSONObject(json);
                    if (!obj.getString("account_id").equals("null")) {
                        metSochitelId.setText(obj.getString("account_id"));
                        tvActionSochitelId.setText(getStrings(R.string.edit));
                    } else {
                        metSochitelId.setText("");
                        tvActionSochitelId.setText(getStrings(R.string.add));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    private void setFilter() {
        FilterUtils.setPasswordFilter(etCurrentPassword, ValidateClass.PASSWORD_MAX_LENGTH);
        FilterUtils.setPasswordFilter(etNewPassword, ValidateClass.PASSWORD_MAX_LENGTH);

        etCurrentPassword.setTransformationMethod(new BulletPassTransformationMethod());
        etNewPassword.setTransformationMethod(new BulletPassTransformationMethod());
        etPassword.setTransformationMethod(new BulletPassTransformationMethod());

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT) {
//                    if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                    etEmail.requestFocus();
//                    } else {
//                        etPhone.requestFocus();
//                    }
                    return true;
                }
                return false;
            }
        });
        etNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    editProfileAction();
                    return true;
                }
                return false;
            }
        });

    }

    private void setUserData() {
        setStrings();
        sendPic = false;
        vendorData = StorefrontCommonData.getUserData().getData().getVendorDetails();
        etName.setText(vendorData.getFirstName() + " " + vendorData.getLastName());
        etEmail.setText(vendorData.getEmail());


        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
            if (vendorData.getAddress() != null && !vendorData.getAddress().equalsIgnoreCase("")) {
                etAddress.setVisibility(View.VISIBLE);
                etAddress.setText(vendorData.getAddress());
            } else {
                etAddress.setVisibility(View.GONE);
            }

            if (vendorData.getDob() != null && !vendorData.getDob().equalsIgnoreCase("")) {
                rlDOB.setVisibility(View.VISIBLE);
                etDOB.setText(DateUtils.getInstance().parseDateAs(vendorData.getDob(), Constants.DateFormat.ONLY_DATE, Constants.DateFormat.DOB_DATE_FORMAT));
            } else {
                rlDOB.setVisibility(View.GONE);
            }

        } else {
            rlDOB.setVisibility(View.GONE);
            etAddress.setVisibility(View.GONE);
        }

        if (vendorData.getLanguage() != null) {
            selectedLanguageCode = new LanguagesCode(vendorData.getLanguageCode(), vendorData.getLanguageName(), vendorData.getLanguageDisplayName());
            etChooseLanguage.setText(selectedLanguageCode.getLanguageDisplayName());
        } else {
            selectedLanguageCode = new LanguagesCode(vendorData.getLanguageCode(), vendorData.getLanguageName(), vendorData.getLanguageDisplayName());
//            selectedLanguageCode = null;
            etChooseLanguage.setText("");
        }
//        String[] phoneNumber = vendorData.getPhoneNo().replace("-", " ").trim().split(" ");

        try {
//            String[] phoneNumber = Utils.splitNumberByCode(this, vendorData.getPhoneNo());
            String[] phoneNumber = Utils.splitNumberByCodeNew(this, vendorData.getPhoneNo());
            etCountryCode.setText(phoneNumber[0]);
            etPhone.setText(phoneNumber[1].replace("+", ""));
        } catch (Exception e) {
            String countryCode = Utils.getCountryCode(mActivity, vendorData.getPhoneNo().trim());
            etCountryCode.setText(countryCode);
//            etCountryCode.setText(countryPicker.getUserCountryInfo(this).getDialCode());
            etPhone.setText(vendorData.getPhoneNo().trim().replace(countryCode, "").replace("+", "").replace("-", " ").trim());
        }

        etPassword.setText(getStrings(R.string.password_text));

        final String profilePic = vendorData.getVendorImage();

        if ((profilePic != null) && (!profilePic.equals(""))) {
            progressBarAvtar.setVisibility(View.VISIBLE);

            new GlideUtil.GlideUtilBuilder(ivAvtar)
                    .setPlaceholder(R.drawable.ic_profile_placeholder)
                    .setCenterCrop(true)
                    .setLoadItem(vendorData.getVendorImage())
                    .setTransformation(new CircleCrop())
                    .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                        @Override
                        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                            progressBarAvtar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {

                        }

                        @Override
                        public void onLoadFailed() {
                            progressBarAvtar.setVisibility(View.GONE);
                        }
                    }).build();


        } else {
            progressBarAvtar.setVisibility(View.GONE);
            ivAvtar.setImageResource(R.drawable.ic_profile_placeholder);
        }


        ArrayList<String> userRightArrayList = new ArrayList<>();
        for (int i = 0; i < StorefrontCommonData.getUserData().getData().getUserRights().size(); i++) {
            userRightArrayList.add(StorefrontCommonData.getUserData().getData().getUserRights().get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userRightArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUserRights.setAdapter(adapter);
        spinnerUserRights.setPrompt(getStrings(R.string.select_text));
        spinnerUserRights.setOnItemSelectedListener(this);
//        spinnerUserRights.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//              }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });

        if (StorefrontCommonData.getFormSettings().getMerchantCustomerRating() == 1 && vendorData.getAverageRating().floatValue() > 0) {
            tvCustomerRating.setVisibility(View.VISIBLE);
            tvCustomerRating.setText(String.valueOf(vendorData.getAverageRating().floatValue()));
        }


    }

    public void setEnable(Boolean enable) {
        if (enable) {
            etName.setFocusable(true);
            etName.requestFocus();
            etName.setSelection(etName.length());
        }

        etChooseLanguage.setEnabled(enable);
        ivAvtar.setEnabled(enable);
        rlEditImage.setEnabled(enable);
        rlEditImage.setVisibility(enable ? View.VISIBLE : View.GONE);
        etName.setEnabled(enable);


        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
            etAddress.setEnabled(enable);
            etAddress.setFocusable(false);
            etDOB.setEnabled(false);
            etDOB.setFocusable(false);
        }
//        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
        etEmail.setEnabled(enable);
//        } else {
//            etEmail.setEnabled(false);
//            etEmail.setFocusable(false);
//        }

        etCountryCode.setEnabled(enable);
        etPhone.setEnabled(enable);

        etPassword.setEnabled(false);

        if (vendorData.isDummyPassword()) {
            etPassword.setVisibility(enable ? View.GONE : View.VISIBLE);
        } else {
            etPassword.setVisibility(View.GONE);
        }


        etNewPassword.setText("");
        etCurrentPassword.setText("");
        btnProfile.setText(enable ? getStrings(R.string.save) : getStrings(R.string.edit));
        btnProfile.setVisibility(!enable ? View.GONE : View.VISIBLE);
        ibProfile.setVisibility(enable ? View.GONE : View.VISIBLE);

        if (StorefrontCommonData.getAppConfigurationData().getHideGdpr() == 0) {
            llProfileUserRights.setVisibility(enable ? View.GONE : View.VISIBLE);
        } else {
            llProfileUserRights.setVisibility(View.GONE);
        }

        rlCurrentPassword.setVisibility(enable ? View.VISIBLE : View.GONE);
        rlNewPassword.setVisibility(enable ? View.VISIBLE : View.GONE);

        if (enable) {
            rlCurrentPassword.setVisibility(vendorData.isDummyPassword() ? View.VISIBLE : View.GONE);
            rlNewPassword.setVisibility(vendorData.isDummyPassword() ? View.VISIBLE : View.GONE);
        }

        etChooseLanguage.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, enable ? R.drawable.ic_icon_country_code_arrow : 0, 0);
//        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
        etEmail.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
//        } else {
//            etEmail.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.et_hint_color : R.color.primary_text_color));
//        }
        etName.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etCountryCode.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etPhone.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etPassword.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etChooseLanguage.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etAddress.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));
        etDOB.setTextColor(ContextCompat.getColor(mActivity, enable ? R.color.primary_text_color : R.color.primary_text_color));

        ((MaterialEditText) etName).setHideUnderline(!enable);
        ((MaterialEditText) etEmail).setHideUnderline(!enable);
        ((MaterialEditText) etAddress).setHideUnderline(!enable);
        ((MaterialEditText) etDOB).setHideUnderline(!enable);
        ((MaterialEditText) etPhone).setHideUnderline(!enable);
        ((MaterialEditText) etCountryCode).setHideUnderline(!enable);
        ((MaterialEditText) etPassword).setHideUnderline(!enable);
        ((MaterialEditText) etChooseLanguage).setHideUnderline(!enable);

        if (enable) {
            llAdditionalInfo.setVisibility(View.GONE);
        } else {
            if (StorefrontCommonData.getUserData().getData().getSignupTemplateData().size() > 0) {
                llAdditionalInfo.setVisibility(View.VISIBLE);
                renderCustomFields();
            } else {
                llAdditionalInfo.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ibProfile:
            case R.id.btnProfile:
                editProfileAction();
                break;
            case R.id.ivAvtar:
            case R.id.rlEditImage:
                if (isEditing)
                    mImageChooser.selectImage();
                break;
            case R.id.etCountryCode:
                Utils.hideSoftKeyboard(mActivity);
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        etCountryCode.setText(country.getCountryCode());
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();
                break;
            case R.id.ibViewCurrentPassword:
                boolean isPasswordVisible = etCurrentPassword.getTransformationMethod() == null;
                int cursorPositionOldPass = etCurrentPassword.getSelectionStart();
                if (isPasswordVisible) {
                    etCurrentPassword.setTransformationMethod(new BulletPassTransformationMethod());
                    ibViewCurrentPassword.setImageResource(R.drawable.ic_eye_inactive);
                } else {
                    etCurrentPassword.setTransformationMethod(null);
                    ibViewCurrentPassword.setImageResource(R.drawable.ic_eye_active);
                }
                try {
                    etCurrentPassword.setSelection(cursorPositionOldPass);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                break;
            case R.id.ibViewNewPassword:
                boolean isNewPasswordVisible = etNewPassword.getTransformationMethod() == null;
                int cursorPositionNewPass = etNewPassword.getSelectionStart();
                if (isNewPasswordVisible) {
                    etNewPassword.setTransformationMethod(new BulletPassTransformationMethod());
                    ibViewNewPassword.setImageResource(R.drawable.ic_eye_inactive);
                } else {
                    etNewPassword.setTransformationMethod(null);
                    ibViewNewPassword.setImageResource(R.drawable.ic_eye_active);
                }
                try {
                    etNewPassword.setSelection(cursorPositionNewPass);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                break;
            case R.id.rlHeaderTextOption:
                logoutTransition(mActivity);
                break;
            case R.id.rlProfileUserRightsHeader:
                if (llProfileUserRightsChild.getVisibility() == View.VISIBLE) {
                    llProfileUserRightsChild.setVisibility(View.GONE);
                    tvProfileUserRightsSubmit.setVisibility(View.GONE);
                    ibArrowCustomerRights.setVisibility(View.VISIBLE);
                    ibArrowCustomerRights.setRotation(90);
                } else {
                    llProfileUserRightsChild.setVisibility(View.VISIBLE);
                    tvProfileUserRightsSubmit.setVisibility(View.VISIBLE);
                    ibArrowCustomerRights.setVisibility(View.GONE);
                    ibArrowCustomerRights.setRotation(-90);
                    swScrollView.scrollTo(0, etDescription.getBottom());
                    etDescription.requestFocus();
                }
                break;
            case R.id.rlProfileCustomFieldsHeader:
                if (llProfileCustomFieldsChild.getVisibility() == View.VISIBLE) {
                    llProfileCustomFieldsChild.setVisibility(View.GONE);
                    tvProfileCustomFieldsUpdate.setVisibility(View.GONE);
                    ibArrowCustomFields.setVisibility(View.VISIBLE);
                    ibArrowCustomFields.setRotation(90);
                } else {
                    llProfileCustomFieldsChild.setVisibility(View.VISIBLE);
                    ibArrowCustomFields.setRotation(-90);
                    ibArrowCustomFields.setVisibility(View.GONE);
                    tvProfileCustomFieldsUpdate.setVisibility(View.VISIBLE);
                    swScrollView.scrollTo(0, llCustomFields.getBottom());
                    llCustomFields.requestFocus();
                }
                break;
            case R.id.tvProfileUserRightsSubmit:
                setUserRights();
                break;
            case R.id.tvProfileCustomFieldsUpdate:
                if (update) {
                    if (checkTaskForCompletion())
                        updateCustomFields();
                } else {
                    setEnable = true;
                    update = true;
                    tvProfileCustomFieldsUpdate.setText(getStrings(R.string.submit));
                    renderCustomFields();
                }
                break;
            case R.id.etUserRights:
                spinnerUserRights.performClick();
                break;
            case R.id.etChooseLanguage:
                spinnerChooseLanguage.performClick();
                break;

            case R.id.etAddress:
                Utils.searchPlace(mActivity);
//                gotoFavLocationActivity();
                break;
        }
    }

    private void updateCustomFields() {
        JSONObject jObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < StorefrontCommonData.getUserData().getData().getSignupTemplateData().size(); i++) {
            try {
                jObject.put(StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i).getLabel(),
                        StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i).getData());
            } catch (JSONException e) {

                Utils.printStackTrace(e);
            }

        }
        jsonArray.put(jObject);
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add("dual_user_key", UIManager.isDualUserEnable())
                .add("template_name", StorefrontCommonData.getUserData().getData().getSignupTemplateName())
                .add("fields", getMetaData());
        RestClient.getApiInterface(mActivity).updateProfileCustomFields(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(ProfileActivity.this, baseModel.getMessage());
                setEnable = false;
                update = false;
                tvProfileCustomFieldsUpdate.setText(getStrings(R.string.edit));
                renderCustomFields();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private boolean checkTaskForCompletion() {
        // Check for custom fields
        boolean isCustomFieldPassed = true;
        if (StorefrontCommonData.getUserData().getData().getSignupTemplateData() != null) {
            ArrayList<String> requiredCustomFields = new ArrayList<>();
            for (SignupTemplateData item : StorefrontCommonData.getUserData().getData().getSignupTemplateData()) {
                String data = item.getData().toString();
                String error = null;

                boolean isDataTypeHandled =
                        !(item.getDataType().equals(Keys.DataType.BARCODE) ||
                                item.getDataType().equals(Keys.DataType.CHECKLIST) ||
                                item.getDataType().equals(Keys.DataType.TABLE));

                if (item.getRequired() == 1 && isDataTypeHandled) {
                    switch (item.getDataType()) {
                        case Keys.DataType.EMAIL:
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                            break;
                        case Keys.DataType.TELEPHONE:
                            error = (data.isEmpty() || Utils.isValidPhoneNumberWithCountryCode(data)) ? null : getStrings(R.string.invalid_phone_number);
                            break;
                        case Keys.DataType.CHECKBOX:
                            error = (data.isEmpty() || data.equals("true")) ? null : item.getLabel().replace("_", " ").substring(0, 1).toUpperCase() + item.getLabel().replace("_", " ").substring(1).toLowerCase() + getStrings(R.string.is_required);
                            break;
                    }
                }
                if (data.isEmpty() && item.getRequired() == 1 && isDataTypeHandled) {
                    Utils.snackBar(this, item.getLabel().replace("_", " ").substring(0, 1).toUpperCase() + item.getLabel().replace("_", " ").substring(1).toLowerCase() + " " + getStrings(R.string.is_required));
                    return false;
                }

                if (!data.isEmpty() && isDataTypeHandled) {
                    switch (item.getDataType()) {
                        case Keys.DataType.EMAIL:
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : getStrings(R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : getStrings(R.string.invalid_url);
                            break;
//                        case Keys.DataType.TELEPHONE:
//                            error = (data.isEmpty() || Utils.isValidPhoneNumberWithCountryCode(data)) ? null : getStrings(R.string.invalid_phone_number);
//                            break;
                    }

                    if (error != null) {
                        Utils.snackBar(this, error);
                        return false;
                    }
                }

                if (error != null) {
                    isCustomFieldPassed = false;
                    String name = item.getLabel();
                    requiredCustomFields.add(error);
                }
            }
            if (!isCustomFieldPassed) {
                String errorFiels = "";
                for (String subItem : requiredCustomFields.toArray(new String[requiredCustomFields.size()])) {
                    if (errorFiels.isEmpty()) {
                        errorFiels = "-" + errorFiels.concat(Utils.assign(subItem));
                    } else {
                        errorFiels = errorFiels.concat("\n" + "-" + Utils.assign(subItem));
                    }
                }
                Utils.snackBar(this, getStrings(R.string.fields_invalid_text) + "\n" + errorFiels.replace("_", " ").substring(0, 1).toUpperCase() + errorFiels.replace("_", " ").substring(1).toLowerCase());
            }
        }

        return isCustomFieldPassed;
    }

    private JSONArray getMetaData() {
        JSONArray jArrayMetaData = new JSONArray();
        for (SignupTemplateData item : StorefrontCommonData.getUserData().getData().getSignupTemplateData()) {
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
                } else if (item.getDataType().equalsIgnoreCase(Keys.DataType.TELEPHONE)) {

                    jsonObjectPickUpMetaData.put("label", item.getLabel());

                    Log.e("telephone no. data1>>>>", data);
                    String[] phoneNumber = data.split(" ");
                    try {
                        if (phoneNumber.length > 1) {
                        } else {
                            data = "";
                        }

                    } catch (Exception e) {
                        data = "";
                    }

                    Log.e("telephone no. data2>>>>", data);

                    jsonObjectPickUpMetaData.put("data", data);
                    jArrayMetaData.put(jsonObjectPickUpMetaData);

                } else if (item.getDataType().equals(Keys.DataType.DATETIME) || item.getDataType().equals(Keys.DataType.DATETIME_FUTURE)
                        || item.getDataType().equals(Keys.DataType.DATETIME_PAST)) {

                    String formattedDATE = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(DateUtils.getInstance().getDate(data));
//                    String formattedDATE = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(DateUtils.getInstance().getDate(data));

                    jsonObjectPickUpMetaData.put("label", item.getLabel());
                    jsonObjectPickUpMetaData.put("data", formattedDATE);
                    jArrayMetaData.put(jsonObjectPickUpMetaData);
                } else {
                    jsonObjectPickUpMetaData.put("label", item.getLabel());
                    jsonObjectPickUpMetaData.put("data", data);
                    jArrayMetaData.put(jsonObjectPickUpMetaData);

                }
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }
        }
        return jArrayMetaData;
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(Constants.BroadcastFilters.CHANGE_PASSWORD));
    }

    private void performBackAction() {
        if (isEditing) {
            if (sendPic
                    || !etName.getText().toString().trim().equals((vendorData.getFirstName() + " " + vendorData.getLastName()).trim())
                    || !(etEmail.getText().toString().trim()).equals(vendorData.getEmail())
                    || !(etCountryCode.getText().toString().trim() + " " + etPhone.getText().toString().trim()).equals(vendorData.getPhoneNo())
                    || !etCurrentPassword.getText().toString().isEmpty()
                    || !etNewPassword.getText().toString().isEmpty()) {

                new OptionsDialog.Builder(mActivity).message(getStrings(R.string.sure_to_back_changes_profile)).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {

                        isEditing = false;
                        setUserData();
                        setEnable(false);
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                    }
                }).build().show();

            } else {
                isEditing = false;
                setUserData();
                setEnable(false);
            }
        } else {
            Bundle extras = new Bundle();
            extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            Intent returnIntent = new Intent();
            returnIntent.putExtras(extras);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }


    private void editProfileAction() {
        if (!isEditing) {
            isEditing = true;
            setEnable(true);
        } else {
            if (Utils.internetCheck(mActivity)) {
                if (validate()) {
                    if (!(etCurrentPassword.getText().toString().isEmpty() && etNewPassword.getText().toString().isEmpty())) {
                        changePassword();
                    } else {
                        editProfile();
                    }
                }
            } else {
                new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            }
        }
    }

    Boolean validate() {

        if (!validateClass.checkName(etName)) {
            return false;
        }
        if (!(UIManager.getSignupField() == 1 && etEmail.getText().toString().trim().isEmpty())) {
            if (!validateClass.checkEmail(etEmail, getStrings(R.string.email_field_required))) {
                return false;
            }
        }

        if (!(UIManager.getSignupField() == 0 && etPhone.getText().toString().trim().isEmpty())) {
            if (!validateClass.checkCountryCode(etCountryCode)) {
                return false;
            }
            if (!validateClass.checkPhoneNumber(etPhone)) {
                return false;
            }
        }
        if (!(etCurrentPassword.getText().toString().isEmpty() && etNewPassword.getText().toString().isEmpty())) {
            if (!validateClass.checkPasswordString(etCurrentPassword, getStrings(R.string.old_password_field_required),
                    getStrings(R.string.old_password_field_invalid))) {
                return false;
            }
            if (!validateClass.checkPasswordString(etNewPassword, getStrings(R.string.new_password_field_required),
                    getStrings(R.string.new_password_field_invalid))) {
                return false;
            }
            if (!validateClass.checkOldNewPassword(etCurrentPassword, etNewPassword, getStrings(R.string.password_same_current_new))) {
                return false;
            }
        }

        if (UIManager.getLanguagesArrayList().size() > 1 && selectedLanguageCode == null) {
            Utils.snackBar(this, getStrings(R.string.please_choose_language_before_proceeding));
            return false;
        }


        return true;
    }


    private void changePassword() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("current_password", etCurrentPassword.getText().toString())
                .add("new_password", etNewPassword.getText().toString());

        RestClient.getApiInterface(this).changePassword(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserData userData = new UserData();
                try {
                    userData.setData(baseModel.toResponseModel(Data.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                Dependencies.saveAccessToken(mActivity, userData.getData().getAccessToken());

                userData.getData().setAppAccessToken(userData.getData().getAccessToken());

//                Intent intent = new Intent(Constants.BroadcastFilters.CHANGE_PASSWORD);
//                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
//                performBackAction();

                editProfile();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    public void editProfile() {
        String firstName = "", lastName = "";
        try {
            String[] name = etName.getText().toString().split(" ");

            firstName = name[0];
            lastName = etName.getText().subSequence(name[0].length(), etName.getText().toString().length()).toString();
        } catch (Exception e) {
            Utils.printStackTrace(e);
            firstName = etName.getText().toString();
            lastName = "";
        }

        MultipartParams.Builder multipartParams = Dependencies.setMultiParamsForAPI(mActivity, StorefrontCommonData.getUserData(), false);
        multipartParams.add("first_name", Utils.capitaliseWords(firstName.trim()))
                .add("last_name", Utils.capitaliseWords(lastName.trim()))
                .add("email", etEmail.getText().toString().trim())
                .add("phone_no", etPhone.getText().toString().trim().isEmpty() ? "" : etCountryCode.getText().toString().trim() + " " + etPhone.getText().toString().trim());

        if (selectedLanguageCode != null) {
            multipartParams.add("language", selectedLanguageCode.getLanguageCode());
        }

        //if rentals then address can be editted.
        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
            multipartParams.add("address", etAddress.getText().toString().trim());
        }

        if (imageFile != null && sendPic) {
            try {
                multipartParams.addFile("vendor_image", imageFile);
            } catch (Exception e) {
            }
        }

        RestClient.getApiInterface(mActivity).updateProfile(multipartParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                ProfilePojo profilePojo = new ProfilePojo();
                try {
                    profilePojo.setData(baseModel.toResponseModel(VendorDetails.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                String oldEmail = StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail();
                String oldPhoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo();

                if (!StorefrontCommonData.getUserData().getData().getVendorDetails().getLanguageCode().equals(profilePojo.getData().getLanguageCode())) {
                    isLanguageChanged = true;
                } else {
                    isLanguageChanged = false;
                }

                Dependencies.saveAccessToken(mActivity, profilePojo.getData().getAppAccessToken());
                StorefrontCommonData.getUserData().getData().setVendorDetails(profilePojo.getData());

                StorefrontCommonData.getUserData().getData().getVendorDetails().setUpdatedEmail(etEmail.getText().toString().trim());
                StorefrontCommonData.getUserData().getData().getVendorDetails().setUpdatedPhoneNo(etPhone.getText().toString().trim().isEmpty() ? "" : etCountryCode.getText().toString().trim() + " " + etPhone.getText().toString().trim());

                StorefrontCommonData.getUserData().getData().setAppAccessToken(profilePojo.getData().getAppAccessToken());
                StorefrontCommonData.getUserData().getData().setUserRights(profilePojo.getData().getUserRights());
                StorefrontCommonData.getUserData().getData().setLanguageStrings(profilePojo.getData().getLanguageString());
                StorefrontCommonData.setUserData(StorefrontCommonData.getUserData());
                StorefrontCommonData.setLanguageCode(selectedLanguageCode);
                StorefrontCommonData.setLanguageStrings(mActivity, profilePojo.getData().getLanguageString());
                StorefrontCommonData.setTerminology(profilePojo.getData().getTerminology());

//                Utils.snackbarSuccess(mActivity, getStrings(R.string.profile_updated));
//                isEditing = false;
//                setEnable(false);
//                setUserData();

//                if (!oldEmail.equals(profilePojo.getData().getEmail()) || !oldPhoneNumber.equals(profilePojo.getData().getPhoneNo())) {
//                    logoutUser(mActivity);
//                }
                if (UIManager.isOTPAvailable() && profilePojo.getData().getIsPhoneVerified() == 0 || UIManager.getIsEmailVerificationRequried() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsEmailVerified() == 0) {
                    Intent intent = new Intent(mActivity, RegistrationOnboardingActivity.class);
                    intent.putExtra(IS_ONBOARDING_FROM_PROFILE, true);
                    startActivityForResult(intent, Codes.Request.OPEN_ONBOARDING_SCREEN);
                } else {
                    Utils.snackbarSuccess(mActivity, getStrings(R.string.profile_updated));
                    isEditing = false;
                    setEnable(false);
                    setUserData();
                    restartActivity();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                etNewPassword.setText("");
                etCurrentPassword.setText("");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ImageChooser.PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE)
            mImageChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void loadImage(List<ChosenImage> list) {
        if (list != null && list.size() > 0) {
            try {
                if (list.get(0).getThumbnailPath() != null && !list.get(0).getThumbnailPath().equalsIgnoreCase("")) {
                    imageFile = new File(list.get(0).getThumbnailPath());
                    /*Glide.with(this).load(list.get(0).getThumbnailPath())
                    .asBitmap().centerCrop().diskCacheStrategy(SOURCE)
                    .placeholder(AppCompatResources.getDrawable(mActivity, R.drawable.ic_profile_placeholder))
                    .into(new BitmapImageViewTarget(ivAvtar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            progressBarAvtar.setVisibility(View.GONE);
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivAvtar.setImageDrawable(circularBitmapDrawable);
                        }
                    });*/

                    new GlideUtil.GlideUtilBuilder(ivAvtar)
                            .setPlaceholder(R.drawable.ic_profile_placeholder)
                            .setCenterCrop(true)
                            .setLoadItem(list.get(0).getThumbnailPath())
                            .setTransformation(new CircleCrop())
                            .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                                @Override
                                public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                                    progressBarAvtar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {

                                }

                                @Override
                                public void onLoadFailed() {
                                    progressBarAvtar.setVisibility(View.GONE);
                                }
                            }).build();

                    sendPic = true;
                }
            } catch (Exception e) {
                new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_try_again)).build().show();
            }
        } else {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_try_again)).build().show();
        }
    }


    private void renderCustomFields() {
        llCustomFields.removeAllViews();
        if (StorefrontCommonData.getUserData() == null) {
            return;
        }
        for (int i = 0; i < StorefrontCommonData.getUserData().getData().getSignupTemplateData().size(); i++) {
            if (StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i).isShow()) {
                View view = null;
                switch (StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i).getDataType()) {
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
                        view = new CustomFieldTextViewProfile(this, i).render(StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i), setEnable);
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                    case Keys.DataType.SINGLE_SELECT:
                        view = new CustomFieldCheckboxProfile(this, setEnable).render(StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i));
                        break;
                    case Keys.DataType.IMAGE:
                        view = new CustomFieldImageProfile(this).render(StorefrontCommonData.getUserData().getData().getSignupTemplateData().get(i), setEnable);
                        break;
                }
                if (view != null) {
                    llCustomFields.addView(view);
                    if (removeFirstView) {
                        removeFirstView = false;
                    }
                }
            }
        }
        swScrollView.scrollTo(0, 0);
    }

    /**
     * Method to set the Listener for a CustomFieldImageView
     *
     * @param item
     */
    public void setCustomFieldPosition(SignupTemplateData item) {
        this.customFieldPosition = StorefrontCommonData.getUserData().getData().getSignupTemplateData().indexOf(item);
    }

    /**
     * Method to get the CustomFieldImageViewPickup via Listeners
     *
     * @return
     */
    private CustomFieldImageProfile getCustomFieldImageView() {
        List<SignupTemplateData> customFieldsList = StorefrontCommonData.getUserData().getData().getSignupTemplateData();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldImageProfile ? (CustomFieldImageProfile) customFieldView : null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Picker.PICK_IMAGE_DEVICE:
                mImageChooser.onActivityResult(requestCode, resultCode, data);
                break;
            case Picker.PICK_IMAGE_CAMERA:
                mImageChooser.onActivityResult(requestCode, resultCode, data);
                break;
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {

                    CustomFieldImageProfile cFIVCamera = getCustomFieldImageView();
                    if (cFIVCamera != null) {
                        cFIVCamera.compressAndSaveImageBitmap();
                    }
                }
                break;
            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    CustomFieldImageProfile cFIMVGallery = getCustomFieldImageView();
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
                break;
            case OPEN_OTP_SCREEN:
                Utils.snackbarSuccess(mActivity, getStrings(R.string.profile_updated));
                isEditing = false;
                setEnable(false);
                setUserData();
                restartActivity();
                break;
            case Codes.Request.OPEN_ONBOARDING_SCREEN:
                Utils.snackbarSuccess(mActivity, getStrings(R.string.profile_updated));
                isEditing = false;
                setEnable(false);
                setUserData();
                restartActivity();
                break;

            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    etAddress.setText(data.getStringExtra("address"));
                    latitude = data.getDoubleExtra("latitude", 0.0);
                    longitude = data.getDoubleExtra("longitude", 0.0);
                }
                break;

            case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String address = bundle.getString(Keys.Extras.ADDRESS);
                        com.tookancustomer.mapfiles.placeapi.Location location = bundle.getParcelable(com.tookancustomer.mapfiles.placeapi.Location.class.getName());
                        if (location != null) {
                            latitude = location.getLatLng().latitude;
                            longitude = location.getLatLng().longitude;
                            etAddress.setText(address + "");
                        }
                    }
//                    Place place = PlaceAutocomplete.getPlace(this, data);
//                    latitude = place.getLatLng().latitude;
//                    longitude = place.getLatLng().longitude;
//                    etAddress.setText(place.getAddress() + "");
                }
                /*else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    // TODO: Handle the error.
                }*/
                else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
    }

    private void setUserRights() {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }

        if (selectedUserRight == null) {
            Utils.snackBar(mActivity, getStrings(R.string.please_select_customer_right), false);
            return;
        }


        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("right_type", selectedUserRight.getId());
        commonParams.add("description", etDescription.getText().toString().trim());

        RestClient.getApiInterface(mActivity).setCustomerUserRights(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, baseModel.getMessage());
                rlProfileUserRightsHeader.performClick();
                etDescription.setText("");

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedUserRight = StorefrontCommonData.getUserData().getData().getUserRights().get(position);
        etUserRights.setText(StorefrontCommonData.getUserData().getData().getUserRights().get(position).getName());

//        item.setData(position == 0 ? "" : item.getDropdown().get(position - 1).getValue());
//        tvPlaceHolder.setText(item.getData().toString().isEmpty() ? getStrings(R.string.select_text) : item.getData().toString());
//        vCustomFieldIcon.setRotation(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void setStrings() {
        tvHeading.setText(getStrings(R.string.my_profile));
        TextView tvHeaderOption = findViewById(R.id.tvHeaderOption);
        tvHeaderOption.setText(getStrings(R.string.logout));
        tvAdditionalInfo.setText(getStrings(R.string.additional_info));
//        tvCustomerRights.setText(getStrings(R.string.user_rights));
        tvCustomerRights.setText(getStrings(R.string.customer_rights).replace(CUSTOMER, StorefrontCommonData.getTerminology().getCustomer()));
        tvProfileUserRightsSubmit.setText(getStrings(R.string.submit));
        etDescription.setHint(getStrings(R.string.enter_reason));
        ((MaterialEditText) etName).setFloatingLabelText(getStrings(R.string.name));
        ((MaterialEditText) etName).setHint(getStrings(R.string.name));
        ((MaterialEditText) etAddress).setFloatingLabelText(StorefrontCommonData.getTerminology().getAddress());
        ((MaterialEditText) etAddress).setHint(StorefrontCommonData.getTerminology().getAddress());
        ((MaterialEditText) etDOB).setFloatingLabelText(getStrings(R.string.dob));
        ((MaterialEditText) etDOB).setHint(getStrings(R.string.dob));
        ((MaterialEditText) etEmail).setFloatingLabelText(getStrings(R.string.email));
//        ((MaterialEditText) etEmail).setHint(getStrings(R.string.email));
        ((MaterialEditText) etPhone).setFloatingLabelText(getStrings(R.string.phone));
//        ((MaterialEditText) etPhone).setHint(getStrings(R.string.phone));
        ((MaterialEditText) etCurrentPassword).setFloatingLabelText(getStrings(R.string.your_current_password));
        ((MaterialEditText) etCurrentPassword).setHint(getStrings(R.string.your_current_password));
        ((MaterialEditText) etNewPassword).setFloatingLabelText(getStrings(R.string.new_password));
        ((MaterialEditText) etNewPassword).setHint(getStrings(R.string.new_password));
        ((MaterialEditText) etPassword).setFloatingLabelText(getStrings(R.string.pass));
        ((MaterialEditText) etPassword).setHint(getStrings(R.string.pass));
        ((MaterialEditText) etChooseLanguage).setFloatingLabelText(getStrings(R.string.selected_language));
        ((MaterialEditText) etChooseLanguage).setHint(getStrings(R.string.choose_language));
        ((MaterialEditText) etUserRights).setFloatingLabelText(getStrings(R.string.select_text));
        ((MaterialEditText) etUserRights).setHint(getStrings(R.string.select_text));
        tvPhoneHeader.setText(getStrings(R.string.phone));


    }

    private void restartActivity() {
//        recreate();

        if (isLanguageChanged) {
            Bundle extras = new Bundle();
            extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            extras.putBoolean("isLanguageChanged", isLanguageChanged);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(extras);
            setResult(RESULT_OK, returnIntent);
            finish();

            startActivity(getIntent());
        }
    }
}