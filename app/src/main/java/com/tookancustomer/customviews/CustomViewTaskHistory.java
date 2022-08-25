package com.tookancustomer.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.models.taskdetails.TaskHistory;
import com.tookancustomer.utility.DateUtils;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomViewTaskHistory {
    private final String TAG = CustomViewTaskHistory.class.getSimpleName();

    private View view;
    private TextView tvLabel;
    private TextView tvPlaceHolder;

    private Context context;

    /**
     * Method to initialize the CustomField
     */
    public CustomViewTaskHistory(Context context) {
        this.context = context;

        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_task_details, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        tvPlaceHolder = view.findViewById(R.id.tvPlaceHolder);

    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(TaskHistory taskHistory) {
        tvLabel.setText(taskHistory.getDescription());
        tvPlaceHolder.setText(DateUtils.getInstance().convertToLocal(taskHistory.getCreationDatetime(), STANDARD_DATE_FORMAT_TZ));
        return view;
    }

}
