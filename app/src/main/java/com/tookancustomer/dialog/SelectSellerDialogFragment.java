package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tookancustomer.R;
import com.tookancustomer.adapters.SelectSellersRecyclerAdapter;
import com.tookancustomer.models.ProductCatalogueData.Seller;

import java.util.ArrayList;

public class SelectSellerDialogFragment extends DialogFragment implements SelectSellersRecyclerAdapter.Callback {
    private RecyclerView recyclerSelectSellers;
    private SelectSellersRecyclerAdapter adapter;
    private ArrayList<Seller> sellers;
    private Activity activity;
    private SelectSellersRecyclerAdapter.Callback callback;

    private SelectSellerDialogFragment(ArrayList<Seller> sellers, SelectSellersRecyclerAdapter.Callback callback) {
        this.sellers = sellers;
        this.callback = callback;
    }

    public static SelectSellerDialogFragment newInstance(ArrayList<Seller> sellers, SelectSellersRecyclerAdapter.Callback callback) {
        return new SelectSellerDialogFragment(sellers, callback);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_select_sellers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setAdapter();
    }

    private void setAdapter() {
        adapter = new SelectSellersRecyclerAdapter(sellers, this);
        recyclerSelectSellers.setAdapter(adapter);
    }

    private void init(View view) {
        recyclerSelectSellers = view.findViewById(R.id.recyclerSelectSellers);
        recyclerSelectSellers.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onStart() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onStart();
    }

    @Override
    public void onItemSelected(Seller seller) {
        callback.onItemSelected(seller);
    }
}
