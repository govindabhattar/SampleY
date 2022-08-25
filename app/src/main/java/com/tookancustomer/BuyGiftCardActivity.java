package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tookancustomer.appdata.Codes.Request.OPEN_GIFT_CARD_PAYMENT_ACTIVITY;

public class BuyGiftCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvHeading;
    private TextView tvPurchaseGiftCard;

    private TextView tvCurrencySymbol, tvGiftCardDescription, tvGiftCardAmountHeader;
    private MaterialEditText etGiftCardAmount;

    private TextView tvGiftCardSenderDetailsHeader, tvSenderNameLabel, tvMessageLabel;
    private EditText etSenderName, etMessage;

    private TextView tvRecipientHeader, tvRecipientNameLabel, tvRecipientEmailLabel;
    private EditText etRecipientName, etRecipientEmail;

    private ValidateClass validateClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_gift_card);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);
        initView();
    }

    private void initView() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.buy_gift_card).replace(BUY, StorefrontCommonData.getTerminology().getBuy())
                .replace(GIFT_CARD, StorefrontCommonData.getTerminology().getGiftCard()));

        tvPurchaseGiftCard = findViewById(R.id.tvPurchaseGiftCard);
        tvPurchaseGiftCard.setText(getStrings(R.string.proceed));

//        tvCurrencySymbol = findViewById(R.id.tvCurrencySymbol);
//        tvCurrencySymbol.setText(Utils.getCurrencySymbol());myvehicleconcierge@gmail.com

        tvGiftCardAmountHeader = findViewById(R.id.tvGiftCardAmountHeader);
        tvGiftCardAmountHeader.setText(Utils.capitaliseWords(getStrings(R.string.amount)));
        etGiftCardAmount = findViewById(R.id.etGiftCardAmount);
        etGiftCardAmount.setHint(getStrings(R.string.enter_amount));
        etGiftCardAmount.setFloatingLabelText(Utils.capitaliseWords(getStrings(R.string.amount)) + " (" + Utils.getCurrencySymbol() + ")");


        tvGiftCardDescription = findViewById(R.id.tvGiftCardDescription);
        tvGiftCardDescription.setText(StorefrontCommonData.getAppConfigurationData().getGiftCardDescription());

        tvGiftCardSenderDetailsHeader = findViewById(R.id.tvGiftCardSenderDetailsHeader);
        tvGiftCardSenderDetailsHeader.setText(getStrings(R.string.sender));
        tvSenderNameLabel = findViewById(R.id.tvSenderNameLabel);
        tvSenderNameLabel.setText(getStrings(R.string.name));
        etSenderName = findViewById(R.id.etSenderName);
        etSenderName.setHint(getStrings(R.string.enter_name));
        etSenderName.setText(StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName());

        tvMessageLabel = findViewById(R.id.tvMessageLabel);
        tvMessageLabel.setText(getStrings(R.string.message));
        etMessage = findViewById(R.id.etMessage);
        etMessage.setHint(getStrings(R.string.enter_message));
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.etMessage && etMessage.getLineCount() > 5) {
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

        tvRecipientHeader = findViewById(R.id.tvRecipientHeader);
        tvRecipientHeader.setText(getStrings(R.string.recipient));
        tvRecipientNameLabel = findViewById(R.id.tvRecipientNameLabel);
        tvRecipientNameLabel.setText(getStrings(R.string.name));
        etRecipientName = findViewById(R.id.etRecipientName);
        etRecipientName.setHint(getStrings(R.string.enter_name));
        tvRecipientEmailLabel = findViewById(R.id.tvRecipientEmailLabel);
        tvRecipientEmailLabel.setText(getStrings(R.string.email));
        etRecipientEmail = findViewById(R.id.etRecipientEmail);
        etRecipientEmail.setHint(getStrings(R.string.enter_email));

        Utils.setOnClickListener(this, findViewById(R.id.rlBack), tvPurchaseGiftCard);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.tvPurchaseGiftCard:
                if (isValidate()) {
//                    proceedPurchaseGiftCard();
                    getPaymentMethods();
                }
                break;
        }
    }

    private boolean isValidate() {
        if (getGiftCardAmount() <= 0) {
            Utils.snackBar(mActivity, getStrings(R.string.please_enter_amount));
            return false;
        }
        if (!validateClass.checkName(etSenderName))
            return false;

        if (validateClass.genericEmpty(etMessage, getStrings(R.string.please_enter_message)))
            return false;

        if (validateClass.genericEmpty(etRecipientName, getStrings(R.string.enter_name)))
            return false;

        if (!validateClass.checkEmail(etRecipientEmail, getStrings(R.string.email_field_required)))
            return false;

        return true;
    }

    public double getGiftCardAmount() {
        try {
            if (!(etGiftCardAmount.getText().toString().trim().isEmpty()) && !etGiftCardAmount.getText().toString().trim().equals(".")) {
                return Double.valueOf(etGiftCardAmount.getText().toString().trim());
            } else {
                return 0;
            }
        } catch (final NumberFormatException ex) {
            return 0;
        } catch (final NullPointerException ex) {
            return 0;
        }
    }


    private void proceedPurchaseGiftCard() {
        Bundle bundle = new Bundle();
        bundle.putDouble("GIFT_CARD_AMOUNT", getGiftCardAmount());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender_name", etSenderName.getText().toString().trim());
        hashMap.put("receiver_name", etRecipientName.getText().toString().trim());
        hashMap.put("receiver_email", etRecipientEmail.getText().toString().trim());
        hashMap.put("message", etMessage.getText().toString().trim());
        bundle.putSerializable("GIFT_CARD_DATA", hashMap);
        Transition.transitForResult(mActivity, GiftCardPaymentActivity.class, OPEN_GIFT_CARD_PAYMENT_ACTIVITY, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_GIFT_CARD_PAYMENT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    Transition.exit(this);
                }else {
                    setResult(RESULT_ERROR, data);
                    Transition.exit(this);
                }
                break;
        }
    }

    private void getPaymentMethods() {

        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
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
                proceedPurchaseGiftCard();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }


}