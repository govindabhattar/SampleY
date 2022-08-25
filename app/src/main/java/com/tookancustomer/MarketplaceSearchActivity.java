package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.adapter.ViewPagerAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.fragments.MarketplaceSearchFragment;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

public class MarketplaceSearchActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlBack;
    public EditText etSearch;
    private ImageView ivClearText;
    public ProgressBar pbLoading;
    private RelativeLayout rlTotalQuantity;
    public TextView tvTotalQuantity, tvSubTotal, tvMinOrderAmount;
    public TabLayout tabLayout;
    public ViewPager viewPager;

    public Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    public String pickupAddress = "";
    public String previousActivitySearchedString = "";


    public ViewPagerAdapter mPagerAdapter;
    public String searchedString = "";

    public int selectedTab = 0;

    private String preorderDateTime;

    private LinearLayout llNoProductsFound;

    private TextView tvNoProductsFound;

    private LinearLayout llPlaceCustomOrder;
    private TextView tvCustomOrderTextView1, tvCustomOrderTextView2;
    private Button btnCustomOrder;

    public boolean isCustomCheckout = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_search);
        mActivity = this;

        previousActivitySearchedString = getIntent().getStringExtra(SEARCHED_STRING);
        pickupAddress = getIntent().getStringExtra(PICKUP_ADDRESS);
        pickupLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickupLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        preorderDateTime = getIntent().getStringExtra(DATE_TIME);

        initViews();
    }

    public String getPreorderDateTime() {
        return preorderDateTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTotalQuantity(true);
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setHint(getStrings(R.string.search));
        ivClearText = findViewById(R.id.ivClearText);
        pbLoading = findViewById(R.id.pbLoading);
        etSearch.requestFocus();

        tvMinOrderAmount = findViewById(R.id.tvMinOrderAmount);
        rlTotalQuantity = findViewById(R.id.rlTotalQuantity);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvSubTotal = findViewById(R.id.tvSubTotal);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setVisibility(View.GONE);


        tabLayout.setupWithViewPager(viewPager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        Utils.setVisibility(View.GONE, rlTotalQuantity, tvMinOrderAmount);
        Utils.setOnClickListener(this, rlBack, ivClearText, rlTotalQuantity);

        llNoProductsFound = findViewById(R.id.llNoProductsFound);
        llNoProductsFound.setVisibility(View.VISIBLE);

        tvNoProductsFound = findViewById(R.id.tvNoProductsFound);
        tvNoProductsFound.setText(getStrings(R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getProduct()));

        llPlaceCustomOrder = findViewById(R.id.llPlaceCustomOrder);
        tvCustomOrderTextView1 = findViewById(R.id.tvCustomOrderTextView1);
        tvCustomOrderTextView2 = findViewById(R.id.tvCustomOrderTextView2);
        btnCustomOrder = findViewById(R.id.btnCustomOrder);
        tvCustomOrderTextView1.setText(StorefrontCommonData.getString(mActivity, R.string.could_not_found));

        String customOrderString = StorefrontCommonData.getString(mActivity, R.string.place_custom_order_per_requirement).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder());
        Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, StorefrontCommonData.getTerminology().getCustomOrder());
        tvCustomOrderTextView2.setText(string);
        btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.place_custom_order).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder()));
        setVisibilityNoProduct();
        Utils.setOnClickListener(this, btnCustomOrder);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Utils.hideSoftKeyboard(mActivity);
                return true;
            }
        });


        MarketplaceSearchFragment fragmentBothMPProducts = new MarketplaceSearchFragment(false, this);
        mPagerAdapter.addFragment(fragmentBothMPProducts, StorefrontCommonData.getTerminology().getProduct());
        MarketplaceSearchFragment fragmentOnlyMarketplaces = new MarketplaceSearchFragment(true, this);
        mPagerAdapter.addFragment(fragmentOnlyMarketplaces, StorefrontCommonData.getTerminology().getStore());
        mPagerAdapter.notifyDataSetChanged();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("search string", "<<<<<<<<<<<" + s.toString());
                if (s.toString().trim().isEmpty()) {
                    ivClearText.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    setVisibilityNoProduct();
                    pbLoading.setVisibility(View.GONE);
                    if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            Fragment fragment = mPagerAdapter.getItem(i);
                            if (fragment instanceof MarketplaceSearchFragment) {
                                ((MarketplaceSearchFragment) fragment).cityStorefrontsModel = new CityStorefrontsModel();
                            }
                        }
                    }


                } else if (s.toString().trim().length() < 3) {
                    ivClearText.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);

                } else {
                    ivClearText.setVisibility(View.VISIBLE);

                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    llNoProductsFound.setVisibility(View.GONE);
                    viewPager.setCurrentItem(selectedTab);

                    if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            Fragment fragment = mPagerAdapter.getItem(i);
                            if (fragment instanceof MarketplaceSearchFragment) {
                                ((MarketplaceSearchFragment) fragment).getSearchedProducts();
                            }
                        }
                    }
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedTab = viewPager.getCurrentItem();

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (previousActivitySearchedString != null) {
            etSearch.setText(previousActivitySearchedString);
            etSearch.setSelection(etSearch.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ivClearText:
                etSearch.setText("");
                break;
            case R.id.btnCustomOrder:
                isCustomCheckout = true;

                Bundle extraaCustomOrder = new Bundle();
                extraaCustomOrder.putDouble(PICKUP_LATITUDE, pickupLatitude);
                extraaCustomOrder.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                extraaCustomOrder.putString(PICKUP_ADDRESS, pickupAddress);
                extraaCustomOrder.putBoolean("isCustomOrder", true);
                Transition.openCustomCheckoutActivity(mActivity, extraaCustomOrder);
                break;
            case R.id.rlTotalQuantity:
                if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                    if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
                        Utils.snackBar(mActivity, getStrings(R.string.minimumOrderAmountIs)
                                .replace(ORDER, Utils.getCallTaskAs(true, false))
                                .replace(AMOUNT, Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder().intValue())));
                        return;
                    }
                    if (StorefrontCommonData.getUserData() != null) {
                        if (!Utils.internetCheck(mActivity)) {
                            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                            return;
                        }

                        Boolean goToReviewCart = false;

                        for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {
                            if (Dependencies.getSelectedProductsArrayList().get(i).getSelectedQuantity() > 0) {
                                goToReviewCart = true;
                            }
                        }

                        if (!goToReviewCart) {
                            new AlertDialog.Builder(mActivity).message(getStrings(R.string.choose_product_to_proceed_further).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())).build().show();
                            return;
                        }

                        Bundle extraa = new Bundle();
                        extraa.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                        extraa.putSerializable(STOREFRONT_DATA, Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData());
                        extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                        extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                        extraa.putString(PICKUP_ADDRESS, pickupAddress);
                        Transition.openCheckoutActivity(mActivity, extraa);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtras(new Bundle());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void setTotalQuantity(boolean isVisible) {
        if (isVisible) {
            rlTotalQuantity.setVisibility(Dependencies.getCartSize() == 0 ? View.GONE : View.VISIBLE);
        } else {
            rlTotalQuantity.setVisibility(View.GONE);
        }

        String checkoutString = getString(R.string.checkout_quantity_item)
                .replace(CHECKOUT, StorefrontCommonData.getTerminology().getCheckout())
                .replace(QUANTITY, Dependencies.getCartSize() + "")
                .replace(ITEM, (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getItems(false) : StorefrontCommonData.getTerminology().getItem(false)));

        Spannable string = CustomViewsUtil.createSpanForBoldText(mActivity, checkoutString, StorefrontCommonData.getTerminology().getCheckout());
        tvTotalQuantity.setText(string);

        if (Dependencies.getCartSize() != 0)

            tvSubTotal.setText(Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + "");

        else
            tvSubTotal.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + "");

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Dependencies.getProductListSubtotal() <= 0) {
            tvSubTotal.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvTotalQuantity.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            tvTotalQuantity.setLayoutParams(lp);
        } else {
            tvSubTotal.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvTotalQuantity.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            tvTotalQuantity.setLayoutParams(lp);
        }

        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
            if (rlTotalQuantity.getVisibility() == View.VISIBLE)
                tvMinOrderAmount.setVisibility(View.VISIBLE);
            else
                tvMinOrderAmount.setVisibility(View.GONE);

            tvMinOrderAmount.setText(getStrings(R.string.minimumOrderAmount).replace(ORDER, Utils.getCallTaskAs(true, false))
                    .replace(AMOUNT, Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder())));

        } else {
            tvMinOrderAmount.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity);
        switch (requestCode) {
            case Codes.Request.OPEN_HOME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                        setResult(RESULT_OK, data);
                        Transition.exit(this);
                    }
                }
                break;

            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Datum productDataItem = null;

                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }
                    if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            Fragment fragment = mPagerAdapter.getItem(i);
                            if (fragment instanceof MarketplaceSearchFragment) {
                                ((MarketplaceSearchFragment) fragment).activityResultCheckoutScreen(productDataItem);
                            }
                        }
                    }

                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;

            case Codes.Request.OPEN_CUSTOMISATION_ACTIVITY:
            case Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY:
            case Codes.Request.OPEN_AGENT_LIST_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Datum productDataItem = null;

                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }

                    if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            Fragment fragment = mPagerAdapter.getItem(i);
                            if (fragment instanceof MarketplaceSearchFragment) {
                                ((MarketplaceSearchFragment) fragment).activityResultCheckoutScreen(productDataItem);
                            }
                        }
                    }


                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;

            case Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
//                if (resultCode == Activity.RESULT_OK) {
                setTotalQuantity(true);

                if (mPagerAdapter != null && mPagerAdapter.getCount() > 0&& viewPager.getVisibility()==View.VISIBLE) {
                    for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                        Fragment fragment = mPagerAdapter.getItem(i);
                        if (fragment instanceof MarketplaceSearchFragment) {


                            ((MarketplaceSearchFragment) fragment).activityResultCheckoutScreen(null);
                        }
                    }
                }


//                }
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                        mActivity.setResult(mActivity.RESULT_OK, data);
                        Transition.exit(mActivity);
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Transition.exit(mActivity);
                }
                break;

            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    if (isCustomCheckout) {
                        Transition.openCustomCheckoutActivity(mActivity, data.getExtras());
                        isCustomCheckout = false;
                    } else
                        Transition.openCheckoutActivity(mActivity, data.getExtras());
                }
                break;
        }
    }


    private void setVisibilityNoProduct() {
        llNoProductsFound.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getAppConfigurationData().getIsCustomOrderActive() == 1) {
            llPlaceCustomOrder.setVisibility(View.VISIBLE);
            tvNoProductsFound.setVisibility(View.GONE);
        } else {
            tvNoProductsFound.setVisibility(View.VISIBLE);
        }
    }

}