package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by rajohns on 9/24/13.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;
    private static final boolean useExternalStorage = false;

    public CriminalIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Crime c : crimes) {
            array.put(c.toJSON());
        }

        if (useExternalStorage) {
            ExternalStorageAvailability extStorage = new ExternalStorageAvailability();
            File file = new File(mContext.getExternalFilesDir(null), mFilename);
            Writer writer = null;

            if (extStorage.externalStorageIsAvailableAndWriteable()) {
                try {
                    OutputStream os = new FileOutputStream(file);
                    writer = new OutputStreamWriter(os);
                    writer.write(array.toString());
                } catch (IOException e) {
                    Log.w("ExternalStorage", "Error writing " + file, e);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }

        else {  // Write to internal storage
            Writer writer = null;
            try {
                OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }


    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;

        if (useExternalStorage) {
            ExternalStorageAvailability extStorage = new ExternalStorageAvailability();
            File file = new File(mContext.getExternalFilesDir(null), mFilename);

            if (extStorage.externStorageIsAvailable()) {
                try {
                    InputStream is = new FileInputStream(file);
                    reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder jsonString = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        jsonString.append(line);
                    }

                    JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                    for (int i = 0; i < array.length(); i++) {
                        crimes.add(new Crime(array.getJSONObject(i)));
                    }
                } catch (FileNotFoundException e) {
                    Log.w("ExternalStorage", "File not found to read from");
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        }

        else {  // Read from internal storage
            try {
                InputStream in = mContext.openFileInput(mFilename);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }

                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    crimes.add(new Crime(array.getJSONObject(i)));
                }
            } catch (FileNotFoundException e) {

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }


        return crimes;
    }

}
