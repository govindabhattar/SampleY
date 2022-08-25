package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tookancustomer.CompleteRequestActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.interfaces.RequestInterface;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.requests.Pending;
import com.tookancustomer.models.requests.RequestsData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;

import java.util.ArrayList;

/**
 * Created by neerajwadhwani on 25/07/18.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    private final Context context;
    private final RequestsData reqData;
    private ArrayList<Pending> requestsList;
    private Activity mActivity;
    private UserData userData;
    RequestInterface requestsInterface;

    public RequestAdapter(ArrayList<Pending> requestsList, Context context, UserData userData, RequestsData requestsData,RequestInterface requestsInterface) {
        this.requestsList = requestsList;
        this.context=context;
        mActivity=(CompleteRequestActivity)context;
        this.userData=userData;
        this.reqData=requestsData;
        this.requestsInterface=requestsInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_requests, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if(requestsList.size()>0) {
            requestsInterface.removeNoRequestText();
            holder.tvGuestValue.setText(requestsList.get(holder.getAdapterPosition()).getMerchantName());
            holder.tvStayTimeValue.setText(requestsList.get(holder.getAdapterPosition()).getMerchantName());
            holder.tvRevenueValue.setText(requestsList.get(holder.getAdapterPosition()).getTotalAmount());
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
                    commonParams.add("access_token", userData.getData().getAppAccessToken());
                    commonParams.add("marketplace_user_id", StorefrontCommonData.getFormSettings().getUserId());
                    commonParams.add("user_type", "3");
                    commonParams.add("job_id", requestsList.get(holder.getAdapterPosition()).getJobId());
                    commonParams.add("accept_reject", "1");
                    commonParams.add("product_id", requestsList.get(holder.getAdapterPosition()).getProductId());
                    RestClient.getApiInterface(context).acceptRejectOrder(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                        @Override
                        public void success(BaseModel baseModel) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                            requestsList.remove(holder.getAdapterPosition());
                            notifyItemChanged(holder.getAdapterPosition());
                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {
                            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
                    commonParams.add("access_token", userData.getData().getAppAccessToken());
                    commonParams.add("marketplace_user_id", StorefrontCommonData.getFormSettings().getUserId());
                    commonParams.add("user_type", "3");
                    commonParams.add("job_id", requestsList.get(holder.getAdapterPosition()).getJobId());
                    commonParams.add("accept_reject", "0");
                    commonParams.add("product_id", requestsList.get(holder.getAdapterPosition()).getProductId());
                    RestClient.getApiInterface(context).acceptRejectOrder(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                        @Override
                        public void success(BaseModel baseModel) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                            requestsList.remove(holder.getAdapterPosition());
                            notifyItemChanged(holder.getAdapterPosition());
                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {
                            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
            requestsInterface.showNoRequestText();
    }

    @Override
    public int getItemCount() {
        return requestsList!=null?requestsList.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStayTimeValue,tvGuestValue,tvRevenueValue,btnReject,btnAccept;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvStayTimeValue=itemView.findViewById(R.id.tvStayTimeValue);
            tvRevenueValue=itemView.findViewById(R.id.tvRevenueValue);
            tvGuestValue=itemView.findViewById(R.id.tvGuestValue);
            btnReject=itemView.findViewById(R.id.btnReject);
            btnAccept=itemView.findViewById(R.id.btnAccept);
        }
    }
}
