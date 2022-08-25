package com.tookancustomer.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewImagesVideoAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> imagesList;
    private Activity activity;
    public ViewImagesVideoAdapter(FragmentManager fragmentManager, List<Fragment> imagesList, Activity activity) {
        super(fragmentManager);
        this.imagesList=imagesList;
        this.activity=activity;
    }
   /* @Override
    public Fragment getItem(int i) {
        return // new VideoImageItemFragment();
    }*/

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return  imagesList.get(i);
    }
}

  /*  private final LayoutInflater layoutInflater;

    private Activity activity;
    private ArrayList<ImagesAndVedios> imagesList;
    Handler handler;
    Runnable runnable;

    ImageView imgSnapshot;
    ImageView playPauseIV;
    VideoView videoView;
    ProgressBar videoLoadingProgress;


    public ViewImagesVideoAdapter(Activity activity, ArrayList<ImagesAndVedios> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View customFieldImageView = layoutInflater.inflate(R.layout.view_image_video, container, false);


        // View to display the Image
        imgSnapshot = customFieldImageView.findViewById(R.id.imgSnapshot);
        playPauseIV = customFieldImageView.findViewById(R.id.playPauseIV);
        videoView = customFieldImageView.findViewById(R.id.videoView);
        videoLoadingProgress = customFieldImageView.findViewById(R.id.videoLoadingProgress);

       *//* if (imagesList.get(position).getUrlType() == 1) {
            videoView.setVisibility(View.GONE);
            playPauseIV.setVisibility(View.GONE);
            imgSnapshot.setVisibility(View.VISIBLE);
            setImageView(imagesList.get(position).getImageVideoUrl(), imgSnapshot);
        } else {
            imgSnapshot.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            setVideoView(videoView, imagesList.get(position).getImageVideoUrl(), imagesList.get(position).getVideoThumb(), imgSnapshot);

        }
*//*

        videoView.setBackgroundColor(Color.BLACK); // Your color.

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoLoadingProgress.setVisibility(View.GONE);
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoView.setBackgroundColor(Color.TRANSPARENT);
//                        MediaController mediaController = new MediaController(activity);
//                        videoView.setMediaController(mediaController);
//                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseIV.setVisibility(View.VISIBLE);
                playPauseIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.play));
            }
        });


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


        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });


        // Add the View to container
        container.addView(customFieldImageView);

        return customFieldImageView;
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




    public void stopVideo(int position) {
*//*        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        videoView.stopPlayback();
        handler = null;
        runnable = null;
        if (imagesList.get(position).getUrlType() == 2)
            setVideoView(videoView, imagesList.get(position).getImageVideoUrl(), imagesList.get(position).getVideoThumb(), imgSnapshot);*//*
    }

    public void  setVideo(int position){
        imgSnapshot.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        setVideoView(videoView, imagesList.get(position).getImageVideoUrl(), imagesList.get(position).getVideoThumb(), imgSnapshot);

    }
    public void  setImage(int position){
        videoView.setVisibility(View.GONE);
        playPauseIV.setVisibility(View.GONE);
        imgSnapshot.setVisibility(View.VISIBLE);
        setImageView(imagesList.get(position).getImageVideoUrl(), imgSnapshot);
    }

    private void setVideoView(VideoView videoView, String imageVideoUrl, String imageVideothumb, ImageView imgSnapshot) {
        imgSnapshot.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        if (imageVideothumb == null || imageVideothumb.isEmpty()) {
            imgSnapshot.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));

        } else {
            Log.i("URL", "==" + imageVideothumb);
            // Verify whether a web url
            if (imageVideothumb.startsWith("http://") || imageVideothumb.startsWith("https://"))
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imageVideothumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
            else
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imageVideothumb)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
        }

        videoView.setVideoPath(imageVideoUrl);
        videoView.requestFocus();
    }

    private void setImageView(String imagePath, ImageView imgSnapshot) {
        if (imagePath == null || imagePath.isEmpty()) {
            imgSnapshot.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));

        } else {
            Log.i("URL", "==" + imagePath);
            // Verify whether a web url
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://"))
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
               *//* Glide.with(activity).load(imagePath).asBitmap().fitCenter()
                        .placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(imgSnapshot));*//*
                // Load from Storage
            else
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
               *//* Glide.with(activity).load(imagePath).asBitmap().fitCenter()
                        .placeholder(AppCompatResources.getDrawable(activity, R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(imgSnapshot));*//*
        }

    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}*/
