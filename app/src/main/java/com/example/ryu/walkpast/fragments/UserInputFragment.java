package com.example.ryu.walkpast.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
        //filters for MAC address pattern
        editMetaWear.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editMetaWear.setSingleLine();
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,5}[0-9a-fA-F]")) {
                        if (resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,4}[0-9a-fA-F][0-9a-fA-F]")) {
                            return source.subSequence(start, end) + ":";
                        } else if (!resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,5}[0-9a-fA-F][0-9a-fA-F]")) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        editMetaWear.setFilters(filters);


        //button for name input
        updateName = view.findViewById(R.id.button);
        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUserName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.input_must_be_filled, Toast.LENGTH_LONG).show();
                    return;
                }
                userData = editUserName.getText().toString();
                welcomemsg.setText(R.string.what_is_mac_address);
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
                if (editMetaWear.getText().length() < 17) {
                    Toast.makeText(getActivity(), R.string.not_long_enough, Toast.LENGTH_LONG).show();
                    return;
                }
                onButtonPressed(userData, metaAddr, false);
                metaAddr = editMetaWear.getText().toString();
                editMetaWear.setVisibility(View.GONE);
                updateMetaWear.setVisibility(View.GONE);
                explainMetaWear.setVisibility(View.GONE);
                welcomemsg.setText(getString(R.string.hello) + " " + userData + getString(R.string.instruction));
                start.setVisibility(View.VISIBLE);
            }
        });

        //button for starting game
        start = view.findViewById(R.id.buttonstart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getView() != null) {
                    getView().setVisibility(View.GONE);
                    onButtonPressed(userData, metaAddr, true);
                }
            }
        });

        return view;
    }

    /*
     TELLING LISTENER THAT A BUTTON HAS BEEN PRESSED
     If button story is true the application will know that the Start second button has been pressed and
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