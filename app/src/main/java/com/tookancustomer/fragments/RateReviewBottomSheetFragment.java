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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CustomRatingBar;
import com.tookancustomer.utility.Utils;


/**
 * Created by Shweta on 3/12/18.
 */

public class RateReviewBottomSheetFragment extends BottomSheetDialogFragment {

    private Activity mActivity;
    private int jobId = 0;
    private CallbackReviewRatings callbackReviewRatings;

    public RateReviewBottomSheetFragment(Activity mActivity, int jobId, CallbackReviewRatings callbackReviewRatings) {
        this.mActivity = mActivity;
        this.jobId = jobId;
        this.callbackReviewRatings = callbackReviewRatings;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_rate_review_bottom_sheet, container, false);

        LinearLayout llCancelReasonDropDown = rootView.findViewById(R.id.llCancelReasonDropDown);
        llCancelReasonDropDown.setVisibility(View.GONE);

        TextView tvHeading = rootView.findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getString(mActivity,R.string.rate_review));

        final CustomRatingBar rbRate = rootView.findViewById(R.id.rbRate);
        rbRate.setScore(5f);
        final EditText etReviews = rootView.findViewById(R.id.etReviews);
        etReviews.setHint(StorefrontCommonData.getString(mActivity,R.string.tell_us_about_your_experience));
        Button btnSubmitRate = rootView.findViewById(R.id.btnSubmitRate);
        btnSubmitRate.setText(StorefrontCommonData.getString(mActivity,R.string.submit));

        ImageView ivSkipRate = rootView.findViewById(R.id.ivSkipRate);
        etReviews.setImeActionLabel(StorefrontCommonData.getString(mActivity,R.string.done),EditorInfo.IME_ACTION_DONE);
        etReviews.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etReviews.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etReviews.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Utils.hideSoftKeyboard(mActivity);
                return false;
            }
        });
        etReviews.setOnTouchListener(new View.OnTouchListener() {
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

        btnSubmitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRate(jobId, etReviews, rbRate.getScore());
            }
        });

        ivSkipRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipRate(jobId);
            }
        });

        return rootView;
    }

    public void submitRate(int jobId, final EditText etReviews, final float ratingScore) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity,R.string.no_internet_try_again)).build().show();
            return;
        }

        if ((int) ratingScore <= 0) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity,R.string.please_rate_before_proceeding)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", jobId);
        commonParams.add("customer_comment", etReviews.getText().toString().trim());
        commonParams.add("customer_rating", (int) ratingScore);

        RestClient.getApiInterface(mActivity).createCustomerOrderReview(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                dismiss();
                callbackReviewRatings.ratingApiSuccess(etReviews.getText().toString().trim(), ratingScore);
//                Utils.snackbarSuccess(mActivity, baseModel.getMessage());
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                dismiss();
            }
        });

    }

    public void skipRate(int jobId) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity,R.string.no_internet_try_again)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", jobId);

        RestClient.getApiInterface(mActivity).skipOrderReview(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                dismiss();
                callbackReviewRatings.skipApiSuccess();
//                Utils.snackbarSuccess(mActivity, baseModel.getMessage());
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });

    }

    public interface CallbackReviewRatings {
        void ratingApiSuccess(final String reviews, final float ratingScore);

        void skipApiSuccess();

    }


}