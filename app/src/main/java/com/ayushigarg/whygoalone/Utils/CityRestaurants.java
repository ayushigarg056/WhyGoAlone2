package com.ayushigarg.whygoalone.Utils;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ishaandhamija on 23/07/17.
 */

public class CityRestaurants {

    Context ctx;
    String cityName;

    CityRestaurants(Context ctx, String cityName){

        this.ctx = ctx;
        this.cityName = cityName;
        getLatLon(cityName);
        Log.d("LLLL", "CityRestaurants: ");
    }

    void getLatLon(String cityName){
        String location=cityName;
        String inputLine = "";
        String result = "";
        location=location.replaceAll(" ", "%20");
        String myUrl="http://maps.google.com/maps/geo?q="+location+"&output=csv";
        String lat = null;
        String longi = null;
        try{
            URL url=new URL(myUrl);
            URLConnection urlConnection=url.openConnection();
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(urlConnection.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                result=inputLine;
            }
            lat = result.substring(6, result.lastIndexOf(","));
            longi = result.substring(result.lastIndexOf(",") + 1);
            Log.d("LLL", "getLatLon: Mila");
        }
        catch(Exception e){
            Log.d("LLLL", "getLatLon: Dikkat");
            e.printStackTrace();
        }
        Double latitude=Double.parseDouble(lat);
        Double longitude=Double.parseDouble(longi);

    }

}
