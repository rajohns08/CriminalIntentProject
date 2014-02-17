package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rajohns on 7/16/13.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    public void onCrimeSelected(Crime crime) {
        
    }
}
