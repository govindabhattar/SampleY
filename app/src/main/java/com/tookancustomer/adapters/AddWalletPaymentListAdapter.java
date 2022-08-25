package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.OTPActivity;
import com.tookancustomer.R;
import com.tookancustomer.WalletAddMoneyActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.List;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;


/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class AddWalletPaymentListAdapter extends RecyclerView.Adapter<AddWalletPaymentListAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<Datum> dataList;
    private Double paytmWalletAmount = 0.0;
    private Integer paytmVerified = null;

    public AddWalletPaymentListAdapter(Activity activity, List<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    public AddWalletPaymentListAdapter(Activity activity, List<Datum> dataList, Double paytmWalletAmount, Integer paytmVerified) {
        this.activity = activity;
        this.dataList = dataList;
        this.paytmWalletAmount = paytmWalletAmount;
        this.paytmVerified = paytmVerified;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_payment_methods, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int adapterPos = holder.getAdapterPosition();

        holder.rlAddCard.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (!Utils.internetCheck(activity)) {
                                                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                                                        return;
                                                    }
                                                    if (activity instanceof WalletAddMoneyActivity) {
                                                        ((WalletAddMoneyActivity) activity).onPaytmOptionClicked();
                                                        ((WalletAddMoneyActivity) activity).adapterPos = adapterPos;
                                                        ((WalletAddMoneyActivity) activity).selectedCardId = dataList.get(adapterPos).getCardId();
                                                        for (int i = 0; i < dataList.size(); i++) {
                                                            if (adapterPos == i) {
                                                                dataList.get(i).selectedCard = true;
                                                            } else {
                                                                dataList.get(i).selectedCard = false;
                                                            }
                                                            notifyDataSetChanged();
                                                            ((WalletAddMoneyActivity) activity).isCardSelected = true;
                                                        }

                                                    }
                                                }
                                            }
        );

        holder.tvAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 0) {
                    paytmRequestOTP();
                } else if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue
                        && paytmVerified != null
                        && paytmVerified == 1
                        && ((WalletAddMoneyActivity) activity).getAmountToBeAdded() > paytmWalletAmount) {
                    ((WalletAddMoneyActivity) activity).openAddPaytmMoneyWebview();
                } else {
                    holder.rlAddCard.performClick();
                }
            }
        });

        holder.tvCardNumber.setText((PaymentMethodsClass.getCardPaymentMethodsKeySet().contains(dataList.get(adapterPos).getPaymentMethod()) ?
                " * * * * " : "") + dataList.get(adapterPos).getLast4Digits());

        holder.ivSelectedCard.setVisibility(View.VISIBLE);
        holder.ivMore.setVisibility(View.GONE);
        holder.ivCardType.setVisibility(View.GONE);

        if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) {
            holder.pbLoading.setVisibility(View.VISIBLE);
            holder.tvAmount.setVisibility(View.GONE);
        }

        if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 1) {
            holder.pbLoading.setVisibility(View.GONE);
            holder.tvAmount.setVisibility(View.VISIBLE);
            holder.tvAmount.setText(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(paytmWalletAmount));

            if (activity instanceof WalletAddMoneyActivity) {
                if (paytmVerified != null && paytmVerified == 1 && Double.valueOf(((WalletAddMoneyActivity) activity).getAmountToBeAdded()) > paytmWalletAmount) {
                    holder.tvSubHeadingText.setVisibility(View.VISIBLE);
                    holder.tvSubHeadingText.setText(StorefrontCommonData.getString(activity, R.string.low_balance_add_funds).replace("$$$", Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Double.valueOf(((WalletAddMoneyActivity) activity).getAmountToBeAdded()) - paytmWalletAmount)));
                } else {
                    holder.tvSubHeadingText.setVisibility(View.GONE);
                }
            }
        } else if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 0) {
            holder.pbLoading.setVisibility(View.GONE);
            holder.tvAmount.setVisibility(View.VISIBLE);
            holder.tvAmount.setText(StorefrontCommonData.getString(activity, R.string.link));
            holder.tvSubHeadingText.setVisibility(View.GONE);
        } else {
            holder.tvAmount.setVisibility(View.GONE);
            holder.tvSubHeadingText.setVisibility(View.GONE);
        }

        if (dataList.get(adapterPos).selectedCard) {
            holder.ivSelectedCard.setImageResource(R.drawable.ic_radio_button_filled);
        } else {
            holder.ivSelectedCard.setImageResource(R.drawable.ic_radio_button_unfilled);
        }

        if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue) {
            holder.ivPaymentMethod.setImageResource(R.drawable.ic_paytm_logo_84_27);
            holder.ivPaymentMethod.setVisibility(View.VISIBLE);
            holder.ivPaymentMethod.getLayoutParams().width = (int) activity.getResources().getDimensionPixelSize(R.dimen.paytm_width);
            holder.ivPaymentMethod.getLayoutParams().height = (int) activity.getResources().getDimensionPixelSize(R.dimen.paytm_height);
            holder.ivPaymentMethod.requestLayout();
            holder.tvCardNumber.setVisibility(View.GONE);
        } else {
            holder.ivPaymentMethod.setVisibility(View.GONE);
            holder.tvCardNumber.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlAddCard;
        private ImageView ivCardType, ivPaymentMethod;
        private TextView tvCardNumber, tvAmount, tvSubHeadingText;
        private ProgressBar pbLoading;
        ImageButton ivSelectedCard;
        private ImageView ivMore;

        ViewHolder(View itemView) {
            super(itemView);
            rlAddCard = itemView.findViewById(R.id.rlAddCard);
            ivCardType = itemView.findViewById(R.id.ivCardType);
            ivPaymentMethod = itemView.findViewById(R.id.ivPaymentMethod);
            tvCardNumber = itemView.findViewById(R.id.tvName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvSubHeadingText = itemView.findViewById(R.id.tvSubHeadingText);
            ivSelectedCard = itemView.findViewById(R.id.ivSelectedCard);
            ivMore = itemView.findViewById(R.id.ivMore);
            pbLoading = itemView.findViewById(R.id.pbLoading);
        }
    }

    public void paytmRequestOTP() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("app_access_token", Dependencies.getAccessToken(activity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(activity).paytmRequestOTP(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Intent intent = new Intent(activity, OTPActivity.class);
                        intent.putExtra(Keys.Extras.OPEN_OTP_FOR_PAYTM, true);
                        activity.startActivityForResult(intent, Codes.Request.OPEN_OTP_FOR_PAYTM);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }


}