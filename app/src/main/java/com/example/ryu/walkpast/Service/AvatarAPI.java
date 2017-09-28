package com.example.ryu.walkpast.Service;

import android.util.Log;

/**
 * Created by RYU on 9/25/2017.
 */

public class AvatarAPI {

    private String API_URL = "http://loremavatar.com/api/avatar/halloween/";
    private static final String TAG = AvatarAPI.class.getSimpleName();

    public String generateAvatar(String name, String gender, String size){
        Log.d(TAG, API_URL + name + "/" + gender + "/" + size);
        return API_URL + name + "/" + gender + "/" + size;
    }
}
