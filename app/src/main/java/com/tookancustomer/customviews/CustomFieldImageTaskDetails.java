package com.tookancustomer.customviews;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.R;
import com.tookancustomer.adapter.CustomFieldImagesAdapterDetails;
import com.tookancustomer.models.taskdetails.CustomField;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldImageTaskDetails {

    private final String TAG = CustomFieldImageTaskDetails.class.getSimpleName();

    private ArrayList<String> imagesList;
    private RecyclerView rvAddedImages;
    private LinearLayout llImages;

    private View view;
    private TextView tvLabel, tvPlaceHolder;
    private final Activity activity;
    private Context context;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldImageTaskDetails(Context context) {
        this.context = context;
        if ((activity = (Activity) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_image_details, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        tvPlaceHolder = view.findViewById(R.id.tvPlaceHolder);

        llImages = view.findViewById(R.id.llImages);

        rvAddedImages = view.findViewById(R.id.rvImages);
        rvAddedImages.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        imagesList = new ArrayList<>();

    }

    public View renderImages(CustomField customField) {
        tvLabel.setText(customField.getDisplayName().replace("_"," ").substring(0,1).toUpperCase()+customField.getDisplayName().replace("_"," ").substring(1).toLowerCase());
        String data = Utils.assign(customField.getFleetData(), customField.getData().isEmpty() ? "-" : customField.getData());
        Log.i("data", "" + data);

        if (!data.toString().equalsIgnoreCase("-")) {
            ArrayList<String> imagesUrlList;
            try {
                imagesUrlList = new Gson().fromJson(data, new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (Exception e) {
                imagesUrlList = new ArrayList<>();
                imagesUrlList.add(data);
            }
            imagesList = imagesUrlList;
            llImages.setVisibility(View.VISIBLE);
            rvAddedImages.setAdapter(new CustomFieldImagesAdapterDetails(activity, imagesList));
        } else {
            tvPlaceHolder.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
