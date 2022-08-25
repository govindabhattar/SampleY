package com.tookancustomer.utility;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

public class GlideUtil {
    private int placeholder, error, fallback;
    private View view;
    private Object loadItem, thumbnailItem;
    private Transformation transformation;
    private OnLoadCompleteListener listener;
    private boolean isCenterCrop;
    private boolean isFitCenter;
    private boolean accToSize;




    public GlideUtil(GlideUtilBuilder builder) {
        this.placeholder = builder.placeholder;
        this.error = builder.error;
        this.fallback = builder.fallback;
        this.view = builder.view;
        this.loadItem = builder.loadItem;
        this.thumbnailItem = builder.thumbnailItem;
        this.transformation = builder.transformation;
        this.listener = builder.listener;
        this.isCenterCrop = builder.isCenterCrop;
        this.isFitCenter = builder.isFitCenter;
        this.accToSize = builder.accToSize;



        GlideRequest glideRequest = (GlideRequest) GlideApp.with(view)
                .load(loadItem)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(fallback);

        if (transformation != null)
            glideRequest.apply(RequestOptions.bitmapTransform(transformation));

        if (thumbnailItem != null)
            glideRequest.thumbnail(GlideApp.with(view).load(thumbnailItem));
        else
            glideRequest.thumbnail(0.25f);

        if (isCenterCrop)
            glideRequest.centerCrop();
        if (isFitCenter)
            glideRequest.fitCenter();

        if (listener != null) {
            glideRequest.into(new ImageViewTarget((ImageView) view) {
                @Override
                protected void setResource(@Nullable Object resource) {
                }

                @Override
                public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {
                    listener.onLoadCompleted(resource, transition, ((ImageView) view));
                    listener.onLoadCompleted(resource, transition);
                   try {
                       Bitmap image = ((BitmapDrawable) resource).getBitmap();
                       if (image.getHeight() >= image.getWidth()) {
                           isCenterCrop = false;
                       } else {
                           isCenterCrop = true;
                           glideRequest.centerCrop();
                           if (transformation != null)
                               glideRequest.apply(RequestOptions.bitmapTransform(transformation));
                       }
                   }catch (Exception e){
                       isCenterCrop=true;
                       glideRequest.centerCrop();
                       if (transformation != null)
                           glideRequest.apply(RequestOptions.bitmapTransform(transformation));
                       listener.onLoadFailed();

                       Utils.printStackTrace(e);
                   }

                    ((ImageView) view).setImageDrawable((Drawable) resource);
                    super.onResourceReady(resource, transition);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    listener.onLoadFailed();
                    super.onLoadFailed(errorDrawable);
                }
            });
        } else {
            glideRequest.into((ImageView) view);
        }
    }

    public static class GlideUtilBuilder {
        private int placeholder, error, fallback;
        private View view;
        private Object loadItem;
        private Object thumbnailItem;
        private Transformation transformation;
        private OnLoadCompleteListener listener;
        private boolean isCenterCrop;
        private boolean isFitCenter;
        private boolean accToSize;



        public GlideUtilBuilder(View view) {
            this.view = view;
        }

        public GlideUtilBuilder setPlaceholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public GlideUtilBuilder setError(int error) {
            this.error = error;
            return this;
        }

        public GlideUtilBuilder cropAccToImageSize(boolean accToSize) {
            this.accToSize = accToSize;
            return this;
        }


        public GlideUtilBuilder setFallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public GlideUtilBuilder setView(View view) {
            this.view = view;
            return this;
        }

        public GlideUtilBuilder setLoadItem(Object loadItem) {
            this.loadItem = loadItem;
            return this;
        }

        public GlideUtilBuilder setThumbnailItem(Object thumbnailItem) {
            this.thumbnailItem = thumbnailItem;
            return this;
        }

        public GlideUtilBuilder setTransformation(Transformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public GlideUtilBuilder setLoadCompleteListener(OnLoadCompleteListener listener) {
            this.listener = listener;
            return this;
        }


        public GlideUtilBuilder setCenterCrop(boolean centerCrop) {
            isCenterCrop = centerCrop;
            return this;
        }

        public GlideUtilBuilder setFitCenter(boolean fitCenter) {
            isFitCenter = fitCenter;
            return this;
        }

        public GlideUtil build() {
            return new GlideUtil(this);
        }
    }


    public interface OnLoadCompleteListener {
        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition);

        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view);

        public void onLoadFailed();
    }

}
