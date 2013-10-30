package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by rajohns on 7/15/13.
 */

// TODO: FIGURE OUT HOW TO INHERIT FROM ACTIONBARACTIVITY SO ACTION BAR SHOWS ON 2.X VERSIONS OF ANDROID

public abstract class SingleFragmentActivity extends FragmentActivity{

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
}
