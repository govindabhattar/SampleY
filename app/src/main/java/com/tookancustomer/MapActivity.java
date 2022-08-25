package com.tookancustomer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tookancustomer.adapters.MarketplaceRestaurantListAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapStateListener;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.mapfiles.TouchableMapFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SimpleDividerItemDecorationFull;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.tookancustomer.appdata.Constants.MERCHANT_PAGINATION_LIMIT;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, LocationFetcher.OnLocationChangedListener
        , View.OnClickListener {
    RelativeLayout rlBack;
    Button btNext;
    boolean updateAddress;
    private RelativeLayout rlMyLocation;
    private GoogleMap mMap;
    private LocationFetcher locationFetcher;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    //    private final int MAX_ZOOM = 14;
    private final int MAX_ZOOM = 13;
    private boolean isFirstLocation;
    int otherListSize;
    //    boolean mapInProgress;
    private MarketplaceRestaurantListAdapter adapter;
    private RecyclerView rvItemList;
    private CityStorefrontsModel cityStorefrontsModel;
    private TextView tvHeading, tvAddress;
    private LinearLayout llNoStoresAvailable;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mActivity = this;
        Bundle bundle = getIntent().getExtras();

//        userData = StorefrontCommonData.getUserData();

        cityStorefrontsModel = (CityStorefrontsModel) bundle.getSerializable("cityStorefrontsModel");
        if (getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE) != null) {
            Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
        } else if (getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE) != null) {
            Utils.snackBar(mActivity, getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE));
        }
        startLocationFetcher();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializeFields();

        Location location = LocationUtils.getLastLocation(this);
        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = currentLatlng.latitude;
        longitude = currentLatlng.longitude;
    }


    private void initializeFields() {
        rlBack = findViewById(R.id.rlBack);
        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setHint(StorefrontCommonData.getTerminology().getAddress());
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.find_out_on_map));
        rlMyLocation = findViewById(R.id.rlMyLocation);
        llNoStoresAvailable = findViewById(R.id.llNoStoresAvailable);

        rvItemList = findViewById(R.id.rvItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvItemList.setItemAnimator(new DefaultItemAnimator());
        rvItemList.setHasFixedSize(false);
        rvItemList.addItemDecoration(new SimpleDividerItemDecorationFull(mActivity));

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvItemList);

        if (cityStorefrontsModel.getData() != null && cityStorefrontsModel.getData().size() != 0) {
            llNoStoresAvailable.setVisibility(View.GONE);
            rvItemList.setVisibility(View.VISIBLE);
            adapter = new MarketplaceRestaurantListAdapter(mActivity, cityStorefrontsModel.getData());
            rvItemList.setAdapter(adapter);
        } else {
            llNoStoresAvailable.setVisibility(View.VISIBLE);
            rvItemList.setVisibility(View.GONE);
        }
        ((TextView)findViewById(R.id.tvNoStorefrontFound)).setText(getStrings(R.string.no_restaurants_found_please_change_your_location));
        Utils.setOnClickListener(this, rlBack, rlMyLocation, tvAddress);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mapInProgress = true;
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        TouchableMapFragment mapFragment = ((TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (checkLocationPermissions()) {
            try {
                this.mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {

                               Utils.printStackTrace(e);
            }
        }
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_light));
        Location location = LocationUtils.getLastLocation(this);
        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(currentLatlng).zoom(MAX_ZOOM).tilt(41.25f).build()));
//        mapInProgress = false;


        mMap.clear();
        for (int i = 0; i < cityStorefrontsModel.getData().size(); i++) {
            LatLng latLng = new LatLng(Double.parseDouble(cityStorefrontsModel.getData().get(i).getLatitude())
                    , Double.parseDouble(cityStorefrontsModel.getData().get(i).getLongitude()));

            Log.e("latlong<><>", latLng + "");


            if (cityStorefrontsModel.getData().get(i).getLogo() != null) {
                MarkerTask markerTask = new MarkerTask(i, cityStorefrontsModel.getData().get(i).getLogo(),
                        latLng, cityStorefrontsModel.getData().get(i).getStoreName(),
                        cityStorefrontsModel.getData().get(i).getPhone());
                markerTask.execute();
            } else {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(cityStorefrontsModel.getData().get(i).getStoreName())
                        .snippet(cityStorefrontsModel.getData().get(i).getPhone())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));
                mHashMap.put(marker, i);


            }

//            mHashMap.put(marker, i);
        }


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int pos = mHashMap.get(marker);
                Log.i("Position of arraylist", pos + "");
                openRestaurantActivity(pos);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int pos = mHashMap.get(marker);
                Log.i("Position of arraylist", pos + "");
                rvItemList.getLayoutManager().scrollToPosition(pos);

                return false;
            }
        });


        new MapStateListener(mMap, mapFragment, mActivity) {
            @Override
            public void onMapTouched() {// Map touched
//                mapInProgress = true;
            }

            @Override
            public void onMapReleased() {// Map released
                Log.e("Map released", "===");
            }

            @Override
            public void onMapUnsettled() {
                Log.e("Map unsettled", "===");

            }

            @Override
            public void onMapSettled() {

                Log.e("Map settled", "===");
//                checkForMyLocationButtonVisibility();
//                if (updateAddress) {
//                    LatLng latLng = mMap.getCameraPosition().target;
//                    Double distanceCalculated = Utils.getDistance(latitude, longitude, latLng.latitude, latLng.longitude);
//
//                    Log.e("Distance calculated map ", "<<<<" + distanceCalculated);
//                }
//                updateAddress = true;
//                mMap.getUiSettings().setScrollGesturesEnabled(true);
            }
        };
    }


    private void openRestaurantActivity(int adapterPos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(STOREFRONT_DATA, cityStorefrontsModel.getData().get(adapterPos));
        bundle.putInt(STOREFRONT_DATA_ITEM_POS, adapterPos);

//        if (activity instanceof RestaurantListingActivity) {
//            bundle.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) activity).latitude);
//            bundle.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) activity).longitude);
//            bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) activity).tvAddress.getText().toString());
//        }

        StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(cityStorefrontsModel.getData().get(adapterPos).getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setUserId(cityStorefrontsModel.getData().get(adapterPos).getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModel.getData().get(adapterPos).getMerchantMinimumOrder());
        Intent intent = new Intent(MapActivity.this, HomeActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPEN_HOME_ACTIVITY);
    }


    /**
     * Bitmap converter Async Task
     */
    private class MarkerTask extends AsyncTask<Void, Void, Void> {

        private Bitmap bmp;
        private LatLng latLng;
        private String mUrl;
        private String name;
        private String phoneNumber;
        private int pos;

        /**
         * @param url    url that has to be converter
         * @param latLng latLng where to show on map
         */
        public MarkerTask(final int pos, final String url, final LatLng latLng, final String name, final String phoneNumber) {
            this.latLng = latLng;
            this.mUrl = url;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(final Void... params) {
            URL url;
            try {
                url = new URL(mUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            int height = 45;
            int width = 45;
            Bitmap smallMarker = Bitmap.createScaledBitmap(bmp, width, height, false);
            marker = mMap.addMarker(new MarkerOptions()
                    .title(name)
                    .snippet(phoneNumber)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            mHashMap.put(marker, pos);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity, findViewById(R.id.rlBack));
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            /*case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    //tvLocation.setText(place.getAddress() + "");

                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    moveCameraToLatLng(place.getLatLng());
//                    executeSetAddress();

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    // TODO: Handle the error.

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;*/

            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    tvAddress.setText(data.getStringExtra("address"));
                    latitude = data.getDoubleExtra("latitude", 0.0);
                    longitude = data.getDoubleExtra("longitude", 0.0);
                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(MAX_ZOOM);
                    builder.target(new LatLng(latitude, longitude));
                    if (mMap != null)
                        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
//                    moveCameraToLatLng(new LatLng(latitude, longitude));
                    getMarketplaceStorefronts();
                }
                break;


        }
    }

    /*private void checkForMyLocationButtonVisibility() {
        try {
            if (MapUtils.distance(getCurrentLocation(), mMap.getCameraPosition().target) > MAP_CURRENT_DISTANCE_CHECK) {
                rlMyLocation.setVisibility(View.VISIBLE);
            } else {
                rlMyLocation.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            rlMyLocation.setVisibility(View.VISIBLE);
        }
    }*/

    @Override
    public void onLocationChanged(Location location, int priority) {
        if (location == null) {
            return;
        }

        // Check if no location is fetched
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }

        LocationUtils.saveLocation(this, location);
        if (mMap != null) {
            if (checkLocationPermissions()) {
                try {
                    this.mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {

                               Utils.printStackTrace(e);
                }
            }
//            if (isFirstLocation) {
//                isFirstLocation = false;
//                LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
//                if (getIntent().getStringExtra("address") == null)
//                    updateMap(currentLatlng);
//            }
        }
    }

    /**
     * Method to start the TrackingData Fetcher
     */
    private void startLocationFetcher() {
        if (!checkLocationPermissions()) {
            return;
        }
        if (!LocationUtils.isGPSEnabled(this)) {
            LocationAccess.showImproveAccuracyDialog(this);
            return;
        }
        // Start fetching the location
        if (locationFetcher == null) {
            isFirstLocation = true;
            locationFetcher = new LocationFetcher(this, Constants.TimeRange.LOCATION_FETCH_INTERVAL, Constants.LocationPriority.BEST);
        }
        locationFetcher.connect();

    }

    /**
     * Method to check whether the TrackingData Permission
     * is Granted by the User
     */
    private boolean checkLocationPermissions() {

        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};

        return AppManager.getInstance().askUserToGrantPermission(this, permissionsRequired, getStrings(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationFetcher();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlBack:
//                if (mapInProgress) {
//                    return;
//                }
                super.onBackPressed();
                break;
            case R.id.rlMyLocation:
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(MAX_ZOOM);
                builder.target(getCurrentLocation());
                if (mMap != null)
                    this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

                latitude = getCurrentLocation().latitude;
                longitude = getCurrentLocation().longitude;


                executeSetAddress();
                break;


            case R.id.tvAddress:
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                if (Dependencies.isDemoRunning()) {
                    gotoMapActivity();
                } else {
                    gotoFavLocationActivity();
                }
                break;
        }
    }

    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
//      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    @Override
    public void onBackPressed() {
//        if (mapInProgress) {
//            return;
//        }
        super.onBackPressed();
        performBackAction();
    }


    private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void updateMap(LatLng latLng) {
        moveCameraToLatLng(latLng);
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        executeSetAddress();
    }

    private void moveCameraToLatLng(LatLng target) {
        if (target != null && mMap != null) {
            updateAddress = false;
            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.zoom(MAX_ZOOM);
            builder.target(target);
            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            mMap.clear();
        }
    }

    /**
     * performBackAction method
     */
    private void performBackAction() {
//        Bundle extras = new Bundle();
//        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
//        extras.putDouble("latitude", latitude);
//        extras.putDouble("longitude", longitude);
//        extras.putString("address", tvLocation.getText().toString());
//        extras.putParcelable("latLng", new LatLng(latitude, longitude));
        Intent intent = new Intent();
//        intent.putExtras(extras);
//        setBanner(RESULT_OK, intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void executeSetAddress() {
        Log.e("executeSetAddress", "executeSetAddress called");
        MapActivity.SetAddress setAddress = new MapActivity.SetAddress();
        setAddress.execute();
    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(latitude, longitude),mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null && jsonObject.has("address"))
                    tvAddress.setText(jsonObject.getString("address"));
                getMarketplaceStorefronts();
            } catch (JSONException e) {
            }
        }
    }


    /**
     * get Marketplace Storefronts api call
     */
    private void getMarketplaceStorefronts() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(CITY_ID, "")
                .add(CITY_NAME, "")
                .add(LATITUDE, latitude != null ? latitude : 0)
                .add(LONGITUDE, longitude != null ? longitude : 0)
                .add(SEARCH_TEXT, "");

        if (commonParams.build().getMap().containsKey(USER_ID))
            commonParams.build().getMap().remove(USER_ID);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        commonParams.add("skip",0);
        commonParams.add("limit",MERCHANT_PAGINATION_LIMIT);
        RestClient.getApiInterface(mActivity).getMarketplaceStorefronts(commonParams.build().getMap(),null)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                cityStorefrontsModel = cityStorefrontsModels;

                if (cityStorefrontsModel.getData().size() == 0) {
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    rvItemList.setVisibility(View.GONE);
                } else {
                    llNoStoresAvailable.setVisibility(View.GONE);
                    rvItemList.setVisibility(View.VISIBLE);
                }
                adapter = new MarketplaceRestaurantListAdapter(mActivity, cityStorefrontsModel.getData());
                rvItemList.setAdapter(adapter);


                if (cityStorefrontsModel != null && cityStorefrontsModel.getData().size() > 0 && Dependencies.getSelectedProductsArrayList().size() > 0) {
                    for (int i = 0; i < cityStorefrontsModel.getData().size(); i++) {
                        if (cityStorefrontsModel.getData().get(i).getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                            for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                                Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(cityStorefrontsModel.getData().get(i));
                            }
                        }
                    }
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (adapter != null) {
                } else {
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    rvItemList.setVisibility(View.GONE);
                }
            }
        });
    }

}