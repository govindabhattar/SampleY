package com.tookancustomer.modules.sideMenu.adapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tookancustomer.R;
import com.tookancustomer.modules.sideMenu.model.SideMenu;
import com.tookancustomer.utility.CustomTextView;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

    private ArrayList<SideMenu> sideMenuList;

    public OptionsAdapter(ArrayList<SideMenu> sideMenuList) {
        this.sideMenuList = sideMenuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_side_menu_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SideMenu sideMenu = sideMenuList.get(holder.getAdapterPosition());
        holder.tvOption.setText(sideMenu.getOption());
        holder.tvOption.setCompoundDrawablesRelative(ContextCompat.getDrawable(holder.tvOption.getContext(), sideMenu.getOptionDrawable()),
                null, null, null);

        holder.tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return sideMenuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvOption;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvOption = itemView.findViewById(R.id.tvOption);
        }
    }
}
