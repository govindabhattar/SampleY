package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.AddWalletBalanceResponseData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;

public class RedeemGiftCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvHeading, btnRedeemGiftCard;
    private MaterialEditText etGiftCard;
    private ValidateClass validateClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_gift_card);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);
        initViews();
    }

    private void initViews() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.redeem_gift_card).replace(REDEEM, StorefrontCommonData.getTerminology().getRedeem()).replace(GIFT_CARD, StorefrontCommonData.getTerminology().getGiftCard()));

        etGiftCard = findViewById(R.id.etGiftCard);
        etGiftCard.setHint(getStrings(R.string.enter_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getGiftCard()));
        etGiftCard.setFloatingLabelText(StorefrontCommonData.getTerminology().getGiftCard());

        btnRedeemGiftCard = findViewById(R.id.btnRedeemGiftCard);
        btnRedeemGiftCard.setText(StorefrontCommonData.getTerminology().getRedeem());

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), btnRedeemGiftCard);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.btnRedeemGiftCard:
                if (isValidate()) {
                    redeemGiftCard();
                }
                break;
        }
    }

    private boolean isValidate() {
        if (validateClass.genericEmpty(etGiftCard, getStrings(R.string.please_enter_gift_code)))
            return false;

        return true;
    }

    private void redeemGiftCard() {
        CommonParams.Builder commonParamsBuilder = new CommonParams.Builder();
        commonParamsBuilder.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(APP_ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add("code", etGiftCard.getText().toString().trim())
                .add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());

        RestClient.getApiInterface(mActivity).redeemGiftCard(commonParamsBuilder.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Bundle bundleExtra = new Bundle();
                bundleExtra.putString("GIFT_CARD_MESSAGE", baseModel.getMessage());

                Dependencies.setWalletBalance(baseModel.toResponseModel(AddWalletBalanceResponseData.class).getWalletBalanceAfterTxn());

                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundleExtra);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }
}