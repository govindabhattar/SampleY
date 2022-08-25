package com.tookancustomer.retrofit2;

import java.util.HashMap;

/**
 * Created by cl-macmini-33 on 27/09/16.
 */

public class CommonParams {
    public static Object Builder;
    HashMap<String, String> map = new HashMap<>();

    private CommonParams(Builder builder) {
        this.map = builder.map;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public static class Builder {
        HashMap<String, String> map = new HashMap<>();

        public Builder() {
        }

        public Builder add(String key, Object value) {
            map.put(key, String.valueOf(value));
            return this;
        }

        public Builder remove(String key) {
            map.remove(key);
            return this;
        }


        public CommonParams build() {
            return new CommonParams(this);
        }
    }
}


