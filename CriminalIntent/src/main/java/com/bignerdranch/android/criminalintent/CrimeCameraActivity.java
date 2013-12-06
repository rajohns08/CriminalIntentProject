package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rajohns on 12/6/13.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
