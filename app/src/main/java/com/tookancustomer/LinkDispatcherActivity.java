package com.tookancustomer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.utility.AnimationUtils;

import java.util.Objects;

public class LinkDispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_link_dispatcher);
        if (getIntent().getData() != null) {
            final Uri data = getIntent().getData();
            if (data.getQueryParameter("merchant_id") != null) {
                Intent intent = new Intent(LinkDispatcherActivity.this, HomeActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("MERCHANT_ID", Integer.parseInt(Objects.requireNonNull(data.getLastPathSegment())));
                extras.putBoolean("FROM_DEEP_LINK", true);
                intent.putExtras(extras);
                startActivity(intent);
                AnimationUtils.forwardTransition(LinkDispatcherActivity.this);
                finish();
            } else {
                Intent intent = new Intent(LinkDispatcherActivity.this, MerchantCatalogActivity.class);
                String merchantId = getIntent().getData().getLastPathSegment();
                Bundle extras = new Bundle();
                extras.putInt("MERCHANT_ID", Integer.parseInt(merchantId));
                extras.putInt("DEEP_LINK_CATEGORY_ID", Integer.parseInt(Objects.requireNonNull(data.getQueryParameter("pordCat"))));
                extras.putInt("DEEP_LINK_PRODUCT_ID", Integer.parseInt(Objects.requireNonNull(data.getQueryParameter("prodname"))));
                extras.putBoolean("FROM_DEEP_LINK", true);
                intent.putExtras(extras);
                startActivity(intent);
                AnimationUtils.forwardTransition(LinkDispatcherActivity.this);
                finish();
            }


        }


        finish();

    }
}