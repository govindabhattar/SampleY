package com.tookancustomer.retrofit2;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.appdata.Config;
import com.tookancustomer.utility.Utils;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest Client
 */
public class RestClient {
    private final static int SOCKET_TIMEOUT = 60;

    public static Retrofit retrofit = null;
    public static Retrofit retrofitGoogleApi = null;
    public static Retrofit flightMapApi = null;
    public static Retrofit muticApi = null;
    public static Retrofit junglePaymentsApi = null;



    static Integer BKS_KEYSTORE_RAW_FILE_ID = 0;
    // Integer BKS_KEYSTORE_RAW_FILE_ID = R.raw.keystorebks;
    static Integer SSL_KEY_PASSWORD_STRING_ID = 0;
    //Integer SSL_KEY_PASSWORD_STRING_ID = R.string.sslKeyPassword;

    /**
     * @return
     */
    public static ApiInterface getApiInterface(Context context) {
        // if (retrofit == null || !Config.isRelease()) {

//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
        try {
            retrofit = new Retrofit.Builder()
                    // .baseUrl("http://52.90.184.241:3000/")
                    .baseUrl(Config.getServerUrl(context))
                    //.baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient().build())
                    //.client(secureConnection().build())
                    .build();
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
        // }
        return retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getJunglePaymentsApi(Context context) {
        String baseUrl = "https://payment.jungleworks.com/";
        //https://shubham-3015.tookanapp.com/stripeGpay/transactionInfo
        //https://payment.jungleworks.com/
        //https://payment-test.tookanapp.com/razorpay_upi/transactionInfo
        //BETA https://payment-beta.jungleworks.com/
        //TEST
        if (!Config.isRelease()) {
            baseUrl = "https://payment-beta.jungleworks.com/";
        }
        // baseUrl = "https://payment-test-3017.tookanapp.com/";
        if (junglePaymentsApi == null || !Config.isRelease()) {
            try {
                junglePaymentsApi = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient().build())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return junglePaymentsApi.create(ApiInterface.class);
    }



    /**
     * @return
     */
    public static ApiInterface getGoogleApiInterface(final Context context) {
        try {
            if (retrofitGoogleApi == null) {
                retrofitGoogleApi = new Retrofit.Builder()
                        .baseUrl("https://maps.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient().build())
                        .build();
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
        return retrofitGoogleApi.create(ApiInterface.class);
    }

    public static ApiInterface getJungleMapsApi() {
        if (flightMapApi == null || !Config.isRelease()) {
            try {
                flightMapApi = new Retrofit.Builder()
                        .baseUrl("https://maps.flightmap.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient().build())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flightMapApi.create(ApiInterface.class);
    }

    public static ApiInterface getZohoApiInterface(Context context) {
        //if (retrofit == null || !Config.isRelease()) {
        try {
            retrofit = new Retrofit.Builder()
                    // .baseUrl("http://52.90.184.241:3000/")
                    .baseUrl(Config.getZohoServerUrl(context))
                    //.baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient().build())
                    //.client(secureConnection().build())
                    .build();
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
        // }
        return retrofit.create(ApiInterface.class);
    }

    /**
     * @return
     */
    public static Retrofit getRetrofitBuilder(Context context) {
        if (retrofit == null || !Config.isRelease()) {
            retrofit = new Retrofit.Builder()
                    // .baseUrl("http://52.90.184.241:3000/")
                    .baseUrl(Config.getServerUrl(context))
                    //.baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient().build())
                    .build();
        }
        return retrofit;
    }

    /**
     * @return
     */
    private static OkHttpClient.Builder httpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);


        if (BuildConfig.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS);
        return httpClientTLS(httpClient);
    }


    private static OkHttpClient.Builder httpClientTLS(OkHttpClient.Builder httpClient) {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                httpClient.sslSocketFactory(new TLSSocketFactory());
            } else {
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                httpClient.sslSocketFactory(new TLSSocketFactory(), trustManager);
            }
            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);

            httpClient.connectionSpecs(specs);
        } catch (Exception exc) {
//            exc.printStackTrace();
        }
        return httpClient;
    }


}