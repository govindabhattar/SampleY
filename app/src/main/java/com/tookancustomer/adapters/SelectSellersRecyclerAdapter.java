package com.tookancustomer.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.models.ProductCatalogueData.Seller;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class SelectSellersRecyclerAdapter extends RecyclerView.Adapter<SelectSellersRecyclerAdapter.MyViewHolder> {

    private ArrayList<Seller> sellerArrayList = new ArrayList<>();
    private Callback callback;

    public SelectSellersRecyclerAdapter(ArrayList<Seller> sellerArrayList, Callback callback) {
        this.sellerArrayList = sellerArrayList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_select_sellers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int adapterPos = holder.getAdapterPosition();
        holder.tvSellerName.setText(sellerArrayList.get(adapterPos).getStoreName());
        holder.tvSellerPrice.setText(Utils.getCurrencySymbol() + sellerArrayList.get(adapterPos).getPrice().toString());
        holder.rbtnSeller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    callback.onItemSelected(sellerArrayList.get(adapterPos));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemSelected(sellerArrayList.get(adapterPos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSellerName, tvSellerPrice;
        private RadioButton rbtnSeller;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            tvSellerPrice = itemView.findViewById(R.id.tvSellerPrice);
            rbtnSeller = itemView.findViewById(R.id.rbtnSeller);
        }
    }

    public interface Callback {
        void onItemSelected(Seller seller);
    }

}
