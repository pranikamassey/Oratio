package com.darklightning.partycatrers.Fab;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.darklightning.partycatrers.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by rikki on 10/7/17.
 */

public class TakePictureActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    ImageView main_image, other_image_1, other_image_2, other_image_3, other_image_4;
    CardView main_image_plus, main_image_cross, other_image_1_plus, other_image_1_cross, other_image_2_plus, other_image_2_cross,
            other_image_3_plus, other_image_3_cross, other_image_4_plus, other_image_4_cross;
    TextView next_activity;

    MultipartBody.Builder builder = new MultipartBody.Builder();
    MultipartBody.Part fileToUpload, fileToUpload_1, fileToUpload_2;
    RequestBody filename, filename_1, filename_2;

    private static final int REQUEST_CAMERA_CODE_0 = 8370;
    private static final int REQUEST_CAMERA_CODE_1 = 8371;
    private static final int REQUEST_CAMERA_CODE_2 = 8372;
    private static final int REQUEST_CAMERA_CODE_3 = 8373;
    private static final int REQUEST_CAMERA_CODE_4 = 8374;
    private static final int REQUEST_GALLERY_CODE_0 = 200;
    private static final int REQUEST_GALLERY_CODE_1 = 201;
    private static final int REQUEST_GALLERY_CODE_2 = 202;
    private static final int REQUEST_GALLERY_CODE_3 = 203;
    private static final int REQUEST_GALLERY_CODE_4 = 204;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "Path_to_your_server";
    HashMap<String, RequestBody> map = new HashMap<>(2);
    List<MultipartBody.Part> parts = new ArrayList<>();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 456;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        mContext = this;
        main_image = (ImageView) findViewById(R.id.main_image_prev);
        main_image_plus = (CardView) findViewById(R.id.main_image_plus);
        main_image_cross = (CardView) findViewById(R.id.main_image_cross);
        other_image_1 = (ImageView) findViewById(R.id.other_image_1);
        other_image_1_plus = (CardView) findViewById(R.id.other_image_1_plus);
        other_image_1_cross = (CardView) findViewById(R.id.other_image_1_cross);
        other_image_2 = (ImageView) findViewById(R.id.other_image_2);
        other_image_2_plus = (CardView) findViewById(R.id.other_image_2_plus);
        other_image_2_cross = (CardView) findViewById(R.id.other_image_2_cross);
        other_image_3 = (ImageView) findViewById(R.id.other_image_3);
        other_image_3_plus = (CardView) findViewById(R.id.other_image_3_plus);
        other_image_3_cross = (CardView) findViewById(R.id.other_image_3_cross);
        other_image_4 = (ImageView) findViewById(R.id.other_image_4);
        other_image_4_plus = (CardView) findViewById(R.id.other_image_4_plus);
        other_image_4_cross = (CardView) findViewById(R.id.other_image_4_cross);
        next_activity = (TextView) findViewById(R.id.next_activity);

        main_image_plus.setOnClickListener(this);
        main_image_cross.setOnClickListener(this);
        other_image_1_plus.setOnClickListener(this);
        other_image_1_cross.setOnClickListener(this);
        other_image_2_plus.setOnClickListener(this);
        other_image_2_cross.setOnClickListener(this);
        other_image_3_plus.setOnClickListener(this);
        other_image_3_cross.setOnClickListener(this);
        other_image_4_plus.setOnClickListener(this);
        other_image_4_cross.setOnClickListener(this);
        next_activity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_activity:
                Intent intent = new Intent(mContext, PostAdsDetails.class);
                startActivity(intent);
                break;
            case R.id.main_image_plus:
            case R.id.other_image_1_plus:
            case R.id.other_image_2_plus:
            case R.id.other_image_3_plus:
            case R.id.other_image_4_plus:
                startChooser(v.getId());
                break;
            case R.id.main_image_cross:
            case R.id.other_image_1_cross:
            case R.id.other_image_2_cross:
            case R.id.other_image_3_cross:
            case R.id.other_image_4_cross:
                removeImage(v.getId());
                break;

        }
    }

    private void removeImage(int id) {
        switch (id) {
            case R.id.main_image_cross:
                main_image.setImageResource(R.drawable.photo_preview);
                break;
            case R.id.other_image_1_cross:
                other_image_1.setImageResource(R.drawable.photo_preview);
                break;
            case R.id.other_image_2_cross:
                other_image_2.setImageResource(R.drawable.photo_preview);
                break;
            case R.id.other_image_3_cross:
                other_image_3.setImageResource(R.drawable.photo_preview);
                break;
            case R.id.other_image_4_cross:
                other_image_4.setImageResource(R.drawable.photo_preview);
                break;
        }

    }

    private void startChooser(int image_id) {
        ChooserDialog cd = new ChooserDialog(this, image_id);
        cd.show();
    }

    public void takeImageFromCamera(int request_code) {
        Log.e("camera", "camera_start");
        if (checkPermissionForCamera(mContext, request_code)) {
//            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//
//            File output = new File(dir, "CameraContentDemo.jpeg");
//            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
//
//            startActivityForResult(i, request_code);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, request_code);
        }
    }

    public void getImageFromGallery(int request_code) {
        if (checkPermissionREAD_EXTERNAL_STORAGE(mContext)) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, request_code);
        }
    }


    public boolean checkPermissionForCamera(final Context context, final int MY_PERMISSIONS_REQUESTS) {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {

                    showDialog("Camera", context, Manifest.permission.CAMERA, MY_PERMISSIONS_REQUESTS);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUESTS);
                }
                return false;
            } else {

                return true;
            }
        } else {
            return true;
        }

    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission, final int myPermissionsRequestCamera) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                myPermissionsRequestCamera);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.e("photo_uri " ,""+uri);
            Bitmap photo = null;
            if(data.getExtras()!=null) photo = (Bitmap) data.getExtras().get("data");

            switch (requestCode) {
                case REQUEST_CAMERA_CODE_0:
                    MediaStore.Images.Media.insertImage(getContentResolver(), photo, "helle" , "kjsdn");
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/Kirayepay";
                    File myDir = new File(root);
                    myDir.mkdirs();
                    String fname = "Image-" + "weewwkhabcj" + ".png";
                    File file = new File(myDir, fname);
//                    Uri urib =  Uri.fromFile(file);
//                    System.out.println(file.getAbsolutePath());
                    if (file.exists()) file.delete();
                    Log.i("LOAD", root + fname);
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        photo.compress(Bitmap.CompressFormat.PNG, 90, out);

                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
                    main_image.setImageBitmap(photo);

                    break;

                case REQUEST_GALLERY_CODE_0:
                    main_image.setImageURI(uri);
                    break;
                case REQUEST_CAMERA_CODE_1:
                    other_image_1.setImageBitmap(photo);
                    break;
                case REQUEST_GALLERY_CODE_1:
                    other_image_1.setImageURI(uri);
                    break;
                case REQUEST_CAMERA_CODE_2:
                    other_image_2.setImageBitmap(photo);
                    break;
                case REQUEST_GALLERY_CODE_2:
                    other_image_2.setImageURI(uri);
                    break;
                case REQUEST_CAMERA_CODE_3:
                    other_image_3.setImageBitmap(photo);
                    break;
                case REQUEST_GALLERY_CODE_3:
                    other_image_3.setImageURI(uri);
                    break;
                case REQUEST_CAMERA_CODE_4:
                    other_image_4.setImageBitmap(photo);
                    break;
                case REQUEST_GALLERY_CODE_4:
                    other_image_4.setImageURI(uri);
                    break;

            }
        }

    }

    private void createFile(int request_code, Uri uri) {
        String filePath = getRealPathFromURIPath(uri, TakePictureActivity.this);

        File file = new File(filePath);
        Log.e("Filepath ", "" + filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        if (request_code == 200) {
            fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
            filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        } else {
//            RequestBody filed;
//                filed = RequestBody.create(MediaType.parse("multipart/form-data"),file.getName());
//                map.put("file\"; filename=\"" +file.getName() + ".jpg", filed);

            builder.setType(MultipartBody.FORM);


            builder.addFormDataPart("avatars_attributes", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));


        }
    }

    private String getRealPathFromURIPath(Uri contentURI, AppCompatActivity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            cursor.close();
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
