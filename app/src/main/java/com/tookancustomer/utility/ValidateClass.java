package com.tookancustomer.utility;

import android.app.Activity;
import android.widget.EditText;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;


/**
 * Created by Shweta on 11/23/15.
 */


public class ValidateClass {
    // Validation length constants
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 25;
    public static final int FULL_NAME_MAX_LENGTH = 50;
    public static final int PASSWORD_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 25;
    public static final int REFERRAL_MAX_LENGTH = 6;
    public static final int PHONE_NO_LENGTH_MIN = 6;
    public static final int PHONE_NO_MAX_LENGTH_MIN = 15;
    public static final int COUNTRYCODE_LENGTH = 2;
    private Activity activity;

    public ValidateClass(Activity activity) {
        this.activity = activity;
    }

    public void showError(EditText et, String message) {
        et.setHovered(true);
        et.requestFocus();
        Utils.snackBar(activity, message);

        //  et.setError("Field Empty");
        //  new AlertDialog.Builder(activity).message(message).build().show();
    }


    // restrict empty fields and multiple spaces between characters
    public Boolean genericEmpty(EditText et, String message) {
        if (et.getText().toString().trim().isEmpty()) {
            showError(et, message);
            return true;
        }

//        if (et.getText().toString().trim().contains("  ")) {
//            showError(activity, et, activity.getString(R.string.multiple_spacing_not_valid));
//            return true;
//        }
        return false;
    }


    // validateDataAtAddOnsPage first name
    public Boolean checkName(EditText et) {
        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.name_field_required)))
            return false;

        if (et.getText().toString().trim().length() < NAME_MIN_LENGTH) {
            showError(et, StorefrontCommonData.getString(activity, R.string.name_required_2_char));
            return false;
        }

        //It takes alphabets and spaces and dots...
        if (!et.getText().toString().trim().matches("^[\\p{L} .'-]+$")) {
            showError(et, StorefrontCommonData.getString(activity, R.string.name_field_invalid));
            return false;
        }
        return true;
    }

    // validateDataAtAddOnsPage first name
    public Boolean checkFirstName(EditText et) {
        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.first_name_field_required)))
            return false;

        if (et.getText().toString().trim().length() < NAME_MIN_LENGTH) {
            showError(et, StorefrontCommonData.getString(activity, R.string.first_name_required_2_char));
            return false;
        }

        //It takes alphabets and spaces and dots...
        if (!et.getText().toString().trim().matches("^[\\p{L} .'-]+$")) {
            showError(et, StorefrontCommonData.getString(activity, R.string.first_name_field_invalid));
            return false;
        }
        return true;
    }

    // validateDataAtAddOnsPage last name
    public Boolean checkLastName(EditText et) {
        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.last_name_field_required)))
            return false;

        if (et.getText().toString().trim().length() < NAME_MIN_LENGTH) {
            showError(et, StorefrontCommonData.getString(activity, R.string.last_name_required_2_char));
            return false;
        }

        //It takes alphabets only...
        if (!et.getText().toString().trim().matches("\\p{L}+")) {
            showError(et, StorefrontCommonData.getString(activity, R.string.last_name_field_invalid));
            return false;
        }
        return true;
    }

    // To validateDataAtAddOnsPage email address here, email addresses like abc.@gmail.com,
    // .abc@gmail.com, abc..xyz@gmail.com are also restricted apart from EMAIL_ADDRESS regex
    public Boolean checkEmail(EditText et, String emptyString) {
        String email = et.getText().toString().trim();

        if (genericEmpty(et, emptyString))
            return false;


        Boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!b || email.split("@")[0].endsWith(".") || email.startsWith(".") || email.contains("..")) {
            showError(et, StorefrontCommonData.getString(activity, R.string.email_field_invalid));
            return false;
        }
        return true;
    }


    // To validateDataAtAddOnsPage email address here, email addresses like abc.@gmail.com,
    // .abc@gmail.com, abc..xyz@gmail.com are also restricted apart from EMAIL_ADDRESS regex
    public Boolean checkEmailPhoneNumber(EditText et) {
        String emailPhone = et.getText().toString().trim();

        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.email_phone_field_invalid)))
            return false;

        int counter = 0;
        for (int i = 0; i < emailPhone.length(); i++) {
            if (Character.isDigit(emailPhone.charAt(i))) {
                counter++;
            }
        }
        if (counter == emailPhone.length()) {
            if (et.getText().toString().trim().length() < PHONE_NO_LENGTH_MIN) {
                showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
                return false;
            }

            int counterZero = 0;
            for (int i = 0; i < emailPhone.length(); i++) {
                if (emailPhone.charAt(i) == '0') {
                    counterZero++;
                }
            }
            if (counterZero == emailPhone.length()) {
                showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
                return false;
            }
        } else {
            Boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches();
            if (!b || emailPhone.split("@")[0].endsWith(".") || emailPhone.startsWith(".") || emailPhone.contains("..")) {
                showError(et, StorefrontCommonData.getString(activity, R.string.email_field_invalid));
                return false;
            }

        }
        return true;
    }


    // validateDataAtAddOnsPage password, genericEmpty() not called because we do not want to trim password and also multiple spaces are allowed in password
    public Boolean checkPasswordString(EditText et, String messageEmpty, String messageInvalid) {
        if (et.getText().toString().equals("")) {
            showError(et, messageEmpty);
            return false;
        }
        if (et.getText().toString().length() < PASSWORD_LENGTH) {
            showError(et, messageInvalid);
            return false;
        }
        return true;
    }

    //match any 2 edit text fields, here we are matching passwords
    public Boolean doPasswordsMatch(EditText etPassword, EditText etConfirmPassword, String message) {
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            showError(etConfirmPassword, message);
            return false;
        }
        return true;
    }

    //match any 2 edit text fields, here we are matching passwords
    public Boolean checkOldNewPassword(EditText etPassword, EditText etConfirmPassword, String message) {
        if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            showError(etConfirmPassword, message);
            return false;
        }
        return true;
    }


    public Boolean checkCountryCode(EditText et) {
        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.countrycode_field_required)))
            return false;

        if (et.getText().toString().trim().length() < COUNTRYCODE_LENGTH) {
            showError(et, StorefrontCommonData.getString(activity, R.string.countrycode_field_invalid));
            return false;
        }
        return true;
    }

    public Boolean checkPhoneNumber(EditText et) {
        if (genericEmpty(et, StorefrontCommonData.getString(activity, R.string.phone_field_required)))
            return false;

        if (et.getText().toString().startsWith("0"))  {
            showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
            return false;
        }

        if (et.getText().toString().trim().length() < PHONE_NO_LENGTH_MIN) {
            showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
            return false;
        }

        String s = et.getText().toString().trim();
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                counter++;
            }
        }
        if (counter == s.length()) {
            showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
            return false;
        }

        if (s.contains("+")) {
            showError(et, StorefrontCommonData.getString(activity, R.string.phone_field_invalid));
            return false;
        }
        return true;
    }

    public Boolean checkIfPhoneNumber(String string) {
        if (string == null)
            return false;

        try {
            String[] phoneNumber = string.replace("-", " ").trim().split(" ");

            if (phoneNumber.length == 2) {
                string = phoneNumber[1];
            }
        } catch (Exception e) {
        }

        if (string.trim().isEmpty()) {
            return false;
        }

        int counter = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                counter++;
            }
        }

        if (counter == string.length()) {
            if (string.trim().length() < 3) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }


}