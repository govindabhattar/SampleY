package com.tookancustomer.utility.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Developer: Saurabh Verma
 * Dated: 23/11/16.
 */
public class ImageChooser implements ImagePickerCallback {
    //CacheLocation.EXTERNAL_STORAGE_APP_DIR
    //CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR;
    //CacheLocation.EXTERNAL_CACHE_DIR
    //CacheLocation.INTERNAL_APP_DIR
    /*
           ====================================== How to use =============================================
      --------------->      Initialise the ImageChooser as
            ImageChooser mImageChooser=new ImageChooser(this); pass this only its compatible for Activity & Fragment both
      --------------->      Call your method where you want to select or pick image
            mImageChooser.selectImage();
      --------------->      Add following code in calling class
            In onActivityResult
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Picker.PICK_IMAGE_DEVICE || requestCode == Picker.PICK_IMAGE_CAMERA)
            mImageChooser.onActivityResult(requestCode, resultCode, data);
            }
            In onRequestPermissionsResult
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == ImageChooser.PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE)
            mImageChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
     --------------->       Results from ImageChooser can be retrieved by implementing ImageChooser.OnImageSelectListener in calling
            Note: Dialogs used in ImageChooser is default alert dialogs. Please customize these dialogs according to app theme.
      --------------->      To pick multiple images
            customize your alert dialog to call function :- pickImageMultiple
     */
    /*
            ====================================== private variables ======================================
     */
    public static final int PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE = 1900;
    private static int CACHE_LOCATION = CacheLocation.EXTERNAL_STORAGE_APP_DIR;
    private String CANCEL = "Cancel";
    private String CAMERA = "Camera";
    private String SETTING = "Settings";
    private String GALLERY = "Gallery";
    private String MULTIPLE = "Multiple";
    private String CHOOSE_IMAGE = "Choose Image";
    private String OK = "Ok";
    private String CAMERA_READ_WRITE_PERMISSION_MSG = "Permissions are required for camera & storage.";
    private String CAMERA_READ_WRITE_PERMISSION_DENIED = "Permissions denied. Please grant permission for camera & storage in setting";
    private String[] PERMISSION_CAMERA_READ_WRITE = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String filePath;
    private Activity mActivity;
    private Fragment mFragment;
    private android.app.Fragment appFragment;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private AlertDialog mAlertDialog;
    private OnImageSelectListener onImageSelectListener;

    /*
      ======================================constructors================================================
     */
    public ImageChooser(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public ImageChooser(Activity mActivity, OnImageSelectListener onImageSelectListener) {
        this.mActivity = mActivity;
        this.onImageSelectListener = onImageSelectListener;
    }

    public ImageChooser(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public ImageChooser(android.app.Fragment appFragment) {
        this.appFragment = appFragment;
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        if (list != null && list.size() > 0) {
            try {
                OnImageSelectListener mOnImageSelect = (OnImageSelectListener) (this.mActivity != null ? this.mActivity : (this.mFragment != null ? this.mFragment : this.appFragment));
                if (mOnImageSelect != null)
                    mOnImageSelect.loadImage(list);
            } catch (ClassCastException e) {
                Toast.makeText(getActivityContext(), "Implement OnImageSelectListener in calling class", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onError(String s) {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
        builder.setMessage(s)
                .setPositiveButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.ok_text) : OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAlertDialog.dismiss();
                    }
                });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void requestMarshMallowPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivityContext(), Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivityContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivityContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (mAlertDialog != null && mAlertDialog.isShowing())
                mAlertDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
            builder.setMessage(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.please_grant_permission_to_access_camera) : CAMERA_READ_WRITE_PERMISSION_MSG)
                    .setPositiveButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.ok_text) : OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(getActivityContext(), PERMISSION_CAMERA_READ_WRITE, PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE);
                        }
                    })
                    .setNegativeButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.cancel) : CANCEL, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAlertDialog.dismiss();
                        }
                    });
            mAlertDialog = builder.create();
            mAlertDialog.show();
        } else {
            ActivityCompat.requestPermissions(getActivityContext(), PERMISSION_CAMERA_READ_WRITE, PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE);
        }
    }

    private Activity getActivityContext() {
        return (Activity) (this.mActivity != null ? this.mActivity : (this.mFragment != null ? this.mFragment.getActivity() : (this.appFragment != null ? this.appFragment.getActivity() : null)));
    }

    private void pickImageSingle() {
        imagePicker = getImagePicker();
        imagePicker.setFolderName("Random");
        imagePicker.setRequestId(1234);
        // imagePicker.ensureMaxSize(500, 500);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
        imagePicker.setCacheLocation(CACHE_LOCATION);
        imagePicker.pickImage();
    }

    private void takePicture() {
        cameraPicker = getCameraImagePicker();
        cameraPicker.setCacheLocation(CACHE_LOCATION);
        cameraPicker.setImagePickerCallback(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        filePath = cameraPicker.pickImage();
    }

    private void pickImageMultiple() {
        imagePicker = getImagePicker();
        imagePicker.setImagePickerCallback(this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private ImagePicker getImagePicker() {
        ImagePicker mImagePicker;
        if (mActivity != null)
            mImagePicker = new ImagePicker(mActivity);
        else if (mFragment != null)
            mImagePicker = new ImagePicker(mFragment);
        else
            mImagePicker = new ImagePicker(appFragment);
        return mImagePicker;
    }

    private CameraImagePicker getCameraImagePicker() {
        CameraImagePicker mCameraImagePicker;
        if (mActivity != null)
            mCameraImagePicker = new CameraImagePicker(mActivity);
        else if (mFragment != null)
            mCameraImagePicker = new CameraImagePicker(mFragment);
        else
            mCameraImagePicker = new CameraImagePicker(appFragment);
        return mCameraImagePicker;
    }

    /**
     * Open app setting of installed application
     *
     * @param context
     */
    private void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null)
            return;
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    /**
     * Check that all given permissions have been onGranted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    private boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1)
            return false;
        // Verify that each required permission has been onGranted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public void selectImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestMarshMallowPermission();
                return;
            }
        }
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
        builder.setMessage(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.pick_image_from) : CHOOSE_IMAGE)
                .setPositiveButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.camera) : CAMERA, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAlertDialog.dismiss();
                        takePicture();
                    }
                })
                /**
                 * if want to pick multiple images
                 */
//                .setNeutralButton(MULTIPLE, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mAlertDialog.dismiss();
//                        pickImageMultiple();
//                    }
//                })
                .setNegativeButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.gallery) : GALLERY, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAlertDialog.dismiss();
                        pickImageSingle();
                    }
                });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE:
                if (verifyPermissions(grantResults)) {
                    selectImage();
                } else {
                    if (mAlertDialog != null && mAlertDialog.isShowing())
                        mAlertDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
                    builder.setMessage(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.please_grant_permission_to_access_camera) : CAMERA_READ_WRITE_PERMISSION_DENIED)
                            .setPositiveButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.settings) : SETTING, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mAlertDialog.dismiss();
                                    startInstalledAppDetailsActivity(getActivityContext());
                                }
                            })
                            .setNegativeButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.cancel) : CANCEL, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mAlertDialog.dismiss();
                                }
                            });
                    mAlertDialog = builder.create();
                    mAlertDialog.show();
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
                if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                    if (imagePicker == null) {
                        imagePicker = getImagePicker();
                        imagePicker.setImagePickerCallback(this);
                    }
                    imagePicker.submit(data);
                } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                    if (cameraPicker == null) {
                        cameraPicker = getCameraImagePicker();
                        cameraPicker.setImagePickerCallback(this);
                        cameraPicker.reinitialize(filePath);
                    }
                    cameraPicker.submit(data);
                }
        }
    }

    public interface OnImageSelectListener {
        void loadImage(List<ChosenImage> list);
    }
}