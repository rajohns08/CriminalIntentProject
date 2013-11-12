package com.bignerdranch.android.criminalintent;
// ABS BRANCH!!!!!


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rajohns on 7/15/13.
 */

public class CrimeListFragment extends ListFragment{

    private ArrayList<Crime> mCrimes;
    private static final String TAG = "CrimeListFragment";
    private boolean mSubtitleVisible;
    private Button plusButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);

        setRetainInstance(true);
        mSubtitleVisible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crimelist, parent, false);

        plusButton = (Button)v.findViewById(R.id.add_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddCrimePage();
            }
        });

        if (mSubtitleVisible) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }

        final android.support.v7.view.ActionMode.Callback actionModeCallback = new android.support.v7.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
                MenuInflater infl = actionMode.getMenuInflater();
                infl.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (getListView().isItemChecked(i)) {
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                        }
                        actionMode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {

            }
        };

        ListView listView = (ListView)v.findViewById(android.R.id.list);

        // TODO: HAVE A WAY TO CHECK ITEMS SO THAT IN ONACTIONITEMCLICKED FOR DELETE, IT WILL KNOW WHICH ITEMS ARE CHECKED. SHOULD THIS BE DONE IN AN ONCLICKLISTENER? ALSO SET BACKGROUND DARK COLOR SO WE KNOW WHEN CHECKED
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((ActionBarActivity)getActivity()).startSupportActionMode(actionModeCallback);
                return false;
            }
        });

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmID());
        startActivity(i);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            Crime c = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getmTitle());
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);

            dateTextView.setText(c.getDateFormat().format(c.getmDate()) + ", " + c.getTimeFormat().format(c.getmTime()));

            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.ismSolved());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                goToAddCrimePage();
                return true;
            case R.id.menu_item_show_subtitle:
                toggleSubtitle(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toggleSubtitle(MenuItem item) {
        if (((ActionBarActivity)getActivity()).getSupportActionBar().getSubtitle() == null) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            mSubtitleVisible = true;
            item.setTitle(R.string.hide_subtitle);
        }
        else {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(null);
            mSubtitleVisible = false;
            item.setTitle(R.string.show_subtitle);
        }
    }

    public void goToAddCrimePage() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getmID());
        startActivityForResult(i, 0);
    }

}
