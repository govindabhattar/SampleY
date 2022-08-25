package com.tookancustomer.utility;

import android.app.Activity;

import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;

public class ErrorLogs {

    public static void errorLog(final Activity mActivity, String message) {

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("customer_side_error", "1");
        commonParams.add("app_type", "ANDROID");
        commonParams.add("error_message", message);
        commonParams.add("abc", 3); //3 for android
        if (StorefrontCommonData.getUserData() != null) {
            commonParams.add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
            commonParams.add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        }
        commonParams.add("screen", mActivity.getClass().getSimpleName());
        RestClient.getApiInterface(mActivity).errorLog(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                Log.d("response----------", "");
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Log.d("response----------", "false_error");
            }
        });
    }
}
