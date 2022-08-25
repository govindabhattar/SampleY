package com.tookancustomer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.CustomFieldCheckboxSignup;
import com.tookancustomer.customviews.CustomFieldImageSignup;
import com.tookancustomer.customviews.CustomFieldTextViewSignup;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.UniversalPojo;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT;

public class SignupTemplateFragment extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private TextView tvHeaderText;
    private LinearLayout llCustomFields;
    private Button btnContinue;
    private ScrollView svScrollView;
    private int customFieldPosition;

    public UserData userData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_template, container, false);
        mActivity = getActivity();
        initViews(rootView);
        setUserData();
        return rootView;
    }

    public void initViews(View view) {
        svScrollView = view.findViewById(R.id.svScrollView);
        tvHeaderText = view.findViewById(R.id.tvHeaderText);
        tvHeaderText.setText(StorefrontCommonData.getString(mActivity, R.string.enter_info));
        llCustomFields = view.findViewById(R.id.llCustomFields);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setText(StorefrontCommonData.getString(mActivity, R.string.submit));

        Utils.setOnClickListener(this, btnContinue);
    }

    public void setUserData() {
        if (mActivity != null) {
            userData = StorefrontCommonData.getUserData();
            if (userData != null) {
                userData.getData().getSignupTemplateData().clear();
                userData.getData().setSignupTemplateData(Dependencies.getSignupTemplate(mActivity));
            }
            renderCustomFields();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                if (checkTaskForCompletion()) {
                    submitVenderSignupTemplate();
                }
                break;
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
                        view = new CustomFieldTextViewSignup(mActivity, i).render(userData.getData().getSignupTemplateData().get(i));
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                    case Keys.DataType.SINGLE_SELECT:
                        view = new CustomFieldCheckboxSignup(mActivity).render(userData.getData().getSignupTemplateData().get(i));
                        break;
                    case Keys.DataType.IMAGE:
                        view = new CustomFieldImageSignup(mActivity, i == userData.getData().getSignupTemplateData().size() - 1).render(userData.getData().getSignupTemplateData().get(i));
                        break;
                }
                if (view != null) {
                    llCustomFields.addView(view);
                }
            }
        }
        svScrollView.scrollTo(0, 0);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == mActivity.RESULT_OK) {

                    CustomFieldImageSignup cFIVCamera = getCustomFieldImageView();
                    if (cFIVCamera != null) {
                        cFIVCamera.compressAndSaveImageBitmap();
                    }
                }
                break;
            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == mActivity.RESULT_OK) {
                    CustomFieldImageSignup cFIMVGallery = getCustomFieldImageView();
                    if (cFIMVGallery != null) {
                        try {
                            cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                            cFIMVGallery.compressAndSaveImageBitmap();
                        } catch (IOException e) {

                               Utils.printStackTrace(e);
                            Utils.toast(mActivity, StorefrontCommonData.getString(mActivity, R.string.could_not_read_image));
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
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : StorefrontCommonData.getString(mActivity, R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : StorefrontCommonData.getString(mActivity, R.string.invalid_url);
                            break;
                        case Keys.DataType.TELEPHONE:
                            error = (data.isEmpty() || Utils.isValidPhoneNumberWithCountryCode(data)) ? null : StorefrontCommonData.getString(mActivity, R.string.invalid_phone_number);
                            break;
                        case Keys.DataType.CHECKBOX:
                            error = (data.isEmpty() || data.equals("true")) ? null : item.getLabel().replace("_", " ").substring(0, 1).toUpperCase() + item.getLabel().replace("_", " ").substring(1).toLowerCase() + StorefrontCommonData.getString(mActivity, R.string.is_required);
                            break;
                    }
                }
                if (data.isEmpty() && item.getRequired() == 1 && isDataTypeHandled) {
                    Utils.snackBar(mActivity, item.getLabel().replace("_", " ").substring(0, 1).toUpperCase() + item.getLabel().replace("_", " ").substring(1).toLowerCase() + " " + StorefrontCommonData.getString(mActivity, R.string.is_required));
                    return false;
                }

                if (!data.isEmpty() && isDataTypeHandled) {
                    switch (item.getDataType()) {
                        case Keys.DataType.EMAIL:
                            error = (data.isEmpty() || Utils.isEmailValid(data)) ? null : StorefrontCommonData.getString(mActivity, R.string.invalid_email);
                            break;
                        case Keys.DataType.URL:
                            error = (data.isEmpty() || Utils.isUrlValid(data)) ? null : StorefrontCommonData.getString(mActivity, R.string.invalid_url);
                            break;
//                        case Keys.DataType.TELEPHONE:
//                            error = (data.isEmpty() || Utils.isValidPhoneNumberWithCountryCode(data)) ? null : getStrings(R.string.invalid_phone_number);
//                            break;
                    }

                    if (error != null) {
                        Utils.snackBar(mActivity, error);
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
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.fields_invalid_text) + "\n" + errorFiels.replace("_", " ").substring(0, 1).toUpperCase() + errorFiels.replace("_", " ").substring(1).toLowerCase());
            }
        }

        return isCustomFieldPassed;
    }

    private CommonParams.Builder getCommonParamsBuilder() {
        CommonParams.Builder builder = Dependencies.setCommonParamsForAPI(mActivity, userData);
        if (userData.getData().getSignupTemplateData() != null) {
            builder.add("fields", getMetaData());
        }
        builder.add("template_name", userData.getData().getSignupTemplateName());
        return builder;
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

                }else if (item.getDataType().equals(Keys.DataType.DATETIME) || item.getDataType().equals(Keys.DataType.DATETIME_FUTURE)
                        || item.getDataType().equals(Keys.DataType.DATETIME_PAST)) {

                    String formattedDATE = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(DateUtils.getInstance().getDate(data));
//                    String formattedDATE = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(DateUtils.getInstance().getDate(data));

                    jsonObjectPickUpMetaData.put("label", item.getLabel());
                    jsonObjectPickUpMetaData.put("data", formattedDATE);
                    jArrayMetaData.put(jsonObjectPickUpMetaData);
                }

                else {
                    jsonObjectPickUpMetaData.put("label", item.getLabel());
                    jsonObjectPickUpMetaData.put("data", data);
                    jArrayMetaData.put(jsonObjectPickUpMetaData);


                    Log.d("DateTimeSignUP",item.getLabel());
                    Log.d("DateTimeSignUP",data);
                }
            } catch (JSONException e) {

                               Utils.printStackTrace(e);
            }
        }
        return jArrayMetaData;
    }

    public void submitVenderSignupTemplate() {
        RestClient.getApiInterface(mActivity).submitVenderSignupTemplate(getCommonParamsBuilder().build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UniversalPojo pojo = baseModel.toResponseModel(UniversalPojo.class);
                userData.getData().getVendorDetails().setRegistrationStatus(pojo.getRegistration_status());
                StorefrontCommonData.setUserData(userData);
                Log.e("Status", "Registration Status : " + pojo.getRegistration_status());

                if (mActivity instanceof RegistrationOnboardingActivity) {
                    ((RegistrationOnboardingActivity) mActivity).setCurentStep();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }


}