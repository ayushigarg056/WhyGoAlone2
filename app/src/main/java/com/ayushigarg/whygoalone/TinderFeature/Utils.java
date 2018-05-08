package com.ayushigarg.whygoalone.TinderFeature;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishaandhamija on 22/06/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static List<Profile> loadProfiles(Context context){

        List<Profile> profileList = new ArrayList<>();



        profileList.add(new Profile("https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg", "Sofia",  20, "New York"));
        profileList.add(new Profile("https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg", "Sofia",  20, "New York"));
        profileList.add(new Profile("https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg", "Sofia",  20, "New York"));
        profileList.add(new Profile("https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg", "Sofia",  20, "New York"));
        profileList.add(new Profile("https://pbs.twimg.com/profile_images/572905100960485376/GK09QnNG.jpeg", "Sofia",  20, "New York"));

        return profileList;
    }

}
