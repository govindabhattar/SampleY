package com.tookancustomer.adapters;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.appConfiguration.DynamicPagesDetails;
import com.tookancustomer.models.userpages.UserPagesData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;

import java.util.List;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.IS_HTML;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;

public class DynamicPagesAdapter extends RecyclerView.Adapter<DynamicPagesAdapter.ViewHolder> {
    private Activity activity;
    private List<DynamicPagesDetails> dataList;

    public DynamicPagesAdapter(Activity activity, List<DynamicPagesDetails> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_dynamicpages, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();
//        final String dynamicUrl = "https://" + StorefrontCommonData.getAppConfigurationData().getDomainName()
//                + "/" + StorefrontCommonData.getSelectedLanguageCode().getLanguageCode()
//                + "/" + "page/" + dataList.get(adapterPos).getRoute();

        viewHolder.tvDynamicPage.setText(dataList.get(adapterPos).getName());
        viewHolder.llParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMerchantDynamicPages(dataList.get(adapterPos).getRoute(), dataList.get(adapterPos).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llParentView;
        TextView tvDynamicPage;

        public ViewHolder(View itemView) {
            super(itemView);
            llParentView = itemView.findViewById(R.id.llParentView);
            tvDynamicPage = itemView.findViewById(R.id.tvDynamicPage);
        }
    }

    public void setMerchantDynamicPages(String route, int userId) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());

        commonParams.add("route", route);
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        if (userId == 0) {
            commonParams.add("is_admin_page", 1);
            commonParams.add(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        } else {
            commonParams.add("is_admin_page", 0);
            commonParams.add(USER_ID, userId);
        }

        RestClient.getApiInterface(activity).getUserPages(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserPagesData userPagesData = baseModel.toResponseModel(UserPagesData.class);

                if (userPagesData.getTemplateData().size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(HEADER_WEBVIEW, userPagesData.getTemplateData().get(0).getName());
                    bundle.putString(URL_WEBVIEW, userPagesData.getTemplateData().get(0).getTemplateData());
                    bundle.putBoolean(IS_HTML, true);
                    Transition.startActivity(activity, WebViewActivity.class, bundle, false);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }
}