package com.tookancustomer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.FlightMapStateListener;
import com.tookancustomer.mapfiles.MapStateListener;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.mapfiles.TouchableFlightMapFragment;
import com.tookancustomer.mapfiles.TouchableMapFragment;
import com.tookancustomer.models.appConfiguration.MapObject;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tookancustomer.appdata.Constants.MAP_CURRENT_DISTANCE_CHECK;
import static com.tookancustomer.appdata.Constants.MAP_UPDATED_LOCATION_DIFFERENCE;

public class AddFromMapActivity extends BaseActivity implements OnMapReadyCallback, com.mapbox.mapboxsdk.maps.OnMapReadyCallback, LocationFetcher.OnLocationChangedListener
        , View.OnClickListener {
    private final int MAX_ZOOM = 17;
    private final int MAX_ZOOMFLIGHTMAP = 14;
    public boolean isHomeUpdate = false, isWorkUpdate = false, isOtherUpdate = false;
    public String homeLandmark, homeHouse, homePinCode, workLandmark, workHouse, workPinCode, otherLandmark, otherHouse, otherPinCode, otherLabel;
    public LatLng homeLatLng, workLatLng, otherLatLng;
    RelativeLayout rlBack;
    Button btNext;
    TextView tvLocation;
    boolean updateAddress;
    int locationType;
    boolean isAddLocation, isHomeAdded, isWorkAdded;
    int otherListSize;
    String fullAddress = "", streetNumber = "", route = "", subLocality2 = "", subLocality1 = "", locality = "", administrativeArea2 = "", administrativeArea1 = "", country = "", postalCode = "";
    boolean mapInProgress;
    boolean isflightmap = false;
    MapObject mapObject;
    TouchableFlightMapFragment flightmapFragment;
    TouchableMapFragment mapFragment;
    private RelativeLayout rlMyLocation;
    private GoogleMap mMap;
    private MapboxMap mflightMap;
    private LocationFetcher locationFetcher;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private boolean isFirstLocation;
    private boolean isFromAutoComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();
        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP) {
            setContentView(R.layout.activity_add_from_map);
        } else {
            Mapbox.getInstance(getApplicationContext(), mapObject.getAndroidMapApiKey());
            setContentView(R.layout.activity_add_from_flight_map);
        }
        mActivity = this;


        isAddLocation = getIntent().getBooleanExtra("isAddLocation", false);
        isHomeAdded = getIntent().getBooleanExtra("isHomeAdded", false);
        isWorkAdded = getIntent().getBooleanExtra("isWorkAdded", false);
        isHomeUpdate = getIntent().getBooleanExtra("isHomeUpdate", false);
        isWorkUpdate = getIntent().getBooleanExtra("isWorkUpdate", false);
        isOtherUpdate = getIntent().getBooleanExtra("isOtherUpdate", false);

        homeHouse = getIntent().getStringExtra("homeFlatNumber");
        homeLandmark = getIntent().getStringExtra("homeLandMark");
        homePinCode = getIntent().getStringExtra("homePinCode");
        homeLatLng = getIntent().getParcelableExtra("homeLatlng");


        workHouse = getIntent().getStringExtra("workFlatNumber");
        workLandmark = getIntent().getStringExtra("workLandMark");
        workPinCode = getIntent().getStringExtra("workPinCode");

        workLatLng = getIntent().getParcelableExtra("workLatlng");


        otherHouse = getIntent().getStringExtra("otherFlatNumber");
        otherLabel = getIntent().getStringExtra("otherLabel");
        otherLandmark = getIntent().getStringExtra("otherLandMark");
        otherPinCode = getIntent().getStringExtra("otherPinCode");
        otherLatLng = getIntent().getParcelableExtra("otherLatlng");
        locationType = getIntent().getIntExtra("locationType", -1);
        otherListSize = getIntent().getIntExtra("otherListSize", 0);
        if (getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE) != null) {
            Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
        } else if (getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE) != null) {
            Utils.snackBar(mActivity, getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE));
        }

        startLocationFetcher();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


//

        initializeFields();
        setmapview();


        executeSetAddress();

    }

    private void setmapview() {
        Location location = LocationUtils.getLastLocation(this);
        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
            latitude = currentLatlng.latitude;
            longitude = currentLatlng.longitude;

        } else {

            com.mapbox.mapboxsdk.maps.SupportMapFragment flightmapFragment = (com.mapbox.mapboxsdk.maps.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            flightmapFragment.getMapAsync(this);
            com.mapbox.mapboxsdk.geometry.LatLng currentLatlngflightmap = new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());
            latitude = currentLatlngflightmap.latitude;
            longitude = currentLatlngflightmap.longitude;

        }
        //String address = LocationUtils.getAddressFromLatLng(mActivity, new LatLng(latitude, longitude));
        //tvLocation.setText(address);
    }


    public void executeSetAddress() {
        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    private void initializeFields() {
        rlBack = findViewById(R.id.rlBack);
        btNext = findViewById(R.id.btNext);
        tvLocation = findViewById(R.id.tvLocation);
        tvLocation.setHint(getStrings(R.string.search_your_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        rlMyLocation = findViewById(R.id.rlMyLocation);
        if (isAddLocation) {
            btNext.setText(getStrings(R.string.next));
        } else {
            btNext.setText(getStrings(R.string.done));
        }
        Utils.setOnClickListener(this, btNext, rlBack, tvLocation, rlMyLocation);
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
        mapInProgress = true;
        mMap = googleMap;
        isflightmap = false;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mapFragment = ((TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        if (checkLocationPermissions()) {
            try {
                this.mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {

                Utils.printStackTrace(e);
            }
        }
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, UIManager.getMapStyle()));
        Location location = LocationUtils.getLastLocation(this);
        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(currentLatlng).zoom(MAX_ZOOM).tilt(41.25f).build()));
        mapInProgress = false;

        new MapStateListener(mMap, mapFragment, mActivity) {
            @Override
            public void onMapTouched() {// Map touched
                mapInProgress = true;
            }

            @Override
            public void onMapReleased() {// Map released
                Log.e("Map released", "===");
            }

            @Override
            public void onMapUnsettled() {
                Log.e("Map unsettled", "===");
                if (updateAddress) {
                    isFromAutoComplete = false;
                    tvLocation.setHint(getStrings(R.string.getting_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                    tvLocation.setTag(tvLocation.getText());
                    tvLocation.setText("");
                }
            }

            @Override
            public void onMapSettled() {

                Log.e("Map settled", "===");
                checkForMyLocationButtonVisibility();
                if (updateAddress && !isFromAutoComplete) {
                    LatLng latLng = mMap.getCameraPosition().target;
                    Double distanceCalculated = Utils.getDistance(latitude, longitude, latLng.latitude, latLng.longitude);
                    tvLocation.setHint(getStrings(R.string.pick_up_location));
//                    23448.384765625
//                    0.01843537762761116
                    Log.e("Distance calculated map ", "<<<<" + distanceCalculated);

                    if (distanceCalculated > MAP_UPDATED_LOCATION_DIFFERENCE) {
                        Log.e("Mapdistance", "===");
                        if (isHomeUpdate) {
                            homeLatLng = new LatLng(latLng.latitude, latLng.longitude);
                        } else if (isWorkUpdate) {
                            workLatLng = new LatLng(latLng.latitude, latLng.longitude);

                        } else if (isOtherUpdate) {
                            otherLatLng = new LatLng(latLng.latitude, latLng.longitude);
                        } else {
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
                        }


                        executeSetAddress();
                        // String address = LocationUtils.getAddressFromLatLng(mActivity, latLng);
                        // tvLocation.setText(address.trim().isEmpty() ? getStrings(R.string.go_to_pin) : address.trim());
                    } else {
                        Log.e("Nodsitance", "===");
//                        tvLocation.setText(tvLocation.getTag().toString());
                    }

                    tvLocation.setSelected(true);
                    tvLocation.setFocusable(true);
                }
                updateAddress = true;
                mMap.getUiSettings().setScrollGesturesEnabled(true);
              //  mapInProgress = false;
            }
        };
    }

    @Override
    public void onMapReady(@NonNull MapboxMap fmaps) {
        mapInProgress = true;
        mflightMap = fmaps;
        isflightmap = true;

        flightmapFragment = ((TouchableFlightMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mflightMap.getUiSettings().setScrollGesturesEnabled(false);
        mflightMap.getUiSettings().setZoomGesturesEnabled(false);
        mflightMap.getUiSettings().setTiltGesturesEnabled(false);
        mflightMap.getUiSettings().setCompassEnabled(false);
        mflightMap.getUiSettings().setAttributionGravity(Gravity.RIGHT | Gravity.END | Gravity.BOTTOM);


        mflightMap.getUiSettings().setLogoMargins(0, 0, 0, 205);
        mflightMap.setStyle(new Style.Builder().fromUri(Style.LIGHT),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        if (checkLocationPermissions()) {
                            try {
                                enableLocationComponent(style);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
        Location location = LocationUtils.getLastLocation(this);
        com.mapbox.mapboxsdk.geometry.LatLng currentLatlng = new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());
        mflightMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(new com.mapbox.mapboxsdk.camera.CameraPosition.Builder().target(currentLatlng).zoom(MAX_ZOOMFLIGHTMAP).tilt(42.25f).build()));
        mapInProgress = false;

//        mflightMap.getMaxZoomLevel();

//        mflightMap.setMaxZoomPreference(14);

        new FlightMapStateListener(mflightMap, flightmapFragment, mActivity) {
            @Override
            public void onMapTouched() {// Map touched
                mapInProgress = true;
                mflightMap.getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onMapReleased() {// Map released
                Log.e("Map releasedflightmap", "===");
            }

            @Override
            public void onMapUnsettled() {
                Log.e("Map unsettledflightmap", "===");
                if (updateAddress) {
                    isFromAutoComplete = false;
                    tvLocation.setHint(getStrings(R.string.getting_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                    tvLocation.setTag(tvLocation.getText());
                    tvLocation.setText("");
                }
            }

            @Override
            public void onMapSettled() {

                Log.e("Map settledflightmap", "===");
//                checkForMyLocationButtonVisibility();
                if (updateAddress && !isFromAutoComplete) {

                    com.mapbox.mapboxsdk.geometry.LatLng latLng = mflightMap.getCameraPosition().target;
//                    NumberFormat formatter = NumberFormat.getNumberInstance();
//
//                    formatter.setMaximumFractionDigits(4);
//                    String outputLat = formatter.format(latLng.latitude);
//                    String outputLong = formatter.format(latLng.longitude);
//                    Log.e("outputLat", "" + outputLat);
//                    Log.e("outputLong", ""+outputLong);
                    Double distanceCalculated = Utils.getDistance(latitude, longitude, latLng.latitude, latLng.longitude);


                    tvLocation.setHint(getStrings(R.string.pick_up_location));

                    Log.e("Distance calculated flightmap ", "<<<<" + distanceCalculated);
                    mflightMap.getUiSettings().setScrollGesturesEnabled(true);
                    if (distanceCalculated > 2) {
                        Log.e("Distance", "new" + distanceCalculated);

                        if (isHomeUpdate) {

                            homeLatLng = new LatLng(latLng.latitude, latLng.longitude);
                        } else if (isWorkUpdate) {

                            workLatLng = new LatLng(latLng.latitude, latLng.longitude);

                        } else if (isOtherUpdate) {

                            otherLatLng = new LatLng(latLng.latitude, latLng.longitude);
                        } else {
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
                        }


                        executeSetAddress();

                    } else {
                        Log.e("Nodsitance", "===");
//                        tvLocation.setText(tvLocation.getTag().toString());
                    }

                    tvLocation.setSelected(true);
                    tvLocation.setFocusable(true);
                }
                updateAddress = true;
                mflightMap.getUiSettings().setScrollGesturesEnabled(true);
                mapInProgress = false;
            }
        };
    }

    private void enableLocationComponent(Style style) {
        LocationComponent locationComponent = mflightMap.getLocationComponent();

// Activate with options
        locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(AddFromMapActivity.this, style).build());

// Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity, findViewById(R.id.rlBack));
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        fullAddress = bundle.getString(Keys.Extras.ADDRESS);
                        com.tookancustomer.mapfiles.placeapi.Location location = bundle.getParcelable(com.tookancustomer.mapfiles.placeapi.Location.class.getName());
                        isFromAutoComplete = true;
                        if (location != null) {
                            if (isHomeUpdate) {
                                homeLatLng = location.getLatLng();
                                moveCameraToLatLng(homeLatLng);
                            } else if (isWorkUpdate) {
                                workLatLng = location.getLatLng();

                                moveCameraToLatLng(workLatLng);
                            } else if (isOtherUpdate) {
                                otherLatLng = location.getLatLng();
                                moveCameraToLatLng(otherLatLng);
                            } else {
                                latitude = location.getLatLng().latitude;
                                longitude = location.getLatLng().longitude;
                                moveCameraToLatLng(location.getLatLng());
                            }
                            tvLocation.setText(fullAddress);
//                            executeSetAddress();
                        }
                    }


                }

                break;
            case Codes.Request.OPEN_CONFIRM_ADDRESS:
                if (resultCode == RESULT_OK) {
                    Bundle extras = new Bundle();
                    extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                    extras.putInt("locationType", data.getIntExtra("locationType", 0));
                    extras.putString("label", data.getStringExtra("label"));
                    extras.putString("flat", data.getStringExtra("flat"));
                    extras.putString("landmark", subLocality1);
                    extras.putBoolean("isHomeUpdate", data.getBooleanExtra("isHomeUpdate", false));
                    extras.putBoolean("isWorkUpdate", data.getBooleanExtra("isWorkUpdate", false));
                    extras.putBoolean("isOtherUpdate", data.getBooleanExtra("isOtherUpdate", false));
                    extras.putString("landmark", data.getStringExtra("landmark"));
                    extras.putString("postalcode", data.getStringExtra("postalcode"));
                    extras.putString("address", data.getStringExtra("address"));
                    if (isHomeUpdate) {
                        extras.putDouble("latitude", homeLatLng.latitude);
                        extras.putDouble("longitude", homeLatLng.longitude);
                    } else if (isWorkUpdate) {
                        extras.putDouble("latitude", workLatLng.latitude);
                        extras.putDouble("longitude", workLatLng.longitude);

                    } else if (isOtherUpdate) {
                        extras.putDouble("latitude", otherLatLng.latitude);
                        extras.putDouble("longitude", otherLatLng.longitude);
                    } else {
                        extras.putDouble("latitude", latitude);
                        extras.putDouble("longitude", longitude);
                    }


                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                /*else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    // TODO: Handle the error.

                }*/
                else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
    }

    private void checkForMyLocationButtonVisibility() {
        try {
            if (MapUtils.distance(getCurrentLocation(), mMap.getCameraPosition().target) > MAP_CURRENT_DISTANCE_CHECK) {
                rlMyLocation.setVisibility(View.VISIBLE);
            } else {
                rlMyLocation.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            rlMyLocation.setVisibility(View.VISIBLE);
        }
    }

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
        if (mMap != null || mflightMap != null) {
            if (!isflightmap) {
                if (checkLocationPermissions()) {
                    try {
                        this.mMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {

                        Utils.printStackTrace(e);
                    }
                }

            }
            if (isFirstLocation) {
                isFirstLocation = false;

                LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                if (getIntent().getStringExtra("address") == null)
                    updateMap(currentLatlng);

            }
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
        if (locationFetcher == null) {
            isFirstLocation = true;
            locationFetcher = new LocationFetcher(this, Constants.TimeRange.LOCATION_FETCH_INTERVAL, Constants.LocationPriority.BEST);
        }
        // Start fetching the location
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
            case R.id.btNext:
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                if (isAddLocation) {
                    if (tvLocation.getText().toString().isEmpty()) {
                        Utils.snackBar(mActivity, getStrings(R.string.select_location_to_proceed_further));
                        return;
                    }
                    gotoConfirmAddressActivity();
                } else {
                    performBackAction();
                }

                break;
            case R.id.rlBack:
                if (mapInProgress) {
                    return;
                }
                super.onBackPressed();
                break;
            case R.id.tvLocation:
                Utils.searchPlace(mActivity);
                break;
            case R.id.rlMyLocation:
                isFromAutoComplete = false;

                if (isflightmap) {
                    com.mapbox.mapboxsdk.camera.CameraPosition.Builder builder = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder();
                    builder.zoom(MAX_ZOOMFLIGHTMAP);
                    builder.target(getCurrentLocationflightmap());
                    if (mflightMap != null)

                        this.mflightMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(builder.build()));

                    if (isHomeUpdate) {
                        homeLatLng = new LatLng(getCurrentLocationflightmap().latitude, getCurrentLocationflightmap().longitude);
                    } else if (isWorkUpdate) {

                        workLatLng = new LatLng(getCurrentLocationflightmap().latitude, getCurrentLocationflightmap().longitude);
                    } else if (isOtherUpdate) {

                        otherLatLng = new LatLng(getCurrentLocationflightmap().latitude, getCurrentLocationflightmap().longitude);
                    } else {
                        latitude = getCurrentLocationflightmap().latitude;
                        longitude = getCurrentLocationflightmap().longitude;
                    }
                } else {
                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(MAX_ZOOM);
                    builder.target(getCurrentLocation());
                    if (mMap != null)
                        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

                    if (isHomeUpdate) {
                        homeLatLng = getCurrentLocation();
                    } else if (isWorkUpdate) {
                        workLatLng = getCurrentLocation();

                    } else if (isOtherUpdate) {
                        otherLatLng = getCurrentLocation();
                    } else {
                        latitude = getCurrentLocation().latitude;
                        longitude = getCurrentLocation().longitude;
                    }
                }
                executeSetAddress();


                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mapInProgress) {
            return;
        }
        super.onBackPressed();
    }

    public void gotoConfirmAddressActivity() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        extras.putBoolean("isAddLocation", isAddLocation);
        extras.putBoolean("isHomeAdded", isHomeAdded);
        extras.putBoolean("isWorkAdded", isWorkAdded);
        extras.putBoolean("isHomeUpdate", isHomeUpdate);
        extras.putBoolean("isWorkUpdate", isWorkUpdate);
        extras.putBoolean("isOtherUpdate", isOtherUpdate);
        extras.putString("otherLabel", otherLabel);


        //  extras.putString("homeFlatNumber", homeHouse);
        extras.putParcelable("homeLatlng", homeLatLng);
        //    extras.putString("homeLandMark", homeLandmark);
        // extras.putString("workFlatNumber", workHouse);
        extras.putParcelable("workLatlng", workLatLng);
        //   extras.putString("workLandMark", workLandmark);
        if (isOtherUpdate) {
            extras.putString("otherFlatNumber", otherHouse);
            extras.putString("otherPinCode", otherPinCode);
            extras.putString("otherLandMark", otherLandmark);
        }
        if (isHomeUpdate) {
            extras.putString("homeFlatNumber", homeHouse);
            extras.putString("homePinCode", homePinCode);
            extras.putString("homeLandMark", homeLandmark);
        }
        if (isWorkUpdate) {
            extras.putString("workFlatNumber", workHouse);
            extras.putString("workPinCode", workPinCode);
            extras.putString("workLandMark", workLandmark);
        }
        extras.putParcelable("otherLatlng", otherLatLng);
        //   extras.putString("otherLandMark", otherLandmark);


        extras.putString("formattedAddress", fullAddress);
        extras.putString("flat", ((streetNumber != null && !streetNumber.isEmpty()) ? streetNumber + ", " : "") + route);
        extras.putString("landmark", subLocality1);
        extras.putString("address", ((administrativeArea1 != null && !administrativeArea1.isEmpty()) ? administrativeArea1 + ", " : "") +
                ((country != null && !country.isEmpty()) ? country + ", " : "")
                + postalCode);


        extras.putInt("locationType", locationType);
        extras.putInt("otherListSize", otherListSize);

        Intent intent = new Intent(this, ConfirmAddressActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_CONFIRM_ADDRESS);

    }

    private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);

//            return new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private com.mapbox.mapboxsdk.geometry.LatLng getCurrentLocationflightmap() {
        Location location = LocationUtils.getLastLocation(this);


        return new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());
    }

    private void updateMap(LatLng latLng) {
        moveCameraToLatLng(latLng);
        // tvLocation.setText(address.trim().isEmpty() ? getStrings(R.string.go_to_pin) : address.trim());
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        executeSetAddress();

        tvLocation.setSelected(true);
        tvLocation.setFocusable(true);
    }

    private void moveCameraToLatLng(LatLng target) {

        if (isflightmap) {
            if (target != null && mflightMap != null) {
                updateAddress = false;
                com.mapbox.mapboxsdk.camera.CameraPosition.Builder builder = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder();
                builder.zoom(MAX_ZOOMFLIGHTMAP);
                if (isHomeUpdate) {
                    builder.target(new com.mapbox.mapboxsdk.geometry.LatLng(homeLatLng.latitude, homeLatLng.longitude));
                } else if (isWorkUpdate) {
                    builder.target(new com.mapbox.mapboxsdk.geometry.LatLng(workLatLng.latitude, workLatLng.longitude));


                } else if (isOtherUpdate) {

                    builder.target(new com.mapbox.mapboxsdk.geometry.LatLng(otherLatLng.latitude, otherLatLng.longitude));

                } else {
                    builder.target(new com.mapbox.mapboxsdk.geometry.LatLng(target.latitude, target.longitude));
                }
                this.mflightMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(builder.build()));
                mflightMap.clear();
            }

        } else {
            if (target != null && mMap != null) {
                updateAddress = false;
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(MAX_ZOOM);
                if (isHomeUpdate) {
                    builder.target(homeLatLng);
                } else if (isWorkUpdate) {
                    builder.target(workLatLng);

                } else if (isOtherUpdate) {
                    builder.target(otherLatLng);
                } else {
                    builder.target(target);
                }
                this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                mMap.clear();
            }

        }
    }

    private void performBackAction() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        extras.putDouble("latitude", latitude);
        extras.putDouble("longitude", longitude);
        extras.putString("address", tvLocation.getText().toString());
        extras.putParcelable("latLng", new LatLng(latitude, longitude));
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");

            if (isflightmap) {

                if (isHomeUpdate) {

                    return MapUtils.getGapiJson(new LatLng(homeLatLng.latitude, homeLatLng.longitude), mActivity);

                } else if (isWorkUpdate) {

                    return MapUtils.getGapiJson(new LatLng(workLatLng.latitude, workLatLng.longitude), mActivity);

                } else if (isOtherUpdate) {

                    return MapUtils.getGapiJson(new LatLng(otherLatLng.latitude, otherLatLng.longitude), mActivity);

                } else {

                    return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
                }
            } else {
                if (isHomeUpdate) {
                    return MapUtils.getGapiJson(homeLatLng, mActivity);
                } else if (isWorkUpdate) {
                    return MapUtils.getGapiJson(workLatLng, mActivity);

                } else if (isOtherUpdate) {
                    return MapUtils.getGapiJson(new LatLng(otherLatLng.latitude, otherLatLng.longitude), mActivity);
                } else {
                    return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
                }
            }


//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(JSONObject zero) {
            try {
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }
            if (zero != null) {
                try {
                    if ((zero.getString("address") != null) && !(zero.getString("address").isEmpty())) {
                        fullAddress = zero.getString("address");
                    } else {
                        if (zero.has("address_components")) {
                            fullAddress = "";
                            streetNumber = "";
                            route = "";
                            subLocality2 = "";
                            subLocality1 = "";
                            locality = "";
                            administrativeArea2 = "";
                            administrativeArea1 = "";
                            country = "";
                            postalCode = "";


                            ArrayList<String> selectedAddressComponentsArr = new ArrayList<String>();
                            JSONArray addressComponents = zero.getJSONArray("address_components");

                            for (int i = 0; i < addressComponents.length(); i++) {

                                JSONObject iObj = addressComponents.getJSONObject(i);
                                JSONArray jArr = iObj.getJSONArray("types");

                                ArrayList<String> addressTypes = new ArrayList<String>();
                                for (int j = 0; j < jArr.length(); j++) {
                                    addressTypes.add(jArr.getString(j));
                                }

                                if ("".equalsIgnoreCase(streetNumber) && addressTypes.contains("street_number")) {
                                    streetNumber = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(streetNumber) && !selectedAddressComponentsArr.toString().contains(streetNumber)) {
                                        selectedAddressComponentsArr.add(streetNumber);
                                    }
                                }
                                if ("".equalsIgnoreCase(route) && addressTypes.contains("route")) {
                                    route = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(route) && !selectedAddressComponentsArr.toString().contains(route)) {
                                        selectedAddressComponentsArr.add(route);
                                    }
                                }
                                if ("".equalsIgnoreCase(subLocality2) && addressTypes.contains("sublocality_level_2")) {
                                    subLocality2 = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(subLocality2) && !selectedAddressComponentsArr.toString().contains(subLocality2)) {
                                        selectedAddressComponentsArr.add(subLocality2);
                                    }
                                }
                                if ("".equalsIgnoreCase(subLocality1) && addressTypes.contains("sublocality_level_1")) {
                                    subLocality1 = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(subLocality1) && !selectedAddressComponentsArr.toString().contains(subLocality1)) {
                                        selectedAddressComponentsArr.add(subLocality1);
                                    }
                                }
                                if ("".equalsIgnoreCase(locality) && addressTypes.contains("locality")) {
                                    locality = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(locality) && !selectedAddressComponentsArr.toString().contains(locality)) {
                                        selectedAddressComponentsArr.add(locality);
                                    }
                                }
                                if ("".equalsIgnoreCase(administrativeArea2) && addressTypes.contains("administrative_area_level_2")) {
                                    administrativeArea2 = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(administrativeArea2) && !selectedAddressComponentsArr.toString().contains(administrativeArea2)) {
                                        selectedAddressComponentsArr.add(administrativeArea2);
                                    }
                                }
                                if ("".equalsIgnoreCase(administrativeArea1) && addressTypes.contains("administrative_area_level_1")) {
                                    administrativeArea1 = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(administrativeArea1) && !selectedAddressComponentsArr.toString().contains(administrativeArea1)) {
                                        selectedAddressComponentsArr.add(administrativeArea1);
                                    }
                                }
                                if ("".equalsIgnoreCase(country) && addressTypes.contains("country")) {
                                    country = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(country) && !selectedAddressComponentsArr.toString().contains(country)) {
                                        selectedAddressComponentsArr.add(country);
                                    }
                                }
                                if ("".equalsIgnoreCase(postalCode) && addressTypes.contains("postal_code")) {
                                    postalCode = iObj.getString("long_name");
                                    if (!"".equalsIgnoreCase(postalCode) && !selectedAddressComponentsArr.toString().contains(postalCode)) {
                                        selectedAddressComponentsArr.add(postalCode);
                                    }
                                }
                            }

                            fullAddress = "";
                            if (selectedAddressComponentsArr.size() > 0) {
                                for (int i = 0; i < selectedAddressComponentsArr.size(); i++) {
                                    if (i < selectedAddressComponentsArr.size() - 1) {
                                        fullAddress = fullAddress + selectedAddressComponentsArr.get(i) + ", ";
                                    } else {
                                        fullAddress = fullAddress + selectedAddressComponentsArr.get(i);
                                    }
                                }
                            } else {
                                fullAddress = zero.getString("address");
                            }


                        } else {
                            fullAddress = zero.optString("address");
                        }
                    }
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                    //e.printStackTrace();
                    fullAddress = zero.optString("address");
                }
            }
            if (mapInProgress) {
                String[] splitAddress = fullAddress.split(",");
                String[] newArray =Arrays.copyOfRange(splitAddress, 1, splitAddress.length);
                StringBuilder str = new StringBuilder("");

                // Traversing the ArrayList
                for (String eachstring : newArray) {

                    // Each element in ArrayList is appended
                    // followed by comma
                    str.append(eachstring).append(",");
                }

                // StringBuffer to String conversion
                String commaseparatedlist = str.toString();

                // By following condition you can remove the last
                // comma
                if (commaseparatedlist.length() > 0)
                    commaseparatedlist
                            = commaseparatedlist.substring(
                            0, commaseparatedlist.length() - 1);

               // System.out.println(commaseparatedlist);
                tvLocation.setText(commaseparatedlist);
            } else {
                tvLocation.setText(fullAddress);
            }


            mapInProgress = false;
            Log.e("fullAddress", fullAddress);
            Log.e("streetNumber", streetNumber);
            Log.e("route", route);
            Log.e("subLocality2", subLocality2);
            Log.e("subLocality1", subLocality1);
            Log.e("locality", locality);
            Log.e("administrativeArea2", administrativeArea2);
            Log.e("administrativeArea1", administrativeArea1);
            Log.e("country", country);
            Log.e("postalCode", postalCode);

        }
    }


}