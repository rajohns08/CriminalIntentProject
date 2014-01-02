package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rajohns on 7/8/13.
 */
public class Crime {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_TIME = "time";
    private static final String JSON_DATEFORMAT = "dateFormat";
    private static final String JSON_TIMEFORMAT = "timeFormat";

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private Photo mPhoto;
    private Date mTime;
    private boolean mSolved;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    public Crime() {
        mID = UUID.randomUUID();
        mDate = new Date();
        mTime = new Date();
        dateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
        timeFormat = new SimpleDateFormat("K:mm a");
    }

    public Crime(JSONObject json) throws JSONException {
        mID = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        mTime = new Date(json.getLong(JSON_TIME));
        dateFormat = new SimpleDateFormat(json.getString(JSON_DATEFORMAT));
        timeFormat = new SimpleDateFormat(json.getString(JSON_TIMEFORMAT));

        if (json.has(JSON_PHOTO)) {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mID.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_TIME, mTime.getTime());
        json.put(JSON_DATEFORMAT, dateFormat.toPattern());
        json.put(JSON_TIMEFORMAT, getTimeFormat().toPattern());

        // TODO: FIGURE OUT HOW TO DELETE PHOTOS

        if (mPhoto != null) {
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }

        return json;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public SimpleDateFormat getTimeFormat() {
        if (mTime.getHours() == 12 || mTime.getHours() == 0) {
            return new SimpleDateFormat("12:mm a");
        }
        return timeFormat;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public UUID getmID() {
        return mID;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }
}
