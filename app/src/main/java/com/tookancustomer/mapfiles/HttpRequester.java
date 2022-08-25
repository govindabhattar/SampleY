package com.tookancustomer.mapfiles;

import com.tookancustomer.utility.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequester {

    public static String SERVER_TIMEOUT = "SERVER_TIMEOUT";
    public static int TIMEOUT_CONNECTION = 30000, TIMEOUT_SOCKET = 30000, RETRY_COUNT = 0, SLEEP_BETWEEN_RETRY = 0;

    // constructor
    public HttpRequester() {
        SERVER_TIMEOUT = "SERVER_TIMEOUT";
    }

    public String getJSONFromUrl(String stringUrl) {

        String json = "";

        try {

            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line + "\n");

            json = stringBuilder.toString();
            inputStream.close();

        } catch (Exception e) {
           Log.e("Buffer Error", "Error parsing result " + e.toString());
        }

        return json;
    }
}








