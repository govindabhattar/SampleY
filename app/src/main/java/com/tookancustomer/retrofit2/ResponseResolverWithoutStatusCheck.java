package com.tookancustomer.retrofit2;

import android.app.Activity;

import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tookancustomer.appdata.Codes.StatusCode.INVALID_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Codes.StatusCode.PARSING_ERROR;

/**
 * Custom Retrofit ResponseResolver
 *
 * @param <T>
 */
public abstract class ResponseResolverWithoutStatusCheck<T extends BaseModel> implements Callback<T> {
    private static final String TAG = ResponseResolverWithoutStatusCheck.class.getSimpleName();
    private WeakReference<Activity> weakActivity = null;
    private Boolean showLoading = false;
    private Boolean showError = false;

    private final String NO_INTERNET_MESSAGE = "No internet connection. Please try again later.";
    private final String REMOTE_SERVER_FAILED_MESSAGE = "Something went wrong. Please try again later";
    public final static String UNEXPECTED_ERROR_OCCURRED = "Something went wrong. Please try again later";


    /**
     * @param activity
     */
    public ResponseResolverWithoutStatusCheck(Activity activity) {
        this.weakActivity = new WeakReference<>(activity);
        this.showError = true;
    }

    public ResponseResolverWithoutStatusCheck() {
    }

    /**
     * @param activity
     * @param showLoading
     */
    public ResponseResolverWithoutStatusCheck(Activity activity, Boolean showLoading) {
        this.weakActivity = new WeakReference<>(activity);
        this.showLoading = showLoading;
        if (showLoading) {
            ProgressDialog.show(activity);
        }
    }


    /**
     * @param activity
     * @param showLoading
     * @param showError
     */
    public ResponseResolverWithoutStatusCheck(Activity activity, Boolean showLoading, Boolean showError) {
        this.weakActivity = new WeakReference<>(activity);
        this.showLoading = showLoading;
        this.showError = showError;
        if (showLoading) {
            ProgressDialog.show(activity);
        }

    }

    public abstract void success(T t);

    public abstract void failure(APIError error);

    @Override
    public void onResponse(Call<T> t, Response<T> tResponse) {
        if (showLoading) {
            ProgressDialog.dismiss();
        }
//        try {
//            int statusCode = new JSONObject(new Gson().toJson(tResponse.body())).getInt("status");
//            if (isSuccess(statusCode)) {
//                success(tResponse.body());
//            } else {
//                APIError apiError = new APIError(new JSONObject(new Gson().toJson(tResponse.body())).getInt("status"),
//                        new JSONObject(new Gson().toJson(tResponse.body())).getString("message"));
//                fireError(apiError);
//            }
//        } catch (Exception e) {
//            if (BuildConfig.DEBUG)
//        Utils.printStackTrace(e);
//            APIError apiError = new APIError(PARSING_ERROR.getStatusCode(), Constants.StatusDescription.PARSING_ERROR.getMessage(weakActivity.get()));
//            fireError(apiError);
//        }

        Log.e(TAG, "success: " + "\t" + tResponse.code());

        try {
            resolve(t, tResponse);

        } catch (Throwable e) {


                               Utils.printStackTrace(e);
            Log.e(TAG, "success: " + e.getMessage() + "\t" + tResponse.code());

            // Pipeline the flow of Control
            APIError apiError = new APIError(PARSING_ERROR.getStatusCode(), Constants.StatusDescription.PARSING_ERROR.getMessage(weakActivity.get()));
            fireError(apiError);

        }

    }

    @Override
    public void onFailure(Call<T> t, Throwable throwable) {
        Log.e(TAG, "success: " + "\t" + throwable.getCause());
        if (showLoading) {
            ProgressDialog.dismiss();
        }
        fireError(new APIError(900, resolveNetworkError(throwable)));
    }


    private void resolve(Call<T> t, Response<T> tResponse) {
        if (tResponse.isSuccessful() && tResponse.body() != null) {

            BaseModel baseModel = tResponse.body();

            Codes.StatusCode status = Codes.StatusCode.get(baseModel.getStatus());
            APIError apiError = new APIError(baseModel.getStatus(), baseModel.getMessage());
            switch (status) {

                case ACTION_COMPLETE:
                    success(tResponse.body());
                    break;
                case NONE:
                    success(tResponse.body());
                    break;
                case INVALID_ACCESS_TOKEN:
                    fireError(apiError);
                    AppManager.getInstance().invalidateSession(weakActivity.get(), true, baseModel.getMessage());
                    break;
                case PARAMETER_MISSING:
                case SHOW_ERROR_MESSAGE:
                    fireError(apiError);
                    break;
                default:
                    fireError(apiError);
                    break;
            }


        } else {
            APIError apiError = new APIError(Codes.StatusCode.EXECUTION_ERROR.getStatusCode(), StorefrontCommonData.getString(weakActivity.get(), R.string.remote_server_failed_error));
            fireError(apiError);
        }
    }


    /**
     * @param apiError
     */
    public void fireError(APIError apiError) {
        if (showError) {
            //  checkAuthorizationError(apiError);
            if (weakActivity.get() != null) {
                if (checkAuthorizationError(apiError)) {
                    Utils.snackBar(weakActivity.get(), apiError.getMessage(), false);
                    //    new AlertDialog.Builder(weakActivity.get()).message(apiError.getMessage()).build().show();
                }
            }

        }

        failure(apiError);
    }


    /**
     * @param apiError
     * @return
     */
    public Boolean checkAuthorizationError(APIError apiError) {

        if (apiError.getStatusCode() == INVALID_ACCESS_TOKEN.getStatusCode()) {

            return false;
        }
        return true;
    }

    /**
     * Method resolve network errors
     *
     * @param cause
     * @return
     */

    private String resolveNetworkError(Throwable cause) {
        Log.e("resolveNetworkError =", String.valueOf(cause.toString()));

        if (cause instanceof UnknownHostException) {
            return StorefrontCommonData.getString(weakActivity.get(), R.string.no_internet_try_again);
        } else if (cause instanceof SocketTimeoutException) {
            return StorefrontCommonData.getString(weakActivity.get(), R.string.socket_timeout_connection);
        } else if (cause instanceof ConnectException) {
            return StorefrontCommonData.getString(weakActivity.get(), R.string.remote_server_failed_message);
        } else if (cause instanceof IllegalStateException) {
            return StorefrontCommonData.getString(weakActivity.get(), R.string.parse_exception);
        }
        return StorefrontCommonData.getString(weakActivity.get(), R.string.unexpected_error_occurred);
    }

    private boolean isSuccess(final int statusCode) {
        boolean success = false;
        switch (statusCode) {
            case 200:
                success = true;
                break;
            default:
                success = false;
                break;
        }

        return success;
    }


}
