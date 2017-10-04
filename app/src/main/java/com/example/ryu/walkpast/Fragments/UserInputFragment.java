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
    public EditText userInput;
    public Button update;
    public Button start;
    private String userData;
    //private String gender;
    //private Spinner spinner;

    public UserInputFragment() {
    }

    /*
     SETUP VIEW
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        welcomemsg = view.findViewById(R.id.whoareyou);
        userInput = view.findViewById(R.id.user_input);

        /*
        //Spinner in case of choosing gender
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
                onButtonPressed(userData, false);
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
                    mListener.onFragmentInteraction(userData, true);
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
    public void onButtonPressed(String userContent, Boolean story) {
        if (mListener != null) {
            mListener.onFragmentInteraction(userContent, story);
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
        void onFragmentInteraction(String userContent, Boolean story);
    }

}