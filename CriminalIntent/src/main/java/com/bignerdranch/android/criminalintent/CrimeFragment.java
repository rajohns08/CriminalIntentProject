package com.bignerdranch.android.criminalintent;
// MAIN BRANCH!!!! AFTER FIXING GITIGNORE AGAIN TEST

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rajohns on 7/9/13.
 * Activity for Viewing setting/viewing an individual crime's details
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateAndTimeButton;
    private CheckBox mSolvedCheckbox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        setHasOptionsMenu(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCrime.setmTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateAndTimeButton = (Button)v.findViewById(R.id.crime_date_and_time);
        updateDateAndTime();
        mDateAndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Change Date or Time")
                        .setPositiveButton("Time", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getmTime());
                                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                                timeDialog.show(fm, DIALOG_TIME);
                            }
                        })
                        .setNegativeButton("Date", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                                dialog.show(fm, DIALOG_DATE);
                            }
                        })
                        .show();
            }
        });

        mSolvedCheckbox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.ismSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setmSolved(b);
            }
        });

        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                i.putExtra(EXTRA_CRIME_ID, mCrime.getmID());
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo p = mCrime.getmPhoto();
                if (p == null) {
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            mPhotoButton.setEnabled(false);
        }

        return v;
    }

    private void showPhoto() {
        Photo p = mCrime.getmPhoto();
        BitmapDrawable b = null;

        //TODO: 1. FIGURE OUT WHY THUMBNAIL PHOTO ROTATES INCORRECTLY FOR LANDSCAPE PICTURE AFTER RELAUNCHING APP
        //TODO: IF I UNCOMMENT THE IF STATEMENTS AND COMMENT THE LINE DIRECTLY BELOW (THE FIX I CAME UP WITH FOR ABOVE) WHY DOES THIS FUNCTION GET CALLED SO MANY TIMES WITH DIFFERENT RESULTS 
        // it is probably because after the image was successfully rotated after taking it, the program doesn't know it has already been successfully rotated the next time the app is launched. need to say boolean has been rotated.
        // need to make already rotated to false every time a new pic is taken

        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);

            if (p.getmRotation() == Surface.ROTATION_0) {
                Matrix matrix = new Matrix();

//                if (!mCrime.getmPhoto().ismAlreadyRotated()) matrix.postRotate(90);
                matrix.postRotate(90);

                Bitmap rotatedBitmap = Bitmap.createBitmap(b.getBitmap(), 0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight(), matrix, true);
                mPhotoView.setImageBitmap(rotatedBitmap);
                mCrime.getmPhoto().setmAlreadyRotated(true);
            }
            else if (p.getmRotation() == Surface.ROTATION_270) {
                Matrix matrix = new Matrix();

//                if (!mCrime.getmPhoto().ismAlreadyRotated()) matrix.postRotate(180);
                matrix.postRotate(180);

                Bitmap rotatedBitmap = Bitmap.createBitmap(b.getBitmap(), 0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight(), matrix, true);
                mPhotoView.setImageBitmap(rotatedBitmap);
                mCrime.getmPhoto().setmAlreadyRotated(true);
            }
            else {
                mPhotoView.setImageDrawable(b);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDateAndTime();
        }
        else if (requestCode == REQUEST_TIME) {
            Date time = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmTime(time);
            updateDateAndTime();
        }
        else if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            int rotation = data.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0);

            if (filename != null) {
                Photo p = new Photo(filename);
                p.setmRotation(rotation);
                mCrime.setmPhoto(p);
                showPhoto();
            }
        }
    }

    public void updateDateAndTime() {
        mDateAndTimeButton.setText(mCrime.getDateFormat().format(mCrime.getmDate()) + ", " +  mCrime.getTimeFormat().format(mCrime.getmTime()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_crime_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_detail_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                alertDialogBuilder.setTitle("Delete Crime");

                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this crime?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                                if (NavUtils.getParentActivityName(getActivity()) != null) {
                                    NavUtils.navigateUpFromSameTask(getActivity());
                                }
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
