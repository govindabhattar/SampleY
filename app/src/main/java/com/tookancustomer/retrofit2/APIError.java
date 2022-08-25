package com.tookancustomer.retrofit2;

import com.google.gson.Gson;

/**
 * APIError
 */
public class APIError {

    private int statusCode;
    private int code;
    private String message;
    private String error;
    private String error_message;
    private String status;
    public Object data;
    private Data datanew;
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @param statusCode
     * @param message
     */
    public APIError(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public APIError(int statusCode, String message, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public APIError(int statusCode, String message, Data datanew) {
        this.message = message;
        this.statusCode = statusCode;
        this.datanew = datanew;
    }



    /**
     * @return
     */
    public int getStatusCode() {
        if (statusCode == 0)
            if (code != 0)
                return code;
            else
                statusCode = 900;
        return statusCode;
    }


    /**
     * @return
     */
    public String getMessage() {
        if (message == null) {
            if (error != null)
                return error;
            else if (error_message != null)
                return error_message;
            else
                return ResponseResolver.UNEXPECTED_ERROR_OCCURRED;
        } else
            return message;
    }


    /**
     * @return
     */
    public boolean isEmptyObject() {
        if (statusCode == 0 && code == 0 && message == null && error == null && error_message == null && status == null && data == null)
            return true;
        else
            return false;
    }


    /**
     * Get data model.
     *
     * @param classRef
     * @param <T>
     * @return
     */
    public <T> T toResponseModel(final Class<T> classRef) {
        return new Gson().fromJson(new Gson().toJson(data), classRef);
    }
}
