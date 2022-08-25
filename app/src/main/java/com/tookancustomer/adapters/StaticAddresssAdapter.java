package com.tookancustomer.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.models.staticAddressData.StaticAddressData;

import java.util.ArrayList;

public class StaticAddresssAdapter extends RecyclerView.Adapter<StaticAddresssAdapter.MyViewHodler> {
    private AddressSelectionListener listener;

    private ArrayList<StaticAddressData> addressList;

    public StaticAddresssAdapter(AddressSelectionListener listener) {
        this.listener = listener;
    }

    public StaticAddresssAdapter(AddressSelectionListener listener, ArrayList<StaticAddressData> addressList) {
        this.listener = listener;
        this.addressList = addressList;
    }

    public StaticAddresssAdapter(ArrayList<StaticAddressData> addressList) {
        this.addressList = addressList;
    }

    public void setData(ArrayList<StaticAddressData> tempAddressList) {
        this.addressList = tempAddressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_static_address_adapter, parent, false);
        return new MyViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHodler holder, int position) {
        final int adapterPos = holder.getAdapterPosition();
        holder.tvAddressName.setText(addressList.get(adapterPos).getLabel());
        holder.tvFullAddress.setText(addressList.get(adapterPos).getFullAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onAddressSelected(adapterPos, addressList.get(adapterPos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList != null ? addressList.size() : 0;
    }

    public class MyViewHodler extends RecyclerView.ViewHolder {
        private TextView tvAddressName, tvFullAddress;

        public MyViewHodler(View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvFullAddress = itemView.findViewById(R.id.tvFullAddress);
        }
    }


    public interface AddressSelectionListener {
        void onAddressSelected(int pos, StaticAddressData address);
    }
}
