package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.appConfiguration.DynamicPagesDetails;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.List;

import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.IS_HTML;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;


/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class MerchantDynamicPagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<DynamicPagesDetails> dataList;
    private int width;

    public MerchantDynamicPagesAdapter(Activity activity, List<DynamicPagesDetails> dataList, int width) {
        this.activity = activity;
        this.dataList = dataList;
        this.width = width;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.itemview_merchant_dynamicpages, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int adapterPos = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            try {
                display.getRealSize(size);
            } catch (NoSuchMethodError err) {
                display.getSize(size);
            }
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (size.x / 2),
//                    LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
//            viewHolder.llDynamicPageParentView.setLayoutParams(lp);

            if (dataList.size() == 1) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (width),
                        LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                viewHolder.llDynamicPageParentView.setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (width / 2),
                        LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                viewHolder.llDynamicPageParentView.setLayoutParams(lp);
            }
//            ViewTreeObserver viewTreeObserver = rvMerchantDynamicPages.getViewTreeObserver();
//            if (viewTreeObserver.isAlive()) {
//                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        rvMerchantDynamicPages.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        }
//                });
//            }


            viewHolder.tvDynamicPageHeader.setText(dataList.get(adapterPos).getName());

//            final String dynamicUrl = "https://" + StorefrontCommonData.getAppConfigurationData().getDomainName()
//                    + "/" + StorefrontCommonData.getSelectedLanguageCode().getLanguageCode()
//                    + "/" + "page/" + dataList.get(adapterPos).getRoute();

            viewHolder.llDynamicPageParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.internetCheck(activity)) {
                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(HEADER_WEBVIEW, dataList.get(adapterPos).getName());
                    bundle.putString(URL_WEBVIEW, dataList.get(adapterPos).getTemplateData());
                    bundle.putBoolean(IS_HTML, true);
                    Transition.startActivity(activity, WebViewActivity.class, bundle, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llDynamicPageParentView;
        private TextView tvDynamicPageHeader;

        ViewHolder(View itemView) {
            super(itemView);
            llDynamicPageParentView = itemView.findViewById(R.id.llDynamicPageParentView);
            tvDynamicPageHeader = itemView.findViewById(R.id.tvDynamicPageHeader);
        }
    }
}