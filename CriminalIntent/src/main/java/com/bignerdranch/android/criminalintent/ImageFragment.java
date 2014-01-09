package com.bignerdranch.android.criminalintent;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by rajohns on 12/19/13.
 */
public class ImageFragment extends android.support.v4.app.DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.bignerdranch.android.criminalintent.image_path";
    public static final String EXTRA_PHOTO_ROTATION = "com.bignerdranch.android.criminalintent.photo_rotation";

    public static ImageFragment newInstance(String imagePath, int rotation) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        args.putInt(EXTRA_PHOTO_ROTATION, rotation);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        int rotation = getArguments().getInt(EXTRA_PHOTO_ROTATION);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);

        //TODO: 1. COMBINE THIS ROTATE CODE AND SAME ROTATE CODE IN SHOWPHOTO() OF CRIMEFRAGMENT INTO SEPARATE CLASS

        if (rotation == Surface.ROTATION_0) {           // NORMAL PORTRAIT
            Matrix matrix = new Matrix();

            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(image.getBitmap(), 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight(), matrix, true);
            mImageView.setImageBitmap(rotatedBitmap);
        }
        else if (rotation == Surface.ROTATION_270) {    // WEIRD LANDSCAPE
            Matrix matrix = new Matrix();

            matrix.postRotate(180);
            Bitmap rotatedBitmap = Bitmap.createBitmap(image.getBitmap(), 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight(), matrix, true);
            mImageView.setImageBitmap(rotatedBitmap);
        }
        else {                                                  // NORMAL LANDSCAPE
            mImageView.setImageDrawable(image);
        }

        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}