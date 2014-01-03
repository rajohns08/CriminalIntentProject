package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajohns on 12/19/13.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFilename;
    private int mRotation;
    private boolean mAlreadyRotated;

    public Photo(String filename) {
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }

    public int getmRotation() {
        return mRotation;
    }

    public void setmRotation(int mRotation) {
        this.mRotation = mRotation;
    }

    public boolean ismAlreadyRotated() {
        return mAlreadyRotated;
    }

    public void setmAlreadyRotated(boolean mAlreadyRotated) {
        this.mAlreadyRotated = mAlreadyRotated;
    }
}
