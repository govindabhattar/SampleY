package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tookancustomer.CheckOutActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.Utils;

import java.util.List;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class CheckoutCartAdapter extends RecyclerView.Adapter<CheckoutCartAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<Datum> dataList;
    private ViewHolder holder;

    public CheckoutCartAdapter(Activity activity, List<Datum> dataList) {
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
        holder = viewHolder;

        CheckoutCartSubAdapter cartAdapter = new CheckoutCartSubAdapter(this, activity, dataList.get(adapterPos));
        holder.rvCartList.setAdapter(cartAdapter);
    }

    public void setSubtotal() {
        double subTotal = 0.0;
        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getItemSelectedList().size() > 0) {

                for (int j = 0; j < dataList.get(i).getItemSelectedList().size(); j++) {
                    if (dataList.get(i).getItemSelectedList().get(j).getProductTotalCalculatedPrice() > 0) {
                        subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(dataList.get(i).getItemSelectedList().get(j).getProductTotalCalculatedPrice()));
                    } else {
                        if (dataList.get(i).getServiceTime() > 0) {

                            subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(
                                    ((dataList.get(i).getPrice().doubleValue() * Dependencies.getPredefiendInterval(dataList.get(i)))
                                            + dataList.get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                            * ((double) dataList.get(i).getItemSelectedList().get(j).getQuantity())
                            ));
                        } else if (Dependencies.getInterval(dataList.get(i)) == 0.0) {
                            subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(
                                    ((dataList.get(i).getPrice().doubleValue() * 1)
                                            + dataList.get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                            * ((double) dataList.get(i).getItemSelectedList().get(j).getQuantity())
                            ));

                        } else {
                            subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(
                                    ((dataList.get(i).getPrice().doubleValue() * Dependencies.getInterval(dataList.get(i)))
                                            + dataList.get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                            * ((double) dataList.get(i).getItemSelectedList().get(j).getQuantity())
                            ));

                        }
                    }
                }

            } else {

                if (dataList.get(i).getProductTotalCalculatedPrice() > 0) {
                    subTotal = subTotal + dataList.get(i).getProductTotalCalculatedPrice();
                } else {
                    if (dataList.get(i).getServiceTime() > 0) {
                        subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(
                                dataList.get(i).getPrice().doubleValue() * Dependencies.getPredefiendInterval(dataList.get(i))
                                        * dataList.get(i).getSelectedQuantity().doubleValue()

                        ));
                    } else if (Dependencies.getInterval(dataList.get(i)) == 0.0) {
                        subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(

                                (((dataList.get(i).getPrice().doubleValue() * 1))
                                        * dataList.get(i).getSelectedQuantity().doubleValue())

                        ));
                    } else {

                        subTotal = subTotal + Double.valueOf(Utils.getDoubleTwoDigits(

                                (dataList.get(i).getPrice().doubleValue()
                                        * Dependencies.getInterval(dataList.get(i))) * dataList.get(i).getSelectedQuantity().doubleValue()

                        ));
                    }
                }
            }


        }
        ((CheckOutActivity) activity).setSubTotal(subTotal);

//        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void setData() {
//        dataList = dataListSelected;

        // Log.d("Tag", dataListSelected.size() + " size");
        notifyDataSetChanged();

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