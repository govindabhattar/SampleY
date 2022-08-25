package com.tookancustomer.mapfiles.placeapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.appConfiguration.MapObject;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver_;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Constants.SEARCH_INTERVAL;


public class PlaceSearchActivity extends BaseActivity implements PlaceSearchAdapter.OnItemListener, TextWatcher, View.OnClickListener {
    private RecyclerView rvSearchList;
    private PlaceSearchAdapter placeSearchAdapter;
    private ArrayList<Predictions> predictionList;
    private AppCompatEditText etSearch;
    private AppCompatImageButton ivDelete;
    private AppCompatImageView ivPoweredBy;
    private LinearLayout llSearch;
    private RelativeLayout rlNoSearchResult;
    private TextView tvNoSearchResults;
    private String lastSearch = "";
    private Handler searchHandler;
    private Runnable searchRunnable;
    private View vLogoSeperator;
    private LinearLayout llAddressListParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        initViews();
    }

    private void initViews() {
        predictionList = new ArrayList<>();
        rvSearchList = findViewById(R.id.rvSearchList);
        etSearch = findViewById(R.id.etSearch);
        etSearch.requestFocus();
        etSearch.setHint(getStrings(R.string.search));
        ivDelete = findViewById(R.id.ivDelete);
        ivPoweredBy = findViewById(R.id.ivPoweredBy);
        ivPoweredBy.setVisibility(BuildConfig.isYeloDemoApp ? View.VISIBLE : View.GONE);
        llSearch = findViewById(R.id.llSearch);
        rlNoSearchResult = findViewById(R.id.rlNoSearchResult);
        tvNoSearchResults = findViewById(R.id.tvNoSearchResults);
        vLogoSeperator = findViewById(R.id.vLogoSeperator);
        llAddressListParent = findViewById(R.id.llAddressListParent);
        vLogoSeperator.setVisibility(View.GONE);
        rvSearchList.setVisibility(View.VISIBLE);
        rlNoSearchResult.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        etSearch.addTextChangedListener(this);
        Utils.setOnClickListener(this, ivDelete, findViewById(R.id.llBack));
        placeSearchAdapter = new PlaceSearchAdapter(this, predictionList);
        rvSearchList.setLayoutManager(new LinearLayoutManager(this));
        rvSearchList.setAdapter(placeSearchAdapter);
        searchRunnable = new Runnable() {
            @Override
            public void run() {
                lastSearch = Utils.get(etSearch);
                onSearchUpdateFlightMAp(lastSearch);
            }
        };
        searchHandler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showSoftKeyboard(PlaceSearchActivity.this);
                    }
                });
            }
        }, 250);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        Transition.exit(this);
    }

    @Override
    public void onAddressSelected(Predictions value) {
        if (Utils.hasData(value.getPlaceID())) {
            getPlaceDetails(value);
        } else {
            onSearchPicked(value);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    private void onSearchUpdateFlightMAp(final String searchKeyword) {
        if (isInternetAndMapKeyNotAvailable()) {
            return;
        }
        MapObject mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();


        final CommonParams.Builder commonParams;
        commonParams = new CommonParams.Builder()
                .add("text", searchKeyword)
                .add("currentlatitude", LocationUtils.LATITUDE)
                .add("currentlongitude", LocationUtils.LONGITUDE)
                .add("offering", 2)
                .add("radius", 1000)
                .add("skip_cache", mapObject.getSkipCache())
                .add("type", "android")
                .add("fm_token", mapObject.getAndroidMapApiKey())
                .add("options", mapObject.getMapType());
        commonParams.add("user_unique_key", StorefrontCommonData.getAppConfigurationData().getUserId());


        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP) {
            commonParams.remove("fm_token");
            addGoogleKey(commonParams);
        }

        llSearch.setVisibility(View.VISIBLE);
        rvSearchList.setVisibility(View.VISIBLE);
        llAddressListParent.setVisibility(View.VISIBLE);
        rlNoSearchResult.setVisibility(View.GONE);
        vLogoSeperator.setVisibility(View.GONE);

        RestClient.getJungleMapsApi().getAddressFromFlightMapSearchApi(commonParams.build().getMap()).enqueue(
                new ResponseResolver_<AutoComplete>(this, false, true) {
                    @Override
                    public void success(AutoComplete autoComplete) {
                        llSearch.setVisibility(View.GONE);
                        try {
                            ArrayList<Predictions> predictions = autoComplete.getPredictions();
                            if (Utils.hasData(predictions)) {
                                onListRefreshed(predictions);
                            } else {
                                rvSearchList.setVisibility(View.GONE);
                                llAddressListParent.setVisibility(View.GONE);
                                rlNoSearchResult.setVisibility(View.VISIBLE);
                                tvNoSearchResults.setText(String.format("%s %s", getString(R.string.no_results_for), searchKeyword));
                                onListRefreshed(null);
                            }
                        } catch (Exception e) {
                            Utils.printStackTrace(e);
                            Utils.snackBar(PlaceSearchActivity.this, getString(R.string.some_error_occurred));
                        }
                    }

                    @Override
                    public void failure(APIError error, AutoComplete autoComplete) {
                        llSearch.setVisibility(View.GONE);
                        rvSearchList.setVisibility(View.GONE);
                        Utils.snackBar(PlaceSearchActivity.this, error.getMessage());
                        onListRefreshed(null);
                    }


                });


    }

    private void onListRefreshed(ArrayList<Predictions> predictionList) {
        this.predictionList = predictionList;
        vLogoSeperator.setVisibility(predictionList == null || predictionList.isEmpty() ? View.GONE : View.VISIBLE);
        ivDelete.setVisibility(predictionList == null || predictionList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        if (placeSearchAdapter == null) {
            placeSearchAdapter = new PlaceSearchAdapter(this, predictionList);
            rvSearchList.setAdapter(placeSearchAdapter);
        } else {
            placeSearchAdapter.refreshAdapterDataSet(predictionList);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchHandler.removeCallbacks(searchRunnable);
        if (s.toString().length() > 2) {
            if (!lastSearch.equalsIgnoreCase(Utils.get(etSearch))) {
                if (searchHandler != null && searchRunnable != null) {
                    searchHandler.postDelayed(searchRunnable, SEARCH_INTERVAL);
                }
            }
        } else if (s.toString().isEmpty()) {
            onListRefreshed(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                onDeleteSearch();
                break;
            case R.id.llBack:
                onBackPressed();
                break;
        }
    }

    private void onDeleteSearch() {
        vLogoSeperator.setVisibility(View.GONE);
        etSearch.setText(Constants.EMPTY_STRING);
        onListRefreshed(null);
    }

    private boolean isInternetAndMapKeyNotAvailable() {
        if (!Utils.internetCheck(this)) {
            Utils.snackBar(this, getStrings(R.string.not_connected_to_internet_text));
            return true;
        }

        return false;
    }

    private void getPlaceDetails(final Predictions value) {
        if (isInternetAndMapKeyNotAvailable()) {
            return;
        }
        MapObject mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.hideSoftKeyboard(PlaceSearchActivity.this, etSearch);
            }
        }, 200);


        final CommonParams.Builder commonParams;
        commonParams = new CommonParams.Builder()
//                .add("priority",mapObject.getMapType())
                .add("placeId", value.getPlaceID())
                .add("offering", 2)
                .add("type", "android")
                .add("currentlatitude", LocationUtils.LATITUDE)
                .add("currentlongitude", LocationUtils.LONGITUDE);
        commonParams.add("user_unique_key", StorefrontCommonData.getAppConfigurationData().getUserId());


        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP)
            commonParams.add("api_key", mapObject.getGoogleApiKey());


        if (mapObject.getMapType() == Constants.MapConstants.FLIGHTMAP)
            commonParams.add("fm_token", mapObject.getAndroidMapApiKey());
        llSearch.setVisibility(View.VISIBLE);

        RestClient.getJungleMapsApi().getPlaceDetailsFlightMapFromPlaceID(commonParams.build().getMap())
                .enqueue(new ResponseResolver_<PlaceDetails>(this, false, true) {
                    @Override
                    public void success(PlaceDetails baseModel) {
                        value.setLatLng(baseModel.getDataObject().getResults().get(0).getGeometry().getLocation().getLatLng());
                        onSearchPicked(value);
                    }

                    @Override
                    public void failure(APIError error, PlaceDetails placeDetails) {
                        llSearch.setVisibility(View.GONE);
                        onListRefreshed(null);
                    }


                });

    }

    private void addGoogleKey(CommonParams.Builder commonParams) {
        MapObject mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();
        if (mapObject == null) {
            return;
        }
        commonParams.add("api_key", mapObject.getGoogleApiKey());
    }

    private void onSearchPicked(Predictions prediction) {
        llSearch.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        String address = prediction.getAddress();
//        if (Utils.hasData(address) && !address.contains(prediction.getName())) {
//            address = prediction.getName() + ", " + address;
//        }

        bundle.putParcelable(Location.class.getName(), prediction.getLocation());
        bundle.putString(Keys.Extras.ADDRESS, address);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        Transition.exit(PlaceSearchActivity.this);
    }
}
