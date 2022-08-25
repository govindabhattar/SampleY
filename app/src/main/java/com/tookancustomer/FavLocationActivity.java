package com.tookancustomer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.adapters.OtherLocationsAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.favLocations.FavouriteLocations;
import com.tookancustomer.models.favLocations.Locations;
import com.tookancustomer.models.favLocations.LocationsModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavLocationActivity extends BaseActivity {
    public String address;
    public double latitude, longitude;
    public LatLng latLng;
    public String addressType, flat, landmark, postalCode;
    public boolean isAddLocation;
    public int locationType; //0 for home, 1 for work, 2 for other
    public boolean isPickup, fromAccountScreen = false;
    public Integer fav_id;
    public boolean isHomeUpdate = false, isWorkUpdate = false, isOtherUpdate = false;
    public String homeLandmark, homeHouse, homePinCode, workLandmark, workHouse, workPinCode, otherLandmark, otherHouse, otherPinCode, otherLabel;
    public LatLng homeLatLng, workLatLng, otherLatLng;
    public View otherAnchorView;
    public int locType;
    TextView tvAddHomeAddress, tvAddWorkAddress, tvAddOtherAddress, tvSearchLocation, tvNoIntermet, tvFavouriteHeader;
    boolean isHomeAdded, isWorkAdded;
    RelativeLayout rlBack;
    ImageButton ibHomeOptions, ibWorkOptions;
    TextView tvHomeAddress, tvWorkAddress, tvHeading;
    LinearLayout llHomeAddress, llWorkAddress;
    Integer homeFavId, workFavId, otherFavId;
    String homeAddress, workAddress;
    double homeLatitude, homeLongitude, workLatitude, workLongitude;
    RelativeLayout rlHomeLocation, rlWorkLocation;
    android.widget.Button btAddAnotherAddress;
    RecyclerView rvOtherLocations;
    List<Locations> otherLocationList = new ArrayList<>();
    View vwOne, vwTwo;
    PopupMenu popup;
    private int isCustomAddress = 0;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_location);
        mActivity = this;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isPickup = bundle.getBoolean("isPickup");
        }
        fromAccountScreen = getIntent().getBooleanExtra(FROM_ACCOUNT_SCREEN, false);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.internetCheck(mActivity)) {
            tvNoIntermet.setVisibility(View.VISIBLE);
            tvAddOtherAddress.setVisibility(View.GONE);
            rvOtherLocations.setVisibility(View.GONE);
            tvAddWorkAddress.setVisibility(View.GONE);
            rlWorkLocation.setVisibility(View.GONE);
            rlHomeLocation.setVisibility(View.GONE);
            tvFavouriteHeader.setVisibility(View.GONE);
            vwOne.setVisibility(View.GONE);
            vwTwo.setVisibility(View.GONE);
        } else {
            tvNoIntermet.setVisibility(View.GONE);
        }
    }

    public void init() {
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        rvOtherLocations = findViewById(R.id.rvOtherLocations);
        rvOtherLocations.setLayoutManager(new LinearLayoutManager(mActivity));
        rvOtherLocations.setNestedScrollingEnabled(false);
        tvAddWorkAddress = findViewById(R.id.tvAddWorkAddress);
        tvAddWorkAddress.setText(getStrings(R.string.work_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        tvAddHomeAddress = findViewById(R.id.tvAddHomeAddress);
//        tvAddHomeAddress.setText(getStrings(R.string.home_address));
        tvAddHomeAddress.setText(StorefrontCommonData.getTerminology().getHomeAddress());
        tvAddOtherAddress = findViewById(R.id.tvAddOtherAddress);
        tvAddOtherAddress.setText(getStrings(R.string.other_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        tvFavouriteHeader = findViewById(R.id.tvFavouriteHeader);
        tvFavouriteHeader.setText(getStrings(R.string.favorites));
        tvSearchLocation = findViewById(R.id.tvSearchLocation);
        tvSearchLocation.setHint(getStrings(R.string.search_your_address));

        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.my_addresses_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
        tvNoIntermet = findViewById(R.id.tvNoIntermet);
        tvNoIntermet.setText(getStrings(R.string.no_internet_try_again));
        rlBack = findViewById(R.id.rlBack);
        rlHomeLocation = findViewById(R.id.rlHomeLocation);
        rlWorkLocation = findViewById(R.id.rlWorkLocation);
        ibHomeOptions = findViewById(R.id.ibHomeOptions);
        ibWorkOptions = findViewById(R.id.ibWorkOptions);
        tvHomeAddress = findViewById(R.id.tvHomeAddress);
        tvWorkAddress = findViewById(R.id.tvWorkAddress);
        llHomeAddress = findViewById(R.id.llHomeAddress);
        llWorkAddress = findViewById(R.id.llWorkAddress);
        btAddAnotherAddress = findViewById(R.id.btAddAnotherAddress);
        btAddAnotherAddress.setText(getStrings(R.string.select_from_map));

        ((TextView) findViewById(R.id.tvHome)).setText(getStrings(R.string.home));
        ((TextView) findViewById(R.id.tvWork)).setText(getStrings(R.string.work));

        btAddAnotherAddress.setVisibility(fromAccountScreen ? View.GONE : View.VISIBLE);
        tvSearchLocation.setVisibility(fromAccountScreen ? View.GONE : View.VISIBLE);
        tvHeading.setVisibility(fromAccountScreen ? View.VISIBLE : View.GONE);


        vwOne = findViewById(R.id.vwOne);
        vwTwo = findViewById(R.id.vwTwo);
        ibHomeOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_id = homeFavId;
                addressType = getStrings(R.string.home);
                locType = 0;
                isAddLocation = true;
                locationType = 0;
                showAddressOptions();
            }
        });
        ibWorkOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_id = workFavId;
                addressType = getStrings(R.string.work);
                locType = 1;
                isAddLocation = true;
                locationType = 1;
                showAddressOptions();
            }
        });

        if (!fromAccountScreen) {
            llHomeAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    address = homeAddress;
//                    address = getStrings(R.string.home);
                    latitude = homeLatitude;
                    longitude = homeLongitude;
                    latLng = homeLatLng;
                    performBackAction(((TextView) findViewById(R.id.tvHome)).getText().toString().trim());
                }
            });
            llWorkAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    address = workAddress;
//                    address = getStrings(R.string.work);
                    latitude = workLatitude;
                    longitude = workLongitude;
                    latLng = workLatLng;
                    performBackAction(((TextView) findViewById(R.id.tvWork)).getText().toString().trim());
                }
            });
        }
        tvSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                Utils.searchPlace(mActivity);
                isAddLocation = false;
            }
        });
        tvAddHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = getStrings(R.string.home);
                locType = 0;
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                isAddLocation = true;
                locationType = 0;
                isHomeUpdate = false;
                isWorkUpdate = false;
                isOtherUpdate = false;
                gotoAddAnotherAddressActivity();
            }
        });
        tvAddWorkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = getStrings(R.string.work);
                locType = 1;
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                isAddLocation = true;
                locationType = 1;
                isHomeUpdate = false;
                isWorkUpdate = false;
                isOtherUpdate = false;
                gotoAddAnotherAddressActivity();
            }
        });
        tvAddOtherAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = getStrings(R.string.other);
                locType = 2;
                isOtherUpdate = false;
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                isAddLocation = true;
                locationType = 2;
                isHomeUpdate = false;
                isWorkUpdate = false;
                isOtherUpdate = false;
                gotoAddAnotherAddressActivity();
            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btAddAnotherAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddLocation = false;
                gotoAddAnotherAddressActivity();
            }
        });

        startShimmerAnimation(shimmerFrameLayout);
        getFavouriteLocations(false);
    }

    public void showAddressOptions() {
        //Creating the instance of PopupMenu
        if (locationType == 0)
            popup = new PopupMenu(FavLocationActivity.this, ibHomeOptions);
        else if (locationType == 1)
            popup = new PopupMenu(FavLocationActivity.this, ibWorkOptions);
        else if (locationType == 2)
            popup = new PopupMenu(FavLocationActivity.this, otherAnchorView);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_address_options, popup.getMenu());
        popup.setGravity(Gravity.BOTTOM);

        MenuItem itemView = popup.getMenu().findItem(R.id.itemEdit);
        itemView.setTitle(getStrings(R.string.edit));
        MenuItem itemDelete = popup.getMenu().findItem(R.id.itemDelete);
        itemDelete.setTitle(getStrings(R.string.delete));

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(getStrings(R.string.edit))) {
                    new OptionsDialog.Builder(mActivity).message(getStrings(R.string.sure_to_edit_this_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress())).listener(new OptionsDialog.Listener() {
                        @Override
                        public void performPositiveAction(int purpose, Bundle backpack) {
                            if (locationType == 0) {
                                isHomeUpdate = true;
                                isWorkUpdate = false;
                                isOtherUpdate = false;
                            } else if (locationType == 1) {
                                isHomeUpdate = false;
                                isWorkUpdate = true;
                                isOtherUpdate = false;
                            } else if (locationType == 2) {
                                isHomeUpdate = false;
                                isWorkUpdate = false;
                                isOtherUpdate = true;
                            }
                            gotoAddAnotherAddressActivity();
                        }

                        @Override
                        public void performNegativeAction(int purpose, Bundle backpack) {
                        }
                    }).build().show();
                }
                if (item.getTitle().equals(getStrings(R.string.delete))) {
                    deleteFavouriteLocation();
                }
                //Toast.makeText(FavLocationActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    public void gotoAddAnotherAddressActivity() {
        Bundle extras = new Bundle();
        if (tvAddHomeAddress.getVisibility() == View.VISIBLE) {
            extras.putBoolean("isHomeAdded", false);
        } else {
            extras.putBoolean("isHomeAdded", true);
        }
        if (tvAddWorkAddress.getVisibility() == View.VISIBLE) {
            extras.putBoolean("isWorkAdded", false);
        } else {
            extras.putBoolean("isWorkAdded", true);
        }
        extras.putBoolean("isHomeUpdate", isHomeUpdate);
        extras.putBoolean("isWorkUpdate", isWorkUpdate);
        extras.putBoolean("isOtherUpdate", isOtherUpdate);
        extras.putString("homeFlatNumber", homeHouse);
        extras.putParcelable("homeLatlng", homeLatLng);
        extras.putString("homeLandMark", homeLandmark);
        extras.putString("workFlatNumber", workHouse);
        extras.putParcelable("workLatlng", workLatLng);
        extras.putString("workLandMark", workLandmark);
        extras.putString("otherFlatNumber", otherHouse);
        extras.putString("otherLabel", otherLabel);
        extras.putParcelable("otherLatlng", otherLatLng);
        extras.putString("otherLandMark", otherLandmark);
        extras.putInt("otherListSize", otherLocationList.size());
        extras.putBoolean("isAddLocation", isAddLocation);
        extras.putInt("locationType", locationType);
        extras.putString("homePinCode", homePinCode);
        extras.putString("workPinCode", workPinCode);
        extras.putString("otherPinCode", otherPinCode);


        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.ADD_FROM_MAP_ACTIVITY);
    }

    private LatLng getCurrentLocation() {
        Location location = LocationUtils.getLastLocation(this);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity, findViewById(R.id.rlBack));
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String address = bundle.getString(Keys.Extras.ADDRESS);
                        com.tookancustomer.mapfiles.placeapi.Location location = bundle.getParcelable(com.tookancustomer.mapfiles.placeapi.Location.class.getName());

                        if (location != null) {
                            this.address = address + "";
                            latitude = location.getLatLng().latitude;
                            longitude = location.getLatLng().longitude;
                            latLng = location.getLatLng();
                            if (isAddLocation) {
                                addFavouriteLocation();
                            } else {
                                performBackAction("");
                            }
                        }
                    }

//                    Place place = PlaceAutocomplete.getPlace(this, data);
//                    address = place.getAddress() + "";
//                    latitude = place.getLatLng().latitude;
//                    longitude = place.getLatLng().longitude;
//                    latLng = place.getLatLng();
//                    if (isAddLocation) {
//                        addFavouriteLocation();
//                    } else {
//                        performBackAction();
//                    }
                }
                /*else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    // TODO: Handle the error.
                }*/
                else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case Codes.Request.ADD_FROM_MAP_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (!isAddLocation) {
                        latitude = data.getDoubleExtra("latitude", 0.0);
                        longitude = data.getDoubleExtra("longitude", 0.0);
                        address = data.getStringExtra("address");
                        latLng = data.getParcelableExtra("latLng");
                        performBackAction("");
                    } else {
                        locType = data.getIntExtra("locationType", 0);

                        address = data.getStringExtra("address");
                        latitude = data.getDoubleExtra("latitude", 0.0);
                        longitude = data.getDoubleExtra("longitude", 0.0);
                        addressType = data.getStringExtra("label");
                        flat = data.getStringExtra("flat");
                        isCustomAddress = data.getIntExtra("is_custom_address", 0);
                        landmark = data.getStringExtra("landmark");
                        postalCode = data.getStringExtra("postalcode");
                        isHomeUpdate = data.getBooleanExtra("isHomeUpdate", false);
                        isWorkUpdate = data.getBooleanExtra("isWorkUpdate", false);
                        isOtherUpdate = data.getBooleanExtra("isOtherUpdate", false);
                        if (isHomeUpdate || isWorkUpdate || isOtherUpdate) {
                            editFavouriteLocation();
                        } else {
                            addFavouriteLocation();
                        }
                    }
                }
                break;
        }
    }

    public void performBackAction(String label) {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
        extras.putDouble("latitude", latitude);
        extras.putDouble("longitude", longitude);
        extras.putString("address", address);
        extras.putParcelable("latLng", latLng);
        extras.putBoolean("isPickup", isPickup);
        extras.putString("label", label);
        Prefs.with(mActivity).save(Keys.Prefs.ADDRESS, address);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void getFavouriteLocations() {
        getFavouriteLocations(true);
    }

    public void getFavouriteLocations(boolean showLoading) {
        if (Dependencies.isDemoRunning()) {
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(FavLocationActivity.this).getFavLocations(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(FavLocationActivity.this, showLoading, false) {
            @Override
            public void success(BaseModel baseModel) {
                tvNoIntermet.setVisibility(View.GONE);
                LocationsModel locationsModel = new LocationsModel();
                try {
                    locationsModel.setData(baseModel.toResponseModel(FavouriteLocations.class));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                otherLocationList.clear();
                for (int i = 0; i < locationsModel.getData().getFavouriteLocations().size(); i++) {
                    if (locationsModel.getData().getFavouriteLocations().get(i).getLocType() == 0) {
                        isHomeAdded = true;
                        homeHouse = locationsModel.getData().getFavouriteLocations().get(i).getHouse();
                        homeLandmark = locationsModel.getData().getFavouriteLocations().get(i).getLandmark();
                        homePinCode = locationsModel.getData().getFavouriteLocations().get(i).getPostalCode();


                        tvHomeAddress.setText(
                                ((homeHouse != null && !homeHouse.isEmpty()) ? homeHouse + ", " : "")
                                        + locationsModel.getData().getFavouriteLocations().get(i).getAddress() + ", "
                                        + ((homeLandmark != null && !homeLandmark.isEmpty()) ? homeLandmark + ", " : "")
                                        + ((homePinCode != null && !homePinCode.isEmpty() && !homePinCode.equals("null") ? homePinCode : "")


                                ));

                        if (tvHomeAddress.getText().toString().endsWith(", ")) {
                            tvHomeAddress.setText(tvHomeAddress.getText().toString().substring(0, tvHomeAddress.getText().toString().length() - 2));
                        }


//                        tvHomeAddress.setText(locationsModel.getData().getFavouriteLocations().get(i).getHouse() + "," + locationsModel.getData().getFavouriteLocations().get(i).getAddress() + "," + locationsModel.getData().getFavouriteLocations().get(i).getLandmark());
                        homeFavId = locationsModel.getData().getFavouriteLocations().get(i).getFav_id();
                        homeAddress = tvHomeAddress.getText().toString();
                        homeLatitude = locationsModel.getData().getFavouriteLocations().get(i).getLatitude();
                        homeHouse = locationsModel.getData().getFavouriteLocations().get(i).getHouse();
                        homeLandmark = locationsModel.getData().getFavouriteLocations().get(i).getLandmark();
                        homeLongitude = locationsModel.getData().getFavouriteLocations().get(i).getLongitude();
                        homeLongitude = locationsModel.getData().getFavouriteLocations().get(i).getLongitude();
                        homeLatLng = new LatLng(locationsModel.getData().getFavouriteLocations().get(i).getLatitude(), locationsModel.getData().getFavouriteLocations().get(i).getLongitude());
                    }
                    if (locationsModel.getData().getFavouriteLocations().get(i).getLocType() == 1) {
                        isWorkAdded = true;
                        workHouse = locationsModel.getData().getFavouriteLocations().get(i).getHouse();
                        workLandmark = locationsModel.getData().getFavouriteLocations().get(i).getLandmark();
                        workPinCode = locationsModel.getData().getFavouriteLocations().get(i).getPostalCode();
                        tvWorkAddress.setText(
                                ((workHouse != null && !workHouse.isEmpty()) ? workHouse + ", " : "")
                                        + locationsModel.getData().getFavouriteLocations().get(i).getAddress() + ", "
                                        + ((workLandmark != null && !workLandmark.isEmpty()) ? workLandmark + ", " : "")
                                        + ((workPinCode != null && !workPinCode.isEmpty() && !workPinCode.equals("null") ? workPinCode : "")));
                        if (tvWorkAddress.getText().toString().endsWith(", ")) {
                            tvWorkAddress.setText(tvWorkAddress.getText().toString().substring(0, tvWorkAddress.getText().toString().length() - 2));
                        }

//                        tvWorkAddress.setText(locationsModel.getData().getFavouriteLocations().get(i).getHouse() + "," + locationsModel.getData().getFavouriteLocations().get(i).getAddress() + "," + locationsModel.getData().getFavouriteLocations().get(i).getLandmark());
                        workFavId = locationsModel.getData().getFavouriteLocations().get(i).getFav_id();
                        workAddress = tvWorkAddress.getText().toString();
                        workHouse = locationsModel.getData().getFavouriteLocations().get(i).getHouse();
                        workLandmark = locationsModel.getData().getFavouriteLocations().get(i).getLandmark();
                        workLatitude = locationsModel.getData().getFavouriteLocations().get(i).getLatitude();
                        workLongitude = locationsModel.getData().getFavouriteLocations().get(i).getLongitude();
                        workLatLng = new LatLng(locationsModel.getData().getFavouriteLocations().get(i).getLatitude(), locationsModel.getData().getFavouriteLocations().get(i).getLongitude());
                    }
                    if (locationsModel.getData().getFavouriteLocations().get(i).getLocType() == 2) {
                        Locations locations = new Locations();
                        locations.setFav_id(locationsModel.getData().getFavouriteLocations().get(i).getFav_id());
                        locations.setLatitude(locationsModel.getData().getFavouriteLocations().get(i).getLatitude());
                        locations.setLongitude(locationsModel.getData().getFavouriteLocations().get(i).getLongitude());
                        locations.setAddress(locationsModel.getData().getFavouriteLocations().get(i).getAddress());
                        locations.setDefault_location(locationsModel.getData().getFavouriteLocations().get(i).getDefault_location());
                        locations.setName(locationsModel.getData().getFavouriteLocations().get(i).getName());
                        locations.setVendor_id(locationsModel.getData().getFavouriteLocations().get(i).getVendor_id());
                        locations.setHouse(locationsModel.getData().getFavouriteLocations().get(i).getHouse());
                        locations.setLandmark(locationsModel.getData().getFavouriteLocations().get(i).getLandmark());
                        locations.setPostalCode(locationsModel.getData().getFavouriteLocations().get(i).getPostalCode());
                        otherLocationList.add(locations);
                    }
                }
                setlocationUI();
                rvOtherLocations.setAdapter(new OtherLocationsAdapter(FavLocationActivity.this, otherLocationList, StorefrontCommonData.getUserData()));
                if (otherLocationList.size() > 0) {
                    tvAddOtherAddress.setText(getStrings(R.string.add_another_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                } else {
                    tvAddOtherAddress.setText(getStrings(R.string.other_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
                }
                if (otherLocationList.size() > 7) {
                    tvAddOtherAddress.setVisibility(View.GONE);
                } else {
                    tvAddOtherAddress.setVisibility(View.VISIBLE);
                }
                stopShimmerAnimation(shimmerFrameLayout);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                tvNoIntermet.setVisibility(View.VISIBLE);
                if (!Utils.internetCheck(mActivity)) {
                    tvNoIntermet.setText(getStrings(R.string.no_internet_try_again));
                } else {
                    tvNoIntermet.setText(error.getMessage());
                }
                tvAddOtherAddress.setVisibility(View.GONE);
                rvOtherLocations.setVisibility(View.GONE);
                tvAddWorkAddress.setVisibility(View.GONE);
                rlWorkLocation.setVisibility(View.GONE);
                rlHomeLocation.setVisibility(View.GONE);
                tvFavouriteHeader.setVisibility(View.GONE);
                vwOne.setVisibility(View.GONE);
                vwTwo.setVisibility(View.GONE);
                stopShimmerAnimation(shimmerFrameLayout);
            }
        });
    }

    private void setlocationUI() {
        if (!isHomeAdded) {
            tvAddHomeAddress.setVisibility(View.VISIBLE);
            rlHomeLocation.setVisibility(View.GONE);
        } else {
            tvAddHomeAddress.setVisibility(View.GONE);
            rlHomeLocation.setVisibility(View.VISIBLE);
        }
        if (!isWorkAdded) {
            tvAddWorkAddress.setVisibility(View.VISIBLE);
            rlWorkLocation.setVisibility(View.GONE);
        } else {
            tvAddWorkAddress.setVisibility(View.GONE);
            rlWorkLocation.setVisibility(View.VISIBLE);
        }
        tvAddOtherAddress.setVisibility(View.VISIBLE);
        vwOne.setVisibility(View.VISIBLE);
        vwTwo.setVisibility(View.VISIBLE);
        tvFavouriteHeader.setVisibility(View.VISIBLE);
        rvOtherLocations.setVisibility(View.VISIBLE);
        isHomeAdded = false;
        isWorkAdded = false;
    }

    public void addFavouriteLocation() {
//        if(addressType.equals("Home")){
//            locType=0;
//        }
//        else if(addressType.equals("Work")){
//            locType=1;
//        }
//        else if(addressType.equals("Other")){
//            locType=2;
//        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        commonParams.add("address", address)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("is_custom_address", isCustomAddress)
                .add("name", addressType)
                .add("landmark", landmark != null ? landmark : "")
                .add("house", flat != null ? flat : "")
                .add("postal_code", postalCode != null ? postalCode : "")
                .add("phone_no", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo())
                .add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail())
//                .add("name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName())
                .add("loc_type", locType);

        RestClient.getApiInterface(this).addFavLocation(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel data) {
                getFavouriteLocations();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    public void editFavouriteLocation() {
        if (isHomeUpdate) {
            fav_id = homeFavId;
        } else if (isWorkUpdate) {
            fav_id = workFavId;
        }
//        if(addressType.equals("Home")){
//            locType=0;
//        }
//        else if(addressType.equals("Work")){
//            locType=1;
//        }
//        else if(addressType.equals("Other")){
//            locType=2;
//        }
//        else if(isOtherUpdate){
//            fav_id = otherFavId;
//        }
        JSONObject editBodyObject = new JSONObject();
        try {
            editBodyObject.put("address", address);
            editBodyObject.put("latitude", latitude);
            editBodyObject.put("longitude", longitude);
            editBodyObject.put("is_custom_address", isCustomAddress);

            editBodyObject.put("name", addressType);
            editBodyObject.put("landmark", landmark);
            editBodyObject.put("postal_code", postalCode);
            editBodyObject.put("phone_no", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo());
            editBodyObject.put("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
//            editBodyObject.put("name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " " + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName());
            editBodyObject.put("house", flat);
            editBodyObject.put("loc_type", locType);
        } catch (JSONException e) {

            Utils.printStackTrace(e);
        }
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("fav_id", fav_id)
                .add("edit_body", editBodyObject);

        RestClient.getApiInterface(this).editFavLocation(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel data) {
                getFavouriteLocations();
                landmark = "";
                flat = "";
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    public void deleteFavouriteLocation() {
        new OptionsDialog.Builder(mActivity).message(getStrings(R.string.sure_to_delete_this_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress())).listener(new OptionsDialog.Listener() {
            @Override
            public void performPositiveAction(int purpose, Bundle backpack) {
                CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
                commonParams.add("fav_id", fav_id);

                RestClient.getApiInterface(mActivity).deleteFavLocation(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel data) {
                        getFavouriteLocations();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
            }

            @Override
            public void performNegativeAction(int purpose, Bundle backpack) {
            }
        }).build().show();
    }

}