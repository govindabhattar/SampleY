package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.FavLocationActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.favLocations.Locations;
import com.tookancustomer.models.userdata.UserData;

import java.util.List;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class OtherLocationsAdapter extends RecyclerView.Adapter<OtherLocationsAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<Locations> dataList;
    private UserData userData;

    public OtherLocationsAdapter(Activity activity, List<Locations> dataList, UserData userData) {
        this.activity = activity;
        this.dataList = dataList;
        this.userData = userData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_items_other_locations, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();
//        holder.tvOtherLocationType.setText(StorefrontCommonData.getString(activity,R.string.other));
        holder.tvOtherLocationType.setText(dataList.get(adapterPos).getName());
        holder.tvOtherAddress.setText(

                ((dataList.get(adapterPos).getHouse() != null && !dataList.get(adapterPos).getHouse().isEmpty()) ? dataList.get(adapterPos).getHouse() + ", " : "")
                        + dataList.get(adapterPos).getAddress() + ", "
                        + ((dataList.get(adapterPos).getLandmark() != null && !dataList.get(adapterPos).getLandmark().isEmpty()) ? dataList.get(adapterPos).getLandmark() + ", " : "")
                        + ((dataList.get(adapterPos).getPostalCode() != null && !dataList.get(adapterPos).getPostalCode().equals("null")
                        && !dataList.get(adapterPos).getPostalCode().isEmpty()) ? dataList.get(adapterPos).getPostalCode() : ""));
//        holder.tvOtherAddress.setText(dataList.get(adapterPos).getAddress());
        if (holder.tvOtherAddress.getText().toString().endsWith(", ")){
            holder.tvOtherAddress.setText(holder.tvOtherAddress.getText().toString().substring(0,holder.tvOtherAddress.getText().toString().length() - 2));
        }

//        holder.tvOtherAddress.setText(dataList.get(adapterPos).getAddress());

        holder.ibOtherOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FavLocationActivity) activity).addressType = StorefrontCommonData.getString(activity, R.string.other);
                //  ((FavLocationActivity) activity).locType = 2;
                ((FavLocationActivity) activity).isAddLocation = true;
                ((FavLocationActivity) activity).locationType = 2;
                ((FavLocationActivity) activity).fav_id = dataList.get(position).getFav_id();
                ((FavLocationActivity) activity).isOtherUpdate = true;
                ((FavLocationActivity) activity).otherAnchorView = holder.ibOtherOptions;
                ((FavLocationActivity) activity).otherHouse = dataList.get(position).getHouse();
                ((FavLocationActivity) activity).otherPinCode = dataList.get(position).getPostalCode();

                ((FavLocationActivity) activity).otherLabel = dataList.get(position).getName();
                ((FavLocationActivity) activity).otherLandmark = dataList.get(position).getLandmark();
                ((FavLocationActivity) activity).otherLatLng = new LatLng(dataList.get(position).getLatitude(), dataList.get(position).getLongitude());
                ((FavLocationActivity) activity).showAddressOptions();
            }
        });

        if (!((FavLocationActivity) activity).fromAccountScreen) {
            holder.rlOtherLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((FavLocationActivity) activity).address = holder.tvOtherAddress.getText().toString();
//                    ((FavLocationActivity) activity).address = dataList.get(position).getName();
                    ((FavLocationActivity) activity).latitude = dataList.get(position).getLatitude();
                    ((FavLocationActivity) activity).longitude = dataList.get(position).getLongitude();
                    ((FavLocationActivity) activity).latLng = new LatLng(dataList.get(position).getLatitude(), dataList.get(position).getLongitude());
                    ((FavLocationActivity) activity).performBackAction(dataList.get(position).getName());
                    ((FavLocationActivity) activity).otherPinCode = dataList.get(position).getPostalCode();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOtherLocationType;
        private TextView tvOtherAddress;
        private ImageButton ibOtherOptions;
        private RelativeLayout rlOtherLocation;

        ViewHolder(View itemView) {
            super(itemView);
            tvOtherLocationType = itemView.findViewById(R.id.tvOtherLocationType);
            tvOtherAddress = itemView.findViewById(R.id.tvOtherAddress);
            ibOtherOptions = itemView.findViewById(R.id.ibOtherOptions);
            rlOtherLocation = itemView.findViewById(R.id.rlOtherLocation);
        }
    }
}