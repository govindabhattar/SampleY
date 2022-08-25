package com.tookancustomer.modules.recurring;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.adapters.ScheduleTimeSlotsAdapter;
import com.tookancustomer.adapters.TimeSlotAdapter;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.models.subscription.SubscriptionSlots;
import com.tookancustomer.models.subscription.TimeSlotsSubscription;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashutosh Ojha on 2/15/19.
 */
public class SubscriptionSlotsActivity extends BaseActivity implements TimeSlotAdapter.Callback, View.OnClickListener {

    private RecyclerView rvTimeSlotsRecyclerList;
    private ScheduleTimeSlotsAdapter scheduleTimeSlotsAdapter;
    private ArrayList<TimeSlotsSubscription> availTimeSlotsList;
    private ArrayList<SubscriptionSlots> timeSlotsList;



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_slot);

        if(getIntent()!=null){
            timeSlotsList= (ArrayList<SubscriptionSlots>) getIntent().getSerializableExtra("slotsList");
        }
        mActivity = this;

        initViews();

    }

    private void initViews() {
        ((TextView) findViewById(R.id.tvHeading)).setText(getStrings(R.string.select_start_date).replace(TerminologyStrings.START__TEXT, getStrings(R.string.start_text)));

        rvTimeSlotsRecyclerList = findViewById(R.id.rvTimeSlotsRecyclerList);

        Utils.setOnClickListener( this,findViewById(R.id.rlBack));

        availTimeSlotsList = new ArrayList<>();
        ArrayList<SubscriptionSlots> morningTimeSlotsList = new ArrayList<>(), noonTimeSlotsList = new ArrayList<>(), eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            TimeSlotsSubscription availableTimeSlotsModelResponse = new TimeSlotsSubscription(getStrings(R.string.morning), morningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            TimeSlotsSubscription availableTimeSlotsModelResponse = new TimeSlotsSubscription(getStrings(R.string.afternoon), noonTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            TimeSlotsSubscription availableTimeSlotsModelResponse = new TimeSlotsSubscription(getStrings(R.string.evening), eveningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        scheduleTimeSlotsAdapter=new ScheduleTimeSlotsAdapter(availTimeSlotsList, mActivity, new TimeSlotAdapter.CallbackSubscription() {
            @Override
            public void onTimeSlotSelected(String slot) {
                Log.e("datesdf result",slot+"");
                Intent intent=getIntent();
                intent.putExtra("selectedSlot",slot);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        rvTimeSlotsRecyclerList.setLayoutManager(new LinearLayoutManager(mActivity));

        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);
        scheduleTimeSlotsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onTimeSlotSelected(final Date date) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rlBack:
                finish();
                break;
        }
    }
}
