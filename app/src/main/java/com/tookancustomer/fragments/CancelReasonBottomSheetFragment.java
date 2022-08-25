package com.tookancustomer.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.cancellationPolicy.model.GetCancellationData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.CancellationReasonModel;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CustomRatingBar;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Created by Shweta on 3/12/18.
 */

public class CancelReasonBottomSheetFragment extends BottomSheetDialogFragment {
    private Activity mActivity;
    private int jobId = 0, productId = 0;
    private ArrayList<CancellationReasonModel> cancelReasonArrayList = new ArrayList<>();
    private CallbackCancelFragment callbackCancelFragment;
    private String cancelReason = "";
    private GetCancellationData cancellationData;

    public CancelReasonBottomSheetFragment(Activity mActivity, int jobId, ArrayList<CancellationReasonModel> cancelReasonArrayList, CallbackCancelFragment callbackCancelFragment) {
        this.mActivity = mActivity;
        this.jobId = jobId;
        this.cancelReasonArrayList = cancelReasonArrayList;
        this.callbackCancelFragment = callbackCancelFragment;
    }

    public CancelReasonBottomSheetFragment(Activity mActivity, int jobId,
                                           ArrayList<CancellationReasonModel> cancelReasonArrayList,
                                           GetCancellationData cancellationData,
                                           CallbackCancelFragment callbackCancelFragment) {
        this.mActivity = mActivity;
        this.jobId = jobId;
        this.cancelReasonArrayList = cancelReasonArrayList;
        this.callbackCancelFragment = callbackCancelFragment;
        this.cancellationData = cancellationData;
    }

    public CancelReasonBottomSheetFragment(Activity mActivity, int jobId, int productId,
                                           ArrayList<CancellationReasonModel> cancelReasonArrayList,
                                           CallbackCancelFragment callbackCancelFragment) {
        this.mActivity = mActivity;
        this.jobId = jobId;
        this.productId = productId;
        this.cancelReasonArrayList = cancelReasonArrayList;
        this.callbackCancelFragment = callbackCancelFragment;
    }

    public CancelReasonBottomSheetFragment(Activity mActivity, int jobId, int productId,
                                           ArrayList<CancellationReasonModel> cancelReasonArrayList,
                                           GetCancellationData cancellationData,
                                           CallbackCancelFragment callbackCancelFragment) {
        this.mActivity = mActivity;
        this.jobId = jobId;
        this.productId = productId;
        this.cancelReasonArrayList = cancelReasonArrayList;
        this.callbackCancelFragment = callbackCancelFragment;
        this.cancellationData = cancellationData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_rate_review_bottom_sheet, container, false);

        TextView tvHeading = rootView.findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getString(mActivity, R.string.reason_for_cancellation));

        ImageView ivSkipCancel = rootView.findViewById(R.id.ivSkipRate);
        ivSkipCancel.setVisibility(View.GONE);
        ivSkipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        CustomRatingBar rbRate = rootView.findViewById(R.id.rbRate);
        rbRate.setVisibility(View.GONE);

        LinearLayout llCancelReasonDropDown = rootView.findViewById(R.id.llCancelReasonDropDown);
        llCancelReasonDropDown.setVisibility(cancelReasonArrayList.size() > 0 ? View.VISIBLE : View.GONE);

        final Spinner spinnerCancelReason = rootView.findViewById(R.id.spinnerCancelReason);
        final EditText etCancelReasonDropDown = rootView.findViewById(R.id.etCancelReasonDropDown);
        ((MaterialEditText) etCancelReasonDropDown).setFloatingLabelText(StorefrontCommonData.getString(mActivity, R.string.select_text));
        ((MaterialEditText) etCancelReasonDropDown).setHint(StorefrontCommonData.getString(mActivity, R.string.select_text));
        etCancelReasonDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_icon_country_code_arrow, 0);
        etCancelReasonDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCancelReason.performClick();
            }
        });


        ArrayList<String> cancelReasonList = new ArrayList<>();
        cancelReasonList.add(StorefrontCommonData.getString(mActivity, R.string.select_text));
        for (int i = 0; i < cancelReasonArrayList.size(); i++) {
            cancelReasonList.add(cancelReasonArrayList.get(i).getReason());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.layout_custom_field_drop_down_list_item, cancelReasonList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCancelReason.setAdapter(adapter);
//        spinnerCancelReason.setPrompt(StorefrontCommonData.getString(mActivity, R.string.select_text));
        spinnerCancelReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    cancelReason = cancelReasonArrayList.get(position - 1).getReason();
                    etCancelReasonDropDown.setText(cancelReasonArrayList.get(position - 1).getReason());
                } else {
                    cancelReason = "";
                    etCancelReasonDropDown.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        final EditText etCancelReason = rootView.findViewById(R.id.etReviews);
        etCancelReason.setHint(StorefrontCommonData.getString(mActivity, R.string.enter_reason_for_cancellation));
        etCancelReason.setImeActionLabel(StorefrontCommonData.getString(mActivity, R.string.done), EditorInfo.IME_ACTION_DONE);
        etCancelReason.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCancelReason.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etCancelReason.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Utils.hideSoftKeyboard(mActivity);
                return false;
            }
        });
        etCancelReason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        LinearLayout layoutCancellationCharge = rootView.findViewById(R.id.layoutCancellationCharge);
        LinearLayout layoutLoyalityPoints = rootView.findViewById(R.id.layoutLoyalityPoints);
        TextView tvCancellationChargeLabel = rootView.findViewById(R.id.tvCancellationChargeLabel);
        TextView tvCancellationCharge = rootView.findViewById(R.id.tvCancellationCharge);
        TextView tvRefundAmountLabel = rootView.findViewById(R.id.tvRefundAmountLabel);
        TextView tvRefundAmount = rootView.findViewById(R.id.tvRefundAmount);
        TextView tvRefundLoyalityPointLabel = rootView.findViewById(R.id.tvRefundLoyalityPointLabel);
        TextView tvRefundLoyalityPoints = rootView.findViewById(R.id.tvRefundLoyalityPoints);

//        cancellationData = new GetCancellationData(100, 100, 100);

        if (cancellationData != null) {
            layoutCancellationCharge.setVisibility(View.VISIBLE);
            layoutLoyalityPoints.setVisibility(cancellationData.getRefundedLoyaltyPoint() > 0 ? View.VISIBLE : View.GONE);

            tvCancellationChargeLabel.setText(StorefrontCommonData.getString(mActivity, R.string.cancellation_charges)
                    + StorefrontCommonData.getString(mActivity, R.string.colon));
            tvCancellationCharge.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(cancellationData.getCancellationCharges())));
            tvRefundAmountLabel.setText(StorefrontCommonData.getString(mActivity, R.string.amount_to_be_refunded)
                    + StorefrontCommonData.getString(mActivity, R.string.colon));
            tvRefundAmount.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(cancellationData.getRefundAmount()))));
            tvRefundLoyalityPointLabel.setText(StorefrontCommonData.getString(mActivity, R.string.loyality_points_to_be_refunded)
                    + StorefrontCommonData.getString(mActivity, R.string.colon));
            tvRefundLoyalityPoints.setText(String.valueOf(cancellationData.getRefundedLoyaltyPoint()));
        }


        Button btnSubmitCancel = rootView.findViewById(R.id.btnSubmitRate);
        btnSubmitCancel.setText(StorefrontCommonData.getString(mActivity, R.string.submit));

        btnSubmitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelReasonArrayList.size() > 0 && cancelReason.isEmpty()) {
                    new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.cancel_reason_is_required)).build().show();
                    return;
                }
                cancelOrder(jobId, etCancelReason);
            }
        });

        return rootView;
    }

    private void cancelOrder(int jobId, final EditText etCancelReason) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", jobId);
        if (productId != 0) {
            commonParams.add("product_id", productId);
        }
        commonParams.add("reason", etCancelReason.getText().toString().trim());
        if (!cancelReason.isEmpty()) {
            commonParams.add("cancellation_reason", cancelReason);
        }

        RestClient.getApiInterface(mActivity).cancelOrder(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        dismiss();
                        callbackCancelFragment.cancelApiSuccess();
//                Utils.snackbarSuccess(mActivity, baseModel.getMessage());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        dismiss();
                    }
                });
    }

    public interface CallbackCancelFragment {
        void cancelApiSuccess();

    }

}