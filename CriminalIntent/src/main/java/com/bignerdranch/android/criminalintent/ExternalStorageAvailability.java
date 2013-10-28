package com.bignerdranch.android.criminalintent;

import android.os.Environment;

/**
 * Created by rajohns on 10/7/13.
 */
public class ExternalStorageAvailability {
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;

    private void checkStorage() {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = true;
        }
        else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        }
        else {
            mExternalStorageAvailable = false;
            mExternalStorageWriteable = false;
        }
    }

    public boolean externStorageIsAvailable() {
        checkStorage();
        return mExternalStorageAvailable;
    }

    public boolean externalStorageIsWriteable() {
        checkStorage();
        return mExternalStorageWriteable;
    }

    public boolean externalStorageIsAvailableAndWriteable() {
        checkStorage();

        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getExternalFilePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
