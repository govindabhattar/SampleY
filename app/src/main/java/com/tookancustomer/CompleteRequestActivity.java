package com.tookancustomer;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.adapters.RequestAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.interfaces.RequestInterface;
import com.tookancustomer.models.requests.Pending;
import com.tookancustomer.models.requests.RequestsData;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Created by neerajwadhwani on 25/07/18.
 */

public class CompleteRequestActivity extends BaseActivity implements RequestInterface{

    public static final String REQUEST_DATA = "requestData";
    private static final String TAG = "tag";
    private RecyclerView rvRequests;
    private RequestAdapter reqAdapter;
    private ArrayList<Pending> requestsList;
    private String tags;
    RequestsData requestsData;
    private TextView tvHeading;
    private RelativeLayout rlBack;
    private TextView tvNoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_request);
        init();
        gettingIntent();
        setupAdapter();

    }

    private void init() {
        requestsList=new ArrayList<>();
        rvRequests=findViewById(R.id.rvRequests);
        tvNoRequest=findViewById(R.id.tvNoRequest);
        tvHeading=findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.complete_request));
        tvHeading.setText(Utils.getCallTaskAs(StorefrontCommonData.getUserData(), true, true) + " " + StorefrontCommonData.getString(mActivity, R.string.requests));
        rlBack=findViewById(R.id.rlBack);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupAdapter() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        reqAdapter=new RequestAdapter(requestsList,this,StorefrontCommonData.getUserData(),requestsData,this);
        rvRequests.setLayoutManager(manager);
        rvRequests.setAdapter(reqAdapter);

    }

    private void gettingIntent() {
        requestsData = (RequestsData) getIntent().getSerializableExtra(REQUEST_DATA);
        tags=getIntent().getStringExtra(TAG);
        separateListUsingtags();

    }

    private void separateListUsingtags() {
        if(requestsList!=null&&tags!=null)
        switch (tags)
        {
            case CANCELLED:
                requestsList.clear();
                if(requestsData.getCancelled()!=null)
                    requestsList.addAll(requestsData.getCancelled());
                tvHeading.setText(getStrings(R.string.cancelled_request));
                break;
            case COMPLETED:
                requestsList.clear();
                if(requestsData.getCompleted()!=null)
                    requestsList.addAll(requestsData.getCompleted());
                tvHeading.setText(getStrings(R.string.complete_request));
                break;
            case DISPATCHED:
                requestsList.clear();
                if(requestsData.getDispatched()!=null)
                requestsList.addAll(requestsData.getDispatched());
                tvHeading.setText(getStrings(R.string.dispatched_request));
                break;
            case PENDING:
                requestsList.clear();
                if(requestsData.getPending()!=null)
                    requestsList.addAll(requestsData.getPending());
                tvHeading.setText(getStrings(R.string.pending_request));
                break;
            default:
        }
    }

    @Override
    public void showNoRequestText() {
        rvRequests.setVisibility(View.GONE);
        tvNoRequest.setVisibility(View.VISIBLE);
    }
    @Override
    public void removeNoRequestText() {
        rvRequests.setVisibility(View.VISIBLE);
        tvNoRequest.setVisibility(View.GONE);
    }
}
