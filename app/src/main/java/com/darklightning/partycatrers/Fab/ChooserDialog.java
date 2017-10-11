package com.darklightning.partycatrers.Fab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.darklightning.partycatrers.R;


/**
 * Created by rikki on 10/5/17.
 */

public class ChooserDialog extends Dialog implements View.OnClickListener
{
    LinearLayout camera_layout,gallery_layout;
    TakePictureActivity demoActivity;
    int image_id;

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

    public ChooserDialog(@NonNull TakePictureActivity demoActivity, int image_id) {
        super(demoActivity);
        this.demoActivity = demoActivity;
        this.image_id = image_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_chooser);

        camera_layout = (LinearLayout) findViewById(R.id.camera_layout);
        gallery_layout = (LinearLayout) findViewById(R.id.gallery_layout);
        camera_layout.setOnClickListener(this);
        gallery_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.camera_layout:
                switch (image_id){
                    case R.id.main_image_plus:
                        demoActivity.takeImageFromCamera(REQUEST_CAMERA_CODE_0);
                        break;
                    case R.id.other_image_1_plus:
                        demoActivity.takeImageFromCamera(REQUEST_CAMERA_CODE_1);
                        break;
                    case R.id.other_image_2_plus:
                        demoActivity.takeImageFromCamera(REQUEST_CAMERA_CODE_2);
                        break;
                    case R.id.other_image_3_plus:
                        demoActivity.takeImageFromCamera(REQUEST_CAMERA_CODE_3);
                        break;
                    case R.id.other_image_4_plus:
                        demoActivity.takeImageFromCamera(REQUEST_CAMERA_CODE_4);
                        break;
                }
                dismiss();
                break;
            case R.id.gallery_layout:
                switch (image_id){
                    case R.id.main_image_plus:
                        demoActivity.getImageFromGallery(REQUEST_GALLERY_CODE_0);
                        break;
                    case R.id.other_image_1_plus:
                        demoActivity.getImageFromGallery(REQUEST_GALLERY_CODE_1);
                        break;
                    case R.id.other_image_2_plus:
                        demoActivity.getImageFromGallery(REQUEST_GALLERY_CODE_2);
                        break;
                    case R.id.other_image_3_plus:
                        demoActivity.getImageFromGallery(REQUEST_GALLERY_CODE_3);
                        break;
                    case R.id.other_image_4_plus:
                        demoActivity.getImageFromGallery(REQUEST_GALLERY_CODE_4);
                        break;
                }
                dismiss();
                break;
        }
    }
}
