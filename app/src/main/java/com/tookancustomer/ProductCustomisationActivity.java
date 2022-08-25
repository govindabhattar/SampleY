package com.tookancustomer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tookancustomer.adapters.MenusItemCustomizeAdapter;
import com.tookancustomer.adapters.SideOrderAdapter;
import com.tookancustomer.agentListing.AgentListingActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItem;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItemSelected;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CateringCartUtil;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ProductCustomisationActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public RelativeLayout rlBack;
    String startDate = "";
    private TextView tvHeading;
    private ItemSelected itemSelected;

    private TextView tvAddToCart, tvSideOrderLabel;
    private RecyclerView rvCustomizeItem, rvSideOrder;
    private RelativeLayout llTotalQuantity;
    private TextView tvSubTotal;
    private MenusItemCustomizeAdapter menusItemCustomizeAdapter;
    private SideOrderAdapter sideOrderAdapter;
    private Datum productDataItem = null;
    private int itemPos = 0;
    private double normalProductAmount, sideOrderAmount;
    private ArrayList<Datum> sideOrderArrayList = new ArrayList<>();
    private double latitude, longitude;
    private boolean isAddFromEditable = false;
    private String quantity = "0";
    private double productPrice = 0.0;
    private boolean isEditCustomization = false, isAddonsEdited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storefront_product_customisation);
        mActivity = this;
        itemSelected = new ItemSelected();

        if (getIntent().hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
            itemPos = getIntent().getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
        }
        if (getIntent().hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
            productDataItem = (Datum) getIntent().getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
        }

        if (getIntent().hasExtra(Keys.Extras.PRODUCT_QUANTITY)) {
            quantity = getIntent().getStringExtra(Keys.Extras.PRODUCT_QUANTITY);
        }
        if (getIntent().hasExtra(Keys.Extras.IS_EDIT_CUSTOMIZATION)) {
            isEditCustomization = getIntent().getBooleanExtra((Keys.Extras.IS_EDIT_CUSTOMIZATION), false);
        }
        if (getIntent().hasExtra(Keys.Extras.PRODUCT_PRICE)) {
            productPrice = getIntent().getDoubleExtra((Keys.Extras.PRODUCT_PRICE), 0.0);
        }
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble(PICKUP_LATITUDE);
        longitude = bundle.getDouble(PICKUP_LONGITUDE);

//        if (getIntent().hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
//            ArrayList<ProductTypeData> productCatalogueData = (ArrayList<ProductTypeData>) getIntent().getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
//            productDataItem = productCatalogueData.get(itemPos);
//        }
        initViews();

        if (productDataItem != null) {

            if (productDataItem.getLayoutData() != null && productDataItem.getLayoutData().getButtons() != null
                    && productDataItem.getLayoutData().getButtons().size() > 0
                    && Constants.NLevelButtonType.getButtonIdByValue(mActivity, productDataItem.getLayoutData().getButtons().get(0).getType()) == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {

                tvAddToCart.setText(productDataItem.getStorefrontData().getButtons().getButtonNames().getAdd() + " " + getStrings(R.string.to) + " " + StorefrontCommonData.getTerminology().getCart(true));
            } else {
                tvAddToCart.setText(getStrings(R.string.add_to_cart).replace(CART, StorefrontCommonData.getTerminology().getCart(true)));
            }
            menusItemCustomizeAdapter = new MenusItemCustomizeAdapter(mActivity, productDataItem, itemPos, quantity, isEditCustomization,
                    new MenusItemCustomizeAdapter.Callback() {
                        @Override
                        public void updateItemTotalPrice(ItemSelected itemSelectedd, double customizationPrice, boolean isItemCusto, boolean isEditAddons) {
                            itemSelected = itemSelectedd;
                            isAddonsEdited = isEditAddons;

                            // menusItemCustomizeAdapter.getItem().getItemSelectedList().add(itemSelected);
                            normalProductAmount = Double.parseDouble(Utils.getDoubleTwoDigits(itemSelectedd.getTotalPriceWithQuantity()));


                            if (!isItemCusto) {
                                tvSubTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol(productDataItem.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(productPrice))));
                            } else {
                                tvSubTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol(productDataItem.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(sideOrderAmount + normalProductAmount))));
                            }

                            if (!isEditCustomization)
                                tvSubTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol(productDataItem.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(sideOrderAmount + normalProductAmount))));
                        }

                        @Override
                        public void onItemMinusClick(boolean allItemsFinished) {
                            if (allItemsFinished)
                                onBackPressed();
                        }

                        @Override
                        public void onItemPlusClick(boolean isEditable) {
                            isAddFromEditable = isEditable;
                        }


                    });
            rvCustomizeItem.setAdapter(menusItemCustomizeAdapter);

        }
        sideOrderAdapter = new SideOrderAdapter(mActivity, getSupportFragmentManager(), sideOrderArrayList,
                new SideOrderAdapter.Callback() {
                    @Override
                    public void updateItemTotalPrice(double totalPriceSideOrder) {
                        sideOrderAmount = totalPriceSideOrder;
                        tvSubTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol(sideOrderArrayList.get(0).getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(sideOrderAmount + normalProductAmount))));

                    }

                });

        rvSideOrder.setAdapter(sideOrderAdapter);


        if (UIManager.getSideOrderActive() &&
                StorefrontCommonData.getAppConfigurationData().getOnboardingBusinessType() == Constants.OnboardingBusinessType.CATERING) {
            getSubCategory();

        } else {
            tvSideOrderLabel.setVisibility(View.GONE);

        }


    }

    private void getSubCategory() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        commonParams.add("offset", 0);
        commonParams.add("limit", 0);

        RestClient.getApiInterface(this).getSubcategory(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true) {
            @Override
            public void success(BaseModel baseModel) {

                ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();

                try {

                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    productCatalogueDataResponse.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }


                for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {

                    Datum sideOrderData = productCatalogueDataResponse.getData().get(i);
                    sideOrderData.setStorefrontData(productDataItem.getStorefrontData());
                    sideOrderData.setFormId(productDataItem.getFormId());
                    sideOrderData.setVendorId(productDataItem.getVendorId());
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {

                        Datum selectedData = Dependencies.getSelectedProductsArrayList().get(j);
                        if (sideOrderData.getProductId().equals(selectedData.getProductId())) {
                            sideOrderData.setSelectedQuantity(selectedData.getSelectedQuantity());
                            sideOrderData.setProductStartDate(selectedData.getProductStartDate());
                            sideOrderData.setSurgeAmount(selectedData.getSurgeAmount());
                            sideOrderData.setPaymentSettings(selectedData.getPaymentSettings());
                            sideOrderData.setProductEndDate(selectedData.getProductEndDate());


                            sideOrderAmount = sideOrderAmount + selectedData.getSelectedQuantity() * selectedData.getPrice().doubleValue();

                        }
                    }
                }

                tvSubTotal.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(sideOrderAmount + normalProductAmount))));

                sideOrderAdapter.setTotalAmount(sideOrderAmount);
                sideOrderArrayList.addAll(productCatalogueDataResponse.getData());

                Collections.sort(sideOrderArrayList, new Comparator<Datum>() {
                    @Override
                    public int compare(Datum lhs, Datum rhs) {
                        return lhs.getParentCategoryId().compareTo(rhs.getParentCategoryId());
                    }
                });

                for (int i = 0; i < sideOrderArrayList.size(); i++) {
                    if (i == 0)
                        sideOrderArrayList.get(i).setPreviousCategoryID(0);
                    else
                        sideOrderArrayList.get(i).setPreviousCategoryID(sideOrderArrayList.get(i - 1).getParentCategoryId());

                }

                rvSideOrder.getAdapter().notifyDataSetChanged();

                if (sideOrderArrayList.size() > 0) {
                    tvSideOrderLabel.setVisibility(View.VISIBLE);
                } else {
                    tvSideOrderLabel.setVisibility(View.GONE);
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (sideOrderArrayList.size() > 0) {
                    tvSideOrderLabel.setVisibility(View.VISIBLE);
                } else {
                    tvSideOrderLabel.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.customize_item).replace(ITEM, StorefrontCommonData.getTerminology().getItem(true)));

        llTotalQuantity = findViewById(R.id.llTotalQuantity);
        tvSideOrderLabel = findViewById(R.id.tvSideOrderLabel);
        tvSideOrderLabel.setText(getStrings(R.string.sideOrders).replace(ORDERS, StorefrontCommonData.getTerminology().getOrders()));
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvAddToCart = findViewById(R.id.tvAddToCart);
        rvCustomizeItem = findViewById(R.id.rvCustomizeItem);
        rvSideOrder = findViewById(R.id.rvSideOrder);


        rvCustomizeItem.setLayoutManager(new LinearLayoutManager(mActivity));
        rvCustomizeItem.setItemAnimator(new DefaultItemAnimator());
        rvCustomizeItem.setHasFixedSize(false);


        rvSideOrder.setLayoutManager(new LinearLayoutManager(mActivity));
        rvSideOrder.setItemAnimator(new DefaultItemAnimator());
        rvSideOrder.setHasFixedSize(false);


        Utils.setOnClickListener(this, rlBack, llTotalQuantity);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rlBack) {
            onBackPressed();
        } else if (view.getId() == R.id.llTotalQuantity) {

            if (CateringCartUtil.validateDataAtAddOnsPage(menusItemCustomizeAdapter.getItemSelected(), productDataItem, this))
                if (menusItemCustomizeAdapter.getItem().getSelectedQuantity() == 0 &&
                        menusItemCustomizeAdapter.getItem().getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                        (menusItemCustomizeAdapter.getItem().getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                || menusItemCustomizeAdapter.getItem().getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT)) {


                    if (menusItemCustomizeAdapter.getItem().getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                            && (Constants.ProductsUnitType.getUnitType(menusItemCustomizeAdapter.getItem().getUnitType()) == Constants.ProductsUnitType.FIXED)
                            && menusItemCustomizeAdapter.getItem().getEnableTookanAgent() == 0) {
                        openDatePicker();

                    } else {
                        if (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && productDataItem.getIsProductTemplateEnabled() == 1) {
                            Intent intent = new Intent(mActivity, QuestionnaireTemplateActivity.class);
                            intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                            intent.putExtra(Keys.Extras.UPDATE_QUESTIONNAIRE, true);
                            intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, menusItemCustomizeAdapter.getItem());
                            intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(Keys.Extras.IS_START_TIME, true);
                            intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                            intent.putExtra("service_time", productDataItem.getServiceTime());
                            intent.putExtra(Keys.Extras.AGENT_ID, productDataItem.getAgentId());
                            startActivityForResult(intent, OPEN_QUESTIONNAIRE_ACTIVITY);
                        } else if (productDataItem.getIsAgentsOnProductTagsEnabled() == 1) {
                            setAgentList(itemPos);
                        } else if (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && (Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                                && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                            Intent intent = new Intent(mActivity, DatesOnCalendarActivity.class);
                            intent.putExtra(KEY_ITEM_POSITION, itemPos);
                            intent.putExtra(PRODUCT_CATALOGUE_DATA, menusItemCustomizeAdapter.getItem());
                            intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(IS_START_TIME, true);
                            intent.putExtra(SELECTED_DATE, "");
                            startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        } else {
                            Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                            intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                            intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, menusItemCustomizeAdapter.getItem());
                            intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(Keys.Extras.IS_START_TIME, true);
                            intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                            intent.putExtra(FROM_AGENT_SCREEN,true);
                            if (MyApplication.getInstance().getSelectedPickUpMode() == 2) {
                                intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 1);
                            } else {
                                intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 0);
                            }

                            intent.putExtra("service_time", productDataItem.getServiceTime());
                            intent.putExtra(Keys.Extras.AGENT_ID, productDataItem.getAgentId());
                            startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        }

                    }
                } else {
                    addToCart();

                    addSideOrderToCart();
                }
        }

    }


    public void setAgentList(int itemPos) {
        Intent intent = new Intent(mActivity, AgentListingActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, itemPos);
        intent.putExtra(PICKUP_LATITUDE, latitude);
        intent.putExtra(PICKUP_LONGITUDE, longitude);
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, menusItemCustomizeAdapter.getItem());
        startActivityForResult(intent, OPEN_AGENT_LIST_ACTIVITY);
    }


    private void addSideOrderToCart() {

        for (int i = 0; i < sideOrderArrayList.size(); i++) {
//            if (sideOrderArrayList.get(i).getSelectedQuantity() != 0) {
            Dependencies.addCartItem(mActivity, sideOrderArrayList.get(i));
//            }
        }
    }


    private boolean validateDataAtAddOnsPage() {
        if (menusItemCustomizeAdapter.getItemSelected().getQuantity() < productDataItem.getMinProductquantity()) {
            Utils.snackBar(this,
                    getStrings(R.string.error_msg_product_quantity_less_than_minimum_quantity) +
                            getStrings(R.string.empty_space) + productDataItem.getMinProductquantity());
            return false;
        } else {
            for (int i = 0; i < menusItemCustomizeAdapter.getItem().getCustomizeItem().size(); i++) {
                CustomizeItemSelected customizeItemSelected = null;
                CustomizeItem customizeItem = menusItemCustomizeAdapter.getItem().getCustomizeItem().get(i);
                for (int j = 0; j < menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().size(); j++) {

                    if (customizeItem.getCustomizeId() ==
                            menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().get(j).getCustomizeId() &&
                            menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().get(j).getCustomizeOptions().size() > 0) {
                        customizeItemSelected = menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().get(j);
                        break;
                    }
                }

                if (customizeItemSelected != null) {

                    if (customizeItem.getMinimumSelectionRequired() == 1) {
                        if (customizeItem.getIsCheckBox() == 1)
                            if (customizeItemSelected.getCustomizeOptions().size() != customizeItem.getMinimumSelection()) {
                                Utils.snackBar(this,
                                        getStrings(R.string.in_name_selected_quantity_equal_to_number)
                                                .replace(NAME, customizeItem.getCustomizeItemName())
                                                .replace(QUANTITY, customizeItem.getMinimumSelection() + ""));
                                return false;
                            }

                    }

                } else if (customizeItem.getMinimumSelectionRequired() == 1) {
                    Utils.snackBar(this,
                            customizeItem.getCustomizeItemName() + " is required");
                    return false;
                }

            }

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
            case Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY:
            case Codes.Request.OPEN_AGENT_LIST_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        menusItemCustomizeAdapter.setItem((Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA));
                    }
                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                    }
                    addToCart();
                }
                break;
        }
    }

    private void addToCart() {
        for (int i = 0; i < menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().size(); i++) {
            CustomizeItemSelected customizeItemSelected = menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().get(i);
            if (customizeItemSelected.getCustomizeOptions(false) == null || customizeItemSelected.getCustomizeOptions(false).size() == 0) {
                menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().remove(i);
                i--;
            }
        }

        int qnty = 0;

        if (menusItemCustomizeAdapter.getItem().getItemSelectedList().size() > 0) {
            for (int i = 0; i < menusItemCustomizeAdapter.getItem().getItemSelectedList().size(); i++) {
                qnty = qnty + menusItemCustomizeAdapter.getItem().getItemSelectedList().get(i).getQuantity();
            }
        }

//        menusItemCustomizeAdapter.getItem().getItemSelectedList().get(0).getCustomizeItemSelectedList()

        int index = menusItemCustomizeAdapter.getItem().getItemSelectedList().indexOf(menusItemCustomizeAdapter.getItemSelected());

        if (isEditCustomization) {

            if (index < 0)
                for (int i = 0; i < menusItemCustomizeAdapter.getItem().getItemSelectedList().size(); i++) {
                    for (int j = 0; j < menusItemCustomizeAdapter.getItem().getItemSelectedList().get(i).getCustomizeItemSelectedList().size(); j++) {
                        for (int k = 0; k < menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().size(); k++) {
                            if (menusItemCustomizeAdapter.getItemSelected().getCustomizeItemSelectedList().get(k).getCustomizeId()
                                    .equals(menusItemCustomizeAdapter.getItem().getItemSelectedList().get(i).getCustomizeItemSelectedList().get(j).getCustomizeId())) {
                                index = i;
                                break;
                            }

                        }
                    }
                }


            if (isAddFromEditable) {
                qnty = menusItemCustomizeAdapter.getItemSelected().getQuantity();
            } else if (itemPos == index) {
                qnty = menusItemCustomizeAdapter.getItemSelected().getQuantity();
            } else {
                qnty = qnty + menusItemCustomizeAdapter.getItemSelected().getQuantity();
            }

            if (productDataItem.getMaxProductquantity() == 0 || qnty <= productDataItem.getMaxProductquantity()) {
                if (isAddFromEditable) {
                    menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setQuantity(menusItemCustomizeAdapter.getItemSelected().getQuantity());
                } else if (itemPos == index) {
                    menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setQuantity(menusItemCustomizeAdapter.getItemSelected().getQuantity());
                } else {
                    menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setQuantity(
                            menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).getQuantity()
                                    + menusItemCustomizeAdapter.getItemSelected().getQuantity());
                }
                menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setCustomizationPrice(menusItemCustomizeAdapter.getItemSelected().getCustomizationPrice());


                ArrayList<ItemSelected> li = (ArrayList<ItemSelected>) menusItemCustomizeAdapter.getItem().getItemSelectedList();
                ArrayList<ItemSelected> li2 = new ArrayList<>();
                for (int i = 0; i < li.size(); i++) {
                    if (!li2.contains(li.get(i))) {
                        li2.add(li.get(i));
                    }
                }
                menusItemCustomizeAdapter.getItem().setItemSelectedList(li2);

            } else {
                String message = (StorefrontCommonData.getString(this, R.string.maximum_quantity_error))
                        .replace(NAME, productDataItem.getName())
                        .replace(QUANTITY, productDataItem.getMaxProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                Utils.snackBar(this, message);
                return;
            }
        } else if (index > -1) {
            if (isAddFromEditable) {
                qnty = menusItemCustomizeAdapter.getItemSelected().getQuantity();
            } else {
                qnty = qnty + menusItemCustomizeAdapter.getItemSelected().getQuantity();
            }

            if (productDataItem.getMaxProductquantity() == 0 || qnty <= productDataItem.getMaxProductquantity()) {
                if (isAddFromEditable) {
                    menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setQuantity(menusItemCustomizeAdapter.getItemSelected().getQuantity());
                } else {
                    menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setQuantity(
                            menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).getQuantity()
                                    + menusItemCustomizeAdapter.getItemSelected().getQuantity());
                }

                menusItemCustomizeAdapter.getItem().getItemSelectedList().get(index).setCustomizationPrice(menusItemCustomizeAdapter.getItemSelected().getCustomizationPrice());


                ArrayList<ItemSelected> li = (ArrayList<ItemSelected>) menusItemCustomizeAdapter.getItem().getItemSelectedList();
                ArrayList<ItemSelected> li2 = new ArrayList<>();
                for (int i = 0; i < li.size(); i++) {
                    if (!li2.contains(li.get(i))) {
                        li2.add(li.get(i));
                    }
                }
                menusItemCustomizeAdapter.getItem().setItemSelectedList(li2);

            } else {
                String message = (StorefrontCommonData.getString(this, R.string.maximum_quantity_error))
                        .replace(NAME, productDataItem.getName())
                        .replace(QUANTITY, productDataItem.getMaxProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                Utils.snackBar(this, message);
                return;
            }
        } else {
            if (isAddFromEditable) {
                qnty = menusItemCustomizeAdapter.getItemSelected().getQuantity();
            } else {
                qnty = qnty + menusItemCustomizeAdapter.getItemSelected().getQuantity();
            }

            if (productDataItem.getMaxProductquantity() == 0 || (qnty <= productDataItem.getMaxProductquantity())) {
                menusItemCustomizeAdapter.getItem().getItemSelectedList().add(menusItemCustomizeAdapter.getItemSelected());

            } else {
                String message = (StorefrontCommonData.getString(this, R.string.maximum_quantity_error))
                        .replace(NAME, productDataItem.getName())
                        .replace(QUANTITY, productDataItem.getMaxProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                Utils.snackBar(this, message);
                return;
            }

        }


        menusItemCustomizeAdapter.getItem().setSelectedQuantity(menusItemCustomizeAdapter.getItem().getTotalQuantity());
        Dependencies.addCartItem(mActivity, menusItemCustomizeAdapter.getItem());
        menusItemCustomizeAdapter.notifyDataSetChanged();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
        returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
        setResult(RESULT_OK, returnIntent);
        finish();


    }


    public void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (datePicker.isShown()) {
            openTimePicker();
        }
    }

    public void openTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isValidTime(startDate + " " + hourOfDay + ":" + minute)) {
            startDate = startDate + " " + hourOfDay + ":" + minute;
            Date productStartDate = DateUtils.getInstance().getDateFromString(startDate, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC);

            productDataItem.setProductStartDate(productStartDate);
            productDataItem.setProductEndDate(productStartDate);
            menusItemCustomizeAdapter.setItem(productDataItem);
            addToCart();

        } else {
            Utils.snackBar(mActivity, getStrings(R.string.invalid_selected_date));
        }
    }


    public boolean isValidTime(String date) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();

            if (DateUtils.getInstance().getDateFromString(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC).getTime() < calendar.getTime().getTime())
                isValidDate = false;
        }
        return isValidDate;
    }

    private double getSubTotal(Datum item) {
        double productTotalCalculatedPrice = 0.0;
        if (item.getItemSelectedList().size() > 0) {

            if (item.getServiceTime() > 0) {
                productTotalCalculatedPrice = (((item.getPrice().doubleValue() * Dependencies.getPredefiendInterval(item)) + (item.getItemSelectedList().get(itemPos).getCustomizationPrice())) * item.getItemSelectedList().get(itemPos).getQuantity());

            } else if (Dependencies.getInterval(item) == 0.0) {
                productTotalCalculatedPrice = (((item.getPrice().doubleValue() * 1) + (item.getItemSelectedList().get(itemPos).getCustomizationPrice())) * item.getItemSelectedList().get(itemPos).getQuantity());

            } else {
                productTotalCalculatedPrice = (((item.getPrice().doubleValue() * Dependencies.getInterval(item)) + (item.getItemSelectedList().get(itemPos).getCustomizationPrice())) * item.getItemSelectedList().get(itemPos).getQuantity());
            }
            productTotalCalculatedPrice = productTotalCalculatedPrice + item.getQuestionnaireTemplateCost();
            productTotalCalculatedPrice = productTotalCalculatedPrice + (item.getSurgeAmount() * item.getItemSelectedList().get(itemPos).getQuantity());
        }
        return productTotalCalculatedPrice;
    }

}