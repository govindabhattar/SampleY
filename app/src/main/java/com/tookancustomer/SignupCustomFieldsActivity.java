package com.tookancustomer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tookancustomer.adapters.SignupCustomFieldsAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.CustomFieldCheckboxSignup;
import com.tookancustomer.customviews.CustomFieldImageSignup;
import com.tookancustomer.customviews.CustomFieldTextViewSignup;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.UniversalPojo;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CommonAPIUtils;
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
import java.util.List;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT;

public class SignupCustomFieldsActivity extends BaseActivity {
    UserData userData;
    TextView tvHeaderText, tvRequiredCustomField, tvUserName, tvActivateMessage, tvLogout;
    LinearLayout llCustomFields, llUserInfo;
    RelativeLayout rlActionBar;
    Button btnContinue;
    RecyclerView rvCustomFields;
    SignupCustomFieldsAdapter signupCustomFieldsAdapter;
    ScrollView swScrollView;
    ImageView ivSad;
    boolean broadcastFirst = true;
    boolean isRefreshInProgress, removeFirstView = true;
    private int customFieldPosition;
    private boolean isLoginFromCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_custom_field);
        mActivity = this;
        userData = (UserData) getIntent().getSerializableExtra(UserData.class.getName());
        isLoginFromCheckout = getIntent().getBooleanExtra(IS_LOGIN_FROM_CHECKOUT, false);

        swScrollView = findViewById(R.id.swScrollView);
        tvHeaderText = findViewById(R.id.tvHeaderText);
        tvHeaderText.setText(getStrings(R.string.enter_info));
        tvRequiredCustomField = findViewById(R.id.tvRequiredCustomField);
        tvRequiredCustomField.setText(getStrings(R.string.you_will_need_the_following_to_complete_the_sign_up_process));
        tvUserName = findViewById(R.id.tvUserName);
        tvActivateMessage = findViewById(R.id.tvActivateMessage);
        tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setText(getStrings(R.string.logout));
        rlActionBar = findViewById(R.id.rlBack);
        rlActionBar.setVisibility(View.GONE);
        ivSad = findViewById(R.id.ivSad);
        llCustomFields = findViewById(R.id.llCustomFields);
        llUserInfo = findViewById(R.id.llUserInfo);
        rvCustomFields = findViewById(R.id.rvCustomFields);
        rvCustomFields.setLayoutManager(new LinearLayoutManager(mActivity));
        rvCustomFields.setNestedScrollingEnabled(false);
        btnContinue = findViewById(R.id.btnContinue);

        rlActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEnterCustomFieldsUI();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                if (btnContinue.getText().toString().equalsIgnoreCase(getStrings(R.string.continu))) {
                    showEnterCustomFieldsUI();
                } else if (btnContinue.getText().toString().equalsIgnoreCase(getStrings(R.string.submit))) {
                    if (checkTaskForCompletion()) {
                        submitVenderSignupTemplate();
                    }
                } else if (btnContinue.getText().toString().equalsIgnoreCase(getStrings(R.string.refresh))) {
                    if (isRefreshInProgress) {
                        return;
                    }
                    btnContinue.setClickable(false);
                    loginViaAccessToken();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnContinue.setClickable(true);
                        }
                    }, 6000);
                } else if (btnContinue.getText().toString().equalsIgnoreCase(getStrings(R.string.logout))) {
                    logoutTransition(mActivity);
                }
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                logoutTransition(mActivity);
            }
        });

        if (userData != null) {
            userData.getData().getSignupTemplateData().clear();
            userData.getData().setSignupTemplateData(Dependencies.getSignupTemplate(this));
        }
        switch (userData.getData().getVendorDetails().getRegistrationStatus()) {
            case Constants.RegistrationStatus.OTP_PENDING:
            case Constants.RegistrationStatus.OTP_VERIFIED:
                if (userData.getData().getSignupTemplateData().size() > 0)
                    setCustomFieldAdapter();
                else
                    showReviewUI();
                break;
            case Constants.RegistrationStatus.VERIFICATION_PENDING:
            case Constants.RegistrationStatus.ACKNOWLEDGMENT_PENDING:
                showReviewUI();
                break;
            case Constants.RegistrationStatus.REJECTED:
                showRejectedUI();
                break;
            case Constants.RegistrationStatus.RESUBMIT_VERIFICATION:
                showEnterCustomFieldsUI();
                break;
            case Constants.RegistrationStatus.VERIFIED:
                Bundle extras = new Bundle();
                extras.putSerializable(UserData.class.getName(), userData);
                goToNextActivity(userData, extras);
                break;
        }
        rvCustomFields.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                swScrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("refresh"));
        btnContinue.setText(getStrings(R.string.continu));
        getSupportFragmentManager();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            if (!Utils.preventMultipleClicks()) {
                return;
            }
            if (isRefreshInProgress) {
                return;
            }
            loginViaAccessToken();
        }
    };

    public void setCustomFieldAdapter() {
        if (userData.getData().getSignupTemplateData() != null || userData.getData().getSignupTemplateData().size() > 0) {
            signupCustomFieldsAdapter = new SignupCustomFieldsAdapter(this, userData.getData().getSignupTemplateData(), userData);
            rvCustomFields.setAdapter(signupCustomFieldsAdapter);
        } else {
            showReviewUI();
        }
    }

    private void renderCustomFields() {
        llCustomFields.removeAllViews();
        if (userData == null) {
            return;
        }
        for (int i = 0; i < userData.getData().getSignupTemplateData().size(); i++) {
            if (userData.getData().getSignupTemplateData().get(i).isShow()) {
                View view = null;
                switch (userData.getData().getSignupTemplateData().get(i).getDataType()) {
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
                        view = new CustomFieldTextViewSignup(this, i).render(userData.getData().getSignupTemplateData().get(i));
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                        view = new CustomFieldCheckboxSignup(this).render(userData.getData().getSignupTemplateData().get(i));
                        break;
                    case Keys.DataType.IMAGE:
                        view = new CustomFieldImageSignup(this, i == userData.getData().getSignupTemplateData().size() - 1).render(userData.getData().getSignupTemplateData().get(i));
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
        this.customFieldPosition = userData.getData().getSignupTemplateData().indexOf(item);
    }

    /**
     * Method to get the CustomFieldImageViewPickup via Listeners
     *
     * @return
     */
    private CustomFieldImageSignup getCustomFieldImageView() {
        List<SignupTemplateData> customFieldsList = userData.getData().getSignupTemplateData();
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldImageSignup ? (CustomFieldImageSignup) customFieldView : null;
    }

    public void deleteCustomFieldImageSignup(int position) {
        CustomFieldImageSignup cFIVDeleteImage = getCustomFieldImageView();
        if (cFIVDeleteImage != null) {
            cFIVDeleteImage.deleteImage(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {

                    CustomFieldImageSignup cFIVCamera = getCustomFieldImageView();
                    if (cFIVCamera != null) {
                        cFIVCamera.compressAndSaveImageBitmap();
                    }
                }
                break;
            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    CustomFieldImageSignup cFIMVGallery = getCustomFieldImageView();
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
        }
    }

    /**
     * Method to check whether the Fleet has Completed the Task
     *
     * @return
     */
    private boolean checkTaskForCompletion() {
        // Check for custom fields
        boolean isCustomFieldPassed = true;
        if (userData.getData().getSignupTemplateData() != null) {
            ArrayList<String> requiredCustomFields = new ArrayList<>();
            for (SignupTemplateData item : userData.getData().getSignupTemplateData()) {
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

    private CommonParams.Builder getCommonParamsBuilder() {
        CommonParams.Builder builder = Dependencies.setCommonParamsForAPI(mActivity, userData);
        addCustomFieldsData(builder);
        builder.add("template_name", userData.getData().getSignupTemplateName());
        return builder;
    }

    private void addCustomFieldsData(CommonParams.Builder builder) {
        if (userData.getData().getSignupTemplateData() != null) {
            builder.add("fields", getMetaData());
        }
    }

    private JSONArray getMetaData() {
        JSONArray jArrayMetaData = new JSONArray();
        for (SignupTemplateData item : userData.getData().getSignupTemplateData()) {
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

                    Log.d("DateTimeSignUP", item.getLabel());
                    Log.d("DateTimeSignUP", data);

                }
            } catch (JSONException e) {

                               Utils.printStackTrace(e);
            }
        }
        return jArrayMetaData;
    }

    public void submitVenderSignupTemplate() {
        RestClient.getApiInterface(this).submitVenderSignupTemplate(getCommonParamsBuilder().build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UniversalPojo pojo = baseModel.toResponseModel(UniversalPojo.class);
                userData.getData().getVendorDetails().setRegistrationStatus(pojo.getRegistration_status());
                StorefrontCommonData.setUserData(userData);
                Log.e("Status", "Registration Status : " + pojo.getRegistration_status());
                showReviewUI();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void loginViaAccessToken() {
        Location location = LocationUtils.getLastLocation(this);
        isRefreshInProgress = true;
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, userData);
        commonParams.add("lat", location.getLatitude())
                .add("lng", location.getLongitude());

        RestClient.getApiInterface(this).loginViaAccessToken(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                UserData userDatas = new UserData();
                try {
                    userDatas.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));

                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                userData = userDatas;
                Utils.saveUserInfo(mActivity, userData, !isLoginFromCheckout);

//                Utils.saveUserInfo(mActivity, userDatas);

                Bundle extras = new Bundle();
                extras.putSerializable(UserData.class.getName(), userDatas);
                if (userDatas.getData().getVendorDetails().getIsPhoneVerified() == 0 && UIManager.isOTPAvailable()) {
                    Transition.transit(mActivity, OTPActivity.class, extras);
                } else {
                    Bundle extra = getIntent().getExtras();
                    if (extra != null) {
                        try {
                            int jobID = Integer.valueOf(extra.getString("job_id"));
                            int flag = extra.getInt("flag", -1);
                            int status = extra.getInt("jobStatus");
                            Class<?> toClass = TasksActivity.class;
                            if (flag == Constants.NotificationFlags.TASK_UPDATE) {
                                if (status == Constants.TaskStatus.ACCEPTED.value) {
                                    toClass = TasksActivity.class;
                                } else {
                                    toClass = Transition.launchOrderDetailsActivity();
                                }
                            } else if (flag == Constants.NotificationFlags.JOB_DELETED) {
                                toClass = Transition.launchHomeActivity();
                            } else {
                                toClass = TasksActivity.class;
                            }
                            extras.putInt(Keys.Extras.JOB_ID, jobID);
                            Transition.transit(mActivity, toClass, extras);
                        } catch (Exception e) {
                            switch (userDatas.getData().getVendorDetails().getRegistrationStatus()) {
                                case Constants.RegistrationStatus.OTP_VERIFIED:
                                    // layoutStartVerification.setVisibility(View.VISIBLE);
                                    break;
                                case Constants.RegistrationStatus.VERIFICATION_PENDING:
                                case Constants.RegistrationStatus.ACKNOWLEDGMENT_PENDING:
                                    showReviewUI();
                                    break;
                                case Constants.RegistrationStatus.REJECTED:
                                    showRejectedUI();
                                    break;
                                case Constants.RegistrationStatus.RESUBMIT_VERIFICATION:
                                    showEnterCustomFieldsUI();
                                    Utils.snackBar(mActivity, getStrings(R.string.please_resubmit_the_additional_information));
                                    break;
                                case Constants.RegistrationStatus.VERIFIED:
                                    goToNextActivity(userDatas, extras);
                                    break;
                            }
                        }
                    } else {
                        switch (userDatas.getData().getVendorDetails().getRegistrationStatus()) {
                            case Constants.RegistrationStatus.OTP_VERIFIED:
                                break;
                            case Constants.RegistrationStatus.VERIFICATION_PENDING:
                            case Constants.RegistrationStatus.ACKNOWLEDGMENT_PENDING:
                                showReviewUI();
                                break;
                            case Constants.RegistrationStatus.REJECTED:
                                showRejectedUI();
                                break;
                            case Constants.RegistrationStatus.RESUBMIT_VERIFICATION:
                                showEnterCustomFieldsUI();
                                Utils.snackBar(mActivity, getStrings(R.string.please_submit_the_aditional_information_again));
                                break;
                            case Constants.RegistrationStatus.VERIFIED:
                                goToNextActivity(userDatas, extras);
                                break;
                        }

                    }
                }

                broadcastFirst = true;
                isRefreshInProgress = false;
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 900 && error.getMessage().equalsIgnoreCase(getStrings(R.string.socket_timeout_connection))) {
                    new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            loginViaAccessToken();
                        }
                    }).build().show();
                }
                isRefreshInProgress = false;
            }
        });
    }

    private void goToNextActivity(UserData userData, Bundle extras) {
        if (isLoginFromCheckout) {
            Bundle bundleExtra = getIntent().getExtras();
            bundleExtra.putSerializable(UserData.class.getName(), userData);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            CommonAPIUtils.getSuperCategories(userData, mActivity);
        }
    }

    public void showEnterCustomFieldsUI() {
        userData = StorefrontCommonData.getUserData();
        rlActionBar.setVisibility(View.VISIBLE);
        rvCustomFields.setVisibility(View.GONE);
        tvRequiredCustomField.setVisibility(View.GONE);
        llCustomFields.setVisibility(View.VISIBLE);
        btnContinue.setText(getStrings(R.string.submit));
        tvLogout.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        tvActivateMessage.setVisibility(View.GONE);
        tvHeaderText.setText(getStrings(R.string.enter_info));
        ivSad.setVisibility(View.GONE);
        renderCustomFields();
    }

    public void hideEnterCustomFieldsUI() {
        rlActionBar.setVisibility(View.GONE);
        rvCustomFields.setVisibility(View.VISIBLE);
        tvRequiredCustomField.setVisibility(View.VISIBLE);
        llCustomFields.setVisibility(View.GONE);
        btnContinue.setText(getStrings(R.string.continu));
        tvLogout.setVisibility(View.VISIBLE);
        tvUserName.setVisibility(View.VISIBLE);
        tvActivateMessage.setVisibility(View.VISIBLE);
        tvHeaderText.setText(getStrings(R.string.enter_info));
        ivSad.setVisibility(View.GONE);
        setCustomFieldAdapter();
        swScrollView.scrollTo(0, 0);
    }

    public void showReviewUI() {
        if (userData.getData().getVendorDetails().getRegistrationStatus() == Constants.RegistrationStatus.VERIFIED) {
            goToNextActivity(userData, new Bundle());
            return;
        }
        rlActionBar.setVisibility(View.GONE);
        btnContinue.setText(getStrings(R.string.refresh));
        llCustomFields.setVisibility(View.GONE);
        tvRequiredCustomField.setVisibility(View.VISIBLE);
        tvRequiredCustomField.setText(userData.getData().getVendorSignupInfo());
        tvUserName.setVisibility(View.GONE);
        tvActivateMessage.setVisibility(View.GONE);
        tvUserName.setText(userData.getData().getVendorDetails().getFirstName() + " " + userData.getData().getVendorDetails().getLastName());
        tvActivateMessage.setText(userData.getData().getVendorSignupInfo());
        tvHeaderText.setText(getStrings(R.string.your_account_is));
        tvLogout.setVisibility(View.VISIBLE);
        ivSad.setVisibility(View.GONE);
    }

    public void showRejectedUI() {
        rlActionBar.setVisibility(View.GONE);
        btnContinue.setText(getStrings(R.string.logout));
        tvLogout.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        tvActivateMessage.setVisibility(View.GONE);
        tvRequiredCustomField.setVisibility(View.VISIBLE);
        tvRequiredCustomField.setText(getStrings(R.string.please_contact_support_for_further_details));
        tvUserName.setText(userData.getData().getVendorDetails().getFirstName() + " " + userData.getData().getVendorDetails().getLastName());
        tvHeaderText.setText(getStrings(R.string.account_has_been_rejected));
        tvActivateMessage.setText(getStrings(R.string.account_has_been_rejected) + "," + getStrings(R.string.please_contact_support_for_further_details));
        ivSad.setVisibility(View.VISIBLE);
    }

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
                logoutUser(mActivity);
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
    public void onBackPressed() {
//        super.onBackPressed();

        if (rlActionBar.getVisibility() == View.VISIBLE) {
            hideEnterCustomFieldsUI();
        }
    }
}