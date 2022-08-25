package com.tookancustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;


/**
 * Created by neerajwadhwani on 25/07/18.
 */

public class CatalogueActivity extends BaseActivity{

    private Button btnCategoryAndProd,btnProductOnly;
    private RelativeLayout rlBack;
    private TextView tvHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        btnCategoryAndProd=findViewById(R.id.btnCategoryAndProd);
        btnProductOnly=findViewById(R.id.btnProductOnly);
        btnProductOnly.setText( StorefrontCommonData.getTerminology().getProduct()+" "+getStrings(R.string.only_text));
        rlBack=findViewById(R.id.rlBack);
        tvHeading=findViewById(R.id.tvHeading);
        tvHeading.setText(R.string.add_catalogue);


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCategoryAndProd.setVisibility(StorefrontCommonData.getFormSettings().getProductView()==1?View.GONE:View.VISIBLE);
        btnCategoryAndProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition.startActivity(CatalogueActivity.this,AddProductActivity.class,null,true);
            }
        });
        btnProductOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition.startActivity(CatalogueActivity.this,AddProductActivity.class,null,true);
            }
        });
    }
}
