package com.tookancustomer.rating.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.fragments.BaseFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.rating.activity.RatingsActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CustomRatingBar;
import com.tookancustomer.utility.Utils;

public class RatingFragment extends BaseFragment implements View.OnClickListener, TerminologyStrings {
    public static final String EXTRA_ORDER_RATING_FLOW = "EXTRA_ORDER_RATING_FLOW";
    public static final String EXTRA_JOB_ID = "EXTRA_JOB_ID";

    private boolean isOrderRatingFlow;
    private TaskData taskDetails;
    private Activity mActivity;

    private TextView tvHeading;
    private CustomRatingBar rbRate;
    private EditText etReviews;
    private Button btnSubmitRate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rating, container, false);
        init(rootView);
        setParameters(getArguments());
        setData();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    private void setData() {
        if (isOrderRatingFlow) {
            tvHeading.setText(StorefrontCommonData.getString(mActivity, R.string.rate_your_order).replace(ORDER, Utils.getCallTaskAs(true, false)));
        } else {
            tvHeading.setText(StorefrontCommonData.getString(mActivity, R.string.rate_your_agent));
        }

        btnSubmitRate.setText(StorefrontCommonData.getString(mActivity, R.string.submit));
    }

    private void setParameters(Bundle arguments) {
        isOrderRatingFlow = arguments.getBoolean(EXTRA_ORDER_RATING_FLOW, false);
        taskDetails = (TaskData) arguments.getSerializable(RatingsActivity.EXTRA_TASK_DETAILS);
    }

    private void init(ViewGroup rootView) {
        tvHeading = rootView.findViewById(R.id.tvHeading);
        rbRate = rootView.findViewById(R.id.rbRate);
        etReviews = rootView.findViewById(R.id.etReviews);
        btnSubmitRate = rootView.findViewById(R.id.btnSubmitRate);

        btnSubmitRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmitRate:
                if (isOrderRatingFlow)
                    submitOrderRating(etReviews.getText().toString().trim(), (int) rbRate.getScore());
                else
                    submitAgentRating(etReviews.getText().toString().trim(), (int) rbRate.getScore());
                break;
        }
    }

    private void submitOrderRating(final String feedback, final int rating) {
//        taskDetails.setIsJobRated(1);
//        taskDetails.setCustomerRating(rating);
//        taskDetails.setCustomerComment(feedback);
//        ((RatingsActivity) mActivity).moveToNext();
//        return;
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(this).message(getString(R.string.no_internet_try_again)).build().show();
            return;
        }

        if (rating <= 0) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.please_rate_before_proceeding)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", taskDetails.getJobId());
        commonParams.add("customer_comment", feedback);
        commonParams.add("customer_rating", rating);

        RestClient.getApiInterface(mActivity).createCustomerOrderReview(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        taskDetails.setIsJobRated(1);
                        taskDetails.setCustomerRating(rating);
                        taskDetails.setCustomerComment(feedback);
                        ((RatingsActivity) mActivity).moveToNext();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });
    }

    private void submitAgentRating(String feedback, int rating) {

//        ((RatingsActivity) mActivity).onBackPressed();
//        return;

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", taskDetails.getJobId());
        commonParams.add("job_hash", taskDetails.getTookanJobHash());
        commonParams.add("customer_comment", feedback);
        commonParams.add("rating", rating);

        RestClient.getApiInterface(mActivity).rateAgent(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        ((RatingsActivity) mActivity).onBackPressed();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });
    }

}
