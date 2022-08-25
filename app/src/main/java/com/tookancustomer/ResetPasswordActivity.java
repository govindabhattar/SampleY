package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_REF_ID;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText etNewPassword, etConfirmPassword;
    private ImageButton ibViewNewPassword, ibViewConfirmPassword;
    private ValidateClass validateClass;
    private String secretToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);
        secretToken=getIntent().getStringExtra("secretToken");
        initViews();
        setStrings();
    }


    private void initViews() {
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ibViewNewPassword = findViewById(R.id.ibViewNewPassword);
        ibViewConfirmPassword = findViewById(R.id.ibViewConfirmPassword);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack),
                ibViewNewPassword, ibViewConfirmPassword,
                findViewById(R.id.rlResetPasswordButton));
    }

    private void setStrings() {
        etNewPassword.setHint(getStrings(R.string.new_password));
        etConfirmPassword.setHint(getStrings(R.string.confirm_password));

        ((TextView) findViewById(R.id.tvResetPassword)).setText(getStrings(R.string.reset_password));
        ((TextView) findViewById(R.id.tvResetPasswordMessage)).setText(getStrings(R.string.enter_new_password_for_account));
        ((TextView) findViewById(R.id.tvSubmit)).setText(getStrings(R.string.submit));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ibViewNewPassword:
                boolean isNewPasswordVisible = etNewPassword.getTransformationMethod() == null;
                if (isNewPasswordVisible) {
                    etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ibViewNewPassword.setImageResource(R.drawable.ic_eye_inactive);
                } else {
                    etNewPassword.setTransformationMethod(null);
                    ibViewNewPassword.setImageResource(R.drawable.ic_eye_active);
                }
                break;
            case R.id.ibViewConfirmPassword:
                boolean isConfirmPasswordVisible = etConfirmPassword.getTransformationMethod() == null;
                if (isConfirmPasswordVisible) {
                    etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ibViewConfirmPassword.setImageResource(R.drawable.ic_eye_inactive);
                } else {
                    etConfirmPassword.setTransformationMethod(null);
                    ibViewConfirmPassword.setImageResource(R.drawable.ic_eye_active);
                }
                break;
            case R.id.rlResetPasswordButton:
                if (validate()) {
                    resetPasswordAPICallback();
                }
                break;
        }
    }

    private boolean validate() {
        if (!validateClass.checkPasswordString(etNewPassword, getStrings(R.string.new_password_field_required),
                getStrings(R.string.new_password_field_invalid))) {
            return false;
        }
        if (!validateClass.checkPasswordString(etConfirmPassword, getStrings(R.string.confirm_password_field_required),
                getStrings(R.string.confirm_password_field_invalid))) {
            return false;
        }

        if (!validateClass.doPasswordsMatch(etNewPassword, etConfirmPassword, getStrings(R.string.new_password_confirm_password_same))) {
            return false;
        }
        return true;
    }

    private void resetPasswordAPICallback() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());
        commonParams.add("password", etNewPassword.getText().toString());
        commonParams.add("secret_token", secretToken);

        RestClient.getApiInterface(this).resetPassword(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, baseModel.getMessage());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }
}
