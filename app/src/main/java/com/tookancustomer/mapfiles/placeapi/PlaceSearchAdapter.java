package com.tookancustomer.mapfiles.placeapi;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tookancustomer.R;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder> {
    private ArrayList<Predictions> predictionList;
    private OnItemListener onItemListener;
    private Context mContext;

    public PlaceSearchAdapter(OnItemListener onItemListener, ArrayList<Predictions> predictionList) {
        this.predictionList = predictionList == null ? new ArrayList<Predictions>() : predictionList;
        this.onItemListener = onItemListener;
    }


    public void refreshAdapterDataSet(ArrayList<Predictions> predictionList) {
        if (predictionList == null) {
            this.predictionList = new ArrayList<>();
        } else {
            this.predictionList.clear();
            this.predictionList.addAll(predictionList);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_place_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        final Predictions prediction = predictionList.get(position);
        holder.tvAddress.setText(prediction.getName());
        holder.tvAddressDescription.setText(prediction.getAddress());
        holder.llPlaceParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.internetCheck(mContext)) {
                    Utils.snackBar((Activity) mContext, mContext.getString(R.string.not_connected_to_internet_text));
                    return;
                }
                refreshAdapterDataSet(null);
                onItemListener.onAddressSelected(prediction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvAddress;
        private AppCompatTextView tvAddressDescription;
        private LinearLayout llPlaceParent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvAddressDescription = itemView.findViewById(R.id.tvAddressDescription);
            llPlaceParent = itemView.findViewById(R.id.llPlaceParent);
        }
    }

    public interface OnItemListener {
        void onAddressSelected(Predictions predictions);
    }

}
