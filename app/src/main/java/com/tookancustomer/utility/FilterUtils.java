package com.tookancustomer.utility;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by cl-macmini-85 on 13/01/17.
 */

public class FilterUtils {

    /**
     * Method to convert string into camel case string
     */
    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

//    //for disabling smileys in edittext box
//    public static InputFilter emojiFilter = new InputFilter() {
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            for (int index = start; index < end; index++) {
//                int type = Character.getType(source.charAt(index));
//                if (type == Character.SURROGATE) {
//                    return "";
//                }
//            }
//            return null;
//        }
//    };

    //for disabling smileys in edittext box
    public static InputFilter emojiFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    };


    /**
     * Filter on editText to block space character
     */
    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = " ";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    public static void setPasswordFilter(final EditText et,final int maxLength) {
        InputFilter passwordFilter = new InputFilter() {
            boolean canEnterSpace = false;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (et.getText().toString().equals("")) {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

//                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
//                        builder.append(currentChar);
//                        canEnterSpace = true;
//                    }
                    if (!Character.isWhitespace(currentChar)) {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }
                    if (Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }


                }
                return builder.toString();
            }
        };
        et.setFilters(new InputFilter[]{passwordFilter, new InputFilter.LengthFilter(maxLength)});
    }


    public static void setEmailFilter(final EditText et) {
        InputFilter passwordFilter = new InputFilter() {
            boolean canEnterSpace = false;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (et.getText().toString().equals("")) {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

//                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
//                        builder.append(currentChar);
//                        canEnterSpace = true;
//                    }
                    if (!Character.isWhitespace(currentChar)) {
                        builder.append(currentChar);
                        canEnterSpace = false;
                    }
                    if (Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }


                }
                return builder.toString();
            }
        };
        et.setFilters(new InputFilter[]{passwordFilter});
    }


}
