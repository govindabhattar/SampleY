package com.tookancustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.tookancustomer.adapter.ViewImagesPDPAdapter;
import com.tookancustomer.adapters.MerchantReviewsAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.CustomFieldNumberPDP;
import com.tookancustomer.customviews.CustomFieldTextViewPDP;
import com.tookancustomer.customviews.ResizableCustomView;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ImageVideoScreenDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.models.ProductCatalogueData.CustomField;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {

    private Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    private String pickupAddress = "";
    private Datum data;

    private ImageView ivBack;
    private TextView tvHeading;
    private TextView tvDescription, tvProductName, tvSubTotal, tvMerchantName, tvAddress, tvOutOfStockText;
    private ImageView ivForwardArrowButton;
    public RelativeLayout rlTotalQuantity;
    private ViewPager vpProductImage;
    private LinearLayout llIndicators;
    private LinearLayout linearLayoutQuantitySelector;
    private TextView tvSingleSelectionButton, textViewQuantity, tvTotalQuantity, tvMinOrderAmount;
    private RelativeLayout rlAddItem, rlRemoveItem;

    private int currentItem = 0;
    private String startDate;

    private int selectedQuantity;

    private LinearLayout llPDPCustomFields, llPDPCustomFieldsNumber;

    private LinearLayout llPriceAddRemove;
    private TextView tvProceed, tvPrice;
    private GoogleMap mMap;
    private final int MAX_ZOOM = 15;
    private static final int MAX_LINES = 4;

    private int itemPos = 0;

    private AppBarLayout appbarLayout;
    private int appbarHeight = 0;
    private CollapsingToolbarLayout collapsingToolbar;
    private NestedScrollView nsvScrollBar;
    private CoordinatorLayout clParentLayout;
    private View rlPDPView1;
    public TabLayout tabLayout;
    private TextView tvViewProfile;

    /**
     * For Rate and Review layout
     */
    private LinearLayout llRateReviewTopLayout;
    private LinearLayout llEditMerchantRatings, llAverageMerchantRatings, llGoToWriteReview, llAverageRating;
    private TextView tvSelfRating, tvEditReview;
    private LinearLayout llSelfRatingLayout;

    private LinearLayout llAverageRatingStar,llAverageRatingLayout;
    private LinearLayout llRateMerchantEmpty;
    private TextView tvAverageRating, tvTotalRateReviews, tvRateMerchantText;

    private RecyclerView rvReviewsList;
    private TextView tvRatingsReviewsHeading;
    private TextView tvSeeAllReviews;
    private MerchantReviewsAdapter merchantReviewsAdapter;
    private ArrayList<LastReviewRating> merchantReviewsList = new ArrayList<>();
    boolean isRateTouch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mActivity = this;

        Gson gson = new Gson();
        data = gson.fromJson(getIntent().getStringExtra(PRODUCT_DETAIL_DATA), Datum.class);
        Bundle bundle = getIntent().getExtras();
        pickupLatitude = bundle.getDouble(PICKUP_LATITUDE);
        pickupLongitude = bundle.getDouble(PICKUP_LONGITUDE);
        pickupAddress = bundle.getString(PICKUP_ADDRESS);
        itemPos = bundle.getInt(KEY_ITEM_POSITION);

        init();
    }

    /**
     * set the pdp view type acc to pdpViewTemplates
     */
    private void setPDPView() {
        rlPDPView1.setVisibility(View.VISIBLE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (data.getLatitude() != null && data.getLongitude() != null) {
            mapFragment.getView().setVisibility(View.VISIBLE);
        } else {
            mapFragment.getView().setVisibility(View.GONE);
        }
    }

    /**
     * initialize view
     */
    private void init() {
        rlPDPView1 = findViewById(R.id.rlPDPView1);
        ((TextView) rlPDPView1.findViewById(R.id.tvProductDetails)).setText(getStrings(R.string.product_detail));

        linearLayoutQuantitySelector = rlPDPView1.findViewById(R.id.linearLayoutQuantitySelector);
        tvSingleSelectionButton = rlPDPView1.findViewById(R.id.tvSingleSelectionButton);
        ivForwardArrowButton = rlPDPView1.findViewById(R.id.ivForwardArrowButton);
        textViewQuantity = rlPDPView1.findViewById(R.id.textViewQuantity);
        rlAddItem = rlPDPView1.findViewById(R.id.rlAddItem);
        rlRemoveItem = rlPDPView1.findViewById(R.id.rlRemoveItem);
        rlTotalQuantity = rlPDPView1.findViewById(R.id.rlTotalQuantity);
        tvTotalQuantity = rlPDPView1.findViewById(R.id.tvTotalQuantity);
        tvSubTotal = rlPDPView1.findViewById(R.id.tvSubTotal);
        tvMinOrderAmount = rlPDPView1.findViewById(R.id.tvMinOrderAmount);
        tvDescription = rlPDPView1.findViewById(R.id.tvDescription);
        tvProductName = rlPDPView1.findViewById(R.id.tvProductName);
        ivBack = rlPDPView1.findViewById(R.id.ivBack);
        tvHeading = rlPDPView1.findViewById(R.id.tvHeading);
        tvSubTotal = rlPDPView1.findViewById(R.id.tvSubTotal);
        tvMerchantName = rlPDPView1.findViewById(R.id.tvMerchantName);
        tvAddress = rlPDPView1.findViewById(R.id.tvAddress);
        tvOutOfStockText = rlPDPView1.findViewById(R.id.tvOutOfStockText);
        rlTotalQuantity = rlPDPView1.findViewById(R.id.rlTotalQuantity);
        vpProductImage = rlPDPView1.findViewById(R.id.vpProductImage);
        llIndicators = rlPDPView1.findViewById(R.id.llIndicators);

        appbarLayout = rlPDPView1.findViewById(R.id.appbarLayout);

        collapsingToolbar = rlPDPView1.findViewById(R.id.collapsingToolbar);

        nsvScrollBar = rlPDPView1.findViewById(R.id.nsvScrollBar);

        clParentLayout = rlPDPView1.findViewById(R.id.clParentLayout);
        tvProceed = rlPDPView1.findViewById(R.id.tvProceed);
        tvProceed.setText(getStrings(R.string.proceed));

        tvPrice = rlPDPView1.findViewById(R.id.tvPrice);

        llPDPCustomFields = rlPDPView1.findViewById(R.id.llPDPCustomFields);

        llPDPCustomFieldsNumber = rlPDPView1.findViewById(R.id.llPDPCustomFieldsNumber);

        llPriceAddRemove = rlPDPView1.findViewById(R.id.llPriceAddRemove);
        llPriceAddRemove.setVisibility(View.GONE);
        tvViewProfile = rlPDPView1.findViewById(R.id.tvViewProfile);
        tvViewProfile.setText(getStrings(R.string.view_profile));


        /**
         * For Rate and Review layout
         */
        llRateReviewTopLayout = rlPDPView1.findViewById(R.id.llRateReviewTopLayout);
        tvRatingsReviewsHeading = rlPDPView1.findViewById(R.id.tvRatingsReviewsHeading);
        tvRatingsReviewsHeading.setText(getStrings(R.string.reviews_ratings));
        llEditMerchantRatings = rlPDPView1.findViewById(R.id.llEditMerchantRatings);
        ((TextView) rlPDPView1.findViewById(R.id.tvReviewedThisPlace)).setText(getStrings(R.string.you_reviewed_this_place));
        tvSelfRating = rlPDPView1.findViewById(R.id.tvSelfRating);
        llSelfRatingLayout = rlPDPView1.findViewById(R.id.llSelfRatingLayout);
        tvEditReview = rlPDPView1.findViewById(R.id.tvEditReview);
        tvEditReview.setText(getStrings(R.string.edit_review));

        llAverageMerchantRatings = rlPDPView1.findViewById(R.id.llAverageMerchantRatings);
        llAverageRating = rlPDPView1.findViewById(R.id.llAverageRating);
        llAverageRatingStar = rlPDPView1.findViewById(R.id.llAverageRatingStar);
        llAverageRatingLayout = rlPDPView1.findViewById(R.id.llAverageRatingLayout);
        tvAverageRating = rlPDPView1.findViewById(R.id.tvAverageRating);
        tvTotalRateReviews = rlPDPView1.findViewById(R.id.tvTotalRateReviews);
        tvRateMerchantText = rlPDPView1.findViewById(R.id.tvRateMerchantText);
        llRateMerchantEmpty = rlPDPView1.findViewById(R.id.llRateMerchantEmpty);
        llGoToWriteReview = rlPDPView1.findViewById(R.id.llGoToWriteReview);

        tvSeeAllReviews = rlPDPView1.findViewById(R.id.tvSeeAllReviews);
        tvSeeAllReviews.setText(getStrings(R.string.see_all_reviews));

        rvReviewsList = rlPDPView1.findViewById(R.id.rvReviewsList);
        rvReviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvReviewsList.setNestedScrollingEnabled(false);
        merchantReviewsAdapter = new MerchantReviewsAdapter(mActivity, merchantReviewsList);
        rvReviewsList.setAdapter(merchantReviewsAdapter);


        Utils.setOnClickListener(this, rlTotalQuantity, rlPDPView1.findViewById(R.id.rlBack), rlAddItem, rlRemoveItem, tvSingleSelectionButton, tvProceed, tvViewProfile, tvEditReview, tvSeeAllReviews);
        Utils.setVisibility(View.GONE, tvSingleSelectionButton, linearLayoutQuantitySelector, ivForwardArrowButton, rlTotalQuantity);
        Utils.setVisibility(View.GONE, rlTotalQuantity);

        appbarLayout.post(new Runnable() {
            @Override
            public void run() {
                appbarHeight = appbarLayout.getMeasuredHeight();
                if (appbarLayout.getMeasuredHeight() > 0 && clParentLayout.getMeasuredHeight() > 0)
                    Log.e("IS_FIRST_SETTED", "true");
                appbarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(appbarLayout.getMeasuredWidth(), appbarHeight));
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            }
        });

        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());
                vpProductImage.setAlpha(1 - (offsetAlpha * -1));

                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    tvHeading.setText(data.getName());
                    ivBack.setImageResource(R.drawable.ic_back);
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.white));

                } else if (scrollRange + verticalOffset == scrollRange) {
                    isShow = false;
                    tvHeading.setText("");
//                    ivBack.setImageResource((R.drawable.ic_back_white));
                    ivBack.setImageResource(R.drawable.ic_back);
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparent));
                } else if (isShow) {
                    isShow = false;
                    tvHeading.setText("");
//                    ivBack.setImageResource((R.drawable.ic_back_white));
                    ivBack.setImageResource(R.drawable.ic_back);
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        setPDPView();
        setData();
        setRatingsData();
    }

    /**
     * setData for views
     */
    private void setData() {
        tvViewProfile.setVisibility(View.GONE);
        if (data != null) {
            if (data.getLayoutData() != null) {

                //view profile visible or not
                if (UIManager.getMerchantViewProfile() != 0) {
                    tvMerchantName.setText(getStrings(R.string.hosted_by));
                    tvMerchantName.append(" ");
                    Spannable spannable = new SpannableString(data.getStoreName());
                    spannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            if (!Utils.preventMultipleClicks()) {
                                return;
                            }

                            Intent intentVP = new Intent(mActivity, OwnerProfileActivity.class);
                            intentVP.putExtra(USER_ID, data.getUserId());
                            startActivity(intentVP);
                            AnimationUtils.forwardTransition(mActivity);

                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.btn_normal_bg_color));
                            ds.setUnderlineText(false);
                        }
                    }, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvMerchantName.append(spannable);
                    tvMerchantName.setMovementMethod(LinkMovementMethod.getInstance());


                } else {
                    tvMerchantName.setText(getStrings(R.string.hosted_by) + " " + data.getStoreName());
                }

                tvProductName.setText(data.getName());

                if (data.getDisplayAddress().isEmpty()) {
                    tvAddress.setVisibility(View.GONE);
                } else {
                    tvAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(data.getDisplayAddress());
                }

                if (data.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(data.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    tvPrice.setText(Utils.getCurrencySymbol(data.getPaymentSettings()) + Utils.getDoubleTwoDigits(data.getPrice().doubleValue()) + " " + getStrings(R.string.per) + " " +
                            Constants.ProductsUnitType.getUnitTypeText(mActivity, data.getUnit().intValue(), data.getUnitType(), false));
                } else {
                    tvPrice.setText(Utils.getCurrencySymbol(data.getPaymentSettings()) + Utils.getDoubleTwoDigits(data.getPrice().doubleValue()) + "");
                }


                tvDescription.setText(data.getDescription() + "");
                tvDescription.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tvDescription.getLineCount();
                        // Use lineCount here
                        if (tvDescription.getLineCount() > MAX_LINES) {
                            //added textview , Max Lines(you want to show at normal),and view more label true so that you can expand
                            ResizableCustomView.doResizeTextView(mActivity, tvDescription, MAX_LINES, getStrings(R.string.read_more), true);
                        }
                    }
                });


                if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {
                    Utils.setVisibility(View.GONE, llPriceAddRemove);
//                    Utils.setVisibility(View.VISIBLE, llPriceAddRemove);
                } else {
                    Utils.setVisibility(View.VISIBLE, llPriceAddRemove);
                    for (int i = 0; i < data.getLayoutData().getButtons().size(); i++) {
                        switch (i) {
                            case 0:
                                int i1 = Constants.NLevelButtonType.getButtonIdByValue(mActivity, data.getLayoutData().getButtons().get(0).getType());
                                if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue) {
                                } else if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                                    Utils.setVisibility(View.VISIBLE, linearLayoutQuantitySelector);
                                    linearLayoutQuantitySelector.setVisibility(data.getAvailableQuantity() == 0 && data.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                                    Utils.setVisibility(View.VISIBLE, tvSingleSelectionButton);
                                    tvSingleSelectionButton.setVisibility(data.getAvailableQuantity() == 0 && data.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                                    Utils.setVisibility(View.VISIBLE, ivForwardArrowButton);
                                    ivForwardArrowButton.setVisibility(data.getAvailableQuantity() == 0 && data.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                }
                                break;
                        }
                    }
                }

            }


            selectedQuantity = data.getSelectedQuantity();
            setQuantity(selectedQuantity);

            rlAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0 && Dependencies.getSelectedProductsArrayList().size() > 0 && !Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(data.getUserId())) {
                        new OptionsDialog.Builder(mActivity).message(getStrings(R.string.sure_to_select_this_offering_will_delete_previous_data).replace(getStrings(R.string.product), StorefrontCommonData.getTerminology().getProduct()).replace(getStrings(R.string.cart), StorefrontCommonData.getTerminology().getCart(false))).listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {
                                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                                addCartItem(data.getSelectedQuantity());
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                            }
                        }).build().show();
                    } else if (Dependencies.getSelectedProductsArrayList().size() > 0 && !Dependencies.getSelectedProductsArrayList().get(0).getProductId().equals(data.getProductId()) && data.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                        Utils.snackBar(mActivity, getStrings(R.string.can_avail_only_one_product).replace(getStrings(R.string.product), StorefrontCommonData.getTerminology().getProduct(false)));
                    } else {
                        StorefrontCommonData.getFormSettings().setUserId(data.getUserId());
                        addCartItem(data.getSelectedQuantity());
                    }

                    setQuantity(data.getSelectedQuantity());
                }
            });


            rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getItemSelectedList().size() > 1) {
                        Utils.showToast(mActivity, getStrings(R.string.please_remove_items_from_checkout).replace(getStrings(R.string.item), StorefrontCommonData.getTerminology().getItem(false)).replace(getStrings(R.string.checkout), StorefrontCommonData.getTerminology().getCheckout(false)).replace(getStrings(R.string.cart), StorefrontCommonData.getTerminology().getCart(false)));
                    } else {
                        if (data.getSelectedQuantity() <= 0) {
                            data.setSelectedQuantity(0);
                        } else {
                            data.setSelectedQuantity(data.getSelectedQuantity() - 1);
                        }
                        if (data.getItemSelectedList().size() == 1) {
                            data.getItemSelectedList().get(0).setQuantity(data.getSelectedQuantity());
                            if (data.getSelectedQuantity() == 0) {
                                data.getItemSelectedList().remove(0);
                            }
                        }

                        setQuantity(data.getSelectedQuantity());
//                        notifyItemChanged(position);
                        Dependencies.addCartItem(mActivity, data);
                        setTotalQuantity(true);

                    }
                }
            });

            tvSingleSelectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getSelectedQuantity() == 0) {
                        rlAddItem.performClick();
                    } else {
                        rlRemoveItem.performClick();
                    }
                }
            });

            tvOutOfStockText.setVisibility(data.getAvailableQuantity() == 0 && data.getInventoryEnabled() == 1 ? View.VISIBLE : View.GONE);


//            if (data.getMultiImageUrl() != null) {

            inflateIndicators(currentItem, llIndicators);

            if (data.getMultiImageUrl().size() == 0) {
                data.getMultiImageUrl().add("");
            }
            vpProductImage.setAdapter(new ViewImagesPDPAdapter(ProductDetailActivity.this, data.getMultiImageUrl()));
            vpProductImage.setCurrentItem(currentItem);
            vpProductImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    inflateIndicators(position, llIndicators);
                    currentItem = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

//            }

//            tvHeading.setText(data.getName());
            renderCustomFieldsPDPView1();

        }

    }

    /**
     * render custom fields
     */
    private void renderCustomFieldsPDPView1() {
        llPDPCustomFieldsNumber.removeAllViews();
        llPDPCustomFields.removeAllViews();
        if (data.getCustomFields() == null) {
            return;
        }
        ArrayList<CustomField> numberCustomFieldArrayList = new ArrayList<>();
        for (int i = 0; i < data.getCustomFields().size(); i++) {
            if (data.getCustomFields().get(i).getDataType().equals(Keys.DataType.NUMBER)) {
                numberCustomFieldArrayList.add(data.getCustomFields().get(i));
            }
        }
        if (numberCustomFieldArrayList.size() > 0) {
            View mView = new CustomFieldNumberPDP(this).render(mActivity, numberCustomFieldArrayList);
            if (mView != null) {
                llPDPCustomFieldsNumber.addView(mView);
            }
        }


        for (CustomField item : data.getCustomFields()) {
            View view = null;
            switch (item.getDataType()) {
//                    case Keys.DataType.NUMBER:
//                    case Keys.DataType.EMAIL:
//                    case Keys.DataType.TELEPHONE:
//                    case Keys.DataType.TEXT:
//                    case Keys.DataType.URL:
//                    case Keys.DataType.DATE:
//                    case Keys.DataType.DATE_FUTURE:
//                    case Keys.DataType.DATE_PAST:
//                    case Keys.DataType.DATE_TODAY:
//                    case Keys.DataType.DATETIME:
//                    case Keys.DataType.DATETIME_FUTURE:
//                    case Keys.DataType.DATETIME_PAST:

//                    case Keys.DataType.CHECKBOX:
//                    case Keys.DataType.DROP_DOWN:
//                        view = new CustomFieldCheckboxDelivery(this).render(item);
//                        break;
                case Keys.DataType.MULTI_SELECT:
                    view = new CustomFieldTextViewPDP(this, item.getDataType()).render(mActivity, item);
                    break;

                case Keys.DataType.TEXT_AREA:
                    view = new CustomFieldTextViewPDP(this, item.getDataType()).render(mActivity, item);
                    break;

                case Keys.DataType.NUMBER:
                    break;

                default:
                    view = new CustomFieldTextViewPDP(this, item.getDataType()).render(mActivity, item);
                    break;
            }
            if (view != null) {
                llPDPCustomFields.addView(view);
            }
        }
    }


    /**
     * set quantity of product
     */
    private void setQuantity(int selectedQuantity) {
//        selectedQuantity = data.getSelectedQuantity();
        if (selectedQuantity > 0) {
            textViewQuantity.setText(selectedQuantity + "");
            if (data.getLayoutData() != null && data.getLayoutData().getButtons().size() > 0) {
                int i1 = Constants.NLevelButtonType.getButtonIdByValue(mActivity, data.getLayoutData().getButtons().get(0).getType());
                if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                    rlAddItem.setVisibility(View.VISIBLE);
                    rlRemoveItem.setVisibility(View.VISIBLE);
                    textViewQuantity.setVisibility(View.VISIBLE);
                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                    try {
                        tvSingleSelectionButton.setText(data.getStorefrontData().getButtons().getButtonNames().getRemove());
                    } catch (Exception e) {

                               Utils.printStackTrace(e);
                    }
                }
            }

        } else {
            if (data.getLayoutData() != null && data.getLayoutData().getButtons().size() > 0) {
                int i1 = Constants.NLevelButtonType.getButtonIdByValue(mActivity, data.getLayoutData().getButtons().get(0).getType());
                if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                    rlAddItem.setVisibility(View.VISIBLE);
                    rlRemoveItem.setVisibility(View.GONE);
                    textViewQuantity.setVisibility(View.GONE);
                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                    try {
                        tvSingleSelectionButton.setText(data.getStorefrontData().getButtons().getButtonNames().getAdd());
                    } catch (Exception e) {

                               Utils.printStackTrace(e);
                    }
                }
            }
        }
    }


    /**
     * @param isVisible view visibility
     */
    public void setTotalQuantity(boolean isVisible) {
        if (isVisible && Dependencies.getCartSize() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                .getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.MULTI_PRODUCT) {
            rlTotalQuantity.setVisibility(View.VISIBLE);
        } else {
            rlTotalQuantity.setVisibility(View.GONE);
        }

        String checkoutString = "";
        checkoutString = (getStrings(R.string.view_cart) + " (" + Dependencies.getCartSize() + " " + (Dependencies.getCartSize() > 1 ? getStrings(R.string.items) : getStrings(R.string.item)) + ")");


        Spannable sb = new SpannableString(checkoutString);
//        sb.setSpan((Font.getSemiBold(getActivity())), checkoutString.indexOf(getString(R.string.checkout)), (getString(R.string.checkout).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
        sb.setSpan(new CustomTypefaceSpan("", Font.getSemiBold(ProductDetailActivity.this)), checkoutString.indexOf(getStrings(R.string.view_cart)), ((getStrings(R.string.view_cart)).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
        tvTotalQuantity.setText(sb);

        tvSubTotal.setText(Utils.getCurrencySymbol(data.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + "");
//
//  if (data.getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
//            tvMinOrderAmount.setVisibility(View.VISIBLE);
//            tvMinOrderAmount.setText(getString(R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + getString(R.string.minimum_order_amount) + " " + Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(data.getStorefrontData().getMerchantMinimumOrder()));
//        } else {
//            tvMinOrderAmount.setVisibility(View.GONE);
//        }
    }


    /**
     * Method to inflate the indicator for the
     */
    private void inflateIndicators(int position, LinearLayout llIndicators) {
        if (data.getMultiImageUrl().size() <= 1) {
            llIndicators.setVisibility(View.GONE);
        } else {
            llIndicators.setVisibility(View.VISIBLE);
            llIndicators.removeAllViews();

            int pixels = Utils.convertDpToPixels(mActivity, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);

            for (int index = 0; index < data.getMultiImageUrl().size(); index++) {
                View view = new View(mActivity);
                view.setLayoutParams(params);
                view.setBackgroundResource(index == position ? R.drawable.switcher_filled : R.drawable.switcher);
                llIndicators.addView(view);
            }
        }
    }


    private void addCartItem(int selectedQuantity) {
        if (data.getMaxProductquantity() == 0 || data.getSelectedQuantity() < data.getMaxProductquantity()) {
                if (data.getInventoryEnabled() == 0 || (data.getSelectedQuantity() < data.getAvailableQuantity() && data.getInventoryEnabled() == 1)) {

                    if (data.getCustomizeItem().size() > 0) {
                        setItemCustomisation();
                        return;
                    }

                    if (selectedQuantity == 0 && data.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {

                        if (data.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                && (Constants.ProductsUnitType.getUnitType(data.getUnitType()) == Constants.ProductsUnitType.FIXED)
                                && data.getEnableTookanAgent() == 0) {

                            openDatePicker();

                        } else {
                            if (data.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                    && (Constants.ProductsUnitType.getUnitType(data.getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                                    && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {

                                Intent intent = new Intent(mActivity, DatesOnCalendarActivity.class);
                                intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                                intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, data);
                                intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                                intent.putExtra(Keys.Extras.IS_START_TIME, true);
                                intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            } else {
                                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                                intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                                intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, data);
                                intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                                intent.putExtra(Keys.Extras.IS_START_TIME, true);
                                intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            }
                        }
                    } else {
                        addToCart(selectedQuantity);
                    }
                } else {
                    Utils.showToast(ProductDetailActivity.this, getStrings(R.string.order_quantity_limited));
                }
            }else{
            String message = (StorefrontCommonData.getString(this, R.string.maximum_quantity_error))
                    .replace(NAME, data.getName())
                    .replace(QUANTITY, data.getMaxProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
            Utils.snackBar(this, message);

        }
    }

    @Override
    public void onClick(View view) {
//        if (!Utils.preventMultipleClicks()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.vpProductImage:
                ImageVideoScreenDialog viewImagesDialogProducts = new ImageVideoScreenDialog(data, mActivity);
                viewImagesDialogProducts.show(getSupportFragmentManager(), "");

//                new ViewImagesDialogProducts.Builder(mActivity)
//                        .images(data.getMultiImageUrl())
//                        .videos(data.getMultiVideoUrl())
//                        .title(data.getName()).position(0).build().show();
                break;


            case R.id.tvProceed:
                if (data.getCustomizeItem().size() > 0) {
                    Intent CustomizeIntent = new Intent(mActivity, ProductCustomisationActivity.class);
                    CustomizeIntent.putExtra(KEY_ITEM_POSITION, 0);
                    CustomizeIntent.putExtra(PRODUCT_CATALOGUE_DATA, data);
                    startActivityForResult(CustomizeIntent, OPEN_CUSTOMISATION_ACTIVITY);
                } else {
                    if (data.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                            && (Constants.ProductsUnitType.getUnitType(data.getUnitType()) == Constants.ProductsUnitType.FIXED)
                            && data.getEnableTookanAgent() == 0) {

                        openDatePicker();

                    } else {
                        if (data.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && (Constants.ProductsUnitType.getUnitType(data.getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                                && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {

                            Intent intent = new Intent(mActivity, DatesOnCalendarActivity.class);
                            intent.putExtra(KEY_ITEM_POSITION, 0);
                            intent.putExtra(PRODUCT_CATALOGUE_DATA, data);
                            intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(IS_START_TIME, true);
                            intent.putExtra(SELECTED_DATE, "");
                            startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        } else {
                            Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                            intent.putExtra(KEY_ITEM_POSITION, 0);
                            intent.putExtra(PRODUCT_CATALOGUE_DATA, data);
                            intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(IS_START_TIME, true);
                            intent.putExtra(SELECTED_DATE, "");
                            startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        }
                    }
                }
                break;

//            case R.id.tvProceed:
//                if (data.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
//                        && (Constants.ProductsUnitType.getUnitType(data.getUnitType()) == Constants.ProductsUnitType.FIXED)
//                        && data.getEnableTookanAgent() == 0) {
//
//                    openDatePicker();
//
//                } else {
//                    Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
//                    intent.putExtra(KEY_ITEM_POSITION, 0);
//                    intent.putExtra(PRODUCT_CATALOGUE_DATA, data);
//                    intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
//                    intent.putExtra(IS_START_TIME, true);
//                    intent.putExtra(SELECTED_DATE, "");
//                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
//                }
//                break;

            case R.id.rlBack:
                onBackPressed();
                break;

            case R.id.rlTotalQuantity:
                if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
                    if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
                        Utils.snackBar(mActivity, getStrings(R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + getStrings(R.string.minimum_order_amount_is) + " " + Utils.getCurrencySymbol(data.getPaymentSettings()) + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder().intValue()));
                        return;
                    }
                } else {
                    if (data.getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
                        Utils.snackBar(mActivity, getStrings(R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + getStrings(R.string.minimum_order_amount_is) + " " + Utils.getCurrencySymbol(data.getPaymentSettings()) + Utils.getDoubleTwoDigits(data.getStorefrontData().getMerchantMinimumOrder().intValue()));
                        return;
                    }
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
                        new AlertDialog.Builder(mActivity).message(getStrings(R.string.choose_products_for_proceeding).replace(getStrings(R.string.product), StorefrontCommonData.getTerminology().getProduct())).build().show();
                        return;
                    }

                    Bundle extraa = new Bundle();
                    extraa.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                    extraa.putSerializable(STOREFRONT_DATA, data.getStorefrontData());
                    extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                    extraa.putString(PICKUP_ADDRESS, pickupAddress);
                    Transition.openCheckoutActivity(mActivity, extraa);
                }
                break;

            case R.id.llCall:
                Utils.openCallDialer(mActivity, data.getPhone());
                break;
            case R.id.llContactUs2:
//                if (UIManager.isFuguChatEnabled()) {
//                    Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
//                    FuguConfig.getInstance().showConversations(this, getString(R.string.support));
//                }
                break;
            case R.id.llLocateUs2:
//                try {
//                    Utils.openGoogleMapsApp(this, new LatLng(data.getLatitude(), data.getLongitude()));
//                } catch (Exception e) {
//                    Utils.openGoogleMapsApp(this, new LatLng(0.0, 0.0));
//                }

                Intent intent = new Intent(mActivity, ShowProductOnMapActivity.class);
                intent.putExtra(PICKUP_LATITUDE, data.getLatitude());
                intent.putExtra(PICKUP_LONGITUDE, data.getLongitude());
                startActivityForResult(intent, OPEN_SHOW_PRODUCT_ON_MAP_SCREEN);
                break;

            case R.id.tvViewProfile:
                Intent intentVP = new Intent(mActivity, OwnerProfileActivity.class);
                intentVP.putExtra(USER_ID, data.getUserId());
                startActivity(intentVP);
                break;

            case R.id.tvEditReview:
                Intent intentEditReview = new Intent(mActivity, MerchantAddRateReviewActivity.class);
                intentEditReview.putExtra(SHOW_RATE_STARS, data.getMyRating());
                intentEditReview.putExtra(PRODUCT_DATA, data);
                startActivityForResult(intentEditReview, OPEN_ADD_MERCHANT_RATE_REVIEW);
                break;
            case R.id.tvSeeAllReviews:
                Intent reviewIntent = new Intent(mActivity, MerchantReviewsActivity.class);
                reviewIntent.putExtra(PRODUCT_DATA, data);
                startActivityForResult(reviewIntent, OPEN_ALL_MERCHANT_RATE_REVIEW);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        extras.putSerializable(PRODUCT_DATA, data);
        extras.putSerializable(KEY_ITEM_POSITION, itemPos);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(this, tvSubTotal);
        isRateTouch = true;
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
                if (resultCode == Activity.RESULT_OK) {
                    setTotalQuantity(true);
                    activityResultCheckoutScreen();
                }

                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                        setResult(RESULT_OK, data);
                        Transition.exit(mActivity);
                    }
                }

                break;

            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    Transition.openCheckoutActivity(mActivity, data.getExtras());
                } else {
                    if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                            Dependencies.clearSelectedProductArraylist();
                            this.data.setSelectedQuantity(0);
                            setTotalQuantity(true);
                            this.data.setItemSelectedList(new ArrayList<ItemSelected>());
                        }
                    }
                }
                break;


            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;

                    Datum productDataItem = null;

//                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
//                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
//                    }
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }


                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity(true);
                    activityResultCheckoutScreen();

                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }

//                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
//                        rlTotalQuantity.performClick();
//                    }

                }

                break;

            case Codes.Request.OPEN_CUSTOMISATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;

                    Datum productDataItem = null;

//                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
//                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
//                    }
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }


                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity(true);
                    activityResultCheckoutScreen();

                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }

//                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
//                        rlTotalQuantity.performClick();
//                    }
                }
                break;
            case OPEN_ADD_MERCHANT_RATE_REVIEW:
            case OPEN_ALL_MERCHANT_RATE_REVIEW:
               if (resultCode == RESULT_OK) {
                    this.data = (Datum) data.getExtras().getSerializable(PRODUCT_DATA);
                }
                setRatingsData();
                break;
        }
    }

    /**
     * activityResult for Checkout screen
     */
    private void activityResultCheckoutScreen() {
        if (data != null) {
            if (Dependencies.getSelectedProductsArrayList().size() == 0) {
                data.setSelectedQuantity(0);
                data.setItemSelectedList(new ArrayList<ItemSelected>());

            }

            for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                if (data.getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                    data.setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                    data.setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                    data.setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());
                    data.setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                    data.setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());

                    for (int k = 0; k < data.getItemSelectedList().size(); k++) {
                        if (data.getItemSelectedList().get(k).getQuantity() == 0) {
                            data.getItemSelectedList().remove(k);
                            k--;
                        }
                    }

                }
            }

            setQuantity(data.getSelectedQuantity());

        }
    }


    private void addToCart(int selectedQuantity) {

        selectedQuantity++;
        data.setSelectedQuantity(selectedQuantity);
//                    tvProductQuantity.setText(selectedQuantity[0]+"");
        Dependencies.addCartItem(mActivity, data);
        setTotalQuantity(true);
        if (data.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && data.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
            Bundle extraa = new Bundle();
            extraa.putSerializable(STOREFRONT_DATA, data.getStorefrontData());
//            if (activity instanceof NLevelWorkFlowActivity) {
//                extraa.putDouble(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
//                extraa.putDouble(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
//                extraa.putString(PICKUP_ADDRESS, ((NLevelWorkFlowActivity) activity).pickAddress);
//            }
            Transition.openCheckoutActivity(mActivity, extraa);
        }

    }

    private void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "Date Picker");
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
        timePickerFragment.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isValidTime(startDate + " " + hourOfDay + ":" + minute)) {
            startDate = startDate + " " + hourOfDay + ":" + minute;
            Date productStartDate = DateUtils.getInstance().getDateFromString(startDate, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC);
            data.setProductStartDate(productStartDate);
            data.setProductEndDate(productStartDate);

            int selectedQuantity = data.getSelectedQuantity();

            addToCart(selectedQuantity);

        } else {
            Utils.snackBar(mActivity, getStrings(R.string.invalid_selected_date));
        }
    }


    private boolean isValidTime(String date) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();

            if (DateUtils.getInstance().getDateFromString(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC).getTime() < calendar.getTime().getTime())
                isValidDate = false;
        }
        return isValidDate;
    }


    private void setItemCustomisation() {
        Intent intent = new Intent(mActivity, ProductCustomisationActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, 0);
        intent.putExtra(PICKUP_LATITUDE, pickupLatitude);
        intent.putExtra(PICKUP_LONGITUDE, pickupLongitude);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, data);
        startActivityForResult(intent, OPEN_CUSTOMISATION_ACTIVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTotalQuantity(true);
        getProductRateReviewData();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_light));
//        Location location = LocationUtils.getLastLocation(this);
//        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(currentLatlng).zoom(MAX_ZOOM).tilt(41.25f).build()));
//        mapInProgress = false;


        mMap.clear();

        if (data.getLatitude() != null && data.getLongitude() != null) {
            LatLng latLng = new LatLng(data.getLatitude(), data.getLongitude());

            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmap(mActivity, R.drawable.ic_icon_pin_location))));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAX_ZOOM), 1000, null);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(mActivity, ShowProductOnMapActivity.class);
                intent.putExtra(PICKUP_LATITUDE, data.getLatitude());
                intent.putExtra(PICKUP_LONGITUDE, data.getLongitude());
                startActivityForResult(intent, OPEN_SHOW_PRODUCT_ON_MAP_SCREEN);
            }
        });

    }

    public void setRatingsData() {
        if (data.getStorefrontData().getIsReviewRatingEnabled() == 1) {

            if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) && data.getProductRating().floatValue() <= 0) {
                llRateReviewTopLayout.setVisibility(View.GONE);
                llAverageRatingLayout.setVisibility(View.GONE);
            } else {
                llRateReviewTopLayout.setVisibility(View.VISIBLE);

                if (data.getProductRating().floatValue() <= 0) {
                    llAverageRatingLayout.setVisibility(View.GONE);
                } else {
                    llAverageRatingLayout.setVisibility(View.VISIBLE);
                    Utils.addStarsToLayout(mActivity, llAverageRatingLayout, data.getProductRating().doubleValue(), R.dimen._17dp);
                }

                if (data.getMyRating().intValue() > 0) {
                    llAverageMerchantRatings.setVisibility(View.GONE);
                    llEditMerchantRatings.setVisibility(View.VISIBLE);

                    tvSelfRating.setText(data.getMyRating().floatValue() + "");
                    Utils.addStarsToLayout(mActivity, llSelfRatingLayout, data.getMyRating().doubleValue(), R.dimen._15dp);

                } else {

                    if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                        llAverageMerchantRatings.setVisibility(View.GONE);
                    } else {
                        llAverageMerchantRatings.setVisibility(View.VISIBLE);
                    }

                    llEditMerchantRatings.setVisibility(View.GONE);
                    llAverageRating.setVisibility(View.VISIBLE);
                    Utils.addSingleStarToLayout(mActivity, llAverageRatingStar, data.getProductRating().doubleValue(), R.dimen._20dp);
                    tvAverageRating.setText(data.getProductRating().floatValue() + "");
                    if (data.getTotalRatingsCount() > 0 && data.getTotalReviewCount() > 0) {
                        tvTotalRateReviews.setText(data.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text)
                                + " " + getStrings(R.string.and) + " "
                                + data.getTotalReviewCount() + " " + getStrings(R.string.reviews_text));
                    } else if (data.getTotalRatingsCount() > 0) {
                        tvTotalRateReviews.setText(data.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text));
                    } else if (data.getTotalReviewCount() > 0) {
                        tvTotalRateReviews.setText(data.getTotalReviewCount() + " " + getStrings(R.string.reviews_text));
                    } else {
                        tvTotalRateReviews.setText(getStrings(R.string.no_rate_reviews_be_first_one));
                        llAverageRating.setVisibility(View.GONE);
                    }

                    tvRateMerchantText.setText(getStrings(R.string.rate_storename).replace(NAME, data.getName()));
                    Utils.addStarsToLayout(mActivity, llRateMerchantEmpty, 0.0, R.dimen._20dp);


                    for (int i = 0; i < llRateMerchantEmpty.getChildCount(); i++) {

                        llRateMerchantEmpty.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.e("llrateMErchantempty", "<<" + v.getId());
                                if (isRateTouch) {
                                    isRateTouch = false;

                                    Intent intent = new Intent(mActivity, MerchantAddRateReviewActivity.class);
                                    intent.putExtra(SHOW_RATE_STARS, 5f);
                                    intent.putExtra(PRODUCT_DATA, data);
                                    startActivityForResult(intent, OPEN_ADD_MERCHANT_RATE_REVIEW);
                                }
                            }
                        });
                    }

                }

                if (data.getLastReviewRating().size() > 0) {
                    rvReviewsList.setVisibility(View.VISIBLE);

                    if (data.getTotalReviewCount() > 0) {
                        tvSeeAllReviews.setVisibility(View.VISIBLE);
                        tvSeeAllReviews.setText(getStrings(R.string.see_all_reviews) + " (" + data.getTotalReviewCount() + ")");
                    } else {
                        tvSeeAllReviews.setVisibility(View.GONE);
                    }

                    merchantReviewsList.clear();
                    merchantReviewsList.addAll(data.getLastReviewRating());
                    merchantReviewsAdapter.notifyDataSetChanged();

                } else {
                    rvReviewsList.setVisibility(View.GONE);
                    tvSeeAllReviews.setVisibility(View.GONE);
                }

            }
        } else {
            llRateReviewTopLayout.setVisibility(View.GONE);
            llAverageRatingLayout.setVisibility(View.GONE);
        }

        rvReviewsList.post(new Runnable() {
            @Override
            public void run() {
                (findViewById(R.id.nsvScrollBar)).scrollTo(0, 0);
            }
        });

        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            llGoToWriteReview.setVisibility(View.VISIBLE);
        } else {
            llGoToWriteReview.setVisibility(View.GONE);
        }

    }

    public void getProductRateReviewData() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("product_id", data.getProductId());

        RestClient.getApiInterface(mActivity).getProductLastReviews(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                try {
                    Datum datum = baseModel.toResponseModel(Datum.class);
                    data.setLastReviewRating(datum.getLastReviewRating());
                    data.setTotalReviewCount(datum.getTotalReviewCount());
                    data.setTotalRatingsCount(datum.getTotalRatingsCount());
                    data.setMyReview(datum.getMyReview());
                    data.setMyRating(datum.getMyRating());
                    data.setProductRating(datum.getProductRating());

                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }

                setRatingsData();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

}
