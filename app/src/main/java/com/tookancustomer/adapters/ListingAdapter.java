package com.tookancustomer.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.interfaces.ListingInterface;

import com.tookancustomer.models.listingdata.Datum;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.SquareImageView;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Created by neerajwadhwani on 27/07/18.
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.MyViewHolder> {

    private final ArrayList<Datum> list;
    private final Context context;
    private ListingInterface listingInterface;

    public ListingAdapter(ArrayList<Datum> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_list_nlevel_new, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        if (list.size() > 0) {

            holder.tvAverageRatingImage.setText(list.get(position).getTotalRatingsCount() + "");


            for (int i = 0; i < list.get(position).getLayoutData().getLines().size(); i++) {
                switch (i) {
                    case 0:
                        if (list.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                            Utils.setVisibility(View.GONE, holder.tvDescription1);
                        } else {
                            Utils.setVisibility(View.VISIBLE, holder.tvDescription1);
                        }
                        holder.tvDescription1.setText(list.get(position).getLayoutData().getLines().get(i).getData());
                        //holder.tvDescription1.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, list.get(position).getLayoutData().getLines().get(i).getStyle())));
                        break;
                    case 1:
                        if (list.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                            Utils.setVisibility(View.GONE, holder.tvDescription2);
                        } else {
                            Utils.setVisibility(View.VISIBLE, holder.tvDescription2);
                        }
                        holder.tvDescription2.setText(list.get(position).getLayoutData().getLines().get(i).getData());
                        String str = list.get(position).getLayoutData().getLines().get(i).getData();

                        // Use lineCount here
//                                    if ( holder.tvDescription2.getLineCount() > 2) {

                        String upToNCharacters = str.substring(0, Math.min(str.length(), 50));


                        final SpannableString text = new SpannableString(upToNCharacters + " " + context.getString(R.string.more_with_dots));
                        text.setSpan(new RelativeSizeSpan(1f), text.length() - context.getString(R.string.more_with_dots).length(), text.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new ForegroundColorSpan(Color.RED), text.length() - 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.tvDescription2.setText(text);


//


                        //  holder.tvDescription2.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, list.get(position).getLayoutData().getLines().get(i).getStyle())));
                        break;
                    case 2:
                        if (list.get(position).getLayoutData().getLines().get(i).getData().isEmpty()
                                || (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && list.get(position).getPrice().doubleValue() <= 0)
                                ) {
                            Utils.setVisibility(StorefrontCommonData.getFormSettings().getPdpView() == 1 ? View.GONE : View.INVISIBLE, holder.tvDescription3);
                        } else {


                            Utils.setVisibility(View.VISIBLE, holder.tvDescription3);
                        }
                        holder.tvDescription3.setText(list.get(position).getLayoutData().getLines().get(i).getData());
                        //holder.tvDescription3.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, list.get(position).getLayoutData().getLines().get(i).getStyle())));
                        break;
                }
            }

            /*Glide.with(context).load(list.get(position).getImageUrl()).asBitmap()
                    .centerCrop()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(AppCompatResources.getDrawable(context, R.drawable.ic_image_placeholder))
                    .into(new BitmapImageViewTarget(holder.ivNLevelImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            holder.ivNLevelImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });*/

            new GlideUtil.GlideUtilBuilder(holder.ivNLevelImage)
                    .setLoadItem(list.get(position).getImageUrl())
                    .setCenterCrop(true)
                    .setFitCenter(true)
                    .setPlaceholder(R.drawable.ic_image_placeholder)
                    .setTransformation(new CenterCrop())
                    .build();

        } else {
            listingInterface.showNoListText();
        }

        if ((list.get(position).getIs_review_rating_enabled() == 0 || list.get(position).getProductRating().floatValue() <= 0)) {
            holder.llRatingImageLayout.setVisibility(View.GONE);
        } else {
            holder.llRatingImageLayout.setVisibility(View.VISIBLE);
            holder.tvAverageRatingImage.setText(list.get(position).getProductRating().floatValue() + "");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SquareImageView ivNLevelImage;
        private TextView tvDescription1, tvDescription2, tvDescription3, tvAverageRatingImage;
        private LinearLayout llRatingImageLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);
            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);
            tvAverageRatingImage = itemView.findViewById(R.id.tvAverageRatingImage);
            llRatingImageLayout = itemView.findViewById(R.id.llRatingImageLayout);
        }
    }
}
