package com.tookancustomer.retrofit2;

import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * Created by cl-macmini-33 on 27/09/16.
 */

public class RequestBodyParams {
    HashMap<String, RequestBody> map = new HashMap<>();

    private RequestBodyParams(Builder builder) {
        this.map = builder.map;

    }

    public HashMap<String, RequestBody> getMap() {
        return map;
    }


    public static class Builder {
        HashMap<String, RequestBody> map = new HashMap<>();

        public Builder() {
        }

        public Builder add(String key, Object value) {
            map.put(key, RetrofitUtils.getRequestBodyFromString(String.valueOf(value)));
            return this;
        }

        public RequestBodyParams build() {
            return new RequestBodyParams(this);
        }
    }
}


