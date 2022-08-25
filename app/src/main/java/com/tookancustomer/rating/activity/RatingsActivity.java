package com.tookancustomer.rating.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.NonSwipeableViewPager;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.rating.adapter.RatingsPagerAdapter;
import com.tookancustomer.rating.fragment.RatingFragment;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class RatingsActivity extends BaseActivity implements View.OnClickListener {
    private TaskData taskDetails;

    private RelativeLayout rlBack;
    private TextView tvHeading;
    private Button btnSkip;
    private ProgressBar progressBar;

    private NonSwipeableViewPager pagerRating;
    private RatingsPagerAdapter pagerAdapter;

    public static final String EXTRA_TASK_DETAILS = "EXTRA_TASK_DETAILS";
    public static final int REQUEST_CODE_FOR_RATING = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        setParameters(getIntent());
        init();
        setData();
        setPagerAdapter();
        setPagerListener();
    }

    private void setData() {
        btnSkip.setVisibility(View.VISIBLE);
        if (isTookanRatingEnabled()) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);
        }

        tvHeading.setText(getStrings(R.string.reviews_ratings));
        btnSkip.setText(getStrings(R.string.skip));

    }

    private void startAnimation(int progressEnd) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), progressEnd);
        progressAnimator.setDuration(500);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    public void moveToNext() {
        if (isTookanRatingEnabled()) {
            pagerRating.setCurrentItem(1);
            btnSkip.setVisibility(View.GONE);
            findViewById(R.id.rlInvisible).setVisibility(View.VISIBLE);
        } else {
            onBackPressed();
        }
    }

    private void setPagerListener() {
        pagerRating.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int progressEnd = 100;
                if (position == 0) {
                    progressEnd = 50;
                }
                startAnimation(progressEnd);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setParameters(Intent intent) {
        taskDetails = (TaskData) intent.getSerializableExtra(EXTRA_TASK_DETAILS);
    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        btnSkip = findViewById(R.id.btnSkip);

        progressBar = findViewById(R.id.progressBar);
        pagerRating = findViewById(R.id.pagerRating);


        rlBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }


    private void setPagerAdapter() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();

        Bundle orderRatingBundle = new Bundle();
        orderRatingBundle.putBoolean(RatingFragment.EXTRA_ORDER_RATING_FLOW, true);
        orderRatingBundle.putSerializable(EXTRA_TASK_DETAILS, taskDetails);

        RatingFragment orderRating = new RatingFragment();
        orderRating.setArguments(orderRatingBundle);

        fragmentList.add(orderRating);
        if (isTookanRatingEnabled()) {
            Bundle driverRatingBundle = new Bundle();
            driverRatingBundle.putBoolean(RatingFragment.EXTRA_ORDER_RATING_FLOW, false);
            driverRatingBundle.putSerializable(EXTRA_TASK_DETAILS, taskDetails);

            RatingFragment driverRating = new RatingFragment();
            driverRating.setArguments(driverRatingBundle);
            fragmentList.add(driverRating);
        }

        pagerAdapter = new RatingsPagerAdapter(getSupportFragmentManager(), fragmentList);
        pagerRating.setAdapter(pagerAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.btnSkip:
                skipRate(taskDetails.getJobId());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TASK_DETAILS, taskDetails);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void skipRate(int jobId) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", jobId);

        RestClient.getApiInterface(mActivity).skipOrderReview(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                taskDetails.setIsJobRated(2);
                onBackPressed();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });

    }


    private boolean isTookanRatingEnabled() {
        return UIManager.isTookanActive() && taskDetails.getTookanJobHash() != null;
    }
}
