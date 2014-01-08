package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Created by rajohns on 12/6/13.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    public UUID crimeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

//        crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
    }

    @Override
    protected Fragment createFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("hey", crimeId);
//        CrimeCameraFragment crimeCameraFragment = new CrimeCameraFragment();
//        crimeCameraFragment.setArguments();
        crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeCameraFragment.newInstance(crimeId);
    }
}
