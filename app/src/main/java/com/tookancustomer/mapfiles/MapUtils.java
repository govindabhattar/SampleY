package com.tookancustomer.mapfiles;

import android.app.Activity;
import android.location.Location;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.hippo.utils.filepicker.Constant;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.appConfiguration.MapObject;
import com.tookancustomer.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.tookancustomer.utility.Utils.printStackTrace;


public class MapUtils {
    private static final String TAG = MapUtils.class.getName();


    public static double distance(LatLng start, LatLng end) {
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.latitude);
            location1.setLongitude(start.longitude);
            Location location2 = new Location("locationA");
            location2.setLatitude(end.latitude);
            location2.setLongitude(end.longitude);

            double distance = location1.distanceTo(location2);
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.getInstance(new Locale("en", "US"));
            DecimalFormatSymbols custom = new DecimalFormatSymbols();
            custom.setDecimalSeparator('.');
            decimalFormat.setDecimalFormatSymbols(custom);
            double distanceFormated = Double.parseDouble(decimalFormat.format(distance));
            return distanceFormated;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                printStackTrace(e);
        }
        return 0;
    }


    //http://maps.googleapis.com/maps/api/directions/json?origin=30.7342187,76.78088307&destination=30.74571777,76.78635478&sensor=false&mode=driving&alternatives=false
    public static String makeDirectionsURL(LatLng source, LatLng destination) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(source.latitude));
        urlString.append(",");
        urlString.append(Double.toString(source.longitude));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destination.latitude));
        urlString.append(",");
        urlString.append(Double.toString(destination.longitude));
        urlString.append("&sensor=false&mode=driving&alternatives=false");
        return urlString.toString();
    }

    //https://maps.googleapis.com/maps/api/distancematrix/json?origins=30.75,76.78&destinations=30.78,76.79&language=EN&sensor=false
    public static String makeDistanceMatrixURL(LatLng source, LatLng destination, Boolean isImperialUnit) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json");
        urlString.append("?origins=");// from
        urlString.append(Double.toString(source.latitude));
        urlString.append(",");
        urlString.append(Double.toString(source.longitude));
        urlString.append("&destinations=");// to
        urlString.append(Double.toString(destination.latitude));
        urlString.append(",");
        urlString.append(Double.toString(destination.longitude));
        urlString.append("&language=EN&sensor=false&alternatives=false");
        if (isImperialUnit)
            urlString.append("&units=imperial"); //Imperial distance in miles and metric distance in kms.

        return urlString.toString();
    }


    public static List<LatLng> decodeDirectionsPolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int bInt, shift = 0, result = 0;
            do {
                bInt = encoded.charAt(index++) - 63;
                result |= (bInt & 0x1f) << shift;
                shift += 5;
            } while (bInt >= 0x20);
            int dlat = ((result & 1) == 0 ? (result >> 1) : ~(result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                bInt = encoded.charAt(index++) - 63;
                result |= (bInt & 0x1f) << shift;
                shift += 5;
            } while (bInt >= 0x20);
            int dlng = ((result & 1) == 0 ? (result >> 1) : ~(result >> 1));
            lng += dlng;

            LatLng pLatLng = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(pLatLng);
        }
        return poly;
    }

    public static String getGAPIAddress(LatLng latLng) {
        String fullAddress = "";
        try {
            String url = getGAPIAddressUrl(latLng);

//            if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("hi_in")) {
//                url = url + "&language=hi";
//            }

            JSONObject jsonObj = new JSONObject(
                    new HttpRequester().getJSONFromUrl(url));

            Log.e("jsonObj", "=" + jsonObj);

            String status = jsonObj.getString("status");
            if (status.equalsIgnoreCase("OK")) {

                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);

                String streetNumber = "", route = "", subLocality2 = "", subLocality1 = "", locality = "", administrativeArea2 = "", administrativeArea1 = "", country = "", postalCode = "";

                if (zero.has("address_components")) {
                    try {

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

                    } catch (Exception e) {
                        if (BuildConfig.DEBUG)
                            printStackTrace(e);
                        fullAddress = zero.getString("address");
                    }
                } else {
                    fullAddress = zero.getString("address");
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                printStackTrace(e);
        }
        return fullAddress;
    }

    public static JSONObject getGapiJson(LatLng latLng, Activity mActivity) {
        JSONObject fullAddressJSON = null;
        try {
            String url = getGAPIAddressUrl(latLng);

            JSONObject jsonObj = new JSONObject(
                    new HttpRequester().getJSONFromUrl(url));

            byte ptext[] = jsonObj.toString().getBytes("ISO-8859-1");
            String value = new String(ptext, "UTF-8");
            jsonObj = new JSONObject(value);


            Log.e("jsonObj", "=" + jsonObj);

            String status = jsonObj.getString("status");
//            if (status.equalsIgnoreCase("OK")) {
            if (status.equalsIgnoreCase("200")) {

                fullAddressJSON = jsonObj.getJSONObject("data");
//                fullAddressJSON = Results.getJSONObject(0);

            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                printStackTrace(e);
        }
        return fullAddressJSON;
    }


    //http://maps.googleapis.com/maps/api/geocode/json?latlng=30.75,76.75
    //Locale.getDefault().getLanguage(
    private static String getGAPIAddressUrl(final LatLng latLng) {
        MapObject mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();
        if (mapObject == null) {
            return "";
        }

        String url = "https://maps.flightmap.io/api/search_reverse?";
        url = url + "lat=" + latLng.latitude + "&lng=" + latLng.longitude + "&zoom=18"+"&offering=2";

        if (mapObject.getMapType() == Constants.MapConstants.FLIGHTMAP) {
            url = url + "&fm_token=" + mapObject.getAndroidMapApiKey()+"&options="+"0";
        } else {
            url = url + "&api_key=" + mapObject.getGoogleApiKey()+"&options="+"2";

        }


        Log.e("reverse gio cide", url);

        return url;

    }

    private static String generateGoogleSignature(final String urlToSign, final String signatureKey) throws NoSuchAlgorithmException,
            InvalidKeyException {

        // Convert the key from 'web safe' base 64 to binary
        String keyString = signatureKey;
        keyString = keyString.replace('-', '+');
        keyString = keyString.replace('_', '/');
        // Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
        byte[] key = Base64.decode(keyString, Base64.DEFAULT);


        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);

        // compute the binary signature for the request
        byte[] sigBytes = mac.doFinal(urlToSign.getBytes());

        // base 64 encode the binary signature
        // Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
        String signature = Base64.encodeToString(sigBytes, Base64.DEFAULT);

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');

        return signature;
    }

}
