package com.example.ryu.walkpast.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryu.walkpast.R;

/**
 * FRAGMENT FOR ASKING USER'S NAME AND HAS WELCOME MESSAGE
 * user's input is given to Image Fragment to update the avatar.
 * Created by RYU on 9/28/2017.
 */

public class UserInputFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public TextView welcomemsg;
    public EditText editUserName;
    public Button updateName;
    public EditText editMetaWear;
    public Button updateMetaWear;
    public Button start;
    private String userData;
    private String metaAddr;
    private TextView askMetaWear;
    private TextView explainMetaWear;

    public UserInputFragment() {
    }

    /*
     SETUP VIEW
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        welcomemsg = view.findViewById(R.id.whoareyou);
        editUserName = view.findViewById(R.id.user_name);
        explainMetaWear = view.findViewById(R.id.whatismac);
        editMetaWear = view.findViewById(R.id.user_metawear);


        //button for name input
        updateName = view.findViewById(R.id.button);
        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUserName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "User input value must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                userData = editUserName.getText().toString();
                welcomemsg.setText("What is your MetaWear MAC address?");
                editUserName.setVisibility(View.GONE);
                updateName.setVisibility(View.GONE);
                editMetaWear.setVisibility(View.VISIBLE);
                explainMetaWear.setVisibility(View.VISIBLE);
                updateMetaWear.setVisibility(View.VISIBLE);
            }
        });

        //button for metawear address input
        updateMetaWear = view.findViewById(R.id.button2);
        updateMetaWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metaAddr = editMetaWear.getText().toString();
                onButtonPressed(userData, metaAddr, false);
                editMetaWear.setVisibility(View.GONE);
                updateMetaWear.setVisibility(View.GONE);
                //TODO: put strings in resource
                welcomemsg.setText(getString(R.string.hello) + " " + userData + ". This is an interactive story that requires " +
                        "actual walking around in real life. Are you ready to take a few steps to reach your destination? Take your " +
                        "MetaWear with you to see your character move around. Let's go!");
                start.setVisibility(View.VISIBLE);
            }
        });

        //button for starting game
        start = view.findViewById(R.id.buttonstart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
                if (mListener != null) {
                    mListener.onFragmentInteraction(userData, metaAddr, true);
                }
            }
        });

        return view;
    }

    /*
     TELLING LISTENER THAT A BUTTON HAS BEEN PRESSED
     If button story is true the application will know that the second button has been pressed and
     the story can be shown.
     */
    public void onButtonPressed(String userContent, String userAddr, Boolean story) {
        if (mListener != null) {
            mListener.onFragmentInteraction(userContent, userAddr, story);
        }
    }

    /*
     START LISTENER
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /*
     STOP LISTENING
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
     INFORM MAIN ACTIVITY THAT BUTTON HAS BEEN PRESSED
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String userContent, String userMetaWear, Boolean story);
    }

}