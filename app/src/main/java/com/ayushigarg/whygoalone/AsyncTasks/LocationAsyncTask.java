package com.ayushigarg.whygoalone.AsyncTasks;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

/**
 * Created by ishaandhamija on 02/07/17.
 */

public class LocationAsyncTask extends AsyncTask<LocationManager, Void, Void> {

    public LocationAsyncTask(Location location, LocationManager locationManager, String provide_info) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(LocationManager... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
    }
}
