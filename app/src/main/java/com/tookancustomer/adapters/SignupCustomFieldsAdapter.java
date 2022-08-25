package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;

import java.util.List;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class SignupCustomFieldsAdapter extends RecyclerView.Adapter<SignupCustomFieldsAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<SignupTemplateData> dataList;
    private UserData userData;

    public SignupCustomFieldsAdapter(Activity activity, List<SignupTemplateData> dataList, UserData userData) {
        this.activity = activity;
        this.dataList = dataList;
        this.userData = userData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_signup_custom_fields, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();
        if (!userData.getData().getSignupTemplateData().get(position).isShow()
                || userData.getData().getSignupTemplateData().get(position).getDataType().equals(Keys.DataType.BARCODE)
                || userData.getData().getSignupTemplateData().get(position).getDataType().equals(Keys.DataType.CHECKLIST)
                || userData.getData().getSignupTemplateData().get(position).getDataType().equals(Keys.DataType.TABLE)) {
            holder.tvCustomField.setVisibility(View.GONE);
        }
        holder.tvCustomField.setText(dataList.get(adapterPos).getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + dataList.get(adapterPos).getDisplayName().replace("_", " ").substring(1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCustomField;

        ViewHolder(View itemView) {
            super(itemView);
            tvCustomField = itemView.findViewById(R.id.tvCustomField);
        }
    }
}