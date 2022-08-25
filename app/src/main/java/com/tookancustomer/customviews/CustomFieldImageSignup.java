package com.tookancustomer.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.SignupCustomFieldsActivity;
import com.tookancustomer.adapter.CustomFieldImagesAdapterSignup;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ImageUrl;
import com.tookancustomer.models.ImageUrlPojo;
import com.tookancustomer.models.taskdetails.CustomField;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.MultipartParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.ImageUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldImageSignup implements SignupTemplateData.Listener, View.OnClickListener {

    private final String TAG = CustomFieldImageSignup.class.getSimpleName();

    public static final int PERM_CAMERA = 201;
    public static final int PERM_READ_FILE = 202;
    public static final int PERM_OPEN_GALLERY = 203;
    public static final int PERM_SAVE_BITMAP = 204;
    private ArrayList<String> imagesList;
    private RecyclerView rvAddedImages;
    //  private RelativeLayout llImages;
    // private View vSeperator;

    public ImageUtils imageUtils;

    private View view;
    private TextView tvLabel;
    //   private TextView tvPlaceHolder;
    private ImageView vCustomFieldIcon;
    private final Activity activity;
    private Context context;
    private SignupTemplateData item;
    private CustomFieldImagesAdapterSignup customFieldImageAdapter;
    View vwTop, vwBottom;


    /**
     * Method to initialize the CustomField
     */
    public CustomFieldImageSignup(Context context, boolean isViewBottomHide) {
        this.context = context;
        if ((activity = (Activity) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_image, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        //  tvPlaceHolder = (TextView) view.findViewById(R.id.tvPlaceHolder);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);
        vwTop = view.findViewById(R.id.vwTop);
        vwBottom = view.findViewById(R.id.vwBottom);
        vwTop.setVisibility(View.GONE);

        vwBottom.setVisibility(isViewBottomHide ? View.GONE : View.VISIBLE);
        //    llImages = (RelativeLayout) view.findViewById(R.id.llImages);
        // vSeperator = view.findViewById(R.id.vSeparator);
        rvAddedImages = view.findViewById(R.id.rvImages);
        rvAddedImages.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        imagesList = new ArrayList<>();

        imageUtils = new ImageUtils(activity);
        imageUtils.setPermissionCodes(PERM_CAMERA, PERM_READ_FILE, PERM_OPEN_GALLERY, PERM_SAVE_BITMAP);

        Utils.setOnClickListener(this, view.findViewById(R.id.vCustomFieldIcon));
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(SignupTemplateData item) {
        this.item = item;
        this.item.setListener(this);

        tvLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
        Log.i("ListingData", "=" + item.getData());
        if (!item.getData().toString().isEmpty() && item.getData() instanceof ArrayList) {
            imagesList = (ArrayList<String>) item.getData();
            //  llImages.setVisibility(View.VISIBLE);
            //   vSeperator.setVisibility(View.GONE);
            rvAddedImages.setAdapter(customFieldImageAdapter = new CustomFieldImagesAdapterSignup(activity, imagesList, item));
        }

        if (item.isReadOnly()) {
            view.findViewById(R.id.vCustomFieldIcon).setEnabled(false);
        }

        return view;
    }


    public View renderImages(CustomField customField) {
        tvLabel.setText(customField.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + customField.getDisplayName().replace("_", " ").substring(1).toLowerCase());
//        tvPlaceHolder.setVisibility(View.GONE);
        String data = Utils.assign(customField.getFleetData(), customField.getData().isEmpty() ? "-" : customField.getData());
        Log.i("data", data);
        return view;
    }


    @Override
    public void onClick(View view) {
        UIManager.isPickup = true;
        switch (view.getId()) {
            case R.id.vCustomFieldIcon:
                if (activity instanceof SignupCustomFieldsActivity) {
                    ((SignupCustomFieldsActivity) activity).setCustomFieldPosition(item);
                } else if (activity instanceof RegistrationOnboardingActivity) {
                    ((RegistrationOnboardingActivity) activity).setCustomFieldPosition(item);
                }
                imageUtils.showImageChooser(Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE,
                        Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE);
                break;
        }

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)

            switch (requestCode) {
                case PERM_CAMERA:
                    imageUtils.startCamera();
                    break;

                case PERM_READ_FILE:
                    compressAndSaveImageBitmap();
                    break;

                case PERM_OPEN_GALLERY:
                    imageUtils.openGallery();
                    break;

                case PERM_SAVE_BITMAP:
                    compressAndSaveImageBitmap();
                    break;
            }

        else Utils.toast(activity, StorefrontCommonData.getString(activity,R.string.permission_was_not_granted_text));
    }


    @Override
    public Object getView() {
        return this;
    }


    /**
     * Method to set, compress and Save the Bitmap
     */
    public void compressAndSaveImageBitmap() {

        String image = imageUtils.compressAndSaveBitmap(activity);

        Log.e(TAG, "Image Path: " + image);

        if (image == null) {
            Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.could_not_read_from_source), Toast.LENGTH_SHORT).show();
            return;
        }


        // Upload the Image
        uploadCustomFieldImage(image);

        // Set the Image on the View
//        setImage(image, vImageItemView, false);
    }

    private void uploadCustomFieldImage(final String image) {

        MultipartParams multipartParams = new MultipartParams.Builder().addFile("ref_image", new File(image)).build();
        RestClient.getApiInterface(activity).getImageUrl(multipartParams.getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                ImageUrlPojo imageUrlPojo = new ImageUrlPojo();
                try {
                    imageUrlPojo.setData(baseModel.toResponseModel(ImageUrl.class));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }              //  llImages.setVisibility(View.VISIBLE);
//                vSeperator.setVisibility(View.GONE);
                imagesList.add(imageUrlPojo.getData().getImageUrl());
                if (customFieldImageAdapter != null) {
                    customFieldImageAdapter.notifyDataSetChanged();
                } else {
                    rvAddedImages.setAdapter(customFieldImageAdapter = new CustomFieldImagesAdapterSignup(activity, imagesList, item));
                }

                item.setData(imagesList);
                rvAddedImages.post(new Runnable() {
                    @Override
                    public void run() {
                        rvAddedImages.smoothScrollToPosition(imagesList.size());
                    }
                });
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }


    public void deleteImage(int position) {
        final String url = imagesList.get(position);
        imagesList.remove(position);
        customFieldImageAdapter.notifyDataSetChanged();
        if (imagesList.size() <= 0) {
            // llImages.setVisibility(View.GONE);
            //   vSeperator.setVisibility(View.VISIBLE);
        }

    }
}
