package com.tookancustomer;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
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
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;


import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;

public class ShowProductOnMapActivity extends BaseActivity implements OnMapReadyCallback
        , View.OnClickListener {
    RelativeLayout rlBack;
    private RelativeLayout rlMyLocation;
    private GoogleMap mMap;
//    private LocationFetcher locationFetcher;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private final int MAX_ZOOM = 15;
    private TextView tvHeading;
//    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_map);
        mActivity = this;

        latitude = getIntent().getExtras().getDouble(PICKUP_LATITUDE);
        longitude = getIntent().getExtras().getDouble(PICKUP_LONGITUDE);

        initializeFields();

    }


    private void initializeFields() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.location_on_map));
        rlMyLocation = findViewById(R.id.rlMyLocation);


        Utils.setOnClickListener(this, rlBack, rlMyLocation);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLng latLng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmap(mActivity, R.drawable.ic_icon_pin_location))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(MAX_ZOOM), 1000, null);

        Log.e("latlong<><>", latLng + "");


    }


    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlBack:
                super.onBackPressed();
                break;

            case R.id.rlMyLocation:
                LatLng currentLatlng = new LatLng(latitude, longitude);
//                CameraPosition.Builder builder = new CameraPosition.Builder();
//                builder.zoom(MAX_ZOOM);
//                builder.target(currentLatlng);
                if (mMap != null)
//                    mMap.addMarker(new MarkerOptions().position(currentLatlng)
//                            .icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmap(mActivity, R.drawable.ic_icon_pin_location))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(MAX_ZOOM), 1000, null);

//                this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        performBackAction();
    }


    /*private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }*/


    private void performBackAction() {
        setResult(RESULT_OK);
        finish();
    }

}