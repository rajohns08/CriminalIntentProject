package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajohns on 12/19/13.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";
    private static final String JSON_ROTATION = "rotation";
    private String mFilename;
    private int mRotation;

    public Photo(String filename) {
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
        mRotation = json.getInt(JSON_ROTATION);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        json.put(JSON_ROTATION, mRotation);
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
}
