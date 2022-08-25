package com.tookancustomer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tookancustomer.adapters.StaticAddresssAdapter;
import com.tookancustomer.appdata.ExtraConstants;
import com.tookancustomer.models.staticAddressData.StaticAddressData;
import com.tookancustomer.utility.SimpleDividerItemDecoration;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class SelectStaticAddressActivity extends BaseActivity implements View.OnClickListener, StaticAddresssAdapter.AddressSelectionListener {

    private RelativeLayout rlBack;
    private RecyclerView recyclerStaticAddresses;
    private StaticAddresssAdapter adapter;
    private EditText etSearchLocation;
    private ImageView ivClearText;
    private ArrayList<StaticAddressData> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_static_address);
        init();
        setOnClickListeners();
        setData();
        setAdapter();
        setTextWatcher();
    }

    private void setData() {
        if (getIntent().hasExtra(ExtraConstants.EXTRA_ADDRESS_LIST))
            addressList = getIntent().getExtras().getParcelableArrayList(ExtraConstants.EXTRA_ADDRESS_LIST);

        if (addressList == null)
            addressList = new ArrayList<>();
    }

    private void setTextWatcher() {
        etSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    ivClearText.setVisibility(View.VISIBLE);
                    setSearchResult(editable.toString());
                } else {
                    ivClearText.setVisibility(View.GONE);
                    setOriginalAddressList();
                }
            }
        });
    }

    private void setOriginalAddressList() {
        adapter.setData(addressList);
    }

    private void setSearchResult(String string) {
        ArrayList<StaticAddressData> tempAddressList = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).getFullAddress().toLowerCase().contains(string.toLowerCase()) ||
                    addressList.get(i).getLabel().toLowerCase().contains(string.toLowerCase())) {
                tempAddressList.add(addressList.get(i));
            }
        }

        adapter.setData(tempAddressList);
    }

    private void setAdapter() {
        adapter = new StaticAddresssAdapter(this, addressList);
        recyclerStaticAddresses.setAdapter(adapter);
    }

    private ArrayList<String> prepareDummyData() {
        ArrayList<String> addressList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            addressList.add((i + 1) + " Address");
        }
        return addressList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setOnClickListeners() {
        Utils.setOnClickListener(this, rlBack, ivClearText);
    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        recyclerStaticAddresses = findViewById(R.id.recyclerStaticAddresses);
        recyclerStaticAddresses.setLayoutManager(new LinearLayoutManager(this));
        recyclerStaticAddresses.addItemDecoration(new SimpleDividerItemDecoration(this));
        etSearchLocation = findViewById(R.id.etSearch);
        ivClearText = findViewById(R.id.ivClearText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ivClearText:
                etSearchLocation.setText("");
                break;
        }
    }

    @Override
    public void onAddressSelected(int pos, StaticAddressData addressData) {
        Intent intent = new Intent();
        intent.putExtra("address", addressData.getFullAddress());
        intent.putExtra("latitude", addressData.getLatitude());
        intent.putExtra("longitude", addressData.getLongitude());
        setResult(RESULT_OK, intent);
        finish();
    }
}
