package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tookancustomer.adapters.NLevelProductsAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.models.searchedProducts.SearchedProductsData;
import com.tookancustomer.models.searchedProducts.SearchedProductsModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Codes.StatusCode.NO_DATA_FOUND;

public class SearchProductActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlBackHeader;
    private EditText etSearch;
    private TextView tvHeading;
    private ImageView ivClearText;
    private ProgressBar pbLoading;
    private RecyclerView rvSearchedProductsList;
    private ProductCatalogueData productCatalogueData;
    private ArrayList<Datum> productCatalogueArrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private NLevelProductsAdapter nLevelProductsAdapter;
    public RelativeLayout rlTotalQuantity;
    public TextView tvTotalQuantity, tvSubTotal, tvMinOrderAmount, tvGoToEnhancedSearch;
    private Double minAmountPrice = 0.0;

    public Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    public String pickupAddress = "";
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;

    public LinearLayout llVegSwitchLayout;
    public Switch switchVegFilter;

    private String preorderDateTime;
    private boolean isFromMandatory;

    private LinearLayout llNoProductsFound;

    private TextView tvNoProductsFound;

    private LinearLayout llPlaceCustomOrder;
    private TextView tvCustomOrderTextView1, tvCustomOrderTextView2;
    private Button btnCustomOrder;

    private boolean isCustomCheckout = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        mActivity = this;
        minAmountPrice = StorefrontCommonData.getFormSettings().getMerchantMinimumOrder();
        pickupAddress = getIntent().getStringExtra(PICKUP_ADDRESS);
        pickupLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickupLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        preorderDateTime = getIntent().getStringExtra(DATE_TIME);
        isFromMandatory = getIntent().getBooleanExtra(IS_FROM_MANDATORY_CATEGORY, false);
        storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) getIntent().getSerializableExtra(STOREFRONT_DATA);

        initViews();
    }

    private void initViews() {
        rlBackHeader = findViewById(R.id.rlBack);
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvSearchedProductsList = findViewById(R.id.rvSearchedProductsList);
        rvSearchedProductsList.setLayoutManager(linearLayoutManager);
//        rvSearchedProductsList.addItemDecoration(new SimpleDividerItemDecoration(mActivity));
        rvSearchedProductsList.getItemAnimator().setChangeDuration(0);
        pbLoading = findViewById(R.id.pbLoading);
        ivClearText = findViewById(R.id.ivClearText);
        nLevelProductsAdapter = new NLevelProductsAdapter(mActivity, getSupportFragmentManager(), productCatalogueArrayList, new ArrayList<Integer>(), true);
        rvSearchedProductsList.setAdapter(nLevelProductsAdapter);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setHint(getStrings(R.string.search));
        etSearch.requestFocus();

        tvMinOrderAmount = findViewById(R.id.tvMinOrderAmount);

        tvGoToEnhancedSearch = findViewById(R.id.tvGoToEnhancedSearch);
        tvGoToEnhancedSearch.setText(getStrings(R.string.search_across_all_stores).replace(MERCHANTS, StorefrontCommonData.getTerminology().getMerchants()));
        tvGoToEnhancedSearch.setVisibility(UIManager.getTotalStores() > 1 ? View.VISIBLE : View.GONE);

        rlTotalQuantity = findViewById(R.id.rlTotalQuantity);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        Utils.setVisibility(View.GONE, rlTotalQuantity);
        Utils.setOnClickListener(this, rlTotalQuantity, tvGoToEnhancedSearch);

        ((TextView) findViewById(R.id.tvVegOnlyLabel)).setText(StorefrontCommonData.getTerminology().getVegOnly());
        llVegSwitchLayout = findViewById(R.id.llVegSwitchLayout);
        llVegSwitchLayout.setVisibility((StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegFilter() == 1
                && storefrontData.isVegFilterActive()) ? View.VISIBLE : View.GONE);
        switchVegFilter = findViewById(R.id.switchVegFilter);
        switchVegFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (etSearch.getText().toString().trim().length() >= 3) {
                    getSearchedProducts();
                }
            }
        });


        llNoProductsFound = findViewById(R.id.llNoProductsFound);
        llNoProductsFound.setVisibility(View.GONE);

        tvNoProductsFound = findViewById(R.id.tvNoProductsFound);
        tvNoProductsFound.setText(getStrings(R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getProduct()));

        llPlaceCustomOrder = findViewById(R.id.llPlaceCustomOrder);
        tvCustomOrderTextView1 = findViewById(R.id.tvCustomOrderTextView1);
        tvCustomOrderTextView2 = findViewById(R.id.tvCustomOrderTextView2);
        btnCustomOrder = findViewById(R.id.btnCustomOrder);


        if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {

            tvCustomOrderTextView1.setText(StorefrontCommonData.getTerminology().getREADY_TO_PLACE_YOUR_ORDER());

            String customOrderString = storefrontData.getStoreName();
            Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, customOrderString);
            tvCustomOrderTextView2.setText(string);
            btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.text_create) + " "
                    + getStrings(R.string.text_order_now).replace(ORDER, Utils.getCallTaskAs(true, true)));

        } else {
            tvCustomOrderTextView1.setText(StorefrontCommonData.getString(mActivity, R.string.could_not_found));

            String customOrderString = StorefrontCommonData.getString(mActivity, R.string.place_custom_order_per_requirement).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder());
            Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, StorefrontCommonData.getTerminology().getCustomOrder());
            tvCustomOrderTextView2.setText(string);
            btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.place_custom_order).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder()));
        }


        Utils.setOnClickListener(this, btnCustomOrder);


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Utils.hideSoftKeyboard(mActivity);
                return true;
            }
        });

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
                    productCatalogueArrayList.clear();
                    if (rvSearchedProductsList.getAdapter() != null)
                        rvSearchedProductsList.getAdapter().notifyDataSetChanged();

                    llNoProductsFound.setVisibility(View.GONE);

                } else if (s.toString().trim().length() < 3) {
                    ivClearText.setVisibility(View.VISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                    getSearchedProducts();
                }
            }
        });

        Utils.setOnClickListener(this, rlBackHeader, ivClearText);
    }

    private void getSearchedProducts() {
        if (!Utils.internetCheck(mActivity)) {
            Utils.snackBar(mActivity, getStrings(R.string.no_internet_try_again));
            pbLoading.setVisibility(View.GONE);
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("search_text", etSearch.getText().toString().trim());
        commonParams.add("latitude", pickupLatitude);
        commonParams.add("longitude", pickupLongitude);
        if (switchVegFilter.isChecked()) {
            commonParams.add("is_veg", 1);
        }

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        commonParams.add(DATE_TIME, preorderDateTime == null ? DateUtils.getInstance().getCurrentDateTimeUtc() : preorderDateTime);
        commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());
        pbLoading.setVisibility(View.VISIBLE);

        RestClient.getApiInterface(mActivity).searchProducts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                rvSearchedProductsList.setVisibility(View.VISIBLE);
                UpdateNLevelProductData updateNLevelProductData = new UpdateNLevelProductData();
                updateNLevelProductData.execute(baseModel);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 900 && productCatalogueData != null) {
                    Utils.snackBar(mActivity, error.getMessage());
                } else {
                    setVisibilityNoProduct();
                    rvSearchedProductsList.setVisibility(View.GONE);

                    if (error.getStatusCode() == NO_DATA_FOUND.getStatusCode()
                            && error.getMessage().equalsIgnoreCase(getStrings(R.string.no_data_found_backend_message))) {
                    } else {
                        new AlertDialog.Builder(mActivity).message(error.getMessage()).build().show();
                    }
                }
                pbLoading.setVisibility(View.GONE);
            }
        });
    }


    private class UpdateNLevelProductData extends AsyncTask<BaseModel, Void, ArrayList<Datum>> {
        String searchedString = "";
        String searchedTextString = etSearch.getText().toString().trim();

        @Override
        protected ArrayList<Datum> doInBackground(BaseModel... baseModels) {
            BaseModel baseModel = baseModels[0];

            SearchedProductsModel searchedProductsModel = new SearchedProductsModel();
            ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();

            try {
                searchedProductsModel.setData(baseModel.toResponseModel(SearchedProductsData.class));
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }
            searchedString = searchedProductsModel.getData().getSearchText();

            if (searchedTextString.equals(searchedString)) {
                productCatalogueDataResponse.setData(searchedProductsModel.getData().getResultList());

                for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {

                    productCatalogueDataResponse.getData().get(i).setStorefrontData(storefrontData);
//                    productCatalogueDataResponse.getData().get(i).setSellerId(StorefrontCommonData.getUserData().getData().getVendorDetails().getUserId());
                    productCatalogueDataResponse.getData().get(i).setSellerId(storefrontData.getStorefrontUserId());
                    productCatalogueDataResponse.getData().get(i).setFormId(StorefrontCommonData.getFormSettings().getFormId());
                    productCatalogueDataResponse.getData().get(i).setVendorId(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                        if (productCatalogueDataResponse.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                            productCatalogueDataResponse.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                            productCatalogueDataResponse.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());
                            productCatalogueDataResponse.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());
                            productCatalogueDataResponse.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                            productCatalogueDataResponse.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                            productCatalogueDataResponse.getData().get(i).setPaymentSettings(Dependencies.getSelectedProductsArrayList().get(j).getPaymentSettings());

                            Dependencies.getSelectedProductsArrayList().set(j, productCatalogueDataResponse.getData().get(i));

                        }
                    }
//                }
                }
                productCatalogueData = productCatalogueDataResponse;
                productCatalogueArrayList.clear();
                productCatalogueArrayList.addAll(productCatalogueData.getData());
                return productCatalogueDataResponse.getData();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Datum> data) {
            super.onPostExecute(data);
            if (etSearch.getText().toString().trim().isEmpty()) {

                productCatalogueArrayList.clear();
                if (rvSearchedProductsList.getAdapter() != null)
                    rvSearchedProductsList.getAdapter().notifyDataSetChanged();
                setVisibilityNoProduct();
                pbLoading.setVisibility(View.GONE);

            } else if (etSearch.getText().toString().trim().equals(searchedString)) {
                if (productCatalogueArrayList.size() == 0) {
                    setVisibilityNoProduct();
                } else {
                    llNoProductsFound.setVisibility(View.GONE);
                }

                if (rvSearchedProductsList.getAdapter() != null) {
                    rvSearchedProductsList.getAdapter().notifyDataSetChanged();
                }
                pbLoading.setVisibility(View.GONE);
            }
            setTotalQuantity(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGoToEnhancedSearch:
                Bundle bundle = new Bundle();
                bundle.putDouble(PICKUP_LATITUDE, pickupLatitude);
                bundle.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                bundle.putString(PICKUP_ADDRESS, pickupAddress);
                bundle.putString(SEARCHED_STRING, etSearch.getText().toString());
                bundle.putString(DATE_TIME, preorderDateTime == null ? DateUtils.getInstance().getCurrentDateTimeUtc() : preorderDateTime);

                Transition.transitForResult(mActivity, MarketplaceSearchActivity.class, Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY, bundle, false);
//            activity.startActivityForResult(intent, OPEN_HOME_ACTIVITY);

                break;
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.ivClearText:
                etSearch.setText("");
                break;
            case R.id.rlTotalQuantity:

                if (isFromMandatory) {

                    Intent intent = new Intent();
                    Bundle extrasMandatory = new Bundle();
                    extrasMandatory.putBoolean(IS_FROM_MANDATORY_CATEGORY, true);
                    intent.putExtras(extrasMandatory);

                    setResult(RESULT_OK, intent);
                    finish();

                    return;
                }


                if (minAmountPrice > Dependencies.getProductListSubtotal()) {
                    Utils.snackBar(mActivity, getStrings(R.string.minimumOrderAmountIs).replace(ORDER, Utils.getCallTaskAs(true, false))
                            .replace(AMOUNT, Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minAmountPrice.intValue())));
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
                    extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                    extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                    extraa.putString(PICKUP_ADDRESS, pickupAddress);
                    Transition.openCheckoutActivity(mActivity, extraa);
                    // Intent intent1 = new Intent(getActivity(), MapActivity.class);
//                Intent intent1 = new Intent(getActivity(), CheckOutActivity.class);
//                intent1.putExtras(extraa);
//                startActivityForResult(intent1, OPEN_CHECKOUT_SCREEN);
//                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                break;
            case R.id.btnCustomOrder:
                isCustomCheckout = true;

                Bundle extraa = new Bundle();
                extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                extraa.putString(PICKUP_ADDRESS, pickupAddress);
                extraa.putBoolean("isCustomOrder", true);


                if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {
                    extraa.putBoolean("isCustomOrderMerchantLevel", true);
                }


                extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                Transition.openCustomCheckoutActivity(mActivity, extraa);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity);
        switch (requestCode) {
            case Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                        setResult(mActivity.RESULT_OK, data);
                        Transition.exit(mActivity);
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtras(new Bundle());
                        setResult(RESULT_OK, returnIntent);
                        finish();
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
                    activityResultCheckoutScreen(productDataItem);
                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }

                break;
            case Codes.Request.OPEN_CUSTOMISATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Datum productDataItem = null;

                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }
                    activityResultCheckoutScreen(productDataItem);
                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }

                break;
            case Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY:
            case Codes.Request.OPEN_AGENT_LIST_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Datum productDataItem = null;
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }
                    activityResultCheckoutScreen(productDataItem);
                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;

            case Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
//                if (resultCode == Activity.RESULT_OK) {
                setTotalQuantity(true);
                activityResultCheckoutScreen(null);
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

    public void activityResultCheckoutScreen(Datum productDataItem) {
        if (productCatalogueData != null && productCatalogueData.getData() != null) {
            if (Dependencies.getSelectedProductsArrayList().size() == 0) {
                for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                    productCatalogueData.getData().get(i).setSelectedQuantity(0);
                    productCatalogueData.getData().get(i).setItemSelectedList(new ArrayList<ItemSelected>());
                }
            }

            for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                    if (productCatalogueData.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                        productCatalogueData.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                        productCatalogueData.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());

                        productCatalogueData.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                        productCatalogueData.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());

                        productCatalogueData.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());

                        for (int k = 0; k < productCatalogueData.getData().get(i).getItemSelectedList().size(); k++) {
                            if (productCatalogueData.getData().get(i).getItemSelectedList().get(k).getQuantity() == 0) {
                                productCatalogueData.getData().get(i).getItemSelectedList().remove(k);
                                k--;
                            }
                        }
                    }
                }
            }

            if (productDataItem != null) {
                for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                    if (productCatalogueData.getData().get(i).getProductId().equals(productDataItem.getProductId())) {
                        productCatalogueData.getData().set(i, productDataItem);
                        productCatalogueArrayList.set(i, productDataItem);
                    }
                }
            }
        }

        if (rvSearchedProductsList.getAdapter() != null) {
            rvSearchedProductsList.getAdapter().notifyDataSetChanged();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setTotalQuantity(true);

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

        if (Dependencies.getCartSize() != 0 && productCatalogueData != null && productCatalogueData.getData().size() > 0 && productCatalogueData.getData().get(0).getPaymentSettings() != null)

            tvSubTotal.setText(Utils.getCurrencySymbol(productCatalogueData.getData().get(0).getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + "");
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

        if (minAmountPrice > Dependencies.getProductListSubtotal()) {
            if (rlTotalQuantity.getVisibility() == View.VISIBLE)
                tvMinOrderAmount.setVisibility(View.VISIBLE);
            else
                tvMinOrderAmount.setVisibility(View.GONE);

            tvMinOrderAmount.setText(getStrings(R.string.minimumOrderAmount).replace(ORDER, Utils.getCallTaskAs(true, false)).replace(AMOUNT, Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minAmountPrice)));
        } else {
            tvMinOrderAmount.setVisibility(View.GONE);
        }
    }

    private void setVisibilityNoProduct() {
        llNoProductsFound.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getAppConfigurationData().getIsCustomOrderActive() == 1 && storefrontData.isStoreAvailableForBooking()) {
            llPlaceCustomOrder.setVisibility(View.VISIBLE);
            tvNoProductsFound.setVisibility(View.GONE);
        } else {
            tvNoProductsFound.setVisibility(View.VISIBLE);
        }
    }

}