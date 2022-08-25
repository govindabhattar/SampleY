package com.tookancustomer.mapfiles;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tookancustomer.BuildConfig;

import java.io.File;
import java.io.IOException;

import static com.tookancustomer.utility.Utils.printStackTrace;


public class Utils {

    public static String GAME_IMAGE_NAME = "userImage.jpg";

    /**
     * Compares two double values with epsilon precision
     *
     * @param d1 double value 1
     * @param d2 double value 2
     * @return 1 if d1 > d2,
     * -1 if d1 < d2 &
     * 0 if d1 == d2
     */
    public static int compareDouble(double d1, double d2) {
        if (d1 == d2) {
            return 0;
        } else {
            double epsilon = 0.0000001;
            if ((d1 - d2) > epsilon) {
                return 1;
            } else if ((d1 - d2) < epsilon) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Hides keyboard
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity, View searchET) {
        try {
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                printStackTrace(e);
        }

    }

    public static void showSoftKeyboard(Activity activity, View searchET) {
        try {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(searchET, 0);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                printStackTrace(e);
        }
    }

    public static File getTempImageFile() {
        File file = new File(
                android.os.Environment.getExternalStorageDirectory(),
                GAME_IMAGE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                if (BuildConfig.DEBUG)
                    printStackTrace(e);
            }
        }
        return file;
    }

}
