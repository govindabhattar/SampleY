package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.PaymentMethodActivity;
import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.ViewCardActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.fragments.SignupFeeFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.modules.payment.PaymentManager;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.List;

import static com.tookancustomer.appdata.Constants.CARD_AMERICAN_EXPRESS;
import static com.tookancustomer.appdata.Constants.CARD_DINERS_CLUB;
import static com.tookancustomer.appdata.Constants.CARD_DISCOVER;
import static com.tookancustomer.appdata.Constants.CARD_JCB;
import static com.tookancustomer.appdata.Constants.CARD_MASTER;
import static com.tookancustomer.appdata.Constants.CARD_VISA;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_TYPE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_VERSION;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FORM_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_DEMO_APP;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_REF_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.YELO_APP_TYPE;
import static com.tookancustomer.appdata.Keys.Prefs.DEVICE_TOKEN;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.AUTHORISE_DOT_NET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.FAC;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INAPP_WALLET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM_LINK;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_ON_DELIVERY;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<Datum> dataList;
    private Double paytmWalletAmount = 0.0;
    private Integer paytmVerified = null;
    private OnPaymentOptionSelectedListener listener;

    public PaymentListAdapter(Activity activity, List<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    public PaymentListAdapter(Activity activity, List<Datum> dataList, Double paytmWalletAmount, Integer paytmVerified) {
        this.activity = activity;
        this.dataList = dataList;
        this.paytmWalletAmount = paytmWalletAmount;
        this.paytmVerified = paytmVerified;
    }

    public PaymentListAdapter(Activity activity, List<Datum> dataList,
                              Double paytmWalletAmount, Integer paytmVerified,
                              OnPaymentOptionSelectedListener listener) {
        this.activity = activity;
        this.dataList = dataList;
        this.paytmWalletAmount = paytmWalletAmount;
        this.paytmVerified = paytmVerified;
        this.listener = listener;
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
                                                    if (activity instanceof MakePaymentActivity) {
                                                        ((MakePaymentActivity) activity).adapterPos = adapterPos;
                                                        if (((MakePaymentActivity) activity).billBreakDown != null)
                                                            ((MakePaymentActivity) activity).onPaytmOptionClicked();

                                                        if (((MakePaymentActivity) activity).billBreakDown != null && ((MakePaymentActivity) activity).billBreakDown.getData().getPaymentSettings() != null) {
                                                            ((MakePaymentActivity) activity).currencySymbol = ((MakePaymentActivity) activity).billBreakDown.getData().getPaymentSettings().getSymbol();
                                                        }



                                                        ((MakePaymentActivity) activity).selectedCardId = dataList.get(adapterPos).getCardId();

                                                        for (int i = 0; i < dataList.size(); i++) {
                                                            if (adapterPos == i) {
                                                                dataList.get(i).selectedCard = true;
                                                            } else {
                                                                dataList.get(i).selectedCard = false;
                                                            }
                                                            notifyDataSetChanged();
                                                            ((MakePaymentActivity) activity).isCardSelected = true;
                                                        }

                                                    } else if (activity instanceof RegistrationOnboardingActivity) {

                                                        SignupFeeFragment signUpFeeFragment = (SignupFeeFragment) ((RegistrationOnboardingActivity) activity).mPagerAdapter.getItem(3);
                                                        if (signUpFeeFragment != null) {
                                                            signUpFeeFragment.onPaytmOptionClicked();
                                                            signUpFeeFragment.adapterPos = adapterPos;
                                                            signUpFeeFragment.selectedCardId = dataList.get(adapterPos).getCardId();
                                                            for (int i = 0; i < dataList.size(); i++) {
                                                                if (adapterPos == i) {
                                                                    dataList.get(i).selectedCard = true;
                                                                } else {
                                                                    dataList.get(i).selectedCard = false;
                                                                }
                                                                notifyDataSetChanged();
                                                                signUpFeeFragment.isCardSelected = true;
                                                            }
                                                        }

                                                    } else if (activity instanceof PaymentMethodActivity) {
                                                        if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue)
                                                            ((PaymentMethodActivity) activity).onPaytmOptionClicked();
                                                    }

                                                    /*if (listener != null) {
                                                        listener.onPaymentOptionSelected(dataList.get(adapterPos));
                                                    }
                                                    */
                                                }
                                            }
        );

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(adapterPos, holder.ivMore);
            }
        });

        holder.tvAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 0) {
                    PaymentManager.requestOtpForPaytm(activity);
                } else if (activity instanceof MakePaymentActivity && dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue
                        && paytmVerified != null && paytmVerified == 1 && ((MakePaymentActivity) activity).billBreakDown != null
                        && Double.valueOf(((MakePaymentActivity) activity).billBreakDown.getData().getPaybleAmount()) > paytmWalletAmount) {
                    ((MakePaymentActivity) activity).openAddPaytmMoneyWebview();
                } else if (activity instanceof RegistrationOnboardingActivity
                        && dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue
                        && paytmVerified != null
                        && paytmVerified == 1
                        && Double.valueOf(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()) > paytmWalletAmount) {
                    SignupFeeFragment signUpFeeFragment = (SignupFeeFragment) ((RegistrationOnboardingActivity) activity).mPagerAdapter.getItem(3);
                    if (signUpFeeFragment != null) {
                        signUpFeeFragment.openAddPaytmMoneyWebview();
                    }
                } else {
                    holder.rlAddCard.performClick();
                }
            }
        });

        holder.tvCardNumber.setText((PaymentMethodsClass.getCardPaymentMethodsKeySet().contains(dataList.get(adapterPos).getPaymentMethod()) ?
                " * * * * " : "") + dataList.get(adapterPos).getLast4Digits());

        if (activity instanceof MakePaymentActivity || activity instanceof RegistrationOnboardingActivity) {
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
                holder.tvAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(paytmWalletAmount)));

                if (activity instanceof MakePaymentActivity) {
                    if (((MakePaymentActivity) activity).billBreakDown != null && paytmVerified != null && paytmVerified == 1 && Double.valueOf(((MakePaymentActivity) activity).billBreakDown.getData().getPaybleAmount()) > paytmWalletAmount) {
                        holder.tvSubHeadingText.setVisibility(View.VISIBLE);
                        holder.tvSubHeadingText.setText(StorefrontCommonData.getString(activity, R.string.low_balance_add_funds).replace("$$$", Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Double.valueOf(((MakePaymentActivity) activity).billBreakDown.getData().getPaybleAmount()) - paytmWalletAmount)));
                    } else {
                        holder.tvSubHeadingText.setVisibility(View.GONE);
                    }
                } else if (activity instanceof RegistrationOnboardingActivity) {
                    SignupFeeFragment signUpFeeFragment = (SignupFeeFragment) ((RegistrationOnboardingActivity) activity).mPagerAdapter.getItem(3);
                    if (signUpFeeFragment != null) {
                        if (paytmVerified != null && paytmVerified == 1 && Double.valueOf(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()) > paytmWalletAmount) {
                            holder.tvSubHeadingText.setVisibility(View.VISIBLE);
                            holder.tvSubHeadingText.setText(StorefrontCommonData.getString(activity, R.string.low_balance_add_funds).replace("$$$", Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Double.valueOf(StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPlanAmount()) - paytmWalletAmount)));
                        } else {
                            holder.tvSubHeadingText.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 0) {
                holder.pbLoading.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText(StorefrontCommonData.getString(activity, R.string.link));
                holder.tvSubHeadingText.setVisibility(View.GONE);
            } else if (dataList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue) {
                holder.pbLoading.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getWalletBalance())));
                holder.tvSubHeadingText.setVisibility(View.GONE);
            } else {
                holder.tvAmount.setVisibility(View.GONE);
                holder.tvSubHeadingText.setVisibility(View.GONE);
            }

            if (dataList.get(adapterPos).selectedCard) {
                /**
                 * on card selected show amount will be holded alert
                 * if payfort hold flow is there
                 */
                if (listener != null) {
                    listener.onPaymentOptionSelected(dataList.get(adapterPos));
                }

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
        } else {
            if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 1) {
                holder.pbLoading.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(paytmWalletAmount)));
            } else if (dataList.get(adapterPos).getPaymentMethod() == PAYTM.intValue && paytmVerified != null && paytmVerified == 0) {
                holder.pbLoading.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText(StorefrontCommonData.getString(activity, R.string.link));
            } else if (dataList.get(adapterPos).getPaymentMethod() == INAPP_WALLET.intValue) {
                holder.pbLoading.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getWalletBalance())));
            } else {
                holder.tvAmount.setVisibility(View.GONE);
            }

            holder.ivMore.setVisibility(PaymentMethodsClass.getCardPaymentMethodsKeySet().contains(dataList.get(adapterPos).getPaymentMethod()) ? View.VISIBLE : View.GONE);
            holder.tvSubHeadingText.setVisibility(View.GONE);
            holder.ivSelectedCard.setVisibility(View.GONE);
            holder.ivCardType.setVisibility(View.VISIBLE);
            setCardType(adapterPos, holder);
        }

    }

    /**
     * set Card image
     *
     * @param pos    postion
     * @param holder view
     */
    private void setCardType(final int pos, ViewHolder holder) {
        switch (dataList.get(pos).getBrand().trim()) {
            case CARD_VISA:
                holder.ivCardType.setImageResource(R.drawable.ic_visa);
                break;

            case CARD_MASTER:
                holder.ivCardType.setImageResource(R.drawable.ic_mastercard);
                break;

            case CARD_AMERICAN_EXPRESS:
                holder.ivCardType.setImageResource(R.drawable.ic_american_express);
                break;

            case CARD_DISCOVER:
                holder.ivCardType.setImageResource(R.drawable.ic_discover);
                break;

            case CARD_DINERS_CLUB:
                holder.ivCardType.setImageResource(R.drawable.ic_icon_card);
                break;

            case CARD_JCB:
                holder.ivCardType.setImageResource(R.drawable.ic_jcb);
                break;

            default:
                if (dataList.get(pos).getPaymentMethod() == PAYTM.intValue) {
                    holder.ivCardType.setImageResource(R.drawable.ic_paytm);
                } else if (dataList.get(pos).getPaymentMethod() == PAYTM_LINK.intValue) {
                    holder.ivCardType.setImageResource(R.drawable.ic_paytm);
                } else if (dataList.get(pos).getPaymentMethod() == CASH.intValue) {
                    holder.ivCardType.setImageResource(R.drawable.ic_icon_cash);
                } else if (dataList.get(pos).getPaymentMethod() == INAPP_WALLET.intValue) {
                    holder.ivCardType.setImageResource(R.drawable.ic_wallet);
                }else if (dataList.get(pos).getPaymentMethod() == PAY_ON_DELIVERY.intValue) {
                    holder.ivCardType.setImageResource(R.drawable.ic_icon_card);
                } else {
                    holder.ivCardType.setImageResource(R.drawable.ic_icon_card);
                }
                break;
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

    public void showMoreOptions(final int adapterPos, View view) {
        PopupMenu popup;
        //Creating the instance of PopupMenu
        popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_payment_options, popup.getMenu());
        popup.setGravity(Gravity.BOTTOM);

        MenuItem itemView = popup.getMenu().findItem(R.id.itemView);
        itemView.setTitle(StorefrontCommonData.getString(activity, R.string.view_menu_option));
        MenuItem itemDelete = popup.getMenu().findItem(R.id.itemDelete);
        itemDelete.setTitle(StorefrontCommonData.getString(activity, R.string.delete));

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(StorefrontCommonData.getString(activity, R.string.view_menu_option))) {
                    Intent intent = new Intent(activity, ViewCardActivity.class);
                    intent.putExtra(CARD_DATA, dataList.get(adapterPos));
                    activity.startActivityForResult(intent, 100);
                }
                if (item.getTitle().equals(StorefrontCommonData.getString(activity, R.string.delete))) {
                    deleteCard(adapterPos);
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private void deleteCard(final int adapterPos) {
        if (dataList.get(adapterPos).getPaymentMethod() == AUTHORISE_DOT_NET.intValue) {

            CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
            commonParams.add("is_active", "0")
                    .add("card_id", dataList.get(adapterPos).getCardId())
                    .add("last4_digits", dataList.get(adapterPos).getLast4Digits())
                    .add("expiry_date", dataList.get(adapterPos).getExpiryDate())
                    .add("brand", dataList.get(adapterPos).getBrand())
                    .build();

            RestClient.getApiInterface(activity).deleteMerchantCards_AuthorizeDotNet(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                @Override
                public void success(BaseModel baseModel) {
                    Utils.snackbarSuccess(activity, baseModel.getMessage());
                    dataList.remove(adapterPos);
                    notifyDataSetChanged();
                    if (activity instanceof PaymentMethodActivity) {
                        ((PaymentMethodActivity) activity).setPaymentListViews();
                    }
                }

                @Override
                public void failure(APIError error, BaseModel baseModel) {
                }
            });
        } else if (dataList.get(adapterPos).getPaymentMethod() == FAC.intValue) {


            CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
            commonParams.add("is_active", "0")
                    .add("card_id", dataList.get(adapterPos).getCardId())
                    .add("payment_method", dataList.get(adapterPos).getPaymentMethod())
                    .add("last4_digits", dataList.get(adapterPos).getLast4Digits())
                    .add("expiry_date", dataList.get(adapterPos).getExpiryDate())
                    .add("brand", dataList.get(adapterPos).getBrand())
                    .build();

            commonParams.remove(APP_VERSION);
            commonParams.remove(APP_TYPE);
            commonParams.remove(REFERENCE_ID);
            commonParams.remove(MARKETPLACE_REF_ID);
            commonParams.remove(APP_ACCESS_TOKEN);
            commonParams.remove(FORM_ID);
            commonParams.remove(YELO_APP_TYPE);
            commonParams.remove(IS_DEMO_APP);
            commonParams.remove(DEVICE_TOKEN);

            RestClient.getApiInterface(activity).deleteMerchantCards_FAC(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                @Override
                public void success(BaseModel baseModel) {
                    Utils.snackbarSuccess(activity, baseModel.getMessage());
                    dataList.remove(adapterPos);
                    notifyDataSetChanged();
                    if (activity instanceof PaymentMethodActivity) {
                        ((PaymentMethodActivity) activity).setPaymentListViews();
                    }
                }

                @Override
                public void failure(APIError error, BaseModel baseModel) {
                }
            });
        } else {
            CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
            commonParams.add("payment_method", dataList.get(adapterPos).getPaymentMethod())
                    .add("card_id", dataList.get(adapterPos).getCardId())
                    .build();

            RestClient.getApiInterface(activity).deleteMerchantCards(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                @Override
                public void success(BaseModel baseModel) {
                    Utils.snackbarSuccess(activity, baseModel.getMessage());
                    dataList.remove(adapterPos);
                    notifyDataSetChanged();
                    if (activity instanceof PaymentMethodActivity) {
                        ((PaymentMethodActivity) activity).setPaymentListViews();
                    }
                }

                @Override
                public void failure(APIError error, BaseModel baseModel) {
                }
            });
        }
    }


    public interface OnPaymentOptionSelectedListener {
        void onPaymentOptionSelected(Datum data);
    }


}