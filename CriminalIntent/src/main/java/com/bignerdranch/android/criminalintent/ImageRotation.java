package com.bignerdranch.android.criminalintent;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Surface;
import android.widget.ImageView;

/**
 * Created by rajohns on 1/10/14.
 */
public class ImageRotation {

    public static ImageView rotateImage(ImageView imageView, int rotation, BitmapDrawable bitmap) {
        if (rotation == Surface.ROTATION_0) {           // NORMAL PORTRAIT
            Matrix matrix = new Matrix();

            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap.getBitmap(), 0, 0, bitmap.getIntrinsicWidth(), bitmap.getIntrinsicHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }
        else if (rotation == Surface.ROTATION_270) {    // WEIRD LANDSCAPE
            Matrix matrix = new Matrix();

            matrix.postRotate(180);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap.getBitmap(), 0, 0, bitmap.getIntrinsicWidth(), bitmap.getIntrinsicHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }
        else {                                          // NORMAL LANDSCAPE
            imageView.setImageDrawable(bitmap);
        }

        return imageView;
    }
}
