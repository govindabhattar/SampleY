package com.tookancustomer.modules.recurring.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tookancustomer.R;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.adapters.AllRecurringTasksAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.allrecurringdata.AllRecurringData;
import com.tookancustomer.models.allrecurringdata.Result;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class AllRecurringTaskFragment extends Fragment {

    private AllRecurringTasksAdapter allRecurringTasksAdapter;
    private ArrayList<Result> resultArrayList;
    private RecyclerView rvAllTasks;
    private ShimmerFrameLayout shimmerLayout;
    private LinearLayout llNoOrders;
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private int limit = 0; //Here limit refers to offset for  fetching nextBookings.

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_all_recurring_task, container, false);

        initView(rootView);

        getAllRecurringRules();


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }

    private void getAllRecurringRules() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(activity).getRecurringTask(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(activity, false, false) {
                    @Override
                    public void success(final BaseModel baseModel) {
                        try {
                            ((TasksActivity)activity).stopShimmerAnimation(shimmerLayout);


                            AllRecurringData allRecurringData = baseModel.toResponseModel(AllRecurringData.class);
                            resultArrayList = (ArrayList<Result>) allRecurringData.getResult();
                            setupAdapter();


                        } catch (Exception e) {

                               Utils.printStackTrace(e);
                        }

                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        ((TasksActivity)activity).stopShimmerAnimation(shimmerLayout);
                        setupAdapter();


                    }
                });
    }

    private void setupAdapter() {

        if (resultArrayList != null && resultArrayList.size() > 0) {

            llNoOrders.setVisibility(View.GONE);
            allRecurringTasksAdapter.setData(resultArrayList);
        } else {
            llNoOrders.setVisibility(View.VISIBLE);
        }

    }

    private void initView(ViewGroup view) {

        TextView tvHeading = view.findViewById(R.id.tvHeading);
        tvHeading.setText(   ((TasksActivity)activity).getStrings(R.string.subscription));



        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        llNoOrders = view.findViewById(R.id.llNoOrders);
        TextView tvNoData = view.findViewById(R.id.tvNoData);
        tvNoData.setText(   ((TasksActivity)activity).getStrings(R.string.msg_no_subscription_found));

        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        ((TasksActivity)activity).startShimmerAnimation(shimmerLayout);

        allRecurringTasksAdapter = new AllRecurringTasksAdapter(activity, resultArrayList);
        rvAllTasks = view.findViewById(R.id.rvAllTasks);
        rvAllTasks.setLayoutManager(new LinearLayoutManager(activity));
        rvAllTasks.setAdapter(allRecurringTasksAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllRecurringRules();

            }
        });


    }


}
