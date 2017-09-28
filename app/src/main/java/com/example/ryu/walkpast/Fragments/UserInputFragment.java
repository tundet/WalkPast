package com.example.ryu.walkpast.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ryu.walkpast.MainActivity;
import com.example.ryu.walkpast.R;

/**
 * Created by RYU on 9/28/2017.
 */

public class UserInputFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText userInput;
    private String userData;
    //private String gender;
    //private Spinner spinner;

    public UserInputFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        userInput = view.findViewById(R.id.user_input);

        /*
        //get the spinner from the xml.
        spinner = view.findViewById(R.id.spinner);
//create a list of items for the spinner.
        String[] items = new String[]{"female", "male", "all"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
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
                // TODO Auto-generated method stub
            }
        });

*/
        Button update = view.findViewById(R.id.button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "User input value must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                userData = userInput.getText().toString();
                onButtonPressed(userData);
            }
        });
        return view;
    }

    public void onButtonPressed(String userContent) {
        if (mListener != null) {
            mListener.onFragmentInteraction(userContent);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String userContent);
    }

}