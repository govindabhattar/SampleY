package com.tookancustomer.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.UnavailableProductData.UnavailableProduct;

import java.util.List;

public class UnavailableProductsItemAdapter extends RecyclerView.Adapter<UnavailableProductsItemAdapter.MyViewHolder> {


    private final Activity activity;
    private final List<UnavailableProduct> unavailableProducts;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView availabilityTV, productNameTV;

        public MyViewHolder(View view) {
            super(view);
            productNameTV = view.findViewById(R.id.productNameTV);
            availabilityTV = view.findViewById(R.id.availabilityTV);


        }
    }


    public UnavailableProductsItemAdapter(Activity activity, List<UnavailableProduct> unavailableProducts) {
        this.activity = activity;
        this.unavailableProducts = unavailableProducts;

    }

    @Override
    public UnavailableProductsItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_unavailable_products_item, parent, false);


        return new UnavailableProductsItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productNameTV.setText(unavailableProducts.get(position).getProductName());

        if (unavailableProducts.get(position).getIsEnabled() == 1) {
            if (unavailableProducts.get(position).getQuantity() > 0) {
                holder.availabilityTV.setText(StorefrontCommonData.getString(activity, R.string.only_available_product).replace("123", unavailableProducts.get(position).getQuantity() + ""));
                holder.availabilityTV.setTextColor(activity.getResources().getColor(R.color.black));
            } else {
                holder.availabilityTV.setText(StorefrontCommonData.getString(activity, R.string.out_of_stock));
                holder.availabilityTV.setTextColor(activity.getResources().getColor(R.color.color_red));
            }
        } else {
            holder.availabilityTV.setText(StorefrontCommonData.getString(activity, R.string.not_available));
            holder.availabilityTV.setTextColor(activity.getResources().getColor(R.color.color_red));

        }
    }


    @Override
    public int getItemCount() {
        return unavailableProducts.size();
    }


}
