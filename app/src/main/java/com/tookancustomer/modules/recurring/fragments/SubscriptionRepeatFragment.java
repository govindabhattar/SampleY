package com.tookancustomer.modules.recurring.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.SubscriptionDaysAdapter;
import com.tookancustomer.models.subscription.ActiveDay;
import com.tookancustomer.models.subscription.Days;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import com.tookancustomer.modules.recurring.SubscriptionActivity;

/**
 * Created by Ashutosh Ojha on 2/14/19.
 */
@SuppressLint("ValidFragment")
public class SubscriptionRepeatFragment extends Fragment implements View.OnClickListener, SubscriptionDaysAdapter.Callback {

    private Activity activity;
    private Subscription subscriptionCallback;
    private TextView tvSave;
    private TextView tvEveryDay, tvWeekDays, tvWeekEnds, tvDisplayName;
    private ArrayList<Days> subscriptionDataArrayList;
    private RecyclerView rvSubscriptionDays;
    private SubscriptionDaysAdapter subscriptionDaysAdapter;
    private List<ActiveDay> activeDays;

    @SuppressLint("ValidFragment")
    public SubscriptionRepeatFragment(final List<ActiveDay> activeDays, final ArrayList<Days> subscriptionDataArrayList) {
        this.activeDays = activeDays;


        this.subscriptionDataArrayList = subscriptionDataArrayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_subscription_repeat, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(final ViewGroup rootView) {
        tvSave = rootView.findViewById(R.id.tvSave);
        tvEveryDay = rootView.findViewById(R.id.tvEveryDay);
        tvWeekDays = rootView.findViewById(R.id.tvWeekDays);
        tvWeekEnds = rootView.findViewById(R.id.tvWeekEnds);
        tvDisplayName = rootView.findViewById(R.id.tvDisplayName);

        rvSubscriptionDays = rootView.findViewById(R.id.rvSubscriptionDays);
        rvSubscriptionDays.setLayoutManager(new LinearLayoutManager(activity));


        SubscriptionActivity subscriptionActivity = (SubscriptionActivity) activity;
        tvEveryDay.setText(subscriptionActivity.getStrings(R.string.everyday_text));
        tvDisplayName.setText(subscriptionActivity.getStrings(R.string.repeat_text));
        tvWeekDays.setText(subscriptionActivity.getStrings(R.string.weekdays_text));
        tvWeekEnds.setText(subscriptionActivity.getStrings(R.string.weekend_text));
        tvSave.setText(subscriptionActivity.getStrings(R.string.apply));

        Utils.setOnClickListener(this, tvSave, rootView.findViewById(R.id.rlClose), tvEveryDay, tvWeekDays, tvWeekEnds);


        String[] daysArrayFull = new String[7];
        daysArrayFull[0] = ((SubscriptionActivity) activity).getStrings(R.string.sunday_text);
        daysArrayFull[1] = ((SubscriptionActivity) activity).getStrings(R.string.monday_text);
        daysArrayFull[2] = ((SubscriptionActivity) activity).getStrings(R.string.tuesday_text);
        daysArrayFull[3] = ((SubscriptionActivity) activity).getStrings(R.string.wednesday_text);
        daysArrayFull[4] = ((SubscriptionActivity) activity).getStrings(R.string.thursday_text);
        daysArrayFull[5] = ((SubscriptionActivity) activity).getStrings(R.string.friday_text);
        daysArrayFull[6] = ((SubscriptionActivity) activity).getStrings(R.string.saturday_text);
//        String[] daysArrayFull = getResources().getStringArray(R.array.days_array_full);
        String[] daysArray = getResources().getStringArray(R.array.days_array);

        if (subscriptionDataArrayList == null) {
            subscriptionDataArrayList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                subscriptionDataArrayList.add(new Days(false, false,
                        daysArrayFull[i], daysArray[i], i));

            }
        }


        subscriptionDaysAdapter = new SubscriptionDaysAdapter(activity, subscriptionDataArrayList, this);

        for (int i = 0; i < activeDays.size(); i++) {

            subscriptionDataArrayList.get(activeDays.get(i).getDayId()).setActive(true);

        }

        setButtonsVisibility();

        rvSubscriptionDays.setAdapter(subscriptionDaysAdapter);

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    private void setButtonsVisibility() {

        //Week days gone case
        if ((activeDays.size() == 2) && (activeDays.get(0).getDayId() == 0 || activeDays.get(0).getDayId() == 6)
                && activeDays.get(1).getDayId() == 0 || activeDays.get(1).getDayId() == 6) {
            tvEveryDay.setVisibility(View.GONE);
            tvWeekDays.setVisibility(View.GONE);
        }

        //Weekend gone case
        boolean isWeekendActive = false;
        if (activeDays.size() <= 5) {
            for (int i = 0; i < activeDays.size(); i++) {
                if (activeDays.get(i).getDayId() == 6) {
                    isWeekendActive = true;
                    break;
                } else if (activeDays.get(i).getDayId() == 0) {
                    isWeekendActive = true;
                    break;
                }


            }

            if (!isWeekendActive) {
                tvWeekEnds.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        subscriptionCallback = (Subscription) activity;
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.tvSave:

                if (isValid()) {
                    subscriptionCallback.repetitionDays(subscriptionDataArrayList);
                } else {
                    Utils.snackBar(activity, ((SubscriptionActivity) activity).getStrings(R.string.error_one_day_selection));

                }
                break;

            case R.id.rlClose:
                subscriptionCallback.close();
                break;

            case R.id.tvEveryDay:

                clearAllDaysSelection();

                for (int i = 0; i < subscriptionDataArrayList.size(); i++) {

                    if (subscriptionDataArrayList.get(i).isActive())
                        subscriptionDataArrayList.get(i).setSelected(true);
                }
                subscriptionDaysAdapter.setData(subscriptionDataArrayList);

                break;

            case R.id.tvWeekDays:

                clearAllDaysSelection();
                for (int i = 0; i < subscriptionDataArrayList.size(); i++) {

                    Days days = subscriptionDataArrayList.get(i);

                    if (days.isActive() && !(days.getDayId() == 0
                            || days.getDayId() == 6))
                        days.setSelected(true);
                }
                subscriptionDaysAdapter.setData(subscriptionDataArrayList);
                break;

            case R.id.tvWeekEnds:

                clearAllDaysSelection();

                //6==Saturday,0 Sunday
                if (subscriptionDataArrayList.get(0).isActive())
                    subscriptionDataArrayList.get(0).setSelected(true);

                if (subscriptionDataArrayList.get(6).isActive())
                    subscriptionDataArrayList.get(6).setSelected(true);

                subscriptionDaysAdapter.setData(subscriptionDataArrayList);

                break;
        }
    }

    private boolean isValid() {
        boolean atLeastSelected = false;
        for (int i = 0; i < subscriptionDataArrayList.size(); i++) {
            if (subscriptionDataArrayList.get(i).isActive() && subscriptionDataArrayList.get(i).isSelected()) {
                atLeastSelected = true;
                break;
            }
        }
        return atLeastSelected;
    }

    void clearAllDaysSelection() {
        for (int i = 0; i < subscriptionDataArrayList.size(); i++) {
            subscriptionDataArrayList.get(i).setSelected(false);
        }
    }


    @Override
    public void onDaySelected(final ArrayList<Days> subscriptionData) {
        tvEveryDay.setSelected(false);
        tvWeekDays.setSelected(false);
        tvWeekEnds.setSelected(false);

        subscriptionDataArrayList = subscriptionData;

    }

    public interface Subscription {
        void repetitionDays(ArrayList<Days> arrayList);

        void close();
    }
}
