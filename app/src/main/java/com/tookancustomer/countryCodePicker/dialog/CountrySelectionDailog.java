package com.tookancustomer.countryCodePicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.tookancustomer.R;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class CountrySelectionDailog extends Dialog {

    private RecyclerView recyclerCountryPicker;
    private EditText etSearch;
    private CountryPickerAdapter adapter;
    private ArrayList<Country> countries;
    private ArrayList<Country> tempCountries;
    private CountryPickerAdapter.OnCountrySelectedListener listener;
    private static CountrySelectionDailog dailog;

    public static Dialog getInstance(Context context, CountryPickerAdapter.OnCountrySelectedListener listener) {
        return dailog = new CountrySelectionDailog(context, loadJSONFromAsset(context), listener);
    }

    public static void dismissDialog() {
        dailog.dismiss();
    }

    private static ArrayList<Country> loadJSONFromAsset(Context context) {
        ArrayList<Country> countryList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = context.getAssets().open("countries_list");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
//            ex.printStackTrace();
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("countries");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Country country = new Country();
                Locale loc = new Locale("", jo_inside.getString("countryShortCode"));
                country.setCountryName(loc.getDisplayCountry());
                country.setCountryCode("+" + jo_inside.getString("countryCode").replace("-", ""));
                country.setCountryShortCode(jo_inside.getString("countryShortCode"));

                countryList.add(country);
            }

            Collections.sort(countryList, new Country());
        } catch (JSONException e) {

                               Utils.printStackTrace(e);
        }
        return countryList;

    }


    public CountrySelectionDailog(@NonNull Context context, ArrayList<Country> countries,
                                  CountryPickerAdapter.OnCountrySelectedListener listener) {
        super(context);
        this.countries = countries;
        this.tempCountries = new ArrayList<>(countries);
        this.listener = listener;
    }

    public CountrySelectionDailog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CountrySelectionDailog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.country_code_picker);
        setDialogWindow();
        init();
        setTextWatcher();
    }

    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    tempCountries.clear();
                    for (Country country : countries) {
                        if (country.getCountryName().toLowerCase().contains(s)) {
                            tempCountries.add(country);
                        }
                    }
                } else if (s.length() == 0) {
                    tempCountries = new ArrayList<>(countries);
                }

                adapter.setCountries(tempCountries);
            }
        });
    }

    private void setDialogWindow() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
    }

    private void init() {
        etSearch = findViewById(R.id.etSearch);
        etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search,0,0,0);
        recyclerCountryPicker = findViewById(R.id.recyclerCountryPicker);
        recyclerCountryPicker.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CountryPickerAdapter(tempCountries, listener);
        recyclerCountryPicker.setAdapter(adapter);
    }
}
