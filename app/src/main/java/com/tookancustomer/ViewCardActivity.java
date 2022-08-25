package com.tookancustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Constants.CARD_AMERICAN_EXPRESS;
import static com.tookancustomer.appdata.Constants.CARD_DINERS_CLUB;
import static com.tookancustomer.appdata.Constants.CARD_DISCOVER;
import static com.tookancustomer.appdata.Constants.CARD_JCB;
import static com.tookancustomer.appdata.Constants.CARD_MASTER;
import static com.tookancustomer.appdata.Constants.CARD_VISA;

public class ViewCardActivity extends BaseActivity implements View.OnClickListener, Keys.Extras {
    private Datum cardData;
    private TextView tvCardNumber, tvExpiryDate, tvCardType;
    private TextView tvHeading;
    private ImageView ivCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        mActivity = this;

        if (getIntent() != null) {
            cardData = (Datum) getIntent().getParcelableExtra(CARD_DATA);
        }

        initializeData();
        setData();
    }

    private void initializeData() {
        tvCardNumber = (TextView) findViewById(R.id.tvName);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.view_card));
        tvExpiryDate = (TextView) findViewById(R.id.tvExpiryDate);
        tvCardType = (TextView) findViewById(R.id.tvCardType);
        ivCardType = (ImageView) findViewById(R.id.ivCardType);

        ((TextView) findViewById(R.id.tvDeleteCard)).setText(getStrings(R.string.delete_card));
        ((TextView) findViewById(R.id.tvType)).setText(getStrings(R.string.type));
        ((TextView) findViewById(R.id.tvValidThru)).setText(getStrings(R.string.valid_thru));
        ((TextView) findViewById(R.id.tvCardDetails)).setText(getStrings(R.string.card_details));


        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.tvDeleteCard));
    }

    private void setData() {
        if (cardData != null) {
            tvCardNumber.setText("XXXX   XXXX   XXXX   " + cardData.getLast4Digits());
            tvExpiryDate.setText(cardData.getExpiryDate());
            tvCardType.setText(cardData.getBrand());
            setCardType();
        }
    }


    /**
     * set Card image
     */
    private void setCardType() {
        switch (cardData.getBrand().trim()) {
            case CARD_VISA:
                ivCardType.setImageResource(R.drawable.ic_visa);
                break;

            case CARD_MASTER:
                ivCardType.setImageResource(R.drawable.ic_mastercard);
                break;

            case CARD_AMERICAN_EXPRESS:
                ivCardType.setImageResource(R.drawable.ic_american_express);
                break;

            case CARD_DISCOVER:
                ivCardType.setImageResource(R.drawable.ic_discover);
                break;

            case CARD_DINERS_CLUB:
                ivCardType.setImageResource(R.drawable.ic_icon_card);
                break;

            case CARD_JCB:
                ivCardType.setImageResource(R.drawable.ic_jcb);
                break;

            default:
                ivCardType.setImageResource(R.drawable.ic_icon_card);
                break;

        }

    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;

            case R.id.tvDeleteCard:
                new OptionsDialog.Builder(mActivity)
                        .message(getStrings(R.string.delete_task_confirmation)).positiveButton(getStrings(R.string.yes_text)).negativeButton(getStrings(R.string.no_text)).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        deleteCard();
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                    }
                }).build().show();
                break;
        }
    }

    private void deleteCard() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("payment_method", cardData.getPaymentMethod())
                .add("card_id", cardData.getCardId())
                .build();

        RestClient.getApiInterface(this).deleteMerchantCards(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                new com.tookancustomer.dialog.AlertDialog.Builder(mActivity)
                        .message(baseModel.getMessage()).button(getStrings(R.string.ok_text)).listener(new com.tookancustomer.dialog.AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        finish();
                    }
                }).build().show();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }
}