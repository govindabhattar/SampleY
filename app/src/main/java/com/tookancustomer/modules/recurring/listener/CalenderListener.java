package com.tookancustomer.modules.recurring.listener;

import com.tookancustomer.modules.recurring.model.DateInfo;
import com.tookancustomer.modules.recurring.model.DateItem;

/**
 * Created by Ashutosh Ojha on 2/19/19.
 */
public interface CalenderListener {

    boolean isDateSelected(DateItem dateTasks);

    void dateChecked(DateInfo dateInfo, int position);
}
