package com.tookancustomer.retrofit2;

import android.app.Activity;

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
public abstract class ResponseResolver<T extends BaseModel> implements Callback<T> {
    public final static String UNEXPECTED_ERROR_OCCURRED = "Something went wrong. Please try again later";
    private static final String TAG = ResponseResolver.class.getSimpleName();
    private final String NO_INTERNET_MESSAGE = "No internet connection. Please try again later.";
    private final String REMOTE_SERVER_FAILED_MESSAGE = "Something went wrong. Please try again later";
    private WeakReference<Activity> weakActivity = null;
    private Boolean showLoading = false;
    private Boolean showError = false;


    /**
     * @param activity
     */
    public ResponseResolver(Activity activity) {
        this.weakActivity = new WeakReference<>(activity);
        this.showError = true;
    }

    public ResponseResolver() {
    }

    /**
     * @param activity
     * @param showLoading
     */
    public ResponseResolver(Activity activity, Boolean showLoading) {
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
    public ResponseResolver(Activity activity, Boolean showLoading, Boolean showError) {
        this.weakActivity = new WeakReference<>(activity);
        this.showLoading = showLoading;
        this.showError = showError;
        if (showLoading) {
            ProgressDialog.show(activity);
        }

    }

    public abstract void success(T t);

    public abstract void failure(APIError error, T t);

//    public abstract void failure(APIError error);

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
//                                Utils.printStackTrace(e);
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

            APIError apiError;

//            if (status == SHOW_REQUIRED_CATALOUGE) {
//
//                apiError = new APIError(PARSING_ERROR.getStatusCode(), Constants.StatusDescription.PARSING_ERROR.getMessage(weakActivity.get()),
//                        tResponse.body());
//
//            } else {
//
//                apiError = new APIError(baseModel.getStatus(), baseModel.getMessage());
//
//            }
            apiError = new APIError(baseModel.getStatus(), baseModel.getMessage(),
                    tResponse.body());


            switch (status) {

                case ACTION_COMPLETE:

                    success(tResponse.body());
                    break;
                case INVALID_ACCESS_TOKEN:
                    if (showError) {
                        fireError(apiError);
                        AppManager.getInstance().invalidateSession(weakActivity.get(), true, baseModel.getMessage());
                    }else{
                        failure(apiError, null);
                    }
                    break;
                case PARAMETER_MISSING:
                case SHOW_ERROR_MESSAGE:
                case SHOW_REQUIRED_CATALOUGE:

                    if (tResponse.body() != null) {
                        fireError(apiError, tResponse.body());
                        break;
                    }

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

        failure(apiError, null);
    }

    public void fireError(APIError apiError, T t) {
        if (showError) {
            //  checkAuthorizationError(apiError);
            if (weakActivity.get() != null) {
                if (checkAuthorizationError(apiError)) {
                    Utils.snackBar(weakActivity.get(), apiError.getMessage(), false);
                    //    new AlertDialog.Builder(weakActivity.get()).message(apiError.getMessage()).build().show();
                }
            }

        }

        failure(apiError, t);
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
