package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.R;
import com.tookancustomer.TaskDetailsActivity;
import com.tookancustomer.TaskDetailsNewActivity;
import com.tookancustomer.WebViewTrackingActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.cancellationPolicy.model.GetCancellationData;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.fragments.CancelReasonBottomSheetFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.CancellationReasonModel;
import com.tookancustomer.models.alltaskdata.OrderDetails;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tookancustomer.appdata.Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.PRODUCT_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.JOB_ID;
import static com.tookancustomer.appdata.Keys.Extras.KEY_ITEM_POSITION;
import static com.tookancustomer.appdata.Keys.Extras.UPDATE_QUESTIONNAIRE;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;
import static com.tookancustomer.appdata.TerminologyStrings.PRODUCT;
import static com.tookancustomer.appdata.TerminologyStrings.SURGE;
import static com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class OrderItemRecyclerAdapter extends RecyclerView.Adapter<OrderItemRecyclerAdapter.ViewHolder> {
    private Activity activity;
    private List<OrderDetails> subItems;
    private TaskData taskData;
    private ArrayList<CancellationReasonModel> cancelReasonArrayList = new ArrayList<>();

    public OrderItemRecyclerAdapter(Activity activity, TaskData taskData, List<OrderDetails> subItems) {
        this.activity = activity;
        this.subItems = subItems;
        this.taskData = taskData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order_item, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder mHolder, int position) {
        final int adapterPos = mHolder.getAdapterPosition();

        if (subItems.get(subItems.size() - 1) == subItems.get(position)) {
            mHolder.imageViewSep.setVisibility(View.GONE);
        } else {
            mHolder.imageViewSep.setVisibility(View.VISIBLE);
        }

        // mHolder.textViewItemName.setText(subItems.get(adapterPos).getProduct().getProductName());
        mHolder.textViewItemName.setText(subItems.get(adapterPos).getProduct().getProductName() + "(" + UIManager.getCurrency(Utils.getCurrencySymbolNew(taskData.getOrderCurrencySymbol())) + subItems.get(adapterPos).getProduct().getUnitPrice() + " * " + subItems.get(adapterPos).getProduct().getQuantity() + ")");

        if (subItems.get(adapterPos).getCustomizations() != null && subItems.get(adapterPos).getCustomizations().size() > 0) {
            mHolder.tvItemCustomisations.setVisibility(View.VISIBLE);
            String customisationString = "";
            for (int i = 0; i < subItems.get(adapterPos).getCustomizations().size(); i++) {
                if (!customisationString.isEmpty())
                    customisationString = customisationString + ", ";
                customisationString = customisationString + subItems.get(adapterPos).getCustomizations().get(i).getCustName();
            }
            mHolder.tvItemCustomisations.setText(customisationString);
        } else {
            mHolder.tvItemCustomisations.setVisibility(View.GONE);
        }

        int upSymbolUnicode = 0x2191;
        String text = Character.toString((char) upSymbolUnicode);

        if (subItems.get(adapterPos).getProduct().getSurgeAmount() > 0) {
            String subtotalData = StorefrontCommonData.getString(activity, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge()) + " "
                    + UIManager.getCurrency(Utils.getCurrencySymbolNew(taskData.getOrderCurrencySymbol()) + (subItems.get(adapterPos).getProduct().getSurgeAmount()));

            mHolder.tvsurgeAmount.setText("(" + subtotalData + ")" + text);
            mHolder.tvsurgeAmount.setVisibility(View.VISIBLE);
        } else {
            mHolder.tvsurgeAmount.setVisibility(View.GONE);
        }

        if (taskData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && taskData.getPd_or_appointment() == Constants.ServiceFlow.APPOINTMENT && subItems.get(adapterPos).getProduct().getServices() != null) {
            Utils.setVisibility(View.VISIBLE, mHolder.llServiceLayout);

            mHolder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(subItems.get(adapterPos).getProduct().getServices().getJobStatus()).getPassive(activity)));
            mHolder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.TaskStatus.getColorRes(subItems.get(adapterPos).getProduct().getServices().getJobStatus())));


            if (subItems.get(adapterPos).getProduct().getAgentId() > 0) {
                if (subItems.get(adapterPos).getProduct().getServices().getTrackingLink().isEmpty()) {
                    mHolder.tvServiceTrackLink.setText("Agent");
                } else {
                    mHolder.tvServiceTrackLink.setText(StorefrontCommonData.getString(activity, R.string.track));
                }
                mHolder.tvServiceTrackLink.setVisibility(View.VISIBLE);
                setTrakingData(mHolder, adapterPos);
            } else {
                mHolder.tvServiceTrackLink.setText("");
                mHolder.tvServiceTrackLink.setVisibility(View.GONE);
            }

            if (subItems.size() == 1)
                mHolder.tvServiceTrackLink.setVisibility(View.GONE);


            Utils.setVisibility(subItems.get(adapterPos).getProduct().getServices().getCancelAllowed() == 1 ? View.VISIBLE : View.GONE, mHolder.tvProductCancelBtn);

        } else {
            Utils.setVisibility(View.GONE, mHolder.llServiceLayout, mHolder.tvProductCancelBtn);
        }

        if (taskData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {

            Utils.setVisibility(View.VISIBLE, mHolder.tvItemStartTime);
            mHolder.tvItemStartTime.setText(StorefrontCommonData.getTerminology().getStartTime(true) + ": " + DateUtils.getInstance().parseDateAs(subItems.get(adapterPos).getProduct().getTaskStartTimeLocal(),
                    UIManager.getDateTimeFormat()));

            if (taskData.getPd_or_appointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                    && Constants.ProductsUnitType.getUnitType(subItems.get(adapterPos).getProduct().getUnitType()) == Constants.ProductsUnitType.FIXED
                    && subItems.get(adapterPos).getProduct().getEnableTookanAgent() == 0) {
                Utils.setVisibility(View.GONE, mHolder.tvItemEndTime);
            } else {
                Utils.setVisibility(View.VISIBLE, mHolder.tvItemEndTime);
                mHolder.tvItemEndTime.setText(StorefrontCommonData.getTerminology().getEndTime(true) + ": " + DateUtils.getInstance().parseDateAs(subItems.get(adapterPos).getProduct()
                        .getTaskEndTimeLocal(), UIManager.getDateTimeFormat()));
            }

        } else {
            Utils.setVisibility(View.GONE, mHolder.tvItemStartTime, mHolder.tvItemEndTime);
        }

        if (!Dependencies.isLaundryApp()) {
            mHolder.textViewItemQuantity.setText("x " + subItems.get(adapterPos).getProduct().getQuantity() +
                    (taskData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && subItems.get(adapterPos).getProduct().getUnitType() != Constants.ProductsUnitType.FIXED.value ? " x " + subItems.get(adapterPos).getProduct().getUnitCount().intValue() : ""));
        } else {
            mHolder.textViewItemQuantity.setText("x " + subItems.get(adapterPos).getProduct().getQuantity() + " " + Constants.UnitType.getUnitTextFromValue(activity, subItems.get(adapterPos).getProduct().getUnitType()));
        }

        if (taskData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && subItems.get(adapterPos).getProduct().getUnitType() != Constants.ProductsUnitType.FIXED.value) {
            mHolder.tvItemChargeText.setVisibility(View.VISIBLE);
            mHolder.tvItemChargeText.setText(StorefrontCommonData.getString(activity, R.string.charge) + " @ " +
                    Constants.ProductsUnitType.getUnitTypeText(activity, subItems.get(adapterPos).getProduct().getUnit().intValue(), subItems.get(adapterPos).getProduct().getUnitType(), true)
                    + "  x " + subItems.get(adapterPos).getProduct().getUnitCount().intValue());
        } else {
            mHolder.tvItemChargeText.setVisibility(View.GONE);
        }

        mHolder.textViewItemPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(taskData.getOrderCurrencySymbol()) + "" +
                Utils.getDoubleTwoDigits(subItems.get(adapterPos).getProduct().getUnitPrice() * subItems.get(adapterPos).getProduct().getQuantity()))));

        mHolder.textViewItemTotalPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(taskData.getOrderCurrencySymbol()) + "" + Utils.getDoubleTwoDigits(getItemPriceWithCustomisation(subItems.get(adapterPos))))));


        mHolder.viewQuestionnaireTV.setText(CustomViewsUtil.createSpan(activity, StorefrontCommonData.getString(activity,
                R.string.productInfo).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()),
                " (", StorefrontCommonData.getString(activity, R.string.view), ")", ContextCompat.getColor(activity, R.color.colorPrimary)));

        mHolder.viewQuestionnaireTV.setTag(position);

        if (subItems.get(adapterPos).getProduct().getIsProductTemplateEnabled() == 1 && subItems.get(adapterPos).getProduct().getProductTemplate() != null) {
            mHolder.questionnaireAmountTV.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(taskData.getOrderCurrencySymbol()) + Utils.getDoubleTwoDigits(subItems.get(adapterPos).getProduct().getTemplateCost()))));
            mHolder.questionnaireRL.setVisibility(View.VISIBLE);
        } else {
            mHolder.questionnaireRL.setVisibility(View.GONE);
        }

        mHolder.viewQuestionnaireTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Intent templateIntent = new Intent(activity, QuestionnaireTemplateActivity.class);
                if (subItems.get(adapterPos).getProduct().getProductTemplate() != null) {
                    templateIntent.putExtra(UPDATE_QUESTIONNAIRE, false);
                    templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, true);
                    templateIntent.putExtra(KEY_ITEM_POSITION, pos);
                    templateIntent.putExtra("product_title", subItems.get(adapterPos).getProduct().getProductName());
                    templateIntent.putExtra(EXTRA_TEMPLATE_LIST, subItems.get(adapterPos).getProduct().getProductTemplate());
                    templateIntent.putExtra("questionnaireCurrencySymbol", taskData.getOrderCurrencySymbol());

                    activity.startActivityForResult(templateIntent, OPEN_QUESTIONNAIRE_ACTIVITY);
                }
            }
        });

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && getItemPriceWithCustomisation(subItems.get(adapterPos)) <= 0) {
            mHolder.textViewItemTotalPrice.setVisibility(View.GONE);
            mHolder.textViewItemPrice.setVisibility(View.GONE);
            mHolder.tvItemChargeText.setVisibility(View.GONE);
            mHolder.textViewItemQuantity.setVisibility(View.GONE);
        } else {
            mHolder.textViewItemTotalPrice.setVisibility(View.VISIBLE);
            mHolder.textViewItemPrice.setVisibility(View.VISIBLE);
            mHolder.tvItemChargeText.setVisibility(View.VISIBLE);
            mHolder.textViewItemQuantity.setVisibility(View.VISIBLE);
        }

        //if task is of type pickup than remove product price in laundry app
        if (Dependencies.isLaundryApp()) {
            if (taskData.getTaskType() == 1)
                mHolder.textViewItemTotalPrice.setVisibility(View.GONE);
            else
                mHolder.textViewItemTotalPrice.setVisibility(View.VISIBLE);
            mHolder.textViewItemQuantity.setVisibility(View.VISIBLE);
            mHolder.textViewItemPrice.setVisibility(View.VISIBLE);

        }

        /*Set taxes arraylist at product level*/
        ArrayList<BillBreakdownData> productPricesList = new ArrayList<>();
        if (subItems.get(adapterPos).getProduct().getTaxesArrayList().size() > 0) {

            for (int i = 0; i < subItems.get(adapterPos).getProduct().getTaxesArrayList().size(); i++)
                subItems.get(adapterPos).getProduct().getTaxesArrayList().get(i).setCurrencySymbol(taskData.getOrderCurrencySymbol());
        }
        for (int i = 0; i < subItems.get(adapterPos).getProduct().getTaxesArrayList().size(); i++) {
            productPricesList.add(new BillBreakdownData(subItems.get(adapterPos).getProduct().getTaxesArrayList().get(i).getTaxName(),
                    new BigDecimal(subItems.get(adapterPos).getProduct().getTaxesArrayList().get(i).getTaxAmount()),
                    false, subItems.get(adapterPos).getProduct().getTaxesArrayList().get(i).getTaxPercentage(),
                    subItems.get(adapterPos).getProduct().getTaxesArrayList().get(i).getTaxType()));
        }
      //  productPricesList.addAll(subItems.get(adapterPos).getProduct().getTaxesArrayList());

        BillBreakdownAdapter productPricesAdapter = new BillBreakdownAdapter(activity, productPricesList, true);
        mHolder.rvProductTaxesList.setAdapter(productPricesAdapter);

        mHolder.tvProductCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UIManager.isCancellationPolicyEnabled())
                    getCancellationCharges(subItems.get(adapterPos).getProduct().getServices().getProductId(),
                            adapterPos, mHolder);
                else {
                    if (UIManager.getCancellationReasonType() == 0) {
                        openCancelPopup(adapterPos, mHolder);
                    } else {
                        getCancellationReason(adapterPos, mHolder, null);
                    }
                }
            }
        });
    }

    private void setTrakingData(ViewHolder mHolder, final int adapterPos) {


        if (subItems.get(adapterPos).getProduct().getServices().isSelected()) {
            mHolder.tvServiceTrackLink.setBackground(activity.getResources().getDrawable(R.drawable.background_round_corners_accent));
            mHolder.tvServiceTrackLink.setTextColor(activity.getResources().getColor(R.color.white));
        } else {
            mHolder.tvServiceTrackLink.setBackground(activity.getResources().getDrawable(R.drawable.bg_round_corners_accent_stroke));
            mHolder.tvServiceTrackLink.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        }


        mHolder.tvServiceTrackLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity instanceof TaskDetailsNewActivity) {
                    if (!subItems.get(adapterPos).getProduct().getServices().isSelected())
                        ((TaskDetailsNewActivity) activity).getAgentData(adapterPos);
                } else if (activity instanceof TaskDetailsActivity) {
                    Intent intentPayment = new Intent(activity, WebViewTrackingActivity.class);
                    intentPayment.putExtra(URL_WEBVIEW, subItems.get(adapterPos).getProduct().getServices().getDeliveryTrackingLink());
                    intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(activity, R.string.track));
                    activity.startActivity(intentPayment);
                    AnimationUtils.forwardTransition(activity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subItems != null ? subItems.size() : 0;
    }

    private double getItemPriceWithCustomisation(OrderDetails orderDetails) {
        double itemPrice = 0.0;


        if (orderDetails != null) {
            if (orderDetails.getCustomizations() != null && orderDetails.getCustomizations().size() > 0) {
                for (int i = 0; i < orderDetails.getCustomizations().size(); i++) {
                    itemPrice = itemPrice + orderDetails.getCustomizations().get(i).getTotalPrice();
                }
            }
            itemPrice = itemPrice + orderDetails.getProduct().getTotalPrice();
        }
        return itemPrice;
    }

    private void openCancelPopup(final int adapterPos, final ViewHolder mHolder) {
        CancelReasonBottomSheetFragment cancelReasonBottomSheetFragment = new CancelReasonBottomSheetFragment(activity, taskData.getJobId(),
                subItems.get(adapterPos).getProduct().getServices().getProductId(), cancelReasonArrayList,
                new CancelReasonBottomSheetFragment.CallbackCancelFragment() {
                    @Override
                    public void cancelApiSuccess() {
                        subItems.get(adapterPos).getProduct().getServices().setCancelAllowed(0);
                        subItems.get(adapterPos).getProduct().getServices().setJobStatus(Constants.TaskStatus.CANCELLED.value);

                        mHolder.tvProductCancelBtn.setVisibility(subItems.get(adapterPos).getProduct().getServices().getCancelAllowed() == 1 ? View.VISIBLE : View.GONE);
                        mHolder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(subItems.get(adapterPos).getProduct().getServices().getJobStatus()).getPassive(activity)));
                        mHolder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.TaskStatus.getColorRes(subItems.get(adapterPos).getProduct().getServices().getJobStatus())));
                    }

                });
        cancelReasonBottomSheetFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), cancelReasonBottomSheetFragment.getTag());

    }

    private void openCancelPopup(final int adapterPos, final ViewHolder mHolder, final GetCancellationData data) {
        CancelReasonBottomSheetFragment cancelReasonBottomSheetFragment = new CancelReasonBottomSheetFragment(activity, taskData.getJobId(),
                subItems.get(adapterPos).getProduct().getServices().getProductId(), cancelReasonArrayList,
                data,
                new CancelReasonBottomSheetFragment.CallbackCancelFragment() {
                    @Override
                    public void cancelApiSuccess() {
                        subItems.get(adapterPos).getProduct().getServices().setCancelAllowed(0);
                        subItems.get(adapterPos).getProduct().getServices().setJobStatus(Constants.TaskStatus.CANCELLED.value);

                        mHolder.tvProductCancelBtn.setVisibility(subItems.get(adapterPos).getProduct().getServices().getCancelAllowed() == 1 ? View.VISIBLE : View.GONE);
                        mHolder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(subItems.get(adapterPos).getProduct().getServices().getJobStatus()).getPassive(activity)));
                        mHolder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.TaskStatus.getColorRes(subItems.get(adapterPos).getProduct().getServices().getJobStatus())));
                    }

                });
        cancelReasonBottomSheetFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), cancelReasonBottomSheetFragment.getTag());

    }

    private void getCancellationReason(final int adapterPos, final ViewHolder mHolder, final GetCancellationData data) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(activity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("user_id", StorefrontCommonData.getFormSettings().getUserId())
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        Dependencies.addCommonParameters(commonParams, activity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(activity).getCancellationReason(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        CancellationReasonModel[] cancellationReasonArrayList = baseModel.toResponseModel(CancellationReasonModel[].class);
                        cancelReasonArrayList = new ArrayList<CancellationReasonModel>(Arrays.asList(cancellationReasonArrayList));
                        if (data == null)
                            openCancelPopup(adapterPos, mHolder);
                        else
                            openCancelPopup(adapterPos, mHolder, data);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }

    private void getCancellationCharges(final int productId, final int adapterPos, final ViewHolder mHolder) {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(activity))
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(JOB_ID, taskData.getJobId())
                .add(PRODUCT_ID, productId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            builder.add("language", "en");
        }

        RestClient.getApiInterface(activity).getCancellationCharges(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        GetCancellationData data = baseModel.toResponseModel(GetCancellationData.class);
                        if (UIManager.getCancellationReasonType() == 0) {
                            openCancelPopup(adapterPos, mHolder, data);
                        } else {
                            getCancellationReason(adapterPos, mHolder, data);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemName, textViewItemPrice, textViewItemQuantity, textViewItemTotalPrice, tvsurgeAmount,
                tvItemCustomisations, tvItemStartTime, tvItemEndTime, tvOrderStatus, tvServiceTrackLink, tvItemChargeText, viewQuestionnaireTV, questionnaireAmountTV;
        private View imageViewSep;
        private Button tvProductCancelBtn;
        private RelativeLayout llServiceLayout, questionnaireRL;
        private CheckBox rbtnReturn;
        private RecyclerView rvProductTaxesList;

        public ViewHolder(View itemView) {
            super(itemView);

            llServiceLayout = itemView.findViewById(R.id.llServiceLayout);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            tvsurgeAmount = itemView.findViewById(R.id.tvsurgeAmount);

            tvItemCustomisations = itemView.findViewById(R.id.tvItemCustomisations);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            tvItemChargeText = itemView.findViewById(R.id.tvItemChargeText);
            tvItemStartTime = itemView.findViewById(R.id.tvItemStartTime);
            tvItemEndTime = itemView.findViewById(R.id.tvItemEndTime);
            textViewItemTotalPrice = itemView.findViewById(R.id.textViewItemTotalPrice);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvServiceTrackLink = itemView.findViewById(R.id.tvServiceTrackLink);
            tvProductCancelBtn = itemView.findViewById(R.id.tvProductCancelBtn);
            tvProductCancelBtn.setText(StorefrontCommonData.getString(activity, R.string.cancel_text));
            imageViewSep = itemView.findViewById(R.id.imageViewSep);
            rbtnReturn = itemView.findViewById(R.id.rbtnReturn);
            rvProductTaxesList = itemView.findViewById(R.id.rvProductTaxesList);
            rvProductTaxesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

            questionnaireAmountTV = itemView.findViewById(R.id.questionnaireAmountTV);
            viewQuestionnaireTV = itemView.findViewById(R.id.viewQuestionnaireTV);
            questionnaireRL = itemView.findViewById(R.id.questionnaireRL);
        }
    }


}
