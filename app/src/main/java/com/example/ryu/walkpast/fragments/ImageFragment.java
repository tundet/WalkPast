package com.example.ryu.walkpast.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ryu.walkpast.R;
import com.example.ryu.walkpast.service.AvatarAPI;
import com.squareup.picasso.Picasso;

/**
 * FRAGMENT TO SHOW USER AVATAR IN IMAGE VIEW
 * gets username from User Input Fragment and fetches the image from Avatar API using Picasso.
 * Picasso handles the image loading asynchronously.
 * Created by RYU on 9/28/2017.
 */

public class ImageFragment extends Fragment {

    public ImageView updateImage;
    private AvatarAPI avatarAPI = new AvatarAPI();
    private String avatarURL;

    public ImageFragment() {
    }

    /*
     SETUP VIEW
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        updateImage = view.findViewById(R.id.image_update);
        return view;
    }

    /*
     INSERT AVATAR INTO IMAGE VIEW
     */
    public void updateImageView(String newText, Context context) {
        //avatarURL = avatarAPI.generateAvatar(newText, "all", "256x");
        //Picasso.with(context).load(avatarURL).into(updateImage);
        Picasso.with(context.getApplicationContext())
                //API currently unavailable so image is set to Spondebob Squarepants
                .load("https://vignette2.wikia.nocookie.net/spongebob/images/6/6c/OldSpongeBobStock5-25-13.png")
                .placeholder(R.drawable.progress_animation)
                .resize(400, 503)
                .into(updateImage);
    }
}