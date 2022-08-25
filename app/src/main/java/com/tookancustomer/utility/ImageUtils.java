package com.tookancustomer.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.core.content.FileProvider;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private Activity activity;

    private int cameraRequestCode;
    private int galleryRequestCode;

    private int cameraPermission;
    private int readFilePermission;
    private int openGalleryPermission;
    private int saveBitmapPermission;

    public ImageUtils(Activity activity) {

        this.activity = activity;

        cameraPermission = Codes.Permission.CAMERA;
        readFilePermission = Codes.Permission.READ_FILE;
        openGalleryPermission = Codes.Permission.OPEN_GALLERY;
        saveBitmapPermission = Codes.Permission.SAVE_BITMAP;
    }

    public void showImageChooser(int cameraRequestCode, int galleryRequestCode) {

        this.cameraRequestCode = cameraRequestCode;
        this.galleryRequestCode = galleryRequestCode;

        try {

            final Dialog dialog = new Dialog(activity, R.style.DialogTheme);

            dialog.setContentView(R.layout.dialog_image_chooser);

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            ((TextView) dialog.findViewById(R.id.tvTitle)).setTypeface(Font.getRegular(activity));
            ((TextView) dialog.findViewById(R.id.tvTitle)).setText(StorefrontCommonData.getString(activity, R.string.pick_image_from));

            Button btnCamera = dialog.findViewById(R.id.btnCamera);
            btnCamera.setTypeface(Font.getRegular(activity));
            btnCamera.setText(StorefrontCommonData.getString(activity, R.string.camera));

            Button btnGallery = dialog.findViewById(R.id.btnGallery);
            btnGallery.setTypeface(Font.getRegular(activity));
            btnGallery.setText(StorefrontCommonData.getString(activity, R.string.gallery));

            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    startCamera();
                }
            });

            btnGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    openGallery();
                }
            });

            dialog.show();

        } catch (Exception e) {

            Log.e(TAG, "Crashed: " + e);
        }
    }

    /**
     * This method is called by every other image chooser method finally.
     */
    public void showImageChooser() {

        showImageChooser(
                Codes.ImageSelection.CAPTURE_FROM_CAMERA,
                Codes.ImageSelection.PICK_FROM_GALLERY
        );
    }

    /**
     * Method to start the Camera
     */
    public void startCamera(int cameraRequestCode) {
        this.cameraRequestCode = cameraRequestCode;
        Log.e(TAG, "startCamera");

        /*  Check if the Permission for the Camera was Granted  */
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.CAMERA, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_access_camera),
                cameraPermission)) return;


        /*  Check whether the Camera feature is available or not    */
        if (!isCameraAvailable()) {
            Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.external_storage_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        /*  Check for the SD CARD or External Storage   */
        if (!isExternalStorageAvailable()) {
            Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.camera_feature_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

//            File fileToBeWritten = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            File fileToBeWritten = new File(getDirectory(Constants.FileType.IMAGE_FILE), "temp.jpg");

            if (!fileToBeWritten.exists()) {
                try {
                    fileToBeWritten.createNewFile();
                } catch (IOException e) {

                               Utils.printStackTrace(e);
                }
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileToBeWritten));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    fileToBeWritten));
            activity.startActivityForResult(takePictureIntent, cameraRequestCode);
        }
    }


    /**
     * Method to start the Camera
     */
    public void startCamera() {

        Log.e(TAG, "startCamera");

        /*  Check if the Permission for the Camera was Granted  */
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.CAMERA, activity.getString(R.string.please_grant_permission_to_access_camera),
                cameraPermission)) return;
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, activity.getString(R.string.please_grant_permission_to_storage),
                readFilePermission)) return;




        /*  Check whether the Camera feature is available or not    */
        if (!isCameraAvailable()) {
            Toast.makeText(activity, activity.getString(R.string.external_storage_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        /*  Check for the SD CARD or External Storage   */
        if (!isExternalStorageAvailable()) {
            Toast.makeText(activity, activity.getString(R.string.camera_feature_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

////            File fileToBeWritten = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            File fileToBeWritten = new File(getDirectory(Constants.FileType.IMAGE_FILE), "temp.jpg");

            if (!fileToBeWritten.exists()) {
                try {
                    fileToBeWritten.createNewFile();
                } catch (IOException e) {

                               Utils.printStackTrace(e);
                }
            }


            try {
                /**
                 * file provider is used to produce Uri to support above nougat
                 */
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        fileToBeWritten);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, cameraRequestCode);
            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }
        }
    }

    /**
     * Method to retrieve the App Directory,
     * where files like logs can be Saved
     *
     * @param type The FileType
     * @return directory corresponding to the FileType
     */
    private File getDirectory(Constants.FileType type) {

        try {

            String strFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+
                    File.separator + "Tookan" + File.separator + type.directory;

            File folder = new File(strFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder;
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
        return null;
    }

    /**
     * Method to check whether the Camera feature
     * is Available or not
     *
     * @return
     */
    private boolean isCameraAvailable() {

        Log.e(TAG, "isCameraAvailable");

        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Method to check whether the Camera feature
     * is Available or not
     *
     * @return
     */
    private boolean isExternalStorageAvailable() {

        Log.e(TAG, "isExternalStorageAvailable");

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Method to perform operation on image after
     * selecting from Gallery
     *
     * @param uri
     */
    public void copyFileFromUri(Uri uri) throws IOException {

        Log.e(TAG, "copyFileFromUri");
        if (activity == null)
            return;

        InputStream inputStream = activity.getContentResolver().openInputStream(uri);

        FileOutputStream fileOutputStream = new FileOutputStream(
                new File(getDirectory(Constants.FileType.IMAGE_FILE), "temp.jpg")
        );

        byte[] buffer = new byte[1024];

        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1)
            fileOutputStream.write(buffer, 0, bytesRead);

        fileOutputStream.close();

        inputStream.close();
    }

    /**
     * Method to compress and save the bitmap to a file
     *
     * @return
     */
    public String compressAndSaveBitmap(Context context) {

        Log.e(TAG, "compressAndSaveBitmap");

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        if (screenWidth > 1024)
            screenWidth = 1024;

        return compressAndSaveBitmap(null, screenWidth);
    }

    public String compressAndSaveBitmap(ImageView imgSnapshot, int squareEdge) {

        Log.e(TAG, "compressAndSaveBitmap");

        return compressAndSaveBitmap(imgSnapshot, squareEdge, squareEdge);
    }

    /**
     * Method to compress and save the Bitmap
     *
     * @param imgSnapshot
     * @param defaultWidth
     * @param defaultHeight
     */
    public String compressAndSaveBitmap(ImageView imgSnapshot, int defaultWidth, int defaultHeight) {

        Log.e(TAG, "compressAndSaveBitmap");


        // Check and ask for Permissions
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_storage),
                saveBitmapPermission)) return null;


        int targetWidth = 0;
        int targetHeight = 0;

        if (imgSnapshot != null) {
            targetWidth = imgSnapshot.getWidth();
            if (targetWidth > 1024) targetWidth = 1024;

            targetHeight = imgSnapshot.getHeight();
            if (targetHeight > 1024) targetHeight = 1024;
        }

        if (targetWidth == 0 || targetHeight == 0) {

            targetWidth = defaultWidth;
            targetHeight = defaultHeight;

            Log.e(TAG, "Bitmap Scaling Default values (" + defaultWidth + ", " + defaultHeight + ")");
        }

//        String path = new File(Environment.getExternalStorageDirectory(), "temp.jpg").getAbsolutePath();
        String path = new File(getDirectory(Constants.FileType.IMAGE_FILE), "temp.jpg").getAbsolutePath();

        try {

            /*  Create bitmap from the default path with required width and height */
            Bitmap bitmap = convertFileToBitmap(path, targetWidth, targetHeight);

            if (bitmap == null) {
                return null;
            }

            /*  Center crop the Bitmap  */
//            Bitmap centerCrop = centerCrop(bitmap);
            // Already center cropped

            /*  Save the reduced bitmap to a file   */
            path = saveBitmapImage(bitmap);


            /*  Set the bitmap to the image */
            if (imgSnapshot != null) {
                imgSnapshot.setImageBitmap(bitmap);
            }

        } catch (IOException e) {

                               Utils.printStackTrace(e);
        }

        return path;
    }

    /**
     * Method to center crop a bitmap
     *
     * @param source
     * @return
     */
    public Bitmap centerCrop(Bitmap source) {

        Log.e(TAG, "centerCrop: Source bitmap: " + source);

        if (source == null) return null;


        if (source.getWidth() >= source.getHeight())
            return Bitmap.createBitmap(
                    source,
                    source.getWidth() / 2 - source.getHeight() / 2,
                    0,
                    source.getHeight(),
                    source.getHeight()
            );

        return Bitmap.createBitmap(
                source,
                0,
                source.getHeight() / 2 - source.getWidth() / 2,
                source.getWidth(),
                source.getWidth()
        );
    }


    /**
     * Method to open the Gallery view
     */

    public void openGallery() {

        Log.e(TAG, "openGallery");

        // Check and ask for Permissions
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_storage),
                openGalleryPermission)) return;

        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            activity.startActivityForResult(photoPickerIntent, galleryRequestCode);
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            Utils.toast(activity, StorefrontCommonData.getString(activity, R.string.no_gallery));
        }
    }

    /**
     * Method to open the Gallery view
     */

    public void openGallery(int galleryRequestCode) {

        Log.e(TAG, "openGallery");
        this.galleryRequestCode = galleryRequestCode;
        // Check and ask for Permissions
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_storage),
                openGalleryPermission)) return;

        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            activity.startActivityForResult(photoPickerIntent, galleryRequestCode);
        } catch (Exception e) {

                               Utils.printStackTrace(e);
            Utils.toast(activity, StorefrontCommonData.getString(activity, R.string.no_gallery));
        }
    }

    /**
     * Method to convert the Bitmap into file
     *
     * @param imagePath
     */
    public Bitmap convertFileToBitmap(String imagePath, ImageView targetView) {

        Log.e(TAG, "convertFileToBitmap");

        int requiredWidth = 0;
        int requiredHeight = 0;

        if (targetView != null) {
            requiredWidth = targetView.getWidth();
            requiredHeight = targetView.getHeight();
        }

        if (requiredHeight == 0 || requiredWidth == 0) {
            requiredWidth = 1024;
            requiredHeight = 1024;
        }

        return convertFileToBitmap(imagePath, requiredWidth, requiredHeight);
    }

    /**
     * Method to convert the Bitmap into file
     *
     * @param imagePath
     * @Developer: Rishabh
     * @Dated: 12.07.2015
     */
    public Bitmap convertFileToBitmap(String imagePath, int width, int height) {

        Log.e(TAG, "convertFileToBitmap");

        // Check and ask for Permissions
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, StorefrontCommonData.getString(activity, R.string.please_grant_permission_to_storage),
                readFilePermission)) return null;

        // Options to calculate the Bitmap Dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();

        // Avoid memory allocation
        options.inJustDecodeBounds = true;

        // Decode the byte array to know the width and height of bitmap
        BitmapFactory.decodeFile(imagePath, options);

        // Get the MimeType of the Image
        // String mimeType = options.outMimeType;

        // Calculate and set the Sample Size of the Bitmap
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Enable memory Allocation to the Bitmap
        options.inJustDecodeBounds = false;

        Bitmap targetBitmap;
        Bitmap sampledBitmap = null;
        try {
            // Create a Sample and Scaled down bitmap
            sampledBitmap = BitmapFactory.decodeFile(imagePath, options);


            // Cropped bitmap -> May throw an OutOfMemoryError
//            Bitmap croppedBitmap = centerCrop(sampledBitmap);
            Bitmap croppedBitmap = sampledBitmap;

            try {

                ExifInterface exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                Matrix matrix = new Matrix();

                int angle = 0;

                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        angle = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        angle = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        angle = 270;
                        break;
                }

                matrix.postRotate(angle);

                // -> May throw an OutOfMemoryError
                targetBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0,
                        croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);

            } catch (Throwable err) {

                targetBitmap = croppedBitmap;
              //  err.printStackTrace();
            }
        } catch (Throwable err) {

            targetBitmap = sampledBitmap;
           // err.printStackTrace();
        }

        return targetBitmap;
    }

    /**
     * A power of two value is calculated because the decoder
     * uses a final value by rounding down to the nearest power of two.
     *
     * @param options
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {

        Log.e(TAG, "calculateInSampleSize");

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > requiredHeight && (halfWidth / inSampleSize) > requiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * returns the byte size of the give bitmap
     */
    public int byteSizeOf(Bitmap bitmap) {

        Log.e(TAG, "byteSizeOf");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    /**
     * Method to convert a Bitmap to file
     *
     * @param bitmap
     * @param filePath
     * @throws IOException
     */
    public File convertBitmapToFile(Bitmap bitmap, String filePath) throws IOException {

        Log.e(TAG, "convertBitmapToFile");

        // Create a file to write bitmap data
        File file = new File(filePath);
        file.createNewFile();

        if (bitmap == null)
            return null;

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50   /*ignored for PNG*/, bos);

        // Write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();

        return file;
    }

    /**
     * Method to draw text on bitmap
     *
     * @param bitmap
     * @param mText
     * @return
     */
    public Bitmap drawTextOnBitmap(Bitmap bitmap, String mText) {

        Log.e(TAG, "drawTextOnBitmap");

        if (mText.indexOf('\n') == -1)
            return drawSingleLineTextOnBitmap(activity, bitmap, mText);

        return drawMultilineTextOnBitmap(bitmap, mText);

    }

    /**
     * Method to blur {@link Bitmap}s
     *
     * @param activity
     * @param source
     * @return blurred {@link Bitmap}
     */
    @SuppressLint("NewApi")
    public Bitmap blurImage(Activity activity, Bitmap source, int blurRadius) {

        Log.e(TAG, "blurImage");

        RenderScript rsScript = RenderScript.create(activity);
        Allocation alloc = Allocation.createFromBitmap(rsScript, source);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript,
                alloc.getElement());
        blur.setRadius(blurRadius);
        // blur.setRadius(23);
        blur.setInput(alloc);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();

        Bitmap output = Bitmap.createBitmap(metrics.widthPixels, 400,
                source.getConfig());
        Allocation outAlloc = Allocation.createFromBitmap(rsScript, output);
        blur.forEach(outAlloc);
        outAlloc.copyTo(output);

        rsScript.destroy();
        return output;
    }

    public Bitmap drawSingleLineTextOnBitmap(Context context, Bitmap bitmap, String mText) {

        Log.e(TAG, "drawSingleLineTextOnBitmap");

        try {
            float scale = context.getResources().getDisplayMetrics().density;

            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are immutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new anti-aliased Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(29, 24, 24));
            // text size in pixels
            paint.setTextSize((int) (33 * scale));
            // text font
            paint.setTypeface(Font.getBold(context));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) / 4;
            int y = (bitmap.getHeight() + bounds.height()) / 4;

            canvas.drawText(mText, x * scale, y * scale, paint);

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to draw text on bitmap
     *
     * @param bitmap
     * @param mText
     * @return
     */
    public Bitmap drawMultilineTextOnBitmap(Bitmap bitmap, String mText) {

        Log.e(TAG, "drawMultilineTextOnBitmap");

        try {
            float scale = activity.getResources().getDisplayMetrics().density;

            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are immutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);

            //Create anti-aliased paint
            TextPaint mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            mTextPaint.setTypeface(Font.getBold(activity));
            mTextPaint.setTextSize((int) (33 * scale));


            StaticLayout mTextLayout = new StaticLayout(mText, mTextPaint, canvas.getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

            canvas.save();

            Rect bounds = new Rect();
            mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
            mTextPaint.setColor(Color.rgb(61, 61, 61));
            mTextPaint.setTextAlign(Paint.Align.CENTER);

            int origin_x = (bitmap.getWidth()) / 2;
            int origin_y = (bitmap.getHeight()) / 3;

            canvas.translate(origin_x, origin_y);
            mTextLayout.draw(canvas);
            canvas.restore();


            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to save the Bitmap
     *
     * @param bitmap
     */
    public String saveBitmapImage(Bitmap bitmap) throws IOException {

        Log.e(TAG, "saveBitmapImage");

        String timeStamp = new SimpleDateFormat("ddMMyyyy_hhmmss", Locale.getDefault()).format(new Date());
        String fileName = activity.getString(R.string.app_name) + "_" + timeStamp + ".jpg";

//        String path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName).getAbsolutePath();
        String path = new File(getDirectory(Constants.FileType.IMAGE_FILE), fileName).getAbsolutePath();

        convertBitmapToFile(bitmap, path);

        return path;
    }

    /**
     * Method to set Custom Permission codes
     *
     * @param permCamera
     * @param permReadFile
     * @param permOpenGallery
     * @param permSaveBmp
     */
    public void setPermissionCodes(int permCamera, int permReadFile, int permOpenGallery, int permSaveBmp) {
        this.cameraPermission = permCamera;
        this.readFilePermission = permReadFile;
        this.openGalleryPermission = permOpenGallery;
        this.saveBitmapPermission = permSaveBmp;
    }
}
