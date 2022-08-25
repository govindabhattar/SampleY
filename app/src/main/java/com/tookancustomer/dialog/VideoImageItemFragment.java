package com.tookancustomer.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.tookancustomer.R;
import com.tookancustomer.models.ProductCatalogueData.ImagesAndVedios;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class VideoImageItemFragment extends Fragment {

    private Activity activity;
    Handler handler = null;
    Runnable runnable = null;

    ImageView imgSnapshot, urlThumbIV;
    ImageView playPauseIV;
    VideoView videoView;
    ProgressBar videoLoadingProgress;
    ImagesAndVedios imagesAndVedios;

    @SuppressLint("ValidFragment")
    public VideoImageItemFragment(ImagesAndVedios imagesAndVedios) {
        this.imagesAndVedios = imagesAndVedios;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser && imagesAndVedios.getUrlType() == 2)   // If we are becoming invisible, then...
            {
                if (videoView != null) {
                    if (videoView != null && videoView.isPlaying()) {
                        videoView.pause();
                    }
                }
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_image_video, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        runnable = null;
        handler = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgSnapshot = view.findViewById(R.id.imgSnapshot);
        urlThumbIV = view.findViewById(R.id.urlThumbIV);
        playPauseIV = view.findViewById(R.id.playPauseIV);
        videoView = view.findViewById(R.id.videoView);
        videoLoadingProgress = view.findViewById(R.id.videoLoadingProgress);
        activity = getActivity();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (videoView.isPlaying() && playPauseIV.getVisibility() == View.VISIBLE) {
                    playPauseIV.setVisibility(View.GONE);
                } else if (videoView.isPlaying() && playPauseIV.getVisibility() != View.VISIBLE) {
                    playPauseIV.setVisibility(View.VISIBLE);
                    playPauseIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.pause_button));
                } else if (!videoView.isPlaying() && playPauseIV.getVisibility() == View.VISIBLE) {
                    playPauseIV.setVisibility(View.GONE);
                } else if (!videoView.isPlaying() && playPauseIV.getVisibility() != View.VISIBLE) {
                    playPauseIV.setVisibility(View.VISIBLE);
                    playPauseIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.play));
                }

                startdelay(playPauseIV);
            }
        });

        playPauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSnapshot.setVisibility(View.GONE);
                urlThumbIV.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                if (videoView.isPlaying()) {
                    videoView.pause();
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    playPauseIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.play));
                } else {
                    videoView.start();
                    if (handler == null)
                        videoLoadingProgress.setVisibility(View.VISIBLE);
                    playPauseIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.pause_button));
                    startdelay(playPauseIV);
                }

            }
        });

        videoView.setBackgroundColor(Color.BLACK); // Your color.

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoLoadingProgress.setVisibility(View.GONE);
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoView.setBackgroundColor(Color.TRANSPARENT);

                    }
                });
            }
        });


        setData(imagesAndVedios);

    }

    public void setData(ImagesAndVedios imagesAndVedios) {

        if (imagesAndVedios.getUrlType() == 1) {
            videoView.setVisibility(View.GONE);
            playPauseIV.setVisibility(View.GONE);
            imgSnapshot.setVisibility(View.VISIBLE);
            setImage(imagesAndVedios);
        } else {
            urlThumbIV.setVisibility(View.VISIBLE);
            imgSnapshot.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            playPauseIV.setVisibility(View.VISIBLE);
            setVideo(imagesAndVedios);
        }
    }


    private void setVideo(ImagesAndVedios imagesAndVedios) {


        String imageVideoThumb = imagesAndVedios.getVideoThumb();
        String imageVideoUrl = imagesAndVedios.getImageVideoUrl();
        if (imageVideoThumb == null || imageVideoThumb.isEmpty()) {
            imgSnapshot.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
            urlThumbIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));

        } else {
            Log.i("URL", "==" + imageVideoThumb);
            // Verify whether a web url
            if (imageVideoThumb.startsWith("http://") || imageVideoThumb.startsWith("https://")) {
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imageVideoThumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
                new GlideUtil.GlideUtilBuilder(urlThumbIV)
                        .setLoadItem(imageVideoThumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
            } else {
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imageVideoThumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
                new GlideUtil.GlideUtilBuilder(urlThumbIV)
                        .setLoadItem(imageVideoThumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
            }
        }

        videoView.setVideoPath(imageVideoUrl);
        videoView.requestFocus();
    }

    private void setImage(ImagesAndVedios imagesAndVedios) {
        String imagePath = imagesAndVedios.getImageVideoUrl();
        if (imagePath == null || imagePath.isEmpty()) {
            imgSnapshot.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
            urlThumbIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));

        } else {
            Log.i("URL", "==" + imagePath);
            // Verify whether a web url
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
                new GlideUtil.GlideUtilBuilder(urlThumbIV)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();

            } else {
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
                new GlideUtil.GlideUtilBuilder(urlThumbIV)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();

            }
        }

    }

    private void startdelay(final ImageView playPauseIV) {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (playPauseIV.getVisibility() == View.VISIBLE)
                        playPauseIV.setVisibility(View.GONE);
                    // Do what ever you want
                }
            };
        }
        handler.postDelayed(runnable, 2000);
    }

}
