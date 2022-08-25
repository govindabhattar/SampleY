package com.tookancustomer;

import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.Utils;


public class ConfirmAddressActivity extends BaseActivity implements View.OnClickListener {
    // public String homeLandmark, homeHouse, workLandmark, workHouse, otherLandmark, otherHouse, otherLabel;
    public String homeLandmark, homeHouse, workLandmark, workHouse, otherLandmark = "", otherHouse = "", otherPinCode = "", otherLabel = "",
            workPinCode = "", homePinCode = "";
    public LatLng homeLatLng, workLatLng, otherLatLng;
    TextView tvHome, tvWork, tvOther, tvLabel;
    RelativeLayout rlHome, rlWork, rlOther;
    EditText etLabel, etAddress, etFlat, etLandmark, etPostalCode;
    TextView tvConfirm;
    int locationType;
    View vwLabel;
    boolean allFieldValid;
    RelativeLayout rlBack;
    int otherListSize;
    private boolean isHomeAdded = false, isWorkAdded = false;
    private boolean isHomeUpdate = false, isWorkUpdate = false, isOtherUpdate = false;
    private TextView tvCustomAddress, tvAddCustomAddress;
    private EditText etCustomAddress;

    //is_custom_address:1

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address);
        mActivity = this;
        Bundle bundle = this.getIntent().getExtras();
        locationType = getIntent().getIntExtra("locationType", -1);
        otherListSize = getIntent().getIntExtra("otherListSize", 0);
        isHomeAdded = getIntent().getBooleanExtra(IS_HOME_ADDED, false);
        isWorkAdded = getIntent().getBooleanExtra(IS_WORK_ADDED, false);
        isHomeUpdate = getIntent().getBooleanExtra("isHomeUpdate", false);
        isWorkUpdate = getIntent().getBooleanExtra("isWorkUpdate", false);
        isOtherUpdate = getIntent().getBooleanExtra("isOtherUpdate", false);

        homeHouse = getIntent().getStringExtra("homeFlatNumber");
        homeLandmark = getIntent().getStringExtra("homeLandMark");
        homeLatLng = getIntent().getParcelableExtra("homeLatlng");
        homePinCode = getIntent().getStringExtra("homePinCode");


        workHouse = getIntent().getStringExtra("workFlatNumber");
        workLandmark = getIntent().getStringExtra("workLandMark");
        workLatLng = getIntent().getParcelableExtra("workLatlng");
        workPinCode = getIntent().getParcelableExtra("workPinCode");


        //  otherHouse = getIntent().getStringExtra("otherFlatNumber");
        // otherLabel = getIntent().getStringExtra("otherLabel");
        //    otherLandmark = getIntent().getStringExtra("otherLandMark");
        otherLatLng = getIntent().getParcelableExtra("otherLatlng");

        try {
            otherHouse = getIntent().getStringExtra("otherFlatNumber");
            otherLabel = getIntent().getStringExtra("otherLabel");
            otherLandmark = getIntent().getStringExtra("otherLandMark");
            otherPinCode = getIntent().getStringExtra("otherPinCode");

            homeHouse = getIntent().getStringExtra("homeFlatNumber");
            // homeLabel = getIntent().getStringExtra("otherLabel");
            homeLandmark = getIntent().getStringExtra("homeLandMark");
            homePinCode = getIntent().getStringExtra("homePinCode");

            workHouse = getIntent().getStringExtra("workFlatNumber");
            //workLabel = getIntent().getStringExtra("otherLabel");
            workLandmark = getIntent().getStringExtra("workLandMark");
            workPinCode = getIntent().getStringExtra("workPinCode");
        } catch (Exception e) {
            e.printStackTrace();
        }


        init();
        setStrings();

        if (otherListSize >= 8) {
            rlOther.setVisibility(View.GONE);
        } else {
            rlOther.setVisibility(View.VISIBLE);
        }
        if (isHomeUpdate) {
            rlHome.setVisibility(View.VISIBLE);
            rlWork.setVisibility(View.GONE);
            rlOther.setVisibility(View.GONE);
        } else if (isWorkUpdate) {
            rlWork.setVisibility(View.VISIBLE);
            rlHome.setVisibility(View.GONE);
            rlOther.setVisibility(View.GONE);
        } else if (isOtherUpdate) {
            rlOther.setVisibility(View.VISIBLE);
            rlHome.setVisibility(View.GONE);
            rlWork.setVisibility(View.GONE);
        }
    }

    public void init() {
        tvAddCustomAddress = findViewById(R.id.tvAddCustomAddress);
        tvAddCustomAddress.setPaintFlags(tvAddCustomAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvCustomAddress = findViewById(R.id.tvCustomAddress);
        etCustomAddress = findViewById(R.id.etCustomAddress);

        if (StorefrontCommonData.getAppConfigurationData().getIsCustomAddressEnabled() == 1) {
            tvAddCustomAddress.setVisibility(View.VISIBLE);
        } else {
            tvAddCustomAddress.setVisibility(View.GONE);
            tvCustomAddress.setVisibility(View.GONE);
            etCustomAddress.setVisibility(View.GONE);
        }

        tvHome = findViewById(R.id.tvHome);
        tvWork = findViewById(R.id.tvWork);
        tvOther = findViewById(R.id.tvOther);
        rlHome = findViewById(R.id.rlHome);
        rlWork = findViewById(R.id.rlWork);
        rlOther = findViewById(R.id.rlOther);
        tvLabel = findViewById(R.id.tvLabel);
        etLabel = findViewById(R.id.etLabel);
        etAddress = findViewById(R.id.etAddress);
        etFlat = findViewById(R.id.etFlat);
        etLandmark = findViewById(R.id.etLandmark);
        etPostalCode = findViewById(R.id.etPostalCode);
        rlBack = findViewById(R.id.rlBack);
        tvConfirm = findViewById(R.id.tvConfirm);
        vwLabel = findViewById(R.id.vwLabel);

        if (getIntent().getStringExtra("formattedAddress") != null && !getIntent().getStringExtra("formattedAddress").isEmpty()) {
            etAddress.setText(getIntent().getStringExtra("formattedAddress"));
        } else {
            etAddress.setText(getIntent().getStringExtra("flat") + " " +
                    getIntent().getStringExtra("landmark") + " " + getIntent().getStringExtra("address"));
        }
        if (isOtherUpdate) {
            etLabel.setText(otherLabel);
        }
        /**
         * Don't show text in landmark and flat et-5th sep 2018
         */
        /*etFlat.setText(getIntent().getStringExtra("flat"));
        etLandmark.setText(getIntent().getStringExtra("landmark"));*/

//        if (isHomeUpdate) {
//            etFlat.setText(homeHouse);
//            etLandmark.setText(homeLandmark);
//        } else if (isWorkUpdate) {
//            etFlat.setText(workHouse);
//            etLandmark.setText(workLandmark);
//        } else if (isOtherUpdate) {
//            etLabel.setText(otherLabel);
//            etFlat.setText(otherHouse);
//            etLandmark.setText(otherLandmark);
//        }

        Utils.setOnClickListener(this, rlBack, rlHome, rlWork, rlOther, tvConfirm, tvAddCustomAddress);

        if (locationType == 0) {
            setHomeSelected();
        } else if (locationType == 1) {
            setWorkSelected();
        } else {
            setOtherSelected();
        }
    }

    public void setStrings() {
        ((TextView) findViewById(R.id.tvConfirmAddress)).setText(getStrings(R.string.confirm_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        ((TextView) findViewById(R.id.tvAddress)).setText(StorefrontCommonData.getTerminology().getAddress());
        ((TextView) findViewById(R.id.tvCustomAddress)).setText("Custom Address");
        ((TextView) findViewById(R.id.tvFlatNo)).setText(StorefrontCommonData.getTerminology().getApartmentNo());
        ((TextView) findViewById(R.id.tvLandmark)).setText(StorefrontCommonData.getTerminology().getLandmark());
        ((TextView) findViewById(R.id.tvPostalCode)).setText(StorefrontCommonData.getTerminology().getPostalCode());

        ((TextView) findViewById(R.id.tvConfirm)).setText(getStrings(R.string.done));

        ((TextView) findViewById(R.id.tvHome)).setText(getStrings(R.string.home));
        ((TextView) findViewById(R.id.tvWork)).setText(getStrings(R.string.work));
        ((TextView) findViewById(R.id.tvOther)).setText(getStrings(R.string.other));
        ((TextView) findViewById(R.id.tvLabel)).setText(getStrings(R.string.label));
        ((EditText) findViewById(R.id.etLabel)).setHint(getStrings(R.string.enter_label));
        ((EditText) findViewById(R.id.etCustomAddress)).setHint(getStrings(R.string.enter_custom_address));
        ((EditText) findViewById(R.id.etAddress)).setHint(getStrings(R.string.enter_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        ((EditText) findViewById(R.id.etFlat)).setHint(getStrings(R.string.enter_flat));
        ((EditText) findViewById(R.id.etLandmark)).setHint(getStrings(R.string.enter_landmark_v2).replace(LANDMARK, StorefrontCommonData.getTerminology().getLandmark()));
        ((EditText) findViewById(R.id.etPostalCode)).setHint(getStrings(R.string.enter_postalcode_v2).replace(POSTALCODE, StorefrontCommonData.getTerminology().getPostalCode()));
        if (isOtherUpdate) {
            ((EditText) findViewById(R.id.etFlat)).setText(otherHouse);
            ((EditText) findViewById(R.id.etLandmark)).setText(otherLandmark);
            ((EditText) findViewById(R.id.etPostalCode)).setText(otherPinCode);
        }
        if (isHomeUpdate) {
            ((EditText) findViewById(R.id.etFlat)).setText(homeHouse);
            ((EditText) findViewById(R.id.etLandmark)).setText(homeLandmark);
            ((EditText) findViewById(R.id.etPostalCode)).setText(homePinCode);
        }
        if (isWorkUpdate) {
            ((EditText) findViewById(R.id.etFlat)).setText(workHouse);
            ((EditText) findViewById(R.id.etLandmark)).setText(workLandmark);
            ((EditText) findViewById(R.id.etPostalCode)).setText(workPinCode);
        }


    }

    public void setHomeSelected() {
        unselectAll();
        rlHome.setBackground(getResources().getDrawable(R.drawable.boundary_blue));
        tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
        tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_home_active, 0, 0, 0);
    }

    public void setWorkSelected() {
        unselectAll();

        rlWork.setBackground(getResources().getDrawable(R.drawable.boundary_blue));
        tvWork.setTextColor(getResources().getColor(R.color.colorAccent));
        tvWork.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_work_active, 0, 0, 0);
    }

    public void setOtherSelected() {
        unselectAll();

        rlOther.setBackground(getResources().getDrawable(R.drawable.boundary_blue));
        tvOther.setTextColor(getResources().getColor(R.color.colorAccent));
        tvOther.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_other_active, 0, 0, 0);

        Utils.setVisibility(View.VISIBLE, tvLabel, etLabel, vwLabel);
    }

    public void unselectAll() {
        rlHome.setBackground(getResources().getDrawable(R.drawable.boundary_grey));
        tvHome.setTextColor(getResources().getColor(R.color.sub_text_color_onboard));
        tvHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_home_inactive, 0, 0, 0);

        rlWork.setBackground(getResources().getDrawable(R.drawable.boundary_grey));
        tvWork.setTextColor(getResources().getColor(R.color.sub_text_color_onboard));
        tvWork.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_work_inactive, 0, 0, 0);

        rlOther.setBackground(getResources().getDrawable(R.drawable.boundary_grey));
        tvOther.setTextColor(getResources().getColor(R.color.sub_text_color_onboard));
        tvOther.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm_page_other_inactive, 0, 0, 0);

        Utils.setVisibility(View.GONE, tvLabel, etLabel, vwLabel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                super.onBackPressed();
                break;
            case R.id.tvAddCustomAddress:
                if (tvAddCustomAddress.getText().toString().equals(getStrings(R.string.add_custom_address))) {
                    tvAddCustomAddress.setText(getStrings(R.string.hide_custom_address));
                    tvCustomAddress.setVisibility(View.VISIBLE);
                    etCustomAddress.setVisibility(View.VISIBLE);
                } else {
                    tvAddCustomAddress.setText(getStrings(R.string.add_custom_address));
                    tvCustomAddress.setVisibility(View.GONE);
                    etCustomAddress.setVisibility(View.GONE);
                }

                break;
            case R.id.rlHome:
                if (!isHomeUpdate) {
                    if (isHomeAdded) {
                        new OptionsDialog.Builder(mActivity).message(getStrings(R.string.home_address_already_selected_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress())).listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {
                                allFieldValid = true;
                                isHomeUpdate = true;
                                setHomeSelected();
                                locationType = 0;
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {


                            }
                        }).build().show();
                        // Utils.snackbar(mActivity, getStrings(R.string.home_address_already_selected), etAddress);
                        return;
                    } else {
                        setHomeSelected();
                        locationType = 0;
                    }
                }
                break;
            case R.id.rlWork:
                if (!isWorkUpdate) {
                    if (isWorkAdded) {
                        new OptionsDialog.Builder(mActivity).message(getStrings(R.string.work_address_already_selected_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress())).listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {
                                allFieldValid = true;
                                isWorkUpdate = true;
                                setWorkSelected();
                                locationType = 1;
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                            }
                        }).build().show();
                        //   Utils.snackbar(mActivity, getStrings(R.string.work_address_already_selected), etAddress);
                        return;
                    } else {
                        setWorkSelected();
                        locationType = 1;
                    }
                }
                break;
            case R.id.rlOther:
                setOtherSelected();
                locationType = 2;
                break;
            case R.id.tvConfirm:
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                if (etLabel.getVisibility() == View.VISIBLE) {
                    if (etLabel.getText().toString().isEmpty()) {
                        Utils.snackBar(mActivity, getStrings(R.string.please_add_label_for_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                        return;
                    }
                }
                if (etAddress.getText().toString().isEmpty()) {
                    Utils.snackBar(mActivity, getStrings(R.string.please_select_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                    finish();
                    return;
                }
//                if (isHomeAdded && locationType == 0) {
//                    new OptionsDialog.Builder(mActivity).message(getStrings(R.string.home_address_already_selected)).listener(new OptionsDialog.Listener() {
//                        @Override
//                        public void performPositiveAction(int purpose, Bundle backpack) {
//                            allFieldValid = true;
//                            isHomeUpdate = true;
//                            performBackAction();
//                        }
//
//                        @Override
//                        public void performNegativeAction(int purpose, Bundle backpack) {
//                        }
//                    }).build().show();
//                    // Utils.snackbar(mActivity, getStrings(R.string.home_address_already_selected), etAddress);
//                    return;
//                } else if (isWorkAdded && locationType == 1) {
//                    new OptionsDialog.Builder(mActivity).message(getStrings(R.string.work_address_already_selected)).listener(new OptionsDialog.Listener() {
//                        @Override
//                        public void performPositiveAction(int purpose, Bundle backpack) {
//                            allFieldValid = true;
//                            isWorkUpdate = true;
//                            performBackAction();
//                        }
//
//                        @Override
//                        public void performNegativeAction(int purpose, Bundle backpack) {
//                        }
//                    }).build().show();
//                    //   Utils.snackbar(mActivity, getStrings(R.string.work_address_already_selected), etAddress);
//                    return;
//                }

                if (StorefrontCommonData.getAppConfigurationData().getIsApartmentNoMandatory() == 1) {
                    //apartment
                    if (etFlat.getText().toString().isEmpty()) {
                        Utils.snackBar(mActivity, getStrings(R.string.enter_apartment_v2).replace(TerminologyStrings.APARTMENT, StorefrontCommonData.getTerminology().getApartmentNo()));
                        return;
                    }
                }
                if (StorefrontCommonData.getAppConfigurationData().getIsLandMarkMandatory() == 1) {
                    if (etLandmark.getText().toString().isEmpty()) {
                        Utils.snackBar(mActivity, (getStrings(R.string.enter_landmark_v2).replace(LANDMARK, StorefrontCommonData.getTerminology().getLandmark())));
                        return;
                    }
                }

                if (StorefrontCommonData.getAppConfigurationData().getIsPostalCodeMandatory() == 1) {
                    if (etPostalCode.getText().toString().isEmpty()) {
                        Utils.snackBar(mActivity, (getStrings(R.string.enter_postalcode_v2).replace(POSTALCODE, StorefrontCommonData.getTerminology().getPostalCode())));
                        return;
                    }
                }


                allFieldValid = true;
                performBackAction();
                break;
        }
    }

    public void performBackAction() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        extras.putInt("locationType", locationType);
        if (locationType == 0) {
            extras.putString("label", getStrings(R.string.home));
        } else if (locationType == 1) {
            extras.putString("label", getStrings(R.string.work));
        } else {
            extras.putString("label", etLabel.getText().toString());
        }
        extras.putString("flat", etFlat.getText().toString());
        if (tvCustomAddress.getVisibility() == View.VISIBLE &&
                !tvCustomAddress.getText().toString().isEmpty()) {
            extras.putString("address", etCustomAddress.getText().toString());
            extras.putInt("is_custom_address", 1);
        } else {
            extras.putString("address", etAddress.getText().toString());
        }
        extras.putBoolean("isHomeUpdate", isHomeUpdate);
        extras.putBoolean("isWorkUpdate", isWorkUpdate);
        extras.putBoolean("isOtherUpdate", isOtherUpdate);
        extras.putString("landmark", etLandmark.getText().toString());
        extras.putString("postalcode", etPostalCode.getText().toString());
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void gotoAddAnotherAddressActivity() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.ADD_FROM_MAP_ACTIVITY);
    }

    private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

}