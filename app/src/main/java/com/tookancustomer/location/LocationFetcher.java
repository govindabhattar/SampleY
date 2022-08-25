package com.tookancustomer.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;


public class LocationFetcher implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long CHECK_LOCATION_INTERVAL = 20000;
    private static final long LAST_LOCATION_TIME_THRESHOLD = 2 * 60000;

    private final String TAG = this.getClass().getSimpleName();

    public int priority;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationrequest;
    private Location location;

    private long requestInterval;

    private OnLocationChangedListener onLocationChangeListener;

    private Context context;

    private String LOCATION_SP = "location_sp";
    private String LOCATION_LAT = "location_lat";
    private String LOCATION_LNG = "location_lng";

    private Handler checkLocationUpdateStartedHandler;
    private Runnable checkLocationUpdateStartedRunnable;

    public LocationFetcher(OnLocationChangedListener onLocationChangeListener, long requestInterval, int priority) {

        this.onLocationChangeListener = onLocationChangeListener;
        this.context = (Context) onLocationChangeListener;
        this.requestInterval = requestInterval;
        this.priority = priority;
        connect();
    }

    public synchronized void connect() {

        destroy();

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resp == ConnectionResult.SUCCESS) { // google play services working

            if (isLocationEnabled(context)) {   // location fetching enabled

                buildGoogleApiClient(context);
            } else {                            // location disabled

            }
        } else {                                // google play services not working

            Log.e("Google Play error", "=" + resp);
        }
        startCheckingLocationUpdates();
    }

    public synchronized void destroyWaitAndConnect() {

        destroy();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, 2000);
    }

    private synchronized void saveLatLngToSP(double latitude, double longitude) {

        SharedPreferences preferences = context.getSharedPreferences(LOCATION_SP, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOCATION_LAT, "" + latitude);
        editor.putString(LOCATION_LNG, "" + longitude);
        editor.commit();
    }


    private synchronized double getSavedLatFromSP() {

        SharedPreferences preferences = context.getSharedPreferences(LOCATION_SP, 0);
        String latitude = preferences.getString(LOCATION_LAT, "" + 0);
        Log.d("SAVED LAST LAT", "==" + latitude);
        return Double.parseDouble(latitude);
    }

    private synchronized double getSavedLngFromSP() {

        SharedPreferences preferences = context.getSharedPreferences(LOCATION_SP, 0);
        String longitude = preferences.getString(LOCATION_LNG, "" + 0);
        return Double.parseDouble(longitude);
    }


    /**
     * Checks if location fetching is enabled in device or not
     *
     * @param context application context
     * @return true if any location provider is enabled else false
     */
    private synchronized boolean isLocationEnabled(Context context) {

        try {
            ContentResolver contentResolver = context.getContentResolver();

            boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
                    LocationManager.GPS_PROVIDER);

            boolean netStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
                    LocationManager.NETWORK_PROVIDER);

            return gpsStatus || netStatus;

        } catch (Exception e) {


                               Utils.printStackTrace(e);
            return false;
        }
    }


    protected void createLocationRequest(long interval, int priority) {

        locationrequest = new LocationRequest();
        locationrequest.setInterval(interval);
        locationrequest.setFastestInterval(interval / 2);

        if (priority == 1) {

            locationrequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else if (priority == 2) {

            locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } else {

            locationrequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }
    }


    protected synchronized void buildGoogleApiClient(Context context) {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    protected void startLocationUpdates(long interval, int priority) {

        createLocationRequest(interval, priority);
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationrequest, this);
    }


    /**
     * Function to get latitude
     */
    public double getLatitude() {
        try {
            Location loc = getLocation();
            if (loc != null) {
                return loc.getLatitude();
            }
        } catch (Exception e) {
            Log.e("e", "=" + e.toString());
        }
        return getSavedLatFromSP();
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {

        try {
            Location loc = getLocation();
            if (loc != null) {
                return loc.getLongitude();
            }
        } catch (Exception e) {
            Log.e("e", "=" + e.toString());
        }
        return getSavedLngFromSP();
    }

    @SuppressLint("LongLogTag")
    public Location getLocation() {

        try {
            if (location != null) {
                return location;
            } else {
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    Log.e("Fetching last fused location", "=" + location);
                    return location;
                }
            }
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
        return null;
    }


    public synchronized void destroy() {

        try {
            this.location = null;
            Log.e("location", "destroy");
            if (googleApiClient != null) {
                if (googleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                    googleApiClient.disconnect();
                } else if (googleApiClient.isConnecting()) {
                    googleApiClient.disconnect();
                }
            }
        } catch (Exception e) {
            Log.e("e", "=" + e.toString());
        }
        stopCheckingLocationUpdates();
    }


    private synchronized void startRequest() {

        try {
            startLocationUpdates(requestInterval, priority);
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Log.e(TAG, "onConnected");
        Location loc = getLocation();
        if (loc != null) {
            onLocationChangeListener.onLocationChanged(loc, priority);
        }
        startRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

        this.location = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.e(TAG, "onConnectionFailed");
        this.location = null;
    }


    @Override
    public void onLocationChanged(Location location) {

        try {
            if (location != null) {

//                Log.i("-----------------------", "-----------------------");
//                Log.i("LOCATION_CHANGED", location.toString());
//                Log.i("-----------------------", "-----------------------");
                this.location = location;
                onLocationChangeListener.onLocationChanged(location, priority);
                saveLatLngToSP(location.getLatitude(), location.getLongitude());
            }
        } catch (Exception e) {


                               Utils.printStackTrace(e);
        }
    }


    private synchronized void startCheckingLocationUpdates() {

        checkLocationUpdateStartedHandler = new Handler();

        checkLocationUpdateStartedRunnable = new Runnable() {

            @Override
            public void run() {

                if (LocationFetcher.this.location == null) {

//                    destroyWaitAndConnect();
                } else {

                    long timeSinceLastLocationFix = System.currentTimeMillis()
                            - LocationFetcher.this.location.getTime();

                    if (timeSinceLastLocationFix > LAST_LOCATION_TIME_THRESHOLD) {

//                        destroyWaitAndConnect();
                    } else {

                        checkLocationUpdateStartedHandler
                                .postDelayed(checkLocationUpdateStartedRunnable,
                                        CHECK_LOCATION_INTERVAL);
                    }
                }
            }
        };
        checkLocationUpdateStartedHandler.postDelayed(checkLocationUpdateStartedRunnable, CHECK_LOCATION_INTERVAL);
    }


    public synchronized void stopCheckingLocationUpdates() {
        try {
            if (checkLocationUpdateStartedHandler != null && checkLocationUpdateStartedRunnable != null) {
                checkLocationUpdateStartedHandler.removeCallbacks(checkLocationUpdateStartedRunnable);
            }
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        } finally {
            checkLocationUpdateStartedHandler = null;
            checkLocationUpdateStartedRunnable = null;
        }
    }

    public interface OnLocationChangedListener {

        /**
         * Override this method to listen to the Location Updates
         *
         * @param location
         * @param priority
         */
        void onLocationChanged(Location location, int priority);
    }
}