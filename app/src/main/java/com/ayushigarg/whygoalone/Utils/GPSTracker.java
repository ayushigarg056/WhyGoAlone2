package com.ayushigarg.whygoalone.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import  com.ayushigarg.whygoalone.Activities.DashboardActivity;
import  com.ayushigarg.whygoalone.AsyncTasks.LocationAsyncTask;
import  com.ayushigarg.whygoalone.Interfaces.GetLL;
import  com.ayushigarg.whygoalone.Interfaces.OnGettingLocation;
import  com.ayushigarg.whygoalone.R;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;


public class GPSTracker implements LocationListener {

    private static final int LOC_REQ_CODE = 9876;
    private static String TAG = GPSTracker.class.getName();
    private final Context mContext;
    private final LocationManager locationManager;
    ProgressDialog progressDialog;
    GetLL getLL;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean isGPSTrackingEnabled = false;

    Location location = null;
    Double latitude = new Double(0);
    Double longitude = new Double(0);

    int geocoderMaxResults = 1;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private String provider_info;

    Context ctx;
    String area, city;
    RecyclerView rvList;
    String la, lo;

    public GPSTracker(Context context, LocationManager locationManager, GetLL getLL, Context ctx, RecyclerView rvList) {
        this.mContext = context;
        this.locationManager = locationManager;
        this.getLL = getLL;
        this.ctx = ctx;
        this.rvList = rvList;
        getLocation(locationManager);
    }

    public void getLocation(LocationManager locationManager) {

        try {

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;

                provider_info = LocationManager.GPS_PROVIDER;

            }
            else if (isNetworkEnabled) {
                this.isGPSTrackingEnabled = true;

                Log.d(TAG, "Application use Network State to get GPS coordinates");

                provider_info = LocationManager.NETWORK_PROVIDER;

            }

            if (!provider_info.isEmpty()) {

                //noinspection MissingPermission
                locationManager.requestLocationUpdates(provider_info,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    //noinspection MissingPermission
//                    while (location == null) {
//                    LocationAsyncTask locationAsyncTask = new LocationAsyncTask(location, locationManager, provider_info);
//                    CityRestaurants c = new CityRestaurants(ctx, "Delhi");

                    location = locationManager.getLastKnownLocation(provider_info);
                        if (location == null){
                            progressDialog = new ProgressDialog(ctx);
                            progressDialog.setMessage("Fetching Location...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getLL.onSuccess(ctx, "Noida", "Noida", "28.6305", "77.3721", rvList);
                                    progressDialog.dismiss();
                                }
                            }, 5000);
                        }

//                        onGettingLocation.onSuccess(location);
//                    }
                    else {
                        updateGPSCoordinates();
                        getLL.onSuccess(ctx, getAddressLine(ctx), getLocality(ctx), getLatitude().toString(), getLongitude().toString(), rvList);
                    }
                }
                else{
                    getLL.onError("Location Not Found");
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to connect to LocationManager", e);
        }
    }

    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public Double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public Double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean getIsGPSTrackingEnabled() {

        return this.isGPSTrackingEnabled;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Please Turn on GPS");

        alertDialog.setMessage("Your GPS is Disabled, this app need the GPS enabled to work!");

        alertDialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                mContext.startActivity(intent);
                ((Activity) mContext).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOC_REQ_CODE);

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                ((Activity) mContext).finish();
                Toast.makeText(mContext, "Please Turn On Location", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }

    public List<Address> getGeocoderAddress(Context context) {
        if (location != null) {

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, this.geocoderMaxResults);

                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e(TAG, "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }

    public String getAddressLine(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }

    public String getLocality(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            Log.d("OO", "getLocality: " + locality);
            return locality;
        }
        else {
            return null;
        }
    }

    public String getPostalCode(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        } else {
            return null;
        }
    }

    public String getCountryName(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();

            return countryName;
        } else {
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        onGettingLocation = new OnGettingLocation() {
//            @Override
//            public void onSuccess(Location location1) {
//                location1 = location;
//            }
//        };
//        this.location = location;
        Log.d("Badla", "onLocationChanged: ");
        this.location = location;
        updateGPSCoordinates();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        getLL.onSuccess(ctx, getAddressLine(ctx), getLocality(ctx), getLatitude().toString(), getLongitude().toString(), rvList);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void thodaDelay(){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 5000);
    }



}
