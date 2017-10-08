package com.example.ryu.walkpast.service;

import android.util.Log;

/**
 * AVATAR API
 * fetch path for user's avatar based on their name from Lorem Avatar
 * Created by RYU on 9/25/2017.
 */

public class AvatarAPI {

    private String API_URL = "http://loremavatar.com/api/avatar/halloween/";
    private static final String TAG = AvatarAPI.class.getSimpleName();

    /*
     GENERATE URL FROM NAME, GENDER AND IMAGE SIZE
     */
    public String generateAvatar(String name, String gender, String size){
        Log.d(TAG, API_URL + name + "/" + gender + "/" + size);
        return API_URL + name + "/" + gender + "/" + size;
    }
}
