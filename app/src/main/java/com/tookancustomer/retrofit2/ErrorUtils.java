package com.tookancustomer.retrofit2;

import android.content.Context;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * ErrorUtils
 */


public class ErrorUtils {

    /**
     *
     * @param response
     * @return
     */
    public static APIError parseError(Response<?> response, Context context) {
        Converter<ResponseBody, APIError> converter = RestClient.getRetrofitBuilder(context).responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try {
            if (response.errorBody() != null) {//&& !converter.convert(response.errorBody()).isEmptyObject()
                error = converter.convert(response.errorBody());
            } else {
                error = new APIError(response.code(), response.message());
            }

        } catch (Exception e) {
            int statusCode = 900;
            String message = StorefrontCommonData.getString(context,R.string.unexpected_error_occurred);
            if (response.code() != 0)
                statusCode = response.code();
            if (response.message() != null && !response.message().isEmpty())
                message = response.message();
            return new APIError(statusCode, message);
        }
        return error;
    }
}
