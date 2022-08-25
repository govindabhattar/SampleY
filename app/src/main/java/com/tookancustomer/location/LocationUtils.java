package com.tookancustomer.location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.mapfiles.HttpRequester;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import static com.tookancustomer.appdata.TerminologyStrings.ADDRESS;

public class LocationUtils {

    public static double LATITUDE = 0;
    public static double LONGITUDE = 0;


    /**
     * Method to initialize the Location
     */
    public static void init(Context context) {

        try {

            long latitude = Prefs.with(context).getLong(Keys.Prefs.LATITUDE, 0);
            long longitude = Prefs.with(context).getLong(Keys.Prefs.LONGITUDE, 0);

            LATITUDE = Double.longBitsToDouble(latitude);
            LONGITUDE = Double.longBitsToDouble(longitude);

        } catch (Exception ex) {

            Prefs.with(context).remove(Keys.Prefs.LATITUDE);
            Prefs.with(context).remove(Keys.Prefs.LONGITUDE);

            init(context);
        }
    }

    /**
     * Method to save the location in preferences
     *
     * @param location
     */
    public static void saveLocation(Context context, Location location) {

        if (location == null)
            return;

        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();

        Prefs.with(context).save(Keys.Prefs.LATITUDE, Double.doubleToRawLongBits(LATITUDE));
        Prefs.with(context).save(Keys.Prefs.LONGITUDE, Double.doubleToRawLongBits(LONGITUDE));
    }

    /**
     * Method to check whether the mock locations are enabled
     *
     * @return
     */
    public static boolean isMockLocationsEnabled(Context context) {

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M &&
                !(Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"));
    }

    /**
     * Method to check whether location services
     * through network are enabled or not
     *
     * @return
     */
    private static boolean isProviderEnabled(Context context, String provider) {

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            return manager.isProviderEnabled(provider);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Method to check whether the GPS is enabled or not
     *
     * @param context
     * @return
     */
    public static boolean isGPSEnabled(Context context) {

        return isProviderEnabled(context, LocationManager.GPS_PROVIDER);
    }

    /**
     * Method to check whether location services via Network are enabled or not
     *
     * @return
     */
    public static boolean isNetworkEnabled(Context context) {

        return isProviderEnabled(context, LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Method to save the location in preferences
     */
    public static Location getLastLocation(Context context) {

        long lat = Prefs.with(context).getLong(Keys.Prefs.LATITUDE, 0);
        long lng = Prefs.with(context).getLong(Keys.Prefs.LONGITUDE, 0);

        double latitude = Double.longBitsToDouble(lat);
        double longitude = Double.longBitsToDouble(lng);

        Location lastLocation = new Location("LAST_LOCATION");

        lastLocation.setLatitude(latitude);
        lastLocation.setLongitude(longitude);

        return lastLocation;
    }

    /**
     * Method to build location from latitude and Longitude
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public static Location toLocation(double latitude, double longitude) {

        Location location = new Location("Location");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    /**
     * Method to build location from latitude and Longitude
     *
     * @param locationLatLng
     * @return
     */
    public static Location toLocation(LatLng locationLatLng) {

        if (locationLatLng == null)
            return null;

        return toLocation(locationLatLng.latitude, locationLatLng.longitude);
    }

//    public static String getAddressFromLatLng(Context context, LatLng latLng) {
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.w("My Current loction address", "" + strReturnedAddress.toString());
//            } else {
//                Log.w("My Current loction address", "No Address returned!");
//            }
//        } catch (Exception e) {
//            if (BuildConfig.DEBUG)
//                                Utils.printStackTrace(e);
//            Log.w("My Current loction address", "Canont get Address!");
//        }
//        return strAdd;
//
////        if (strAdd.isEmpty()) {
////            return getAddress(latLng);
////        } else {
////        }
//
//    }

    public static String getAddressFromLatLng(Context context, LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Log.i("LatLng", "==" + latLng);
            Log.i("addresses", "==" + addresses.toString());
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {

            }
        } catch (Exception e) {

                               Utils.printStackTrace(e);

        }
        return strAdd;
    }


    public static void setAddressFromLatLng(Activity activity, LatLng latLng, TextView textView) {
        SetAddressFromLatLng distanceAsyncTask = new SetAddressFromLatLng(activity,latLng,textView);
        distanceAsyncTask.execute();
    }

    public static class SetAddressFromLatLng extends AsyncTask<Void, Void, String> {
        LatLng latLng;
        TextView textView;
        Activity activity;

        public SetAddressFromLatLng(Activity activity, LatLng latLng, TextView textView) {
            this.latLng = latLng;
            this.textView = textView;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String strAdd = "";
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Log.i("LatLng", "==" + latLng);
                Log.i("addresses", "==" + addresses.toString());
                if (addresses != null && addresses.size() > 0) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                }
            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }
            return strAdd;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                textView.setText(result.trim().isEmpty() ? StorefrontCommonData.getString(activity,R.string.go_to_pin).replace(ADDRESS,StorefrontCommonData.getTerminology().getAddress()) : result.trim());
            }
        }
    }

    public static String getAddress(LatLng latLng) {
        String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";
        Address1 = "";
        Address2 = "";
        City = "";
        State = "";
        Country = "";
        County = "";
        PIN = "";

        try {

            JSONObject jsonObj = new JSONObject(new HttpRequester().getJSONFromUrl("http://xmaps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + ","
                    + latLng.longitude + "&sensor=true"));
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }
                    }

                    // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }
            }

        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
        return Address1 + ", " + Address2 + ", " + City + ", " + Country;
    }


}
