package com.example.ryu.walkpast.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ryu.walkpast.R;
import com.example.ryu.walkpast.Service.AvatarAPI;
import com.squareup.picasso.Picasso;

/**
 * Created by RYU on 9/28/2017.
 */

public class ImageFragment extends Fragment {

    private ImageView updateImage;
    private AvatarAPI avatarAPI = new AvatarAPI();
    private String avatarURL;

    public ImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        updateImage = view.findViewById(R.id.image_update);
        return view;
    }

    //insert fetched image to imageview
    public void updateImageView(String newText, Context context) {
        avatarURL = avatarAPI.generateAvatar(newText, "all", "256x");
        //Picasso.with(context).load(avatarURL).into(updateImage);
        Picasso.with(context.getApplicationContext())
                .load(avatarURL)
                //.error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .fit()
                .into(updateImage);
    }
}