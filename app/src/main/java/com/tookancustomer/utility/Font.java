package com.tookancustomer.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Class to facilitate the Actions and Event on the {@link EditText}
 *
 * @author Rishabh
 */
public class Font {

    public static final int REGULAR = 0;
    public static final int BOLD = 1;
    public static final int MEDIUM = 2;
    public static final int LIGHT = 3;
    public static final int ULTRA_LIGHT = 4;


    private static Typeface bold;
    private static Typeface semibold;
    private static Typeface medium;
    private static Typeface regular;
    private static Typeface light;
    private static Typeface ultraLight;


    /**
     * Method return a Typeface according to the Type specified
     *
     * @param context
     * @param type
     * @return
     */
    private static Typeface getFont(Context context, int type) {

        switch (type) {

            case BOLD:
                return getBold(context);

            case MEDIUM:
                return getMedium(context);

            case LIGHT:
                return getLight(context);
            case ULTRA_LIGHT:
                return getUltraLight(context);


            default:
                return getRegular(context);
        }
    }

    /**
     * Method to set font to Text inside the {@link EditText}.
     */
    public static void bind(Context context, int type, EditText... editTexts) {

        Typeface font = getFont(context, type);

        for (EditText editText : editTexts)
            editText.setTypeface(font);
    }

    /**
     * Method to set font to Text inside the {@link EditText}.
     */
    public static void bind(Context context, int type, TextView... textViews) {

        Typeface font = getFont(context, type);

        for (TextView textView : textViews)
            textView.setTypeface(font);
    }

    /**
     * Method to set font to Text inside the {@link EditText}.
     */
    public static void bind(Context context, int type, Button... buttons) {

        Typeface font = getFont(context, type);

        for (Button button : buttons)
            button.setTypeface(font);
    }


    public static Typeface getTypeFaceNLevel(Context context,String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    /**
     * Method to set heavy font ( {@link Typeface} ) to the text
     *
     * @param context The {@link Activity} at which the font pulling view is placed
     * @return
     */
    public static Typeface getBold(Context context) {

        if (bold == null)
            bold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        return bold;
    }

    public static Typeface getSemiBold(Context context) {

        if (semibold == null)
            semibold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");

        return semibold;
    }

    /**
     * Method to set heavy font ( {@link Typeface} ) to the text
     *
     * @param context The {@link Activity} at which the font pulling view is placed
     * @return
     */
    public static Typeface getLight(Context context) {

        if (light == null)
            light = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");

        return light;
    }

    /**
     * Method to set medium font ( {@link Typeface} ) to the text
     *
     * @param context The {@link Activity} at which the font pulling view is placed
     * @return
     */
    public static Typeface getMedium(Context context) {

        if (medium == null)
            medium = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf");

        return medium;
    }

    /**
     * Method to set book font ( {@link Typeface} ) to the text
     *
     * @param context The {@link Activity} at which the font pulling view is placed
     * @return
     */
    public static Typeface getRegular(Context context) { // accessing fonts

        if (regular == null)
            regular = Typeface.createFromAsset(context.getAssets(), "fonts/maven_pro_regular.otf");

        return regular;
    }

    public static Typeface getUltraLight(Context context) { // accessing fonts

        if (ultraLight == null)
            ultraLight = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-UltraLight.otf");

        return ultraLight;
    }


}
