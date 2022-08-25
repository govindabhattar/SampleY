package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tookancustomer.CheckOutActivityOld;
import com.tookancustomer.R;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.Utils;

import java.util.List;


/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<Datum> dataList;

    public CartRecyclerAdapter(Activity activity, List<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_cart_checkout, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int adapterPos = viewHolder.getAdapterPosition();

        CartAdapter cartAdapter = new CartAdapter(this, activity, dataList.get(adapterPos));
        viewHolder.rvCartList.setAdapter(cartAdapter);
    }

    public void setSubtotal() {
        double subTotal = 0.0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getItemSelectedList().size() > 0) {
                for (int j = 0; j < dataList.get(i).getItemSelectedList().size(); j++) {
                    subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(dataList.get(i).getItemSelectedList().get(j).getTotalPriceWithQuantity()));
                }
            } else
                subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(dataList.get(i).getSelectedQuantity().doubleValue() * dataList.get(i).getPrice().doubleValue()));
        }
        ((CheckOutActivityOld) activity).setSubTotal(subTotal);
//        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvCartList;

        public ViewHolder(View itemView) {
            super(itemView);
            rvCartList = itemView.findViewById(R.id.rvCartList);
            rvCartList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        }
    }
}