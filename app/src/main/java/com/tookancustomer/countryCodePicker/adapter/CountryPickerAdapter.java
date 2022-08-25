package com.tookancustomer.countryCodePicker.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.model.Country;

import java.util.ArrayList;

public class CountryPickerAdapter extends RecyclerView.Adapter {
    private ArrayList<Country> countries;
    private OnCountrySelectedListener listener;
    private static int VIEW_TYPE_RESULT = 0;
    private static int VIEW_TYPE_NO_RESULT = 1;

    public CountryPickerAdapter(ArrayList<Country> countries, OnCountrySelectedListener listener) {
        this.countries = countries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESULT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_country_picker, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_no_result_found, parent, false);
            return new NoResultViewHolder(view);
        }
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            onBindViewHolderForResult((MyViewHolder) holder, holder.getAdapterPosition());
        } else {
            onBindViewHolderForNoResult((NoResultViewHolder) holder, holder.getAdapterPosition());
        }


    }

    private void onBindViewHolderForNoResult(NoResultViewHolder holder, int adapterPosition) {
        holder.tvNoResultFound.setText(StorefrontCommonData.getString(holder.tvNoResultFound.getContext(),
                R.string.no_result_found));
    }

    private void onBindViewHolderForResult(final MyViewHolder holder, final int adapterPosition) {
        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;

        String country = countries.get(adapterPosition).getCountryShortCode();

        int firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset;

        String flag = new String(Character.toChars(firstChar))
                + new String(Character.toChars(secondChar));

        holder.tvCountryName.setText(flag + "  " + countries.get(adapterPosition).getCountryName());
        holder.tvCountryCode.setText(countries.get(adapterPosition).getCountryCode());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCountrySelected(countries.get(adapterPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size() != 0 ? countries.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (countries.size() != 0) {
            return VIEW_TYPE_RESULT;
        } else {
            return VIEW_TYPE_NO_RESULT;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCountryName, tvCountryCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
        }
    }

    public class NoResultViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNoResultFound;

        public NoResultViewHolder(View itemView) {
            super(itemView);
            tvNoResultFound = itemView.findViewById(R.id.tvNoResultFound);
        }
    }

    public interface OnCountrySelectedListener {
        void onCountrySelected(Country country);
    }
}
