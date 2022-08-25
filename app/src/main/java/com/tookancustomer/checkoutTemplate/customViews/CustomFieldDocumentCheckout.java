package com.tookancustomer.checkoutTemplate.customViews;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.CheckOutCustomActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.adapter.CustomFieldDocumentAdapterFreelancer;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ImageUrl;
import com.tookancustomer.models.ImageUrlPojo;
import com.tookancustomer.models.taskdetails.CustomField;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.MultipartParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldDocumentCheckout implements SignupTemplateData.Listener, View.OnClickListener {

    private final String TAG = CustomFieldDocumentCheckout.class.getSimpleName();

    public static final int PERM_READ_FILE = 202;
    private ArrayList<String> imagesList;
    private RecyclerView rvAddedImages;

    private View view;
    private TextView tvLabel, tvLabelForDisplay, tvEmpty;
    private ImageView vCustomFieldIcon;
    private final Activity activity;
    private Context context;
    private Template item;
    private CustomFieldDocumentAdapterFreelancer customFieldImageAdapter;
    View vwTop, vwBottom;
    private boolean isForDisplay;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldDocumentCheckout(Context context, boolean isViewBottomHide, boolean isForDisplay) {
        this.context = context;
        this.isForDisplay = isForDisplay;
        if ((activity = (Activity) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_document, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        tvLabelForDisplay = view.findViewById(R.id.tvLabelForDisplay);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);
        vwTop = view.findViewById(R.id.vwTop);
        vwBottom = view.findViewById(R.id.vwBottom);
        vwTop.setVisibility(View.GONE);

        vwBottom.setVisibility(isViewBottomHide ? View.GONE : View.VISIBLE);

        rvAddedImages = view.findViewById(R.id.rvImages);
        rvAddedImages.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        imagesList = new ArrayList<>();


        Utils.setOnClickListener(this, view.findViewById(R.id.vCustomFieldIcon));
    }


    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(Template item) {
        if (isForDisplay) {
            vCustomFieldIcon.setClickable(false);
            vCustomFieldIcon.setVisibility(View.GONE);
            if (item.getValue().toString().isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvAddedImages.setVisibility(View.GONE);
            }
        } else {
            vCustomFieldIcon.setClickable(true);
            vCustomFieldIcon.setVisibility(View.VISIBLE);
        }
        this.item = item;
        this.item.setListener(this);

        tvLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
        tvLabelForDisplay.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() +
                item.getDisplayName().replace("_", " ").substring(1).toLowerCase() + " :");
        Log.i("ReqCatalogueData", "=" + item.getData());

        if (!item.getValue().toString().isEmpty() && item.getValue() instanceof ArrayList) {
            imagesList = (ArrayList<String>) item.getValue();
            rvAddedImages.setAdapter(customFieldImageAdapter = new CustomFieldDocumentAdapterFreelancer(activity, imagesList, item, isForDisplay));
        }


        if (item.isRequired()) {
            tvLabel.setText(CustomViewsUtil.createSpan(activity, item.getDisplayName(), "*"));
        } else {
            tvLabel.setText(item.getDisplayName());
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
                if (AppManager.getInstance().askUserToGrantPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_storage),
                        Codes.Permission.WRITE_STORAGE)) {
                    if (activity instanceof CheckoutTemplateActivity)
                        ((CheckoutTemplateActivity) activity).setCustomFieldPosition(item);
                    else if (activity instanceof CheckOutCustomActivity)
                        ((CheckOutCustomActivity) activity).setCustomFieldPosition(item);
                    else if (activity instanceof QuestionnaireTemplateActivity)
                        ((QuestionnaireTemplateActivity) activity).setCustomFieldPosition(item);

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
                    activity.startActivityForResult(intent, Codes.Request.OPEN_STORAGE_DOCUMENT);
                }
                break;
        }

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)

            switch (requestCode) {

                case PERM_READ_FILE:
//                    compressAndSaveImageBitmap();
                    break;
            }

        else Utils.toast(activity, activity.getString(R.string.permission_was_not_granted_text));
    }


    @Override
    public Object getView() {
        return this;
    }


    /**
     * Method to set, compress and Save the Bitmap
     */
//    public void compressAndSaveImageBitmap() {
//
//        String image = imageUtils.compressAndSaveBitmap(activity);
//
//        Log.e(TAG, "Image Path: " + image);
//
//        if (image == null) {
//            Toast.makeText(activity, activity.getString(R.string.could_not_read_from_source), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Upload the Image
//        uploadCustomFieldImage(image);
//
//    }
    public void uploadCustomFieldImage(File image) {

        MultipartParams multipartParams = new MultipartParams.Builder().addFile("ref_image", image).build();
        RestClient.getApiInterface(activity).getImageUrl(multipartParams.getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                ImageUrlPojo imageUrlPojo = new ImageUrlPojo();
                try {
                    imageUrlPojo.setData(baseModel.toResponseModel(ImageUrl.class));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                imagesList.add(imageUrlPojo.getData().getImageUrl());
                if (customFieldImageAdapter != null) {
                    customFieldImageAdapter.notifyDataSetChanged();
                } else {
                    rvAddedImages.setAdapter(customFieldImageAdapter = new CustomFieldDocumentAdapterFreelancer(activity, imagesList,
                            item, isForDisplay));
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

        }

    }
}
