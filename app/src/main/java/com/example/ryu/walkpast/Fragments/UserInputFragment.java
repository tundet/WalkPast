package com.example.ryu.walkpast.Fragments;

import android.app.Activity;
import android.app.Fragment;
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
 * Created by RYU on 9/28/2017.
 */

public class UserInputFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public TextView welcomemsg;
    public EditText userInput;
    public Button update;
    public Button start;
    private String userData;
    private Boolean showStory = false;
    //private String gender;
    //private Spinner spinner;

    public UserInputFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        welcomemsg = view.findViewById(R.id.whoareyou);
        userInput = view.findViewById(R.id.user_input);

        /*
        //spinner in case of choosing gender
        spinner = view.findViewById(R.id.spinner);
        String[] items = new String[]{"female", "male", "all"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        gender = "female";
                        break;
                    case 1:
                        gender = "male";
                        break;
                    case 2:
                        gender = "all";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        */

        //button for name input
        update = view.findViewById(R.id.button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "User input value must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                userData = userInput.getText().toString();
                onButtonPressed(userData, showStory);
                start.setVisibility(View.VISIBLE);
            }
        });


        start = view.findViewById(R.id.buttonstart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
                showStory = true;
                if (mListener != null) {
                    mListener.onFragmentInteraction(userData, showStory);
                }
            }
        });

        return view;
    }

    public void onButtonPressed(String userContent, Boolean story) {
        if (mListener != null) {
            mListener.onFragmentInteraction(userContent, story);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //tell that button has been pressed
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String userContent, Boolean story);
    }

}